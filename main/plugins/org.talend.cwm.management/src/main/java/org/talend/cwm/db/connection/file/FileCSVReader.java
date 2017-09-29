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
package org.talend.cwm.db.connection.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.dataquality.matchmerge.Attribute;
import org.talend.dataquality.matchmerge.Record;
import org.talend.dataquality.record.linkage.grouping.swoosh.RichRecord;
import org.talend.dq.helper.FileUtils;
import orgomg.cwm.objectmodel.core.ModelElement;

import com.talend.csv.CSVReader;

/**
 * created by yyin on 2014-9-22 Detailled comment
 * 
 */
public class FileCSVReader implements IFileReader {

    private CSVReader csvReader = null;

    private int[] analysedColumnIndex;

    private String[] analysedColumnName;

    private long csvLimitValue = 0;

    private int csvHeadValue = 0;

    private long index = 0;

    private long currentRowIndex = -1;
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.file.IFileReader#init(java.io.File,
     * org.talend.core.model.metadata.builder.connection.DelimitedFileConnection, java.util.List)
     */
    public FileCSVReader(File file, DelimitedFileConnection delimitedFileconnection, List<ModelElement> analysisElementList)
            throws IOException, FileNotFoundException {
        analysedColumnIndex = new int[analysisElementList.size()];
        analysedColumnName = new String[analysisElementList.size()];

        csvLimitValue = JavaSqlFactory.getLimitValue(delimitedFileconnection);
        csvHeadValue = JavaSqlFactory.getHeadValue(delimitedFileconnection);
        csvReader = FileUtils.createCsvReader(file, delimitedFileconnection);
        FileUtils.initializeCsvReader(delimitedFileconnection, csvReader);

        findElementPosition(analysisElementList);
    }

    /**
     * need to find the analysed element position , and only get these analysed column's values.
     * 
     * @param analysisElementList
     * @throws IOException
     */
    private void findElementPosition(List<ModelElement> analysisElementList) throws IOException {
        MetadataColumn mColumn = (MetadataColumn) analysisElementList.get(0);
        MetadataTable metadataTable = ColumnHelper.getColumnOwnerAsMetadataTable(mColumn);
        EList<MetadataColumn> columns = metadataTable.getColumns();
        List<String> columnLabels = new ArrayList<String>();
        for (MetadataColumn column : columns) {
            columnLabels.add(column.getLabel());
        }
        for (int j = 0; j < analysisElementList.size(); j++) {
            analysedColumnName[j] = ((MetadataColumn) analysisElementList.get(j)).getLabel();
            analysedColumnIndex[j] = columnLabels.indexOf(analysedColumnName[j]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.file.IFileReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        return csvReader.readNext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.file.IFileReader#next()
     */
    public Record next() throws IOException {
        currentRowIndex++;
        if (currentRowIndex < csvHeadValue) {
            if (hasNext()) {
                return next();
            } else {
                return null;
            }
        }
        if (csvLimitValue != -1 && currentRowIndex > csvLimitValue - 1) {
            return null;
        }
        String[] values = csvReader.getValues();
        String[] analysedValues = new String[analysedColumnIndex.length];
        for (int i = 0; i < analysedColumnIndex.length; i++) {
            analysedValues[i] = values[analysedColumnIndex[i]];
        }
        return createRichRecord(analysedValues);
    }

    /**
     * DOC yyin Comment method "createRichRecord".
     * 
     * @param analysedValues
     * @return
     */
    private Record createRichRecord(String[] analysedValues) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        for (int i = 0; i < analysedValues.length; i++) {
            Attribute attribute = new Attribute(analysedColumnName[i], i);

            String value = String.valueOf(analysedValues[i]);
            attribute.setValue(value);
            attributes.add(attribute);
        }
        return new RichRecord(attributes, String.valueOf(index++), 0, StringUtils.EMPTY);
    }

    public void close() throws IOException {
        this.csvReader.close();
    }

}
