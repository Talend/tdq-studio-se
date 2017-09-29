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
package org.talend.dataprofiler.rcp.intro.linksbar;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.dataprofiler.rcp.Activator;
import org.talend.dataprofiler.rcp.i18n.Messages;

/**
 * DOC xtan class global comment. Detailled comment <br/>
 * 
 */
public class LinksToolbarItem extends ContributionItem {

    public static final String COOLITEM_LINKS_ID = Activator.PLUGIN_ID + ".CoolItemLinks"; //$NON-NLS-1$
    
    private ToolItem toolitem;

    private static final String LEARN_URL = "<a href=\"http://www.talendforge.org/tutorials/menu.php\">Learn</a>"; //$NON-NLS-1$

    private static final String ASK_URL = "<a href=\"https://community.talend.com/\">Ask</a>"; //$NON-NLS-1$

    // private static final String SHARE_URL = "<a href=\"http://www.talendforge.org/exchange/\">Share</a>"; //$NON-NLS-1$

    private static final String UPGRADE_URL = "<a href=\"http://www.talend.com/products/why-upgrade/\">Upgrade!</a>"; //$NON-NLS-1$

    @Override
    public void fill(ToolBar parent, int index) {
        // parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

        toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
        Control control = createControl(parent);
        toolitem.setWidth(400);
        toolitem.setControl(control);
    }

    protected Control createControl(Composite parent) {

        final Composite composite = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(12, false);
        layout.marginHeight = 0;
        composite.setLayout(layout);

        // 1.learn
        Label learnLabel = new Label(composite, SWT.NONE);

        learnLabel.setImage(Activator.getImageDescriptor("icons/demo.png").createImage()); //$NON-NLS-1$

        Link learn = new Link(composite, SWT.NONE);
        learn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        learn.setText(LEARN_URL);
        learn.setToolTipText(Messages.getString("LinksToolbarItem_0"));//$NON-NLS-1$

        learn.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                openBrower(event.text);
            }
        });

        // 2.ask
        Label askLabel = new Label(composite, SWT.NONE);

        askLabel.setImage(Activator.getImageDescriptor("icons/irc_protocol.png").createImage()); //$NON-NLS-1$

        Link ask = new Link(composite, SWT.NONE);
        ask.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        ask.setText(ASK_URL);
        ask.setToolTipText(Messages.getString("LinksToolbarItem_7"));//$NON-NLS-1$

        ask.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                openBrower(event.text);
            }
        });

        // 4.upgrade
        Label upgradeLabel = new Label(composite, SWT.NONE);
        upgradeLabel.setImage(Activator.getImageDescriptor("icons/wizard.png").createImage()); //$NON-NLS-1$
        Link upgrade = new Link(composite, SWT.NONE);
        upgrade.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        upgrade.setText(UPGRADE_URL);
        upgrade.setToolTipText(Messages.getString("LinksToolbarItem_11"));//$NON-NLS-1$

        upgrade.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                openBrower(event.text);
            }
        });

        return composite;
    }

    private void openBrower(String url) {
        Program.launch(url);
    }

}
