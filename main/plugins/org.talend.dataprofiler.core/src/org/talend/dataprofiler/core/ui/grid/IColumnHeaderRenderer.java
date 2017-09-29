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
package org.talend.dataprofiler.core.ui.grid;

import org.eclipse.nebula.widgets.grid.IRenderer;

/**
 * The interface for the renderer of Column Header.
 * 
 */
public interface IColumnHeaderRenderer extends IRenderer {

    /**
     * Set the rotation of the header text. Please note that you have to call <code>redraw()</code> on the table
     * yourself if you change the rotation while the table is showing.
     * 
     * @param rotation rotation in degrees anti clockwise between 0 and 90 degrees.
     */
    public void setRotation(int rotation);

}
