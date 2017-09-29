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
package org.talend.dq.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.helpers.ReportHelper.ReportType;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author rli
 * 
 */
public final class EObjectHelper {

    private EObjectHelper() {

    }

    public static TdColumn[] getColumns(ColumnSet columnSet) {
        List<TdColumn> columns = ColumnSetHelper.getColumns(columnSet);
        return columns.toArray(new TdColumn[columns.size()]);
    }

    public static Package getParent(ColumnSet columnSet) {
        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(columnSet.eContainer());
        if (catalog != null) {
            return catalog;
        } else {
            Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(columnSet.eContainer());
            return schema;
        }
    }

    /**
     * 
     * @deprecated it's probably better to use {@link ConnectionHelper#getTdDataProvider(TdColumn)}
     */
    @Deprecated
    public static Connection getTdDataProvider(TdColumn column) {
        ColumnSet columnSetOwner = ColumnHelper.getColumnSetOwner(column);
        Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(columnSetOwner);
        return ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);

    }

    public static Connection getTdDataProvider(TdTable table) {
        Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(table);
        return ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);

    }

    public static void removeDependencys(IResource... resources) {
        for (IResource selectedObj : resources) {

            IFile file = ((IFile) selectedObj);
            // String fileName = file.getName();
            if (file.getFileExtension() == null) {
                continue;
            }
            ModelElement elementToDelete = getModelElement(file);
            if (elementToDelete != null) {
                List<Resource> modifiedResources = DependenciesHandler.getInstance().clearDependencies(elementToDelete);

                // save now modified resources (that contain the Dependency objects)
                for (Resource resource : modifiedResources) {
                    EMFUtil.saveSingleResource(resource);
                }

            }
        }
    }

    /**
     * 
     * DOC qiongli Comment method "removeDependencys".
     * 
     * @param elementToDelete
     */
    public static void removeDependencys(ModelElement elementToDelete) {
        if (elementToDelete != null) {
            List<Resource> modifiedResources = DependenciesHandler.getInstance().clearDependencies(elementToDelete);

            // save now modified resources (that contain the Dependency objects)
            for (Resource resource : modifiedResources) {
                EMFUtil.saveSingleResource(resource);
                // ElementWriterFactory.getInstance().createDataProviderWriter().save(modify);
            }

        }

    }

    private static ModelElement getModelElement(IFile file) {
        ResourceFileMap resourceFileMap = ModelElementFileFactory.getResourceFileMap(file);
        return resourceFileMap != null ? resourceFileMap.getModelElement(file) : null;
    }

    private static ModelElement getModelElement(IRepositoryViewObject repositoryObject) {
        ModelElement modelElement = null;
        Item theItem = repositoryObject.getProperty().getItem();
        if (theItem instanceof ConnectionItem) {
            modelElement = ((ConnectionItem) theItem).getConnection();
        }
        return modelElement;
        // ResourceFileMap resourceFileMap = ModelElementFileFactory.getResourceFileMap(file);
        // return resourceFileMap != null ? resourceFileMap.getModelElement(file) : null;
    }

    public static List<ModelElement> getDependencySuppliers(IFile file) {
        ModelElement findElement = getModelElement(file);
        EList<Dependency> clientDependencys = findElement.getClientDependency();
        // locate resource of each Dependency object
        List<ModelElement> supplierList = new ArrayList<ModelElement>();
        for (Dependency dependency : clientDependencys) {
            EList<ModelElement> supplier = dependency.getSupplier();
            if (supplier != null) {
                supplierList.addAll(supplier);
            }
        }
        return supplierList;
    }

    public static List<ModelElement> getDependencyClients(IFile file) {
        ModelElement findElement = getModelElement(file);
        return getDependencyClients(findElement);
    }

    public static List<ModelElement> getDependencyClients(IRepositoryViewObject repositoryObject) {
        ModelElement findElement = getModelElement(repositoryObject);
        return getDependencyClients(findElement);
    }

    /**
     * 
     * DOC qiongli Comment method "getDependencyClients".
     * 
     * @param respositoryNode
     * @return
     */
    public static List<ModelElement> getDependencyClients(IRepositoryNode respositoryNode) {
        // Added 20130305 check the jRxml and its folder here, because the jrxml are not modelelement type.
        if (ERepositoryObjectType.TDQ_JRAXML_ELEMENT.equals(respositoryNode.getObjectType())) {
            return getDependedReportOfJrxml(respositoryNode);
        } else if (ERepositoryObjectType.METADATA_CON_TABLE.equals(respositoryNode.getObjectType())
                || ERepositoryObjectType.METADATA_CON_VIEW.equals(respositoryNode.getObjectType())) {
            return EObjectHelper.getFirstDependency(respositoryNode);
        }
        ModelElement findElement = RepositoryNodeHelper.getModelElementFromRepositoryNode(respositoryNode);
        return getDependencyClients(findElement);
    }

    /**
     * Go throught all reports in the project and return all which used the current jrxml.
     * 
     * @param node the Jrxml node
     * @return list of reports who used this jrxml as user defined template
     */
    public static List<ModelElement> getDependedReportOfJrxml(IRepositoryNode node) {
        List<ModelElement> dependedReport = new ArrayList<ModelElement>();
        if (node.getObject().getProperty() == null) {
            return dependedReport;
        }
        IPath path = PropertyHelper.getItemPath(node.getObject().getProperty());
        // check if it has depended Report
        // get all reports
        List<ReportRepNode> repNodes = RepositoryNodeHelper.getReportRepNodes(
                RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.REPORTS), true, true);
        // go through every report to find if any one used current jrxml
        for (ReportRepNode report : repNodes) {
            EList<AnalysisMap> analysisMap = ((TdReport) report.getReport()).getAnalysisMap();
            for (AnalysisMap anaMap : analysisMap) {
                if (isUsedByDeletedJrxml(path, anaMap)) {
                    dependedReport.add(report.getReport());
                    break;
                }
            }
        }
        return dependedReport;
    }

    /**
     * check if the anaMap comtains the Jrxml or not, by compare the jrxml's path with anaMap's jrxml source(when user
     * mode)
     * 
     * @param path the path of the jrxml saved in the analysis map
     * @param anaMap the analysis map in the report.
     * @return the analysis map used the current jrxml or not.
     */
    private static boolean isUsedByDeletedJrxml(IPath path, AnalysisMap anaMap) {
        ReportType reportType = ReportHelper.ReportType.getReportType(anaMap.getAnalysis(), anaMap.getReportType());
        // compare the Jrxml path if the report has the user defined one.
        if (ReportHelper.ReportType.USER_MADE.equals(reportType)) {
            String jrxmlPath = anaMap.getJrxmlSource();
            String deletedpath = path.removeFirstSegments(2).toString();
            if (jrxmlPath.contains(deletedpath)) {
                return true;
            }
        }
        return false;
    }

    public static List<ModelElement> getDependencyClients(ModelElement modelElement) {
        if (modelElement == null) {
            return new ArrayList<ModelElement>();
        }
        EList<Dependency> clientDependencys = modelElement.getSupplierDependency();
        // locate resource of each Dependency object
        List<ModelElement> supplierList = new ArrayList<ModelElement>();
        for (Dependency dependency : clientDependencys) {
            EList<ModelElement> clients = dependency.getClient();
            if (clients != null) {
                for (ModelElement client : clients) {
                    if (!client.eIsProxy()) {
                        supplierList.add(client);
                    }
                }
            }
        }
        return supplierList;
    }

    /**
     * 
     * DOC qiongli Comment method "hasDependences".
     * 
     * @param node
     * @return
     */
    public static boolean hasDependencyClients(IRepositoryNode node) {
        List<ModelElement> dependencies = getDependencyClients(node);
        if (dependencies == null || dependencies.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * get the first Dependency of node(only used for METADATA_CON_TABLE and METADATA_CON_VIEW).
     * 
     * @param node
     * @return
     */
    public static List<ModelElement> getFirstDependency(IRepositoryNode node) {
        List<ModelElement> result = new ArrayList<ModelElement>();
        if (node == null) {
            return result;
        }

        // use the connection supplier to get analyses, and then check them when they use the droped table

        // get the connection
        ModelElement deleteModel = RepositoryNodeHelper.getMetadataElement(node);
        Connection connection = ModelElementHelper.getConnection(deleteModel);
        if (connection == null) {
            return result;
        }

        // get the supplier Dependency of the connection
        EList<Dependency> supplierDependency = connection.getSupplierDependency();
        if (supplierDependency != null && supplierDependency.size() > 0) {
            EList<ModelElement> clients = supplierDependency.get(0).getClient();
            for (ModelElement client : clients) {
                if (!(client instanceof Analysis)) {
                    continue;
                }
                Analysis analysis = (Analysis) client;
                if (analysis.eIsProxy()) {
                    analysis = (Analysis) EObjectHelper.resolveObject(analysis);
                }
                AnalysisContext context = analysis.getContext();
                EList<ModelElement> analysedElements = context.getAnalysedElements();
                if (analysedElements != null && analysedElements.size() > 0) {
                    ModelElement analysisModel = null;
                    ModelElement firstAnalysisElement = analysedElements.get(0);
                    if (firstAnalysisElement instanceof MetadataColumn) {
                        analysisModel = ModelElementHelper.getContainer(firstAnalysisElement);
                    } else if (firstAnalysisElement instanceof MetadataTable) {
                        analysisModel = firstAnalysisElement;
                    }
                    if (compareModelElement(deleteModel, analysisModel)) {
                        result.add(analysis);
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * compare two ModelElements.
     * 
     * @param model1
     * @param model2
     * @return boolean true: when they have the same name and uuid.
     */
    private static boolean compareModelElement(ModelElement model1, ModelElement model2) {
        if (model1 == null && model2 == null) {
            return true;
        }

        if (model1 != null && model2 != null) {
            if (model1.getName().equals(model2.getName())) {
                if (model2.eIsProxy()) {
                    model2 = (ModelElement) EObjectHelper.resolveObject(model2);
                }
                if (model1.eIsProxy()) {
                    model1 = (ModelElement) EObjectHelper.resolveObject(model1);
                }
                if (ModelElementHelper.compareUUID(model1, model2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * add Dependencies For File.
     * 
     * @param file
     * @param modelElements
     */
    public static void addDependenciesForFile(IFile file, List<ModelElement> modelElements) {
        ModelElement findElement = getModelElement(file);
        for (int i = 0; i < modelElements.size(); i++) {
            DependenciesHandler.getInstance().setUsageDependencyOn(findElement, modelElements.get(i));
        }
    }

    /**
     * DOC bZhou Comment method "retrieveEObject".
     * 
     * @param filePath
     * @param classfier
     * @return
     */
    public static Object retrieveEObject(IPath filePath, EClass classfier) {
        if (isEObjectPathExited(filePath)) {

            URI uri;
            if (filePath.isAbsolute()) {
                uri = URI.createFileURI(filePath.toOSString());
            } else {
                uri = URI.createPlatformResourceURI(filePath.toOSString(), false);
            }

            Resource res = new ResourceSetImpl().getResource(uri, true);
            return EcoreUtil.getObjectByType(res.getContents(), classfier);
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "isEObjectPathExited".
     * 
     * @param objectPath
     * @return
     */
    public static boolean isEObjectPathExited(IPath objectPath) {
        if (objectPath.isAbsolute()) {
            return objectPath.toFile().exists();
        } else {
            return ResourceManager.getRoot().getFile(objectPath).exists();
        }
    }

    /**
     * 
     * DOC qiongli Comment method "resolveObject".
     * 
     * @param proxy
     * @return
     */
    public static EObject resolveObject(EObject proxy) {
        if (proxy != null && proxy.eIsProxy()) {
            // if it is remote project,xmiRes will be null
            XmiResourceManager xmiRes = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
                    .getResourceManager();
            if (xmiRes != null) {
                ResourceSet resourceSet = xmiRes.resourceSet;
                // FIXME the input proxy object should not be changed in this method.
                proxy = EcoreUtil.resolve(proxy, resourceSet);
            }
        }
        return proxy;
    }

    /**
     * DOC bZhou Comment method "getURI".
     * 
     * Get uri for an object.
     * 
     * @param object
     * @return
     */
    public static URI getURI(EObject object) {
        URI uri = null;
        if (object != null) {
            if (object.eIsProxy()) {
                uri = ((InternalEObject) object).eProxyURI();
            } else {
                uri = object.eResource().getURI();
            }
        }

        return uri;
    }

    public static <T extends EObject> T deepCopy(T emfObject) {
        EcoreUtil.Copier copier = new EcoreUtil.Copier(Boolean.TRUE, Boolean.FALSE);
        preDeepCopy(emfObject, copier);
        T copy = (T) copier.copy(emfObject);
        copier.copyReferences();
        return copy;
    }

    private static void preDeepCopy(EObject emfObject, EcoreUtil.Copier copier) {
        Stack<EObject> work = new Stack<EObject>();
        work.push(emfObject);
        Collection<EObject> collection = new LinkedHashSet<EObject>();
        while (!work.isEmpty()) {
            EObject o = work.pop();
            if (collection.contains(o)) {
                continue;
            }
            collection.add(o);
            List<EObject> list = o.eContents();
            for (EObject eo : list) {
                if (!collection.contains(eo)) {
                    work.push(eo);
                }
            }
            list = o.eCrossReferences();
            for (EObject eo : list) {
                if (!collection.contains(eo)) {
                    work.push(eo);
                }
            }
            EObject container = o.eContainer();
            if (container != null && !collection.contains(container)) {
                work.push(container);
            }
        }
        collection = copier.copyAll(collection);
        copier.copyReferences();

    }

    public static File modelElement2File(ModelElement mElement) {
        URI uri = getURI(mElement);
        if (uri != null) {
            String uriString = WorkspaceUtils.toFile(uri);
            if (uriString != null) {
                return new File(uriString);
            }
        }
        return null;
    }
}
