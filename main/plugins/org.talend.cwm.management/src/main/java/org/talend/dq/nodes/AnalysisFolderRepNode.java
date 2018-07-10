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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class AnalysisFolderRepNode extends DQFolderRepNode {

    private static Logger log = Logger.getLogger(AnalysisFolderRepNode.class);

    /**
     * DOC talend AnalysisFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public AnalysisFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
    public void getChildrenForProject(boolean withDeleted, org.talend.core.model.general.Project project)
            throws PersistenceException {
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(project, this);

        // sub folders
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }
            AnalysisSubFolderRepNode childNodeFolder = new AnalysisSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                    project);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            folder.setRepositoryNode(childNodeFolder);
            super.getChildren().add(childNodeFolder);
        }

        // ana files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            AnalysisRepNode anaNode = new AnalysisRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            anaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            anaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            viewObject.setRepositoryNode(anaNode);
            super.getChildren().add(anaNode);
        }
    }

    public String getLabelWithCount() {
        String label = "Analysis"; //$NON-NLS-1$
        if (this.getObject() != null) {
            label = this.getObject().getLabel();
        } else {
            label = super.getLabel();
        }
        if (EResourceConstant.ANALYSIS.getName().equals(label)) {
            label = Messages.getString("RepositoryNodeHelper." + label);
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
                Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                if (isIgnoreFolder(withDeleted, getProject(), folder)) {
                    continue;
                }
                AnalysisSubFolderRepNode childNodeFolder = new AnalysisSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                        getProject());
                childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                folder.setRepositoryNode(childNodeFolder);
                nodes.addAll(childNodeFolder.getChildrenAll(withDeleted));
            }

            // ana files
            for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
                if (!withDeleted && viewObject.isDeleted()) {
                    continue;
                }
                AnalysisRepNode anaNode = new AnalysisRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, getProject());
                anaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                anaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                viewObject.setRepositoryNode(anaNode);
                nodes.add(anaNode);
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        return nodes;
    }

    /**
     * get all children under the folder.
     * 
     * @param withDelete include deleted ones
     * @return
     */
    public List<IRepositoryNode> getChildrenAll(boolean withDeleted, ViewerFilter filter) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        try {
            RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(getProject(), this);

            // sub folders
            for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
                Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                if (isIgnoreFolder(withDeleted, getProject(), folder)) {
                    continue;
                }
                AnalysisSubFolderRepNode childNodeFolder = new AnalysisSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                        getProject());
                childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                folder.setRepositoryNode(childNodeFolder);
                nodes.addAll(childNodeFolder.getChildrenAll(withDeleted, filter));
            }

            // ana files
            for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
                if (!withDeleted && viewObject.isDeleted()) {
                    continue;
                }
                AnalysisRepNode anaNode = new AnalysisRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, getProject());
                anaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                anaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
                viewObject.setRepositoryNode(anaNode);
                if (filter.select(null, anaNode.getParent(), anaNode)) {
                    nodes.add(anaNode);
                }
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
}
