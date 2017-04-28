// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.AllDataProfilerCoreTests;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dq.analysis.AnalysisBuilder;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.dq.helper.ParameterUtil;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.ui.model.ProjectNodeHelper;
import org.talend.utils.exceptions.TalendException;
import org.talend.utils.properties.PropertiesLoader;
import org.talend.utils.properties.TypedProperties;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MultiColAnalysisCreationTest {

    private static final String ANALYSIS_NAME = "My_test_MultiColAnalysis"; //$NON-NLS-1$\

    private static final String DATA_PROVIDER_NAME = "My_data_provider_MultiColAnalysis"; //$NON-NLS-1$\

    private static Logger log = Logger.getLogger(MultiColAnalysisCreationTest.class);

    private AnalysisBuilder analysisBuilder;

    private static final boolean GRAPHICALTEST = true;

    private static final String[] COLUMNS = GRAPHICALTEST ? new String[] { "position_title", "gender", "management_role", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            "salary" } : new String[] { "position_title", "gender", "management_role", "salary" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    private static final String[] NUMERICFUNC = GRAPHICALTEST ? new String[] { "SUM({0})", "COUNT({0})", "SUM(ISNULL({0}))" } //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            : new String[] { "AVG({0})", "SUM(ISNULL({0}))", "COUNT({0})", "MIN({0})" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String CATALOG = "tbi"; //$NON-NLS-1$

    private static final String TABLE = "employee"; //$NON-NLS-1$

    private ColumnSetMultiValueIndicator columnSetMultiValueIndicator;

    /**
     * Getter for columnSetMultiValueIndicator.
     * 
     * @return the columnSetMultiValueIndicator
     */
    public ColumnSetMultiValueIndicator getColumnSetMultiValueIndicator() {
        return this.columnSetMultiValueIndicator;
    }

    /**
     * Sets the columnSetMultiValueIndicator.
     * 
     * @param columnSetMultiValueIndicator the columnSetMultiValueIndicator to set
     */
    public void setColumnSetMultiValueIndicator(ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        this.columnSetMultiValueIndicator = columnSetMultiValueIndicator;
    }

    @Before
    public void setUp() throws Exception {
        if (DQStructureManager.getInstance().isNeedCreateStructure()) {
            DQStructureManager.getInstance().createDQStructure();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * DOC scorreia Comment method "run".
     * 
     * @throws TalendException
     */
    @Ignore
    @Test
    public void run() throws TalendException {
        // analysisBuilder = new AnalysisBuilder();
        //
        // boolean analysisInitialized = analysisBuilder.initializeAnalysis(ANALYSIS_NAME, AnalysisType.MULTIPLE_COLUMN);
        //        Assert.assertTrue(ANALYSIS_NAME + " failed to initialize!", analysisInitialized); //$NON-NLS-1$
        //
        // // get the connection
        // Connection dataManager = getDataManager();
        //        Assert.assertNotNull("No datamanager found!", dataManager); //$NON-NLS-1$
        // analysisBuilder.setAnalysisConnection(dataManager);
        //
        // // get a column to analyze
        // List<TdColumn> columns = new ArrayList<TdColumn>();
        // try {
        // columns.addAll(getColumns(dataManager));
        // } catch (Exception e) {
        // log.error(e, e);
        // }
        // ColumnSetMultiValueIndicator indicator = getIndicator(columns);
        // this.setColumnSetMultiValueIndicator(indicator);
        // for (TdColumn tdColumn : columns) {
        // analysisBuilder.addElementToAnalyze(tdColumn, indicator);
        // }
        //
        // // TODO scorreia save domain with analysisbuilder?
        // FolderProvider folderProvider = new FolderProvider();
        // folderProvider.setFolderResource(ResourceManager.getAnalysisFolder());
        //
        // // run analysis
        // Analysis analysis = analysisBuilder.getAnalysis();
        //
        // IAnalysisExecutor exec = new MultiColumnAnalysisExecutor();
        // ReturnCode executed = exec.execute(analysis);
        //        Assert.assertTrue("Problem executing analysis: " + ANALYSIS_NAME + ": " + executed.getMessage(), executed.isOk()); //$NON-NLS-1$ //$NON-NLS-2$
        //
        // // assert before create: the folder is empty
        // File[] listFiles = folderProvider.getFolder().listFiles();
        // // Assert.assertTrue(listFiles == null || listFiles.length == 0);
        //
        // // save data provider
        // ElementWriterFactory.getInstance().createDataProviderWriter().create(dataManager, folderProvider.getFolderResource());
        // // save analysis
        // ElementWriterFactory.getInstance().createAnalysisWrite().create(analysis, folderProvider.getFolderResource());
        //
        // // assert after create
        // boolean dataProviderOK = false;
        // boolean analysisOK = false;
        // listFiles = folderProvider.getFolder().listFiles();
        // if (listFiles != null) {
        // for (File file : listFiles) {
        // String name = file.getName();
        // if (name.startsWith(DATA_PROVIDER_NAME)) {
        // dataProviderOK = true;
        // } else if (name.startsWith(ANALYSIS_NAME)) {
        // analysisOK = true;
        // }
        // }
        // }
        // Assert.assertTrue(dataProviderOK);
        // Assert.assertTrue(analysisOK);
    }

    /**
     * DOC scorreia Comment method "getIndicators".
     * 
     * @param column
     * @return
     */
    private ColumnSetMultiValueIndicator getIndicator(List<TdColumn> columns) {
        ColumnSetMultiValueIndicator ind = ColumnsetFactory.eINSTANCE.createColumnSetMultiValueIndicator();
        ind.getAnalyzedColumns().addAll(columns);

        boolean definitionSet = DefinitionHandler.getInstance().setDefaultIndicatorDefinition(ind);
        if (log.isDebugEnabled()) {
            log.debug("Definition set for " + ind.getName() + ": " + definitionSet); //$NON-NLS-1$ //$NON-NLS-2$
        }

        for (String f : NUMERICFUNC) {
            ind.getNumericFunctions().add(f);
        }

        return ind;
    }

    /**
     * DOC scorreia Comment method "getColumn".
     * 
     * @param dataManager
     * @return
     * @throws Exception
     */
    private List<TdColumn> getColumns(Connection dataManager) throws Exception {
        List<Catalog> tdCatalogs = CatalogHelper.getCatalogs(dataManager.getDataPackage());
        Catalog catalog = null;
        for (Catalog tdCatalog : tdCatalogs) {
            if (CATALOG.equals(tdCatalog.getName())) {
                catalog = tdCatalog;
                break;
            }
        }
        Assert.assertNotNull(catalog);
        Assert.assertFalse(tdCatalogs.isEmpty());
        System.out.println("analysed Catalog: " + catalog.getName()); //$NON-NLS-1$

        List<TdTable> tables = DqRepositoryViewService.getTables(dataManager, catalog, TABLE, true, false);

        // store tables in catalog
        CatalogHelper.addTables(tables, catalog);

        Assert.assertFalse(tables.isEmpty());
        TdTable tdTable = tables.get(0);
        System.out.println("analyzed Table: " + tdTable.getName()); //$NON-NLS-1$
        List<TdColumn> columns;
        columns = DqRepositoryViewService.getColumns(dataManager, tdTable, null, true);
        // MOD scorreia 2009-01-29 columns are stored in the table
        // TableHelper.addColumns(tdTable, columns);

        Assert.assertFalse(columns.isEmpty());

        List<TdColumn> usedCols = new ArrayList<TdColumn>();
        for (TdColumn tdColumn : columns) {
            for (String c : COLUMNS) {
                if (tdColumn.getName().equals(c)) {
                    usedCols.add(tdColumn);
                }
            }
        }
        // set DM type for each used column
        for (TdColumn tdColumn : usedCols) {
            final int javaType = tdColumn.getJavaType();
            if (Java2SqlType.isNumbericInSQL(javaType)) {
                MetadataHelper.setDataminingType(DataminingType.INTERVAL, tdColumn);
            } else if (Java2SqlType.isTextInSQL(javaType)) {
                MetadataHelper.setDataminingType(DataminingType.NOMINAL, tdColumn);
            }
        }
        return usedCols;
    }

    /**
     * DOC scorreia Comment method "getDataManager".
     * 
     * @return
     */
    public Connection getDataManager() {
        TypedProperties connectionParams = PropertiesLoader.getProperties(AllDataProfilerCoreTests.class, "db.properties"); //$NON-NLS-1$
        String driverClassName = connectionParams.getProperty("driver"); //$NON-NLS-1$
        String dbUrl = connectionParams.getProperty("url"); //$NON-NLS-1$
        String sqlTypeName = connectionParams.getProperty("sqlTypeName"); //$NON-NLS-1$

        DBConnectionParameter params = new DBConnectionParameter();
        params.setName("My connection"); //$NON-NLS-1$
        params.setDriverClassName(driverClassName);
        params.setJdbcUrl(dbUrl);
        params.setSqlTypeName(sqlTypeName);
        params.setParameters(connectionParams);
        params.getParameters();

        // create connection
        ConnectionUtils.setTimeout(false);

        MetadataFillFactory instance = MetadataFillFactory.getDBInstance();
        IMetadataConnection metaConnection = instance.fillUIParams(ParameterUtil.toMap(params));
        ReturnCode rc = null;
        try {
            rc = instance.checkConnection(metaConnection);
        } catch (java.lang.RuntimeException e) {
            Assert.fail("connect to " + dbUrl + "failed," + e.getMessage());
        }
        Connection dataProvider = null;
        if (rc.isOk()) {
            dataProvider = instance.fillUIConnParams(metaConnection, null);
            dataProvider.setName(DATA_PROVIDER_NAME);
            // because the DI side code is changed, modify the following code.
            metaConnection.setCurrentConnection(dataProvider);
            try {
                ProjectNodeHelper.fillCatalogAndSchemas(metaConnection, (DatabaseConnection) dataProvider);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Assert.assertNotNull(dataProvider);
        return dataProvider;
    }
}
