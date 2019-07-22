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
package org.talend.dq.indicators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;


/**
 * Check if every related sql expression for Netezza db has been added into related text indicators, benford indicator,
 * and soundex indicators
 */
public class TextIndicatorForNetezzaTest {

    private static final String AVERAGE_LENGTH = "Average Length"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_SQL = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL = "Average Length With Blank and Null"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_SQL =
            "SELECT SUM(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_NULL = "Average Length With Null"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_NULL_SQL = "SELECT SUM(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH = "Maximal Length"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_SQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_NULL = "Maximal Length With Null"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_NULL_SQL = "SELECT MAX(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL = "Maximal Length With Blank and Null";//$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL = "SELECT MAX(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$

    private static final String MINIMAL_LENGTH = "Minimal Length"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_SQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL = "Minimal Length With Blank and Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL = "SELECT MIN(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_NULL = "Minimal Length With Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_NULL_SQL = "SELECT MIN(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String BENFORD_LAW = "Benford Law Frequency";//$NON-NLS-1$

    private static final String BENFORD_LAW_SQL = "SELECT cast(SUBSTR(<%=__COLUMN_NAMES__%>,1,1) as char), COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 1 order by 1";//$NON-NLS-1$

    private static final String SOUNDEX_LOW_FREQUENCY = "Soundex Low Frequency Table";//$NON-NLS-1$

    private static final String SOUNDEX_LOW_FREQUENCY_SQL = "SELECT MAX(<%=__COLUMN_NAMES__%>), NYSIIS(<%=__COLUMN_NAMES__%>), COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY NYSIIS(<%=__COLUMN_NAMES__%>) ORDER BY d,c ASC";//$NON-NLS-1$

    private static final String SOUNDEX_FREQUENCY = "Soundex Frequency Table";//$NON-NLS-1$

    private static final String SOUNDEX_FREQUENCY_SQL = "SELECT MAX(<%=__COLUMN_NAMES__%>), NYSIIS(<%=__COLUMN_NAMES__%>) , COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 2 ORDER BY d DESC,c DESC";//$NON-NLS-1$

    private static final String PATTERN_LOW_FREQUENCY = "Pattern Low Frequency Table"; //$NON-NLS-1$

    private static final String PATTERN_LOW_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c ASC";//$NON-NLS-1$

    private static final String PATTERN_FREQUENCY = "Pattern Frequency Table";//$NON-NLS-1$

    private static final String PATTERN_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) AS c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c DESC";//$NON-NLS-1$

    private final String Netezza = SupportDBUrlType.NETEZZADEFAULTURL.getLanguage();

    /**
     * init TDQ_Libraries folder
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
    }

    @Test
    public void testAverageLength() {
        TdExpression expression = findExpressionForNetezza(AVERAGE_LENGTH);
        Assert.assertNotNull(expression);
        Assert.assertEquals(AVERAGE_LENGTH_SQL, expression.getBody());
    }

    @Test
    public void testAverageLengthWithBlankAndNull() {
        TdExpression expression = findExpressionForNetezza(AVERAGE_LENGTH_WITH_BLANK_AND_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_SQL, expression.getBody());
    }

    @Test
    public void testAverageLengthWithNull() {
        TdExpression expression = findExpressionForNetezza(AVERAGE_LENGTH_WITH_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(AVERAGE_LENGTH_WITH_NULL_SQL, expression.getBody());
    }

    @Test
    public void testMaxLength() {
        TdExpression expression = findExpressionForNetezza(MAXIMAL_LENGTH);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MAXIMAL_LENGTH_SQL, expression.getBody());
    }

    @Test
    public void testMaxLengthWithNull() {
        TdExpression expression = findExpressionForNetezza(MAXIMAL_LENGTH_WITH_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MAXIMAL_LENGTH_WITH_NULL_SQL, expression.getBody());
    }

    @Test
    public void testMaxLengthWithBlankNull() {
        TdExpression expression = findExpressionForNetezza(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL, expression.getBody());
    }

    @Test
    public void testMinLength() {
        TdExpression expression = findExpressionForNetezza(MINIMAL_LENGTH);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MINIMAL_LENGTH_SQL, expression.getBody());
    }

    @Test
    public void testMinLengthBlankNull() {
        TdExpression expression = findExpressionForNetezza(MINIMAL_LENGTH_WITH_BLANK_AND_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL, expression.getBody());
    }

    @Test
    public void testMinLengthNull() {
        TdExpression expression = findExpressionForNetezza(MINIMAL_LENGTH_WITH_NULL);
        Assert.assertNotNull(expression);
        Assert.assertEquals(MINIMAL_LENGTH_WITH_NULL_SQL, expression.getBody());
    }

    @Test
    public void testBenFordLaw() {
        TdExpression expression = findExpressionForNetezza(BENFORD_LAW);
        Assert.assertNotNull(expression);
        Assert.assertEquals(BENFORD_LAW_SQL, expression.getBody());
    }

    @Test
    public void testSoundexLow() {
        TdExpression expression = findExpressionForNetezza(SOUNDEX_LOW_FREQUENCY);
        Assert.assertNotNull(expression);
        Assert.assertEquals(SOUNDEX_LOW_FREQUENCY_SQL, expression.getBody());
    }

    @Test
    public void testSoundex() {
        TdExpression expression = findExpressionForNetezza(SOUNDEX_FREQUENCY);
        Assert.assertNotNull(expression);
        Assert.assertEquals(SOUNDEX_FREQUENCY_SQL, expression.getBody());
    }

    @Test
    public void testPatternLow() {
        TdExpression expression = findExpressionForNetezza(PATTERN_LOW_FREQUENCY);
        Assert.assertNotNull(expression);
        Assert.assertEquals(PATTERN_LOW_FREQUENCY_SQL, expression.getBody());
    }

    @Test
    public void testPattern() {
        TdExpression expression = findExpressionForNetezza(PATTERN_FREQUENCY);
        Assert.assertNotNull(expression);
        Assert.assertEquals(PATTERN_FREQUENCY_SQL, expression.getBody());
    }

    private TdExpression findExpressionForNetezza(String indicatorName) {
        IndicatorDefinition indiDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(indicatorName);
        for (TdExpression e : indiDefinition.getSqlGenericExpression()) {
            if (e.getLanguage().equals(Netezza)) {
                return e;
            }
        }
        return null;
    }

}
