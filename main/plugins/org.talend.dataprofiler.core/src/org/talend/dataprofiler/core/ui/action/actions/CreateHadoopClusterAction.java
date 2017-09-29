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
package org.talend.dataprofiler.core.ui.action.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年4月22日 Detailled comment
 *
 */
public class CreateHadoopClusterAction extends AbstractMetadataCreationAction {

    public CreateHadoopClusterAction(RepositoryNode node) {
        super.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#createWizard()
     */
    @Override
    protected IWizard createWizard() {
        return HadoopClusterUtils.getDefault().createHadoopClusterWizard(PlatformUI.getWorkbench(), true, node,
                getExistingNames());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionLabel()
     */
    @Override
    protected String getActionLabel() {
        return DefaultMessagesImpl.getString("CreateHadoopClusterAction.create"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionImage()
     */
    @Override
    protected ImageDescriptor getActionImage() {
        return ImageLib.getImageDescriptor(ImageLib.HADOOP_CLUSTER);
    }

}
