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
package org.talend.dataprofiler.common.ui.pagination.controller;

import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.swt.SWT;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class PageableWithIndexController extends PageableController {

    private static final long serialVersionUID = -6714609750285266698L;

    private String[] propertiesStr;

    public PageableWithIndexController(int pageSize) {
        super(pageSize);
    }

    /**
     * Sets the propertiesStr.
     * 
     * @param propertiesStr the propertiesStr to set
     */
    public void setPropertiesStr(String[] propertiesStr) {
        this.propertiesStr = propertiesStr;
    }

    /**
     * Getter for propertiesStr.
     * 
     * @return the propertiesStr
     */
    public String[] getPropertiesStr() {
        return this.propertiesStr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.pagination.PageableController#getSortDirection()
     */
    @Override
    public int getSortDirection() {
        if (super.getSortDirection() == SWT.UP) {
            return 1;
        }
        return 0;
    }

}
