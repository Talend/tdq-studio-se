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
package org.talend.cwm.db.connection;

import java.util.Collection;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public interface IXMLDBConnection {

    public ReturnCode checkDatabaseConnection();

    public Collection<TdXmlSchema> createConnection(Connection dataProvider);

    public void setSofewareSystem(Connection dataProvider, DBConnectionParameter parameter);

    public void setProviderConnection(Connection dataProvider, DBConnectionParameter parameter);

    public Collection<String> getConnectionContent();
}
