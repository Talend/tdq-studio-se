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
package org.talend.dataprofiler.core.ui.wizard.analysis;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.wizard.Wizard;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.pattern.CreatePatternWizard;
import org.talend.dataprofiler.core.sql.CreateSqlFileWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.catalog.CatalogAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.BasicColumnWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnSetWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.DiscreteDataWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.MatchWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.NominalCorrelationWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.NominalValuesWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.NumericalCorrelationWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.PatternFrequencyWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.SummaryStatisticsWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.TimeCorrelationWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.ConnectionAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.schema.SchemaAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.table.FunctionDependencyWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.table.TableAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.dqrules.NewDQRulesWizard;
import org.talend.dataprofiler.core.ui.wizard.indicator.NewUDIndicatorWizard;
import org.talend.dataprofiler.core.ui.wizard.matchrule.NewMatchRuleWizard;
import org.talend.dataprofiler.core.ui.wizard.parserrule.NewParserRulesWizard;
import org.talend.dataprofiler.service.ISemanticStudioService;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.analysis.parameters.DQMatchRuleParameter;
import org.talend.dq.analysis.parameters.DQParserRulesParameter;
import org.talend.dq.analysis.parameters.DQRulesParameter;
import org.talend.dq.analysis.parameters.FuncationDependencyParameter;
import org.talend.dq.analysis.parameters.NamedColumnSetAnalysisParameter;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.analysis.parameters.PatternParameter;
import org.talend.dq.analysis.parameters.SqlFileParameter;
import org.talend.dq.analysis.parameters.UDIndicatorParameter;
import org.talend.repository.model.RepositoryNode;

/**
 * @author zqin
 */
public final class WizardFactory {

    private WizardFactory() {
    }

    public static Wizard createAnalysisWizard(AnalysisType type, AnalysisParameter parameter) {
        assert type != null;

        switch (type) {
        case COLUMN_CORRELATION:
            if (parameter == null) {
                parameter = new AnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            if (((AnalysisLabelParameter) parameter).isNumbericCorrelation()) {
                return new NumericalCorrelationWizard(parameter);
            }

            if (((AnalysisLabelParameter) parameter).isDateCorrelation()) {
                return new TimeCorrelationWizard(parameter);
            }

            if (((AnalysisLabelParameter) parameter).isNominalCorrelation()) {
                return new NominalCorrelationWizard(parameter);
            }
        case MULTIPLE_COLUMN:
            if (parameter == null) {
                parameter = new AnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());

            if (parameter instanceof AnalysisLabelParameter) {
                if (((AnalysisLabelParameter) parameter).isSemanticDiscoveryAnalysis()) {
                    ISemanticStudioService service = CorePlugin.getDefault().getSemanticStudioService();
                    if (service != null) {
                        return service.getSemanticDiscoveryWizard(null, parameter);
                    }
                }

                if (((AnalysisLabelParameter) parameter).isBasicColumnAnalysis()) {
                    return new BasicColumnWizard(parameter);
                }

                if (((AnalysisLabelParameter) parameter).isNominalValuesAnalysis()) {
                    return new NominalValuesWizard(parameter);
                }

                if (((AnalysisLabelParameter) parameter).isPatternFrequencyAnalysis()) {
                    return new PatternFrequencyWizard(parameter);
                }

                if (((AnalysisLabelParameter) parameter).isDiscreteDataAnalysis()) {
                    return new DiscreteDataWizard(parameter);
                }

                if (((AnalysisLabelParameter) parameter).isSummaryStatisticsAnalysis()) {
                    return new SummaryStatisticsWizard(parameter);
                }
            }

            return new ColumnWizard(parameter);
        case COLUMNS_COMPARISON:
            if (parameter == null) {
                parameter = new AnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new ColumnWizard(parameter);
        case CONNECTION:
            if (parameter == null) {
                parameter = new AnalysisFilterParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new ConnectionAnalysisWizard((AnalysisFilterParameter) parameter);

            // MOD mzhao 2009-02-03 CATALOG and SCHEMA should use
            // PackagesAnalyisParameter.
        case CATALOG:
            if (parameter == null) {
                parameter = new PackagesAnalyisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new CatalogAnalysisWizard((PackagesAnalyisParameter) parameter);

        case SCHEMA:
            if (parameter == null) {
                parameter = new PackagesAnalyisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new SchemaAnalysisWizard((PackagesAnalyisParameter) parameter);
        case TABLE:
            if (parameter == null) {
                parameter = new NamedColumnSetAnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new TableAnalysisWizard((NamedColumnSetAnalysisParameter) parameter);
        case TABLE_FUNCTIONAL_DEPENDENCY:
            if (parameter == null) {
                parameter = new FuncationDependencyParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new FunctionDependencyWizard(parameter);
        case COLUMN_SET:
            if (parameter == null) {
                parameter = new AnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new ColumnSetWizard(parameter);
        case MATCH_ANALYSIS:
            if (parameter == null) {
                parameter = new AnalysisParameter();
            }
            parameter.setAnalysisTypeName(type.getLiteral());
            return new MatchWizard(parameter);
        default:
            return null;
        }
    }

    public static Wizard createAnalysisWizard(AnalysisType type) {
        return createAnalysisWizard(type, null);
    }

    public static CreateNewAnalysisWizard createNewAnalysisWizard() {
        return new CreateNewAnalysisWizard();
    }

    /**
     * DOC klliu Comment method "createNewAnalysisWizard".
     *
     * @param node
     * @return
     */
    public static CreateNewAnalysisWizard createNewAnalysisWizard(RepositoryNode node) {
        return new CreateNewAnalysisWizard(node);
    }

    /**
     *
     * DOC xqliu Comment method "createNewDQRuleWizard".
     *
     * @param parameter
     * @return
     */
    public static NewDQRulesWizard createNewDQRuleWizard(DQRulesParameter parameter) {
        return new NewDQRulesWizard(parameter);
    }

    public static NewParserRulesWizard createNewParserRuleWizard(DQParserRulesParameter parameter) {
        return new NewParserRulesWizard(parameter);
    }

    public static NewMatchRuleWizard createNewMatchRuleWizard(DQMatchRuleParameter parameter) {
        return new NewMatchRuleWizard(parameter);
    }

    public static NewUDIndicatorWizard createNewUDIWizard(UDIndicatorParameter parameter) {
        return new NewUDIndicatorWizard(parameter);
    }

    public static Wizard createSqlFileWizard(IFolder folder) {
        SqlFileParameter parameter = new SqlFileParameter();
        FolderProvider folderProvider = parameter.getFolderProvider();
        if (folderProvider == null) {
            folderProvider = new FolderProvider();
        }

        folderProvider.setFolderResource(folder);
        parameter.setFolderProvider(folderProvider);
        return new CreateSqlFileWizard(parameter);
    }

    public static Wizard createPatternWizard(ExpressionType type) {
        return createPatternWizard(type, null);
    }

    public static Wizard createPatternWizard(ExpressionType type, PatternParameter parameter) {
        if (parameter == null) {
            parameter = new PatternParameter();
        }
        return new CreatePatternWizard(parameter, type);
    }

    public static Wizard createPatternWizard(ExpressionType type, PatternParameter parameter, String expression, String language) {
        if (parameter == null) {
            parameter = new PatternParameter();
        }
        return new CreatePatternWizard(parameter, type, expression, language);
    }
}
