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
package org.talend.dataprofiler.core.ui.editor.preview.model;

import org.eclipse.swt.graphics.Image;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dq.analysis.explore.DataExplorer;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class MenuItemEntity {

    private String label;

    private Image icon;

    private String query;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getIcon() {
        return icon == null ? ImageLib.getImage(ImageLib.EXPLORE_IMAGE) : icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public MenuItemEntity(String label, Image icon, String query) {
        this.label = label;
        this.icon = icon;
        this.query = query;
    }

    /**
     * 
     * it is only used to display on UI(like as drill down menu).
     * 
     * @return
     */
    public String geti18nLabel() {
        if (DataExplorer.MENU_VIEW_VALUES.equals(label)) {
            return Messages.getString("DataExplorer.ViewValues"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_ROWS.equals(label)) {
            return Messages.getString("DataExplorer.ViewRows"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_INVALID_ROWS.equals(label)) {
            return Messages.getString("DataExplorer.ViewInvalidRows"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_VALID_ROWS.equals(label)) {
            return Messages.getString("DataExplorer.ViewValidRows"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_VALID_VALUES.equals(label)) {
            return Messages.getString("DataExplorer.ViewValidValues"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_DETAILED_VALID_VALUES.equals(label)) {
            return Messages.getString("DataExplorer.ViewDetailedValidValues"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_DETAILED_INVALID_VALUES.equals(label)) {
            return Messages.getString("DataExplorer.ViewDetailedInvalidValues"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_INVALID_VALUES.equals(label)) {
            return Messages.getString("DataExplorer.ViewInvalidValues"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_MATCH_ROWS.equals(label)) {
            return Messages.getString("DataExplorer.ViewMatchRows"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_VIEW_NOT_MATCH_ROWS.equals(label)) {
            return Messages.getString("DataExplorer.ViewNotMatchRows"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_ROWS_IN_RANGE.equals(label)) {
            return Messages.getString("DataExplorer.RowsInRange"); //$NON-NLS-1$
        }
        if (DataExplorer.MENU_ROWS_OUTSIDE_RANGE.equals(label)) {
            return Messages.getString("DataExplorer.RowsOutsideRange"); //$NON-NLS-1$
        }

        return PluginConstant.EMPTY_STRING;
    }
}
