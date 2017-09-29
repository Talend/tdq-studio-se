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
package org.talend.dataprofiler.core.model;

import org.apache.log4j.Logger;

/**
 * DOC rli class global comment. Detailled comment
 */
public final class SqlExplorerBridge {

    private static Logger log = Logger.getLogger(SqlExplorerBridge.class);

    private SqlExplorerBridge() {

    }

    // public static TypedReturnCode<TableNode> findSqlExplorerTableNode(Connection providerConnection,
    // Package parentPackageElement, String tableName, String activeTabName) {
    //
    // // Open data explore perspective.
    // new ChangePerspectiveAction(PluginConstant.SE_ID).run();
    // Collection<Alias> aliases = SQLExplorerPlugin.getDefault().getAliasManager().getAliases();
    // String url = JavaSqlFactory.getURL(providerConnection);
    // User currentUser = null;
    // for (Alias alias : aliases) {
    // if (alias.getUrl().equals(url)) {
    // currentUser = alias.getDefaultUser();
    // OpenPasswordConnectDialogAction openDlgAction = new OpenPasswordConnectDialogAction(alias,
    // alias.getDefaultUser(), false);
    // openDlgAction.run();
    // break;
    // }
    // }
    //
    // // MOD qiongli bug 13093,2010-7-2,show the warning dialog when the table can't be found
    // Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    // if (currentUser == null) {
    //            MessageDialog.openWarning(shell, DefaultMessagesImpl.getString("SqlExplorerBridge.Warning"), //$NON-NLS-1$
    //                    DefaultMessagesImpl.getString("SqlExplorerBridge.MissTable", tableName)); //$NON-NLS-1$
    // return new TypedReturnCode<TableNode>(DefaultMessagesImpl.getString(
    //                    "SqlExplorerBridge.NotFindCorrespondTable", tableName), //$NON-NLS-1$
    // false);
    // }
    // DatabaseNode root = currentUser.getMetaDataSession().getRoot();
    // root.load();
    // List<INode> catalogs = root.getCatalogs();
    // List<INode> schemas = root.getSchemas();
    //
    // Catalog doSwitch = SwitchHelpers.CATALOG_SWITCH.doSwitch(parentPackageElement);
    // Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(parentPackageElement);
    //
    // INode catalogOrSchemaNode = null;
    // if (doSwitch != null) {
    // // MOD klliu bug 14662 2010-08-05
    // if (catalogs.size() != 0) {
    // for (INode catalogNode : catalogs) {
    // if (parentPackageElement.getName().equalsIgnoreCase(catalogNode.getName())) {
    // catalogOrSchemaNode = catalogNode;
    // break;
    // }
    // }
    // } else {
    // catalogOrSchemaNode = root;
    // }
    //
    // } else {
    // // MOD by zshen for 20517
    // if (schemas.size() == 0) {// the case for mssql/postgrel(which have catalog and schema structor) schema
    // // analysis.
    // Catalog shcmeaOfCatalogNode = CatalogHelper.getParentCatalog(parentPackageElement);
    // for (INode catalogNode : catalogs) {
    // if (shcmeaOfCatalogNode.getName().equalsIgnoreCase(catalogNode.getName())) {
    // catalogOrSchemaNode = catalogNode;
    // break;
    // }
    // }
    // }
    // for (INode schemaNode : schemas) {
    // if (parentPackageElement.getName().equalsIgnoreCase(schemaNode.getName())) {
    // catalogOrSchemaNode = schemaNode;
    // break;
    // }
    // }
    // }
    // // find the table folder node.
    // if (catalogOrSchemaNode == null) {
    //            throw new NullPointerException(DefaultMessagesImpl.getString("SqlExplorerBridge.CATORSCHMISNULL")); //$NON-NLS-1$
    // }
    // // Added 20130409 TDQ-6823 yyin when want to get some schema's tables, should give the schema name to the
    // // catalog node.
    // if (schema != null) {
    // if (catalogOrSchemaNode.getSchemaName() == null) {
    // catalogOrSchemaNode.setSchemaName(schema.getName());
    // } else if (!StringUtils.equals(catalogOrSchemaNode.getSchemaName(), schema.getName())) {
    // // if this catalog already loaded its children of some schema, should reload for this schema.
    // if (catalogOrSchemaNode.isChildrenLoaded()) {
    // SQLExplorerPlugin.getDefault().getDatabaseStructureView()
    // .refreshSessionTrees(currentUser.getMetaDataSession());
    // List<INode> catalogs2 = currentUser.getMetaDataSession().getRoot().getCatalogs();
    // if (catalogs2.size() != 0) {
    // for (INode catalogNode : catalogs2) {
    // if (catalogOrSchemaNode.getName().equalsIgnoreCase(catalogNode.getName())) {
    // catalogOrSchemaNode = catalogNode;
    // catalogOrSchemaNode.setSchemaName(schema.getName());
    // break;
    // }
    // }
    //
    // }
    // }
    // }
    // }// ~
    //
    // INode[] childNodes = catalogOrSchemaNode.getChildNodes();
    //
    // TableFolderNode tableFolderNode = null;
    // for (INode node : childNodes) {
    //            if ("TABLE".equals(node.getQualifiedName())) { //$NON-NLS-1$
    // tableFolderNode = (TableFolderNode) node;
    // break;
    // }
    // }
    // if (tableFolderNode == null) {
    //            log.fatal(DefaultMessagesImpl.getString("SqlExplorerBridge.TABLE_FOLDER_NULL0")); //$NON-NLS-1$
    // } else {
    // INode[] tableNodes = tableFolderNode.getChildNodes();
    // for (INode node : tableNodes) {
    // if (tableName.equalsIgnoreCase(node.getName())) {
    // TypedReturnCode<TableNode> typedReturnCode = new TypedReturnCode<TableNode>(null, true);
    // typedReturnCode.setObject((TableNode) node);
    //
    // DetailTabManager.setActiveTabName(activeTabName);
    // DatabaseStructureView dsView = SQLExplorerPlugin.getDefault().getDatabaseStructureView();
    // dsView.setSessionSelectionNode(currentUser.getMetaDataSession(), new StructuredSelection(node));
    // // MOD qiongli bug 13093,2010-7-2
    // SQLExplorerPlugin.getDefault().getConnectionsView().getTreeViewer()
    // .setSelection(new StructuredSelection(currentUser));
    //
    // return typedReturnCode;
    // }
    // }
    // }
    //        MessageDialog.openWarning(shell, DefaultMessagesImpl.getString("SqlExplorerBridge.Warning"), //$NON-NLS-1$
    //                DefaultMessagesImpl.getString("SqlExplorerBridge.NotFindCorrespondTable", tableName)); //$NON-NLS-1$
    // return new TypedReturnCode<TableNode>(DefaultMessagesImpl.getString(
    //                "SqlExplorerBridge.NotFindCorrespondTableObject", tableName), //$NON-NLS-1$
    // false);
    // }
}
