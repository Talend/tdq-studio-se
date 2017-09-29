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
package org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ExpectedValueForm extends AbstractIndicatorForm {

    private Text expectedValue;

    /**
     * DOC zqin ExpectedValueForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param parameter
     */
    public ExpectedValueForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        this.setLayout(new GridLayout(2, false));
        this.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label label = new Label(this, SWT.NONE);
        label.setText(DefaultMessagesImpl.getString("ExpectedValueForm.expectedValue")); //$NON-NLS-1$

        expectedValue = new Text(this, SWT.BORDER);
        expectedValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        expectedValue.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateStatus(IStatus.OK, MSG_OK);
                IndicatorHelper.setIndicatorExpectedValue(parameters, expectedValue.getText());
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {

        String existedExpectedValue = IndicatorHelper.getExpectedValue(parameters);
        if (existedExpectedValue != null) {
            expectedValue.setText(existedExpectedValue);
        }
    }

    @Override
    public boolean performFinish() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractIndicatorForm#getFormEnum()
     */
    @Override
    public FormEnum getFormEnum() {
        return FormEnum.ExpectedValueForm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        return false;
    }
}
