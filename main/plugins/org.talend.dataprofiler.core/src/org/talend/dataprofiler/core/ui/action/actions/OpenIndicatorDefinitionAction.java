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

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditor;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditorInput;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class OpenIndicatorDefinitionAction extends Action {

    private static Logger log = Logger.getLogger(OpenIndicatorDefinitionAction.class);

    private IndicatorDefinition[] definitiones;

    public OpenIndicatorDefinitionAction(IndicatorDefinition[] definitiones) {
        super(DefaultMessagesImpl.getString("OpenIndicatorDefinitionAction.Open")); //$NON-NLS-1$

        this.definitiones = definitiones;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (definitiones == null) {
            return;
        }

        for (IndicatorDefinition definition : definitiones) {
            IndicatorEditorInput input = new IndicatorEditorInput(definition);
            try {
                CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input,
                        IndicatorEditor.class.getName());
            } catch (PartInitException e) {
                log.error(e, e);
            }
        }
    }
}
