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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 *
 */
public class SurvivorshipLabelProvider extends LabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {

        if (element instanceof SurvivorshipKeyDefinition) {
            SurvivorshipKeyDefinition skd = (SurvivorshipKeyDefinition) element;
            switch (columnIndex) {
            case 0:
                return skd.getName();
            case 1:
                return skd.getColumn();
            case 2:
                return SurvivorShipAlgorithmEnum.getTypeBySavedValue(skd.getFunction().getAlgorithmType()).getValue();
            case 3:
                return skd.isAllowManualResolution() ? "True" : "False"; //$NON-NLS-1$//$NON-NLS-2$
            }
        }

        return ""; //$NON-NLS-1$
    }

}
