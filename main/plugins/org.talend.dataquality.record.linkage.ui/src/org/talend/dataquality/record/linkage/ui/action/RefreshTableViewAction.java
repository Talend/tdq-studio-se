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
package org.talend.dataquality.record.linkage.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;


/**
 * created by zshen on Aug 2, 2013
 * Detailled comment
 *
 */
public class RefreshTableViewAction extends Action {

    private TableViewer tv;

    public RefreshTableViewAction(TableViewer tv) {
        this.tv = tv;
        setText("Refresh"); //$NON-NLS-1$
    }

    @Override
    public void run() {
        tv.refresh();
    }
}
