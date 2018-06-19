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
package org.talend.dataquality.record.linkage.ui.section;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.cwm.db.connection.SQLExecutor;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by zshen on Oct 21, 2013 Detailled comment
 * 
 */
public class MatchParameterSection extends AbstractSectionComposite {

    private Analysis currAnalysis = null;

    private Boolean isStoreOnDisk = Boolean.FALSE;

    private String MaxSize = "200000"; //$NON-NLS-1$

    private String tempPath = StringUtils.EMPTY;

    private Composite contentComp = null;

    /**
     * DOC zshen MatchParameterSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     */
    public MatchParameterSection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit, Analysis analysis) {
        super(form, parent, style, toolkit);
        section.setText(DefaultMessagesImpl.getString("MatchParameterSection.section.name")); //$NON-NLS-1$
        currAnalysis = analysis;
        initData();
    }

    /**
     * DOC zshen Comment method "initData".
     */
    private void initData() {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(SQLExecutor.TEMP_DATA_DIR, currAnalysis.getTaggedValue());
        if (taggedValue != null) {
            tempPath = taggedValue.getValue();
        } else {
            TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.TEMP_DATA_DIR, tempPath);
        }
        taggedValue = TaggedValueHelper.getTaggedValue(SQLExecutor.MAX_BUFFER_SIZE, currAnalysis.getTaggedValue());
        if (taggedValue != null) {
            MaxSize = taggedValue.getValue();
        } else {
            TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.MAX_BUFFER_SIZE, MaxSize);
        }
        taggedValue = TaggedValueHelper.getTaggedValue(SQLExecutor.STORE_ON_DISK_KEY, currAnalysis.getTaggedValue());
        if (taggedValue != null) {
            isStoreOnDisk = Boolean.valueOf(taggedValue.getValue());
        } else {
            TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.STORE_ON_DISK_KEY, isStoreOnDisk.toString());
        }

    }

    public void createParameterCom() {
        Composite mainComp = toolkit.createComposite(section);
        mainComp.setLayout(new GridLayout());
        GridData gd = new GridData();
        gd.widthHint = 200;
        Composite container = toolkit.createComposite(mainComp, SWT.NONE);
        GridLayout gdLayout = new GridLayout(1, false);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);
        container.setLayout(gdLayout);

        Group storeDiskGroup = new Group(container, SWT.NO_FOCUS);
        storeDiskGroup.setText(DefaultMessagesImpl.getString("MatchParameterSection.group.name")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(storeDiskGroup);
        storeDiskGroup.setLayout(gdLayout);

        Button checkButton = toolkit.createButton(storeDiskGroup,
                DefaultMessagesImpl.getString("MatchParameterSection.group.name"), SWT.CHECK); //$NON-NLS-1$
        if (isStoreOnDisk) {
            checkButton.setSelection(true);
        }

        contentComp = toolkit.createComposite(storeDiskGroup, SWT.NONE);

        GridLayout gdLayoutFiveCol = new GridLayout(30, false);
        GridData contentCompGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        contentComp.setLayoutData(contentCompGD);
        contentComp.setLayout(gdLayoutFiveCol);
        changeDisplayStatus();

        checkButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isStoreOnDisk = !isStoreOnDisk;
                changeDisplayStatus();
                TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.STORE_ON_DISK_KEY, isStoreOnDisk.toString());
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }

        });

        toolkit.createLabel(contentComp, DefaultMessagesImpl.getString("MatchParameterSection.MaxBuffer"), SWT.NONE); //$NON-NLS-1$
        final Text createText = toolkit.createText(contentComp, MaxSize, SWT.BORDER);
        createText.setLayoutData(gd);
        createText.addModifyListener(new ModifyListener() {

            String oldValue = createText.getText();

            @Override
            public void modifyText(ModifyEvent e) {
                String text = ((Text) e.widget).getText();
                if (!NumberUtils.isDigits(text)) {
                    ((Text) e.widget).setText(oldValue);
                    ((Text) e.widget).setSelection(oldValue.length());
                } else {
                    oldValue = text;
                }
                TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.MAX_BUFFER_SIZE, oldValue);
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);

            }

        });

        Label label = new Label(contentComp, SWT.NONE);
        label.setText(DefaultMessagesImpl.getString("MatchParameterSection.tempPath.label")); //$NON-NLS-1$

        final Text outputPathText = new Text(contentComp, SWT.BORDER);
        gd = new GridData();
        gd.widthHint = 300;
        outputPathText.setLayoutData(gd);
        outputPathText.setText(tempPath);
        outputPathText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                String tempText = ((Text) e.widget).getText();
                String text = StringUtils.isBlank(tempText) ? tempText : tempText.endsWith(File.separator) ? tempText : tempText
                        + File.separator;
                TaggedValueHelper.setTaggedValue(currAnalysis, SQLExecutor.TEMP_DATA_DIR, text);
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
            }

        });

        Button outputFolderBtn = new Button(contentComp, SWT.NONE);
        GridData btnData = new GridData();
        outputFolderBtn.setLayoutData(btnData);
        outputFolderBtn.setText("...");//$NON-NLS-1$
        outputFolderBtn.setToolTipText(DefaultMessagesImpl.getString("MatchParameterSection.select"));//$NON-NLS-1$
        outputFolderBtn.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                // do nothing here
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                DirectoryDialog dd = new DirectoryDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.NONE);
                String temp = dd.open();
                if (temp != null && !PluginConstant.EMPTY_STRING.equals(temp.trim())) {
                    outputPathText.setText(temp.trim());
                    listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
                }
            }

            @Override
            public void mouseUp(MouseEvent arg0) {
                // do nothing here
            }
        });

        section.setClient(mainComp);
    }

    private void changeDisplayStatus() {
        GridData gd = (GridData) contentComp.getLayoutData();
        gd.exclude = !isStoreOnDisk;
        contentComp.setVisible(isStoreOnDisk);
        getSection().setExpanded(true);
        getSection().getParent().layout();
    }
}
