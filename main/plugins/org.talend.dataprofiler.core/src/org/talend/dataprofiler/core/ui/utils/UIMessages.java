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
package org.talend.dataprofiler.core.ui.utils;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * @author scorreia
 * 
 * Contains messages often used in wizards or in other UI windows.
 */
public final class UIMessages {

    private UIMessages() {
    }

    public static final String MSG_EMPTY_FIELD = DefaultMessagesImpl.getString("UIMessages.empty"); //$NON-NLS-1$

    public static final String MSG_ONLY_CHAR = DefaultMessagesImpl.getString("UIMessages.characterText"); //$NON-NLS-1$

    public static final String MSG_ONLY_NUMBER = DefaultMessagesImpl.getString("UIMessages.numeric"); //$NON-NLS-1$

    public static final String MSG_ONLY_POSITIVE_NUMBER = DefaultMessagesImpl.getString("UIMessages.positiveNumeric"); //$NON-NLS-1$

    public static final String MSG_ONLY_REAL_NUMBER = DefaultMessagesImpl.getString("UIMessages.realNumber"); //$NON-NLS-1$

    public static final String MSG_ONLY_DATE = DefaultMessagesImpl.getString("UIMessages.date"); //$NON-NLS-1$

    public static final String MSG_VALID_FIELD = DefaultMessagesImpl.getString("UIMessages.inputValid"); //$NON-NLS-1$

    public static final String MSG_INVALID_FIELD = DefaultMessagesImpl.getString("UIMessages.fieldInvalid"); //$NON-NLS-1$

    public static final String MSG_LOWER_LESS_HIGHER = DefaultMessagesImpl.getString("UIMessages.lowerValuelessThanHigherValue"); //$NON-NLS-1$

    public static final String MSG_INDICATOR_WIZARD = DefaultMessagesImpl.getString("UIMessages.ParametersInIndicator"); //$NON-NLS-1$

    public static final String MSG_INDICATOR_VALUE_OUT_OF_RANGE = DefaultMessagesImpl.getString("UIMessages.PercentValue"); //$NON-NLS-1$

    public static final String MSG_INDICATOR_VALUE_OUT_OF_RANGE_LONG = DefaultMessagesImpl.getString("UIMessages.LongValue"); //$NON-NLS-1$

    public static final String MSG_SELECT_GENERIC_JDBC = DefaultMessagesImpl.getString("UIMessages.GenericConnection") //$NON-NLS-1$
            + DefaultMessagesImpl.getString("UIMessages.CustomDriver"); //$NON-NLS-1$

    public static final String MSG_ANALYSIS_SAME_NAME = DefaultMessagesImpl.getString("UIMessages.WarnModelNameExist"); //$NON-NLS-1$

    public static final String MSG_DIFF_STRING_LENGTH = DefaultMessagesImpl.getString("UIMessages.diffStringLength"); //$NON-NLS-1$

    public static final String MSG_EXIST_SAME_NAME = DefaultMessagesImpl.getString("UIMessages.ItemExistsError"); //$NON-NLS-1$
}
