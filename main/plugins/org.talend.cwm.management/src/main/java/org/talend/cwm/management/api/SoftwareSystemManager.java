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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.utils.exceptions.TalendException;
import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.foundation.softwaredeployment.DeployedComponent;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * This class manages the software systems.
 */
public final class SoftwareSystemManager {

    private static Logger log = Logger.getLogger(SoftwareSystemManager.class);

    private static SoftwareSystemManager instance;

    private SoftwareSystemManager() {
    }

    public static SoftwareSystemManager getInstance() {
        if (instance == null) {
            instance = new SoftwareSystemManager();
        }
        return instance;
    }

    /**
     * 
     * Find the TdSoftwareSystem instance from EMF model, if it does not exist, will NOT reload from database.
     * 
     * @param dataProvider
     * @return
     * @deprecated
     */
    @Deprecated
    public TdSoftwareSystem getSoftwareSystemFromModel(Connection dataProvider) {
        if (dataProvider == null) {
            return null;
        }
        Resource softwareSystemResource = EMFSharedResources.getInstance().getSoftwareDeploymentResource();
        if (WorkspaceUtils.getModelElementResource(softwareSystemResource.getURI()).exists()) {
            softwareSystemResource = EMFSharedResources.getInstance().reloadsoftwareDeploymentResource();
        }
        if (softwareSystemResource != null) {
            List<EObject> softwareSystems = softwareSystemResource.getContents();
            // Loop the software system from .softwaresystem.softwaredeployment file.
            for (EObject softwareSystem : softwareSystems) {

                if (softwareSystem instanceof TdSoftwareSystem) {
                    List<ModelElement> ownedELements = ((TdSoftwareSystem) softwareSystem).getOwnedElement();
                    // Loop owned element.
                    for (ModelElement me : ownedELements) {
                        if (me == null || !(me instanceof Component)) {
                            continue;
                        }
                        List<DeployedComponent> deployComponents = ((Component) me).getDeployment();
                        // deployComponents != null is true any time
                        if (deployComponents.size() > 0) {
                            if (ResourceHelper.areSame(deployComponents.get(0), dataProvider)) {
                                // Find the software system by data provider.
                                return (TdSoftwareSystem) softwareSystem;
                            }

                        }
                    }

                }

            }

        }
        return null;
    }

    /**
     * DOC scorreia Comment method "saveSoftwareSystem".
     * 
     * @param util
     * @param softwareSystem
     */
    public static boolean saveSoftwareSystem(TdSoftwareSystem softwareSystem) {
        EMFSharedResources util = EMFSharedResources.getInstance();
        return util.saveSoftwareDeploymentResource(softwareSystem);
    }

    /**
     * 
     * remove the softWareSystem which have relation about dataprovider and any softWareSystem which don't contain any
     * one.
     * 
     * @param dataProvider
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean cleanSoftWareSystem(Connection dataProvider) {
        if (dataProvider == null) {
            return false;
        }
        Resource softwareSystemResource = EMFSharedResources.getInstance().getSoftwareDeploymentResource();
        if (WorkspaceUtils.getModelElementResource(softwareSystemResource.getURI()).exists()) {
            softwareSystemResource = EMFSharedResources.getInstance().reloadsoftwareDeploymentResource();
        }
        if (softwareSystemResource != null) {
            List<EObject> softwareSystems = softwareSystemResource.getContents();
            // Loop the software system from .softwaresystem.softwaredeployment file.
            List<EObject> needToRemoves = new ArrayList<EObject>();
            for (EObject softwareSystem : softwareSystems) {
                if (softwareSystem instanceof TdSoftwareSystem) {
                    List<ModelElement> ownedELements = ((TdSoftwareSystem) softwareSystem).getOwnedElement();
                    // Loop owned element.
                    for (ModelElement me : ownedELements) {
                        if (me == null || !(me instanceof Component)) {
                            continue;
                        }
                        List<DeployedComponent> deployComponents = ((Component) me).getDeployment();
                        if (deployComponents.size() > 0) {
                            if (ResourceHelper.areSame(deployComponents.get(0), dataProvider)) {
                                needToRemoves.add(softwareSystem);
                                break;
                            }
                        } else {
                            needToRemoves.add(softwareSystem);
                        }
                    }
                    if (ownedELements.size() <= 0) {
                        needToRemoves.add(softwareSystem);
                    }
                }
            }
            if (needToRemoves.size() > 0) {
                softwareSystems.removeAll(needToRemoves);
                EMFSharedResources.getInstance().saveSoftwareDeploymentResource();
                return true;
            }
        }
        return false;

    }

    public boolean cleanSoftWareSystem() {
        DatabaseConnection createDatabaseConnection = org.talend.core.model.metadata.builder.connection.ConnectionFactory.eINSTANCE
                .createDatabaseConnection();
        return cleanSoftWareSystem(createDatabaseConnection);
    }

    /**
     * Update the software systems given the database connection instance.<br>
     * 
     * @param database connection (Talend type)
     * @return
     * @throws SQLException
     */
    public void updateSoftwareSystem(DatabaseConnection databaseConnection) throws SQLException {
        if (databaseConnection == null) {
            return;
        }
        Resource softwareSystemResource = EMFSharedResources.getInstance().getSoftwareDeploymentResource();
        List<EObject> softwareSystems = softwareSystemResource.getContents();

        Boolean isExisted = Boolean.FALSE;
        try {
            isExisted = isExistedInSoftwareSystem(databaseConnection, softwareSystems);
        } catch (TalendException e) {
            log.debug(e.getMessage());
            return;
        }
        if (!isExisted) {
            update(databaseConnection, softwareSystems, softwareSystemResource);
        }

    }

    /**
     * DOC zhao Comment method "isExistedInSoftwareSystem".
     * 
     * @param databaseConnection
     * @return
     */
    private Boolean isExistedInSoftwareSystem(DatabaseConnection databaseConnection, List<EObject> softwareSystems)
            throws TalendException {
        String databaseType = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_NAME, databaseConnection);
        if (StringUtils.isEmpty(databaseType)) {
            throw new TalendException(Messages.getString("SoftwareSystemManager_NULL_DB_TYPE") + databaseConnection.getName()); //$NON-NLS-1$
        }
        String productVersion = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_VERSION, databaseConnection);
        for (EObject system : softwareSystems) {
            if (system instanceof TdSoftwareSystem) {
                if (StringUtils.equalsIgnoreCase(((TdSoftwareSystem) system).getSubtype(), databaseType)
                        && StringUtils.endsWithIgnoreCase(((TdSoftwareSystem) system).getVersion(), productVersion)) {
                    // Found the software system given the database type and version.
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    /**
     * DOC zhao Comment method "update".
     * 
     * @param databaseProductName
     * @param databaseProductVersion
     */
    private void update(DatabaseConnection databaseConnection, List<EObject> softwareSystems, Resource softwareSystemResource) {

        String databaseType = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_NAME, databaseConnection);
        if (StringUtils.isEmpty(databaseType)) {
            return;
        }
        String productVersion = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_VERSION, databaseConnection);
        // update software system.

        TdSoftwareSystem softwareSystemNew = SoftwaredeploymentPackage.eINSTANCE.getSoftwaredeploymentFactory()
                .createTdSoftwareSystem();
        softwareSystemNew.setName(databaseType);
        softwareSystemNew.setSubtype(databaseType);
        if (productVersion != null) {
            softwareSystemNew.setVersion(productVersion);
        }
        softwareSystems.add(softwareSystemNew);
        EMFSharedResources.getInstance().saveResource(softwareSystemResource);
    }

    /**
     * 
     * Get new database types from software system.
     * 
     * @return the new datababse types (e.g. new type created via generic JDBC connection).
     */
    public List<String> getNewDBTypesFromSoftwareSystem(Set<String> existingTypes) {
        List<String> newDBTypes = new ArrayList<String>();
        Resource softwareSystemResource = EMFSharedResources.getInstance().getSoftwareDeploymentResource();
        List<EObject> softwareSystems = softwareSystemResource.getContents();
        for (EObject system : softwareSystems) {
            if (system instanceof TdSoftwareSystem) {
                String subtype = ((TdSoftwareSystem) system).getSubtype();
                if (subtype == null) {
                    continue;
                }
                Boolean isExist = Boolean.FALSE;
                for (String existType : existingTypes) {
                    if (StringUtils.equalsIgnoreCase(existType, subtype)) {
                        isExist = Boolean.TRUE;
                        break;
                    }
                }
                if (!isExist) {
                    // TDQ-11558 msjian: because we have "Redshift" and "Hive | Impala" types, so ignore these
                    if (subtype.equalsIgnoreCase(SupportDBUrlType.REDSHIFT.getLanguage())
                            || subtype.equalsIgnoreCase(SupportDBUrlType.IMPALA.getLanguage())
                            || subtype.contains(SupportDBUrlType.HIVEDEFAULTURL.getLanguage())) {
                        continue;
                    }
                    // TDQ-11558~
                    newDBTypes.add(subtype);
                }
            }
        }
        return newDBTypes;
    }
}
