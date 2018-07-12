// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Display;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.exception.AnalysisExecutionException;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.helpers.RowCountIndicatorsAdapter;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.TextParameters;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.GenericSQLHandler;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.metadata.managment.hive.HiveClassLoaderFactory;
import org.talend.metadata.managment.utils.DatabaseConstant;
import org.talend.utils.collections.MultiMapHelper;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.ResultSetUtils;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.RelationalPackage;

/**
 * @author scorreia
 * 
 * Generates the SQL queries for each indicator and each column to be analyzed. Then executes the queries and stores the
 * results.
 */
public class ColumnAnalysisSqlExecutor extends ColumnAnalysisExecutor {

    protected boolean parallel = true;

    private static final String ALIAS = "(\\w*)\\."; //$NON-NLS-1$

    private static final int TOP_N = org.talend.dataquality.PluginConstant.DEFAULT_TOP_N;

    private static Logger log = Logger.getLogger(ColumnAnalysisSqlExecutor.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    public String createSqlStatement(Analysis analysis) {
        this.cachedAnalysis = analysis;
        AnalysisResult results = analysis.getResults();
        assert results != null;

        try {
            // --- get data filter
            ModelElementAnalysisHandler handler = new ModelElementAnalysisHandler();
            handler.setAnalysis(analysis);
            String stringDataFilter = handler.getStringDataFilter();

            // --- get all the leaf indicators used for the sql computation
            Collection<Indicator> leafIndicators = IndicatorHelper.getIndicatorLeaves(results);
            // --- create one sql statement for each leaf indicator
            for (Indicator indicator : leafIndicators) {
                if (!this.continueRun()) {
                    break;
                }

                if (!createSqlQuery(stringDataFilter, indicator)) {
                    log.error(Messages.getString(
                            "ColumnAnalysisSqlExecutor.CREATEQUERYERROR", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
                    // return null;
                }
            }
        } catch (AnalysisExecutionException e) {
            log.error(e, e);
            return null;
        }

        return PluginConstant.EMPTY_STRING;
    }

    /**
     * Method "createSqlQuery".
     * 
     * @param dataFilterExpression
     * @param analysis
     * @param indicator
     * 
     * @throws ParseException
     * @throws AnalysisExecutionException
     */
    private boolean createSqlQuery(String dataFilterAsString, Indicator indicator) throws AnalysisExecutionException {
        TypedReturnCode<TdColumn> checkResult = getTdColumn(indicator);
        if (!checkResult.isOk()) {
            return false;
        }
        TdColumn tdColumn = checkResult.getObject();
        if (tdColumn.eIsProxy()) {
            tdColumn = (TdColumn) EObjectHelper.resolveObject(tdColumn);
        }

        TypedReturnCode<String> columnName = getColumnName(indicator, tdColumn);
        if (!columnName.isOk()) {
            return false;
        }
        String colName = columnName.getObject();

        TypedReturnCode<IndicatorDefinition> id = getIndicatorDefinition(indicator);
        if (!id.isOk()) {
            return false;
        }
        IndicatorDefinition indicatorDefinition = id.getObject();

        // get correct language for current database
        String language = dbms().getDbmsName();

        // --- create select statement
        // get indicator's sql columnS (generate the real SQL statement from its definition)
        Expression sqlGenericExpression = dbms().getSqlExpression(indicatorDefinition);

        final EClass indicatorEclass = indicator.eClass();
        if (sqlGenericExpression == null || sqlGenericExpression.getBody() == null) {
            // Added TDQ-8468 yyin 20131227 : if the used UDI already has its correct expression instance in the
            // analysis, will not check the sql expression and create again(from the definition).
            if (UDIHelper.isUDI(indicator) && indicator.getInstantiatedExpressions().size() > 0) {
                return Boolean.TRUE;
            }// ~
             // when the indicator is a pattern indicator, a possible cause is that the DB does not support regular
             // expressions.
            if (IndicatorsPackage.eINSTANCE.getRegexpMatchingIndicator().equals(indicatorEclass)) {
                traceError(Messages.getString("ColumnAnalysisSqlExecutor.PLEASEREMOVEALLPATTEN"));//$NON-NLS-1$
                return Boolean.FALSE;
            }

            traceError(Messages
                    .getString(
                            "ColumnAnalysisSqlExecutor.UNSUPPORTEDINDICATOR",//$NON-NLS-1$
                            (indicator.getName() != null ? AnalysisExecutorHelper.getIndicatorName(indicator) : indicatorEclass
                                    .getName())));
            return Boolean.FALSE;
        }

        // --- get indicator parameters and convert them into sql expression
        List<String> whereExpression = new ArrayList<String>();
        if (StringUtils.isNotBlank(dataFilterAsString)) {
            whereExpression.add(dataFilterAsString);
        }
        List<String> rangeStrings = null;
        DateGrain dateAggregationType = null;
        IndicatorParameters parameters = indicator.getParameters();
        if (parameters != null) {
            // handle bins
            Domain bins = parameters.getBins();
            if (bins != null) {
                rangeStrings = getBinsAsGenericString(bins.getRanges(), colName);
            }

            DateParameters dateParameters = parameters.getDateParameters();
            if (dateParameters != null) {
                dateAggregationType = dateParameters.getDateAggregationType();
            }

            TextParameters textParameter = parameters.getTextParameter();
            if (textParameter != null) {
                if (textParameter.isIgnoreCase()) {
                    colName = dbms().toUpperCase(colName);
                }
                if (!textParameter.isUseBlank()
                        && IndicatorsPackage.eINSTANCE.getLengthIndicator().isSuperTypeOf(indicatorEclass)) {

                    String tdColName = getQuotedColumnName(tdColumn);
                    tdColName = dbms().replaceNullsWithString(tdColName, "'NULL TALEND'");//$NON-NLS-1$

                } else if (textParameter.isUseBlank()
                        && IndicatorsPackage.eINSTANCE.getFrequencyIndicator().isSuperTypeOf(indicatorEclass)) {
                    colName = dbms().trim(colName);
                }
            }
        }

        TypedReturnCode<String> completedQuery = getCompletedQuery(indicator, tdColumn, colName, indicatorDefinition, language,
                sqlGenericExpression, indicatorEclass, whereExpression, rangeStrings, dateAggregationType);
        if (!completedQuery.isOk()) {
            return false;
        }
        String finalQuery = completedQuery.getObject();

        if (finalQuery != null) {
            TdExpression instantiateSqlExpression = BooleanExpressionHelper.createTdExpression(language, finalQuery);
            indicator.setInstantiatedExpression(instantiateSqlExpression);
            return true;
        }

        return false;
    }

    /**
     * DOC talend Comment method "getCompletedQuery".
     * 
     * @param indicator
     * @param tdColumn
     * @param colName
     * @param indicatorDefinition
     * @param language
     * @param sqlGenericExpression
     * @param indicatorEclass
     * @param whereExpression
     * @param rangeStrings
     * @param dateAggregationType
     * @param table
     * @return
     * @throws AnalysisExecutionException
     */
    private TypedReturnCode<String> getCompletedQuery(Indicator indicator, TdColumn tdColumn, String colName,
            IndicatorDefinition indicatorDefinition, String language, Expression sqlGenericExpression,
            final EClass indicatorEclass, List<String> whereExpression, List<String> rangeStrings, DateGrain dateAggregationType)
            throws AnalysisExecutionException {
        TypedReturnCode<String> rt = new TypedReturnCode<String>(true);
        String completedSqlString;

        String table = dbms().getQueryColumnSetWithPrefix(tdColumn);

        // ### evaluate SQL Statement depending on indicators ###
        // --- handle case when indicator is a quantile
        if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMedianIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getLowerQuartileIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getUpperQuartileIndicator())) {
            // TODO scorreia test type of column and cast when needed
            completedSqlString = getCompletedStringForQuantiles(indicator, sqlGenericExpression, colName, table, whereExpression);
            if (completedSqlString != null) {
                if (!PluginConstant.EMPTY_STRING.equals(completedSqlString)) {
                    whereExpression = duplicateForCrossJoin(completedSqlString, whereExpression, tdColumn);
                    completedSqlString = addWhereToSqlStringStatement(whereExpression, completedSqlString);
                }
            }
        } else if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getFrequencyIndicator())
                || IndicatorsPackage.eINSTANCE.getFrequencyIndicator().isSuperTypeOf(indicatorEclass)
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getModeIndicator()) || UDIHelper.isFrequency(indicator)) {
            // --- handle case when frequency indicator

            // TODO scorreia test type of column and cast when needed
            // with ranges (frequencies of numerical intervals)

            int topN = indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getModeIndicator()) ? 1 : getTopN(indicator);
            topN =
                    indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getBenfordLawFrequencyIndicator()) ? 10
                            : getTopN(indicator);
            if (topN <= 0) {
                topN = TOP_N;
            }

            if (rangeStrings != null) {
                completedSqlString = getUnionCompletedString(indicator, sqlGenericExpression, colName, table, whereExpression,
                        rangeStrings);
                if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getModeIndicator())) {
                    // get the best row
                    completedSqlString = dbms().getTopNQuery(completedSqlString, topN);
                }
            } else if (dateAggregationType != null && !dateAggregationType.equals(DateGrain.NONE)
            // MOD scorreia 2008-06-23 check column type (robustness against bug 4287)
                    && Java2SqlType.isDateInSQL(tdColumn.getSqlDataType().getJavaDataType())) {
                // frequencies with date aggregation
                completedSqlString = getDateAggregatedCompletedStringWithoutAlia(sqlGenericExpression, colName, table,
                        dateAggregationType);
                completedSqlString = addWhereToSqlStringStatement(whereExpression, completedSqlString);
                completedSqlString = dbms().getTopNQuery(completedSqlString, topN);
            } else { // usual nominal frequencies
                // wrap column name into a function for Pattern Frequency Statistics indicator
                if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getPatternFreqIndicator())
                        || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getPatternLowFreqIndicator())) {
                    // done scorreia: get user defined functions for Pattern Frequency Statistics
                    // MOD xqliu 2009-07-01 bug 7818
                    // MOD zshen for bug 12675 2010-05-12
                    if (Java2SqlType.isNumbericInSQL(tdColumn.getSqlDataType().getJavaDataType())
                            || Java2SqlType.isDateInSQL(tdColumn.getSqlDataType().getJavaDataType())) {
                        colName = addFunctionTypeConvert(colName);
                        // ~12675
                    }

                    final EList<CharactersMapping> charactersMapping = indicatorDefinition.getCharactersMapping();
                    TypedReturnCode<String> columnNameWithFunction = getColumnNameWithFunction(indicator, colName, language,
                            charactersMapping);
                    if (!columnNameWithFunction.isOk()) {
                        rt.setOk(false);
                        return rt;
                    }
                    // MOD for TDQ-8600 If the thread can come here mean that tempColName is not null so give the value
                    // to colName
                    colName = columnNameWithFunction.getObject();
                    // ~
                } else if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getSoundexFreqIndicator())
                        || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getSoundexLowFreqIndicator())) {
                    // change table and soundex(column)
                    table = dbms().getSoundexFunction(table, colName);
                }

                completedSqlString = dbms().fillGenericQueryWithColumnTableAndAlias(sqlGenericExpression.getBody(), colName,
                        table, colName);
                completedSqlString = addWhereToSqlStringStatement(whereExpression, completedSqlString);
                completedSqlString = dbms().getTopNQuery(completedSqlString, topN);
            }
        } else if (IndicatorsPackage.eINSTANCE.getPatternMatchingIndicator().isSuperTypeOf(indicatorEclass)) {
            // --- handle case of matching pattern count
            TypedReturnCode<List<String>> checkPatterns = checkPatterns(indicator, language);
            if (!checkPatterns.isOk()) {
                rt.setOk(false);
                return rt;
            }
            List<String> patterns = checkPatterns.getObject();
            completedSqlString = replaceVariables(sqlGenericExpression.getBody(), colName, table, patterns);
            completedSqlString = addWhereToSqlStringStatement(whereExpression, completedSqlString);
        } else {
            // --- handle case of default value count -> create the where clause
            if (IndicatorsPackage.eINSTANCE.getDefValueCountIndicator().equals(indicatorEclass)) {
                String defValue = ColumnHelper.getDefaultValue(tdColumn);
                if (defValue == null) {
                    traceError(Messages
                            .getString(
                                    "ColumnAnalysisSqlExecutor.NODEFAULTVALUE", colName, AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
                    rt.setOk(false);
                    return rt;
                }
                // need to generate different SQL where clause for each type.
                int javaType = tdColumn.getSqlDataType().getJavaDataType();
                // MOD qiongli 2011-10-31 add single quotation '' for mysql date type.
                if (!Java2SqlType.isNumbericInSQL(javaType)
                        // MOD msjian TDQ-10783: varchar type but with number content, we should add single quotation ''
                        && (!isFunction(defValue, table) || StringUtils.isNumeric(defValue.trim()))
                        || (Java2SqlType.isDateInSQL(javaType) && SupportDBUrlType.MYSQLDEFAULTURL.getLanguage().equals(language))) {
                    defValue = "'" + defValue + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                whereExpression.add(colName + dbms().equal() + defValue);
            }

            // --- default case
            // MOD msjian 2011-6-14 21809 fixed: the sql of user defined indicator contains "<%=__GROUP_BY_ALIAS__%>"
            // should be considered
            String sqlBody = sqlGenericExpression.getBody();
            if (sqlBody.indexOf(GenericSQLHandler.GROUP_BY_ALIAS) != -1) {
                completedSqlString = dbms().fillGenericQueryWithColumnTableAndAlias(sqlBody, colName, table, colName);
            } else {
                completedSqlString = dbms().fillGenericQueryWithColumnsAndTable(sqlBody, colName, table);
            }
            completedSqlString = addWhereToSqlStringStatement(whereExpression, completedSqlString);
        }

        rt.setObject(completedSqlString);
        return rt;
    }

    /**
     * DOC talend Comment method "getColumnNameWithFunction".
     * 
     * @param indicator
     * @param colName
     * @param language
     * @param charactersMapping
     * @return
     */
    private TypedReturnCode<String> getColumnNameWithFunction(Indicator indicator, String colName, String language,
            final EList<CharactersMapping> charactersMapping) {
        TypedReturnCode<String> rt = new TypedReturnCode<String>(true);
        String colNameWithFunction = dbms().getPatternFinderFunction(colName, charactersMapping);
        if (colNameWithFunction == null) {
            colNameWithFunction = dbms().getPatternFinderDefaultFunction(colName);
            if (colNameWithFunction == null) {
                traceError(Messages
                        .getString(
                                "ColumnAnalysisSqlExecutor.NOREPLACEMENTFOUNDFORDBTYPE", language, AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
                rt.setOk(false);
            }
        }
        rt.setObject(colNameWithFunction);
        return rt;
    }

    /**
     * DOC talend Comment method "checkHavePatterns".
     * 
     * @param indicator
     * @param language
     * @return
     */
    private TypedReturnCode<List<String>> checkPatterns(Indicator indicator, String language) {
        TypedReturnCode<List<String>> rt = new TypedReturnCode<List<String>>(true);
        List<String> patterns = getPatterns(indicator);
        if (patterns.isEmpty()) {
            // Added TDQ-8468 yyin 20131227 : if the used SQL pattern already has its correct expression instance in
            // the analysis, will not check the expression and create again(from the definition).
            if (indicator.getInstantiatedExpressions().size() <= 0) {
                traceError(Messages
                        .getString(
                                "ColumnAnalysisSqlExecutor.NOPATTERNFOUNDFORDBTYPE", language, AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
                rt.setOk(false);
            }
        }
        rt.setObject(patterns);
        return rt;
    }

    /**
     * DOC talend Comment method "getColumnName".
     * 
     * @param indicator
     * @param tdColumn
     * @return
     */
    private TypedReturnCode<String> getColumnName(Indicator indicator, TdColumn tdColumn) {
        TypedReturnCode<String> rt = new TypedReturnCode<String>(true);
        String colName = getQuotedColumnName(tdColumn);
        if (!belongToSameSchemata(tdColumn)) {
            StringBuffer buf = new StringBuffer();
            for (orgomg.cwm.objectmodel.core.Package schema : schemata.values()) {
                buf.append(schema.getName() + " "); //$NON-NLS-1$
            }
            traceError(Messages.getString(
                    "ColumnAnalysisSqlExecutor.COLUMNNOTBELONGTOEXISTSCHEMA", colName, buf.toString().trim()));//$NON-NLS-1$
            rt.setOk(false);
            return rt;
        }

        colName = dbms().castColumn4ColumnAnalysisSqlExecutor(indicator, tdColumn, colName);
        rt.setObject(colName);
        return rt;
    }

    /**
     * DOC talend Comment method getTdColumn.
     * 
     * @param indicator
     */
    private TypedReturnCode<TdColumn> getTdColumn(Indicator indicator) {
        TypedReturnCode<TdColumn> rt = new TypedReturnCode<TdColumn>(true);
        ModelElement analyzedElement = indicator.getAnalyzedElement();
        if (analyzedElement == null) {
            traceError(Messages.getString(
                    "ColumnAnalysisSqlExecutor.ANALYSISELEMENTISNULL", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
            rt.setOk(false);
            return rt;
        }

        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analyzedElement);
        rt.setObject(tdColumn);
        if (tdColumn == null) {
            traceError(Messages.getString(
                    "ColumnAnalysisSqlExecutor.ANALYZEDISNOTCOLUMNINDICATOR", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
            rt.setOk(false);
        }
        return rt;
    }

    /**
     * DOC talend Comment method "getIndicatorDefinition".
     * 
     * @param indicator
     * @return
     */
    private TypedReturnCode<IndicatorDefinition> getIndicatorDefinition(Indicator indicator) {
        TypedReturnCode<IndicatorDefinition> rt = new TypedReturnCode<IndicatorDefinition>(true);
        IndicatorDefinition indicatorDefinition;
        String label = indicator.getIndicatorDefinition().getLabel();
        if (label == null || PluginConstant.EMPTY_STRING.equals(label)) {
            indicatorDefinition = indicator.getIndicatorDefinition();
        } else {
            indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(label);
        }

        // TDQ-10559: when the indicator is in reference project, the above can get NOTHING
        if (indicatorDefinition == null) {
            indicatorDefinition = indicator.getIndicatorDefinition();
            if (indicatorDefinition.eIsProxy()) {
                indicatorDefinition = (IndicatorDefinition) EObjectHelper.resolveObject(indicatorDefinition);
            }
        }
        // TDQ-10559~

        if (indicatorDefinition == null) {
            traceError(Messages.getString(
                    "ColumnAnalysisSqlExecutor.INTERNALERROR", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
            rt.setOk(false);
        }
        rt.setObject(indicatorDefinition);
        return rt;
    }

    /**
     * @param defValue
     * @param table
     * @return
     */
    private boolean isFunction(String defValue, String table) {
        boolean ok = false;
        Connection conenction = null;
        Statement stat = null;
        try {
            String queryStmt = "select " + defValue + " from " + table;//$NON-NLS-1$//$NON-NLS-2$
            TypedReturnCode<Connection> conn = getConnection(cachedAnalysis);
            conenction = conn.getObject();

            stat = conenction.createStatement();
            ok = stat.execute(queryStmt);

        } catch (Exception e) {
            ok = false;
        } finally {
            // MOD qiongli 2011-5-20,don't print error in error log view and use finnaly to close Statement.
            try {
                stat.close();
            } catch (SQLException e) {
                log.error(e, e);
            }
            ConnectionUtils.closeConnection(conenction);
        }
        return ok;
    }

    private String addFunctionTypeConvert(String colName) {
        if (colName == null) {
            return colName;
        }
        colName = " CAST(" + colName + " AS CHAR(20)) ";//$NON-NLS-1$//$NON-NLS-2$
        return colName;
    }

    /**
     * Method "duplicateForCrossJoin". For some SQL queries, auto-joins are used in subqueries. This means that the
     * table has two differents aliases and the columns must be prefixed with the alias of the table. Each where clause
     * must be duplicated. For example, the clause "AGE > 10" must be duplicated to give "a.AGE > 10" and "b.AGE" when
     * table aliases are "a" and "b".
     * 
     * @param completedSqlString the SQL query
     * 
     * @param whereExpression some where clauses
     * @param tdColumn the analyzed column
     * @return a list of new where clauses (or the one given as argument)
     */
    private List<String> duplicateForCrossJoin(String completedSqlString, List<String> whereExpression, TdColumn tdColumn) {
        if (whereExpression.isEmpty()) {
            return whereExpression;
        }
        String quotedColName = getQuotedColumnName(tdColumn);
        String[] tableAliases = getTableTableAliasA(completedSqlString, quotedColName);
        if (tableAliases == null) {
            return whereExpression;
        }
        List<String> duplicatedWhereExpressions = new ArrayList<String>();
        // get the table
        ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(tdColumn);
        List<TdColumn> columns = ColumnHelper.getColumns(columnSetOwner.getFeature());
        for (String where : whereExpression) {
            // we expect only 2 table aliases, hence two distinct where clauses.
            String whereA = where;
            String whereB = where;
            for (TdColumn col : columns) {
                String colNameToReplace = where.contains(quotedColName) ? quotedColName : col.getName();
                if (where.contains(colNameToReplace)) {
                    whereA = whereA.replace(colNameToReplace, tableAliases[0] + PluginConstant.DOT_STRING + colNameToReplace);
                    whereB = whereB.replace(colNameToReplace, tableAliases[1] + PluginConstant.DOT_STRING + colNameToReplace);
                }
            }
            duplicatedWhereExpressions.add(whereA);
            duplicatedWhereExpressions.add(whereB);
        }

        return duplicatedWhereExpressions;
    }

    /**
     * DOC scorreia Comment method "getTableTableAliasA".
     * 
     * @param completedSqlString
     * @param quotedColName
     * @return
     */
    private String[] getTableTableAliasA(String completedSqlString, String quotedColName) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ALIAS + quotedColName);
        Matcher matcher = p.matcher(completedSqlString);
        if (!matcher.find()) {
            return null;
        }
        String g1 = matcher.group(1);
        String g2 = null;
        while (matcher.find()) {
            g2 = matcher.group(1);
            if (!g1.equals(g2)) {
                break;
            }
        }
        if (g1 == null || g2 == null) {
            return null;
        }
        return new String[] { g1, g2 };
    }

    /**
     * DOC scorreia Comment method "getTopN".
     * 
     * @param indicator
     * @return
     */
    private int getTopN(Indicator indicator) {
        IndicatorParameters parameters = indicator.getParameters();
        return (parameters == null) ? -1 : parameters.getTopN();
    }

    /**
     * DOC scorreia Comment method "replaceVariables".
     * 
     * @param sqlGenericString
     * @param colName
     * @param table
     * @param patterns
     * @return
     * 
     */
    private String replaceVariables(String sqlGenericString, String colName, String table, List<String> patterns) {
        assert (patterns.size() != 0);
        return dbms().fillGenericQueryWithColumnTablePattern(sqlGenericString, colName, table, patterns.get(0));
    }

    /**
     * DOC scorreia Comment method "getPatterns".
     * 
     * @param indicator
     * @return the patterns or null if none has been found
     */
    protected List<String> getPatterns(Indicator indicator) {
        List<String> patternStrings = new ArrayList<String>();
        Domain dataValidDomain = indicator.getParameters().getDataValidDomain();
        if (dataValidDomain == null) {
            return patternStrings;
        }
        EList<Pattern> patterns = dataValidDomain.getPatterns();
        for (Pattern pattern : patterns) {
            Expression expression = this.dbms().getRegexp(pattern);
            String regexp = expression == null ? null : expression.getBody();
            if (regexp != null) {
                patternStrings.add(regexp);
            }
        }
        return patternStrings;
    }

    private static final String COMMA = " , "; //$NON-NLS-1$

    /**
     * 
     * DOC mzhao bug 12675: Drill down of date type not correct. Here would not use alias.
     * 
     * @param sqlExpression
     * @param colName
     * @param table
     * @param dateAggregationType
     * @return
     */
    private String getDateAggregatedCompletedStringWithoutAlia(Expression sqlExpression, String colName, String table,
            DateGrain dateAggregationType) {
        String result = PluginConstant.EMPTY_STRING;
        //        String aliases = ""; // used in group by clause in MySQL //$NON-NLS-1$
        // String alias;
        switch (dateAggregationType) {
        case DAY:
            // alias = getAlias(colName, DateGrain.DAY);
            result = dbms().extractDay(colName) + comma(result);
        case WEEK:
            // alias = getAlias(colName, DateGrain.WEEK);
            result = dbms().extractWeek(colName) + comma(result);
            // no break
        case MONTH:
            // alias = getAlias(colName, DateGrain.MONTH);
            result = dbms().extractMonth(colName) + comma(result);
            // no break
        case QUARTER:
            // alias = getAlias(colName, DateGrain.QUARTER);
            result = dbms().extractQuarter(colName) + comma(result);
            // no break
        case YEAR:
            // alias = getAlias(colName, DateGrain.YEAR);
            result = dbms().extractYear(colName) + comma(result);
            break;
        case NONE:
            result = colName;
            // aliases = colName; // bug 5336 fixed aliases must not be empty otherwise the group by clause is empty.
            break;
        default:
            break;
        }
        // String groupByAliases = dbms().supportAliasesInGroupBy() ? aliases : result;
        String sql = dbms().fillGenericQueryWithColumnTableAndAlias(sqlExpression.getBody(), result, table, result);
        return sql;
    }

    /**
     * Method "unquote" remove surrounding identifier quotes.
     * 
     * @param colName a name with quotes (or without)
     * @return the name without the quotes.
     */
    private String unquote(String colName) {
        String dbQuoteString = dbms().getDbQuoteString();
        return colName.replace(dbQuoteString, PluginConstant.EMPTY_STRING);
    }

    /**
     * Method "comma" puts a comma before a non empty string.
     * 
     * @param previousContent
     * @return either previousContent or " , " + previousContent
     */
    private String comma(String previousContent) {
        return (previousContent.length() == 0) ? previousContent : COMMA + previousContent;
    }

    /**
     * DOC scorreia Comment method "getFinalSqlStringStatement".
     * 
     * @param dataFilterExpression
     * @param whereExpression
     * @param completedSqlString
     * @return
     * @throws ParseException
     */
    private String addWhereToSqlStringStatement(List<String> whereExpressions, String completedSqlString) {
        return dbms().addWhereToSqlStringStatement(completedSqlString, whereExpressions);
    }

    /**
     * DOC scorreia Comment method "getUnionCompletedString".
     * 
     * @param indicator
     * @param sqlExpression
     * @param colName
     * @param table
     * @param whereExpression
     * @param rangeStrings
     * @return
     * @throws ParseException
     */
    private String getUnionCompletedString(Indicator indicator, Expression sqlExpression, String colName, String table,
            List<String> whereExpression, List<String> rangeStrings) {
        StringBuffer buf = new StringBuffer();
        final int last = rangeStrings.size();

        String sqlGenericExpression = sqlExpression.getBody();

        // MOD scorreia 2008-09-03 Bug #4976 Order by clause cannot be in each single select statement (Oracle
        // constraint, but MySQL allows it)
        // hence we extract the ORDER BY clause and add it at the end.
        int idxOfOrderBY = sqlGenericExpression.indexOf(" ORDER BY"); //$NON-NLS-1$
        // MOD scorreia 2008-12-16 handle teradata case where "order by" clause needs a number to identify the column
        // by removing the unnecessary "order by" clause
        // String orderBy = (idxOfOrderBY != -1) ? sqlGenericExpression.substring(idxOfOrderBY) : "";
        String singleStatement = (idxOfOrderBY != -1) ? sqlGenericExpression.substring(0, idxOfOrderBY) : sqlGenericExpression;

        for (int i = 0; i < last; i++) {
            String singleSelect = getCompletedSingleSelect(indicator, singleStatement, colName, table, whereExpression,
                    rangeStrings.get(i));
            // parenthesis necessary for MySQL
            buf.append('(');
            buf.append(singleSelect);
            // parenthesis necessary for MySQL
            buf.append(')');
            if (i != last - 1) {
                buf.append(dbms().unionAll());
            }
        }
        // MOD scorreia 2008-09-03 Bug #4976 append the order by clause at the end of the whole statement
        // if (idxOfOrderBY != -1) {
        // buf.append(orderBy);
        // }
        return buf.toString();
    }

    /**
     * DOC scorreia Comment method "getCompletedSingleSelect".
     * 
     * @param indicator
     * @param sqlExpression
     * @param colName
     * @param table
     * @param whereExpression
     * @param range
     * @return
     * @throws ParseException
     */
    private String getCompletedSingleSelect(Indicator indicator, String sqlGenericExpression, String colName, String table,
            List<String> whereExpression, String range) {
        String completedRange = this.unquote(range); // replaceVariablesLow(range, this.unquote(colName),
        // this.unquote(table));
        String rangeColumn = "'" + completedRange + "'"; //$NON-NLS-1$ //$NON-NLS-2$

        String singleQuery = removeGroupBy(sqlGenericExpression);
        String completedSqlString = dbms().fillGenericQueryWithColumnsAndTable(singleQuery, rangeColumn, table);

        List<String> allWheresForSingleSelect = new ArrayList<String>(whereExpression);

        // add this range clause to the given where clause (but do not modify the given where clause)
        // allWheresForSingleSelect.add(completedRange);

        completedSqlString = addWhereToSqlStringStatement(allWheresForSingleSelect, completedSqlString);
        // replacement in order to get lines even when no data is available
        // do this replacement after having added the where clause otherwise the parsing with ZQL will fail.
        completedSqlString = replaceCountByZeroCount(completedSqlString, range);
        return completedSqlString;
    }

    /**
     * DOC scorreia Comment method "removeGroupBy".
     * 
     * @param sqlGenericExpression
     * @return
     */
    private String removeGroupBy(String sqlGenericExpression) {
        int idxOfGroupBy = sqlGenericExpression.toUpperCase().indexOf(dbms().groupBy());
        return sqlGenericExpression.substring(0, idxOfGroupBy);
    }

    /**
     * Method "replaceCountByZeroCount" replaces "COUNT(*)" by "CASE WHEN completedRange THEN COUNT(*) ELSE 0 END" in
     * the given SQL statement completedSqlString.
     * 
     * @param completedSqlString
     * @param completedRange
     * @return the new SQL statement
     */
    private String replaceCountByZeroCount(String completedSqlString, String completedRange) {
        return completedSqlString.replace("COUNT(*)", "COUNT( CASE WHEN " + completedRange + " THEN 1 END )"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * DOC scorreia Comment method "getBinsAsString".
     * 
     * @param ranges
     * @return
     */
    private List<String> getBinsAsGenericString(EList<RangeRestriction> ranges, String colName) {
        List<String> bins = new ArrayList<String>();
        for (RangeRestriction rangeRestriction : ranges) {
            String bin = colName + dbms().greaterOrEqual() + DomainHelper.getMinValue(rangeRestriction) + dbms().and() + colName
                    + dbms().less() + DomainHelper.getMaxValue(rangeRestriction);
            // set the name of the RangeRestriction here
            // TODO range name should be set at the construction (in the bins designer wizard)
            rangeRestriction.setName(this.unquote(bin));
            bins.add(bin);
        }
        return bins;
    }

    /**
     * DOC scorreia Comment method "getCompletedString".
     * 
     * @param indicator
     * @param sqlExpression
     * @param colName
     * @param table
     * @param whereExpression
     * @throws AnalysisExecutionException
     */
    private String getCompletedStringForQuantiles(Indicator indicator, Expression sqlExpression, String colName, String table,
            List<String> whereExpression) throws AnalysisExecutionException {
        // first, count nb lines
        String catalogOrSchema = getCatalogOrSchemaName(indicator.getAnalyzedElement());
        long count = getCount(cachedAnalysis, colName, table, catalogOrSchema, whereExpression);
        if (count == -1) {
            setError(Messages.getString("ColumnAnalysisSqlExecutor.GotInvalidResultSet", //$NON-NLS-1$
                    dbms().toQualifiedName(catalogOrSchema, null, colName)));
            return null;
        }

        if (count == 0) {
            // then use 0 to fill the query
            return dbms().fillGenericQueryWithColumnTableLimitOffset(sqlExpression.getBody(), colName, table, "0", "0", "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        Long midleCount = getOffsetInLimit(indicator, count);
        Integer nbRow = getNbReturnedRows(indicator, count);

        long nPlusSkip = midleCount + nbRow; // needed for MSSQL query with TOP clause
        return dbms().fillGenericQueryWithColumnTableLimitOffset(sqlExpression.getBody(), colName, table, String.valueOf(nbRow),
                String.valueOf(midleCount), String.valueOf(nPlusSkip));
    }

    /**
     * DOC scorreia Comment method "getNbReturnedRows".
     * 
     * @param indicator
     * @param count
     * @return
     */
    private Integer getNbReturnedRows(Indicator indicator, long count) {
        if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getMedianIndicator())) {
            boolean isEven = count % 2 == 0;
            return (isEven) ? 2 : 1;
        } else if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getLowerQuartileIndicator())
                || indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getUpperQuartileIndicator())) {
            return (count % 4) == 0 ? 2 : 1;
        }
        return 1;
    }

    /**
     * See http://en.wikipedia.org/wiki/Quartile Method "getLimitFirstArg".
     * 
     * @param indicator
     * @param count
     * @return the number of rows to skip in order to compute the quartiles. In case of an odd number of rows, one row
     * is returned and the returned value is the index of the row before the quartile. In case of an even number of
     * rows, two rows are returned and the quartile is the average of the two values (for the median).
     */
    private Long getOffsetInLimit(Indicator indicator, long count) {
        // get the number of rows to be skipped by the LIMIT n,o in MySQL query.
        // Be careful, -1 is needed to get the searched index -1 because the searched index is 3 when n=4
        boolean isEven = count % 2 == 0;
        if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getMedianIndicator())) {
            return isEven ? count / 2 - 1 : (count - 1) / 2;
        } else if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getLowerQuartileIndicator())) {
            return /* isEven ? */Math.round(0.25 * (count + 1)) - 1 /* : Math.round(0.25 count) */;
        } else if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getUpperQuartileIndicator())) {
            Long res = 0L; // count - (Math.round(0.25 * (count + 1))) - 1; does not work
            if (isEven) {
                if ((count / 2) % 2 == 0) {
                    res = 3 * count / 4 - 1;
                } else {
                    res = 3 * ((count / 2) + 1) / 2 - 2;
                }
            } else { // odd number of rows
                if (((count + 1) / 2) % 2 == 0) {
                    res = 3 * (count + 1) / 4 - 1;
                } else {
                    res = 3 * (((count + 1) / 2) - 1) / 2;
                }
            }
            return res;
        }
        return null;
    }

    /**
     * Method "getCount".
     * 
     * @param analysis
     * @param colName the column name should be surrounded by the SQL quotes
     * @param table the table name should be surrounded by the SQL quotes
     * @param catalog the catalog (or schema) name
     * @param whereExpression
     * @return
     * @throws AnalysisExecutionException
     */
    protected Long getCount(Analysis analysis, String colName, String table, String catalog, List<String> whereExpression)
            throws AnalysisExecutionException {
        try {
            return getCountLow(analysis, colName, table, catalog, whereExpression);
        } catch (SQLException e) {
            throw new AnalysisExecutionException(Messages.getString("ColumnAnalysisSqlExecutor.CannotGetCount",//$NON-NLS-1$
                    analysis.getName(), colName, dbms().toQualifiedName(catalog, null, table)), e);
        }
    }

    /**
     * DOC scorreia Comment method "getCount".
     * 
     * @param cachedAnalysis2
     * @param colName
     * @param quote
     * @param whereExpression
     * @param catalogName
     * @return -1L when sql went ok, but obtained result set is invalid.
     * @throws SQLException
     * @throws AnalysisExecutionException
     */
    private Long getCountLow(Analysis analysis, String colName, String table, String catalogName, List<String> whereExpression)
            throws SQLException, AnalysisExecutionException {
        TypedReturnCode<Connection> trc = this.getConnection(analysis);
        if (!trc.isOk()) {
            throw new AnalysisExecutionException(Messages.getString(
                    "ColumnAnalysisSqlExecutor.CannotExecuteAnalysis", analysis.getName() //$NON-NLS-1$
                    , trc.getMessage()));
        }
        Connection connection = trc.getObject();
        String whereExp = (whereExpression == null || whereExpression.isEmpty()) ? PluginConstant.EMPTY_STRING : " WHERE " //$NON-NLS-1$
                + dbms().buildWhereExpression(whereExpression);
        String queryStmt = "SELECT COUNT(" + colName + ") FROM " + table + whereExp; // + dbms().eos(); //$NON-NLS-1$ //$NON-NLS-2$

        List<Object[]> myResultSet = executeQuery(catalogName, connection, queryStmt);

        org.talend.utils.sql.ConnectionUtils.closeConnection(connection);

        if (myResultSet.isEmpty() || myResultSet.size() > 1) {
            log.error(Messages.getString("ColumnAnalysisSqlExecutor.TOOMANYRESULTOBTAINED") + myResultSet);//$NON-NLS-1$  
            return -1L;
        }
        // MOD zshen exchange OracleODBC will get a double value to int.Else Have a NumberFormatException at here.
        return Long.valueOf(String.valueOf(myResultSet.get(0)[0]).split("\\.")[0]);//$NON-NLS-1$  
        // ~
    }

    /**
     * 
     * 
     * DOC scorreia Comment method "replaceVariablesLow".
     * 
     * @param sqlGenericString
     * @param arguments
     * @return
     */
    protected String replaceVariablesLow(String sqlGenericString, Object... arguments) {
        String toFormat = surroundSingleQuotes(sqlGenericString);
        return MessageFormat.format(toFormat, arguments);
    }

    /**
     * Method "surroundSingleQuotes".
     * 
     * see http://java.sun.com/j2se/1.4.2/docs/api/java/text/MessageFormat.html
     * 
     * @param sqlGenericString
     * @return
     */
    private String surroundSingleQuotes(String sqlGenericString) {
        return sqlGenericString.replaceAll("'", "''"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected ReturnCode evaluate(Analysis analysis, java.sql.Connection connection, String sqlStatement) {
        boolean ok = true;

        // store map of element to each indicator used for computation (leaf indicator)
        Map<ModelElement, List<Indicator>> elementToIndicator = new HashMap<ModelElement, List<Indicator>>();

        // execute the sql statement for each indicator
        List<Indicator> indicators = IndicatorHelper.getIndicatorLeaves(analysis.getResults());

        try {
            if (canParallel(connection)) {
                ok = runAnalysisIndicatorsParallel(analysis, elementToIndicator, indicators, POOLED_CONNECTION);
            } else {
                ok = runAnalysisIndicators(connection, elementToIndicator, indicators);
            }
            // --- finalize indicators by setting the row count and null when they exist.
            setRowCountAndNullCount(elementToIndicator);
        } catch (SQLException e) {
            log.error(e, e);
            setError(e.getMessage());
            ok = false;
        }
        if (StringUtils.isEmpty(getErrorMessage()) && getMonitor() != null && getMonitor().isCanceled()) {
            setError(Messages.getString("ColumnAnalysisSqlExecutor.AnalysisIsCanceled", analysis.getName())); //$NON-NLS-1$
        }

        return new ReturnCode(getErrorMessage(), ok);

    }

    /**
     * DOC xqliu Comment method "runAnalysisIndicators".
     * 
     * @param connection
     * @param elementToIndicator
     * @param indicators
     * @return
     * @throws SQLException
     */
    private boolean runAnalysisIndicators(Connection connection, Map<ModelElement, List<Indicator>> elementToIndicator,
            Collection<Indicator> indicators) throws SQLException {
        boolean runStatus = Boolean.TRUE;
        for (Indicator indicator : indicators) {
            // skip composite indicators that do not require a sql execution
            if (indicator instanceof CompositeIndicator) {
                // options of composite indicators are handled elsewhere
                continue;
            }
            // set the connection's catalog
            if (needChangeCatalog(connection)) {
                String catalogName = getCatalogOrSchemaName(indicator.getAnalyzedElement());
                if (catalogName != null) { // check whether null argument can be given
                    changeCatalog(catalogName, connection);
                }
            }

            Expression query = dbms().getInstantiatedExpression(indicator);
            if (query == null || !executeQuery(indicator, connection, query.getBody())) {
                traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" " //$NON-NLS-1$//$NON-NLS-2$
                        + ((query == null) ? "query is null" : "SQL query: " + query.getBody())); //$NON-NLS-1$//$NON-NLS-2$  
                runStatus = Boolean.FALSE;
            } else {
                // set computation done
                indicator.setComputed(true);
            }

            // add mapping of analyzed elements to their indicators
            addElements2IndicatorsMapping(elementToIndicator, indicator);
        }
        return runStatus;
    }

    /**
     * add mapping of analyzed elements to their indicators.
     * 
     * @param elementToIndicator
     * @param indicator
     */
    protected void addElements2IndicatorsMapping(Map<ModelElement, List<Indicator>> elementToIndicator, Indicator indicator) {
        // TDQ-12743 msjian: for each element, we need to add a rowCountIndicator mapping, because later we will use the mapping
        // to set rowcount for each element
        if (indicator instanceof RowCountIndicatorsAdapter) {
            RowCountIndicatorsAdapter indicator1 = (RowCountIndicatorsAdapter) indicator;
            Set<RowCountIndicator> rowCountIndiSet = indicator1.getRowCountIndiSet();
            for (RowCountIndicator rowCountInd : rowCountIndiSet) {
                MultiMapHelper.addUniqueObjectToListMap(rowCountInd.getAnalyzedElement(), indicator, elementToIndicator);
            }
        } else {
            MultiMapHelper.addUniqueObjectToListMap(indicator.getAnalyzedElement(), indicator, elementToIndicator);
        }
    }

    /**
     * 
     * @param parent
     * @param connection
     * @param elementToIndicator
     * @param indicator
     * @return IStatus
     * @throws
     */
    // TDQ Guodong bu 2011-2-25, feature 19107
    static class ExecutiveAnalysisJob extends Job {

        ColumnAnalysisSqlExecutor parent;

        Connection connection;

        Map<ModelElement, List<Indicator>> elementToIndicator;

        Indicator indicator;

        String errorMessage;

        public ExecutiveAnalysisJob(ColumnAnalysisSqlExecutor parent, Connection connection,
                Map<ModelElement, List<Indicator>> elementToIndicator, Indicator indicator) {
            super(PluginConstant.EMPTY_STRING);
            this.parent = parent;
            this.connection = connection;
            this.elementToIndicator = elementToIndicator;
            this.indicator = indicator;
        }

        /*
         * (non-Jsdoc)
         * 
         * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            if (parent.getMonitor() != null && parent.getMonitor().isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            ColumnAnalysisSqlParallelExecutor columnSqlParallel = ColumnAnalysisSqlParallelExecutor.createInstance(parent,
                    connection, elementToIndicator, indicator);
            Boolean isSuccess = columnSqlParallel.run();
            // System.out.println("i:" + i + ":::::" + indicator.getAnalyzedElement().getName() + "--" + indicator.getName());
            if (isSuccess) {
                return Status.OK_STATUS;
            } else {
                this.errorMessage = Messages.getString("ColumnAnalysisSqlExecutor.AnalysisExecutionFailed"); //$NON-NLS-1$
                return Status.CANCEL_STATUS;
            }
        }

        @Override
        public boolean shouldRun() {
            if (!parent.continueRun()) {
                return false;
            }
            return true;
        }

    }

    /**
     * DOC xqliu Comment method "runAnalysisIndicatorsParallel".
     * 
     * @param analysis
     * @param elementToIndicator
     * @param indicators
     * @param pooledConnection
     * @return
     * @throws SQLException
     */
    private boolean runAnalysisIndicatorsParallel(Analysis analysis, Map<ModelElement, List<Indicator>> elementToIndicator,
            List<Indicator> indicators, boolean pooledConnection) throws SQLException {
        // reset the connection pool before run this analysis
        resetConnectionPool(analysis);

        // MOD gdbu 2011-6-10 bug : 21273
        try {
            final int totleWork = compIndicatorsWorked;
            List<ExecutiveAnalysisJob> jobs = new ArrayList<ExecutiveAnalysisJob>();

            final IProgressMonitor monitor = this.getMonitor();
            if (monitor != null) {
                monitor.subTask("Run Indicators Parallel"); //$NON-NLS-1$
            }
            int temp = 0;
            for (int i = 0; i < indicators.size(); i++) {
                final Indicator indicator = indicators.get(i);
                if (!this.continueRun()) {
                    return false;
                }
                // TDQ-11851,in order to syn UI and backend-threads, add this Display.
                if (monitor != null) {
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            monitor.subTask(Messages.getString(
                                    "ColumnAnalysisSqlExecutor.AnalyzedElement", indicator.getAnalyzedElement() //$NON-NLS-1$
                                            .getName()));
                        }
                    });

                    int current = (i + 1) * totleWork / indicators.size();
                    if (current > temp) {
                        monitor.worked(current - temp);
                        temp = current;
                    }
                }
                Connection conn = null;
                if (pooledConnection) {
                    conn = getPooledConnection(analysis).getObject();
                } else {
                    conn = getConnection(analysis).getObject();
                }

                if (conn != null) {
                    ExecutiveAnalysisJob eaj = new ExecutiveAnalysisJob(ColumnAnalysisSqlExecutor.this, conn, elementToIndicator,
                            indicator);
                    eaj.setName(AnalysisExecutorHelper.getIndicatorName(indicator));
                    eaj.schedule();
                    jobs.add(eaj);
                }

            }

            boolean hasErrorMessage = false;
            // should call join() after schedule all the jobs
            for (int i = 0; i < jobs.size(); i++) {
                ExecutiveAnalysisJob eaj = jobs.get(i);
                if (!this.continueRun()) {
                    break;
                }
                eaj.join();
                if (eaj.errorMessage != null) {
                    hasErrorMessage = true;
                    ColumnAnalysisSqlExecutor.this.parallelExeStatus = false;
                }

            }
            // Added TDQ-8388 20140530 yyin: only show one message to let the user check detail in error log.
            if (hasErrorMessage) {
                setError(Messages.getString("ColumnAnalysisSqlExecutor.ERRORREFERTOLOG"));//$NON-NLS-1$
            }

        } catch (Throwable thr) {
            log.error(thr);
        } finally {
            resetConnectionPool(analysis);
        }
        // ~21273
        return parallelExeStatus;
        // ~
    }

    /**
     * DOC scorreia Comment method "setRowCountAndNullCount".
     * 
     * @param elementToIndicator
     */
    protected void setRowCountAndNullCount(Map<ModelElement, List<Indicator>> elementToIndicator) {
        Set<ModelElement> analyzedElements = elementToIndicator.keySet();
        for (ModelElement modelElement : analyzedElements) {
            // get row count indicator
            RowCountIndicator rowCount = IndicatorHelper.getRowCountIndicator(modelElement, elementToIndicator);
            // get null count indicator
            NullCountIndicator nullCount = IndicatorHelper.getNullCountIndicator(modelElement, elementToIndicator);

            List<Indicator> list = elementToIndicator.get(modelElement);
            for (Indicator ind : list) {
                // set row count value to each indicator
                if (rowCount != null && needPercentage(ind)) {
                    ind.setCount(rowCount.getCount());
                }
                // set null count value to each indicator
                if (nullCount != null) {
                    ind.setNullCount(nullCount.getNullCount());
                }
            }
        }
    }

    /**
     * DOC bZhou Comment method "needPercentage".
     * 
     * @param ind
     * @return
     */
    private boolean needPercentage(Indicator ind) {
        IndicatorEnum indType = IndicatorEnum.findIndicatorEnum(ind.eClass());

        return indType != IndicatorEnum.ModeIndicatorEnum && !indType.isAChildOf(IndicatorEnum.TextIndicatorEnum)
                && !indType.isAChildOf(IndicatorEnum.BoxIIndicatorEnum);
    }

    /**
     * Method "getCatalogName".
     * 
     * @param analyzedElement
     * @return the catalog or schema quoted name
     */
    protected String getCatalogOrSchemaName(ModelElement analyzedElement) {
        Package schema = super.schemata.get(analyzedElement);
        if (schema == null) {
            if (!isSchemataProxy()) {
                log.error(Messages.getString("ColumnAnalysisSqlExecutor.NOSCHEMAFOUNDFORCOLUMN") + " " + ((analyzedElement != null) ? analyzedElement.getName() : "Unknow column"));//$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$  
            }
            return null;
        }
        // else
        if (RelationalPackage.eINSTANCE.getSchema().equals(schema.eClass())) {
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(schema);
            if (parentCatalog != null) {
                return parentCatalog.getName();
            }
        }
        return schema.getName();
    }

    /**
     * DOC scorreia Comment method "executeQuery".
     * 
     * @param indicator
     * @param connection
     * @param queryStmt
     * @return
     * @throws SQLException
     */
    protected boolean executeQuery(final Indicator indicator, Connection connection, String queryStmt) throws SQLException {
        String cat = getCatalogOrSchemaName(indicator.getAnalyzedElement());
        if (log.isInfoEnabled()) {
            log.info(Messages.getString(
                    "ColumnAnalysisSqlExecutor.COMPUTINGINDICATOR", AnalysisExecutorHelper.getIndicatorName(indicator)) //$NON-NLS-1$ 
                    + "\t" + Messages.getString("ColumnAnalysisSqlExecutor.EXECUTINGQUERY", queryStmt));//$NON-NLS-1$ //$NON-NLS-2$ 
        }
        // give result to indicator so that it handles the results
        boolean ret = false;
        // MOD qiongli 2012-3-7 TDQ-4632 delete some redundancy code for DistinctIndicator.modify directly the sql
        // expression in definition file.
        List<Object[]> myResultSet = executeQuery(cat, connection, queryStmt);
        if (!continueRun()) {
            return false;
        }
        ret = indicator.storeSqlResults(myResultSet);

        // Added TDQ-8787 publish the related event when one indicator is finished: to refresh the chart with new result
        // of the current indicator
        final ITDQRepositoryService tdqRepositoryService = AnalysisExecutorHelper.getTDQService();
        if (tdqRepositoryService != null) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    tdqRepositoryService.publishDynamicEvent(indicator, IndicatorCommonUtil.getIndicatorValue(indicator));
                }
            });
        }// ~

        // MOD delete the try/catch TDQ-8388
        return ret;
    }

    /**
     * 
     * @param catalogName (can be null)
     * @param connection
     * @param queryStmt
     * @return
     * @throws SQLException
     */
    protected List<Object[]> executeQuery(String catalogName, Connection connection, String queryStmt) throws SQLException {
        // set current thread classLoader if it is hive connection
        ClassLoader currClassLoader = Thread.currentThread().getContextClassLoader();
        org.talend.core.model.metadata.builder.connection.Connection dbConn = this.getAnalysisDataProvider(cachedAnalysis);
        IMetadataConnection metadataBean = ConvertionHelper.convert(dbConn);
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataBean.getDbType())) {
            ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataBean);
            Thread.currentThread().setContextClassLoader(hiveClassLoader);
        }
        List<Object[]> myResultSet = new ArrayList<Object[]>();
        Statement statement = null;
        try {
            if (catalogName != null && needChangeCatalog(connection)) { // check whether null argument can be given
                changeCatalog(catalogName, connection);
            }
            // MOD xqliu 2009-02-09 bug 6237
            if (continueRun()) {
                // create query statement
                statement = connection.createStatement();
                // statement.setFetchSize(fetchSize);
                statement.execute(queryStmt);
                // get the results
                ResultSet resultSet = statement.getResultSet();
                if (resultSet == null) {
                    String mess = Messages.getString("ColumnAnalysisSqlExecutor.NORESULTSETFORTHISSTATEMENT") + queryStmt;//$NON-NLS-1$  
                    log.warn(mess);
                    return null;
                }
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Object[] result = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        result[i] = ResultSetUtils.getBigObject(resultSet, i + 1);
                    }
                    myResultSet.add(result);
                }
                resultSet.close();
            }
            // -- release resources

        } catch (NullPointerException nullExc) {
            // TDQ-11851 when click 'cancel' on wizard,the connection should be closed, so that some object may be Null.Catch the
            // Exception and logging here.
            if (getMonitor() != null && getMonitor().isCanceled()) {
                log.error(nullExc);
            } else {
                throw nullExc;
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
            Thread.currentThread().setContextClassLoader(currClassLoader);
        }
        return myResultSet;
    }

    /**
     * DOC xqliu Comment method "canParallel".
     * 
     * @return
     */
    private boolean canParallel(Connection connection) {
        try {
            @SuppressWarnings("deprecation")
            DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
            if (connectionMetadata.getDriverName() != null
                    && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)) {
                return false;
            }

            // ADD msjian 2011-6-24 22483 fixed: fixed "java.sql.SQLException: database is locked"
            // because the sqlite db is not supported the parallel very well
            if ("SQLite".equals(connection.getMetaData().getDatabaseProductName())) { //$NON-NLS-1$ 
                return false;
            }
            if (ExtractMetaDataUtils.getInstance().isHiveConnection(connection)) {
                // TDQ-12020 only Hive2 supports connection concurrency.
                org.talend.core.model.metadata.builder.connection.Connection analysisDataProvider = getAnalysisDataProvider(cachedAnalysis);
                DatabaseConnection dbConn = ((DatabaseConnection) analysisDataProvider);
                String hiveVersion = dbConn.getParameters().get(ConnParameterKeys.HIVE_SERVER_VERSION);
                if (HiveServerVersionInfo.HIVE_SERVER_1.getKey().equals(hiveVersion)) {
                    return false;
                }

                // TDQ-13909: when run Hive on Spark not support Parallel
                // TODO: if we have a new hive on spark envirement and test profiling run well, we can remove this part
                String jdbcProperties = dbConn.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
                List<Map<String, Object>> hiveJDBCPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(jdbcProperties);
                if (!hiveJDBCPropertiesList.isEmpty()) {
                    List keyList = new ArrayList();
                    List valueList = new ArrayList();
                    for (Map<String, Object> propertyMap : hiveJDBCPropertiesList) {
                        keyList.add(propertyMap.get("PROPERTY")); //$NON-NLS-1$
                        valueList.add(propertyMap.get("VALUE")); //$NON-NLS-1$
                    }
                    if (keyList.contains("hive.execution.engine") && valueList.contains("spark")) { //$NON-NLS-1$ //$NON-NLS-2$
                        return false;
                    }
                }
                // TDQ-13909~
                return true;
            }
        } catch (SQLException e) {
            log.warn(e, e);
        }
        return this.parallel;
    }
}
