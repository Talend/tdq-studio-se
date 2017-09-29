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
package org.talend.dataprofiler.chart;

import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;
import org.talend.dataprofiler.chart.util.PluginConstant;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class CustomConceptRenderer extends BarRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 6160268162123402062L;

    private Integer selectedColumn;

    /**
     * DOC qiongli CustomBarRenderer constructor comment.
     */
    public CustomConceptRenderer(Integer currentBar) {
        this.selectedColumn = currentBar;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.chart.renderer.AbstractRenderer#getItemPaint(int, int)
     */
    @Override
    public Paint getItemPaint(int row, int column) {
        // highlight the selected column
        if (selectedColumn != null && column == selectedColumn.intValue()) {
            return PluginConstant.PRIMARY_GREEN_AWT;
        }
        return PluginConstant.PRIMARY_BLUE_AWT;
    }
}
