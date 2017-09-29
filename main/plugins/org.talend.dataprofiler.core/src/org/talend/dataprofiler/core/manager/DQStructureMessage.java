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
package org.talend.dataprofiler.core.manager;

import java.util.HashMap;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.resource.EResourceConstant;

/**
 * Restore the relationship between folder name and message key. yyi 2009-10-16 Feature 9481
 */
public final class DQStructureMessage {

    private static final HashMap<String, String> MESSAGEMAP = new HashMap<String, String>();

    private DQStructureMessage() {

    }

    public static String getString(String key) {

        String message = key;
        String messageKey = MESSAGEMAP.get(key);

        if (null != messageKey && !DefaultMessagesImpl.getString(messageKey).startsWith(DefaultMessagesImpl.KEY_NOT_FOUND_PREFIX)
                && !DefaultMessagesImpl.getString(messageKey).endsWith(DefaultMessagesImpl.KEY_NOT_FOUND_SUFFIX)) {

            message = DefaultMessagesImpl.getString(messageKey);
        }
        return message;
    }

    static {
        MESSAGEMAP.put(EResourceConstant.ANALYSIS.getName(), "DQStructureManager.analyses"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.DATA_PROFILING.getName(), "DQStructureManager.data_Profiling"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.DB_CONNECTIONS.getName(), "DQStructureManager.dbConnections"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.LIBRARIES.getName(), "DQStructureManager.libraries"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.METADATA.getName(), "DQStructureManager.metadata"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.PATTERNS.getName(), "DQStructureManager.patterns"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.PATTERN_REGEX.getName(), "DQStructureManager.regex"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.REPORTS.getName(), "DQStructureManager.reports"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.SOURCE_FILES.getName(), "DQStructureManager.sourceFiles"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.PATTERN_SQL.getName(), "DQStructureManager.sqlPatterns"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.RULES.getName(), "DQStructureManager.rules"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.RULES_SQL.getName(), "DQStructureManager.sqls"); //$NON-NLS-1$

        MESSAGEMAP.put(EResourceConstant.USER_DEFINED_INDICATORS.getName(), "DQStructureManager.userDefinedIndicators"); //$NON-NLS-1$

        // MESSAGEMAP.put(EResourceConstant.RULES.getName(), "DQStructureManager.dqRules");
    }

}
