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
package org.talend.dq.nodes.hadoopcluster;

import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.helper.HadoopClusterUtils;
import org.talend.dq.nodes.DQFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年4月21日 Detailled comment
 *
 */
public class HadoopClusterFolderRepNode extends DQFolderRepNode {

    /**
     * DOC yyin HadoopClusterFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public HadoopClusterFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
        return Messages.getString("DQRepositoryViewLabelProvider.HadoopClusterFolderName"); //$NON-NLS-1$
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
            Folder folder = new Folder((Property) container.getProperty(), HadoopClusterUtils.getDefault().getHadoopClusterType());
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }
            HadoopClusterSubFolderRepNode childNodeFolder = new HadoopClusterSubFolderRepNode(folder, this,
                    ENodeType.SIMPLE_FOLDER, project);
            childNodeFolder.setProperties(EProperties.LABEL, HadoopClusterUtils.getDefault().getHadoopClusterType());
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, HadoopClusterUtils.getDefault().getHadoopClusterType());
            folder.setRepositoryNode(childNodeFolder);
            super.getChildren().add(childNodeFolder);
        }
        // clusters
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }

            HadoopClusterConnectionRepNode repNode = new HadoopClusterConnectionRepNode(viewObject, this,
                    ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.LABEL, HadoopClusterUtils.getDefault().getHadoopClusterType());
            repNode.setProperties(EProperties.CONTENT_TYPE, HadoopClusterUtils.getDefault().getHadoopClusterType());
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }
    }

}
