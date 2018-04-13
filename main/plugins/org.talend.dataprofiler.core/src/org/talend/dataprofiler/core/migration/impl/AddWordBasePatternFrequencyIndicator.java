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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.ResourceManager;

/**
 * Add these 4 indicators when import an old project from window logon
 * CS Word Pattern Frequency
 * CS Word Pattern Low Frequency
 * CI Word Pattern Frequency
 * CI Word Pattern Low Frequency
 */
public class AddWordBasePatternFrequencyIndicator extends AbstractWorksapceUpdateTask {

    private final String DESFOLDERNAME = "Pattern Finder"; //$NON-NLS-1$

    private final String SRCFOLDERPATH = "/indicators/Pattern Frequency Statistics/"; //$NON-NLS-1$

    private final String INDICATORNAME = "CS Word Pattern Frequency";//$NON-NLS-1$


    public Date getOrder() {
        return createDate(2018, 4, 13);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        DQStructureManager manager = DQStructureManager.getInstance();
        // if the project version is lower than 7.1 and import from logon window, the target folder is "Pattern Finder".
        // or else, the target folder is renamed to "Pattern Frequency Statistics" .
        IFolder desFolder = ResourceManager.getSystemIndicatorFolder().getFolder(DESFOLDERNAME);
        if (!desFolder.exists()) {
            desFolder = ResourceManager.getSystemIndicatorFolder().getFolder("Pattern Frequency Statistics"); //$NON-NLS-1$
        }
        //copy it only when the 'CS Word Pattern Frequency' definition file doesn't exist
        if (!desFolder.getFile(INDICATORNAME).exists()) {
            manager.copyFilesToFolder(CorePlugin.getDefault(), new Path(SRCFOLDERPATH).toString(), false, desFolder,
                    null);
        }
        return true;
    }

}
