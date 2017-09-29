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

import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC gdbu class global comment. Detailled comment
 */
public class RulesFolderRepNode extends DQFolderRepNode {

    /**
     * DOC gdbu RulesFolderRepNode constructor comment.
     *
     * @param object
     * @param parent
     * @param type
     */
    public RulesFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
        // when merge display the ref project items, we will not show the system indicators
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            if (project.isMainProject()) {
                createChildrenNode(withDeleted, project);
            }
        } else {
            createChildrenNode(withDeleted, project);
        }
    }

    /**
     * DOC msjian Comment method "createChildrenNode".
     * 
     * @param withDeleted
     * @param project
     * @throws PersistenceException
     */
    private void createChildrenNode(boolean withDeleted, Project project) throws PersistenceException {
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(project, this);

        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = null;
            boolean isSql = container.getLabel().equals("SQL"); //$NON-NLS-1$
            boolean isParser = container.getLabel().equals("Parser"); //$NON-NLS-1$
            boolean isMatcher = container.getLabel().equals("Match"); //$NON-NLS-1$
            if (isSql) {
                folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_RULES_SQL);
                if (isIgnoreFolder(withDeleted, project, folder)) {
                    continue;
                }
                RulesSQLFolderRepNode systemIndicatorFolderNode = new RulesSQLFolderRepNode(folder, this,
                        ENodeType.SYSTEM_FOLDER, project);
                folder.setRepositoryNode(systemIndicatorFolderNode);
                systemIndicatorFolderNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_RULES_SQL);
                systemIndicatorFolderNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_RULES_SQL);
                super.getChildren().add(systemIndicatorFolderNode);

            } else if (isMatcher) {
                folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_RULES_MATCHER);
                if (isIgnoreFolder(withDeleted, project, folder)) {
                    continue;
                }
                RulesMatcherFolderRepNode ruleMatcherFolder = new RulesMatcherFolderRepNode(folder, this,
                        ENodeType.SYSTEM_FOLDER, project);
                folder.setRepositoryNode(ruleMatcherFolder);
                ruleMatcherFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_RULES_MATCHER);
                ruleMatcherFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_RULES_MATCHER);
                super.getChildren().add(ruleMatcherFolder);

            } else if (isParser && PluginChecker.isTDQLoaded()) {
                folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_RULES_PARSER);
                if (isIgnoreFolder(withDeleted, project, folder)) {
                    continue;
                }
                RulesParserFolderRepNode systemIndicatorFolderNode = new RulesParserFolderRepNode(folder, this,
                        ENodeType.SYSTEM_FOLDER, project);
                folder.setRepositoryNode(systemIndicatorFolderNode);
                systemIndicatorFolderNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_RULES_PARSER);
                systemIndicatorFolderNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_RULES_PARSER);
                super.getChildren().add(systemIndicatorFolderNode);
            }

        }
    }

}
