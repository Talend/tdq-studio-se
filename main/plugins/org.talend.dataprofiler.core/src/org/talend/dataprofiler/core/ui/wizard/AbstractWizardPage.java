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
package org.talend.dataprofiler.core.ui.wizard;

import java.util.EventListener;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dq.analysis.parameters.ConnectionParameter;

/**
 * DOC zqin class global comment. Detailled comment
 */
public abstract class AbstractWizardPage extends WizardPage implements IConnectionParameter {

    // ----message define----

    public static final String MSG_EMPTY = UIMessages.MSG_EMPTY_FIELD;

    public static final String MSG_ONLY_CHAR = UIMessages.MSG_ONLY_CHAR;

    public static final String MSG_ONLY_NUMBER = UIMessages.MSG_ONLY_NUMBER;

    public static final String MSG_ONLY_DATE = UIMessages.MSG_ONLY_DATE;

    public static final String MSG_INVALID = UIMessages.MSG_INVALID_FIELD;

    public static final String MSG_OK = UIMessages.MSG_VALID_FIELD;

    protected AbstractWizardPage.ICheckListener listener;

    private int statusLevel = IStatus.OK;

    private String status;

    /**
     * DOC zqin AbstractWizardPage constructor comment.
     */
    protected AbstractWizardPage() {
        super(DefaultMessagesImpl.getString("AbstractWizardPage.wizard")); //$NON-NLS-1$

        this.listener = new AbstractWizardPage.ICheckListener() {

            public void checkPerformed(AbstractWizardPage sourcePage) {
                if (sourcePage.isStatusOnError()) {
                    setPageComplete(false);
                    setErrorMessage(sourcePage.getStatus());
                } else {
                    setPageComplete(true);
                    setErrorMessage(null);
                    setMessage(sourcePage.getStatus(), sourcePage.getStatusLevel());
                }
            }
        };
    }

    /**
     * DOC zqin Comment method "checkFieldsValue".
     * 
     * @return
     */
    public boolean checkFieldsValue() {
        return true;
    }

    /**
     * DOC zqin AbstractWizardPage class global comment. Detailled comment
     */
    public static interface ICheckListener extends EventListener {

        void checkPerformed(AbstractWizardPage sourcePage);
    }

    /**
     * update Status of the Wizard OR of the label Status.
     * 
     * @param String
     */
    protected void updateStatus(final int status, final String statusLabelText) {
        this.status = statusLabelText;
        this.statusLevel = status;
        if (listener != null) {
            listener.checkPerformed(this);
        }
    }

    public String getStatus() {
        return status;
    }

    /**
     * Getter for statusOnError.
     * 
     * @return the statusOnError
     */
    public boolean isStatusOnError() {
        return this.statusLevel == IStatus.ERROR;
    }

    /**
     * Getter for statusOnValid.
     * 
     * @return the statusOnValid
     */
    public boolean isStatusOnValid() {
        return this.statusLevel == IStatus.OK;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    public ConnectionParameter getParameter() {
        if (getWizard() instanceof IConnectionParameter) {
            return ((IConnectionParameter) getWizard()).getParameter();
        }
        return null;
    }
}
