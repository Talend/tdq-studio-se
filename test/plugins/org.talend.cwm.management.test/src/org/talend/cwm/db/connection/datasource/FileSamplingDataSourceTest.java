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
package org.talend.cwm.db.connection.datasource;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.dataquality.sampling.SamplingDataSource;
import org.talend.dq.helper.UnitTestBuildHelper;

import com.talend.csv.CSVReader;

/**
 * created by zhao on 2015年4月27日 Detailed comment
 *
 */
public class FileSamplingDataSourceTest {

    private String fieldSeperator = ","; //$NON-NLS-1$

    private String encoding = "UTF-8"; //$NON-NLS-1$

    private String rowSep = "\\n"; //$NON-NLS-1$

    private String textEnclosure = "\""; //$NON-NLS-1$

    private String escapeChar = "\\"; //$NON-NLS-1$

    private SamplingDataSource<DelimitedFileConnection> fileDataSource = null;

    @Test
    public void testGetRecord() throws Exception {
        fileDataSource = new FileSamplingDataSource();
        DelimitedFileConnection fileConnection = ConnectionPackage.eINSTANCE.getConnectionFactory()
                .createDelimitedFileConnection();
        URL fileUrl = this.getClass().getResource("simple_data.csv"); //$NON-NLS-1$
        UnitTestBuildHelper.getDefault().initFileConnection(fileUrl, fileConnection);
        fileDataSource.setDataSource(fileConnection);
        int[] pos = { 0, 1, 2, 3 };
        ((FileSamplingDataSource) fileDataSource).setColumnPositions(pos);
        // CSVReader csvReader = createCsvReader(new File(getClass().getClassLoader()
        // .getResource("org/talend/cwm/db/connection/datasource/simple_data.csv").getFile()));
        int idx = 0;
        while (fileDataSource.hasNext()) {
            String value = getString(fileDataSource.getRecord());
            System.out.println(value);
            if (0 == idx) {
                assertEquals("-24,male,4000,2010-10-23,", value); //$NON-NLS-1$
            } else if (1 == idx) {
                assertEquals("-50.0,male,2000,2011-02-02 12:10:00,", value); //$NON-NLS-1$
            } else if (5 == idx) {
                assertEquals(",female,4000,02/01/2008,", value); //$NON-NLS-1$
            } else if (9 == idx) {
                assertEquals("a str,male,30000,2004-12-20 00:00:00,", value); //$NON-NLS-1$
            }
            idx++;
        }
    }

    /**
     * DOC zhao create csv reader given file, field separator , encoding and row separator .
     * 
     * @param file
     * @param fieldSeperator field separator
     * @param encoding file encoding
     * @param rowSep row separator
     * @param textEnclosure text enclosure
     * @param escapeChar escape character.
     * @return CSVReader instance.
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private CSVReader createCsvReader(File file) throws UnsupportedEncodingException, FileNotFoundException {

        CSVReader csvReaderNew = new CSVReader(new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file),
                encoding)), fieldSeperator.charAt(0));
        csvReaderNew.setSeparator(rowSep.charAt(0));

        csvReaderNew.setSkipEmptyRecords(true);
        if (textEnclosure != null && textEnclosure.length() > 0) {
            csvReaderNew.setQuoteChar(textEnclosure.charAt(0));
        }
        if (escapeChar != null) {
            csvReaderNew.setEscapeChar(escapeChar.charAt(0));
        }
        return csvReaderNew;
    }

    public String getString(Object[] data) {
        StringBuffer sb = new StringBuffer();
        for (Object o : data) {
            sb.append(o == null ? "" : o.toString()).append(","); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return sb.toString();
    }
}
