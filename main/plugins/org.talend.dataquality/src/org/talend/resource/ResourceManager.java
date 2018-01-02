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
package org.talend.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.model.bridge.ReponsitoryContextBridge;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class ResourceManager {

    private static final Logger log = Logger.getLogger(ResourceManager.class);

    private ResourceManager() {
    }

    /**
     * DOC bzhou Comment method "getRoot".
     * 
     * @return
     */
    public static IWorkspaceRoot getRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    /**
     * DOC bZhou Comment method "getRootProject".
     * 
     * @return
     */

    public static IProject getRootProject() {
        return ReponsitoryContextBridge.getRootProject();
    }

    public static IProject getProject(String projectName) {
        return ReponsitoryContextBridge.findProject(projectName);
    }

    /**
     * DOC bZhou Comment method "getRootProjectName".
     * 
     * @return
     */
    public static String getRootProjectName() {
        return ReponsitoryContextBridge.getProjectName();
    }

    // ADD msjian 2011-8-5 TDQ-3165: get the Project Description
    /**
     * DOC msjian Comment method "getRootProjectDescription".
     * 
     * @return
     */
    public static String getRootProjectDescription() {
        return ReponsitoryContextBridge.getProjectDescription();
    }

    /**
     * DOC bZhou Comment method "getDataProfilingFolder".
     * 
     * @return
     */
    public static IFolder getDataProfilingFolder() {
        return getOneFolder(EResourceConstant.DATA_PROFILING);
    }

    /**
     * get Temp MapDB Path
     * 
     * @return
     */
    public static IPath getTempMapDBFolder() {
        IPath worskpacePath = getWorskpacePath();

        IPath TempMapDBFolder = worskpacePath.append(ReponsitoryContextBridge.getProjectName()).append(
                EResourceConstant.TEMP_MAPDB.getPath());
        if (!Platform.isRunning()) {
            return TempMapDBFolder.append("jobApplication");//$NON-NLS-1$
        }
        return TempMapDBFolder;
    }

    public static IPath getWorskpacePath() {
        if (Platform.isRunning()) {
            return getRootFolderLocation();
        } else {
            String talendProjctPathFromReportApplication = System.getProperty("talend.project.path");//$NON-NLS-1$
            return talendProjctPathFromReportApplication == null ? new Path("") : new Path(talendProjctPathFromReportApplication)
                    .removeLastSegments(1);
        }
    }

    /**
     * Get the path of Map DB file.
     * 
     * @return
     */
    public static String getMapDBFilePath() {
        return getTempMapDBFolder().toOSString();
    }

    /**
     * Get the name of Map DB file.
     * 
     * @param indicator we should find the name of mapDB file which should be the uuid of analysis
     * 
     * @return the name of mapDB file
     */
    public static String getMapDBFileName(Indicator indicator) {
        String analysisUUID = AnalysisHelper.getAnalysisUUID(indicator);
        if (analysisUUID == null) {
            log.error(Messages.getString("ResourceManager.CanNotGetAnalysis")); //$NON-NLS-1$
        }
        return analysisUUID;
    }

    /**
     * Get the catalog name of current indicator.
     * 
     * @param indicator we should find the name of analysis,analysisElement,indicatorDefinition.So that it should be
     * used by some one analysis
     * @param dbName The name of mapDB catalog
     * 
     * ColumnAnalysis like(../analysisName/columnName/indicatorName) others like(../analysisName/indicatorName)
     * @return
     */
    public static String getMapDBCatalogName(Indicator indicator, String dbName) {
        String mapDBCatalogPrefix = getMapDBCatalogName(indicator);
        String mapDBCatalogName = mapDBCatalogPrefix + dbName;
        return mapDBCatalogName;

    }

    /**
     * Get the catalog name of current indicator.
     * 
     * @param indicator we should find the name of analysis,analysisElement,indicatorDefinition.So that it should be
     * used by some one analysis
     * 
     * 
     * ColumnAnalysis like(../analysisName/columnName/indicatorName) others like(../analysisName/indicatorName)
     * @return
     */
    public static String getMapDBCatalogName(Indicator indicator) {
        Analysis analysis = AnalysisHelper.getAnalysis(indicator);
        String analysisUUID = null;
        String indicatorUUID = ResourceHelper.getUUID(indicator);
        String modelElementName = Path.EMPTY.toString();
        if (analysis == null) {
            log.error(Messages.getString("ResourceManager.CanNotGetAnalysis")); //$NON-NLS-1$
        } else {
            analysisUUID = ResourceHelper.getUUID(analysis);
            if (AnalysisType.MULTIPLE_COLUMN.equals(analysis.getParameters().getAnalysisType())) {
                modelElementName = indicator.getAnalyzedElement().getName();
                //TDQ-12795, only for file delimited connection, the name of the column maybe null, but the label always has value.
                if(modelElementName==null){
                    if(indicator.getAnalyzedElement() instanceof MetadataColumn){
                        if(indicator.getAnalyzedElement().eIsProxy()){
                            modelElementName =Path.EMPTY.toString();
                        }else{
                            modelElementName = ((MetadataColumn)indicator.getAnalyzedElement()).getLabel();
                        }
                    }else{
                        modelElementName =Path.EMPTY.toString();
                        log.error(Messages.getString("ResourceManager.CanNotGetColunName"));
                    }
                }
            }
        }
        return getTempMapDBFolder().append(analysisUUID).append(modelElementName).append(indicatorUUID).append("_").toString();
    }

    /**
     * DOC bZhou Comment method "getLibrariesFolder".
     * 
     * @return
     */
    public static IFolder getLibrariesFolder() {
        return getOneFolder(EResourceConstant.LIBRARIES);
    }

    /**
     * DOC bZhou Comment method "getMetadataFolder".
     * 
     * @return
     */
    public static IFolder getMetadataFolder() {
        return getOneFolder(EResourceConstant.METADATA);
    }

    /**
     * DOC bZhou Comment method "getAnalysisFolder".
     * 
     * @return
     */
    public static IFolder getAnalysisFolder() {
        return getOneFolder(EResourceConstant.ANALYSIS);
    }

    /**
     * DOC bZhou Comment method "getReportsFolder".
     * 
     * @return
     */
    public static IFolder getReportsFolder() {
        return getOneFolder(EResourceConstant.REPORTS);
    }

    /**
     * DOC bZhou Comment method "getReportingDBFolder".
     * 
     * @return
     */
    public static IFolder getReportDBFolder() {
        return getOneFolder(EResourceConstant.REPORTING_DB);
    }

    /**
     * DOC bZhou Comment method "getRulesFolder".
     * 
     * @return
     */
    public static IFolder getRulesFolder() {
        return getOneFolder(EResourceConstant.RULES);
    }

    /**
     * DOC bZhou Comment method "getRulesSQLFolder".
     * 
     * @return
     */
    public static IFolder getRulesSQLFolder() {
        return getOneFolder(EResourceConstant.RULES_SQL);
    }

    /**
     * 
     * DOC klliu Comment method "getRulesParserFolder".
     * 
     * @return
     */
    public static IFolder getRulesParserFolder() {
        return getOneFolder(EResourceConstant.RULES_PARSER);
    }

    /**
     * 
     * @return
     */
    public static IFolder getRulesMatcherFolder() {
        return getOneFolder(EResourceConstant.RULES_MATCHER);
    }

    /**
     * DOC bZhou Comment method "getPatternFolder".
     * 
     * @return
     */
    public static IFolder getPatternFolder() {
        return getOneFolder(EResourceConstant.PATTERNS);
    }

    /**
     * DOC bZhou Comment method "getPatternSQLFolder".
     * 
     * @return
     */
    public static IFolder getPatternSQLFolder() {
        return getOneFolder(EResourceConstant.PATTERN_SQL);
    }

    /**
     * DOC bZhou Comment method "getPatternRegexFolder".
     * 
     * @return
     */
    public static IFolder getPatternRegexFolder() {
        return getOneFolder(EResourceConstant.PATTERN_REGEX);
    }

    /**
     * DOC bZhou Comment method "getIndicatorFolder".
     * 
     * @return
     */
    public static IFolder getIndicatorFolder() {
        return getOneFolder(EResourceConstant.INDICATORS);
    }

    /**
     * DOC xqliu Comment method "getSystemIndicatorFolder".
     * 
     * @return
     */
    public static IFolder getSystemIndicatorFolder() {
        return getOneFolder(EResourceConstant.SYSTEM_INDICATORS);
    }

    public static IFolder getSysIndicatorPatternFrequencyStatisticsFolder() {
        return getOneFolder(EResourceConstant.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS);
    }

    /**
     * DOC bZhou Comment method "getUDIFolder".
     * 
     * @return
     */
    public static IFolder getUDIFolder() {
        return getOneFolder(EResourceConstant.USER_DEFINED_INDICATORS);
    }

    /**
     * 
     * zshen Comment method "getUDIJarFolder".
     * 
     * @return
     */
    public static IFolder getUDIJarFolder() {
        return getOneFolder(EResourceConstant.USER_DEFINED_INDICATORS_LIB);
    }

    /**
     * DOC bZhou Comment method "getJRXMLFolder".
     * 
     * @return
     */
    public static IFolder getJRXMLFolder() {
        return getOneFolder(EResourceConstant.JRXML_TEMPLATE);
    }

    /**
     * DOC bZhou Comment method "getConnectionFolder".
     * 
     * @return
     */
    public static IFolder getConnectionFolder() {
        return getOneFolder(EResourceConstant.DB_CONNECTIONS);
    }

    /**
     * DOC bZhou Comment method "getMDMConnectionFolder".
     * 
     * @return
     */
    public static IFolder getMDMConnectionFolder() {
        return getOneFolder(EResourceConstant.MDM_CONNECTIONS);
    }

    public static IFolder getHadoopClusterFolder() {
        return getOneFolder(EResourceConstant.HADOOP_CLUSTER);
    }

    /**
     * DOC bZhou Comment method "getExchangeFolder".
     * 
     * @return
     */
    public static IFolder getExchangeFolder() {
        return getOneFolder(EResourceConstant.EXCHANGE);
    }

    /**
     * DOC bZhou Comment method "getSourceFileFolder".
     * 
     * @return
     */
    public static IFolder getSourceFileFolder() {
        return getOneFolder(EResourceConstant.SOURCE_FILES);
    }

    /**
     * DOC bZhou Comment method "isDataProfilingFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isDataProfilingFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.DATA_PROFILING);
    }

    /**
     * DOC bZhou Comment method "isLibrariesFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isLibrariesFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.LIBRARIES);
    }

    /**
     * DOC bZhou Comment method "isMetadataFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isMetadataFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.METADATA);
    }

    /**
     * DOC bZhou Comment method "isAnalysisFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isAnalysisFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.ANALYSIS);
    }

    /**
     * DOC bZhou Comment method "isReportsFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isReportsFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.REPORTS);
    }

    /**
     * DOC bZhou Comment method "isReportDBFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isReportDBFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.REPORTING_DB);
    }

    /**
     * DOC bZhou Comment method "isRulesFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isRulesFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.RULES);
    }

    /**
     * DOC bZhou Comment method "isRulesSQLFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isRulesSQLFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.RULES_SQL);
    }

    /**
     * DOC bZhou Comment method "isPatternFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isPatternFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.PATTERNS);
    }

    /**
     * DOC bZhou Comment method "isPatternSQLFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isPatternSQLFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.PATTERN_SQL);
    }

    /**
     * DOC bZhou Comment method "isPatternRegexFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isPatternRegexFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.PATTERN_REGEX);
    }

    /**
     * DOC bZhou Comment method "isIndicatorFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isIndicatorFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.INDICATORS);
    }

    /**
     * DOC bZhou Comment method "isUDIFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isUDIFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.USER_DEFINED_INDICATORS);
    }

    /**
     * DOC bZhou Comment method "isJRXMLFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isJRXMLFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.JRXML_TEMPLATE);
    }

    /**
     * DOC bZhou Comment method "isConnectionFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isDBConnectionFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.DB_CONNECTIONS);
    }

    /**
     * DOC bZhou Comment method "isExchangeFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isExchangeFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.EXCHANGE);
    }

    /**
     * DOC bZhou Comment method "isSourceFileFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isSourceFileFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.SOURCE_FILES);
    }

    /**
     * DOC bZhou Comment method "isFolder".
     * 
     * @param resource
     * @param constant
     * @return
     */
    public static boolean isOneFolder(IResource resource, EResourceConstant constant) {
        assert resource != null;

        String path = resource.getProjectRelativePath().toString();
        String compPath = constant.getPath();

        return path.equalsIgnoreCase(compPath);
    }

    /**
     * DOC bZhou Comment method "getFolder".
     * 
     * @param name
     * @return
     */
    public static IFolder getOneFolder(EResourceConstant constant) {
        assert constant != null;

        return getRootProject().getFolder(new Path(constant.getPath()));
    }

    /**
     * DOC xqliu Comment method "getRootFolderLocation".
     * 
     * @return
     */
    public static IPath getRootFolderLocation() {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation();
    }

    /**
     * get the product root folder location.
     * 
     * @return
     */
    public static IPath getProductFolderLocation() {
        return getRootFolderLocation().removeLastSegments(1);
    }

    /**
     * 
     * DOC qiongli Comment method "isFileDelimitedFolder".
     * 
     * @param resource
     * @return
     */
    public static boolean isFileDelimitedFolder(IResource resource) {
        return isOneFolder(resource, EResourceConstant.FILEDELIMITED);
    }

    /**
     * 
     * DOC qiongli Comment method "getFileDelimitedFolder".
     * 
     * @return
     */
    public static IFolder getFileDelimitedFolder() {
        return getOneFolder(EResourceConstant.FILEDELIMITED);
    }

    /**
     * get the absolute path relative to the current project.
     * 
     * @param path
     * @return never null
     */
    public static String getAbsolutePathRelative2Project(String path) {
        if (path != null && !"".equals(path)) { //$NON-NLS-1$
            String relative2ProjectPath = path;
            String projectAbsolutePath = getRootProject().getLocation().toString();
            if (path.startsWith(projectAbsolutePath)) {
                relative2ProjectPath = path;
            } else {
                relative2ProjectPath = getRootProject().getLocation().append(path).toOSString();
            }
            return relative2ProjectPath;
        }
        return ""; //$NON-NLS-1$
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toUpperCase().indexOf("LINUX") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().indexOf("WIN") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWinXP() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS XP") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWin7() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS 7") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toUpperCase().indexOf("MAC") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * remove the jrxml Resource from ResourceSet, should call this method only when call MatchService.doMatch().
     * 
     * @param resourceSet
     * @return Jrxml Resource List
     */
    public static List<Resource> removeJrxmls(ResourceSet resourceSet) {
        List<Resource> resToRemove = new ArrayList<Resource>();
        for (Resource res : resourceSet.getResources()) {
            if (isJrxml(res)) {
                resToRemove.add(res);
            }
        }
        resourceSet.getResources().removeAll(resToRemove);
        return resToRemove;
    }

    /**
     * if the Resource is Jrxml return true else return false.
     * 
     * @param res
     * @return
     */
    public static boolean isJrxml(Resource res) {
        boolean isJrxml = false;
        if (res != null) {
            Object object = EcoreUtil.getObjectByType(res.getContents(), PropertiesPackage.eINSTANCE.getProperty());
            if (object != null) {
                Item item = ((Property) object).getItem();
                if (item != null) {
                    isJrxml = item instanceof TDQJrxmlItem;
                }
            }
        }
        return isJrxml;
    }
}
