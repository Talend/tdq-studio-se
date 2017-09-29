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
package org.talend.dq.analysis.parameters;

import org.talend.repository.model.IRepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalysisLabelParameter extends AnalysisParameter {

    public static final String NUMBERIC_CORRELATION = "Numerical Correlation Analysis"; //$NON-NLS-1$

    public static final String DATE_CORRELATION = "Time Correlation Analysis"; //$NON-NLS-1$

    public static final String NOMINAL_CORRELATION = "Nominal Correlation Analysis"; //$NON-NLS-1$

    public static final String FUNCTIONAL_DEPENDENCY_ANALYSIS = "Functional Dependency Analysis"; //$NON-NLS-1$

    public static final String CONNECTION_OVERVIEW_ANALYSIS = "Connection Overview Analysis"; //$NON-NLS-1$

    public static final String CATALOG_OVERVIEW_ANALYSIS = "Catalog Overview Analysis"; //$NON-NLS-1$

    public static final String SCHEMA_OVERVIEW_ANALYSIS = "Schema Overview Analysis"; //$NON-NLS-1$

    public static final String SEMANTIC_DISCOVERY_ANALYSIS = "Semantic-aware Analysis"; //$NON-NLS-1$

    public static final String BASIC_COLUMN_ANALYSIS = "Basic Column Analysis"; //$NON-NLS-1$

    public static final String NOMINAL_VALUES_ANALYSIS = "Nominal Values Analysis"; //$NON-NLS-1$

    public static final String PATTERN_FREQUENCY_ANALYSIS = "Pattern Frequency Analysis"; //$NON-NLS-1$

    public static final String DISCRETE_DATA_ANALYSIS = "Discrete Data Analysis"; //$NON-NLS-1$

    public static final String SUMMARY_STATISTICS_ANALYSIS = "Summary Statistics Analysis"; //$NON-NLS-1$

    protected String categoryLabel;

    IRepositoryNode[] nodes;

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public IRepositoryNode[] getColumnNodes() {
        return nodes;
    }

    public void setColumnNodes(IRepositoryNode[] nodes) {
        this.nodes = nodes;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public boolean isSemanticDiscoveryAnalysis() {
        return SEMANTIC_DISCOVERY_ANALYSIS.equals(categoryLabel);
    }

    public boolean isBasicColumnAnalysis() {
        return BASIC_COLUMN_ANALYSIS.equals(categoryLabel);
    }

    public boolean isNominalValuesAnalysis() {
        return NOMINAL_VALUES_ANALYSIS.equals(categoryLabel);
    }

    public boolean isPatternFrequencyAnalysis() {
        return PATTERN_FREQUENCY_ANALYSIS.equals(categoryLabel);
    }

    public boolean isDiscreteDataAnalysis() {
        return DISCRETE_DATA_ANALYSIS.equals(categoryLabel);
    }

    public boolean isSummaryStatisticsAnalysis() {
        return SUMMARY_STATISTICS_ANALYSIS.equals(categoryLabel);
    }

    public boolean isNumbericCorrelation() {
        return NUMBERIC_CORRELATION.equals(categoryLabel);
    }

    public boolean isDateCorrelation() {
        return DATE_CORRELATION.equals(categoryLabel);
    }

    public boolean isNominalCorrelation() {
        return NOMINAL_CORRELATION.equals(categoryLabel);
    }

}
