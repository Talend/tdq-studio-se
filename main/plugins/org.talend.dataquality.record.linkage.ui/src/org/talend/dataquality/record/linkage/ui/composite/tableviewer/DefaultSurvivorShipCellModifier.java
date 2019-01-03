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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
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
            DefaultSurvivorshipDefinition dsd = (DefaultSurvivorshipDefinition) element;
            if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                return isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE)
                        | isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.CONCATENATE);

            } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
                return isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.MOST_ANCIENT)
                        || isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.MOST_RECENT);
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
        DefaultSurvivorshipDefinition dsd = (DefaultSurvivorshipDefinition) element;
        if (MatchAnalysisConstant.DATA_TYPE.equalsIgnoreCase(property)) {
            return DefaultSurvivorShipDataTypeEnum.getTypeByValue(dsd.getDataType()).getIndex();
        } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
            return SurvivorShipAlgorithmEnum.getTypeBySavedValue(dsd.getFunction().getAlgorithmType()).getIndex();
        } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
            return dsd.getFunction().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
            return getReferenceColumnValue(dsd);
        }
        return null;
    }

    protected Object getReferenceColumnValue(DefaultSurvivorshipDefinition dsd) {
        int colIdx = 0;
        String referenceColumn = dsd.getFunction().getReferenceColumn();
        boolean isReferenceColumnExist = true;
        if (StringUtils.isEmpty(referenceColumn)) {
            isReferenceColumnExist = false;
        }
        for (MetadataColumn metaColumn : columnList) {
            ColumnsDateFilter dateColFilter = new ColumnsDateFilter();
            if (!dateColFilter.test(metaColumn)) {
                continue;
            }
            if (metaColumn.getName().equals(isReferenceColumnExist ? StringUtils.EMPTY : referenceColumn)) {
                break;
            }
            colIdx++;
        }
        return colIdx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, String property, Object value) {
        DefaultSurvivorshipDefinition dsd = (DefaultSurvivorshipDefinition) ((TableItem) element).getData();
        if (element instanceof TableItem) {
            String newValue = String.valueOf(value);
            if (MatchAnalysisConstant.DATA_TYPE.equalsIgnoreCase(property)) {
                DefaultSurvivorShipDataTypeEnum valueByIndex =
                        DefaultSurvivorShipDataTypeEnum.getTypeByIndex(Integer.valueOf(newValue).intValue());
                if (StringUtils.equals(dsd.getDataType(), valueByIndex.getValue())) {
                    return;
                }
                dsd.setDataType(valueByIndex.getValue());
            } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
                SurvivorShipAlgorithmEnum valueByIndex =
                        SurvivorShipAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue).intValue());
                if (StringUtils.equals(dsd.getFunction().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                dsd.getFunction().setAlgorithmType(valueByIndex.getComponentValueName());
                if (!(isSurvivorShipAlgorithm(dsd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE) | isSurvivorShipAlgorithm(
                        dsd, SurvivorShipAlgorithmEnum.CONCATENATE))) {
                    dsd.getFunction().setAlgorithmParameters(StringUtils.EMPTY);
                    CellEditor[] cellEditors = tableViewer.getCellEditors();
                    if (cellEditors.length == 3) {
                        cellEditors[2].setValue(StringUtils.EMPTY);
                    }
                }
            } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                dsd.getFunction().setAlgorithmParameters(newValue);
            } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
                String metaColumnName = convertColIndex2Name(newValue, new ColumnsDateFilter());
                if (metaColumnName == null) {
                    return;
                    // no modify anything
                }
                dsd.getFunction().setReferenceColumn(metaColumnName);
            } else {
                return;
            }
            tableViewer.update(dsd, null);
        }

    }

}
