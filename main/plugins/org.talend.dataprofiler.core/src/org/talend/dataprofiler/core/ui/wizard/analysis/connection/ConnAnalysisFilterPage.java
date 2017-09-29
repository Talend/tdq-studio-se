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
package org.talend.dataprofiler.core.ui.wizard.analysis.connection;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * @author zqin
 */
public class ConnAnalysisFilterPage extends AnalysisFilterPage {

    /**
     * @param pageName
     */
    public ConnAnalysisFilterPage() {
        setTitle(DefaultMessagesImpl.getString("ConnAnalysisPageStep1.newAnalysis")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("ConnAnalysisPageStep1.addFiltersConnectionAnalysis")); //$NON-NLS-1$
    }
}
