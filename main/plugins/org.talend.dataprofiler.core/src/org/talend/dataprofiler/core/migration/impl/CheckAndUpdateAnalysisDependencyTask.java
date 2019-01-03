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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.EResourceConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;

/**
 * created by yyin on 2013-7-10 check the client dependency of the analysis according to TDQ-7327 if there are more than
 * one client dependency(db connection), remove the not used one, and remove the related dependency in the removed db
 * connection
 * 
 */
public class CheckAndUpdateAnalysisDependencyTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2013, 7, 10);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        // load all connections and check if any catelog has more than one datamanager
        checkAndRemoveWrongDataManager(getConnections());

        // load all analyses and check their dependency correct or not.
        checkAndRemoveWrongDependencies(getAnalyses());

        return true;
    }

    /**
     * DOC yyin Comment method "getConnections".
     * 
     * @return
     */
    private List<DataManager> getConnections() {
        List<DataManager> connections = new ArrayList<DataManager>();
        File sysIndsFolder = getWorkspacePath().append(EResourceConstant.DB_CONNECTIONS.getPath()).toFile();
        ArrayList<File> fileList = new ArrayList<File>();
        FilesUtils.getAllFilesFromFolder(sysIndsFolder, fileList, getFilenameFilter("item"));
        for (File file : fileList) {
            DataManager indDef = getDataManagerFromFile(file);
            if (indDef != null) {
                connections.add(indDef);
            }
        }

        return connections;
    }

    private List<Analysis> getAnalyses() {
        List<Analysis> analyses = new ArrayList<Analysis>();
        File sysIndsFolder = getWorkspacePath().append(EResourceConstant.ANALYSIS.getPath()).toFile();
        ArrayList<File> fileList = new ArrayList<File>();
        FilesUtils.getAllFilesFromFolder(sysIndsFolder, fileList, getFilenameFilter("ana"));
        for (File file : fileList) {
            File cleanFile = removeNotUsedModels(file);
            Analysis indDef = getAnalysisFromFile(cleanFile);
            if (indDef != null) {
                analyses.add(indDef);
            }
        }

        return analyses;
    }

    private File removeNotUsedModels(File file) {
        // remove not used any more: where aide rule
        DocumentBuilder db;
        try {
            boolean needSave = false;
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(file);
            Element root = document.getDocumentElement();
            NodeList list = root.getElementsByTagName("indicators"); //$NON-NLS-1$
            for (int i = 0; i < list.getLength(); i++) {
                Node item = list.item(i);
                NamedNodeMap attributes = item.getAttributes();
                Node typeItem = attributes.getNamedItem("xsi:type"); //$NON-NLS-1$
                if (StringUtils.equals("dataquality.indicators.sql:WhereRuleAideIndicator", typeItem.getNodeValue())) { //$NON-NLS-1$
                    item.getParentNode().removeChild(item);
                    needSave = true;
                }
            }
            if (needSave) {
                saveFile(file, document);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private void saveFile(File needSaveFile, Document doc) {// 将Document输出到文件
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            transFactory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
            Transformer transformer = transFactory.newTransformer();
            Source xmlSource = new DOMSource(doc);
            Result result = new StreamResult(needSaveFile);
            transformer.transform(xmlSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private FilenameFilter getFilenameFilter(final String ends) {
        return new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith(ends)) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * @param file
     * @return
     */
    private DataManager getDataManagerFromFile(File file) {
        Resource itemResource = getResource(file);
        if (itemResource == null) {
            return null;
        }
        for (EObject object : itemResource.getContents()) {
            if (object instanceof DataManager) {
                return (DataManager) object;
            }
        }

        return null;
    }

    /**
     * @param file
     * @return
     */
    private Analysis getAnalysisFromFile(File file) {
        Resource itemResource = getResource(file);
        if (itemResource == null) {
            return null;
        }
        for (EObject object : itemResource.getContents()) {
            if (object instanceof Analysis) {
                return (Analysis) object;
            }
        }

        return null;
    }

    /**
     * only need to check database connection
     * 
     * @param connections
     */
    private void checkAndRemoveWrongDataManager(List<DataManager> connections) {
        for (DataManager connection : connections) {
            if (connection instanceof DatabaseConnection) {
                EList<Package> packages = connection.getDataPackage();
                for (EObject obj : packages) {
                    Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(obj);
                    // if catelog has more than one data manager
                    if (catalog != null && catalog.getDataManager().size() > 1) {
                        List<DataManager> wrongManagers = new ArrayList<DataManager>();
                        // remove the wrong datamanger from the catelog;
                        for (DataManager manager : catalog.getDataManager()) {
                            // if the name of the manager does not equal with the parent of the catalog,it is the wrong
                            // one
                            if (!StringUtils.equals(((DatabaseConnection) manager).getName(),
                                    ((DatabaseConnection) connection).getName())) {
                                wrongManagers.add(manager);
                            }
                        }
                        removeWrongDataManager(catalog, wrongManagers, connection);
                    }
                }
            }
        }
    }

    /**
     * remove the Wrong Data Manager,and save the connection
     * 
     * @param catalog
     * @param connection
     * @param parent
     */
    private void removeWrongDataManager(Catalog catalog, List<DataManager> wrongManagers, DataManager parent) {
        for (DataManager wrongone : wrongManagers) {
            catalog.getDataManager().remove(wrongone);
        }
        EMFSharedResources.getInstance().saveResource(parent.eResource());
    }

    /**
     * check each analysis if it has only one client dependency, if more than one, remove the useless one.
     * 
     * @param list
     * @throws CoreException
     */
    private void checkAndRemoveWrongDependencies(List<Analysis> analyses) throws CoreException {

        for (Analysis analysis : analyses) {
            if (analysis != null) {
                boolean isAnalysisModified = false;

                if (analysis.getContext().getAnalysedElements() == null || analysis.getContext().getAnalysedElements().size() < 1) {
                    continue;
                }

                TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analysis.getContext().getAnalysedElements().get(0));
                if (tdColumn == null) {
                    continue;
                }
                // find the correct db connection from analyzed element
                DataManager correctDB = ConnectionHelper.getConnection(tdColumn);

                // check if the connection is correct or not
                DataManager connection = analysis.getContext().getConnection();
                if (connection == null || !correctDB.getName().equals(connection.getName())) {
                    analysis.getContext().setConnection(correctDB);
                    isAnalysisModified = true;
                }

                List<DataProvider> clientDependencyDB = null;
                for (Dependency dependency : analysis.getClientDependency()) {
                    ModelElement supplier = dependency.getSupplier().get(0);

                    if (supplier instanceof DataProvider) {
                        if (supplier != null && correctDB.getName().equals(supplier.getName())) {
                            continue;
                        } else {
                            if (clientDependencyDB == null) {
                                clientDependencyDB = new ArrayList<DataProvider>();
                            }
                            clientDependencyDB.add((DataProvider) supplier);
                        }
                    }
                }
                if (clientDependencyDB != null) {
                    for (DataProvider uselessDB : clientDependencyDB) {
                        // if the db in client dependency do not equal to the correct db, remove it from both the
                        // analysis and db connection
                        removeDependenciesBetweenAnaCon(uselessDB, analysis);
                        isAnalysisModified = true;
                    }
                }
                if (isAnalysisModified) {
                    EMFSharedResources.getInstance().saveResource(analysis.eResource());
                }
            }
        }

    }

    private void removeDependenciesBetweenAnaCon(DataProvider oldDataProvider, Analysis tempAnalysis) {
        List<ModelElement> tempList = new ArrayList<ModelElement>();
        tempList.add(oldDataProvider);
        // remove the cliend dependency in the analysis
        List<Resource> modified = DependenciesHandler.getInstance().removeDependenciesBetweenModels(tempAnalysis, tempList);
        for (Resource me : modified) {
            EMFUtil.saveSingleResource(me);
        }
        // remove the supplier dependency in the dataprovider
        tempList.clear();
        tempList.add(tempAnalysis);
        modified = removeSupplierDependenciesBetweenModels(oldDataProvider, tempList);
        for (Resource me : modified) {
            EMFUtil.saveSingleResource(me);
        }
        EMFUtil.saveSingleResource(oldDataProvider.eResource());
    }

    /*
     * This task should be done before the import
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#isModelTask()
     */
    @Override
    public Boolean isModelTask() {
        return true;
    }

    private List<Resource> removeSupplierDependenciesBetweenModels(ModelElement elementFromRemove,
            List<? extends ModelElement> elementToRemove) {
        EList<Dependency> supplierDependencies;
        List<String> toRemoveResources = new ArrayList<String>();
        for (ModelElement modelElement : elementToRemove) {
            toRemoveResources.add(modelElement.getName());
        }
        // get the client dependencies (
        supplierDependencies = elementFromRemove.getSupplierDependency();

        List<Resource> modifiedResources = new ArrayList<Resource>();
        for (Dependency dependency : supplierDependencies) {
            EList<ModelElement> client = dependency.getClient();
            // get the resource of each client
            Iterator<ModelElement> dependencyIterator = client.iterator();
            while (dependencyIterator.hasNext()) {
                ModelElement dependencyModel = dependencyIterator.next();
                Resource clientResource = dependencyModel.eResource();

                if (clientResource != null) {
                    if (toRemoveResources.contains(dependencyModel.getName())) {
                        modifiedResources.add(clientResource);
                        dependencyIterator.remove();
                    }
                }
            }
        }

        return modifiedResources;
    }
}
