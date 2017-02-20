// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.wizard.indicator.forms;

import org.eclipse.swt.widgets.Composite;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC zqin class global comment. Detailled comment
 */
public abstract class AbstractIndicatorForm extends AbstractForm {

    // ----message define----

    public static final String MSG_EMPTY = UIMessages.MSG_EMPTY_FIELD;

    public static final String MSG_ONLY_CHAR = UIMessages.MSG_ONLY_CHAR;

    public static final String MSG_ONLY_NUMBER = UIMessages.MSG_ONLY_NUMBER;

    public static final String MSG_ONLY_POSITIVE_NUMBER = UIMessages.MSG_ONLY_POSITIVE_NUMBER;

    public static final String MSG_ONLY_REAL_NUMBER = UIMessages.MSG_ONLY_REAL_NUMBER;

    public static final String MSG_ONLY_DATE = UIMessages.MSG_ONLY_DATE;

    public static final String MSG_OK = UIMessages.MSG_VALID_FIELD;

    protected final String limitResultsGrp = DefaultMessagesImpl.getString("AbstractIndicatorForm.limitResult"); //$NON-NLS-1$

    protected IndicatorParameters parameters;

    /**
     * DOC Administrator AbstractIndicatorForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param parameters
     */
    public AbstractIndicatorForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style);
        this.parameters = parameters;
    }

    public String getFormName() {
        return getFormEnum().getFormName();
    }

    public abstract boolean performFinish();

    public abstract FormEnum getFormEnum();
}
