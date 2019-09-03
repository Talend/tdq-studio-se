// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.helper.HadoopClusterUtils;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年4月24日 Detailled comment
 *
 */
public class HDFSOfHCFolderRepNode extends DQFolderRepNode {

    private static Logger log = Logger.getLogger(HDFSOfHCFolderRepNode.class);

    public HDFSOfHCFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public String getDisplayText() {
        return Messages.getString("HDFSOfHCFolderRepNode.displayText"); //$NON-NLS-1$
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
        // this folder path is: hadoop/hdfs, its parent's path is: hadoop/hadoopcluster
        IPath path = getPathOfHDFSFolder();
        Item clusterConnectionItem = getParent().getObject().getProperty().getItem();
        String clusterId = clusterConnectionItem.getProperty().getId();

        RootContainer<String, IRepositoryViewObject> tdqViewObjects = ProxyRepositoryFactory.getInstance()
                .getTdqRepositoryViewObjects(project, HadoopClusterUtils.getDefault().getHDFSType(), path.toString());
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            HDFSOfHCConnectionNode repNode = null;
            // check if this hdfs's relativeHadoopClusterId = this hadoop cluster's id
            if (viewObject != null && viewObject.getProperty() != null) {
                String hcId = HadoopClusterUtils.getDefault().getHadoopClusterID(viewObject);
                if (!StringUtils.equals(clusterId, hcId)) {
                    continue;
                }
            }
            try {
                repNode = new HDFSOfHCConnectionNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            } catch (Exception e) {
                log.error(e, e);
                continue;
            }
            repNode.setProperties(EProperties.LABEL, viewObject.getLabel());
            // repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }
    }

    private IPath getPathOfHDFSFolder() {
        // TDQ-10570 msjian: normally is metadata/hadoop/hadoopcluster, but when there have a folder is
        // metadata/hadoop/hadoopcluster/a
        IPath path = RepositoryNodeHelper.getPath(this.getParent());
        // change it to: metadata/hadoop/hdfs
        return new Path(path.segment(0)).append(path.segment(1)).append("hdfs"); //$NON-NLS-1$
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
