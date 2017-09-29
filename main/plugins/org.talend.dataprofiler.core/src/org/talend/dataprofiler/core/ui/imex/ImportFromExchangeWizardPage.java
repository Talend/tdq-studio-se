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

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.imex.model.EImexType;
import org.talend.dataprofiler.core.ui.imex.model.ImportWriterFactory;

/**
 * created by xqliu on Feb 1, 2013 Detailled comment
 */
public class ImportFromExchangeWizardPage extends ImportWizardPage {

    /**
     * DOC xqliu ImportFromExchangeWizardPage constructor comment.
     * 
     * @param basePath
     */
    public ImportFromExchangeWizardPage(String basePath) {
        super();
        String importItemName = new Path(basePath).removeFileExtension().lastSegment();
        setMessage(DefaultMessagesImpl.getString("ImportFromExchange.import", importItemName)); //$NON-NLS-1$
        this.basePath = basePath;
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);

        this.writer = ImportWriterFactory.create(EImexType.ZIP_FILE);
        this.overwriteBTN.setSelection(true);

        this.archBTN.setSelection(true);
        this.archTxt.setText(basePath);

        this.dirBTN.setEnabled(false);
        setDirState(false);
        this.archBTN.setEnabled(false);
        setArchState(false);

        updateBasePath();
        checkforErrors();
        updatePageStatus();
    }
}
