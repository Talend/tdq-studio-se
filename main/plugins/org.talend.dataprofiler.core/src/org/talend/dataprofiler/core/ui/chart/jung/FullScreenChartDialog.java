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
package org.talend.dataprofiler.core.ui.chart.jung;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class FullScreenChartDialog extends TrayDialog {

    protected Object chart;

    public FullScreenChartDialog(Shell shell, Object chart) {
        super(shell);
        this.chart = chart;
        setShellStyle(SWT.RESIZE | SWT.CLOSE | SWT.MIN | SWT.MAX);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setSize(800, 500);
        newShell.setMaximized(true);
        // newShell.setFullScreen(true);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        return (Control) TOPChartUtils.getInstance().createChartCompositeWithFull(parent, chart);
    }

    @Override
    public boolean close() {
        chart = null;
        return super.close();
    }
}
