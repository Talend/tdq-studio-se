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

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class TimeSlicesForm extends AbstractIndicatorForm {

    private Button btn;

    private ArrayList<Button> allBtns = new ArrayList<Button>();

    public TimeSlicesForm(Composite parent, int style, IndicatorParameters parameters) {
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
        this.setLayout(new GridLayout());

        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(DateGrain.VALUES.size() / 2, true));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText(DefaultMessagesImpl.getString("TimeSlicesForm.AggregatedateBy")); //$NON-NLS-1$

        for (DateGrain oneDate : DateGrain.VALUES) {
            btn = new Button(group, SWT.RADIO);
            btn.setText(oneDate.getLiteral());
            allBtns.add(btn);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        for (final Button button : allBtns) {
            button.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    String timeUnit = button.getText();
                    DateGrain dateGrain = DateGrain.get(timeUnit);
                    DateParameters dateParameters = parameters.getDateParameters();
                    if (dateParameters == null) {
                        dateParameters = IndicatorsFactory.eINSTANCE.createDateParameters();
                    }
                    dateParameters.setDateAggregationType(dateGrain);
                    updateStatus(IStatus.OK, MSG_OK);
                }

            });
        }
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        if (parameters != null) {
            DateParameters dateParameters = parameters.getDateParameters();
            if (dateParameters != null) {
                DateGrain dateGrain = dateParameters.getDateAggregationType();

                for (Button oneBtn : allBtns) {
                    if (oneBtn.getText().equals(dateGrain.getLiteral())) {

                        oneBtn.setSelection(true);
                    }
                }
            }
        }
    }

    @Override
    public FormEnum getFormEnum() {
        return FormEnum.TimeSlicesForm;
    }

    @Override
    public boolean performFinish() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }
}
