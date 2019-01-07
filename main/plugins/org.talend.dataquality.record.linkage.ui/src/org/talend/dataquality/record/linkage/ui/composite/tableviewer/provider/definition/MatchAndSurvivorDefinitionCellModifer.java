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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition;

import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAndSurvivorCellModifer;

public class MatchAndSurvivorDefinitionCellModifer extends MatchAndSurvivorCellModifer {

    /**
     * @param newTableViewer
     */
    public MatchAndSurvivorDefinitionCellModifer(
            AbstractMatchAnalysisTableViewer<MatchKeyAndSurvivorDefinition> newTableViewer) {
        super(newTableViewer);
    }

    @Override
    protected String convertColIndex2Name(String newValue, ColumnsDateFilter colDateFilter) {
        return newValue;
    }

    @Override
    protected Object getReferenceColumnValue(MatchKeyAndSurvivorDefinition mkd) {
        return mkd.getSurvivorShipKey().getFunction().getReferenceColumn();
    }

}
