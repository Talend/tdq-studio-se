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
package org.talend.dataprofiler.core.ui.wizard.analysis.schema;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.AnalysisFilterPage;

/**
 * DOC rli class global comment. Detailled comment
 */
public class SchemaAnalysisFilterPage extends AnalysisFilterPage {

    /**
     * DOC rli SchemaAnalysisFilterPage constructor comment.
     */
    public SchemaAnalysisFilterPage() {
        setTitle(DefaultMessagesImpl.getString("SchemaAnalysisPageStep1.newAnalysis")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("SchemaAnalysisFilterPage.AddFilters")); //$NON-NLS-1$
    }
}
