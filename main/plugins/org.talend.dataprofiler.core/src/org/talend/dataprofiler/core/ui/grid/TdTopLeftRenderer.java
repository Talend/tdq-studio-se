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
 * The renderer for the empty top left area when both column and row headers are visible.
 */
public class TdTopLeftRenderer extends AbstractRenderer {

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
        gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

        gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width - 1, getBounds().height);

        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));

        gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x + getBounds().width - 1, getBounds().y
                + getBounds().height);

        gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width, getBounds().y
                + getBounds().height - 1);

    }
}
