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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * DOC zshen class global comment. Detailled comment
 */
public class MultipleSelectionCombo extends Composite {

    private Text textContent = null;

    private java.util.List<String> items = new ArrayList<String>();

    private int[] usrSelection = null;

    private Shell floatShell = null;

    private List itemList = null;

    private Button arrowButton;

    private Color gray = null;

    public MultipleSelectionCombo(Composite parent, int style) {
        super(parent, style);
        init();
    }

    public MultipleSelectionCombo(Composite parent, String[] items, int[] selection, int style) {
        super(parent, style);
        usrSelection = selection;
        this.items = Arrays.asList(items);
        init();
    }

    private void init() {
        usrSelection = new int[] {};
        GridLayout layout = new GridLayout();
        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.numColumns = 2;
        layout.horizontalSpacing = 0;
        setLayout(layout);
        textContent = new Text(this, SWT.BORDER | SWT.READ_ONLY);
        gray = textContent.getBackground();
        textContent.setLayoutData(new GridData(GridData.FILL_BOTH));

        displayText();

        textContent.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent event) {
                super.mouseDown(event);
                initFloatShell();
            }

        });
        arrowButton = new Button(this, SWT.ARROW | SWT.DOWN);
        arrowButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
        arrowButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent event) {
                super.mouseDown(event);
                initFloatShell();
            }

        });
    }

    private void initFloatShell() {
        Point p = textContent.getParent().toDisplay(textContent.getLocation());
        Point textsize = textContent.getSize();
        Point arrowSize = arrowButton.getSize();
        Rectangle shellRect = new Rectangle(p.x, p.y + textsize.y, textsize.x + arrowSize.x, 0);
        floatShell = new Shell(MultipleSelectionCombo.this.getShell(), SWT.NO_TRIM);

        GridLayout gl = new GridLayout();
        gl.marginBottom = 2;
        gl.marginTop = 2;
        gl.marginRight = 2;
        gl.marginLeft = 2;
        gl.marginWidth = 0;
        gl.marginHeight = 0;
        floatShell.setLayout(gl);

        itemList = new List(floatShell, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        for (String value : items) {
            itemList.add(value);
        }

        itemList.setSelection(usrSelection);

        GridData gd = new GridData(GridData.FILL_BOTH);
        itemList.setLayoutData(gd);

        floatShell.setSize(shellRect.width, 100);
        floatShell.setLocation(shellRect.x, shellRect.y);

        itemList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent event) {
                super.mouseUp(event);
                usrSelection = itemList.getSelectionIndices();
                if ((event.stateMask & SWT.CTRL) == 0) {
                    floatShell.dispose();
                    displayText();
                }
            }
        });

        floatShell.addShellListener(new ShellAdapter() {

            public void shellDeactivated(ShellEvent arg0) {
                if (floatShell != null && !floatShell.isDisposed()) {
                    usrSelection = itemList.getSelectionIndices();
                    displayText();
                    floatShell.dispose();
                }
            }
        });
        floatShell.open();
    }

    private void displayText() {
        if (usrSelection != null && usrSelection.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < usrSelection.length; i++) {
                if (i > 0)
                    sb.append(",");//$NON-NLS-1$
                sb.append(items.get(usrSelection[i]));
            }
            textContent.setText(sb.toString());
        } else {
            textContent.setText("");//$NON-NLS-1$
        }
    }

    public void removeAll() {
        if (itemList != null && !itemList.isDisposed()) {
            itemList.removeAll();
        }
        this.items.clear();
    }

    public void addModifyListener(ModifyListener listener) {
        textContent.addModifyListener(listener);
    }

    public void add(String string) {
        if (itemList != null && !itemList.isDisposed()) {
            this.itemList.add(string);
            this.items = Arrays.asList(this.itemList.getItems());
        } else {
            if (this.items == null) {
                this.items = new ArrayList<String>();
            }
            this.items.add(string);
        }
    }

    public int getItemCount() {

        return this.items.size();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {

            textContent.setBackground(new Color(null, 255, 255, 255));
        } else {
            textContent.setBackground(gray);

        }
        textContent.setEnabled(enabled);
        arrowButton.setEnabled(enabled);
    }
}
