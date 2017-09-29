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
package org.talend.dataprofiler.core.ui.dialog.message;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ImportInfoDialog extends MessageDialog {

    Object[] information;

    /**
     * DOC Administrator ImportInfoDialog constructor comment.
     * 
     * @param parentShell
     * @param dialogTitle
     * @param dialogTitleImage
     * @param dialogMessage
     * @param dialogImageType
     * @param dialogButtonLabels
     * @param defaultIndex
     */
    public ImportInfoDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
            int dialogImageType, String[] dialogButtonLabels, int defaultIndex, Object[] information) {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
        this.information = information;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse. swt.widgets.Composite)
     */
    @Override
    protected Control createCustomArea(Composite parent) {

        if (information instanceof String[]) {
            String[] lists = (String[]) information;

            List list = new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            list.setItems(lists);
            GridData data = new GridData();
            data.heightHint = 120;
            data.widthHint = 350;
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            list.setLayoutData(data);
        } else if (information instanceof ReturnCode[]) {
            ReturnCode[] rcs = (ReturnCode[]) information;

            Table rcTable = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            GridData data = new GridData();
            data.heightHint = 120;
            data.widthHint = 350;
            data.horizontalAlignment = GridData.FILL;
            data.verticalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            rcTable.setLayoutData(data);
            rcTable.setLinesVisible(true);

            TableColumn tableColumn1 = new TableColumn(rcTable, SWT.NONE);
            for (ReturnCode rc : rcs) {
                if (rc.getMessage().length() > 0) {
                    TableItem rcItem = new TableItem(rcTable, SWT.NONE);
                    if (rc.isOk()) {
                        Image okImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
                        rcItem.setImage(okImg);
                    } else {
                        Image errorImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
                        rcItem.setImage(errorImg);
                        imageLabel.setImage(getWarningImage());
                        messageLabel.setText(DefaultMessagesImpl.getString("ImportInfoDialog.ERROR_TSK"));//$NON-NLS-1$ 
                    }
                    rcItem.setText(rc.getMessage());
                }
            }
            tableColumn1.pack();
        }
        return parent;
    }

    /**
     * DOC yyi Comment method "openInformationDialog".
     * 
     * @param information
     */
    public static void openImportInformation(Shell parent, String message, String[] information) {

        ImportInfoDialog dialog = new ImportInfoDialog(parent,
                DefaultMessagesImpl.getString("ImportInfoDialog.Information"), null, message, INFORMATION, //$NON-NLS-1$
                new String[] { IDialogConstants.OK_LABEL }, 0, information);
        dialog.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
        dialog.open();
        return;
    }

    /**
     * yyi 2009-09-30 feature:9106.
     * 
     * @param parent
     * @param message
     * @param information
     * @param dialogImageType
     */
    public static void openImportInformation(Shell parent, String message, String[] information, int dialogImageType) {

        ImportInfoDialog dialog = new ImportInfoDialog(parent,
                DefaultMessagesImpl.getString("ImportInfoDialog.Information"), null, message, dialogImageType, //$NON-NLS-1$
                new String[] { IDialogConstants.OK_LABEL }, 0, information);
        dialog.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
        dialog.open();
        return;
    }

    /**
     * DOC yyi Comment method "openInformationDialog".
     * 
     * @param information
     */
    public static void openImportInformation(Shell parent, String message, ReturnCode[] information) {

        ImportInfoDialog dialog = new ImportInfoDialog(parent,
                DefaultMessagesImpl.getString("ImportInfoDialog.Information"), null, message, INFORMATION, //$NON-NLS-1$
                new String[] { IDialogConstants.OK_LABEL }, 0, information);
        dialog.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
        dialog.open();
        return;
    }

}
