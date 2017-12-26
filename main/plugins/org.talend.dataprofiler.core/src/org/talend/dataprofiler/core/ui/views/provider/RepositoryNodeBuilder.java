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
package org.talend.dataprofiler.core.ui.views.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.AbstractEMFRepositoryFactory;
import org.talend.core.repository.model.FolderHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.ui.exchange.ExchangeFolderRepNode;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.analysis.category.CategoryHandler;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisFolderRepNode;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DFConnectionFolderRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.IndicatorFolderRepNode;
import org.talend.dq.nodes.JrxmlTempFolderRepNode;
import org.talend.dq.nodes.PatternFolderRepNode;
import org.talend.dq.nodes.ReportFolderRepNode;
import org.talend.dq.nodes.RulesFolderRepNode;
import org.talend.dq.nodes.SourceFileFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HadoopClusterFolderRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.localprovider.model.LocalFolderHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC klliu class global comment. Detailled comment
 */
public final class RepositoryNodeBuilder {

    private static RepositoryNodeBuilder instance;

    /**
     * DOC klliu RepositoryNodeBuilder constructor comment.
     */
    public RepositoryNodeBuilder() {

    }

    /**
     * DOC klliu Comment method "retrieveRepObjectType".
     * 
     * @param path
     * @return
     */
    public ERepositoryObjectType retrieveRepObjectTypeByPath(String path) {
        return RepositoryNodeHelper.retrieveRepObjectTypeByPath(path);
    }

    /**
     * get parent path
     * 
     * @param path relative path to current project
     * @return if path is empty will return {@link Path#EMPTY}
     */
    private String getParentPath(String path) {
        if (path == null) {
            return Path.EMPTY.toOSString();
        }
        IPath removeLastSegments = new Path(path).removeLastSegments(1);
        return removeLastSegments.toString();
    }

    /**
     * DOC klliu Comment method "createSystemFolder".
     * 
     * @param node
     * @param resConstant
     * @return
     * @throws PersistenceException
     * @deprecated instead of it ues {@link #createRepositoryNodeSubSystemFolder(RepositoryNode, EResourceConstant)}
     */
    @Deprecated
    public RepositoryNode createRepositoryNodeSystemFolder(FolderHelper folderHelper, RepositoryNode node,
            EResourceConstant resConstant) throws PersistenceException {
        IRepositoryViewObject folder = null;
        if (folderHelper != null) {
            FolderItem folder2 = folderHelper.getFolder(resConstant.getPath());
            folder = new Folder(folder2.getProperty(), retrieveRepObjectTypeByPath(resConstant.getPath()));
        } else {
            folder = ProxyRepositoryFactory.getInstance().createFolder(retrieveRepObjectTypeByPath(resConstant.getPath()),
                    Path.EMPTY, resConstant.getName());
        }
        return createRepositoryNode(folder, node, resConstant, null);
    }

    /**
     * create repository node for sub system folder
     * 
     * @param node create repository node
     * @param resConstant sub folder type
     * @return sub node
     * @throws PersistenceException
     */
    public RepositoryNode createRepositoryNodeSubSystemFolder(RepositoryNode node, EResourceConstant resConstant)
            throws PersistenceException {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        return createRepositoryNodeSubSystemFolder(node, resConstant, currentProject);

    }

    /**
     * create repository node for sub system folder
     * 
     * @param node create repository node
     * @param resConstant sub folder type
     * @return sub node
     * @throws PersistenceException
     */
    public RepositoryNode createRepositoryNodeSubSystemFolder(RepositoryNode node, EResourceConstant resConstant,
            org.talend.core.model.general.Project currentProject) throws PersistenceException {
        IRepositoryViewObject folder = null;
        ERepositoryObjectType repositoryObjectType = retrieveRepObjectTypeByPath(resConstant.getPath());

        FolderItem folderItem = ProxyRepositoryFactory.getInstance().getFolderItem(currentProject, repositoryObjectType,
                Path.EMPTY);
        if (folderItem != null) {
            folder = new Folder(folderItem.getProperty(), repositoryObjectType);
        } else {
            folder = ProxyRepositoryFactory.getInstance().createFolder(
                    retrieveRepObjectTypeByPath(getParentPath(resConstant.getPath())), Path.EMPTY, resConstant.getName());
        }
        return createRepositoryNode(folder, node, resConstant, currentProject);
    }

    private RepositoryNode createRepositoryNode(IRepositoryViewObject folder, RepositoryNode parentNode,
            EResourceConstant resConstant, org.talend.core.model.general.Project inWhichProject) throws PersistenceException {
        DQRepositoryNode subFolderNode = null;

        switch (resConstant) {
        case ANALYSIS:
            AnalysisFolderRepNode anaFolderNode = new AnalysisFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(anaFolderNode);
            parentNode.getChildren().add(anaFolderNode);
            return anaFolderNode;
        case REPORTS:
            ReportFolderRepNode repFolderNode = new ReportFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(repFolderNode);
            parentNode.getChildren().add(repFolderNode);
            return repFolderNode;
        case INDICATORS:
            IndicatorFolderRepNode indicatorFolderRepNode = new IndicatorFolderRepNode(folder, parentNode,
                    ENodeType.SYSTEM_FOLDER, inWhichProject);
            folder.setRepositoryNode(indicatorFolderRepNode);
            parentNode.getChildren().add(indicatorFolderRepNode);
            return indicatorFolderRepNode;
        case JRXML_TEMPLATE:
            JrxmlTempFolderRepNode jrxmlFolderNode = new JrxmlTempFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(jrxmlFolderNode);
            parentNode.getChildren().add(jrxmlFolderNode);
            return jrxmlFolderNode;
        case SOURCE_FILES:
            SourceFileFolderRepNode sourceFileFolder = new SourceFileFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(sourceFileFolder);
            parentNode.getChildren().add(sourceFileFolder);
            return sourceFileFolder;
        case PATTERNS:
            PatternFolderRepNode patternFolder = new PatternFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(patternFolder);
            parentNode.getChildren().add(patternFolder);
            return patternFolder;
        case RULES:
            RulesFolderRepNode ruleFolder = new RulesFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER, inWhichProject);
            folder.setRepositoryNode(ruleFolder);
            parentNode.getChildren().add(ruleFolder);
            return ruleFolder;
        case DB_CONNECTIONS:
            DBConnectionFolderRepNode dbFolder = new DBConnectionFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(dbFolder);
            parentNode.getChildren().add(dbFolder);
            return dbFolder;
        case FILEDELIMITED:
            DFConnectionFolderRepNode dfmFolder = new DFConnectionFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(dfmFolder);
            parentNode.getChildren().add(dfmFolder);
            return dfmFolder;
        case HADOOP_CLUSTER:
            if (HadoopClusterUtils.getDefault().isServiceInstalled()) {
                HadoopClusterFolderRepNode hcFolder = new HadoopClusterFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                        inWhichProject);
                folder.setRepositoryNode(hcFolder);
                parentNode.getChildren().add(hcFolder);
                return hcFolder;
            }
        case EXCHANGE:
            ExchangeFolderRepNode exchangeFolder = new ExchangeFolderRepNode(folder, parentNode, ENodeType.SYSTEM_FOLDER,
                    inWhichProject);
            folder.setRepositoryNode(exchangeFolder);
            parentNode.getChildren().add(exchangeFolder);
            return exchangeFolder;
        default:
            subFolderNode = new DQRepositoryNode(folder, parentNode, ENodeType.SYSTEM_FOLDER, inWhichProject);
            folder.setRepositoryNode(subFolderNode);
            parentNode.getChildren().add(subFolderNode);
            if (resConstant.equals(EResourceConstant.PATTERNS)) {
                // MOD gdbu 2011-8-26 bug 23303 : initialization regex and sql folder when initialization pattern folder
                createRepositoryNodeSubSystemFolder(subFolderNode, EResourceConstant.PATTERN_REGEX, inWhichProject);
                createRepositoryNodeSubSystemFolder(subFolderNode, EResourceConstant.PATTERN_SQL, inWhichProject);
            }
            break;
        }
        return subFolderNode;

    }

    /**
     * DOC klliu Comment method "createRepositoryNodeSystemFolders".
     * 
     * @param node
     * @param resConstants
     * @return
     * @throws PersistenceException
     * @deprecated use {@link #createRepositoryNodeSystemFolders(RepositoryNode, List)} instead
     */
    @Deprecated
    public List<RepositoryNode> createRepositoryNodeSystemFolders(FolderHelper folderHelper, RepositoryNode node,
            List<EResourceConstant> resConstants) throws PersistenceException {
        List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();
        for (EResourceConstant resConstant : resConstants) {
            repositoryNodes.add(createRepositoryNodeSystemFolder(folderHelper, node, resConstant));
        }
        return repositoryNodes;
    }

    /**
     * create repository node for a system folder
     * 
     * @param node
     * @param resConstants
     * @return sub node list
     * @throws PersistenceException
     */
    public List<RepositoryNode> createRepositoryNodeSystemFolders(RepositoryNode node, List<EResourceConstant> resConstants)
            throws PersistenceException {
        List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();
        for (EResourceConstant resConstant : resConstants) {
            repositoryNodes.add(createRepositoryNodeSubSystemFolder(node, resConstant));
        }
        return repositoryNodes;
    }

    /**
     * create repository node for a system folder
     * 
     * @param node
     * @param resConstants
     * @return sub node list
     * @throws PersistenceException
     */
    public List<RepositoryNode> createRepositoryNodeSystemFolders(RepositoryNode node, List<EResourceConstant> resConstants,
            org.talend.core.model.general.Project currentProject) throws PersistenceException {
        List<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();
        for (EResourceConstant resConstant : resConstants) {
            repositoryNodes.add(createRepositoryNodeSubSystemFolder(node, resConstant, currentProject));
        }
        return repositoryNodes;
    }

    /**
     * DOC klliu Comment method "getIndicatorsChildren".
     * 
     * @param conList
     * @return
     */
    public Collection<? extends IRepositoryNode> getIndicatorsChildren(List<IRepositoryViewObject> conList) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        Iterator<IRepositoryViewObject> iterator = conList.iterator();
        while (iterator.hasNext()) {
            IRepositoryViewObject indicatorFolder = iterator.next();
            list.add(new RepositoryNode(indicatorFolder, null, ENodeType.SYSTEM_FOLDER));
        }
        return list;
    }

    /**
     * 
     * create a Local folder Helper
     * 
     * @return Local Folder helper
     * 
     * @deprecated use {@link AbstractEMFRepositoryFactory#getFolderHelper()} instead.
     */
    @Deprecated
    public FolderHelper getFolderHelper() {
        RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        Project project = null;
        FolderHelper folderHelper = null;
        if (repositoryContext != null) {
            project = repositoryContext.getProject();
            if (project != null) {
                folderHelper = LocalFolderHelper.createInstance(project.getEmfProject(), repositoryContext.getUser());
            }
        }
        return folderHelper;
    }

    /**
     * DOC xqliu Comment method "getIndicatorsChildren".
     * 
     * @param category
     * @return
     */
    public Object[] getIndicatorsChildren(IndicatorCategory category) {
        List<IndicatorDefinition> indicatorDefinitionList = CategoryHandler.getIndicatorDefinitionList(category);
        if (indicatorDefinitionList == null) {
            indicatorDefinitionList = new ArrayList<IndicatorDefinition>();
        }
        return indicatorDefinitionList.toArray();
    }

    /**
     * DOC xqliu Comment method "getIndicatorChildren".
     * 
     * @return
     */
    public Object[] getIndicatorsChildren(IFolder folder) {
        List<Object> list = new ArrayList<Object>();
        // MOD mzhao feature 13676, split system indicators. 2010-07-08
        // list.add(new IndicatorFolderNode("System"));
        try {
            list.addAll(Arrays.asList(folder.members()));
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return list.toArray();
    }

    /**
     * DOC klliu Comment method "getInstance".
     * 
     * @return
     */
    public static RepositoryNodeBuilder getInstance() {
        if (instance == null) {
            instance = new RepositoryNodeBuilder();
        }
        return instance;
    }

    public Folder getObjectFolder(EResourceConstant resConstant) {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        ERepositoryObjectType repositoryObjectType = retrieveRepObjectTypeByPath(resConstant.getPath());
        FolderItem folderItem = ProxyRepositoryFactory.getInstance().getFolderItem(currentProject, repositoryObjectType,
                Path.EMPTY);
        Folder folder = new Folder(folderItem.getProperty(), retrieveRepObjectTypeByPath(resConstant.getPath()));
        return folder;
    }

}
