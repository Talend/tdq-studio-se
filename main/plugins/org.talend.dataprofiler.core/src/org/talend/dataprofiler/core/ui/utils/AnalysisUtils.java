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
package org.talend.dataprofiler.core.ui.utils;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.general.Project;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultBAWDataset;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SummaryStatisticsState;
import org.talend.dataprofiler.core.ui.events.BenfordFrequencyDynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.DynamicBAWChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.events.FrequencyDynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.PatternDynamicChartEventReceiver;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * the Analysis's utility class which associated with UI.
 */
public class AnalysisUtils {

    /**
     * used for table analysis-- select dq rules, add filter for match rule folder TDQ-8001
     * 
     * @return
     */
    public static ViewerFilter createRuleFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    IFile file = (IFile) element;
                    if (FactoriesUtil.DQRULE.equals(file.getFileExtension())) {
                        return true;
                    }
                } else if (element instanceof IFolder) {
                    IFolder folder = (IFolder) element;
                    // filter the match rule folder in table analysis
                    if (EResourceConstant.RULES_MATCHER.getName().equals(folder.getName())) {
                        return false;
                    }// ~
                    return ResourceService.isSubFolder(ResourceManager.getRulesFolder(), folder);
                }
                return false;
            }
        };
    }

    /**
     * create a DynamicChart Event Receiver, according to its type
     * 
     * @param categoryDataset
     * @param index
     * @param oneIndicator
     * @param eIndicatorChartType
     * @return
     */
    public static DynamicChartEventReceiver createDynamicChartEventReceiver(DynamicIndicatorModel indicatorModel, int index,
            Indicator oneIndicator) {
        DynamicChartEventReceiver eReceiver = null;
        if (isBenford(indicatorModel.getChartType())) {
            eReceiver = new BenfordFrequencyDynamicChartEventReceiver();
            ((BenfordFrequencyDynamicChartEventReceiver) eReceiver).setSecondDataset(indicatorModel.getSecondDataset());
        } else if (isFrequency(indicatorModel.getChartType())) {
            // TDQ-12870, should clear the last result
            oneIndicator.setCount(0l);
            eReceiver = new FrequencyDynamicChartEventReceiver();
        } else if (isPattern(indicatorModel.getChartType())) {
            eReceiver = new PatternDynamicChartEventReceiver();
        } else {
            eReceiver = new DynamicChartEventReceiver();
        }
        eReceiver.setDataset(indicatorModel.getDataset());
        eReceiver.setIndexInDataset(index);
        eReceiver.setIndicatorName(oneIndicator.getName());

        eReceiver.setIndicator(oneIndicator);
        return eReceiver;
    }

    /**
     * Judge whether chartType is pattern
     * 
     * @param chartType
     * @return true when chart type is sql pattern regex pattern or udi matching
     */
    private static boolean isPattern(EIndicatorChartType chartType) {
        switch (chartType) {
        case SQL_PATTERN_MATCHING:
        case PATTERN_MATCHING:
        case UDI_MATCHING:
            return true;
        default:
            break;
        }
        return false;
    }

    /**
     * DOC yyin Comment method "isBenford".
     * 
     * @param eIndicatorChartType
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    private static boolean isFrequency(EIndicatorChartType eIndicatorChartType) {
        switch (eIndicatorChartType) {
        case BIN_FREQUENCE_STATISTICS:
        case BIN_LOW_FREQUENCE_STATISTICS:
        case DATE_FREQUENCE_STATISTICS:
        case DATE_LOW_FREQUENCE_STATISTICS:
        case FREQUENCE_STATISTICS:
        case LOW_FREQUENCE_STATISTICS:
        case SOUNDEX_FREQUENCY_TABLE:
        case SOUNDEX_LOW_FREQUENCY_TABLE:
        case UDI_FREQUENCY:
        case WEEK_FREQUENCE_STATISTICS:
        case WEEK_LOW_FREQUENCE_STATISTICS:
        case MONTH_FREQUENCE_STATISTICS:
        case MONTH_LOW_FREQUENCE_STATISTICS:
        case QUARTER_FREQUENCE_STATISTICS:
        case QUARTER_LOW_FREQUENCE_STATISTICS:
        case YEAR_FREQUENCE_STATISTICS:
        case YEAR_LOW_FREQUENCE_STATISTICS:
        case MODE_INDICATOR:
        case PATTERN_FREQUENCE_STATISTICS:
        case EAST_ASIA_PATTERN_FREQUENCE_STATISTICS:
        case EAST_ASIA_PATTERN_LOW_FREQUENCE_STATISTICS:
        case PATTERN_LOW_FREQUENCE_STATISTICS:
            return true;
        }
        return false;
    }

    /**
     * DOC yyin Comment method "isFrequency".
     * 
     * @param eIndicatorChartType
     * @return
     */
    private static boolean isBenford(EIndicatorChartType eIndicatorChartType) {
        if (EIndicatorChartType.BENFORD_LAW_STATISTICS.equals(eIndicatorChartType)) {
            return true;
        }
        return false;
    }

    /**
     * DOC yyin Comment method "createDynamicBAWChartEventReceiver".
     * 
     * @param oneCategoryIndicatorModel
     * @param categoryDataset
     * @return
     */
    public static DynamicBAWChartEventReceiver createDynamicBAWChartEventReceiver(
            DynamicIndicatorModel oneCategoryIndicatorModel, Map<Indicator, EventReceiver> eventReceivers) {
        DynamicBAWChartEventReceiver bawReceiver = new DynamicBAWChartEventReceiver();
        Object categoryDataset = oneCategoryIndicatorModel.getDataset();
        if (categoryDataset instanceof CustomerDefaultBAWDataset) {
            // all summary selected
            bawReceiver.setBawDataset((CustomerDefaultBAWDataset) categoryDataset);
        } else {
            // not-all summary selected
            bawReceiver.setDataset(categoryDataset);
        }
        bawReceiver.setBAWparentComposite(oneCategoryIndicatorModel.getBawParentChartComp());
        bawReceiver.setTableViewer(oneCategoryIndicatorModel.getTableViewer());
        int index = 0;
        int length = oneCategoryIndicatorModel.getSummaryIndicators().size();
        for (Indicator oneIndicator : oneCategoryIndicatorModel.getSummaryIndicators()) {
            DynamicChartEventReceiver eReceiver = bawReceiver.createEventReceiver(
                    IndicatorEnum.findIndicatorEnum(oneIndicator.eClass()), oneIndicator);
            eReceiver.setTableViewer(oneCategoryIndicatorModel.getTableViewer());
            eReceiver.setEntityIndex(index++);
            if (SummaryStatisticsState.FULL_FLAG != length) {
                eReceiver.setDataset(categoryDataset);
            }
            eReceiver.clearValue();
            eventReceivers.put(oneIndicator, eReceiver);
            EventManager.getInstance().register(oneIndicator, EventEnum.DQ_DYMANIC_CHART, eReceiver);
        }
        bawReceiver.clearValue();
        return bawReceiver;
    }

    /**
     * create a Dynamic Model for one category of indicators, who related to the same chart.
     * 
     * @param chartType
     * @param indicators
     * @param chart
     * @return
     */
    public static DynamicIndicatorModel createDynamicModel(EIndicatorChartType chartType, List<Indicator> indicators, Object chart) {
        DynamicIndicatorModel dyModel = new DynamicIndicatorModel();

        // one dataset <--> several indicators in same category
        if (chart != null) {
            Object dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, -1);
            // Added TDQ-8787 20140612 : store the dataset, and the index of the current indicator
            if (EIndicatorChartType.BENFORD_LAW_STATISTICS.equals(chartType)) {
                dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, 1);
                dyModel.setSecondDataset(TOPChartUtils.getInstance().getDatasetFromChart(chart, 0));
            }
            dyModel.setDataset(dataset);
        }
        dyModel.setIndicatorList(indicators);
        dyModel.setChartType(chartType);
        return dyModel;
    }

    /**
     * get Analysis Select Dialog Input Data.
     * 
     * @param eResourceConstant
     * @return
     */
    public static DQRepositoryNode getAnalysisSelectDialogInputData(EResourceConstant eResourceConstant) {
        DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                .getCurrentProject());
        node.getChildren().clear();
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            IRepositoryNode analysisFolderNode = RepositoryNodeHelper.getDataProfilingFolderNode(eResourceConstant);
            node.getChildren().add(analysisFolderNode);
        } else {
            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                IRepositoryNode analysisFolderNode = RepositoryNodeHelper.getDataProfilingFolderNode(eResourceConstant, project);
                node.getChildren().add(analysisFolderNode);
            }
        }
        return node;
    }

    /**
     * get Analysis Select Dialog Input Data.
     * 
     * @param eResourceConstant
     * @return
     */
    public static DQRepositoryNode getAnalysisSelectDialogInputDataWithoutRef(EResourceConstant eResourceConstant) {
        DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                .getCurrentProject());
        node.getChildren().clear();

        IRepositoryNode analysisFolderNode = RepositoryNodeHelper.getDataProfilingFolderNode(eResourceConstant);
        node.getChildren().add(analysisFolderNode);
        return node;
    }

    /**
     * get Select Dialog Input Data.
     * 
     * @param eResourceConstant: for select UDI dialog is EResourceConstant.USER_DEFINED_INDICATORS
     * @return DQRepositoryNode
     */
    public static DQRepositoryNode getSelectDialogInputData(EResourceConstant eResourceConstant) {
        DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                .getCurrentProject());
        node.getChildren().clear();
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            IRepositoryNode librariesFolderNode = RepositoryNodeHelper.getLibrariesFolderNode(eResourceConstant);
            node.getChildren().add(librariesFolderNode);
        } else {
            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                IRepositoryNode librariesFolderNode = RepositoryNodeHelper.getLibrariesFolderNode(eResourceConstant, project);
                node.getChildren().add(librariesFolderNode);
            }
        }
        return node;
    }

    /**
     * get Metadata Select Dialog Input Data.
     * 
     * @param eResourceConstant
     * @return DQRepositoryNode
     */
    public static DQRepositoryNode getMetadataSelectDialogInputData(EResourceConstant eResourceConstant) {
        DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                .getCurrentProject());
        node.getChildren().clear();
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            IRepositoryNode librariesFolderNode = RepositoryNodeHelper.getMetadataFolderNode(eResourceConstant);
            node.getChildren().add(librariesFolderNode);
        } else {
            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                IRepositoryNode librariesFolderNode = RepositoryNodeHelper.getMetadataFolderNode(eResourceConstant, project);
                node.getChildren().add(librariesFolderNode);
            }
        }
        return node;
    }

}
