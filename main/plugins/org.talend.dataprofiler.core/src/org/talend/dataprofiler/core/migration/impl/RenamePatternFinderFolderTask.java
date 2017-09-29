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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.utils.WorkspaceUtils;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * TDQ-11101: Rename "Pattern Finder" to "Pattern Frequency Statistics" Task
 */
public class RenamePatternFinderFolderTask extends AbstractWorksapceUpdateTask {

    protected static Logger log = Logger.getLogger(RenamePatternFinderFolderTask.class);

    public static final String PATTERN_FINDER = "Pattern Finder"; //$NON-NLS-1$

    private static final String PATTERN_FREQUENCY_STATISTICS = "Pattern Frequency Statistics"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        File newFolder = WorkspaceUtils.ifolderToFile(ResourceManager.getSysIndicatorPatternFrequencyStatisticsFolder());

        // only when "Pattern Frequency Statistics" do not exist, then do rename
        if (!ResourceManager.getSysIndicatorPatternFrequencyStatisticsFolder().exists()) {
            // rename folder name
            File oldFolder = ResourceManager.getSystemIndicatorFolder().getRawLocation().append(PATTERN_FINDER).toFile();
            result &= oldFolder.renameTo(newFolder);
        }

        try {
            // replace for the path in TalendProperties:ItemState of indicators
            String[] indicatorProFileExtentionName = { FactoriesUtil.PROPERTIES_EXTENSION };
            result &= FilesUtils.migrateFolder(newFolder, indicatorProFileExtentionName, initIndicatorReplaceMap(), log);

            // replace for the indicatorDefinition in analysis item file
            File analysisFolder = WorkspaceUtils.ifolderToFile(ResourceManager.getAnalysisFolder());
            String[] analysisFileExtentionName = { FactoriesUtil.ANA };
            result &= FilesUtils.migrateFolder(analysisFolder, analysisFileExtentionName, initIndicatorReplaceMap(), log);
            if (isWorksapcePath()) {
                for (IRepositoryViewObject viewObject : ProxyRepositoryFactory.getInstance().getAll(
                        ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT)) {
                    ProxyRepositoryFactory.getInstance().reload(viewObject.getProperty());
                }
            }
            ResourceService.refreshStructure();
        } catch (Exception e) {
            result = false;
            log.error(e, e);
        }

        // should reload definitions and all categories
        IFile file = ResourceManager.getLibrariesFolder().getFile(DefinitionHandler.FILENAME);
        file.delete(true, new NullProgressMonitor());
        DefinitionHandler.getInstance().copyDefinitionsIntoFolder(ResourceManager.getLibrariesFolder());
        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
        EMFSharedResources.getInstance().reloadResource(uri);
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2015, 10, 26);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.migration.IWorkspaceMigrationTask# getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /**
     * DOC msjian Comment method "initIndicatorReplaceMap".
     * 
     * @return
     */
    public static Map<String, String> initIndicatorReplaceMap() {
        Map<String, String> replaceStringMap = new HashMap<String, String>();
        replaceStringMap.put(PATTERN_FINDER, PATTERN_FREQUENCY_STATISTICS);
        return replaceStringMap;
    }
}
