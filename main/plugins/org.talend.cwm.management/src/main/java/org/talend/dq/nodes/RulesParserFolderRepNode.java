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
public class RulesParserFolderRepNode extends DQFolderRepNode {

    /**
     * DOC klliu RuleParserFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public RulesParserFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
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
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_RULES_PARSER);

            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }

            RulesParserSubFolderRepNode childNodeFolder = new RulesParserSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                    project);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_RULES_PARSER);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_RULES_PARSER);
            super.getChildren().add(childNodeFolder);
        }
        // rule files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            RuleRepNode repNode = new RuleRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_RULES_PARSER);
            repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_RULES_PARSER);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }

    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
