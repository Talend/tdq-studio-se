// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.nodes;

import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DFConnectionFolderRepNode extends DQFolderRepNode {

    /**
     * DOC qiongli FileDelimitedFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DFConnectionFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        if (getObject() == null) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        return this.getObject().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return Messages.getString("DQRepositoryViewLabelProvider.DFConnectionFolderName"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        return getChildren(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQFolderRepNode#getChildrenForProject(boolean, org.talend.core.model.general.Project)
     */
    @Override
    public void getChildrenForProject(boolean withDeleted, Project project) throws PersistenceException {
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(project, this);
        // sub folders
        // MOD qiongli 2011-1-18.setProperties for every node
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.METADATA_FILE_DELIMITED);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }
            DFConnectionSubFolderRepNode childNodeFolder = new DFConnectionSubFolderRepNode(folder, this,
                    ENodeType.SIMPLE_FOLDER, project);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            super.getChildren().add(childNodeFolder);
        }
        // connection files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }

            DFConnectionRepNode repNode = new DFConnectionRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }
    }

}
