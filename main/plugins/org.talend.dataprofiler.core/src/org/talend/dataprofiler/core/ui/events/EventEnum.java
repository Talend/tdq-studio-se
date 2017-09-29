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
package org.talend.dataprofiler.core.ui.events;

/**
 * DOC yyin class global comment. Detailled comment
 */
public enum EventEnum {
    DQ_JRXML_RENAME,
    DQ_JRXML_MOVE,
    DQ_JRXML_DELETE,
    DQ_ANALYSIS_RUN_FROM_MENU,
    DQ_ANALYSIS_CHECK_BEFORERUN,
    // Update the software system when : new connection created; existing connection altered, exsiting connection
    // reloaded, connections checkedout from svn repository.
    DQ_SOFTWARESYSTEM_UPDATE,
    // Match analysis and Column analysis will need this action after create connection on the editor
    DQ_SELECT_ELEMENT_AFTER_CREATE_CONNECTION,
    DQ_MATCH_ANALYSIS_REFRESH_WITH_RESULT,
    // REFRESH db connection label in match analysis.
    DQ_MATCH_ANALYSIS_REFRESH_DATAPROVIDER_LABEL,
    // ONLY REFRESH the db list in combobox
    DQ_ANALYSIS_REFRESH_DATAPROVIDER_LIST,
    // reopen the related analysis after reload connection
    DQ_ANALYSIS_REOPEN_EDITOR,
    // add msjian 2014-4-30:used to check whether show allmatchindicator in the indicators section for columnset only
    DQ_COLUMNSET_SHOW_MATCH_INDICATORS,
    // Added YDQ-8787 dynamic chart
    DQ_DYNAMIC_REGISTER_DYNAMIC_CHART,
    DQ_DYNAMIC_REFRESH_DYNAMIC_CHART,
    DQ_DYNAMIC_UNREGISTER_DYNAMIC_CHART,
    DQ_DYMANIC_CHART,
    DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE,
    DQ_SEMANTIC_ENRICH_PAGE;
}
