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
package org.talend.dataprofiler.core.migration.impl;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.TalendDefinitionFileUpdate;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.ResourceManager;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public abstract class CreateSystemIndicatorTask extends AbstractWorksapceUpdateTask {

    String separator = System.getProperty("line.separator"); //$NON-NLS-1$
    String endLine = "</dataquality.indicators.definition:IndicatorsDefinitions>"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(CreateSystemIndicatorTask.class);

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        TalendDefinitionFileUpdate talendDefinitionFileUpdate = new TalendDefinitionFileUpdate();
        if (-1 == talendDefinitionFileUpdate.indexOf("name=\"" + getFoldername() + "\"")) { //$NON-NLS-1$ //$NON-NLS-2$
            talendDefinitionFileUpdate.add(endLine, getCategoryString() + separator + endLine);
            talendDefinitionFileUpdate.replace(this.getClass().getName());
            IPath definitionPath = ResourceManager.getLibrariesFolder().getFullPath().append(".Talend.definition"); //$NON-NLS-1$
            URI uri = URI.createPlatformResourceURI(definitionPath.toString(), false);
            EMFSharedResources.getInstance().reloadResource(uri);
            IFolder folder = ResourceManager.getSystemIndicatorFolder().getFolder(getFoldername());
            if (!folder.exists()) {
                copyToFolder(folder);
            }
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }

        return true;
    }

    /**
     * DOC yyin Comment method "getFoldername".
     * 
     * @return
     */
    abstract protected String getFoldername();

    /**
     * DOC yyin Comment method "getCategoryString".
     * 
     * @return
     */
    abstract protected String getCategoryString();

    /**
     * create the related folder with the path.
     * 
     * @param folder
     */
    private void copyToFolder(IFolder folder) {
        try {
            DQStructureManager manager = DQStructureManager.getInstance();
            folder.create(false, true, null);
            manager.copyFilesToFolder(CorePlugin.getDefault(), new Path("/indicators/" + getFoldername()).toString(), false, //$NON-NLS-1$
                    folder, null);

        } catch (Exception e1) {
            log.error("do migration for " + getFoldername() + " failed:", e1); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

}
