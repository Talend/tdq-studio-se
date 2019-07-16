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
package org.talend.dq.dbms;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.core.IRepositoryContextService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternPackage;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.sql.SqlPredicate;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.rules.JoinElement;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.utils.ProductVersion;
import org.talend.utils.sql.Java2SqlType;

import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 *
 * This class handle DBMS specific SQL terms and functions. It provides methods to handle SQL clause and evrything
 * related to a specific DBMS.
 *
 * this class provides a default implementation. Subclasses exist for each DBMS so that specific implementation can be
 * written.
 */
public class DbmsLanguage {

    public static Logger log = Logger.getLogger(DbmsLanguage.class);

    // TODO scorreia put this into its own class and offer simple methods to replace tokens.

    // --- add here other supported systems (always in uppercase) // DBMS_SUPPORT

    static final String ORACLE = SupportDBUrlType.ORACLEWITHSIDDEFAULTURL.getLanguage();

    static final String MYSQL = SupportDBUrlType.MYSQLDEFAULTURL.getLanguage();

    static final String POSTGRESQL = SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage();

    static final String MSSQL = SupportDBUrlType.MSSQLDEFAULTURL.getLanguage();

    static final String DB2 = SupportDBUrlType.DB2DEFAULTURL.getLanguage();

    static final String AS400 = SupportDBUrlType.AS400DEFAULTURL.getLanguage();

    static final String DB2ZOS = SupportDBUrlType.DB2ZOSDEFAULTURL.getLanguage();

    static final String SYBASE = SupportDBUrlType.SYBASEDEFAULTURL.getLanguage();

    static final String SQLITE3 = SupportDBUrlType.SQLITE3DEFAULTURL.getLanguage();

    static final String TERADATA = SupportDBUrlType.TERADATADEFAULTURL.getLanguage();

    static final String INGRES = SupportDBUrlType.INGRESDEFAULTURL.getLanguage();

    static final String JAVA = SupportDBUrlType.JAVADEFAULTURL.getLanguage();

    static final String INFOMIX = SupportDBUrlType.INFORMIXDEFAULTURL.getLanguage();

    static final String NETEZZA = SupportDBUrlType.NETEZZADEFAULTURL.getLanguage();

    static final String DELIMITEDFILE = SupportDBUrlType.DELIMITEDFILE.getLanguage();

    static final String HIVE = SupportDBUrlType.HIVEDEFAULTURL.getLanguage();

    static final String VERTICA = EDatabaseTypeName.VERTICA.getXmlName();

    static final String IMPALA = SupportDBUrlType.IMPALA.getLanguage();

    static final String REDSHIFT = SupportDBUrlType.REDSHIFT.getLanguage();

    static final String EXASOLUTION = SupportDBUrlType.EXASOL.getLanguage();

    public static final String BIGQUERY = "Google BigQuery"; //$NON-NLS-1$

    public static final String SNOWFLAKE = "Snowflake"; //$NON-NLS-1$

    /**
     * Ansi SQL.
     */
    public static final String SQL = "SQL"; //$NON-NLS-1$

    public static final String DOT = "."; //$NON-NLS-1$

    private static final String ASTERISK = "*"; //$NON-NLS-1$

    private static final String COUNT_ASTERISK = "COUNT(*)"; //$NON-NLS-1$

    /**
     * End of Statement: ";".
     */
    protected static final String EOS = ";"; //$NON-NLS-1$

    protected static final String JOIN_LEFT = "LEFT"; //$NON-NLS-1$

    protected static final String JOIN_RIGHT = "RIGHT"; //$NON-NLS-1$

    protected static final String JOIN_FULL = "FULL"; //$NON-NLS-1$

    static final java.util.regex.Pattern SELECT_PATTERN = java.util.regex.Pattern.compile(
            "SELECT", java.util.regex.Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

    /**
     * in upper case.
     */
    private final String dbmsName;

    /**
     * version for current database.
     */
    private final ProductVersion dbVersion;

    /**
     * the quoting string or an empty string if quoting is not supported.
     */
    private String dbQuoteString = ""; //$NON-NLS-1$

    private String regularExpressionFunction = ""; //$NON-NLS-1$

    // default soundex string
    private final String SOUNDEX_PREFIX = "SOUNDEX";//$NON-NLS-1$

    private String regularfunctionReturnValue = ""; //$NON-NLS-1$

    /**
     * DbmsLanguage constructor for generic ANSI SQL (independent of any DBMS).
     */
    DbmsLanguage() {
        this(SQL);
    }

    /**
     * DbmsLanguage constructor.
     *
     * @param dbmsType the name of the DBMS (MySQL, Oracle,...)
     */
    DbmsLanguage(String dbmsType) {
        assert dbmsType != null : "DBMS type must not be null!!"; //$NON-NLS-1$
        this.dbmsName = dbmsType;
        this.dbVersion = null;
    }

    /**
     * DbmsLanguage constructor. Use this constructor when functions are specific to a given release of the DBMS.
     *
     * @param dbmsType the database name
     * @param dbVersion the database version number
     */
    DbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        this.dbmsName = dbmsType;
        this.dbVersion = dbVersion;
    }

    /**
     * Method "quote".
     *
     * @param sqlIdentifier the SQL identifier to quote
     * @return the sqlIdentifier quoted.
     */
    public String quote(String sqlIdentifier) {
        if (sqlIdentifier == null || sqlIdentifier.equals(PluginConstant.EMPTY_STRING)) {
            return PluginConstant.EMPTY_STRING;
        }
        String quotedSqlIdentifier = sqlIdentifier;
        if (!quotedSqlIdentifier.startsWith(dbQuoteString)) {
            quotedSqlIdentifier = dbQuoteString + quotedSqlIdentifier;
        }
        if (!quotedSqlIdentifier.endsWith(dbQuoteString)) {
            quotedSqlIdentifier = quotedSqlIdentifier + dbQuoteString;
        }
        return quotedSqlIdentifier;
    }

    public String and() {
        return surroundWithSpaces(SqlPredicate.AND.getLiteral());
    }

    public String or() {
        return surroundWithSpaces(SqlPredicate.OR.getLiteral());
    }

    public String greater() {
        return surroundWithSpaces(SqlPredicate.GREATER.getLiteral());
    }

    public String greaterOrEqual() {
        return surroundWithSpaces(SqlPredicate.GREATER_EQUAL.getLiteral());
    }

    public String less() {
        return surroundWithSpaces(SqlPredicate.LESS.getLiteral());
    }

    public String lessOrEqual() {
        return surroundWithSpaces(SqlPredicate.LESS_EQUAL.getLiteral());
    }

    public String between() {
        return surroundWithSpaces(SqlPredicate.BETWEEN.getLiteral());
    }

    /**
     * Method "isNull".
     *
     * @return IS NULL surrounded with spaces.
     */
    public String isNull() {
        return surroundWithSpaces(SqlPredicate.IS_NULL.getLiteral());
    }

    /**
     * Method "isNotNull".
     *
     * @return " IS NOT NULL " surrounded with spaces.
     */
    public String isNotNull() {
        return surroundWithSpaces(SqlPredicate.IS_NOT_NULL.getLiteral());
    }

    /**
     * Method "equal".
     *
     * @return the = sign
     */
    public String equal() {
        return surroundWithSpaces(SqlPredicate.EQUAL.getLiteral());
    }

    /**
     * Method "notEqual".
     *
     * @return "<>" by default or "!=" on some specific DBMS
     */
    public String notEqual() {
        // --- add the DBMS that do not allow <> operator

        // ANSI SQL, MySQL,
        return surroundWithSpaces(SqlPredicate.NOT_EQUAL.getLiteral());

    }

    public String unionAll() {
        return union() + all();
    }

    public String all() {
        return surroundWithSpaces(SqlPredicate.ALL.getLiteral());
    }

    public String union() {
        return surroundWithSpaces(SqlPredicate.UNION.getLiteral());
    }

    public String like() {
        return surroundWithSpaces(SqlPredicate.LIKE.getLiteral());
    }

    public String from() {
        return surroundWithSpaces("FROM"); //$NON-NLS-1$
    }

    /**
     * Method "eos".
     *
     * @return the end of SQL statement token (";")
     */
    public String eos() {
        return EOS;
    }

    public String getDbmsName() {
        return this.dbmsName;
    }

    public ProductVersion getDbVersion() {
        return dbVersion;
    }

    /**
     * Method "getDefaultLanguage".
     *
     * @return the default String to use when no dbms is defined.
     */
    public static String getDefaultLanguage() {
        return SQL;
    }

    public String toQualifiedName(String catalog, String schema, String table) {
        StringBuffer qualName = new StringBuffer();
        if (catalog != null && catalog.trim().length() > 0) {
            qualName.append(this.handleContextModeOrAddQuotes(catalog));
            qualName.append(getCatalogDelimiter());
        }
        if (schema != null && schema.trim().length() > 0) {
            qualName.append(this.handleContextModeOrAddQuotes(schema));
            qualName.append(getDelimiter());
        }

        qualName.append(this.handleContextModeOrAddQuotes(table));
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s.%s.%s -> %s", catalog, schema, table, qualName)); //$NON-NLS-1$
        }
        return qualName.toString();
    }

    /**
     * Getter for delimiter between catalog and schema
     *
     * This may be different from the default delimiter for some databases, ex: Informix.
     *
     * @return catalog delimiter
     */
    protected String getCatalogDelimiter() {
        return getDelimiter();
    }

    /**
     * wrap context mode parameters by tags. otherwise, add quotes database identifier quotes.
     *
     * @param param
     * @return
     */
    protected String handleContextModeOrAddQuotes(String param) {
        if (param.startsWith("context.")) { //$NON-NLS-1$
            return "<%=" + param + "%>"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return this.quote(param);
    }

    /**
     * Getter for default SQL delimiter of the database.
     *
     * For the delimiter between catalog and schema, we should use {@link #getCatalogDelimiter()} instead of this.
     *
     * @return default SQL delimiter
     */
    public String getDelimiter() {
        return DOT;
    }

    /**
     * Method "getPatternFinderDefaultFunction".
     *
     * @param expression a column name or a string
     * @return a default SQL expression which can be used as Pattern Frequency Statistics or null
     * @deprecated use {@link #getPatternFinderFunction(String, String, String)} instead
     */
    @Deprecated
    public String getPatternFinderDefaultFunction(String expression) {
        // TDQ-15039 msjian: consider the JDBC type connection, its language is different from our defined
        // xxxDbmslanguage type. that means they will use this default Dbmslanguage,
        // so we must define a default method for them to support Pattern (Low) Frequency.
        return StringUtils.repeat("REPLACE(", 59) + expression //$NON-NLS-1$
                + ",'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A')" //$NON-NLS-1$
                + ",'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A')" //$NON-NLS-1$
                + ",'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A')" //$NON-NLS-1$
                + ",'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a')" //$NON-NLS-1$
                + ",'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a')" //$NON-NLS-1$
                + ",'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a')" //$NON-NLS-1$
                + ",'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a')" //$NON-NLS-1$
                + ",'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9')" + ",'7','9'),'8','9'),'0','9')"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Method "getPatternFinderFunction".
     *
     * @param expression a column name or a string
     * @param charsToReplace the list of characters to remove
     * @param replacementChars the replacement characters
     * @return a default SQL expression which can be used as Pattern Frequency Statistics or null
     */
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        return null;
    }

    /**
     * Method "getPatternFinderFunction".
     *
     * @param colName a column name or a string
     * @param charactersMapping the mapping of the character to replace
     * @return an SQL expression which can be used as Pattern Frequency Statistics or null
     */
    public String getPatternFinderFunction(String colName, EList<CharactersMapping> charactersMapping) {
        CharactersMapping charactersMap = adaptCharactersMapping(charactersMapping);

        if (charactersMap == null) {
            return null;
        }
        return this.getPatternFinderFunction(colName, charactersMap.getCharactersToReplace(),
                charactersMap.getReplacementCharacters());
    }

    /**
     *
     * Get CharactersMapping from charactersMappingList, if not found, use the default "SQL" CharactersMapping.
     *
     * @param charactersMappingList all of charactersMapping
     * @return if there is CharactersMapping return it else if there is default "SQL" CharactersMapping else return
     * null.
     */
    private CharactersMapping adaptCharactersMapping(EList<CharactersMapping> charactersMappingList) {
        CharactersMapping defaultCharactersMapping = null;
        for (CharactersMapping charactersMap : charactersMappingList) {
            if (this.is(charactersMap.getLanguage())) {
                if (validCharactersMapping(charactersMap)) {
                    return charactersMap;
                } else {
                    // current CharactersMapping is invalid so needn't care about default language case
                    continue;
                }

            } else if (defaultCharactersMapping == null && DbmsLanguageFactory.isAllDatabaseType(charactersMap.getLanguage())) {
                if (validCharactersMapping(charactersMap)) {
                    defaultCharactersMapping = charactersMap;
                }

                // else go to next character mapping
            }

        }
        return defaultCharactersMapping;
    }

    private boolean validCharactersMapping(CharactersMapping charactersMap) {
        final String charactersToReplace = charactersMap.getCharactersToReplace();
        final String replacementCharacters = charactersMap.getReplacementCharacters();
        if (!StringUtils.isEmpty(charactersToReplace) && !StringUtils.isEmpty(replacementCharacters)
                && charactersToReplace.length() == replacementCharacters.length()) {
            return true;
        }
        return false;
    }

    /**
     * Method "replaceOneChar".
     *
     * @param partialExpression
     * @param toReplace
     * @param replacement
     * @return the string REPLACE(partialExpression,'toReplace','replacement')
     */
    protected String replaceOneChar(String partialExpression, char toReplace, char replacement) {
        return "REPLACE(" + partialExpression + ",'" + toReplace + "','" + replacement + "')"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * Method "translateUsingPattern".
     *
     * @param expression
     * @param charsToReplace
     * @param replacementChars
     * @return the string "TRANSLATE(expression,charsToReplace,replacementChars)"
     */
    protected String translateUsingPattern(String expression, String charsToReplace, String replacementChars) {
        return "TRANSLATE(" + expression + " , '" + charsToReplace + "' , '" + replacementChars + "')"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public String replaceNullsWithString(String colName, String replacement) {
        return " CASE WHEN " + colName + isNull() + " THEN " + replacement + " ELSE " + colName + " END "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * Method "isNotBlank".
     *
     * @param colName a column name
     * @return the expression saying that the given column is not blank.
     */
    public String isNotBlank(String colName) {
        // default is OK for MySQL
        return trim(colName) + notEqual() + " '' "; //$NON-NLS-1$
    }

    public String trim(String colName) {
        // default is OK for MySQL
        return " TRIM(" + colName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String toUpperCase(String colName) {
        // default is OK for MySQL, Oracle
        return " UPPER(" + colName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String toLowerCase(String colName) {
        // default is OK for MySQL, Oracle
        return " LOWER(" + colName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String extractYear(String colName) {
        return extract(DateGrain.YEAR, colName);
    }

    public String extractQuarter(String colName) {
        return extract(DateGrain.QUARTER, colName);
    }

    public String extractMonth(String colName) {
        return extract(DateGrain.MONTH, colName);
    }

    public String extractWeek(String colName) {
        return extract(DateGrain.WEEK, colName);
    }

    public String extractDay(String colName) {
        return extract(DateGrain.DAY, colName);
    }

    /**
     * Method "getTopNQuery".
     *
     * @param query
     * @param n
     * @return the n first row of the given query
     */
    public String getTopNQuery(String query, int n) {
        return query + " LIMIT " + n; //$NON-NLS-1$
    }

    /**
     * Method "countRowInSubquery".
     *
     * @param subquery
     * @param alias the mandatory alias for the subquery
     * @return the select count(*) from aliased subquery
     */
    public String countRowInSubquery(String subquery, String alias) {
        // ANSI SQL, MySQL
        return " SELECT COUNT(*) FROM (" + subquery + ") AS " + alias; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String sumRowInSubquery(String colToSum, String subquery, String alias) {
        // ANSI SQL, MySQL
        return " SELECT SUM(" + colToSum + ") FROM (" + subquery + ") AS " + alias; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public String selectAllRowsWhereColumnIn(String column, String table, String subquery) {
        return " SELECT * FROM " + table + where() + column + in() + "( SELECT " + column + from() + "(" + subquery //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") AS mysubquery )"; //$NON-NLS-1$
    }

    public String selectColumnsFromTable(List<String> columns, String table) {
        return " SELECT " + StringUtils.join(columns.iterator(), ',') + from() + table; //$NON-NLS-1$
    }

    public String in() {
        return " IN "; //$NON-NLS-1$
    }

    public String notIn() {
        return " NOT IN "; //$NON-NLS-1$
    }

    public String not() {
        return " NOT "; //$NON-NLS-1$
    }

    /**
     * Method "getDbQuoteString".
     *
     * @return the quote identifier string set in this object.
     */
    public String getDbQuoteString() {
        return this.dbQuoteString;
    }

    public void setDbQuoteString(String dbQuoteString) {
        if (log.isDebugEnabled()) {
            log.debug("Database SQL quote: " + dbQuoteString); //$NON-NLS-1$
        }
        this.dbQuoteString = dbQuoteString;
    }

    protected String extract(DateGrain dateGrain, String colName) {
        if (ConnectionUtils.isSybaseeDBProducts(getDbmsName())) {
            return "DATEPART(" + dateGrain + "," + colName + ") ";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        // ANSI SQL, MySQL, Oracle
        return " EXTRACT(" + dateGrain + from() + colName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Method "is".
     *
     * @param dbName a DBMS name
     * @return true if this DBMS is given string
     */
    private boolean is(String dbName) {
        return DbmsLanguageFactory.compareDbmsLanguage(dbName, this.dbmsName);
    }

    protected String surroundWithSpaces(String toSurround) {
        return surroundWith(' ', toSurround, ' ');
    }

    protected String surroundWith(char left, String toSurround, char right) {
        return left + toSurround + right;
    }

    /**
     * Method "addWhereToSqlStringStatement". The list
     *
     * @param completedSqlString a generic SQL expression in which the where clause variable will be replaced.
     * @param whereExpressions the list of where expressions to concatenate (must not be null)
     * @return the SQL statement with the where clause
     */
    public String addWhereToSqlStringStatement(String completedSqlString, List<String> whereExpressions) {
        return addWhereToSqlStringStatement(completedSqlString, whereExpressions, true);
    }

    /**
     * add the where clause to the sql statement.
     *
     * @param completedSqlString a generic SQL expression in which the where clause variable will be replaced.
     * @param whereExpressions the list of where expressions to concatenate (must not be null)
     * @param valid if false add ! before where clause
     * @return the SQL statement with the where clause
     */
    public String addWhereToSqlStringStatement(String completedSqlString, List<String> whereExpressions, boolean valid) {
        String query = completedSqlString;
        String where = this.buildWhereExpression(whereExpressions);
        if (where != null) {
            if (!valid) {
                where = '!' + this.surroundWith('(', where, ')');
            }
            query = this.addWhereToStatement(query, where);
        }
        return query;
    }

    /**
     * Method "addWhereToStatement".
     *
     * @param statement a statement already prepared for parsing
     * @param whereClause (must not be null, but can be empty when there is no where clause)
     * @return the new statement
     */
    public String addWhereToStatement(String statement, String whereClause) {
        String tempStatement = statement;
        String tempWhereClause = whereClause;
        final GenericSQLHandler genericSQLHandler = new GenericSQLHandler(tempStatement);
        if (genericSQLHandler.containsWhereClause()) {
            tempWhereClause = tempWhereClause.length() != 0 ? where() + tempWhereClause : tempWhereClause;
            tempStatement = genericSQLHandler.replaceWhere(tempWhereClause).getSqlString();
        }
        if (genericSQLHandler.containsAndClause()) {
            tempWhereClause = tempWhereClause.length() != 0 ? and() + tempWhereClause : tempWhereClause;
            tempStatement = genericSQLHandler.replaceAndClause(tempWhereClause).getSqlString();
        }
        if (genericSQLHandler.containsUDIWhere()) {
            tempStatement = genericSQLHandler.replaceUDIWhere(tempWhereClause).getSqlString();
        }
        return tempStatement;
    }

    public String where() {
        return " WHERE "; //$NON-NLS-1$
    }

    /**
     * Method "buildWhereExpression".
     *
     * @param whereExpression a list of boolean clauses
     * @return clauses between parentheses and concatenated with "AND"
     */
    public String buildWhereExpression(List<String> whereExpression) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < whereExpression.size(); i++) {
            String exp = whereExpression.get(i);
            buf.append(this.surroundWith('(', exp, ')'));
            if (i != whereExpression.size() - 1) {
                buf.append(and());
            }
        }
        return buf.toString();
    }

    public String desc() {
        return " DESC "; //$NON-NLS-1$
    }

    public String orderBy() {
        return " ORDER BY "; //$NON-NLS-1$
    }

    public String groupBy() {
        return " GROUP BY "; //$NON-NLS-1$
    }

    /**
     * Method "getSqlExpression".
     *
     * @param indicatorDefinition contains a list of possible expression (one for each supported database)
     * @return the expression for this database language or for the default SQL or null when not found
     */
    public Expression getSqlExpression(IndicatorDefinition indicatorDefinition) {
        EList<TdExpression> sqlGenericExpression = indicatorDefinition.getSqlGenericExpression();

        // MOD xqliu 2010-02-25 feature 11201 MOD by zshen for bug 10630
        boolean excelToAccess = "Distinct Count".equals(indicatorDefinition.getName()) && "EXCEL".equals(this.dbmsName);//$NON-NLS-1$//$NON-NLS-2$
        String dbms = excelToAccess ? "Access" : this.dbmsName;//$NON-NLS-1$
        return getSqlExpression(indicatorDefinition, dbms, sqlGenericExpression, this.getDbVersion());
        // ~11201

    }

    /**
     * Method "getChartacterMappingExpression".
     *
     * @param indicatorDefinition contains a list of possible expression (one for each supported database)
     * @return the expression for this database language or for the default SQL or null when not found
     */
    public CharactersMapping getChartacterMappingExpression(IndicatorDefinition indicatorDefinition) {
        EList<CharactersMapping> charactersMappingExpression = indicatorDefinition.getCharactersMapping();

        // MOD xqliu 2010-02-25 feature 11201 MOD by zshen for bug 10630
        boolean excelToAccess = "Distinct Count".equals(indicatorDefinition.getName()) && "EXCEL".equals(this.dbmsName);//$NON-NLS-1$//$NON-NLS-2$
        String dbms = excelToAccess ? "Access" : this.dbmsName;//$NON-NLS-1$
        return getCharacterMappingExpression(indicatorDefinition, dbms, charactersMappingExpression, this.getDbVersion());
        // ~11201

    }

    /**
     *
     * Get the query Expression for one column
     *
     * @param column
     * @param where
     * @return
     */
    public Expression getColumnQueryExpression(TdColumn column, String where) {
        ModelElement columnSet = ColumnHelper.getColumnOwnerAsColumnSet(column);
        Schema parentSchema = SchemaHelper.getParentSchema(columnSet);
        Catalog parentCatalog = CatalogHelper.getParentCatalog(columnSet);
        if (parentSchema != null) {
            parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
        }
        String schemaName = parentSchema == null ? null : parentSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();
        String qualifiedName = this.toQualifiedName(catalogName, schemaName, columnSet.getName());
        Expression queryExpression = CoreFactory.eINSTANCE.createExpression();
        String expressionBody = getQuerySql(column.getName(), qualifiedName, where);
        queryExpression.setBody(expressionBody);
        queryExpression.setLanguage(this.getDbmsName());
        return queryExpression;

    }

    /**
     *
     * Get the query Expression for one table of column
     *
     * @param column
     * @param where
     * @return
     */
    public Expression getTableQueryExpression(MetadataTable metadataTable, String where) {
        Schema parentSchema = SchemaHelper.getParentSchema(metadataTable);
        Catalog parentCatalog = CatalogHelper.getParentCatalog(metadataTable);
        if (parentSchema != null) {
            parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
        }
        String schemaName = parentSchema == null ? null : parentSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();
        String qualifiedName = this.toQualifiedName(catalogName, schemaName, metadataTable.getName());
        Expression queryExpression = CoreFactory.eINSTANCE.createExpression();
        String expressionBody = getQuerySql(getSelectColumnsStr(metadataTable), qualifiedName, where);
        queryExpression.setBody(expressionBody);
        queryExpression.setLanguage(this.getDbmsName());
        return queryExpression;

    }

    /**
     *
     * Get select column string
     *
     * @param metadataTable
     * @return if columns size is zero will return * else return look like a,b,c
     */
    protected String getSelectColumnsStr(MetadataTable metadataTable) {
        StringBuffer strBuff = new StringBuffer();
        EList<MetadataColumn> filterColumns = metadataTable.getColumns();
        if (filterColumns.size() <= 0) {
            return ASTERISK;
        }
        for (MetadataColumn column : filterColumns) {
            if (strBuff.length() > 0) {
                strBuff.append(getSeparatedCharacter());
            }
            strBuff.append(this.quote(column.getName()));
        }
        return strBuff.toString();
    }

    protected String getSeparatedCharacter() {
        return PluginConstant.COMMA_STRING;
    }

    /**
     *
     * Get the query Expression for one table of column
     *
     * @param column
     * @param where
     * @return
     */
    public Expression getTableCountQueryExpression(MetadataTable metadataTable, String where) {
        Schema parentSchema = SchemaHelper.getParentSchema(metadataTable);
        Catalog parentCatalog = CatalogHelper.getParentCatalog(metadataTable);
        if (parentSchema != null) {
            parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
        }
        String schemaName = parentSchema == null ? null : parentSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();
        String qualifiedName = this.toQualifiedName(catalogName, schemaName, metadataTable.getName());
        Expression queryExpression = CoreFactory.eINSTANCE.createExpression();
        String expressionBody = getQuerySql(COUNT_ASTERISK, qualifiedName, where);
        queryExpression.setBody(expressionBody);
        queryExpression.setLanguage(this.getDbmsName());
        return queryExpression;

    }

    /**
     *
     * Get the query Expression for one table
     *
     * @param column
     * @param where
     * @return
     */
    public Expression getTableQueryExpression(TdColumn column, String where) {
        ModelElement columnSet = ColumnHelper.getColumnOwnerAsColumnSet(column);
        Schema parentSchema = SchemaHelper.getParentSchema(columnSet);
        Catalog parentCatalog = CatalogHelper.getParentCatalog(columnSet);
        if (parentSchema != null) {
            parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
        }
        String schemaName = parentSchema == null ? null : parentSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();
        String qualifiedName = this.toQualifiedName(catalogName, schemaName, columnSet.getName());
        Expression queryExpression = CoreFactory.eINSTANCE.createExpression();
        String expressionBody = getQuerySql(ASTERISK, qualifiedName, where);
        queryExpression.setBody(expressionBody);
        queryExpression.setLanguage(this.getDbmsName());
        return queryExpression;

    }

    /**
     * DOC talend Comment method "getQuerySql".
     *
     * @param name
     * @param qualifiedName
     */
    protected String getQuerySql(String name, String qualifiedName, String where) {
        String returnStr = "select " + name + " from " + qualifiedName;//$NON-NLS-1$ //$NON-NLS-2$
        if (where != null && where.length() > 0) {
            returnStr += where() + where;
        }
        return returnStr;
    }

    /**
     *
     * DOC zshen Comment method "getsoundexFunction".
     *
     * @param table, the name of table.
     * @param colName, the name of column.
     * @return sub Select Statement which instead the function of soundex().And if the database support for soundex()
     * then return old table name.
     */
    public String getSoundexFunction(String table, String colName) {

        return table;
    }

    /**
     *
     * DOC zshen Comment method "getsoundexFunction".
     *
     * @param table, the name of table.
     * @param colName, the name of column.
     * @param key, the condition of sql query.
     * @return the sql query for rows Statement.
     */
    public String getFreqRowsStatement(String colName, String table, String key) {
        return null;
    }

    /**
     * Method "getAggregate1argFunctions".
     *
     * @param indicatorDefinition
     * @return the ordered list of aggregate functions
     */
    public List<String> getAggregate1argFunctions(IndicatorDefinition indicatorDefinition) {
        final EList<TdExpression> aggregate1argFunctions = indicatorDefinition.getAggregate1argFunctions();
        return getFunctions(indicatorDefinition, aggregate1argFunctions);
    }

    /**
     * Method "getDate1argFunctions".
     *
     * @param indicatorDefinition
     * @return the ordered list of functions applicable to date columns
     */
    public List<String> getDate1argFunctions(IndicatorDefinition indicatorDefinition) {
        final EList<TdExpression> date1argFunctions = indicatorDefinition.getDate1argFunctions();
        return getFunctions(indicatorDefinition, date1argFunctions);
    }

    private List<String> getFunctions(IndicatorDefinition indicatorDefinition, final EList<TdExpression> functions) {
        // MOD xqliu 2010-02-25 feature 11201
        TdExpression sqlGenExpr = getSqlExpression(indicatorDefinition, this.dbmsName, functions, this.getDbVersion());
        // ~11201
        if (sqlGenExpr != null) {
            final String body = sqlGenExpr.getBody();
            final String[] fonc = body.split(";"); //$NON-NLS-1$
            return Arrays.asList(fonc); // language found
        }

        return Collections.emptyList();
    }

    /**
     * Method "getRegexPatternString".
     *
     * @param indicator
     * @return the regular expression or null if none was found
     */
    public String getRegexPatternString(Indicator indicator) {
        if (indicator instanceof PatternMatchingIndicator
                || (indicator instanceof UserDefIndicator && IndicatorCategoryHelper.isUserDefMatching(IndicatorCategoryHelper
                        .getCategory(indicator.getIndicatorDefinition())))) {
            IndicatorParameters parameters = indicator.getParameters();
            if (parameters == null) {
                return null;
            }
            Domain dataValidDomain = parameters.getDataValidDomain();
            if (dataValidDomain == null) {
                return null;
            }
            EList<Pattern> patterns = dataValidDomain.getPatterns();
            for (Pattern pattern : patterns) {
                Expression expression = this.getRegexp(pattern);
                return expression == null ? null : expression.getBody();
            }
        }
        return null;
    }

    /**
     * get language Sql Expression from TdExpression list with dbVersion, if not found, use the default "SQL" language.
     *
     * @param language
     * @param sqlGenericExpression
     * @param dbVersion
     * @return
     */
    public static CharactersMapping getCharacterMappingExpression(IndicatorDefinition indicatorDefinition, String language,
            EList<CharactersMapping> characterMappingExpression, ProductVersion dbVersion) {
        CharactersMapping defaultExpression = null;
        if (characterMappingExpression == null || characterMappingExpression.size() == 0) {
            return defaultExpression;
        }

        List<CharactersMapping> tempExpressions = new ArrayList<CharactersMapping>();
        // exact match the language first, if don't match then use fuzzy matching
        boolean matchingFlag = false;
        for (CharactersMapping cmExpr : characterMappingExpression) {
            if (DbmsLanguageFactory.equalsDbmsLanguage(language, cmExpr.getLanguage())) {
                tempExpressions.add(cmExpr);
                matchingFlag = true;
            }
        }
        if (!matchingFlag) {
            // if the language contain the key word, it is considered to be equals
            for (CharactersMapping sqlGenExpr : characterMappingExpression) {
                if (DbmsLanguageFactory.compareDbmsLanguage(language, sqlGenExpr.getLanguage())) {
                    tempExpressions.add(sqlGenExpr);
                }
            }
        }
        for (CharactersMapping exp : tempExpressions) {
            defaultExpression = exp;
        }
        if (defaultExpression != null) {
            // return the found default version expression
            return defaultExpression;
        }

        // else try with default language (ANSI SQL)
        String defaultLanguage = getDefaultLanguage();

        // if can not find the expression of default language, return null
        if (language.equals(defaultLanguage)) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.warn("The indicator SQL expression has not been found for the database type " + language //$NON-NLS-1$
                    + " for the indicator" + indicatorDefinition.getName() //$NON-NLS-1$
                    + ". This is not necessarily a problem since the default SQL expression will be used. " //$NON-NLS-1$
                    + "Nevertheless, if an SQL error during the analysis, this could be the cause."); //$NON-NLS-1$
            log.info("Trying to compute the indicator with the default language " + defaultLanguage); //$NON-NLS-1$
        }
        return getCharacterMappingExpression(indicatorDefinition, defaultLanguage, characterMappingExpression, dbVersion);
    }

    /**
     * get language Sql Expression from TdExpression list with dbVersion, if not found, use the default "SQL" language.
     *
     * @param language
     * @param sqlGenericExpression
     * @param dbVersion
     * @return
     */
    public TdExpression getSqlExpression(IndicatorDefinition indicatorDefinition, String language,
            EList<TdExpression> sqlGenericExpression, ProductVersion dbVersion) {
        TdExpression defaultExpression = null;
        if (sqlGenericExpression == null || sqlGenericExpression.size() == 0) {
            return defaultExpression;
        }

        List<TdExpression> tempExpressions = new ArrayList<TdExpression>();
        // exact match the language first, if don't match then use fuzzy matching
        boolean matchingFlag = false;
        for (TdExpression sqlGenExpr : sqlGenericExpression) {
            if (DbmsLanguageFactory.equalsDbmsLanguage(language, sqlGenExpr.getLanguage())) {
                tempExpressions.add(sqlGenExpr);
                matchingFlag = true;
            }
        }
        if (!matchingFlag) {
            // if the language contain the key word, it is considered to be equals
            for (TdExpression sqlGenExpr : sqlGenericExpression) {
                if (DbmsLanguageFactory.compareDbmsLanguage(language, sqlGenExpr.getLanguage())) {
                    tempExpressions.add(sqlGenExpr);
                }
            }
        }
        List<TdExpression> tempExpressions2 = new ArrayList<TdExpression>();
        for (TdExpression exp : tempExpressions) {
            if (exp.getVersion() == null || PluginConstant.EMPTY_STRING.equals(exp.getVersion())) {
                defaultExpression = exp;
            } else {
                if (dbVersion.toString().equals(exp.getVersion())) {
                    // find the identical version
                    return exp;
                } else {
                    tempExpressions2.add(exp);
                }
            }
        }
        TdExpression findByVersion = findTdExpressionByVersion(dbVersion, tempExpressions2);
        if (findByVersion != null) {
            return findByVersion;
        }

        if (defaultExpression != null) {
            // return the found default version expression
            return defaultExpression;
        }

        // TDQ-11558: added by msjian when there is no REDSHIFT definition, we use postgresql first
        // TDQ-16020: changed by msjian support redshift-sso
        if (DbmsLanguageFactory.compareDbmsLanguage(language, SupportDBUrlType.REDSHIFT.getLanguage())) {
            return getSqlExpression(indicatorDefinition, SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage(),
                    sqlGenericExpression, dbVersion);
        }
        // TDQ-11558~

        // else try with default language (ANSI SQL)
        String defaultLanguage = getDefaultLanguage();

        // if can not find the expression of default language, return null
        if (language.equals(defaultLanguage)) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.warn("The indicator SQL expression has not been found for the database type " + language //$NON-NLS-1$
                    + " for the indicator" + indicatorDefinition.getName() //$NON-NLS-1$
                    + ". This is not necessarily a problem since the default SQL expression will be used. " //$NON-NLS-1$
                    + "Nevertheless, if an SQL error during the analysis, this could be the cause."); //$NON-NLS-1$
            log.info("Trying to compute the indicator with the default language " + defaultLanguage); //$NON-NLS-1$
        }
        return getSqlExpression(indicatorDefinition, defaultLanguage, sqlGenericExpression, dbVersion);
    }

    /**
     * Method "getRegexp".
     *
     * When in SQL engine, retrieve the regex with the matching Dbms Language. If null, return the regex with default
     * SQL (even if it's null)
     *
     * @param pattern a pattern
     * @return the body of the regular expression applicable to this dbms or null
     */
    public Expression getRegexp(Pattern pattern) {
        // MOD by zhsne for bug 17172 2010.12.10
        Expression expression = null;
        Expression defaultExpression = null;
        EList<PatternComponent> components = pattern.getComponents();
        for (PatternComponent patternComponent : components) {
            if (patternComponent != null) {
                expression = this.getExpression(patternComponent);
                if (expression != null && DbmsLanguageFactory.compareDbmsLanguage(this.dbmsName, expression.getLanguage())) {
                    return expression;// return this db's expression
                } else if (expression != null
                        && DbmsLanguageFactory.compareDbmsLanguage(ExecutionLanguage.SQL.getName(), expression.getLanguage())) {
                    defaultExpression = expression; // remember the default expression
                }
            }
        }
        return defaultExpression;// if can not find itself, return the default one.
    }

    public Expression getExpression(ModelElement element) {
        if (element == null) {
            return null;
        }
        Expression expression = null;
        if (element instanceof Pattern) {
            expression = getRegexp(((Pattern) element));
        } else if (element instanceof Indicator) {

            Indicator indicator = ((Indicator) element);
            expression = this.getSqlExpression(indicator.getIndicatorDefinition());
        }
        return expression;
    }

    public String getBackSlashForRegex() {
        return "\\"; //$NON-NLS-1$
    }

    /**
     * Method "getExpression".
     *
     * @param patternComponent
     * @return the expression for the correct language or null
     */
    public Expression getExpression(PatternComponent patternComponent) {
        if (patternComponent != null && patternComponent.eClass().equals(PatternPackage.eINSTANCE.getRegularExpression())) {
            RegularExpression regExp = (RegularExpression) patternComponent;
            Expression expression = regExp.getExpression();
            if (expression != null) {
                return getApplicable(expression);
            }
        }
        // not a regular expression
        return null;
    }

    private Expression getApplicable(Expression expression) {
        return isApplicable(expression) ? expression : null;
    }

    /**
     * Method "getApplicableExpression".
     *
     * @param expression an expression (not null)
     * @return
     */
    public boolean isApplicable(Expression expression) {
        // handle database specific patterns
        if (is(expression.getLanguage())) {
            return true;
        }
        // try generic language
        if (SQL.equals(expression.getLanguage())) {
            return true;
        }
        // try Java language
        if (JAVA.equals(expression.getLanguage())) {
            return true;
        }
        // else no correct language found
        return false;
    }

    /**
     * Method "getRegexpTestString" returns the SQL SELECT statement that can be used to check a string against a
     * regular expression.
     *
     * @param stringToCheck a string to check (not a column name)
     * @param regularExpression
     * @return the appropriate SQL SELECT statement that can be used to check a string against a regular expression.
     */
    public String getSelectRegexpTestString(String stringToCheck, Expression regularExpression) {
        if (!isApplicable(regularExpression)) {
            return null;
        }
        String regex = regularExpression.getBody();
        if (regex == null) {
            return null;
        }
        return getSelectRegexpTestString(stringToCheck, regex);
    }

    /**
     * Method "getSelectRegexpTestString".
     *
     * @param stringToCheck a string to check against the regular expression (can contain quotes at start and end)
     * @param regex a regular expression
     * @return the appropriate SQL SELECT statement that can be used to check a string against a regular expression.
     */
    public String getSelectRegexpTestString(String stringToCheck, String regex) {
        String surroundedTestString = (stringToCheck.startsWith("'") && stringToCheck.endsWith("'")) ? stringToCheck //$NON-NLS-1$ //$NON-NLS-2$
                : surroundWith('\'', stringToCheck, '\'');
        String regexLikeExpression = regexLike(surroundedTestString, regex);
        // else
        if (regexLikeExpression == null) {
            return null;
        }
        return getSelectRegexp(regexLikeExpression);
    }

    protected String getSelectRegexp(String regexLikeExpression) {
        return "SELECT " + regexLikeExpression + EOS; //$NON-NLS-1$
    }

    /**
     * Method "regexLike". By default, it will try to extract the function name from user defined regular expression. If
     * the database support the regex like regular expression function , the sub-class will have to override this method
     * in order to return the correct regex like name.
     *
     * @param element
     * @param regex
     * @return get the String of Regular function(for example : "regex_like(element,regex)").
     *
     */
    public String regexLike(String element, String regex) {
        // TDQ-8637 UDF as a default case,if the database type has regular expression function,should overide this
        // method.

        if (null == regularExpressionFunction || PluginConstant.EMPTY_STRING.equals(regularExpressionFunction)
                || existEmptyInParameter(element, regex)) {
            return null;
        }
        String functionNameSQL = regularExpressionFunction + "( " + element + "," + regex + " )";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return surroundWithSpaces(functionNameSQL);
    }

    /**
     *
     * @param element
     * @param regex
     * @return false if every one is not empty else return true
     */
    private boolean existEmptyInParameter(String element, String regex) {
        return null == element || PluginConstant.EMPTY_STRING.equals(element) || null == regex
                || PluginConstant.EMPTY_STRING.equals(regex);
    }

    /**
     *
     * method "regexNotLike"
     *
     * @param element
     * @param regex
     * @return get the String of Regular function which is not match(for example :"not regex_like(element,regex)")
     *
     */

    public String regexNotLike(String element, String regex) {
        // TDQ-8637 UDF as a default case,if the database type has regular expression function,should overide this
        // method.
        String functionNameSQL = regexLike(element, regex);
        if (functionNameSQL == null) {
            return null;
        }
        return this.not() + functionNameSQL;
    }

    /**
     * Method "getHardCodedQuoteIdentifier" returns the hard coded quote identifier string. You should call
     * {@link #getDbQuoteString()} instead.
     *
     * @return hard coded quote identifier string
     */
    public String getHardCodedQuoteIdentifier() {
        return PluginConstant.EMPTY_STRING;
    }

    /**
     * Method "supportAliasesInGroupBy".
     *
     * @return true when the DB supports aliases in group by clause
     */
    public boolean supportAliasesInGroupBy() {
        // else Oracle, MSSQL, DB2, Postgresql
        return false;
    }

    /**
     * Method "isPkIndexSupported".
     *
     * @return true when the driver supports primary key and index
     */
    public boolean isPkIndexSupported() {
        return true;
    }

    /**
     * Method "supportCatalogSelection". See {@link java.sql.Connection#setCatalog(String catalog)}
     *
     * @return true when the driver can select a catalog as a subspace of the connection
     */
    public boolean supportCatalogSelection() {
        return true;
    }

    /**
     * Method "supportNonIntegerConstantInGroupBy".
     *
     * @return true if expression like "GROUP BY 'toto'" are supported
     */
    public boolean supportNonIntegerConstantInGroupBy() {
        // DBMS_SUPPORT
        // MySQL, Oracle
        return true;
    }

    /**
     * Method "getSelectRemarkOnTable".
     *
     * @param tableName a table name
     * @return the select statement to execute to get the comment on the table, or null
     */
    public String getSelectRemarkOnTable(String tableName) {
        return null;
    }

    /**
     * Method "getSelectRemarkOnColumn".
     *
     * @param columnName a column name
     * @return the select statement to execute to get the comment on the column, or null
     */
    public String getSelectRemarkOnColumn(String columnName) {
        return null;
    }

    /**
     * Method "getSelectOrderedAggregate".
     *
     * @param distinct whether to add distinct keyword in select statement
     * @param columns the list of columns to select
     * @param table the table
     * @param whereClause the where clause
     * @param groupByIndexes the indexes of the columns on which the aggregation is done
     * @param orderByIndexes the indexes of the columns on which the order is computed
     * @return the SQL statement
     */
    public String getSelectOrderedAggregate(boolean distinct, List<String> columns, String table, String whereClause,
            List<Integer> groupByIndexes, String havingClause, List<Integer> orderByIndexes) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT "); //$NON-NLS-1$
        builder.append(StringUtils.join(columns.iterator(), ','));
        builder.append(", COUNT(*) "); //$NON-NLS-1$
        builder.append(from());
        builder.append(table);

        if (whereClause != null) {
            builder.append(whereClause);
        }

        List<String> groupByColumns = new ArrayList<String>();
        for (int i = 0; i < groupByIndexes.size(); i++) {
            groupByColumns.add(columns.get(i));
        }
        if (!groupByColumns.isEmpty()) {
            builder.append(" GROUP BY "); //$NON-NLS-1$
            builder.append(StringUtils.join(groupByColumns.iterator(), ','));
        }

        if (havingClause != null) {
            builder.append(havingClause);
        }

        List<String> orderByColumns = new ArrayList<String>();
        for (int i = 0; i < orderByIndexes.size(); i++) {
            orderByColumns.add(columns.get(i));
        }
        if (!orderByColumns.isEmpty()) {
            builder.append(" ORDER BY "); //$NON-NLS-1$
            builder.append(StringUtils.join(orderByColumns.iterator(), ','));
        }

        return builder.toString();
    }

    public String orderBy(List<String> columns, boolean ascending) {
        return orderBy() + StringUtils.join(columns.iterator(), ',') + (ascending ? " ASC " : " DESC "); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Method "getGenericInvalidDetailedValues".
     *
     * @return the generic query to get the invalid detailed values in the functional dependency analysis
     */
    public String getFDGenericInvalidDetailedValues() {
        return "SELECT <%=__COLUMN_NAME_A__%> , <%=__COLUMN_NAME_B__%> , count(*) AS countNum FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A   FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) > 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAME_A__%> , <%=__COLUMN_NAME_B__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    public String getFDGenericValidDetailedValues() {
        return "SELECT <%=__COLUMN_NAME_A__%> , <%=__COLUMN_NAME_B__%> , count(*) AS countNum FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A   FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) = 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAME_A__%> , <%=__COLUMN_NAME_B__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    public String getFDGenericInvalidValues() {
        return "SELECT <%=__COLUMN_NAME_A__%> , count(*) AS countNum FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A   FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) > 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAME_A__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    public String getFDGenericValidValues() {
        return "SELECT <%=__COLUMN_NAME_A__%> , count(*) AS countNum FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A   FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) = 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAME_A__%> , <%=__COLUMN_NAME_B__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    public String getFDGenericValidRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A , COUNT(*) as countNum  FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) = 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    public String getFDGenericInvalidRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> JOIN (SELECT DISTINCT A , COUNT(*) as countNum  FROM (SELECT DISTINCT <%=__COLUMN_NAME_A__%> AS A , <%=__COLUMN_NAME_B__%> AS B FROM  <%=__TABLE_NAME__%> C   <%=__WHERE_CLAUSE__%> ) T GROUP BY A HAVING COUNT(*) > 1 ) J on (J.A = <%=__TABLE_NAME__%>.<%=__COLUMN_NAME_A__%>) <%=__WHERE_CLAUSE__%> ORDER BY <%=__COLUMN_NAME_A__%> ASC"; //$NON-NLS-1$
    }

    /**
     * Method "getInstantiatedExpression".
     *
     * @param indicator
     * @return the appropriate expression or the default one (or null when it does not exists)
     */
    public Expression getInstantiatedExpression(Indicator indicator) {
        Expression query = indicator.getInstantiatedExpressions(this.getDbmsName());
        if (query == null) {
            // try to get a default sql expression
            query = indicator.getInstantiatedExpressions(getDefaultLanguage());
        }
        return query;
    }

    /**
     * DOC scorreia Comment method "fillGenericQueryWithColumnTableAndAlias".
     *
     * @param genericQuery
     * @param columns
     * @param table
     * @param groupByAliases
     */
    public String fillGenericQueryWithColumnTableAndAlias(String genericQuery, String columns, String table, String groupByAliases) {
        return new GenericSQLHandler(genericQuery).replaceColumnTableAlias(columns, table, groupByAliases).getSqlString();
    }

    public String fillGenericQueryWithColumnsAndTable(String genericQuery, String columns, String table) {
        return new GenericSQLHandler(genericQuery).replaceColumnTable(columns, table).getSqlString();
    }

    public String fillGenericQueryWithColumnsAndTable(String genericQuery, String columns, String table,
            String originalClause) {
        return new GenericSQLHandler(genericQuery).replaceColumnTable(columns, table).getSqlString()
                + originalClause == null ? "" : originalClause; //$NON-NLS-1$
    }

    public String fillGenericQueryWithColumnsABAndTable(String genericQuery, String columnA, String columnB, String table) {
        return new GenericSQLHandler(genericQuery).replaceColumnA(columnA).replaceColumnB(columnB).replaceTable(table)
                .getSqlString();
    }

    public String fillGenericQueryWithColumnTablePattern(String genericQuery, String columns, String table, String regexp) {
        return new GenericSQLHandler(genericQuery).replaceColumnTablePattern(columns, table, regexp).getSqlString();
    }

    /**
     * DOC scorreia Comment method "fillGenericQueryWithColumnTableLimitOffset".
     *
     * @param genericQuery
     * @param colName
     * @param table
     * @param limitRow
     * @param offset
     * @param limitRowPlusOffset
     * @return
     */
    public String fillGenericQueryWithColumnTableLimitOffset(String genericQuery, String colName, String table, String limitRow,
            String offset, String limitRowPlusOffset) {
        return new GenericSQLHandler(genericQuery).replaceLimitOffset(colName, table, limitRow, offset, limitRowPlusOffset)
                .getSqlString();
    }

    public String fillGenericQueryWithJoin(String genericSQL, String tableNameA, String tableNameB, String joinClause,
            String whereClause) {
        return new GenericSQLHandler(genericSQL).replaceWithJoin(tableNameA, tableNameB, joinClause, whereClause).getSqlString();
    }

    /**
     * DOC scorreia Comment method "fillGenericQueryWithJoin".
     *
     * @param body
     * @param tableName
     * @param joinclause
     * @return
     */
    public String fillGenericQueryWithJoin(String genericSQL, String tableName, String joinclause) {
        return new GenericSQLHandler(genericSQL).replaceTable(tableName).replaceJoinClause(joinclause).getSqlString();
    }

    /**
     * Method "charLength".
     *
     * @param columnName
     * @return CHAR_LENGTH(columnName)
     */
    public String charLength(String columnName) {
        return " CHAR_LENGTH(" + columnName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * DOC bzhou Comment method "supportRegexp".
     *
     * check if the database surpport the Regexp.
     *
     * @return false by default.
     */
    public boolean supportRegexp() {
        return false;
    }

    // MOD mzhao 2010-2-24 bug 11753. Add prefix catalog or schema in case of join tables.
    public String createJoinConditionAsString(ModelElement leftTable, List<JoinElement> joinElements, String catalogName,
            String schemaName) {
        return createJoinConditionAsString(leftTable, joinElements, catalogName, schemaName, null);
    }

    /**
     * create left join condiction string.
     *
     * @param leftTable
     * @param joinElements
     * @param catalogName
     * @param schemaName
     * @return
     */
    public String createLeftJoinConditionAsString(ModelElement leftTable, List<JoinElement> joinElements, String catalogName,
            String schemaName) {
        return createJoinConditionAsString(leftTable, joinElements, catalogName, schemaName, JOIN_LEFT);
    }

    /**
     * create right join condiction string.
     *
     * @param leftTable
     * @param joinElements
     * @param catalogName
     * @param schemaName
     * @return
     */
    public String createRightJoinConditionAsString(ModelElement leftTable, List<JoinElement> joinElements, String catalogName,
            String schemaName) {
        return createJoinConditionAsString(leftTable, joinElements, catalogName, schemaName, JOIN_RIGHT);
    }

    /**
     * create full join condiction string.
     *
     * @param leftTable
     * @param joinElements
     * @param catalogName
     * @param schemaName
     * @return
     */
    public String createFullJoinConditionAsString(ModelElement leftTable, List<JoinElement> joinElements, String catalogName,
            String schemaName) {
        return createJoinConditionAsString(leftTable, joinElements, catalogName, schemaName, JOIN_FULL);
    }

    /**
     * create join condiction string.
     *
     * @param leftTable
     * @param joinElements
     * @param catalogName
     * @param schemaName
     * @param joinType (null: JOIN) (left: LEFT JOIN) (right: RIGHT JOIN) (full: FULL JOIN)
     * @return
     */
    public String createJoinConditionAsString(ModelElement leftTable, List<JoinElement> joinElements, String catalogName,
            String schemaName, String joinType) {
        if (joinElements.isEmpty()) {
            return PluginConstant.EMPTY_STRING;
        }
        // else
        String tempSchemaName = schemaName;
        StringBuilder builder = new StringBuilder();
        for (JoinElement joinElement : joinElements) {

            ModelElement colA = joinElement.getColA();
            String tableA = getTableName(colA);
            String tableAliasA = joinElement.getTableAliasA();

            String columnAName = getColumnName(colA);

            boolean hasTableAliasA = !StringUtils.isEmpty(tableAliasA);

            ModelElement colB = joinElement.getColB();
            String tableB = getTableName(colB);
            String tableAliasB = joinElement.getTableAliasB();

            String columnBName = getColumnName(colB);

            boolean hasTableAliasB = !StringUtils.isEmpty(tableAliasB);

            String operator = joinElement.getOperator();
            // MOD by klliu bug 20926 #c82152
            if (joinClauseStartsWithWrongTable(leftTable, getTable(colB)) && hasTableAliasA && hasTableAliasB) {
                // we need to exchange the table names otherwise we could get "tableA join tableA" which would cause
                // an SQL exception.
                // ~MOD mzhao 2010-2-24 bug 11753. Add prefix catalog or schema in case of join tables.
                tableA = toQualifiedName(catalogName, tempSchemaName, tableA);
                // ~
                buildJoinClause(builder, tableB, tableAliasB, columnBName, hasTableAliasB, tableA, tableAliasA, columnAName,
                        hasTableAliasA, operator, joinType);
            } else {
                // ~MOD mzhao 2010-2-24 bug 11753. Add prefix catalog or schema in case of join tables.
                tableB = toQualifiedName(catalogName, tempSchemaName, tableB);
                // ~
                buildJoinClause(builder, tableA, tableAliasA, columnAName, hasTableAliasA, tableB, tableAliasB, columnBName,
                        hasTableAliasB, operator, joinType);
            }
        }
        return builder.toString();
    }

    private void buildJoinClause(StringBuilder builder, String tableA, String tableAliasA, String columnAName,
            boolean hasTableAliasA, String tableB, String tableAliasB, String columnBName, boolean hasTableAliasB,
            String operator, String joinType) {
        boolean hasAlreadyOneJoin = builder.toString().contains(this.join());
        // begin of query is built ouside this method and should be:
        // SELECT count(*) FROM leftTableName
        // tableAliasA JOIN
        if (hasTableAliasA && !hasAlreadyOneJoin) {
            builder.append(surroundWithSpaces(tableAliasA));
        }
        // ~MOD mzhao 2010-2-24 bug 11753. Add prefix catalog or schema in case of join tables.
        join(builder, quote(tableA), tableAliasA, quote(columnAName), hasTableAliasA, tableB, tableAliasB, quote(columnBName),
                hasTableAliasB, operator, joinType);
    }

    /**
     * Method "innerJoin".
     *
     * @param tableA
     * @param tableAliasA
     * @param columnAName
     * @param hasTableAliasA
     * @param tableB
     * @param tableAliasB
     * @param columnBName
     * @param hasTableAliasB
     * @return "JOIN tableB tableAliasB ON (tableAliasA.columnAName = tableAliasB.columnBName)
     */
    public String innerJoin(String tableA, String tableAliasA, String columnAName, boolean hasTableAliasA, String tableB,
            String tableAliasB, String columnBName, boolean hasTableAliasB) {
        StringBuilder builder = new StringBuilder();
        return this.join(builder, tableA, tableAliasA, columnAName, hasTableAliasA, tableB, tableAliasB, columnBName,
                hasTableAliasB, this.equal());
    }

    public String join(StringBuilder builder, String tableA, String tableAliasA, String columnAName, boolean hasTableAliasA,
            String tableB, String tableAliasB, String columnBName, boolean hasTableAliasB, String operator) {
        return join(builder, tableA, tableAliasA, columnAName, hasTableAliasA, tableB, tableAliasB, columnBName, hasTableAliasB,
                operator, null);
    }

    public String join(StringBuilder builder, String tableA, String tableAliasA, String columnAName, boolean hasTableAliasA,
            String tableB, String tableAliasB, String columnBName, boolean hasTableAliasB, String operator, String joinType) {

        String join = joinType == null ? join() : " " + joinType + join(); //$NON-NLS-1$
        builder.append(join);

        // tableB tableAliasB ON
        builder.append(surroundWithSpaces(tableB));
        if (hasTableAliasB) {
            builder.append(surroundWithSpaces(tableAliasB));
        }
        builder.append(" ON "); //$NON-NLS-1$

        String tA = hasTableAliasA ? tableAliasA : tableA;
        String tB = hasTableAliasB ? tableAliasB : tableB;
        String cA = columnAName;
        String cB = columnBName;

        createJoinClause(builder, tA, cA, tB, cB, operator);
        return builder.toString();
    }

    public String join() {
        return " JOIN "; //$NON-NLS-1$
    }

    /**
     * DOC scorreia Comment method "joinClauseStartsWithWrongTable".
     *
     * @param leftTable
     * @param table
     * @return
     */
    private boolean joinClauseStartsWithWrongTable(ModelElement leftTable, ModelElement table) {
        return ResourceHelper.areSame(leftTable, table);
    }

    /**
     * Method "createJoinClause" appends a join condition to the builder.
     *
     * @param builder
     * @param tableA the name of the table or null
     * @param columnAName a column name (or an alias)
     * @param tableB the name of the second table or null
     * @param columnBName the column name (or an alias)
     * @param operator the operator used in the join
     */
    private void createJoinClause(StringBuilder builder, String tableA, String columnAName, String tableB, String columnBName,
            String operator) {
        // (tablealiasA.colA = tablealiasB.colB)
        builder.append('(');
        if (tableA != null) {
            builder.append(tableA + DOT);
        }
        builder.append(columnAName);

        builder.append(operator);
        if (tableB != null) {
            builder.append(tableB + DOT);
        }
        builder.append(columnBName);
        builder.append(')');
    }

    public String getColumnName(ModelElement col) {
        TdColumn column = col != null ? SwitchHelpers.COLUMN_SWITCH.doSwitch(col) : null;
        return column != null ? column.getName() : null;
    }

    public String getTableName(ModelElement col) {
        TdColumn column = col != null ? SwitchHelpers.COLUMN_SWITCH.doSwitch(col) : null;
        return (column != null) ? ColumnHelper.getTableFullName(column) : null;
    }

    public ModelElement getTable(ModelElement col) {
        TdColumn column = col != null ? SwitchHelpers.COLUMN_SWITCH.doSwitch(col) : null;
        return (column != null) ? ColumnHelper.getColumnOwnerAsColumnSet(column) : null;
    }

    /**
     * DOC jet adapt to {@link GenericSQLHandler}("").createGenericSqlWithRegexFunction() method.
     * <p>
     *
     * @see GenericSQLHandler
     * @param function UDF function name.
     * @return special sql statement
     */
    public String createGenericSqlWithRegexFunction(String function) {
        return new GenericSQLHandler(PluginConstant.EMPTY_STRING).createGenericSqlWithRegexFunction(function);
    }

    /**
     * @return if this DbmsLanage hasn't been inited return true, else return false
     */
    public boolean isSql() {
        return SQL.equals(getDbmsName());
    }

    public String getRegularExpressionFunction() {
        return this.regularExpressionFunction;
    }

    public void setRegularExpressionFunction(String functionName) {
        this.regularExpressionFunction = functionName;
    }

    public String trimIfBlank(String colName) {
        // just trim for blank data
        return " CASE WHEN  " + charLength(trim(colName)) + "=0  THEN '' ELSE  " + colName + " END"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * DOC yyi 2011-06-14 22246:view rows for aveagge length
     *
     * @return average length sql statement
     */
    public String getAverageLengthWithBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    /**
     * DOC qiongli TDQ-2474: view rows for average length with blank.
     *
     * @return average length with blank sql statement
     */
    public String getAverageLengthRows() {
        String whereExpression = "WHERE <%=__COLUMN_NAMES__%> IS NOT NULL AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' )"; //$NON-NLS-1$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression + " AND LENGTH(<%=__COLUMN_NAMES__%>"
                + ") BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%> "
                + whereExpression
                + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(* )) FROM <%=__TABLE_NAME__%> "
                + whereExpression + ")"; //$NON-NLS-1$ 
    }

    /**
     * DOC qiongli TDQ-2474 :view rows for average length with null blank.
     *
     * @return average length with null blank sql statement
     */
    public String getAverageLengthWithNullBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%>)" //$NON-NLS-1$
                + " AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(* )) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    /**
     * DOC qiongli TDQ-2474:view rows for average length with null.
     *
     * @return average length with null sql statement
     */
    public String getAverageLengthWithNullRows() {
        String whereExpression = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression //$NON-NLS-1$
                + "AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%> ))*1.0 / COUNT( * )) FROM <%=__TABLE_NAME__%> " //$NON-NLS-1$
                + whereExpression
                + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%> ))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%>  " //$NON-NLS-1$
                + whereExpression + ")"; //$NON-NLS-1$
    }

    /**
     * DOC xqliu Comment method "createStatement".
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public Statement createStatement(java.sql.Connection connection, int fetchSize) throws SQLException {
        Statement statement = createStatement(connection);
        statement.setFetchSize(fetchSize);
        return statement;
    }

    /**
     * DOC msjian Comment method "createStatement".
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public Statement createStatement(java.sql.Connection connection) throws SQLException {
        return connection.createStatement();
    }

    /**
     *
     * Get query string with prefix (catalog/schema.table.column) given column array.
     *
     * @param columns
     * @return
     */
    public String getQueryColumnsWithPrefix(TdColumn[] columns) {
        String columnClause = PluginConstant.EMPTY_STRING;
        if (columns.length == 0) {
            return columnClause;
        }
        ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(columns[0]);
        String tableName = getQueryColumnSetWithPrefix(columnSet);
        for (TdColumn column : columns) {
            columnClause += tableName + DOT + quote(column.getName()) + getSeparatedCharacter();
        }
        columnClause = columnClause.substring(0, columnClause.length() - 1);
        return columnClause;
    }

    /**
     *
     * move this method from ColumnSetNameHelper.getColumnSetQualifiedName().
     *
     * @param columnset
     * @return
     */
    public String getQueryColumnSetWithPrefix(ColumnSet columnset) {
        Catalog catalog = getCatalog(columnset);
        Schema schema = getSchema(columnset);
        String catalogName = null;
        String schemaName = null;
        if (catalog != null) {
            catalogName = catalog.getName();
        }
        if (schema != null) {
            schemaName = schema.getName();
        }
        return getQualifiedColumnSetName(columnset, catalogName, schemaName);

    }

    /**
     * DOC qiongli Comment method "getQualifiedColumnSetName".
     *
     * @param columnset
     * @param catalogName
     * @param schemaName
     * @return
     */
    protected String getQualifiedColumnSetName(ColumnSet columnset, String catalogName, String schemaName) {
        DatabaseConnection dbConn = (DatabaseConnection) ConnectionHelper.getConnection(columnset);
        if (dbConn != null && dbConn.isContextMode()) {
            return getQueryColumnSetWithPrefixFromContext(dbConn, catalogName, schemaName, columnset.getName());
        }

        return toQualifiedName(catalogName, schemaName, columnset.getName());
    }

    protected String getQueryColumnSetWithPrefixFromContext(DatabaseConnection dbConn, String catalogName, String schemaName,
            String tableName) {
        String catalogNameFromContext = getCatalogNameFromContext(dbConn);
        String schemaNameFromContext = getSchemaNameFromContext(dbConn);
        String cName = catalogNameFromContext != null && catalogNameFromContext.trim().length() > 0 ? catalogNameFromContext
                : catalogName;
        String sName = schemaNameFromContext != null && schemaNameFromContext.trim().length() > 0 ? schemaNameFromContext
                : schemaName;
        return toQualifiedName(cName, sName, tableName);
    }

    /**
     *
     * move this method from ColumnSetNameHelper.getColumnSetQualifiedName().
     *
     * @param columnset
     * @return
     */
    public String getQueryColumnSetWithPrefix(TdColumn tdColumn) {
        ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(tdColumn);
        if (columnSet == null) {
            log.error("Can not find table by column"); //$NON-NLS-1$
            return PluginConstant.EMPTY_STRING;

        }
        return getQueryColumnSetWithPrefix(columnSet);

    }

    protected String getQueryColumns(TdColumn[] columns) {
        String columnClause = PluginConstant.EMPTY_STRING;
        for (TdColumn column : columns) {
            columnClause += quote(column.getName()) + getSeparatedCharacter();
        }
        columnClause = columnClause.substring(0, columnClause.length() - 1);
        return columnClause;
    }

    /**
     *
     * Get invalid clause for Benford indicator.
     *
     * @param columnName
     * @return
     */
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or " + columnName + " not REGEXP '^[0-9]'"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     *
     * Cast column name to char type. .e.g.vertica column name is to_char(columnName) in query clause.
     *
     * @param columnName
     * @return
     */
    public String castColumnNameToChar(String columnName) {
        return columnName;
    }

    /**
     * get the catalog name from the context in the DatabaseConnection(the database connection must be context mode).
     *
     * @param dbConn
     * @return catalog name or null
     */
    public String getCatalogNameFromContext(DatabaseConnection dbConn) {
        return cloneOriginalValueConnection(dbConn).getSID();
    }

    /**
     * get the schema name from the context in the DatabaseConnection(the database connection must be context mode).
     *
     * @param dbConn
     * @return schema name or null
     */
    public String getSchemaNameFromContext(DatabaseConnection dbConn) {
        return cloneOriginalValueConnection(dbConn).getUiSchema();
    }

    /**
     * clone the database connection with original value according to the context(the database connection must be
     * context mode).
     *
     * @param dbConn
     * @return
     */
    private DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
        IRepositoryContextService repositoryContextService = CoreRuntimePlugin.getInstance().getRepositoryContextService();
        if (repositoryContextService != null) {
            String contextName = dbConn.getContextName();
            if (contextName == null) {
                String msg = Messages.getString("DbmsLanguage.ContextNameIsNull"); //$NON-NLS-1$
                RuntimeException exp = new RuntimeException(msg);
                log.error(msg, exp);
                throw exp;
            }
            return repositoryContextService.cloneOriginalValueConnection(dbConn, false, contextName);
        } else {
            String msg = Messages.getString("DbmsLanguage.IRepositoryContextServiceIsNull"); //$NON-NLS-1$
            RuntimeException exp = new RuntimeException(msg);
            log.error(msg, exp);
            throw exp;
        }
    }

    /**
     * get the catalog or schema name according to the analyzed column.
     *
     * @param analyzedColumn
     * @return if the catalog is not null, return catalog's name, else if schema is not null, return schema's name, else
     * return null
     */
    public String getCatalogOrSchemaName(TdColumn tdColumn) {
        String name = null;
        // get the catalog/schema name from the context
        DatabaseConnection dbConn = ConnectionHelper.getTdDataProvider(tdColumn);
        if (dbConn != null && dbConn.isContextMode()) {
            name = getCatalogNameFromContext(dbConn);
            if (!StringUtils.isEmpty(name)) {
                return name;
            }
            name = getSchemaNameFromContext(dbConn);
            if (!StringUtils.isEmpty(name)) {
                return name;
            }
        }

        // if the catalog/schema name from the context is empty, get it by the column
        if (StringUtils.isEmpty(name)) {
            ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(tdColumn);
            // Get catalog
            Catalog catalog = getCatalog(columnSet);
            if (catalog != null) {
                return catalog.getName();
            }
            // Get schema
            Schema schema = getSchema(columnSet);
            if (schema != null) {
                return schema.getName();
            }
            // no catalog and schema
            log.error(Messages.getString("DbmsLanguage.NoCatalogOrSchema", columnSet.getName()));//$NON-NLS-1$
        }
        return name;
    }

    /**
     * get the schema from the columnSetOwner.
     *
     * @param columnSetOwner
     * @return
     */
    protected Schema getSchema(ModelElement columnSetOwner) {
        Package pack = ColumnSetHelper.getParentCatalogOrSchema(columnSetOwner);
        if (pack instanceof Schema) {
            return (Schema) pack;
        }
        return null;
    }

    /**
     * get the catalog from the columnSetOwner.
     *
     * @param columnSetOwner it should be a table or schema
     * @return
     */
    protected Catalog getCatalog(ModelElement columnSetOwner) {
        Package pack = ColumnSetHelper.getParentCatalogOrSchema(columnSetOwner);
        if (pack instanceof Catalog) {
            return (Catalog) pack;
        }
        return null;
    }

    /**
     * Getter for soundexPrefix.
     *
     * @return the soundexPrefix
     */
    public String getSoundexPrefix() {
        return SOUNDEX_PREFIX;
    }

    /**
     *
     * Extract the name of regular Expression Function If current database type need to use UDF deal regular expression,
     * the expresssion which will definition on "Regular Expression Matching.definition" should like below:
     *
     * "* + when REGULAR_FUNCTION(+ * +) + *". else this method will not return correct result which you want
     *
     * @param expression
     * @return the name of regular Expression Function or empty string when the expression is invalid
     *
     */
    public String extractRegularExpressionFunction(Expression expression, String regexp) {
        String functionName = null;
        try {
            String tempString = splictExpression(expression);
            functionName = tempString.split("\\(").length > 1 ? tempString.split("\\(")[0] : PluginConstant.EMPTY_STRING;//$NON-NLS-1$//$NON-NLS-2$
            functionName = functionName.trim();
        } catch (NullPointerException e) {
            log.error(e, e);
        }
        return functionName;
    }

    /**
     * DOC talend Comment method "splictExpression".
     *
     * @param expression
     * @return
     */
    protected String splictExpression(Expression expression) {
        if (expression == null || expression.getBody() == null) {
            return PluginConstant.EMPTY_STRING;
        }
        String body = expression.getBody().toUpperCase();
        String tempString = body.split("WHEN").length > 1 ? body.split("WHEN")[1] : PluginConstant.EMPTY_STRING;//$NON-NLS-1$//$NON-NLS-2$
        return tempString;
    }

    /**
     *
     * Extract the return value of regular Expression Function If current database type need to use UDF deal regular
     * expression, the expresssion which will definition on "Regular Expression Matching.definition" should like below:
     *
     * "* + when REGULAR_FUNCTION(+ * +) + return value + then". else this method will not return correct result which
     * you want
     *
     * @param expression
     * @return the return value of regular Expression Function or empty string when the expression is invalid
     *
     */
    public String extractRegularExpressionFunctionReturnValue(Expression expression, String regexp) {
        String tempString = splictExpression(expression);
        if (regexp == null) {
            return tempString;
        }
        String splitKey = regexp.toUpperCase() + ")"; //$NON-NLS-1$
        int keyIndex = tempString.indexOf(splitKey) + splitKey.length();
        tempString = tempString.indexOf(splitKey) > -1 ? tempString.substring(keyIndex) : PluginConstant.EMPTY_STRING;
        tempString = tempString.split("THEN").length > 1 ? tempString.split("THEN")[0] : PluginConstant.EMPTY_STRING; //$NON-NLS-1$ //$NON-NLS-2$
        return tempString.trim();
    }

    /**
     *
     * remember the result value for regular expression.So that we can get complete expression and it should be "=1"
     * always
     *
     * @param expression
     */
    public void setFunctionReturnValue(String returnValue) {
        regularfunctionReturnValue = returnValue.trim();
    }

    /**
     * Getter for regularfunctionReturnValue.
     *
     * @return the regularfunctionReturnValue if it is null then return ""
     */
    public String getFunctionReturnValue() {
        if (regularfunctionReturnValue == null) {
            return StringUtils.EMPTY;
        }
        return this.regularfunctionReturnValue;
    }

    /**
     * Method "castColumn".
     *
     * @param indicator
     * @param tdColumn
     * @param colName the name of the given column (tdColumn.getName() ) (could contain quotes)
     * @return
     */
    public String castColumn4ColumnAnalysisSqlExecutor(Indicator indicator, TdColumn tdColumn, String colName) {
        int javaType = tdColumn.getSqlDataType().getJavaDataType();
        boolean isText = Java2SqlType.isTextInSQL(javaType);

        String contentType = tdColumn.getContentType();
        DataminingType dataminingType = DataminingType.get(contentType);
        if (DataminingType.INTERVAL.equals(dataminingType) && (isText)) {
            // cast is needed
            // MOD qiongli 2011-4-18,bug 16723(data cleansing),handle date
            boolean isDate = Java2SqlType.isDateInSQL(javaType);
            if (isDate) {
                return "CAST(" + colName + " AS DATE)";//$NON-NLS-1$//$NON-NLS-2$
            }
            boolean isNumeric = Java2SqlType.isNumbericInSQL(javaType);
            if (isNumeric) {
                // TODO scorreia user should tell the expected format
                return "CAST(" + colName + " AS DECIMAL)"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return colName;
    }

    /**
     * DOC msjian Comment method "getRandomQuery".
     *
     * @param query
     * @return query with random method
     */
    public String getRandomQuery(String query) {
        // this method can be used for as400, db2, hive, ingres, mysql, exasol
        return query + orderBy() + "RAND() "; //$NON-NLS-1$
    }

    /**
     * @Description: Find a TdExpression by a matched/nearest version between TdExpression and ProductVersion
     * @param dbVersion
     * @param allExpressions
     * @return
     */
    protected TdExpression findTdExpressionByVersion(ProductVersion dbVersion,
            List<TdExpression> allExpressions) {
        if (dbVersion == null || allExpressions.isEmpty()) {
            return null;
        }
        int majorVersion = dbVersion.getMajor();
        TdExpression nearestVersionExp = null;
        int minDistance = 1000;
        for (TdExpression exp : allExpressions) {
            String expDBVersion = exp.getVersion();
            int mainVersionInExp;
            if (expDBVersion.contains(".")) {
                String[] splitVersion = expDBVersion.split("\\.");
                mainVersionInExp = Integer.parseInt(splitVersion[0]);
            } else {
                mainVersionInExp = Integer.parseInt(expDBVersion);
            }
            int subsResult = majorVersion - mainVersionInExp;
            // return it when same major version, example: the sql expression's version is 5.1, the db version is 5.1.2
            if (subsResult == 0) {
                return exp;
            }
            // find the nearest version in allExpressions.example: real dbVersion is 9.0, 2 sql expression's version 5.0
            // and 8.0. should find the version 8.0's sql expression
            if (subsResult > 0 && subsResult < minDistance) {
                minDistance = subsResult;
                nearestVersionExp = exp;
            }
        }
        if (nearestVersionExp != null && minDistance != 1000) {
            return nearestVersionExp;
        }
        return null;
    }

    public String getMinLengthWithNullRows() {
        return "OR  <%=__COLUMN_NAMES__%> IS NULL ";
    }

    public String getMinLengthWithBlankNullRows() {
        return "OR <%=__COLUMN_NAMES__%> IS NULL";
    }
}
