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
package org.talend.dataquality.helpers;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.ReportsFactory;
import org.talend.dataquality.reports.TdReport;
import org.talend.utils.properties.PropertiesLoader;
import org.talend.utils.properties.TypedProperties;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class ReportHelper {

    public static final String HSQL_DEFAULT_DBTYPE = "HSQL"; //$NON-NLS-1$

    public static final String DOT_MARK = "."; //$NON-NLS-1$

    // ~ADD mzhao 2009-02-05

    public static final String BASIC = "Basic"; // TODO externalize

    public static final String EVOLUTION = "Evolution"; // TODO externalize

    public static final String USER_DEFINED = "User defined"; // TODO externalize

    private static final String DEFAULT = "Default"; //$NON-NLS-1$

    private static final TypedProperties PROPS = PropertiesLoader
            .getProperties(ReportHelper.class, "predefined_jrxml.properties"); //$NON-NLS-1$

    /**
     * The report types.
     * 
     * MOD mzhao 2009-02-19 MOD mzhao 2009-04-15 jrxml template files changed its location.
     */
    public static enum ReportType {

        // Analysis: multi column
        BASIC_MUTICOLUMN(BASIC, PROPS.getProperty("BASIC_MUTICOLUMN")), //$NON-NLS-1$
        EVOLUTION_MUTICOLUMN(EVOLUTION, PROPS.getProperty("EVOLUTION_MUTICOLUMN")), //$NON-NLS-1$
        // Analysis: connection
        BASIC_CONNECTION(BASIC, PROPS.getProperty("BASIC_CONNECTION")), //$NON-NLS-1$
        BASIC_CONNECTION_ORACLE(BASIC, PROPS.getProperty("BASIC_CONNECTION_ORACLE")), //$NON-NLS-1$
        EVOLUTION_CONNECTION(EVOLUTION, PROPS.getProperty("EVOLUTION_CONNECTION")), //$NON-NLS-1$
        EVOLUTION_CONNECTION_ORACLE(EVOLUTION, PROPS.getProperty("EVOLUTION_CONNECTION_ORACLE")), //$NON-NLS-1$
        // EVOLUTION_CONNECTION(BASIC, "/reports/column/report_04.jrxml"),
        // Analysis: schema
        BASIC_SCHEMA(BASIC, PROPS.getProperty("BASIC_SCHEMA")), //$NON-NLS-1$
        BASIC_SCHEMA_ORACLE(BASIC, PROPS.getProperty("BASIC_SCHEMA_ORACLE")), //$NON-NLS-1$

        EVOLUTION_SCHEMA(EVOLUTION, PROPS.getProperty("EVOLUTION_SCHEMA")), //$NON-NLS-1$
        EVOLUTION_SCHEMA_ORACLE(EVOLUTION, PROPS.getProperty("EVOLUTION_SCHEMA_ORACLE")), //$NON-NLS-1$
        // TODO assign type to specific jrxml
        // Analysis: catalog
        BASIC_CATALOG(BASIC, PROPS.getProperty("BASIC_CATALOG")), //$NON-NLS-1$
        BASIC_CATALOG_ORACLE(BASIC, PROPS.getProperty("BASIC_CATALOG_ORACLE")), //$NON-NLS-1$
        EVOLUTION_CATALOG(EVOLUTION, PROPS.getProperty("EVOLUTION_CATALOG")), //$NON-NLS-1$
        EVOLUTION_CATALOG_ORACLE(EVOLUTION, PROPS.getProperty("EVOLUTION_CATALOG_ORACLE")), //$NON-NLS-1$
        // Analysis: table
        BASIC_TABLE(BASIC, PROPS.getProperty("BASIC_TABLE")), //$NON-NLS-1$
        EVOLUTION_TABLE(EVOLUTION, PROPS.getProperty("EVOLUTION_TABLE")), //$NON-NLS-1$
        // Analysis: column comparison
        BASIC_COLUMNS_COMPARISON(BASIC, PROPS.getProperty("BASIC_COLUMNS_COMPARISON")), //$NON-NLS-1$
        EVOLUTION_COLUMNS_COMPARISON(EVOLUTION, PROPS.getProperty("EVOLUTION_COLUMNS_COMPARISON")), //$NON-NLS-1$
        // Analysis: column correation
        BASIC_COLUMNS_CORRELATION(BASIC, PROPS.getProperty("BASIC_COLUMNS_CORRELATION")), //$NON-NLS-1$
        EVOLUTION_COLUMNS_CORRELATION(EVOLUTION, PROPS.getProperty("EVOLUTION_COLUMNS_CORRELATION")), //$NON-NLS-1$

        // Analysis: Functional Dependency analysis.
        BASIC_TABLE_FUNCTIONAL_DEPENDENCY(BASIC, PROPS.getProperty("BASIC_TABLE_FUNCTIONAL_DEPENDENCY")), //$NON-NLS-1$
        EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY(EVOLUTION, PROPS.getProperty("EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY")), //$NON-NLS-1$

        // Analysis: Column set analysis
        BASIC_COLUMN_SET(BASIC, PROPS.getProperty("BASIC_COLUMN_SET")), //$NON-NLS-1$
        EVOLUTION_COLUMN_SET(EVOLUTION, PROPS.getProperty("EVOLUTION_COLUMN_SET")), //$NON-NLS-1$        

        // Analysis: match analysis
        BASIC_MATCH_ANALYSIS(BASIC, PROPS.getProperty("BASIC_MATCH_ANALYSIS")), //$NON-NLS-1$
        EVOLUTION_MATCH_ANALYSIS(EVOLUTION, PROPS.getProperty("EVOLUTION_MATCH_ANALYSIS")), //$NON-NLS-1$      

        USER_MADE(USER_DEFINED, null); // for the user to set his own file path

        private String label;

        /**
         * Getter for label.
         * 
         * @return the label
         */
        public String getLabel() {
            return this.label;
        }

        /**
         * Getter for jrxmlFilename.
         * 
         * @return the jrxmlFilename
         */
        public String getJrxmlFilename() {
            return this.jrxmlFilename;
        }

        private String jrxmlFilename;

        /**
         * Sets the jrxmlFilename.
         * 
         * @param jrxmlFilename the jrxmlFilename to set
         */
        public void setJrxmlFilename(String jrxmlFilename) {
            this.jrxmlFilename = jrxmlFilename;
        }

        ReportType(String lab, String filename) {
            this.label = lab;
            this.jrxmlFilename = filename;
        }

        public static List<String> getLabels() {
            List<String> list = new ArrayList<String>();
            for (ReportType t : ReportType.values()) {
                if (!list.contains(t.getLabel())) {
                    list.add(t.getLabel());
                }
            }
            return list;
        }

        /**
         * DOC qzhang Comment method "getReportType". MOD mzhao 2009-02-19
         * 
         * @param text
         * @return
         */
        public static ReportType getReportType(AnalysisType anaType, String text) {
            if (USER_MADE.getLabel().equals(text)) {
                return USER_MADE;
            }
            // Multi column
            if (anaType == null) {
                return null;
            }
            if (anaType.getName().equals(AnalysisType.MULTIPLE_COLUMN.getName())) {
                if (BASIC_MUTICOLUMN.getLabel().equals(text)) {
                    return BASIC_MUTICOLUMN;
                } else if (EVOLUTION_MUTICOLUMN.getLabel().equals(text)) {
                    return EVOLUTION_MUTICOLUMN;
                }
            } else if (anaType.getName().equals(AnalysisType.CONNECTION.getName())) {
                if (BASIC_CONNECTION.getLabel().equals(text)) {
                    return BASIC_CONNECTION;
                } else if (EVOLUTION_CONNECTION.getLabel().equals(text)) {
                    return EVOLUTION_CONNECTION;
                }
            } else if (anaType.getName().equals(AnalysisType.SCHEMA.getName())) {
                if (BASIC_SCHEMA.getLabel().equals(text)) {
                    return BASIC_SCHEMA;
                } else if (EVOLUTION_SCHEMA.getLabel().equals(text)) {
                    return EVOLUTION_SCHEMA;
                }
            } else if (anaType.getName().equals(AnalysisType.CATALOG.getName())) {
                if (BASIC_CATALOG.getLabel().equals(text)) {
                    return BASIC_CATALOG;
                } else if (EVOLUTION_CATALOG.getLabel().equals(text)) {
                    return EVOLUTION_CATALOG;
                }
            } else if (anaType.getName().equals(AnalysisType.TABLE.getName())) {
                if (BASIC_TABLE.getLabel().equals(text)) {
                    return BASIC_TABLE;
                } else if (EVOLUTION_TABLE.getLabel().equals(text)) {
                    return EVOLUTION_TABLE;
                }
            } else if (anaType.getName().equals(AnalysisType.COLUMNS_COMPARISON.getName())) {
                if (BASIC_COLUMNS_COMPARISON.getLabel().equals(text)) {
                    return BASIC_COLUMNS_COMPARISON;
                } else if (EVOLUTION_COLUMNS_COMPARISON.getLabel().equals(text)) {
                    return EVOLUTION_COLUMNS_COMPARISON;
                }
            } else if (anaType.getName().equals(AnalysisType.COLUMN_CORRELATION.getName())) {
                if (BASIC_COLUMNS_CORRELATION.getLabel().equals(text)) {
                    return BASIC_COLUMNS_CORRELATION;
                } else if (EVOLUTION_COLUMNS_CORRELATION.getLabel().equals(text)) {
                    return EVOLUTION_COLUMNS_CORRELATION;
                }
            } else if (anaType.getName().equals(AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY.getName())) {
                if (BASIC_TABLE_FUNCTIONAL_DEPENDENCY.getLabel().equals(text)) {
                    return BASIC_TABLE_FUNCTIONAL_DEPENDENCY;
                } else if (EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY.getLabel().equals(text)) {
                    return EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY;
                }
            } else if (anaType.getName().equals(AnalysisType.COLUMN_SET.getName())) {
                // MOD mzhao 10706, column set analysis
                if (BASIC_COLUMN_SET.getLabel().equals(text)) {
                    return BASIC_COLUMN_SET;
                } else if (EVOLUTION_COLUMN_SET.getLabel().equals(text)) {
                    return EVOLUTION_COLUMN_SET;
                }
            } else if (anaType.getName().equals(AnalysisType.MATCH_ANALYSIS.getName())) {
                // ADD msjian TDQ-7692, match analysis
                if (BASIC_MATCH_ANALYSIS.getLabel().equals(text)) {
                    return BASIC_MATCH_ANALYSIS;
                } else if (EVOLUTION_MATCH_ANALYSIS.getLabel().equals(text)) {
                    return EVOLUTION_MATCH_ANALYSIS;
                }
            }

            return null;
        }

        // public static ReportType getReportType(AnalysisType anaType, String text, String dbType) {
        // // MOD klliu 16372 add Basic and evolution to fit oracle type
        // if (USER_MADE.getLabel().equals(text)) {
        // return USER_MADE;
        // }
        // // Multi column
        // if (anaType.getName().equals(AnalysisType.MULTIPLE_COLUMN.getName())) {
        // if (BASIC_MUTICOLUMN.getLabel().equals(text)) {
        // return BASIC_MUTICOLUMN;
        // } else if (EVOLUTION_MUTICOLUMN.getLabel().equals(text)) {
        // return EVOLUTION_MUTICOLUMN;
        // }
        // } else if (anaType.getName().equals(AnalysisType.CONNECTION.getName())) {
        // if (StringUtils.equalsIgnoreCase(dbType, "Oracle with SID")) {
        // if (BASIC_CONNECTION_ORACLE.getLabel().equals(text)) {
        // return BASIC_CONNECTION_ORACLE;
        // } else if (EVOLUTION_CONNECTION_ORACLE.getLabel().equals(text)) {
        // return EVOLUTION_CONNECTION_ORACLE;
        // }
        // } else {
        // if (BASIC_CONNECTION.getLabel().equals(text)) {
        // return BASIC_CONNECTION;
        // } else if (EVOLUTION_CONNECTION.getLabel().equals(text)) {
        // return EVOLUTION_CONNECTION;
        // }
        // }
        // } else if (anaType.getName().equals(AnalysisType.SCHEMA.getName())) {
        // if (StringUtils.equalsIgnoreCase(dbType, "Oracle with SID")) {
        // if (BASIC_SCHEMA_ORACLE.getLabel().equals(text)) {
        // return BASIC_SCHEMA_ORACLE;
        // } else if (EVOLUTION_SCHEMA_ORACLE.getLabel().equals(text)) {
        // return EVOLUTION_SCHEMA_ORACLE;
        // }
        // } else {
        // if (BASIC_SCHEMA.getLabel().equals(text)) {
        // return BASIC_SCHEMA;
        // } else if (EVOLUTION_SCHEMA.getLabel().equals(text)) {
        // return EVOLUTION_SCHEMA;
        // }
        // }
        // } else if (anaType.getName().equals(AnalysisType.CATALOG.getName())) {
        // if (StringUtils.equalsIgnoreCase(dbType, "Oracle with SID")) {
        // if (BASIC_CATALOG_ORACLE.getLabel().equals(text)) {
        // return BASIC_CATALOG_ORACLE;
        // } else if (EVOLUTION_CATALOG_ORACLE.getLabel().equals(text)) {
        // return EVOLUTION_CATALOG_ORACLE;
        // }
        // } else {
        // if (BASIC_CATALOG.getLabel().equals(text)) {
        // return BASIC_CATALOG;
        // } else if (EVOLUTION_CATALOG.getLabel().equals(text)) {
        // return EVOLUTION_CATALOG;
        // }
        // }
        // } else if (anaType.getName().equals(AnalysisType.TABLE.getName())) {
        // if (BASIC_TABLE.getLabel().equals(text)) {
        // return BASIC_TABLE;
        // } else if (EVOLUTION_TABLE.getLabel().equals(text)) {
        // return EVOLUTION_TABLE;
        // }
        // } else if (anaType.getName().equals(AnalysisType.COLUMNS_COMPARISON.getName())) {
        // // MOD qiongli 2011-6-22 bug 16570,use the same jrxml between oracle and mysql
        // if (BASIC_COLUMNS_COMPARISON.getLabel().equals(text)) {
        // return BASIC_COLUMNS_COMPARISON;
        // } else if (EVOLUTION_COLUMNS_COMPARISON.getLabel().equals(text)) {
        // return EVOLUTION_COLUMNS_COMPARISON;
        // }
        // } else if (anaType.getName().equals(AnalysisType.COLUMN_CORRELATION.getName())) {
        // // MOD qiongli 2011-6-22 bug 16570,use the same jrxml between oracle and mysql
        // if (BASIC_COLUMNS_CORRELATION.getLabel().equals(text)) {
        // return BASIC_COLUMNS_CORRELATION;
        // } else if (EVOLUTION_COLUMNS_CORRELATION.getLabel().equals(text)) {
        // return EVOLUTION_COLUMNS_CORRELATION;
        // }
        //
        // } else if (anaType.getName().equals(AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY.getName())) {
        // if (BASIC_TABLE_FUNCTIONAL_DEPENDENCY.getLabel().equals(text)) {
        // return BASIC_TABLE_FUNCTIONAL_DEPENDENCY;
        // } else if (EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY.getLabel().equals(text)) {
        // return EVOLUTION_TABLE_FUNCTIONAL_DEPENDENCY;
        // }
        // } else if (anaType.getName().equals(AnalysisType.COLUMN_SET.getName())) {
        // // MOD mzhao 10706, column set analysis
        // if (BASIC_COLUMN_SET.getLabel().equals(text)) {
        // return BASIC_COLUMN_SET;
        // } else if (EVOLUTION_COLUMN_SET.getLabel().equals(text)) {
        // return EVOLUTION_COLUMN_SET;
        // }
        // }
        //
        // return null;
        // }

        public static ReportType getReportType(Analysis ana, String text) {
            AnalysisType at = AnalysisHelper.getAnalysisType(ana);
            return getReportType(at, text);
        }

        // public static ReportType getReportTypeWithDB(Analysis ana, String text, String dbType) {
        // AnalysisType at = AnalysisHelper.getAnalysisType(ana);
        // return getReportType(at, text, dbType);
        // }

        // /**
        // * DOC Administrator Comment method "getReportType".
        // *
        // * @param analysis
        // * @param reportType
        // * @param db
        // * @return
        // */
        // public static ReportType getReportType(Analysis analysis, String reportType, String db) {
        // // TODO Auto-generated method stub
        // return null;
        // }
    }

    private ReportHelper() {
    }

    /**
     * Method "getAnalyses".
     * 
     * @param report
     * @return a list of analyses or an empty list. Do not use this list to add analysis to the report.
     */
    public static List<Analysis> getAnalyses(Report report) {
        List<Analysis> analyses = new ArrayList<Analysis>();
        // MOD yyin 20120530 TDQ-5050
        if (report instanceof TdReport) {
            for (AnalysisMap anaMap : ((TdReport) report).getAnalysisMap()) {
                Analysis analysis = anaMap.getAnalysis();
                if (analysis != null) {
                    analyses.add(analysis);
                }
            }
        }
        return analyses;
    }

    /**
     * Method "removeAnalyses".
     * 
     * @param report
     * @param analyses analyses to remove from the report
     * @return true if the analyses list of the report changed as a result of the call.
     */
    public static boolean removeAnalyses(TdReport report, Collection<Analysis> analyses) {
        boolean removed = true;
        for (Analysis analysis : analyses) {
            if (!report.removeAnalysis(analysis)) {
                removed = false;
            }
        }
        return removed;
    }

    /**
     * Method "addAnalyses".
     * 
     * @param analyses a collection of analyses.
     * @param report a report (must not be null)
     * @return true if the analysis list changed as a result of the call.
     */
    public static boolean addAnalyses(Collection<Analysis> analyses, TdReport report) {
        boolean added = true;
        for (Analysis analysis : analyses) {
            if (!report.addAnalysis(analysis)) {
                added = false;
            }
        }
        return added;
    }

    public static TdReport createReport(String name) {
        TdReport report = ReportsFactory.eINSTANCE.createTdReport();
        report.setName(name);
        return report;
    }

    /**
     * Method "mustRefreshAllAnalyses".
     * 
     * @param report
     * @param refresh true if all analyses must be refreshed. False means that no analysis will be refreshed.
     */
    public static void mustRefreshAllAnalyses(TdReport report, boolean refresh) {
        EList<AnalysisMap> analysisMap = report.getAnalysisMap();
        for (AnalysisMap map : analysisMap) {
            map.setMustRefresh(refresh);
        }
    }

    /**
     * Method "getExecutionInformations" returns the execution informations of the given report. If none existed, they
     * are created and stored in the report.
     * 
     * @param report a report
     * @return the existing execution informations
     */
    public static ExecutionInformations getExecutionInformations(TdReport report) {
        ExecutionInformations execInformations = report.getExecInformations();
        if (execInformations == null) {
            execInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
            report.setExecInformations(execInformations);
        }
        return execInformations;
    }

    /**
     * Method "setReportType".
     * 
     * MOD mzhao 2009-02-16
     * 
     * @param report the report object to update
     * @param reportType the report type to set
     * @param jrxmlFullPath the full path to the jxrxml file (can be null when the type of report is different from the
     * USER_DEFINED)
     * @return true if everything is set correctly, false otherwise.
     */
    public static boolean setReportType(TdReport report, Analysis analysis, ReportType reportType, String jrxmlFullPath) {
        boolean ok = true;
        reportType = reportType == null ? ReportHelper.ReportType.USER_MADE : reportType;
        switch (reportType) {
        case USER_MADE:
            report.setReportType(reportType.getLabel(), jrxmlFullPath, analysis);
            if (StringUtils.isBlank(jrxmlFullPath)) {
                // do not log an error here
                ok = false;
            }
            break;
        default:
            report.setReportType(reportType.getLabel(), null, analysis);
            break;
        }
        return ok;
    }

    /**
     * 
     * DOC mzhao Set analysis filter date from.
     * 
     * @param report
     * @param dateText
     * @return
     * @throws ParseException
     */
    public static void setAnalysisFilterDateFrom(TdReport report, String dateText) {
        report.setDateFrom(StringUtils.isEmpty(dateText) ? null : dateText);
    }

    /**
     * 
     * DOC mzhao Comment method "setAnalysisFilterDateFrom".
     * 
     * @param report
     * @param dateText
     * @return
     * @return
     * @throws ParseException
     */
    public static void setAnalysisFilterDateTo(TdReport report, String dateText) {
        report.setDateTo(StringUtils.isEmpty(dateText) ? null : dateText);
    }

    /**
     * 
     * DOC mzhao Comment method "getReportType".
     * 
     * @param report
     * @param analysis
     * @return
     */
    public static String getReportType(TdReport report, Analysis analysis) {
        String reportType = null;
        EList<AnalysisMap> anMaps = report.getAnalysisMap();
        for (AnalysisMap anMap : anMaps) {
            if (ResourceHelper.areSame(analysis, anMap.getAnalysis())) {
                reportType = anMap.getReportType();
                break;
            }
        }
        return reportType;
    }

    public static String getReportJrxml(TdReport report, Analysis analysis) {
        String jrxmlSource = null;
        EList<AnalysisMap> anMaps = report.getAnalysisMap();
        for (AnalysisMap anMap : anMaps) {
            if (ResourceHelper.areSame(analysis, anMap.getAnalysis())) {
                jrxmlSource = anMap.getJrxmlSource();
                break;
            }
        }
        return jrxmlSource;
    }

    /**
     * DOC bZhou Comment method "setOutputType".
     * 
     * @param outputType
     * @param report
     * @return
     */
    public static boolean setOutputType(String outputType, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.OUTPUT_TYPE_TAG, outputType);
    }

    /**
     * DOC bZhou Comment method "getOutputType".
     * 
     * @param tag
     * @param element
     * @return
     */
    public static String getOutputType(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.OUTPUT_TYPE_TAG, report.getTaggedValue());
        if (taggedValue == null || taggedValue.getValue() == null) {
            // this default output type should be same with JasperReportBuilder.OUTPUT_FORMAT.pdf
            return "pdf";//$NON-NLS-1$ 
        }
        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setSingleGenReport".
     * 
     * @param single
     * @param report
     * @return
     */
    public static boolean setSingleGenReport(Boolean single, Report report) {
        String statusStr = String.valueOf(single);
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.GEN_SINGLE_REPORT, statusStr);
    }

    /**
     * set the password context to the report.
     * 
     * @param isContext the password is context or not
     * @param report
     * @return
     */
    public static boolean setPasswordContext(Boolean isContext, Report report) {
        String statusStr = String.valueOf(isContext);
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_PASSWORD_CONTEXT, statusStr);
    }

    /**
     * DOC bZhou Comment method "getSingleGenReport".
     * 
     * @param report
     * @return
     */
    public static Boolean getSingleGenReport(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.GEN_SINGLE_REPORT, report.getTaggedValue());
        if (taggedValue == null) {
            return false;
        }
        return Boolean.valueOf(taggedValue.getValue());
    }

    /**
     * if the password is context return true, else return false.
     * 
     * @param report
     * @return
     */
    public static Boolean getPasswordContext(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_PASSWORD_CONTEXT,
                report.getTaggedValue());
        if (taggedValue == null) {
            return false;
        }
        return Boolean.valueOf(taggedValue.getValue());
    }

    /**
     * DOC bZhou Comment method "setOutputFileName".
     * 
     * @param fileName
     * @param report
     * @return
     */
    public static boolean setOutputFileName(String fileName, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.OUTPUT_FILENAME_TAG, fileName);
    }

    /**
     * DOC bZhou Comment method "getOutputFileName".
     * 
     * @param report
     * @return
     */
    public static String getOutputFileName(Report report) {
        TaggedValue taggedValue = TaggedValueHelper
                .getTaggedValue(TaggedValueHelper.OUTPUT_FILENAME_TAG, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }

        return taggedValue.getValue();
    }

    /**
     * DOC bZhou Comment method "setOutputFolderName".
     * 
     * @param folderName
     * @param report
     * @return
     */
    public static boolean setOutputFolderName(String folderName, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.OUTPUT_FOLDER_TAG, folderName);
    }

    /**
     * DOC xqliu Comment method "getDbType".
     * 
     * @param report
     * @return
     */
    public static String getDbType(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_DBTYPE, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    public static String getDbVersion(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_DBVERSION,
                report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getDbName".
     * 
     * @param report
     * @return
     */
    public static String getDbName(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_DBNAME, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getUser".
     * 
     * @param report
     * @return
     */
    public static String getUser(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_USER, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getPassword".
     * 
     * @param report
     * @return
     */
    public static String getPassword(Report report) {
        TaggedValue taggedValue = TaggedValueHelper
                .getTaggedValue(TaggedValueHelper.REP_DBINFO_PASSWORD, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        // decrypt the password
        String password = taggedValue.getValue();
        String decryptPassword = ConnectionHelper.getDecryptPassword(password);
        if (decryptPassword != null) {
            password = decryptPassword;
        }
        return password;
    }

    /**
     * DOC xqliu Comment method "getDriver".
     * 
     * @param report
     * @return
     */
    public static String getDriver(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_DRIVER, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getDialect".
     * 
     * @param report
     * @return
     */
    public static String getDialect(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_DIALECT, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getUrl".
     * 
     * @param report
     * @return
     */
    public static String getUrl(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_URL, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getHost".
     * 
     * @param report
     * @return
     */
    public static String getHost(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_HOST, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    public static String getParameter(Report report) {
        TaggedValue taggedValue =
                TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_PARAMETER, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getLastRunContext".
     * 
     * @param report
     * @return
     */
    public static String getLastRunContext(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_LAST_RUN_CONTEXT,
                report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getPort".
     * 
     * @param report
     * @return
     */
    public static String getPort(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_PORT, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getSchema".
     * 
     * @param report
     * @return
     */
    public static String getSchema(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_SCHEMA, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "setDbType".
     * 
     * @param dbType
     * @param report
     * @return
     */
    public static boolean setDbType(String dbType, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_DBTYPE, dbType);
    }

    /**
     * DOC xqliu Comment method "dbVersion".
     * 
     * @param dbVersion
     * @param report
     * @return
     */
    public static boolean setDbVersion(String dbVersion, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_DBVERSION, dbVersion);
    }

    /**
     * DOC xqliu Comment method "setDbName".
     * 
     * @param dbName
     * @param report
     * @return
     */
    public static boolean setDbName(String dbName, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_DBNAME, dbName);
    }

    /**
     * DOC xqliu Comment method "setUser".
     * 
     * @param user
     * @param report
     * @return
     */
    public static boolean setUser(String user, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_USER, user);
    }

    /**
     * DOC xqliu Comment method "setPassword".
     * 
     * @param password
     * @param report
     * @return
     * @deprecated use {@link #setPassword(String, Report, boolean)}
     */
    @Deprecated
    public static boolean setPassword(String password, Report report) {
        return setPassword(password, report, true);
    }

    /**
     * DOC xqliu Comment method "setPassword".
     * 
     * @param password
     * @param report
     * @param encrypt encrypt the password or not
     * @return
     */
    public static boolean setPassword(String password, Report report, boolean encrypt) {
        // encrypt the password
        if (encrypt) {
            String encryptPassword = ConnectionHelper.getEncryptPassword(password);
            if (encryptPassword != null) {
                password = encryptPassword;
            }
        }
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_PASSWORD, password);
    }

    /**
     * DOC xqliu Comment method "setDriver".
     * 
     * @param driver
     * @param report
     * @return
     */
    public static boolean setDriver(String driver, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_DRIVER, driver);
    }

    /**
     * DOC xqliu Comment method "setDialect".
     * 
     * @param dialect
     * @param report
     * @return
     */
    public static boolean setDialect(String dialect, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_DIALECT, dialect);
    }

    /**
     * DOC xqliu Comment method "setUrl".
     * 
     * @param url
     * @param report
     * @return
     */
    public static boolean setUrl(String url, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_URL, url);
    }

    /**
     * DOC xqliu Comment method "setHost".
     * 
     * @param host
     * @param report
     * @return
     */
    public static boolean setHost(String host, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_HOST, host);
    }

    public static boolean setParameter(String parameter, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_PARAMETER, parameter);
    }

    /**
     * DOC xqliu Comment method "setLastRunContext".
     * 
     * @param lastRunContext
     * @param report
     * @return
     */
    public static boolean setLastRunContext(String lastRunContext, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_LAST_RUN_CONTEXT, lastRunContext);
    }

    /**
     * DOC xqliu Comment method "setPort".
     * 
     * @param port
     * @param report
     * @return
     */
    public static boolean setPort(String port, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_PORT, port);
    }

    /**
     * DOC xqliu Comment method "setSchema".
     * 
     * @param schema
     * @param report
     * @return
     */
    public static boolean setSchema(String schema, Report report) {
        return TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_SCHEMA, schema);
    }

    /**
     * DOC bZhou Comment method "getOutputFolderName".
     * 
     * @param report
     * @return
     */
    public static String getOutputFolderNameAssinged(Report report) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.OUTPUT_FOLDER_TAG, report.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }

        return taggedValue.getValue();
    }

    /**
     * DOC xqliu Comment method "getOutputName".
     * 
     * @param reportContainer
     * @param simpleName
     * @return
     */
    public static String getOutputFolderNameDefault(IFolder reportContainer, String simpleName) {
        // TDQ-11353: fix the folder path to support git remote folder path like
        // (".repositories/1867765462/master/PROJECT_NAME/)
        return WorkspaceUtils.ifolderToFile(reportContainer).getAbsolutePath() + File.separator + "." + simpleName;
    }

    /**
     * DOC yyi Comment method "getSubReportsPath".
     * 
     * @return
     */
    public static IPath getSubReportsPath() {
        return new Path(PROPS.getProperty("SUB_REPORT_FOLDER")); //$NON-NLS-1$
    }

    /**
     * get the report last run context group name, if it is empty, return the default context group name.
     * 
     * @param tdReport
     * @return
     */
    public static String getContextGroupName(TdReport tdReport) {
        String contextGroupNameInUse = ReportHelper.getLastRunContext(tdReport);
        if (StringUtils.isEmpty(contextGroupNameInUse)) {
            contextGroupNameInUse = tdReport.getDefaultContext();
        }
        if (StringUtils.isEmpty(contextGroupNameInUse)) {
            contextGroupNameInUse = DEFAULT;
        }
        return contextGroupNameInUse;
    }

}
