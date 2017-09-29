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
package org.talend.dq.analysis.explore;

import java.util.HashMap;
import java.util.Map;

import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.TableAnalysisSqlExecutor;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleExplorer extends DataExplorer {

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new HashMap<String, String>();

        switch (this.indicatorEnum) {
        case WhereRuleIndicatorEnum:
            map.put(MENU_VIEW_VALID_ROWS, getComment(MENU_VIEW_VALID_ROWS) + getRowsStatement(true));
            map.put(MENU_VIEW_INVALID_ROWS, getComment(MENU_VIEW_INVALID_ROWS) + getRowsStatement(false));
            break;
        case RowCountIndicatorEnum:
            map.put(MENU_VIEW_ROWS, getComment(MENU_VIEW_ROWS) + getRowsStatement());
            break;
        default:
        }

        return map;
    }

    /**
     * DOC xqliu Comment method "getRowsStatement".
     * 
     * @param valid
     * @return
     */
    private String getRowsStatement(boolean valid) {
        // MOD xqliu 2009-10-29 bug 9702
        String dataFilterClause = this.getDataFilterClause();
        Indicator indicator2 = this.indicator;

        TableAnalysisSqlExecutor tasExecutor = new TableAnalysisSqlExecutor();
        tasExecutor.setCachedAnalysis(this.analysis);

        return tasExecutor.getValidStatement(dataFilterClause, indicator2, valid);
    }
}
