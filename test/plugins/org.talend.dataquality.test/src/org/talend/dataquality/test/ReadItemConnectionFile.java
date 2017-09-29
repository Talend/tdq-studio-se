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
package org.talend.dataquality.test;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.emf.EMFUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.ViewHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC scorreia class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class ReadItemConnectionFile {

    /**
     * DOC scorreia Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        EMFUtil emfUtil = new EMFUtil();
        File file = new File("data/my_0.1.item");
        System.out.println("Loading file " + file.getAbsolutePath());
        ResourceSet rs = emfUtil.getResourceSet();
        Resource r = rs.getResource(URI.createFileURI(file.getAbsolutePath()), true);
        // TreeIterator<EObject> allContents = r.getAllContents();
        // while (allContents.hasNext()) {
        // EObject metadata = allContents.next();
        // System.out.println(metadata);
        // }

        // should contain the connection, then catalog/schema, then tables, then columns
        EList<EObject> contents = r.getContents();

        // switch class that returns a connection when it finds one.
        ConnectionSwitch<Connection> connectionFinder = new ConnectionSwitch<Connection>() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch#caseConnection(org.talend.core
             * .model.metadata.builder.connection.Connection)
             */
            @Override
            public Connection caseConnection(Connection object) {
                return object;
            }
        };

        // loop on all the content of the resource
        for (EObject eObject : contents) {
            Connection connection = connectionFinder.doSwitch(eObject);
            if (connection != null) {

                Set<Catalog> allCatalogs = ConnectionHelper.getAllCatalogs(connection);
                for (Catalog catalog : allCatalogs) {
                    System.out.println("## Catalog: " + catalog.getName());
                    System.out.println("\t## Tables");
                    List<TdTable> tables = CatalogHelper.getTables(catalog);
                    for (TdTable tdTable : tables) {
                        System.out.println("\t\t" + tdTable.getName());
                        List<TdColumn> columns = TableHelper.getColumns(tdTable);
                        if (!columns.isEmpty()) {
                            System.out.println("\t\t\t## Columns");
                        }
                        for (TdColumn tdColumn : columns) {
                            System.out.println("\t\t\t\t" + tdColumn.getName());
                        }

                    }
                    System.out.println("\t## Views");
                    List<TdView> views = CatalogHelper.getViews(catalog);
                    for (TdView tdView : views) {
                        System.out.println("\t\t" + tdView.getName());
                        List<TdColumn> columns = ViewHelper.getColumns(tdView);
                        if (!columns.isEmpty()) {
                            System.out.println("\t\t\t## Columns");
                        }
                        for (TdColumn tdColumn : columns) {
                            System.out.println("\t\t\t\t" + tdColumn.getName());
                        }

                    }

                }

            }
        }
    }

}
