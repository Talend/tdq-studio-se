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
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class RuleFolderFliter extends ViewerFilter {

    private boolean isShowFile;

    public RuleFolderFliter() {
        this(false);
    }

    public RuleFolderFliter(boolean isShowFile) {
        this.isShowFile = isShowFile;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        // MOD klliu bug TDQ-3202 filter Parser Rule folder and item.
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            if (FactoriesUtil.DQRULE.equals(file.getFileExtension())) {
                // add support for match rule
                ModelElement modelElement = DQRuleResourceFileHelper.getInstance().getModelElement(file);
                if (modelElement == null) {
                    return false;
                }
                ModelElement rule = DQRuleResourceFileHelper.getInstance().doSwitch(modelElement);
                if (rule != null && rule instanceof MatchRuleDefinition) {
                    return true;
                }// ~
                WhereRule findWhereRule = DQRuleResourceFileHelper.getInstance().findWhereRule(file);
                if (findWhereRule == null) {
                    return false;
                }
                return true;
            }
        } else if (element instanceof IFolder) {
            IFolder folder = (IFolder) element;
            boolean subFolder = ResourceService.isSubFolder(ResourceManager.getRulesFolder(), folder);
            if (subFolder) {
                return !folder.getFullPath().equals(ResourceManager.getRulesParserFolder().getFullPath());
            }
            return subFolder;
        }
        // ~
        return isShowFile;
    }

}
