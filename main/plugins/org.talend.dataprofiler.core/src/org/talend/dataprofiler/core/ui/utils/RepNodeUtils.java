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
package org.talend.dataprofiler.core.ui.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataprofiler.core.ui.views.resources.LocalRepositoryObjectCRUD;
import org.talend.dataprofiler.core.ui.views.resources.RemoteRepositoryObjectCRUD;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.helpers.ReportHelper.ReportType;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.dq.nodes.ColumnSetRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * RepositoryNode's UI utils.
 */
public final class RepNodeUtils {

    private static Logger log = Logger.getLogger(RepNodeUtils.class);

    private static String separator = File.separator;

    private RepNodeUtils() {
    }

    /**
     * close file node's editor.
     * 
     * @param files
     * @param save
     */
    public static void closeFileEditor(List<IFile> files, boolean save) {
        List<IEditorReference> need2CloseEditorRefs = new ArrayList<IEditorReference>();
        IEditorReference[] editorReferences = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();

        try {
            for (IEditorReference editorRef : editorReferences) {
                if (editorRef != null) {
                    IEditorInput editorInput = editorRef.getEditorInput();
                    if (editorInput != null) {
                        if (editorInput instanceof FileEditorInput) {
                            IFile file = ((FileEditorInput) editorInput).getFile();
                            if (file != null) {
                                for (IFile ifile : files) {
                                    if (ifile != null) {
                                        String osString = ifile.getRawLocation().toOSString();
                                        String osString2 = file.getRawLocation().toOSString();
                                        if (osString.equals(osString2)) {
                                            need2CloseEditorRefs.add(editorRef);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (PartInitException e) {
            log.warn(e, e);
        }

        if (need2CloseEditorRefs.size() > 0) {
            CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .closeEditors(need2CloseEditorRefs.toArray(new IEditorReference[need2CloseEditorRefs.size()]), save);

        }
    }

    /**
     * close ModelElement node's editor.
     * 
     * @param nodes
     */
    public static void closeModelElementEditor(List<? extends IRepositoryNode> nodes, boolean save) {
        List<String> uuids = RepositoryNodeHelper.getUuids(nodes);

        List<IEditorReference> need2CloseEditorRefs = new ArrayList<IEditorReference>();
        IEditorReference[] editorReferences = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getEditorReferences();

        try {
            for (IEditorReference editorRef : editorReferences) {
                if (editorRef != null) {
                    IEditorInput editorInput = editorRef.getEditorInput();
                    if (editorInput != null) {
                        if (editorInput instanceof AbstractItemEditorInput) {
                            String modelElementUuid = ((AbstractItemEditorInput) editorInput).getModelElementUuid();
                            if (modelElementUuid != null && uuids.contains(modelElementUuid)) {
                                need2CloseEditorRefs.add(editorRef);
                            }
                        }
                    }
                }
            }
        } catch (PartInitException e) {
            log.warn(e, e);
        }

        if (need2CloseEditorRefs.size() > 0) {
            CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .closeEditors(need2CloseEditorRefs.toArray(new IEditorReference[need2CloseEditorRefs.size()]), save);

        }
    }

    /**
     * 
     * Get repostiroy object CRUD class according to project type.
     * 
     * @return
     */
    public static IRepositoryObjectCRUDAction getRepositoryObjectCRUD() {
        if (ProxyRepositoryManager.getInstance().isLocalProject()) {
            return new LocalRepositoryObjectCRUD();
        } else {
            return new RemoteRepositoryObjectCRUD();
        }
    }

    /**
     * when the report's user defined template(Jrxml file) changed its name, or be moved, the path info in the report's
     * anaMap: jrxml source should also be updated. This method is used to update the related reports when the jrxml
     * name or path is changed.
     * 
     * @param oldPath : the whole path with whole name of the jrxml, e.g./TDQ_Libraries/JRXML
     * Template/columnset/column_set_basic_0.1.jrxml
     * @param newPath
     * @throws PersistenceException
     */
    public static void updateJrxmlRelatedReport(IPath oldPath, IPath newPath) {
        if (oldPath == null || newPath == null) {
            return;
        }

        List<String> jrxmlFileNames = new ArrayList<String>();
        List<String> jrxmlFileNamesAfterMove = new ArrayList<String>();
        jrxmlFileNames.add(oldPath.toString());
        jrxmlFileNamesAfterMove.add(newPath.toString());

        updateJrxmlRelatedReport(jrxmlFileNames, jrxmlFileNamesAfterMove);
    }

    /**
     * check if the anaMap comtains the Jrxml or not, by compare the jrxml's path with anaMap's jrxml source(when user
     * mode)
     * 
     * @param path contain the file name like:/TDQ_Libraries/JRXMLTemplate/column/column_basic_0.1.jrxml
     * @param anaMap
     * @return true :if the anaMap contains the path.
     */
    private static boolean isUsedByJrxml(IPath path, AnalysisMap anaMap) {
        ReportType reportType = ReportHelper.ReportType.getReportType(anaMap.getAnalysis(), anaMap.getReportType());
        // compare the Jrxml path if the report has the user defined one.
        if (ReportHelper.ReportType.USER_MADE.equals(reportType)) {
            String jrxmlPath = anaMap.getJrxmlSource();
            if (new Path(jrxmlPath).toOSString().contains(path.toOSString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * when the report's user defined template(Jrxml file) changed its name, or be moved, the path info in the report's
     * anaMap: jrxml source should also be updated. This method is used to update the related reports when the jrxml
     * name or path is changed.
     * 
     * @param jrxmlFileNames : the whole path with whole name of the jrxml, e.g./TDQ_Libraries/JRXML
     * Template/columnset/column_set_basic_0.1.jrxml
     * @param jrxmlFileNamesAfterMove
     */
    public static void updateJrxmlRelatedReport(List<String> jrxmlFileNames, List<String> jrxmlFileNamesAfterMove) {
        if (jrxmlFileNames.size() == 0 || jrxmlFileNamesAfterMove.size() == 0
                || jrxmlFileNamesAfterMove.size() < jrxmlFileNames.size()) {
            return;
        }

        Project project = ProjectManager.getInstance().getCurrentProject();
        // get all reports
        IRepositoryNode ReportRootFolderNode = RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.REPORTS);
        List<ReportRepNode> repNodes = RepositoryNodeHelper.getReportRepNodes(ReportRootFolderNode, true, true);
        // go through every report to :if any one used current jrxml-->modify its jrxml resource name
        for (ReportRepNode report : repNodes) {
            boolean isUpdated = false;
            EList<AnalysisMap> analysisMap = ((TdReport) report.getReport()).getAnalysisMap();
            for (AnalysisMap anaMap : analysisMap) {
                for (int i = 0; i < jrxmlFileNames.size(); i++) {
                    String oldPath = jrxmlFileNames.get(i);
                    if (isUsedByJrxml(new Path(oldPath), anaMap)) {

                        // like: ../../../TDQ_Libraries/JRXML Template/column1/b01_column_basic_0.1.jrxml
                        String newPath = getRelativeJrxmlPath(report.getReport(), jrxmlFileNamesAfterMove.get(i));

                        // Added 20130128, using event/listener to refresh the page if opening
                        EventManager.getInstance().publish(report, EventEnum.DQ_JRXML_RENAME, newPath);

                        // save the new jrxml path.
                        anaMap.setJrxmlSource(newPath);
                        isUpdated = true;
                    }
                }
            }
            if (isUpdated) {
                try {
                    ProxyRepositoryFactory.getInstance().save(project, report.getObject().getProperty().getItem());
                } catch (PersistenceException e) {
                    MessageUI.openError(DefaultMessagesImpl.getString("RepNodeUtils.updateReport.fail", report.getLabel())); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * get the Relative path of Jrxml file.
     * 
     * @param Report report
     * @param String path
     * @return String
     */
    public static String getRelativeJrxmlPath(Report report, String path) {
        return ResourceManager.getRootProject().getFile(path).getLocation()
                .makeRelativeTo(ModelElementHelper.getIFile(report).getLocation()).toString();
    }

    /**
     * get the full jrxml name with path before move/rename its parent folder.
     * 
     * @param oldPath: the path before rename
     * @param jrxmlFileRepNodes the related reports list with name
     * @return the full jrxml new names with path
     */
    public static List<String> getListOfJrxmlNameWithPath(IPath path, List<JrxmlTempleteRepNode> jrxmlFileRepNodes) {
        List<String> jrxmlFileNames = new ArrayList<String>();
        for (JrxmlTempleteRepNode jrxml : jrxmlFileRepNodes) {
            // if the parent of the jrxml is not the current folder,
            IPath parentPath = RepositoryNodeHelper.getPath(jrxml.getParent());
            IPath makeRelativeTo = null;
            if (path.equals(parentPath)) {
                makeRelativeTo = path.append(separator).append(RepositoryNodeHelper.getFileNameOfTheNode(jrxml));
            } else {
                makeRelativeTo = parentPath.append(separator).append(RepositoryNodeHelper.getFileNameOfTheNode(jrxml));
            }
            jrxmlFileNames.add(makeRelativeTo.toOSString());
        }
        return jrxmlFileNames;
    }

    /**
     * Used for replace only the renamed folder name, the path of the parent& the path of the sub folder should remain.
     * 
     * @param oldPath: the path before rename
     * @param newPath: the new foler name
     * @param jrxmlFileRepNodes the related reports list with name
     * @return the full jrxml new names after folder renamed
     */
    public static List<String> getListOfJrxmlNewNameWithPath(IPath oldPath, IPath newPath,
            List<JrxmlTempleteRepNode> jrxmlFileRepNodes) {
        List<String> jrxmlFileNames = new ArrayList<String>();
        for (JrxmlTempleteRepNode jrxml : jrxmlFileRepNodes) {
            // if the parent of the jrxml is not the current folder,
            IPath parentPath = RepositoryNodeHelper.getPath(jrxml.getParent());
            if (oldPath.equals(parentPath)) {
                jrxmlFileNames.add(newPath.append(separator).append(RepositoryNodeHelper.getFileNameOfTheNode(jrxml))
                        .toOSString());
            } else {
                // change the old folder name in parent path to new path:
                // e.g. /tdq_libraries/JRXML Template/c01/(sub/) to -->/tdq_libraries/JRXML Template/c01_new/sub/
                IPath replacedPath = new Path(""); //$NON-NLS-1$
                for (int i = 0; i < parentPath.segmentCount(); i++) {
                    if (i < newPath.segmentCount() && !newPath.segment(i).equals(parentPath.segment(i))) {
                        replacedPath = replacedPath.append(newPath.segment(i)).append(separator);
                    } else {
                        replacedPath = replacedPath.append(parentPath.segment(i)).append(separator);
                    }
                }
                jrxmlFileNames.add(replacedPath.append(RepositoryNodeHelper.getFileNameOfTheNode(jrxml)).toOSString());

            }
        }
        return jrxmlFileNames;
    }

    public static String getSeparator() {
        return separator;
    }

    public static boolean isDelimitedFile(Object object) {
        return (object instanceof DFTableRepNode || object instanceof DFColumnRepNode);
    }

    public static List<IRepositoryNode> translateSelectedToStandardReposityoryNode(Object[] objs) {
        List<IRepositoryNode> reposList = new ArrayList<IRepositoryNode>();
        for (Object obj : objs) {
            // MOD klliu 2011-02-16 feature 15387
            if (obj instanceof DBColumnRepNode || obj instanceof DFColumnRepNode) {
                reposList.add((RepositoryNode) obj);
            } else if (obj instanceof DBTableRepNode || obj instanceof DBViewRepNode || obj instanceof DFTableRepNode) {
                List<IRepositoryNode> children = ((IRepositoryNode) obj).getChildren().get(0).getChildren();
                reposList.addAll(children);

            } else if (obj instanceof MetadataColumn) {
                // MOD qiongli TDQ-7052 if the node is filtered ,it will be return null,so should create a new node.
                RepositoryNode repNode = RepositoryNodeHelper.recursiveFind((ModelElement) obj);
                if (repNode == null) {
                    repNode = RepositoryNodeHelper.createRepositoryNode((ModelElement) obj);
                }
                reposList.add(repNode);
            }
        }
        return reposList;
    }

    /**
     * Added TDQ-8647 20140220 yyin: check the selected nodes, only when the user select some columns from one same
     * column set, or the user select one column set, the Finish button is enabled, all other selection will not enable
     * the Finish button.
     */
    public static boolean isValidSelectionFromSameTable(List<IRepositoryNode> nodes) {
        if (nodes != null && nodes.size() > 0) {
            // when the first selected node is a column set node type, then the size of the nodes list must be 1
            if ((nodes.get(0) instanceof ColumnSetRepNode) && nodes.size() == 1) {
                return Boolean.TRUE;
            }

            // when the first selected node is a column node type, then:
            // all selected nodes must be in the same column set.
            if (nodes.get(0) instanceof ColumnRepNode) {
                if (nodes.size() == 1) {
                    return Boolean.TRUE;
                }
                RepositoryNode firstParent = nodes.get(0).getParent();
                for (int index = 1; index < nodes.size(); index++) {
                    RepositoryNode currentParent = nodes.get(index).getParent();
                    // if any node's parent not equals to the first one, return false
                    if (!firstParent.equals(currentParent)) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * check is All the Columns are Numberal type.
     * 
     * @param nodes
     * @return
     */
    public static boolean isAllNumberalColumns(List<IRepositoryNode> nodes) {
        if (nodes != null && nodes.size() > 0) {
            for (int index = 0; index < nodes.size(); index++) {
                IRepositoryNode repositoryNode = nodes.get(index);
                if (!(repositoryNode instanceof ColumnRepNode)) {
                    return false;
                }
                int javaType = getColumnJavaType(repositoryNode);
                if (!Java2SqlType.isNumbericInSQL(javaType)) {
                    return false;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * check is All the Columns are text type.
     * 
     * @param nodes
     * @return
     */
    public static boolean isAllTextColumns(List<IRepositoryNode> nodes) {
        if (nodes != null && nodes.size() > 0) {
            for (int index = 0; index < nodes.size(); index++) {
                IRepositoryNode repositoryNode = nodes.get(index);
                if (!(repositoryNode instanceof ColumnRepNode)) {
                    return false;
                }
                int javaType = getColumnJavaType(repositoryNode);
                if (!Java2SqlType.isTextInSQL(javaType)) {
                    return false;
                }
            }
        }
        return Boolean.TRUE;
    }

    private static int getColumnJavaType(IRepositoryNode repositoryNode) {
        int javaType = 0;
        if ((repositoryNode instanceof ColumnRepNode)) {
            MetadataColumn column = ((MetadataColumnRepositoryObject) repositoryNode.getObject()).getTdColumn();
            if (SwitchHelpers.COLUMN_SWITCH.doSwitch(column) != null) {
                javaType = ((TdColumn) column).getSqlDataType().getJavaDataType();
            } else {
                javaType = TalendTypeConvert.convertToJDBCType(column.getTalendType());
            }
        }
        return javaType;
    }

    /**
     * check whether the nodes can support pattern Frequency indicator for sql engine.
     * 
     * @param nodes
     * @return
     */
    public static boolean isSupportPatternFrequency(List<IRepositoryNode> nodes) {
        if (nodes != null && !nodes.isEmpty()) {
            Connection connection = null;
            if (nodes.get(0) instanceof ColumnRepNode) {
                MetadataColumn column = ((MetadataColumnRepositoryObject) nodes.get(0).getObject()).getTdColumn();
                connection = ConnectionHelper.getTdDataProvider(column);
            } else if (nodes.get(0) instanceof ColumnSetRepNode) {
                MetadataTable table = ((MetadataTableRepositoryObject) nodes.get(0).getObject()).getTable();
                connection = ConnectionHelper.getTdDataProvider(table);
            }

            if (connection != null
                    && (ConnectionHelper.isTeradata(connection) || ConnectionHelper.isIngress(connection) || ConnectionHelper
                            .isSybase(connection))) {
                return false;
            }
        }
        return true;
    }
}
