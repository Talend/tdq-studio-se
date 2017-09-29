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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.CommonContenteProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.PatternLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class PatternStatisticsTableState extends AbstractTableTypeStates {

    public PatternStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("PatternStatisticsState.Label"), DefaultMessagesImpl.getString("PatternStatisticsState.Match"), DefaultMessagesImpl.getString("PatternStatisticsState.NoMatch"), DefaultMessagesImpl.getString("PatternStatisticsState.Match_"), DefaultMessagesImpl.getString("PatternStatisticsState.NoMatch_") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        entity.setFieldWidths(new Integer[] { 200, 75, 75, 75, 75 });
        return entity;
    }

    @Override
    protected ITableLabelProvider getLabelProvider() {
        return new PatternLabelProvider();
    }

    @Override
    protected IStructuredContentProvider getContentProvider() {
        return new CommonContenteProvider();
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
            PatternMatchingExt patternExt = PatternStatisticeStateUtil.getUnitValue(unit.getIndicator(), unit.getValue());
            String notMathCount = PatternStatisticeStateUtil.getNotMatchCount(patternExt);
            String machCount = PatternStatisticeStateUtil.getMatchCount(patternExt);
            PatternChartDataEntity patternEntity = PatternStatisticeStateUtil.createDataEntity(unit, unit.getIndicator()
                    .getName(), notMathCount, machCount);
            dataEnities.add(patternEntity);
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
        return PatternStatisticeStateUtil.getDataExplorer();
    }


}
