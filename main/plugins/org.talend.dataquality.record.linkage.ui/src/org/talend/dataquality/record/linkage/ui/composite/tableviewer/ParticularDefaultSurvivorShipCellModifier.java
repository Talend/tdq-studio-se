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
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.editingSupport.FunctionEditingSupport;
import org.talend.dataquality.record.linkage.utils.DefaultSurvivorShipDataTypeEnum;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * Cell modifier of Particular default survivorShip
 */
public class ParticularDefaultSurvivorShipCellModifier extends
        AbstractMatchCellModifier<ParticularDefaultSurvivorshipDefinitions> {

    /**
     * Constructor of ParticularDefaultSurvivorShipCellModifier class
     */
    public ParticularDefaultSurvivorShipCellModifier(
            AbstractMatchAnalysisTableViewer<ParticularDefaultSurvivorshipDefinitions> tableViewer) {
        this.tableViewer = tableViewer;
    }

    @Override
    public boolean canModify(Object element, String property) {
        if (element != null && element instanceof ParticularDefaultSurvivorshipDefinitions) {
            if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                ParticularDefaultSurvivorshipDefinitions pdsd = (ParticularDefaultSurvivorshipDefinitions) element;
                return isSurvivorShipAlgorithm(pdsd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE)
                        | isSurvivorShipAlgorithm(pdsd, SurvivorShipAlgorithmEnum.CONCATENATE);
            } else if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
                return columnList.size() > 0;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isSurvivorShipAlgorithm(ParticularDefaultSurvivorshipDefinitions pdsd, SurvivorShipAlgorithmEnum algorithm) {
        return pdsd.getFunction().getAlgorithmType().equals(algorithm.getComponentValueName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        ParticularDefaultSurvivorshipDefinitions pskd = (ParticularDefaultSurvivorshipDefinitions) element;
        if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
            if (pskd.getColumn() != null) {
                for (MetadataColumn metadataCol : columnList) {
                    if (pskd.getColumn().equals(metadataCol.getName())) {
                        return columnList.indexOf(metadataCol);
                    }
                }
            }
            return -1;
        } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
            return SurvivorShipAlgorithmEnum.getTypeBySavedValue(pskd.getFunction().getAlgorithmType()).getIndex();
        } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
            String algorithmParameters = pskd.getFunction().getAlgorithmParameters();
            return algorithmParameters == null ? StringUtils.EMPTY : algorithmParameters;
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
            ParticularDefaultSurvivorshipDefinitions pdskd = (ParticularDefaultSurvivorshipDefinitions) ((TableItem) element)
                    .getData();
            String newValue = String.valueOf(value);
            if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {

                if (Integer.parseInt(newValue) == -1) {
                    return;
                }
                MetadataColumn metadataColumn = columnList.get(Integer.parseInt(newValue));
                String columnName = metadataColumn.getName();
                if (StringUtils.equals(pdskd.getColumn(), columnName)) {
                    return;
                }
                pdskd.setColumn(columnName);
                pdskd.setDataType(metadataColumn.getTalendType());
                if (isFunctionInvalid(pdskd, metadataColumn.getTalendType())) {
                    resetFunction(pdskd);
                }
            } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
                SurvivorShipAlgorithmEnum valueByIndex = SurvivorShipAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(pdskd.getFunction().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                setFunction(pdskd, valueByIndex);
            } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                pdskd.getFunction().setAlgorithmParameters(newValue);
            } else {
                return;
            }
            tableViewer.update(pdskd, null);
        }

    }

    /**
     * Set function to special one(from SurvivorShipAlgorithmEnum)
     * 
     * @param pdskd
     */
    private void setFunction(ParticularDefaultSurvivorshipDefinitions pdskd, SurvivorShipAlgorithmEnum functionEnum) {
        pdskd.getFunction().setAlgorithmType(functionEnum.getComponentValueName());
        if (!(isSurvivorShipAlgorithm(pdskd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE) | isSurvivorShipAlgorithm(pdskd,
                SurvivorShipAlgorithmEnum.CONCATENATE))) {
            pdskd.getFunction().setAlgorithmParameters(StringUtils.EMPTY);
            CellEditor[] cellEditors = tableViewer.getCellEditors();
            if (cellEditors.length == 3) {
                cellEditors[2].setValue(StringUtils.EMPTY);
            }
        }

    }

    /**
     * Reset function to default one(SurvivorShipAlgorithmEnum.CONCATENATE)
     * 
     * @param pdskd
     */
    private void resetFunction(ParticularDefaultSurvivorshipDefinitions pdskd) {
        pdskd.getFunction().setAlgorithmType(SurvivorShipAlgorithmEnum.MOST_COMMON.getComponentValueName());
        pdskd.getFunction().setAlgorithmParameters(StringUtils.EMPTY);
    }

    /**
     * Judge whether current function is valid for new data type
     * 
     * @param pdskd
     * @param talendType
     * @return
     */
    private boolean isFunctionInvalid(ParticularDefaultSurvivorshipDefinitions pdskd, String talendType) {
        String dataType = pdskd.getDataType();
        DefaultSurvivorShipDataTypeEnum[] functionDataType = SurvivorShipAlgorithmEnum.getTypeBySavedValue(
                pdskd.getFunction().getAlgorithmType()).getDataType();
        if (functionDataType.length == 0 || FunctionEditingSupport.isSupportDataType(functionDataType, talendType)) {
            return false;
        }
        return true;
    }

}
