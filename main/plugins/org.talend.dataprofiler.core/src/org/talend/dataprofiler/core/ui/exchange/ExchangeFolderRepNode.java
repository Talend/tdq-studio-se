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
package org.talend.dataprofiler.core.ui.exchange;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.ecos.jobs.ComponentSearcher;
import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ExchangeFolderRepNode extends DQRepositoryNode {

    /**
     * ExchangeFolderRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public ExchangeFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        Object[] result = null;
        boolean isOnFilter = ExchangeFolderRepNode.isOnFilterring();
        try {
            String version = CorePlugin.getDefault().getProductVersion().toString();
            result = ComponentSearcher.getAvailableCategory(version, isOnFilter).toArray();
        } catch (SocketTimeoutException e) {
            if (!isOnFilter) {
                MessageDialogWithToggle.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        DefaultMessagesImpl.getString("ExchangeFolderRepNode.connectFailedTitle"), //$NON-NLS-1$
                        e.getMessage());
            }
        } catch (Exception e) {
            if (!isOnFilter) {
                MessageDialogWithToggle.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        DefaultMessagesImpl.getString("ExchangeFolderRepNode.connectFailedTitle"), //$NON-NLS-1$
                        DefaultMessagesImpl.getString("ExchangeFolderRepNode.connectFailed2")); //$NON-NLS-1$
            }
        }
        // MOD gdbu 2011-6-29 bug : 22204
        return filterResultsIfAny(buildRepositoryNode(result));
        // ~22204
    }

    /**
     * DOC xqliu Comment method "buildRepositoryNode".
     * 
     * @param result
     * @return
     */
    private List<IRepositoryNode> buildRepositoryNode(Object[] result) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        if (result != null && result.length > 0) {
            if (result[0] instanceof String) {
                for (Object obj : result) {
                    ExchangeCategoryRepNode exchangeCategoryRepNode = new ExchangeCategoryRepNode(null, this,
                            ENodeType.REPOSITORY_ELEMENT, getProject());
                    exchangeCategoryRepNode.setFlag(false);
                    exchangeCategoryRepNode.setMsg((String) obj);
                    list.add(exchangeCategoryRepNode);
                }
            } else if (result[0] instanceof IEcosCategory) {
                for (Object obj : result) {
                    // MOD klliu bug TDQ-3754: Hide the parser rules when in TOP 2011-10-24
                    IEcosCategory category = (IEcosCategory) obj;
                    String name = category.getName();
                    ExchangeCategoryRepNode exchangeCategoryRepNode = new ExchangeCategoryRepNode(category, this,
                            ENodeType.REPOSITORY_ELEMENT, getProject());
                    exchangeCategoryRepNode.setFlag(true);
                    exchangeCategoryRepNode.setMsg("");//$NON-NLS-1$ 
                    if (!(name.equals("ParserRule") && PluginChecker.isOnlyTopLoaded())) {//$NON-NLS-1$ 
                        list.add(exchangeCategoryRepNode);
                    }
                }
            }
        }
        return list;
    }

}
