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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;

/**
 * created by yyin on 2014-12-4 Detailled comment
 * 
 */
public abstract class AbstractRuleStatisticsTableState extends AbstractTableTypeStates {

    protected List<TableIndicatorUnit> tableunits = new ArrayList<TableIndicatorUnit>();

    public AbstractRuleStatisticsTableState(List<TableIndicatorUnit> units) {
        if (units != null) {
            this.tableunits.addAll(units);
        }
    }

    @Override
    public TableViewer getTableForm(Composite parent) {
        TableViewer tbViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);

        Table table = tbViewer.getTable();

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GridData gd = new GridData();
        gd.heightHint = 220;
        gd.widthHint = 500;
        gd.verticalAlignment = SWT.BEGINNING;
        table.setLayoutData(gd);

        createTableColumnStructure(getTableStructure(), table);

        tbViewer.setLabelProvider(getLabelProvider());

        tbViewer.setContentProvider(getContentProvider());

        return tbViewer;
    }

    protected void createTableColumnStructure(TableStructureEntity entity, Table table) {
        if (entity.isValid()) {
            for (int i = 0; i < entity.getColumnCount(); i++) {
                TableColumn column = new TableColumn(table, SWT.NONE);
                column.setText(entity.getFieldNames()[i]);
                column.setWidth(entity.getFieldWidths()[i]);
            }
        }
    }

}
