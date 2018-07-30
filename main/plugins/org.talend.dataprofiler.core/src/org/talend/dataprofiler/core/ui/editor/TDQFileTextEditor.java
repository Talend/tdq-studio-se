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
package org.talend.dataprofiler.core.ui.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.talend.dq.helper.ProxyRepositoryManager;


public class TDQFileTextEditor extends TextEditor {

    public static final String FILE_EDITOR_ID = "org.talend.dataprofiler.core.ui.editor.TDQFileTextEditor";//$NON-NLS-1$

    @Override
    public boolean isEditable() {
        if (ProxyRepositoryManager.getInstance().isReadOnly()) {
            return false;
        }
        return super.isEditable();
    }

    @Override
     public boolean isDirty(){
        if (ProxyRepositoryManager.getInstance().isReadOnly()) {
            return false;
        }
        return super.isDirty();
     }

}
