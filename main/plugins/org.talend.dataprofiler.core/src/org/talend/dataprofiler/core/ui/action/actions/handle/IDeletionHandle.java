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

import java.util.List;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IDeletionHandle extends IActionHandle {

    /**
     * DOC bZhou Comment method "delete".
     * 
     * This function is to delete an item in tdq reponsitory.
     * 
     * @param isPhysical
     * @return
     * @throws Exception
     */
    public boolean delete() throws Exception;

    /**
     * DOC bZhou Comment method "getDependencies".
     * 
     * This function is to get dependencies of a file.
     * 
     * @return
     */
    public List<ModelElement> getDependencies();

    /**
     * DOC bZhou Comment method "isPhysicalDelete".
     * 
     * @return
     */
    public boolean isPhysicalDelete();
}
