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

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.CorrelationAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.utils.sql.Java2SqlType;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalyzeColumnCorrelationAction extends Action {

    boolean hasNumberColumn;

    boolean hasDateColumn;

    TreeSelection selection;

    public AnalyzeColumnCorrelationAction() {
        super(DefaultMessagesImpl.getString("AnalyzeColumnCorrelationAction.AnalyzeCorr")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        hasDateColumn = false;
        hasNumberColumn = false;

        if (openStandardAnalysisDialog(AnalysisType.COLUMN_CORRELATION) == Window.OK) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                CorrelationAnalysisDetailsPage page = (CorrelationAnalysisDetailsPage) editor
                        .getMasterPage();
                if (!this.selection.isEmpty()) {
                    DBColumnRepNode[] columns = new DBColumnRepNode[selection.size()];
                    Iterator it = this.selection.iterator();

                    int i = 0;
                    while (it.hasNext()) {
                        columns[i] = (DBColumnRepNode) it.next();
                        i++;
                    }
                    if (page != null) {
                    	page.getTreeViewer().setInput(columns);
                    	page.doSave(null);
                    }
                }
            }
        }
    }

    public void setColumnSelection(TreeSelection selection) {
        this.selection = selection;
    }

    private int openStandardAnalysisDialog(AnalysisType type) {
        AnalysisLabelParameter parameter = new AnalysisLabelParameter();

        checkSelectedColumn();
        if (hasNumberColumn && hasDateColumn) {
            MessageUI.openError(DefaultMessagesImpl.getString("AnalyzeColumnCorrelationAction.InvalidOperation")); //$NON-NLS-1$
            return Window.CANCEL;
        } else if (hasNumberColumn && !hasDateColumn) {
            parameter.setCategoryLabel(AnalysisLabelParameter.NUMBERIC_CORRELATION);
        } else if (!hasNumberColumn && hasDateColumn) {
            parameter.setCategoryLabel(AnalysisLabelParameter.DATE_CORRELATION);
        } else {
            parameter.setCategoryLabel(AnalysisLabelParameter.NOMINAL_CORRELATION);
        }

        Wizard wizard = WizardFactory.createAnalysisWizard(type, parameter);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        return dialog.open();
    }

    private void checkSelectedColumn() {
        if (!selection.isEmpty()) {
            Iterator it = selection.iterator();

            while (it.hasNext()) {
                Object theObject = it.next();
                TdColumn column = null;
                if (theObject instanceof DBColumnRepNode) {
                    column = ((DBColumnRepNode) theObject).getTdColumn();
                }
                if (Java2SqlType.isNumbericInSQL(column.getSqlDataType().getJavaDataType())) {
                    hasNumberColumn = true;
                } else if (Java2SqlType.isDateInSQL(column.getSqlDataType().getJavaDataType())
                        || Java2SqlType.isDateTimeSQL(column.getSqlDataType().getJavaDataType())) {
                    hasDateColumn = true;
                }
            }
        }
    }
}
