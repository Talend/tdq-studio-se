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
package org.talend.dataprofiler.core.ui.action.actions;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.EEcosCategory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ImportFactory;
import org.talend.dataprofiler.core.ui.dialog.message.ImportInfoDialog;
import org.talend.dataprofiler.ecos.EcosConstants;
import org.talend.dataprofiler.ecos.jobs.ComponentDownloader;
import org.talend.dataprofiler.ecos.jobs.DownloadListener;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dataprofiler.ecos.model.IRevision;
import org.talend.dataprofiler.ecos.service.JobService;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportRemotePatternAction extends Action {

    private IEcosComponent[] components;

    private int fExtensionDownloaded;

    private List<IEcosComponent> fInstalledComponents;

    public ImportRemotePatternAction(IEcosComponent[] components) {
        super(DefaultMessagesImpl.getString("ImportRemotePatternAction.ImportDqRepository")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));

        this.components = components;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        setEnabled(false);
        try {
            Job job = new DownloadJob(components);
            fExtensionDownloaded = 0;
            fInstalledComponents = new ArrayList<IEcosComponent>();
            job.addJobChangeListener(new JobChangeAdapter() {

                @Override
                public void done(final IJobChangeEvent event) {

                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            doImportAfterDownload(event);
                        }
                    });
                }
            });
            JobService.scheduleUserJob(job);

        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }
    }

    private void doImportAfterDownload(IJobChangeEvent event) {

        final List<ReturnCode> information = new ArrayList<ReturnCode>();

        setEnabled(true);
        if (fExtensionDownloaded > 0) {
            String csvFormat = System.getProperty("talend.exchange.csv"); //$NON-NLS-1$
            if ("true".equals(csvFormat)) { //$NON-NLS-1$
                for (IEcosComponent componet : fInstalledComponents) {
                    List<ImportObject> validImportObject = ImportObject.extractImportObject(componet, information);

                    if (!validImportObject.isEmpty()) {
                        String categoryName = componet.getCategry().getName();

                        EEcosCategory ecosCategory = EEcosCategory.getEcosCategory(categoryName);

                        if (ecosCategory != null) {
                            EResourceConstant resourceType = ecosCategory.getResource();
                            for (ImportObject importObject : validImportObject) {
                                information.addAll(ImportFactory.doImport(resourceType, importObject, componet.getName()));
                            }
                        }
                    }
                }

                // MOD qiongli 2011-11-28 TDQ-4038,give the message when there is nothing to import.
                if (information.isEmpty()) {
                    information.add(new ReturnCode(
                            DefaultMessagesImpl.getString("ImportRemotePatternAction.NothingImport"), false)); //$NON-NLS-1$
                }
                ImportInfoDialog
                        .openImportInformation(
                                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                DefaultMessagesImpl.getString("ImportRemotePatternAction.ImportFinish"), information.toArray(new ReturnCode[0])); //$NON-NLS-1$
                CorePlugin.getDefault().refreshDQView();
            } else {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        for (IEcosComponent componet : fInstalledComponents) {
                            try {
                                ImportFactory.importFromExchange(componet);
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Notify after download complete.
     * 
     * @param extension
     */
    void extensionDownloadCompleted(IEcosComponent extension) {
        fExtensionDownloaded++;
        fInstalledComponents.add(extension);
    }

    /**
     * DOC bZhou ImportRemotePatternAction class global comment. Detailled comment
     */
    class DownloadJob extends Job implements DownloadListener {

        private IProgressMonitor fMonitor = null;

        private String fProgressLabel;

        private int fBytesDownloaded;

        private IEcosComponent[] fExtensions;

        public DownloadJob(IEcosComponent[] extensions) {
            super(EcosConstants.DOWNLOAD_TASK_TITLE);
            fExtensions = extensions;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, fExtensions.length * 10 + 5);
            progress.setTaskName(this.getName());
            for (IEcosComponent extension : fExtensions) {
                if (progress.isCanceled()) {
                    return Status.CANCEL_STATUS;
                }
                fMonitor = progress.newChild(10);
                downloadExtension(extension, fMonitor);
            }
            progress.setTaskName(EcosConstants.RELOAD_PALETTE);
            // progress.done();
            return Status.OK_STATUS;
        }

        private void downloadExtension(final IEcosComponent extension, final IProgressMonitor monitor) {
            // get the revision
            try {
                IRevision revision = getRevision(extension);
                if (revision == null) {
                    return;
                }
                String componentUrl = revision.getUrl();
                monitor.setTaskName(EcosConstants.DOWNLOAD_TASK_NAME + PluginConstant.SPACE_STRING + componentUrl);
                String targetFolder = ResourceManager.getExchangeFolder().getLocation().toOSString();
                String fileName = revision.getFileName();
                File localZipFile = new File(targetFolder, fileName);

                if (extension.getInstalledLocation() != null && extension.getInstalledRevision() != null) {
                    // if already install the latest revision, ignore
                    if (extension.getInstalledRevision().getName().equals(revision.getName())) {
                        if (localZipFile.exists() && checkIfExisted(extension.getInstalledLocation())) {
                            // if the file size is same, this means the file has been downloaded already
                            if (isSameSize(localZipFile, componentUrl)) {
                                monitor.done();
                                extensionDownloadCompleted(extension);
                                return;
                            }
                        }
                    } else {
                        // before installing the new revision, delete the older
                        // revision that has been installed
                        File installedFile = new File(extension.getInstalledLocation());
                        if (installedFile.exists()) {
                            if (installedFile.isDirectory()) {
                                FileUtils.deleteDirectory(installedFile);
                            } else if (installedFile.isFile()) {
                                installedFile.delete();
                            }
                        }

                        extension.setInstalledLocation(null);
                        extension.setInstalledRevision(null);
                    }
                }

                URL url = new URL(componentUrl);

                monitor.setTaskName(EcosConstants.DOWNLOAD_TASK_NAME + url.toString());
                ComponentDownloader downloader = new ComponentDownloader();
                downloader.addDownloadListener(DownloadJob.this);
                // block until download complete
                downloader.download(url, localZipFile);

                // check if the job is cancelled
                if (!monitor.isCanceled()) {
                    // update extesion status
                    extension.setInstalledRevision(revision);
                    extension.setInstalledLocation(localZipFile.getAbsolutePath());
                    monitor.done();
                    extensionDownloadCompleted(extension);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        /**
         * DOC xqliu Comment method "isSameSize".
         * 
         * @param localFile local file
         * @param urlFile url file
         * @throws IOException
         */
        private boolean isSameSize(File localFile, String urlFile) throws IOException {
            boolean sameSize = false;
            if (localFile.exists() && localFile.isFile()) {
                HttpURLConnection httpConnection = (HttpURLConnection) new URL(urlFile).openConnection();
                if (localFile.length() == httpConnection.getContentLength()) {
                    sameSize = true;
                }
            }
            return sameSize;
        }

        /**
         * get the revision of the IEcosComponent, if there exist more than one, show a dialog for user to select.
         * 
         * @param extension
         * @return
         * @throws InterruptedException
         */
        private IRevision getRevision(IEcosComponent extension) throws InterruptedException {
            IRevision revision = null;
            final List<IRevision> revisions = extension.getRevisions();
            if (revisions != null && !revisions.isEmpty()) {
                if (revisions.size() == 1) {
                    revision = revisions.get(0);
                } else {
                    SelectRevisionDialogThread thread = new SelectRevisionDialogThread(new SelectRevisionDialog(null, revisions));
                    Display.getDefault().syncExec(thread);
                    revision = thread.getRevision();
                }
            }
            return revision;
        }

        /**
         * Check if the component folder really exist, as the user may delete the folder from filesystem.
         * 
         * @param installedLocation
         * @return
         */
        private boolean checkIfExisted(String installedLocation) {
            try {
                File dir = new File(installedLocation);
                if (dir.exists()) {
                    return true;
                }
            } catch (Throwable e) {
                // do nothing;
            }
            return false;
        }

        public void downloadComplete() {
        }

        public void downloadProgress(ComponentDownloader downloader, int bytesRead) {
            if (fMonitor.isCanceled()) {
                // cancel download
                downloader.setCancel(true);
                return;
            }
            fBytesDownloaded += bytesRead;
            fMonitor.setTaskName(toKbFormat(fBytesDownloaded) + fProgressLabel);
            fMonitor.worked(bytesRead);
        }

        public void downloadStart(int totalSize) {
            fProgressLabel = "/" + toKbFormat(totalSize); //$NON-NLS-1$
            fBytesDownloaded = 0;
            fMonitor.beginTask("0 KB" + fProgressLabel, totalSize); //$NON-NLS-1$
        }

        private String toKbFormat(int size) {
            return String.format("%1$s KB", size >> 10); //$NON-NLS-1$
        }
    }

    /**
     * created by xqliu on Sep 25, 2012 Detailled comment
     */
    class SelectRevisionDialogThread extends Thread {

        private SelectRevisionDialog selectRevisionDialog;

        private IRevision revision;

        public IRevision getRevision() {
            return this.revision;
        }

        public SelectRevisionDialogThread(SelectRevisionDialog selectRevisionDialog) {
            this.selectRevisionDialog = selectRevisionDialog;
        }

        @Override
        public void run() {
            if (this.selectRevisionDialog != null) {
                if (this.selectRevisionDialog.open() == Window.OK) {
                    this.revision = this.selectRevisionDialog.getRevision();
                }
            }
        }
    }

    /**
     * created by xqliu on Sep 25, 2012 Detailled comment
     */
    class SelectRevisionDialog extends Dialog {

        private List<IRevision> revisions;

        public List<IRevision> getRevisions() {
            if (this.revisions == null) {
                this.revisions = new ArrayList<IRevision>();
            }
            return this.revisions;
        }

        public void setRevisions(List<IRevision> revisions) {
            this.revisions = revisions;
        }

        private IRevision revision;

        public IRevision getRevision() {
            return this.revision;
        }

        public void setRevision(IRevision revision) {
            this.revision = revision;
        }

        /**
         * DOC xqliu SelectRevisionDialog constructor comment.
         * 
         * @param parentShell
         * @param revisions
         */
        protected SelectRevisionDialog(Shell parentShell, List<IRevision> revisions) {
            super(parentShell);
            this.setRevisions(revisions);
        }

        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(DefaultMessagesImpl.getString("SelectRevisionDialog.title")); //$NON-NLS-1$
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite comp = new Composite(parent, SWT.NONE);
            comp.setLayout(new GridLayout(2, true));
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));

            Label label = new Label(comp, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("SelectRevisionDialog.revisions")); //$NON-NLS-1$

            final CCombo revisionsCombo = new CCombo(comp, SWT.BORDER);
            revisionsCombo.setEditable(false);
            revisionsCombo.setItems(getRevisionItems());
            revisionsCombo.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    String revisionName = revisionsCombo.getText();
                    for (IRevision rev : getRevisions()) {
                        if (revisionName.equals(rev.getName())) {
                            setRevision(rev);
                            break;
                        }
                    }
                }

            });

            if (revisionsCombo.getItemCount() > 0) {
                revisionsCombo.select(0);
            }

            return comp;
        }

        private String[] getRevisionItems() {
            String[] items = new String[this.getRevisions().size()];
            int i = 0;
            for (IRevision rev : this.getRevisions()) {
                items[i] = rev.getName();
                i++;
            }
            return items;
        }
    }
}
