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

import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * The renderer for a cell in Grid.
 */
public class TdCellRenderer extends AbstractTdCellRenderer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(GC gc, Object value) {
        GridItem item = (GridItem) value;
        gc.setAntialias(SWT.ON);

        int column = getColumn();
        boolean checkable = item.getCheckable(column);
        boolean checked = item.getChecked(column);

        // fill background rectangle
        Color systemBackColor = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        if (checkable) {
            gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        } else {
            gc.setBackground(IndicatorSelectGrid.gray);
        }

        int originX = getBounds().x;
        gc.fillRectangle(originX, getBounds().y, getBounds().width, getBounds().height);

        if (checkable) {
            // draw highlight color as background
            Color highlight = item.getBackground(column);
            if (highlight != null) {
                gc.setBackground(highlight);
                gc.fillRectangle(originX, getBounds().y, getBounds().width, getBounds().height);
            }

            if (checked) {
                // draw background oval
                int offset = (getBounds().width - getBounds().height);
                gc.setBackground(IndicatorSelectGrid.blue);
                gc.fillOval(originX + offset / 2 + 2, getBounds().y + 2, getBounds().width - offset - 4, getBounds().height - 4);

                // draw a white oval for partially selected cells
                if (item.getGrayed(column)) {
                    gc.setBackground(systemBackColor);
                    gc.setAlpha(160);
                    gc.fillOval(originX + offset / 2 + 2, getBounds().y + 2, getBounds().width - offset - 4,
                            getBounds().height - 4);
                    gc.setAlpha(-1);
                }

                // draw tick image
                if (highlight != null) {
                    gc.setForeground(highlight);
                } else {
                    gc.setForeground(systemBackColor);
                }
                gc.setLineWidth(3);
                gc.drawLine(originX + offset / 2 + 4, getBounds().y + 10, originX + offset / 2 + getBounds().height - 12,
                        getBounds().y + 15);
                gc.drawLine(originX + offset / 2 + 7, getBounds().y + 15, originX + offset / 2 + getBounds().height - 5,
                        getBounds().y + 6);
                gc.setLineWidth(1);
            }
        }
        if (item.getParent().getLinesVisible()) {
            gc.setForeground(item.getParent().getLineColor());
            gc.drawLine(originX, getBounds().y + getBounds().height, originX + getBounds().width - 1, getBounds().y
                    + getBounds().height);
            gc.drawLine(originX + getBounds().width - 1, getBounds().y, originX + getBounds().width - 1, getBounds().y
                    + getBounds().height);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point computeSize(GC gc, int wHint, int hHint, Object value) {
        GridItem item = (GridItem) value;

        gc.setFont(item.getFont(getColumn()));

        int x = 0;

        x += leftMargin;

        if (isCheck()) {
            x += getBounds().width + insideMargin;
        }

        int y = 0;

        Image image = item.getImage(getColumn());
        if (image != null) {
            y = topMargin + image.getBounds().height + bottomMargin;

            x += image.getBounds().width + insideMargin;
        }

        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean notify(int event, Point point, Object value) {
        return false;
    }

}
