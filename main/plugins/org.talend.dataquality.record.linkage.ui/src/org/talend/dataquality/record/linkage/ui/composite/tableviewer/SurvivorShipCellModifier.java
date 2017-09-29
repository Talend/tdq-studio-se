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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.TableItem;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class SurvivorShipCellModifier extends AbstractMatchCellModifier<SurvivorshipKeyDefinition> {

    public SurvivorShipCellModifier(AbstractMatchAnalysisTableViewer<SurvivorshipKeyDefinition> tableViewer) {
        this.tableViewer = tableViewer;
    }

    @Override
    public boolean canModify(Object element, String property) {
        if (element != null && element instanceof SurvivorshipKeyDefinition) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        SurvivorshipKeyDefinition skd = (SurvivorshipKeyDefinition) element;
        if (MatchAnalysisConstant.SURVIVORSHIP_KEY_NAME.equalsIgnoreCase(property)) {
            return skd.getName();
        } else if (MatchAnalysisConstant.COLUMN.equalsIgnoreCase(property)) {
            return skd.getColumn();
        } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
            return SurvivorShipAlgorithmEnum.getTypeBySavedValue(skd.getFunction().getAlgorithmType()).getIndex();
        } else if (MatchAnalysisConstant.ALLOW_MANUAL_RESOLUTION.equalsIgnoreCase(property)) {
            return skd.isAllowManualResolution();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, String property, Object value) {
        if (element instanceof TableItem) {
            SurvivorshipKeyDefinition skd = (SurvivorshipKeyDefinition) ((TableItem) element).getData();
            String newValue = String.valueOf(value);
            if (MatchAnalysisConstant.SURVIVORSHIP_KEY_NAME.equalsIgnoreCase(property)) {
                if (StringUtils.equals(skd.getName(), newValue)) {
                    return;
                }
                skd.setName(newValue);
            } else if (MatchAnalysisConstant.COLUMN.equalsIgnoreCase(property)) {
                if (StringUtils.equals(skd.getColumn(), newValue)) {
                    return;
                }
                skd.setColumn(newValue);
            } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
                SurvivorShipAlgorithmEnum valueByIndex = SurvivorShipAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(skd.getFunction().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                skd.getFunction().setAlgorithmType(valueByIndex.getComponentValueName());
            } else if (MatchAnalysisConstant.ALLOW_MANUAL_RESOLUTION.equalsIgnoreCase(property)) {
                if (skd.isAllowManualResolution() == Boolean.valueOf(newValue)) {
                    return;
                }
                skd.setAllowManualResolution(Boolean.valueOf(newValue));
            } else {
                return;
            }
            tableViewer.update(skd, null);
        }

    }

}
