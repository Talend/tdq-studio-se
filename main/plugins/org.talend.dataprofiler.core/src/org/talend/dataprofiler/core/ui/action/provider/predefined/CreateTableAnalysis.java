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
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedTableActionProvider;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedTableAnalysisAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.CreateTableAnalysisAction;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class CreateTableAnalysis extends AbstractPredefinedTableActionProvider {

    private ModelElement modelElement;

    @Override
    protected AbstractPredefinedTableAnalysisAction getAction() {
        return modelElement == null ? null : new CreateTableAnalysisAction();
    }

    @Override
    public void init(ICommonActionExtensionSite site) {
        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            StructuredSelection selection = (StructuredSelection) site.getStructuredViewer().getSelection();
            Object fe = selection.getFirstElement();
            if (fe instanceof IRepositoryNode) {
                IRepositoryViewObject object = ((IRepositoryNode) fe).getObject();
                if (object instanceof TdTableRepositoryObject) {
                    TdTableRepositoryObject tableObject = (TdTableRepositoryObject) object;
                    modelElement = tableObject.getTdTable();
                } else if (object instanceof TdViewRepositoryObject) {
                    TdViewRepositoryObject viewObject = (TdViewRepositoryObject) object;
                    modelElement = viewObject.getTdView();
                }
            }
        }
        super.init(site);
    }
}
