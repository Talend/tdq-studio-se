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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * TDQ-7418 msjian: update the dateFrom and dateEnd type from Date to String.
 * e.g: from dateFrom="2004-06-12T00:00:00.000+0800"  to dateFrom="06/12/2004".
 */
public class UpdateReportBeginEndDateTask extends AbstractWorksapceUpdateTask {

    public Date getOrder() {
        return createDate(2014, 7, 7);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        List<? extends ModelElement> allElement = RepResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allElement) {
            if (me instanceof TdReport) {
            	TdReport tdReport = (TdReport) me;
            	boolean isChanged = false;
            	
            	// dateFrom
            	String oldDateFromStr = tdReport.getDateFrom();
            	if (oldDateFromStr!= null ) {
            		if (!ContextHelper.isContextVar(oldDateFromStr)) {
            			if (oldDateFromStr.length() > 10) {
            				// like: 2004-06-12T00:00:00.000+0800 --> 06/12/2004
            				Date oldDateFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(oldDateFromStr);
            				tdReport.setDateFrom(new SimpleDateFormat("MM/dd/yyyy").format(oldDateFromDate));     
            				isChanged = true;
            		    }
            		}
            	}
            	
            	// dateTo
            	String oldDateToStr = tdReport.getDateTo();
            	if (oldDateToStr!= null ) {
            		if (!ContextHelper.isContextVar(oldDateToStr)) {
            			if (oldDateToStr.length() > 10) {
            				// like: 2004-06-12T00:00:00.000+0800 --> 06/12/2004
            				Date oldDateToDate = new SimpleDateFormat("yyyy-MM-dd").parse(oldDateToStr);
            				tdReport.setDateTo(new SimpleDateFormat("MM/dd/yyyy").format(oldDateToDate));     
            				isChanged = true;
            		    }
            		}
            	}
            	
            	if (isChanged) {
            		ElementWriterFactory.getInstance().createReportWriter().save(me);
            	}
            }
        }
        return true;
    }
}
