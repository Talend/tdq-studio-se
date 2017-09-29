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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.dataquality.matchmerge.Attribute;
import org.talend.dataquality.matchmerge.Record;
import org.talend.dataquality.record.linkage.grouping.swoosh.RichRecord;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.fileprocess.FileInputDelimited;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2014-9-22 Detailled comment
 * 
 */
public class FileDelimitedReader implements IFileReader {

    private FileInputDelimited fileInputDelimited;

    private int analysedColumnIndex[];

    private String analysedColumnName[];

    private long index = 0;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.file.IFileReader#init(java.io.File,
     * org.talend.core.model.metadata.builder.connection.DelimitedFileConnection, java.util.List)
     */
    public FileDelimitedReader(File file, DelimitedFileConnection delimitedFileconnection, List<ModelElement> analysisElementList)
            throws IOException {
        fileInputDelimited = AnalysisExecutorHelper.createFileInputDelimited(delimitedFileconnection);
        analysedColumnIndex = new int[analysisElementList.size()];
        analysedColumnName = new String[analysisElementList.size()];

        findElementPositionByColumn(analysisElementList);
    }

    /**
     * find the position of the analysed elements in the file table's column
     */
    private void findElementPositionByColumn(List<ModelElement> analysisElementList) {
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
        return fileInputDelimited.nextRecord();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.file.IFileReader#next()
     */
    public Record next() throws IOException {
        String[] rowValues = new String[analysedColumnIndex.length];
        for (int i = 0; i < analysedColumnIndex.length; i++) {
            rowValues[i] = fileInputDelimited.get(analysedColumnIndex[i]);
        }
        return createRichRecord(rowValues);
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

    public void close() {
        fileInputDelimited.close();
    }
}
