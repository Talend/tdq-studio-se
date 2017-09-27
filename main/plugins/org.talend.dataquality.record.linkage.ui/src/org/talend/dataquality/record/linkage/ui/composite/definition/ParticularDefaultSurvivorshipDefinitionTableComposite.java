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
package org.talend.dataquality.record.linkage.ui.composite.definition;

import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.ParticularDefaultSurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.ParticularDefaultSurvivorShipDefintionTableViewer;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * The table composite of Particular Default Survivorship
 */
public class ParticularDefaultSurvivorshipDefinitionTableComposite extends ParticularDefaultSurvivorshipTableComposite {

    /**
     * The constructor of ParticularDefaultSurvivorshipDefinitionTableComposite.
     * 
     * @param parent
     * @param style
     */
    public ParticularDefaultSurvivorshipDefinitionTableComposite(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.ParticularDefaultSurvivorshipTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer<ParticularDefaultSurvivorshipDefinitions> createTableViewer() {
        return new ParticularDefaultSurvivorShipDefintionTableViewer(this, getTableStyle());
    }

}
