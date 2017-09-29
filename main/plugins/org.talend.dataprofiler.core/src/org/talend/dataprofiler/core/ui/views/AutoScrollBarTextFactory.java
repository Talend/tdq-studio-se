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
package org.talend.dataprofiler.core.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * DOC sizhaoliu class global comment. Detailled comment
 */
public class AutoScrollBarTextFactory {

    private static AutoScrollBarTextFactory instance;

    public static AutoScrollBarTextFactory getDefault() {
        if (instance == null) {
            instance = new AutoScrollBarTextFactory();
        }
        return instance;
    }

    public Text createText(Composite parent, int style) {
        Text text = new Text(parent, style | SWT.V_SCROLL);
        text.getVerticalBar().setVisible(false);
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                Text text = (Text) e.getSource();
                if (text.getLineCount() > text.getSize().y / text.getLineHeight()) {
                    text.getVerticalBar().setVisible(true);
                } else {
                    text.getVerticalBar().setVisible(false);
                }
            }
        });
        return text;
    }

}
