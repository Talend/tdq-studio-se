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
package org.talend.dataprofiler.core.ui.wizard.analysis.connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizardPage;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;

/**
 * DOC rli class global comment. Detailled comment
 */
public class AnalysisFilterPage extends AbstractAnalysisWizardPage {

    private String defaultInfor = DefaultMessagesImpl.getString("ConnAnalysisPageStep1.defaultInfor"); //$NON-NLS-1$

    private Text tableFilter;

    private Text viewFilter;

    public AnalysisFilterPage() {
        super();
    }

    public void createControl(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 70;
        Label inforLabel = new Label(container, SWT.BORDER);
        inforLabel.setText(defaultInfor);
        inforLabel.setLayoutData(gd);

        Composite subContainer = new Composite(container, SWT.NONE);
        GridLayout subLayout = new GridLayout(2, false);
        subContainer.setLayout(subLayout);
        subContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label labelTable = new Label(subContainer, SWT.NONE);
        labelTable.setText(DefaultMessagesImpl.getString("ConnAnalysisPageStep1.tableNameFilter")); //$NON-NLS-1$
        tableFilter = new Text(subContainer, SWT.BORDER);
        tableFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        tableFilter.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                ((AnalysisFilterParameter) getConnectionParams()).setTableFilter(tableFilter.getText());

            }
        });

        Label labelView = new Label(subContainer, SWT.NONE);
        labelView.setText(DefaultMessagesImpl.getString("ConnAnalysisPageStep1.viewNameFilter")); //$NON-NLS-1$
        viewFilter = new Text(subContainer, SWT.BORDER);
        viewFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        viewFilter.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                ((AnalysisFilterParameter) getConnectionParams()).setViewFilter(viewFilter.getText());

            }
        });
        setControl(container);
    }
}
