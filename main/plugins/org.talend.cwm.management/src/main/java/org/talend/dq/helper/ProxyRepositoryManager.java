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
package org.talend.dq.helper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.IReferencedProjectService;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class ProxyRepositoryManager {

    private static ProxyRepositoryManager instance = new ProxyRepositoryManager();

    private static Logger log = Logger.getLogger(ProxyRepositoryManager.class);

    public static ProxyRepositoryManager getInstance() {
        return instance;
    }

    /**
     * 
     * DOC qiongli:just update/commit .
     */
    public void save() {

        if (!isReadOnly()) {
            RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("save TDQ Project") {//$NON-NLS-1$

                @Override
                protected void run() {

                }
            };
            workUnit.setAvoidUnloadResources(true);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
        }

    }

    /**
     * 
     * DOC qiongli Comment method "refresh".
     */
    public void refresh() {
        try {
            ProxyRepositoryFactory.getInstance().initialize();
        } catch (PersistenceException e) {
            log.error(e, e);
        }
    }

    /**
     * 
     * DOC qiongli Comment method "lock".
     * 
     * @param item
     */
    public void lock(final Item item) {
        if (!isReadOnly() && item != null) {
            if (!item.eIsProxy()) {
                try {
                    ProxyRepositoryFactory.getInstance().lock(item);
                } catch (PersistenceException e) {
                    log.error(e, e);
                } catch (LoginException e) {
                    log.error(e, e);
                }
            }

        }
    }

    /**
     * 
     * DOC qiongli Comment method "unLock".
     * 
     * @param item
     */
    public void unLock(final Item item) {
        if (!isReadOnly() & item != null) {
            try {
                ProxyRepositoryFactory.getInstance().unlock(item);
            } catch (PersistenceException e) {
                log.error(e, e);
            } catch (LoginException e) {
                log.error(e, e);
            }
        }

    }

    public boolean isLocalProject() {
        RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();
        if (project == null) {
            return true;
        }
        if (project.isLocal()) {
            return true;
        }
        return false;
    }

    public Boolean isEditable(Item item) {
        ERepositoryStatus status = ProxyRepositoryFactory.getInstance().getStatus(item);
        switch (status) {
        case LOCK_BY_OTHER:
        case DELETED:
        case NOT_UP_TO_DATE:
        case READ_ONLY:
            return false;
        default:
            return true;
        }
    }

    public Boolean isLocked(Item item) {
        ERepositoryStatus status = ProxyRepositoryFactory.getInstance().getStatus(item);
        switch (status) {
        case LOCK_BY_OTHER:
        case LOCK_BY_USER:
            return Boolean.TRUE;
        default:
            return Boolean.FALSE;
        }
    }

    public Boolean isReadOnly() {
        return ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject();
    }

    public Boolean isLockByOthers(Item item) {
        ERepositoryStatus status = ProxyRepositoryFactory.getInstance().getStatus(item);
        switch (status) {
        case LOCK_BY_OTHER:
            return true;
        default:
            return false;
        }
    }

    public Boolean isLockByUserOwn(Item item) {
        ERepositoryStatus status = ProxyRepositoryFactory.getInstance().getStatus(item);
        switch (status) {
        case LOCK_BY_USER:
            return true;
        default:
            return false;
        }
    }

    public boolean isMergeRefProject() {
        if (org.talend.core.PluginChecker.isRefProjectLoaded()) {
            IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault().getService(
                    IReferencedProjectService.class);
            if (service != null) {
                return service.isMergeRefProject();
            }
        }
        return false;
    }

    public Set<org.talend.core.model.general.Project> getAllProjects() {
        // make sure there is no duplicate projects, and the main project is the first one.
        Set<Project> result = new LinkedHashSet<Project>();
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        List<Project> referencedProjects = ProjectManager.getInstance().getAllReferencedProjects();
        for (Project pro : referencedProjects) {
            pro.setMainProject(false);
        }
        result.add(currentProject);
        result.addAll(referencedProjects);
        return result;
    }

    public Project getProjectFromProjectTechnicalLabel(String technicalLabel) {
        for (Project project : getAllProjects()) {
            if (project.getTechnicalLabel().equals(technicalLabel)) {
                return project;
            }
        }
        return null;
    }
}
