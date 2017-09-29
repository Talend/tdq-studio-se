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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.dataquality.matchmerge.Attribute;
import org.talend.dataquality.matchmerge.Record;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by xqliu on 2016年6月24日
 * Detailled comment
 *
 */
public class FileCSVReaderTest {

    /**
     * DOC xqliu Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC xqliu Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC xqliu Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC xqliu Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.cwm.db.connection.file.FileCSVReader#next()}.
     */
    @Test
    public void testNext() {
        try {
            File file = new File("./file.txt");
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                out.println("id,name,email");
                out.println("1,tom,a@b.cn");
                out.println("2,jerry,x@b.cn");
                out.println("3,bob,top@b.cn");
                out.close();

                DelimitedFileConnection delimitedFileconnection = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
                delimitedFileconnection.setFilePath(file.getAbsolutePath());
                delimitedFileconnection.setLimitValue("0");
                delimitedFileconnection.setHeaderValue("1");
                delimitedFileconnection.setFieldSeparatorValue(",");

                List<ModelElement> analysisElementList = new ArrayList<ModelElement>();
                MetadataTable table = ConnectionFactory.eINSTANCE.createMetadataTable();
                table.setName("table");
                MetadataColumn id = ConnectionFactory.eINSTANCE.createMetadataColumn();
                id.setName("id");
                id.setLabel("id");
                id.setTable(table);
                MetadataColumn name = ConnectionFactory.eINSTANCE.createMetadataColumn();
                name.setName("name");
                name.setLabel("name");
                name.setTable(table);
                MetadataColumn email = ConnectionFactory.eINSTANCE.createMetadataColumn();
                email.setName("email");
                email.setLabel("email");
                email.setTable(table);
                analysisElementList.add(id);
                analysisElementList.add(name);
                analysisElementList.add(email);

                FileCSVReader fileCSVReader = new FileCSVReader(file, delimitedFileconnection, analysisElementList);
                String fileContent = "";
                int i = 0;
                while (fileCSVReader.hasNext()) {
                    i++;
                    Record record = fileCSVReader.next();
                    List<Attribute> attributes = record.getAttributes();
                    fileContent += i + ":";
                    for (Attribute attribute : attributes) {
                        fileContent += "[" + attribute.getLabel() + "," + attribute.getValue() + "]";
                    }
                }
                fileCSVReader.close();
                Assert.assertEquals(
                        "1:[id,1][name,tom][email,a@b.cn]2:[id,2][name,jerry][email,x@b.cn]3:[id,3][name,bob][email,top@b.cn]",
                        fileContent);
            }
            // delete the temp file
            if (file.exists()) {
                file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
