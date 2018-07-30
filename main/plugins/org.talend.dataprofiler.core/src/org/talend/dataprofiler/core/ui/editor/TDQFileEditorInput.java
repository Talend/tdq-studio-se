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

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.properties.Item;


/**
 * Added 20130603, yyin TDQ-7143: Used to handle the file editor close event to unlock the file item.
 */
public class TDQFileEditorInput extends FileEditorInput {

    protected Item item = null;


    /**
     * DOC yyin TDQFileEditorInput constructor comment.
     *
     * @param file
     */
    public TDQFileEditorInput(IFile file) {
        super(file);
    }

    public void setFileItem(Item fItem) {
        this.item = fItem;
    }

    public Item getFileItem() {
        return this.item;
    }

}
