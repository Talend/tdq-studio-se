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
package org.talend.dataprofiler.core.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.talend.dataprofiler.core.ui.grid.IndicatorSelectDialog;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class SelectIndicatorsHandler extends AbstractHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        Shell activeShell = HandlerUtil.getActiveShell(event);
        if (null != activeShell) {
            // MOD sizhaoliu TDQ-6075 Enable keyboard shortcut to select all the indicators
            boolean select;
            if (event.getCommand().getId().equals("org.talend.dataprofiler.core.selectAllIndicators")) {//$NON-NLS-1$
                select = true;
            } else if (event.getCommand().getId().equals("org.talend.dataprofiler.core.deselectAllIndicators")) {//$NON-NLS-1$
                select = false;
            } else {
                return null;
            }
            Object dialog = activeShell.getData();

            if (dialog instanceof IndicatorSelectDialog) {
                ((IndicatorSelectDialog) dialog).selectAllIndicators(select);
            }
        }
        return null;
    }
}
