// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.eclipse.swt.graphics.Image;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * The label provider of Particular DefaultSurvivorship
 */
public class ParticularDefaultSurvivorshipLabelProvider extends DefaultSurvivorshipLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ParticularDefaultSurvivorshipDefinitions) {
            ParticularDefaultSurvivorshipDefinitions pdskd = (ParticularDefaultSurvivorshipDefinitions) element;
            switch (columnIndex) {
            case 0:
                return pdskd.getColumn();
            case 1:
                return SurvivorShipAlgorithmEnum.getTypeBySavedValue(pdskd.getFunction().getAlgorithmType()).getValue();
            case 2:
                return pdskd.getFunction().getAlgorithmParameters();
            }
        }

        return ""; //$NON-NLS-1$
    }
}
