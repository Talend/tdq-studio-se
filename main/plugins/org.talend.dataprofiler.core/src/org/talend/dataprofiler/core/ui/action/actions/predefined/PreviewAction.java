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
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.ColumnSetNameHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * the PreviewAction can used for both table and view.
 */
public class PreviewAction extends Action {

    private NamedColumnSet set;

    /**
     * DOC qzhang PreviewTableAction constructor comment.
     * 
     * @param table
     */
    public PreviewAction(NamedColumnSet set) {
        super(DefaultMessagesImpl.getString("PreviewAction.previewMenu")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.EXPLORE_IMAGE));
        this.set = set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // MOD scorreia 2008-12-12 do not switch perspective for preview
        // new ChangePerspectiveAction(PluginConstant.SE_ID).run();

        Connection tdDataProvider = ConnectionHelper.getDataProvider(set);
        String qualifiedName = ColumnSetNameHelper.getColumnSetQualifiedName(tdDataProvider, set);
        String query = "select * from " + qualifiedName; //$NON-NLS-1$
        SqlExplorerUtils.getDefault().runInDQViewer(tdDataProvider, query, qualifiedName);
    }
}
