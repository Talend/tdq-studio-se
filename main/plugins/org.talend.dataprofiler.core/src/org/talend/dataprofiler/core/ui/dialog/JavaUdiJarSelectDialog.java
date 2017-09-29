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
package org.talend.dataprofiler.core.ui.dialog;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;
import org.talend.dataquality.record.linkage.utils.CustomAttributeMatcherClassNameConvert;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author zshen
 * 
 */
public class JavaUdiJarSelectDialog extends AbstractJarSelectDialog<UserDefIndicatorImpl> {

    protected IndicatorDefinition definition;

    // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
    private Text jarPathText;

    private Text classNameText;

    // ADD end

    /**
     * Constructs an instance of <code>ElementTreeSelectionDialog</code>.
     * 
     * @param parent The shell to parent from.
     * @param labelProvider the label provider to render the entries
     * @param contentProvider the content provider to evaluate the tree structure
     */
    public JavaUdiJarSelectDialog(IndicatorDefinition definition, Shell parent, ILabelProvider labelProvider,
            ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
        this.definition = definition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // TODO Auto-generated method stub
        // super.createButtonsForButtonBar(parent);
    }

    /**
     * DOC msjian Comment method "setControl".
     * 
     * @param jarPathText
     * @deprecated we should use setControls(Text jarPathText, Text classNameText) instead of this method
     */
    @Deprecated
    public void setControl(Text jarPathText) {
        this.jarPathText = jarPathText;
    }

    /**
     * 
     * set jarPathText and classNameText
     * 
     * @param jarPathText save path of the jar
     * @param classNameText save name of the class
     */
    public void setControls(Text jarPathText, Text classNameText) {
        this.jarPathText = jarPathText;
        this.classNameText = classNameText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#checkJarDependency()
     */

    @Override
    public ReturnCode checkJarDependency(File delFile) {
        return UDIUtils.checkUDIDependency(definition, delFile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#createOKButton(org.eclipse.swt.widgets.Composite
     * )
     */
    @Override
    protected void createOKButton(Composite buttonComposite) {
        // ADD msjian 2011-11-17 TDQ-3556 : add ok/cancel button to the selecter window
        Button okButton = createButton(buttonComposite, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        SelectionListener listenerOK = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String path = "";//$NON-NLS-1$
                for (Object obj : getResult()) {
                    if (obj instanceof File) {
                        IFile file = ResourceManager.getRoot().getFile(new org.eclipse.core.runtime.Path(((File) obj).getPath()));
                        if (!"".equalsIgnoreCase(path)) {//$NON-NLS-1$
                            path += CustomAttributeMatcherHelper.SEPARATOR;
                        }
                        path += file.getName();
                    }
                }
                jarPathText.setText(path);
            }
        };
        okButton.addSelectionListener(listenerOK);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#getDescriptionMessage(java.lang.String)
     */
    @Override
    protected String getDescriptionMessage(String name) {
        return DefaultMessagesImpl.getString("JavaUdiJarSelectDialog.addJarFile", name); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#getFileHasBeenSelectedMessages(java.lang.Object)
     */
    @Override
    protected String getFileHasBeenSelectedMessages(Object delFile) {
        return DefaultMessagesImpl.getString("JavaUdiJarSelectDialog.fileHasBeenSelected", ((File) delFile).getName()); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#initfValidator()
     */
    @Override
    protected ISelectionStatusValidator initfValidator() {
        return new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {

                if (jarList.getSelectionCount() <= 0) {
                    return new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID,
                            DefaultMessagesImpl.getString("JavaUdiJarSelectDialog.atleastSelectedOneJar")); //$NON-NLS-1$
                }

                return new Status(IStatus.OK, CorePlugin.PLUGIN_ID, StringUtils.EMPTY);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#getSuperClass()
     */
    @Override
    protected Class<UserDefIndicatorImpl> getSuperClass() {

        return UserDefIndicatorImpl.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#spliteJarFile()
     */
    @Override
    protected String[] spliteJarFile() {
        String[] allElements = getCheckValue().split(CustomAttributeMatcherClassNameConvert.REGEXKEY);
        String[] jarPathElements = new String[allElements.length];
        for (int index = 0; index < allElements.length; index++) {
            jarPathElements[index] = new File(allElements[index]).getName();
        }
        return jarPathElements;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#getSelectResult()
     */
    @Override
    public String getSelectResult() {
        return selectClassName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#selectClassName()
     */
    @Override
    protected void selectClassName() {
        String className = CustomAttributeMatcherHelper.getClassName(this.classNameText.getText());
        int indexOf = jarList.indexOf(className);
        if (indexOf != -1) {
            jarList.select(indexOf);
            this.selectClassName = className;
        }
    }

}
