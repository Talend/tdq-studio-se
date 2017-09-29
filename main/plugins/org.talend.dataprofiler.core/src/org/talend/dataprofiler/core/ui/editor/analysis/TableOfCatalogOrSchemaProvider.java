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
import org.talend.dataquality.indicators.schema.TableIndicator;

/**
 * The provider for table viewer display.
 */
public class TableOfCatalogOrSchemaProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider,
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
        String text = PluginConstant.EMPTY_STRING;
        if (element instanceof OverviewIndUIElement) {
            TableIndicator indicator = (TableIndicator) ((OverviewIndUIElement) element).getOverviewIndicator();
            switch (columnIndex) {
            case 0:
                text = indicator.getTableName();
                return text;
            case 1:
                text = PluginConstant.EMPTY_STRING + indicator.getRowCount();
                return text;
            case 2:
                text = indicator.getKeyCount() + PluginConstant.EMPTY_STRING;
                return text;
            case 3:
                text = indicator.getIndexCount() + PluginConstant.EMPTY_STRING;
                return text;
            default:
                break;
            }
        }
        // if (element instanceof TableIndicator) {
        //
        // }

        return text;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        if (((OverviewIndUIElement) element).getOverviewIndicator() instanceof TableIndicator && columnIndex == 0) {
            return CWMImageEnum.Table.getImg();
        }
        return null;
    }

    // added by hcheng 2009-2-20, for 006270: Highlight elements when #rows = 0
    private Color bg = new Color(null, 249, 139, 121);

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof TableIndicator) {
            TableIndicator indicator = (TableIndicator) element;
            if (indicator.getRowCount() == 0) {
                return bg;
            }
        }
        return null;
    }

}
