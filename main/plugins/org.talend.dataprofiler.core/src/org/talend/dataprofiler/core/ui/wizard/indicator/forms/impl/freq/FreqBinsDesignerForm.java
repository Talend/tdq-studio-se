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
package org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq;

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
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.BinsDesignerForm;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class FreqBinsDesignerForm extends BinsDesignerForm {

    private Text numberTxt;

    public FreqBinsDesignerForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
    }

    @Override
    protected void addFields() {
        super.addFields();

        Group group = new Group(this, SWT.NONE);
        group.setText(limitResultsGrp);
        group.setLayout(new GridLayout(2, false));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        group.setLayoutData(gd);

        Label lab = new Label(group, SWT.NONE);
        lab.setText(DefaultMessagesImpl.getString("FreqBinsDesignerForm.numberOfResultsShown")); //$NON-NLS-1$

        numberTxt = new Text(group, SWT.BORDER);
        GridData gdTxt = new GridData();
        gdTxt.widthHint = 100;
        numberTxt.setLayoutData(gdTxt);
    }

    @Override
    protected void addFieldsListeners() {
        super.addFieldsListeners();

        numberTxt.addModifyListener(new ModifyListener() {

            // MOD scorreia annotation commented out for Java 5 compilation @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                if (isStatusOnValid()) {
                    parameters.setTopN(Integer.parseInt(numberTxt.getText()));
                }
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();

        int topN = parameters.getTopN();
        if (topN == 0) {
            topN = 10;
        }

        numberTxt.setText(String.valueOf(topN));
    }

    @Override
    protected boolean checkFieldsValue() {
        if (numberTxt.getText() == "") { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, MSG_EMPTY);
            return false;
        }

        if (!CheckValueUtils.isNumberOfShownValue(numberTxt.getText())) {
            updateStatus(IStatus.ERROR, MSG_ONLY_POSITIVE_NUMBER);
            return false;
        }

        updateStatus(IStatus.OK, MSG_OK);
        return super.checkFieldsValue();
    }
}
