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
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author rli
 * 
 */
public class TableFolderNode extends NamedColumnSetFolderNode<TdTable> {

    /**
     * 
     */
    public TableFolderNode() {
        super(DefaultMessagesImpl.getString("TableFolderNode.tables")); //$NON-NLS-1$
    }

    @Override
    public void loadChildren() {
        // MODSCA 2008-03-14 load children also when no catalog is given, but a schema exists (e.g. for DB2 database)
        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch((EObject) getParent());
        if (catalog != null) {
            loadChildrenLow(catalog, catalog, null, new ArrayList<TdTable>());
        } else {
            Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch((EObject) getParent());
            if (schema != null) {
                loadChildrenLow(schema, null, schema, new ArrayList<TdTable>());
            }
        }
        super.loadChildren();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.model.nodes.AbstractFolderNode#getColumnSets(org.talend.cwm.relational.TdCatalog,
     * org.talend.cwm.relational.TdSchema)
     */
    @Override
    protected List<TdTable> getColumnSets(Catalog catalog, Schema schema) {
        if (catalog != null) {
            return CatalogHelper.getTables(catalog);
        }
        if (schema != null) {
            return SchemaHelper.getTables(schema);
        }
        return Collections.emptyList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.model.nodes.AbstractFolderNode#loadColumnSets(org.talend.cwm.relational.TdCatalog,
     * org.talend.cwm.relational.TdSchema, org.talend.cwm.softwaredeployment.TdDataProvider, java.util.List)
     */
    @Override
    protected <T extends List<TdTable>> boolean loadColumnSets(Catalog catalog, Schema schema, Connection provider,
            final T columnSets) {
        try {
            boolean ok = false;
            assert provider != null : DefaultMessagesImpl.getString("TableFolderNode.noProvider"); //$NON-NLS-1$
            assert catalog != null ^ schema != null : DefaultMessagesImpl.getString("TableFolderNode.catalogOrSchema", //$NON-NLS-1$
                    provider.getName());

            if (catalog != null) {
                ok = columnSets.addAll(DqRepositoryViewService.getTables(provider, catalog, null, true, true));
            }
            if (schema != null) {
                ok = columnSets.addAll(DqRepositoryViewService.getTables(provider, schema, null, true, true));
            }
            return ok;
        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
            return false;
        }
    }

    @Override
    public int getFolderNodeType() {
        return TABLEFOLDER_NODE_TYPE;
    }

    @Override
    protected List<TdTable> getColumnSetsWithFilter(Catalog catalog, Schema schema) {
        if (catalog != null) {
            String tableFilter = ColumnSetHelper.getTableFilter(catalog);
            return filterColumnSets(CatalogHelper.getTables(catalog), tableFilter);
        }
        if (schema != null) {
            String tableFilter = ColumnSetHelper.getTableFilter(schema);
            return filterColumnSets(SchemaHelper.getTables(schema), tableFilter);
        }
        return Collections.emptyList();
    }

}
