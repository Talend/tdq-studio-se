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
package org.talend.dq.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
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
public class ReportFolderRepNode extends DQFolderRepNode {

    private static Logger log = Logger.getLogger(ReportFolderRepNode.class);

    /**
     * DOC klliu ReportFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public ReportFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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

    private String getLabelWithCount() {
        String label = "Report"; //$NON-NLS-1$
        if (this.getObject() != null) {
            label = this.getObject().getLabel();
        } else {
            label = super.getLabel();
        }
        return decorateCount(label);
    }

    private String decorateCount(String label) {
        return label + " (" + getChildrenAll(false).size() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * get all children under the folder.
     * 
     * @param withDelete include deleted ones
     * @return
     */
    public List<IRepositoryNode> getChildrenAll(boolean withDeleted) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        try {
            RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(getProject(), this);

            // sub folders
            for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
                Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_REPORT_ELEMENT);
                if (isIgnoreFolder(withDeleted, getProject(), folder)) {
                    continue;
                }
                ReportSubFolderRepNode childNodeFolder = new ReportSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                        getProject());
                childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
                childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
                folder.setRepositoryNode(childNodeFolder);
                nodes.addAll(childNodeFolder.getChildrenAll(withDeleted));
            }

            // rep files
            for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
                if (!withDeleted && viewObject.isDeleted()) {
                    continue;
                }
                ReportRepNode repNode = new ReportRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, getProject());
                repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
                repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
                viewObject.setRepositoryNode(repNode);
                nodes.add(repNode);
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        return nodes;
    }

    public boolean isVirtualFolder() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabelWithCount();
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
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_REPORT_ELEMENT);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }

            ReportSubFolderRepNode childNodeFolder = new ReportSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER, project);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
            folder.setRepositoryNode(childNodeFolder);
            super.getChildren().add(childNodeFolder);
        }
        // rep files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            ReportRepNode repNode = new ReportRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQFolderRepNode#isIgnoreFolder(boolean, org.talend.core.model.general.Project,
     * org.talend.core.model.repository.Folder)
     */
    @Override
    public boolean isIgnoreFolder(boolean withDeleted, Project project, Folder folder) {
        // filter the generate report folders
        if (folder.getLabel().startsWith(".")) { //$NON-NLS-1$
            return true;
        }
        return super.isIgnoreFolder(withDeleted, project, folder);
    }
}
