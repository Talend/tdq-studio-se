// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.definition;

import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.DefaultSurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.DefaultSurvivorShipDefinitionTableViewer;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;

public class DefaultSurvivorshipDefinitionTableComposite extends DefaultSurvivorshipTableComposite {

    /**
     * @param parent
     * @param style
     */
    public DefaultSurvivorshipDefinitionTableComposite(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected AbstractMatchAnalysisTableViewer<DefaultSurvivorshipDefinition> createTableViewer() {
        return new DefaultSurvivorShipDefinitionTableViewer(this, getTableStyle());
    }

}
