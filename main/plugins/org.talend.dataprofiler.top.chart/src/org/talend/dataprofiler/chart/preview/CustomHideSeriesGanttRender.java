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

import java.awt.Color;
import java.awt.Paint;
import java.util.Map;

/**
 * created by yyin on 2014-12-18 Detailled comment
 * 
 */
public class CustomHideSeriesGanttRender extends HideSeriesGanttRenderer {

    private Map<String, RowColumPair> hightlightSeriesMap;// = new HashMap<String, RowColumPair>();

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CustomHideSeriesGanttRender(Map<String, RowColumPair> hightlightSeriesMap1) {
        hightlightSeriesMap = hightlightSeriesMap1;
    }

    /**
     * 
     * DOC zhaoxinyi CustomHideSeriesGantt constructor comment.
     * 
     * @param colors
     */
    @Override
    public Paint getItemPaint(int row, int column) {
        Paint itemPaint = super.getItemPaint(row, column);
        String key = String.valueOf(row) + String.valueOf(column);
        if (hightlightSeriesMap.get(key) != null && hightlightSeriesMap.get(key).getRow() == row
                && hightlightSeriesMap.get(key).getColumn() == column) {
            return ((Color) itemPaint).brighter().brighter();
        }
        return itemPaint;
    }

}
