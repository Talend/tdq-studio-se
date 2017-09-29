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
 * DOC qiongli class global comment. Detailled comment
 */
public class PhoneNumbStatisticsExplorer extends DataExplorer {

    public PhoneNumbStatisticsExplorer() {
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new HashMap<String, String>();
        switch (this.indicatorEnum) {
        case ValidPhoneCountIndicatorEnum:
        case PossiblePhoneCountIndicatorEnum:
        case ValidRegCodeCountIndicatorEnum:
        case InvalidRegCodeCountIndicatorEnum:
        case WellFormE164PhoneCountIndicatorEnum:
        case WellFormIntePhoneCountIndicatorEnum:
        case WellFormNationalPhoneCountIndicatorEnum:
            map.put(MENU_VIEW_ROWS, null);
            map.put(MENU_VIEW_VALUES, null);
            break;

        default:
        }

        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.DataExplorer#NotShowMenu()
     */
    @Override
    protected boolean NotShowMenu() {
        return !AnalysisHelper.isJavaExecutionEngine(this.analysis);
    }

}
