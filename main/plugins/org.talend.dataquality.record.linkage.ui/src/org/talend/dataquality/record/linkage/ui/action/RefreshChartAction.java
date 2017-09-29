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
import org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class RefreshChartAction extends Action {

    private AbstractMatchAnaysisTableSection matchingTableSection = null;

    public RefreshChartAction(AbstractMatchAnaysisTableSection matchingKeySection) {
        this.matchingTableSection = matchingKeySection;
    }

    @Override
    public void run() {
        this.matchingTableSection.refreshChart();
    }

}
