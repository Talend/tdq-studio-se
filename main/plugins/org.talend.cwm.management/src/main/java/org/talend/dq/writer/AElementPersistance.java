// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.writer;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.User;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.TDQServiceRegister;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.ListUtils;
import org.talend.dq.helper.ModelElementIdentifier;
import org.talend.dq.helper.PropertyHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.analysis.informationvisualization.RenderedObject;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public abstract class AElementPersistance {

    private static Logger log = Logger.getLogger(AElementPersistance.class);

    protected EMFSharedResources util = EMFSharedResources.getInstance();

    /**
     * Persist an element in the specified folder, the file name is created logically by the name of this element.
     * 
     * @param element
     * @param folder
     * @return
     */
    public TypedReturnCode<Object> create(ModelElement element, IFolder folder, boolean... isImportItem) {
        TypedReturnCode<Object> trc = new TypedReturnCode<Object>();

        if (getFileExtension() == null) {
            trc.setMessage("File extension is null."); //$NON-NLS-1$
            log.error(Messages.getString("Get file extension error")); //$NON-NLS-1$
        } else {
            IPath itemPath = folder.getFullPath();
            Property property = initProperty(element);
            Item item = property.getItem();
            try {
                ProxyRepositoryFactory.getInstance().create(item, getPath(element, itemPath), isImportItem);
                trc.setObject(item);
                trc.setOk(Boolean.TRUE);
            } catch (Exception e) {
                trc.setMessage(e.getMessage());
                trc.setOk(Boolean.FALSE);

                log.warn("Create item failed, try to create it by a logical name. ", e);
            }
        }
        return trc;
    }

    /**
     * DOC xqliu Comment method "getPath".
     * 
     * @param element
     * @param itemPath
     * @return
     */
    private IPath getPath(ModelElement element, IPath itemPath) {
        IPath path = new Path(PluginConstant.EMPTY_STRING);
        if (element instanceof DatabaseConnection) { // database connection
            path = itemPath.makeRelativeTo(ResourceManager.getConnectionFolder().getFullPath());
        } else if (element instanceof Analysis) { // analysis
            path = itemPath.makeRelativeTo(ResourceManager.getAnalysisFolder().getFullPath());
        } else if (element instanceof Report) { // report
            path = itemPath.makeRelativeTo(ResourceManager.getReportsFolder().getFullPath());
        } else if (element instanceof IndicatorDefinition) {
            if (element instanceof WhereRule) { // dqrule
                path = itemPath.makeRelativeTo(ResourceManager.getRulesSQLFolder().getFullPath());
            } else if (element instanceof ParserRule) { // parserrule
                path = itemPath.makeRelativeTo(ResourceManager.getRulesParserFolder().getFullPath());
            } else if (element instanceof MatchRuleDefinition) {
                path = itemPath.makeRelativeTo(ResourceManager.getRulesMatcherFolder().getFullPath());
            } else { // indicator definition
                path = itemPath.makeRelativeTo(ResourceManager.getIndicatorFolder().getFullPath());
            }
        } else if (element instanceof Pattern) { // pattern
            path = itemPath.makeRelativeTo(ResourceManager.getPatternFolder().getFullPath());
        }
        return path;
    }

    /**
     * DOC bZhou Comment method "createLogicalFileNmae".
     * 
     * @param element
     * @param extension
     * @return
     */
    public static String createLogicalFileName(ModelElement element, String extension) {
        return DqRepositoryViewService.createTechnicalName(element.getName()) + "_" + MetadataHelper.getVersion(element) //$NON-NLS-1$
                + org.talend.dataquality.PluginConstant.DOT_STRING + extension;
    }

    /**
     * DOC bZhou Comment method "create".
     * 
     * Persist the element into the specified file.
     * 
     * @param element
     * @param file
     * @return
     */
    public ReturnCode create(ModelElement element, IFile file) {
        ReturnCode rc = new ReturnCode();
        if (!check(file)) {
            rc.setReturnCode(Messages.getString("AElementPersistance.FailToSave1"), false); //$NON-NLS-1$
        } else {
            rc = create(element, file.getFullPath(), true);
        }
        return rc;
    }

    /**
     * DOC bZhou Comment method "create".
     * 
     * Persist the element into the specified path.
     * 
     * @param element
     * @param file
     * @param withProperty
     * @return
     */
    public ReturnCode create(ModelElement element, IPath itemPath, boolean withProperty) {
        ReturnCode rc = new ReturnCode();

        if (!util.addEObjectToResourceSet(itemPath.toString(), element)) {
            rc.setReturnCode(Messages.getString("AElementPersistance.FailToSave2", util.getLastErrorMessage()), false); //$NON-NLS-1$
        } else {
            if (element instanceof RenderedObject) {
                ((RenderedObject) element).setFileName(itemPath.toString());
            }

            if (withProperty) {
                createProperty(element);
            }
            rc = save(element, withProperty);

        }

        return rc;
    }

    /**
     * DOC bZhou Comment method "createProperty".
     * 
     * Create and save a property from model element resource.
     * 
     * @param modelElement
     * @return
     */
    public Property createProperty(ModelElement modelElement) {
        Resource eResource = modelElement.eResource();
        if (eResource == null) {
            log.error(Messages.getString("AElementPersistance.FailToCreateProperty")); //$NON-NLS-1$
            return null;
        }

        Property property = initProperty(modelElement);

        User user = ReponsitoryContextBridge.getUser();
        if (user != null) {
            property.setAuthor(user);
        }

        URI propURI = eResource.getURI().trimFileExtension().appendFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

        saveProperty(property, propURI);

        return property;
    }

    /**
     * DOC bZhou Comment method "initProperty".
     * 
     * Initialized a new property.
     * 
     * @param modelElement
     * @return
     */
    public Property initProperty(ModelElement modelElement) {
        Property property = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();

        setPropertyMetadata(modelElement, property);

        property.setCreationDate(new Date());

        Item item = createItem(modelElement);
        property.setItem(item);
        item.setProperty(property);
        return property;
    }

    private void setPropertyMetadata(ModelElement modelElement, Property property) {
        String purpose = MetadataHelper.getPurpose(modelElement);
        String description = MetadataHelper.getDescription(modelElement);
        String version = MetadataHelper.getVersion(modelElement);
        String status = MetadataHelper.getDevStatus(modelElement);

        if (property.getId() == null) {
            property.setId(EcoreUtil.generateUUID());
        }
        // MOD qiongli 2011-1-7 delimitedfile connection dosen't use modelElement.getName().
        if (SwitchHelpers.DELIMITEDFILECONNECTION_SWITCH.doSwitch(modelElement) == null) {
            if (property.getLabel() == null) {
                property.setLabel(WorkspaceUtils.normalize(modelElement.getName()));
            }
            property.setDisplayName(modelElement.getName());
        }
        property.setPurpose(purpose);
        property.setDescription(description);
        property.setStatusCode(status);
        property.setVersion(version);

        List<Information> informations = ListUtils.castList(Information.class, property.getInformations());
        InformationLevel maxLevel = null;
        for (Information information : informations) {
            int value = information.getLevel().getValue();
            if (maxLevel == null || value > maxLevel.getValue()) {
                maxLevel = information.getLevel();
            }
        }
        property.setMaxInformationLevel(maxLevel);
    }

    /**
     * DOC bZhou Comment method "saveProperty".
     * 
     * Save a property.
     * 
     * @param property
     * @return
     */
    public ReturnCode saveProperty(Property property) {
        ReturnCode rc = new ReturnCode();

        Resource propertyResource = property.eResource();
        propertyResource.getContents().add(property.getItem());
        propertyResource.getContents().add(property.getItem().getState());

        rc.setOk(util.saveResource(propertyResource));

        if (!rc.isOk()) {
            rc.setMessage(util.getLastErrorMessage());
        }

        return rc;
    }

    /**
     * DOC bZhou Comment method "saveProperty".
     * 
     * @param property
     * @param uri
     * @return
     */
    public ReturnCode saveProperty(Property property, URI uri) {
        ReturnCode rc = new ReturnCode();

        Resource propResource = util.createResource(uri);
        propResource.getContents().add(property);

        rc = saveProperty(property);

        return rc;
    }

    /**
     * DOC bZhou Comment method "save".
     * 
     * Save a model element and update the related property by default.
     * 
     * @param element
     * @return
     */
    public ReturnCode save(ModelElement element) {
        return save(element, true);
    }

    /**
     * DOC bZhou Comment method "save".
     * 
     * Save a model element and update the related property.
     * 
     * @param element
     * @param withProperty
     * @return
     */
    public ReturnCode save(ModelElement element, boolean withProperty) {
        ReturnCode rc = new ReturnCode();

        addDependencies(element);

        addResourceContent(element);

        rc.setOk(util.saveResource(element.eResource()));

        if (withProperty) {
            updateProperty(element);
        }

        if (rc.isOk()) {
            rc.setMessage("save " + element.getName() + " is OK!"); //$NON-NLS-1$ //$NON-NLS-2$
            if (withProperty) {
                notifyResourceChanges();
            }
        } else {
            rc.setMessage(util.getLastErrorMessage());
        }

        return rc;
    }

    protected abstract void notifyResourceChanges();

    /**
     * DOC bZhou Comment method "updateProperty".
     * 
     * Use model element's attribute to update the related property.
     * 
     * @param element
     */
    public void updateProperty(ModelElement element) {
        Property property = PropertyHelper.getProperty(element);
        if (property != null) {
            setPropertyMetadata(element, property);

            property.setModificationDate(new Date());

            saveProperty(property);
        }
    }

    /**
     * DOC bZhou Comment method "createItem".
     * 
     * @param element
     * @return
     */
    public Item createItem(ModelElement element) {
        Item item = null;
        // MOD mzhao feature 13114, 2010-05-19 distinguish tdq items.
        if (ModelElementIdentifier.isAnalysis(element)) {
            item = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
            ((TDQAnalysisItem) item).setAnalysis((Analysis) element);
        } else if (ModelElementIdentifier.isDQRule(element)) {
            item = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
            ((TDQBusinessRuleItem) item).setDqrule((DQRule) element);
        } else if (ModelElementIdentifier.isDataProvider(element)) {
            if (element instanceof DatabaseConnection) {
                item = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
                ((ConnectionItem) item).setConnection((DatabaseConnection) element);
            }
            ((ConnectionItem) item).setConnection((Connection) element);
        } else if (ModelElementIdentifier.isMatchRule(element)) {
            // this Condition must before the IndicatorDefinition one because MatchRule instance of it.
            item = PropertiesFactory.eINSTANCE.createTDQMatchRuleItem();
            ((TDQMatchRuleItem) item).setMatchRule((MatchRuleDefinition) element);
        } else if (ModelElementIdentifier.isID(element)) {
            item = PropertiesFactory.eINSTANCE.createTDQIndicatorDefinitionItem();
            ((TDQIndicatorDefinitionItem) item).setIndicatorDefinition((IndicatorDefinition) element);
        } else if (ModelElementIdentifier.isPattern(element)) {
            item = PropertiesFactory.eINSTANCE.createTDQPatternItem();
            ((TDQPatternItem) item).setPattern((Pattern) element);
        } else if (ModelElementIdentifier.isReport(element)) {
            item = PropertiesFactory.eINSTANCE.createTDQReportItem();
            ((TDQReportItem) item).setReport((Report) element);
        } else {
            item = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createTDQItem();
        }

        ItemState itemState = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(false);
        item.setState(itemState);
        if (item instanceof TDQItem) {
            setTDQItemFileName(element, item);
        }

        Resource eResource = element.eResource();
        if (eResource != null) {
            URI uri = eResource.getURI();

            if (uri.isPlatform()) {
                IPath elementPath = new Path(uri.toPlatformString(true)).removeLastSegments(1);
                IPath typedPath = ResourceManager.getRootProject().getFullPath().append(PropertyHelper.getItemTypedPath(item));

                IPath statePath = elementPath.makeRelativeTo(typedPath);
                itemState.setPath(statePath.toString());
            }
        }

        return item;
    }

    /**
     * Set the TDQ item file name, this file name will be usefull when commit changes to svn for example.
     * 
     * @param element The element of which the name might have been renamed.
     * @param item
     */
    private void setTDQItemFileName(ModelElement element, Item item) {
        ((TDQItem) item).setFilename(WorkspaceUtils.normalize(element.getName())
                + "_" + MetadataHelper.getVersion(element) + PluginConstant.DOT_STRING //$NON-NLS-1$
                + this.getFileExtension());
    }

    /**
     * DOC bZhou Comment method "check".
     * 
     * @param file
     * @return
     */
    protected boolean check(IFile file) {
        return getFileExtension().equalsIgnoreCase(file.getFileExtension());
    }

    /**
     * DOC bZhou Comment method "addDependencies".
     * 
     * @param element
     */
    protected abstract void addDependencies(ModelElement element);

    /**
     * DOC bZhou Comment method "updateDependencies".
     * 
     * @param element
     */
    // protected abstract void updateDependencies(ModelElement element);

    /**
     * DOC bZhou Comment method "addResourceContent".
     * 
     * @param element
     * @return
     */
    protected void addResourceContent(ModelElement element) {
        Resource eResource = element.eResource();
        if (eResource != null) {
            addResourceContent(eResource, element);
        }
    }

    public void addResourceContent(Resource resource, ModelElement element) {
        if (resource != null) {
            EList<EObject> resourceContents = resource.getContents();

            if (!resourceContents.contains(element)) {
                resourceContents.add(element);
            }

            EList<Dependency> supplierDependency = element.getSupplierDependency();
            if (supplierDependency != null) {
                for (Dependency dependency : supplierDependency) {
                    if (!resourceContents.contains(dependency)) {
                        resourceContents.add(dependency);
                    }
                }
            }
        }
    }

    /**
     * DOC bZhou Comment method "getFileExtension".
     * 
     * @return
     */
    protected abstract String getFileExtension();

    /**
     * Save item and it's dependencies(optional).
     * 
     * @param item
     * @param careDependency Set explicitly <B>true</B> for needs to update dependencies of item.
     * @return
     */
    public abstract ReturnCode save(Item item, boolean careDependency);

    /**
     * Save item with dependencies.
     * 
     * @param element
     * @throws PersistenceException
     */
    protected ReturnCode saveWithDependencies(Item item, ModelElement element) throws PersistenceException {
        ReturnCode rc = new ReturnCode();

        removeDependencies(item);
        addDependencies(element);
        addResourceContent(element.eResource(), element);

        Map<EObject, Collection<Setting>> find = EcoreUtil.ExternalCrossReferencer.find(element.eResource());
        Set<EObject> needSaves = new HashSet<EObject>();
        for (EObject object : find.keySet()) {
            Resource re = object.eResource();
            if (re == null) {
                continue;
            }
            // only do save when the dependency is not reference project node, and do not do resolve
            if (!re.getURI().segment(1).equals(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel())) {
                continue;
            }

            // MOD sizhaoliu TDQ-6296 the resource should be resolved before saving the item to make sure the references
            // are updated.
            EcoreUtil.resolveAll(re);
            needSaves.add(object);
        }

        // Set the TDQ item file name.
        if (item instanceof TDQItem) {
            setTDQItemFileName(element, item);
        }

        ProxyRepositoryFactory.getInstance().save(item);
        AbstractResourceChangesService resChangeService = TDQServiceRegister.getInstance().getResourceChangeService(
                AbstractResourceChangesService.class);
        if (resChangeService != null) {
            for (EObject toSave : needSaves) {
                // only do save when the dependency is not reference project node
                toSave = EObjectHelper.resolveObject(toSave);
                if (!toSave.eResource().getURI().segment(1)
                        .equals(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel())) {
                    continue;
                }

                resChangeService.saveResourceByEMFShared(toSave.eResource());
            }
        }

        return rc;
    }

    /**
     * Save item <B>without</B> dependencies.
     * 
     * @param element
     * @throws PersistenceException
     */
    protected ReturnCode saveWithoutDependencies(Item item, ModelElement element) throws PersistenceException {
        ReturnCode rc = new ReturnCode();

        // Set the TDQ item file name.
        if (item instanceof TDQItem) {
            setTDQItemFileName(element, item);
        }
        ProxyRepositoryFactory.getInstance().save(item);

        return rc;
    }

    protected abstract ReturnCode removeDependencies(Item item);
}
