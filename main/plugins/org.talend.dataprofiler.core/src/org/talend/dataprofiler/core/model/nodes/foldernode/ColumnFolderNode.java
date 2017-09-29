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
package org.talend.dataprofiler.core.model.nodes.foldernode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.nodes.foldernode.AbstractDatabaseFolderNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * @author rli
 * 
 */
public class ColumnFolderNode extends AbstractDatabaseFolderNode {

    public static final int COLUMN_MAX = 2500;

    /**
     * @param name
     */
    public ColumnFolderNode() {
        super(DefaultMessagesImpl.getString("ColumnFolderNode.columns")); //$NON-NLS-1$
    }

    private static final boolean FILTER_FLAG = Platform.getPreferencesService().getBoolean(
            CWMPlugin.getDefault().getBundle().getSymbolicName(), PluginConstant.FILTER_TABLE_VIEW_COLUMN, false, null);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.model.nodes.AbstractFolderNode#loadChildren()
     */
    @Override
    public void loadChildren() {
        // MOD xqliu 2009-04-27 bug 6507
        // get columns from either tables or views.
        ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch((EObject) getParent());
        if (columnSet != null) {
            List<TdColumn> columnList = null;
            if (FILTER_FLAG) {
                String columnFilter = ColumnHelper.getColumnFilter(columnSet);
                columnList = filterColumns(ColumnSetHelper.getColumns(columnSet), columnFilter);
            } else {
                columnList = ColumnSetHelper.getColumns(columnSet);
            }
            if (columnList.size() > 0) {
                if (columnList.size() > COLUMN_MAX) {
                    this.setChildren(null);
                    MessageUI.openWarning(DefaultMessagesImpl.getString("ColumnFolderNode.warnMsg", COLUMN_MAX)); //$NON-NLS-1$
                } else {
                    this.setChildren(columnList.toArray());
                }
                return;
            } else {
                if (FILTER_FLAG) {
                    this.setChildren(null);
                    if (ColumnSetHelper.getColumns(columnSet).size() > 0) {
                        return;
                    }
                }
            }
            if (columnSet.eIsProxy()) {
                // resolve the proxy object.
                columnSet = (ColumnSet) EObjectHelper.resolveObject(columnSet);
            }
            Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(columnSet);
            if (parentCatalogOrSchema == null) {
                return;
            }
            Connection conn = ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);
            if (conn == null) {
                return;
            }
            try {
                columnList = DqRepositoryViewService.getColumns(conn, columnSet, null, true);
                if (columnList.size() > COLUMN_MAX) {
                    this.setChildren(null);
                    MessageUI.openWarning(DefaultMessagesImpl.getString("ColumnFolderNode.warnMsg", COLUMN_MAX)); //$NON-NLS-1$
                    return;
                }
            } catch (Exception e) {
                MessageBoxExceptionHandler.process(e);
            }
            // store tables in catalog
            // MOD scorreia 2009-01-29 columns are stored in the table
            // ColumnSetHelper.addColumns(columnSet, columnList);
            this.setChildren(columnList.toArray());
            ElementWriterFactory.getInstance().createDataProviderWriter().save(conn);
        }
        super.loadChildren();
        // ~
    }

    @Override
    public int getFolderNodeType() {
        return COLUMNFOLDER_NODE_TYPE;
    }

    /**
     * DOC xqliu Comment method "filterColumns". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param <T>
     * @param columns
     * @param columnPattern
     * @return
     */
    private <T extends TdColumn> List<T> filterColumns(List<T> columns, String columnPattern) {
        if (needFilter(columnPattern)) {
            String[] patterns = cleanPatterns(columnPattern.split(",")); //$NON-NLS-1$
            return filterMatchingColumns(columns, patterns);
        }
        return columns;
    }

    /**
     * DOC xqliu Comment method "filterMatchingColumns". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param <T>
     * @param columns
     * @param patterns
     * @return
     */
    private <T extends TdColumn> List<T> filterMatchingColumns(List<T> columns, String[] patterns) {
        List<T> retColumns = new ArrayList<T>();
        int size = 0;
        for (T t : columns) {
            for (String pattern : patterns) {
                // MOD scorreia 2009-05-13 use SQL patterns for filter
                String regex = pattern.replaceAll("%", ".*"); //$NON-NLS-1$ //$NON-NLS-2$
                if (t.getName().matches(regex)) {
                    retColumns.add(t);
                    size++;
                    if (size > COLUMN_MAX) {
                        return retColumns;
                    }
                    break;
                }
            }
        }
        return retColumns;
    }

    /**
     * DOC xqliu Comment method "cleanPatterns". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param split
     * @return
     */
    private String[] cleanPatterns(String[] split) {
        ArrayList<String> ret = new ArrayList<String>();
        for (String s : split) {
            if (s != null && !"".equals(s) && !ret.contains(s)) { //$NON-NLS-1$
                ret.add(s);
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * DOC xqliu Comment method "needFilter". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param columnSetPattern
     * @return
     */
    private boolean needFilter(String columnSetPattern) {
        if (FILTER_FLAG) {
            if (columnSetPattern != null && !columnSetPattern.equals("")) { //$NON-NLS-1$
                String[] patterns = cleanPatterns(columnSetPattern.split(",")); //$NON-NLS-1$
                if (patterns != null && patterns.length > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
