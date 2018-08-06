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
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class JrxmlTempFolderRepNode extends DQFolderRepNode {

    /**
     * DOC klliu JrxmlTempFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public JrxmlTempFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    private boolean duplicateNode(List<IRepositoryNode> children, JrxmlTempleteRepNode jrxmlNode) {
        for (IRepositoryNode node : children) {
            if (node.equals(jrxmlNode)) {
                return true;
            }
        }
        return false;
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
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }
            JrxmlTempSubFolderNode childNodeFolder =
                    new JrxmlTempSubFolderNode(folder, this, ENodeType.SIMPLE_FOLDER, project);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            folder.setRepositoryNode(childNodeFolder);
            super.getChildren().add(childNodeFolder);
        }

        // jrxml templates
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            JrxmlTempleteRepNode jrxmlNode =
                    new JrxmlTempleteRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            jrxmlNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            jrxmlNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            viewObject.setRepositoryNode(jrxmlNode);
            // MOD yyi 2011-04-21 19977 duplicate template after replace
            if (!duplicateNode(super.getChildren(), jrxmlNode)) {
                super.getChildren().add(jrxmlNode);
            }
        }
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }
}
