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
package org.talend.dataprofiler.core.ui.views.layout;

import java.awt.Dimension;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 */
public abstract class AWTLayout extends Layout {

    /**
     * Key under which an eventual preferred size (set with setPreferredSize) is stored as a user data in the SWT
     * control.
     */
    public static final String KEY_PREFERRED_SIZE = "preferredSize"; //$NON-NLS-1$

    /**
     * Gets the preferred size of a component. If a preferred size has been set with setPreferredSize, returns it,
     * otherwise returns the component computed preferred size.
     */
    protected Point getPreferredSize(Control control, int wHint, int hHint, boolean changed) {
        // check if a preferred size was set on the control with
        // SWTComponent.setPreferredSize(Dimension)
        Dimension d = (Dimension) control.getData(KEY_PREFERRED_SIZE);
        if (d != null)
            return new Point(d.width, d.height);
        return control.computeSize(wHint, hHint, changed);
    }
}
