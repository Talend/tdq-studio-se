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
package org.talend.dataprofiler.rcp.intro;

import java.io.PrintWriter;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
public class DynamicContentProvider implements IIntroXHTMLContentProvider {

    @Override
    public void init(IIntroContentProviderSite site) {
        // needn't to do anything???
    }

    @Override
    public void createContent(String id, PrintWriter out) {
        // needn't to do anything???
    }

    @Override
    public void createContent(String id, Composite parent, FormToolkit toolkit) {
        // needn't to do anything???
    }

    private String getCurrentTimeString() {
        StringBuffer content = new StringBuffer("Dynamic content from Intro ContentProvider: "); //$NON-NLS-1$
        content.append("Current time is: "); //$NON-NLS-1$
        content.append(new Date(System.currentTimeMillis()));
        return content.toString();
    }

    @Override
    public void createContent(String id, Element parent) {
        Document dom = parent.getOwnerDocument();
        Element para = dom.createElement("p"); //$NON-NLS-1$
        para.setAttribute("id", "someDynamicContentId"); //$NON-NLS-1$ //$NON-NLS-2$
        para.appendChild(dom.createTextNode(getCurrentTimeString()));
        parent.appendChild(para);

    }

    @Override
    public void dispose() {
        // needn't to do anything???
    }
}
