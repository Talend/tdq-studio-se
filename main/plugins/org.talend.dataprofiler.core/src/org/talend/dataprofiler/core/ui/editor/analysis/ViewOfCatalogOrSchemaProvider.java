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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.talend.dataprofiler.core.ImageLib.CWMImageEnum;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.indicators.schema.ViewIndicator;

/**
 * The provider for table viewer display.
 */
public class ViewOfCatalogOrSchemaProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider,
        ITableColorProvider {

    @SuppressWarnings("unchecked")
    public Object[] getElements(Object inputElement) {

        if (inputElement == null) {
            return new Object[0];
        }
        Object[] objs = ((List) inputElement).toArray();
        return objs;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof OverviewIndUIElement) {
            ViewIndicator indicatorUIEle = (ViewIndicator) ((OverviewIndUIElement) element).getOverviewIndicator();
            switch (columnIndex) {
            case 0:
                return indicatorUIEle.getTableName();
            case 1:
                return PluginConstant.EMPTY_STRING + indicatorUIEle.getRowCount();
            default:
                break;
            }
        }

        return PluginConstant.EMPTY_STRING;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        if (element instanceof OverviewIndUIElement && columnIndex == 0) {
            return CWMImageEnum.View.getImg();
        }
        return null;

    }

    // added by hcheng 2009-2-20, for 006270: Highlight elements when #rows = 0
    private Color bg = new Color(null, 249, 139, 121);

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof OverviewIndUIElement) {
            ViewIndicator indicator = (ViewIndicator) ((OverviewIndUIElement) element).getOverviewIndicator();
            if (indicator.getRowCount() == 0) {
                return bg;
            }
        }
        return null;
    }

}
