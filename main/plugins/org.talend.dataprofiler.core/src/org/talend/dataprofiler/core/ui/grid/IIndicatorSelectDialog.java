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

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dq.nodes.indicator.IIndicatorNode;

/**
 * The interface of indicator select Dialog
 * 
 */
public interface IIndicatorSelectDialog extends IShellProvider {

    /**
     * 
     * Update the Purpose label and description label
     * 
     * @param item which item has been selected
     */
    public void updateIndicatorInfo(GridItem item);

    /**
     * 
     * Get the area of dialog
     * 
     * @return the area of dialog
     */
    public Control getDialogControl();

    /**
     * 
     * Get the root composite of dialog
     * 
     * @return The root composite of dialog
     */
    public Composite getDialogComposite();

    /**
     * 
     * Judge whether currentIndicator can be checked for the column
     * 
     * @param currentIndicator current indicator
     * @param indicatorNode the model of current tree item
     * @return true if current indicator can be checked else return false
     */
    public boolean isMatchCurrentIndicator(ModelElementIndicator currentIndicator, IIndicatorNode indicatorNode);
}
