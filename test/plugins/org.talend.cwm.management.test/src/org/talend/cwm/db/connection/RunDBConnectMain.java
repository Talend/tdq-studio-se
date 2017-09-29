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
package org.talend.cwm.db.connection;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class RunDBConnectMain {

    private static final Class<RunDBConnectMain> THAT = RunDBConnectMain.class;

    private static Logger log = Logger.getLogger(THAT);

    private RunDBConnectMain() {
    }

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    // public static void main(String[] args) {
    // TimeTracer tt = new TimeTracer("DB CONNECT", log);
    // tt.start();
    // // --- load connection parameters from properties file
    // TypedProperties connectionParams = PropertiesLoader.getProperties(THAT, "db.properties");
    //
    // String driverClassName = connectionParams.getProperty("driver");
    // String dbUrl = connectionParams.getProperty("url");
    // DBConnect connector = new DBConnect(dbUrl, driverClassName, connectionParams);
    // // --- connect and check the connection
    // boolean connected = connector.connectAndRetrieveInformations();
    // if (!connected) {
    // log.error("Could not connect to " + connector);
    // connector.closeConnection();
    // return; // BREAK here
    // }
    //
    // tt.end("Metadata informations retrieved");
    //
    // // --- start loading metadata into CWM structures
    //
    // TdSoftwareSystem softwareSystem = connector.getSoftwareSystem();
    // TypeSystem typeSystem = connector.getTypeSystem();
    // if (typeSystem == null) {
    // log.error("typeSystem is null");
    // }
    //
    // // TODO scorreia create a ProviderConnection for the given connection
    // // TdProviderConnection providerConnection = connector.getProviderConnection();
    //
    // Connection dataProvider = connector.getDataProvider();
    //
    // Collection<Catalog> catalogs = connector.getCatalogs();
    // Collection<Schema> schemata = connector.getSchemata();
    //
    // // --- print some informations
    // printInformations(catalogs, schemata);
    //
    // // --- save informations in file
    // String relational = RelationalPackage.eNAME;
    // String softwaredeployment = SoftwaredeploymentPackage.eNAME;
    //
    // for (Catalog tdCatalog2 : catalogs) {
    // String filename = createFilename(tdCatalog2.getName(), relational);
    // connector.storeInResourceSet(tdCatalog2, filename);
    // }
    //
    // for (Schema tdSchema2 : schemata) {
    // String filename = createFilename(tdSchema2.getName(), relational);
    // connector.storeInResourceSet(tdSchema2, filename);
    // }
    //
    // String filename = createFilename(softwareSystem.getName(), softwaredeployment);
    // connector.storeInResourceSet(softwareSystem, filename);
    // filename = createFilename(typeSystem.getName(), TypemappingPackage.eNAME);
    // connector.storeInResourceSet(typeSystem, filename);
    //
    // // filename = createFilename("provider", softwaredeployment);
    // // connector.storeInResourceSet(providerConnection, filename);
    //
    // filename = createFilename("driver", softwaredeployment);
    // connector.storeInResourceSet(dataProvider, filename);
    //
    // // --- finally save all
    // connector.saveInFiles();
    //
    // connector.closeConnection();
    // tt.end();
    // }

    private static String createFilename(String basename, String extension) {
        return "out" + File.separator + basename + "." + extension;
    }

    /**
     * Method "printInformations" only for test purposes.
     * 
     * @param catalogs
     * @param schemata
     */
    private static void printInformations(Collection<Catalog> catalogs, Collection<Schema> schemata) {
        for (Catalog tdCatalog : catalogs) {
            System.out.println("Catalog = " + tdCatalog);
        }
        for (Schema tdSchema : schemata) {
            System.out.println("Schema = " + tdSchema + " in catalog " + tdSchema.getNamespace());
        }
    }
}
