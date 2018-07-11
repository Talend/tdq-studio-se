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
package org.talend.dataprofiler.core.migration.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.impl.DefinitionFactoryImpl;

/**
 * created by qiongli on Sep 5, 2012 Detailled comment
 * 
 */
public class IndicatorDefinitionFileHelperTest {

    private IndicatorDefinition indiDefinition;


    @Before
    public void setup() {
        indiDefinition = DefinitionFactoryImpl.eINSTANCE.createIndicatorDefinition();
        EList<TdExpression> tdExpessionLs = new BasicEList<TdExpression>();
        TdExpression tdExpression1 = BooleanExpressionHelper.createTdExpression("MySql",//$NON-NLS-1$
                "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%>");//$NON-NLS-1$
        TdExpression tdExpression2 = BooleanExpressionHelper
                .createTdExpression("DB2",//$NON-NLS-1$
                        "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>");//$NON-NLS-1$
        tdExpessionLs.add(tdExpression1);
        tdExpessionLs.add(tdExpression2);
        indiDefinition.getSqlGenericExpression().add(tdExpression1);
        indiDefinition.getSqlGenericExpression().add(tdExpression2);

        UnitTestBuildHelper.initProjectStructure();
    }

    @After
    public void tearDown() throws Exception {
        // UnitTestBuildHelper.deleteCurrentProject();
    }

    /**
     * Test method for judgeing if exist TdExpression of the specify lanuage. abnormal case
     * {@link org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper#isExistSqlExprWithLanguage(org.talend.dataquality.indicators.definition.IndicatorDefinition, java.lang.String)}
     * .
     */
    @Test
    public void testIsExistSqlExprWithLanguage_1() {
        boolean flag = IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(indiDefinition, "Hive");//$NON-NLS-1$
        assertFalse(flag);
    }

    /**
     * Test method for judgeing if exist TdExpression of the specify lanuage. normal case.
     * {@link org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper#isExistSqlExprWithLanguage(org.talend.dataquality.indicators.definition.IndicatorDefinition, java.lang.String)}
     * .
     */
    @Test
    public void testIsExistSqlExprWithLanguage_2() {
        boolean flag = IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(indiDefinition, "MySql");//$NON-NLS-1$
        assertTrue(flag);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper#isSubCategoryIndicator(java.lang.String)}
     * .
     */
    @Test
    public void testIsTechnialIndicatorWithSubCategory() {
        // sub category indicator
        String textStatisticsUuid = "_yb9x0zh8Ed2XmO7pl5Yuyg"; //$NON-NLS-1$
        String summaryStatisticsUuid = "_ccI48BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
        String interQuartileRangeUuid = "_ccI48xF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
        String rangeUuid = "_ccI49hF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
        String simpleStatisticsUuid = "_yb-Y4Dh8Ed2XmO7pl5Yuyg"; //$NON-NLS-1$
        String phoneNumberStatisticsUuid = "_JOE-0LQ2EeCrhIKO626ZRQ"; //$NON-NLS-1$
        String connectionOverviewUuid = "_nZEo8MYSEd27NP4lvE0A4w"; //$NON-NLS-1$
        String catalogOverviewUuid = "_QwDiwMYUEd27NP4lvE0A4w"; //$NON-NLS-1$
        String schemaOverviewUuid = "_V4SA0MYUEd27NP4lvE0A4w"; //$NON-NLS-1$

        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(textStatisticsUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(summaryStatisticsUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(interQuartileRangeUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(rangeUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(simpleStatisticsUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(phoneNumberStatisticsUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(connectionOverviewUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(catalogOverviewUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(schemaOverviewUuid));
    }

    @Test
    public void testIsTechnialIndicator() {
        String tableOverviewUuid = "_hgO7YMYUEd27NP4lvE0A4w"; //$NON-NLS-1$
        String viewOverviewUuid = "_lNIE0MbNEd2d_JPxxDRSfQ"; //$NON-NLS-1$
        String datePatternFrequencyTableUuid = "_OCTbwJR_Ed2XO-JvLwVAaa"; //$NON-NLS-1$
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(tableOverviewUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(viewOverviewUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(datePatternFrequencyTableUuid));

        String ciWordPatternFrequencyUuid = "_frqV0D1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String ciWordPatternLowFrequencyUuid = "_kvvTsT1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String csWordPatternFrequencyUuid = "_UhvbED1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String csWordPatternLowFrequencyUuid = "_Z1RKMD1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(ciWordPatternFrequencyUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(ciWordPatternLowFrequencyUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(csWordPatternFrequencyUuid));
        assertTrue(IndicatorDefinitionFileHelper.isTechnialIndicator(csWordPatternLowFrequencyUuid));

        // null argument will return false.
        assertFalse(IndicatorDefinitionFileHelper.isTechnialIndicator(null));
        assertFalse(IndicatorDefinitionFileHelper.isTechnialIndicator(""));//$NON-NLS-1$

        // normal indicator
        String rowCountUuid = "_ccFOkBF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
        assertFalse(IndicatorDefinitionFileHelper.isTechnialIndicator(rowCountUuid));

        String multipleColumnUuid = "_JoeMkM-jEd6qN5aKpPNGTg";//$NON-NLS-1$
        assertFalse(IndicatorDefinitionFileHelper.isTechnialIndicator(multipleColumnUuid));
    }
}
