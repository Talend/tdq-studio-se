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
package org.talend.dataprofiler.rcp.intro;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.dataprofiler.rcp.Activator;

public class ImageAction extends Action {

    private final IWorkbenchWindow window;

    private String url;

    public ImageAction(IWorkbenchWindow window, String imagePath, String url, String tipText) {
        this.window = window;
        this.url = url;
        ImageDescriptor imageDescriptorFromPlugin = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
        setImageDescriptor(imageDescriptorFromPlugin);
        setToolTipText(tipText);
    }

    @Override
    public void run() {
        if (window != null) {
            openBrower(url);
        }
    }

    protected void openBrower(String url) {
        Program.launch(url);
    }
}
