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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm.ICheckListener;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class DynamicIndicatorOptionsPage extends WizardPage {

    protected static Logger log = Logger.getLogger(DynamicIndicatorOptionsPage.class);

    private IndicatorUnit indicatorUnit;

    private TabFolder tabFolder;

    private AbstractIndicatorForm[] validFroms;

    private AbstractIndicatorForm.ICheckListener listener;

    /**
     * DOC zqin DynamicIndicatorOptionsPage constructor comment.
     * 
     * @param pageName
     */
    public DynamicIndicatorOptionsPage(IndicatorUnit indicatorUnit) {
        super(DefaultMessagesImpl.getString("DynamicIndicatorOptionsPage.indicatorSettings")); //$NON-NLS-1$

        this.indicatorUnit = indicatorUnit;

        setPageComplete(false);
        setTitle(DefaultMessagesImpl.getString("DynamicIndicatorOptionsPage.indicatorSetting")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("DynamicIndicatorOptionsPage.setIndicatorParameters")); //$NON-NLS-1$

        this.listener = new AbstractForm.ICheckListener() {

            public void checkPerformed(AbstractForm source) {
                if (source.isStatusOnError()) {
                    setPageComplete(false);
                    setErrorMessage(source.getStatus());
                } else {
                    setPageComplete(true);
                    setErrorMessage(null);
                    setMessage(source.getStatus(), source.getStatusLevel());
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());

        tabFolder = new TabFolder(container, SWT.FLAT);
        tabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Object formObject = e.item.getData();
                if (formObject != null && getCurrentDialog() != null) {
                    AbstractIndicatorForm form = (AbstractIndicatorForm) formObject;
                    String helpHref = form.getFormEnum().getHelpHref();
                    getCurrentDialog().setHref(helpHref);
                    getCurrentDialog().showHelp();
                }
            }

        });

        FormEnum[] forms = FormEnum.getForms(indicatorUnit);
        if (forms != null) {
            validFroms = createForm(tabFolder, listener, forms);
            setControl(createView(validFroms));
        }
    }

    private Composite createView(AbstractIndicatorForm... forms) {
        try {
            for (AbstractIndicatorForm iForm : forms) {
                TabItem item = new TabItem(tabFolder, SWT.NONE);
                item.setData(iForm);
                item.setText(iForm.getFormName());
                item.setControl(iForm);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

        return tabFolder;
    }

    public AbstractIndicatorForm[] createForm(Composite parent, ICheckListener listener, FormEnum[] formTypes) {
        IndicatorParameters parameters = indicatorUnit.getIndicator().getParameters();
        if (parameters == null) {
            parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
            indicatorUnit.getIndicator().setParameters(parameters);
        }

        AbstractIndicatorForm[] froms = new AbstractIndicatorForm[formTypes.length];
        for (int i = 0; i < formTypes.length; i++) {
            AbstractIndicatorForm form = IndicatorFormFactory.createForm(parent, formTypes[i], parameters);
            form.setListener(listener);
            froms[i] = form;
        }

        return froms;
    }

    @Override
    public void setErrorMessage(String newMessage) {
        super.setErrorMessage(newMessage);

        if (isCurrentPage()) {
            getContainer().updateMessage();
        }
    }

    public AbstractIndicatorForm[] getValidFroms() {
        return validFroms;
    }

    /**
     * DOC bZhou Comment method "getCurrentDialog".
     * 
     * @return
     */
    private OpeningHelpWizardDialog getCurrentDialog() {
        IWizardContainer container = getWizard().getContainer();
        if (container instanceof OpeningHelpWizardDialog) {
            return (OpeningHelpWizardDialog) container;
        }
        return null;
    }
}
