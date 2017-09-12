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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * The table viewer class particular default survivorShip which used on match rule editor
 */
public class ParticularDefaultSurvivorShipDefintionTableViewer extends ParticularDefaultSurvivorShipTableViewer {

    /**
     * The constructor of ParticularDefaultSurvivorShipDefintionTableViewer.
     * 
     * @param parent
     * @param style
     */
    public ParticularDefaultSurvivorShipDefintionTableViewer(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer#getCellEditor(java
     * .util.List, java.util.List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            {
                switch (i) {

                case 0:
                    editors[i] = new TextCellEditor(innerTable);
                    break;
                case 1:
                    editors[i] = new ComboBoxCellEditor(innerTable, SurvivorShipAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 2:
                    editors[i] = new TextCellEditor(innerTable);

                }
            }
        }
        return editors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer#
     * addEditingSupportForColumn(org.eclipse.jface.viewers.TableViewerColumn, java.lang.String)
     */
    @Override
    protected void addEditingSupportForColumn(TableViewerColumn tableViewerColumn, String columnTechnologyLabel) {
        // no need to implements
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer#getTableCellModifier
     * ()
     */
    @Override
    protected AbstractMatchCellModifier<ParticularDefaultSurvivorshipDefinitions> getTableCellModifier() {
        return new ParticularDefaultSurvivorShipDefinitionCellModifier(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer#createNewKeyDefinition
     * (java.lang.String)
     */
    @Override
    protected ParticularDefaultSurvivorshipDefinitions createNewKeyDefinition(String columnName) {
        ParticularDefaultSurvivorshipDefinitions newKeyDefinition = super.createNewKeyDefinition(columnName);
        newKeyDefinition.setColumn(columnName);
        return newKeyDefinition;
    }

}
