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
package org.talend.dq.analysis;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchAnalysisHandler extends AnalysisHandler {

    private static Logger log = Logger.getLogger(MatchAnalysisHandler.class);

    private ModelElement[] selectedColumns = null;

    private DataManager connection;

    private boolean isChangeConnection = false;

    @Override
    public void setAnalysis(Analysis columnAnalysis) {
        super.setAnalysis(columnAnalysis);
        initSelectedColumns();
        initConnection();
    }

    /**
     * DOC yyin Comment method "initConnection".
     */
    private void initConnection() {
        connection = analysis.getContext().getConnection();
        // Added TDQ-8267, when the connection is null, need to clear keys
        if (connection == null) {
            clearAllKeys();
        }
    }

    public void SetConnection(DataManager newConnection) {
        this.connection = newConnection;
        isChangeConnection = Boolean.TRUE;
    }

    public DataManager getConnection() {
        return this.connection;
    }

    public void updateAnaConnRelationship(TDQAnalysisItem analysisItem) {
        assert analysisItem.getAnalysis() != null;
        assert analysisItem.getAnalysis() != null;
        // remove the old dependencies if any
        if (isChangeConnection && analysisItem.getAnalysis().getContext().getConnection() != null) {
            DependenciesHandler.getInstance().removeConnDependencyAndSave(analysisItem);
        }// ~
        analysisItem.getAnalysis().getContext().setConnection(connection);

        // Added TDQ-8183 add db dependency on match analysis
        if (isChangeConnection && connection != null) {
            TypedReturnCode<Dependency> rc = DependenciesHandler.getInstance().setDependencyOn(analysisItem.getAnalysis(),
                    connection);
            if (!rc.isOk()) {
                log.info("fail to save dependency analysis:" + analysisItem.getAnalysis().getFileName());//$NON-NLS-1$
            }// ~
        }
        // TDQ-11710 after update, should set this back to false.
        isChangeConnection = Boolean.FALSE;
    }

    /**
     * DOC yyin Comment method "initSelectedColumns".
     */
    private void initSelectedColumns() {
        EList<ModelElement> analyzedColumns = getAnalyzedColumns();
        selectedColumns = new ModelElement[analyzedColumns.size()];
        int i = 0;
        for (ModelElement element : analyzedColumns) {
            selectedColumns[i++] = element;
        }
    }

    public ModelElement[] getSelectedColumns() {
        return this.selectedColumns;
    }

    public void setSelectedColumns(ModelElement[] columns) {
        this.selectedColumns = columns;
    }

    public boolean addColumnToAnalyze(ModelElement modelElement) {
        assert analysis != null;
        assert analysis.getContext() != null;
        return analysis.getContext().getAnalysedElements().add(modelElement);
    }

    public boolean addColumnsToAnalyze(Collection<ModelElement> modelElement) {
        assert analysis != null;
        assert analysis.getContext() != null;
        return analysis.getContext().getAnalysedElements().addAll(modelElement);
    }

    // modify the analysis's analyzed elements
    public boolean saveSelectedAnalyzedElements() {
        assert analysis != null;
        assert analysis.getContext() != null;
        analysis.getContext().getAnalysedElements().clear();
        if (selectedColumns == null) {
            return true;
        }
        return analysis.getContext().getAnalysedElements().addAll(Arrays.asList(selectedColumns));
    }

    /**
     * when the columns is empty, clear all keys.
     */
    public void clearAllKeys() {
        EList<Indicator> indicators = this.analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            if (indicator instanceof RecordMatchingIndicator) {
                RecordMatchingIndicator matchIndicator = (RecordMatchingIndicator) indicator;
                matchIndicator.getBuiltInMatchRuleDefinition().getBlockKeys().clear();
                matchIndicator.getBuiltInMatchRuleDefinition().getMatchRules().clear();
            }
        }

    }

}
