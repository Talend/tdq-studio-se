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

import org.eclipse.swt.widgets.Composite;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.TextLengthForm;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class FreqTextLengthForm extends TextLengthForm {

    public FreqTextLengthForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
    }

    @Override
    protected void addFields() {
        super.addFields();
        this.blankBtn.setText(DefaultMessagesImpl.getString("FreqTextLengthForm.aggregateBlanks")); //$NON-NLS-1$
        this.nullBtn.setText(DefaultMessagesImpl.getString("FreqTextLengthForm.aggregateNullsWithBlanks")); //$NON-NLS-1$
    }

    @Override
    public FormEnum getFormEnum() {
        return FormEnum.FreqTextLengthForm;
    }
}
