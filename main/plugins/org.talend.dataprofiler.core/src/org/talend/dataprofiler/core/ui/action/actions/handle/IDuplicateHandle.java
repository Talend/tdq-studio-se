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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.talend.commons.exception.BusinessException;
import org.talend.core.model.properties.Item;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IDuplicateHandle {

    /**
     * duplicate a TDQ item: For the Analysis, Report, the duplicated item will contains the client dependencies in old
     * one. For other type, like connection, pattern, UDI, the duplicated item will not contains any dependencies in old
     * one.
     * 
     * @param oldItem
     * @param newName the new name of the duplicated one
     * @return the duplicated item, name is the newName
     */
    public Item duplicateItem(Item oldItem, String newName) throws BusinessException;

}
