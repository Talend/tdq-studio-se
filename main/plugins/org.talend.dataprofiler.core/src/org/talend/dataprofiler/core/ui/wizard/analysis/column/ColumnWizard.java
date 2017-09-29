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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataXmlElementTypeRepositoryObject;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnSetAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.utils.ModelElementIndicatorRule;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author zqin
 */
public class ColumnWizard extends AbstractAnalysisWizard {

    private IWizardPage[] extenalPages;

    private Indicator indicator;

    protected ColumnAnalysisDOSelectionPage selectionPage;

    public IWizardPage[] getExtenalPages() {
        if (extenalPages == null) {
            return new WizardPage[0];
        }
        return extenalPages;
    }

    public void setExtenalPages(IWizardPage[] extenalPages) {
        this.extenalPages = extenalPages;
    }

    public ColumnWizard(AnalysisParameter parameter) {
        super(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#initCWMResourceBuilder()
     */
    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();

        if (indicator != null) {
            DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator);
            analysis.getResults().getIndicators().add(indicator);
        }

        return analysis;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(new AnalysisMetadataWizardPage());
        AnalysisParameter parameter = (AnalysisParameter) getParameter();
        if (parameter.getConnectionRepNode() == null
                && (parameter.getAnalysisType().equals(AnalysisType.MULTIPLE_COLUMN) || parameter.getAnalysisType().equals(
                        AnalysisType.COLUMN_SET))) {
            selectionPage = new ColumnAnalysisDOSelectionPage();
            addPage(selectionPage);
        }
        for (IWizardPage page : getExtenalPages()) {
            addPage(page);
        }
    }

    /**
     * Sets the indicator.
     * 
     * @param indicator the indicator to set
     */
    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    /**
     * Getter for indicator.
     * 
     * @return the indicator
     */
    public Indicator getIndicator() {
        return this.indicator;
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
        if (this.selectionPage != null) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                AbstractAnalysisMetadataPage masterPage = editor.getMasterPage();
                List<IRepositoryNode> nodes = this.selectionPage.nodes;
                if (nodes != null && nodes.size() > 0) {
                    // MOD msjian TDQ-6665 2013-1-7: after the wizard, make the editor is saved status
                    if (masterPage instanceof ColumnAnalysisDetailsPage) {
                        ((ColumnAnalysisDetailsPage) masterPage)
                                .setTreeViewInput(nodes.toArray(new RepositoryNode[nodes.size()]));
                        ModelElementIndicator[] predefinedColumnIndicator = this.getPredefinedColumnIndicator();
                        if (predefinedColumnIndicator != null) {
                            ((ColumnAnalysisDetailsPage) masterPage).refreshPreviewTable(predefinedColumnIndicator, false);
                            ((ColumnAnalysisDetailsPage) masterPage).refreshTheTree(predefinedColumnIndicator);
                        }

                        // TDQ-12349: when the database can not support PatternFrequency, set to java engine automatically
                        if (this instanceof PatternFrequencyWizard && !RepNodeUtils.isSupportPatternFrequency(nodes)) {
                            masterPage.changeExecuteLanguageToJava(true);
                        }
                        // TDQ-12349~

                    } else if (masterPage instanceof ColumnSetAnalysisDetailsPage) {
                        ((ColumnSetAnalysisDetailsPage) masterPage).setTreeViewInput(nodes.toArray(new RepositoryNode[nodes
                                .size()]));
                    } else {
                        masterPage.getTreeViewer().setInput(nodes.toArray(new RepositoryNode[nodes.size()]));
                    }
                    masterPage.doSave(new NullProgressMonitor());
                    // TDQ-6665~
                }
            }
        }

    }

    /**
     * DOC msjian Comment method "getPredefinedColumnIndicator".
     * 
     * @return
     */
    protected ModelElementIndicator[] getPredefinedColumnIndicator() {
        // need to implement in the subClass
        return null;
    }

    protected ModelElementIndicator[] composePredefinedColumnIndicator(IndicatorEnum[] allowedEnum) {
        List<IRepositoryNode> nodes;
        // from the right menu
        if (selectionPage == null) {
            nodes = Arrays.asList(((AnalysisLabelParameter) parameter).getColumnNodes());
        } else {
            // TDQ-11240:　get all the column nodes
            nodes = Arrays.asList(RepositoryNodeHelper.getAllColumnNodes(selectionPage.nodes
                    .toArray(new IRepositoryNode[selectionPage.nodes.size()])));
        }
        ModelElementIndicator[] predefinedColumnIndicator = new ModelElementIndicator[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {

            IRepositoryNode columnNode = nodes.get(i);
            ModelElementIndicator columnIndicator = ModelElementIndicatorHelper.createModelElementIndicator(columnNode);

            for (IndicatorEnum oneEnum : allowedEnum) {
                columnIndicator.addTempIndicatorEnum(oneEnum);
                if (oneEnum.getChildren() != null) {
                    for (IndicatorEnum childEnum : oneEnum.getChildren()) {
                        // MOD by zshen:need language to decide DatePatternFrequencyIndicator whether can be choose by
                        // user.
                        IRepositoryViewObject object = columnNode.getObject();

                        ModelElement element = null;
                        if (object instanceof MetadataColumnRepositoryObject) {
                            element = ((MetadataColumnRepositoryObject) object).getTdColumn();
                        } else if (object instanceof MetadataXmlElementTypeRepositoryObject) {
                            element = ((MetadataXmlElementTypeRepositoryObject) object).getModelElement();
                        }

                        if (element != null && ModelElementIndicatorRule.patternRule(childEnum, element, ExecutionLanguage.SQL)) {
                            columnIndicator.addTempIndicatorEnum(childEnum);
                        }
                    }
                }
            }

            columnIndicator.storeTempIndicator();

            predefinedColumnIndicator[i] = columnIndicator;
        }

        return predefinedColumnIndicator;
    }

}
