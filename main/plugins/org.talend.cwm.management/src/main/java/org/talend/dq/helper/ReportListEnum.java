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


/**
 * DOC xqliu  class global comment. Detailled comment
 */
public enum ReportListEnum {

    Name(0, "Name"), //$NON-NLS-1$
    Path(1, "Path"), //$NON-NLS-1$
    CreateTime(2, "CreateTime"); //$NON-NLS-1$

    private String literal;

    private int index;

    public int getIndex() {
        return index;
    }

    public String getLiteral() {
        return literal;
    }

    ReportListEnum(int index, String literal) {
        this.index = index;
        this.literal = literal;
    }
}
