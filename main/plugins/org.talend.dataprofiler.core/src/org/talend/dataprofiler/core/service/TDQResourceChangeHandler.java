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
package org.talend.dataprofiler.core.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.message.DeleteModelElementConfirmDialog;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.DQDeleteHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.ReportFileHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.DQRuleWriter;
import org.talend.dq.writer.impl.IndicatorDefinitionWriter;
import org.talend.dq.writer.impl.MatchRuleDefinitionWriter;
import org.talend.dq.writer.impl.PatternWriter;
import org.talend.dq.writer.impl.ReportWriter;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import org.talend.utils.files.FileUtils;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * 
 * DOC mzhao Handle resource unload events from TOS.
 */
public class TDQResourceChangeHandler extends AbstractResourceChangesService {

    private static Logger log = Logger.getLogger(TDQResourceChangeHandler.class);

    private XmiResourceManager xmiResourceManager = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
            .getResourceManager();

    public TDQResourceChangeHandler() {
    }

    @Override
    public void handleUnload(Resource toBeUnloadedResource) {
        for (EObject eObject : toBeUnloadedResource.getContents()) {
            // try {
            if (eObject instanceof DatabaseConnection) {
                // ProxyRepositoryViewObject.registerURI((DatabaseConnection) eObject, toBeUnloadedResource.getURI());
                // if (xmiResourceManager != null) {
                // try {
                // xmiResourceManager.saveResource(toBeUnloadedResource);
                // } catch (PersistenceException e) {
                // log.error(e, e);
                // }
                //
                // }
            }
            // } catch (PersistenceException e) {
            // log.error(e, e);
            // }
            // else anaysis,report etc.
        }
        super.handleUnload(toBeUnloadedResource);
    }

    @Override
    public Resource create(IProject project, Item item, int classID, IPath path) {
        String fileExtension = FileConstants.ITEM_EXTENSION;
        Resource itemResource = null;
        try {
            switch (classID) {
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_ANALYSIS_ITEM:
                fileExtension = FileConstants.ANA_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT, false, fileExtension);

                AnalysisWriter createAnalysisWrite = org.talend.dq.writer.impl.ElementWriterFactory.getInstance()
                        .createAnalysisWrite();
                Analysis analysis = ((TDQAnalysisItem) item).getAnalysis();
                createAnalysisWrite.addResourceContent(itemResource, analysis);
                createAnalysisWrite.addDependencies(analysis);

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_REPORT_ITEM:
                fileExtension = FileConstants.REP_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_REPORT_ELEMENT, false, fileExtension);

                ReportWriter createReportWriter = org.talend.dq.writer.impl.ElementWriterFactory.getInstance()
                        .createReportWriter();
                Report report = ((TDQReportItem) item).getReport();
                createReportWriter.addResourceContent(itemResource, report);
                createReportWriter.addDependencies(report);

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_INDICATOR_DEFINITION_ITEM:
                fileExtension = FileConstants.DEF_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_INDICATOR_ELEMENT, false, fileExtension);

                IndicatorDefinition indicatorDefinition = ((TDQIndicatorDefinitionItem) item).getIndicatorDefinition();
                IndicatorDefinitionWriter createIndicatorDefinitionWriter = org.talend.dq.writer.impl.ElementWriterFactory
                        .getInstance().createIndicatorDefinitionWriter();
                createIndicatorDefinitionWriter.addResourceContent(itemResource, indicatorDefinition);
                // createIndicatorDefinitionWriter.addDependencies(indicatorDefinition);

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_PATTERN_ITEM:
                fileExtension = FileConstants.PAT_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_PATTERN_ELEMENT, false, fileExtension);

                Pattern pattern = ((TDQPatternItem) item).getPattern();
                PatternWriter createPatternWriter = org.talend.dq.writer.impl.ElementWriterFactory.getInstance()
                        .createPatternWriter();
                createPatternWriter.addResourceContent(itemResource, pattern);
                // createPatternWriter.addDependencies(pattern);

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_BUSINESS_RULE_ITEM:
                fileExtension = FileConstants.RULE_EXTENSION;
                // MOD gdbu 2011-6-10 bug : 21823 2011-06-27 revert the error type code by klliu
                DQRule dqrule = ((TDQBusinessRuleItem) item).getDqrule();
                if (dqrule instanceof WhereRule) {
                    itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                            ERepositoryObjectType.TDQ_RULES_SQL, false, fileExtension);
                } else {
                    itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                            ERepositoryObjectType.TDQ_RULES_PARSER, false, fileExtension);
                }
                // ~21823
                DQRuleWriter createdRuleWriter = org.talend.dq.writer.impl.ElementWriterFactory.getInstance().createdRuleWriter();
                createdRuleWriter.addResourceContent(itemResource, dqrule);

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_JRXML_ITEM:
                fileExtension = FileConstants.JRXML_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_JRAXML_ELEMENT, true, fileExtension);
                itemResource.getContents().add(((TDQFileItem) item).getContent());

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_SOURCE_FILE_ITEM:
                fileExtension = FileConstants.SQL_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT, true, fileExtension);
                itemResource.getContents().add(((TDQFileItem) item).getContent());

                break;
            case org.talend.dataquality.properties.PropertiesPackage.TDQ_MATCH_RULE_ITEM:
                fileExtension = FileConstants.RULE_EXTENSION;
                itemResource = xmiResourceManager.createItemResourceWithExtension(project, item, path,
                        ERepositoryObjectType.TDQ_RULES_MATCHER, false, fileExtension);
                MatchRuleDefinition matchRuleDefinition = ((TDQMatchRuleItem) item).getMatchRule();
                MatchRuleDefinitionWriter matchRuleDefinitionWriter = org.talend.dq.writer.impl.ElementWriterFactory
                        .getInstance().createdMatchRuleWriter();
                matchRuleDefinitionWriter.addResourceContent(itemResource, matchRuleDefinition);

                break;
            default:
            }

        } catch (PersistenceException e) {
            log.error(e, e);
        }
        return itemResource;
    }

    @Override
    public boolean isAnalysisOrReportItem(Item item) {
        String path = item.getState().getPath();
        if (path != null) {
            if (item instanceof TDQAnalysisItem) {
                return path.equals(ResourceManager.getAnalysisFolder().getFullPath().toString());
            } else if (item instanceof TDQReportItem) {
                return path.equals(ResourceManager.getReportsFolder().getFullPath().toString());
            }
        }
        return super.isAnalysisOrReportItem(item);
    }

    @Override
    public void removeAllDependecies(Item item) {
        ModelElement modelElement = RepositoryNodeHelper.getResourceModelElement(item);
        if (modelElement != null) {
            EObjectHelper.removeDependencys(modelElement);
        }
    }

    @Override
    public void updateDependeciesWhenVersionChange(ConnectionItem connItem, String oldVersion, String newVersion) {
        Connection connection = connItem.getConnection();
        if (connection != null) {
            EList<Dependency> supplierDependencies = connection.getSupplierDependency();

            if (supplierDependencies != null && !supplierDependencies.isEmpty()) {
                Dependency[] arraySupplierDeps = supplierDependencies.toArray(new Dependency[supplierDependencies.size()]);

                for (Dependency supplierDep : arraySupplierDeps) {
                    EList<ModelElement> clients = supplierDep.getClient();

                    if (clients != null && !clients.isEmpty()) {
                        ModelElement[] arrayClients = clients.toArray(new ModelElement[clients.size()]);

                        for (ModelElement client : arrayClients) {

                            if (client instanceof Analysis) {
                                IFile clientFile = AnaResourceFileHelper.findCorrespondingFile(client);
                                if (clientFile != null) {
                                    File file = WorkspaceUtils.ifileToFile(clientFile);

                                    if (file != null) {
                                        replaceVersionInfo(connection.getName(), file, oldVersion, newVersion);
                                        reloadFile(file);
                                    }
                                }
                            }
                        }
                    }
                }

                ResourceService.refreshStructure();
                WorkbenchUtils.refreshAnalysesNode();
                WorkbenchUtils.refreshMetadataNode();
            }
        }
    }

    /**
     * replace the oldVersion with newVersion in the file.
     * 
     * @param name
     * @param clientFile
     * @param oldVersion
     * @param newVersion
     * @throws URISyntaxException
     * @throws IOException
     */
    private void replaceVersionInfo(String name, File clientFile, String oldVersion, String newVersion) {
        String oldString = name + "_" + oldVersion; //$NON-NLS-1$
        String newString = name + "_" + newVersion; //$NON-NLS-1$
        try {
            FileUtils.replaceInFile(clientFile.getAbsolutePath(), oldString, newString);
        } catch (IOException e) {
            log.error(e);
        } catch (URISyntaxException e) {
            log.error(e);
        }
    }

    /**
     * DOC xqliu Comment method "reloadFile".
     * 
     * @param file
     */
    private void reloadFile(File file) {
        IFile ifile = WorkspaceUtils.fileToIFile(file);
        URI uri = URI.createPlatformResourceURI(ifile.getFullPath().toString(), false);
        EMFSharedResources.getInstance().reloadResource(uri);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.repository.utils.AbstractResourceChangesService#saveResourceByEMFShared(org.talend.core.model
     * .properties.Item)
     */
    @Override
    public void saveResourceByEMFShared(Resource toSave) {
        // ADD gdbu 2011-10-24 TDQ-3546
        EMFSharedResources.getInstance().saveResource(toSave);
    }

    @Override
    public void moveReportGeneratedDocFolder(TDQItem tdqItem, File tarFolder) {
        if (tdqItem instanceof TDQReportItem && tarFolder.exists()) {
            TDQReportItem tdqReportItem = (TDQReportItem) tdqItem;
            Report report = tdqReportItem.getReport();
            IFile iFile = ModelElementHelper.getIFile(report);
            IFolder outputFolder = ReportFileHelper.getOutputFolder(iFile);
            File srcFolder = WorkspaceUtils.ifolderToFile(outputFolder);
            FilesUtils.copyDirectoryWithoutSvnFolder(srcFolder, tarFolder);
            File newFolder = new File(tarFolder.getAbsolutePath() + File.separator + srcFolder.getName());
            if (newFolder.exists()) {
                IFolder ifolder = WorkspaceUtils.fileToIFolder(newFolder);
                if (ifolder != null) {
                    try {
                        ifolder.refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (CoreException e) {
                        log.info(e);
                    }
                }
            }
        }
    }

    @Override
    /**
     * get has client dependence nodes.
     */
    public List<IRepositoryNode> getDependentNodes(IRepositoryNode currentNode) {
        List<IRepositoryNode> dependentNodes = new ArrayList<IRepositoryNode>();
        if (currentNode == null) {
            return dependentNodes;
        }
        if (currentNode.getType() == ENodeType.SIMPLE_FOLDER) {
            for (IRepositoryNode curNode : currentNode.getChildren()) {
                List<IRepositoryNode> subDependentNodes = getDependentNodes(curNode);
                if (!subDependentNodes.isEmpty()) {
                    dependentNodes.addAll(subDependentNodes);
                }
            }
        } else {
            List<ModelElement> clientDependencys = new ArrayList<ModelElement>();
            clientDependencys = EObjectHelper.getDependencyClients(currentNode);
            if (!clientDependencys.isEmpty()) {
                dependentNodes.add(currentNode);
            }

        }
        return dependentNodes;
    }

    /**
     * judge if has connection in recycle bin which depended by DQ analysis.
     */
    @Override
    public List<IRepositoryNode> getDependentConnNodesInRecycleBin(List<IRepositoryNode> firstLevelRecyNodes) {
        List<IRepositoryNode> canBeDependedNodes = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode node : firstLevelRecyNodes) {
            canBeDependedNodes.addAll(getCanBeDependedNodes(node));
        }
        return DQDeleteHelper.getCanNotDeletedNodes(canBeDependedNodes, false);
    }

    /**
     * 
     * get all nodes that can be depended in recycle bin.
     * 
     * @param parent
     * @return List: AllNodesCanBeDepended
     */
    private List<IRepositoryNode> getCanBeDependedNodes(IRepositoryNode parent) {
        List<IRepositoryNode> childrenList = new ArrayList<IRepositoryNode>();
        if (parent.getType() == ENodeType.SIMPLE_FOLDER) {
            List<IRepositoryNode> children = parent.getChildren(true);
            for (IRepositoryNode node : children) {
                childrenList.addAll(getCanBeDependedNodes(node));
            }
        } else {
            ERepositoryObjectType objectType = parent.getObjectType();
            if (objectType != null
                    && (objectType == ERepositoryObjectType.METADATA_CONNECTIONS
                            || objectType == ERepositoryObjectType.METADATA_FILE_DELIMITED
                            || objectType == ERepositoryObjectType.METADATA_CON_TABLE || objectType == ERepositoryObjectType.METADATA_CON_VIEW)) {
                childrenList.add(parent);
            }
        }
        return childrenList;
    }

    @Override
    public void openDependcesDialog(List<IRepositoryNode> nodes) {
        DeleteModelElementConfirmDialog.showDialog(Display.getCurrent().getActiveShell(), nodes,
                DefaultMessagesImpl.getString("DQEmptyRecycleBinAction.allDependencies")); //$NON-NLS-1$
    }
}
