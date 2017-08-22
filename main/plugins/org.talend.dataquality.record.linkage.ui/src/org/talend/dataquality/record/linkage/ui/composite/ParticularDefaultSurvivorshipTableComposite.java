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
package org.talend.dataquality.record.linkage.ui.composite;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.ParticularDefaultSurvivorShipTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;

/**
 * Particular DefaultSurvivorship Table Composite
 */
public class ParticularDefaultSurvivorshipTableComposite extends
        AbsMatchAnalysisTableComposite<ParticularDefaultSurvivorshipDefinitions> {

    /**
     * Constructor of ParticularDefaultSurvivorship Table Composite
     * 
     * @param parent
     * @param style
     */
    public ParticularDefaultSurvivorshipTableComposite(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.PRECOLUMN);
        headers.add(MatchAnalysisConstant.FUNCTION);
        headers.add(MatchAnalysisConstant.PARAMETER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer<ParticularDefaultSurvivorshipDefinitions> createTableViewer() {
        return new ParticularDefaultSurvivorShipTableViewer(this, getTableStyle());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.DefaultSurvivorshipTableComposite#setInput(org.eclipse.emf.common.util
     * .EList)
     */
    public void setTableViewerInput(EList<ParticularDefaultSurvivorshipDefinitions> inputs) {
        tableViewer.setInput(inputs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTable()
     */
    @Override
    protected void createTable() {
        tableViewer = createTableViewer();
        tableViewer.addPropertyChangeListener(this);
        ((ParticularDefaultSurvivorShipTableViewer) tableViewer).initTable(headers, columnList, false);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#removeKeyDefinition(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void removeKeyDefinition(ParticularDefaultSurvivorshipDefinitions keyDef,
            List<ParticularDefaultSurvivorshipDefinitions> keyDefs) {
        ((ParticularDefaultSurvivorShipTableViewer) tableViewer).removeElement(keyDef, keyDefs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#removeKeyDefinition(java.lang.String,
     * java.util.List)
     */
    @Override
    public void removeKeyDefinition(String column, List<ParticularDefaultSurvivorshipDefinitions> keyDefs) {
        ((ParticularDefaultSurvivorShipTableViewer) tableViewer).removeElement(column, keyDefs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#moveDownKeyDefinition(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void moveDownKeyDefinition(ParticularDefaultSurvivorshipDefinitions keyDef,
            List<ParticularDefaultSurvivorshipDefinitions> keyDefs) {
        super.moveDownKeyDefinition(keyDef, keyDefs);
    }

}
