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

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.CheckValueUtils;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class NumbericNominalForm extends AbstractIndicatorForm {

    private Text numberTxt;

    /**
     * DOC zqin NumbericNominalForm constructor comment.
     *
     * @param parent
     * @param style
     * @param parameter
     */
    public NumbericNominalForm(Composite parent, int style, IndicatorParameters parameters) {
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

        Group group = new Group(this, SWT.NONE);
        group.setText(limitResultsGrp);
        group.setLayout(new GridLayout(2, false));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        group.setLayoutData(gd);

        Label lab = new Label(group, SWT.NONE);
        lab.setText(DefaultMessagesImpl.getString("NumbericNominalForm.numberOfResultsShown")); //$NON-NLS-1$

        numberTxt = new Text(group, SWT.BORDER);
        GridData gdTxt = new GridData();
        gdTxt.widthHint = 100;
        numberTxt.setLayoutData(gdTxt);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        numberTxt.addModifyListener(new ModifyListener() {

            // MOD scorreia annotation commented out for Java 5 compilation @Override
            public void modifyText(ModifyEvent e) {

                checkFieldsValue();

                if (isStatusOnValid()) {
                    String topN = numberTxt.getText();
                    parameters.setTopN(Integer.parseInt(topN));
                }
            }

        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        if (numberTxt.getText().equals(StringUtils.EMPTY)) {
            updateStatus(IStatus.ERROR, MSG_EMPTY);
            return false;
        }

        if (!CheckValueUtils.isNumberOfShownValue(numberTxt.getText())) {
            updateStatus(IStatus.ERROR, MSG_ONLY_NUMBER);
            return false;
        }

        updateStatus(IStatus.OK, MSG_OK);
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        numberTxt.setText(String.valueOf(parameters.getTopN()));
    }

    @Override
    public boolean performFinish() {
        if (!StringUtils.equals(numberTxt.getText(), String.valueOf(parameters.getTopN()))) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.utils.AbstractIndicatorForm#getFormEnum()
     */
    @Override
    public FormEnum getFormEnum() {
        return FormEnum.NumbericNominalForm;
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
}
