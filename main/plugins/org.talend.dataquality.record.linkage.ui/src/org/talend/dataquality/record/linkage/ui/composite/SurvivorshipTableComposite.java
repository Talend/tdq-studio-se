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
package org.talend.dataquality.record.linkage.ui.composite;

import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.SurvivorShipTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 *
 */
public class SurvivorshipTableComposite extends AbsMatchAnalysisTableComposite<SurvivorshipKeyDefinition> {

    /**
     * DOC HHB SurvivorshipTableComposite constructor comment.
     *
     * @param parent
     * @param style
     */
    public SurvivorshipTableComposite(Composite parent, int style) {
        super(parent, style);
        setAddColumn(false);
    }

    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.SURVIVORSHIP_KEY_NAME);
        headers.add(MatchAnalysisConstant.COLUMN);
        headers.add(MatchAnalysisConstant.FUNCTION);
        headers.add(MatchAnalysisConstant.ALLOW_MANUAL_RESOLUTION);

    }

    @Override
    protected void createTable() {
        tableViewer = createTableViewer();
        tableViewer.addPropertyChangeListener(this);
        ((SurvivorShipTableViewer) tableViewer).initTable(headers);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer<SurvivorshipKeyDefinition> createTableViewer() {
        return new SurvivorShipTableViewer(this, getTableStyle(), isAddColumn());
    }

}
