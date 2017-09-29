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
package org.talend.dataprofiler.core.ui.action.actions.predefined;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.service.ISemanticStudioService;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * created by scorreia on Oct 1, 2013 Detailled comment
 * 
 */
public class SemanticDiscoveryAction extends Action {

    private MetadataTable metadataTable;

    public SemanticDiscoveryAction(MetadataTable set) {
        this();
        this.metadataTable = set;
    }

    public SemanticDiscoveryAction() {
        super(DefaultMessagesImpl.getString("SuggestAnalysisActionProvider.suggestAnalysis")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ISemanticStudioService service = CorePlugin.getDefault().getSemanticStudioService();

        if (service != null) {
            int wizardReturnCode = service.openSemanticDiscoveryWizard(metadataTable);
            if (Window.OK == wizardReturnCode) {
                IRepositoryNode node = RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.ANALYSIS);
                CorePlugin.getDefault().refreshDQView(node);
            } else {
                // MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error",
                // "Failed to recommend analysis. See the log for more details.");
            }
        }

    }

}
