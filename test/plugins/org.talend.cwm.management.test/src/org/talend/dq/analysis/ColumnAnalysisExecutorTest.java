// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;

import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class ColumnAnalysisExecutorTest {

    /**
     * when there are some catelog
     * {@link org.talend.dq.analysis.ColumnAnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)}
     * .
     */
    @Test
    public void testCreateSqlStatement_1() {
        Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisContext context = AnalysisFactory.eINSTANCE.createAnalysisContext();

        analysis.setContext(context);
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();

        TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
        tdTable.setName("tableName"); //$NON-NLS-1$
        tdColumn.setOwner(tdTable);
        tdColumn.setName("columnName"); //$NON-NLS-1$

        AnalysisParameters analysisPara = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        analysisPara.setStoreData(false);
        analysis.setParameters(analysisPara);
        context.getAnalysedElements().add(tdColumn);

        AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations info = AnalysisFactory.eINSTANCE.createExecutionInformations();
        analysisResult.setResultMetadata(info);
        analysis.setResults(analysisResult);

        Package catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();// mock(Catalog.class);
        catalog.setName("catalogName"); //$NON-NLS-1$
        tdTable.setNamespace(catalog);
        DatabaseConnection createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection
                .getTaggedValue()
                .add(TaggedValueHelper
                        .createTaggedValue(TaggedValueHelper.DB_PRODUCT_NAME,
                                SupportDBUrlType.MYSQLDEFAULTURL.getLanguage()));
        createConnection
                .getTaggedValue()
                .add(TaggedValueHelper.createTaggedValue(TaggedValueHelper.DB_PRODUCT_VERSION, "5.0")); //$NON-NLS-1$
        analysis.getContext().setConnection(createConnection);

        ColumnAnalysisExecutor columnAnalysisExecutor = new ColumnAnalysisExecutor();
        assertEquals("SELECT `columnName` FROM `catalogName`.`tableName`", //$NON-NLS-1$
                columnAnalysisExecutor.createSqlStatement(analysis));
    }

    /**
     * when there are some schema.
     * {@link org.talend.dq.analysis.ColumnAnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)}
     * .
     */
    @Test
    public void testCreateSqlStatement_2() {
        Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisContext context = AnalysisFactory.eINSTANCE.createAnalysisContext();

        analysis.setContext(context);
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();

        TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
        tdTable.setName("tableName"); //$NON-NLS-1$
        tdColumn.setOwner(tdTable);
        tdColumn.setName("columnName"); //$NON-NLS-1$

        AnalysisParameters analysisPara = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        analysisPara.setStoreData(false);
        analysis.setParameters(analysisPara);
        context.getAnalysedElements().add(tdColumn);

        AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations info = AnalysisFactory.eINSTANCE.createExecutionInformations();
        analysisResult.setResultMetadata(info);
        analysis.setResults(analysisResult);

        Package schema = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createSchema();// mock(Schema.class);
        schema.setName("schemaName"); //$NON-NLS-1$
        tdTable.setNamespace(schema);

        Package catalog = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createCatalog();// mock(Catalog.class);
        catalog.setName("catalogName"); //$NON-NLS-1$
        schema.setNamespace(catalog);

        DatabaseConnection createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection
                .getTaggedValue()
                .add(TaggedValueHelper
                        .createTaggedValue(TaggedValueHelper.DB_PRODUCT_NAME,
                                SupportDBUrlType.MSSQLDEFAULTURL.getLanguage()));
        analysis.getContext().setConnection(createConnection);

        ColumnAnalysisExecutor columnAnalysisExecutor = new ColumnAnalysisExecutor();
        assertEquals("SELECT \"columnName\" FROM \"catalogName\".\"schemaName\".\"tableName\"", //$NON-NLS-1$
                columnAnalysisExecutor.createSqlStatement(analysis));
    }

}
