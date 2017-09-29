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

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;


/**
 * DOC xqliu  class global comment. Detailled comment
 */
public class DefaultSaveAction extends Action {

    private IEditorPart editor;

    public DefaultSaveAction(IEditorPart editor) {
        super(DefaultMessagesImpl.getString("DefaultSaveAction.Save")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.SAVE));
        this.editor = editor;
    }

    @Override
    public void run() {
        if (editor != null) {
            editor.doSave(null);
        }
    }
}
