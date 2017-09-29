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
import org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchRuleDefinitionTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchRule;

/**
 * created by zshen on Aug 26, 2013
 * Detailled comment
 *
 */
public class MatchRuleTableDefinitionComposite extends MatchRuleTableComposite {

    /**
     * DOC zshen MatchRuleTableDefinitionComposite constructor comment.
     *
     * @param parent
     * @param style
     * @param matchRule
     */
    public MatchRuleTableDefinitionComposite(Composite parent, int style, MatchRule matchRule) {
        super(parent, style, matchRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite#initHeaders()
     */
    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.MATCH_KEY_NAME); // 14
        headers.add(MatchAnalysisConstant.MATCHING_TYPE); // 12
        headers.add(MatchAnalysisConstant.CUSTOM_MATCHER); // 20
        headers.add(MatchAnalysisConstant.TOKENIZATION_TYPE); // 20
        if (isAddColumn()) {
            headers.add(MatchAnalysisConstant.THRESHOLD); // 14
        }
        headers.add(MatchAnalysisConstant.CONFIDENCE_WEIGHT); // 17
        headers.add(MatchAnalysisConstant.HANDLE_NULL); // 11
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.MatchRuleTableComposite#createTableViewer()
     */
    @Override
    protected AbstractMatchAnalysisTableViewer createTableViewer() {
        return new MatchRuleDefinitionTableViewer(this, getTableStyle(), isAddColumn());
    }

}
