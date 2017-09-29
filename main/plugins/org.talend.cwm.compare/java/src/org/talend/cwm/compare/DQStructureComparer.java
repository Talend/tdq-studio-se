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
package org.talend.cwm.compare;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.i18n.DefaultMessagesImpl;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class DQStructureComparer {

    public static final String COMPARE_FILE_EXTENSION = "comp"; //$NON-NLS-1$

    public static final String RESULT_EMFDIFF_FILE_EXTENSION = "emfdiff"; //$NON-NLS-1$

    private static final String NEED_RELOAD_ELEMENTS_PRV = ".needReloadElements.comp"; //$NON-NLS-1$

    private static final String RESULT_EMFDIFF_FILE = ".result.emfdiff"; //$NON-NLS-1$

    private static final String TEMP_REFRESH_FILE = ".refresh.comp"; //$NON-NLS-1$

    // ADD mzhao 2009-01-20 Add two temporary comparison files and one diff
    // result file at local
    // structure.
    private static final String FIRST_COMPARE_FILE = ".first_local.comp"; //$NON-NLS-1$

    private static final String SECOND_COMPARE_FILE = ".second_local.comp"; //$NON-NLS-1$

    private static final String RESULT_EMFDIFF_LOCAL_FILE = ".result_local.emfdiff"; //$NON-NLS-1$

    private static final Class<DQStructureComparer> THAT = DQStructureComparer.class;

    protected static Logger log = Logger.getLogger(THAT);

    @SuppressWarnings("unused")
    private static DQStructureComparer comparer = new DQStructureComparer();

    private DQStructureComparer() {
    }

    /**
     * Method "getCopyedFile" copies the source file into the destination file .
     * 
     * @param sourceFile
     * @param destinationFile
     * @return
     */
    public static IFile copyedToDestinationFile(IFile sourceFile, IFile destinationFile) {
        IFile desFile = destinationFile;
        try {
            if (destinationFile.exists()) {
                IFolder parentFolder = (IFolder) destinationFile.getParent();
                String fileName = desFile.getName();
                deleteFile(destinationFile);
                desFile = parentFolder.getFile(fileName);
            }

            sourceFile.copy(desFile.getFullPath(), true, new NullProgressMonitor());
        } catch (CoreException e) {
            log.error(e, e);
        }
        return desFile;
    }

    public static IFile getTempRefreshFile() {
        IFile file = iterateGetNotExistFile(TEMP_REFRESH_FILE);
        return file;
    }

    private static IFile iterateGetNotExistFile(String fileName) {
        IFile file = getFile(fileName);
        if (file.exists()) {
            return iterateGetNotExistFile(fileName.substring(0, fileName.lastIndexOf(".")) + EcoreUtil.generateUUID() //$NON-NLS-1$ 
                    + fileName.substring(fileName.lastIndexOf("."))); //$NON-NLS-1$
        } else {
            return file;
        }
    }

    public static IFile getNeedReloadElementsFile() {
        IFile file = iterateGetNotExistFile(NEED_RELOAD_ELEMENTS_PRV);
        return file;
    }

    /**
     * 
     * DOC mzhao Comment method "getFirstComparisonLocalFile".
     * 
     * @return First comparison file.
     */
    public static IFile getFirstComparisonLocalFile() {
        IFile file = getFile(FIRST_COMPARE_FILE);
        return file;
    }

    /**
     * 
     * DOC mzhao Comment method "getSecondComparisonLocalFile".
     * 
     * @return Second comparison file.
     */
    public static IFile getSecondComparisonLocalFile() {
        IFile file = getFile(SECOND_COMPARE_FILE);
        return file;
    }

    /**
     * Method "deleteCopiedResourceFile".
     * 
     * @return true if temporary file ".refresh.prv" has been deleted (or did not exist)
     */
    // public static boolean deleteCopiedResourceFile() {
    // return deleteFile(getTempRefreshFile());
    // }
    //
    // public static boolean deleteNeedReloadElementFile() {
    // return deleteFile(getNeedReloadElementsFile());
    // }

    /**
     * 
     * DOC mzhao Delete first selected resource tmp file.
     * 
     * @return
     */
    public static boolean deleteFirstResourceFile() {
        return deleteFile(getFirstComparisonLocalFile());
    }

    /**
     * 
     * DOC mzhao Delete second selected resource tmp file.
     * 
     * @return
     */
    public static boolean deleteSecondResourceFile() {
        return deleteFile(getSecondComparisonLocalFile());
    }

    public static IFile getDiffResourceFile() {
        IFile file = iterateGetNotExistFile(RESULT_EMFDIFF_FILE);
        try {
            InputStream inputStream = new ByteArrayInputStream("".getBytes()); //$NON-NLS-1$
            file.create(inputStream, true, new NullProgressMonitor());
            inputStream.close();
        } catch (CoreException e) {
            log.error(e, e);
        } catch (IOException e) {
            log.error(e, e);
        }
        return file;
    }

    /**
     * 
     * DOC mzhao Get compared emf diff result file.
     * 
     * @return
     */
    public static IFile getLocalDiffResourceFile() {
        IFile file = getFile(RESULT_EMFDIFF_LOCAL_FILE);
        return file;
    }

    /**
     * To delete the file of "DB Connections" folder by the specific fileName.
     * 
     * @return
     */
    public static boolean deleteFile(IFile file) {
        boolean retValue = false;
        if (file.exists()) {
            URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
            EMFSharedResources.getInstance().unloadResource(uri.toString());
            try {
                file.delete(true, new NullProgressMonitor());
                retValue = true;
            } catch (CoreException e) {

                log.warn(DefaultMessagesImpl.getString("DQStructureComparer.errorDelTmpFile", file.getFullPath().toOSString()), e);//$NON-NLS-1$
                retValue = false;
            }
        } else {
            retValue = true;
        }
        return retValue;
    }

    /**
     * 
     * remove one Resource from workspace contains unload,remove from resourceSet and delete file
     * 
     * @param currResource
     * @return
     */
    public static boolean removeResourceFromWorkspace(Resource currResource) {
        boolean returnCode = false;
        if (currResource != null) {
            URI removeUri = currResource.getURI();
            if (removeUri == null) {
                return returnCode;
            }
            IFile modelElementResource = null;
            if (removeUri.isPlatformResource()) {
                modelElementResource = WorkspaceUtils.getModelElementResource(removeUri);
            } else {
                modelElementResource = WorkspaceUtils.fileToIFile(new File(removeUri.toFileString()));
            }
            if (modelElementResource != null && modelElementResource.exists()) {
                returnCode = deleteFile(modelElementResource);
            }
        }
        return returnCode;
    }

    /**
     * 
     * DOC mzhao get file by name at the same location.
     * 
     * @param fileName
     * @return IFile
     */
    private static IFile getFile(String fileName) {
        IFolder folder = ResourceManager.getConnectionFolder();
        IFile file = folder.getFile(fileName);
        return file;
    }

    public static TypedReturnCode<Connection> getRefreshedDataProvider(Connection prevDataProvider) {
        // ADD xqliu 2010-03-29 bug 11951
        TypedReturnCode<Connection> returnProvider = new TypedReturnCode<Connection>();
        // ~11951

        // MOD by zshen 2012-07-05 for bug 5074 remove convert about DatabaseParameter instead
        // Connection->DatabaseParameter->ImetadataConnection into Connection->ImetadataConnection
        IMetadataConnection metadataConnection = ConvertionHelper.convert((DatabaseConnection) prevDataProvider, false,
                prevDataProvider.getContextName());
        Connection copyedConnection = null;
        EDatabaseTypeName currentEDatabaseType = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        if (currentEDatabaseType != null) {
            MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(metadataConnection);
            TypedReturnCode<?> trc = (TypedReturnCode<?>) dbInstance.createConnection(metadataConnection);
            Object sqlConnObject = trc.getObject();
            DatabaseMetaData dbJDBCMetadata = null;
            if (trc.isOk() && sqlConnObject instanceof java.sql.Connection) {
                java.sql.Connection sqlConn = (java.sql.Connection) sqlConnObject;

                // MOD sizhaoliu 2012-5-21 TDQ-4884 reload structure issue
                // dbJDBCMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(sqlConn);
                dbJDBCMetadata = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConn,
                        (DatabaseConnection) prevDataProvider);

                copyedConnection = EObjectHelper.deepCopy(prevDataProvider);
                copyedConnection.getDataPackage().clear();
                // MOD zshen the parameter for packageFiler need to differnent isCatalog or not.
                dbInstance.fillCatalogs(copyedConnection, dbJDBCMetadata, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(copyedConnection, dbJDBCMetadata, true));
                dbInstance.fillSchemas(copyedConnection, dbJDBCMetadata, metadataConnection,
                        MetadataConnectionUtils.getPackageFilter(copyedConnection, dbJDBCMetadata, false));

                ConnectionUtils.closeConnection(sqlConn);

            } else {
                returnProvider.setMessage(trc.getMessage());
            }

        }
        if (copyedConnection == null) {
            returnProvider.setOk(false);
        } else {
            returnProvider.setObject(copyedConnection);
        }
        // ~11951
        return returnProvider;
    }

    /**
     * Find the matched package of matchDataProvider.
     * 
     * @param selectedPackage
     * @param matchDataProvider
     * @return
     * @throws ReloadCompareException
     */
    public static Package findMatchedPackage(Package selectedPackage, Connection matchDataProvider) throws ReloadCompareException {

        // code clean by gdbu 2011-4-18 : when conn is null , throw a ReloadCompareException.
        if (null == matchDataProvider) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("DQStructureComparer.ConnectionIsNull")); //$NON-NLS-1$
        }
        // code clean

        Catalog catalogCase = SwitchHelpers.CATALOG_SWITCH.doSwitch(selectedPackage);
        if (catalogCase != null) {
            return findMatchedCatalogObj(catalogCase, matchDataProvider);
        } else {
            Schema schemaCase = (Schema) selectedPackage;
            Catalog parentCatalog = CatalogHelper.getParentCatalog(schemaCase);
            if (parentCatalog != null) {
                Catalog matchCatalog = findMatchedCatalogObj(parentCatalog, matchDataProvider);
                List<Schema> schemas = CatalogHelper.getSchemas(matchCatalog);
                return findMatchedSchema(schemaCase, schemas);
            } else {
                List<Schema> schemas = ConnectionHelper.getSchema(matchDataProvider);
                return findMatchedSchema(schemaCase, schemas);
            }
        }
    }

    /**
     * Find the matched columnSet of matchDataProvider.
     * 
     * @param selectedColumnSet
     * @return
     * @throws ReloadCompareException
     */
    public static ColumnSet findMatchedColumnSet(ColumnSet selectedColumnSet, Connection toMatchDataProvider)
            throws ReloadCompareException {
        Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(selectedColumnSet);

        // find the corresponding package from reloaded object.
        Package toReloadPackage = DQStructureComparer.findMatchedPackage(parentCatalogOrSchema, toMatchDataProvider);

        // find the corresponding columnSet from reloaded object.
        TdTable oldTable = SwitchHelpers.TABLE_SWITCH.doSwitch(selectedColumnSet);
        ColumnSet toReloadcolumnSet = null;
        if (oldTable != null) {
            List<TdTable> tables = PackageHelper.getTables(toReloadPackage);
            for (TdTable table : tables) {
                // bug 11934 MOD zshen judge the tableOwner when database is sybase.
                if (oldTable.getName().equals(table.getName())) {
                    // ~11934
                    toReloadcolumnSet = table;
                    break;
                }
            }

        } else {
            List<TdView> views = PackageHelper.getViews(toReloadPackage);
            for (TdView view : views) {
                // bug 11934 MOD zshen judge the viewOwner when database is sybase.
                if (selectedColumnSet.getName().equals(view.getName())) {
                    // ~11934
                    toReloadcolumnSet = view;
                    break;
                }
            }
        }
        if (toReloadcolumnSet == null) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("DQStructureComparer.NotFindCorrespondNode",//$NON-NLS-1$
                    selectedColumnSet.getName()));
        }
        return toReloadcolumnSet;
    }

    /**
     * 
     * DOC mzhao Find the matched column of toMatchDataProvider.
     * 
     * @param column
     * @param toMatchDataProvider
     * @return
     * @throws ReloadCompareException
     */
    public static TdColumn findMatchedColumn(TdColumn column, Connection toMatchDataProvider) throws ReloadCompareException {
        // MOD klliu update ColumnHelper.getColumnSetOwner(column)
        ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(column);
        ColumnSet toReloadColumnSet = DQStructureComparer.findMatchedColumnSet(columnSet, toMatchDataProvider);
        List<TdColumn> columns = ColumnSetHelper.getColumns(toReloadColumnSet);
        TdColumn oldColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(column);
        TdColumn toMatchedColumn = null;
        if (oldColumn != null) {
            for (TdColumn col : columns) {
                if (oldColumn.getName().equals(col.getName())) {
                    toMatchedColumn = col;
                    break;
                }
            }
        }

        if (toMatchedColumn == null) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("DQStructureComparer.NotFoundCorrespondColumnNode",//$NON-NLS-1$
                    column.getName()));
        }
        return toMatchedColumn;

    }

    /**
     * DOC rli Comment method "findMatchSchema".
     * 
     * @param schemaCase
     * @param schemas
     * @throws ReloadCompareException
     */
    private static Schema findMatchedSchema(Schema schemaCase, List<Schema> schemas) throws ReloadCompareException {
        for (Schema schema : schemas) {
            if (schemaCase.getName().equals(schema.getName())) {
                return schema;
            }
        }
        throw new ReloadCompareException(DefaultMessagesImpl.getString("DQStructureComparer.NotFoundCorrespondSchemaNode", //$NON-NLS-1$
                schemaCase.getName()));
    }

    /**
     * DOC rli Comment method "findMatchCatalogObj".
     * 
     * @param catalog
     * @throws ReloadCompareException
     */
    private static Catalog findMatchedCatalogObj(Catalog catalog, Connection matchDataProvider) throws ReloadCompareException {
        List<Catalog> tdCatalogs = ConnectionHelper.getCatalogs(matchDataProvider);
        for (Catalog matchCatalog : tdCatalogs) {
            if (catalog.getName().equals(matchCatalog.getName())) {
                return matchCatalog;
            }
        }
        throw new ReloadCompareException(DefaultMessagesImpl.getString("DQStructureComparer.NotFoundCorrespondCatalogNode" //$NON-NLS-1$
                , catalog.getName()));
    }

    public static void clearSubNode(ModelElement needReloadElement) {
        Connection dataProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(needReloadElement);
        if (dataProvider != null) {
            List<Catalog> tdCatalogs = ConnectionHelper.getCatalogs(dataProvider);
            for (Catalog catalog : tdCatalogs) {
                clearSubNode(catalog);
            }
            List<Schema> tdSchemas = ConnectionHelper.getSchema(dataProvider);
            for (Schema schema : tdSchemas) {
                clearSubNode(schema);
            }
            return;
        }
        Catalog tdCatalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(needReloadElement);
        if (tdCatalog != null) {
            List<Schema> schemas = CatalogHelper.getSchemas(tdCatalog);
            for (Schema schema : schemas) {
                clearSubNode(schema);
            }
            if (schemas.size() == 0) {
                tdCatalog.getOwnedElement().clear();
            }
            return;
        }
        Schema tdSchema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(needReloadElement);
        if (tdSchema != null) {
            tdSchema.getOwnedElement().clear();
            return;
        }

        ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(needReloadElement);
        if (columnSet != null) {
            columnSet.getFeature().clear();
            columnSet.getTaggedValue().clear();
            // ~MOD mzhao 2009-03-12 Clear primary key(contains in ownedElement)
            // as well. If not clear, it will cause
            // exception: not contained in
            // a resource...
            columnSet.getOwnedElement().clear();
            return;
        }
        TdColumn column = SwitchHelpers.COLUMN_SWITCH.doSwitch(needReloadElement);
        if (column != null) {
            column.getTaggedValue().clear();
            // ~MOD mzhao 2009-03-12 Clear primary key as well. If not clear, it
            // will cause exception: not contained in
            // a resource...
            column.getUniqueKey().clear();
            // ~
            // ~MOD mzhao 2009-04-08 Clear foreign key.
            column.getKeyRelationship().clear();
            return;
        }
    }

    /**
     * Open a compare editor UI, will clear the information which hasn't relationship with current selected level
     * first(For example: if we compare the catalog level, will clear it's table(view) from every catalog), then will
     * compare current level object.
     * 
     * @param rightResource
     * @param oldDataProviderFile
     * @return
     * @throws ReloadCompareException
     */
    // public static DiffModel openDiffCompareEditor(Resource leftResource, Resource rightResource, Map<String, Object>
    // opt,
    // IUIHandler guiHandler, IFile efmDiffResultFile, String dbName, Object selectedObject, boolean compareEachOther)
    // throws ReloadCompareException {
    //
    // // ~ MOD mzhao bug 11449. 2010-03-16
    // if (leftResource.getContents() == null || leftResource.getContents().size() == 0) {
    // // Could not merge this.
    // MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
    //                    DefaultMessagesImpl.getString("DQStructureComparer.errorDialog1"), //$NON-NLS-1$
    //                    DefaultMessagesImpl.getString("DQStructureComparer.errorDialog2"));//$NON-NLS-1$ $NON-NLS-2$
    // DQStructureComparer.removeResourceFromWorkspace(leftResource);
    // DQStructureComparer.removeResourceFromWorkspace(rightResource);
    // DQStructureComparer.deleteFile(efmDiffResultFile);
    // return null;
    // }
    // MatchModel match = null;
    // try {
    // boolean isTos = isTos(leftResource);
    // match = MatchService
    // .doResourceMatch(cleanUpResource(leftResource, isTos), cleanUpResource(rightResource, isTos), opt);
    // } catch (InterruptedException e) {
    // throw new ReloadCompareException(e);
    // }
    // final DiffModel diff = DiffService.doDiff(match);
    // EList<DiffElement> ownedElements = diff.getOwnedElements();
    // for (DiffElement de : ownedElements) {
    // EList<DiffElement> subDiffElements = de.getSubDiffElements();
    // for (DiffElement difElement : subDiffElements) {
    // if (difElement instanceof ModelElementChangeRightTarget) {
    // ((ModelElementChangeRightTarget) difElement).setLeftParent(leftResource.getContents().get(0));
    //
    // }
    // }
    // }
    // // ~
    //
    // // Open UI for different comparison
    // final ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
    // snapshot.setDate(Calendar.getInstance().getTime());
    // snapshot.setMatch(match);
    // snapshot.setDiff(diff);
    // IFile createDiffResourceFile = efmDiffResultFile;
    // try {
    // // klliu
    // createDiffResourceFile.clearHistory(new NullProgressMonitor());
    // final String fullPath = createDiffResourceFile.getLocation().toOSString();
    //
    // ModelUtils.save(snapshot, fullPath);
    // } catch (IOException e) {
    // throw new ReloadCompareException(e);
    // } catch (CoreException e) {
    // log.error(e);
    // }
    // if (guiHandler != null) {
    // guiHandler.popComparisonUI(createDiffResourceFile.getLocation(), dbName, selectedObject, compareEachOther);
    // }
    // return diff;
    // }

    private static boolean isTos(Resource resource) {
        EList<EObject> contents = resource.getContents();
        for (EObject content : contents) {
            if (content instanceof TdColumn) {
                TdColumn tdColumn = (TdColumn) content;
                return tdColumn.getSourceType() != null;
            }
        }
        return false;
    }

    private static Resource cleanUpResource(Resource resource, boolean isTos) {
        boolean isTosTemp = isTos;
        EList<EObject> contents = resource.getContents();
        for (EObject content : contents) {
            if (content instanceof TdTable) {
                TdTable tdTable = (TdTable) content;
                isTosTemp = tdTable.getId() != null;
                tdTable.setId(null);
                tdTable.setComment(null);
            } else if (content instanceof TdView) {
                TdView tdView = (TdView) content;
                tdView.setId(null);
                tdView.setComment(null);
            } else if (content instanceof TdColumn) {
                TdColumn tdColumn = (TdColumn) content;
                tdColumn.setSourceType(null);
                if (isTosTemp) {
                    tdColumn.setLength(10);
                    tdColumn.setPrecision(4);
                    if (tdColumn.getInitialValue() != null) {
                        tdColumn.getInitialValue().setBody(null);
                    }
                    tdColumn.setPattern(null);
                    tdColumn.setKey(false);
                    tdColumn.setId(PluginConstant.EMPTY_STRING);
                }
            }
        }
        return resource;
    }
}
