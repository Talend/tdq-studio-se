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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.editingSupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer;
import org.talend.dataquality.record.linkage.utils.DefaultSurvivorShipDataTypeEnum;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * Support dynamic load cell editor
 */
public class FunctionEditingSupport extends EditingSupport {

    /**
     * constructor of FunctionEditingSupport class.
     * 
     * @param viewer
     */
    public FunctionEditingSupport(ColumnViewer viewer) {
        super(viewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
     */
    @Override
    protected CellEditor getCellEditor(Object element) {
        String talendDataType = ((ParticularDefaultSurvivorshipDefinitions) element).getDataType();
        ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(
                ((ParticularDefaultSurvivorShipTableViewer) this.getViewer()).getTable(),
                getAllFunctionByDataTypes(talendDataType), SWT.READ_ONLY);
        return comboBoxCellEditor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
     */
    @Override
    protected boolean canEdit(Object element) {
        if (element != null && element instanceof ParticularDefaultSurvivorshipDefinitions) {
            String columnName = ((ParticularDefaultSurvivorshipDefinitions) element).getColumn();
            if (columnName == null || columnName.isEmpty()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
     */
    @Override
    protected Object getValue(Object element) {
        String talendDataType = ((ParticularDefaultSurvivorshipDefinitions) element).getDataType();
        String[] allShowTypes = getAllFunctionByDataTypes(talendDataType);
        SurvivorShipAlgorithmEnum typeBySavedValue = SurvivorShipAlgorithmEnum
                .getTypeBySavedValue(((ParticularDefaultSurvivorshipDefinitions) element).getFunction().getAlgorithmType());
        for (int index = 0; index < allShowTypes.length; index++) {
            if (typeBySavedValue.getValue().equals(allShowTypes[index])) {
                return index;
            }
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void setValue(Object element, Object value) {
        ParticularDefaultSurvivorshipDefinitions pdskd = ((ParticularDefaultSurvivorshipDefinitions) element);
        String talendDataType = ((ParticularDefaultSurvivorshipDefinitions) element).getDataType();
        int functionTypeIndex = Integer.valueOf(value.toString()).intValue();
        SurvivorShipAlgorithmEnum valueByIndex = findEnumValue(talendDataType, functionTypeIndex);
        if (StringUtils.equals(pdskd.getFunction().getAlgorithmType(), valueByIndex.getComponentValueName())) {
            return;
        }
        pdskd.getFunction().setAlgorithmType(valueByIndex.getComponentValueName());
        if (!(isSurvivorShipAlgorithm(pdskd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE) || isSurvivorShipAlgorithm(pdskd,
                SurvivorShipAlgorithmEnum.CONCATENATE))) {
            pdskd.getFunction().setAlgorithmParameters(StringUtils.EMPTY);
            CellEditor[] cellEditors = this.getViewer().getCellEditors();
            if (cellEditors.length == 3) {
                cellEditors[2].setValue(StringUtils.EMPTY);
            }
        }
        this.getViewer().update(element, null);
    }

    /**
     * Find enum value by data type of talend and index of function
     * 
     * @param talendDataType like id_String or id_Boolean
     * @param functionTypeIndex the index of function which want to choose -1 mean that no funncion be choosed
     * 
     * @return value of SurvivorShipAlgorithmEnum which choose else return default one(MostCommon)
     */
    private SurvivorShipAlgorithmEnum findEnumValue(String talendDataType, int functionTypeIndex) {
        // One case about this issue is imported matchRule choose error functionType
        if (functionTypeIndex == -1) {
            return SurvivorShipAlgorithmEnum.MOST_COMMON;
        }
        String[] allFunctionByDataTypes = getAllFunctionByDataTypes(talendDataType);
        if (allFunctionByDataTypes.length == 0) {
            return SurvivorShipAlgorithmEnum.MOST_COMMON;
        }
        SurvivorShipAlgorithmEnum valueByIndex = SurvivorShipAlgorithmEnum
                .getTypeByValue(allFunctionByDataTypes[functionTypeIndex]);
        return valueByIndex;

    }

    private boolean isSurvivorShipAlgorithm(ParticularDefaultSurvivorshipDefinitions pdsd, SurvivorShipAlgorithmEnum algorithm) {
        return pdsd.getFunction().getAlgorithmType().equals(algorithm.getComponentValueName());
    }

    public static String[] getAllFunctionByDataTypes(String talendDataType) {
        List<String> list = new ArrayList<String>();
        for (SurvivorShipAlgorithmEnum theType : SurvivorShipAlgorithmEnum.values()) {
            DefaultSurvivorShipDataTypeEnum[] supportDataTypes = theType.getDataType();
            if (supportDataTypes.length == 0 || isSupportDataType(supportDataTypes, talendDataType)) {
                list.add(theType.getValue());
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public static boolean isSupportDataType(DefaultSurvivorShipDataTypeEnum[] supportDataTypes, String talendDataType) {
        for (DefaultSurvivorShipDataTypeEnum theType : supportDataTypes) {
            if (StringUtils.equalsIgnoreCase(talendDataType, "id_" + theType) //$NON-NLS-1$
                    || StringUtils.equals(theType.getValue(), "Number") //$NON-NLS-1$
                    && JavaTypesManager.isNumber(talendDataType)) {
                return true;
            }
        }
        return false;
    }

}
