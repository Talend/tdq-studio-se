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

import org.eclipse.nebula.widgets.grid.AbstractRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * A renderer for the last empty column header.
 */
public class TdEmptyColumnHeaderRenderer extends AbstractRenderer {

    /**
     * {@inheritDoc}
     */
    public Point computeSize(GC gc, int wHint, int hHint, Object value) {
        return new Point(wHint, hHint);
    }

    /**
     * {@inheritDoc}
     */
    public void paint(GC gc, Object value) {
        gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        gc.drawLine(getBounds().x, getBounds().height - 1, getBounds().x + getBounds().width + 1, getBounds().height - 1);
    }
}
