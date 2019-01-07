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

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.DefaultSurvivorShipTableViewer;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;

public class DefaultSurvivorShipDefinitionTableViewer extends DefaultSurvivorShipTableViewer {

    /**
     * @param parent
     * @param style
     */
    public DefaultSurvivorShipDefinitionTableViewer(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] cellEditors = super.getCellEditor(headers, columnList);
        // reference column
        cellEditors[2] = new TextCellEditor(innerTable);
        return cellEditors;
    }

    @Override
    protected ComboBoxCellEditor createColumnCombEditor(List<MetadataColumn> columnList,
            Predicate<MetadataColumn> filter) {
        return null;
    }

    @Override
    protected AbstractMatchCellModifier<DefaultSurvivorshipDefinition> getTableCellModifier() {
        return new DefaultSurvivorShipDefinitionCellModifier(this);
    }

}
