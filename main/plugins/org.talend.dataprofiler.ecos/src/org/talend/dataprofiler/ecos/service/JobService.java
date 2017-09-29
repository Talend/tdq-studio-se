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
package org.talend.dataprofiler.ecos.service;

import org.eclipse.core.runtime.jobs.Job;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class JobService {

    private JobService() {

    }

    public static void scheduleUserJob(Job job) {
        job.setUser(true);
        job.setPriority(Job.INTERACTIVE);
        job.schedule();
        job.wakeUp(); // start as soon as possible
    }
}
