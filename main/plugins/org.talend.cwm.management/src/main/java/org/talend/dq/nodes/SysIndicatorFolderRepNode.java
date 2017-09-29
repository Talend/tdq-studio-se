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
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC klliu class global comment. Detailled comment: system indicator folder repository node
 */
public class SysIndicatorFolderRepNode extends DQFolderRepNode {

    /**
     * DOC msjian SysIndicatorFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public SysIndicatorFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
        // sub folders
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = new Folder((Property) container.getProperty(),
                    RepositoryNodeHelper.getSystemIndicatorFolderRepositoryType(container.getLabel()));

            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }

            SysIndicatorFolderRepNode childNodeFolder = new SysIndicatorFolderRepNode(folder, this, ENodeType.SYSTEM_FOLDER,
                    project);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
            super.getChildren().add(childNodeFolder);
        }

        // rule files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {

            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }

            // TDQ-11110: hidden some system indicators.
            String viewObjectlabel = viewObject.getLabel();
            if (viewObjectlabel.equals("Multiple Column Correlation") //$NON-NLS-1$
                    || viewObjectlabel.equals("Multiple Column Frequency Table") //$NON-NLS-1$
                    || viewObjectlabel.equals("All Match") //$NON-NLS-1$
                    || viewObjectlabel.equals("Multiple Column Simple Statistics")) { //$NON-NLS-1$
                continue;
            }
            // TDQ-11110~

            if (!viewObjectlabel.equals("Sum")) {//$NON-NLS-1$
                SysIndicatorDefinitionRepNode repNode = new SysIndicatorDefinitionRepNode(viewObject, this,
                        ENodeType.REPOSITORY_ELEMENT, project);
                repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
                repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
                viewObject.setRepositoryNode(repNode);
                repNode.setSystemIndicator(true);
                // MOD gdbu 2011-7-27 bug : 23161
                List<TdExpression> indiExpression = repNode.getIndicatorDefinition().getSqlGenericExpression();
                // MOD qiongli 2011-7-27,feature 22362
                boolean isPhoneNumberStatics = false;
                IndicatorCategory category = IndicatorCategoryHelper.getCategory(repNode.getIndicatorDefinition());
                if (category != null && IndicatorCategoryHelper.isPhoneNumberCategory(category)) {
                    isPhoneNumberStatics = true;
                }
                if (!isPhoneNumberStatics && !isEastAsiaPatternFequencyStatics(repNode.getIndicatorDefinition())
                        && (indiExpression == null || indiExpression.size() == 0)) {
                    continue;
                }
                // ~23161

                super.getChildren().add(repNode);
            }
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
        if (!withDeleted && folder.isDeleted()) {
            return true;
        }

        // TDQ-11110: hidden some system indicator folders.
        boolean isBusinessRules = folder.getLabel().equals(EResourceConstant.SYSTEM_INDICATORS_BUSINESS_RULES.getName());
        boolean isPhoneNumber = folder.getLabel().equals(EResourceConstant.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS.getName());
        if (isBusinessRules || isPhoneNumber) {
            return true;
        }
        // TDQ-11110~

        return false;
    }

    /**
     * DOC talend Comment method "isEastAsiaPatternFequencyStatics".
     * 
     * @param indicatorDefinition
     * @return
     */
    private boolean isEastAsiaPatternFequencyStatics(IndicatorDefinition indicatorDefinition) {
        String uuid = ResourceHelper.getUUID(indicatorDefinition);
        // EastAsiaPatternFrequency and EastAsiaPatternLowFrequency
        if ("_JMeW0F0TEeWGcZIHtEALuw".equals(uuid) || "_GIAgQF0TEeWGcZIHtEALuw".equals(uuid)) { //$NON-NLS-1$//$NON-NLS-2$
            return true;
        }
        return false;
    }

}
