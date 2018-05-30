// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.rcp.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.i18n.BabiliInfo;
import org.talend.commons.i18n.BabiliUpdateUtil;
import org.talend.commons.i18n.ImportBabiliCancelException;
import org.talend.commons.utils.VersionUtils;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC hcheng class global comment. Detailled comment
 */
public class I18nPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private boolean updateCompleted;

    private Combo execCombo;

    private Label header;

    private Composite mainComposite;

    private static Logger log = Logger.getLogger(I18nPreferencePage.class);

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(\\.(RC|M)\\d+)?_r\\d+"); //$NON-NLS-1$

    private static final Pattern DEFAULT_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.*(\\d*)"); //$NON-NLS-1$

    public I18nPreferencePage() {
        super();

        // Set the preference store for the preference page.
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub

    }

    @Override
    protected Control createContents(Composite parent) {
        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout());
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        header = new Label(mainComposite, SWT.WRAP);
        header.setText(DefaultMessagesImpl.getString("I18nPreferencePage.needRestart")); //$NON-NLS-1$
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        header.setLayoutData(gd);

        execCombo = new Combo(mainComposite, SWT.READ_ONLY);
        for (LocalToLanguageEnum oneEnum : LocalToLanguageEnum.values()) {
            execCombo.add(oneEnum.getLocale());
        }
        String language =
                Platform.getPreferencesService().getString(CorePlugin.PLUGIN_ID, PluginConstant.LANGUAGE_SELECTOR,
                        LocalToLanguageEnum.ENGLISH.getShortOfLocale(), null);
        LocalToLanguageEnum languageType = LocalToLanguageEnum.findLocalByShort(language);
        execCombo.setText(language == null ? LocalToLanguageEnum.ENGLISH.getLocale() : languageType.getLocale());
        GridData d = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        execCombo.setLayoutData(d);

        Label label = new Label(mainComposite, SWT.WRAP);
        label.setText(DefaultMessagesImpl.getString("I18nPreferencePage.translationInformation")); //$NON-NLS-1$
        GridData g = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        label.setLayoutData(g);

        Button allUpdate = new Button(mainComposite, SWT.FLAT);
        allUpdate.setText(DefaultMessagesImpl.getString("I18nPreferencePage.allTranslation")); //$NON-NLS-1$
        allUpdate.setLayoutData(new GridData());

        Button validatedUpdate = new Button(mainComposite, SWT.NONE);
        validatedUpdate.setText(DefaultMessagesImpl.getString("I18nPreferencePage.validateTranslation")); //$NON-NLS-1$
        validatedUpdate.setLayoutData(new GridData());

        allUpdate.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // import all update from Babili
                String language = LocalToLanguageEnum.findEnglishLocale(execCombo.getText());
                runProgressMonitorDialog(false, language);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });

        validatedUpdate.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // import validated update from Babili
                String language = LocalToLanguageEnum.findEnglishLocale(execCombo.getText());
                runProgressMonitorDialog(true, language);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }

        });

        CorePlugin.getDefault().handleUserReadOnlyStatus(mainComposite);
        return mainComposite;
    }

    @Override
    protected void performDefaults() {
        execCombo.deselectAll();
        InstanceScope.INSTANCE.getNode(CorePlugin.PLUGIN_ID).put(PluginConstant.LANGUAGE_SELECTOR,
                LocalToLanguageEnum.ENGLISH.getShortOfLocale());
        execCombo.setText(LocalToLanguageEnum.ENGLISH.getLocale());
        super.performDefaults();
    }

    public void runProgressMonitorDialog(final boolean validated, final String language) {
        updateCompleted = false;
        // BabiliTool.clear();
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(mainComposite.getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) {
                try {

                    String version = getCurrentVersion(true);
                    // get list from Babili
                    List<BabiliInfo> bList = BabiliUpdateUtil.getBabiliList(language, validated, version, monitor);
                    for (BabiliInfo info : bList) {
                        BabiliUpdateUtil.checkProcessCancel(monitor);
                        // store to memory
                        String pluginId = info.getFilepath();
                        int pos = pluginId.indexOf("/"); //$NON-NLS-1$
                        if (pos != -1) {
                            pluginId = pluginId.substring(0, pos);
                            if (pluginId.endsWith(".nl")) { //$NON-NLS-1$
                                pluginId = pluginId.replace(".nl", ""); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
                        // BabiliTool.storeBabiliTranslation(info.getKey(), pluginId, info.getLabel());
                        updateCompleted = true;

                    }
                } catch (ImportBabiliCancelException e) {
                    updateCompleted = false;
                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            progressDialog.run(true, true, runnable);
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e1) {
            ExceptionHandler.process(e1);
        }

        if (updateCompleted) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                            DefaultMessagesImpl.getString("I18nPreferencePage.title"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("I18nPreferencePage.completeInfo")); //$NON-NLS-1$
                }
            });
        } else {
            // BabiliTool.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        LocalToLanguageEnum language = LocalToLanguageEnum.findLocal(execCombo.getText());
        InstanceScope.INSTANCE.getNode(CorePlugin.PLUGIN_ID).put(PluginConstant.LANGUAGE_SELECTOR,
                language.getShortOfLocale());
        try {
            InstanceScope.INSTANCE.getNode(CorePlugin.PLUGIN_ID).flush();
        } catch (BackingStoreException e) {
            log.error(e);
        }
        saveLanguageType();
        return ok;
    }

    /**
     * 
     * DOC hcheng Comment method "saveLanguageType".
     */
    private void saveLanguageType() {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            URL url = Platform.getConfigurationLocation().getURL();
            log(url.getFile());
            Properties p = new Properties();
            // load the file configuration/config.ini
            File iniFile = new File(url.getFile(), "config.ini"); //$NON-NLS-1$
            fin = new FileInputStream(iniFile);
            p.load(fin);
            String languageType =
                    Platform.getPreferencesService().getString(CorePlugin.PLUGIN_ID, PluginConstant.LANGUAGE_SELECTOR,
                            LocalToLanguageEnum.ENGLISH.getShortOfLocale(), null);

            if (languageType == null || languageType.equals(p.getProperty(EclipseStarter.PROP_NL))) {
                return;
            }

            p.setProperty(EclipseStarter.PROP_NL, languageType);
            fout = new FileOutputStream(iniFile);
            p.store(fout, "#Configuration File"); //$NON-NLS-1$
            fout.flush();

        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {
                    // do nothing
                }

            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                    // do nothing
                }

            }
        }
    }

    /**
     * 
     * DOC hcheng Comment method "getCurrentVersion".
     * 
     * @param normalize
     * @return
     */
    public static String getCurrentVersion(boolean normalize) {
        String version = VersionUtils.getVersion();
        if (normalize) {
            version = normalizeVersion(version);
        }
        return version;
    }

    /**
     * 
     * DOC hcheng Comment method "normalizeVersion".
     * 
     * @param version
     * @return
     */
    public static String normalizeVersion(String version) {
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (matcher.matches()) {
            String str = version.substring(0, version.indexOf("_r")); //$NON-NLS-1$
            return str.replaceAll("\\.RC", "RC").replaceAll("\\.M", "M"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        } else {
            // try again, ignore M, RC
            matcher = DEFAULT_PATTERN.matcher(version);
            matcher.find();
            return matcher.group();
        }
    }

    private void log(String s) {
        log.log(Level.INFO, s);
    }

    /**
     * DOC Administrator I18nPreferencePage class global comment. Detailled comment
     */
    enum LocalToLanguageEnum {
        CHINESES(Locale.CHINESE.getDisplayLanguage(), Locale.CHINESE.getDisplayLanguage(Locale.ENGLISH), Locale.CHINESE
                .getLanguage()),
        ENGLISH(Locale.ENGLISH.getDisplayLanguage(), Locale.CHINESE.getDisplayLanguage(Locale.ENGLISH), Locale.ENGLISH
                .getLanguage()),
        FRENCH(Locale.FRENCH.getDisplayLanguage(), Locale.FRENCH.getDisplayLanguage(Locale.ENGLISH), Locale.FRENCH
                .getLanguage()),
        GERMAN(Locale.GERMAN.getDisplayLanguage(), Locale.GERMAN.getDisplayLanguage(Locale.ENGLISH), Locale.GERMAN
                .getLanguage()),
        JAPANESE(
                Locale.JAPANESE.getDisplayLanguage(),
                Locale.JAPANESE.getDisplayLanguage(Locale.ENGLISH),
                Locale.JAPANESE.getLanguage()),
        ITALIAN(Locale.ITALIAN.getDisplayLanguage(), Locale.ITALIAN.getDisplayLanguage(Locale.ENGLISH), Locale.ITALIAN
                .getLanguage()),
        BRASIL("Brasil", "Brasil", "pt"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ESPAGNOL("Espagnol", "Espagnol", "ca"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        private String locale;

        private String localeInEnglish;

        private String shortOfLocale;

        LocalToLanguageEnum(String locale, String localeInEnglish, String shortOfLocale) {
            this.locale = locale;
            this.localeInEnglish = localeInEnglish;
            this.shortOfLocale = shortOfLocale;

        }

        static String findEnglishLocale(String locale) {
            for (LocalToLanguageEnum oneEnum : values()) {
                if (oneEnum.getLocale().equals(locale)) {
                    return oneEnum.getLocaleInEnglish();
                }
            }

            return PluginConstant.EMPTY_STRING;
        }

        static LocalToLanguageEnum findLocal(String locale) {
            for (LocalToLanguageEnum oneEnum : values()) {
                if (oneEnum.getLocale().equals(locale)) {
                    return oneEnum;
                }
            }
            return LocalToLanguageEnum.ENGLISH;

        }

        static LocalToLanguageEnum findLocalByShort(String shortOfLocale) {
            for (LocalToLanguageEnum oneEnum : values()) {
                if (oneEnum.getShortOfLocale().equals(shortOfLocale)) {
                    return oneEnum;
                }
            }
            return LocalToLanguageEnum.ENGLISH;

        }

        public String getLocale() {
            return locale;
        }

        public String getLocaleInEnglish() {
            return localeInEnglish;
        }

        public String getShortOfLocale() {
            return shortOfLocale;
        }
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
