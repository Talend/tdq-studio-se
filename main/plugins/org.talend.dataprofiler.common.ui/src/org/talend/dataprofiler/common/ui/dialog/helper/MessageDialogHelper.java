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
package org.talend.dataprofiler.common.ui.dialog.helper;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.dataprofiler.common.ui.i18n.Messages;
import org.talend.dataquality.analysis.AnalysisType;

/**
 * created by zshen on Sep 4, 2013
 * Detailled comment
 *
 */
public class MessageDialogHelper {

    /**
     * 
     * open one warrning dialog to told user which analysis can not be used in the report
     * 
     * @param parent
     * @param uncompatibleAnaLs
     */
    public static void openAnalysisUnSupportWarringDialog(Shell parent, List<String> uncompatibleAnaLs) {
        if (uncompatibleAnaLs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (String line : uncompatibleAnaLs) {
                sb.append("-").append(line).append("\n");//$NON-NLS-1$ //$NON-NLS-2$
            }

            MessageDialog.openWarning(parent, Messages.getString("AnalysesSelectionAction.wrong"), //$NON-NLS-1$
                    Messages.getString("AnalysesSelectionAction.sorry", AnalysisType.COLUMN_CORRELATION, sb));//$NON-NLS-1$
        }
    }

}
