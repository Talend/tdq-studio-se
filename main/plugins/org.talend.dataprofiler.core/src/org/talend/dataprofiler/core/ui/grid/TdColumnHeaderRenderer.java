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

import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.ITalendGridHeaderRendererHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

/**
 * The column header renderer.
 */
public class TdColumnHeaderRenderer extends AbstractColumnHerderRenderer implements ITalendGridHeaderRendererHandler {

    int leftMargin = 6;

    int rightMargin = 6;

    int topMargin = 3;

    int bottomMargin = 3;

    int imageSpacing;

    private TextLayout textLayout;

    /** rotation of the header text. */
    protected int _rotation;

    /** Transformations for rotated text. */
    protected Transform _transform;

    /** inverse transformation to reset gc. */
    protected Transform _transformInv;

    private double sinRotation;

    private double cosRotation;

    public TdColumnHeaderRenderer() {
        _transform = new Transform(Display.getCurrent());
        _transformInv = new Transform(Display.getCurrent());
        _transform.rotate(-_rotation);
        _transformInv.rotate(-_rotation);
        _transformInv.invert();
        sinRotation = Math.sin(_rotation * Math.PI / 180);
        cosRotation = Math.cos(_rotation * Math.PI / 180);
        imageSpacing = (int) (10 / sinRotation);
    }

    /**
     * {@inheritDoc}
     */
    public Point computeSize(GC gc, int wHint, int hHint, Object value) {
        GridColumn column = (GridColumn) value;

        gc.setFont(column.getHeaderFont());

        int x = leftMargin;
        int y = topMargin + gc.getFontMetrics().getHeight() + bottomMargin;

        if (column.getImage() != null) {
            x += column.getImage().getBounds().width + imageSpacing;
            y = Math.max(y, topMargin + column.getImage().getBounds().height + bottomMargin);
        }
        if (!isWordWrap()) {
            x += gc.stringExtent(column.getText()).x + rightMargin;
            y += gc.stringExtent(column.getText()).x * sinRotation;
        } else {
            int plainTextWidth;
            if (wHint == SWT.DEFAULT) {
                plainTextWidth = getBounds().width - x - rightMargin;
            } else {
                plainTextWidth = wHint - x - rightMargin;
            }

            getTextLayout(gc, column);
            textLayout.setText(column.getText());
            textLayout.setWidth(plainTextWidth < 1 ? 1 : plainTextWidth);

            x += plainTextWidth + rightMargin;

            int textHeight = topMargin;
            textHeight += textLayout.getBounds().height;
            textHeight += bottomMargin;

            y = Math.max(y, textHeight);
        }

        y += computeControlSize(column).y;

        return new Point(x, y);
    }

    /**
     * {@inheritDoc}
     */
    public void paint(GC gc, Object value) {

        gc.setAntialias(SWT.ON);
        gc.setTransform(_transform);

        float[] original = { (float) getBounds().x - 2, (float) getBounds().y + (float) getBounds().height - 2 };
        _transformInv.transform(original);

        GridColumn column = (GridColumn) value;

        // set the font to be used to display the text.
        gc.setFont(column.getHeaderFont());

        gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

        if (isSelected()) {
            gc.setBackground(IndicatorSelectGrid.highlightBlue);
            gc.fillRectangle((int) original[0], (int) original[1] + 2, (int) (getBounds().height / sinRotation)
                    + getBounds().width, (int) (getBounds().width * sinRotation));
        }

        int x = leftMargin;
        int width = getBounds().width - x;
        width -= rightMargin;

        float[] cords = { (getBounds().x + width / 2 - leftMargin), (getBounds().y + (float) getBounds().height - topMargin) };

        _transformInv.transform(cords);

        if (column.getImage() != null) {
            gc.drawImage(column.getImage(), (int) cords[0] + leftMargin + imageSpacing, (int) cords[1]);
            x += column.getImage().getBounds().width;
        }

        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));

        String text = column.getText();

        if (column.getAlignment() == SWT.RIGHT) {
            int len = gc.stringExtent(text).x;
            if (len < width) {
                x += width - len;
            }
        } else if (column.getAlignment() == SWT.CENTER) {
            int len = gc.stringExtent(text).x;
            if (len < width) {
                x += (width - len) / 2;
            }
        }

        gc.drawString(text, (int) cords[0] + x + imageSpacing, (int) cords[1] + topMargin);

        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        gc.drawLine((int) (original[0]), (int) (original[1]),
                (int) (original[0] + getBounds().height / sinRotation - imageSpacing), (int) (original[1]));
        gc.drawLine((int) (original[0]), (int) (original[1] + getBounds().width * sinRotation), (int) (original[0]
                + getBounds().height / sinRotation + getBounds().width * cosRotation - imageSpacing - leftMargin),
                (int) (original[1] + getBounds().width * sinRotation));

        gc.setTransform(null);

        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
        gc.drawLine(getBounds().x, getBounds().y + getBounds().height - 1, getBounds().x + getBounds().width, getBounds().y
                + getBounds().height - 1);
    }

    /**
     * {@inheritDoc}
     */
    public boolean notify(int event, Point point, Object value) {
        return false;
    }

    /**
     * @return the bounds reserved for the control
     */
    @Override
    protected Rectangle getControlBounds(Object value, boolean preferred) {
        Rectangle bounds = getBounds();
        GridColumn column = (GridColumn) value;
        Point controlSize = computeControlSize(column);

        int y = getBounds().y + getBounds().height - bottomMargin - controlSize.y;

        return new Rectangle(bounds.x + 3, y, bounds.width - 6, controlSize.y);
    }

    private Point computeControlSize(GridColumn column) {
        if (column.getHeaderControl() != null) {
            return column.getHeaderControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        }
        return new Point(0, 0);
    }

    private void getTextLayout(GC gc, GridColumn column) {
        if (textLayout == null) {
            textLayout = new TextLayout(gc.getDevice());
            textLayout.setFont(gc.getFont());
            column.getParent().addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    textLayout.dispose();
                }
            });
        }
        textLayout.setAlignment(column.getAlignment());
    }

    /**
     * Set the rotation of the header text. Please note that you have to call <code>redraw()</code> on the table
     * yourself if you change the rotation while the table is showing.
     * 
     * @param rotation rotation in degrees anti clockwise between 0 and 90 degrees.
     */
    public void setRotation(int rotation) {
        if (rotation < 0 || rotation > 90) {
            throw new IllegalArgumentException("Rotation range 0..90");
        }
        if (_rotation != rotation) {
            disposeTransformations();
            _rotation = rotation;
            _transform = new Transform(Display.getCurrent());
            _transformInv = new Transform(Display.getCurrent());
            _transform.rotate(-rotation);
            _transformInv.rotate(-rotation);
            _transformInv.invert();
            sinRotation = Math.sin(_rotation * Math.PI / 180);
            imageSpacing = (int) (10 / sinRotation);
        }
    }

    /**
     * Getter for rotation.
     * 
     * @return the rotation
     */
    public int getRotation() {
        return _rotation;
    }

    private void disposeTransformations() {
        if (_transform != null) {
            _transform.dispose();
        }
        if (_transformInv != null) {
            _transformInv.dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.grid.ITalendGridHeaderRendererHandle#dispose()
     */
    public void dispose() {
        disposeTransformations();
    }
}
