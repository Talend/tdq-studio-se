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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.FrequencyLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.PieStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class PieStatisticsTableState extends AbstractTableTypeStates {

    public PieStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
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
            if (unit.isExcuted()) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();

                int numOfShown = PieStatisticsStateUtil.getNumberOfShown(unit, frequencyExt);

                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    ChartDataEntity entity = PieStatisticsStateUtil.createDataEntity(unit, freqExt,
                            PieStatisticsStateUtil.getkeyLabel(freqExt));
                    dataEnities.add(entity);
                }
            }
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
        return PieStatisticsStateUtil.getDataExplorer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.AbstractChartTypeStates#getTableStructure()
     */
    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("FrequencyTypeStates.value"), DefaultMessagesImpl.getString("FrequencyTypeStates.count"), "%" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        entity.setFieldWidths(new Integer[] { 200, 150, 150 });
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.AbstractChartTypeStates#getLabelProvider()
     */
    @Override
    protected ITableLabelProvider getLabelProvider() {
        return new FrequencyLabelProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.AbstractChartTypeStates#getContentProvider()
     */
    @Override
    protected IStructuredContentProvider getContentProvider() {
        return new CommonContenteProvider();
    }

}
