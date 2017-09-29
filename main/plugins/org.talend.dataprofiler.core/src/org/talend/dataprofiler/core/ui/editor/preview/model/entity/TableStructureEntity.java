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
package org.talend.dataprofiler.core.ui.editor.preview.model.entity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class TableStructureEntity {

    private String[] fieldNames;

    private Integer[] fieldWidths;

    public String[] getFieldNames() {
        return fieldNames;
    }

    public Integer[] getFieldWidths() {
        return fieldWidths;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public void setFieldWidths(Integer[] fieldWidths) {
        this.fieldWidths = fieldWidths;
    }

    public boolean isValid() {
        return fieldNames.length == fieldWidths.length;
    }

    public int getColumnCount() {
        return fieldNames.length;
    }
}
