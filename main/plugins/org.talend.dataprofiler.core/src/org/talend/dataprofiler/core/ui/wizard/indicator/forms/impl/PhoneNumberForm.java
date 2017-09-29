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

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.TextParameters;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class PhoneNumberForm extends AbstractIndicatorForm {

    private String oldCountryName;

    private int countryToSelect = 0;

    private CCombo combo;

    private String selectCountryCode = PluginConstant.EMPTY_STRING;

    public PhoneNumberForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);

        setupForm();
    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (combo != null) {
            selectCountryCode = combo.getData(combo.getText()).toString();
        }
        Indicator indicator = (Indicator) parameters.eContainer();
        IndicatorHelper.propagateCountyCodeInChildren(indicator, selectCountryCode);
        return true;
    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm#getFormEnum()
     */
    @Override
    public FormEnum getFormEnum() {
        return FormEnum.PhoneNumberForm;
    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        return false;
    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        combo.setItems(initiateCountryList());
        combo.select(countryToSelect);

    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // TODO Auto-generated method stub

    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        this.setLayout(new GridLayout());

        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText(DefaultMessagesImpl.getString("PhoneNumberForm.setCountryCode")); //$NON-NLS-1$
        // GridData gdText = new GridData(GridData.FILL_HORIZONTAL);
        Label countryLable = new Label(group, SWT.NONE);
        countryLable.setText(DefaultMessagesImpl.getString("PhoneNumberForm.country"));
        combo = new CCombo(group, SWT.BORDER | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); //$NON-NLS-1$

    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        combo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateStatus(IStatus.OK, MSG_OK);

            }

        });

    }

    /* (non-Jsdoc)
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * DOC qiongli Comment method "initiateCountryList".
     * 
     * @return
     */
    private String[] initiateCountryList() {
        SortedSet<String> countryList = new TreeSet<String>();
        TextParameters textParameter = parameters.getTextParameter();
        String oldCountryCode = null;
        if (textParameter != null) {
            oldCountryCode = textParameter.getCountryCode();
        }
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().compareTo("") != 0) { //$NON-NLS-1$
                countryList.add(locale.getDisplayCountry());
                combo.setData(locale.getDisplayCountry(), locale.getCountry());
                if (oldCountryCode != null && oldCountryCode.equals(locale.getCountry())) {
                    oldCountryName = locale.getDisplayCountry();
                }
            }
        }

        String defaultCountry = Locale.getDefault().getDisplayCountry();
        // give a value of "United States" to defaultCountry when it is empty string.
        if (defaultCountry.equals(PluginConstant.EMPTY_STRING)) {
            String countryBySys = IndicatorHelper.getDefCountryCodeBySystem();
            Locale defLocalBySys = new Locale(Locale.getDefault().getLanguage(), countryBySys);
            defaultCountry = defLocalBySys.getDisplayCountry();
            // defaultCountry = "United States";
        }

        int i = 0;

        if (oldCountryName != null) {
            for (String country : countryList) {

                if (country.equals(oldCountryName)) {
                    countryToSelect = i;
                    // combo.select(i);
                    break;
                }
                i++;
            }
        } else {
            for (String country : countryList) {
                if (country.equals(defaultCountry)) {
                    countryToSelect = i;
                    // combo.select(i);
                    break;
                }
                i++;
            }
        }
        return countryList.toArray(new String[] {});
    }


}
