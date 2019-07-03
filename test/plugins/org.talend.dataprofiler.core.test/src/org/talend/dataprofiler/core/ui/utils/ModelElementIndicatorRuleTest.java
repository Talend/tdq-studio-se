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
package org.talend.dataprofiler.core.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.sql.Types;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

import junit.framework.Assert;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConnectionHelper.class, Messages.class, DbmsLanguageFactory.class })
public class ModelElementIndicatorRuleTest {

    private TdSqlDataType tdSqlDataType;

    private TdColumn tdColumn;

    IndicatorEnum[] indicators = new IndicatorEnum[] {
            // Simple Statistics
            IndicatorEnum.RowCountIndicatorEnum, IndicatorEnum.NullCountIndicatorEnum,
            IndicatorEnum.DistinctCountIndicatorEnum, IndicatorEnum.UniqueIndicatorEnum,
            IndicatorEnum.DuplicateCountIndicatorEnum, IndicatorEnum.BlankCountIndicatorEnum,
            IndicatorEnum.DefValueCountIndicatorEnum,
            // Text Statistics
            IndicatorEnum.MinLengthIndicatorEnum, IndicatorEnum.MinLengthWithNullIndicatorEnum,
            IndicatorEnum.MinLengthWithBlankIndicatorEnum, IndicatorEnum.MinLengthWithBlankNullIndicatorEnum,
            IndicatorEnum.MaxLengthIndicatorEnum, IndicatorEnum.MaxLengthWithNullIndicatorEnum,
            IndicatorEnum.MaxLengthWithBlankIndicatorEnum, IndicatorEnum.MaxLengthWithBlankNullIndicatorEnum,
            IndicatorEnum.AverageLengthIndicatorEnum, IndicatorEnum.AverageLengthWithNullIndicatorEnum,
            IndicatorEnum.AverageLengthWithBlankIndicatorEnum, IndicatorEnum.AverageLengthWithNullBlankIndicatorEnum,
            // Summary Statistics
            IndicatorEnum.MeanIndicatorEnum, IndicatorEnum.MedianIndicatorEnum, IndicatorEnum.IQRIndicatorEnum,
            IndicatorEnum.LowerQuartileIndicatorEnum, IndicatorEnum.UpperQuartileIndicatorEnum,
            IndicatorEnum.RangeIndicatorEnum, IndicatorEnum.MinValueIndicatorEnum, IndicatorEnum.MaxValueIndicatorEnum,
            // Advanced Statistics
            IndicatorEnum.ModeIndicatorEnum, IndicatorEnum.FrequencyIndicatorEnum,
            IndicatorEnum.LowFrequencyIndicatorEnum, IndicatorEnum.DateFrequencyIndicatorEnum,
            IndicatorEnum.DateLowFrequencyIndicatorEnum, IndicatorEnum.WeekFrequencyIndicatorEnum,
            IndicatorEnum.WeekLowFrequencyIndicatorEnum, IndicatorEnum.MonthFrequencyIndicatorEnum,
            IndicatorEnum.MonthLowFrequencyIndicatorEnum, IndicatorEnum.QuarterFrequencyIndicatorEnum,
            IndicatorEnum.QuarterLowFrequencyIndicatorEnum, IndicatorEnum.YearFrequencyIndicatorEnum,
            IndicatorEnum.YearLowFrequencyIndicatorEnum, IndicatorEnum.BinFrequencyIndicatorEnum,
            IndicatorEnum.BinLowFrequencyIndicatorEnum,
            // Pattern Frequency Statistics
            IndicatorEnum.PatternFreqIndicatorEnum, IndicatorEnum.PatternLowFreqIndicatorEnum,
            IndicatorEnum.EastAsiaPatternFreqIndicatorEnum, IndicatorEnum.EastAsiaPatternLowFreqIndicatorEnum,
            IndicatorEnum.DatePatternFreqIndicatorEnum, IndicatorEnum.CSWordPatternFreqIndicatorEnum,
            IndicatorEnum.CSWordPatternLowFreqIndicatorEnum, IndicatorEnum.CIWordPatternFreqIndicatorEnum,
            IndicatorEnum.CIWordPatternLowFreqIndicatorEnum,
            // Soundex Frequency Statistics
            IndicatorEnum.SoundexIndicatorEnum, IndicatorEnum.SoundexLowIndicatorEnum,
            // Phone Number Statistics
            IndicatorEnum.ValidPhoneCountIndicatorEnum, IndicatorEnum.ValidRegCodeCountIndicatorEnum,
            IndicatorEnum.InvalidRegCodeCountIndicatorEnum, IndicatorEnum.PossiblePhoneCountIndicatorEnum,
            IndicatorEnum.WellFormIntePhoneCountIndicatorEnum, IndicatorEnum.WellFormNationalPhoneCountIndicatorEnum,
            IndicatorEnum.WellFormE164PhoneCountIndicatorEnum, IndicatorEnum.FormatFreqPieIndictorEnum,
            // Fraud Detection
            IndicatorEnum.BenfordLawFrequencyIndicatorEnum,
            // User Defined Indicators
            IndicatorEnum.UserDefinedIndicatorEnum,
            // Sql patterns
            IndicatorEnum.SqlPatternMatchingIndicatorEnum,
            // Regex patterns
            IndicatorEnum.RegexpMatchingIndicatorEnum };

    boolean[] ExpectResults = {
            // please follow
            // https://talend365.sharepoint.com/:x:/r/sites/DqTeamBeijing/_layouts/15/doc2.aspx?sourcedoc=%7Bb9903b74-31d1-4263-bdb9-6e8870b28bad%7D&action=edit&activeCell=%27MysqlSelectIndicator%27!U8&wdInitialSession=a8cc6194-2b98-47c3-b7c7-aade684e7158&wdRldC=1

            // the order is java engine, sql engine

            // part1-->int/bigint/bit
            // Simple Statistics B5, C5~B6, C6~~~~B11, C11
            true, true, true, true, true, true, true, true, true, true, false, false, false, false,
            // Text Statistics B13, C13~B24, C24
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false,
            // Summary Statistics B26, C26~B33, C33
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            // Advanced Statistics B35, C35~B42, C42, *2 include low
            true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, true, true, true, true,
            // Pattern Frequency Statistics
            true, true, true, true, true, false, true, false, false, false, true, false, true, false, true, false, true,
            false,
            // Soundex Frequency Statistics
            false, false, false, false,
            // Phone Number Statistics
            true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
            // Fraud Detection
            true, true,
            // User Defined Indicators
            false, false, // (this is not follow the excel real result(true,true))
            // Sql patterns
            false, false, // (this is not follow the excel real result(false,true))
            // Regex patterns
            false, false, // (this is not follow the excel real result(true,true))

            // part2-->varchar/char/text
            // Simple Statistics D5, E5~D6, E6~~~~D11, E11
            true, true, true, true, true, true, true, true, true, true, true, true, false, false,
            // Text Statistics D13, E13~D24, E24
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true,
            // Summary Statistics D26, E26~D33, E33
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false,
            // Advanced Statistics D35, E35~D42, E42, *2 include low
            true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            // Pattern Frequency Statistics
            true, true, true, true, true, false, true, false, true, false, true, false, true, false, true, false, true,
            false,
            // Soundex Frequency Statistics
            true, true, true, true,
            // Phone Number Statistics
            true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
            // Fraud Detection
            true, true,
            // User Defined Indicators
            false, false, // (this is not follow the excel real result(true,true))
            // Sql patterns
            false, false, // (this is not follow the excel real result(false,true))
            // Regex patterns
            false, false, // (this is not follow the excel real result(true,true))

            // part3-->date/datetime/time/timestamp
            // Simple Statistics F5, G5~F6, G6~~~~F11, G11
            true, true, true, true, true, true, true, true, true, true, false, false, false, false,
            // Text Statistics F13, G13~F24, G24
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false,
            // Summary Statistics F26, G26~F33, G33
            false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, true,
            // Advanced Statistics F35, G35~F42, G42, *2 include low
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, false, false, false, false,
            // Pattern Frequency Statistics
            true, true, true, true, true, false, true, false, true, false, true, false, true, false, true, false, true,
            false,
            // Soundex Frequency Statistics
            false, false, false, false,
            // Phone Number Statistics
            true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
            // Fraud Detection
            true, true,
            // User Defined Indicators
            false, false, // (this is not follow the excel real result(true,true))
            // Sql patterns
            false, false, // (this is not follow the excel real result(false,true))
            // Regex patterns
            false, false, // (this is not follow the excel real result(true,true))

            // part4-->binary/blob/...
            // Simple Statistics H5, I5~H6, I6~~~~H11, I11
            true, true, true, true, true, true, true, true, true, true, false, false, false, false,
            // Text Statistics H13, I13~H24, I24
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false,
            // Summary Statistics H26, I26~H33, I33
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false,
            // Advanced Statistics H35, I35~H42, I42, *2 include low
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            // Pattern Frequency Statistics
            false, false, false, false, true, false, true, false, false, false, false, false, false, false, false,
            false, false, false,
            // Soundex Frequency Statistics
            false, false, false, false,
            // Phone Number Statistics
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false,
            // Fraud Detection
            true, true,
            // User Defined Indicators
            false, false, // (this is not follow the excel real result(true,true))
            // Sql patterns
            false, false, // (this is not follow the excel real result(false,true))
            // Regex patterns
            false, false, // (this is not follow the excel real result(true,true))
    };

    /**
     * DOC yyin Comment method "setUp".
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tdSqlDataType = mock(TdSqlDataType.class);
        tdColumn = mock(TdColumn.class);
        when(tdColumn.getSqlDataType()).thenReturn(tdSqlDataType);
        when(tdColumn.getContentType()).thenReturn("type"); //$NON-NLS-1$

        ResourceBundle rb = mock(ResourceBundle.class);
        stub(method(ResourceBundle.class, "getBundle", String.class)).toReturn(rb); //$NON-NLS-1$
        PowerMockito.mock(Messages.class);
        stub(method(Messages.class, "getString", String.class)).toReturn("String"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * TDQ-5241 Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.ModelElementIndicatorRule#patternRule(org.talend.dq.nodes.indicator.type.IndicatorEnum, orgomg.cwm.objectmodel.core.ModelElement, org.talend.dataquality.analysis.ExecutionLanguage)}
     * .
     */
    @Test
    public void testPatternRule_1() {
        // modified: when the column is TIME type, any indicator using year(),month(),week() ,quarter(),day() should be
        // disabled
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.TIME);
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.ModeIndicatorEnum, tdColumn, ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.FrequencyIndicatorEnum, tdColumn, ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DateFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.WeekFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.MonthFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.QuarterFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.YearFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DateLowFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.WeekLowFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.MonthLowFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.QuarterLowFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.YearLowFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
    }

    /**
     * test for BenfordLaw indicator
     */
    @Test
    public void testPatternRule_2() {
        // modified: when the column is TIME type, any indicator using year(),month(),week() ,quarter(),day() should be
        // disabled
        when(tdSqlDataType.getJavaDataType())
                .thenReturn(Types.DOUBLE)
                .thenReturn(Types.INTEGER)
                .thenReturn(Types.VARCHAR);
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
    }

    /**
     *
     * test for Hive connection and some indicators should be disabled.
     */
    @Test
    public void testPatternRule_3() {
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.VARCHAR);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(tdColumn)).thenReturn(connection);
        when(ConnectionHelper.isHive(connection)).thenReturn(true);

        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn("MySQL"); //$NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);

        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);

        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.SoundexIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.SoundexLowIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.INTEGER);
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.MedianIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.UpperQuartileIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
    }

    // Test for Teradata's INTERVAL type , sql language
    @Test
    public void testPatternRule_4() {
        when(tdSqlDataType.getName()).thenReturn("INTERVAL YEAR"); //$NON-NLS-1$
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.REAL);

        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.MeanIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.SoundexLowIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.TextIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
    }

    // Test for Teradata's INTERVAL type with java language
    @Test
    public void testPatternRule_5() {
        when(tdSqlDataType.getName()).thenReturn("INTERVAL YEAR"); //$NON-NLS-1$
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.REAL);

        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.MeanIndicatorEnum, tdColumn, ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.SoundexLowIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
    }

    // Test for Teradata's INTERVAL_xx_to_xx type , sql language
    @Test
    public void testPatternRule_6() {
        when(tdSqlDataType.getName()).thenReturn("INTERVAL YEAR TO MONTH"); //$NON-NLS-1$
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.NVARCHAR);

        Assert.assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BlankCountIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.ModeIndicatorEnum, tdColumn, ExecutionLanguage.SQL));

        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.TextIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
    }

    /**
     *
     * test for teradata connection and Pattern Frequency Statistics indicators should be disabled.
     */
    @Test
    public void testPatternRule_7() {
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.VARCHAR);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(tdColumn)).thenReturn(connection);
        when(ConnectionHelper.isTeradata(connection)).thenReturn(true);
        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn("MySQL"); //$NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);

        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);

        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));

        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.DATE);
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertFalse(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, tdColumn,
                ExecutionLanguage.JAVA));
    }

    /**
     * DOC msjian Comment method "testPatternRuleForDatabase".
     * @param dbmsName
     * @param javaDataTypes
     */
    private void testPatternRuleForDatabase(String dbmsName, int[] javaDataTypes) {
        int i = 0;
        for (int javaDataType : javaDataTypes) {
            for (; i < ExpectResults.length;) {
                for (IndicatorEnum indicatorEnum : indicators) {
                    testPatternRule(dbmsName, javaDataType, indicatorEnum, ExpectResults[i], ExpectResults[i + 1]);
                    i += 2;
                }
                // when the indicator for one javaDataType finished, that means we need to skip and go on to another
                // javaDataType
                break;
            }
        }
    }

    /**
     * DOC msjian Comment method "testPatternRuleForAll".
     * 
     * @param dbmsName "Mysql" dbVersion null
     * @param javaDataType Types.VARCHAR
     * @param indicatorEnum IndicatorEnum.BenfordLawFrequencyIndicatorEnum
     * @param javaExpectResult true/false(true means can be selected)
     * @param sqlExpectResult true/false(false means cannot be selected)
     */
    private void testPatternRule(String dbmsName, int javaDataType, IndicatorEnum indicatorEnum,
            boolean javaExpectResult, boolean sqlExpectResult) {
        when(tdSqlDataType.getJavaDataType()).thenReturn(javaDataType);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(tdColumn)).thenReturn(connection);
        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn(dbmsName); // $NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);
        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);
        String message = printDetailsforDebug(dbmsName, javaDataType, indicatorEnum, javaExpectResult, sqlExpectResult);
        assertEquals(message, javaExpectResult,
                ModelElementIndicatorRule.patternRule(indicatorEnum, tdColumn, ExecutionLanguage.JAVA));
        assertEquals(message, sqlExpectResult,
                ModelElementIndicatorRule.patternRule(indicatorEnum, tdColumn, ExecutionLanguage.SQL));
    }

    /**
     * DOC msjian Comment method "printDetailsforDebug".
     * @param dbmsName
     * @param javaDataType
     * @param indicatorEnum
     * @param javaExpectResult
     * @param sqlExpectResult
     */
    private String printDetailsforDebug(String dbmsName, int javaDataType, IndicatorEnum indicatorEnum,
            boolean javaExpectResult, boolean sqlExpectResult) {
        StringBuffer messageBuffer = new StringBuffer();
        messageBuffer.append("-------------------------");
        messageBuffer.append("dbmsName is:" + dbmsName);
        messageBuffer.append(" javaDataType is:");
        switch (javaDataType) {
        case Types.INTEGER:
            messageBuffer.append("Types.INTEGER");
            break;
        case Types.VARCHAR:
            messageBuffer.append("Types.VARCHAR");
            break;
        case Types.FLOAT:
            messageBuffer.append("Types.FLOAT");
            break;
        case Types.DATE:
            messageBuffer.append("Types.DATE");
            break;
        case Types.BINARY:
            messageBuffer.append("Types.BINARY");
            break;
        case Types.CLOB:
            messageBuffer.append("Types.CLOB");
            break;
        case Types.BLOB:
            messageBuffer.append("Types.BLOB");
            break;
        default:
            messageBuffer.append(javaDataType);
        }
        messageBuffer.append(" indicatorEnum is:" + indicatorEnum);
        messageBuffer.append(" javaExpectResult is:" + javaExpectResult);
        messageBuffer.append(" sqlExpectResult is:" + sqlExpectResult);
        return messageBuffer.toString();
    }

    @Test
    public void testPatternRule_mysql() {
        String dbmsName = "Mysql";
        int[] javaDataTypes = {
                // int/bigint/bit
                Types.INTEGER// , Types.BIGINT, Types.BIT
                // float/double/decimal
                // , Types.FLOAT//, Types.DOUBLE, Types.DECIMAL

                // varchar/char/text
                , Types.VARCHAR// , Types.CHAR, Types.LONGNVARCHAR

                // date/datetime // /time/timestamp
                , Types.DATE// , Types.TIMESTAMP// Types.TIME,

                // binary/blob/...
                , Types.BINARY// , Types.BLOB
        };

        testPatternRuleForDatabase(dbmsName, javaDataTypes);
    }

    @Test
    public void testPatternRule_oracle() {
        String dbmsName = "Oracle";
        int[] javaDataTypes = {
                // int/bigint/bit
                Types.INTEGER// , Types.BIGINT, Types.BIT
                // float/double/decimal
                // , Types.FLOAT//, Types.DOUBLE, Types.DECIMAL, Types.CLOB

                // varchar/char/
                , Types.NVARCHAR// , Types.CHAR

                // date/datetime // /time/timestamp
                , Types.DATE// , Types.TIMESTAMP// Types.TIME,

                // binary/blob/...
                , Types.BLOB
        };

        testPatternRuleForDatabase(dbmsName, javaDataTypes);
    }

    @Test
    public void testPatternRule_MSSQL() {
        String dbmsName = "Microsoft SQL Server";
        int[] javaDataTypes = {
                // int/bigint/bit
                Types.INTEGER// , Types.BIGINT, Types.BIT
                // float/double/decimal
                // , Types.FLOAT//, Types.DOUBLE, Types.DECIMAL, Types.CLOB

                // varchar/char/
                , Types.CHAR// , Types.CHAR

                // date/datetime // /time/timestamp
                , Types.DATE// , Types.TIMESTAMP// Types.TIME,

                // binary/blob/...
                , Types.BINARY };

        testPatternRuleForDatabase(dbmsName, javaDataTypes);
    }

    @Test
    public void testPatternRule_PostgreSQL() {
        String dbmsName = "PostgreSQL";
        int[] javaDataTypes = {
                // int/bigint/bit
                Types.INTEGER// , Types.BIGINT, Types.BIT
                // float/double/decimal
                // , Types.FLOAT//, Types.DOUBLE, Types.DECIMAL, Types.CLOB

                // varchar/char/
                , Types.VARCHAR// , Types.CHAR

                // date/datetime // /time/timestamp
                , Types.TIMESTAMP// , Types.TIMESTAMP// Types.TIME,

                // binary/blob/...
                , Types.SQLXML };

        testPatternRuleForDatabase(dbmsName, javaDataTypes);
    }

    @Test
    public void testPatternRule_sybase() {
        String dbmsName = "Sybase (ASE and IQ)";
        int[] javaDataTypes = {
                // int/bigint/bit
                Types.BIGINT// , Types.BIGINT, Types.BIT
                // float/double/decimal
                // , Types.FLOAT//, Types.DOUBLE, Types.DECIMAL, Types.CLOB

                // varchar/char/
                , Types.VARCHAR// , Types.CHAR

                // date/datetime // /time/timestamp
                , Types.DATE// , Types.TIMESTAMP// Types.TIME,

                // binary/blob/...
                , Types.VARBINARY };

        testPatternRuleForDatabase(dbmsName, javaDataTypes);
    }
}
