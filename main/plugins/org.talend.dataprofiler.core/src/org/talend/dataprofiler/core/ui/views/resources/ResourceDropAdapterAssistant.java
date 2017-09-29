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
package org.talend.dataprofiler.core.ui.views.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ResourceTransfer;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.Property;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC rli class global comment. Detailled comment
 * 
 * @deprecated not used any more
 */
public class ResourceDropAdapterAssistant extends CommonDropAdapterAssistant {

    protected static Logger log = Logger.getLogger(ResourceDropAdapterAssistant.class);

    private static final IResource[] NO_RESOURCES = new IResource[0];

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonDropAdapterAssistant#handleDrop (org.eclipse.ui.navigator.CommonDropAdapter,
     * org.eclipse.swt.dnd.DropTargetEvent, java.lang.Object)
     */
    @Override
    public IStatus handleDrop(CommonDropAdapter aDropAdapter, DropTargetEvent aDropTargetEvent, Object target) {
        // use RepositoryNodeDorpAdapterAssistant to handle drag&drop RepositoryNode.
        // alwaysOverwrite = false;
        if (aDropAdapter.getCurrentTarget() == null || aDropTargetEvent.data == null) {
            return Status.CANCEL_STATUS;
        }
        // IStatus status = null;
        IResource[] resources = null;
        TransferData currentTransfer = aDropAdapter.getCurrentTransfer();
        if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {
            resources = getSelectedResources();
        } else if (ResourceTransfer.getInstance().isSupportedType(currentTransfer)) {
            resources = (IResource[]) aDropTargetEvent.data;
        }
        if ((target instanceof IFile)) { // && dropRep
            TdReport findReport = RepResourceFileHelper.getInstance().findReport(((IFile) target));

            List<Analysis> analyses = ReportHelper.getAnalyses(findReport);
            Map<String, Analysis> resourcesMap = new HashMap<String, Analysis>();
            for (Analysis ana : analyses) {
                String uriString = ana.eResource().getURI().toString();
                resourcesMap.put(uriString, ana);
            }
            if (resources != null && resources.length > 0) {
                List<Analysis> anaList = new ArrayList<Analysis>();
                for (IResource res : resources) {
                    Analysis findAnalysis = AnaResourceFileHelper.getInstance().findAnalysis((IFile) res);
                    if (findAnalysis != null) {
                        String uriKey = findAnalysis.eResource().getURI().toString();
                        if (resourcesMap.containsKey(uriKey)) {
                            findReport.removeAnalysis(resourcesMap.get(uriKey));
                        }
                        anaList.add(findAnalysis);
                    }
                }
                // ReportHelper.addAnalyses(anaList, findReport);
                RepResourceFileHelper.getInstance().save(findReport);
            }
        } else if (resources != null && (target instanceof IFolder)) { // && dropSql
            IFolder folder = (IFolder) target;
            // IPath location = folder.getFullPath();
            for (IResource res : resources) {
                if (res.getType() != IResource.FILE) {
                    return null;
                }
                if (folder.equals(res.getParent())) {
                    continue;
                }
                String name = res.getName();
                IFile fileRes = (IFile) res;
                IFile movedIFile = folder.getFile(name);
                if (!FactoriesUtil.isEmfFile(fileRes.getFileExtension())) {

                    try {
                        fileRes.move(movedIFile.getFullPath(), false, null);
                        fileRes.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
                        folder.refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                    return Status.OK_STATUS;
                }
                IContainer srcParent = fileRes.getParent();
                URI srcUri = URI.createPlatformResourceURI((fileRes).getFullPath().toString(), false);
                Resource resource = EMFSharedResources.getInstance().getResource(srcUri, true);
                if (resource != null) {
                    URI desUri = URI.createPlatformResourceURI(folder.getFullPath().toString(), false);
                    EMFSharedResources.getInstance().saveToUri(resource, desUri);
                    // ADD xqliu 2010-09-21 bug 15685
                    IFile propertyFile = PropertyHelper.getPropertyFile(fileRes);
                    if (propertyFile != null) {
                        IFile movedPropertyFile = folder.getFile(propertyFile.getName());
                        try {
                            propertyFile.move(movedPropertyFile.getFullPath(), true, null);
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                    // ~ 15685
                }
                try {
                    closeEditorIfOpened(fileRes);
                    fileRes.delete(true, null);
                    srcParent.refreshLocal(IResource.DEPTH_INFINITE, null);
                    folder.refreshLocal(IResource.DEPTH_INFINITE, null);
                    // ADD xqliu 2010-09-25 bug 15685 update the path of ItemState
                    Property property = PropertyHelper.getProperty(PropertyHelper.getPropertyFile(movedIFile));
                    property.getItem().getState().setPath(PropertyHelper.computePath(property));
                    // ~ 15685
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
                movedIFile = folder.getFile(name);
            }
        }
        ProxyRepositoryManager.getInstance().save();
        CorePlugin.getDefault().refreshDQView();
        return null;
    }

    /**
     * DOC bZhou Comment method "closeEditorIfOpened".
     * 
     * @param fileRes
     */
    private void closeEditorIfOpened(IFile fileRes) {
        IWorkbenchPage activePage = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorReferences = activePage.getEditorReferences();
        for (IEditorReference reference : editorReferences) {
            try {
                IEditorInput editorInput = reference.getEditorInput();
                if (editorInput instanceof FileEditorInput) {
                    FileEditorInput fileInput = (FileEditorInput) editorInput;

                    if (fileRes.getName().equals(fileInput.getFile().getName())) {
                        activePage.closeEditor(reference.getEditor(false), false);
                        break;
                    }
                }
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the resource selection from the LocalSelectionTransfer.
     * 
     * @return the resource selection from the LocalSelectionTransfer
     */
    private IResource[] getSelectedResources() {
        ISelection selection = LocalSelectionTransfer.getTransfer().getSelection();
        if (selection instanceof IStructuredSelection) {
            return getSelectedResources((IStructuredSelection) selection);
        }
        return NO_RESOURCES;
    }

    /**
     * Returns the resource selection from the LocalSelectionTransfer.
     * 
     * @return the resource selection from the LocalSelectionTransfer
     */
    @SuppressWarnings("unchecked")
    private IResource[] getSelectedResources(IStructuredSelection selection) {
        ArrayList selectedResources = new ArrayList();

        for (Iterator i = selection.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof IResource) {
                selectedResources.add(o);
            } else if (o instanceof IAdaptable) {
                IAdaptable a = (IAdaptable) o;
                IResource r = (IResource) a.getAdapter(IResource.class);
                if (r != null) {
                    selectedResources.add(r);
                }
            }
        }
        return (IResource[]) selectedResources.toArray(new IResource[selectedResources.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonDropAdapterAssistant#validateDrop(java.lang.Object, int,
     * org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public IStatus validateDrop(Object target, int operation, TransferData transferType) {
        if (!(target instanceof IResource)) {
            return Status.CANCEL_STATUS;
        }
        IResource targetRes = (IResource) target;
        for (IResource res : getSelectedResources()) {
            if (res.getType() == IResource.FILE) {
                IFile sourceFile = (IFile) res;

                switch (targetRes.getType()) {
                case IResource.FOLDER:
                    IFolder targetFolder = (IFolder) targetRes;
                    if (allowDND(sourceFile, targetFolder)) {
                        return Status.OK_STATUS;
                    }
                    break;
                case IResource.FILE:
                    IFile targetFile = (IFile) targetRes;
                    if (FactoriesUtil.isAnalysisFile(sourceFile.getFileExtension())
                            && FactoriesUtil.isReportFile(targetFile.getFileExtension())) {
                        return Status.OK_STATUS;
                    }

                    break;

                default:
                    break;
                }
            }
        }
        return Status.CANCEL_STATUS;
    }

    /**
     * DOC bZhou Comment method "allowDND".
     * 
     * @param sourceFile
     * @param targetFolder
     * @return
     */
    private boolean allowDND(IFile sourceFile, IFolder targetFolder) {
        ModelElement modelElement = ModelElementFileFactory.getModelElement(sourceFile);

        EResourceConstant typedConstant = EResourceConstant.getTypedConstant(modelElement);
        if (typedConstant != null) {
            IFolder oneFolder = ResourceManager.getOneFolder(typedConstant);
            return oneFolder == null ? false : ResourceService.isSubFolder(oneFolder, targetFolder);
        }

        return false;
    }
}
