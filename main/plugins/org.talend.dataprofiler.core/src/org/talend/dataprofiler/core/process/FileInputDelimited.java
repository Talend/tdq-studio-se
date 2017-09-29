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
package org.talend.dataprofiler.core.process;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * FileInputDelimited is dedicated to Talend's tFileInputDelimited component. It wraps all parameters in
 * tFileInputDelimted, so it makes the generated code much easier and cleaner. This class is not recommended to use in
 * other circumstance.<br/>
 * 
 * @author gke
 */
public class FileInputDelimited {

    private DelimitedFileReader delimitedDataReader = null;

    Iterator<Integer> random = null;

    private int loopCount = 0;

    private boolean randomSwitch = false;

    private int current = 0;

    private boolean countNeedAdjust = false;

    /**
     * This constructor is only for compatibility with the old usecase.(Before add the function support split Record.)
     * 
     */
    public FileInputDelimited(String file, String encoding, String fieldSeparator, String rowSeparator, boolean skipEmptyRow,
            int header, int footer, int limit, int random) throws IOException {
        this(file, encoding, fieldSeparator, rowSeparator, skipEmptyRow, header, footer, limit, random, false);
    }

    /**
     * The constructor's parameter wraps all parameters' value, and a pretreatment was made according the value of
     * header, footer, limit and random.
     * 
     * @param file
     * @param encoding
     * @param fieldSeparator
     * @param rowSeparator
     * @param skipEmptyRow
     * @param header
     * @param footer
     * @param limit
     * @param random
     * @throws IOException
     */
    public FileInputDelimited(String file, String encoding, String fieldSeparator, String rowSeparator, boolean skipEmptyRow,
            int header, int footer, int limit, int random, boolean splitRecord) throws IOException {
        if (header < 0) {
            header = 0;
        }
        if (footer < 0) {
            footer = 0;
        }
        if (random != 0 && limit != 0) {
            this.delimitedDataReader = new DelimitedFileReader(file, encoding, fieldSeparator, rowSeparator, skipEmptyRow);
            this.delimitedDataReader.setSplitRecord(splitRecord);

            this.delimitedDataReader.skipHeaders(header);
            if (random < 0 && footer == 0) {
                if (limit > 0) {
                    this.loopCount = limit;
                } else {
                    this.loopCount = Integer.MAX_VALUE;
                }
                this.countNeedAdjust = true;
            } else {
                int count = (int) this.delimitedDataReader.getAvailableRowCount(footer);
                this.delimitedDataReader.close();
                this.delimitedDataReader = new DelimitedFileReader(file, encoding, fieldSeparator, rowSeparator, skipEmptyRow);
                this.delimitedDataReader.setSplitRecord(splitRecord);

                this.delimitedDataReader.skipHeaders(header);
                if (limit > 0 && random < 0) {
                    this.loopCount = limit < count ? limit : count;
                } else if (limit < 0 && random > 0) {
                    if (random >= count) {
                        this.loopCount = count;
                    } else {
                        setRandoms(random, count);
                        this.loopCount = random;
                    }
                } else if (limit > 0 && random > 0) {
                    if (random >= limit) {
                        random = limit;
                    }
                    if (random >= count) {
                        this.loopCount = count;
                    } else {
                        setRandoms(random, count);
                        this.loopCount = random;
                    }
                } else {
                    this.loopCount = count;
                }
            }
        } else {
            loopCount = 0;
        }
    }

    private void setRandoms(int random, int count) {
        this.randomSwitch = true;
        Set<Integer> ranSet = new java.util.TreeSet<Integer>();
        Random ran = new java.util.Random();
        while (ranSet.size() < random) {
            ranSet.add(ran.nextInt(count));
        }
        this.random = ranSet.iterator();
    }

    /**
     * Skip to the next record, get ready before get new record's value.
     * 
     * @return whether a next record is available.
     * @throws IOException
     */
    public boolean nextRecord() throws IOException {
        if (this.countNeedAdjust) {
            if (this.delimitedDataReader.getProcessedRecordCount() == loopCount) {
                return false;
            }
            return this.delimitedDataReader.readRecord();
        } else {
            if (++current > loopCount) {
                return false;
            }
            if (randomSwitch) {
                if (!this.random.hasNext()) {
                    return false;
                }
                int index = this.random.next();
                do {
                    if (!this.delimitedDataReader.readRecord()) {
                        return false;
                    }
                } while (this.delimitedDataReader.getProcessedRecordCount() <= index);
                return true;
            } else {
                return this.delimitedDataReader.readRecord();
            }
        }
    }

    /**
     * See DelimitedDataReader.get(columnIndex)
     * 
     * @param columnIndex
     * @return
     * @throws IOException
     */
    public String get(int columnIndex) throws IOException {
        return this.delimitedDataReader.get(columnIndex);
    }

    /**
     * Close the delimitedDataReader if delimitedDataReader is not null.
     */
    public void close() {
        if (this.delimitedDataReader != null) {
            this.delimitedDataReader.close();
        }
    }

    /**
     * 
     * @return number of rows get by tFileInputDelimited
     */
    public int getRowNumber() {
        if (this.countNeedAdjust) {
            return (int) this.delimitedDataReader.getProcessedRecordCount();
        } else {
            return this.loopCount;
        }
    }

    public int getColumnsCountOfCurrentRow() throws IOException {
        return this.delimitedDataReader.getAvailableColumnsCount();
    }

    /**
     * check the first limit number of records, and fetch the max columns, this is only for GUI wizard.
     * 
     */
    public static int getMaxColumnCount(String fileName, String encoding, String fieldDelimiter, String recordDelimiter,
            boolean needSkipEmptyRecord, boolean splitRecord, int headerRows, int limit) throws IOException {
        DelimitedFileReader tosDelimitedReader = new DelimitedFileReader(fileName, encoding, fieldDelimiter, recordDelimiter,
                needSkipEmptyRecord);
        tosDelimitedReader.setSplitRecord(splitRecord);
        tosDelimitedReader.skipHeaders(headerRows);
        int result = 0;
        for (int i = 0; i < limit && tosDelimitedReader.readRecord(); i++) {
            int currentColumnsCount = tosDelimitedReader.getAvailableColumnsCount();
            if (currentColumnsCount > result) {
                result = currentColumnsCount;
            }
        }

        tosDelimitedReader.close();

        return result;
    }
}
