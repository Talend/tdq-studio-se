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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition;

import org.eclipse.swt.widgets.TableItem;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * The cell modifier of particular default survivorShip
 */
public class ParticularDefaultSurvivorShipDefinitionCellModifier extends ParticularDefaultSurvivorShipCellModifier {

    /**
     * The constructor of ParticularDefaultSurvivorShipDefinitionCellModifier class.
     * 
     * @param tableViewer
     */
    public ParticularDefaultSurvivorShipDefinitionCellModifier(
            AbstractMatchAnalysisTableViewer<ParticularDefaultSurvivorshipDefinitions> tableViewer) {
        super(tableViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipCellModifier#canModify
     * (java
     * .lang.Object, java.lang.String)
     */
    @Override
    public boolean canModify(Object element, String property) {
        if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
            return true;
        }
        return super.canModify(element, property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipCellModifier#getValue(
     * java.
     * lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
            ParticularDefaultSurvivorshipDefinitions pskd = (ParticularDefaultSurvivorshipDefinitions) element;
            return pskd.getColumn();
        }
        return super.getValue(element, property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipCellModifier#modify(java
     * .lang
     * .Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, String property, Object value) {
        if (element instanceof TableItem) {
            ParticularDefaultSurvivorshipDefinitions pdskd =
                    (ParticularDefaultSurvivorshipDefinitions) ((TableItem) element).getData();
            String newColumnName = String.valueOf(value);
            if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
                pdskd.setColumn(newColumnName);
                tableViewer.update(pdskd, null);
                return;
            }
        }
        super.modify(element, property, value);
    }

    @Override
    protected Object getReferenceColumnValue(ParticularDefaultSurvivorshipDefinitions pskd) {
        return pskd.getFunction().getReferenceColumn();
    }

    @Override
    protected String convertColIndex2Name(String newValue, ColumnsDateFilter colDateFilter) {
        return newValue;
    }

}
