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
package org.talend.dataprofiler.chart.util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

/**
 * created by talend on Dec 26, 2012 Detailled comment use this class instead of ChartComposite will avoid "no more
 * handle" in useful
 * 
 */
public class TalendChartComposite extends ChartComposite {

    private Menu popupMenu;

    /**
     * DOC talend TalendChartComposite constructor comment.
     * 
     * @param comp
     * @param style
     * @param chart
     * @param useBuffer
     */
    public TalendChartComposite(Composite comp, int style, JFreeChart chart, boolean useBuffer) {
        super(comp, style, chart, useBuffer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.experimental.chart.swt.ChartComposite#createPopupMenu(boolean, boolean, boolean, boolean)
     */
    @Override
    protected Menu createPopupMenu(boolean properties, boolean save, boolean print, boolean zoom) {
        popupMenu = super.createPopupMenu(properties, save, print, zoom);
        return popupMenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.experimental.chart.swt.ChartComposite#dispose()
     * 
     * Override this dispose is because of ChartComposite don't dispose some default menuItem. To avoid no more handle
     */
    @Override
    public void dispose() {
        super.dispose();
        if (popupMenu == null || popupMenu.isDisposed()) {
            return;
        }
        for (MenuItem item : popupMenu.getItems()) {
            item.removeSelectionListener(this);
            item.dispose();
        }
        popupMenu.dispose();
    }

}
