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

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.compare.exception.ReloadCompareException;

/**
 * The class use to display the current level comparison's result(EMF file model
 * and database model comparison) , or reload the current level element and save
 * the updated elements.
 */
public interface IComparisonLevel {

    public Connection reloadCurrentLevelElement()
			throws ReloadCompareException;

	public void popComparisonUI(IUIHandler uiHandler)
			throws ReloadCompareException;
}
