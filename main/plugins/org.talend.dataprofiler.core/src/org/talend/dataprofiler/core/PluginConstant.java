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
package org.talend.dataprofiler.core;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * This class store all the constant of current plugin.
 */
public final class PluginConstant {

    private PluginConstant() {
    }

    public static final String SE_ID = "net.sourceforge.sqlexplorer.plugin.perspectives.SQLExplorerPluginPerspective"; //$NON-NLS-1$

    public static final String CHEAT_SHEET_VIEW = "org.eclipse.ui.cheatsheets.views.CheatSheetView"; //$NON-NLS-1$

    public static final String GETTING_STARTED_CHEAT_SHEET_ID = "org.talend.dataprofiler.core.talenddataprofiler"; //$NON-NLS-1$

    public static final String START_HERE_CHEAT_SHEET_ID = "org.talend.datacleansing.core.ui.dqcheatsheet"; //$NON-NLS-1$

    public static final String PERSPECTIVE_ID = "org.talend.dataprofiler.DataProfilingPerspective"; //$NON-NLS-1$

    public static final String SQLEXPLORER_PERSPECTIVE_ID = "net.sourceforge.sqlexplorer.plugin.perspectives.SQLExplorerPluginPerspective"; //$NON-NLS-1$

    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    public static final String ENTER_STRING = "\n"; //$NON-NLS-1$

    public static final int DEFAULT_INT_VALUE = 0;

    public static final String SEMICOLON_STRING = ";";//$NON-NLS-1$

    public static final String COMMA_STRING = ",";//$NON-NLS-1$

    public static final String DOT_STRING = "."; //$NON-NLS-1$

    public static final String SPACE_STRING = " "; //$NON-NLS-1$

    public static final String SLASH_STRING = "/"; //$NON-NLS-1$

    public static final String UNDER_LINE = "_"; //$NON-NLS-1$

    public static final String PROJECTCREATED_FLAG = "PROJECTCREATED_FLAG"; //$NON-NLS-1$

    public static final String CONNECTION_URL_PROPERTY = "CONNECTION_URL_PROPERTY"; //$NON-NLS-1$

    public static final String CONNECTION_DBNAME_PROPERTY = "CONNECTION_DBNAME_PROPERTY"; //$NON-NLS-1$

    public static final String HTML_SUFFIX = "html"; //$NON-NLS-1$

    public static final String SQL_SUFFIX = "*.sql"; //$NON-NLS-1$

    public static final String JRXML_STRING = "jrxml"; //$NON-NLS-1$

    public static final String JASPER_STRING = "jasper"; //$NON-NLS-1$

    public static final String SQL_STRING = "sql"; //$NON-NLS-1$

    public static final String FILE_SUFFIX = "FILE_SUFFIX"; //$NON-NLS-1$

    public static final String ISDIRTY_PROPERTY = "ISDIRTY_PROPERTY"; //$NON-NLS-1$

    public static final String DATAFILTER_PROPERTY = "DATAFILTER_PROPERTY"; //$NON-NLS-1$

    public static final String REPORT_FOLDER_PATH = "/Data Profiling/Reports"; //$NON-NLS-1$

    public static final String DQ_VIEW_ID = "org.talend.dataprofiler.core.ui.views.DQRespositoryView"; //$NON-NLS-1$

    public static final String PARENTHESIS_LEFT = "("; //$NON-NLS-1$

    public static final String PARENTHESIS_RIGHT = ")"; //$NON-NLS-1$

    public static final String PATTERN_EDITOR = "org.talend.dataprofiler.core.ui.editor.pattern.PatternEditor"; //$NON-NLS-1$

    public static final String DQRULE_EDITOR = "org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor"; //$NON-NLS-1$

    public static final String MAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)"; //$NON-NLS-1$

    public static final String FOLDER_PATTERN = "^[a-zA-Z]+[a-zA-Z0-9\\_]*$"; //$NON-NLS-1$

    public static final String LESS_OR_EQUAL = (Character.isDefined('\u2264')) ? "\u2264" : "<="; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String GREATER_OR_EQUAL = (Character.isDefined('\u2265')) ? "\u2265" : ">="; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String DATAEXPLORER_PERSPECTIVE = DefaultMessagesImpl.getString("PluginConstant.dataExplorer"); //$NON-NLS-1$

    public static final String DATAPROFILER_PERSPECTIVE = DefaultMessagesImpl.getString("PluginConstant.dataProfiler"); //$NON-NLS-1$

    public static final String VERSION_FILE_PATH = ".version.txt"; //$NON-NLS-1$

    public static final int CHART_STANDARD_WIDHT = 600;

    public static final int CHART_STANDARD_HEIGHT = 275;

    public static final String LANGUAGE_SELECTOR = "languageSelector"; //$NON-NLS-1$

    public static final String UNCATEGORIES = "uncategories";//$NON-NLS-1$

    public static final String SVN_SUFFIX = ".svn"; //$NON-NLS-1$

    public static final String CHEAT_SHEET_GETSTART_ID = "org.talend.dataprofiler.core.talenddataprofiler"; //$NON-NLS-1$

    public static final String AUTO_CHANGE2DATA_PROFILER = "AUTO_CHANGE2DATA_PROFILER"; //$NON-NLS-1$

    public static final String EXPAND_TREE = "Tree_Expands"; //$NON-NLS-1$

    public static final String NA_STRING = org.talend.dataquality.PluginConstant.NA_STRING;

    public static final String MAX_NB_ROWS_ANALYSIS_EDITOR = "MAX_NB_ROWS_ANALYSIS_EDITOR"; //$NON-NLS-1$

    public static final String WHERE = "WHERE"; //$NON-NLS-1$

    public static final int CRITICALITY_LEVEL_MIN = 1;

    public static final int CRITICALITY_LEVEL_MAX = 10;

    public static final String DEFAULT_OPERATOR = "="; //$NON-NLS-1$

    public static final String[] OPERATORS = { "=", ">", "<", ">=", "<=" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

}
