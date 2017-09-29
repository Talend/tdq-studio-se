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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.action.actions.RunAnalysisAction;
import org.talend.dataprofiler.core.ui.events.DynamicBAWChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * DOC yyin class global comment. Detailled comment
 */
public abstract class DynamicAnalysisMasterPage extends AbstractAnalysisMetadataPage {

    protected SashForm sForm;

    protected Composite previewComp;

    protected Section previewSection = null;

    protected Composite chartComposite;

    abstract List<ExpandableComposite> getPreviewChartList();

    protected Map<Indicator, EventReceiver> eventReceivers = new IdentityHashMap<Indicator, EventReceiver>();

    private EventReceiver registerDynamicRefreshEvent;

    private EventReceiver switchBetweenPageEvent;

    /**
     * DOC yyin DynamicAnalysisMasterPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public DynamicAnalysisMasterPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    protected void createPreviewComposite() {
        previewComp = toolkit.createComposite(sForm);
        previewComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        previewComp.setLayout(new GridLayout());
        // add by hcheng for 0007290: Chart cannot auto compute it's size in
        // DQRule analsyis Editor
        previewComp.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                super.controlResized(e);
                sForm.redraw();
                form.reflow(true);
            }
        });
    }

    void createPreviewSection(final ScrolledForm form1, Composite parent) {
        previewSection = createSection(
                form1,
                parent,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.graphics"), DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$ //$NON-NLS-2$
        previewSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite sectionClient = toolkit.createComposite(previewSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite actionBarComp = toolkit.createComposite(sectionClient);
        GridLayout gdLayout = new GridLayout();
        gdLayout.numColumns = 3;
        actionBarComp.setLayout(gdLayout);

        createCollapseAllLink(actionBarComp);

        createExpandAllLink(actionBarComp);

        Button chartButton = new Button(actionBarComp, SWT.NONE);
        chartButton.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.refreshGraphics")); //$NON-NLS-1$
        final Label message = toolkit.createLabel(sectionClient,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.spaceWhite")); //$NON-NLS-1$
        message.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        message.setVisible(false);
        chartButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                disposeChartComposite();

                Analysis analysis = getAnalysisHandler().getAnalysis();

                boolean analysisStatue = analysis.getResults().getResultMetadata() != null
                        && analysis.getResults().getResultMetadata().getExecutionDate() != null;

                if (!analysisStatue) {
                    boolean returnCode = MessageDialog.openConfirm(null,
                            DefaultMessagesImpl.getString("ColumnMasterDetailsPage.ViewResult"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("ColumnMasterDetailsPage.RunOrSeeSampleData")); //$NON-NLS-1$

                    if (returnCode) {
                        new RunAnalysisAction().run();
                        message.setVisible(false);
                    } else {
                        createPreviewCharts(form1, chartComposite);
                        message.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.warning")); //$NON-NLS-1$
                        message.setVisible(true);
                    }
                } else {
                    createPreviewCharts(form1, chartComposite);
                }

                reLayoutChartComposite();
            }
        });

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        chartComposite = toolkit.createComposite(sectionClient);
        chartComposite.setLayout(new GridLayout());
        chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewSection.setClient(sectionClient);
    }

    private void createExpandAllLink(Composite actionBarComp) {
        ImageHyperlink expandAllImageLink = toolkit.createImageHyperlink(actionBarComp, SWT.NONE);
        expandAllImageLink.setToolTipText(getExpandString());
        WorkbenchUtils.setHyperlinkImage(expandAllImageLink, ImageLib.getImage(ImageLib.EXPAND_ALL));
        expandAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                List<ExpandableComposite> previewChartList = getPreviewChartList();
                if (previewChartList != null && !previewChartList.isEmpty()) {
                    for (ExpandableComposite comp : previewChartList) {
                        comp.setExpanded(true);
                        comp.getParent().pack();
                    }
                }
                if (getChartComposite() != null) {
                    getChartComposite().getParent().pack();
                }
            }
        });
    }

    public abstract void createPreviewCharts(final ScrolledForm form1, final Composite composite);

    abstract String getExpandString();

    abstract String getCollapseAllString();

    private void createCollapseAllLink(Composite actionBarComp) {
        ImageHyperlink collapseAllImageLink = toolkit.createImageHyperlink(actionBarComp, SWT.NONE);
        collapseAllImageLink.setToolTipText(getCollapseAllString());
        WorkbenchUtils.setHyperlinkImage(collapseAllImageLink, ImageLib.getImage(ImageLib.COLLAPSE_ALL));
        collapseAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                List<ExpandableComposite> previewChartList = getPreviewChartList();
                if (previewChartList != null && !previewChartList.isEmpty()) {
                    for (ExpandableComposite comp : previewChartList) {
                        comp.setExpanded(false);
                        comp.getParent().pack();
                    }
                }
            }
        });
    }

    /**
     * Added TDQ-8787 20140613 yyin: create all charts before running, register each chart with its related indicator.
     */
    public void registerDynamicEvent() {
        // only worked for the analysis which support dynamic chart
        // create all charts for related indicator in current page
        createDynamicChartsBeforeRun();

        // get all indicators and datasets
        List<DynamicIndicatorModel> indiAndDatasets = getDynamicDatasets();

        // register dynamic event,for the indicator (for each column)
        for (DynamicIndicatorModel oneCategoryIndicatorModel : indiAndDatasets) {
            if (EIndicatorChartType.SUMMARY_STATISTICS.equals(oneCategoryIndicatorModel.getChartType())) {
                // when all summary indicators are selected
                DynamicBAWChartEventReceiver bawReceiver = AnalysisUtils.createDynamicBAWChartEventReceiver(
                        oneCategoryIndicatorModel, eventReceivers);
                bawReceiver.setChartComposite(chartComposite);
                // no need to register the parent baw receiver with one of summary indicator, no need to handle baw
                // actually
            } else {
                int index = 0;
                for (Indicator oneIndicator : oneCategoryIndicatorModel.getIndicatorList()) {
                    // if the indicator is a frequency indicator, create a Frequency Event Receiver
                    DynamicChartEventReceiver eReceiver = createEventReceiver(oneCategoryIndicatorModel, index++, oneIndicator);
                    eReceiver.setChartComposite(chartComposite);
                    // clear data
                    eReceiver.clearValue();

                    registerIndicatorEvent(oneIndicator, eReceiver);
                }
            }
        }
        reLayoutChartComposite();

        registerOtherDynamicEvent();
    }

    /**
     * DOC yyin Comment method "createEventReceiver".
     * 
     * @param categoryDataset
     * @param index
     * @param oneIndicator
     * @param eIndicatorChartType
     * @return
     */
    protected DynamicChartEventReceiver createEventReceiver(DynamicIndicatorModel indicatorModel, int index,
            Indicator oneIndicator) {
        return AnalysisUtils.createDynamicChartEventReceiver(indicatorModel, index, oneIndicator);
    }

    private void registerIndicatorEvent(Indicator oneIndicator, DynamicChartEventReceiver eReceiver) {
        eventReceivers.put(oneIndicator, eReceiver);
        EventManager.getInstance().register(oneIndicator, EventEnum.DQ_DYMANIC_CHART, eReceiver);
    }

    public void reLayoutChartComposite() {
        if (chartComposite != null) {
            chartComposite.getParent().pack();
        }
    }

    /**
     * refresh the composite of the chart, to show the changes on the chart.
     */
    private void registerOtherDynamicEvent() {
        registerDynamicRefreshEvent = new EventReceiver() {

            @Override
            public boolean handle(Object data) {
                reLayoutChartComposite();
                return true;
            }
        };
        EventManager.getInstance().register(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART,
                registerDynamicRefreshEvent);

        // register a event to handle switch between master and result page
        switchBetweenPageEvent = new EventReceiver() {

            int times = 0;

            @Override
            public boolean handle(Object data) {
                // only need to refresh for the first time switch, and must be during a running.
                if (times == 0) {
                    times++;
                    ((AnalysisEditor) currentEditor).getResultPage().refresh(((AnalysisEditor) currentEditor).getMasterPage());
                }
                return true;
            }
        };
        EventManager.getInstance().register(getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE,
                switchBetweenPageEvent);

    }

    /**
     * unregister every dynamic events which registered before executing analysis
     * 
     * @param eventReceivers
     */
    public void unRegisterDynamicEvent() {
        EventManager.getInstance().clearEvent(getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE);

        for (Indicator oneIndicator : eventReceivers.keySet()) {
            DynamicChartEventReceiver eventReceiver = (DynamicChartEventReceiver) eventReceivers.get(oneIndicator);
            eventReceiver.clear();
            EventManager.getInstance().clearEvent(oneIndicator, EventEnum.DQ_DYMANIC_CHART);
        }
        eventReceivers.clear();
        EventManager.getInstance().clearEvent(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART);

        clearDynamicDatasets();
    }

    /**
     * dispose ChartComposite.
     */
    public void disposeChartComposite() {
        if (chartComposite != null && !chartComposite.isDisposed()) {
            for (Control control : chartComposite.getChildren()) {
                control.dispose();
            }
        }
    }

    public void createDynamicChartsBeforeRun() {
        // call refresh to create all charts
        refreshGraphicsInSettingsPage();

    }

    // should be implemented in child classes
    abstract public List<DynamicIndicatorModel> getDynamicDatasets();

    public void clearDynamicDatasets() {
        // make the run button workable again
        ((AnalysisEditor) currentEditor).setRunActionButtonState(true);

    }
}
