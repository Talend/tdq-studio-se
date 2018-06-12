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

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.PluginConstant;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC gdbu class global comment. Detailled comment
 */
public class DQRepositoryNode extends RepositoryNode {

    /**
     * this can tell us which project(reference project, or current project) the node belongs to
     */
    private org.talend.core.model.general.Project project = null;

    /**
     * when filtering , record the filter's contents.
     */
    private static String treeFilter = "";//$NON-NLS-1$

    /**
     * Record whether the user is filtering. true : is filtering ; false : not filtering.
     */
    private static boolean isOnFiltering = false;

    /**
     * when filtering , this value is used to determine whether to return all of the nodes.(if not filtering , this
     * value is useless.)
     */
    private static boolean isReturnAllNodesWhenFiltering = true;

    /**
     * after filter the tree , when we push next or previous button , this variable will be true , after display the
     * next or previous match node , this variable will be false again.
     */
    private static boolean isOnDisplayNextOrPreviousNode = false;

    /**
     * this will be only used in the "table select dialog", when do the schema filter.
     */
    private static boolean untilSchema = false;

    /**
     * this will be only used in the "column select dialog", when do the table filter.
     */
    private static boolean untilTable = false;

    /**
     * Getter for untilSchema.
     * 
     * @return the untilSchema
     */
    public static boolean isUntilSchema() {
        return untilSchema;
    }

    /**
     * Sets the untilSchema.
     * 
     * @param untilSchema the untilSchema to set
     */
    public static void setUntilSchema(boolean untilSchema) {
        DQRepositoryNode.untilSchema = untilSchema;
    }

    /**
     * Getter for untilTable.
     * 
     * @return the untilTable
     */
    public static boolean isUntilTable() {
        return untilTable;
    }

    /**
     * Sets the untilTable.
     * 
     * @param untilTable the untilTable to set
     */
    public static void setUntilTable(boolean untilTable) {
        DQRepositoryNode.untilTable = untilTable;
    }

    public static void setFiltering(boolean onFiltering) {
        isOnFiltering = onFiltering;
    }

    /**
     * 
     * DOC mzhao if the tree is rendering by filter or not.
     * 
     * @return
     */
    public static boolean isOnFilterring() {
        return isOnFiltering;
    }

    /**
     * DQRepositoryNode constructor.(this is used for all type's node)
     * 
     * @param object
     * @param parent
     * @param type
     * @param project if this is null, means we will use the current project
     */
    public DQRepositoryNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project project) {
        super(object, parent, type);
        this.project = project;
    }

    /**
     * filter method
     * 
     * DOC gdbu Comment method "filterResultsIfAny".
     * 
     * @param children
     * @return
     */
    public List<IRepositoryNode> filterResultsIfAny(List<IRepositoryNode> children) {
        if (!isOnFilterring()) {
            return children;
        }

        if (isReturnAllNodesWhenFiltering()) {
            return children;
        }

        List<IRepositoryNode> sortChildren = RepositoryNodeHelper.sortChildren(children);

        List<IRepositoryNode> filteredChildren = new ArrayList<IRepositoryNode>();
        if (!getFilterStr().equals(PluginConstant.EMPTY_STRING)) {
            DQRepositoryNode dqRepNode = null;
            for (IRepositoryNode dqNode : sortChildren) {
                dqRepNode = (DQRepositoryNode) dqNode;
                if (isUntilSchema()) {
                    if (dqRepNode instanceof DBTableRepNode || dqRepNode instanceof DBViewRepNode
                            || dqRepNode instanceof DFTableRepNode) {
                        break;
                    }
                }
                if (isUntilTable()) {
                    if (dqRepNode instanceof DBColumnFolderRepNode) {
                        break;
                    }
                }
                if (dqRepNode.canMatch()) {
                    filteredChildren.add(dqRepNode);
                }
            }
        }

        // if (getChildrenFilterStepList.size() != 0) {
        //
        // for (IRepositoryNode iRepositoryNode : getChildrenFilterStepList) {
        // RepositoryNodeHelper.setAllFilterNodeList((DQRepositoryNode) iRepositoryNode);
        // }
        //
        // // RepositoryNodeHelper.getAllFilteredNode().addAll(getChildrenFilterStepList);
        // getChildrenFilterStepList.clear();
        // }
        return filteredChildren;
    }

    public boolean canMatch() {
        boolean returnVal = false;
        // MOD gdbu 2011-7-18 bug : 23161
        // MOD msjian 2011-7-13 feature 22206 : fix note 0091973 issue1
        // when select table/view from oracle, the label is not correct
        String label = getLabel();
        if (isUntilSchema()) {
            label = getObject().getLabel();
        } else {
            if (null != this.getObject() && this.getObject().isDeleted()) {
                label = this.getProperties(EProperties.LABEL) == null ? PluginConstant.EMPTY_STRING : this.getProperties(
                        EProperties.LABEL).toString();
            }
        }
        if (label.toLowerCase().contains(getFilterStr())) {
            if (!isUntilSchema()) {
                return true;
            } else {
                returnVal = true;
            }
        }
        // ~23161
        DQRepositoryNode childNode = null;
        List<IRepositoryNode> nodes = getChildren();
        for (IRepositoryNode child : nodes) {
            childNode = (DQRepositoryNode) child;
            if (isUntilSchema()) {
                if (childNode instanceof DBTableFolderRepNode || childNode instanceof DBViewFolderRepNode) {
                    return returnVal ? true : false;
                }
                if (childNode instanceof DBTableRepNode || childNode instanceof DBViewRepNode
                        || childNode instanceof DFTableRepNode) {
                    continue;
                }
            }
            if (isUntilTable()) {
                if (childNode instanceof DBColumnFolderRepNode) {
                    continue;
                }
            }
            if (childNode.canMatch()) {
                return true;
            }
        }
        return false;
    }

    /**
     * ADD gdbu 2011-7-26 bug : 23220
     * 
     * DOC gdbu Comment method "filterRecycleBin". This method just used to filter Recycle Bin node .
     * 
     * @param children
     * @return
     */
    public List<IRepositoryNode> filterRecycleBin(List<IRepositoryNode> children) {
        if (!isOnFilterring()) {
            return children;
        }
        if (isReturnAllNodesWhenFiltering()) {
            return children;
        }
        List<IRepositoryNode> sortChildren = RepositoryNodeHelper.sortChildren(children);
        List<IRepositoryNode> filteredChildren = new ArrayList<IRepositoryNode>();
        if (!getFilterStr().equals(PluginConstant.EMPTY_STRING)) {
            DQRepositoryNode dqRepNode = null;
            for (IRepositoryNode dqNode : sortChildren) {
                dqRepNode = (DQRepositoryNode) dqNode;
                if (dqRepNode.canMatchRecycleBin()) {
                    filteredChildren.add(dqRepNode);
                }
            }
        }
        return filteredChildren;
    }

    private boolean canMatchRecycleBin() {
        boolean returnVal = false;
        String label = getLabel();
        if (null != this.getObject() && this.getObject().isDeleted()) {
            label = this.getProperties(EProperties.LABEL) == null ? PluginConstant.EMPTY_STRING : this.getProperties(
                    EProperties.LABEL).toString();
        }
        if (label.toLowerCase().contains(getFilterStr())) {
            returnVal = true;
        }
        List<IRepositoryNode> children = getChildren();
        boolean matchRecycleBin = false;
        DQRepositoryNode childNode = null;
        List<IRepositoryNode> filteredChildren = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode child : children) {
            childNode = (DQRepositoryNode) child;
            if (childNode.canMatchRecycleBin()) {
                matchRecycleBin = true;
            } else {
                filteredChildren.add(child);
            }
        }
        this.getChildren().removeAll(filteredChildren);
        return returnVal || matchRecycleBin;
    }

    public static String getFilterStr() {
        return treeFilter.trim().toLowerCase();
    }

    public static void setFilterStr(String filterStrInput) {
        treeFilter = filterStrInput;
    }

    public static boolean isReturnAllNodesWhenFiltering() {
        return isReturnAllNodesWhenFiltering;
    }

    /**
     * DOC gdbu Comment method "setExpandNodeWhenFiltering".
     * 
     * @param isExpandNodeWhenFiltering
     */
    public static void setIsReturnAllNodesWhenFiltering(boolean isExpandNodeWhenFiltering) {
        DQRepositoryNode.isReturnAllNodesWhenFiltering = isExpandNodeWhenFiltering;
    }

    public static boolean isOnDisplayNextOrPreviousNode() {
        return isOnDisplayNextOrPreviousNode;
    }

    public static void setOnDisplayNextOrPreviousNode(boolean isOnDisplayNextOrPreviousNode) {
        DQRepositoryNode.isOnDisplayNextOrPreviousNode = isOnDisplayNextOrPreviousNode;
    }

    /**
     * get Parent viewObject
     * 
     * @return
     */
    protected IRepositoryViewObject getParentViewObject() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        IRepositoryViewObject object = this.getObject();
        if (object != null) {
            return object.getLabel();
        }
        return super.getLabel();
    }

    /**
     * Getter for project.
     * 
     * @return the project
     */
    public org.talend.core.model.general.Project getProject() {
        if (project == null) {
            return ProjectManager.getInstance().getCurrentProject();
        }
        return project;
    }

    public String getDisplayTextWithProjectName() {
        if (!this.getProject().isMainProject()) {
            return getDisplayText() + getDisplayProjectName();
        }
        return getDisplayText();
    }

    public String getDisplayProjectName() {
        return "(@" + this.getProject().getLabel() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ 
    }

    @Override
    public IImage getIcon() {
        if (getParent() != null && getParent() instanceof RecycleBinRepNode
                && ERepositoryObjectType.FOLDER.equals(this.getObjectType())) {
            return ECoreImage.FOLDER_CLOSE_ICON;
        }

        IImage iImage = super.getIcon();
        if (iImage != null) {
            return iImage;
        }
        return null;
    }

    public Image getImage() {
        return null;
    }
}
