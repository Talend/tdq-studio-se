// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.helpers;

import java.sql.Types;

import org.junit.Test;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.indicators.DataminingType;

/**
 * created by msjian on 2020年6月5日
 * Detailled comment
 *
 */
public class MetadataHelperTest {


    /**
     * Test method for
     * {@link org.talend.dataquality.helpers.MetadataHelper#getDataminingType(org.talend.cwm.relational.TdColumn)}
     * 
     */
    @Test
    public void testGetDataminingType() {
        TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
        column1.setSourceType("INT");
        column1.setTalendType("id_Integer");
        TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        sqlDataType.setName("INT");
        sqlDataType.setJavaDataType(Types.INTEGER);
        column1.setSqlDataType(sqlDataType);
        DataminingType dataminingType = MetadataHelper.getDataminingType(column1);
        assert (DataminingType.INTERVAL == dataminingType);
        column1.setKey(true);
        orgomg.cwm.resource.relational.PrimaryKey primaryKey =
                orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createPrimaryKey();
        ColumnHelper.addKeyToColumn(primaryKey, column1);
        dataminingType = MetadataHelper.getDataminingType(column1);
        assert (DataminingType.NOMINAL == dataminingType);
    }

}
