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
package org.talend.dq.writer.impl;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.management.api.SoftwareSystemManager;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class DataProviderWriter extends AElementPersistance {

    static Logger log = Logger.getLogger(DataProviderWriter.class);

    /**
     * DOC bZhou DataProviderWriter constructor comment.
     */
    public DataProviderWriter() {
        super();
    }

    @Override
    public ReturnCode create(ModelElement element, IFile file) {
        ReturnCode rc = new ReturnCode();

        IPath itemPath = file.getFullPath();

        Property property = initProperty(element);
        Item item = property.getItem();

        try {
            ProxyRepositoryFactory.getInstance().create(item, itemPath.removeFirstSegments(3).removeLastSegments(1));
        } catch (PersistenceException e) {
            log.error(e, e);
        }

        return rc;
    }

    @Override
    public ReturnCode saveProperty(Property property) {
        ReturnCode rc = new ReturnCode();
        try {
            ProxyRepositoryFactory.getInstance().save(property);
        } catch (PersistenceException e) {
            log.error(e, e);
            rc.setOk(Boolean.FALSE);
            rc.setMessage(e.getMessage());
        }

        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    @Deprecated
    protected void addDependencies(ModelElement element) {
        TdSoftwareSystem softwareSystem = ConnectionHelper.getSoftwareSystem((Connection) element);
        if (softwareSystem != null) {
            SoftwareSystemManager.saveSoftwareSystem(softwareSystem);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addResourceContent(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected void addResourceContent(ModelElement element) {
        super.addResourceContent(element);

        if (element.eResource() != null) {
            EList<EObject> resourceContents = element.eResource().getContents();

            // add each catalog to its own file
            Collection<? extends ModelElement> catalogs = ConnectionHelper.getCatalogs((Connection) element);
            resourceContents.addAll(catalogs);

            if (log.isDebugEnabled()) {
                log.debug("Catalogs added "); //$NON-NLS-1$
            }

            // add each schema to its own file
            Collection<? extends ModelElement> schemas = ConnectionHelper.getSchema((Connection) element);
            resourceContents.addAll(schemas);

            // add each schema to its own file
            Collection<? extends ModelElement> xmlSchemas = ConnectionHelper.getTdXmlDocument((Connection) element);
            resourceContents.addAll(xmlSchemas);

            if (log.isDebugEnabled()) {
                log.debug("Schema added "); //$NON-NLS-1$
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#getFileExtension()
     */
    @Override
    protected String getFileExtension() {
        return FactoriesUtil.ITEM_EXTENSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#check(org.eclipse.core.resources.IFile)
     */
    @Override
    public boolean check(IFile file) {
        String fileExtension = file.getFileExtension();
        return fileExtension.equals(getFileExtension()) || fileExtension.equals("comp"); //$NON-NLS-1$
    }

    /**
     * Save connection item and update the dependencies(optional). <B>To update dependencies of the connection the
     * #careDependency must be set as TRUE.</B>
     * 
     * @see org.talend.dq.writer.AElementPersistance#save(org.talend.core.model.properties.Item, boolean[])
     */
    @Override
    public ReturnCode save(Item item, boolean careDependency) {
        final ConnectionItem connItem = (ConnectionItem) item;
        final boolean isCare = careDependency;
        // MOD qiongli TDQ-6287 avoid all notification of changes before the end of the modifications.
        final ReturnCode returnCode = new ReturnCode();

        RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Save connection item: " //$NON-NLS-1$
                + item.getProperty().getDisplayName()) {

            @Override
            protected void run() {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) throws CoreException {
                        try {
                            if (isCare) {
                                saveWithDependencies(connItem, connItem.getConnection());
                            } else {
                                saveWithoutDependencies(connItem, connItem.getConnection());
                            }
                        } catch (PersistenceException e) {
                            log.error(e, e);
                            returnCode.setOk(Boolean.FALSE);
                            returnCode.setMessage(e.getMessage());
                        }
                    }
                };
                ISchedulingRule schedulingRule = workspace.getRoot();
                try {
                    workspace.run(operation, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                } catch (CoreException e) {
                    log.error(e, e);
                    returnCode.setOk(false);
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);

        // MOD yyi 2012-02-07 TDQ-4621:Update dependencies(connection) when careDependency is true.
        return returnCode;
    }

    @Override
    protected void notifyResourceChanges() {
        ProxyRepositoryManager.getInstance().save();
    }

    @Override
    public ReturnCode save(ModelElement element) {
        // MOD gdbu 2011-12-16 TDQ-4203 Previous logic will cause an infinite loop.
        Property anaEleProperty = PropertyHelper.getProperty(element);
        if (null != anaEleProperty) {
            // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
            return save(anaEleProperty.getItem(), true);
        }
        return super.save(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#removeDependencies(org.talend.core.model.properties.Item)
     */
    @Override
    protected ReturnCode removeDependencies(Item item) {
        return new ReturnCode(true);
    }
}
