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
package org.talend.cwm.compare.factory.comparisonlevel;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.i18n.DefaultMessagesImpl;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.writer.EMFSharedResources;

/**
 * FOr the table/view level, which use the same way to create the temp file
 */
public abstract class AbstractTableComparisonLevel extends AbstractComparisonLevel {

    /**
     * DOC yyin AbstractTableComparisonLevel constructor comment.
     * 
     * @param selObj
     */
    public AbstractTableComparisonLevel(Object selObj) {
        super(selObj);
    }

    @Override
    protected IFile createTempConnectionFile() throws ReloadCompareException {
        IFile tempConnectionFile = createTempFile(oldDataProvider);

        tempReloadProvider = createTempProvider(tempConnectionFile);

        return tempConnectionFile;
    }

    public IFile createTempFile(Connection oldDataProvider) throws ReloadCompareException {
        // MOD klliu bug 15822 201-09-30
        if (oldDataProvider != null && oldDataProvider.eIsProxy()) {
            oldDataProvider = (Connection) EObjectHelper.resolveObject(oldDataProvider);
        }
        // MOD klliu bug 16503 201-10-28 Attention,we will not use
        // PrvResourceFileHelper and instead of WorkspaceUtils
        // in the application,except migratory task
        IFile findCorrespondingFile = WorkspaceUtils.getModelElementResource(oldDataProvider);
        if (findCorrespondingFile == null) {

            throw new ReloadCompareException(DefaultMessagesImpl.getString(
                    "TableViewComparisonLevel.NotFindFileOfDataprovider", oldDataProvider.getName())); //$NON-NLS-1$
        }
        return DQStructureComparer.copyedToDestinationFile(findCorrespondingFile, DQStructureComparer.getTempRefreshFile());

    }

    public Connection createTempProvider(IFile tempConnectionFile) throws ReloadCompareException {
        URI uri = URI.createPlatformResourceURI(tempConnectionFile.getFullPath().toString(), false);
        Resource resource = EMFSharedResources.getInstance().getResource(uri, true);
        if (resource == null) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("TableViewComparisonLevel.NoFactoryFoundForURI", uri)); //$NON-NLS-1$
        }
        Collection<Connection> tdDataProviders = ConnectionHelper.getTdDataProviders(resource.getContents());

        if (tdDataProviders.isEmpty()) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("TableViewComparisonLevel.NoDataProviderFound", //$NON-NLS-1$
                    tempConnectionFile.getLocation().toFile().getAbsolutePath()));
        }
        if (tdDataProviders.size() > 1) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString(
                    "TableViewComparisonLevel.TooManyDataProviderInFile", tdDataProviders.size(), //$NON-NLS-1$
                    tempConnectionFile.getLocation().toFile().getAbsolutePath()));
        }
        return tdDataProviders.iterator().next();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.compare.factory.comparisonlevel.AbstractComparisonLevel#getLeftResource()
     */
    @Override
    protected Resource getLeftResource() throws ReloadCompareException {
        // no need for reload, only for compare.
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.compare.factory.comparisonlevel.AbstractComparisonLevel#getRightResource()
     */
    @Override
    protected Resource getRightResource() throws ReloadCompareException {
        // no need for reload, only for compare.
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.compare.factory.comparisonlevel.AbstractComparisonLevel#getSavedReloadObject()
     */
    @Override
    protected EObject getSavedReloadObject() throws ReloadCompareException {
        // TODO Auto-generated method stub
        return null;
    }

}
