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
package org.talend.dataprofiler.core.ui.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Plugin;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC bZhou class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z bzhou $
 * 
 */
public abstract class AbstractImportBuildInFileAction extends AbstractImportFileAction {

    public AbstractImportBuildInFileAction(RepositoryNode node) {
        super(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractImportFileAction#computeFilePath()
     */
    @Override
    public Map<File, IPath> computeFilePath() throws Exception {
        ERepositoryObjectType type = getRepositoryType();
        Plugin plugin = getPlugin();
        String srcPath = getSrcPath();
        Folder destFolder = (Folder) node.getObject();
        Set<String> suffix = getSuffix();
        boolean isResurse = isRecurse();

        Map<File, IPath> resultMap = new HashMap<File, IPath>();
        if (type != null && plugin != null && srcPath != null) {
            WorkspaceResourceHelper.computeFileFromPlugin(plugin, srcPath, isResurse, destFolder, suffix, type, resultMap);
        }
        return resultMap;
    }

    protected boolean isRecurse() {
        return true;
    }

    protected Set<String> getSuffix() {
        return null;
    }

    public abstract Plugin getPlugin();

    public abstract String getSrcPath();

}
