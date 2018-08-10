/*
 * Copyright (C) 2007 SQL Explorer Development Team http://sourceforge.net/projects/eclipsesql
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.sqlexplorer.plugin.editors;

import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.dbproduct.Session;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.sessiontree.model.utility.Dictionary;
import net.sourceforge.sqlexplorer.sqleditor.SQLTextViewer;
import net.sourceforge.sqlexplorer.sqleditor.actions.ExecSQLAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.properties.Item;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * TextEditor specialisation; encapsulates functionality specific to editing SQL.
 * 
 * Virtually all of this code came from SQLEditor, which used to be derived directly from TextEditor; SQLEditor now
 * combines the text editor (here, SQLTextEditor) and the result and messages panes in a single editor, hence this was
 * separated out for clarity
 * 
 * Note that MouseClickListener was also moved to a top-level, package-private class for readability
 * 
 * @modified John Spackman
 * 
 */
public class SQLTextEditor extends TextEditor {

    static int i = 0;

    private SQLEditor editor;

    private MouseClickListener mcl;

    private IPartListener partListener;

    /* package */SQLTextViewer sqlTextViewer;

    private boolean _enableContentAssist = SQLExplorerPlugin.getDefault().getPluginPreferences()
            .getBoolean(IConstants.SQL_ASSIST);

    private IPreferenceStore store;

    // ADD xqliu 2010-03-23 feature 10675
    private static final String DEFAULT_FILE_EXTENSION = ".sql";

    // TODO should use the file version property
    private static final String DEFAULT_VERSION_STRING = "_0.1";

    public SQLTextEditor(SQLEditor editor) {
        super();
        this.editor = editor;
        mcl = new MouseClickListener(editor);
        store = SQLExplorerPlugin.getDefault().getPreferenceStore();
        setPreferenceStore(SQLExplorerPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected boolean isLineNumberRulerVisible() {
        return true;
    }

    @Override
    protected boolean isOverviewRulerVisible() {
        return true;
    }

    @Override
    protected boolean isPrefQuickDiffAlwaysOn() {
        return true;
    }

    @Override
    public boolean isChangeInformationShowing() {
        return true;
    }

    @Override
    public IProgressMonitor getProgressMonitor() {
        return super.getProgressMonitor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#performSaveAs(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected void performSaveAs(IProgressMonitor progressMonitor) {
        // do nothing when the user is read-only for current project
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            return;
        }
        // PTODO qzhang correct save the sql file. for bug 3860.
        Shell shell = getSite().getShell();
        final IEditorInput input = getEditorInput();
        IDocumentProvider provider = getDocumentProvider();
        IEditorInput newInput;
        if (input instanceof IURIEditorInput && !(input instanceof IFileEditorInput)) {
            super.performSaveAs(progressMonitor);
            return;
        }

        ILabelProvider lp = new WorkbenchLabelProvider();
        ITreeContentProvider cp = new WorkbenchContentProvider();
        FolderSelectionDialog dialog = new FolderSelectionDialog(shell, lp, cp);
        if (dialog.open() == Window.CANCEL) {
            if (progressMonitor != null) {
                progressMonitor.setCanceled(true);
            }
            return;
        }

        Object elements = dialog.getResult()[0];
        // ADD xqliu 2010-03-08 feature 10675
        IResource elem = (IResource) elements;
        if (elem instanceof IFolder) {
            IPath filePath = ((IFolder) elem).getFullPath();
            filePath = filePath.append(dialog.getFileName() + DEFAULT_FILE_EXTENSION);
            if (filePath == null) {
                if (progressMonitor != null) {
                    progressMonitor.setCanceled(true);
                }
                return;
            }
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IFile file = workspace.getRoot().getFile(filePath);
            newInput = new FileEditorInput(file);
            if (provider == null) {
                // editor has programmatically been closed while the dialog was open
                return;
            }
            boolean success = false;
            try {

                provider.aboutToChange(newInput);
                // MOD qiongli 2011-4-21 bug 20205.after creating property file,file name is changed(contain version
                // info),so reset'newInput'.
                file = createIFile(file, getViewer().getDocument().get());
                newInput = new FileEditorInput(file);
                success = true;

            } catch (CoreException x) {
                final IStatus status = x.getStatus();
                if (status == null || status.getSeverity() != IStatus.CANCEL) {
                    String title = "The file save failure.";
                    String msg = "The file save failure.";
                    MessageDialog.openError(shell, title, msg);
                }
            } finally {
                provider.changed(newInput);
                if (success) {
                    setInput(newInput);
                }
            }

            if (progressMonitor != null) {
                progressMonitor.setCanceled(!success);
            }
        }
    }

    /**
     * DOC qzhang Comment method "createIFile".
     * 
     * @param monitor
     * @param file
     * @param content
     * @throws CoreException
     */
    private IFile createIFile(IFile file, String content) throws CoreException {
        // MOD qiongli 2011-4-21.bug 20205 .should create sql file and property.use extension of service mechanism.
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            ITDQRepositoryService service = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                    ITDQRepositoryService.class);
            if (service != null) {
                String fName = StringUtils.removeEnd(StringUtils.removeEnd(file.getName(), DEFAULT_FILE_EXTENSION),
                        DEFAULT_VERSION_STRING);
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IPath rootPath = new Path("TDQ_Libraries/Source Files");
                Item item = service.createFile(content,
                        file.getProjectRelativePath().removeLastSegments(1).makeRelativeTo(rootPath), fName,
                        file.getFileExtension());
                // get the correct path(contain version info) for newInput file in editor.
                IPath location = file.getLocation();
                if (item != null && item.getProperty() != null && location != null) {

                    location = location.removeLastSegments(1);
                    StringBuffer strb = new StringBuffer();
                    strb.append(location.toString());
                    String version = item.getProperty().getVersion() == null ? "" : "_" + item.getProperty().getVersion();
                    strb.append(Path.SEPARATOR).append(fName).append(version).append(DEFAULT_FILE_EXTENSION);
                    location = Path.fromOSString(strb.toString());
                    file = workspace.getRoot().getFileForLocation(location);
                }
            }
        }
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()
     */
    @Override
    protected void createActions() {

        super.createActions();

        if (!_enableContentAssist) {
            return;
        }

        Action action = new Action("Auto-Completion") {

            @Override
            public void run() {
                sqlTextViewer.showAssistance();
            }
        };

        // This action definition is associated with the accelerator Ctrl+Space
        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssistProposal", action);

    }

    @Override
    public void createPartControl(Composite parent) {

        super.createPartControl(parent);

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(getSourceViewer().getTextWidget(), SQLExplorerPlugin.PLUGIN_ID + ".SQLEditor");

        Object adapter = getAdapter(org.eclipse.swt.widgets.Control.class);
        if (adapter instanceof StyledText) {
            StyledText text = (StyledText) adapter;
            text.setWordWrap(SQLExplorerPlugin.getDefault().getPluginPreferences().getBoolean(IConstants.WORD_WRAP));

            FontData[] fData = PreferenceConverter.getFontDataArray(store, IConstants.FONT);
            if (fData.length > 0) {
                JFaceResources.getFontRegistry().put(fData[0].toString(), fData);
                text.setFont(JFaceResources.getFontRegistry().get(fData[0].toString()));
            }
        }
    }

    @Override
    protected ISourceViewer createSourceViewer(final Composite parent, IVerticalRuler ruler, int style) {

        parent.setLayout(new FillLayout());
        final Composite myParent = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        layout.marginHeight = layout.marginWidth = layout.horizontalSpacing = layout.verticalSpacing = 0;
        myParent.setLayout(layout);

        // create divider line

        Composite div1 = new Composite(myParent, SWT.NONE);
        GridData lgid = new GridData();
        lgid.grabExcessHorizontalSpace = true;
        lgid.horizontalAlignment = GridData.FILL;
        lgid.heightHint = 1;
        lgid.verticalIndent = 1;
        div1.setLayoutData(lgid);
        div1.setBackground(editor.getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        // create text viewer

        GridData gid = new GridData();
        gid.grabExcessHorizontalSpace = gid.grabExcessVerticalSpace = true;
        gid.horizontalAlignment = gid.verticalAlignment = GridData.FILL;

        Dictionary dictionary = null;
        if (editor.getSession() != null && _enableContentAssist) {
            dictionary = editor.getSession().getUser().getMetaDataSession().getDictionary();
        }
        sqlTextViewer = new SQLTextViewer(myParent, style, store, dictionary, ruler);
        sqlTextViewer.getControl().setLayoutData(gid);

        // create bottom divider line

        Composite div2 = new Composite(myParent, SWT.NONE);
        lgid = new GridData();
        lgid.grabExcessHorizontalSpace = true;
        lgid.horizontalAlignment = GridData.FILL;
        lgid.heightHint = 1;
        lgid.verticalIndent = 0;
        div2.setLayoutData(lgid);
        div2.setBackground(editor.getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        final SQLEditor thisEditor = editor;
        sqlTextViewer.getTextWidget().addVerifyKeyListener(new VerifyKeyListener() {

            private ExecSQLAction _execSQLAction = new ExecSQLAction(thisEditor);

            @Override
            public void verifyKey(VerifyEvent event) {

                if (event.stateMask == SWT.CTRL && event.keyCode == 13) {
                    event.doit = false;
                    _execSQLAction.run();
                }
            }
        });

        sqlTextViewer.getTextWidget().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                SQLTextEditor.this.editor.getEditorSite().getPage().activate(SQLTextEditor.this.editor.getEditorSite().getPart());
            }
        });

        myParent.layout();

        IDocument dc = new Document();
        sqlTextViewer.setDocument(dc);

        mcl.install(sqlTextViewer);

        return sqlTextViewer;
    }

    public void setNewDictionary(final Dictionary dictionary) {
        if (editor.getSite() != null && editor.getSite().getShell() != null && editor.getSite().getShell().getDisplay() != null) {
            editor.getSite().getShell().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {

                    if (sqlTextViewer != null && !editor.getTitleImage().isDisposed()) {
                        sqlTextViewer.setNewDictionary(dictionary);
                    }

                }
            });
        }
    }

    public void onEditorSessionChanged(Session session) {
        if (session != null && _enableContentAssist) {
            setNewDictionary(editor.getSession().getUser().getMetaDataSession().getDictionary());
        } else {
            setNewDictionary(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        if (partListener != null) {
            editor.getEditorSite().getPage().removePartListener(partListener);
        }
        mcl.uninstall();
        super.dispose();
    }

    ISourceViewer getViewer() {
        return getSourceViewer();
    }
}
