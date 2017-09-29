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
package org.talend.dataprofiler.core.ui.views.proposal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.proposal.ContextParameterProposal;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * created by xqliu on Jul 30, 2013 Detailled comment
 * 
 */
public class TdqProposalProvider implements IContentProposalProvider {

    private SupportContextEditor currentEditor = null;

    public SupportContextEditor getSupportContextEditor() {
        return this.currentEditor;
    }

    public TdqProposalProvider(SupportContextEditor currentEditor) {
        this.currentEditor = currentEditor;
    }

    public IContentProposal[] getProposals(String contents, int position) {
        List<IContentProposal> proposals = new ArrayList<IContentProposal>();

        if (this.getSupportContextEditor() != null && this.getSupportContextEditor().getContextManager() != null) {
            IContextManager contextManager = this.getSupportContextEditor().getContextManager();
            String defaultContextName = contextManager.getDefaultContext().getName();
            EList<ContextType> contexts = new BasicEList<ContextType>();
            contextManager.saveToEmf(contexts);

            List<IContextParameter> ctxParams = new JobContextManager(contexts, defaultContextName).getDefaultContext()
                    .getContextParameterList();
            for (IContextParameter ctxParam : ctxParams) {
                proposals.add(new ContextParameterProposal(ctxParam));
            }
        }

        return proposals.toArray(new IContentProposal[proposals.size()]);
    }
}
