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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 */
public class BorderLayout extends AWTLayout {

    public static final String CENTER = "Center"; //$NON-NLS-1$

    public static final String EAST = "East"; //$NON-NLS-1$

    public static final String NORTH = "North"; //$NON-NLS-1$

    public static final String SOUTH = "South"; //$NON-NLS-1$

    public static final String WEST = "West"; //$NON-NLS-1$

    // -----------------------

    private int hgap, vgap;

    private Control centerChild, eastChild, northChild, southChild, westChild;

    public BorderLayout() {
        super();
    }

    public BorderLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
        readLayoutData(composite);
        Point size = new Point(0, 0);

        Point preferredSize;
        if (northChild != null) {
            preferredSize = getPreferredSize(northChild, wHint, SWT.DEFAULT, flushCache);
            size.y += preferredSize.y + vgap;
        }
        if (southChild != null) {
            preferredSize = getPreferredSize(southChild, wHint, SWT.DEFAULT, flushCache);
            size.y += preferredSize.y + vgap;
        }
        if (westChild != null) {
            preferredSize = getPreferredSize(westChild, SWT.DEFAULT, hHint, flushCache);
            size.x += preferredSize.x + hgap;
        }
        if (eastChild != null) {
            preferredSize = getPreferredSize(eastChild, SWT.DEFAULT, hHint, flushCache);
            size.x += preferredSize.x + hgap;
        }
        if (centerChild != null) {
            preferredSize = getPreferredSize(centerChild, wHint, hHint, flushCache);
            size.x += preferredSize.x;
            size.y += preferredSize.y;
        }
        return size;
    }

    protected void layout(Composite composite, boolean flushCache) {
        readLayoutData(composite);
        Rectangle clientArea = composite.getClientArea();
        int top = clientArea.y;
        int bottom = clientArea.y + clientArea.height;
        int left = clientArea.x;
        int right = clientArea.x + clientArea.width;

        Point preferredSize;
        if (northChild != null) {
            preferredSize = getPreferredSize(northChild, clientArea.width, SWT.DEFAULT, flushCache);
            northChild.setBounds(left, top, right - left, preferredSize.y);
            top += preferredSize.y + vgap;
        }
        if (southChild != null) {
            preferredSize = getPreferredSize(southChild, clientArea.width, SWT.DEFAULT, flushCache);
            southChild.setBounds(left, bottom - preferredSize.y, right - left, preferredSize.y);
            bottom -= preferredSize.y + vgap;
        }
        if (westChild != null) {
            preferredSize = getPreferredSize(westChild, SWT.DEFAULT, bottom - top, flushCache);
            westChild.setBounds(left, top, preferredSize.x, bottom - top);
            left += preferredSize.x + hgap;
        }
        if (eastChild != null) {
            preferredSize = getPreferredSize(eastChild, SWT.DEFAULT, bottom - top, flushCache);
            eastChild.setBounds(right - preferredSize.x, top, preferredSize.x, bottom - top);
            right -= preferredSize.x + hgap;
        }
        if (centerChild != null) {
            centerChild.setBounds(left, top, right - left, bottom - top);
        }
    }

    /**
     * Read the layout data of the children of a composite.
     * 
     * @param composite the parent composite
     */
    private void readLayoutData(Composite composite) {
        northChild = null;
        southChild = null;
        eastChild = null;
        westChild = null;
        centerChild = null;

        Control[] children = composite.getChildren();
        for (int i = 0; i < children.length; i++) {
            // if (!children[i].isVisible())
            // continue;
            Object layoutData = children[i].getLayoutData();
            if (NORTH.equals(layoutData)) {
                northChild = children[i];
            } else if (SOUTH.equals(layoutData)) {
                southChild = children[i];
            } else if (EAST.equals(layoutData)) {
                eastChild = children[i];
            } else if (WEST.equals(layoutData)) {
                westChild = children[i];
            } else {
                centerChild = children[i];
            }
        }
    }

    /**
     * @return Returns the hgap.
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * @param hgap The hgap to set.
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * @return Returns the vgap.
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * @param vgap The vgap to set.
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }
}
