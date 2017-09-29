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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.dqrule.DQRuleUtilities;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.analysis.parameters.NamedColumnSetAnalysisParameter;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableAnalysisWizard extends AbstractAnalysisWizard {

    private TableAnalysisMetadataWizardPage analysisMetadataWizardPage = null;

    @SuppressWarnings("deprecation")
    private TableAnalysisDPSelectionPage tableAnalysisDPSelectionPage = null;

    private DQRuleSelectPage dqruleSelectPage = null;

    public boolean showTableSelectPage = true;

    public boolean isShowTableSelectPage() {
        return showTableSelectPage;
    }

    public void setShowTableSelectPage(boolean showTableSelectPage) {
        this.showTableSelectPage = showTableSelectPage;
    }

    public NamedColumnSet[] getNamedColumnSet() {
        return getParameter() == null ? null : getParameter().getNamedColumnSets();
    }

    public void setNamedColumnSet(NamedColumnSet[] namedColumnSet) {
        if (getParameter() != null) {
            getParameter().setNamedColumnSets(namedColumnSet);
        }
    }

    public Connection getTdDataProvider() {
        return getParameter() == null ? null : getParameter().getTdDataProvider();
    }

    public void setTdDataProvider(Connection tdDataProvider) {
        if (getParameter() != null) {
            getParameter().setTdDataProvider(tdDataProvider);
        }
    }

    @Override
    public boolean canFinish() {
        return analysisMetadataWizardPage == null ? false : analysisMetadataWizardPage.isPageComplete();
    }

    /**
     * DOC xqliu TableAnalysisWizard constructor comment.
     * 
     * @param parameter
     */
    public TableAnalysisWizard(AnalysisFilterParameter parameter) {
        super(parameter);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addPages() {
        this.getParameter().setName(""); //$NON-NLS-1$

        analysisMetadataWizardPage = new TableAnalysisMetadataWizardPage();
        this.addPage(analysisMetadataWizardPage);

        // never set true currently
        if (isShowTableSelectPage()) {
            tableAnalysisDPSelectionPage = new TableAnalysisDPSelectionPage();
            addPage(tableAnalysisDPSelectionPage);
        }
        dqruleSelectPage = new DQRuleSelectPage();
        addPage(dqruleSelectPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#createAndSaveCWMFile(orgomg.cwm.objectmodel
     * .core.ModelElement)
     */
    @Override
    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement cwmElement) {
        Analysis analysis = (Analysis) cwmElement;
        DataManager connection = analysis.getContext().getConnection();
        if (connection != null) {
            DependenciesHandler.getInstance().setDependencyOn(analysis, connection);
        }

        // MOD by hcheng for 7173:Broken dependency between analyses and connection
        TypedReturnCode<Object> saveCWMFile = super.createAndSaveCWMFile(analysis);

        if (saveCWMFile.isOk() && connection != null) {
            ElementWriterFactory.getInstance().createDataProviderWriter().save(connection);
        }

        return saveCWMFile;
    }

    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();
        NamedColumnSet[] ncss = getNamedColumnSet();
        Connection tdp = getTdDataProvider();

        if (ncss != null && getAnalysisBuilder() != null) {
            List<Indicator> indicatorList = new ArrayList<Indicator>();
            WhereRule[] whereRules = getWhereRules(dqruleSelectPage.getCViewer().getCheckedElements());

            for (NamedColumnSet ncs : ncss) {
                // add RowCountIndicator
                RowCountIndicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
                DefinitionHandler.getInstance().setDefaultIndicatorDefinition(rowCountIndicator);
                rowCountIndicator.setAnalyzedElement(ncs);
                indicatorList.add(rowCountIndicator);
                // add user selected WhereRuleIndicator
                if (whereRules != null) {
                    for (WhereRule whereRule : whereRules) {
                        WhereRuleIndicator[] compositeWhereRuleIndicator = DQRuleUtilities.createCompositeWhereRuleIndicator(ncs,
                                whereRule);
                        indicatorList.addAll(Arrays.asList(compositeWhereRuleIndicator));
                    }
                }
            }
            getAnalysisBuilder().addElementsToAnalyze(ncss, indicatorList.toArray(new Indicator[indicatorList.size()]));
            getAnalysisBuilder().setAnalysisConnection(tdp);
        }
        return analysis;
    }

    /**
     * DOC xqliu Comment method "getWhereRules".
     * 
     * @param checkedElements
     * @return
     */
    private WhereRule[] getWhereRules(Object[] objects) {
        if (objects != null) {
            List<WhereRule> wrList = new ArrayList<WhereRule>();
            for (Object object : objects) {
                if (object instanceof IFile) {
                    WhereRule wr = DQRuleResourceFileHelper.getInstance().findWhereRule((IFile) object);
                    if (wr != null) {
                        wrList.add(wr);
                    }
                }
            }
            return wrList.toArray(new WhereRule[wrList.size()]);
        }
        return null;
    }

    @Override
    public NamedColumnSetAnalysisParameter getParameter() {
        return (NamedColumnSetAnalysisParameter) super.getParameter();
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
        AnalysisItemEditorInput itemEditorInput = new AnalysisItemEditorInput(repNode);
        CorePlugin.getDefault().openEditor(itemEditorInput, AnalysisEditor.class.getName());
    }
}
