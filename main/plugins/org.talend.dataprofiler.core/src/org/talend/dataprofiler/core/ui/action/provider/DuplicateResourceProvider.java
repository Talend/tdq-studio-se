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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.ui.action.actions.DuplicateAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class DuplicateResourceProvider extends AbstractCommonActionProvider {

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenuWhenIsReadonlyUser()) {
            return;
        }

        // ADD msjian TDQ-10444: fix get error when click on the exchange node
        if (isExchangeNode()) {
            return;
        }
        // TDQ-10444~

        TreeSelection selection = (TreeSelection) this.getContext().getSelection();
        Object[] objs = selection.toArray();
        if (shouldShowMenu(objs)) {
            List<IRepositoryNode> repositoryNodeList = RepositoryNodeHelper.getRepositoryNodeList(objs);
            DuplicateAction duplicate = new DuplicateAction(repositoryNodeList.toArray(new IRepositoryNode[repositoryNodeList
                    .size()]));
            menu.add(duplicate);
        }
    }

    /**
     * DOC xqliu Comment method "shouldShowMenu".
     * 
     * @param array
     * @return
     */
    private boolean shouldShowMenu(Object[] array) {

        List<ENodeType> nodeTypes = new ArrayList<ENodeType>();
        nodeTypes.add(ENodeType.REPOSITORY_ELEMENT);
        nodeTypes.add(ENodeType.TDQ_REPOSITORY_ELEMENT);

        List<IRepositoryNode> repositoryNodeList = RepositoryNodeHelper.getRepositoryNodeList(array, nodeTypes);
        if (repositoryNodeList.size() == 0) {
            return false;
        }

        List<ERepositoryObjectType> objectTypes = new ArrayList<ERepositoryObjectType>();
        objectTypes.add(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        objectTypes.add(ERepositoryObjectType.TDQ_REPORT_ELEMENT);
        objectTypes.add(ERepositoryObjectType.TDQ_INDICATOR_ELEMENT);
        objectTypes.add(ERepositoryObjectType.TDQ_RULES_SQL);
        objectTypes.add(ERepositoryObjectType.TDQ_RULES);
        objectTypes.add(ERepositoryObjectType.TDQ_RULES_PARSER);
        objectTypes.add(ERepositoryObjectType.TDQ_PATTERN_ELEMENT);
        objectTypes.add(ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
        objectTypes.add(ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT);
        objectTypes.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        objectTypes.add(ERepositoryObjectType.TDQ_RULES_MATCHER);

        for (IRepositoryNode node : repositoryNodeList) {
            // MOD qiongli 2011-2-12.filter elements in recycle bin.
            if (RepositoryNodeHelper.isStateDeleted(node)) {
                return false;
            }
            ERepositoryObjectType contentType = node.getObjectType();
            if (!objectTypes.contains(contentType)) {
                return false;
            }
            RepositoryNode parent = node.getParent();
            if ((parent instanceof ReportSubFolderRepNode) && (node instanceof AnalysisRepNode)) {
                return false;
            }
            if (node instanceof ReportFileRepNode || node instanceof ReportAnalysisRepNode) {
                return false;
            }
        }
        return true;
    }
}
