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

import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.SqlExplorerUtils;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class PreviewColumnAction extends Action {

    private ModelElement[] modelElements;

    public PreviewColumnAction(ModelElement[] modelElements) {
        super(DefaultMessagesImpl.getString("PreviewColumnAction.Preview")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.EXPLORE_IMAGE));
        this.modelElements = modelElements;
    }

    @Override
    public void run() {
        if (modelElements[0] instanceof TdColumn) {
            TdColumn[] columns = new TdColumn[modelElements.length];
            int i = 0;
            for (ModelElement me : modelElements) {
                columns[i] = (TdColumn) me;
                ++i;
            }
            if (ColumnHelper.isFromSameTable(Arrays.asList(columns))) {
                TdColumn oneColumn = columns[0];
                Connection dataprovider = ConnectionHelper.getTdDataProvider(oneColumn);
                ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(oneColumn);
                DbmsLanguage language = DbmsLanguageFactory.createDbmsLanguage(dataprovider);
                String columnsWithPrefix = language.getQueryColumnsWithPrefix(columns);
                String columnSetName = language.getQueryColumnSetWithPrefix(columnSetOwner);
                String query = "select " + columnsWithPrefix + " from " + columnSetName; //$NON-NLS-1$ //$NON-NLS-2$ 
                SqlExplorerUtils.getDefault().runInDQViewer(dataprovider, query, columnSetName);
            } else {
                MessageDialogWithToggle
                        .openWarning(
                                null,
                                DefaultMessagesImpl.getString("PreviewColumnAction.Warning"), DefaultMessagesImpl.getString("PreviewColumnAction.previewColumns")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }
}
