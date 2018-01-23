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
package org.talend.dq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class CWMPlugin extends Plugin {

    private static CWMPlugin self;

    private BundleContext bundleContext;

    /**
     * Getter for context.
     * 
     * @return the context
     */
    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    public CWMPlugin() {
        super();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.bundleContext = context;
        self = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
     * DOC bZhou Comment method "getDefault".
     * 
     * @return
     */
    public static CWMPlugin getDefault() {
        return self;
    }

    /**
     * DOC bZhou Comment method "addConnetionAliasToSQLPlugin".
     * 
     * @param dataproviders
     */
    public void addConnetionAliasToSQLPlugin(ModelElement... dataproviders) {
        SqlExplorerUtils.getDefault().addConnetionAliasToSQLPlugin(dataproviders);
    }

    /**
     * 
     * DOC qiongli Comment method "updateConnetionAliasByName".
     * 
     * @param connection
     * @param aliasName
     */
    public void updateConnetionAliasByName(Connection connection, String aliasName) {
        SqlExplorerUtils.getDefault().updateConnetionAliasByName(connection, aliasName);
    }

    /**
     * 
     * update ManagedDriver driver jars.
     * 
     * @param connection
     */
    public void loadDriverByLibManageSystem(DatabaseConnection connection) {
        SqlExplorerUtils.getDefault().loadDriverByLibManageSystem(connection);
    }

    /**
     * DOC bZhou Comment method "removeAliasInSQLExplorer".
     * 
     * @param dataproviders
     */
    public void removeAliasInSQLExplorer(DataProvider... dataproviders) {
        SqlExplorerUtils.getDefault().removeAliasInSQLExplorer(dataproviders);
    }

    /**
     * 
     * when you start TOP ,the 'lib/java' dosen't exist,should create it.
     */
    public void createLibFolderIfNotExist() {
        String installLocation = getLibrariesPath();
        File libFile = new File(installLocation);
        if (!libFile.exists()) {
            org.talend.utils.io.FilesUtils.createFoldersIfNotExists(installLocation, false);
        }
    }

    public String getLibrariesPath() {
        String installLocation = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        return installLocation;
    }

    public boolean isCommandLine() {
        BundleContext configuratorBundleContext = getBundleContext();
        if (configuratorBundleContext != null) {
            String commands = configuratorBundleContext.getProperty("eclipse.commands"); //$NON-NLS-1$
            if (commands != null && commands.contains("-application") //$NON-NLS-1$
                    // commandLine
                    && commands.contains("org.talend.commandline.CommandLine")) { //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    public boolean isTCOMPJdbc(String dbType) {
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = getGenericDBService();
        if (dbService != null) {
            extraTypes.addAll(dbService.getExtraTypes());
        }
        for (ERepositoryObjectType type : extraTypes) {
            if (type.getType().equals(dbType)) {
                return true;
            }
        }
        return false;
    }

    public IGenericDBService getGenericDBService() {
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
        }
        return dbService;
    }

}
