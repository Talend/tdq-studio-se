// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.expressions.BooleanExpressionNode;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;

import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * @author scorreia
 *
 * Helper class.
 */
public final class AnalysisHelper {

    public static final int DATA_FILTER_A = 0;

    public static final int DATA_FILTER_B = 1;

    private static final String DEFAULT = "Default"; //$NON-NLS-1$

    private AnalysisHelper() {
    }

    /**
     * Method "createAnalysis".
     *
     * @param name the name of the analysis
     * @return the new analysis with the given name
     */
    public static Analysis createAnalysis(String name) {
        Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
        analysis.setName(name);
        return analysis;
    }

    /**
     *
     * Get Analysis by indicator
     *
     * @param indicator
     * @return
     */
    public static Analysis getAnalysis(Indicator indicator) {
        if (indicator == null) {
            return null;
        }
        EObject eContainer = indicator.eContainer();
        while (eContainer != null && !Analysis.class.isInstance(eContainer)) {
            eContainer = eContainer.eContainer();
        }
        if (eContainer == null) {
            return null;
        }
        return (Analysis) eContainer;
    }

    /**
     *
     * Get Analysis uuid by indicator
     *
     * @param indicator
     * @return
     */
    public static String getAnalysisUUID(Indicator indicator) {
        Analysis analysis = getAnalysis(indicator);
        String analysisUUID = null;
        if (analysis != null) {
            analysisUUID = ResourceHelper.getUUID(analysis);
        }
        return analysisUUID;
    }

    /**
     * Method "getAnalysisType".
     *
     * @param analysis
     * @return the analysis type or null if not set
     */
    public static AnalysisType getAnalysisType(Analysis analysis) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return null;
        }
        // else
        return parameters.getAnalysisType();
    }

    /**
     * Method "getExecutionEngine".
     *
     * @param analysis the analysis
     * @return the execution language (SQL by default)
     */
    public static ExecutionLanguage getExecutionEngine(Analysis analysis) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return ExecutionLanguage.SQL;
        }
        // else
        return parameters.getExecutionLanguage();
    }

    /**
     * Current analysis whether is java engine
     *
     * @param analysis the analysis
     * @return true if is java engin else false
     */
    public static boolean isJavaExecutionEngine(Analysis analysis) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return false;
        }
        return ExecutionLanguage.JAVA == parameters.getExecutionLanguage();
    }

    /**
     * Current analysis whether is store data
     *
     * @param analysis
     * @return
     */
    public static boolean isStoreData(Analysis analysis) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return false;
        }
        return parameters.isStoreData();
    }

    /**
     * Method "setAnalysisType".
     *
     * @param analysis an analysis
     * @param analysisType the type to set to the analysis
     */
    public static void setAnalysisType(Analysis analysis, AnalysisType analysisType) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            parameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
            analysis.setParameters(parameters);
        }
        parameters.setAnalysisType(analysisType);
    }

    /**
     * Method "getDataFilter".
     *
     * @param analysis
     * @return the list of domains or null
     */
    public static EList<Domain> getDataFilter(Analysis analysis) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return null;
        }
        return parameters.getDataFilter();
    }

    /**
     * Method "getStringDataFilter".
     *
     * @param analysis
     * @return the data filter as a string or null if none.
     */
    public static String getStringDataFilter(Analysis analysis) {
        return getStringDataFilter(analysis, 0);
    }

    /**
     * DOC xqliu Comment method "getStringDataFilter".
     *
     * @param analysis
     * @param index 0 for DataFilterA, 1 for DataFilterB
     * @return
     */
    public static String getStringDataFilter(Analysis analysis, int index) {
        AnalysisParameters parameters = analysis.getParameters();
        if (parameters == null) {
            return null;
        }
        EList<Domain> dataFilters = parameters.getDataFilter();
        // remove existing filters
        if (dataFilters == null || dataFilters.isEmpty()) {
            return null;
        }

        int i = 0;
        for (Domain domain : dataFilters) {
            if (domain == null) {
                continue;
            }
            EList<RangeRestriction> ranges = domain.getRanges();
            for (RangeRestriction rangeRestriction : ranges) {
                BooleanExpressionNode expressions = rangeRestriction.getExpressions();
                if (expressions == null) {
                    continue;
                }
                Expression expression = expressions.getExpression();
                if (expression == null) {
                    continue;
                }
                if (i == index) {
                    // MOD klliu 2011-07-14 bug 23150 solve an NPE
                    String body = expression.getBody();
                    if (body == null) {
                        continue;
                    }
                    // ~
                    return body.trim();
                } else if (i > index) {
                    return null;
                }
            }
            i++;
        }
        return null;
    }

    public static boolean setStringDataFilter(Analysis analysis, String datafilterString) {
        EList<Domain> dataFilters = analysis.getParameters().getDataFilter();
        // update existing filters
        if (!dataFilters.isEmpty()) {
            Domain domain = dataFilters.get(0);
            EList<RangeRestriction> ranges = domain.getRanges();
            RangeRestriction rangeRestriction = (ranges.isEmpty()) ? DomainHelper.addRangeRestriction(domain) : ranges.get(0);
            BooleanExpressionNode expressions = rangeRestriction.getExpressions();
            if (expressions == null) {
                expressions = BooleanExpressionHelper.createBooleanExpressionNode(datafilterString);
                rangeRestriction.setExpressions(expressions);
            } else {
                Expression expression = expressions.getExpression();
                if (expression == null) {
                    expression = BooleanExpressionHelper.createTdExpression(BooleanExpressionHelper.DEFAULT_LANGUAGE,
                            datafilterString);
                    expressions.setExpression(expression);
                } else {
                    expression.setBody(datafilterString);
                }
            }
            return false;
        }
        // else
        return dataFilters.add(createDomain(analysis, datafilterString));
    }

    /**
     * DOC xqliu Comment method "setStringDataFilter".
     *
     * @param analysis
     * @param datafilterString
     * @param index 0 for DataFilterA, 1 for DataFilterB
     * @return
     */
    public static boolean setStringDataFilter(Analysis analysis, String datafilterString, int index) {
        if (index == 1) {
            EList<Domain> dataFilters = analysis.getParameters().getDataFilter();
            int size = dataFilters.size();
            if (size == 0) {
                dataFilters.add(createDomain(analysis, "")); //$NON-NLS-1$
                return dataFilters.add(createDomain(analysis, datafilterString, "1")); //$NON-NLS-1$
            } else if (size == 1) {
                return dataFilters.add(createDomain(analysis, datafilterString, "1")); //$NON-NLS-1$
            } else if (size == 2) {
                Domain domain = dataFilters.get(1);
                EList<RangeRestriction> ranges = domain.getRanges();
                RangeRestriction rangeRestriction = (ranges.isEmpty()) ? DomainHelper.addRangeRestriction(domain) : ranges.get(0);
                BooleanExpressionNode expressions = rangeRestriction.getExpressions();
                if (expressions == null) {
                    expressions = BooleanExpressionHelper.createBooleanExpressionNode(datafilterString);
                    rangeRestriction.setExpressions(expressions);
                } else {
                    Expression expression = expressions.getExpression();
                    if (expression == null) {
                        expression = BooleanExpressionHelper.createTdExpression(BooleanExpressionHelper.DEFAULT_LANGUAGE,
                                datafilterString);
                        expressions.setExpression(expression);
                    } else {
                        expression.setBody(datafilterString);
                    }
                }
                return false;
            } else {
                return false;
            }
        } else {
            return setStringDataFilter(analysis, datafilterString);
        }

    }

    private static Domain createDomain(Analysis analysis, String datafilterString) {
        // by default use same name as the analysis. This is ok as long as there is only one datafilter.
        Domain domain = DomainHelper.createDomain(analysis.getName());
        RangeRestriction rangeRestriction = DomainHelper.addRangeRestriction(domain);
        BooleanExpressionNode expressionNode = BooleanExpressionHelper.createBooleanExpressionNode(datafilterString);
        rangeRestriction.setExpressions(expressionNode);
        return domain;
    }

    private static Domain createDomain(Analysis analysis, String datafilterString, String alias) {
        // by default use same name as the analysis. This is ok as long as there is only one datafilter.
        Domain domain = DomainHelper.createDomain(analysis.getName() + alias);
        RangeRestriction rangeRestriction = DomainHelper.addRangeRestriction(domain);
        BooleanExpressionNode expressionNode = BooleanExpressionHelper.createBooleanExpressionNode(datafilterString);
        rangeRestriction.setExpressions(expressionNode);
        return domain;
    }

    public static List<IndicatorDefinition> getUserDefinedIndicators(Analysis analysis) {
        List<IndicatorDefinition> rets = new ArrayList<IndicatorDefinition>();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            if (indicator instanceof UserDefIndicator) {
                rets.add(indicator.getIndicatorDefinition());
            }
        }
        return rets;
    }

    public static List<Pattern> getPatterns(Analysis analysis) {
        List<Pattern> rets = new ArrayList<Pattern>();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            if (indicator instanceof PatternMatchingIndicator) {
                IndicatorParameters parameters = ((PatternMatchingIndicator) indicator).getParameters();
                if (null != parameters && null != parameters.getDataValidDomain()) {
                    rets.addAll(parameters.getDataValidDomain().getPatterns());
                }
            }
            // MOD scorreia 2009-10-07 User defined matching indicator is NOT a pattern matching indicator.
            // else if (indicator instanceof UserDefIndicator) {
            // if (IndicatorCategoryHelper.isUserDefMatching(getIndicatorCategory(indicator.getIndicatorDefinition())))
            // {
            // rets.addAll(((UserDefIndicator) indicator).getParameters().getDataValidDomain().getPatterns());
            // }
            // }

            // MOD yyi 2010-08-03 14292 Add dependency for children indicator in Column Set analysis
            if (indicator instanceof AllMatchIndicator) {
                EList<RegexpMatchingIndicator> list = ((AllMatchIndicator) indicator).getCompositeRegexMatchingIndicators();
                for (RegexpMatchingIndicator pattern : list) {
                    IndicatorParameters parameters = pattern.getParameters();
                    if (null != parameters && null != parameters.getDataValidDomain()) {
                        rets.addAll(parameters.getDataValidDomain().getPatterns());
                    }
                }
            }
            // ~
        }
        return rets;
    }

    /**
     * DOC xqliu Comment method "getReloadDatabases".
     *
     * @param analysis
     * @return
     */
    public static boolean getReloadDatabases(Analysis analysis) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.RELOAD_DATABASES, analysis.getTaggedValue());
        if (taggedValue != null) {
            try {
                return Boolean.valueOf(taggedValue.getValue());
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "setReloadDatabases".
     *
     * @param analysis
     * @param reloadDatabases
     * @return
     */
    public static boolean setReloadDatabases(Analysis analysis, boolean reloadDatabases) {
        return TaggedValueHelper.setTaggedValue(analysis, TaggedValueHelper.RELOAD_DATABASES, String.valueOf(reloadDatabases));
    }

    /**
     * DOC xqliu Comment method "getPurpose".
     *
     * @param analysis
     * @return
     */
    public static String getPurpose(Analysis analysis) {
        String purpose = ""; //$NON-NLS-1$
        if (analysis != null) {
            TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.PURPOSE, analysis.getTaggedValue());
            if (taggedValue != null && taggedValue.getValue() != null) {
                purpose = taggedValue.getValue();
            }
        }
        return purpose;
    }

    /**
     * DOC xqliu Comment method "getDescription".
     *
     * @param analysis
     * @return
     */
    public static String getDescription(Analysis analysis) {
        String description = ""; //$NON-NLS-1$
        if (analysis != null) {
            TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DESCRIPTION, analysis.getTaggedValue());
            if (taggedValue != null && taggedValue.getValue() != null) {
                description = taggedValue.getValue();
            }
        }
        return description;
    }

    /**
     *
     * judge the type of analysis whether is Match Rule Analysis
     *
     * @param analysis
     * @return
     */
    public static boolean isMatchRuleAnalysis(Analysis analysis) {
        return AnalysisType.MATCH_ANALYSIS == analysis.getParameters().getAnalysisType();
    }

    public static boolean isUseSampleData(Analysis analysis) {
        return TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_USE_SAMPLE_DATA, analysis);
    }

    /**
     *
     * judge the type of analysis whether is COLUMN CORRELATION Analysis
     *
     * @param analysis
     * @return
     */
    public static boolean isColumnCorrelationAnalysis(Analysis analysis) {
        return AnalysisType.COLUMN_CORRELATION == analysis.getParameters().getAnalysisType();
    }

    /**
     * Get recording matching indicator from analysis
     *
     * @param analysis
     * @return
     */
    public static RecordMatchingIndicator getRecordMatchIndicatorFromAna(Analysis analysis) {
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator ind : indicators) {
            if (ind instanceof RecordMatchingIndicator) {
                return (RecordMatchingIndicator) ind;
            }
        }
        return null;
    }

    /**
     * get Last Run Context.
     *
     * @param analysis
     * @return
     */
    public static String getLastRunContext(Analysis analysis) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.ANA_LAST_RUN_CONTEXT,
                analysis.getTaggedValue());
        if (taggedValue == null) {
            return PluginConstant.EMPTY_STRING;
        }
        return taggedValue.getValue();
    }

    /**
     * get the analysis last run context group name, if it is empty, return the default context group name.
     *
     * @param analysis
     * @return
     */
    public static String getContextGroupName(Analysis analysis) {
        String contextGroupNameInUse = getLastRunContext(analysis);
        if (StringUtils.isEmpty(contextGroupNameInUse)) {
            contextGroupNameInUse = analysis.getDefaultContext();
        }
        if (StringUtils.isEmpty(contextGroupNameInUse)) {
            contextGroupNameInUse = DEFAULT;
        }
        return contextGroupNameInUse;
    }

    /**
     * DOC msjian Comment method "setLastRunContext".
     *
     * @param lastRunContext
     * @param analysis
     * @return
     */
    public static boolean setLastRunContext(String lastRunContext, Analysis analysis) {
        return TaggedValueHelper.setTaggedValue(analysis, TaggedValueHelper.ANA_LAST_RUN_CONTEXT, lastRunContext);
    }
}
