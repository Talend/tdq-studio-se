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
import org.talend.dataquality.record.linkage.ui.composite.BlockingKeyTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.BlockingKeyDefinitionTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;


/**
 * created by zshen on Aug 26, 2013
 * Detailled comment
 *
 */
public class BlockingKeyTableDefinitionComposite extends BlockingKeyTableComposite {

    /**
     * DOC zshen BlockingKeyTableDefinitionComposite constructor comment.
     *
     * @param parent
     * @param style
     */
    public BlockingKeyTableDefinitionComposite(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataquality.record.linkage.ui.composite.BlockingKeyTableComposite#initHeaders()
     */
    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.BLOCKING_KEY_NAME); // 14
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
     * @see org.talend.dataquality.record.linkage.ui.composite.BlockingKeyTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer createTableViewer() {
        return new BlockingKeyDefinitionTableViewer(this, getTableStyle(), isAddColumn());
    }

}
