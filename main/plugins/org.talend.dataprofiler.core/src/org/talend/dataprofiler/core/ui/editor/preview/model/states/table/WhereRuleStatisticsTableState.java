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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.BaseChartTableLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.CommonContenteProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ChartTableProviderClassSet.PatternLabelProvider;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.WhereRuleStatisticsStateUtil;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.format.StringFormatUtil;

/**
 * created by yyin on 2014-12-4 Detailled comment
 * 
 */
public class WhereRuleStatisticsTableState extends AbstractRuleStatisticsTableState {

    private Long rowCount;

    public WhereRuleStatisticsTableState(List<TableIndicatorUnit> units, TableIndicator tableIndicator) {
        super(units);
        this.rowCount = WhereRuleStatisticsStateUtil.initRowCount(tableIndicator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.table.AbstractTableTypeStates#getDataEntity()
     */
    @Override
    public ChartDataEntity[] getDataEntity() {
        List<WhereRuleChartDataEntity> dataEnities = new ArrayList<WhereRuleChartDataEntity>();

        for (TableIndicatorUnit unit : tableunits) {
            if (IndicatorEnum.WhereRuleIndicatorEnum.equals(unit.getType())) {
                double value = WhereRuleStatisticsStateUtil.getMatchValue(unit.getValue());
                WhereRuleChartDataEntity entity = WhereRuleStatisticsStateUtil.createRuleDataEntity(unit,
                        unit.getIndicatorName(), value,
                        WhereRuleStatisticsStateUtil.getNotMatchValue(unit.getValue(), value, unit.geIndicatorCount()));

                dataEnities.add(entity);
            }
        }
        return dataEnities.toArray(new WhereRuleChartDataEntity[dataEnities.size()]);
    }

    /**
     * DOC xqliu Comment method "getDataEntityRowCount".
     * 
     * @return
     */
    public ChartDataEntity[] getDataEntityOfRowCount() {
        TableIndicatorUnit rownCountUnit = WhereRuleStatisticsStateUtil.getRownCountUnit(tableunits);
        List<ChartDataEntity> dataEnities = new ArrayList<ChartDataEntity>();
        if (rownCountUnit != null) {
            // final Object unitValue = rownCountUnit.getValue();
            // unitValue != null ?Double.parseDouble(unitValue.toString()): Double.NaN;
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
        return WhereRuleStatisticsStateUtil.getDataExplorer();
    }

    @Override
    protected TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.Label"), DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.Match"), DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.NoMatch"), DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.Match_"), DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.NoMatch_") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        entity.setFieldWidths(new Integer[] { 200, 75, 75, 75, 75 });
        return entity;
    }

    protected TableStructureEntity getTableStructureRowCount() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("SimpleStatisticsStateTable.Label"), DefaultMessagesImpl.getString("SimpleStatisticsStateTable.Count"), "%" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        entity.setFieldWidths(new Integer[] { 200, 150, 150 });
        return entity;
    }

    @Override
    protected ITableLabelProvider getLabelProvider() {
        return new WhereRuleTableLabelProvider(rowCount);
    }

    @Override
    protected IStructuredContentProvider getContentProvider() {
        return new CommonContenteProvider();
    }

    /**
     * DOC xqliu Comment method "getTableFormRowCount".
     * 
     * @param composite
     * @return
     */
    public TableViewer getTableFormRowCount(Composite composite) {
        TableViewer tbViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);

        Table table = tbViewer.getTable();

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GridData gd = new GridData();
        gd.heightHint = 220;
        gd.widthHint = 500;
        gd.verticalAlignment = SWT.BEGINNING;
        table.setLayoutData(gd);

        createTableColumnStructure(getTableStructureRowCount(), table);

        tbViewer.setLabelProvider(getLabelProviderRowCount());

        tbViewer.setContentProvider(getContentProvider());

        return tbViewer;
    }

    /**
     * DOC xqliu Comment method "getLabelProviderRowCount".
     * 
     * @return
     */
    private IBaseLabelProvider getLabelProviderRowCount() {
        return new BaseChartTableLabelProvider();
    }

    /**
     * DOC xqliu WhereRuleStatisticsStateTable class global comment. Detailled comment
     */
    public static class WhereRuleTableLabelProvider extends PatternLabelProvider {

        private Long rowCount = 0L;

        public Long getRowCount() {
            return this.rowCount;
        }

        public void setRowCount(Long rowCount) {
            this.rowCount = rowCount;
        }

        public WhereRuleTableLabelProvider(Long rowCount) {
            setRowCount(rowCount);
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            Image result = super.getColumnImage(element, columnIndex);

            if (result == null) {
                boolean largeThanRowCount = false;

                Indicator indicator = ((WhereRuleChartDataEntity) element).getIndicator();

                if (!Double.isNaN(Double.parseDouble(((WhereRuleChartDataEntity) element).getNumMatch()))) {
                    largeThanRowCount = getRowCount() < ((WhereRuleIndicator) indicator).getUserCount();
                }

                if (3 == columnIndex && largeThanRowCount) {
                    result = ImageLib.getImage(ImageLib.LEVEL_WARNING);
                }
            }

            return result;
        }

        @Override
        public Color getForeground(Object element, int columnIndex) {
            Color result = super.getForeground(element, columnIndex);

            if (result == null) {
                boolean largeThanRowCount = false;

                Indicator indicator = ((WhereRuleChartDataEntity) element).getIndicator();

                if (!Double.isNaN(Double.parseDouble(((WhereRuleChartDataEntity) element).getNumMatch()))) {
                    // MOD yyin 20121031 TDQ-6194, when: match+no match>row count, highlight
                    largeThanRowCount = getRowCount() < ((WhereRuleIndicator) indicator).getCount();
                }

                if ((3 == columnIndex || 4 == columnIndex) && largeThanRowCount) {
                    result = Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            }

            return result;
        }

    }

    public TableIndicatorUnit getRownCountUnit(List<TableIndicatorUnit> units1) {
        return WhereRuleStatisticsStateUtil.getRownCountUnit(units1);
    }

    public List<TableIndicatorUnit> removeRowCountUnit(List<TableIndicatorUnit> units1) {
        return WhereRuleStatisticsStateUtil.removeRowCountUnit(units1);
    }

}
