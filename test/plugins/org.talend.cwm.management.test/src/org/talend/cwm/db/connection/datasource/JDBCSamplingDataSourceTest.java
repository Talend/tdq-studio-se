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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.talend.dataquality.sampling.SamplingDataSource;

/**
 * created by zhao on 2015年4月22日 Detailled comment
 *
 */
public class JDBCSamplingDataSourceTest {

    SamplingDataSource<ResultSet> dataSource = null;

    Connection conn = null;

    /**
     * DOC zhao Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    @Ignore
    public void setUp() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tbi", "root", "");
        Statement statement = conn.createStatement();
        dataSource = new JDBCSamplingDataSource();
        ResultSet rsData = statement.executeQuery("SELECT * FROM employee");
        dataSource.setDataSource(rsData);
        ((JDBCSamplingDataSource) dataSource).setColumnSize(rsData.getMetaData().getColumnCount());
    }

    /**
     * DOC zhao Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    @Ignore
    public void tearDown() throws Exception {
        dataSource.finalizeDataSampling();
        conn.close();
    }

    @Ignore
    public void testGetRecord() {
        try {
            while (dataSource.hasNext()) {
                System.out.println(getString(dataSource.getRecord()));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getString(Object[] data) {
        StringBuffer sb = new StringBuffer();
        for (Object o : data) {
            sb.append(o == null ? "" : o.toString()).append(",");
        }
        return sb.toString();
    }
}
