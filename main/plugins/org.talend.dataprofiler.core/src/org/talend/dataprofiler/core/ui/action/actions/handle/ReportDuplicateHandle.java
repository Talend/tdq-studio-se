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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.talend.dataquality.reports.TdReport;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * duplicate a report
 */
public class ReportDuplicateHandle extends ModelElementDuplicateHandle {

    @Override
    protected ModelElement update(ModelElement oldObject, ModelElement newObject) {
        TdReport report = (TdReport) super.update(oldObject, newObject);
        // copy the client dependency if any
        newObject.getClientDependency().addAll(oldObject.getClientDependency());

        return report;
    }

    protected boolean needSaveDepend() {
        return true;
    }
}
