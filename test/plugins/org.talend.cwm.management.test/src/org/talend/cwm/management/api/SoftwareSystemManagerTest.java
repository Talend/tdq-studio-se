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
package org.talend.cwm.management.api;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.SoftwareSystem;

/**
 * created by zhao on Aug 4, 2013 Detailled comment
 * 
 */
@PrepareForTest({ DriverManager.class, EMFSharedResources.class, ConvertionHelper.class, MetadataFillFactory.class })
public class SoftwareSystemManagerTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    /**
     * Test method for
     * {@link org.talend.cwm.management.api.SoftwareSystemManager#updateSoftwareSystem(java.sql.Connection, orgomg.cwm.foundation.softwaredeployment.SoftwareSystem)}
     * .
     */
    @Test
    public void testUpdateSoftwareSystem() {
        SoftwareSystemManager softwareSystemManger = SoftwareSystemManager.getInstance();
        PowerMockito.mockStatic(EMFSharedResources.class);
        EMFSharedResources emfSharedResourceMock = mock(EMFSharedResources.class);
        when(EMFSharedResources.getInstance()).thenReturn(emfSharedResourceMock).thenReturn(emfSharedResourceMock);
        Resource softwareSysResource = mock(Resource.class);
        when(emfSharedResourceMock.getSoftwareDeploymentResource()).thenReturn(softwareSysResource);
        EList<EObject> existedSystems = new BasicEList<EObject>();
        when(softwareSysResource.getContents()).thenReturn(existedSystems);
        when(emfSharedResourceMock.saveResource(softwareSysResource)).thenReturn(Boolean.TRUE);

        try {
            softwareSystemManger.updateSoftwareSystem(null);

            Connection mockConnection = mock(Connection.class);
            DatabaseMetaData mockDBMetaData = mock(DatabaseMetaData.class);
            // Mocking Static Class
            PowerMockito.mockStatic(DriverManager.class);
            String sql = "jdbc:mysql://localhost:3306/tbi"; //$NON-NLS-1$
            when(DriverManager.getConnection(sql)).thenReturn(mockConnection);
            when(mockConnection.getMetaData()).thenReturn(mockDBMetaData);

            // Mock database product name.
            String mysqlDB = "MYSQL";//$NON-NLS-1$
            when(mockDBMetaData.getDatabaseProductName()).thenReturn(mysqlDB);
            // Mock database product version
            String version = "5.5";//$NON-NLS-1$
            when(mockDBMetaData.getDatabaseProductVersion()).thenReturn(version);
            when(mockDBMetaData.getDatabaseMinorVersion()).thenReturn(5);
            when(mockDBMetaData.getDatabaseMajorVersion()).thenReturn(5);

            DatabaseConnection dbConn = ConnectionPackage.eINSTANCE.getConnectionFactory().createDatabaseConnection();
            TaggedValueHelper.setTaggedValue(dbConn, TaggedValueHelper.DB_PRODUCT_NAME, mysqlDB);
            TaggedValueHelper.setTaggedValue(dbConn, TaggedValueHelper.DB_PRODUCT_VERSION, version);
            PowerMockito.mockStatic(ConvertionHelper.class);
            when(ConvertionHelper.convert(dbConn, false, "")).thenReturn(null); //$NON-NLS-1$
            PowerMockito.mockStatic(MetadataFillFactory.class);
            MetadataFillFactory metadataFillFactory = mock(MetadataFillFactory.class);
            when(MetadataFillFactory.getDBInstance()).thenReturn(metadataFillFactory);
            TypedReturnCode<java.sql.Connection> returnCode = new TypedReturnCode<Connection>(Boolean.TRUE);
            returnCode.setObject(mockConnection);
            when(metadataFillFactory.createConnection(null)).thenReturn(returnCode);

            softwareSystemManger.updateSoftwareSystem(dbConn);
            Assert.assertTrue(existedSystems.size() > 0);
            SoftwareSystem newSoftwareSys = (SoftwareSystem) existedSystems.get(0);
            Assert.assertEquals(mysqlDB, newSoftwareSys.getSubtype());
            Assert.assertEquals(version, newSoftwareSys.getVersion());

        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void testGetNewDBTypes() {
        SoftwareSystemManager softwareSystemManger = SoftwareSystemManager.getInstance();
        Set<String> existingTypes = new HashSet<String>();
        existingTypes.add("MYSQL"); //$NON-NLS-1$
        existingTypes.add("oracle"); //$NON-NLS-1$
        PowerMockito.mockStatic(EMFSharedResources.class);
        EMFSharedResources emfSharedResourceMock = mock(EMFSharedResources.class);
        when(EMFSharedResources.getInstance()).thenReturn(emfSharedResourceMock).thenReturn(emfSharedResourceMock);
        Resource softwareSysResource = mock(Resource.class);
        when(emfSharedResourceMock.getSoftwareDeploymentResource()).thenReturn(softwareSysResource);
        EList<EObject> existedSystems = new BasicEList<EObject>();
        TdSoftwareSystem softwareSystem1 = SoftwaredeploymentPackage.eINSTANCE.getSoftwaredeploymentFactory()
                .createTdSoftwareSystem();
        softwareSystem1.setSubtype("mysql"); //$NON-NLS-1$
        TdSoftwareSystem softwareSystem2 = SoftwaredeploymentPackage.eINSTANCE.getSoftwaredeploymentFactory()
                .createTdSoftwareSystem();
        softwareSystem2.setSubtype("VERTICA"); //$NON-NLS-1$
        TdSoftwareSystem softwareSystem3 = SoftwaredeploymentPackage.eINSTANCE.getSoftwaredeploymentFactory()
                .createTdSoftwareSystem();
        softwareSystem3.setSubtype("Oracle"); //$NON-NLS-1$
        existedSystems.add(softwareSystem1);
        existedSystems.add(softwareSystem2);
        existedSystems.add(softwareSystem3);
        when(softwareSysResource.getContents()).thenReturn(existedSystems);

        List<String> newTypes = softwareSystemManger.getNewDBTypesFromSoftwareSystem(existingTypes);
        Assert.assertEquals(1, newTypes.size());
        Assert.assertEquals("VERTICA", newTypes.get(0)); //$NON-NLS-1$
    }
}
