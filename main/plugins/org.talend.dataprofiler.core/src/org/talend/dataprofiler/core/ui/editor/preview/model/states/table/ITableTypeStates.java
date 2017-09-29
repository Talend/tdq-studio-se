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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-2 Create table viewer, and its related data entity.
 * 
 */
public interface ITableTypeStates {

    /**
     * DOC Zqin Comment method "getDataEntity".
     * 
     * @return the data entity of the specified chart, this is to create table viewer.
     */
    public ChartDataEntity[] getDataEntity();

    /**
     * DOC Zqin Comment method "getDataExplorer".
     * 
     * @return the specified data explorer for kinds of chart.
     */
    public DataExplorer getDataExplorer();

    /**
     * DOC Zqin Comment method "getTableForm".
     * 
     * @param parent
     * @return the table form of the specified chart data.
     */
    public TableViewer getTableForm(Composite parent);

}
