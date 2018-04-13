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
package org.talend.dataprofiler.core.ui.utils;

import java.sql.Types;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.nodes.indicator.IIndicatorNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class ModelElementIndicatorRule {

    private ModelElementIndicatorRule() {
    }

    public static boolean match(IIndicatorNode node, ModelElementIndicator meIndicator, ExecutionLanguage language) {
        IndicatorEnum indicatorType = node.getIndicatorEnum();
        if (indicatorType == null) {

            for (IIndicatorNode one : node.getChildren()) {
                if (match(one, meIndicator, language)) {
                    return true;
                }
            }

            return false;
        }
        IRepositoryNode rd = meIndicator.getModelElementRepositoryNode();

        // return patternRule(indicatorType, ((MetadataColumnRepositoryObject) rd.getObject()).getTdColumn(), language);
        return patternRule(indicatorType, RepositoryNodeHelper.getSubModelElement(rd), language, node);
    }

    public static boolean patternRule(IndicatorEnum indicatorType, ModelElement me, ExecutionLanguage language) {
        return patternRule(indicatorType, me, language, null);
    }

    public static boolean patternRule(IndicatorEnum indicatorType, ModelElement me, ExecutionLanguage language,
            IIndicatorNode node) {
        int javaType = 0;
        boolean isDeliFileColumn = !(me instanceof TdColumn) && me instanceof MetadataColumn;
        int isTeradataInterval = -1;
        if (me instanceof TdColumn) {
            javaType = ((TdColumn) me).getSqlDataType().getJavaDataType();
            // Added yyin 20121211 TDQ-6099:
            isTeradataInterval = Java2SqlType.isTeradataIntervalType(((TdColumn) me).getSqlDataType().getName());
            // ~
        } else if (isDeliFileColumn) {
            javaType = TalendTypeConvert.convertToJDBCType(((MetadataColumn) me).getTalendType());
        }
        DataminingType dataminingType = MetadataHelper.getDataminingType(me);
        if (dataminingType == null || isDeliFileColumn) {
            dataminingType = MetadataHelper.getDefaultDataminingType(javaType);
        }
        // MOD qiongli 2012-4-25 TDQ-2699
        Connection connection = null;
        if (me instanceof TdColumn) {
            connection = ConnectionHelper.getTdDataProvider((TdColumn) me);
        } else if (me instanceof MetadataColumn) {
            connection = ConnectionHelper.getTdDataProvider((MetadataColumn) me);
        }

        Indicator indicator = null;
        if (node != null) {
            indicator = node.getIndicatorInstance();
        }
        boolean isSQLEngine = ExecutionLanguage.SQL.equals(language);
        boolean isJavaEngine = ExecutionLanguage.JAVA.equals(language);
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(connection, language);
        if (javaType == Types.LONGVARCHAR && isSQLEngine) {
            if (connection != null && ConnectionHelper.isDb2(connection)) {
                return enableLongVarchar(indicatorType, dataminingType, me);
            }
        }
        // MOD qiongli 2012-8-10 TDQ-5907 need to disabled indicators for hive with sql engine.
        boolean isHiveSQL = connection == null ? false : ConnectionHelper.isHive(connection) && isSQLEngine;

        // MOD msjian 2013-5-15 TDQ-7275 need to disabled indicators for teradata with sql engine.
        boolean isTeradataSQL = connection == null ? false : ConnectionHelper.isTeradata(connection) && isSQLEngine;

        // MOD msjian 2016-8-25 TDQ-12349 need to disabled indicators for ingres with sql engine.
        boolean isIngres = connection == null ? false : ConnectionHelper.isIngress(connection) && isSQLEngine;

        // MOD msjian 2016-8-25 TDQ-12464 need to disabled indicators for ingres with sql engine.
        boolean isSybase = connection == null ? false : ConnectionHelper.isSybase(connection) && isSQLEngine;

        // MOD qiongli 2013-8-27 TDQ-2104 disabled soundex indicators for hive with sql engine.
        boolean isVerticaSQL = connection == null ? false : ConnectionHelper.isVertica(connection) && isSQLEngine;

        switch (indicatorType) {
        case CountsIndicatorEnum:
        case RowCountIndicatorEnum:
        case NullCountIndicatorEnum:
        case DistinctCountIndicatorEnum:
        case UniqueIndicatorEnum:
        case DuplicateCountIndicatorEnum:

            // MOD scorreia 2008-06-04 enable distinct count, unique count and duplicate count for all types
            // if (dataminingType == DataminingType.NOMINAL) {
            return true;
        case DefValueCountIndicatorEnum:
            Expression initialValue = null;
            if (me instanceof TdColumn) {
                initialValue = ((TdColumn) me).getInitialValue();
            }
            if (initialValue != null && initialValue.getBody() != null) {
                // MOD scorreia 2009-04-21 bug 6979
                // non nullable numeric column give a non null default value as ''
                return initialValue.getBody().length() != 0 || Java2SqlType.isTextInSQL(javaType);
            }

            break;
        case BlankCountIndicatorEnum:
            // MOD klliu 2011-07-19 bug 22980 from repository as same as indicator dialog
            // MOD xwang 2011-07-29 bug TDQ-1731 disable blank count checkable for other data type but Text
            if (!Java2SqlType.isTextInSQL(javaType)) {
                return false;
            } else if (isTeradataInterval == Java2SqlType.TERADATA_INTERVAL_TO && isSQLEngine) {
                // Added yyin 20121212 TDQ-6099: disable for Teradata's interval_xx_to_xx type.
                return false;
            } else {
                return true;
            }

        case TextIndicatorEnum:
        case MinLengthIndicatorEnum:
        case MinLengthWithNullIndicatorEnum:
        case MinLengthWithBlankIndicatorEnum:
        case MinLengthWithBlankNullIndicatorEnum:
        case MaxLengthIndicatorEnum:
        case MaxLengthWithNullIndicatorEnum:
        case MaxLengthWithBlankIndicatorEnum:
        case MaxLengthWithBlankNullIndicatorEnum:
        case AverageLengthIndicatorEnum:
        case AverageLengthWithNullIndicatorEnum:
        case AverageLengthWithBlankIndicatorEnum:
        case AverageLengthWithNullBlankIndicatorEnum:
            if (Java2SqlType.isTextInSQL(javaType)) {
                // Added yyin 20121212 TDQ-6099: disable for Teradata's interval_xx_to_xx type.
                if (isTeradataInterval > 0) {
                    return false;
                }
                if (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.UNSTRUCTURED_TEXT) {
                    return true;
                }
            }
            break;
        case EastAsiaPatternFreqIndicatorEnum:
        case EastAsiaPatternLowFreqIndicatorEnum:
            if (isSQLEngine && isEmpryExpression(indicator, dbmsLanguage)) {
                return false;
            } else if (isJavaEngine) {
                return true;
            }
        case BenfordLawFrequencyIndicatorEnum:
            // Added yyin 20121211 TDQ-6099: disable these three for INTERVAL type of Teradata
            // disable the benford for interval type: both sql and java
            if (isTeradataInterval > 0) {
                return false;
            }
        case PatternFreqIndicatorEnum:
        case PatternLowFreqIndicatorEnum:
            if (isTeradataSQL || isIngres || isSybase) {
                return false;
            }
        case ModeIndicatorEnum:
            // Added yyin 20121212 TDQ-6099: disable for Teradata's interval_xx_to_xx type.
            if (isTeradataInterval == Java2SqlType.TERADATA_INTERVAL_TO && isSQLEngine) {
                return false;
            }
        case FrequencyIndicatorEnum:
        case LowFrequencyIndicatorEnum:
            if (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL) {
                return true;
            }
            break;
        // MOD zshen 2010-01-27 Date Pattern frequency indicator
        case DatePatternFreqIndicatorEnum:
            if (isJavaEngine && (Java2SqlType.isDateInSQL(javaType) || Java2SqlType.isTextInSQL(javaType))) {
                return true;
            }
            break;
        // MOD mzhao 2009-03-05 Soundex frequency indicator
        case SoundexIndicatorEnum:
        case SoundexLowIndicatorEnum:
            if (!Java2SqlType.isDateInSQL(javaType) && !Java2SqlType.isNumbericInSQL(javaType)
                    && (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL)) {
                if (isHiveSQL || isVerticaSQL) {
                    return false;
                }
                // Added yyin 20121212 TDQ-6099: disable for Teradata's interval_xx_to_xx type.
                if (isTeradataInterval > 0 && isSQLEngine) {
                    return false;
                }
                return true;
            }
            break;
        case MeanIndicatorEnum:
        case MedianIndicatorEnum:
        case IQRIndicatorEnum:
        case LowerQuartileIndicatorEnum:
        case UpperQuartileIndicatorEnum:

            // MOD scorreia 2008-09-19 do not allow box plot on date fields because it is not correctly handled in the
            // graphics and database yet.
            if (Java2SqlType.isNumbericInSQL(javaType) /* || Java2SqlType.isDateInSQL(javaType) */) {
                if (dataminingType == DataminingType.INTERVAL) {
                    if (isHiveSQL && !(indicatorType == IndicatorEnum.MeanIndicatorEnum)) {
                        return false;
                    }
                    return true;
                }
            }
            break;
        case BoxIIndicatorEnum:
        case RangeIndicatorEnum:
        case MinValueIndicatorEnum:
        case MaxValueIndicatorEnum:
            // MOD scorreia 2008-09-25 do not allow min and max on date fields because it is not correctly handled in
            // the graphics and database yet.
            if (Java2SqlType.isNumbericInSQL(javaType) || Java2SqlType.isDateInSQL(javaType)) {
                if (dataminingType == DataminingType.INTERVAL) {
                    return true;
                }
            }
            break;
        case DateFrequencyIndicatorEnum:
        case WeekFrequencyIndicatorEnum:
        case MonthFrequencyIndicatorEnum:
        case QuarterFrequencyIndicatorEnum:
        case YearFrequencyIndicatorEnum:
        case DateLowFrequencyIndicatorEnum:
        case WeekLowFrequencyIndicatorEnum:
        case MonthLowFrequencyIndicatorEnum:
        case QuarterLowFrequencyIndicatorEnum:
        case YearLowFrequencyIndicatorEnum:

            // ADD yyi 2010-07-23 13676
            // Mod yyin 20120511 TDQ-5241
            if (Java2SqlType.isDateInSQL(javaType) && !Java2SqlType.isTimeSQL(javaType)
                    && (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL)) {
                return true;
            }
            break;
        case BinFrequencyIndicatorEnum:
        case BinLowFrequencyIndicatorEnum:
            if (Java2SqlType.isNumbericInSQL(javaType)
                    && (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL)) {
                return true;
            }
            break;
        case ValidPhoneCountIndicatorEnum:
        case PossiblePhoneCountIndicatorEnum:
        case ValidRegCodeCountIndicatorEnum:
        case InvalidRegCodeCountIndicatorEnum:
        case WellFormE164PhoneCountIndicatorEnum:
        case WellFormIntePhoneCountIndicatorEnum:
        case WellFormNationalPhoneCountIndicatorEnum:
        case PhoneNumbStatisticsIndicatorEnum:
        case FormatFreqPieIndictorEnum:
        case CSWordPatternFreqIndicatorEnum:
        case CSWordPatternLowFreqIndicatorEnum:
        case CIWordPatternFreqIndicatorEnum:
        case CIWordPatternLowFreqIndicatorEnum:
            if (isJavaEngine && (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL)) {
                return true;
            }
            break;

        case SqlPatternMatchingIndicatorEnum:
            if (node == null) {
                return false;
            }
            if (!isSQLEngine) {
                return false;
            }
            Pattern pattern = IndicatorHelper.getPattern(indicator);
            Expression returnExpression = dbmsLanguage.getRegexp(pattern);
            if (returnExpression != null) {
                return true;
            }
            break;
        case RegexpMatchingIndicatorEnum:
            if (node == null) {
                return false;
            }
            pattern = IndicatorHelper.getPattern(indicator);
            returnExpression = dbmsLanguage.getRegexp(pattern);
            if (returnExpression != null) {
                return true;
            }
            break;
        case UserDefinedIndicatorEnum:
            // judge language
            if (node == null) {
                return false;
            }
            Indicator judi = null;
            try {
                judi = UDIHelper.adaptToJavaUDI(indicator);
            } catch (Throwable e) {
                return false;
            }
            if (judi != null) {
                indicator = judi;
            }

            returnExpression = dbmsLanguage.getExpression(indicator);
            if (isJavaEngine && judi == null || isSQLEngine && returnExpression == null) {
                return false;
            }
            return true;
        default:
            return false;
        }

        return false;
    }

    /**
     * DOC talend Comment method "isEmpryExpression".
     * 
     * @param indicator
     * @return
     */
    private static boolean isEmpryExpression(Indicator indicator, DbmsLanguage dbmsLanguage) {
        Expression sqlExpression = dbmsLanguage.getSqlExpression(indicator.getIndicatorDefinition());
        CharactersMapping characterMappingExpression = dbmsLanguage.getChartacterMappingExpression(indicator
                .getIndicatorDefinition());
        return sqlExpression == null || characterMappingExpression == null;
    }

    /**
     * 
     * just several indicator support longvarchar.because longvarchar dosn't support some sql query,.eg.,distinct,group
     * by,function...
     * 
     * @param indicatorType
     * @param dataminingType
     * @return
     */
    private static boolean enableLongVarchar(IndicatorEnum indicatorType, DataminingType dataminingType, ModelElement mod) {
        switch (indicatorType) {
        case CountsIndicatorEnum:
        case RowCountIndicatorEnum:
            return true;
        case TextIndicatorEnum:
        case MinLengthWithBlankIndicatorEnum:
        case MinLengthWithNullIndicatorEnum:
        case MinLengthWithBlankNullIndicatorEnum:
        case MaxLengthWithBlankIndicatorEnum:
        case MaxLengthWithBlankNullIndicatorEnum:
        case MaxLengthWithNullIndicatorEnum:
        case AverageLengthIndicatorEnum:
        case AverageLengthWithBlankIndicatorEnum:
        case AverageLengthWithNullBlankIndicatorEnum:
        case AverageLengthWithNullIndicatorEnum:
        case MinLengthIndicatorEnum:
        case MaxLengthIndicatorEnum:
            if (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.UNSTRUCTURED_TEXT) {
                return true;
            }
            break;
        case PatternFreqIndicatorEnum:
        case PatternLowFreqIndicatorEnum:
            if (dataminingType == DataminingType.NOMINAL || dataminingType == DataminingType.INTERVAL) {
                return true;
            }
            break;
        default:
            return false;
        }
        return false;
    }
}
