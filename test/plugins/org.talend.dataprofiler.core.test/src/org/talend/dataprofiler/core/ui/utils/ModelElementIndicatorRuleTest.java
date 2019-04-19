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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.sql.Types;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC yyin class global comment. Detailled comment
 */
@PrepareForTest({ ConnectionHelper.class, Messages.class, DbmsLanguageFactory.class })
public class ModelElementIndicatorRuleTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private TdSqlDataType tdsql;

    private TdColumn me;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tdsql = mock(TdSqlDataType.class);
        me = mock(TdColumn.class);
        when(me.getSqlDataType()).thenReturn(tdsql);
        when(me.getContentType()).thenReturn("type"); //$NON-NLS-1$

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
        when(tdsql.getJavaDataType()).thenReturn(Types.TIME);
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.ModeIndicatorEnum, me, ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.FrequencyIndicatorEnum, me, ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.DateFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.WeekFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.MonthFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.QuarterFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.YearFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.DateLowFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.WeekLowFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.MonthLowFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.QuarterLowFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.YearLowFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
    }

    /**
     * test for BenfordLaw indicator
     */
    @Test
    public void testPatternRule_2() {
        // modified: when the column is TIME type, any indicator using year(),month(),week() ,quarter(),day() should be
        // disabled
        when(tdsql.getJavaDataType()).thenReturn(Types.DOUBLE).thenReturn(Types.INTEGER).thenReturn(Types.VARCHAR);
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.SQL));
    }

    /**
     * 
     * test for Hive connection and some indicators should be disabled.
     */
    @Test
    public void testPatternRule_3() {
        when(tdsql.getJavaDataType()).thenReturn(Types.VARCHAR);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(me)).thenReturn(connection);
        when(ConnectionHelper.isHive(connection)).thenReturn(true);

        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn("MySQL"); //$NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);

        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);

        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.SoundexIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule
                .patternRule(IndicatorEnum.SoundexLowIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule
                .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        when(tdsql.getJavaDataType()).thenReturn(Types.INTEGER);
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.MedianIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.UpperQuartileIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, me,
                ExecutionLanguage.SQL));
    }

    // Test for Teradata's INTERVAL type , sql language
    @Test
    public void testPatternRule_4() {
        when(tdsql.getName()).thenReturn("INTERVAL YEAR"); //$NON-NLS-1$
        when(tdsql.getJavaDataType()).thenReturn(Types.REAL);

        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.MeanIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule
                .patternRule(IndicatorEnum.SoundexLowIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule
                .patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.TextIndicatorEnum, me, ExecutionLanguage.SQL));
    }

    // Test for Teradata's INTERVAL type with java language
    @Test
    public void testPatternRule_5() {
        when(tdsql.getName()).thenReturn("INTERVAL YEAR"); //$NON-NLS-1$
        when(tdsql.getJavaDataType()).thenReturn(Types.REAL);

        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.MeanIndicatorEnum, me, ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.SoundexLowIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, me,
                ExecutionLanguage.JAVA));
    }

    // Test for Teradata's INTERVAL_xx_to_xx type , sql language
    @Test
    public void testPatternRule_6() {
        when(tdsql.getName()).thenReturn("INTERVAL YEAR TO MONTH"); //$NON-NLS-1$
        when(tdsql.getJavaDataType()).thenReturn(Types.NVARCHAR);

        Assert.assertFalse(ModelElementIndicatorRule
                .patternRule(IndicatorEnum.BlankCountIndicatorEnum, me, ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.ModeIndicatorEnum, me, ExecutionLanguage.SQL));

        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.LowerQuartileIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.TextIndicatorEnum, me, ExecutionLanguage.SQL));
    }

    /**
     * 
     * test for teradata connection and Pattern Frequency Statistics indicators should be disabled.
     */
    @Test
    public void testPatternRule_7() {
        when(tdsql.getJavaDataType()).thenReturn(Types.VARCHAR);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(me)).thenReturn(connection);
        when(ConnectionHelper.isTeradata(connection)).thenReturn(true);
        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn("MySQL"); //$NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);

        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);

        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));

        when(tdsql.getJavaDataType()).thenReturn(Types.DATE);
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertFalse(ModelElementIndicatorRule.patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, me,
                ExecutionLanguage.SQL));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.PatternLowFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
        Assert.assertTrue(ModelElementIndicatorRule.patternRule(IndicatorEnum.DatePatternFreqIndicatorEnum, me,
                ExecutionLanguage.JAVA));
    }
}
