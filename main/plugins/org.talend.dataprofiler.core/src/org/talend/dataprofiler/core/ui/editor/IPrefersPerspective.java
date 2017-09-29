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

import java.util.List;

/**
 * Let Workbench parts implement this interface if you want {@link PreferredPerspectivePartListener} to automatically
 * activate the preferred perspective on part activation.
 */
public interface IPrefersPerspective {

    /**
     * @return the preferred perspective's id list of this part or null if no perspective is preferred.
     */
    List<String> getPreferredPerspectiveId();
}
