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
package org.talend.dq.helper;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dq.analysis.parameters.DBConnectionParameter;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ParameterUtilTest {

    /**
     * Test method for
     * {@link org.talend.dq.helper.ParameterUtil#toMap(org.talend.dq.analysis.parameters.DBConnectionParameter)}.
     */
    @Test
    public void testToMap() {

        DBConnectionParameter dbParameter = new DBConnectionParameter();
        dbParameter.setParameters(new Properties());
        Map<String, String> parameterMap = ParameterUtil.toMap(dbParameter);

        assertSame(dbParameter.getPort(), parameterMap.get("port")); //$NON-NLS-1$
        assertSame(dbParameter.getHost(), parameterMap.get("host")); //$NON-NLS-1$
        assertSame(dbParameter.getDriverPath(), parameterMap.get("driverPath")); //$NON-NLS-1$
        assertSame(dbParameter.getStatus(), parameterMap.get("status")); //$NON-NLS-1$
        assertSame(dbParameter.getFilePath(), parameterMap.get("filePath")); //$NON-NLS-1$
        assertSame(dbParameter.getDriverClassName(), parameterMap.get("driverClassName")); //$NON-NLS-1$
        assertSame(dbParameter.getPurpose(), parameterMap.get("purpose")); //$NON-NLS-1$
        assertSame(dbParameter.getParameters().getProperty(TaggedValueHelper.DATA_FILTER), parameterMap.get("datafilter")); //$NON-NLS-1$
        assertSame(dbParameter.getParameters().getProperty(TaggedValueHelper.UNIVERSE), parameterMap.get("universe")); //$NON-NLS-1$
        assertSame(dbParameter.getParameters().getProperty(TaggedValueHelper.USER), parameterMap.get(TaggedValueHelper.USER));
        assertSame(dbParameter.getParameters().getProperty(TaggedValueHelper.PASSWORD),
                parameterMap.get(TaggedValueHelper.PASSWORD));
        assertSame(dbParameter.getVersion(), parameterMap.get("version")); //$NON-NLS-1$
        assertSame(dbParameter.getAuthor(), parameterMap.get("author")); //$NON-NLS-1$
        assertSame(dbParameter.getSqlTypeName(), parameterMap.get("sqlTypeName")); //$NON-NLS-1$
        assertSame(dbParameter.getADDParameter(), parameterMap.get("aDDParameter")); //$NON-NLS-1$
        assertSame(dbParameter.getDbName(), parameterMap.get("dbName")); //$NON-NLS-1$
        assertSame(dbParameter.getDescription(), parameterMap.get("description")); //$NON-NLS-1$
        assertSame(Boolean.toString(dbParameter.isRetrieveAllMetadata()), parameterMap.get("retrieveAllMetadata")); //$NON-NLS-1$
        assertSame(dbParameter.getJdbcUrl(), parameterMap.get("jdbcUrl")); //$NON-NLS-1$

    }

}
