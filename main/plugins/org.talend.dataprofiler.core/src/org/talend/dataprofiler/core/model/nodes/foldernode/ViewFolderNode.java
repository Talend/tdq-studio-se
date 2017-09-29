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

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdView;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author rli
 * 
 */
public class ViewFolderNode extends NamedColumnSetFolderNode<TdView> {

    protected static Logger log = Logger.getLogger(ViewFolderNode.class);

    /**
     * @param name
     */
    public ViewFolderNode() {
        super(DefaultMessagesImpl.getString("ViewFolderNode.views")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.model.nodes.AbstractFolderNode#loadChildren()
     */
    @Override
    public void loadChildren() {
        // MODSCA 2008-03-14 load children also when no catalog is given, but a schema exists (e.g. for DB2 database)
        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch((EObject) getParent());
        if (catalog != null) {
            loadChildrenLow(catalog, catalog, null, new ArrayList<TdView>());
        } else {
            Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch((EObject) getParent());
            if (schema != null) {
                loadChildrenLow(schema, null, schema, new ArrayList<TdView>());
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
    protected List<TdView> getColumnSets(Catalog catalog, Schema schema) {
        if (catalog != null) {
            return CatalogHelper.getViews(catalog);
        }
        if (schema != null) {
            return SchemaHelper.getViews(schema);
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
    protected <T extends List<TdView>> boolean loadColumnSets(Catalog catalog, Schema schema, Connection provider,
            final T columnSets) {
        try {
            boolean ok = false;
            assert provider != null : DefaultMessagesImpl.getString("ViewFolderNode.noProviderForViews"); //$NON-NLS-1$
            assert catalog != null ^ schema != null : DefaultMessagesImpl.getString("ViewFolderNode.catalogOrSchemaExist",//$NON-NLS-1$
                    provider.getName());

            if (catalog != null) {
                ok = columnSets.addAll(DqRepositoryViewService.getViews(provider, catalog, null, true, true));
            }
            if (schema != null) {
                ok = columnSets.addAll(DqRepositoryViewService.getViews(provider, schema, null, true, true));
            }
            return ok;
        } catch (Exception e) {
            log.error(e, e);
            return false;
        }
    }

    @Override
    public int getFolderNodeType() {
        return VIEWFOLDER_NODE_TYPE;
    }

    @Override
    protected List<TdView> getColumnSetsWithFilter(Catalog catalog, Schema schema) {
        if (catalog != null) {
            String viewFilter = ColumnSetHelper.getViewFilter(catalog);
            return filterColumnSets(CatalogHelper.getViews(catalog), viewFilter);
        }
        if (schema != null) {
            String viewFilter = ColumnSetHelper.getViewFilter(schema);
            return filterColumnSets(SchemaHelper.getViews(schema), viewFilter);
        }
        return Collections.emptyList();
    }

}
