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
package org.talend.dataprofiler.core.ui.action.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class CreateDFConnectionAction extends AbstractMetadataCreationAction {

    protected static final int WIZARD_WIDTH = 920;

    protected static final int WIZARD_HEIGHT = 560;

    /**
     * DOC bZhou CreateDFConnectionAction constructor comment.
     * 
     * @param node
     */
    public CreateDFConnectionAction(RepositoryNode node) {
        super(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#createWizard()
     */
    @Override
    protected IWizard createWizard() {
        return new DelimitedFileWizard(PlatformUI.getWorkbench(), true, node, getExistingNames());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#initDialogBeforeOpen()
     */
    @Override
    protected void initDialogBeforeOpen(WizardDialog dialog) {
        dialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionImage()
     */
    @Override
    protected ImageDescriptor getActionImage() {
        return ImageLib.createAddedIcon(ImageLib.FILE_DELIMITED, IDecoration.TOP_RIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionLabel()
     */
    @Override
    protected String getActionLabel() {
        return DefaultMessagesImpl.getString("CreateDFConnectionAction.CreateFileDelimited"); //$NON-NLS-1$
    }

}
