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
package org.talend.dataprofiler.core.ui.action;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.FileUtils;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z bzhou $
 * 
 */
public abstract class AbstractImportFileAction extends Action implements ICheatSheetAction {

    protected RepositoryNode node;

    public AbstractImportFileAction(RepositoryNode node) {
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
        this.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("import items") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                if (node != null) {

                    try {

                        final Map<File, IPath> resultMap = computeFilePath();

                        if (resultMap != null && resultMap.size() != 0) {
                            for (final File file : resultMap.keySet()) {
                                // MOD msjian TDQ-4608 2012-3-6: when the file is *.jasper, copy it.
                                IPath path = resultMap.get(file);
                                if (file.getName().endsWith(PluginConstant.JASPER_STRING)) {
                                    // TDQ-7451 Replace File copy with eclipse IFile create.make svn could syn and
                                    // control.
                                    IFile targetFile = ResourceManager.getJRXMLFolder().getFile(path.append(file.getName()));
                                    WorkspaceResourceHelper.createIFileFromFile(
                                            file,
                                            targetFile,
                                            DefaultMessagesImpl.getString(
                                                    "AbstractImportFileAction.importJasperFile", file.getName())); //$NON-NLS-1$
                                } else {
                                    createItem(file, path);

                                }
                                // TDQ-4608~
                            }
                            saveAndRefresh();
                        }

                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);

    }

    /**
     * DOC bZhou Comment method "createItem".
     * 
     * @param initFile
     * @param path
     * @return
     * @throws Exception
     */
    protected Item createItem(File initFile, IPath path) throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(PluginConstant.EMPTY_STRING);
        property.setLabel(FileUtils.getName(initFile));

        Item item = initItem(initFile);
        if (item != null) {
            item.setProperty(property);
            property.setItem(item);

            IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
            property.setId(repositoryFactory.getNextId());
            if (path != null) {
                repositoryFactory.createParentFoldersRecursively(ERepositoryObjectType.getItemType(item), path);
            }
            repositoryFactory.create(item, path, true);
        }
        return item;
    }

    /**
     * DOC bZhou Comment method "saveAndRefresh".
     */
    private void saveAndRefresh() {
        CorePlugin.getDefault().refreshDQView(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        run();
    }

    public abstract Map<File, IPath> computeFilePath() throws Exception;

    public abstract Item initItem(File srcFile) throws Exception;

    public abstract ERepositoryObjectType getRepositoryType();
}
