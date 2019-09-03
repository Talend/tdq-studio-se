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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * created by yyin on 2015年4月24日 Detailled comment
 *
 */
public class HiveOfHCFolderRepNode extends DQFolderRepNode {

    private static Logger log = Logger.getLogger(HiveOfHCFolderRepNode.class);

    /**
     * DOC yyin HiveOfHCFolderRepNode constructor comment.
     *
     * @param object
     * @param parent
     * @param type
     */
    public HiveOfHCFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
        // for hive folder, its children are under connections folder, not here, so find children there, and show link
        // here.
        IPath path = getPathOfHiveFolder();
        Item clusterConnectionItem = getParent().getObject().getProperty().getItem();
        String clusterId = clusterConnectionItem.getProperty().getId();

        RootContainer<String, IRepositoryViewObject> tdqViewObjects = ProxyRepositoryFactory.getInstance()
                .getTdqRepositoryViewObjects(project, ERepositoryObjectType.METADATA_CONNECTIONS, path.toString());

        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            HiveOfHCConnectionNode repNode = null;
            // check if ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID = current hadoop cluster's id
            if (viewObject != null && viewObject.getProperty() != null) {
                String hcId = ConnectionUtils.getHadoopClusterIDOfHive(viewObject);
                if (!clusterId.equals(hcId)) {
                    continue;
                }
            }

            try {
                repNode = new HiveOfHCConnectionNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            } catch (Exception e) {
                log.error(e, e);
                continue;
            }
            repNode.setProperties(EProperties.LABEL, viewObject.getLabel());
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);
        }
    }

    private IPath getPathOfHiveFolder() {
        // TDQ-10570 msjian: normally is metadata/hadoop/hadoopcluster, but when there have a folder is
        // metadata/hadoop/hadoopcluster/a
        IPath path = RepositoryNodeHelper.getPath(this.getParent());
        // change it to: metadata/connections
        return new Path(path.segment(0)).append(EResourceConstant.DB_CONNECTIONS.getName()); // $NON-NLS-1$
    }

    @Override
    public String getDisplayText() {
        return Messages.getString("HiveOfHCFolderRepNode.displayText"); //$NON-NLS-1$
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
