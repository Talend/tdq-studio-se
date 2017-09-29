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
package org.talend.dataprofiler.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.dataprofiler.core.model.nodes.analysis.AnalysisTypeNode;
import org.talend.dataquality.analysis.category.AnalysisCategories;
import org.talend.dataquality.analysis.category.AnalysisCategory;
import org.talend.dq.analysis.category.CategoryHandler;

/**
 * DOC zqin class global comment. Detailled comment
 */
public final class ViewerDataFactory {

    private ViewerDataFactory() {
    }

    public static Object createTreeData() {
        List<AnalysisTypeNode> returnList = new ArrayList<AnalysisTypeNode>();

        // use CategoryHandler
        AnalysisCategories analysisCategories = CategoryHandler.getAnalysisCategories();
        // use this tree (use label attribute of each Category instance)
        EList<AnalysisCategory> categories = analysisCategories.getCategories();

        for (AnalysisCategory category : categories) {
            AnalysisTypeNode typeNode = new AnalysisTypeNode(category.getLabel(), category.getAnalysisType().getLiteral(), null);
            if (category.getSubCategories() != null) {
                List<AnalysisTypeNode> subCategories = new ArrayList<AnalysisTypeNode>();
                for (AnalysisCategory subCategory : category.getSubCategories()) {
                    // the Semantic analysis must not appear in TOS DQ.
                    String label = subCategory.getLabel();
                    if (org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
                        if (label.equals("Semantic-aware Analysis")) { //$NON-NLS-1$
                            continue;
                        }
                    }
                    subCategories.add(new AnalysisTypeNode(label, label, typeNode));
                }
                typeNode.setChildren(subCategories.toArray());
            }

            returnList.add(typeNode);
        }

        return returnList;

    }
}
