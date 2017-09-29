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
package org.talend.dataprofiler.ecos.jobs;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.dataprofiler.ecos.EcosConstants;
import org.talend.dataprofiler.ecos.i18n.DefaultMessagesImpl;
import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dataprofiler.ecos.model.impl.EcosCategory;
import org.talend.dataprofiler.ecos.service.EcosystemService;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ObtainEcosComponentJob extends Job {

    private List<IEcosComponent> fAvailableComponents;

    private String version;

    private String category;

    public ObtainEcosComponentJob(String version, String category) {
        super(EcosConstants.OBTAIN_COMPONENT_TITLE);
        this.version = version;
        this.category = category;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        monitor.beginTask(DefaultMessagesImpl.getString("ObtainEcosComponentJob.searchComponents"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

        // run in another thread, make it possible to stop the remote procedure call when user press cancel
        // button
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<IEcosComponent>> task = executor.submit(new Callable<List<IEcosComponent>>() {

            public List<IEcosComponent> call() throws Exception {
                EcosystemService.getVersionList();
                IEcosCategory ecosCategory = new EcosCategory(category);
               
                return ComponentSearcher.getAvailableComponentExtensions(version, ecosCategory);
            }

        });

        while (true) {
            try {
                if (monitor.isCanceled()) {
                    // stop the web service call
                    task.cancel(true);
                    return Status.CANCEL_STATUS;
                } else if (task.isDone()) {
                    // web service call complete
                    fAvailableComponents = task.get();
                    break;
                }
            } catch (Exception e) {
                return Status.CANCEL_STATUS;
            } finally {
                executor.shutdown();
            }
        }
        monitor.done();
        return Status.OK_STATUS;
    }

    /**
     * Getter for fAvailableComponents.
     * 
     * @return the fAvailableComponents
     */
    public List<IEcosComponent> getFAvailableComponents() {
        return fAvailableComponents;
    }
}
