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
package org.talend.dataprofiler.core.ui.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Handle the actions on the host composite.
 */
public abstract class AbstractAnalysisActionHandler {

    private final Composite host;

    public AbstractAnalysisActionHandler(Composite host) {
        this.host = host;
    }

    public Composite getHost() {
        return this.host;
    }

    public boolean doGlobalAction(String actionId) {
        if (actionId.equals(ActionFactory.DELETE.getId())) {
            handleRemove();
            return true;
        } else {
            return false;
        }
    }

    protected abstract void handleRemove();

}
