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

import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.DefaultSurvivorShipCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;

public class DefaultSurvivorShipDefinitionCellModifier extends DefaultSurvivorShipCellModifier {

    /**
     * @param tableViewer
     */
    public DefaultSurvivorShipDefinitionCellModifier(
            AbstractMatchAnalysisTableViewer<DefaultSurvivorshipDefinition> tableViewer) {
        super(tableViewer);
    }

    @Override
    protected String convertColIndex2Name(String newValue, ColumnsDateFilter colDateFilter) {
        return newValue;
    }

    @Override
    protected Object getReferenceColumnValue(DefaultSurvivorshipDefinition dsd) {
        return dsd.getFunction().getReferenceColumn();
    }

}
