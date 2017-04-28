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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.migration.helper.VersionComparator;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.model.emf.CwmResource;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.ProductVersion;

/**
 * 
 * DOC mzhao Migration task for splitting system indicators. The .talend.definition file would be replaced by several
 * separate files, each indicator definition is defined in its own file. feature: 13676, 2010-07-07
 */
public class SplitSysIndicatorTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(SplitSysIndicatorTask.class);

    public SplitSysIndicatorTask() {
    }

    @Override
    protected boolean doExecute() throws Exception {

        boolean ok = true;
        // Copy system indicators.
        DQStructureManager manager = DQStructureManager.getInstance();
        IFolder systemIndicatorFolder = ResourceManager.getSystemIndicatorFolder();
        if (!systemIndicatorFolder.exists()) {
            systemIndicatorFolder = manager.createNewFolder(ResourceManager.getIndicatorFolder(),
                    EResourceConstant.SYSTEM_INDICATORS);
        }

        manager.copyFilesToFolder(CorePlugin.getDefault(), DQStructureManager.SYSTEM_INDICATOR_PATH, true, systemIndicatorFolder,
                null, true);

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        // Migration for analyses (indicator definition)
        Collection<Analysis> analyses = (Collection<Analysis>) AnaResourceFileHelper.getInstance().getAllElement();

        for (Analysis ana : analyses) {
            try {
                for (Indicator ind : ana.getResults().getIndicators()) {
                    updateIndDefinition(ind);
                    if (ind instanceof CompositeIndicator) {
                        for (Indicator indLeave : IndicatorHelper.getIndicatorLeavesBySingleNode(ind)) {
                            updateIndDefinition(indLeave);
                        }
                    }
                }
                EMFSharedResources.getInstance().saveResource(ana.eResource());
            } catch (Exception e) {
                log.warn(e, e);
                ok = false;
            }
        }
        // MOD qiongli 2011-11-16 TDQ-3694,should relaod definitions and all categories after changing the definition
        // file.
        // Copy system indicator categories.
        IFile file = ResourceManager.getLibrariesFolder().getFile(DefinitionHandler.FILENAME);
        file.delete(true, new NullProgressMonitor());
        DefinitionHandler.getInstance().copyDefinitionsIntoFolder(ResourceManager.getLibrariesFolder());
        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
        EMFSharedResources.getInstance().reloadResource(uri);
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        return ok;
    }

    private void updateIndDefinition(Indicator ind) {
        if (ind == null) {
            return;
        }
        IndicatorDefinition indicatorDefinition = ind.getIndicatorDefinition();
        if (indicatorDefinition == null) {
            return;
        }
        // ADD xqliu 2010-07-28 13676 don't update UserDefIndicator
        if (ind instanceof UserDefIndicator) {
            return;
        }
        // ~ 13676
        CwmResource indDefResource = (CwmResource) indicatorDefinition.eResource();

        if (indDefResource == null) {
            // MOD scorreia 2010-10-05 16030 set the link between indicators and their definition
            if (DefinitionHandler.getInstance().setDefaultIndicatorDefinition(ind)) {
                if (log.isDebugEnabled()) {
                    log.debug("MIGRATING: indicator definition " + indicatorDefinition.getName() + " in indicator "//$NON-NLS-1$//$NON-NLS-2$
                            + ind.getName() + " in analysis " + ind.eResource());//$NON-NLS-1$
                }
            } else {
                log.error(DefaultMessagesImpl.getString(
                        "SplitSysIndicatorTask_LogErr", indicatorDefinition.getName(), ind.getName(), ind.eResource())); //$NON-NLS-1$

            }
            // ~ 16030
        } else {
            // Find updated indicator definition
            IndicatorDefinition updatedDefinition = DefinitionHandler.getInstance().getDefinitionById(
                    indDefResource.getID(ind.getIndicatorDefinition()));
            ind.setIndicatorDefinition(updatedDefinition);
        }

    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        return createDate(2010, 07, 07);
    }

    @Override
    public boolean valid() {
        IFile file = WorkspaceVersionHelper.getVersionFile();
        if (file.exists() && VersionComparator.isLower(WorkspaceVersionHelper.getVesion(), new ProductVersion(4, 1))) {
            return true;
        }
        return false;
    }

}
