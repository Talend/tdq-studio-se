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

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

import org.jfree.chart.renderer.category.BarRenderer;

/**
 * Talend bar renderer.
 */
public final class TalendBarRenderer extends BarRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 6652645366987662001L;

    /**
     * use more than one color.
     */
    private boolean rainbow = true;

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    /**
     * The color list using for the bar.
     */
    private List<Color> colors;

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    /**
     * The colorList must contain one color at least.
     * 
     * @param rainbow use more than one color or not
     * @param colorList the color using for the bar
     */
    public TalendBarRenderer(boolean rainbow, List<Color> colorList) {
        setRainbow(rainbow);
        setColors(colorList);
    }

    public Paint getItemPaint(int i, int j) {
        return isRainbow() ? colors.get(j % colors.size()) : colors.get(0);
    }
}
