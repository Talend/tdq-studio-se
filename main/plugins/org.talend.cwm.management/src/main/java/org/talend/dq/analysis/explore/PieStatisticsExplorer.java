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

import org.talend.dataquality.helpers.AnalysisHelper;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class PieStatisticsExplorer extends DataExplorer {

    public PieStatisticsExplorer() {
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {

        Map<String, String> map = new HashMap<String, String>();

        switch (this.indicatorEnum) {
        case FormatFreqPieIndictorEnum:
            if (AnalysisHelper.isJavaExecutionEngine(this.analysis)) {
                map.put(MENU_VIEW_ROWS, null);
            }
            break;

        default:
        }

        return map;

    }

}
