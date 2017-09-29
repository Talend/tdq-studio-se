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
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractStatisticalViewerProvider extends LabelProvider implements ITableLabelProvider,
        IStructuredContentProvider, ITableColorProvider {

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
            SchemaIndicator indicator = (SchemaIndicator) ((OverviewIndUIElement) element).getOverviewIndicator();
            switch (columnIndex) {
            case 0:
                // for TDQ-8833 vertica database has a catalog and the name of it is PluginConstant.EMPTY_STRING
                ModelElement analyzedElement = indicator.getAnalyzedElement();
                if (analyzedElement.getClass() == orgomg.cwm.resource.relational.impl.CatalogImpl.class) {
                    if (PluginConstant.EMPTY_STRING.equals(analyzedElement.getName())) {
                        return text = org.talend.dataquality.PluginConstant.DEFAULT_STRING;
                    }
                }
                // ~
                text = analyzedElement.getName();
                return text;
            case 1:
                text = PluginConstant.EMPTY_STRING + indicator.getTableRowCount();
                return text;
            default:
                break;
            }
            return getOtherColumnText(columnIndex, indicator);
        }
        return text;
    }

    protected abstract String getOtherColumnText(int columnIndex, SchemaIndicator schemaIndicator);

    public Image getColumnImage(Object element, int columnIndex) {
        // MOD klliu 2011-01-27 15750 todo 31
        if (((OverviewIndUIElement) element).getNode() instanceof DBSchemaRepNode && columnIndex == 0) {
            return CWMImageEnum.Schema.getImg();
        } else if (((OverviewIndUIElement) element).getNode() instanceof DBCatalogRepNode && columnIndex == 0) {
            return CWMImageEnum.Catalog.getImg();
        } else {
            return null;
        }
    }

    // added by hcheng 2009-2-20, for 006270: Highlight elements when #rows = 0
    private final Color zeroRowColor = new Color(null, 249, 139, 121);

    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof OverviewIndUIElement) {
            SchemaIndicator indicator = (SchemaIndicator) ((OverviewIndUIElement) element).getOverviewIndicator();
            if (indicator.getTableRowCount() == 0 && indicator.getViewCount() == 0) {
                return zeroRowColor;
            }
        }
        return null;
    }

    public Color getZeroRowColor() {
        return zeroRowColor;
    }
}
