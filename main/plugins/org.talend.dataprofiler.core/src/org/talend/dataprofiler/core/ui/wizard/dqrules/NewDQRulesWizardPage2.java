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
package org.talend.dataprofiler.core.ui.wizard.dqrules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizardPage;
import org.talend.dq.analysis.parameters.DQRulesParameter;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class NewDQRulesWizardPage2 extends AbstractWizardPage {

    protected Text whereText;

    public NewDQRulesWizardPage2() {
        setPageComplete(true);
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gdLayout = new GridLayout(2, false);
        container.setLayout(gdLayout);

        Label label = new Label(container, SWT.NONE);
        label.setText(DefaultMessagesImpl.getString("NewDQRulesWizard.whereClause")); //$NON-NLS-1$
        label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        whereText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData data = new GridData(GridData.FILL_BOTH);
        whereText.setLayoutData(data);

        addListeners();
        setControl(container);
    }

    /**
     * DOC bzhou Comment method "addListeners".
     */
    private void addListeners() {
        whereText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String whereExpression = whereText.getText();
                if (whereExpression != null && !"".equals(whereExpression)) { //$NON-NLS-1$
                    ((DQRulesParameter) getParameter()).setWhereClause(whereExpression);
                    setPageComplete(checkFieldsValue());
                }
            }
        });
    }

    public boolean checkFieldsValue() {
        return true;
    }
}
