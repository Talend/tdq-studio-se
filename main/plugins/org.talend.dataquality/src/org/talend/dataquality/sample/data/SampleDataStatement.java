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
package org.talend.dataquality.sample.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class SampleDataStatement implements Statement {

    private SampleDataResultSet resultSet = null;

    /**
     * DOC zshen SampleDataStatement constructor comment.
     */
    public SampleDataStatement(List<Object[]> sampleData, String[] columnNames) {
        resultSet = new SampleDataResultSet(sampleData, Arrays.asList(columnNames));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#close()
     */
    @Override
    public void close() throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMaxFieldSize()
     */
    @Override
    public int getMaxFieldSize() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getQueryTimeout()
     */
    @Override
    public int getQueryTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#cancel()
     */
    @Override
    public void cancel() throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    @Override
    public void setCursorName(String name) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSet()
     */
    @Override
    public SampleDataResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setFetchDirection(int)
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetType()
     */
    @Override
    public int getResultSetType() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    @Override
    public void addBatch(String sql) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#clearBatch()
     */
    @Override
    public void clearBatch() throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeBatch()
     */
    @Override
    public int[] executeBatch() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMoreResults(int)
     */
    @Override
    public boolean getMoreResults(int current) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getGeneratedKeys()
     */
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetHoldability()
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#isClosed()
     */
    @Override
    public boolean isClosed() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setPoolable(boolean)
     */
    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#isPoolable()
     */
    @Override
    public boolean isPoolable() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#closeOnCompletion()
     */
    @Override
    public void closeOnCompletion() throws SQLException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#isCloseOnCompletion()
     */
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

}
