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
package org.talend.cwm.compare.factory;

import org.eclipse.core.runtime.IPath;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.compare.exception.ReloadCompareException;

/**
 * @author scorreia
 * 
 * Interface for handling UI comparison.
 */
public interface IUIHandler {

    /**
     * Method "popComparisonUI" opens a pop-up which displays the differences listed in the given file. MOD mzhao
     * 2009-03-09 Add dbname method for displaying db name info in compare editor.
     * 
     * @param diffResourcePath the path to the difference file
     * @throws ReloadCompareException
     */
    public void popComparisonUI(final IPath diffResourcePath, String dbName,
			Object selectedObject, boolean compareEachOther)
			throws ReloadCompareException;

    /**
     * Method "popRemoveElement" pops-up a dialog to display the impacts of a change in the data provider.
     * 
     * @param provider the data provider which is to be changed.
     */
    public void popRemoveElement(final Connection provider);
}
