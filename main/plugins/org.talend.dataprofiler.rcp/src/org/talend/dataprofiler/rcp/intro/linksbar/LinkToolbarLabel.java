// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.rcp.intro.linksbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

public class LinkToolbarLabel extends LinksToolbarItem {

    private String url;

    private String tipText;

    public LinkToolbarLabel(String url, String tipText) {
        this.url = url;
        this.tipText = tipText;

    }

    @Override
    protected Control createControl(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);

        composite.setLayout(new FormLayout());

        Label label = new Label(composite, SWT.NONE);
        FormData labelData = new FormData();
        labelData.left = new FormAttachment(0, 0);
        labelData.top = new FormAttachment(0, 0);
        labelData.bottom = new FormAttachment(100, 0);
        label.setLayoutData(labelData);

        Link ask = new Link(composite, SWT.NONE);
        FormData formData = new FormData();
        formData.top = new FormAttachment(label, 0, SWT.CENTER);
        ask.setLayoutData(formData);
        ask.setText(url);
        ask.setToolTipText(tipText); // $NON-NLS-1$
        ask.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                openBrower(event.text);
            }
        });
        return composite;
    }
}
