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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.BinsDesignerForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.DataThresholdsForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.ExpectedValueForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.IndicatorThresholdsForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.JavaOptionsForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.JavaUDIParametersForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.NumbericNominalForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.PhoneNumberForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.TextLengthForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.TextParametersForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.TextParametersWithoutOptionsForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.TimeSlicesForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq.FreqBinsDesignerForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq.FreqTextLengthForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq.FreqTextParametersForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq.FreqTimeSliceForm;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public final class IndicatorFormFactory {

    private IndicatorFormFactory() {
    }

    public static AbstractIndicatorForm createForm(Composite parent, FormEnum formEnum, IndicatorParameters parameters) {
        AbstractIndicatorForm form = null;

        switch (formEnum) {
        case BinsDesignerForm:

            form = new BinsDesignerForm(parent, SWT.NONE, parameters);

            break;
        case TextLengthForm:

            form = new TextLengthForm(parent, SWT.NONE, parameters);

            break;
        case FreqTextLengthForm:

            form = new FreqTextLengthForm(parent, SWT.NONE, parameters);

            break;
        case TextParametersForm:

            form = new TextParametersForm(parent, SWT.NONE, parameters);

            break;
        case TextParametersWithoutOptionsForm:

            form = new TextParametersWithoutOptionsForm(parent, SWT.NONE, parameters);

            break;
        case DataThresholdsForm:

            form = new DataThresholdsForm(parent, SWT.NONE, parameters);

            break;
        case TimeSlicesForm:

            form = new TimeSlicesForm(parent, SWT.NONE, parameters);

            break;
        case FreqBinsDesignerForm:

            form = new FreqBinsDesignerForm(parent, SWT.NONE, parameters);

            break;
        case FreqTextParametersForm:

            form = new FreqTextParametersForm(parent, SWT.NONE, parameters);

            break;
        case FreqTimeSliceForm:

            form = new FreqTimeSliceForm(parent, SWT.NONE, parameters);

            break;

        case IndicatorThresholdsForm:

            form = new IndicatorThresholdsForm(parent, SWT.NONE, parameters);

            break;

        case NumbericNominalForm:

            form = new NumbericNominalForm(parent, SWT.NONE, parameters);
            break;

        case ExpectedValueForm:

            form = new ExpectedValueForm(parent, SWT.NONE, parameters);
            break;

        case JavaOptionsForm:

            form = new JavaOptionsForm(parent, SWT.NONE, parameters);
            break;
        case JavaUDIParametersForm:

            form = new JavaUDIParametersForm(parent, SWT.NONE, parameters);
            break;
        case PhoneNumberForm:
            form = new PhoneNumberForm(parent, SWT.NONE, parameters);
            break;

        default:
        }

        return form;
    }
}
