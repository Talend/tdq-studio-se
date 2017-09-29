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
package org.talend.dataprofiler.core.ui.utils.pagination;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * 
 * DOC mzhao UIPagination class global comment. Detailled comment
 */
public abstract class PaginationInfo implements IPagination {

    protected ScrolledForm form;

    protected List<? extends Object> objectList;

    protected List<Widget> needDispostWidgets = new ArrayList<Widget>();

    protected UIPagination uiPagination;

    public PaginationInfo(ScrolledForm form, List<? extends Object> objectList, UIPagination uiPagination) {
        this.form = form;
        this.objectList = objectList;
        this.uiPagination = uiPagination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.pagination.IPagination#renderContents()
     */
    public void renderContents() {
        uiPagination.notifyPageNavigator();
        render();
        uiPagination.updatePageInfoLabel();
        uiPagination.pack();
        form.reflow(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.pagination.IPagination#dispose()
     */
    public void dispose() {
        for (Widget widget : needDispostWidgets) {
            widget.dispose();
        }
        needDispostWidgets.clear();
    }

    /**
     * DOC bZhou Comment method "render".
     */
    protected abstract void render();

}
