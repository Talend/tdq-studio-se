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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.grouping.MatchGroupResultConsumer;
import org.talend.dataquality.record.linkage.ui.composite.chart.MatchRuleDataChart;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dq.analysis.AnalysisRecordGroupingUtils;
import org.talend.dq.analysis.match.ExecuteMatchRuleHandler;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by zshen on Aug 26, 2013 Detailled comment
 * 
 */
abstract public class AbstractMatchKeyWithChartTableSection extends AbstractMatchAnaysisTableSection {

    /**
     * the computation starts 500ms after the user has changed the value.
     */
    private static final int DELAY_RUN_GROUP_LESS_THEN = 500;

    private List<RunModifyTimerTask> taskList = new ArrayList<RunModifyTimerTask>();

    protected MatchRuleDataChart matchRuleChartComp = null;

    private List<Object[]> tableResult = null;

    private TreeMap<Object, Long> groupSize2groupFrequency = null;

    /**
     * DOC zshen AbstractMatchKeyWithChartTableSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public AbstractMatchKeyWithChartTableSection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit,
            Analysis analysis) {
        super(form, parent, style, toolkit, analysis);
    }

    /**
     * DOC zshen Comment method "createHideGroupComposite".
     * 
     * @param chartComposite
     */
    protected void createHideGroupComposite(Composite chartComposite) {
        GridData gridData;
        Composite toolComp = toolkit.createComposite(chartComposite, SWT.NONE);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        toolComp.setLayoutData(gridData);

        GridLayout gridLayout = new GridLayout(3, false);
        toolComp.setLayout(gridLayout);

        Label lessText = new Label(toolComp, SWT.NONE);
        lessText.setText(DefaultMessagesImpl.getString("AbstractMatchKeyWithChartTableSection.hide_groups")); //$NON-NLS-1$

        // create a spinner with min value 1 and max value
        final Spinner lessSpin = new Spinner(toolComp, SWT.BORDER);
        lessSpin.setMinimum(1);
        lessSpin.setMaximum(Integer.MAX_VALUE);
        lessSpin.setTextLimit(9);
        lessSpin.setSelection(PluginConstant.HIDDEN_GROUP_LESS_THAN_DEFAULT);

        lessSpin.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Long currentRunTime = System.currentTimeMillis();
                if (taskList.size() > 0) {
                    RunModifyTimerTask oldRunTask = taskList.get(0);
                    if (currentRunTime - oldRunTask.getTaskRunTime() < DELAY_RUN_GROUP_LESS_THEN) {
                        oldRunTask.cancel();
                        taskList.clear();
                    }
                }

                // run current after 500ms
                RunModifyTimerTask runModifyTimerTask = new RunModifyTimerTask(currentRunTime, lessSpin.getText().trim());
                new Timer().schedule(runModifyTimerTask, DELAY_RUN_GROUP_LESS_THEN);
                taskList.add(runModifyTimerTask);
            }
        });

        Label lessText2 = new Label(toolComp, SWT.NONE);
        lessText2.setText(DefaultMessagesImpl.getString("AbstractMatchKeyWithChartTableSection.items")); //$NON-NLS-1$
    }

    class RunModifyTimerTask extends TimerTask {

        Long taskRunTime = 0l;

        /**
         * Getter for taskRunTime.
         * 
         * @return the taskRunTime
         */
        public Long getTaskRunTime() {
            return this.taskRunTime;
        }

        private String text;

        public RunModifyTimerTask(Long runTime, String text) {
            this.taskRunTime = runTime;
            this.text = text;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run() {
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    if (matchRuleChartComp != null) {
                        int oldValue = matchRuleChartComp.getTimes();
                        int times = StringUtils.isEmpty(text) ? 1 : Integer.parseInt(text);
                        matchRuleChartComp.setTimes(times);
                        matchRuleChartComp.refresh();
                        listeners.firePropertyChange(MatchAnalysisConstant.HIDE_GROUPS, oldValue, times);
                    }

                    // when run this, it means 500ms later,so we can clear the cache.
                    taskList.clear();

                }
            });
        }
    }

    protected TypedReturnCode<RecordMatchingIndicator> computeMatchResult() {
        TypedReturnCode<RecordMatchingIndicator> rc = new TypedReturnCode<RecordMatchingIndicator>(false);
        final Object[] IndicatorList = MatchRuleAnlaysisUtils.getNeedIndicatorFromAna(analysis);
        final RecordMatchingIndicator recordMatchingIndicator = EcoreUtil.copy((RecordMatchingIndicator) IndicatorList[0]);
        BlockKeyIndicator blockKeyIndicator = EcoreUtil.copy((BlockKeyIndicator) IndicatorList[1]);
        ExecuteMatchRuleHandler execHandler = new ExecuteMatchRuleHandler();
        MatchGroupResultConsumer matchResultConsumer = createMatchGroupResultConsumer(recordMatchingIndicator);
        // Set match key schema to the record matching indicator.
        MetadataColumn[] completeColumnSchema = AnalysisRecordGroupingUtils.getCompleteColumnSchema(columnMap);
        String[] colSchemaString = new String[completeColumnSchema.length];
        int idx = 0;
        for (MetadataColumn metadataCol : completeColumnSchema) {
            colSchemaString[idx++] = metadataCol.getName();
        }
        recordMatchingIndicator.setMatchRowSchema(colSchemaString);
        recordMatchingIndicator.reset();

        TypedReturnCode<MatchGroupResultConsumer> execute = execHandler.execute(columnMap, recordMatchingIndicator, matchRows,
                blockKeyIndicator, matchResultConsumer);
        if (!execute.isOk()) {
            rc.setMessage(DefaultMessagesImpl.getString(
                    "RunAnalysisAction.failRunAnalysis", analysis.getName(), execute.getMessage())); //$NON-NLS-1$ 
            return rc;
        } else {
            if (execute.getObject().getFullMatchResult() == null) {
                return rc;
            }
            // TDQ-9741, the "chart" result must be stored for hiding group(not compute again)
            tableResult = execute.getObject().getFullMatchResult();
            groupSize2groupFrequency = recordMatchingIndicator.getGroupSize2groupFrequency();
        }
        rc.setOk(true);
        rc.setObject(recordMatchingIndicator);
        return rc;

    }

    protected List<Object[]> getTableResult() {
        return tableResult;
    }

    protected TreeMap<Object, Long> getChartResult() {
        return groupSize2groupFrequency;
    }

    /**
     * DOC zhao Comment method "initRecordMatchIndicator".
     * 
     * @param columnMap
     * @return
     */
    private MatchGroupResultConsumer createMatchGroupResultConsumer(final RecordMatchingIndicator recordMatchingIndicator) {

        MatchGroupResultConsumer matchResultConsumer = new MatchGroupResultConsumer(true) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.dataquality.record.linkage.grouping. MatchGroupResultConsumer#handle(java.lang.Object)
             */
            @Override
            public void handle(Object row) {
                recordMatchingIndicator.handle(row);
                addOneRowOfResult(row);
            }
        };
        return matchResultConsumer;
    }

}
