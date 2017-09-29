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

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.BaseChartTableLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.CommonContenteProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SimpleStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-2 Detailled comment
 * 
 */
public class SimpleStatisticsTableState extends AbstractTableTypeStates {

    /**
     * DOC yyin SimpleStatisticsTableState constructor comment.
     * 
     * @param units
     */
    public SimpleStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("SimpleStatisticsState.Label"), DefaultMessagesImpl.getString("SimpleStatisticsState.Count"), "%" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        entity.setFieldWidths(new Integer[] { 200, 150, 150 });
        return entity;
    }

    @Override
    protected ITableLabelProvider getLabelProvider() {
        return new BaseChartTableLabelProvider();
    }

    @Override
    protected IStructuredContentProvider getContentProvider() {
        return new CommonContenteProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataExplorer()
     */
    @Override
    public DataExplorer getDataExplorer() {
        return SimpleStatisticsStateUtil.getDataExplorer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataEntity()
     */
    @Override
    public ChartDataEntity[] getDataEntity() {
        return CommonStateUtil.getDataEntity(units, 0);
    }

}
