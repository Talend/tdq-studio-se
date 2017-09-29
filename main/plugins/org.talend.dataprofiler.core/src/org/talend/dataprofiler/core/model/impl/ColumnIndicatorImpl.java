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

import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.repository.model.IRepositoryNode;

/**
 * This class can store the all the Indicators of one TdColumn, and provide the method to access all indicator.
 * 
 */
public class ColumnIndicatorImpl extends ModelElementIndicatorImpl implements ColumnIndicator {

    public ColumnIndicatorImpl(IRepositoryNode reposNode) {
        super();
        this.setModelElement(reposNode);
    }

    public TdColumn getTdColumn() {
        return (TdColumn) ((MetadataColumnRepositoryObject) this.getModelElementRepositoryNode().getObject()).getTdColumn();
    }

    @Override
    public int getJavaType() {
        // return this.getTdColumn().getJavaType();
        return this.getTdColumn().getSqlDataType().getJavaDataType();
    }

    // public String getName() {
    // return getTdColumn().getName();
    // }
}
