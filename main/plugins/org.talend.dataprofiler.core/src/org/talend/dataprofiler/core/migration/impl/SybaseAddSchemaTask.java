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
package org.talend.dataprofiler.core.migration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

public class SybaseAddSchemaTask extends AbstractWorksapceUpdateTask {

    public static final String TABLE_OWNER = "Table Owner"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(SybaseAddSchemaTask.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2013, 12, 13);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        List<IRepositoryViewObject> allConnectionObject = ProxyRepositoryFactory.getInstance().getAll(
                ERepositoryObjectType.METADATA_CONNECTIONS);

        for (IRepositoryViewObject object : allConnectionObject) {

            ConnectionItem item = (ConnectionItem) object.getProperty().getItem();
            if (item.getConnection() instanceof DatabaseConnection) {
                DatabaseConnection connection = (DatabaseConnection) item.getConnection();
                String DBTypeName = connection.getDatabaseType();
                // it is sybase database and is not contain schema structor
                if (EDatabaseTypeName.SYBASEASE == EDatabaseTypeName.getTypeFromDisplayName(DBTypeName)
                        && !ConnectionHelper.hasSchema(connection)) {
                    boolean needToSave = retrieveSchema(connection);
                    if (needToSave) {
                        ProxyRepositoryFactory.getInstance().save(item);
                    }
                }
            }
        }

        return true;
    }

    /**
     * DOC talend Comment method "retrieveSchema".
     * 
     * @param item
     * @param connection
     * @throws PersistenceException
     */
    private boolean retrieveSchema(DatabaseConnection connection) throws PersistenceException {
        boolean returnCode = false;
        for (orgomg.cwm.objectmodel.core.Package catalogOrSchema : connection.getDataPackage()) {
            if (catalogOrSchema instanceof Catalog) {
                Map<Schema, List<ModelElement>> SchemaToTableListMap = linkSchemaToCatalog(catalogOrSchema);
                linkTablesToSchema(catalogOrSchema, SchemaToTableListMap);
                if (SchemaToTableListMap.size() > 0) {
                    returnCode = true;
                }
            }
        }
        return returnCode;
    }

    /**
     * DOC talend Comment method "linkSchemaToCatalog".
     * 
     * @param catalog
     * @return
     */
    private Map<Schema, List<ModelElement>> linkSchemaToCatalog(orgomg.cwm.objectmodel.core.Package catalog) {
        Map<String, Schema> schemaMap = new HashMap<String, Schema>();
        Map<Schema, List<ModelElement>> SchemaToTableListMap = new HashMap<Schema, List<ModelElement>>();
        List<ModelElement> ownedElementList = new ArrayList<ModelElement>();
        ownedElementList.addAll(((Catalog) catalog).getOwnedElement());
        for (ModelElement table : ownedElementList) {
            TaggedValue tableOwnerTagValue = TaggedValueHelper.getTaggedValue(TABLE_OWNER, table.getTaggedValue());
            if (tableOwnerTagValue == null) {
                log.warn("the table don't contain TableOwner taggedValue so can not retrive schema structor!"); //$NON-NLS-1$
                continue;
            }
            String schemaName = tableOwnerTagValue.getValue();
            Schema currentSchema = schemaMap.get(schemaName);
            // the case : the Schema has been link to catalog
            if (currentSchema == null) {

                currentSchema = SchemaHelper.createSchema(schemaName);
                CatalogHelper.addSchemas(currentSchema, (Catalog) catalog);
                List<ModelElement> tableList = new ArrayList<ModelElement>();
                tableList.add(table);
                SchemaToTableListMap.put(currentSchema, tableList);
                schemaMap.put(schemaName, currentSchema);
                // the case : the schema is not exist yet
            } else {
                List<ModelElement> tableList = SchemaToTableListMap.get(currentSchema);
                tableList.add(table);
            }
            table.getTaggedValue().remove(tableOwnerTagValue);
        }
        return SchemaToTableListMap;
    }

    /**
     * DOC talend Comment method "linkTablesToSchema".
     * 
     * @param catalog
     * @param SchemaToTableListMap
     */
    private void linkTablesToSchema(orgomg.cwm.objectmodel.core.Package catalog,
            Map<Schema, List<ModelElement>> SchemaToTableListMap) {
        for (Schema theSchema : SchemaToTableListMap.keySet()) {
            List<ModelElement> elementList = theSchema.getOwnedElement();
            Resource eResource = theSchema.eResource();
            if (eResource != null) {
                eResource.getContents().addAll(SchemaToTableListMap.get(theSchema));
            }
            elementList.addAll(SchemaToTableListMap.get(theSchema));
            ((Catalog) catalog).getOwnedElement().removeAll(SchemaToTableListMap.get(theSchema));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AWorkspaceTask#valid()
     */
    @Override
    public boolean valid() {
        return ResourceManager.getDataProfilingFolder().exists();
    }

}
