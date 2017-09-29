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

package org.talend.dataprofiler.core.ui.editor.indicator;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorParametersLabelProvider extends LabelProvider implements
ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		// In case you don't like image just return null here
		
		return null;
	}
	public String getColumnText(Object element, int columnIndex) {
		IndicatorDefinitionParameter para = (IndicatorDefinitionParameter) element;
		switch (columnIndex) {
		case 0:
			return para.getKey();
		case 1:
			return para.getValue();
		default:
            throw new RuntimeException("Should not happen");//$NON-NLS-1$
		}

	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_ELEMENT);
	}
}

