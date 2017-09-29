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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dataquality.indicators.schema.SchemaPackage;
import org.talend.dataquality.indicators.schema.TableIndicator;
import org.talend.dataquality.indicators.schema.ViewIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 * 
 * @param <T> the type of analyzed element.
 */
public abstract class AbstractSchemaEvaluator<T> extends Evaluator<T> {

    public static final char FILTER_SEP = ',';

    private DbmsLanguage dbmsLanguage;

    private static final String SELECT_COUNT_FROM = "select count(*) from "; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(AbstractSchemaEvaluator.class);

    /**
     * The maximum number of exception before we restart the connection in order to release correctly the cursors.
     */
    private static final int MAX_EXCEPTION = 20;

    private int nbExceptions = 0;

    private String catalogPattern = null;

    private String schemaPattern = null;

    protected String tablePattern = null;

    protected String viewPattern = null;

    protected DbmsLanguage dbms() {
        if (this.dbmsLanguage == null) {
            DataManager dm = this.getDataManager();
            if (dm == null) {
                throw new RuntimeException("No data manager found."); //$NON-NLS-1$ 
            }
            this.dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(dm);
        }
        return this.dbmsLanguage;
    }

    protected abstract Connection getDataManager();

    private Set<String> catalogsName = new HashSet<String>();

    private Set<String> schemasName = new HashSet<String>();

    /**
     * DOC scorreia Comment method "queryOnTable".
     * 
     * @param catalog
     * @param schema
     * @param schemaIndic
     * @param tablesSet
     * @param tableCube
     * @param ok
     * @throws SQLException
     */
    protected void evalAllCounts(String catalog, String schema, NamedColumnSet t, SchemaIndicator schemaIndic, boolean isTable,
            ReturnCode ok) throws SQLException {
        // MOD klliu 2011-02-17 bug 18961
        // TDQ-8277 should consider tha database just has catalog(like hive/mysal).then get the quCatalog.
        String quCatalog = getCatalogNameWithQuote(schemaIndic);
        String quSchema = schema == null ? null : dbms().quote(schema);
        final String table = t.getName();
        String quTable = dbms().quote(table);

        if (isTable) {
            long rowCount = getRowCounts(quCatalog, quSchema, quTable);
            schemaIndic.setTableRowCount(schemaIndic.getTableRowCount() + rowCount);
            // MOD qiongli 2012-8-13 TDQ-5907.Hive dosen't support PK/INDEX.
            boolean isPkIndexSupported = dbmsLanguage.isPkIndexSupported();
            // ---- pk----indexes
            int pkCount = 0;
            int idxCount = 0;
            if (isPkIndexSupported) {
                pkCount = getPKCount(catalog, schema, table);
                schemaIndic.setKeyCount(schemaIndic.getKeyCount() + pkCount);
                idxCount = getIndexCount(catalog, schema, table);
                schemaIndic.setIndexCount(schemaIndic.getIndexCount() + idxCount);
            }

            // create Table Indicator
            // TODO create tableindicator only if it's in top N or in bottom N (use an option?)
            createTableIndicator(t, schemaIndic, rowCount, pkCount, idxCount);
        } else { // is a view TODO probably need to handle system tables separately
            long rowCount = getRowCounts(quCatalog, schema, quTable);
            schemaIndic.setViewRowCount(schemaIndic.getViewRowCount() + rowCount);
            createViewIndicator(t, schemaIndic, rowCount);
        }
        // --- triggers (JDBC API cannot get triggers)

    }

    /**
     * just extract this method from evalAllCounts,and need to junit.
     * 
     * @param SchemaIndicator
     */
    protected String getCatalogNameWithQuote(SchemaIndicator schemaIndic) {
        String quCatalog = null;
        ModelElement analyzedElement = schemaIndic.getAnalyzedElement();
        if (analyzedElement != null) {
            EObject eContainer = analyzedElement.eContainer();
            if (SwitchHelpers.CATALOG_SWITCH.doSwitch(analyzedElement) != null) {
                quCatalog = dbms().quote(((Catalog) analyzedElement).getName());
            } else if (eContainer != null && SwitchHelpers.CATALOG_SWITCH.doSwitch(eContainer) != null) {
                quCatalog = dbms().quote(((Catalog) eContainer).getName());
            }
        }
        return quCatalog;
    }

    /**
     * DOC scorreia Comment method "createViewIndicator".
     * 
     * @param t
     * @param schemaIndic
     * @param rowCount
     */
    private void createViewIndicator(NamedColumnSet t, SchemaIndicator schemaIndic, long rowCount) {
        ViewIndicator viewIndicator = SchemaFactory.eINSTANCE.createViewIndicator();
        // MOD xqliu 2009-1-21 feature 4715
        DefinitionHandler.getInstance().setDefaultIndicatorDefinition(viewIndicator);
        // t is not stored in xmi file. tableIndicator.setAnalyzedElement(t);
        viewIndicator.setTableName(t.getName());
        viewIndicator.setRowCount(rowCount);
        schemaIndic.addViewIndicator(viewIndicator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#executeSqlQuery(java.lang.String)
     * 
     * Note that the given statement is not used.
     */
    @Override
    protected abstract ReturnCode executeSqlQuery(String sqlStatement) throws SQLException;

    /**
     * DOC scorreia Comment method "createTableIndicator".
     * 
     * @param t
     * @param schemaIndic
     * @param rowCount
     * @param pkCount
     * @param idxCount
     */
    private void createTableIndicator(NamedColumnSet t, SchemaIndicator schemaIndic, long rowCount, int pkCount, int idxCount) {
        TableIndicator tableIndicator = SchemaFactory.eINSTANCE.createTableIndicator();
        // MOD xqliu 2009-1-21 feature 4715
        DefinitionHandler.getInstance().setDefaultIndicatorDefinition(tableIndicator);
        // t is not stored in xmi file. tableIndicator.setAnalyzedElement(t);
        tableIndicator.setTableName(t.getName());
        tableIndicator.setRowCount(rowCount);
        tableIndicator.setKeyCount(pkCount);
        tableIndicator.setIndexCount(idxCount);
        schemaIndic.addTableIndicator(tableIndicator);
    }

    /**
     * DOC scorreia Comment method "getIndexCount".
     * 
     * @param catalog
     * @param schema
     * @param table
     * @param idxCount
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("deprecation")
    private int getIndexCount(String catalog, String schema, String table) throws SQLException {
        int idxCount = 0;
        ResultSet idx = null;
        try {
            // MOD xqliu 2009-07-13 bug 7888
            idx = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(getConnection()).getIndexInfo(catalog, schema,
                    table, false, true);
            // ~
        } catch (SQLException e) {

            log.warn(Messages.getString("AbstractSchemaEvaluator.IndexException", //$NON-NLS-1$
                    this.dbms().toQualifiedName(catalog, schema, table), e.getLocalizedMessage()), e);
            // Oracle increments the number of cursors to close each time a new query is executed after this exception!
            reloadConnectionAfterException(catalog);
        }
        // TODO unicity of index could be a parameter
        if (idx != null) {
            while (idx.next()) {
                // MOD msjian 2011-10-9 TDQ-3566: incorrect index number result in overview analysis
                // MOD 20130418 TDQ-6823 use type!=tableIndexStatistic to filter the statistic index(do not show this
                // type)
                if (0 != idx.getShort("TYPE")) { //$NON-NLS-1$
                    idxCount += 1;
                }
                // TDQ-3566 ~
            }
            idx.close();
        }
        return idxCount;
    }

    /**
     * DOC scorreia Comment method "getPKCount".
     * 
     * @param catalog
     * @param schema
     * @param table
     * @param pkCount
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("deprecation")
    private int getPKCount(String catalog, String schema, String table) throws SQLException {
        int pkCount = 0;
        ResultSet pk = null;
        try {
            // MOD xqliu 2009-07-13 bug 7888
            pk = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(getConnection()).getPrimaryKeys(catalog, schema,
                    table);
            // ~
        } catch (SQLException e1) {
            log.warn(Messages.getString("AbstractSchemaEvaluator.PrimaryException", //$NON-NLS-1$
                    this.dbms().toQualifiedName(catalog, schema, table), e1.getLocalizedMessage()), e1);
            reloadConnectionAfterException(catalog);
        }
        if (pk != null) {
            while (pk.next()) {
                pkCount += 1;
            }
            pk.close();
        }
        return pkCount;
    }

    /**
     * DOC scorreia Comment method "reloadConnection".
     * 
     * @param catalog
     */
    protected void reloadConnectionAfterException(String catalog) {
        nbExceptions++;
        if (nbExceptions < MAX_EXCEPTION) {
            return; // not yet
        }
        ReturnCode connClosed = super.closeConnection();
        if (!connClosed.isOk()) {
            log.error(Messages.getString("AbstractSchemaEvaluator.ReloadProblem", connClosed.getMessage())); //$NON-NLS-1$
        }
        Connection dp = this.getDataManager();
        TypedReturnCode<java.sql.Connection> conn = JavaSqlFactory.createConnection(dp);
        if (!conn.isOk()) {
            log.error(conn.getMessage());
            return;
        }
        // else ok
        this.setConnection(conn.getObject());
        this.selectCatalog(catalog);

        // reset the number of exceptions
        nbExceptions = 0;
    }

    /**
     * DOC scorreia Comment method "getRowCounts".
     * 
     * @param schemaIndic
     * @param quCatalog
     * @param quSchema
     * @param quTable
     * @return
     * @throws SQLException
     */
    private long getRowCounts(String quCatalog, String quSchema, String quTable) throws SQLException {
        String sql = SELECT_COUNT_FROM + dbms().toQualifiedName(quCatalog, quSchema, quTable);

        long totalRowCount = 0;
        java.sql.Connection conn = getConnection();
        if (conn == null || conn.isClosed()) {
            return totalRowCount;
        }

        Statement statement = null;
        // MOD qiongli 2012-8-13.TDQ-5907
        if (DbmsLanguageFactory.isHive(dbmsLanguage.getDbmsName())) {
            statement = conn.createStatement();
        } else {
            statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        }
        // not needed here statement.setFetchSize(fetchSize);
        try {
            if (log.isInfoEnabled()) {
                log.info("The execute query is:  " + sql); //$NON-NLS-1$
            }
            // MOD xqliu 2009-02-09 bug 6237
            if (continueRun()) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            statement.close();
            log.warn(e.getMessage() + " for the query: " + sql); //$NON-NLS-1$
            if (log.isDebugEnabled()) {
                log.debug(e, e);
            }
            // some tables on Oracle give the following exception:
            // ORA-25191: cannot reference overflow table of an index-organized table
            reloadConnectionAfterException(quCatalog);
            return totalRowCount;
        }

        // get the results
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            String mess = Messages.getString("Evaluator.NoResultSet", sql); //$NON-NLS-1$
            log.warn(mess);
        } else {
            while (resultSet != null && resultSet.next()) {
                // MOD xqliu 2009-02-09 bug 6237
                if (!continueRun()) {
                    break;
                }
                // --- get content of column
                String str = String.valueOf(resultSet.getObject(1));
                // MOD gdbu 2011-4-21 bug : 18975
                Long count = IndicatorHelper.getLongFromObject(str);
                // ~18975
                totalRowCount += count;
                if (log.isDebugEnabled()) {
                    log.debug(quCatalog + "/" + quSchema + "/" + quTable + ": " + count); //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
                }

                // TODO scorreia give a full row to indicator (indicator will have to handle its data??

            }
            // --- release resultset
            resultSet.close();
        }

        // -- release JDBC resources
        statement.close();
        return totalRowCount;
    }

    /**
     * DOC scorreia Comment method "evalSchemaIndic".
     * 
     * @param connIndicator
     * @param tdCatalog
     * @param tableBuilder
     * @param tablePattern
     * @param ok
     * @throws SQLException
     */
    protected void evalCatalogIndic(final CatalogIndicator catalogIndic, Catalog tdCatalog, ReturnCode ok) throws SQLException {
        this.evalSchemaIndicLow(null, catalogIndic, tdCatalog, null, ok);
    }

    /**
     * DOC scorreia Comment method "evalSchemaIndic".
     * 
     * @param tdSchema
     * @param tableBuilder
     * @param tablePattern
     * @param ok
     * @throws SQLException
     */
    protected void evalSchemaIndic(Schema tdSchema, ReturnCode ok) throws SQLException {
        // --- create SchemaIndicator for each catalog
        SchemaIndicator schemaIndic = SchemaFactory.eINSTANCE.createSchemaIndicator();
        // MOD xqliu 2009-1-21 feature 4715
        DefinitionHandler.getInstance().setDefaultIndicatorDefinition(schemaIndic);
        this.evalSchemaIndicLow(null, schemaIndic, null, tdSchema, ok);
    }

    protected void evalSchemaIndicLow(final CatalogIndicator catalogIndic, final SchemaIndicator schemaIndic,
            final Catalog tdCatalog, final Schema tdSchema, ReturnCode ok) throws SQLException {
        // MOD klliu 2011-02-17 bug 18961
        boolean hasSchema = tdSchema != null;
        boolean hasCatalog = false;
        String schemaName = hasSchema ? tdSchema.getName() : null;
        String catName = null;
        if (tdCatalog == null) {
            if (tdSchema.eContainer() instanceof Catalog) {
                hasCatalog = true;
                catName = ((Catalog) tdSchema.eContainer()).getName();
            }
        } else {
            hasCatalog = true;
            catName = hasCatalog ? tdCatalog.getName() : null;
        }
        schemaIndic.setAnalyzedElement(hasSchema ? tdSchema : tdCatalog);

        // profile tables
        int tableCount = 0;
        final String[] tablePatterns = tablePattern != null && tablePattern.contains(String.valueOf(FILTER_SEP)) ? StringUtils
                .split(this.tablePattern, FILTER_SEP) : new String[] { this.tablePattern };
        for (String pat : tablePatterns) {
            // MOD zshen bug 12041: the variable trimPat must be null(not a "") if it isn't a table name.
            String trimPat = pat != null && !PluginConstant.EMPTY_STRING.equals(pat) ? pat.trim() : null;
            // ~12041

            try {
                Package pacage = tdSchema == null ? tdCatalog : tdSchema;
                // MOD gdbu 2011-9-22 TDQ-3607
                checkConnectionBeforeGetTableView();
                List<? extends NamedColumnSet> tables = DqRepositoryViewService.getTables(getConnection(), getDataManager(),
                        pacage, trimPat, true, false);
                // ~TDQ-3607
                for (NamedColumnSet t : tables) {
                    if (this.getMonitor() != null) {
                        StringBuilder taskName = new StringBuilder();
                        if (catName != null) {
                            taskName.append(Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementCatalog", catName)) //$NON-NLS-1$
                                    .append(", "); //$NON-NLS-1$
                        }
                        if (schemaName != null) {
                            taskName.append(Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementSchema", schemaName)) //$NON-NLS-1$
                                    .append(", "); //$NON-NLS-1$
                        }
                        taskName.append(Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementTable", t.getName())); //$NON-NLS-1$
                        this.getMonitor().setTaskName(
                                Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElement", taskName.toString())); //$NON-NLS-1$
                    }
                    tableCount++;
                    evalAllCounts(catName, schemaName, t, schemaIndic, true, ok);
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        schemaIndic.setTableCount(tableCount);

        // do the same for views
        int viewCount = 0;
        final String[] viewPatterns = viewPattern != null && viewPattern.contains(String.valueOf(FILTER_SEP)) ? StringUtils
                .split(this.viewPattern, FILTER_SEP) : new String[] { this.viewPattern };
        for (String pat : viewPatterns) {
            // MOD zshen bug 12041: the variable trimPat must be null(not a "") if it isn't a view name.
            String trimPat = pat != null && !PluginConstant.EMPTY_STRING.equals(pat) ? pat.trim() : null;
            // ~12041

            try {
                Package pacage = tdSchema == null ? tdCatalog : tdSchema;
                // MOD gdbu 2011-9-22 TDQ-3607
                checkConnectionBeforeGetTableView();
                List<? extends NamedColumnSet> views = DqRepositoryViewService.getViews(getConnection(), getDataManager(),
                        pacage, trimPat, true, false);
                // ~TDQ-3607
                for (NamedColumnSet t : views) {
                    viewCount++;
                    evalAllCounts(catName, schemaName, t, schemaIndic, false, ok);
                }
            } catch (Exception e) {

                log.error(e, e);
            }
        }
        schemaIndic.setViewCount(viewCount);

        if (hasCatalog && hasSchema && catalogIndic != null) {
            // add it to list of indicators
            this.addToConnectionIndicator(catalogIndic, schemaIndic);

            // add it to list of indicators
            catalogIndic.addSchemaIndicator(schemaIndic);

            // --- increment values of catalog indicator
            catalogIndic.setTableCount(catalogIndic.getTableCount() + tableCount);
            catalogIndic.setTableRowCount(catalogIndic.getTableRowCount() + schemaIndic.getTableRowCount());
            catalogIndic.setViewRowCount(catalogIndic.getViewRowCount() + schemaIndic.getViewRowCount());
            // Added 20130221 TDQ-6546: add the missed the view count
            catalogIndic.setViewCount(catalogIndic.getViewCount() + schemaIndic.getViewCount());
            // Added 20130401 TDQ-6823: add the missed key and index count for the catelog(which contains schemas)
            catalogIndic.setKeyCount(catalogIndic.getKeyCount() + schemaIndic.getKeyCount());
            catalogIndic.setIndexCount(catalogIndic.getIndexCount() + schemaIndic.getIndexCount());
        } else if (!hasCatalog) { // has schema only
            // add it to list of indicators
            this.addToConnectionIndicator(schemaIndic);
        } else if (!hasSchema) { // has catalog only
            if (SchemaPackage.eINSTANCE.getCatalogIndicator().equals(schemaIndic.eClass())) {
                this.addToConnectionIndicator(schemaIndic);
            } else {
                log.error(Messages.getString("AbstractSchemaEvaluator.NoCatalogSchema")); //$NON-NLS-1$
            }
        }
    }

    protected void addToConnectionIndicator(Indicator indicator) {
        // does nothing: implemented only in ConnectionEvaluator
    }

    /*
     * if it has catalog and schema, the table/view count of connectionIndicator is all tables from each schema.
     */
    protected void addToConnectionIndicator(CatalogIndicator catalogIndicator, SchemaIndicator schemaIndicator) {
        // does nothing: implemented only in ConnectionEvaluator
    }

    protected void resetCounts(final Indicator indicator) {
        if (indicator != null) {
            boolean reset = indicator.reset();
            if (log.isDebugEnabled()) {
                log.debug("connection indicator reset: " + reset); //$NON-NLS-1$
            }
        }
    }

    /**
     * Getter for tablePattern.
     * 
     * @return the tablePattern
     */
    public String getTablePattern() {
        return this.tablePattern;
    }

    /**
     * Sets the tablePattern.
     * 
     * @param tablePattern the tablePattern to set
     */
    public void setTablePattern(String tablePattern) {
        this.tablePattern = tablePattern;
    }

    /**
     * Getter for viewPattern.
     * 
     * @return the viewPattern
     */
    public String getViewPattern() {
        return this.viewPattern;
    }

    /**
     * Sets the viewPattern.
     * 
     * @param viewPattern the viewPattern to set
     */
    public void setViewPattern(String viewPattern) {
        this.viewPattern = viewPattern;
    }

    /**
     * Getter for catalogPattern.
     * 
     * @return the catalogPattern
     */
    public String getCatalogPattern() {
        return this.catalogPattern;
    }

    /**
     * Sets the catalogPattern.
     * 
     * @param catalogPattern the catalogPattern to set
     */
    public void setCatalogPattern(String catalogPattern) {
        this.catalogPattern = catalogPattern;
    }

    /**
     * Getter for schemaPattern.
     * 
     * @return the schemaPattern
     */
    public String getSchemaPattern() {
        return this.schemaPattern;
    }

    /**
     * Sets the schemaPattern.
     * 
     * @param schemaPattern the schemaPattern to set
     */
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    /**
     * * yyi 2009-11-30 10187 check catalog is exist in DB.
     * 
     * @param catName
     * @return
     */
    protected boolean checkCatalog(String catName) {
        if (catalogsName.isEmpty()) {
            Collection<Catalog> catalogs = ConnectionHelper.getAllCatalogs(getDataManager());
            for (Catalog tc : catalogs) {
                catalogsName.add(tc.getName());
            }
        }

        if (!catalogsName.contains(catName)) {
            return false;
        }

        return true;
    }

    /**
     * yyi 2009-11-30 10187 check schema is exist in DB.
     * 
     * @param catName
     * @return
     */
    protected boolean checkSchemaByName(String catName) {
        if (schemasName.isEmpty()) {
            for (Schema ts : SchemaHelper.getSchemas(getDataManager().getDataPackage())) {
                schemasName.add(ts.getName());
            }
        }
        if (schemasName.contains(catName)) {
            return true;
        }
        return false;
    }

    /**
     * yyi 2009-11-30 10187 check schema is exist in DB.
     * 
     * @param catName
     * @return
     */
    protected boolean checkSchema(Schema schema) {
        EObject container = schema.eContainer();
        if (container != null) {
            Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(container);
            if (catalog != null) {
                try {
                    getConnection().setCatalog(catalog.getName());
                    List<Schema> schemas = MetadataFillFactory.getDBInstance(getDataManager()).fillSchemaToCatalog(
                            getDataManager(), getConnection().getMetaData(), catalog, null);
                    if (schemas != null) {
                        for (Schema tdSchema : schemas) {
                            if (tdSchema.getName().equals(schema.getName())) {
                                return true;
                            }
                        }
                    }
                    return false;
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
        return checkSchemaByName(schema.getName());
    }

    /*
     * Check DB connection is exist in Metadata
     * 
     * @see org.talend.dq.indicators.Evaluator#checkConnection()
     */
    @Override
    protected ReturnCode checkConnection() {
        ReturnCode rc = super.checkConnection();
        if (!rc.isOk()) {
            return rc;
        }

        // MOD qiongli 2010-9-17，bug 15525
        // MOD qiongli 2010-12-24,bug 17671,avoid NPE
        if (this.getDataManager() == null) {
            rc.setReturnCode(Messages.getString("Evaluator.NoConnectionFoundInMetadata"), false); //$NON-NLS-1$
            return rc;
        }
        return rc;
    }
}
