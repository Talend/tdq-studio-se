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
package org.talend.dataprofiler.core.ui.editor.preview.model;

import static org.mockito.Mockito.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;

/**
 * DOC yyin  class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TableHelper.class })
public class ChartTableFactoryTest {

    private Indicator dqRule;
    /**
     * DOC yyin Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        dqRule = IndicatorSqlFactory.eINSTANCE.createWhereRuleIndicator();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // no after needs
    }

    /**
     * normal case. Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory#isDqRule(org.talend.dataquality.indicators.Indicator)}
     * .
     */
    @Test
    public void testIsDqRule_1() {
        TdTable analyzedElement = RelationalFactory.eINSTANCE.createTdTable();
        dqRule.setAnalyzedElement(analyzedElement);

        DatabaseConnection connection = mock(DatabaseConnection.class);
        when(connection.getDatabaseType()).thenReturn("MySQL");

        PowerMockito.mockStatic(TableHelper.class);
        when(TableHelper.getFirstConnection(analyzedElement)).thenReturn(connection);

        Assert.assertTrue(ChartTableFactory.isDqRule(dqRule));
    }

    // test for unsurpported db type
    @Test
    public void testIsDqRule_2() {
        TdTable analyzedElement = RelationalFactory.eINSTANCE.createTdTable();
        dqRule.setAnalyzedElement(analyzedElement);

        DatabaseConnection connection = mock(DatabaseConnection.class);
        when(connection.getDatabaseType()).thenReturn("Teradata");

        PowerMockito.mockStatic(TableHelper.class);
        when(TableHelper.getFirstConnection(analyzedElement)).thenReturn(connection);

        Assert.assertFalse(ChartTableFactory.isDqRule(dqRule));
    }

    // test for null indicator
    @Test
    public void testIsDqRule_3() {
        Assert.assertFalse(ChartTableFactory.isDqRule(null));
    }

    // test for the analyzed element is null
    @Test
    public void testIsDqRule_4() {
        Assert.assertFalse(ChartTableFactory.isDqRule(dqRule));
    }

    // test for the analyzed element is not a table
    @Test
    public void testIsDqRule_5() {
        TdView createTdView = RelationalFactory.eINSTANCE.createTdView();
        dqRule.setAnalyzedElement(createTdView);

        Assert.assertFalse(ChartTableFactory.isDqRule(dqRule));
    }

    // test for other surpported db type
    @Test
    public void testIsDqRule_6() {
        TdTable analyzedElement = RelationalFactory.eINSTANCE.createTdTable();
        dqRule.setAnalyzedElement(analyzedElement);

        DatabaseConnection connection = mock(DatabaseConnection.class);
        when(connection.getDatabaseType()).thenReturn("Oracle with SID");

        PowerMockito.mockStatic(TableHelper.class);
        when(TableHelper.getFirstConnection(analyzedElement)).thenReturn(connection);

        Assert.assertTrue(ChartTableFactory.isDqRule(dqRule));
    }

    @Test
    public void testIsDqRule_7() {
        TdTable analyzedElement = RelationalFactory.eINSTANCE.createTdTable();
        dqRule.setAnalyzedElement(analyzedElement);

        DatabaseConnection connection = mock(DatabaseConnection.class);
        when(connection.getDatabaseType()).thenReturn("PostgreSQL");

        PowerMockito.mockStatic(TableHelper.class);
        when(TableHelper.getFirstConnection(analyzedElement)).thenReturn(connection);

        Assert.assertTrue(ChartTableFactory.isDqRule(dqRule));
    }

    // test for the indicator isnot a dq rule(where rule)
    @Test
    public void testIsDqRule_8() {
        Indicator udi = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        TdTable analyzedElement = RelationalFactory.eINSTANCE.createTdTable();
        udi.setAnalyzedElement(analyzedElement);

        DatabaseConnection connection = mock(DatabaseConnection.class);
        when(connection.getDatabaseType()).thenReturn("Oracle with SID");

        PowerMockito.mockStatic(TableHelper.class);
        when(TableHelper.getFirstConnection(analyzedElement)).thenReturn(connection);

        Assert.assertFalse(ChartTableFactory.isDqRule(udi));
    }
}
