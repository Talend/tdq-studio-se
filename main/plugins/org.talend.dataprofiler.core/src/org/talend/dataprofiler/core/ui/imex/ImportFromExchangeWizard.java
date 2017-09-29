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
package org.talend.dataprofiler.core.ui.imex;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * created by xqliu on Feb 1, 2013 Detailled comment
 */
public class ImportFromExchangeWizard extends ImportWizard {

    public ImportFromExchangeWizard(String basePath) {
        setWindowTitle(DefaultMessagesImpl.getString("ImportFromExchange.importFromExchange")); //$NON-NLS-1$
        this.importPage = new ImportFromExchangeWizardPage(basePath);
    }

    @Override
    public boolean performFinish() {
        boolean performFinish = super.performFinish();
        // need to refresh the definition list, to add the new UDI just download from the Exchange to the list
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        return performFinish;
    }

}
