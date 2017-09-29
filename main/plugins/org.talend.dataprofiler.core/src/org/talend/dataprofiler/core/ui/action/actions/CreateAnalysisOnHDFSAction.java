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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.predefined.CreateColumnAnalysisAction;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年5月28日 Detailled comment
 *
 */
public class CreateAnalysisOnHDFSAction extends Action {

    private EventReceiver afterCreateTableReceiver = null;

    private RepositoryNode hdfsNode;

    /**
     * DOC yyin CreateAnalysisOnHDFSAction constructor comment.
     * 
     * @param node
     */
    public CreateAnalysisOnHDFSAction(RepositoryNode node) {
        super(DefaultMessagesImpl.getString("CreateAnalysisOnHDFSAction.create")); //$NON-NLS-1$
        hdfsNode = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        DBTableRepNode newTableNode = openCreateHiveTable();
        if (newTableNode != null) {
            List<IRepositoryNode> allColumns = newTableNode.getAllColumns();
            TreePath[] paths = new TreePath[allColumns.size()];

            int i = 0;
            for (IRepositoryNode column : allColumns) {
                paths[i++] = new TreePath(new Object[] { column });
            }
            CreateColumnAnalysisAction analysisAction = new CreateColumnAnalysisAction();
            analysisAction.setSelection(new TreeSelection(paths));
            analysisAction.run();
        }
    }

    /**
     * DOC yyin Comment method "openCreateHiveTableDialog".
     * 
     * @return
     */
    private DBTableRepNode openCreateHiveTable() {
        // to open the wizard: create hive
        Map<Object, Object> tableMap = HadoopClusterUtils.getDefault().createHiveTable(hdfsNode);
        Iterator<Object> iterator = tableMap.keySet().iterator();
        String tableName = (String) iterator.next();
        return findCreatedTable((DatabaseConnectionItem) tableMap.get(tableName), tableName);
    }

    /**
     * DOC yyin Comment method "findCreatedTable".
     * 
     * @param hiveConnectionItem
     * @param createTableName
     * @return
     */
    private DBTableRepNode findCreatedTable(DatabaseConnectionItem hiveConnectionItem, String createTableName) {
        if (hiveConnectionItem == null) {
            return null;
        }
        // TDQ-10462 the table from the hive connection, only use lowercase.
        IRepositoryNode tableNode = TableUtils.findTableInConnection(hiveConnectionItem, StringUtils.lowerCase(createTableName));
        if (tableNode != null && tableNode instanceof DBTableRepNode) {
            return (DBTableRepNode) tableNode;
        }
        return null;
    }

}
