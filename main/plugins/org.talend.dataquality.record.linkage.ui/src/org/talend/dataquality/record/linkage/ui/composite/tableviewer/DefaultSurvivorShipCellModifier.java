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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.TableItem;
import org.talend.dataquality.record.linkage.utils.DefaultSurvivorShipDataTypeEnum;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class DefaultSurvivorShipCellModifier extends AbstractMatchCellModifier<DefaultSurvivorshipDefinition> {

    public DefaultSurvivorShipCellModifier(AbstractMatchAnalysisTableViewer<DefaultSurvivorshipDefinition> tableViewer) {
        this.tableViewer = tableViewer;
    }

    @Override
    public boolean canModify(Object element, String property) {
        if (element != null && element instanceof DefaultSurvivorshipDefinition) {
            if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                DefaultSurvivorshipDefinition dsd = (DefaultSurvivorshipDefinition) element;
                return isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE)
                        | isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.CONCATENATE);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isSurvivorShipAlgorithm(DefaultSurvivorshipDefinition dsd, SurvivorShipAlgorithmEnum algorithm) {
        return dsd.getFunction().getAlgorithmType().equals(algorithm.getComponentValueName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        DefaultSurvivorshipDefinition skd = (DefaultSurvivorshipDefinition) element;
        if (MatchAnalysisConstant.DATA_TYPE.equalsIgnoreCase(property)) {
            return DefaultSurvivorShipDataTypeEnum.getTypeByValue(skd.getDataType()).getIndex();
        } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
            return SurvivorShipAlgorithmEnum.getTypeBySavedValue(skd.getFunction().getAlgorithmType()).getIndex();
        } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
            return skd.getFunction().getAlgorithmParameters();
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
            DefaultSurvivorshipDefinition skd = (DefaultSurvivorshipDefinition) ((TableItem) element).getData();
            String newValue = String.valueOf(value);
            if (MatchAnalysisConstant.DATA_TYPE.equalsIgnoreCase(property)) {
                DefaultSurvivorShipDataTypeEnum valueByIndex = DefaultSurvivorShipDataTypeEnum.getTypeByIndex(Integer.valueOf(
                        newValue).intValue());
                if (StringUtils.equals(skd.getDataType(), valueByIndex.getValue())) {
                    return;
                }
                skd.setDataType(valueByIndex.getValue());
            } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
                SurvivorShipAlgorithmEnum valueByIndex = SurvivorShipAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(skd.getFunction().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                skd.getFunction().setAlgorithmType(valueByIndex.getComponentValueName());
                if (!(isSurvivorShipAlgorithm(skd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE) | isSurvivorShipAlgorithm(skd,
                        SurvivorShipAlgorithmEnum.CONCATENATE))) {
                    skd.getFunction().setAlgorithmParameters(StringUtils.EMPTY);
                    CellEditor[] cellEditors = tableViewer.getCellEditors();
                    if (cellEditors.length == 3) {
                        cellEditors[2].setValue(StringUtils.EMPTY);
                    }
                }
            } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                skd.getFunction().setAlgorithmParameters(newValue);
            } else {
                return;
            }
            tableViewer.update(skd, null);
        }

    }

}
