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
package org.talend.dataprofiler.core.ui.editor.analysis.renderer;

import java.util.Locale;

import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.INavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphics;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphicsItem;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ColumnSetNavigationPageGraphics extends NavigationPageGraphics {

    /**
     * DOC zshen ColumnSetNavigationPageGraphics constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ColumnSetNavigationPageGraphics(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub
    }

    /**
     * DOC zshen ColumnSetNavigationPageGraphics constructor comment.
     * 
     * @param parent
     * @param style
     * @param configurator
     */
    public ColumnSetNavigationPageGraphics(Composite parent, int style, INavigationPageGraphicsConfigurator configurator) {
        super(parent, style, configurator);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphics#update(int[], int,
     * java.util.Locale)
     */
    @Override
    public void update(int[] pageIndexes, int currentPage, Locale locale) {
        super.update(new int[0], currentPage, locale);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphics#select(org.eclipse.nebula.widgets
     * .pagination.renderers.navigation.graphics.NavigationPageGraphicsItem)
     */
    @Override
    public void select(NavigationPageGraphicsItem pageItem) {
        if (!pageItem.isSeparator()) {
            // item selected is not the item '...'
            redraw();
            getDisplay().update();
        }
        this.handleSelection(pageItem);
    }

}
