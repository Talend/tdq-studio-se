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
package org.talend.dataprofiler.core.service;

/**
 * DOC rli class global comment. Detailled comment
 */
public interface IViewerFilterService extends IService {

    public int getViwerFilterId();

    /**
     * Judge add or delete the filter corresponding the special viewerfilterId.
     * 
     * @return if return true, will add the filter; else will delete the filter.
     */
    public boolean isAddOrDel();
}
