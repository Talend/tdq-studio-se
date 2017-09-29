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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.events.DynamicBAWChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.utils.pagination.UIPagination;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ColumnAnalysisResultPage extends AbstractAnalysisResultPage implements PropertyChangeListener {

    private Composite resultComp;

    ColumnAnalysisDetailsPage masterPage;

    private Section resultSection = null;

    private UIPagination uiPagination = null;

    protected Map<Indicator, EventReceiver> eventReceivers = new IdentityHashMap<Indicator, EventReceiver>();

    private EventReceiver registerDynamicRefreshEvent;

    private EventReceiver switchBetweenPageEvent;

    private Composite chartComposite;

    /**
     * DOC zqin ColumnAnalysisResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public ColumnAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (ColumnAnalysisDetailsPage) analysisEditor.getMasterPage();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            resultComp = toolkit.createComposite(topComposite);
            resultComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            resultComp.setLayout(new GridLayout());
            createResultSection(resultComp);
            form.reflow(true);
        }
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    /**
     * create CollapseAll Link for result section.
     * 
     * @param composite
     */
    private void createCollapseAllLink(Composite composite) {
        ImageHyperlink collapseAllImageLink = toolkit.createImageHyperlink(composite, SWT.NONE);
        collapseAllImageLink.setToolTipText(DefaultMessagesImpl.getString("CollapseAllColumns")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(collapseAllImageLink, ImageLib.getImage(ImageLib.COLLAPSE_ALL));
        collapseAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                List<ExpandableComposite> expandableCompositeList = getExpandableCompositeList();
                if (expandableCompositeList != null && !expandableCompositeList.isEmpty()) {
                    for (ExpandableComposite comp : expandableCompositeList) {
                        comp.setExpanded(false);
                        comp.getParent().pack();
                    }
                }
                form.reflow(true);
            }
        });
    }

    /**
     * create ExpandAll Link for result section.
     * 
     * @param composite
     */
    private void createExpandAllLink(Composite composite) {
        ImageHyperlink expandAllImageLink = toolkit.createImageHyperlink(composite, SWT.NONE);
        expandAllImageLink.setToolTipText(DefaultMessagesImpl.getString("ExpandAllColumns")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(expandAllImageLink, ImageLib.getImage(ImageLib.EXPAND_ALL));
        expandAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                if (!resultSection.isExpanded()) {
                    resultSection.setExpanded(true);
                }
                List<ExpandableComposite> expandableCompositeList = getExpandableCompositeList();
                if (expandableCompositeList != null && !expandableCompositeList.isEmpty()) {
                    for (ExpandableComposite comp : expandableCompositeList) {
                        comp.setExpanded(true);
                        comp.getParent().pack();
                    }
                }
                form.reflow(true);
            }
        });
    }

    /**
     * DOC msjian Comment method "getExpandableCompositeList".
     * 
     * @return
     */
    protected List<ExpandableComposite> getExpandableCompositeList() {
        List<ExpandableComposite> allExpandableCompositeList = new ArrayList<ExpandableComposite>();
        if (uiPagination == null) {
            return allExpandableCompositeList;
        }
        return ((IndicatorPaginationInfo) uiPagination.getCurrentPage()).getAllExpandableCompositeList();
    }

    @Override
    protected void createResultSection(Composite parent) {
        resultSection = createSection(form, parent,
                DefaultMessagesImpl.getString("ColumnAnalysisResultPage.analysisResult"), null); //$NON-NLS-1$

        // TDQ-11525 msjian: Add "expand all" and "fold all" icon buttons in the "Analysis Results" section
        Composite collapseExpandComposite = toolkit.createComposite(resultSection);
        GridLayout gdLayout = new GridLayout();
        gdLayout.numColumns = 2;
        collapseExpandComposite.setLayout(gdLayout);
        createCollapseAllLink(collapseExpandComposite);
        createExpandAllLink(collapseExpandComposite);
        resultSection.setTextClient(collapseExpandComposite);
        // TDQ-11525

        Composite chartTableComposite = toolkit.createComposite(resultSection);
        chartTableComposite.setLayout(new GridLayout());
        chartTableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        final ModelElementIndicator[] modelElementIndicatores = masterPage.getCurrentModelElementIndicators();
        // ~ MOD mzhao 2009-04-20, Do pagination. Bug 6512.
        uiPagination = new UIPagination(toolkit, chartTableComposite);
        int pageSize = IndicatorPaginationInfo.getPageSize();
        int totalPages = modelElementIndicatores.length / pageSize;
        List<ModelElementIndicator> modelElementIndLs = null;
        for (int index = 0; index < totalPages; index++) {
            modelElementIndLs = new ArrayList<ModelElementIndicator>();
            for (int idx = 0; idx < pageSize; idx++) {
                modelElementIndLs.add(modelElementIndicatores[index * pageSize + idx]);
            }
            IndicatorPaginationInfo pginfo = new ResultPaginationInfo(form, modelElementIndLs, masterPage, uiPagination);
            uiPagination.addPage(pginfo);
        }

        int left = modelElementIndicatores.length % pageSize;
        if (left != 0) {
            modelElementIndLs = new ArrayList<ModelElementIndicator>();
            for (int leftIdx = 0; leftIdx < left; leftIdx++) {
                modelElementIndLs.add(modelElementIndicatores[totalPages * pageSize + leftIdx]);
            }
            IndicatorPaginationInfo pginfo = new ResultPaginationInfo(form, modelElementIndLs, masterPage, uiPagination);
            uiPagination.addPage(pginfo);
        }
        chartComposite = toolkit.createComposite(chartTableComposite);
        chartComposite.setLayout(new GridLayout());
        chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        // ~
        resultSection.setClient(chartTableComposite);

        uiPagination.setChartComposite(chartComposite);
        uiPagination.init();
        chartComposite.layout();
        chartComposite.pack();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        // no implementation
    }

    /*
     * (non-Javadoc)
     * 
     * @seejava.beans.PropertyChangeListener#propertyChange(java.beans. PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#refresh(org.talend.dataprofiler.core
     * .ui.editor.analysis.AbstractAnalysisMetadataPage)
     */
    @Override
    public void refresh(AbstractAnalysisMetadataPage masterPage1) {
        this.masterPage = (ColumnAnalysisDetailsPage) masterPage1;
        disposeComposite();
        createFormContent(getManagedForm());
    }

    private void disposeComposite() {
        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }
        if (resultComp != null && !resultComp.isDisposed()) {
            resultComp.dispose();
        }
    }

    /**
     * Added TDQ-8787 20140613 yyin: create all charts before running, register each chart with its related indicator.
     */
    public void registerDynamicEvent() {
        // get all indicators and datasets
        List<DynamicIndicatorModel> indiAndDatasets = uiPagination.getAllIndcatorAndDatasetOfCurrentPage();

        // register dynamic event,for the indicator (for each column)
        for (DynamicIndicatorModel oneCategoryIndicatorModel : indiAndDatasets) {
            TableViewer tableViewer = oneCategoryIndicatorModel.getTableViewer();
            if (EIndicatorChartType.SUMMARY_STATISTICS.equals(oneCategoryIndicatorModel.getChartType())) {
                // when all/not-all summary indicators are selected
                DynamicBAWChartEventReceiver bawReceiver = AnalysisUtils.createDynamicBAWChartEventReceiver(
                        oneCategoryIndicatorModel, eventReceivers);
                bawReceiver.setChartComposite(chartComposite);
                bawReceiver.setParentChartComposite((Composite) oneCategoryIndicatorModel.getBawParentChartComp());
                bawReceiver.refreshChart();
                bawReceiver.setTableViewer(tableViewer);
                // no need to register the parent baw receiver with one of summary indicator, no need to handle baw
                // actually
            } else {
                int index = 0;
                for (Indicator oneIndicator : oneCategoryIndicatorModel.getIndicatorList()) {
                    DynamicChartEventReceiver eReceiver = AnalysisUtils.createDynamicChartEventReceiver(
                            oneCategoryIndicatorModel, index++, oneIndicator);
                    eReceiver.setChartComposite(chartComposite);
                    eReceiver.refreshChart();
                    eReceiver.setTableViewer(tableViewer);
 
                    // clear data
                    eReceiver.clearValue();
                    registerIndicatorEvent(oneIndicator, eReceiver);
                }
            }
        }
        reLayoutChartComposite();

        registerOtherDynamicEvent();
    }

    private void registerIndicatorEvent(Indicator oneIndicator, DynamicChartEventReceiver eReceiver) {
        eventReceivers.put(oneIndicator, eReceiver);
        EventManager.getInstance().register(oneIndicator, EventEnum.DQ_DYMANIC_CHART, eReceiver);
    }

    public void reLayoutChartComposite() {
        chartComposite.getParent().layout();
        chartComposite.layout();
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
                if (times == 0) {
                    times++;
                    masterPage.refreshGraphicsInSettingsPage();
                }
                return true;
            }
        };
        EventManager.getInstance().register(masterPage.getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE,
                switchBetweenPageEvent);
    }

    /**
     * unregister every dynamic events which registered before executing analysis
     * 
     * @param eventReceivers
     */
    public void unRegisterDynamicEvent() {
        EventManager.getInstance()
                .clearEvent(masterPage.getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE);

        for (Indicator oneIndicator : eventReceivers.keySet()) {
            DynamicChartEventReceiver eventReceiver = (DynamicChartEventReceiver) eventReceivers.get(oneIndicator);
            eventReceiver.clear();
            EventManager.getInstance().clearEvent(oneIndicator, EventEnum.DQ_DYMANIC_CHART);
        }
        eventReceivers.clear();
        EventManager.getInstance().clearEvent(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART);

        if (uiPagination != null) {
            uiPagination.clearAllDynamicMapOfCurrentPage();
        }

        masterPage.clearDynamicDatasets();
    }

}
