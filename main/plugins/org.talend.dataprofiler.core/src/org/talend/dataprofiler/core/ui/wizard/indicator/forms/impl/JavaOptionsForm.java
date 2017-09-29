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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class JavaOptionsForm extends AbstractIndicatorForm {

    protected Text characters2ReplaceText, replacementCharactersText;

    public JavaOptionsForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
        setupForm();
    }

    @Override
    public FormEnum getFormEnum() {
        return FormEnum.JavaOptionsForm;
    }

    @Override
    public boolean performFinish() {
        if (checkFieldsValue()) {
            this.parameters.getTextParameter().setCharactersToReplace(this.characters2ReplaceText.getText());
            this.parameters.getTextParameter().setReplacementCharacters(this.replacementCharactersText.getText());
        }
        return true;
    }

    @Override
    protected void adaptFormToReadOnly() {

    }

    @Override
    protected void addFields() {
        this.setLayout(new GridLayout());

        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText(DefaultMessagesImpl.getString("JavaOptionsForm.replace")); //$NON-NLS-1$

        Label label1 = new Label(group, SWT.NONE);
        label1.setText(DefaultMessagesImpl.getString("JavaOptionsForm.replace1")); //$NON-NLS-1$
        characters2ReplaceText = new Text(group, SWT.BORDER);
        GridData gd1 = new GridData();
        gd1.widthHint = 200;
        characters2ReplaceText.setLayoutData(gd1);

        Label label2 = new Label(group, SWT.NONE);
        label2.setText(DefaultMessagesImpl.getString("JavaOptionsForm.replace2")); //$NON-NLS-1$
        replacementCharactersText = new Text(group, SWT.BORDER);
        GridData gd2 = new GridData();
        gd2.widthHint = 200;
        replacementCharactersText.setLayoutData(gd2);
    }

    @Override
    protected void addFieldsListeners() {
        characters2ReplaceText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }

        });

        replacementCharactersText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }

        });
    }

    @Override
    protected void addUtilsButtonListeners() {
    }

    @Override
    protected boolean checkFieldsValue() {
        String s1 = characters2ReplaceText.getText();
        String s2 = replacementCharactersText.getText();
        if (s1.length() != s2.length()) {
            updateStatus(IStatus.ERROR, UIMessages.MSG_DIFF_STRING_LENGTH);
            return false;
        } else {
            updateStatus(IStatus.OK, UIMessages.MSG_VALID_FIELD);
        }
        return true;
    }

    @Override
    protected void initialize() {
        String s1 = this.parameters.getTextParameter() == null ? "" : this.parameters.getTextParameter().getCharactersToReplace(); //$NON-NLS-1$
        String s2 = this.parameters.getTextParameter() == null ? "" : this.parameters.getTextParameter() //$NON-NLS-1$
                .getReplacementCharacters();
        characters2ReplaceText.setText(s1);
        replacementCharactersText.setText(s2);
    }
}
