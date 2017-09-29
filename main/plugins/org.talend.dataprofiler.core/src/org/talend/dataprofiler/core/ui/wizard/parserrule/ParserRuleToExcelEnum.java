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
package org.talend.dataprofiler.core.ui.wizard.parserrule;

/**
 * DOC zqin class global comment. Detailled comment
 */
public enum ParserRuleToExcelEnum {

    Label(0, "Label"), //$NON-NLS-1$
    Purpose(1, "Purpose"), //$NON-NLS-1$
    Description(2, "Description"), //$NON-NLS-1$
    Author(3, "Author"), //$NON-NLS-1$
    RelativePath(4, "Relative_Path"), //$NON-NLS-1$
    Name(5, "Name"), //$NON-NLS-1$
    Body(6, "Body"), //$NON-NLS-1$
    Language(7, "Language"), //$NON-NLS-1$
    Type(8, "Type"), //$NON-NLS-1$
    Value(9, "Value"); //$NON-NLS-1$

    private String literal;

    private int index;

    public int getIndex() {
        return index;
    }

    public String getLiteral() {
        return literal;
    }

    ParserRuleToExcelEnum(int index, String literal) {
        this.index = index;
        this.literal = literal;
    }
}
