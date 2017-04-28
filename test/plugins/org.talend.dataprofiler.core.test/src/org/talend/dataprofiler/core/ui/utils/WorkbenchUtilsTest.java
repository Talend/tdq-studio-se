// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.PartInitException;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisPackage;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
@PrepareForTest({ RepositoryNodeHelper.class })
public class WorkbenchUtilsTest {

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.WorkbenchUtils#impactExistingAnalyses(orgomg.cwm.foundation.softwaredeployment.DataProvider)}
     * .
     */
    @Ignore
    @Test
    public void testImpactExistingAnalyses_1() {
        // DatabaseConnection mock_data = mock(DatabaseConnection.class);
        // Resource mock_Resource = mock(Resource.class);
        // when(((ModelElement) mock_data).eResource()).thenReturn(mock_Resource);
        //
        // Analysis mock_ana = mock(Analysis.class);
        // EList<Dependency> clients = mock(EObjectWithInverseResolvingEList.class);
        // Iterator<Dependency> mockIterator = mock(Iterator.class);
        // when(mockIterator.hasNext()).thenReturn(true).thenReturn(false);
        // Dependency dependency = mock(Dependency.class);
        // when(mockIterator.next()).thenReturn(dependency);
        // when(dependency.eResource()).thenReturn(mock_Resource);
        //
        // when(clients.iterator()).thenReturn(mockIterator);
        // when(mock_ana.getClientDependency()).thenReturn(clients);
        //
        // List<Resource> result = this.method_impactExistingAnalyses(mock_data, mock_ana);
        //
        // assertNotNull(result);
        // assertEquals(1, result.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.WorkbenchUtils#impactExistingAnalyses(orgomg.cwm.foundation.softwaredeployment.DataProvider)}
     * .
     */
    // when getClientDependency return null
    @Ignore
    @Test
    public void testImpactExistingAnalyses_2() {
        // DatabaseConnection mock_data = mock(DatabaseConnection.class);
        // Resource mock_Resource = mock(Resource.class);
        // when(((ModelElement) mock_data).eResource()).thenReturn(mock_Resource);
        //
        // Analysis mock_ana = mock(Analysis.class);
        //
        // when(mock_ana.getClientDependency()).thenReturn(null);
        //
        // List<Resource> result = this.method_impactExistingAnalyses(mock_data, mock_ana);
        //
        // assertNotNull(result);
        // assertEquals(0, result.size());
    }

    private List<Resource> method_impactExistingAnalyses(DataProvider oldDataProvider, Analysis analysis) {
        // MOD yyin 20120410, bug 4753
        List<ModelElement> tempList = new ArrayList<ModelElement>();
        tempList.add(oldDataProvider);
        return DependenciesHandler.getInstance().removeDependenciesBetweenModels(analysis, tempList);
    }

    /**
     * Test method for {@link org.talend.commons.utils.WorkspaceUtils#normalize(String)}.
     */
    @Test
    public void testNormalize() {
        assertEquals(null, WorkspaceUtils.normalize(null));
        assertEquals("_", WorkspaceUtils.normalize("?")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("_test", WorkspaceUtils.normalize("*test")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("_test_", WorkspaceUtils.normalize("<test>")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("test_", WorkspaceUtils.normalize("test?")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("test__", WorkspaceUtils.normalize("test'\"")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("_test_", WorkspaceUtils.normalize("(test)")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("___________", WorkspaceUtils.normalize("#^&/:;\\~.! ")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // test for 2 remained columns
    @Test
    public void testUpdateDependAnalysisOfDelimitedFile() throws IOException, URISyntaxException, PartInitException,
            BusinessException {
        // create a file connection
        DelimitedFileConnection fileConnection = ConnectionPackage.eINSTANCE.getConnectionFactory()
                .createDelimitedFileConnection();
        URL fileUrl = this.getClass().getResource("file1"); //$NON-NLS-1$
        MetadataTable metadataTable = UnitTestBuildHelper.initFileConnection(fileUrl, fileConnection);
        UnitTestBuildHelper.initColumns(metadataTable);

        IFile file = WorkspaceUtils.fileToIFile(new File(FileLocator.toFileURL(fileUrl).toURI()));
        IPath itemPath = file.getFullPath();

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setLabel("file1");

        DelimitedFileConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(fileConnection);
        try {
            ProxyRepositoryFactory.getInstance().create(connectionItem, itemPath.removeFirstSegments(3).removeLastSegments(1));
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

        // create an analysis which use the columns in the file connection
        Analysis analysis = UnitTestBuildHelper.createRealAnalysis("Ana01", null, false);
        AnalysisContext context = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisContext();
        context.setConnection(fileConnection);
        analysis.setContext(context);
        context.getAnalysedElements().addAll(metadataTable.getColumns());
        DependenciesHandler.getInstance().setDependencyOn(analysis, fileConnection);
        ElementWriterFactory.getInstance().createAnalysisWrite().save(analysis);

        // change the file's schema
        List<MetadataColumn> tempNewColumns = new ArrayList<MetadataColumn>();
        MetadataColumn name = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        name.setName("name");
        name.setLabel("name");
        tempNewColumns.add(name);

        MetadataColumn country = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country.setName("country");
        country.setLabel("country");
        tempNewColumns.add(country);

        MetadataColumn country1 = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country1.setName("country1");
        country1.setLabel("country1");
        tempNewColumns.add(country1);

        MetadataColumn company = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        company.setName("new column");
        company.setLabel("new column");
        tempNewColumns.add(company);

        metadataTable.getFeature().clear();
        metadataTable.getFeature().addAll(tempNewColumns);

        // before compare, the analysis has 5 analyzed elements
        Assert.assertEquals(5, analysis.getContext().getAnalysedElements().size());

        // call the tested method
        WorkbenchUtils.reloadMetadataOfDelimitedFile(metadataTable);
        List<MetadataColumn> afterCompareColumns = metadataTable.getColumns();

        // check the columns
        Assert.assertEquals(4, afterCompareColumns.size());
        Assert.assertEquals("name", afterCompareColumns.get(0).getLabel());
        Assert.assertEquals("country", afterCompareColumns.get(1).getLabel());
        Assert.assertEquals("country1", afterCompareColumns.get(2).getLabel());
        Assert.assertEquals("new column", afterCompareColumns.get(3).getLabel());

        metadataTable.getColumns().clear();
        metadataTable.getColumns().addAll(afterCompareColumns);
        WorkbenchUtils.impactExistingAnalyses(fileConnection);
        // check the depended analysis
        EList<Dependency> clientDependencies = fileConnection.getSupplierDependency();
        for (Dependency dep : clientDependencies) {
            for (ModelElement mod : dep.getClient()) {
                if (!(mod instanceof Analysis)) {
                    continue;
                }
                Analysis ana = (Analysis) mod;
                // assert the column with same name still in the analysis
                Assert.assertNotNull(ana.getContext().getAnalysedElements());
                // should be: only 2 with same name remained
                Assert.assertEquals(2, ana.getContext().getAnalysedElements().size());
                Assert.assertEquals("name", ana.getContext().getAnalysedElements().get(0).getName());
                Assert.assertEquals("country", ana.getContext().getAnalysedElements().get(1).getName());
            }
        }
    }

    // test for last column remained
    @Test
    public void testUpdateDependAnalysisOfDelimitedFile_2() throws IOException, URISyntaxException, PartInitException,
            BusinessException {
        // create a file connection
        DelimitedFileConnection fileConnection = ConnectionPackage.eINSTANCE.getConnectionFactory()
                .createDelimitedFileConnection();
        URL fileUrl = this.getClass().getResource("file1"); //$NON-NLS-1$
        MetadataTable metadataTable = UnitTestBuildHelper.initFileConnection(fileUrl, fileConnection);
        UnitTestBuildHelper.initColumns(metadataTable);

        IFile file = WorkspaceUtils.fileToIFile(new File(FileLocator.toFileURL(fileUrl).toURI()));
        IPath itemPath = file.getFullPath();

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setLabel("file2");

        DelimitedFileConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(fileConnection);
        try {
            ProxyRepositoryFactory.getInstance().create(connectionItem, itemPath.removeFirstSegments(3).removeLastSegments(1));
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

        // create an analysis which use the columns in the file connection
        Analysis analysis = UnitTestBuildHelper.createRealAnalysis("Ana01", null, false);
        AnalysisContext context = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisContext();
        context.setConnection(fileConnection);
        analysis.setContext(context);
        context.getAnalysedElements().addAll(metadataTable.getColumns());
        DependenciesHandler.getInstance().setDependencyOn(analysis, fileConnection);
        ElementWriterFactory.getInstance().createAnalysisWrite().save(analysis);

        // change the file's schema
        List<MetadataColumn> tempNewColumns = new ArrayList<MetadataColumn>();
        MetadataColumn name = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        name.setName("name2");
        name.setLabel("name2");
        tempNewColumns.add(name);

        MetadataColumn country = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country.setName("country2");
        country.setLabel("country2");
        tempNewColumns.add(country);

        MetadataColumn country1 = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country1.setName("country1");
        country1.setLabel("country1");
        tempNewColumns.add(country1);

        MetadataColumn company = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        company.setName("name");
        company.setLabel("name");
        tempNewColumns.add(company);

        metadataTable.getFeature().clear();
        metadataTable.getFeature().addAll(tempNewColumns);

        // before compare, the analysis has 5 analyzed elements
        Assert.assertEquals(5, analysis.getContext().getAnalysedElements().size());

        // call the tested method
        WorkbenchUtils.reloadMetadataOfDelimitedFile(metadataTable);
        List<MetadataColumn> afterCompareColumns = metadataTable.getColumns();

        // check the columns
        Assert.assertEquals(4, afterCompareColumns.size());
        Assert.assertEquals("name", afterCompareColumns.get(0).getLabel());
        Assert.assertEquals("country2", afterCompareColumns.get(2).getLabel());
        Assert.assertEquals("country1", afterCompareColumns.get(3).getLabel());
        Assert.assertEquals("name2", afterCompareColumns.get(1).getLabel());

        metadataTable.getColumns().clear();
        metadataTable.getColumns().addAll(afterCompareColumns);
        WorkbenchUtils.impactExistingAnalyses(fileConnection);
        // check the depended analysis
        EList<Dependency> clientDependencies = fileConnection.getSupplierDependency();
        for (Dependency dep : clientDependencies) {
            for (ModelElement mod : dep.getClient()) {
                if (!(mod instanceof Analysis)) {
                    continue;
                }
                Analysis ana = (Analysis) mod;
                // assert the column with same name still in the analysis
                Assert.assertNotNull(ana.getContext().getAnalysedElements());
                // should be: only 2 with same name remained
                Assert.assertEquals(1, ana.getContext().getAnalysedElements().size());
                Assert.assertEquals("name", ana.getContext().getAnalysedElements().get(0).getName());
            }
        }
    }

    // test for no column remained
    @Test
    public void testUpdateDependAnalysisOfDelimitedFile_3() throws IOException, URISyntaxException, PartInitException,
            BusinessException {
        // create a file connection
        DelimitedFileConnection fileConnection = ConnectionPackage.eINSTANCE.getConnectionFactory()
                .createDelimitedFileConnection();
        URL fileUrl = this.getClass().getResource("file1"); //$NON-NLS-1$
        MetadataTable metadataTable = UnitTestBuildHelper.initFileConnection(fileUrl, fileConnection);
        UnitTestBuildHelper.initColumns(metadataTable);

        IFile file = WorkspaceUtils.fileToIFile(new File(FileLocator.toFileURL(fileUrl).toURI()));
        IPath itemPath = file.getFullPath();

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setLabel("file3");

        DelimitedFileConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(fileConnection);
        try {
            ProxyRepositoryFactory.getInstance().create(connectionItem, itemPath.removeFirstSegments(3).removeLastSegments(1));
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

        // create an analysis which use the columns in the file connection
        Analysis analysis = UnitTestBuildHelper.createRealAnalysis("Ana01", null, false);
        AnalysisContext context = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisContext();
        context.setConnection(fileConnection);
        analysis.setContext(context);
        context.getAnalysedElements().addAll(metadataTable.getColumns());
        DependenciesHandler.getInstance().setDependencyOn(analysis, fileConnection);
        ElementWriterFactory.getInstance().createAnalysisWrite().save(analysis);

        // change the file's schema
        List<MetadataColumn> tempNewColumns = new ArrayList<MetadataColumn>();
        MetadataColumn name = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        name.setName("name2");
        name.setLabel("name2");
        tempNewColumns.add(name);

        MetadataColumn country = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country.setName("country2");
        country.setLabel("country2");
        tempNewColumns.add(country);

        MetadataColumn country1 = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country1.setName("country1");
        country1.setLabel("country1");
        tempNewColumns.add(country1);

        metadataTable.getFeature().clear();
        metadataTable.getFeature().addAll(tempNewColumns);

        // before compare, the analysis has 5 analyzed elements
        Assert.assertEquals(5, analysis.getContext().getAnalysedElements().size());

        // call the tested method
        WorkbenchUtils.reloadMetadataOfDelimitedFile(metadataTable);
        List<MetadataColumn> afterCompareColumns = metadataTable.getColumns();

        // check the columns
        Assert.assertEquals(3, afterCompareColumns.size());
        Assert.assertEquals("name2", afterCompareColumns.get(0).getLabel());
        Assert.assertEquals("country2", afterCompareColumns.get(1).getLabel());
        Assert.assertEquals("country1", afterCompareColumns.get(2).getLabel());

        metadataTable.getColumns().clear();
        metadataTable.getColumns().addAll(afterCompareColumns);
        WorkbenchUtils.impactExistingAnalyses(fileConnection);
        // check the depended analysis
        EList<Dependency> clientDependencies = fileConnection.getSupplierDependency();
        for (Dependency dep : clientDependencies) {
            for (ModelElement mod : dep.getClient()) {
                if (!(mod instanceof Analysis)) {
                    continue;
                }
                Analysis ana = (Analysis) mod;
                // assert the column with same name still in the analysis
                Assert.assertNotNull(ana.getContext().getAnalysedElements());
                // should be: only 2 with same name remained
                Assert.assertEquals(0, ana.getContext().getAnalysedElements().size());
            }
        }
    }
}
