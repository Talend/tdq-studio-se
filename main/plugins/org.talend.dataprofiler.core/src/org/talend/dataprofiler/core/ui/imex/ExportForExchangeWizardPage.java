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

/**
 * created by xqliu on Jan 31, 2013 Detailled comment
 */
public class ExportForExchangeWizardPage extends ExportWizardPage {

    /**
     * DOC xqliu ExportForExchangeWizardPage constructor comment.
     * 
     * @param specifiedPath
     */
    public ExportForExchangeWizardPage(String specifiedPath) {
        super(specifiedPath);
    }

    @Override
    protected void initControlState() {
        this.dirBTN.setSelection(true);
        this.setDirState(true);
        this.archBTN.setEnabled(false);
        this.setArchState(false);
        this.addRequireBTN.setEnabled(false);
        checkForErrors();
        setPageComplete(false);
    }
}
