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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC jet this is wizard for create new Function Dependency Analysis.
 * <p>
 * 
 */
public class FunctionDependencyWizard extends AbstractAnalysisWizard implements INewWizard {

    /**
     * DOC jet FunctionDependencyWizard constructor comment.
     * 
     * @param parameter
     */
    public FunctionDependencyWizard(AnalysisParameter parameter) {
        super(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        setHelpAvailable(true);
        setWindowTitle("Just a test"); //$NON-NLS-1$
    }

    @Override
    public void addPages() {
        this.addPage(new TableAnalysisMetadataWizardPage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#openEditor(org.talend.repository.model.IRepositoryNode
     * )
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        AnalysisItemEditorInput itemEditorInput = new AnalysisItemEditorInput(repNode);
        CorePlugin.getDefault().openEditor(itemEditorInput, AnalysisEditor.class.getName());
    }
}
