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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.ModeLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.ModeStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class ModeStatisticsTableState extends AbstractTableTypeStates {

    public ModeStatisticsTableState(List<IndicatorUnit> units) {
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
            ChartDataEntity entity = ModeStatisticsStateUtil.createDataEntity(unit, unit.getIndicatorName());
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] { DefaultMessagesImpl.getString("ModeStatisticsState.Mode") }); //$NON-NLS-1$
        entity.setFieldWidths(new Integer[] { 500 });
        return entity;
    }

    @Override
    protected ITableLabelProvider getLabelProvider() {
        // TODO Auto-generated method stub
        return new ModeLabelProvider();
    }

    @Override
    protected IStructuredContentProvider getContentProvider() {
        // TODO Auto-generated method stub
        return new CommonContenteProvider();
    }

}
