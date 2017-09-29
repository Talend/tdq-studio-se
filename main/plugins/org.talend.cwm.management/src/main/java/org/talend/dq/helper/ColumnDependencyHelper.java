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

import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;


/**
 * DOC xqliu  class global comment. Detailled comment
 */
public final class ColumnDependencyHelper {

    private ColumnDependencyHelper() {
    }
    
    public static String getIndicatorName(Indicator indicator) {
        assert indicator instanceof ColumnDependencyIndicator;
        ColumnDependencyIndicator cdIndicator = (ColumnDependencyIndicator) indicator;

        assert cdIndicator.getColumnA() != null;
        assert cdIndicator.getColumnB() != null;

        return cdIndicator.getColumnA().getName() + "-->" + cdIndicator.getColumnB().getName(); //$NON-NLS-1$
    }
}
