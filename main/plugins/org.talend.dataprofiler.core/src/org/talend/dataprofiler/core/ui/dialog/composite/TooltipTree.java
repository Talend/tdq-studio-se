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
package org.talend.dataprofiler.core.ui.dialog.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class TooltipTree extends Tree {

    private static final String TREEITEM = "_TREEITEM"; //$NON-NLS-1$

    private final Composite parent;

    /**
     * DOC zqin TooltipTree constructor comment.
     * 
     * @param parent
     * @param style
     */
    public TooltipTree(Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        init();
    }

    /*
     * Disable the judge of subclass.
     * 
     * @see org.eclipse.swt.widgets.TreeItem#checkSubclass()
     */
    @Override
    protected void checkSubclass() {
    }

    private void init() {
        final Listener labelListener = new Listener() {

            public void handleEvent(Event event) {
                Label label = (Label) event.widget;
                Shell shell = label.getShell();
                switch (event.type) {
                case SWT.MouseDown:
                    Event e = new Event();
                    e.item = (TreeItem) label.getData(TREEITEM);
                    // Assuming table is single select, set the selection as if
                    // the mouse down event went through to the table
                    setSelection(new TreeItem[] { (TreeItem) e.item });
                    notifyListeners(SWT.Selection, e);
                    shell.dispose();
                    // MOD yyi 2011-06-13:20344: sync tree layout on adding pattern
                    // setFocus();
                    break;
                case SWT.MouseExit:
                    shell.dispose();
                    break;
                default:
                }
            }
        };

        Listener treeListener = new Listener() {

            Shell tip = null;

            Label label = null;

            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.Dispose:
                case SWT.KeyDown:
                case SWT.MouseMove:
                    if (tip == null) {
                        break;
                    }
                    tip.dispose();
                    tip = null;
                    label = null;
                    break;
                case SWT.MouseHover:
                    TreeItem item = getItem(new Point(event.x, event.y));
                    if (isValidItem(item)) {
                        if (tip != null && !tip.isDisposed()) {
                            tip.dispose();
                        }
                        tip = new Shell(parent.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
                        tip.setBackground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                        FillLayout layout = new FillLayout();
                        layout.marginWidth = 2;
                        tip.setLayout(layout);
                        label = new Label(tip, SWT.WRAP);
                        label.setForeground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                        label.setBackground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                        label.setData(TREEITEM, item);
                        String displayedText = getItemTooltipText(item);
                        label.setText(displayedText);
                        label.addListener(SWT.MouseExit, labelListener);
                        label.addListener(SWT.MouseDown, labelListener);
                        // MOD 2009-01-07 mzhao wrap text if length too long.
                        int widthHit = SWT.DEFAULT;
                        if (displayedText.length() > 100) {
                            widthHit = 500;
                        }
                        Point size = tip.computeSize(widthHit, SWT.DEFAULT);
                        Rectangle rect = item.getBounds(0);
                        Point pt = toDisplay(rect.x + 80, rect.y);
                        tip.setBounds(pt.x, pt.y, size.x, size.y);
                        tip.setVisible(true);
                    }
                default:
                }
            }
        };
        this.addListener(SWT.Dispose, treeListener);
        this.addListener(SWT.KeyDown, treeListener);
        this.addListener(SWT.MouseMove, treeListener);
        this.addListener(SWT.MouseHover, treeListener);
    }

    protected boolean isValidItem(TreeItem item) {
        return item != null;
    }

    protected String getItemTooltipText(TreeItem item) {
        return item.getText();
    }

}
