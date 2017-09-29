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

import java.util.HashSet;
import java.util.Set;

/**
 * DOC klliu class global comment. Detailled comment
 */
public enum ParserRuleLanguageEnum {
    Enumeration(0, "Enumeration"), //$NON-NLS-1$
    Combination(1, "Combination"), //$NON-NLS-1$
    RegExp(2, "RegExp"), //$NON-NLS-1$
    Index(3, "Index"), //$NON-NLS-1$
    Shape(4, "Shape"), //$NON-NLS-1$
    Format(5, "Format"), //$NON-NLS-1$
    Default(6, "Please Select...");//$NON-NLS-1$

    private String literal;

    private int index;

    public int getIndex() {
        return index;
    }

    public String getLiteral() {
        return literal;
    }

    ParserRuleLanguageEnum(int index, String literal) {
        this.index = index;
        this.literal = literal;
    }

    public static String[] getAllTypes() {
        Set<String> set = new HashSet<String>();

        for (ParserRuleLanguageEnum oneType : values()) {
            set.add(oneType.getLiteral());
        }
        return set.toArray(new String[set.size()]);
    }
}
