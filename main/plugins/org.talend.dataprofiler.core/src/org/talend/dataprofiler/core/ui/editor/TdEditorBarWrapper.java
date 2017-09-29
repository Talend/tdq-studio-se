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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

/**
 * 
 * DOC mzhao TdEditorToolBar class global comment. Detailled comment
 */
public class TdEditorBarWrapper {

    private List<ExpandableComposite> compositeList = null;

    private FormEditor editor;

    /**
     * DOC bZhou TdEditorBarWrapper constructor comment.
     */
    public TdEditorBarWrapper(FormEditor editor) {
        compositeList = new ArrayList<ExpandableComposite>();
        this.editor = editor;
    }

    /**
     * DOC bZhou Comment method "addExpandableComposite".
     * 
     * @param composite
     */
    public void addExpandableComposite(ExpandableComposite composite) {
        if (!compositeList.contains(composite)) {
            this.compositeList.add(composite);
        }
    }

    /**
     * DOC bZhou Comment method "setExpandableComposites".
     * 
     * @param composites
     */
    public void setExpandableComposites(ExpandableComposite[] composites) {
        for (ExpandableComposite composite : composites) {
            addExpandableComposite(composite);
        }
    }

    /**
     * DOC bZhou Comment method "setExpandableComposites".
     * 
     * @param composites
     */
    public void setExpandableComposites(List<ExpandableComposite> composites) {
        for (ExpandableComposite composite : composites) {
            addExpandableComposite(composite);
        }
    }

    /**
     * DOC bZhou Comment method "getExpandableComposites".
     * 
     * @return
     */
    public List<ExpandableComposite> getExpandableComposites() {
        return compositeList;
    }

    /**
     * DOC bZhou Comment method "clearRegisterComposite".
     */
    public void clearRegisterComposite() {
        getExpandableComposites().clear();
    }

    /**
     * Getter for editor.
     * 
     * @return the editor
     */
    public FormEditor getEditor() {
        return editor;
    }
}
