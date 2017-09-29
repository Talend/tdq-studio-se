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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.exception.TalendInternalPersistenceException;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ExportConnectionToTOSAction extends Action {

    private static Logger log = Logger.getLogger(ExportConnectionToTOSAction.class);

    private List<Package> packList = new ArrayList<Package>();

    public ExportConnectionToTOSAction(List<Package> packList) {
        super(DefaultMessagesImpl.getString("ExportConnectionToTOSAction.title"));//$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.EXPORT));

        this.packList = packList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (packList.isEmpty()) {
            return;
        }

        for (Package pack : packList) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

            DatabaseConnection tdDataProvider = (DatabaseConnection) ConnectionHelper.getTdDataProvider(pack);
            ConnectionItem connectionItem = initConnectionItem(tdDataProvider, pack);

            DatabaseConnection exportedConn = (DatabaseConnection) connectionItem.getConnection();
            Property connectionProperty = initConnectionProperty(exportedConn, pack);
            connectionProperty.setId(factory.getNextId());

            connectionItem.setProperty(connectionProperty);

            try {
                factory.create(connectionItem, new Path(""));//$NON-NLS-1$
                openSuccessInformation();
            } catch (TalendInternalPersistenceException e1) {
                //                MessageDialog.openError(null, DefaultMessagesImpl.getString("ExportConnectionToTOSAction.error"), e1.getMessage());//$NON-NLS-1$
            } catch (PersistenceException e) {
                MessageDialog.openError(null, DefaultMessagesImpl.getString("ExportConnectionToTOSAction.error"), e.getMessage());//$NON-NLS-1$
                log.error(e.getMessage(), e);
            }
        }
        refreshViewerAndNode();
    }

    /**
     * DOC zshen Comment method "refreshViewerAndNode".
     */
    protected void refreshViewerAndNode() {
        // refresh TDQ's matadata tree list
        RepositoryNodeHelper.getDQCommonViewer().refresh(RepositoryNodeHelper.getRootNode(ERepositoryObjectType.METADATA, true));
        // refresh TOS's matadata tree list
        RepositoryManager.refreshCreatedNode(ERepositoryObjectType.METADATA_CONNECTIONS);
    }

    /**
     * DOC zshen Comment method "openSuccessInformation".
     */
    protected void openSuccessInformation() {
        MessageDialog
                .openInformation(
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        DefaultMessagesImpl.getString("ExportConnectionToTOSAction.info"), DefaultMessagesImpl.getString("ExportConnectionToTOSAction.meta"));//$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * DOC bZhou Comment method "initConnectionProperty".
     * 
     * @param tdDataProvider
     * @return
     */
    private Property initConnectionProperty(DatabaseConnection tdDataProvider, Package pack) {
        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();

        String purpose = MetadataHelper.getPurpose(tdDataProvider);
        String description = MetadataHelper.getDescription(tdDataProvider);
        String status = MetadataHelper.getDevStatus(tdDataProvider);

        connectionProperty.setLabel(tdDataProvider.getName());
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setPurpose(purpose);
        connectionProperty.setDescription(description);
        connectionProperty.setStatusCode(status);
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);

        return connectionProperty;
    }

    /**
     * DOC bZhou Comment method "initConnectionItem".
     * 
     * @param tdDataProvider
     * @return
     */
    private ConnectionItem initConnectionItem(DatabaseConnection tdDataProvider, Package pack) {
        ConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        if (tdDataProvider != null) {
            DatabaseConnection exportedConn = EObjectHelper.deepCopy(tdDataProvider);

            // Remove the dependencies
            exportedConn.getSupplierDependency().clear();
            exportedConn.getClientDependency().clear();

            filterPackage(pack, exportedConn);

            updateConnectionParameter(exportedConn, pack);
            connectionItem.setConnection(exportedConn);
        }

        return connectionItem;
    }

    /**
     * DOC zshen Comment method "filterPackage".
     * 
     * @param pack
     * @param exportedConn
     */
    private void filterPackage(Package pack, DatabaseConnection exportedConn) {
        Package newPackage = null;
        String oldRootPackageName = null;
        if (pack instanceof Catalog) {
            oldRootPackageName = pack.getName();
        } else {
            Package parentPackage = PackageHelper.getParentPackage(pack);
            if (parentPackage == null) {
                oldRootPackageName = pack.getName();
            } else {
                oldRootPackageName = parentPackage.getName();
            }
        }
        // Add only the package: pack
        for (Package currPackage : exportedConn.getDataPackage()) {
            if (currPackage.getName().equalsIgnoreCase(oldRootPackageName)) {
                newPackage = currPackage;
                if (pack instanceof Schema) {
                    Schema newSchema = null;
                    String schemaName = pack.getName();
                    for (ModelElement CurrentSchema : currPackage.getOwnedElement()) {
                        if (CurrentSchema.getName().equals(schemaName)) {
                            newSchema = (Schema) CurrentSchema;
                            break;
                        }
                    }
                    if (newSchema != null) {
                        ((Catalog) currPackage).getOwnedElement().clear();
                        CatalogHelper.addSchemas(newSchema, (Catalog) currPackage);
                    }
                }
                break;
            }
        }
        if (newPackage != null) {
            exportedConn.getDataPackage().clear();
            newPackage.getDataManager().clear();
            ConnectionHelper.addPackage(newPackage, exportedConn);
        }
    }

    /**
     * DOC zshen Comment method "updateConnectionParameter".
     * 
     * @param exportedConn
     */
    private void updateConnectionParameter(DatabaseConnection exportedConn, Package pack) {
        String connName = exportedConn.getName();
        exportedConn.setLabel(connName + "_" + pack.getName()); //$NON-NLS-1$
        exportedConn.setName(connName + "_" + pack.getName()); //$NON-NLS-1$
        String database = pack.getName();
        // schema case
        if (pack instanceof Schema) {
            Package parent = ColumnSetHelper.getParentCatalogOrSchema(pack);
            if (parent != null) {
                database = parent.getName();
            } else {
                database = exportedConn.getSID();
            }
            exportedConn.setUiSchema(pack.getName());

            // teradata database use "database" attribute as uiSchema on the DatabaseWizard
            if (EDatabaseTypeName.TERADATA.getXmlName().equalsIgnoreCase(exportedConn.getDatabaseType())) {
                if (database.isEmpty()) {
                    database = pack.getName();
                }
            }
        }
        exportedConn.setSID(database);
    }

    /**
     * 
     * DOC gdbu Comment method "fillCatalogSchema".
     * 
     * @deprecated Won't be used.
     * @param tdDataProvider
     */
    @Deprecated
    protected DatabaseConnection fillCatalogSchema(IMetadataConnection newMetadataConn) {
        MetadataFillFactory instance = MetadataFillFactory.getDBInstance(newMetadataConn);

        ReturnCode rc = instance.createConnection(newMetadataConn);
        DatabaseConnection dbConn = null;
        if (rc.isOk()) {
            dbConn = (DatabaseConnection) instance.fillUIConnParams(newMetadataConn, null);
            DatabaseMetaData dbMetadata = null;
            java.sql.Connection sqlConn = null;
            try {
                if (rc instanceof TypedReturnCode) {
                    Object sqlConnObject = ((TypedReturnCode) rc).getObject();
                    if (sqlConnObject instanceof java.sql.Connection) {
                        sqlConn = (java.sql.Connection) sqlConnObject;
                        dbMetadata = ExtractMetaDataUtils.getInstance().getConnectionMetadata(sqlConn);
                    }
                }
                List<String> packageFilterCatalog = MetadataConnectionUtils.getPackageFilter(dbConn, dbMetadata, true);
                instance.fillCatalogs(dbConn, dbMetadata, packageFilterCatalog);
                List<String> packageFilterSchema = MetadataConnectionUtils.getPackageFilter(dbConn, dbMetadata, false);
                instance.fillSchemas(dbConn, dbMetadata, packageFilterSchema);

            } catch (SQLException e) {
                log.error(e, e);
            } finally {
                if (sqlConn != null) {
                    ConnectionUtils.closeConnection(sqlConn);
                }
            }
        } else {
            log.error(rc.getMessage());
        }
        return dbConn;
    }

    /**
     * DOC bZhou Comment method "retrieveDBVersion".
     * 
     * @param product
     * @return
     */
    private String retrieveDBVersion(String product) {
        List<EDatabaseVersion4Drivers> eVersions = EDatabaseVersion4Drivers.indexOfByDbType(product);
        if (eVersions != null && !eVersions.isEmpty()) {
            return eVersions.get(0).getVersionValue();
        }

        return null;
    }

}
