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

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.memory.AnalysisThreadMemoryChangeNotifier;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.utils.collections.MultiMapHelper;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author scorreia
 * 
 * Abstract class for computing indicators.
 * @param <T> the type of the object identifying the analyzed element (usually a string).
 * 
 */
public abstract class Evaluator<T> {

    private static Logger log = Logger.getLogger(Evaluator.class);

    public static final int CHECK_EVERY_N_COUNT = Integer.valueOf(System.getProperty("talend.analysis.memory.check", //$NON-NLS-1$
            "1000")); //$NON-NLS-1$

    private long checkContinueCount = 0L;

    private volatile boolean isLowMemory = false;

    private long usedMemory;

    protected Connection connection;

    private boolean pooledConnection;

    public boolean isPooledConnection() {
        return this.pooledConnection;
    }

    public void setPooledConnection(boolean pooledConnection) {
        this.pooledConnection = pooledConnection;
    }

    protected int fetchSize = 0;

    protected Map<T, List<Indicator>> elementToIndicators = new HashMap<T, List<Indicator>>();

    protected Set<Indicator> allIndicators = new HashSet<Indicator>();

    private String javaPatternMessage = StringUtils.EMPTY;

    protected Analysis analysis = null;

    private boolean keepRunning = true;

    /**
     * Getter for analysis.
     * 
     * @return the analysis
     */
    public Analysis getAnalysis() {
        return this.analysis;
    }

    /**
     * Method "storeIndicator" stores the mapping between the analyzed element name and its indicators. if needed, this
     * method must be called on the Child indicators of the given indicator.
     * 
     * @param elementToAnalyze the element to analyze (column, data provider...)
     * @param indicator the indicator for the given element
     * @return true if ok
     */
    public boolean storeIndicator(T elementToAnalyze, Indicator indicator) {
        this.allIndicators.add(indicator);
        return MultiMapHelper.addUniqueObjectToListMap(elementToAnalyze, indicator, elementToIndicators);
    }

    /**
     * Method "getIndicators".
     * 
     * @param elementName
     * @return the indicator to be computed for the given element
     */
    public List<Indicator> getIndicators(T elementName) {
        List<Indicator> indics = elementToIndicators.get(elementName);
        return (indics != null) ? indics : new ArrayList<Indicator>();
    }

    /**
     * Method "getAnalyzedElements".
     * 
     * @return the unmodifiable set of analyzed elements.
     */
    protected Set<T> getAnalyzedElements() {
        return Collections.unmodifiableSet(this.elementToIndicators.keySet());
    }

    /**
     * Method "evaluateIndicators". A connection must be open. It does not close the connection. It is to the caller
     * responsibility to close the connection.
     * 
     * @param sqlStatement
     * @return a return code with an error message if any problem has been encountered.
     */
    private ReturnCode evaluateIndicators(String sqlStatement) {
        ReturnCode rc = checkConnection();
        if (!rc.isOk()) {
            return rc;
        }
        try {
            if (!prepareIndicators()) {
                rc.setReturnCode(Messages.getString("Evaluator.Problem", javaPatternMessage), false); //$NON-NLS-1$
                return rc;
            }

            if (this.continueRun()) {
                rc = executeSqlQuery(sqlStatement);
            }
            // MOD qiongli tdq-7282 when rc is not ok,should not return,need to continue and dispaly those correct
            // indicator results.
            // if (!rc.isOk()) {
            // return rc;
            // }
            if (!finalizeIndicators()) {
                rc.setReturnCode(Messages.getString("Evaluator.ProblemFinalizeIndicators"), false); //$NON-NLS-1$
                return rc;
            }
            if (isLowMemory) {
                rc.setReturnCode(Messages.getString("Evaluator.OutOfMomory", usedMemory), false); //$NON-NLS-1$
                return rc;
            }
        } catch (SQLException e) {
            log.error(Messages.getString("Evaluator.SQLException", sqlStatement), e); //$NON-NLS-1$
            rc.setReturnCode(e.getMessage(), false);
        }
        return rc;
    }

    private boolean finalizeIndicators() {
        boolean ok = true;
        for (Indicator indic : allIndicators) {
            if (!indic.finalizeComputation()) {
                ok = false;
            } else {
                indic.setComputed(true);
            }
        }
        return ok;
    }

    private boolean prepareIndicators() {
        boolean ok = true;
        for (Indicator indic : allIndicators) {
            if (!indic.prepare()) {
                javaPatternMessage = indic.getName();
                ok = false;
            }
        }
        return ok;
    }

    /**
     * DOC scorreia Comment method "executeSqlQuery".
     * 
     * @param sqlStatement
     * @return
     */
    protected abstract ReturnCode executeSqlQuery(String sqlStatement) throws SQLException;

    /**
     * Method "evaluateIndicators". A connection must be open. This method does not close the connection. It is to the
     * caller responsibility to close the connection.
     * 
     * @param sqlStatement
     * @return a return code with an error message if any problem has been encountered.
     */
    public ReturnCode evaluateIndicators(String sqlStatement, boolean closeConnection) {
        ReturnCode rc = evaluateIndicators(sqlStatement);
        if (this.isPooledConnection()) {
            return rc;
        } else {
            if (!closeConnection) {
                return rc;
            } else {
                if (rc.isOk()) {
                    return closeConnection();
                } else { // problem with evaluation
                    ReturnCode connRc = closeConnection();
                    if (!connRc.isOk()) {
                        // add the message to returned code
                        String message = rc.getMessage();
                        message = Messages.getString("Evaluator.ConnectionProblem", message, connRc.getMessage()); //$NON-NLS-1$
                        rc.setMessage(message);
                    }
                }
                return rc;
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getFetchSize() {
        return this.fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    protected ReturnCode closeConnection() {

        if (connection != null) {
            try {
                return connection.isClosed() ? new ReturnCode(true) : ConnectionUtils.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ReturnCode(Messages.getString("Evaluator.closeNullConnection"), false); //$NON-NLS-1$  

    }

    protected ReturnCode checkConnection() {
        if (connection == null) {
            return new ReturnCode(Messages.getString("Evaluator.openNullConnection"), false); //$NON-NLS-1$
        }
        return ConnectionUtils.isValid(connection);
    }

    /**
     * Method "selectCatalog" attempts to set the catalog for the current connection.
     * 
     * @param catalogName the catalog to select
     * @return true if set, false if problem
     */
    public boolean selectCatalog(String catalogName) {
        if (connection == null) {
            return false;
        }
        try {
            // MOD qiongli 2012-8-9,Method 'Method not supported' not supported for HiveConnection
            if (!(ConnectionUtils.isOdbcProgress(connection) || ExtractMetaDataUtils.getInstance().isHiveConnection(connection))) {
                connection.setCatalog(catalogName);
            }
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    protected Indicator[] getAllIndicators() {
        return this.allIndicators.toArray(new Indicator[allIndicators.size()]);
    }

    private IProgressMonitor monitor;

    public IProgressMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    protected boolean continueRun() {
        // TOCHANGE msjian 2016-10-21 Use thread notification technology to stop the analysis
        // MOD scorreia 2013-09-10 avoid checking for each analyzed row. Check only every 1000 rows
        checkContinueCount++;
        if (checkContinueCount % CHECK_EVERY_N_COUNT != 0) {
            return keepRunning;
        }
        if (!Platform.isRunning()) { // Reporting engine is working as library
            return true;
        }

        if (getMonitor() != null && getMonitor().isCanceled()) {
            keepRunning = false;
        } else if (this.isLowMemory) {
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
     * create the statement.
     * 
     * @return
     * @throws SQLException
     */
    protected Statement createStatement() throws SQLException {
        Statement statement = null;
        if (getAnalysis() != null && getConnection() != null) {
            statement = DbmsLanguageFactory.createDbmsLanguage(getAnalysis().getContext().getConnection()).createStatement(
                    getConnection(), getFetchSize());
        }
        return statement;
    }

    protected ReturnCode checkConnectionBeforeGetTableView() throws Exception {
        ReturnCode isValid = checkConnection();
        if (!isValid.isOk()) {
            log.error(isValid.getMessage());
            throw new Exception(isValid.getMessage());
        }
        return isValid;
    }
}
