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
package org.talend.dataprofiler.core.ui.grid;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * created by talend on Dec 26, 2014 Detailled comment
 * 
 */
public class TdColumnEmptyHeaderRenderer extends TdColumnHeaderRenderer {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.TdColumnHeaderRenderer#computeSize(org.eclipse.swt.graphics.GC, int,
     * int, java.lang.Object)
     */
    @Override
    public Point computeSize(GC gc, int wHint, int hHint, Object value) {
        Point computeSize = super.computeSize(gc, wHint, hHint, value);
        computeSize.y = 0;

        return computeSize;
    }

}
