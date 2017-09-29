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
package org.talend.dataprofiler.core.ui.action.provider;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.service.AbstractSvnRepositoryService;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.ui.exchange.ExchangeCategoryRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeComponentRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeFolderRepNode;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnFolderRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode.ReportSubFolderType;
import org.talend.dq.nodes.foldernode.IConnectionElementSubFolder;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * DOC mzhao Abstract common action provider.
 */
public class AbstractCommonActionProvider extends CommonActionProvider {

    protected static final String NEW_MENU_NAME = "column.analysis.menu"; //$NON-NLS-1$

    public boolean isShowMenu() {
        Object object = getContextObject();
        // TDQ-12622 msjian : when there is nothing selected, no need to check show menu
        if (object == null) {
            return false;
        }

        if (!isShowMenuWhenIsReadonlyUser()) {
            return false;
        }

        // hidden all the menus for all reference project node
        if (object instanceof DQRepositoryNode) {
            DQRepositoryNode node = (DQRepositoryNode) object;
            if (!node.getProject().isMainProject()) {
                return false;
            }
        }

        if (!isShowMenuForRefNode()) {
            return false;
        }

        return true;
    }

    /**
     * if the current select is Exchange related node.
     */
    public boolean isExchangeNode() {
        IRepositoryNode firstContextNode = getFirstRepositoryNode();
        return firstContextNode instanceof ExchangeComponentRepNode || firstContextNode instanceof ExchangeCategoryRepNode;
    }

    /**
     * DOC msjian Comment method "isShowMenuForRefNode".
     */
    public boolean isShowMenuForRefNode() {
        IRepositoryNode firstContextNode = getFirstRepositoryNode();
        ERepositoryObjectType contentType = firstContextNode.getContentType();
        if (contentType != null) {
            if (contentType == ERepositoryObjectType.SVN_ROOT || contentType == ERepositoryObjectType.REFERENCED_PROJECTS) {
                return false;
            }
        }

        if (ERepositoryObjectType.REFERENCED_PROJECTS.getLabel().equals(firstContextNode.getProperties(EProperties.LABEL))) {
            return false;
        }

        return true;
    }

    /**
     * DOC msjian Comment method "isShowMenuWhenIsReadonlyUser".
     */
    public boolean isShowMenuWhenIsReadonlyUser() {
        // MOD mzhao user readonly role on svn repository mode.
        AbstractSvnRepositoryService svnReposService = GlobalServiceRegister.getDefault().getSvnRepositoryService(
                AbstractSvnRepositoryService.class);
        if (svnReposService != null && svnReposService.isReadonly() || !isSelectionSameType()) {
            return false;
        }

        return true;
    }

    /**
     * DOC bZhou Comment method "getSubMenuManager".
     * 
     * @param subMenuId
     * @return
     */
    public IMenuManager getSubMenuManager(IMenuManager topMenu, String subMenuId) {
        if (topMenu != null) {
            for (IContributionItem item : topMenu.getItems()) {
                if (StringUtils.equals(item.getId(), subMenuId)) {
                    return (IMenuManager) item;
                }
            }
        }

        return null;
    }

    /**
     * 
     * DOC bzhou Comment method "getContextObject".
     * 
     * @return
     */
    protected Object getContextObject() {
        return ((TreeSelection) this.getContext().getSelection()).getFirstElement();
    }

    /**
     * DOC msjian Comment method "getFirstRepositoryNode".
     * 
     * @return
     */
    protected IRepositoryNode getFirstRepositoryNode() {
        Object object = getContextObject();

        if (object instanceof IRepositoryNode) {
            return (IRepositoryNode) object;
        }

        return null;
    }

    /**
     * 
     * judge if all selections are the same type nodes.
     * 
     * @return
     */
    protected boolean isSelectionSameType() {
        TreeSelection currentSelections = ((TreeSelection) getContext().getSelection());
        Object[] selectionArrays = currentSelections.toArray();
        // TDQ-5186 MOD qiongli.pop the context menus just when these selections are the same type.
        if (selectionArrays.length > 1) {
            RepositoryNode firstNode = null;
            ERepositoryObjectType firstContentType = null;
            for (Object obj : selectionArrays) {
                if (obj instanceof RepositoryNode) {
                    if (firstNode == null) {
                        firstNode = (RepositoryNode) obj;
                        firstContentType = firstNode.getContentType();
                        continue;
                    }
                    RepositoryNode node = (RepositoryNode) obj;
                    ERepositoryObjectType contentType = node.getContentType();
                    // return false when the content type is different or a node is in recycle bin and another is not
                    // in.
                    if ((firstContentType != null && contentType != null && !contentType.getType().equals(
                            firstContentType.getType()))
                            || (RepositoryNodeHelper.isStateDeleted(node) != RepositoryNodeHelper.isStateDeleted(firstNode))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 
     * Get Connection which cotain by selectedNode(DBConnectionRepNode\DBCatalogRepNode\DBSchemaRepNode) If want to
     * support to DelimiteFileConnection node need to make DFColumnFolderRepNode implements IConnectionElementSubFolder
     * interface The case for MDMConnection is same to DelimiteFileConnection.
     * 
     * @param selectedNode
     * @return
     */
    protected Connection getConnection(Object selectedNode) {
        Connection conn = null;
        IRepositoryViewObject object = null;
        if (selectedNode == null) {
            return conn;
        }
        if (selectedNode instanceof RepositoryNode) {
            object = ((RepositoryNode) selectedNode).getObject();
        }
        if (object != null) {
            Property property = object.getProperty();
            if (property == null) {
                return conn;
            }
            Item item = property.getItem();
            if (item != null && item instanceof ConnectionItem) {
                conn = ((ConnectionItem) item).getConnection();
            }
        } else if (selectedNode instanceof IConnectionElementSubFolder) {
            conn = ((IConnectionElementSubFolder) selectedNode).getConnection();
        }
        return conn;
    }

    protected boolean isVirturalNode(RepositoryNode node) {
        return node instanceof DBCatalogRepNode || node instanceof DBSchemaRepNode || node instanceof DBTableFolderRepNode
                || node instanceof DBViewFolderRepNode || node instanceof DBTableRepNode || node instanceof DBViewRepNode
                || node instanceof DBColumnFolderRepNode || node instanceof DBColumnRepNode || node instanceof DFTableRepNode
                || node instanceof DFColumnFolderRepNode || node instanceof DFColumnRepNode
                || node instanceof ExchangeCategoryRepNode || node instanceof ExchangeComponentRepNode
                || node instanceof ExchangeFolderRepNode || isReportSubFolderVirtualNode(node)
                || isAnalysisSubFolderVirtualNode(node) || node instanceof ReportAnalysisRepNode
                || node instanceof ReportFileRepNode;
    }

    protected boolean isReportSubFolderVirtualNode(RepositoryNode node) {
        if (node instanceof ReportSubFolderRepNode) {
            ReportSubFolderRepNode subFolderNode = (ReportSubFolderRepNode) node;
            return ReportSubFolderType.ANALYSIS.equals(subFolderNode.getReportSubFolderType())
                    || ReportSubFolderType.GENERATED_DOCS.equals(subFolderNode.getReportSubFolderType());
        }
        return false;
    }

    protected boolean isAnalysisSubFolderVirtualNode(RepositoryNode node) {
        if (node instanceof AnalysisSubFolderRepNode) {
            AnalysisSubFolderRepNode subFolderNode = (AnalysisSubFolderRepNode) node;
            return subFolderNode.getObject() == null;
        }
        return false;
    }
}
