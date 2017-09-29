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
package org.talend.dq.analysis.category;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.talend.dataquality.analysis.category.AnalysisCategories;
import org.talend.dataquality.analysis.category.AnalysisCategory;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class CategoryHandlerTest {

    /**
     * Test method for {@link org.talend.dq.analysis.category.CategoryHandler#getAnalysisCategories()}.
     */
    @Test
    public void testGetAnalysisCategories() {
        AnalysisCategories analysisCategories = CategoryHandler.getAnalysisCategories();
        Assert.assertNotNull(analysisCategories);
        EList<AnalysisCategory> categories = analysisCategories.getCategories();
        Assert.assertNotNull(categories);
        Assert.assertFalse(categories.isEmpty());
        for (AnalysisCategory analysisCategory : categories) {
            System.out.println("-" + analysisCategory.getLabel()); //$NON-NLS-1$
            if (!analysisCategory.getSubCategories().isEmpty()) {
                for (AnalysisCategory subCategory : analysisCategory.getSubCategories()) {
                    System.out.println("\t+--" + subCategory.getLabel()); //$NON-NLS-1$
                }
            }
        }
    }

}
