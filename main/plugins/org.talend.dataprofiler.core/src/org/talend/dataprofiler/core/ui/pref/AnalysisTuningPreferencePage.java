// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.pref;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.HeapStatus;
import org.talend.commons.ui.swt.preferences.CheckBoxFieldEditor;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.analysis.memory.AnalysisThreadMemoryChangeNotifier;

/**
 * DOC gdbu class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class AnalysisTuningPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public static final String CHECKED_ELEMENTS_LENGTH = "CHECKED_ELEMENTS_LENGTH";//$NON-NLS-1$

    private Text textNumberOfConnectionsPerAnalysis;

    private ScaleFieldEditor memoryScaleField;

    private CheckBoxFieldEditor autoComboField;

    private int memMax;

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

        busyGC();
        Runtime runtime = Runtime.getRuntime();
        convertToMeg(runtime.totalMemory());
        memMax = convertToMeg(runtime.maxMemory());
        convertToMeg(runtime.freeMemory());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {

        Composite mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout());
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Group group2 = new Group(mainComposite, SWT.SHADOW_ETCHED_IN);
        group2.setText(DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.MemoryGroup")); //$NON-NLS-1$
        GridLayout gridLayout1 = new GridLayout(2, false);
        group2.setLayout(gridLayout1);
        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        group2.setLayoutData(gridData1);

        Composite composite2 = new Composite(group2, SWT.NONE);

        autoComboField = new CheckBoxFieldEditor(AnalysisThreadMemoryChangeNotifier.ANALYSIS_AUTOMATIC_MEMORY_CONTROL,
                DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.EnableThreadControl"), composite2); //$NON-NLS-1$

        final Composite composite4 = new Composite(composite2, SWT.NONE);
        composite4.setLayout(new GridLayout(4, false));
        composite4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        autoComboField.setPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                memoryScaleField.getScaleControl().setEnabled(Boolean.valueOf(event.getNewValue().toString()));
            }

        });

        autoComboField.setPreferenceStore(PlatformUI.getPreferenceStore());
        autoComboField.setPage(this);
        autoComboField.load();

        Label labelScale1 = new Label(composite4, SWT.NONE);
        labelScale1.setText(DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.ForceToStop")); //$NON-NLS-1$

        final Label labelScale2 = new Label(composite4, SWT.RIGHT);

        Label labelScale3 = new Label(composite4, SWT.NONE);
        labelScale3.setText(DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.Mb")); //$NON-NLS-1$

        Composite compositeScale = new Composite(composite4, SWT.NONE);
        compositeScale.setLayout(new GridLayout());
        // MOD yyi 2012-04-18 TDQ-4916 scale layout fixed.
        compositeScale.setLayoutData(gridData1);

        // MOD yyi 2012-06-19 TDQ-4916 if the value is set to zero the threshold function would be disabled.
        memoryScaleField = new ScaleFieldEditor(AnalysisThreadMemoryChangeNotifier.ANALYSIS_MEMORY_THRESHOLD,
                "", compositeScale, 0, memMax, 1, 8); //$NON-NLS-1$

        memoryScaleField.setPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                labelScale2.setText(event.getNewValue().toString());
                composite4.pack();
            }

        });

        memoryScaleField.setPreferenceStore(PlatformUI.getPreferenceStore());
        memoryScaleField.setPage(this);
        memoryScaleField.load();

        labelScale2.setText(String.valueOf(memoryScaleField.getMaximum()));
        labelScale2.setText(String.valueOf(memoryScaleField.getScaleControl().getSelection()));
        if (!autoComboField.getBooleanValue()) {
            memoryScaleField.getScaleControl().setEnabled(false);
        }

        Composite composite3 = new Composite(composite2, SWT.NONE);
        composite3.setLayout(new GridLayout(4, false));
        composite3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label3 = new Label(composite3, SWT.NONE);
        label3.setText(DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.HeapStatus")); //$NON-NLS-1$
        HeapStatus heap = new HeapStatus(composite3, PlatformUI.getPreferenceStore());
        heap.setEnabled(false);
        heap.setMenu(null);

        CLabel label2 = new CLabel(composite2, SWT.WRAP);
        label2.setText(DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.JvmWarning")); //$NON-NLS-1$
        label2.setImage(ImageLib.getImage(ImageLib.RED_WARNING_PNG));

        CorePlugin.getDefault().handleUserReadOnlyStatus(mainComposite);
        return mainComposite;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        // textAnalyzedColumnsLimit.setText(ELEMENTS_DEFAUL_TLENGTH + PluginConstant.EMPTY_STRING);
        // PlatformUI.getPreferenceStore().setValue(CHECKED_ELEMENTS_LENGTH, textAnalyzedColumnsLimit.getText());

        memoryScaleField.loadDefault();
        autoComboField.loadDefault();

        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        memoryScaleField.store();
        autoComboField.store();
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        memoryScaleField.store();
        autoComboField.store();
        super.performApply();
    }

    private void busyGC() {
        for (int i = 0; i < 2; ++i) {
            System.gc();
            System.runFinalization();
        }
    }

    /**
     * Converts the given number of bytes to the corresponding number of megabytes (rounded up).
     */
    private int convertToMeg(long numBytes) {
        return Integer.parseInt(String.valueOf((numBytes + (512 * 1024)) / (1024 * 1024)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#contributeButtons(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void contributeButtons(Composite parent) {
        super.contributeButtons(parent);
        CorePlugin.getDefault().handleUserReadOnlyStatus(parent);
    }
}
