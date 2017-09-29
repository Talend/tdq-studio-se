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
package org.talend.dataprofiler.core.ui.action.actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dq.analysis.AnalysisBuilder;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * Create an Analysis which analyze duplicates.
 */
public class CreateDuplicatesAnalysisAction extends Action {

    private Map<ColumnSet, List<TdColumn>> columnsMap;

    public Map<ColumnSet, List<TdColumn>> getColumnsMap() {
        return this.columnsMap;
    }

    public void setColumnsMap(Map<ColumnSet, List<TdColumn>> columnsMap) {
        this.columnsMap = columnsMap;
    }

    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * DOC xqliu CreateDuplicatesAnalysisAction constructor comment.
     * 
     * @param columnsMap
     */
    public CreateDuplicatesAnalysisAction(Map<ColumnSet, List<TdColumn>> columnsMap) {
        this.setColumnsMap(columnsMap);
        if (columnsMap != null && !columnsMap.isEmpty()) {
            this.setConnection(ConnectionHelper.getConnection(columnsMap.keySet().iterator().next()));
        }
    }

    @Override
    public void run() {
        ReturnCode success = new ReturnCode(true);

        if (this.getColumnsMap() == null || this.getColumnsMap().isEmpty() || this.getConnection() == null) {
            return;
        }

        try {
            Set<ColumnSet> keySet = this.getColumnsMap().keySet();
            for (ColumnSet cs : keySet) {
                List<TdColumn> columns = this.getColumnsMap().get(cs);
                // create the analysis
                Analysis analysis = null;
                AnalysisBuilder analysisBuilder = new AnalysisBuilder();
                boolean analysisInitialized = analysisBuilder.initializeAnalysis(getAnalysisName(cs),
                        AnalysisType.MULTIPLE_COLUMN);
                if (analysisInitialized) {
                    analysis = analysisBuilder.getAnalysis();
                }

                fillMetadataToAnalysis(analysis, createDefaultAnalysisParameter(cs));

                // add Connection into the Analysis Context
                analysis.getContext().setConnection(this.getConnection());

                for (TdColumn theColumn : columns) {
                    // add TdColumn into the Analysis Context
                    analysis.getContext().getAnalysedElements().add(theColumn);

                    // create Row Count Indicator
                    RowCountIndicator rcIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
                    rcIndicator.setAnalyzedElement(theColumn);
                    DefinitionHandler.getInstance().setDefaultIndicatorDefinition(rcIndicator);
                    analysis.getResults().getIndicators().add(rcIndicator);

                    // create Duplicate Count Indicator
                    DuplicateCountIndicator dcIndicator = IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator();
                    dcIndicator.setAnalyzedElement(theColumn);
                    DefinitionHandler.getInstance().setDefaultIndicatorDefinition(dcIndicator);
                    analysis.getResults().getIndicators().add(dcIndicator);
                }

                // create dependencies
                DependenciesHandler.getInstance().setDependencyOn(analysis, this.getConnection());
                // set the domain
                for (Domain domain : analysis.getParameters().getDataFilter()) {
                    domain.setName(analysis.getName());
                }
                // set execution language
                analysis.getParameters().setExecutionLanguage(ExecutionLanguage.SQL);

                // save the analysis
                RepositoryNode analysisRepNode = (RepositoryNode) RepositoryNodeHelper
                        .getDataProfilingFolderNode(EResourceConstant.ANALYSIS);
                IFolder folder = WorkbenchUtils.getFolder(analysisRepNode);
                AnalysisWriter analysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
                TypedReturnCode<Object> create = analysisWriter.create(analysis, folder);
                if (create.isOk()) {
                    // refresh the RepositoryView
                    CorePlugin.getDefault().refreshDQView(analysisRepNode);
                    // open the editor
                    AnalysisRepNode anaRepNode = RepositoryNodeHelper.recursiveFindAnalysis(analysis);
                    AnalysisItemEditorInput analysisEditorInput = new AnalysisItemEditorInput(anaRepNode);
                    IRepositoryNode connectionRepNode = RepositoryNodeHelper.recursiveFind(this.getConnection());
                    analysisEditorInput.setConnectionNode(connectionRepNode);
                    CorePlugin.getDefault().openEditor(analysisEditorInput, AnalysisEditor.class.getName());
                } else {
                    success.setOk(false);
                    success.setMessage(create.getMessage());
                }
            } // for
        } catch (Exception e) {
            success.setOk(false);
            success.setMessage(e.getMessage());
        }

        if (!success.isOk()) {
            MessageUI.openError(success.getMessage());
        }
    }

    /**
     * DOC xqliu Comment method "getAnalysisName".
     * 
     * @param columnSet
     * @return
     */
    private String getAnalysisName(ColumnSet columnSet) {
        String anaName = "AnalyzeDuplicatesOn_"; //$NON-NLS-1$
        anaName = anaName.concat(columnSet.getName());
        SimpleDateFormat sf = new SimpleDateFormat("_yyyyMMddHHmmss"); //$NON-NLS-1$
        anaName = anaName.concat(sf.format(new Date()));
        // check the new Analysis Name
        String finalAnaName = anaName;
        int i = 1;
        while (PropertyHelper.existDuplicateName(finalAnaName, null, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT)) {
            finalAnaName = anaName + i;
            i++;
        }
        return finalAnaName;
    }

    /**
     * DOC xqliu Comment method "fillMetadataToAnalysis".
     * 
     * @param analysis
     * @param anaParam
     */
    private void fillMetadataToAnalysis(Analysis analysis, AnalysisParameter anaParam) {
        MetadataHelper.setDevStatus(analysis, anaParam.getStatus());
        MetadataHelper.setAuthor(analysis, anaParam.getAuthor());
        MetadataHelper.setPurpose(anaParam.getPurpose(), analysis);
        MetadataHelper.setDescription(anaParam.getDescription(), analysis);
        MetadataHelper.setVersion(anaParam.getVersion(), analysis);
    }

    /**
     * DOC xqliu Comment method "createDefaultAnalysisParameter".
     * 
     * @param columnSet
     * @return
     */
    private AnalysisParameter createDefaultAnalysisParameter(ColumnSet columnSet) {
        AnalysisParameter result = new AnalysisParameter();
        result.setStatus("development"); //$NON-NLS-1$
        result.setAuthor(ProjectManager.getInstance().getCurrentProject().getAuthor().getLogin());
        result.setPurpose(""); //$NON-NLS-1$
        result.setDescription("Analysis the duplicated columns on the table " + columnSet.getName()); //$NON-NLS-1$
        result.setVersion(VersionUtils.DEFAULT_VERSION);
        return result;
    }
}
