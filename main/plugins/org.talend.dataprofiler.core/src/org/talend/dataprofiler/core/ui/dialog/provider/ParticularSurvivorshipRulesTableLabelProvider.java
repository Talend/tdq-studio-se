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
package org.talend.dataprofiler.core.ui.dialog.provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The lable provider of particular survivorship rules table
 */
public class ParticularSurvivorshipRulesTableLabelProvider extends MatchRulesTableLabelProvider {

    /**
     * Constructor of ParticularSurvivorshipRulesTableLabelProvider class.
     * 
     * @param inputColumnNames
     */
    public ParticularSurvivorshipRulesTableLabelProvider(List<String> inputColumnNames) {
        super(inputColumnNames);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.dialog.provider.MatchRulesTableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        Map<String, String> rule = (HashMap<String, String>) element;
        String result = null;
        switch (columnIndex) {

        case 0: // column
            result = rule.get(INPUT_COLUMN);
            break;
        case 1: // survivorship function
            result = rule.get(SURVIVORSHIP_FUNCTION);
            break;
        case 2: // parameter
            result = rule.get(PARAMETER);
            break;
        }
        return result;
    }

}
