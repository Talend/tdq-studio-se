/*
 * Copyright (C) 2006 Davy Vanherbergen dvanherbergen@users.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.sqlexplorer.dataset;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.sqlexplorer.ExplorerException;
import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dbproduct.SQLConnection;
import net.sourceforge.sqlexplorer.dbproduct.Session;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;

/**
 * Generic DataSet to hold values for TableViewer.
 * 
 * This class has been changed to remove dependencies on a fixed list of data types; this is to allow database-specific
 * data types. Since every row is represented as Objects (typically instances of String, Integer, Double, etc), it is
 * only a requirement that the cells implement the Comparable interface so that sorting works correctly. The textual
 * representation is obtained by calling toString() on the object.
 * 
 * Any code which used to use the TYPE_XXXX constants defined here should now use instanceof if knowledge of the
 * implementing type is required; however, be aware that non-standard types (i.e. types not defined in java.lang) may be
 * present.
 * 
 * @author Davy Vanherbergen
 * @modified John Spackman
 */
public class DataSet {

    public static class Column {

        private String caption;

        private boolean rightJustify;

        public Column(String caption, boolean rightJustify) {
            super();
            this.caption = caption;
            this.rightJustify = rightJustify;
        }

        public String getCaption() {
            return caption;
        }

        public boolean isRightJustify() {
            return rightJustify;
        }

        public Format getFormat() {
            return null;
        }
    }

    public static class FormattedColumn extends Column {

        private Format format;

        public FormattedColumn(String caption, boolean rightJustify, Format format) {
            super(caption, rightJustify);
            this.format = format;
        }

        @Override
        public Format getFormat() {
            return format;
        }
    }

    // Caption for the results tabs
    private String caption;

    private Column[] columns;

    private DataSetRow[] _rows;

    private DataSetTableSorter _sorter;

    // Whether dates are formatted (from preferences)
    private Boolean formatDates;

    /**
     * Create a new dataSet based on an existing ResultSet.
     * 
     * @param resultSet ResultSet with values [mandatory]
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * 
     * @throws Exception if the dataset could not be created
     */
    public DataSet(ResultSet resultSet, int[] relevantIndeces, int maxRows) throws SQLException {
        initialize(null, resultSet, relevantIndeces, maxRows);
    }

    /**
     * Create a new dataSet based on an existing ResultSet.
     * 
     * @param resultSet ResultSet with values [mandatory]
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * 
     * @throws Exception if the dataset could not be created
     */
    public DataSet(ResultSet resultSet, int[] relevantIndeces) throws SQLException {
        this(resultSet, relevantIndeces, 0);
    }

    /**
     * Create a new dataSet based on an existing ResultSet.
     * 
     * @param resultSet ResultSet with values [mandatory]
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * 
     * @throws Exception if the dataset could not be created
     */
    public DataSet(String caption, ResultSet resultSet, int[] relevantIndeces, int maxRows) throws SQLException {
        this.caption = caption;
        initialize(null, resultSet, relevantIndeces, maxRows);
    }

    /**
     * Create a new dataSet based on an existing ResultSet.
     * 
     * @param resultSet ResultSet with values [mandatory]
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * 
     * @throws Exception if the dataset could not be created
     */
    public DataSet(String caption, ResultSet resultSet, int[] relevantIndeces) throws SQLException {
        this(caption, resultSet, relevantIndeces, 0);
    }

    /**
     * Create new dataset based on sql query.
     * 
     * @param columnLabels string[] of columnLabels, use null if the column name can be used as label
     * @param sql query string
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * @param connection An open SQLConnection [mandatory]
     * @throws Exception if dataSet could not be created
     */
    public DataSet(String[] columnLabels, String sql, int[] relevantIndeces, Session session) throws SQLException,
            ExplorerException {
        SQLConnection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = session.grabConnection();
            statement = connection.createStatement();
            statement.execute(sql);
            resultSet = statement.getResultSet();
            initialize(columnLabels, resultSet, relevantIndeces, 0);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    SQLExplorerPlugin.error(Messages.getString("DataSet.errorCloseRs"), e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    SQLExplorerPlugin.error(Messages.getString("DataSet.errorCloseStmt"), e);
                }
            }
            if (connection != null) {
                session.releaseConnection(connection);
            }
        }
    }

    /**
     * Create new dataset based on String[][].
     * 
     * @param columnLabels string[] of columnLabels [mandatory]
     * @param data string[][] with values for dataset [mandatory]
     * @throws Exception if dataSet could not be created
     */
    public DataSet(String[] columnLabels, Comparable[][] data) {
        this(null, columnLabels, data);
    }

    /**
     * Create new dataset based on String[][].
     * 
     * @param caption
     * @param columnLabels string[] of columnLabels [mandatory]
     * @param data string[][] with values for dataset [mandatory]
     * @throws Exception if dataSet could not be created
     */
    public DataSet(String caption, String[] columnLabels, Comparable[][] data) {
        this.caption = caption;
        columns = convertColumnLabels(columnLabels);

        _rows = new DataSetRow[data.length];

        for (int i = 0; i < data.length; i++) {
            _rows[i] = new DataSetRow(this, data[i]);
        }
    }

    /**
     * Initialize dataSet based on an existing ResultSet.
     * 
     * @param columnLabels String[] of column labels [mandatory]
     * @param resultSet ResultSet with values [mandatory]
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     * @throws Exception if the dataset could not be created
     */
    private void initialize(String[] columnLabels, ResultSet resultSet, int[] relevantIndeces, int maxRows) throws SQLException {

        ResultSetMetaData metadata = resultSet.getMetaData();

        int[] ri = relevantIndeces;

        // create default column indexes
        if (ri == null || ri.length == 0) {
            ri = new int[metadata.getColumnCount()];
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                ri[i - 1] = i;
            }
        }

        // create column labels
        if (columnLabels != null && columnLabels.length != 0) {
            columns = convertColumnLabels(columnLabels);
        } else {
            columns = new Column[ri.length];
            for (int i = 0; i < ri.length; i++) {
                int columnIndex = ri[i];
                columns[i] = createColumn(metadata, columnIndex);
            }
        }

        loadRows(resultSet, ri, maxRows);
    }

    /**
     * Called to create a Column object from the given metadata; this is broken out into a separate method so that
     * database-specific implementations can override it
     * 
     * @param metadata
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    protected Column createColumn(ResultSetMetaData metadata, int columnIndex) throws SQLException {
        int type = metadata.getColumnType(columnIndex);

        // Numeric - figure out a display format
        if (type == Types.DECIMAL || type == Types.NUMERIC || type == Types.DOUBLE || type == Types.FLOAT || type == Types.REAL) {
            int precision = metadata.getPrecision(columnIndex);
            int scale = metadata.getScale(columnIndex);
            if (precision < 1 || scale > precision) {
                return new FormattedColumn(metadata.getColumnName(columnIndex), true, null);// new
                                                                                            // DecimalFormat("#.#"));
            }

            /*
             * NOTE: Scale can be negative (although possibly limited to Oracle), but we cope with this by specifing #
             * after the decimal place precision-1 times; eg a precision of 10 will return #.#########
             */
            StringBuffer sb = new StringBuffer(precision + 2);
            for (int j = 0; j < precision; j++) {
                if (scale < 0 || j < precision - scale - 1) {
                    sb.append('#');
                } else {
                    sb.append('0');
                }
            }

            if (scale > 0) {
                sb.insert(precision - scale, '.');
            } else if (scale < 0) {
                sb.insert(1, '.');
            }

            return new FormattedColumn(metadata.getColumnName(columnIndex), true, new DecimalFormat(sb.toString()));
        }

        if (type == Types.DATE || type == Types.TIMESTAMP || type == Types.TIME) {
            return new FormattedColumn(metadata.getColumnName(columnIndex), false, getDateFormat(type));
        }

        return new Column(metadata.getColumnName(columnIndex), false);
    }

    /**
     * Creates an array of Column descriptors from an array of strings
     * 
     * @param columnLabels
     * @return
     */
    private Column[] convertColumnLabels(String[] columnLabels) {
        Column[] result = new Column[columnLabels.length];
        for (int i = 0; i < columnLabels.length; i++) {
            result[i] = new Column(columnLabels[i], false);
        }
        return result;
    }

    /**
     * Get the column index for a given column name
     * 
     * @param name
     * @return index of column whose name matches or 0 if none found
     */
    public int getColumnIndex(String name) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getCaption().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @return String[] with all column labels
     */
    public Column[] getColumns() {
        return columns;
    }

    /**
     * Obtain number of rows.
     * 
     * @return Number of rows.
     */
    public int getRowCount() {
        return _rows.length;
    }

    /**
     * @return all rows in this dataset
     */
    public DataSetRow[] getRows() {
        return _rows;
    }

    /**
     * Get a single row in this dataset.
     * 
     * @param index Index of row.
     * @return Row.
     * @throws IndexOutOfBoundsException if row at index isn't present.
     */
    public DataSetRow getRow(int index) {
        if (index < 0 || index >= _rows.length) {
            throw new IndexOutOfBoundsException(Messages.getString("DataSet.errorIndexOutOfRange") + index);
        }
        return _rows[index];
    }

    /**
     * Called to load rows from the specified result set; the default implementation simply uses standard JDBC data
     * types to inten to be overridden.
     * 
     * @param resultSet ResultSet to load from
     * @param relevantIndeces int[] of all columns to add to the dataSet, use null if all columns should be included.
     */
    protected void loadRows(ResultSet resultSet, int[] relevantIndeces, int maxRows) throws SQLException {
        ResultSetMetaData metadata = resultSet.getMetaData();

        // create rows
        ArrayList rows = new ArrayList(maxRows > 0 ? maxRows : 100);
        int rowCount = 0;
        while (resultSet.next() && (maxRows == 0 || rowCount < maxRows)) {
            DataSetRow row = new DataSetRow(this);
            for (int i = 0; i < columns.length; i++) {
                int columnIndex = relevantIndeces != null ? relevantIndeces[i] : i;
                Comparable obj = loadCellValue(columnIndex, metadata.getColumnType(columnIndex), resultSet);
                if (resultSet.wasNull()) {
                    row.setValue(i, null);
                } else {
                    row.setValue(i, obj);
                }
            }
            rows.add(row);
            rowCount++;
        }
        _rows = (DataSetRow[]) rows.toArray(new DataSetRow[] {});
    }

    /**
     * Loads a given column from the current row in a ResultSet; can be overridden to provide database-specific
     * implementation
     * 
     * @param columnIndex
     * @param dataType
     * @param resultSet
     * @return
     * @throws SQLException
     */
    protected Comparable loadCellValue(int columnIndex, int dataType, ResultSet resultSet) throws SQLException {
        switch (dataType) {
        case Types.INTEGER:
        case Types.SMALLINT:
        case Types.TINYINT:
            return new Long(resultSet.getLong(columnIndex));

        case Types.BIGINT:
            return new Long(resultSet.getLong(columnIndex));

        case Types.DECIMAL:
        case Types.NUMERIC:
        case Types.DOUBLE:
        case Types.FLOAT:
        case Types.REAL:
            int precision = resultSet.getMetaData().getPrecision(columnIndex);
            if (precision > 16 || precision < 1) {
                return resultSet.getBigDecimal(columnIndex);
            }
            return new Double(resultSet.getDouble(columnIndex));

            // MOD qiongli 2014-4-29 TDQ-8769,void to lose "hh:mm:ss.SSS" for Date type.so revert to use default
            // "getTimestamp(...)".for vertica Date type,it will be catched and use "getDate(...)"
        case Types.DATE:
        case Types.TIMESTAMP:
            Comparable dateTime = null;
            try {
                dateTime = resultSet.getTimestamp(columnIndex);
            } catch (SQLException exc) {
                if (dataType == Types.DATE) {
                    dateTime = resultSet.getDate(columnIndex);
                } else {
                    SQLExplorerPlugin.error(exc.getMessage());
                }
            }
            return dateTime;

        case Types.TIME:
            return resultSet.getTime(columnIndex);

        default:
            // MOD yyi 2012-05-25 TDQ-5460 : Fix getString of JConnector(sybase).
            try {
                return resultSet.getString(columnIndex);
            } catch (SQLException e) {
                // MOD yyi 2012-04-17 TDQ-5176 : Change get string to get bytes for mess decode.
                return null == resultSet.getBytes(columnIndex) ? null : new String(resultSet.getBytes(columnIndex));
            }
        }
    }

    /**
     * Resort the data using the given column and sortdirection.
     * 
     * @param columnIndex primary sort column index
     * @param sortDirection SWT.UP | SWT.DOWN
     */
    public void sort(int columnIndex, int sortDirection) {
        if (_sorter == null) {
            _sorter = new DataSetTableSorter(this);
        }
        _sorter.setTopPriority(columnIndex, sortDirection);

        Arrays.sort(_rows, _sorter);
    }

    private DateFormat getDateFormat(int type) {
        SimpleDateFormat dateFormat = null;
        if (formatDates == null) {
            formatDates = SQLExplorerPlugin.getDefault().getPluginPreferences().getBoolean(IConstants.DATASETRESULT_FORMAT_DATES);
        }
        if (!formatDates) {
            return null;
        }

        // MOD qiongli 2014-4-29,format Time data type as "hh:mm:ss.SSS";format Date and Timestamp as "yyyy-mm-dd HH:mm:ss.SSS".
        if (type == Types.TIME) {
            dateFormat = new SimpleDateFormat(SQLExplorerPlugin.getDefault().getPluginPreferences()
                    .getString(IConstants.DATASETRESULT_TIME_FORMAT));
        } else {
            dateFormat = new SimpleDateFormat(SQLExplorerPlugin.getDefault().getPluginPreferences()
                    .getString(IConstants.DATASETRESULT_DATE_FORMAT));
        }

        return dateFormat;
    }

    public String getCaption() {
        return caption;
    }
}
