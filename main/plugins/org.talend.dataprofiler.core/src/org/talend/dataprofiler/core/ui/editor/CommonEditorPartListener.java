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
package org.talend.dataprofiler.core.ui.editor;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.utils.CheatSheetUtils;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class CommonEditorPartListener extends PartListener {

    private static Logger log = Logger.getLogger(CommonEditorPartListener.class);

    private boolean firstTime = true;

    public Item getItem(IEditorPart editor) {
        Item tdqItem = null;
        IEditorInput editorInput = editor.getEditorInput();
        if (editorInput instanceof AbstractItemEditorInput) {
            return ((AbstractItemEditorInput) editorInput).getItem();
        }

        IFile propertyFile = getPropertyFile(editor);

        if (propertyFile == null) {
            return null;
        }
        URI uri = URI.createPlatformResourceURI(propertyFile.getFullPath().toString(), false);
        EMFSharedResources.getInstance().unloadResource(uri.toString());
        Resource propertyResource = EMFSharedResources.getInstance().getResource(uri, true);
        if (propertyResource != null) {
            EList<EObject> contents = propertyResource.getContents();
            Property property = null;
            for (EObject obj : contents) {
                if (obj instanceof Property) {
                    property = (Property) obj;
                    break;
                }
            }
            if (property != null) {
                tdqItem = property.getItem();
            }
        }
        return tdqItem;
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        // Added TDQ-7531 20130718 add unlock support for sql source file/jrxml file
        if (part.getClass().getName().equals(SqlExplorerUtils.SQLEDITOR_ID)) {
            unlockFile(part);
            super.partClosed(part);
            return;
        }// ~
         // MOD mzhao bug 12497 Firstly check if the part is TDQ common form editor.
        if (!isCommonFormEditor(part)) {
            return;
        }
        Item item = getItem((IEditorPart) part);
        if (item == null) {
            return;
        }
        // MOD qiongli 2011-7-14 bug 22276,just unlock and commit for editable itme.
        if (ProxyRepositoryManager.getInstance().isReadOnly() || ProxyRepositoryManager.getInstance().isEditable(item)) {
            ProxyRepositoryManager.getInstance().unLock(item);
        } else {
            ProxyRepositoryManager.getInstance().refresh();
        }
        // TDQ-11982: only refresh item instead of all view to avoid fold all in the view.
        WorkspaceResourceHelper.refreshItem(item);

        super.partClosed(part);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.PartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
     */
    @SuppressWarnings("restriction")
    @Override
    public void partDeactivated(IWorkbenchPart part) {
        super.partDeactivated(part);
        if (part instanceof org.eclipse.ui.internal.ViewIntroAdapterPart) {
            // The cheat sheet view has been open and max display then don't do it again
            if (CheatSheetUtils.getInstance().isFirstTime() && !PlatformUI.getWorkbench().isClosing() && firstTime) {
                firstTime = false;
                String cheatSheetID = PluginConstant.START_HERE_CHEAT_SHEET_ID;// tdq case
                if (PluginChecker.isOnlyTopLoaded()) {
                    cheatSheetID = PluginConstant.GETTING_STARTED_CHEAT_SHEET_ID;// top case
                }
                CheatSheetUtils.getInstance().findAndmaxDisplayCheatSheet(cheatSheetID);

            }
        }
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
        // Added TDQ-7531 20130718 add lock support for sql source file/jrxml file
        if (part.getClass().getName().equals(SqlExplorerUtils.SQLEDITOR_ID)) {
            lockFile(part);
            super.partOpened(part);
            return;
        }// ~

        // MOD mzhao bug 12497 Firstly check if the part is TDQ common form editor.
        if (!isCommonFormEditor(part)) {
            return;
        }
        Item item = getItem((IEditorPart) part);
        if (item == null) {
            return;
        }

        CorePlugin.getDefault().refreshWorkSpace();
        // If the item is not editable.
        // MOD 20130624 TDQ-7497 yyin, change to use :isEditableAndLockIfPossible instead of lock method
        // (when remote and ask user: when the user select unlock should be also not editable)
        if (!ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(item)) {
            // MOD yyi 2010-11-29 15686: Make the editor readonly when the login user has no sufficient previlege.
            lockCommonFormEditor(part);
            WorkspaceResourceHelper.refreshItem(item);
            return;
        }
        WorkspaceResourceHelper.refreshItem(item);

        if (part instanceof AnalysisEditor) {
            ((AnalysisEditor) part).getMasterPage().autoRefreshPreviewData();
        }
        super.partOpened(part);
    }

    /**
     * lock the sql source file when opening it. and if in remote "ask user" mode, and not locked, then make the editor
     * not editable
     * 
     * @param part
     */
    private void lockFile(IWorkbenchPart part) {
        IEditorInput editorInput = ((IEditorPart) part).getEditorInput();
        if (editorInput instanceof TDQFileEditorInput) {
            Item item = ((TDQFileEditorInput) editorInput).getFileItem();
            if (!ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(item)) {
                SqlExplorerUtils.getDefault().setSqlEditorEditable(part, false);
                return;
            }
            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.SOURCE_FILES));
        }

    }

    /**
     * Unlock the sql source file of two types: 1) directly opened sql file from the view, which use TDQFileEditorInput;
     * 2) created by "Preview Table" and saved, which use SQLEditorInput(create) and FileEditorInput(save) (inside the
     * sql explorer, can not be replaced by TDQFileEditorInput)
     * 
     * @param part
     */
    private void unlockFile(IWorkbenchPart part) {
        IEditorInput editorInput = ((IEditorPart) part).getEditorInput();
        Item item = null;
        if (editorInput instanceof TDQFileEditorInput) {
            item = ((TDQFileEditorInput) editorInput).getFileItem();
        } else if (editorInput instanceof FileEditorInput) {
            RepositoryNode fileNode = RepositoryNodeHelper.recursiveFindFile(((FileEditorInput) editorInput).getFile());
            item = fileNode.getObject().getProperty().getItem();
        }
        if (item == null) {// when preview a table and not save, close directly, the item is null
            return;
        }
        try {
            ProxyRepositoryFactory.getInstance().unlock(item);
        } catch (PersistenceException e) {
            log.error(e, e);
        } catch (LoginException e) {
            log.error(e, e);
        }

        CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.SOURCE_FILES));
    }

}
