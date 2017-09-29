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
package org.talend.dataprofiler.core.model.impl;

import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.dataprofiler.core.model.DelimitedFileIndicator;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sql.TalendTypeConvert;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DelimitedFileIndicatorImpl extends ModelElementIndicatorImpl implements DelimitedFileIndicator {

    public DelimitedFileIndicatorImpl(IRepositoryNode reposNode) {
        super();
        this.setModelElement(reposNode);
    }

    public MetadataColumn getMetadataColumn() {
        return ((MetadataColumnRepositoryObject) this.getModelElementRepositoryNode().getObject()).getTdColumn();
    }

    @Override
    public int getJavaType() {
        return TalendTypeConvert.convertToJDBCType(getMetadataColumn().getTalendType());
    }

    @Override
    public String getElementName() {
        String name = super.getElementName();
        if (name == null) {
            name = this.getMetadataColumn().getLabel();
        }
        return name;
    }
}
