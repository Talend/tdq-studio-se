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
package org.talend.dataprofiler.core.ui.wizard.analysis.column;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.PlatformUI;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.MatchAnaColumnContentProvider;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.dq.nodes.ColumnSetRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchWizard extends ColumnWizard {

    private ColumnAnalysisDOSelectionPage selectionPage;

    // Added TDQ-8647, used for judge the status of button Finish
    private AnalysisMetadataWizardPage metadataPage;

    /**
     * MatchWizard constructor comment.
     * 
     * @param parameter
     */
    public MatchWizard(AnalysisParameter parameter) {
        super(parameter);
    }

    // make the next button available and the next page is to select columns
    @Override
    public void addPages() {
        metadataPage = new AnalysisMetadataWizardPage();
        addPage(metadataPage);
        AnalysisParameter parameter = (AnalysisParameter) getParameter();
        if (parameter.getConnectionRepNode() == null && parameter.getAnalysisType().equals(AnalysisType.MATCH_ANALYSIS)) {
            selectionPage = new ColumnAnalysisDOSelectionPage(new MatchAnaColumnContentProvider(true));
            addPage(selectionPage);
        }
        for (IWizardPage page : getExtenalPages()) {
            addPage(page);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#openEditor(org.talend.repository.model.IRepositoryNode
     * )
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        super.openEditor(repNode);

        if (selectionPage != null) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                updateAnalysisBySelectedNode(editor);
            }
        }
    }

    // when calling this method, the validation of the selection has been approved.the nodes will only contains one
    // column set, or some columns from one same column set.
    private void updateAnalysisBySelectedNode(AnalysisEditor editor) {
        MatchAnalysisDetailsPage masterPage = (MatchAnalysisDetailsPage) editor.getMasterPage();
        List<IRepositoryNode> nodes = selectionPage.nodes;

        if (nodes != null && nodes.size() > 0) {
            // if the first selected node is not column type(there should only one column set in the selected node then)
            if (!(nodes.get(0) instanceof ColumnRepNode)) {
                nodes = ((ColumnSetRepNode) nodes.get(0)).getAllColumns();
            }

            // update analyze data label by selected nodes names(don't cotain columnRepNode).
            if (nodes.size() > 0) {
                masterPage.updateAnalyzeDataLabel(nodes.get(0));
            }
            // give the selected columns to the master page
            masterPage.setSelectedNodes(nodes.toArray(new RepositoryNode[nodes.size()]));
            masterPage.doSave(new NullProgressMonitor());
            masterPage.updateAllColumnsToKeySection();
        }
    }

    /**
     * Added TDQ-8647 20140220 yyin: check the selected nodes, only when the selection is allowed, enable the Finish
     * button; else disable it. MOD 20140226: when only the name is valid, make the Finish enable, which select none
     * nodes.
     */
    @Override
    public boolean canFinish() {
        if (selectionPage != null) {
            List<IRepositoryNode> nodes = selectionPage.nodes;
            if (metadataPage.isPageComplete() && (nodes == null || nodes.size() == 0)) {
                return true;
            }
            return RepNodeUtils.isValidSelectionFromSameTable(nodes);
        }

        return super.canFinish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnSetWizard#initCWMResourceBuilder()
     */
    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();

        // New blocking key indicator.
        Indicator blockKeyIndicator = ColumnsetFactory.eINSTANCE.createBlockKeyIndicator();
        analysis.getResults().getIndicators().add(blockKeyIndicator);

        // Match rule indicator
        RecordMatchingIndicator matchRuleIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition matchRuleDefinition = RulesFactory.eINSTANCE.createMatchRuleDefinition();
        matchRuleIndicator.setBuiltInMatchRuleDefinition(matchRuleDefinition);
        analysis.getResults().getIndicators().add(matchRuleIndicator);

        // default loaded row count
        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
        int maxRows = preferenceStore.getInt(PluginConstant.MAX_NB_ROWS_ANALYSIS_EDITOR);
        TaggedValueHelper.setTaggedValue(analysis, TaggedValueHelper.PREVIEW_ROW_NUMBER, String.valueOf(maxRows));
        return analysis;
    }

}
