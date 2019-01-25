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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element != null && element instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) element;
            if (node.getType().equals(ENodeType.SYSTEM_FOLDER)) {
                return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
            } else if (node.getType().equals(ENodeType.SIMPLE_FOLDER)) {
                return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
            } else if (node.getType().equals(ENodeType.REPOSITORY_ELEMENT)) {
                return ImageLib.getImage(ImageLib.DQ_RULE);
            }
        }
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if (element != null && element instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) element;
            return RepositoryNodeHelper.getDisplayLabel(node);
        }
        String text = super.getText(element);
        return PluginConstant.EMPTY_STRING.equals(text) ? DefaultMessagesImpl.getString("DQRepositoryViewLabelProvider.noName") : text; //$NON-NLS-1$
    }
}
