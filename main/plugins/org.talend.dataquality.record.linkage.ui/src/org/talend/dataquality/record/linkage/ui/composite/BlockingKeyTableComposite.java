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
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.BlockingKeyTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.BlockKeyDefinition;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public class BlockingKeyTableComposite extends AbsMatchAnalysisTableComposite<BlockKeyDefinition> {

    /**
     * DOC zshen BlockingKeyTableComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public BlockingKeyTableComposite(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite#initHeaders()
     */
    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.BLOCKING_KEY_NAME); // 14
        headers.add(MatchAnalysisConstant.PRECOLUMN); // 14
        headers.add(MatchAnalysisConstant.PRE_ALGO); // 12
        headers.add(MatchAnalysisConstant.PRE_VALUE); // 20
        headers.add(MatchAnalysisConstant.KEY_ALGO); // 17
        headers.add(MatchAnalysisConstant.KEY_VALUE); // 11
        headers.add(MatchAnalysisConstant.POST_ALGO); // 11
        headers.add(MatchAnalysisConstant.POST_VALUE); // 11

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite#createTable()
     */
    @Override
    protected void createTable() {

        tableViewer = createTableViewer();
        tableViewer.addPropertyChangeListener(this);
        ((BlockingKeyTableViewer) tableViewer).initTable(headers, columnList, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer<BlockKeyDefinition> createTableViewer() {
        return new BlockingKeyTableViewer(this, getTableStyle(), isAddColumn());
    }

}
