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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.CommonContenteProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.SummaryLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SummaryStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-2 Detailled comment
 * 
 */
public class SummaryStatisticsTableState extends AbstractTableTypeStates {

    private int sqltype;

    private SummaryStatisticsStateUtil summaryUtil;

    /**
     * DOC yyin SummaryStatisticsTableState constructor comment.
     * 
     * @param units
     */
    public SummaryStatisticsTableState(List<IndicatorUnit> units) {
        summaryUtil = new SummaryStatisticsStateUtil();
        if (units != null) {
            this.units.addAll(summaryUtil.check(units));
        }

        sqltype = summaryUtil.findSqlType(units);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataEntity()
     */
    @Override
    public ChartDataEntity[] getDataEntity() {
        List<ChartDataEntity> dataEnities = new ArrayList<ChartDataEntity>();
        for (IndicatorUnit unit : units) {
            String value = summaryUtil.getUnitValue(unit);
            ChartDataEntity entity = summaryUtil.createDataEntity(unit, value);
            dataEnities.add(entity);
        }

        return dataEnities.toArray(new ChartDataEntity[dataEnities.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataExplorer()
     */
    @Override
    public DataExplorer getDataExplorer() {
        return summaryUtil.getDataExplorer(sqltype);
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("SummaryStatisticsState.Label"), DefaultMessagesImpl.getString("SummaryStatisticsState.Count") }); //$NON-NLS-1$ //$NON-NLS-2$
        entity.setFieldWidths(new Integer[] { 200, 300 });
        return entity;
    }

    @Override
    protected ITableLabelProvider getLabelProvider() {
        return new SummaryLabelProvider();
    }

    @Override
    protected IStructuredContentProvider getContentProvider() {
        return new CommonContenteProvider();
    }

}
