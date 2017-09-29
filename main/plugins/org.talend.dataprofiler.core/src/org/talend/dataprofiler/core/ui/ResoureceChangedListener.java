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
package org.talend.dataprofiler.core.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.ResourceManager;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class ResoureceChangedListener extends WorkbenchContentProvider {

    private static Logger log = Logger.getLogger(ResoureceChangedListener.class);

    protected EMFSharedResources util = EMFSharedResources.getInstance();

    // private IWorkspace wsp = ResourceManager.getRootProject().getWorkspace();

    @Override
    public void processDelta(final IResourceDelta rootDelta) {
        final List<IFile> added = new ArrayList<IFile>();
        final List<IFile> removed = new ArrayList<IFile>();
        // wsp.removeResourceChangeListener(this);
        IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {

            public boolean visit(IResourceDelta delta) {
                IResource resource = delta.getResource();

                if (FactoriesUtil.isEmfFile(resource.getFileExtension())) {
                    if (delta.getKind() == IResourceDelta.ADDED) {

                        added.add((IFile) resource);

                    }

                    if (delta.getKind() == IResourceDelta.REMOVED) {
                        removed.add((IFile) resource);
                    }
                }

                return true;
            }

        };

        try {
            IResourceDelta docDelta = initFolderForVisitor(rootDelta);
            if (docDelta == null) {
                return;
            } else {
                docDelta.accept(visitor);
            }
        } catch (CoreException e1) {
            log.error(e1);
        }

        List<Runnable> refreshedRannables = new ArrayList<Runnable>();

        for (IFile file : removed) {
            try {
                refreshedRannables.add(getDeleteRunnable(file));
            } catch (Exception e) {
                log.error(e);
            }
        }
        for (IFile file : added) {
            Resource resource = null;
            try {
                // URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
                // if (isExist(uri)) {

                resource = RepResourceFileHelper.getInstance().getFileResource(file);

                if (resource != null && checkResource(resource)) {
                    refreshedRannables.add(getRefreshRunnable(resource));
                }

            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            }
        }
        if (refreshedRannables != null && refreshedRannables.size() > 0) {
            postEventExecute(refreshedRannables);
        }
        // reRegisterListner();
    }

    /**
     * DOC klliu Comment method "initFolderForVisitor".
     * 
     * @param rootDelta
     */
    private IResourceDelta initFolderForVisitor(IResourceDelta rootDelta) {
        IResourceDelta findMember = null;
        IResourceDelta[] affectedChildren = rootDelta.getAffectedChildren();
        for (IResourceDelta child : affectedChildren) {
            String osString = child.getFullPath().toOSString();
            if (!osString.equals("\\.JETEmitters") && !osString.equals("\\.metadata") && !osString.equals("\\.Java")) {
                IResourceDelta[] folderDeltas = child.getAffectedChildren();
                String[] folderNames = { "TDQ_Data Profiling", "connections", "TDQ_Libraries" };
                IProject rootProject = ResourceManager.getRootProject();
                IFolder folder = null;
                for (String folderName : folderNames) {
                    for (IResourceDelta folderDelta : folderDeltas) {
                        if (folderDelta.getResource().getName().toString().contains(folderName)) {
                            folder = rootProject.getFolder(folderName);
                            IPath fullPath = folder.getFullPath();
                            findMember = rootDelta.findMember(fullPath);
                        }
                    }
                }
            }
        }

        return findMember;
    }

    private boolean checkResource(Resource resource) {
        URI uri = resource.getURI();

        if (uri.isFile()) {
            return new File(uri.toFileString()).exists();
        } else {
            IPath fullPath = new Path(uri.toPlatformString(false));
            return ResourceManager.getRoot().getFile(fullPath).exists();
        }
    }

    // private void reRegisterListner() {
    // WorkspaceJob cleanJob = new WorkspaceJob("Register resource listener") {
    //
    // public boolean belongsTo(Object family) {
    // return ResourcesPlugin.FAMILY_MANUAL_BUILD.equals(family);
    // }
    //
    // public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
    // wsp.addResourceChangeListener(ResoureceChangedListener.this);
    // return Status.OK_STATUS;
    // }
    // };
    // cleanJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
    // cleanJob.setUser(true);
    // cleanJob.schedule();
    //
    // }

    private void postEventExecute(final List<Runnable> refreshedRunnables) {
        WorkspaceJob cleanJob = new WorkspaceJob("Workspace refresh") {

            public boolean belongsTo(Object family) {
                return ResourcesPlugin.FAMILY_MANUAL_BUILD.equals(family);
            }

            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

                runUpdates(refreshedRunnables);

                return Status.OK_STATUS;
            }
        };
        cleanJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
        cleanJob.setUser(true);
        cleanJob.schedule();

    }

    private void runUpdates(List<Runnable> runnables) {
        Iterator<Runnable> runnableIterator = runnables.iterator();
        while (runnableIterator.hasNext()) {
            ((Runnable) runnableIterator.next()).run();
        }

    }

    private Runnable getDeleteRunnable(final IFile file) {
        return new Runnable() {

            public void run() {
                IFile propFile = ResourcesPlugin.getWorkspace().getRoot()
                        .getFile(file.getFullPath().removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION));
                if (propFile.exists()) {
                    try {
                        propFile.delete(true, new NullProgressMonitor());
                    } catch (CoreException e) {
                        log.error(e);
                    }
                }
            }
        };
    }

    private Runnable getRefreshRunnable(final Resource resource) {
        return new Runnable() {

            public void run() {
                // do nothing.
            }
        };
    }

}
