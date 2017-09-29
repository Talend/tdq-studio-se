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
package org.talend.resource;

import org.eclipse.core.runtime.QualifiedName;
import org.talend.dataquality.PluginConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class ResourceConstant {

    private ResourceConstant() {

    }

    static final QualifiedName FOLDER_CLASSIFY_KEY = new QualifiedName(PluginConstant.PLUGIN_ID, "FOLDER_CLASSIFY"); //$NON-NLS-1$

    // PROPERTIES
    static final QualifiedName READONLY = new QualifiedName(PluginConstant.PLUGIN_ID, "FOLDER_READ_ONLY"); //$NON-NLS-1$

    static final QualifiedName NO_SUBFOLDER = new QualifiedName(PluginConstant.PLUGIN_ID, "NO_SUBFOLDER"); //$NON-NLS-1$

    static final QualifiedName PROJECT_TDQ = new QualifiedName(PluginConstant.PLUGIN_ID, "TDQ_PROJECT"); //$NON-NLS-1$

    static final String READONLY_PROPERTY = "FOLDER_READONLY_PROPERTY"; //$NON-NLS-1$

    static final String NO_SUBFOLDER_PROPERTY = "NO_SUBFOLDER_PROPERTY"; //$NON-NLS-1$

    static final String PROJECT_TDQ_PROPERTY = "PROJECT_TDQ_PROPERTY"; //$NON-NLS-1$

}
