// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.download;

import java.util.ArrayList;
import java.util.List;

import org.talend.dq.helper.SqlExplorerUtils;

public class DQSqlExplorerJarsExtraUpdatesFactory extends AbstractDQMissingJarsExtraUpdatesFactory {

    @Override
    protected List<String> getJarFileNames() {
        List<String> jarFiles = new ArrayList<String>();
        jarFiles.add(SqlExplorerUtils.JAR_FILE_NAME);
        jarFiles.add(SqlExplorerUtils.JAR_NL_FILE_NAME);
        return jarFiles;
    }

    @Override
    protected String getPluginName() {
        return SqlExplorerUtils.PLUGIN_NAME;
    }

    @Override
    protected String getContextName() {
        return "context:net.sourceforge.sqlexplorer"; //$NON-NLS-1$
    }

    @Override
    protected String getInforMessage() {
        return "DQ need the plugin: net.sourceforge.sqlexplorer"; //$NON-NLS-1$
    }

    @Override
    protected String getDownloadName() {
        return "DownloadSqlexplorerPluginJarFactory.name"; //$NON-NLS-1$
    }
}
