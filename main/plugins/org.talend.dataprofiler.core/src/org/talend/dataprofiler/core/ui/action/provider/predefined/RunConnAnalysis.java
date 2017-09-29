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
package org.talend.dataprofiler.core.ui.action.provider.predefined;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedActionProvider;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.RunConnAnalysisAction;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class RunConnAnalysis extends AbstractPredefinedActionProvider {

    IRepositoryViewObject reposViewObj;

    /**
     * DOC qzhang RunConnAnalysis constructor comment.
     */
    public RunConnAnalysis() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite site) {
        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            StructuredSelection selection = (StructuredSelection) site.getStructuredViewer().getSelection();
            Object fe = selection.getFirstElement();
            if (fe instanceof IRepositoryNode) {
                IRepositoryViewObject object = ((IRepositoryNode) fe).getObject();
                if (object.getProperty().getItem() instanceof ConnectionItem) {
                    reposViewObj = object;
                }
            }
        }
        super.init(site);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedActionProvider#getAction()
     */
    @Override
    protected AbstractPredefinedAnalysisAction getAction() {
        return reposViewObj == null ? null : new RunConnAnalysisAction(reposViewObj);
    }
}
