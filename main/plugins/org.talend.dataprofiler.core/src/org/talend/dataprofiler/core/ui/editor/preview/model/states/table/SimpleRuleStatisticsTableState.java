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
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.BaseChartTableLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.CommonContenteProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SimpleStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.WhereRuleStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

/**
 * created by yyin on 2014-12-4 Detailled comment
 * 
 */
public class SimpleRuleStatisticsTableState extends AbstractRuleStatisticsTableState {

    /**
     * DOC yyin SimpleRuleStatisticsTableState constructor comment.
     * 
     * @param units
     */
    public SimpleRuleStatisticsTableState(List<TableIndicatorUnit> units) {
        super(units);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataEntity()
     */
    @Override
    public ChartDataEntity[] getDataEntity() {
        TableIndicatorUnit rownCountUnit = WhereRuleStatisticsStateUtil.getRownCountUnit(tableunits);
        List<ChartDataEntity> dataEnities = new ArrayList<ChartDataEntity>();
        if (rownCountUnit != null) {
            String value = CommonStateUtil.getUnitValue(rownCountUnit.getValue(), StringFormatUtil.DOUBLE_NUMBER);
            String label = rownCountUnit.getIndicatorName();
            ChartDataEntity createDataEntity = CommonStateUtil.createDataEntity(rownCountUnit, value, label);
            dataEnities.add(createDataEntity);
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
        return SimpleStatisticsStateUtil.getDataExplorer();
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("SimpleStatisticsStateTable.Label"), DefaultMessagesImpl.getString("SimpleStatisticsStateTable.Count"), "%" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

}
