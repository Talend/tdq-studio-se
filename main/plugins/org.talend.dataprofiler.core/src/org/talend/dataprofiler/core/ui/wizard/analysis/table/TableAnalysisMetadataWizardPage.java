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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableAnalysisMetadataWizardPage extends AnalysisMetadataWizardPage {

    Log log = LogFactory.getLog(TableAnalysisMetadataWizardPage.class);

    private final String pageTitle = DefaultMessagesImpl.getString("TableAnalysisMetadataWizardPage.newAnalysis"); //$NON-NLS-1$

    private final String pageMessage = DefaultMessagesImpl.getString("TableAnalysisMetadataWizardPage.addsAnalysis"); //$NON-NLS-1$

    public TableAnalysisMetadataWizardPage() {
        setTitle(pageTitle);
        setDescription(pageMessage);
    }

    protected void addListeners() {
        super.addListeners();
        this.nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String name = nameText.getText();
                getParameter().setName(name);
                setPageComplete(name.length() > 0);
            }
        });
    }

    @Override
    public boolean isPageComplete() {
        return isTextValueValid && this.getParameter().getName().length() > 0;
    }

    public void resetParameter() {
        this.getParameter();
    }
}
