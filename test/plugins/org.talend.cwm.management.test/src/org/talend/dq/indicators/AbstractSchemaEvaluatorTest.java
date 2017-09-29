// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.ConnectionIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by qiongli on 2013-11-20 Detailled comment
 * 
 */
@PrepareForTest({ DbmsLanguageFactory.class, ConnectionHelper.class, Catalog.class, SchemaIndicator.class, Connection.class,
        Evaluator.class, Schema.class })
public class AbstractSchemaEvaluatorTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private String dbVersion = "502"; //$NON-NLS-1$

    private String catalogName = "catalog1"; //$NON-NLS-1$

    private SchemaIndicator schemarIndicator = null;

    private Set<Object> analysisElements = new HashSet<Object>();

    private Schema schema = null;

    private Catalog catalog = null;

    @Before
    public void setUp() throws Exception {
        schemarIndicator = mock(SchemaIndicator.class);
        schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();
        catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        catalog.setName(catalogName);

        Connection conn = mock(Connection.class);
        stub(method(ConnectionHelper.class, "getTdDataProvider", Catalog.class)).toReturn(conn); //$NON-NLS-1$
    }

    @Test
    public void testgetCatalogNameWithQuote_mySql() {
        CatalogEvaluator catalogEvaluator = new CatalogEvaluator();
        analysisElements.clear();
        analysisElements.add(catalog);
        stub(method(Evaluator.class, "getAnalyzedElements")).toReturn(analysisElements); //$NON-NLS-1$
        when(schemarIndicator.getAnalyzedElement()).thenReturn(catalog);

        DbmsLanguage mysqlDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MYSQLDEFAULTURL.getLanguage(),
                dbVersion);
        stub(method(DbmsLanguageFactory.class, "createDbmsLanguage", DataManager.class, ExecutionLanguage.class)).toReturn(mysqlDbmsLanguage); //$NON-NLS-1$

        String catalogNameWithQuote = catalogEvaluator.getCatalogNameWithQuote(schemarIndicator);
        assertNotNull(catalogNameWithQuote);
        assertEquals(catalogNameWithQuote, "`" + catalogName + "`"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    @Test
    public void testgetCatalogNameWithQuote_oracle() {
        SchemaEvaluator schemaEvaluate = new SchemaEvaluator();
        analysisElements.clear();
        analysisElements.add(schema);
        stub(method(Evaluator.class, "getAnalyzedElements")).toReturn(analysisElements); //$NON-NLS-1$
        when(schemarIndicator.getAnalyzedElement()).thenReturn(schema);
        DbmsLanguage mysqlDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(
                SupportDBUrlType.ORACLEWITHSIDDEFAULTURL.getLanguage(), dbVersion);
        stub(method(DbmsLanguageFactory.class, "createDbmsLanguage", DataManager.class, ExecutionLanguage.class)).toReturn(mysqlDbmsLanguage); //$NON-NLS-1$

        String catalogNameWithQuote = schemaEvaluate.getCatalogNameWithQuote(schemarIndicator);
        assertNull(catalogNameWithQuote);

    }

    @Test
    public void testgetCatalogNameWithQuote_sqlServer() {
        SchemaEvaluator schemaEvaluator = new SchemaEvaluator();
        catalog.getOwnedElement().add(schema);
        analysisElements.clear();
        analysisElements.add(schema);
        stub(method(Evaluator.class, "getAnalyzedElements")).toReturn(analysisElements); //$NON-NLS-1$
        when(schemarIndicator.getAnalyzedElement()).thenReturn(schema);
        DbmsLanguage mysqlDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQLDEFAULTURL.getLanguage(),
                dbVersion);
        stub(method(DbmsLanguageFactory.class, "createDbmsLanguage", DataManager.class, ExecutionLanguage.class)).toReturn(mysqlDbmsLanguage); //$NON-NLS-1$

        String catalogNameWithQuote = schemaEvaluator.getCatalogNameWithQuote(schemarIndicator);
        assertNotNull(catalogNameWithQuote);
        assertEquals(catalogNameWithQuote, catalogName);

    }

    @Test
    /**
     * 
     * No mock. using  java reflect mechanism to set private variable.
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void testAddToConnectionIndicator2Parameters() throws SQLException, InstantiationException, IllegalAccessException,
            NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        //
        Connection dataProvider = ConnectionFactory.eINSTANCE.createConnection();
        ConnectionIndicator connectionIndicator = SchemaFactory.eINSTANCE.createConnectionIndicator();
        List<Indicator> indicators = new ArrayList<Indicator>();
        indicators.add(connectionIndicator);
        Class<ConnectionEvaluator> connEval = ConnectionEvaluator.class;

        ConnectionEvaluator instance = connEval.newInstance();

        Field field = connEval.getDeclaredField("dataProvider"); //$NON-NLS-1$
        field.setAccessible(true);
        field.set(instance, dataProvider);

        Field field2 = connEval.getDeclaredField("elementIndics"); //$NON-NLS-1$
        field2.setAccessible(true);
        field2.set(instance, indicators);

        SchemaIndicator schemaIndic1 = SchemaFactory.eINSTANCE.createSchemaIndicator();
        schemaIndic1.setTableCount(3);
        schemaIndic1.setViewCount(1);
        schemaIndic1.setTableRowCount(100L);

        SchemaIndicator schemaIndic2 = SchemaFactory.eINSTANCE.createSchemaIndicator();
        schemaIndic2.setTableCount(2);
        schemaIndic2.setViewCount(2);
        schemaIndic2.setTableRowCount(95L);
        CatalogIndicator catalogIndic = SchemaFactory.eINSTANCE.createCatalogIndicator();
        catalogIndic.setSchemaCount(2);
        // Catalog catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();
        // Schema schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();

        //        Method method = AbstractSchemaEvaluator.class.getDeclaredMethod("evalSchemaIndicLow", new Class[] { //$NON-NLS-1$
        // CatalogIndicator.class, SchemaIndicator.class, Catalog.class, Schema.class, ReturnCode.class });
        // method.invoke(instance, catalogIndic, schemaIndic, catalog, schema, new ReturnCode(true));

        //        Method method = connEval.getDeclaredMethod("addToConnectionIndicator", CatalogIndicator.class, SchemaIndicator.class); //$NON-NLS-1$
        // method.setAccessible(true);

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                instance.addToConnectionIndicator(catalogIndic, schemaIndic1);
            } else {
                instance.addToConnectionIndicator(catalogIndic, schemaIndic2);
            }
        }
        assertEquals(connectionIndicator.getCatalogCount(), 1);
        assertEquals(connectionIndicator.getSchemaCount(), 2);
        assertEquals(connectionIndicator.getTableCount(), 5);
        assertEquals(connectionIndicator.getViewCount(), 3);
        assertEquals(connectionIndicator.getTableRowCount(), 195);

    }

}
