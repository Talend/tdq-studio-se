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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.ui.action.actions.DQDeleteAction;
import org.talend.dataprofiler.core.ui.action.actions.RenameJrxmlFolderAction;
import org.talend.dataprofiler.core.ui.action.actions.RenameTdqFolderAction;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.DBConnectionSubFolderRepNode;
import org.talend.dq.nodes.DFConnectionSubFolderRepNode;
import org.talend.dq.nodes.JrxmlTempSubFolderNode;
import org.talend.dq.nodes.PatternRegexSubFolderRepNode;
import org.talend.dq.nodes.PatternSqlSubFolderRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.RulesParserSubFolderRepNode;
import org.talend.dq.nodes.RulesSQLSubFolderRepNode;
import org.talend.dq.nodes.UserDefIndicatorSubFolderRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC rli class global comment. Detailled comment
 */
public class DeleteResourceProvider extends AbstractCommonActionProvider {

    /**
     * Adds a submenu to the given menu with the name "New Component".
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }

        // ADD msjian TDQ-10444: fix get error when click on the exchange node
        if (isExchangeNode()) {
            return;
        }
        // TDQ-10444~

        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) obj;
            // RepositoryNode parent = node.getParent();
            // if (!(parent instanceof ReportSubFolderRepNode)) {
            if (shouldShowDeleteMenu(node)) {
                // menu.add(new DeleteObjectsAction());
                menu.add(new DQDeleteAction());
                if (shouldShowRenameFolderMenu(node)) {
                    Object type = node.getProperties(EProperties.CONTENT_TYPE);
                    if (ERepositoryObjectType.TDQ_JRAXML_ELEMENT.equals(type)) {
                        menu.add(new RenameJrxmlFolderAction(node));
                    } else {
                        menu.add(new RenameTdqFolderAction(node));
                    }
                }
            }
            // }
        }
    }

    private boolean shouldShowRenameFolderMenu(RepositoryNode node) {
        boolean show = false;
        if (node instanceof AnalysisSubFolderRepNode) {
            AnalysisSubFolderRepNode anaSubFolderNode = (AnalysisSubFolderRepNode) node;
            show = !anaSubFolderNode.isVirtualFolder();
        } else if (node instanceof ReportSubFolderRepNode) {
            ReportSubFolderRepNode repSubFolderNode = (ReportSubFolderRepNode) node;
            show = !repSubFolderNode.isVirtualFolder();
        } else if (node instanceof UserDefIndicatorSubFolderRepNode || node instanceof PatternRegexSubFolderRepNode
                || node instanceof PatternSqlSubFolderRepNode || node instanceof RulesSQLSubFolderRepNode
                || node instanceof RulesParserSubFolderRepNode || node instanceof DBConnectionSubFolderRepNode
                || node instanceof DFConnectionSubFolderRepNode || node instanceof JrxmlTempSubFolderNode) {
            show = true;
        }
        return show;
    }

    private boolean shouldShowDeleteMenu(RepositoryNode node) {
        return (!isSystemFolder(node) && !isVirturalNode(node) && !isSystemIndicator(node) && !node.isBin())
                || (node instanceof ReportFileRepNode);
    }

    private boolean isSystemFolder(RepositoryNode node) {
        return ENodeType.SYSTEM_FOLDER.equals(node.getType());
    }

    private boolean isSystemIndicator(RepositoryNode node) {
        switch (node.getType()) {
        case TDQ_REPOSITORY_ELEMENT:
        case REPOSITORY_ELEMENT:
            if (node.getObject() != null) {
                Item item = node.getObject().getProperty().getItem();
                IFolder folder = WorkbenchUtils.getFolder(node);
                return item instanceof TDQIndicatorDefinitionItem
                        && ResourceService.isSubFolder(ResourceManager.getSystemIndicatorFolder(), folder);
            }
        default:

        }
        return false;
    }

}
