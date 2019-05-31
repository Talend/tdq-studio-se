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

    @Test
    public void testPatternRule_sybase() {
        when(tdSqlDataType.getJavaDataType()).thenReturn(Types.VARCHAR);
        DatabaseConnection connection = mock(DatabaseConnection.class);
        PowerMockito.mockStatic(ConnectionHelper.class);
        when(ConnectionHelper.getTdDataProvider(tdColumn)).thenReturn(connection);
        when(ConnectionHelper.isTeradata(connection)).thenReturn(true);
        DbmsLanguage dbmsLanguage = mock(DbmsLanguage.class);
        when(dbmsLanguage.getDbmsName()).thenReturn("Sybase"); //$NON-NLS-1$
        when(dbmsLanguage.getDbVersion()).thenReturn(null);

        PowerMockito.mockStatic(DbmsLanguageFactory.class);
        when(DbmsLanguageFactory.createDbmsLanguage(connection)).thenReturn(dbmsLanguage);

        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn, ExecutionLanguage.SQL));
        Assert
                .assertTrue(ModelElementIndicatorRule
                        .patternRule(IndicatorEnum.BenfordLawFrequencyIndicatorEnum, tdColumn, ExecutionLanguage.JAVA));

    }
}
