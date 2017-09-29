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
package org.talend.dataquality.record.linkage.ui.section;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.ui.composite.chart.DuplicateRecordPieChart;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.DuplicateRecordTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.DuplicateStatisticsRow;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.utils.format.StringFormatUtil;

/**
 * created by zhao on Aug 19, 2013 Duplicate record statistics section.
 * 
 */
public class DuplicateRecordStatisticsSection extends AbstractMatchAnaysisTableSection {

    private DuplicateRecordTableViewer duplicateRecordTableViewer = null;

    private DuplicateRecordPieChart duplicateRecordPieChart = null;

    private Label mergedRecordsValue = null;

    /**
     * DOC zhao DuplicateRecordStatisticsSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public DuplicateRecordStatisticsSection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit, Analysis analysis) {
        super(form, parent, style, toolkit, analysis);
    }

    /**
     * DOC zhao Comment method "setDupRecTableInput".
     */
    private void setDupRecordTableInput() {
        RecordMatchingIndicator recordMatchingIndicator = MatchRuleAnlaysisUtils.getRecordMatchIndicatorFromAna(analysis);
        // Row count
        Long rowCount = recordMatchingIndicator.getCount();
        // Compute row count.
        DuplicateStatisticsRow rowCountRow = new DuplicateStatisticsRow();
        rowCountRow.setIsRowCount(Boolean.TRUE);
        rowCountRow.setLabel(DefaultMessagesImpl.getString("DuplicateRecordStatisticsSection.ROW_COUNT")); //$NON-NLS-1$
        rowCountRow.setCount(rowCount);
        setPercentage(rowCount, rowCount, rowCountRow);
        // Unique records
        DuplicateStatisticsRow uniqueRow = new DuplicateStatisticsRow();
        uniqueRow.setLabel(DefaultMessagesImpl.getString("DuplicateRecordStatisticsSection.UNIQUE_RECORDS")); //$NON-NLS-1$
        Long uniqueCount = rowCount - recordMatchingIndicator.getSuspectRecordCount()
                - recordMatchingIndicator.getMatchedRecordCount();
        uniqueRow.setCount(uniqueCount);
        setPercentage(uniqueCount, rowCount, uniqueRow);
        // Matched records.
        DuplicateStatisticsRow matchedRow = new DuplicateStatisticsRow();
        matchedRow.setLabel(DefaultMessagesImpl.getString("DuplicateRecordStatisticsSection.MATCHED_RECORDS")); //$NON-NLS-1$
        matchedRow.setCount(recordMatchingIndicator.getMatchedRecordCount());
        setPercentage(recordMatchingIndicator.getMatchedRecordCount(), rowCount, matchedRow);
        // Suspect records.
        DuplicateStatisticsRow suspectRow = new DuplicateStatisticsRow();
        suspectRow.setLabel(DefaultMessagesImpl.getString("DuplicateRecordStatisticsSection.SUSPECT_RECORDS")); //$NON-NLS-1$
        suspectRow.setCount(recordMatchingIndicator.getSuspectRecordCount());
        setPercentage(recordMatchingIndicator.getSuspectRecordCount(), rowCount, suspectRow);

        List<DuplicateStatisticsRow> duplStatsRowList = new ArrayList<DuplicateStatisticsRow>();
        duplStatsRowList.add(rowCountRow);
        duplStatsRowList.add(uniqueRow);
        duplStatsRowList.add(matchedRow);
        duplStatsRowList.add(suspectRow);
        duplicateRecordTableViewer.setInput(duplStatsRowList);

    }

    /**
     * DOC zhao Comment method "setPercentage".
     * 
     * @param recordMatchingIndicator
     * @param rowCount
     * @param statsRow
     */
    private void setPercentage(Long count, Long rowCount, DuplicateStatisticsRow statsRow) {
        if (rowCount != 0) {
            double percValue = Double.valueOf(count) / Double.valueOf(rowCount);
            String formatedStr = StringFormatUtil.format(percValue, StringFormatUtil.PERCENT).toString();
            statsRow.setPercentage(formatedStr);
        } else {
            statsRow.setPercentage(PluginConstant.NA_STRING);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#createSubChart(org.eclipse.
     * swt.widgets.Composite)
     */
    @Override
    protected void createSubChart(Composite sectionClient) {
        duplicateRecordPieChart = new DuplicateRecordPieChart(sectionClient);
        List<DuplicateStatisticsRow> dupStatistics = (List<DuplicateStatisticsRow>) duplicateRecordTableViewer.getInput();
        duplicateRecordPieChart.createPieChart(dupStatistics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#createSubContent(org.eclipse
     * .swt.widgets.Composite)
     */
    @Override
    protected Composite createSubContent(Composite sectionClient) {
        duplicateRecordTableViewer = new DuplicateRecordTableViewer(sectionClient, SWT.NONE);
        setDupRecordTableInput();
        // Add the merged records information: only for T-swoosh
        final Object[] IndicatorList = MatchRuleAnlaysisUtils.getNeedIndicatorFromAna(analysis);
        final RecordMatchingIndicator recordMatchingIndicator = (RecordMatchingIndicator) IndicatorList[0];
        if (recordMatchingIndicator.getBuiltInMatchRuleDefinition().getRecordLinkageAlgorithm()
                .equals(RecordMatcherType.T_SwooshAlgorithm.name())) {
            Composite mergedRecordComp = new Composite(sectionClient, SWT.NONE);
            mergedRecordComp.setLayout(new GridLayout(2, true));
            Label mergedRcdLabel = new Label(mergedRecordComp, SWT.NONE);
            mergedRcdLabel.setText(DefaultMessagesImpl.getString("DuplicateRecordStatisticsSection.MergedRecord")); //$NON-NLS-1$
            mergedRecordsValue = new Label(mergedRecordComp, SWT.NONE);
            Long mergedRecordsCount = computeMergedRecords();
            mergedRecordsValue.setText(mergedRecordsCount.toString());
        }
        return sectionClient;

    }

    /**
     * DOC zhao Comment method "computeMergedRecords".
     * 
     * @return
     */
    private Long computeMergedRecords() {
        RecordMatchingIndicator recordMatchingIndicator = MatchRuleAnlaysisUtils.getRecordMatchIndicatorFromAna(analysis);
        Map<Object, Long> g2f = recordMatchingIndicator.getGroupSize2groupFrequency();
        Iterator<Object> groupSizeIterator = g2f.keySet().iterator();

        Long mergedRecordsCount = 0l;
        while (groupSizeIterator.hasNext()) {
            Object groupSize = groupSizeIterator.next();
            if (Long.valueOf(groupSize.toString()) > 1) {
                Long groupFreq = g2f.get(groupSize);
                // Merged records
                mergedRecordsCount += groupFreq;
            }
        }
        return mergedRecordsCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#getSectionName()
     */
    @Override
    protected String getSectionName() {
        return DefaultMessagesImpl.getString("MatchAnalysisResultPage.DUP_REC_STATS_NAME"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#refreshChart()
     */
    @Override
    public void refreshChart() {
        // Refresh table
        setDupRecordTableInput();
        List<DuplicateStatisticsRow> dupStatistics = (List<DuplicateStatisticsRow>) duplicateRecordTableViewer.getInput();
        duplicateRecordPieChart.refreshChart(dupStatistics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#isKeyDefinitionAdded(java.lang
     * .String)
     */
    @Override
    public Boolean isKeyDefinitionAdded(String columnName) throws Exception {
        // no implementation
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#createButtons(org.eclipse.swt
     * .widgets.Composite)
     */
    @Override
    protected void createButtons(Composite sectionClient) {
        // No implementation
    }
}
