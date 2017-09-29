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
package org.talend.dataprofiler.chart.preview;

import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;

/**
 * DOC yyi DataChart class global comment. Detailled comment
 * 
 * @deprecated replaced by TalendBarRender
 */
public class CustomRenderer extends BarRenderer {

    private static final long serialVersionUID = -7849007586059270388L;

    private Paint[] colors;

    public CustomRenderer(Paint[] apaint) {
        colors = apaint;
    }

    @Override
    public Paint getItemPaint(int i, int j) {
        CategoryDataset categorydataset = getPlot().getDataset();
        int m = Integer.parseInt(categorydataset.getColumnKeys().get(j).toString());
        return colors[m % colors.length];
    }
}
