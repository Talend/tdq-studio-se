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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.foldernode.AbstractDatabaseFolderNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * @param <COLSET> either TdTable or TdView
 */
public abstract class NamedColumnSetFolderNode<COLSET extends NamedColumnSet> extends AbstractDatabaseFolderNode {

    private static Logger log = Logger.getLogger(NamedColumnSetFolderNode.class);

    public static final int TABLE_VIEW_MAX = 2000;

    private static final boolean FILTER_FLAG = Platform.getPreferencesService().getBoolean(
            CWMPlugin.getDefault().getBundle().getSymbolicName(), PluginConstant.FILTER_TABLE_VIEW_COLUMN, false, null);

    /**
     * @param name
     */
    public NamedColumnSetFolderNode(String name) {
        super(name);
    }

    protected <T extends List<COLSET>> void loadChildrenLow(orgomg.cwm.objectmodel.core.Package pack, Catalog catalog,
            Schema schema, final T columnSets) {
        assert pack != null;
        // MOD xqliu 2009-04-27 bug 6507
        if (FILTER_FLAG) {
            columnSets.addAll(getColumnSetsWithFilter(catalog, schema));
        } else {
            columnSets.addAll(getColumnSets(catalog, schema));
        }
        if (columnSets.size() > 0) {
            if (FILTER_FLAG && columnSets.size() > TABLE_VIEW_MAX) {
                columnSets.clear();
                this.setChildren(null);
                MessageUI.openWarning(DefaultMessagesImpl.getString("NamedColumnSetFolderNode.warnMsg", TABLE_VIEW_MAX)); //$NON-NLS-1$
            } else {
                this.setChildren(columnSets.toArray());
            }
            return;
        } else {
            if (FILTER_FLAG) {
                this.setChildren(null);
                if (getColumnSets(catalog, schema).size() > 0) {
                    return;
                }
            }
        }
        // ~

        if (pack.eIsProxy()) {
            // resolve the proxy object.
            pack = (orgomg.cwm.objectmodel.core.Package) EObjectHelper.resolveObject(pack);
            if (pack instanceof Catalog) {
                catalog = (Catalog) pack;
            } else if (pack instanceof Schema) {
                schema = (Schema) pack;
            }
        }
        Connection conn = ConnectionHelper.getTdDataProvider(pack);
        if (conn == null) {
            log.warn(pack.getName());
            return;
        }
        // load from database
        loadColumnSets(catalog, schema, conn, columnSets);
        // store views in catalog or schema
        pack.getOwnedElement().addAll(columnSets);
        this.setChildren(columnSets.toArray());
        IRepositoryViewObject repositoryViewObject = RepositoryNodeHelper.recursiveFind(conn).getObject();
        ElementWriterFactory.getInstance().createDataProviderWriter().save(repositoryViewObject.getProperty().getItem(), false);
    }

    /**
     * @param catalog
     * @param schema
     * @return the Tables or Views in the given catalog or schema.
     */
    protected abstract List<COLSET> getColumnSets(Catalog catalog, Schema schema);

    /**
     * @param catalog
     * @param schema
     * @return the Tables or Views in the given catalog or schema.
     */
    protected abstract List<COLSET> getColumnSetsWithFilter(Catalog catalog, Schema schema);

    /**
     * Loads columnsets (table or view) from database.
     * 
     * @param <T>
     * @param catalog
     * @param schema
     * @param provider
     * @param columnSets
     * @return
     */
    protected abstract <T extends List<COLSET>> boolean loadColumnSets(Catalog catalog, Schema schema, Connection provider,
            final T columnSets);

    /**
     * DOC xqliu Comment method "filterColumnSets". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param <T>
     * @param columnSets
     * @param columnSetPattern
     */
    protected <T extends NamedColumnSet> List<T> filterColumnSets(List<T> columnSets, String columnSetPattern) {
        if (needFilter(columnSetPattern)) {
            String[] patterns = cleanPatterns(columnSetPattern.split(",")); //$NON-NLS-1$
            return filterMatchingColumnSets(columnSets, patterns);
        }
        return columnSets;
    }

    /**
     * DOC xqliu Comment method "filterMatchingColumnSets". ADD xqliu 2009-05-07 bug 7234
     * 
     * @param <T>
     * @param columnSets
     * @param patterns
     */
    private <T extends NamedColumnSet> List<T> filterMatchingColumnSets(List<T> columnSets, String[] patterns) {
        List<T> retColumnSets = new ArrayList<T>();
        int size = 0;
        for (T t : columnSets) {
            for (String pattern : patterns) {
                // MOD scorreia 2009-05-13 use SQL patterns for filter
                String regex = pattern.replaceAll("%", ".*"); //$NON-NLS-1$ //$NON-NLS-2$
                if (t.getName().matches(regex)) {
                    retColumnSets.add(t);
                    size++;
                    if (size > TABLE_VIEW_MAX) {
                        return retColumnSets;
                    }
                    break;
                }
            }
        }
        return retColumnSets;
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
