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
package org.talend.dataprofiler.core.ui.pref;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * @author scorreia
 *
 * Main page for Data profiler preferences.
 */
public class DataProfilerPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public DataProfilerPreferencePage() {
    }

    public DataProfilerPreferencePage(String title) {
        super(title);
    }

    public DataProfilerPreferencePage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.FILL);
        GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        // gridData.horizontalSpan = 4;
        composite.setLayoutData(gridData);
        Label label = new Label(composite, SWT.NONE);
        label.setText(DefaultMessagesImpl.getString("DataProfilerPreferencePage.UnfoldFolder")); //$NON-NLS-1$
        CorePlugin.getDefault().handleUserReadOnlyStatus(composite);
        return composite;
    }

    public void init(IWorkbench workbench) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#contributeButtons(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void contributeButtons(Composite parent) {
        super.contributeButtons(parent);
        CorePlugin.getDefault().handleUserReadOnlyStatus(parent);
    }
}
