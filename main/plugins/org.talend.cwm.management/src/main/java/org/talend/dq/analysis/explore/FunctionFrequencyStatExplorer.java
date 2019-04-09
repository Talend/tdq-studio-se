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
package org.talend.dq.analysis.explore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.BigQueryDbmsLanguage;
import org.talend.dq.dbms.DB2DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.dbms.InfomixDbmsLanguage;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.utils.sql.Java2SqlType;

/**
 * @author scorreia
 * 
 * This class generate select statement for any function found in an instantiated query of the type:
 * "SELECT function(col), COUNT(*) FROM ..."
 */
public class FunctionFrequencyStatExplorer extends FrequencyStatisticsExplorer {

    // MOD mzhao 2010-04-12 bug 11554
    private static final String REGEX = ".*\\s*SELECT.*\\s* (YEAR.*|REPLACE.*|TRANSLATE.*|TO_NUMBER.*|DATEPART.*)\\s*, COUNT\\(\\*\\)\\s*(AS|as)?\\s*\\w*\\s* FROM"; //$NON-NLS-1$

    private static final String REGEX_INFORMIX = ".*\\s*SELECT (REPLACE.*)\\s*(AS|as)+\\s*\\w+\\s* FROM"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.FrequencyStatisticsExplorer#getFreqRowsStatement()
     */
    @Override
    protected String getFreqRowsStatement() {
        // generate SELECT * FROM TABLE WHERE function(columnName) = labelToFind
        TdColumn column = (TdColumn) indicator.getAnalyzedElement();

        String clause = getInstantiatedClause();
        return "SELECT * FROM " + getFullyQualifiedTableName(column) + dbmsLanguage.where() + inBrackets(clause) //$NON-NLS-1$
                + andDataFilterClause();
    }

    @Override
    protected String getInstantiatedClause() {
        // get function which convert data into a pattern
        String colName = columnName;
        String function = null;
        TdColumn column = (TdColumn) indicator.getAnalyzedElement();
        int javaType = column.getSqlDataType().getJavaDataType();
        if (!Java2SqlType.isNumbericInSQL(javaType)) {
            function = getFunction();
        } else {
            colName = columnNameConvert();

        }
        // MOD mzhao bug 9681 2009-11-09

        Object value = null;
        if (Java2SqlType.isNumbericInSQL(javaType) && dbmsLanguage instanceof DB2DbmsLanguage) {
            value = entity.getKey();
        } else {
            value = "'" + entity.getKey() + "'";//$NON-NLS-1$//$NON-NLS-2$
        }

        String clause = entity.isLabelNull() ? columnName + dbmsLanguage.isNull() : ((function == null ? colName : function)
                + dbmsLanguage.equal() + value);
        // ADD xqliu 2011-06-03 bug 20600's note 86482
        if (dbmsLanguage instanceof InfomixDbmsLanguage) {
            clause = clause.replaceAll(InfomixDbmsLanguage.AS_REPLACE_COLUMN, PluginConstant.EMPTY_STRING);
        }
        // ~ 20600
        return clause;
    }

    private String getFunction() {
        String instantiatedSQL = getIndicatorExpressionSQL();
        if (instantiatedSQL == null) {
            return instantiatedSQL;
        }

        Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

        // MOD mzhao 2010-04-12 bug 11554
        String dbmsName = this.dbmsLanguage.getDbmsName();
        if (DbmsLanguageFactory.isInfomix(dbmsName)) {
            p = Pattern.compile(REGEX_INFORMIX, Pattern.CASE_INSENSITIVE);
        }
        // ~
        Matcher matcher = p.matcher(instantiatedSQL);
        matcher.find();
        // MOD mzhao 2009-11-09 bug 9681: Catch the possibility that the sql body contains "TOP" keywords.
        // MOD MOD mzhao 2010-04-12 bug 11554
        String group = matcher.group(1);
        return group;
    }

    private String addFunctionTypeConvert(String colName) {
        if (colName == null) {
            return colName;
        }
        if (dbmsLanguage instanceof BigQueryDbmsLanguage) {
            colName = dbmsLanguage.castColumnNameToChar(colName);
        } else {
            colName = " CAST(" + colName + " AS CHAR(20)) ";//$NON-NLS-1$//$NON-NLS-2$
        }
        return colName;
    }

    @SuppressWarnings("deprecation")
    private String columnNameConvert() {
        String colName = columnName;
        TdColumn column = (TdColumn) indicator.getAnalyzedElement();
        IndicatorDefinition indicatorDefinition;
        String label = indicator.getIndicatorDefinition().getLabel();
        if (label == null || PluginConstant.EMPTY_STRING.equals(label)) {
            indicatorDefinition = indicator.getIndicatorDefinition();
        } else {
            indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(label);
        }

        if (indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getPatternFreqIndicator())
                || indicator.eClass().equals(IndicatorsPackage.eINSTANCE.getPatternLowFreqIndicator())) {
            // MOD zshen for bug 12675 2010-05-12
            if (Java2SqlType.isNumbericInSQL(column.getSqlDataType().getJavaDataType())) {
                colName = addFunctionTypeConvert(colName);
            }
            // ~12675
            final EList<CharactersMapping> charactersMapping = indicatorDefinition.getCharactersMapping();
            String colNameWithFunction = dbmsLanguage.getPatternFinderFunction(colName, charactersMapping);
            if (colNameWithFunction == null) { // no replacement found, try the default one
                colNameWithFunction = dbmsLanguage.getPatternFinderDefaultFunction(colName);
            }
            colName = colNameWithFunction;
        }
        return colName;
    }
}
