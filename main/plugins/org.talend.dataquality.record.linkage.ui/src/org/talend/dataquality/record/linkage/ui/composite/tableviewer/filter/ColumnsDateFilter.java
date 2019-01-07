// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter;

import java.util.function.Predicate;

import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;

public class ColumnsDateFilter implements Predicate<MetadataColumn> {

    @Override
    public boolean test(MetadataColumn metadataColumn) {
        String talendType = metadataColumn.getTalendType();
        int javaType = TalendTypeConvert.convertToJDBCType(talendType);
        return Java2SqlType.isDateInSQL(javaType);
    }

}
