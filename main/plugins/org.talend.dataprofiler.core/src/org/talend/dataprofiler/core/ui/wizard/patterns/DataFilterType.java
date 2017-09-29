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
package org.talend.dataprofiler.core.ui.wizard.patterns;

/**
 * zshen PatternsSelectPage class global comment. Detailled comment
 */
public enum DataFilterType {
    ALL_DATA("All data"), //$NON-NLS-1$
    MATCHES("matches"), //$NON-NLS-1$
    non_matches("non-matches");//$NON-NLS-1$

    String textName = null;

    private DataFilterType(String name) {
        textName = name;
    }

    public String getTextName() {
        return textName;
    }
}
