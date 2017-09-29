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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

import com.talend.csv.CSVReader;
import com.talend.csv.CSVWriter;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class ReportFileHelper {

    private static Logger log = Logger.getLogger(ReportFileHelper.class);

    public static final String REPORT_LIST = ".report.list";//$NON-NLS-1$

    private static Map<String, List<String>> mainSubRepMap;

    private ReportFileHelper() {
    }

    /**
     * DOC xqliu Comment method "initRepListFile".
     * 
     * @param reportFile
     * @throws Exception
     * @deprecated
     */
    @Deprecated
    public static void initRepListFile(IFile reportFile) throws Exception {
        String reportFileName = reportFile.getName();
        String simpleName = getSimpleName(reportFileName);
        if (simpleName == null) {
            return;
        }
        IFolder reportFileFolder = ((IFolder) reportFile.getParent()).getFolder(PluginConstant.DOT_STRING + simpleName);
        if (reportFileFolder != null && reportFileFolder.exists()) {

            File repListFile = new File(ReportHelper.getOutputFolderNameDefault((IFolder) reportFile.getParent(), simpleName)
                    + File.separator + REPORT_LIST);
            List<ReportListParameters> repList = new ArrayList<ReportListParameters>();

            IResource[] members = reportFileFolder.members();

            for (IResource res : members) {
                if (res.getType() == IResource.FILE) {
                    IFile repFile = (IFile) res;
                    repList.add(buildRepListParams(repFile.getName(), repFile.getRawLocation().toOSString(),
                            String.valueOf(repFile.getModificationStamp())));
                }
            }

            saveReportListFile(repListFile, repList);
        }
    }

    /**
     * get the link file which link to the report generate doc file.
     * 
     * @param fileName report generate doc file name
     * @return
     */
    public static IFile getLinkFile(String fileName) {
        return ResourceManager.getRootProject().getFile("." + fileName); //$NON-NLS-1$
    }

    /**
     * DOC xqliu Comment method "recordReportFiles".
     * 
     * @param reportFile
     * @param fileName
     * @param filePath
     * @param createTime
     * @return
     * @throws Exception
     */
    public static boolean recordReportFiles(IFile reportFile, String fileName, String filePath, long createTime) throws Exception {
        File reportListFile = getReportListFile(reportFile);
        if (reportListFile == null || !reportListFile.exists()) {
            return false;
        }
        // get report list from file
        List<ReportListParameters> repList = getReportListParameters(reportListFile);
        // add new report file record
        repList.add(buildRepListParams(fileName, filePath, String.valueOf(createTime)));
        // save report list
        saveReportListFile(reportListFile, repList);

        return true;
    }

    /**
     * DOC xqliu Comment method "saveReportListFile".
     * 
     * @param reportListFile
     * @param repList
     * @throws IOException
     * @throws PersistenceException
     */
    public static void saveReportListFile(final File reportListFile, final List<ReportListParameters> repList)
            throws PersistenceException {
        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "saveReportListFile") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    CSVWriter out = FileUtils.createCSVWriter(reportListFile, FileUtils.TEXT_QUAL, FileUtils.ESCAPE_CHAR);

                    ReportListEnum[] values = ReportListEnum.values();
                    String[] temp = new String[values.length];

                    for (int i = 0; i < repList.size() + 1; i++) {
                        if (i == 0) {
                            temp[0] = ReportListEnum.Name.getLiteral();
                            temp[1] = ReportListEnum.Path.getLiteral();
                            temp[2] = ReportListEnum.CreateTime.getLiteral();
                        } else {
                            temp[0] = repList.get(i - 1).name;
                            temp[1] = repList.get(i - 1).path;
                            temp[2] = repList.get(i - 1).createTime;
                        }

                        out.writeNext(temp);
                    }

                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    log.error(e);
                } catch (IOException e) {
                    log.error(e);
                }
            }

        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        repositoryWorkUnit.throwPersistenceExceptionIfAny();
    }

    /**
     * get the report doc folder name from the report's preperty.
     * 
     * @param repProp
     * @return
     */
    public static String getSimpleName(Property repProp) {
        return repProp.getLabel() + "_" + repProp.getVersion(); //$NON-NLS-1$ 
    }

    /**
     * DOC xqliu ReportUtils class global comment. Detailled comment
     */
    public class ReportListParameters {

        private String name;

        private String path;

        private String createTime;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return this.path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getCreateTime() {
            return this.createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public ReportListParameters() {
            name = PluginConstant.EMPTY_STRING;
            path = PluginConstant.EMPTY_STRING;
            createTime = PluginConstant.EMPTY_STRING;
        }
    }

    /**
     * set the relationship of main-sub report.
     * 
     * @return
     */
    public static Map<String, List<String>> getMainSubRepMap() {
        // for this map:key is main report name without vesion;value is a List about sub reports.
        if (mainSubRepMap == null || mainSubRepMap.isEmpty()) {
            mainSubRepMap = new HashMap<String, List<String>>();
            String s01ColumnSub = "s01_column_subreport"; //$NON-NLS-1$
            String b01ColumnSetBasicSubreport1 = "b01_column_set_basic_subreport1"; //$NON-NLS-1$
            String s02MatchSubSummary = "s02_match_sub_summary"; //$NON-NLS-1$
            String s03SubOverview = "s03_sub_overview"; //$NON-NLS-1$

            List<String> cloumnSubLs = new ArrayList<String>();
            cloumnSubLs.add(s01ColumnSub);
            mainSubRepMap.put("b01_column_basic", cloumnSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b02_column_evolution", cloumnSubLs); //$NON-NLS-1$

            List<String> columnSetSubLs = new ArrayList<String>();
            columnSetSubLs.add(b01ColumnSetBasicSubreport1);
            mainSubRepMap.put("b01_column_set_basic", columnSetSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b02_column_set_evolution", columnSetSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b03_column_basic_dq_rules", columnSetSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b04_column_dq_rules_evolution", columnSetSubLs); //$NON-NLS-1$
            mainSubRepMap.put("functionalDependencyBasic", columnSetSubLs); //$NON-NLS-1$
            mainSubRepMap.put("functionalDependencyEvolution", columnSetSubLs); //$NON-NLS-1$

            List<String> overviewSubLs = new ArrayList<String>();
            overviewSubLs.add(s03SubOverview);
            mainSubRepMap.put("b03_overview_basic", overviewSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b04_overview_evolution", overviewSubLs); //$NON-NLS-1$

            List<String> matchSubLs = new ArrayList<String>();
            matchSubLs.add(s02MatchSubSummary);
            mainSubRepMap.put("b05_match_basic", matchSubLs); //$NON-NLS-1$
            mainSubRepMap.put("b06_match_evolution", matchSubLs); //$NON-NLS-1$
        }
        return mainSubRepMap;
    }

    /**
     * get the report generate doc folder.
     * 
     * @param reportFile report IFile
     * @return
     */
    public static IFolder getOutputFolder(IFile reportFile) {
        IFolder reportContainer = (IFolder) reportFile.getParent();
        String fileName = reportFile.getName();
        String simpleName = getSimpleName(fileName);
        if (simpleName == null) {
            log.error("The current report file name: " + reportFile.getFullPath() + " is a illegal name."); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }
        return reportContainer.getFolder(PluginConstant.DOT_STRING + simpleName);
    }

    /**
     * get the report generate doc folder.
     * 
     * @param repNode the report repository node
     * @return
     */
    public static IFolder getOutputFolder(ReportRepNode repNode) {
        return getOutputFolder(RepositoryNodeHelper.getIFile(repNode));
    }

    /**
     * if the report's name changed, need to rename the report generated doc folder also.
     * 
     * @param parentFolder
     * @param oldFolderName
     * @param repItem
     * @throws PersistenceException
     */
    public static void renameReportGeneratedDocFolder(final IFolder parentFolder, final String oldFolderName,
            final TDQReportItem repItem) throws PersistenceException {
        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "renameReportGeneratedDocFolder") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                // refresh the container
                try {
                    parentFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
                    // new report folder name
                    String newFolderName = ReportFileHelper.getSimpleName(repItem.getProperty());
                    // if the report's name changed, need to rename the report generated doc folder also
                    if (!oldFolderName.equals(newFolderName)) {
                        File oldFolder = WorkspaceUtils.ifolderToFile(parentFolder.getFolder(Path
                                .fromPortableString(PluginConstant.DOT_STRING + oldFolderName)));
                        File newFolder = WorkspaceUtils.ifolderToFile(parentFolder.getFolder(Path
                                .fromPortableString(PluginConstant.DOT_STRING + newFolderName)));
                        // create new folder
                        if (!newFolder.exists()) {
                            newFolder.mkdirs();
                        }
                        // copy files from old folder to new folder
                        copyFolder(oldFolder, newFolder);
                        // delete old folder
                        deleteFolder(oldFolder);
                    }
                    parentFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    log.info(e);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        repositoryWorkUnit.throwPersistenceExceptionIfAny();
    }

    /**
     * copy the files under old folder to new folder.
     * 
     * @param oldFolder
     * @param newFolder
     */
    protected static void copyFolder(File oldFolder, File newFolder) {
        if (oldFolder.exists() && oldFolder.isDirectory()) {
            if (!newFolder.exists()) {
                newFolder.mkdirs();
            }
            if (newFolder.exists()) {
                File[] listFiles = oldFolder.listFiles();
                for (File file : listFiles) {
                    if (file.isDirectory() && file.getName().equals(".svn")) { //$NON-NLS-1$
                        continue;
                    }
                    FilesUtils.copyDirectoryWithoutSvnFolder(file, newFolder);
                }
            }
        }
    }

    /**
     * get the link files which link to the Report Generated Doc File.
     * 
     * @param file Report file
     * @return
     */
    public static List<IFile> getRepDocLinkFiles(IFile file) {
        List<IFile> linkFiles = new ArrayList<IFile>();
        if (file != null) {
            try {
                IResource[] reportListFiles = ReportFileHelper.getReportGeneratedDocs(file);
                for (IResource res : reportListFiles) {
                    IFile linkFile = ResourceManager.getRoot().getFile(res.getFullPath());
                    if (linkFile.exists() && linkFile.isLinked()) {
                        linkFiles.add(linkFile);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return linkFiles;
    }

    /**
     * get the link files which link to the Report Generated Doc File.
     * 
     * @param findAllRecycleBinNodes RepositoryNode list(can be any type)
     * @return
     */
    public static List<IFile> getRepGenDocLinkFiles(List<IRepositoryNode> findAllRecycleBinNodes) {
        List<IFile> linkFiles = new ArrayList<IFile>();
        for (IRepositoryNode iNode : findAllRecycleBinNodes) {
            IRepositoryViewObject object = iNode.getObject();
            if (object != null) {
                Property property = object.getProperty();
                if (property != null) {
                    Item item = property.getItem();
                    if (item != null && item instanceof TDQReportItem) {
                        linkFiles.addAll(ReportFileHelper.getRepDocLinkFiles(RepositoryNodeHelper.getIFile(iNode)));
                    }
                }
            }
        }
        return linkFiles;
    }

    /**
     * remove the link files which link to the Report Generated Doc File.
     * 
     * @param repDocLinkFiles
     */
    public static void removeRepDocLinkFiles(List<IFile> repDocLinkFiles) {
        try {
            for (IFile file : repDocLinkFiles) {
                if (file.isLinked()) {
                    file.delete(Boolean.TRUE, new NullProgressMonitor());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * delete the related output folder of report.
     * 
     * @param reportFile
     * @throws PersistenceException
     */
    public static ReturnCode deleteRepOutputFolder(final IFile reportFile) throws PersistenceException {
        final ReturnCode rc = new ReturnCode(Boolean.TRUE);
        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "deleteRepOutputFolder") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                IFolder reportOutputFolder = ReportFileHelper.getOutputFolder(reportFile);
                if (reportOutputFolder != null && reportOutputFolder.exists()) {
                    try {
                        IContainer parent = reportOutputFolder.getParent();
                        // refresh the parent at begin
                        if (parent != null) {
                            parent.refreshLocal(IResource.DEPTH_INFINITE, null);
                        }
                        // delete folder
                        File file = WorkspaceUtils.ifolderToFile(reportOutputFolder);
                        if (file != null && file.exists()) {
                            deleteFolder(file);
                        }
                        // refresh the parent at end
                        if (parent != null) {
                            parent.refreshLocal(IResource.DEPTH_INFINITE, null);
                        }
                    } catch (CoreException e) {
                        log.warn(e);
                    }
                } else {
                    rc.setOk(Boolean.FALSE);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        repositoryWorkUnit.throwPersistenceExceptionIfAny();

        return rc;
    }

    /**
     * get the IFile of the Report.
     * 
     * @param findAllRecycleBinNodes
     * @return
     */
    public static List<IFile> getReportFiles(List<IRepositoryNode> findAllRecycleBinNodes) {
        List<IFile> reportFiles = new ArrayList<IFile>();
        for (IRepositoryNode iNode : findAllRecycleBinNodes) {
            IRepositoryViewObject object = iNode.getObject();
            if (object != null) {
                Property property = object.getProperty();
                if (property != null) {
                    Item item = property.getItem();
                    if (item != null && item instanceof TDQReportItem) {
                        reportFiles.add(RepositoryNodeHelper.getIFile(iNode));
                    }
                }
            }
        }
        return reportFiles;
    }

    /**
     * delete the related output folder of reports.
     * 
     * @param repFiles
     */
    public static void deleteRepOutputFolders(List<IFile> repFiles) {
        for (IFile repFile : repFiles) {
            try {
                deleteRepOutputFolder(repFile);
            } catch (PersistenceException e) {
                log.error(e);
            }
        }
    }

    /**
     * DOC xqliu Comment method "getOriginalOutoutFolderPath".
     * 
     * @param srcFolder
     * @param file
     * @param tempFolderName
     * @return
     */
    public static String getOriginalOutoutFolderPath(File srcFolder, File file, String tempFolderName) {
        String path = srcFolder.getAbsolutePath() + File.separator + file.getName();
        int indexOf = path.indexOf(tempFolderName);
        if (indexOf > 0) {
            path = path.substring(0, indexOf) + path.substring(indexOf + tempFolderName.length() + 1, path.length());
        }
        return path;
    }

    /**
     * DOC xqliu Comment method "getSourceFolder".
     * 
     * @param file
     * @param srcFolder
     * @param tarFolder
     * @return
     */
    public static File getSourceFolder(File file, File srcFolder, File tarFolder) {
        File tarFile = null;
        String absolutePath = file.getAbsolutePath();
        String absolutePath2 = tarFolder.getAbsolutePath();
        if (absolutePath.startsWith(absolutePath2)) {
            String absolutePath3 = srcFolder.getAbsolutePath();
            tarFile = new File(absolutePath3 + absolutePath.subSequence(absolutePath2.length(), absolutePath.length()));
        }
        return tarFile;
    }

    /**
     * DOC xqliu Comment method "moveHiddenFolders".
     * 
     * @param srcFolder
     * @param tarFolder
     */
    public static void moveHiddenFolders(File srcFolder, File tarFolder) {
        if (srcFolder == null || tarFolder == null) {
            return;
        }
        File[] listFiles = srcFolder.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    if (file.getName().startsWith(".")) { //$NON-NLS-1$
                        if (file.getName().equals(".svn")) { //$NON-NLS-1$
                            continue;
                        }
                        FilesUtils.copyDirectoryWithoutSvnFolder(file, tarFolder);
                    } else {
                        moveHiddenFolders(file, getTargetFile(file, srcFolder, tarFolder));
                    }
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "getTargetFile".
     * 
     * @param file
     * @param srcFolder
     * @param tarFolder
     * @return
     */
    public static File getTargetFile(File file, File srcFolder, File tarFolder) {
        File tarFile = null;
        String absolutePath = file.getAbsolutePath();
        String absolutePath2 = srcFolder.getAbsolutePath();
        if (absolutePath.startsWith(absolutePath2)) {
            String absolutePath3 = tarFolder.getAbsolutePath();
            tarFile = new File(absolutePath3 + absolutePath.subSequence(absolutePath2.length(), absolutePath.length()));
        }
        return tarFile;
    }

    /**
     * DOC xqliu Comment method "buildRepListParams".
     * 
     * @param name
     * @param path
     * @param createTime
     * @return
     */
    public static ReportListParameters buildRepListParams(String name, String path, String createTime) {
        ReportListParameters repListParameters = new ReportFileHelper().new ReportListParameters();
        repListParameters.setName(name);
        repListParameters.setPath(path);
        repListParameters.setCreateTime(createTime);
        return repListParameters;
    }

    /**
     * DOC xqliu Comment method "getReportListParameters".
     * 
     * @param reportListFile
     * @return
     * @throws IOException
     */
    public static List<ReportListParameters> getReportListParameters(File reportListFile) {
        List<ReportListParameters> repList = new ArrayList<ReportListParameters>();

        if (reportListFile != null && reportListFile.exists()) {
            try {
                CSVReader reader = FileUtils.createCSVReader(reportListFile);
                reader.setSkipEmptyRecords(true);
                reader.setEscapeChar('\\');
                reader.readHeaders();
                while (reader.readNext()) {
                    repList.add(buildRepListParams(reader.get(ReportListEnum.Name.getLiteral()),
                            reader.get(ReportListEnum.Path.getLiteral()), reader.get(ReportListEnum.CreateTime.getLiteral())));
                }
                reader.close();
            } catch (FileNotFoundException e) {
                log.error(e);
            } catch (IllegalArgumentException e) {
                log.error(e);
            } catch (IOException e) {
                log.error(e);
            }
        }
        return repList;
    }

    /**
     * remove the extension of full name to get the simple name of a report file.
     * 
     * @param reportFileName
     * @return
     */
    public static String getSimpleName(String reportFileName) {
        int indexOf = reportFileName.lastIndexOf(PluginConstant.DOT_STRING);
        String simpleName = PluginConstant.EMPTY_STRING;
        if (indexOf != -1) {
            simpleName = reportFileName.substring(0, indexOf);
        } else {
            simpleName = null;
        }
        return simpleName;
    }

    /**
     * return the .report.list file of the report.
     * 
     * @param reportFile
     * @return
     */
    public static File reportListFile(IFile reportFile) {
        String reportFileName = reportFile.getName();
        String simpleName = getSimpleName(reportFileName);
        return new File(ReportHelper.getOutputFolderNameDefault((IFolder) reportFile.getParent(), simpleName) + File.separator
                + REPORT_LIST);
    }

    /**
     * return the latest report.
     * 
     * @param reportFile
     * @return ReportListParameters
     */
    public static ReportListParameters getTheLatestReport(IFile reportFile) {
        ReportListParameters lastest = new ReportFileHelper().new ReportListParameters();

        File reportListFile = ReportFileHelper.reportListFile(reportFile);
        List<ReportListParameters> reportListParameters = ReportFileHelper.getReportListParameters(reportListFile);

        for (ReportListParameters repParam : reportListParameters) {
            if (repParam.getCreateTime().compareTo(lastest.getCreateTime()) > 0) {
                lastest = repParam;
            }
        }
        return lastest;
    }

    /**
     * get the .report.list file for the report, if it doesn't exist, create it.
     * 
     * @param reportFile the IFile of report
     * @return
     */
    public static File getReportListFile(IFile reportFile) {
        File file = reportListFile(reportFile);
        if (!file.exists()) {
            try {
                createReportListFile(reportFile);
            } catch (PersistenceException e) {
                log.error(e);
            }
        }
        return file;
    }

    /**
     * create the hidden Generated Doc Folder, create the .report.list file.
     * 
     * @param reportFile the report IFile
     * @return
     * @throws PersistenceException
     */
    public static boolean createReportListFile(final IFile reportFile) throws PersistenceException {
        final File file = reportListFile(reportFile);
        if (!file.exists()) {
            RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                    "initReportGeneratedDocFolder") { //$NON-NLS-1$

                @Override
                protected void run() throws LoginException, PersistenceException {
                    File parentFile = file.getParentFile();
                    if (parentFile != null) {
                        try {
                            if (!parentFile.exists()) {
                                if (parentFile.mkdirs()) {
                                    file.createNewFile();
                                }
                            } else {
                                file.createNewFile();
                            }
                        } catch (IOException e) {
                            log.warn(e);
                        }
                    }
                    try {
                        reportFile.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (CoreException e) {
                        log.warn(e);
                    }
                }
            };
            repositoryWorkUnit.setAvoidUnloadResources(true);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
            repositoryWorkUnit.throwPersistenceExceptionIfAny();
        }
        return file.exists();
    }

    /**
     * get the generated doc file list.
     * 
     * @param reportFile the report IFile
     * @return never null
     */
    public static IResource[] getReportGeneratedDocs(IFile reportFile) {
        List<IResource> repFileList = new ArrayList<IResource>();

        File reportListFile = getReportListFile(reportFile);
        if (reportListFile != null && reportListFile.exists() && reportListFile.isFile()) {
            List<ReportListParameters> repList = getReportListParameters(reportListFile);

            for (ReportListParameters rep : repList) {
                String path = rep.getPath();
                if (path.startsWith("..")) { //$NON-NLS-1$
                    path = WorkspaceUtils.ifolderToFile(getOutputFolder(reportFile)).getAbsolutePath() + File.separator
                            + path.substring(2, path.length());
                }
                File file = new File(path);
                if (file.exists() && !file.getName().equals(REPORT_LIST)) {
                    IFile iFile = getLinkFile(file.getName());
                    try {
                        iFile.createLink(file.getAbsoluteFile().toURI(), IResource.REPLACE, null);
                    } catch (CoreException e) {
                        log.info(e);
                    }
                    repFileList.add(iFile);
                }
            }
        }

        return repFileList.toArray(new IResource[repFileList.size()]);
    }

    /**
     * delete the folder under TDQ_Data Profiling/Reports from the project and the disk, don't refresh the parent
     * folder, the caller should refresh the parent by hand to change the svn info.
     * 
     * @param folder
     */
    private static void deleteFolder(File folder) {
        // delete the folder from the project
        try {
            IFolder reportsFolder = ResourceManager.getReportsFolder();
            IFolder tempFolder = WorkspaceUtils.fileToIFolder(folder);
            if (tempFolder != null && tempFolder.exists()) {

                IProject fsProject = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject());

                String completePath = new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.TDQ_REPORT_ELEMENT))
                        .append(tempFolder.getFullPath().makeRelativeTo(reportsFolder.getFullPath())).toString();

                // Getting the folder :
                IFolder deleteFolder = ResourceUtils.getFolder(fsProject, completePath, true);
                // delete folder and files from workbench
                for (IResource subResource : deleteFolder.members()) {
                    if (subResource.getType() == IResource.FILE) {
                        subResource.delete(true, null);
                    }
                }
                deleteFolder.delete(true, null);
            }

            // delete folder from the disk
            if (folder.exists()) {
                FilesUtils.deleteFile(folder, Boolean.TRUE);
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        } catch (CoreException e) {
            log.error(e, e);
        }

    }

    /**
     * DOC xqliu Comment method "moveReportGeneratedDocFolder".
     * 
     * @param srcFolder
     * @param tarFolder
     * @throws PersistenceException
     */
    public static void moveReportGeneratedDocFolder(final File srcFolder, final File tarFolder) throws PersistenceException {
        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "moveReportGeneratedDocFolder") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    ResourceManager.getReportsFolder().refreshLocal(IResource.DEPTH_INFINITE, null);
                    if (srcFolder.exists() && srcFolder.isDirectory()) {
                        if (tarFolder.exists() && tarFolder.isDirectory()) {
                            FilesUtils.copyDirectoryWithoutSvnFolder(srcFolder, tarFolder);
                        }
                        deleteFolder(srcFolder);
                    }
                    ResourceManager.getReportsFolder().refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    log.info(e);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        repositoryWorkUnit.throwPersistenceExceptionIfAny();
    }
}
