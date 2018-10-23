// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.dependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dataquality.properties.impl.TDQAnalysisItemImpl;
import org.talend.dataquality.properties.impl.TDQIndicatorDefinitionItemImpl;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.FileUtils;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * A singleton class to handle dependencies between objects.
 * 
 * PTODO scorreia clean code of this class.
 */
public final class DependenciesHandler {

    /**
     * As specified in CWM document at p. 67, the dependency kind can be of two types "Usage" or "Abstraction", but can
     * also be of other types.
     */
    public static final String USAGE = "Usage"; //$NON-NLS-1$

    private static DependenciesHandler instance;

    private static Logger log = Logger.getLogger(DependenciesHandler.class);

    private DependenciesHandler() {
        // this.dependencyResource = loadFromFile(PATH_NAME);
    }

    public static DependenciesHandler getInstance() {
        if (instance == null) {
            instance = new DependenciesHandler();
        }
        return instance;
    }

    /**
     * Method "clearDependencies" is to be used before a file is deleted. The root element is given as argument and the
     * dependencies on this element are removed in all resources that depend on this element.
     * 
     * @param elementToDelete a root element which file will be deleted.
     * @return the list of modified dependencies
     */
    public List<Resource> clearDependencies(ModelElement elementToDelete) {
        EList<Dependency> clientDependencies;
        // get the client dependencies (
        clientDependencies = elementToDelete.getClientDependency();
        // locate resource of each Dependency object
        List<Resource> modifiedResources = new ArrayList<Resource>();
        Iterator<Dependency> it = clientDependencies.iterator();
        while (it.hasNext()) {
            Dependency dependency = it.next();
            // MOD qiongli bug 15587.if dependcy is Proxy,reload it and remove the client element
            if (dependency.eIsProxy()) {
                dependency = (Dependency) EObjectHelper.resolveObject(dependency);

            }// ~
            Resource dependencyResource = dependency.eResource();
            if (dependencyResource != null) {
                modifiedResources.add(dependencyResource);

            }
        }
        // this clears also the reverse dependencies: remove the elementToDelete
        // from
        // Dependency.getClient()
        clientDependencies.clear();

        // now get the objects that depend on the elementToDelete
        EList<Dependency> supplierDependencies = elementToDelete.getSupplierDependency();
        for (Dependency dependency : supplierDependencies) {
            EList<ModelElement> client = dependency.getClient();
            // get the resource of each client
            for (ModelElement modelElement : client) {
                Resource clientResource = modelElement.eResource();
                if (clientResource != null) {
                    modifiedResources.add(clientResource);
                }
            }
            // clear the dependencies of all clients
            // this clear the corresponding getClientDependency() of each client
            // (objects that requires the
            // elementToDelete)
            client.clear();
        }
        // MOD zshen :softwareSystem don't belong to dependency but need to remove together.
        // MOD sizhaoliu TDQ-6316
        // if (elementToDelete instanceof Connection) {
        // SoftwareSystemManager.getInstance().cleanSoftWareSystem((Connection) elementToDelete);
        // }
        return modifiedResources;
    }

    /**
     * Method "removeDependenciesBetweenModels" is to be used before a model dependency(elementToRemove) removed from
     * the elementFromRemove. The elementToRemove not truly to deleted, not a file to deleted, just remove the
     * dependency between elementFromRemove and elementToRemove,
     * 
     * @param elementFromRemove
     * @param elementToRemove
     * @return
     */
    public List<Resource> removeDependenciesBetweenModels(ModelElement elementFromRemove,
            List<? extends ModelElement> elementToRemove) {
        EList<Dependency> clientDependencies;
        List<Resource> toRemoveResources = new ArrayList<Resource>();
        for (ModelElement modelElement : elementToRemove) {
            toRemoveResources.add(modelElement.eResource());
        }
        // get the client dependencies (
        clientDependencies = elementFromRemove.getClientDependency();
        // locate resource of each Dependency object
        List<Resource> modifiedResources = new ArrayList<Resource>();
        if (clientDependencies != null) {
            Iterator<Dependency> dependencyIterator = clientDependencies.iterator();
            while (dependencyIterator.hasNext()) {
                Dependency dependency = dependencyIterator.next();
                Resource dependencyResource = dependency.eResource();
                if (!toRemoveResources.contains(dependencyResource)) {
                    continue;
                }
                if (dependencyResource != null) {
                    modifiedResources.add(dependencyResource);
                    dependencyIterator.remove();
                }
            }
        }
        return modifiedResources;
    }

    /**
     * 
     * This method removes supplier dependencies. See
     * {@link DependenciesHandler#removeDependenciesBetweenModels(ModelElement, List)}
     * 
     * @param rule
     * @param analyses
     * @return
     */
    public boolean removeSupplierDependenciesBetweenModels(ModelElement rule, List<? extends ModelElement> analyses) {

        List<Resource> toRemoveResources = new ArrayList<Resource>();
        for (ModelElement modelElement : analyses) {
            toRemoveResources.add(modelElement.eResource());
        }
        // get the client dependencies (
        EList<Dependency> supplierDependencies = rule.getSupplierDependency();

        for (Dependency dependency : supplierDependencies) {
            EList<ModelElement> client = dependency.getClient();
            // get the resource of each client
            Iterator<ModelElement> dependencyIterator = client.iterator();
            while (dependencyIterator.hasNext()) {
                Resource clientResource = dependencyIterator.next().eResource();
                if (clientResource != null) {
                    if (toRemoveResources.contains(clientResource)) {
                        dependencyIterator.remove();
                    }
                }
            }
        }

        return true;
    }

    /**
     * Method "createUsageDependencyOn".
     * 
     * Example Analysis depends on the exitence of a DataProvider. This method must be called
     * createUsageDependencyOn(Analysis, DataProvider). The created dependency is added to the DataProvider in its
     * "client dependencies" and to the Analysis in its "supplier dependencies". See OMG CWM spec paragraph 4.3.2.7.
     * 
     * The resource in which the dependency is stored is the supplier's resource.
     * 
     * @param kind the kind of dependency
     * @param clientElement the element that requires the requiredElement
     * @param supplierElement the required element
     * @return the Dependency of clientElement on requiredElement
     */
    Dependency createDependencyOn(String kind, ModelElement clientElement, ModelElement supplierElement) {
        Dependency dependency = ModelElementHelper.createDependencyOn(kind, clientElement, supplierElement);
        Resource supplierResource = supplierElement.eResource();
        if (supplierResource != null) {
            supplierResource.getContents().add(dependency);
        }
        return dependency;
    }

    /**
     * Method "createUsageDependencyOn".
     * 
     * @param clientElement the analysis that depends on the data provider.
     * @param dataManager the data provider
     * @return a true return code if the dependency has been correctly added to the resource of the supplier element.
     * Return false otherwise. In any case, the dependency is created and the getObject() method returns it.
     */
    TypedReturnCode<Dependency> createUsageDependencyOn(ModelElement clientElement, ModelElement dataManager) {
        assert dataManager != null;
        Dependency dependency = createDependencyOn(USAGE, clientElement, dataManager);
        TypedReturnCode<Dependency> rc = new TypedReturnCode<Dependency>();
        rc.setObject(dependency);
        return rc;
    }

    /**
     * Method "setDependencyOn" sets the dependency between the analysis and the data manager.
     * 
     * @param analysis the analysis which requires the data manager
     * @param dataManager the data manager required by the analysis
     * @return a true return code if the dependency has been correctly added to the resource of the supplier element.
     * Return false otherwise. The dependency is created only if needed and the getObject() method returns it.
     */
    public TypedReturnCode<Dependency> setDependencyOn(Analysis analysis, DataManager dataManager) {
        return setUsageDependencyOn(analysis, dataManager);
    }

    /**
     * Method "setUsageDependencyOn".
     * 
     * @param client the element which depends on the supplier
     * @param supplier the element needed by the client element
     * @return the dependency object between the two given elements
     */
    public TypedReturnCode<Dependency> setUsageDependencyOn(ModelElement client, ModelElement supplier) {
        // get the supplier's usage dependencies
        EList<Dependency> supplierDependencies = supplier.getSupplierDependency();

        // search for clients into them
        for (Dependency dependency : supplierDependencies) {
            if (dependency.getKind() != null && USAGE.compareTo(dependency.getKind()) == 0) {
                // if exist return true with the dependency
                EObject resolvedObject = ResourceHelper.resolveObject(dependency.getClient(), client);
                // check whether the client contains relation dependency, if yes remove the dependency firstly
                if (resolvedObject == null) {
                    if (checkClientDependencyExist(client, supplier)) {
                        // normal case the code should not come here, except import same project with the second time
                        removeClientDependency(client, supplier);
                    }
                    // if not add analysis to the dependency
                    dependency.getClient().add(client);
                }

                TypedReturnCode<Dependency> rc = new TypedReturnCode<Dependency>();
                rc.setObject(dependency);
                return rc;
            }
        }
        // if not exist create a usage dependency
        return createUsageDependencyOn(client, supplier);
    }

    /**
     * DOC zshen Comment method "removeClientDependency".
     * 
     * @param client
     * @param supplier
     */
    public void removeClientDependency(ModelElement client, ModelElement supplier) {
        for (Dependency dependency : client.getClientDependency()) {
            if (dependency.getKind() != null && USAGE.compareTo(dependency.getKind()) == 0) {
                EObject resolvedObject = ResourceHelper.resolveObject(dependency.getSupplier(), supplier);
                if (resolvedObject != null) {
                    client.getClientDependency().remove(dependency);
                    return;
                }
            }
        }

    }

    /**
     * DOC zshen Comment method "checkClientDependency".
     * 
     * @param client
     * @param supplier
     * @return
     */
    public boolean checkClientDependencyExist(ModelElement client, ModelElement supplier) {
        for (Dependency dependency : client.getClientDependency()) {
            if (dependency.getKind() != null && USAGE.compareTo(dependency.getKind()) == 0) {
                EObject resolvedObject = ResourceHelper.resolveObject(dependency.getSupplier(), supplier);
                if (resolvedObject != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method "setDependencyOn" sets the dependency between the report and the analysis.
     * 
     * @param report
     * @param analysis
     * @return a true return code if the dependency has been correctly added to the resource of the supplier element.
     * Return false otherwise. The dependency is created only if needed and the getObject() method returns it.
     */
    public TypedReturnCode<Dependency> setDependencyOn(TdReport report, Analysis analysis) {
        return setUsageDependencyOn(report, analysis);
    }

    public TypedReturnCode<Dependency> setDependencyOn(Indicator indicator, Pattern pattern) {
        return setUsageDependencyOn(indicator, pattern);
    }

    public TypedReturnCode<Dependency> setDependencyOn(Analysis analysis, Pattern pattern) {
        return setUsageDependencyOn(analysis, pattern);
    }

    public TypedReturnCode<Dependency> setDependencyOn(Analysis analysis, IndicatorDefinition indicatorDefinition) {
        return setUsageDependencyOn(analysis, indicatorDefinition);
    }

    /**
     * 
     * @param object
     * @return SupplierDependency
     * 
     * getSupplierDependency
     */
    public List<IRepositoryViewObject> getSupplierDependency(IRepositoryViewObject object) {
        List<IRepositoryViewObject> listViewObject = new ArrayList<IRepositoryViewObject>();
        ModelElement modelElement = PropertyHelper.getModelElement(object.getProperty());
        if (object.getProperty().getItem() instanceof TDQFileItem) {
            return listViewObject;
        }
        if (modelElement instanceof IndicatorDefinition) {
            listViewObject.addAll(getIndicatorDependency(object));
        } else {
            EList<Dependency> supplierDependency = modelElement.getSupplierDependency();
            for (Dependency supplier : supplierDependency) {
                for (ModelElement depencyModelElement : supplier.getClient()) {
                    if (depencyModelElement.eIsProxy()) {
                        // the depency ModelElement is proxy means it is not exist in current project, so need not to do
                        // anyting, just skip it
                    } else {
                        Property property = PropertyHelper.getProperty(depencyModelElement);
                        IRepositoryViewObject repositoryViewObject = new RepositoryViewObject(property);
                        listViewObject.add(repositoryViewObject);
                    }
                }
            }
        }

        return listViewObject;
    }

    /**
     * 
     * @param property
     * @return SupplierDependency
     * 
     * getClintDependency
     */
    public List<Property> getClintDependency(Property property) {
        List<Property> listProperty = new ArrayList<Property>();
        if (property == null) {
            return listProperty;
        }
        ModelElement modelElement = PropertyHelper.getModelElement(property);
        // MOD qiongli 2012-5-15 TDQ-5259,avoid NPE.
        if (modelElement == null) {
            return listProperty;
        }
        if (property.getItem() instanceof TDQSourceFileItem) {
            return listProperty;
        }
        EList<Dependency> clientDependency = modelElement.getClientDependency();
        for (Dependency clienter : clientDependency) {
            for (ModelElement depencyModelElement : clienter.getSupplier()) {
                Property dependencyProperty = PropertyHelper.getProperty(depencyModelElement);
                // IRepositoryViewObject repositoryViewObject = new RepositoryViewObject(property);
                if (dependencyProperty != null) {
                    listProperty.add(dependencyProperty);
                }
            }
        }

        return listProperty;
    }

    /**
     * get Indicator Dependency.
     * 
     * @return get the list for analysis which use parameter to be a Indicator
     */
    public List<IRepositoryViewObject> getIndicatorDependency(IRepositoryViewObject viewObject) {
        Item item = viewObject.getProperty().getItem();
        List<IRepositoryViewObject> listAnalysisViewObject = new ArrayList<IRepositoryViewObject>();
        if (item instanceof TDQIndicatorDefinitionItemImpl) {
            TDQIndicatorDefinitionItemImpl tdqIndicatorItem = (TDQIndicatorDefinitionItemImpl) item;
            IndicatorDefinition newIndicatorDefinition = tdqIndicatorItem.getIndicatorDefinition();
            List<IRepositoryViewObject> allAnaList = new ArrayList<IRepositoryViewObject>();
            try {
                allAnaList.addAll(ProxyRepositoryFactory.getInstance().getAll(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT, true));
            } catch (PersistenceException e) {
                log.error(e, e);
            }
            for (IRepositoryViewObject theAna : allAnaList) {
                List<Indicator> indicators = IndicatorHelper.getIndicators(((TDQAnalysisItem) theAna.getProperty().getItem())
                        .getAnalysis().getResults());
                for (Indicator indicator : indicators) {
                    IndicatorDefinition oldIndicatorDefinition = indicator.getIndicatorDefinition();
                    if (ModelElementHelper.compareUUID(oldIndicatorDefinition, newIndicatorDefinition)) {
                        listAnalysisViewObject.add(theAna);
                        break;
                    }
                }
            }
        }
        return listAnalysisViewObject;
    }

    /**
     * get Analysis Dependency (for indicator only).
     * 
     * @return get the list of indicator which in use by the analysis
     * 
     */
    public List<Property> getAnaDependency(Property property) {
        Item item = property.getItem();
        List<Property> listProperty = new ArrayList<Property>();
        if (item instanceof TDQAnalysisItemImpl) {
            TDQAnalysisItemImpl tdqAnaItem = (TDQAnalysisItemImpl) item;
            Analysis anaElement = tdqAnaItem.getAnalysis();
            List<Indicator> indicators = IndicatorHelper.getIndicators(anaElement.getResults());
            for (Indicator indicator : indicators) {
                boolean isContain = false;
                IndicatorDefinition newIndicatorDefinition = indicator.getIndicatorDefinition();
                // MOD qiongli 2012-5-11 TDQ-5256
                if (newIndicatorDefinition == null) {
                    continue;
                }
                for (Property containViewObject : listProperty) {
                    Item item2 = containViewObject.getItem();
                    if (item2 instanceof TDQIndicatorDefinitionItemImpl) {
                        IndicatorDefinition oldIndicatorDefinition = ((TDQIndicatorDefinitionItemImpl) item2)
                                .getIndicatorDefinition();
                        if (ModelElementHelper.compareUUID(oldIndicatorDefinition, newIndicatorDefinition)) {
                            isContain = true;
                            break;
                        }
                    }
                }
                if (!isContain) {
                    Property iniProperty = PropertyHelper.getProperty(indicator.getIndicatorDefinition());
                    if (iniProperty != null) {
                        listProperty.add(iniProperty);
                    }
                }
            }
        }
        return listProperty;
    }

    /**
     * get Client Dependency List.
     * 
     * @param Analysis the analysis which we want to save
     * @return The list all of client dependency(Pattern UDI Connection DQRule)
     */
    public List<ModelElement> getClientDepListByResult(Analysis analysis) {
        List<ModelElement> clientDependencyList = new ArrayList<ModelElement>();
        DataManager connection = analysis.getContext().getConnection();
        // when connection is null mean that no any result can be keep so don't need check result again return empty
        // list
        if (connection != null) {
            // DQRule or UDI case
            clientDependencyList.addAll(AnalysisHelper.getUserDefinedIndicators(analysis));
            // pattern case
            clientDependencyList.addAll(AnalysisHelper.getPatterns(analysis));
            clientDependencyList.add(connection);
        }
        return clientDependencyList;
    }

    /**
     * 
     * Comment method "removeDependenciesBetweenModel".
     * 
     * @param supplier
     * @param client
     * @return
     */
    public boolean removeDependenciesBetweenModel(ModelElement supplier, ModelElement client) {
        // get the supplier's usage dependencies
        boolean hasFind = false;
        EList<Dependency> supplierDependencies = supplier.getSupplierDependency();
        Iterator<Dependency> suppiterator = supplierDependencies.iterator();
        while (suppiterator.hasNext()) {
            Dependency supplierDep = suppiterator.next();
            if (supplierDep.getClient().contains(client)) {
                supplierDep.getClient().remove(client);
                hasFind = true;
                break;
            }
        }
        // clint and supplier ues a same dependency so only need to remove once
        // there can do another logic to revert supplier and client then we use this method will not consider the order
        // for the argument
        if (!hasFind) {
            EList<Dependency> clientDependency = supplier.getClientDependency();
            Iterator<Dependency> clientiterator = clientDependency.iterator();
            while (clientiterator.hasNext()) {
                Dependency clientDep = clientiterator.next();
                if (clientDep.getSupplier().contains(client)) {
                    clientiterator.remove();
                    hasFind = true;
                    break;
                }
            }
        }
        return hasFind;

    }

    /**
     * 
     * delete the dependency between analysis and connection,then save the connection and analysis.
     * 
     * @param analysis
     * @return whether it has been deleted
     * 
     */
    public boolean removeConnDependencyAndSave(TDQAnalysisItem analysisItem) {
        Analysis analysis = analysisItem.getAnalysis();
        Connection oldDataProvider = (Connection) analysis.getContext().getConnection();
        ReturnCode rect = new TypedReturnCode<Object>(Boolean.TRUE);
        // Remove old dependencies.
        if (oldDataProvider != null) {
            List<ModelElement> tempList = new ArrayList<ModelElement>();
            tempList.add(oldDataProvider);
            DependenciesHandler.getInstance().removeDependenciesBetweenModels(analysis, tempList);
            Property property = PropertyHelper.getProperty(oldDataProvider);
            if (property != null) {
                rect = ElementWriterFactory.getInstance().createDataProviderWriter().save(property.getItem(), false);
            } else {
                rect.setOk(Boolean.FALSE);
            }
            if (!rect.isOk()) {
                rect.setMessage(Messages.getString("DependenciesHandler.removeDependFailed")); //$NON-NLS-1$
                log.error(rect.getMessage());
            } else {
                analysis.getContext().setConnection(null);
                analysis.getClientDependency().clear();
                rect = ElementWriterFactory.getInstance().createAnalysisWrite().save(analysisItem, false);
            }
        } else {
            log.warn(Messages.getString("DependenciesHandler.removeDependFailByNull", "oldDataProvider")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return rect.isOk();
    }

    public static ReturnCode removeUselessDependencies(Item localItem, Item remoteItem) {

        ReturnCode rc = new ReturnCode();
        ModelElement remoteModelElement = PropertyHelper.getModelElement(remoteItem.getProperty());
        ModelElement localModelElement = PropertyHelper.getModelElement(localItem.getProperty());

        for (Dependency remoteClientDependency : remoteModelElement.getClientDependency()) { // rule2, remote-ana
            List<Dependency> localClients = localModelElement.getClientDependency();// rule1 list
            Iterator<Dependency> localClientDependency = localClients.iterator();
            boolean isContains = false;
            while (localClientDependency.hasNext()) {
                // rule2,rule1
                Dependency next = localClientDependency.next();

                URI uri = EObjectHelper.getURI(remoteClientDependency);
                String fileName = uri.lastSegment();
                URI nexturi = EObjectHelper.getURI(next);
                String nextfileName = nexturi.lastSegment();
                if (StringUtils.equals(fileName, nextfileName)) {
                    isContains = true;
                    break;
                }

            }
            if (isContains == false) {
                findClientAndRemoveDependencyInLocalProject(remoteClientDependency, localModelElement);
            }

        }
        return rc;
    }

    private static void findClientAndRemoveDependencyInLocalProject(ModelElement modelElement, ModelElement analysis) {
        URI uri = EObjectHelper.getURI(modelElement).trimFileExtension().appendFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

        // get file from filename
        String fileName = uri.lastSegment();
        File foundFile = null;
        EResourceConstant[] topConstants = EResourceConstant.getTopConstants();
        for (EResourceConstant resource : topConstants) {
            File reportFolder = ResourceManager.getRootProject().getLocation().append(resource.getPath()).toFile();
            List<File> fileList = FileUtils.getFilesByExtension(reportFolder, FactoriesUtil.PROPERTIES_EXTENSION);
            for (File file : fileList) {
                if (file.getName().equals(fileName)) {
                    foundFile = file;
                    break;
                }
            }
            if (foundFile != null) {
                break;
            }
        }

        // from file to get model and change it
        if (foundFile != null) {
            Property property = PropertyHelper.getProperty(foundFile);

            // change the cache content also
            Item item = property.getItem();
            Resource itemResource = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager()
                    .getItemResource(item);

            List<EObject> toRemoveObjList = new ArrayList<EObject>();
            for (EObject obj : itemResource.getContents()) {
                if (obj != null && obj instanceof Dependency) {
                    EList<ModelElement> clients = ((Dependency) obj).getClient();
                    for (ModelElement model : clients) {
                        if (model.getName() == null) {
                            toRemoveObjList.add(obj);
                        } else {
                            if (StringUtils.equals(model.getName(), analysis.getName())) {
                                toRemoveObjList.add(obj);
                                break;
                            }
                        }
                    }

                    EList<ModelElement> supplier = ((Dependency) obj).getSupplier();
                    for (ModelElement model : supplier) {
                        if (model.getName() == null) {
                            toRemoveObjList.add(obj);
                        } else {
                            if (StringUtils.equals(model.getName(), item.getProperty().getLabel())) {
                                toRemoveObjList.add(obj);
                                break;
                            }
                        }
                    }
                } else {
                    // modify where rule
                    EList<Dependency> supplierDependency = ((ModelElement) obj).getSupplierDependency();
                    Iterator<Dependency> iterator = supplierDependency.iterator();
                    while (iterator.hasNext()) {
                        Dependency model = iterator.next();
                        EList<ModelElement> clients = model.getClient();
                        for (ModelElement client : clients) {
                            if (StringUtils.equals(client.getName(), analysis.getName())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }
            }
            for (EObject removeObj : toRemoveObjList) {
                if (removeObj != null) {
                    itemResource.getContents().remove(removeObj);
                }
            }

            // save the node
            AElementPersistance writer = ElementWriterFactory.getInstance().create(item);
            writer.save(item, true);
        }

    }

}
