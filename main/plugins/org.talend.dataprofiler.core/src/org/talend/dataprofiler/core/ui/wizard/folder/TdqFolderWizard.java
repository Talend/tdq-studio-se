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
package org.talend.dataprofiler.core.ui.wizard.folder;

import org.eclipse.core.runtime.IPath;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.repository.model.IRepositoryNode;

/**
 * This class is a wizard to create/rename a folder on workspace.
 * 
 * @deprecated needn't to use this class anymore, because needn't to do any addtional works when rename a
 * ReportSubFolder, all the works should be done in the supder class
 */
@Deprecated
public class TdqFolderWizard extends org.talend.core.repository.ui.wizard.folder.FolderWizard {

    private IRepositoryNode _node;

    private IRepositoryObjectCRUDAction _repositoryObjectCRUD;

    public TdqFolderWizard(IPath path, ERepositoryObjectType objectType, IRepositoryNode node,
            IRepositoryObjectCRUDAction repositoryObjectCRUD) {
        super(path, objectType, node.getObject().getLabel());
        this._node = node;
        this._repositoryObjectCRUD = repositoryObjectCRUD;
    }

    @Override
    public boolean performFinish() {
        return super.performFinish();
    }
}
