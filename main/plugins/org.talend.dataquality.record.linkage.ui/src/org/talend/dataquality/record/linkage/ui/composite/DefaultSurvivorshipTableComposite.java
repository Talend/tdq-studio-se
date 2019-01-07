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
package org.talend.dataquality.record.linkage.ui.composite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.DefaultSurvivorShipTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.MatchRuleDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class DefaultSurvivorshipTableComposite extends AbsMatchAnalysisTableComposite<DefaultSurvivorshipDefinition> {

    /**
     * DOC HHB SurvivorshipTableComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DefaultSurvivorshipTableComposite(Composite parent, int style) {
        super(parent, style);
        setAddColumn(false);
    }

    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.DATA_TYPE);
        headers.add(MatchAnalysisConstant.FUNCTION);
        headers.add(MatchAnalysisConstant.REFERENCE_COLUMN);
        headers.add(MatchAnalysisConstant.PARAMETER);
    }

    @Override
    protected void createTable() {
        tableViewer = createTableViewer();
        tableViewer.addPropertyChangeListener(this);
        ((DefaultSurvivorShipTableViewer) tableViewer).initTable(headers, columnList, false);
    }

    /**
     * DOC HHB Comment method "setInput".
     * 
     * @param defaultSurvivorshipDefinitions
     */
    public void setInput(EList<DefaultSurvivorshipDefinition> inputs) {
        tableViewer.setInput(inputs);
    }

    /**
     * DOC HHB Comment method "removeKeyDefinition".
     * 
     * @param next
     * @param matchRuleDef
     */
    public void removeKeyDefinition(DefaultSurvivorshipDefinition next, MatchRuleDefinition matchRuleDef) {
        ((DefaultSurvivorShipTableViewer) tableViewer).removeElement(next, matchRuleDef);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer<DefaultSurvivorshipDefinition> createTableViewer() {
        return new DefaultSurvivorShipTableViewer(this, getTableStyle());
    }

}
