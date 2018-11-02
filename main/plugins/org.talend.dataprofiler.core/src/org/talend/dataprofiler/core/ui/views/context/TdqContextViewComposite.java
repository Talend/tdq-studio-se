// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.views.context;

import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.ContextComposite;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;

/**
 * created by xqliu on Jul 25, 2013 Detailled comment
 * 
 */
public class TdqContextViewComposite extends ContextComposite {

    private CommandStack commandStack = new CommandStack();

    private TdqContextView tdqContextView = null;

    /**
     * Getter for tdqContextView.
     * 
     * @return the tdqContextView
     */
    public TdqContextView getTdqContextView() {
        return this.tdqContextView;
    }

    /**
     * DOC xqliu TdqContextViewComposite constructor comment.
     * 
     * @param parent
     * @param contextView
     */
    public TdqContextViewComposite(Composite parent, TdqContextView contextView) {
        super(parent, false);
        this.tdqContextView = contextView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#getContextManager()
     */
    @Override
    public IContextManager getContextManager() {
        return this.part == null ? super.getContextManager() : ((SupportContextEditor) this.part).getContextManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#getCommandStack()
     */
    @Override
    public CommandStack getCommandStack() {
        return this.commandStack;
    }

    @Override
    public void switchSettingsView(String oldName, String newName) {
        IContextManager manager = this.getContextManager();
        if (manager != null && manager instanceof JobContextManager) {
            ((JobContextManager) manager).addNewName(newName, oldName);
        }
    }

    @Override
    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames, String sourceId) {
        if (contextManager != null && contextManager instanceof JobContextManager) {
            ((JobContextManager) contextManager).getLostParameters().addAll(paramNames);
        }
        super.onContextRemoveParameter(contextManager, paramNames, sourceId);
    }
}
