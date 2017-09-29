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
package org.talend.dataprofiler.core.ui.wizard.indicator.forms;

import java.util.EventListener;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.talend.dataprofiler.core.CorePlugin;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractForm.java 12311 2008-03-14 09:40:39Z ggu $
 */
public abstract class AbstractForm extends Composite {

    /**
     * Common Forms Settings.
     */
    protected static final String PID = CorePlugin.PLUGIN_ID;

    protected static final int VERTICAL_SPACING_FORM = 0;

    protected static final int WIDTH_BUTTON_PIXEL = 100;

    protected static final int HEIGHT_BUTTON_PIXEL = 30;

    /**
     * Main Forms Settings.
     */
    private boolean readOnly = false;

    private boolean isInWizard = true;

    /**
     * Use to manage the status (error, warning, info messages).
     */
    private int statusLevel = IStatus.OK;

    private String status;

    protected Label statusLabel;

    protected ICheckListener listener;

    protected static final String DEFAULT_LABEL = "Column"; //$NON-NLS-1$

    /**
     * DOC ocarbone AbstractForm constructor comment.
     * 
     * @param parent
     * @param style
     */
    protected AbstractForm(Composite parent, int style) {
        super(parent, style);

    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm class global comment. Detailled comment <br/>
     * 
     * $Id: AbstractForm.java 12311 2008-03-14 09:40:39Z ggu $
     * 
     */
    public static interface ICheckListener extends EventListener {

        void checkPerformed(AbstractForm source);
    }

    /**
     * checks control values DOC ocarbone Comment method "checkFieldsValue".
     */
    protected abstract boolean checkFieldsValue();

    /**
     * DOC ocarbone Comment method "setupForm".
     */
    protected void setupForm() {
        addComponents();
        // statusLabel is only use to SWT form / not use to Wizard
        if (!isInWizard) {
            statusLabel = new Label(this, SWT.LEFT);
            statusLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
            statusLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
        }

        addFields();
        initialize();
        addUtilsButtonListeners();
        addFieldsListeners();
        if (this instanceof IRefreshable) {
            addAllKeyListener(getShell(), (IRefreshable) this);
        }
    }

    /**
     * adds addComponents to the form DOC ocarbone Comment method "addComponents". add a swt Form width a statusLabel
     * who is a default element of wizard. create an instance of GridLayout and addField()
     */
    protected void addComponents() {
        // Main Layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = VERTICAL_SPACING_FORM;
        this.setLayout(gridLayout);
    }

    /**
     * init the UI and the values.
     * 
     * @param String
     */
    protected abstract void initialize();

    /**
     * adds listeners to controls the utils buttons disposed on the form DOC ocarbone Comment method
     * "addUtilsButtonControls".
     */
    protected abstract void addUtilsButtonListeners();

    /**
     * adds controls to parent composite DOC ocarbone Comment method "addFields".
     * 
     * @param form
     */
    protected abstract void addFields();

    /**
     * adds listeners to controls DOC ocarbone Comment method "addControls".
     */
    protected abstract void addFieldsListeners();

    /**
     * Getter for readOnly. DOC ocarbone Comment method.
     * 
     * @return the readOnly
     */
    protected boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Sets readOnly, adapt the Form to Read Only Mode or edition and execute checkFieldsValue. DOC ocarbone Comment
     * method setReadOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        // adapt all the fields to enabled
        adaptFormToReadOnly();
        if (!readOnly) {
            // adapt the field to the context
            checkFieldsValue();
        }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected abstract void adaptFormToReadOnly();

    /**
     * Sets the listener.
     * 
     * @param listener the listener to set
     */
    public void setListener(ICheckListener listener) {
        this.listener = listener;
    }

    /**
     * update Status of the Wizard OR of the label Status.
     * 
     * @param String
     */
    protected void updateStatus(final int status, final String statusLabelText) {
        this.status = statusLabelText;
        if (!isInWizard) {
            if (statusLabelText != null) {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
                statusLabel.setText(statusLabelText);
            } else {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                statusLabel.setText(""); //$NON-NLS-1$
            }
        }
        this.statusLevel = status;
        if (listener != null) {
            listener.checkPerformed(this);
        }
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

    public String getStatus() {
        return status;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    /**
     * Getter for isInWizard.
     * 
     * @return the isInWizard
     */
    protected boolean isInWizard() {
        return this.isInWizard;
    }

    /**
     * Sets the isInWizard.
     * 
     * @param isInWizard the isInWizard to set
     */
    protected void setInWizard(boolean isInWizard) {
        this.isInWizard = isInWizard;
    }

    /**
     * Add key listener on each control within the wizard page.
     * 
     * yzhang Comment method "configKeyListener".
     */
    private void addAllKeyListener(Control control, final IRefreshable refresh) {

        control.addKeyListener(new KeyAdapter() {

            private IRefreshable refreshableComposite = refresh;

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if ((e.keyCode == SWT.F5)) {
                    refreshableComposite.refresh();
                }
            }
        });

        if (control instanceof Composite) {
            Composite composite = (Composite) control;
            Control[] children = composite.getChildren();
            for (Control ctrl : children) {
                addAllKeyListener(ctrl, refresh);
            }
        }

    }
}
