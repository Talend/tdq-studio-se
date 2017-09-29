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
package org.talend.dq.dbms;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UserdefineFactory;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.utils.ProductVersion;
import org.talend.utils.dates.DateUtils;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class DbmsLanguageTest {

    private static final String sql_1 = "SELECT 1 FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String sql_2 = "SELECT 2 FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private static final String TEST_STRING = "TEST_STRING"; //$NON-NLS-1$

    private static final String DATABASE_TYPE_MYSQL = "MySQL"; //$NON-NLS-1$

    private static final String DATABASE_VERSION = "5.0.2"; //$NON-NLS-1$

    private static final String CATALOG_NAME = "catalog_name"; //$NON-NLS-1$

    private static final String SCHEMA_NAME = "schema_name"; //$NON-NLS-1$

    private static final String TABLE_NAME = "table_name"; //$NON-NLS-1$

    private static final String TABLE_NAME2 = "table_name2"; //$NON-NLS-1$

    private static final String COLUMN_NAME = "column_name"; //$NON-NLS-1$

    private static final String COLUMN_A_NAME = "column_a_name"; //$NON-NLS-1$

    private static final String COLUMN_B_NAME = "column_b_name"; //$NON-NLS-1$

    private static final String KEY = "key"; //$NON-NLS-1$

    private static final String REPLACEMENT_STR = "replacement_str"; //$NON-NLS-1$

    private static final String ALIAS_NAME = "alias_name"; //$NON-NLS-1$

    private static final String DB_QUOTE_STRING = "db_quote_string"; //$NON-NLS-1$

    private static final int TOP_N = 100;

    public static final String SPACE = " "; //$NON-NLS-1$

    private static final String QUERY_STR = "select " + GenericSQLHandler.COLUMN_NAMES + " from " + GenericSQLHandler.TABLE_NAME; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String QUERY_AB_STR = "select " + GenericSQLHandler.COLUMN_NAMES_A + ", " + GenericSQLHandler.COLUMN_NAMES_B + " from " + GenericSQLHandler.TABLE_NAME; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String QUERY_WHERE_STR = QUERY_STR + SPACE + GenericSQLHandler.WHERE_CLAUSE + SPACE
            + GenericSQLHandler.UDI_WHERE;

    private static final String QUERY_PATTERN_STR = "SELECT COUNT(CASE WHEN " + GenericSQLHandler.COLUMN_NAMES //$NON-NLS-1$
            + " REGEXP BINARY " + GenericSQLHandler.PATTERN_EXPRESSION + " THEN 1 END), COUNT(*) FROM " //$NON-NLS-1$ //$NON-NLS-2$
            + GenericSQLHandler.TABLE_NAME + SPACE + GenericSQLHandler.WHERE_CLAUSE;

    private static final String QUERY_GROUPBY_STR = "select count(*), " + GenericSQLHandler.COLUMN_NAMES + " from " //$NON-NLS-1$ //$NON-NLS-2$
            + GenericSQLHandler.TABLE_NAME + SPACE + GenericSQLHandler.GROUP_BY_ALIAS;

    private static final String QUERY_JOIN_WHERE_STR = "SELECT COUNT(*) FROM " + GenericSQLHandler.TABLE_NAME //$NON-NLS-1$
            + " LEFT JOIN " + GenericSQLHandler.TABLE_NAME2 + " ON (" + GenericSQLHandler.JOIN_CLAUSE + ") WHERE (" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            + GenericSQLHandler.WHERE_CLAUSE + ")"; //$NON-NLS-1$

    private static final String REGEXP = "'su.*'"; //$NON-NLS-1$

    private static final String REGEXP_STR = "su.a"; //$NON-NLS-1$

    private static final String ORDER_ASC = "ASC"; //$NON-NLS-1$

    private static final String ORDER_DESC = "DESC"; //$NON-NLS-1$

    private static final String JOIN_CLAUSE = "join_clause"; //$NON-NLS-1$

    private static final String WHERE_CLAUSE = "where_clause"; //$NON-NLS-1$

    private static final String OPERATOR = "="; //$NON-NLS-1$

    private static final String FUNCTION = "function"; //$NON-NLS-1$

    private static final String JavaRegex = "'^java expression$'"; //$NON-NLS-1$

    private static final String SqlRegex = "'^sql expression$'"; //$NON-NLS-1$

    private static final String MssqlRegex = "'^mssql expression$'"; //$NON-NLS-1$

    /**
     * get a default MySQL DbmsLanguage for test
     * 
     * @return
     */
    private DbmsLanguage getMysqlDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DATABASE_TYPE_MYSQL, DATABASE_VERSION);

    }

    /**
     * create a default pattern for test
     * 
     * @return
     */
    private Pattern createPattern() {
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName("My Pattern"); //$NON-NLS-1$
        RegularExpression regularExpr = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression expression = createExpression("SQL"); //$NON-NLS-1$
        regularExpr.setExpression(expression);
        pattern.getComponents().add(regularExpr);

        RegularExpression regularExpr2 = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression expression2 = createExpression("MySql"); //$NON-NLS-1$
        regularExpr2.setExpression(expression2);
        pattern.getComponents().add(regularExpr2);

        return pattern;
    }

    /**
     * create a default expression for test
     * 
     * @return
     */
    private TdExpression createExpression(String language) {
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(REGEXP);
        expression.setLanguage(language);
        return expression;
    }

    /**
     * create a default column name list for test
     * 
     * @return
     */
    private List<String> createColumns() {
        List<String> columns = new ArrayList<String>();
        columns.add(COLUMN_NAME);
        columns.add(COLUMN_NAME + "1"); //$NON-NLS-1$
        columns.add(COLUMN_NAME + "2"); //$NON-NLS-1$
        return columns;
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#quote(java.lang.String)}.
     */
    @Test
    public void testQuote() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.quote(TEST_STRING).indexOf(TEST_STRING) > -1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#and()}.
     */
    @Test
    public void testAnd() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.and());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#or()}.
     */
    @Test
    public void testOr() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.or());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#greater()}.
     */
    @Test
    public void testGreater() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.greater());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#greaterOrEqual()}.
     */
    @Test
    public void testGreaterOrEqual() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.greaterOrEqual());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#less()}.
     */
    @Test
    public void testLess() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.less());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#lessOrEqual()}.
     */
    @Test
    public void testLessOrEqual() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.lessOrEqual());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#between()}.
     */
    @Test
    public void testBetween() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.between());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#isNull()}.
     */
    @Test
    public void testIsNull() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.isNull());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#isNotNull()}.
     */
    @Test
    public void testIsNotNull() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.isNotNull());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#equal()}.
     */
    @Test
    public void testEqual() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.equal());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#notEqual()}.
     */
    @Test
    public void testNotEqual() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.notEqual());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#unionAll()}.
     */
    @Test
    public void testUnionAll() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.unionAll());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#all()}.
     */
    @Test
    public void testAll() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.all());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#union()}.
     */
    @Test
    public void testUnion() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.union());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#from()}.
     */
    @Test
    public void testFrom() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.from());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#eos()}.
     */
    @Test
    public void testEos() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.eos());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getDbmsName()}.
     */
    @Test
    public void testGetDbmsName() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getDbmsName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getDbVersion()}.
     */
    @Test
    public void testGetDbVersion() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getDbVersion());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getDefaultLanguage()}.
     */
    @Test
    public void testGetDefaultLanguage() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getDefaultLanguage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testToQualifiedName() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertFalse(StringUtils.isEmpty(dbms.toQualifiedName(CATALOG_NAME, SCHEMA_NAME, TABLE_NAME)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getDelimiter()}.
     */
    @Test
    public void testGetDelimiter() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getDelimiter());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getPatternFinderFunction(java.lang.String, org.eclipse.emf.common.util.EList)}
     * .
     * 
     * case 1 : netezza case
     */
    @Test
    public void testGetPatternFinderFunctionStringEListOfCharactersMappingCase1() {

        String expectedResult = "TRANSLATE(name , \'abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789Netezza\' , \'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999Netezza\')"; //$NON-NLS-1$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);

        IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        EList<CharactersMapping> charactersMapping = createIndicatorDefinition.getCharactersMapping();
        CharactersMapping createCharactersMapping = DefinitionFactory.eINSTANCE.createCharactersMapping();
        // mysql
        createCharactersMapping.setLanguage(SupportDBUrlType.MYSQLDEFAULTURL.getLanguage());
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789MySql"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999MySql"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~mysql

        // default
        createCharactersMapping.setLanguage(DbmsLanguage.SQL);
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789Default"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999Default"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~default

        // netezza
        createCharactersMapping.setLanguage(SupportDBUrlType.NETEZZADEFAULTURL.getLanguage());
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789Netezza"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999Netezza"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~netezza
        String patternFinderFunction = netezzaDbmsLanguage.getPatternFinderFunction("name", charactersMapping); //$NON-NLS-1$
        Assert.assertEquals(expectedResult, patternFinderFunction);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getPatternFinderFunction(java.lang.String, org.eclipse.emf.common.util.EList)}
     * .
     * 
     * case 2 : default case
     */
    @Test
    public void testGetPatternFinderFunctionStringEListOfCharactersMappingCase2() {

        String expectedResult = "TRANSLATE(name , \'abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789Default\' , \'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999Default\')"; //$NON-NLS-1$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);

        IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        EList<CharactersMapping> charactersMapping = createIndicatorDefinition.getCharactersMapping();
        CharactersMapping createCharactersMapping = DefinitionFactory.eINSTANCE.createCharactersMapping();
        // mysql
        createCharactersMapping.setLanguage(SupportDBUrlType.MYSQLDEFAULTURL.getLanguage());
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789MySql"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999MySql"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~mysql

        // default
        createCharactersMapping.setLanguage(DbmsLanguage.SQL);
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789Default"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999Default"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~default

        String patternFinderFunction = netezzaDbmsLanguage.getPatternFinderFunction("name", charactersMapping); //$NON-NLS-1$
        Assert.assertEquals(expectedResult, patternFinderFunction);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getPatternFinderFunction(java.lang.String, org.eclipse.emf.common.util.EList)}
     * .
     * 
     * case 3 : no default and no netezza
     */
    @Test
    public void testGetPatternFinderFunctionStringEListOfCharactersMappingCase3() {

        String expectedResult = null;
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);

        IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        EList<CharactersMapping> charactersMapping = createIndicatorDefinition.getCharactersMapping();
        CharactersMapping createCharactersMapping = DefinitionFactory.eINSTANCE.createCharactersMapping();
        // mysql
        createCharactersMapping.setLanguage(SupportDBUrlType.MYSQLDEFAULTURL.getLanguage());
        createCharactersMapping
                .setCharactersToReplace("abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789MySql"); //$NON-NLS-1$
        createCharactersMapping
                .setReplacementCharacters("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999MySql"); //$NON-NLS-1$
        charactersMapping.add(createCharactersMapping);
        // ~mysql

        String patternFinderFunction = netezzaDbmsLanguage.getPatternFinderFunction("name", charactersMapping); //$NON-NLS-1$
        Assert.assertEquals(expectedResult, patternFinderFunction);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#replaceNullsWithString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceNullsWithString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.replaceNullsWithString(COLUMN_NAME, REPLACEMENT_STR));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#isNotBlank(java.lang.String)}.
     */
    @Test
    public void testIsNotBlank() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.isNotBlank(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#trim(java.lang.String)}.
     */
    @Test
    public void testTrim() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.trim(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#toUpperCase(java.lang.String)}.
     */
    @Test
    public void testToUpperCase() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.toUpperCase(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#toLowerCase(java.lang.String)}.
     */
    @Test
    public void testToLowerCase() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.toLowerCase(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#extractYear(java.lang.String)}.
     */
    @Test
    public void testExtractYear() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.extractYear(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#extractQuarter(java.lang.String)}.
     */
    @Test
    public void testExtractQuarter() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.extractQuarter(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#extractMonth(java.lang.String)}.
     */
    @Test
    public void testExtractMonth() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.extractMonth(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#extractWeek(java.lang.String)}.
     */
    @Test
    public void testExtractWeek() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.extractWeek(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#extractDay(java.lang.String)}.
     */
    @Test
    public void testExtractDay() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.extractDay(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getTopNQuery(java.lang.String, int)}.
     */
    @Test
    public void testGetTopNQuery() {
        try {
            String limitStr = " LIMIT "; //$NON-NLS-1$
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertEquals(QUERY_STR + limitStr + TOP_N, dbms.getTopNQuery(QUERY_STR, TOP_N));

            dbms = getPostgresqlDbmsLanguage();
            Assert.assertEquals(QUERY_STR + limitStr + TOP_N, dbms.getTopNQuery(QUERY_STR, TOP_N));

            dbms = getVerticaDbmsLanguage();
            Assert.assertEquals(QUERY_STR + limitStr + TOP_N, dbms.getTopNQuery(QUERY_STR, TOP_N));

            dbms = getNetzzaDbmsLanguage();
            Assert.assertEquals(QUERY_STR + limitStr + TOP_N, dbms.getTopNQuery(QUERY_STR, TOP_N));

            dbms = getHiveDbmsLanguage();
            Assert.assertEquals(QUERY_STR + limitStr + TOP_N, dbms.getTopNQuery(QUERY_STR, TOP_N));

            dbms = getMsSqlDbmsLanguage();
            Assert.assertEquals(
                    "SELECT TOP " + TOP_N + "  " + GenericSQLHandler.COLUMN_NAMES + " from " + GenericSQLHandler.TABLE_NAME, dbms.getTopNQuery(QUERY_STR, TOP_N)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            dbms = getSybaseDbmsLanguage();
            Assert.assertEquals("SELECT TOP " + TOP_N + "  " + GenericSQLHandler.COLUMN_NAMES + " from " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + GenericSQLHandler.TABLE_NAME, dbms.getTopNQuery(QUERY_STR, TOP_N)); // $NON

            dbms = getTeradataDbmsLanguage();
            Assert.assertEquals("SELECT TOP " + TOP_N + "  " + GenericSQLHandler.COLUMN_NAMES + " from " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + GenericSQLHandler.TABLE_NAME, dbms.getTopNQuery(QUERY_STR, TOP_N)); // $NON

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * DOC qiongli Comment method "getHiveDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getHiveDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.HIVE, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getNetzzaDbmsLanguage".
     */
    private DbmsLanguage getNetzzaDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.NETEZZA, DATABASE_VERSION);
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#countRowInSubquery(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCountRowInSubquery() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.countRowInSubquery(QUERY_STR, ALIAS_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#sumRowInSubquery(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSumRowInSubquery() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.sumRowInSubquery(COLUMN_NAME, QUERY_STR, ALIAS_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#selectAllRowsWhereColumnIn(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSelectAllRowsWhereColumnIn() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.selectAllRowsWhereColumnIn(COLUMN_NAME, TABLE_NAME, QUERY_STR));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#selectColumnsFromTable(java.util.List, java.lang.String)}.
     */
    @Test
    public void testSelectColumnsFromTable() {
        try {
            List<String> columns = new ArrayList<String>();
            columns.add(COLUMN_NAME);
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.selectColumnsFromTable(columns, TABLE_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#in()}.
     */
    @Test
    public void testIn() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.in());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#notIn()}.
     */
    @Test
    public void testNotIn() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.notIn());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#not()}.
     */
    @Test
    public void testNot() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.not());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getDbQuoteString()}.
     */
    @Test
    public void testGetDbQuoteString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getDbQuoteString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#setDbQuoteString(java.lang.String)}.
     */
    @Test
    public void testSetDbQuoteString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            dbms.setDbQuoteString(DB_QUOTE_STRING);
            Assert.assertEquals(dbms.getDbQuoteString(), DB_QUOTE_STRING);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#addWhereToSqlStringStatement(java.lang.String, java.util.List)}.
     */
    @Test
    public void testAddWhereToSqlStringStatement() {
        try {
            String w1 = "a=a"; //$NON-NLS-1$
            String w2 = "1>0"; //$NON-NLS-1$
            List<String> whereExpressions = new ArrayList<String>();
            whereExpressions.add(w1);
            whereExpressions.add(w2);

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            String addWhereToSqlStringStatement = dbms.addWhereToSqlStringStatement(QUERY_WHERE_STR, whereExpressions);
            Assert.assertTrue(addWhereToSqlStringStatement.indexOf(GenericSQLHandler.WHERE_CLAUSE) < 0
                    && addWhereToSqlStringStatement.indexOf(w1) > -1 && addWhereToSqlStringStatement.indexOf(w2) > -1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#addWhereToStatement(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddWhereToStatement() {
        try {
            String w1 = "a=a"; //$NON-NLS-1$

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            String addWhereToStatement = dbms.addWhereToStatement(QUERY_WHERE_STR, w1);
            Assert.assertTrue(addWhereToStatement.indexOf(GenericSQLHandler.WHERE_CLAUSE) < 0
                    && addWhereToStatement.indexOf(GenericSQLHandler.UDI_WHERE) < 0 && addWhereToStatement.indexOf(w1) > -1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#where()}.
     */
    @Test
    public void testWhere() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.where());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#buildWhereExpression(java.util.List)}.
     */
    @Test
    public void testBuildWhereExpression() {
        try {
            String w1 = "a=a"; //$NON-NLS-1$
            String w2 = "1>0"; //$NON-NLS-1$
            List<String> whereExpressions = new ArrayList<String>();
            whereExpressions.add(w1);
            whereExpressions.add(w2);

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            String buildWhereExpression = dbms.buildWhereExpression(whereExpressions);
            Assert.assertTrue(buildWhereExpression.indexOf(w1) > -1 && buildWhereExpression.indexOf(w2) > -1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#desc()}.
     */
    @Test
    public void testDesc() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.desc());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#orderBy()}.
     */
    @Test
    public void testOrderBy() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.orderBy());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#groupBy()}.
     */
    @Test
    public void testGroupBy() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.groupBy());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testGetSqlExpressionUserDefineCase1() {
        // create indicator
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        UDIndicatorDefinition indicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName("user define"); //$NON-NLS-1$
        userDefIndicator.setName(indicatorDefinition.getName());
        userDefIndicator.setIndicatorDefinition(indicatorDefinition);

        TdExpression newTdExp = BooleanExpressionHelper.createTdExpression("MySQL", //$NON-NLS-1$
                sql_1, null);
        newTdExp.setModificationDate(DateUtils.getCurrentDate(DateUtils.PATTERN_5));
        indicatorDefinition.getSqlGenericExpression().add(newTdExp);

        TdExpression newTdExp_2 = BooleanExpressionHelper.createTdExpression("SQL", //$NON-NLS-1$
                sql_2, null);
        newTdExp_2.setModificationDate(DateUtils.getCurrentDate(DateUtils.PATTERN_5));
        indicatorDefinition.getSqlGenericExpression().add(newTdExp_2);
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        Expression sqlExpression = dbms.getSqlExpression(indicatorDefinition);
        Assert.assertNotNull(sqlExpression);
        Assert.assertEquals(sql_1, sqlExpression.getBody());
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testGetSqlExpressionUserDefineCase2() {
        // test for can not get sql expression(means: both database type and default type don't exist)
        // create indicator
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        UDIndicatorDefinition indicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName("user define"); //$NON-NLS-1$
        userDefIndicator.setName(indicatorDefinition.getName());
        userDefIndicator.setIndicatorDefinition(indicatorDefinition);

        TdExpression newTdExp = BooleanExpressionHelper.createTdExpression("Ingres", //$NON-NLS-1$
                sql_1, null);
        newTdExp.setModificationDate(DateUtils.getCurrentDate(DateUtils.PATTERN_5));
        indicatorDefinition.getSqlGenericExpression().add(newTdExp);

        DbmsLanguage dbms = getMysqlDbmsLanguage();
        Expression sqlExpression = dbms.getSqlExpression(indicatorDefinition);
        Assert.assertNull(sqlExpression);
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getSoundexFunction(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetSoundexFunction() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getSoundexFunction(TABLE_NAME, COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getFreqRowsStatement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetFreqRowsStatement() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNull(dbms.getFreqRowsStatement(COLUMN_NAME, TABLE_NAME, KEY));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getAggregate1argFunctions(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testGetAggregate1argFunctions() {
        // TODO need to implement this method!!!
        // need an IndicatorDefinition object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getDate1argFunctions(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testGetDate1argFunctions() {
        // TODO need to implement this method!!!
        // need an IndicatorDefinition object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexPatternString(org.talend.dataquality.indicators.PatternMatchingIndicator)}
     * .
     */
    @Test
    public void testGetRegexPatternString() {
        // TODO need to implement this method!!!
        // need an Indicator object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexp() {
        Pattern pattern = createPattern();

        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getRegexp(pattern));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexpGetMssqlFromContainDefault() {
        // Pattern
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        // ~Pattern

        // init java Expression data
        RegularExpression createJavaRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createJavaTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createJavaTdExpression.setBody(JavaRegex);
        createJavaTdExpression.setLanguage(SupportDBUrlType.JAVADEFAULTURL.getLanguage());
        createJavaRegularExpression.setExpression(createJavaTdExpression);
        createJavaRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createPattern.getComponents();
        components.add(createJavaRegularExpression);
        // ~init java Expression data

        // init sql Expression data
        RegularExpression createSqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createSqlTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createSqlTdExpression.setBody(SqlRegex);
        createSqlTdExpression.setLanguage(ExecutionLanguage.SQL.getName());
        createSqlRegularExpression.setExpression(createSqlTdExpression);
        createSqlRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        components = createPattern.getComponents();
        components.add(createSqlRegularExpression);
        // ~init sql Expression data

        // init mssql Expression data
        RegularExpression createMssqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createMssqlTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createMssqlTdExpression.setBody(MssqlRegex);
        createMssqlTdExpression.setLanguage(SupportDBUrlType.MSSQLDEFAULTURL.getLanguage());
        createMssqlRegularExpression.setExpression(createMssqlTdExpression);
        createMssqlRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        components = createPattern.getComponents();
        components.add(createMssqlRegularExpression);
        // ~init mssql Expression data

        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQLDEFAULTURL);
            Assert.assertTrue(MssqlRegex.equalsIgnoreCase(dbms.getRegexp(createPattern).getBody()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexpGetJavaFromContainDefault() {
        // Pattern
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        // ~Pattern

        // init sql Expression data
        RegularExpression createSqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createSqlTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createSqlTdExpression.setBody(SqlRegex);
        createSqlTdExpression.setLanguage(ExecutionLanguage.SQL.getName());
        createSqlRegularExpression.setExpression(createSqlTdExpression);
        createSqlRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createPattern.getComponents();
        components.add(createSqlRegularExpression);
        // ~init sql Expression data

        // init mssql Expression data
        RegularExpression createMssqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createMssqlTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createMssqlTdExpression.setBody(MssqlRegex);
        createMssqlTdExpression.setLanguage(SupportDBUrlType.MSSQLDEFAULTURL.getLanguage());
        createMssqlRegularExpression.setExpression(createMssqlTdExpression);
        createMssqlRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        components = createPattern.getComponents();
        components.add(createMssqlRegularExpression);
        // ~init mssql Expression data

        // init java Expression data
        RegularExpression createJavaRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createJavaTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createJavaTdExpression.setBody(JavaRegex);
        createJavaTdExpression.setLanguage(SupportDBUrlType.JAVADEFAULTURL.getLanguage());
        createJavaRegularExpression.setExpression(createJavaTdExpression);
        createJavaRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        components = createPattern.getComponents();
        components.add(createJavaRegularExpression);
        // ~init java Expression data

        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.JAVADEFAULTURL);
            Assert.assertTrue(JavaRegex.equalsIgnoreCase(dbms.getRegexp(createPattern).getBody()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexpGetMssqlFromContainDefaultAndWithoutMssql() {
        // Pattern
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        // ~Pattern

        // init sql Expression data
        RegularExpression createSqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createSqlTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createSqlTdExpression.setBody(SqlRegex);
        createSqlTdExpression.setLanguage(ExecutionLanguage.SQL.getName());
        createSqlRegularExpression.setExpression(createSqlTdExpression);
        createSqlRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        EList<PatternComponent> components = createPattern.getComponents();
        components.add(createSqlRegularExpression);
        // ~init sql Expression data

        // init java Expression data
        RegularExpression createJavaRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        TdExpression createJavaTdExpression = RelationalFactory.eINSTANCE.createTdExpression();
        createJavaTdExpression.setBody(JavaRegex);
        createJavaTdExpression.setLanguage(SupportDBUrlType.JAVADEFAULTURL.getLanguage());
        createJavaRegularExpression.setExpression(createJavaTdExpression);
        createJavaRegularExpression.setExpressionType(ExpressionType.REGEXP.getLiteral());
        components = createPattern.getComponents();
        components.add(createJavaRegularExpression);
        // ~init java Expression data

        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQLDEFAULTURL);
            Assert.assertTrue(SqlRegex.equalsIgnoreCase(dbms.getRegexp(createPattern).getBody()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexpGetNullWhenRegexStringIsNull() {
        // Pattern
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        // ~Pattern

        // init sql Expression data
        RegularExpression createSqlRegularExpression = PatternFactory.eINSTANCE.createRegularExpression();
        EList<PatternComponent> components = createPattern.getComponents();
        components.add(createSqlRegularExpression);
        // ~init sql Expression data
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQLDEFAULTURL);
            Assert.assertTrue(null == dbms.getRegexp(createPattern));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getRegexp(org.talend.dataquality.domain.pattern.Pattern, boolean)}.
     */
    @Test
    public void testGetRegexpGetNullFromNotAnyExperssion() {
        // Pattern
        Pattern createPattern = PatternFactory.eINSTANCE.createPattern();
        // ~Pattern
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQLDEFAULTURL);
            Assert.assertTrue(null == dbms.getRegexp(createPattern));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getExpression(orgomg.cwm.objectmodel.core.ModelElement, boolean)}.
     */
    @Test
    public void testGetExpressionModelElementBoolean() {
        // TODO need to implement this method!!!
        // need an Indicator object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getBackSlashForRegex()}.
     */
    @Test
    public void testGetBackSlashForRegex() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getBackSlashForRegex());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getExpression(org.talend.dataquality.domain.pattern.PatternComponent)}.
     */
    @Test
    public void testGetExpressionPatternComponent() {
        try {
            Pattern createPattern = createPattern();
            EList<PatternComponent> components = createPattern.getComponents();
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getExpression(components.get(0)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#isApplicable(orgomg.cwm.objectmodel.core.Expression)}.
     */
    @Test
    public void testIsApplicable() {
        try {
            Expression createExpression = createExpression("Mysql"); //$NON-NLS-1$
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.isApplicable(createExpression));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectRegexpTestString(java.lang.String, orgomg.cwm.objectmodel.core.Expression)}
     * .
     */
    @Test
    public void testGetSelectRegexpTestStringStringExpression() {
        try {
            Expression createExpression = createExpression("Mysql"); //$NON-NLS-1$
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getSelectRegexpTestString(REGEXP_STR, createExpression));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectRegexpTestString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetSelectRegexpTestStringStringString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getSelectRegexpTestString(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 1 :database type is mysql
     */
    @Test
    public void testRegexLikeCase1() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR + dbms.surroundWithSpaces(dbms.getRegularExpressionFunction()) + REGEXP),
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 2 : database type is oracle
     */
    @Test
    public void testRegexLikeCase2() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.ORACLEWITHSIDDEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(dbms.getRegularExpressionFunction() + "(" + REGEXP_STR + " , " + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 3 : database type is postgresql
     */
    @Test
    public void testRegexLikeCase3() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.POSTGRESQLEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR + dbms.surroundWithSpaces(dbms.getRegularExpressionFunction()) + REGEXP),
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 4 : database type is Hive
     */
    @Test
    public void testRegexLikeCase4() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.HIVEDEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR + dbms.surroundWithSpaces(dbms.getRegularExpressionFunction()) + REGEXP),
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 5 : database type is Vertical
     */
    @Test
    public void testRegexLikeCase5() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.VERTICA);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(dbms.getRegularExpressionFunction()
                            + "(TO_CHAR(" + REGEXP_STR + ") , " + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     * 
     * case 6 : database type is sql
     */
    @Test
    public void testRegexLikeCase6() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage("SQL", "0.1"); //$NON-NLS-1$ //$NON-NLS-2$
            // regularExpressionFunction is null
            Assert.assertEquals(null, dbms.regexLike(REGEXP_STR, REGEXP));
            dbms.setRegularExpressionFunction(StringUtils.EMPTY);
            Assert.assertEquals(null, dbms.regexLike(REGEXP_STR, REGEXP));
            // regularExpressionFunction is not null but element is null
            dbms.setRegularExpressionFunction(REGEXP_STR);
            Assert.assertEquals(null, dbms.regexLike(null, REGEXP));
            Assert.assertEquals(null, dbms.regexLike(StringUtils.EMPTY, REGEXP));
            // regularExpressionFunction and REGEXP_STR are not null but regex is null
            Assert.assertEquals(null, dbms.regexLike(REGEXP_STR, null));
            Assert.assertEquals(null, dbms.regexLike(REGEXP_STR, StringUtils.EMPTY));
            // normal case
            Assert.assertEquals(
                    dbms.surroundWithSpaces(dbms.getRegularExpressionFunction() + "( " + REGEXP_STR + "," + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 1 :database type is mysql
     */
    @Test
    public void testRegexNotLikeCase1() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR
                            + dbms.surroundWithSpaces(dbms.not() + dbms.getRegularExpressionFunction()) + REGEXP),
                    dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 2 : database type is oracle
     */
    @Test
    public void testRegexNotLikeCase2() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.ORACLEWITHSIDDEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(dbms.not() + dbms.getRegularExpressionFunction()
                            + "(" + REGEXP_STR + " , " + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 3 : database type is postgresql
     */
    @Test
    public void testRegexNotLikeCase3() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.POSTGRESQLEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR + dbms.surroundWithSpaces("!" + dbms.getRegularExpressionFunction()) //$NON-NLS-1$
                            + REGEXP), dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 4 : database type is Hive
     */
    @Test
    public void testRegexNotLikeCase4() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.HIVEDEFAULTURL);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(REGEXP_STR
                            + dbms.surroundWithSpaces(dbms.not() + dbms.getRegularExpressionFunction()) + REGEXP),
                    dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 5 : database type is Vertical
     */
    @Test
    public void testRegexNotLikeCase5() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.VERTICA);

            Assert.assertEquals(
                    dbms.surroundWithSpaces(dbms.not() + dbms.getRegularExpressionFunction()
                            + "(TO_CHAR(" + REGEXP_STR + ") , " + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     * 
     * case 6 : database type is sql
     */
    @Test
    public void testRegexNotLikeCase6() {
        try {
            DbmsLanguage dbms = DbmsLanguageFactory.createDbmsLanguage("SQL", "0.1"); //$NON-NLS-1$ //$NON-NLS-2$
            // regularExpressionFunction is null
            Assert.assertEquals(null, dbms.regexNotLike(REGEXP_STR, REGEXP));
            dbms.setRegularExpressionFunction(StringUtils.EMPTY);
            Assert.assertEquals(null, dbms.regexNotLike(REGEXP_STR, REGEXP));
            // regularExpressionFunction is not null but element is null
            dbms.setRegularExpressionFunction(REGEXP_STR);
            Assert.assertEquals(null, dbms.regexNotLike(null, REGEXP));
            Assert.assertEquals(null, dbms.regexNotLike(StringUtils.EMPTY, REGEXP));
            // regularExpressionFunction and REGEXP_STR are not null but regex is null
            Assert.assertEquals(null, dbms.regexNotLike(REGEXP_STR, null));
            Assert.assertEquals(null, dbms.regexNotLike(REGEXP_STR, StringUtils.EMPTY));
            // normal case
            Assert.assertEquals(
                    dbms.not()
                            + dbms.surroundWithSpaces(dbms.getRegularExpressionFunction()
                                    + "( " + REGEXP_STR + "," + REGEXP + " )"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    dbms.regexNotLike(REGEXP_STR, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getHardCodedQuoteIdentifier()}.
     */
    @Test
    public void testGetHardCodedQuoteIdentifier() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getHardCodedQuoteIdentifier());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#supportAliasesInGroupBy()}.
     */
    @Test
    public void testSupportAliasesInGroupBy() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.supportAliasesInGroupBy());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#supportNonIntegerConstantInGroupBy()}.
     */
    @Test
    public void testSupportNonIntegerConstantInGroupBy() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.supportNonIntegerConstantInGroupBy());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getSelectRemarkOnTable(java.lang.String)}.
     */
    @Test
    public void testGetSelectRemarkOnTable() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getSelectRemarkOnTable(TABLE_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getSelectRemarkOnColumn(java.lang.String)}.
     */
    @Test
    public void testGetSelectRemarkOnColumn() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getSelectRemarkOnTable(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectOrderedAggregate(boolean, java.util.List, java.lang.String, java.lang.String, java.util.List, java.lang.String, java.util.List)}
     * .
     */
    @Test
    public void testGetSelectOrderedAggregate() {
        try {
            boolean distinct = true;

            List<String> columns = createColumns();

            String whereCause = "a=a"; //$NON-NLS-1$

            List<Integer> groupByIndexes = new ArrayList<Integer>();
            groupByIndexes.add(Integer.valueOf(0));

            String havingClause = EMPTY_STRING;

            List<Integer> orderByIndexes = new ArrayList<Integer>();
            orderByIndexes.add(Integer.valueOf(0));

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertFalse(StringUtils.isEmpty(dbms.getSelectOrderedAggregate(distinct, columns, TABLE_NAME, whereCause,
                    groupByIndexes, havingClause, orderByIndexes)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#orderBy(java.util.List, boolean)}.
     */
    @Test
    public void testOrderByListOfStringBoolean() {
        try {
            List<String> createColumns = createColumns();
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.orderBy(createColumns, true).indexOf(ORDER_ASC) > -1);
            Assert.assertTrue(dbms.orderBy(createColumns, false).indexOf(ORDER_DESC) > -1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericInvalidDetailedValues()}.
     */
    @Test
    public void testGetFDGenericInvalidDetailedValues() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericInvalidDetailedValues());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericValidDetailedValues()}.
     */
    @Test
    public void testGetFDGenericValidDetailedValues() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericValidDetailedValues());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericInvalidValues()}.
     */
    @Test
    public void testGetFDGenericInvalidValues() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericInvalidValues());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericValidValues()}.
     */
    @Test
    public void testGetFDGenericValidValues() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericValidValues());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericValidRows()}.
     */
    @Test
    public void testGetFDGenericValidRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericValidRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getFDGenericInvalidRows()}.
     */
    @Test
    public void testGetFDGenericInvalidRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getFDGenericInvalidRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getInstantiatedExpression(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    public void testGetInstantiatedExpression() {
        // TODO need to implement this method!!!
        // need an Indicator object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithColumnTableAndAlias(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithColumnTableAndAlias() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithColumnTableAndAlias(QUERY_GROUPBY_STR, COLUMN_NAME, TABLE_NAME,
                    COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithColumnsAndTable(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithColumnsAndTable() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithColumnsAndTable(QUERY_STR, COLUMN_NAME, TABLE_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithColumnsABAndTable(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithColumnsABAndTable() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithColumnsABAndTable(QUERY_AB_STR, COLUMN_A_NAME, COLUMN_B_NAME,
                    TABLE_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithColumnTablePattern(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithColumnTablePattern() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithColumnTablePattern(QUERY_PATTERN_STR, COLUMN_NAME, TABLE_NAME, REGEXP));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithColumnTableLimitOffset(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithColumnTableLimitOffset() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithColumnTableLimitOffset(QUERY_STR, COLUMN_NAME, TABLE_NAME, "10", "5", //$NON-NLS-1$ //$NON-NLS-2$
                    "20")); //$NON-NLS-1$
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithJoin(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithJoinStringStringStringStringString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithJoin(QUERY_JOIN_WHERE_STR, TABLE_NAME, TABLE_NAME2, JOIN_CLAUSE,
                    WHERE_CLAUSE));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#fillGenericQueryWithJoin(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testFillGenericQueryWithJoinStringStringString() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.fillGenericQueryWithJoin(QUERY_JOIN_WHERE_STR, TABLE_NAME, JOIN_CLAUSE));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#charLength(java.lang.String)}.
     */
    @Test
    public void testCharLength() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.charLength(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#supportRegexp()}.
     */
    @Test
    public void testSupportRegexp() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(dbms.supportRegexp());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#createJoinConditionAsString(orgomg.cwm.objectmodel.core.ModelElement, java.util.List, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCreateJoinConditionAsString() {
        // TODO need to implement this method!!!
        // need a TdTable(ModelElement) object to test this method
        // fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#innerJoin(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testInnerJoin() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.innerJoin(TABLE_NAME, TABLE_NAME, COLUMN_A_NAME, true, TABLE_NAME2, TABLE_NAME2,
                    COLUMN_B_NAME, true));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#join(java.lang.StringBuilder, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)}
     * .
     */
    @Test
    public void testJoinStringBuilderStringStringStringBooleanStringStringStringBooleanString() {
        try {
            StringBuilder sb = new StringBuilder();
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.join(sb, TABLE_NAME, TABLE_NAME, COLUMN_A_NAME, true, TABLE_NAME2, TABLE_NAME2,
                    COLUMN_B_NAME, true, OPERATOR));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#join()}.
     */
    @Test
    public void testJoin() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.join());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getColumnName(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testGetColumnName() {
        try {
            TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
            tdColumn.setName(COLUMN_NAME);

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(COLUMN_NAME.equals(dbms.getColumnName(tdColumn)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getTableName(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testGetTableName() {
        try {
            TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
            tdTable.setName(TABLE_NAME);

            TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
            tdColumn.setName(COLUMN_NAME);

            tdTable.getOwnedElement().add(tdColumn);
            tdColumn.setOwner(tdTable);

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(TABLE_NAME.equals(dbms.getTableName(tdColumn)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getTable(orgomg.cwm.objectmodel.core.ModelElement)}.
     */
    @Test
    public void testGetTable() {
        try {
            TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
            tdTable.setName(TABLE_NAME);

            TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
            tdColumn.setName(COLUMN_NAME);

            tdTable.getOwnedElement().add(tdColumn);
            tdColumn.setOwner(tdTable);

            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue(tdTable.equals(dbms.getTable(tdColumn)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#createGenericSqlWithRegexFunction(java.lang.String)}.
     */
    @Test
    public void testCreateGenericSqlWithRegexFunction() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.createGenericSqlWithRegexFunction(FUNCTION));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getAverageLengthRows()}.
     */
    @Test
    public void testGetAverageLengthRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getAverageLengthRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#isSql()}.
     */
    @Test
    public void testIsSql() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertFalse(dbms.isSql());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()}.
     */
    @Test
    public void testGetRegularExpressionFunction() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertTrue("REGEXP BINARY".equals(dbms.getRegularExpressionFunction())); //$NON-NLS-1$
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#trimIfBlank(java.lang.String)}.
     */
    @Test
    public void testTrimIfBlank() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.trimIfBlank(COLUMN_NAME));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()}.
     */
    @Test
    public void testGetAverageLengthWithBlankRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getAverageLengthWithBlankRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()}.
     */
    @Test
    public void testGetAverageLengthWithNullBlankRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getAverageLengthWithNullBlankRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()}.
     */
    @Test
    public void testGetAverageLengthWithNullRows() {
        try {
            DbmsLanguage dbms = getMysqlDbmsLanguage();
            Assert.assertNotNull(dbms.getAverageLengthWithNullRows());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetQueryColumnsWithPrefix() {
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        TdColumn col1 = RelationalPackage.eINSTANCE.getRelationalFactory().createTdColumn();
        col1.setName("myCol1"); //$NON-NLS-1$
        TdColumn col2 = RelationalPackage.eINSTANCE.getRelationalFactory().createTdColumn();
        col2.setName("myCol2"); //$NON-NLS-1$
        TdColumn[] columns = new TdColumn[] { col1, col2 };
        TdTable tdTable = createTdTable();
        ColumnSetHelper.addColumn(col1, tdTable);
        ColumnSetHelper.addColumn(col2, tdTable);

        String queryColWithPrefix = dbms.getQueryColumnsWithPrefix(columns);
        Assert.assertEquals("`myCatalog`.`myTable`.`myCol1`,`myCatalog`.`myTable`.`myCol2`", queryColWithPrefix); //$NON-NLS-1$

        // Assert hive
        dbms = getHiveDbmsLanguage();
        queryColWithPrefix = dbms.getQueryColumnsWithPrefix(columns);
        Assert.assertEquals("`myCol1`,`myCol2`", queryColWithPrefix); //$NON-NLS-1$

    }

    @Test
    public void testGetQueryColumnSetWithPrefix() {
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        TdTable tdTable = createTdTable();
        String queryColWithPrefix = dbms.getQueryColumnSetWithPrefix(tdTable);
        Assert.assertEquals("`myCatalog`.`myTable`", queryColWithPrefix); //$NON-NLS-1$

    }

    private TdTable createTdTable() {
        Connection conn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        Catalog catalog = CatalogHelper.createCatalog("myCatalog"); //$NON-NLS-1$
        ConnectionHelper.addCatalog(catalog, conn);
        List<TdTable> tables = new ArrayList<TdTable>();
        TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
        tdTable.setName("myTable"); //$NON-NLS-1$
        tables.add(tdTable);
        CatalogHelper.addTables(tables, catalog);
        return tdTable;
    }

    @Test
    public void testGetInvalidClauseBenFord() {
        // MYSQL
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        String benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or " + COLUMN_A_NAME + " not REGEXP '^[0-9]'"); //$NON-NLS-1$//$NON-NLS-2$

        // SYBASE
        dbms = getSybaseDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or left(convert(char(15)," + COLUMN_A_NAME + "),1) not " //$NON-NLS-1$ //$NON-NLS-2$
                + dbms.like() + "'%[0-9]%'"); //$NON-NLS-1$

        // POSTGRESQL
        dbms = getPostgresqlDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or SUBSTRING(" + COLUMN_A_NAME + ", 1,1)  ~ '[^0-9]'"); //$NON-NLS-1$//$NON-NLS-2$

        // INFORMIX
        dbms = getInformixDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or SUBSTR(" + COLUMN_A_NAME //$NON-NLS-1$
                + ",0,1) not in ('0','1','2','3','4','5','6','7','8','9')"); //$NON-NLS-1$

        // DB2
        dbms = getDB2DbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or LEFT(" + COLUMN_A_NAME + ",1)" //$NON-NLS-1$//$NON-NLS-2$
                + " not in ('0','1','2','3','4','5','6','7','8','9')"); //$NON-NLS-1$

        // MSSQL
        dbms = getMsSqlDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or LEFT(" + COLUMN_A_NAME + ",1) not" + dbms.like() //$NON-NLS-1$//$NON-NLS-2$
                + "'%[0-9]%'"); //$NON-NLS-1$

        // ORACLE
        dbms = getOracleDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or " + "  regexp_like(SUBSTR(" + COLUMN_A_NAME //$NON-NLS-1$//$NON-NLS-2$
                + ",0,1),'^[^[:digit:]]+$')"); //$NON-NLS-1$

        // TERDATA
        dbms = getTeradataDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or cast(" + COLUMN_A_NAME //$NON-NLS-1$
                + " as char(1)) not in ('0','1','2','3','4','5','6','7','8','9')"); //$NON-NLS-1$

        // VERTICA
        dbms = getVerticaDbmsLanguage();
        benfordClause = dbms.getInvalidClauseBenFord(COLUMN_A_NAME);
        Assert.assertEquals(benfordClause, COLUMN_A_NAME + " is null or " + "not regexp_like(to_char(" + COLUMN_A_NAME //$NON-NLS-1$ //$NON-NLS-2$
                + "),'^[[:digit:]]')"); //$NON-NLS-1$
    }

    /**
     * DOC qiongli Comment method "getVerticaDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getVerticaDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.VERTICA, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getTeradataDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getTeradataDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.TERADATA, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getOracleDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getOracleDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.ORACLE, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getMsSqlDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getMsSqlDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getDB2DbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getDB2DbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.DB2, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getInformixDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getInformixDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.INFOMIX, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getPostgresqlDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getPostgresqlDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.POSTGRESQL, DATABASE_VERSION);
    }

    /**
     * DOC qiongli Comment method "getSybaseDbmsLanguage".
     * 
     * @return
     */
    private DbmsLanguage getSybaseDbmsLanguage() {
        return DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.SYBASE, DATABASE_VERSION);
    }

    @Test
    public void testGetColumnNameForClause() {
        // MYSQL
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        String columnName = dbms.castColumnNameToChar(COLUMN_A_NAME);
        Assert.assertEquals(columnName, COLUMN_A_NAME);

        // TERADATA
        dbms = getTeradataDbmsLanguage();
        columnName = dbms.castColumnNameToChar(COLUMN_A_NAME);
        Assert.assertEquals(columnName, "cast(" + COLUMN_A_NAME + " as char)"); //$NON-NLS-1$ //$NON-NLS-2$

        // POSTGRESQL

        dbms = getPostgresqlDbmsLanguage();
        columnName = dbms.castColumnNameToChar(COLUMN_A_NAME);
        Assert.assertEquals(columnName, "cast(" + COLUMN_A_NAME + " as char)"); //$NON-NLS-1$ //$NON-NLS-2$

        // SYBASE
        dbms = getSybaseDbmsLanguage();
        columnName = dbms.castColumnNameToChar(COLUMN_A_NAME);
        Assert.assertEquals(columnName, "convert(char(15)," + COLUMN_A_NAME + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        // VERTICA
        dbms = getVerticaDbmsLanguage();
        columnName = dbms.castColumnNameToChar(COLUMN_A_NAME);
        Assert.assertEquals(columnName, "to_char(" + COLUMN_A_NAME + ")"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 1:vertica database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForVertica() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.VERTICA);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // Vertica Database
        createExpression.setBody("**"); //$NON-NLS-1$
        String regularFunctionName = netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "***"); //$NON-NLS-1$
        Assert.assertEquals("REGEXP_LIKE", regularFunctionName); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 2:Hive database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForHive() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.HIVEDEFAULTURL);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // Hive Database
        createExpression.setBody("**"); //$NON-NLS-1$
        String regularFunctionName = netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "***"); //$NON-NLS-1$
        Assert.assertEquals("REGEXP", regularFunctionName); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 3:PostgreSQL database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForPostgreSQL() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.POSTGRESQLEFAULTURL);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // Postgresql Database
        createExpression.setBody("**"); //$NON-NLS-1$
        String regularFunctionName = netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "***"); //$NON-NLS-1$
        Assert.assertEquals("~", regularFunctionName); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 4:Oracle database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForOracle() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.ORACLEWITHSIDDEFAULTURL);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // oracle Database
        createExpression.setBody("**"); //$NON-NLS-1$
        String regularFunctionName = netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "***"); //$NON-NLS-1$
        Assert.assertEquals("REGEXP_LIKE", regularFunctionName); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 8:Mssql database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForMssql() {
        DbmsLanguage mssqlDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.MSSQL2008URL);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // netezza Database UDP mode
        createExpression
                .setBody("SELECT COUNT(CASE WHEN dbo.fn_prec_match(<%=__COLUMN_NAMES__%>,<%=__PATTERN_EXPR__%>)=1 THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
        String regularFunctionName = mssqlDbmsLanguage
                .extractRegularExpressionFunction(createExpression, "<%=__PATTERN_EXPR__%>"); //$NON-NLS-1$
        Assert.assertEquals("dbo.fn_prec_match".toUpperCase(), regularFunctionName); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 6:Netezza database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForNetezza() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // netezza Database UDP mode
        createExpression
                .setBody("SELECT COUNT(CASE WHEN REGEXP_EXTRACT(<%=__COLUMN_NAMES__%>,<%=__PATTERN_EXPR__%>) THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
        try {
            netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "<%=__PATTERN_EXPR__%>"); //$NON-NLS-1$
        } catch (UnsupportedOperationException e) {
            Assert.fail("Default DbmsLanguage don't should call extractRegularExpressionFunction method should thorw UnsupportedOperationException."); //$NON-NLS-1$
        }
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(Expression)}.
     * 
     * case 7:Other database expression
     */
    @Test
    public void testExtractRegularExpressionFunctionForOthers() {
        DbmsLanguage netezzaDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage("SQL", "0.1"); //$NON-NLS-1$ //$NON-NLS-2$
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default case
        createExpression.setBody("**** WHEN RegularFunctionName(***)returnValue THEN **)****"); //$NON-NLS-1$
        try {
            netezzaDbmsLanguage.extractRegularExpressionFunction(createExpression, "***"); //$NON-NLS-1$
        } catch (UnsupportedOperationException e) {
            Assert.fail("Default DbmsLanguage don't should call extractRegularExpressionFunction method should thorw UnsupportedOperationException."); //$NON-NLS-1$
        }
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#getFunctionReturnValue()}.
     * 
     * case 1:RegularfunctionReturnValue is null
     */
    @Test
    public void testGetFunctionReturnValue() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage("SQL", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals(StringUtils.EMPTY, regularfunctionReturnValue);
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 1:The body of expression is null
     */
    @Test
    public void testSetFunctionReturnValueCase1() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, "1.0"); //$NON-NLS-1$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals(StringUtils.EMPTY, regularfunctionReturnValue);
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 2:Can not find the keyword for "when" in the body of expression.
     */
    @Test
    public void testSetFunctionReturnValueCase2() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, "1.0"); //$NON-NLS-1$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default Database
        createExpression.setBody("**** RegularFunctionName(***)returnValue THEN **)****"); //$NON-NLS-1$
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals(StringUtils.EMPTY, regularfunctionReturnValue);
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 3:Can not find the keyword for ")" in the body of expression.
     */
    @Test
    public void testSetFunctionReturnValueCase3() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, "1.0"); //$NON-NLS-1$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default Database
        createExpression.setBody("**** WHEN RegularFunctionName(***returnValue THEN ******"); //$NON-NLS-1$
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals(StringUtils.EMPTY, regularfunctionReturnValue);
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 4:Can not find the keyword for "THEN" in the body of expression.
     */
    @Test
    public void testSetFunctionReturnValueCase4() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, "1.0"); //$NON-NLS-1$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default Database
        createExpression.setBody("**** WHEN RegularFunctionName(***)returnValue **)****"); //$NON-NLS-1$
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals(StringUtils.EMPTY, regularfunctionReturnValue);
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 5:normal case
     */
    @Test
    public void testSetFunctionReturnValueCase5() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(DbmsLanguage.MSSQL, "1.0"); //$NON-NLS-1$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default Database
        createExpression.setBody("**** WHEN RegularFunctionName(***)returnValue THEN **)****"); //$NON-NLS-1$
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals("RETURNVALUE", regularfunctionReturnValue); //$NON-NLS-1$
    }

    /**
     * 
     * {@link org.talend.dq.dbms.DbmsLanguage#setFunctionReturnValue(Expression)}.
     * 
     * case 6: dbmsLanguage is sql
     */
    @Test
    public void testSetFunctionReturnValueCase6() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage("sql", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$ 
        Expression createExpression = CoreFactory.eINSTANCE.createExpression();
        // default Database
        createExpression.setBody("**** WHEN RegularFunctionName(***)returnValue THEN **)****"); //$NON-NLS-1$
        dbmsLanguage.setFunctionReturnValue(dbmsLanguage.extractRegularExpressionFunctionReturnValue(createExpression, "***")); //$NON-NLS-1$
        String regularfunctionReturnValue = dbmsLanguage.getFunctionReturnValue();
        Assert.assertEquals("RETURNVALUE", regularfunctionReturnValue); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case1() {
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String db2Lang = "DB2"; //$NON-NLS-1$
        String db2Body = "DB2 body"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$
        String db2NTBody = "DB2/NT body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = db2NTLang;

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression2 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression2.setLanguage(db2Lang);
        tdExpression2.setBody(db2Body);
        sqlGenericExpression.add(tdExpression2);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(db2NTLang);
        tdExpression3.setBody(db2NTBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(tdExpression3, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case2() {
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String db2Lang = "DB2"; //$NON-NLS-1$
        String db2Body = "DB2 body"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$
        String db2NTBody = "DB2/NT body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = db2Lang;

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression2 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression2.setLanguage(db2Lang);
        tdExpression2.setBody(db2Body);
        sqlGenericExpression.add(tdExpression2);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(db2NTLang);
        tdExpression3.setBody(db2NTBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(tdExpression2, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case3() {
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String db2Lang = "DB2"; //$NON-NLS-1$
        String db2Body = "DB2 body"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = db2NTLang;

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression2 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression2.setLanguage(db2Lang);
        tdExpression2.setBody(db2Body);
        sqlGenericExpression.add(tdExpression2);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(tdExpression2, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case4() {
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String db2Lang = "DB2"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$
        String db2NTBody = "DB2/NT body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = db2Lang;

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(db2NTLang);
        tdExpression3.setBody(db2NTBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(tdExpression3, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case5() {
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String db2Lang = "DB2"; //$NON-NLS-1$
        String db2Body = "DB2 body"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$
        String db2NTBody = "DB2/NT body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = "unknown"; //$NON-NLS-1$

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression2 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression2.setLanguage(db2Lang);
        tdExpression2.setBody(db2Body);
        sqlGenericExpression.add(tdExpression2);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(db2NTLang);
        tdExpression3.setBody(db2NTBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(tdExpression1, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionDB2Case6() {
        String db2Lang = "DB2"; //$NON-NLS-1$
        String db2Body = "DB2 body"; //$NON-NLS-1$

        String db2NTLang = "DB2/NT"; //$NON-NLS-1$
        String db2NTBody = "DB2/NT body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        String language = "unknown"; //$NON-NLS-1$

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression2 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression2.setLanguage(db2Lang);
        tdExpression2.setBody(db2Body);
        sqlGenericExpression.add(tdExpression2);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(db2NTLang);
        tdExpression3.setBody(db2NTBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage
                .getSqlExpression(indicatorDefinition, language, sqlGenericExpression, dbVersion);
        Assert.assertEquals(null, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionRedshiftCase1() {
        // TDQ-11558 msjian: test for get sql expresstion for redshift database
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String redshiftLang = SupportDBUrlType.REDSHIFT.getLanguage();
        String redshiftBody = "Amazon redshift body"; //$NON-NLS-1$

        String postgresqlLang = SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage();
        String postgresqlBody = "PostgreSQL body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(postgresqlLang);
        tdExpression3.setBody(postgresqlBody);
        sqlGenericExpression.add(tdExpression3);

        TdExpression tdExpression4 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression4.setLanguage(redshiftLang);
        tdExpression4.setBody(redshiftBody);
        sqlGenericExpression.add(tdExpression4);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage.getSqlExpression(indicatorDefinition, redshiftLang, sqlGenericExpression,
                dbVersion);
        Assert.assertEquals(tdExpression4, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionRedshiftCase2() {
        // TDQ-11558 msjian: test for get sql expresstion for redshift database when there is no redshift defined
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String redshiftLang = SupportDBUrlType.REDSHIFT.getLanguage();

        String postgresqlLang = SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage();
        String postgresqlBody = "PostgreSQL body"; //$NON-NLS-1$

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        TdExpression tdExpression3 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression3.setLanguage(postgresqlLang);
        tdExpression3.setBody(postgresqlBody);
        sqlGenericExpression.add(tdExpression3);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage.getSqlExpression(indicatorDefinition, redshiftLang, sqlGenericExpression,
                dbVersion);
        Assert.assertEquals(tdExpression3, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSqlExpression(IndicatorDefinition, String, EList, ProductVersion)} .
     */
    @Test
    public void testGetSqlExpressionRedshiftCase3() {
        // TDQ-11558 msjian: test for get sql expresstion for redshift database when there is no redshift and ParAccel
        // and PostgreSQL defined
        String sqlLang = "SQL"; //$NON-NLS-1$
        String sqlBody = "SQL body"; //$NON-NLS-1$

        String redshiftLang = SupportDBUrlType.REDSHIFT.getLanguage();

        IndicatorDefinition indicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        EList<TdExpression> sqlGenericExpression = new BasicEList<TdExpression>();

        TdExpression tdExpression1 = RelationalFactory.eINSTANCE.createTdExpression();
        tdExpression1.setLanguage(sqlLang);
        tdExpression1.setBody(sqlBody);
        sqlGenericExpression.add(tdExpression1);

        ProductVersion dbVersion = new ProductVersion(1, 0);

        TdExpression sqlExpression = DbmsLanguage.getSqlExpression(indicatorDefinition, redshiftLang, sqlGenericExpression,
                dbVersion);
        Assert.assertEquals(tdExpression1, sqlExpression);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectColumnsStr(org.talend.core.model.metadata.builder.connection.MetadataTable)
     * case1 mysql 4 columns
     */
    @Test
    public void testGetSelectColumnsStrCase1() {
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        // create metadata table
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        // ~create metadata table

        // create metadata column
        String[] columnNames = new String[] { "id", "name", "number", "date" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        createMetadataColumns(createMetadataTable, columnNames);
        // ~create metadata column
        String selectColumnsStr = dbms.getSelectColumnsStr(createMetadataTable);

        Assert.assertEquals("`id`,`name`,`number`,`date`", selectColumnsStr); //$NON-NLS-1$

    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectColumnsStr(org.talend.core.model.metadata.builder.connection.MetadataTable)
     * case2 mysql 0 columns
     */
    @Test
    public void testGetSelectColumnsStrCase2() {
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        // create metadata table
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        // ~create metadata table

        // create metadata column
        String[] columnNames = new String[] {};
        createMetadataColumns(createMetadataTable, columnNames);
        // ~create metadata column
        String selectColumnsStr = dbms.getSelectColumnsStr(createMetadataTable);

        Assert.assertEquals("*", selectColumnsStr); //$NON-NLS-1$

    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#getSelectColumnsStr(org.talend.core.model.metadata.builder.connection.MetadataTable)
     * case3 mysql 1 column
     */
    @Test
    public void testGetSelectColumnsStrCase3() {
        DbmsLanguage dbms = getMysqlDbmsLanguage();
        // create metadata table
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        // ~create metadata table

        // create metadata column
        String[] columnNames = new String[] { "id" }; //$NON-NLS-1$
        createMetadataColumns(createMetadataTable, columnNames);
        // ~create metadata column
        String selectColumnsStr = dbms.getSelectColumnsStr(createMetadataTable);

        Assert.assertEquals("`id`", selectColumnsStr); //$NON-NLS-1$

    }

    private void createMetadataColumns(MetadataTable parentTable, String[] columnNames) {
        for (String name : columnNames) {
            MetadataColumn createMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
            createMetadataColumn.setName(name);
            parentTable.getColumns().add(createMetadataColumn);
        }
    }

}
