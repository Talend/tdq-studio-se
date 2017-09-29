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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.helpers.DataqualitySwitchHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.ConnectionIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dataquality.indicators.schema.util.SchemaSwitch;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class ConnectionEvaluator extends AbstractSchemaEvaluator<DataProvider> {

    private static Logger log = Logger.getLogger(ConnectionEvaluator.class);

    private Connection dataProvider;

    private List<Indicator> elementIndics;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.AbstractSchemaEvaluator#getDataManager()
     */
    @Override
    protected Connection getDataManager() {
        DataProvider dp = this.getAnalyzedElements().iterator().next();
        if (dp != null) {
            Connection tdp = SwitchHelpers.CONNECTION_SWITCH.doSwitch(dp);
            return tdp;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#executeSqlQuery(java.lang.String)
     * 
     * Note that the given statement is not used.
     */
    @Override
    protected ReturnCode executeSqlQuery(String sqlStatement) throws SQLException {
        // assert this.getAnalyzedElements().size() == 1 : "Invalid number of analyzed elements: "
        // + this.getAnalyzedElements().size();
        ReturnCode ok = new ReturnCode(true);
        dataProvider = this.getDataManager();

        // // --- get data provider
        // if (this.getAnalyzedElements().size() != 1) {
        // String msg = "Should have only one connection to analyze instead of " + getAnalyzedElements().size();
        // log.error(msg);
        // ok.setReturnCode(msg, false);
        // return ok;
        // }

        if (this.elementToIndicators.values().isEmpty()) {
            String msg = Messages.getString("Evaluator.NoInidcator1"); //$NON-NLS-1$
            log.error(msg);
            ok.setReturnCode(msg, false);
            return ok;
        }
        elementIndics = this.elementToIndicators.values().iterator().next();
        if (elementIndics.isEmpty()) {
            String msg = Messages.getString("Evaluator.NoInidcator2", dataProvider); //$NON-NLS-1$
            log.error(msg);
            ok.setReturnCode(msg, false);
            return ok;
        }

        ConnectionIndicator connectionIndicator = getConnectionIndicator();
        this.resetCounts(connectionIndicator);

        List<Catalog> catalogs = ConnectionHelper.getCatalogs(dataProvider);
        if (isTos(dataProvider)) {
            cleanUpCatalog(catalogs);
        }

        if (this.getMonitor() != null) {
            this.getMonitor().beginTask("Analyze catalogs", 100);
        }
        int temp = 0;
        if (catalogs.isEmpty()) { // no catalog, only schemata
            List<Schema> schemata = ConnectionHelper.getSchema(dataProvider);
            // MOD yyi 2009-11-30 10187
            for (Schema tdSchema : schemata) {
                if (!checkSchema(tdSchema)) {
                    ok.setReturnCode(Messages.getString("Evaluator.schemaNotExist", tdSchema.getName()), false); //$NON-NLS-1$
                    return ok;
                }
            }
            // ~
            // for (Schema tdSchema : schemata) {
            for (int i = 0; i < schemata.size(); i++) {
                Schema tdSchema = schemata.get(i);
                if (this.getMonitor() != null) {
                    this.getMonitor().setTaskName(
                            Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElement",
                                    Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementSchema", tdSchema.getName())));
                }
                evalSchemaIndic(tdSchema, ok);
                if (this.getMonitor() != null) {
                    int current = (i + 1) * 100 / schemata.size();
                    if (current > temp) {
                        this.getMonitor().worked(current - temp);
                        temp = current;
                    }
                }
            }
        } else { // catalogs exist
            // MOD yyi 2009-11-30 10187
            for (Catalog tdCatalog : catalogs) {
                if (!checkCatalog(tdCatalog.getName())) {
                    ok.setReturnCode(Messages.getString("Evaluator.catalogNotExist", tdCatalog.getName()), false); //$NON-NLS-1$
                    return ok;
                }
            }
            // ~
            // for (Catalog tdCatalog : catalogs) {
            for (int i = 0; i < catalogs.size(); i++) {
                if (this.continueRun()) {
                    Catalog tdCatalog = catalogs.get(i);
                    String catName = tdCatalog.getName();
                    if (this.getMonitor() != null) {
                        this.getMonitor().setTaskName(
                                Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElement",
                                        Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementCatalog", catName)));
                    }
                    if (dbms().supportCatalogSelection()) {
                        try {
                            connection.setCatalog(catName);
                        } catch (SQLException e) {
                            log.warn("Exception while executing SQL query " + sqlStatement, e); //$NON-NLS-1$  
                        }
                    }
                    CatalogIndicator catalogIndic = SchemaFactory.eINSTANCE.createCatalogIndicator();
                    // MOD xqliu 2009-1-21 feature 4715
                    DefinitionHandler.getInstance().setDefaultIndicatorDefinition(catalogIndic);
                    List<Schema> schemas = CatalogHelper.getSchemas(tdCatalog);
                    if (schemas.isEmpty()) { // no schema
                        evalCatalogIndic(catalogIndic, tdCatalog, ok);
                    } else {
                        catalogIndic.setAnalyzedElement(tdCatalog);
                        // --- create SchemaIndicator for each pair of catalog schema
                        for (Schema tdSchema : schemas) {
                            if (this.continueRun()) {
                                if (this.getMonitor() != null) {
                                    this.getMonitor().setTaskName(
                                            Messages.getString(
                                                    "ColumnAnalysisSqlExecutor.AnalyzedElement",
                                                    Messages.getString("ColumnAnalysisSqlExecutor.AnalyzedElementCatalog",
                                                            catName)
                                                            + ", "
                                                            + Messages.getString(
                                                                    "ColumnAnalysisSqlExecutor.AnalyzedElementSchema",
                                                                    tdSchema.getName())));

                                }
                                // --- create SchemaIndicator for each catalog
                                SchemaIndicator schemaIndic = SchemaFactory.eINSTANCE.createSchemaIndicator();
                                // MOD xqliu 2009-1-21 feature 4715
                                DefinitionHandler.getInstance().setDefaultIndicatorDefinition(schemaIndic);
                                evalSchemaIndicLow(catalogIndic, schemaIndic, tdCatalog, tdSchema, ok);
                            }
                        }
                        catalogIndic.setSchemaCount(schemas.size());

                    }

                    if (this.getMonitor() != null) {
                        int current = (i + 1) * 100 / catalogs.size();
                        if (current > temp) {
                            this.getMonitor().worked(current - temp);
                            temp = current;
                        }
                    }
                }
            }
            if (this.getMonitor() != null) {
                this.getMonitor().done();
            }
        }

        if (log.isDebugEnabled()) {
            printCounts(connectionIndicator);
        }
        return ok;
    }

    /**
     * if the Connection don't contain the catalog, remove the catalog from the list.
     * 
     * @param catalogs
     */
    private void cleanUpCatalog(List<Catalog> catalogs) {
        if (catalogs != null) {
            List<Catalog> temp = new ArrayList<Catalog>();
            for (Catalog catalog : catalogs) {
                if (checkCatalog(catalog.getName()) && setCatalogOk(connection, catalog.getName())) {
                    temp.add(catalog);
                }
            }
            catalogs.clear();
            catalogs.addAll(temp);
        }
    }

    /**
     * call the setCatalog() method to make sure the catalog is ok.
     * 
     * @param connection
     * @param name
     * @return
     */
    private boolean setCatalogOk(java.sql.Connection connection, String name) {
        if (connection != null) {
            try {
                if (dbms().supportCatalogSelection()) {
                    connection.setCatalog(name);
                }
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * if the connection is created by TOS(DI) return true, else return false.
     * 
     * @param conn
     * @return
     */
    private boolean isTos(Connection conn) {
        if (conn instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) conn;
            return dbConn.getUiSchema() != null;
        }
        return false;
    }

    protected void printCounts(ConnectionIndicator connectionIndicator) {
        if (connectionIndicator == null) {
            return;
        }
        log.info("nb table= " + connectionIndicator.getTableCount()); //$NON-NLS-1$
        log.info("nb views= " + connectionIndicator.getViewCount()); //$NON-NLS-1$
        log.info("nb index= " + connectionIndicator.getIndexCount()); //$NON-NLS-1$
        log.info("nb PK= " + connectionIndicator.getKeyCount()); //$NON-NLS-1$
        log.info("total table row count= " + connectionIndicator.getTableRowCount()); //$NON-NLS-1$
        log.info("total view row count= " + connectionIndicator.getViewRowCount()); //$NON-NLS-1$
    }

    @Override
    protected void addToConnectionIndicator(Indicator indicator) {
        final ConnectionIndicator connectionIndicator = getConnectionIndicator();
        if (connectionIndicator == null) {
            return;
        }
        SchemaSwitch<Boolean> schemaSwitch = new SchemaSwitch<Boolean>() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.dataquality.indicators.schema.util.SchemaSwitch#caseCatalogIndicator(org.talend.dataquality
             * .indicators.schema.CatalogIndicator)
             */
            @Override
            public Boolean caseCatalogIndicator(CatalogIndicator object) {
                connectionIndicator.addCatalogIndicator(object);
                connectionIndicator.setCatalogCount(connectionIndicator.getCatalogCount() + 1);
                // increment schema count
                connectionIndicator.setSchemaCount(connectionIndicator.getSchemaCount() + object.getSchemaCount());

                // MOD scorreia 2009-01-16 increment other counts
                incrementCounts(connectionIndicator, object);

                return true;
            }

            private void incrementCounts(final ConnectionIndicator connIndicator, SchemaIndicator childIndicator) {
                connIndicator.setTableCount(connIndicator.getTableCount() + childIndicator.getTableCount());
                connIndicator.setTableRowCount(connIndicator.getTableRowCount() + childIndicator.getTableRowCount());
                // MOD klliu 2011-12-26 bug TDQ-4235
                connIndicator.setViewCount(connIndicator.getViewCount() + childIndicator.getViewCount());
                connIndicator.setViewRowCount(connIndicator.getViewRowCount() + childIndicator.getViewRowCount());
                connIndicator.setKeyCount(connIndicator.getKeyCount() + childIndicator.getKeyCount());
                connIndicator.setIndexCount(connIndicator.getIndexCount() + childIndicator.getIndexCount());
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.dataquality.indicators.schema.util.SchemaSwitch#caseSchemaIndicator(org.talend.dataquality
             * .indicators.schema.SchemaIndicator)
             */
            @Override
            public Boolean caseSchemaIndicator(SchemaIndicator object) {
                connectionIndicator.addSchemaIndicator(object);
                connectionIndicator.setSchemaCount(connectionIndicator.getSchemaCount() + 1);

                // MOD scorreia 2009-01-16 increment other counts
                incrementCounts(connectionIndicator, object);
                return true;
            }

        };
        schemaSwitch.doSwitch(indicator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.indicators.AbstractSchemaEvaluator#addToConnectionIndicator(org.talend.dataquality.indicators.schema
     * .CatalogIndicator, org.talend.dataquality.indicators.schema.SchemaIndicator)
     */
    @Override
    protected void addToConnectionIndicator(CatalogIndicator catalogIndicator, SchemaIndicator schemaIndicator) {
        final ConnectionIndicator connectionIndicator = getConnectionIndicator();
        if (connectionIndicator == null) {
            return;
        }

        boolean addSuccess = connectionIndicator.addCatalogIndicator(catalogIndicator);
        // only increment catalog/schema count when the catalogIndicator is a new instance and is added successfully.
        if (addSuccess) {
            connectionIndicator.setCatalogCount(connectionIndicator.getCatalogCount() + 1);
            connectionIndicator.setSchemaCount(connectionIndicator.getSchemaCount() + catalogIndicator.getSchemaCount());
        }

        // set table count view count... from schemaIndicator not from catalogIndicator.
        connectionIndicator.setTableCount(connectionIndicator.getTableCount() + schemaIndicator.getTableCount());
        connectionIndicator.setTableRowCount(connectionIndicator.getTableRowCount() + schemaIndicator.getTableRowCount());
        connectionIndicator.setViewCount(connectionIndicator.getViewCount() + schemaIndicator.getViewCount());
        connectionIndicator.setViewRowCount(connectionIndicator.getViewRowCount() + schemaIndicator.getViewRowCount());
        connectionIndicator.setKeyCount(connectionIndicator.getKeyCount() + schemaIndicator.getKeyCount());
        connectionIndicator.setIndexCount(connectionIndicator.getIndexCount() + schemaIndicator.getIndexCount());
    }

    /**
     * DOC scorreia Comment method "getConnectionIndicator".
     * 
     * @param dataProvider
     * @param indics
     * @param ok
     * @return
     */
    private ConnectionIndicator getConnectionIndicator() {
        ConnectionIndicator connectionIndicator = DataqualitySwitchHelper.CONNECTION_SWITCH.doSwitch(elementIndics.get(0));
        if (connectionIndicator != null) {
            connectionIndicator.setAnalyzedElement(dataProvider);
        }
        return connectionIndicator;
    }

}
