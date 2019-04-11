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
package org.talend.dq.analysis;

import java.lang.management.ManagementFactory;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.ValueIndicator;
import org.talend.dataquality.indicators.mapdb.MapDBManager;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.analysis.memory.AnalysisThreadMemoryChangeNotifier;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.Evaluator;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.hive.handler.HiveConnectionHandler;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public abstract class AnalysisExecutor implements IAnalysisExecutor {

    private static Logger log = Logger.getLogger(AnalysisExecutor.class);

    protected Boolean parallelExeStatus = Boolean.TRUE;

    protected static final boolean POOLED_CONNECTION = Boolean.TRUE;

    /**
     * use {@link #dbms()} to access this attribute.
     */
    private DbmsLanguage dbmsLanguage;

    protected Analysis cachedAnalysis;

    private long usedMemory;

    private volatile boolean isLowMemory = false;

    private long checkContinueCount = 0L;

    private boolean keepRunning = true;

    private StringBuffer errorMessage = new StringBuffer();

    // the value of monitor worked for executing all indicators
    protected int compIndicatorsWorked = 60;

    /**
     * Getter for usedMemory.
     * 
     * @return the usedMemory
     */
    public long getUsedMemory() {
        return this.usedMemory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.IAnalysisExecutor#execute(org.talend.dataquality.analysis.Analysis)
     */
    public ReturnCode execute(final Analysis analysis) {
        assert analysis != null;
        // --- preconditions
        if (!check(analysis)) {
            AnalysisExecutorHelper.setExecutionInfoInAnalysisResult(analysis, false, getErrorMessage());
            return getReturnCode(false);
        }

        // --- creation time
        final long startime = AnalysisExecutorHelper.setExecutionDateInAnalysisResult(analysis);

        // MOD qiongli 2012-3-14 TDQ-4433,if import from low vesion and not import SystemIdicator,should initionlize
        // these indicator.
        initializeIndicators(analysis);
        // --- create SQL statement
        String sql = createSqlStatement(analysis);
        if (sql == null) {
            AnalysisExecutorHelper.setExecutionInfoInAnalysisResult(analysis, false, getErrorMessage());
            return getReturnCode(false);
        }

        // ADD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
        // note: this feature is not supported now, if support, delete this
        if (!StringUtils.isEmpty(getErrorMessage())) {
            if ("EXCEL".equals(dbms().getDbmsName())) { //$NON-NLS-1$
                return getReturnCode(true);
            }
            AnalysisExecutorHelper.setExecutionInfoInAnalysisResult(analysis, false, getErrorMessage());
            return getReturnCode(false);
        }
        // ~

        // --- run analysis
        boolean ok = false;
        try { // catch any exception
            if (this.continueRun()) {
                // before run analysis need to clear old DB
                if (AnalysisHelper.isJavaExecutionEngine(analysis)) {
                    MapDBManager.getInstance().deleteDB(analysis);
                }
                ok = runAnalysis(analysis, sql);
            }

        } catch (Exception e) {
            ok = false;
            log.error(e, e);
        } finally {
            // ADD msjian TDQ-5952: we should close connections always.
            // after run analysis, close connection at once when don't need it
            TdqAnalysisConnectionPool.closeConnectionPool(analysis);
            // TDQ-5952~
            if (AnalysisHelper.isJavaExecutionEngine(analysis)) {
                MapDBManager.getInstance().closeDB(analysis);
            }
        }

        if (getMonitor() != null) {
            monitor.subTask(Messages.getString("AnalysisExecutor.UpdateAnalysisResut")); //$NON-NLS-1$
        }

        // MOD TDQ-8374 when the setExecutionNumberInAnalysisResult return message is null, should not make the null
        // overwrite the current error messages
        if (isLowMemory) {
            setError(Messages.getString("Evaluator.OutOfMomory", usedMemory));//$NON-NLS-1$
        }

        // --- set metadata information of analysis
        AnalysisExecutorHelper.setExecutionInfoInAnalysisResult(analysis, ok, getErrorMessage());

        // --- compute execution duration
        if (this.continueRun()) {
            long endtime = System.currentTimeMillis();
            final ExecutionInformations resultMetadata = analysis.getResults().getResultMetadata();
            resultMetadata.setExecutionDuration((int) (endtime - startime));

            // MOD qiongli 2010-8-10, feature 14252
            EList<Indicator> indicatorLs = analysis.getResults().getIndicators();
            resultMetadata.setOutThreshold(false);
            for (Indicator indicator : indicatorLs) {
                if (hasOutThreshold(indicator)) {
                    resultMetadata.setOutThreshold(true);
                    break;
                }
            }// ~
        }

        if (getMonitor() != null) {
            getMonitor().worked(10);
        }

        return new ReturnCode(getErrorMessage(), ok);
    }

    /**
     * 
     * DOC qiongli Comment method "hasOutThreshold".
     * 
     * @param indicator
     * @return
     */
    private boolean hasOutThreshold(Indicator indicator) {
        String[] indicatorThreshold = IndicatorHelper.getIndicatorThreshold(indicator);
        String[] indiPercentThreshold = IndicatorHelper.getIndicatorThresholdInPercent(indicator);
        Object obj = IndicatorCommonUtil.getIndicatorValue(indicator);
        if (indicatorThreshold != null || indiPercentThreshold != null) {
            // MOD qiongli 2011-11-15 TDQ-3690 avoid String "null",and get the value for ValueIndicator to transfer.
            if (obj != null && !PluginConstant.EMPTY_STRING.equals(obj.toString()) && !"null".equalsIgnoreCase(obj.toString())) { //$NON-NLS-1$
                String value = PluginConstant.EMPTY_STRING;
                if (indicator instanceof ValueIndicator) {
                    value = ((ValueIndicator) indicator).getValue();
                }
                ChartDataEntity chartDataEntity = new ChartDataEntity(indicator, PluginConstant.EMPTY_STRING, value);
                if (obj instanceof PatternMatchingExt) {
                    obj = (((PatternMatchingExt) obj).getMatchingValueCount());
                }
                if (chartDataEntity.isOutOfRange(obj.toString())) {
                    return true;
                }
            }
        }
        List<Indicator> leaves = IndicatorHelper.getIndicatorLeaves(indicator);
        if (leaves.size() > 0 && !leaves.get(0).equals(indicator)) {
            for (Indicator leaveIndicator : leaves) {
                if (hasOutThreshold(leaveIndicator)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method "createSqlStatement".
     * 
     * @param analysis the analysis from which the SQL will be generated
     * @return the generated SQL statement or null if problem
     */
    protected abstract String createSqlStatement(Analysis analysis);

    /**
     * Method "getReturnCode".
     * 
     * @param ok
     * @return a return code with the last error message
     */
    protected ReturnCode getReturnCode(boolean ok) {
        return ok ? new ReturnCode() : new ReturnCode(getErrorMessage(), false);
    }

    protected boolean check(Analysis analysis) {
        ReturnCode rc = AnalysisExecutorHelper.check(analysis);
        setError(rc.getMessage());
        return rc.isOk();
    }

    /**
     * Method "runAnalysis". Mod 20130314 TDQ-5973 refactor to extract same part here
     * 
     * @param analysis the analysis to be run
     * @param sqlStatement the sql statement to execute on Database
     * @return true if ok
     */
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        // Open connection
        TypedReturnCode<java.sql.Connection> connection = this.getConnectionBeforeRun(analysis);
        if (!connection.isOk()) {
            traceError(connection.getMessage());
            return Boolean.FALSE;
        }

        // abstract method
        ReturnCode rc = evaluate(analysis, connection.getObject(), sqlStatement);

        // close connection
        ReturnCode rcon = closeConnection(analysis, connection.getObject());
        if (!rc.isOk()) {
            traceError(rc.getMessage());
        }

        return rc.isOk() && rcon.isOk();
    }

    protected abstract ReturnCode evaluate(Analysis analysis, java.sql.Connection connection, String sqlStatement);

    /**
     * get the connection before evaluating, same for all sub classes
     * 
     * @param analysis
     * @return the connection(pooled or not)
     */
    protected TypedReturnCode<java.sql.Connection> getConnectionBeforeRun(Analysis analysis) {
        // open a connection
        TypedReturnCode<java.sql.Connection> connection = null;
        if (POOLED_CONNECTION) {
            // reset the connection pool before run this analysis
            resetConnectionPool(analysis);
            connection = getPooledConnection(analysis);
        } else {
            connection = getConnection(analysis);
        }

        return connection;

    }

    /**
     * close the connection for the analysis after running.
     * 
     * @param analysis
     * @param connection
     * @return close success or not
     */
    protected ReturnCode closeConnection(Analysis analysis, java.sql.Connection connection) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        if (connection != null) {
            if (POOLED_CONNECTION) {
                resetConnectionPool(analysis);
            } else {
                rc = ConnectionUtils.closeConnection(connection);
                if (!rc.isOk()) {
                    setError(rc.getMessage());
                }
            }
        } else {
            rc.setOk(Boolean.FALSE);
            rc.setMessage(Messages.getString("AnalysisExecutor.ConnectionNull", analysis.getName())); //$NON-NLS-1$
            log.error(rc.getMessage());
        }
        return rc;
    }

    /**
     * Set the error.
     * 
     * @param error to be set.
     */
    protected void setError(String error) {
        if (!StringUtils.isEmpty(error)) {
            errorMessage = new StringBuffer(error);
        } else {
            errorMessage = new StringBuffer(PluginConstant.EMPTY_STRING);
        }
    }

    /**
     * 
     * Append the error to error message buffer.
     * 
     * @param error the error message to be appended.
     */
    protected void appendError(String error) {
        if (!StringUtils.isEmpty(errorMessage.toString())) {
            errorMessage.append(PluginConstant.ENTER_STRING);
        }
        this.errorMessage.append(error);

    }

    protected String getErrorMessage() {
        return errorMessage.toString();
    }

    /**
     * Method "traceError".
     * 
     * @param error the message to set in errorMessage
     */
    protected void traceError(String error) {
        log.error(error);
        setError(error);
    }

    /**
     * DOC scorreia Comment method "getConnection".
     * 
     * @param analysis
     * @param schema
     * @return
     */
    protected TypedReturnCode<java.sql.Connection> getConnection(Analysis analysis) {
        // MODSCA 2008-03-25 scorreia schema is not used. removed (was used before to select the catalog of the db)
        // now it is done elsewhere
        TypedReturnCode<java.sql.Connection> rc = new TypedReturnCode<java.sql.Connection>();

        DataManager datamanager = analysis.getContext().getConnection();
        if (datamanager == null) {
            rc.setReturnCode(Messages.getString("AnalysisExecutor.DataManagerNull", analysis.getName()), false); //$NON-NLS-1$
            return rc;
        }
        if (datamanager != null && datamanager.eIsProxy()) {
            datamanager = (DataManager) EObjectHelper.resolveObject(datamanager);
        }
        Connection dataprovider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(datamanager);
        if (dataprovider == null) {
            rc.setReturnCode(Messages.getString("AnalysisExecutor.DataProviderNull", datamanager.getName(), //$NON-NLS-1$
                    analysis.getName()), false);
            return rc;
        }

        // create hive connection
        IMetadataConnection metadataConnection = ConvertionHelper.convert(dataprovider);
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
            try {
                HiveConnectionHandler hiveConnHandler = HiveConnectionManager.getInstance().createHandler(metadataConnection);
                java.sql.Connection createConnection = hiveConnHandler.createHiveConnection();
                rc.setOk(true);
                rc.setObject(createConnection);
            } catch (ClassNotFoundException e) {
                log.error(e);
                rc.setOk(false);
            } catch (InstantiationException e) {
                log.error(e);
                rc.setOk(false);
            } catch (IllegalAccessException e) {
                log.error(e);
                rc.setOk(false);
            } catch (SQLException e) {
                log.error(e);
                rc.setOk(false);
            }
            return rc;
        } else {
            // create other type connection
            TypedReturnCode<java.sql.Connection> connection = JavaSqlFactory.createConnection(dataprovider);
            if (!connection.isOk()) {
                rc.setReturnCode(connection.getMessage(), false);
                return rc;
            }

            rc.setObject(connection.getObject());
            return rc;
        }
    }

    /**
     * let the connection pool go back to its original state.
     * 
     * @param analysis
     */
    protected void resetConnectionPool(Analysis analysis) {
        TdqAnalysisConnectionPool.closeConnectionPool(analysis);
    }

    /**
     * DOC xqliu Comment method "getPooledConnection".
     * 
     * @param analysis
     * @param dataProvider
     * @return
     */
    protected TypedReturnCode<java.sql.Connection> getPooledConnection(Analysis analysis) {
        TypedReturnCode<java.sql.Connection> rc = new TypedReturnCode<java.sql.Connection>();
        java.sql.Connection pooledConnection = null;

        try {
            pooledConnection = TdqAnalysisConnectionPool.getConnectionPool(analysis).getConnection();
        } catch (Exception e) {
            log.debug(e, e);
        }

        if (pooledConnection == null) {
            rc.setReturnCode(Messages.getString("AnalysisExecutor.NoUseableConnection"), false); //$NON-NLS-1$
            return rc;
        }

        // else ok
        rc.setObject(pooledConnection);
        return rc;
    }

    /**
     * DOC xqliu Comment method "getAnalysisDataProvider".
     * 
     * @param analysis
     * @return
     */
    protected Connection getAnalysisDataProvider(Analysis analysis) {
        DataManager datamanager = analysis.getContext().getConnection();
        if (datamanager != null && datamanager.eIsProxy()) {
            datamanager = (DataManager) EObjectHelper.resolveObject(datamanager);
        }
        return SwitchHelpers.CONNECTION_SWITCH.doSwitch(datamanager);
    }

    private IProgressMonitor monitor;

    public IProgressMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    // FIXME this method is the same as Evaluator.continueRun(). Factorize code.
    protected boolean continueRun() {
        // MOD scorreia 2013-09-10 avoid checking for each analyzed row. Check only every 1000 rows
        checkContinueCount++;
        if (getMonitor() != null && getMonitor().isCanceled()) {
            keepRunning = false;
            return keepRunning;
        }
        if (getCheckContinueCount() % Evaluator.CHECK_EVERY_N_COUNT != 0) {
            return keepRunning;
        }
        if (!Platform.isRunning()) { // Reporting engine is working as library
            return true;
        }

        if (this.isLowMemory) {
            keepRunning = false;
        } else if (AnalysisThreadMemoryChangeNotifier.getInstance().isUsageThresholdExceeded()) {
            this.usedMemory = AnalysisThreadMemoryChangeNotifier.convertToMB(ManagementFactory.getMemoryMXBean()
                    .getHeapMemoryUsage().getUsed());
            this.isLowMemory = true;
            keepRunning = false;
        }
        return keepRunning;
    }

    /**
     * Getter for checkContinueCount.
     * 
     * @return the checkContinueCount
     */
    public long getCheckContinueCount() {
        return this.checkContinueCount;
    }

    /**
     * Method "getCatalogName".
     * 
     * @param analyzedElement
     * @return the catalog or schema quoted name
     */
    // MOD yyi 2011-02-22 17871:delimitefile
    protected String getQuotedCatalogName(ModelElement analyzedElement) {
        return AnalysisExecutorHelper.getQuotedCatalogName(analyzedElement, dbms());
    }

    /**
     * DOC scorreia Comment method "getSchemaName".
     * 
     * @param columnSetOwner
     * @return
     */
    protected String getQuotedSchemaName(ModelElement columnSetOwner) {
        return AnalysisExecutorHelper.getQuotedSchemaName(columnSetOwner, dbms());
    }

    /**
     * Method "dbms".
     * 
     * @return the DBMS language (not null)
     */
    protected DbmsLanguage dbms() {
        if (this.dbmsLanguage == null) {
            this.dbmsLanguage = createDbmsLanguage();
        }
        return this.dbmsLanguage;
    }

    DbmsLanguage createDbmsLanguage() {
        DataManager connection = this.cachedAnalysis.getContext().getConnection();
        return DbmsLanguageFactory.createDbmsLanguage(connection);
    }

    /**
     * Method "quote".
     * 
     * @param input
     * @return the given string between quotes (for SQL)
     */
    protected String quote(String input) {
        return dbms().quote(input);
    }

    /**
     * 2012-3-14 TDQ-4433,reset indicatorDefinition for indicator if needed(indicatorDefinition is null or proxy).
     */
    protected void initializeIndicators(Analysis analysis) {
        ModelElementAnalysisHandler modHandler = new ModelElementAnalysisHandler();
        for (Indicator ind : analysis.getResults().getIndicators()) {
            modHandler.initializeIndicator(ind);
        }

        // ADD msjian TDQ-8202 2013-11-19: when import a judi indicator, should update JUDIs For Analysis.
        // else when the user do generate report directly, will can not load udi jars
        UDIHelper.updateJUDIsForAnalysis(analysis);
        // TDQ-8202~
    }

    /**
     * return true if the connection need to set the catalog, otherwise return false.
     * <p>
     * The list of databases which don't need to change the catalog:
     * <p>
     * Odbc Mssql
     * <p>
     * Odbc Oracle
     * <p>
     * Odbc Progress
     * <p>
     * Odbc Teradata
     * <p>
     * Exasol
     * <p>
     * Hive
     * <p>
     * Mysql: because there will generate the sql statement with fully qualified name before the table name,so no need to change
     * the catalog of the connection here
     * <p>
     * 
     * @return need to change the catalog or not
     */
    protected boolean needChangeCatalog(java.sql.Connection connection) {
        boolean result = true;
        try {
            DatabaseMetaData metadata = ExtractMetaDataUtils.getInstance().getConnectionMetadata(connection);
            result = !(ConnectionUtils.isOdbcMssql(connection) || ConnectionUtils.isOdbcOracle(connection)
                    || ConnectionUtils.isOdbcProgress(connection) || ConnectionUtils.isOdbcTeradata(connection)
                    || org.talend.utils.sql.ConnectionUtils.isExasol(metadata)
                    // TDQ-16610 msjian: snowflake no need to change catalog to PUBLIC.
                    || org.talend.utils.sql.ConnectionUtils.isSnowflake(metadata)
                    || ExtractMetaDataUtils.getInstance().isHiveConnection(connection) || ConnectionUtils.isMysql(connection));
        } catch (TalendRuntimeException e) {
            traceError(e.getMessage());
            result = Boolean.FALSE;
        } catch (SQLException e) {
            traceError(e.getMessage());
            result = Boolean.FALSE;
        }
        return result;
    }

    /**
     * change the connection's catalog, need to call needChangeCatalog() before call this method
     * 
     * @return true if change catalog successful; false if failed to change catalog
     */
    protected boolean changeCatalog(String catalogName, java.sql.Connection connection) {
        try {
            connection.setCatalog(catalogName);
            return true;
        } catch (TalendRuntimeException e) {
            traceError(Messages.getString("ColumnAnalysisSqlExecutor.ERRORWHENSETCATALOG", catalogName, e.getMessage()));//$NON-NLS-1$
            return Boolean.FALSE;
        } catch (SQLException e) {
            traceError(Messages.getString("ColumnAnalysisSqlExecutor.ERRORWHENSETCATALOGSQL", catalogName, e.getMessage()));//$NON-NLS-1$
            return Boolean.FALSE;
        }
    }
}
