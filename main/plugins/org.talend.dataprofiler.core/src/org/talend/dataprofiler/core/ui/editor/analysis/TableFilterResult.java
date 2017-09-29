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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.List;
import java.util.Map;

import org.talend.dataquality.indicators.RegexpMatchingIndicator;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class TableFilterResult {

    private List<Map<Integer, RegexpMatchingIndicator>> tableFilterResult = null;

    /**
     * DOC zshen TableFilterResult constructor comment.
     * 
     * @param tableInputList
     */
    public TableFilterResult(List<Map<Integer, RegexpMatchingIndicator>> tableInputList) {
        tableFilterResult = tableInputList;
    }

    public boolean isEmptyResult() {
        if (tableFilterResult == null) {
            return true;
        }
        int columnSize = tableFilterResult.size();
        if (columnSize > 0) {
            for (Map<Integer, RegexpMatchingIndicator> patternsMap : tableFilterResult) {
                for (RegexpMatchingIndicator regexIndi : patternsMap.values()) {
                    if (regexIndi != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Getter for tableFilterResult.
     * 
     * @return the tableFilterResult
     */
    public List<Map<Integer, RegexpMatchingIndicator>> getTableFilterResult() {
        return this.tableFilterResult;
    }

}
