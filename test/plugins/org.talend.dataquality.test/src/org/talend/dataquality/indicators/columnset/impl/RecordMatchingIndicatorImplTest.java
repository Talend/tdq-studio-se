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
package org.talend.dataquality.indicators.columnset.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesPackage;

/**
 * created by zhao on Aug 23, 2013 Detailled comment
 * 
 */
public class RecordMatchingIndicatorImplTest {

    RecordMatchingIndicator recordMatchingIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory()
            .createRecordMatchingIndicator();

    @SuppressWarnings("nls")
    private String[] COLUMN_SCHEMAS = new String[] { "id", "name", "city", "email", "street", "GID", "GRP_SIZE", "MASTER",
            "SCORE", "GRP_QUALITY", "ATTRIBUTE_SCORES", "BLOCK_KEY" };

    @SuppressWarnings("nls")
    private String[][] dataToBeMatched = new String[][] {
            { "1", "mzhao", "Beijing", "mzhao@talend.com", "Jianguomen", "1", "3", "TRUE", "1.0", "0.9", "", "" },
            { "2", "zshen", "Beijing", "zshen@talend.com", "Jianguomen", "1", "", "FALSE", "1.0", "", "", "" },
            { "3", "yyin", "Beijing", "yyin@talend.com", "Jianguomenwai", "1", "", "FALSE", "0.9", "", "", "" },
            { "4", "scorreia", "Susene", "scorreia@talend.com", "susence xxx", "2", "1", "TRUE", "1.0", "1.0", "", "" },
            { "5", "john", "Beijing", "john@talend.com", "dongzhimen", "3", "2", "TRUE", "1.0", "0.82", "", "" },
            { "6", "lei", "Beijing", "lei@talend.com", "dangchima", "3", "", "FALSE", "0.6", "", "", "" } };

    /**
     * DOC zhao Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Match key: street
        MatchRuleDefinition definition = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(definition);
        recordMatchingIndicator.setMatchRowSchema(COLUMN_SCHEMAS);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.RecordMatchingIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandleObject() {
        // Confidence threshold 0.85
        recordMatchingIndicator.getBuiltInMatchRuleDefinition().setMatchGroupQualityThreshold(0.85);
        recordMatchingIndicator.reset();
        for (String[] row : dataToBeMatched) {
            recordMatchingIndicator.handle(row);
        }
        // Assert row count
        Assert.assertTrue(6l == recordMatchingIndicator.getCount());
        // Assert Matched records
        Assert.assertTrue(3l == recordMatchingIndicator.getMatchedRecordCount());
        // Assert suspect records
        Assert.assertTrue(2l == recordMatchingIndicator.getSuspectRecordCount());

        // Confidence threshold to 0.8
        recordMatchingIndicator.getBuiltInMatchRuleDefinition().setMatchGroupQualityThreshold(0.8);
        recordMatchingIndicator.reset();
        for (String[] row : dataToBeMatched) {
            recordMatchingIndicator.handle(row);
        }
        // Assert row count
        Assert.assertTrue(6l == recordMatchingIndicator.getCount());
        // Assert Matched records
        Assert.assertTrue(5l == recordMatchingIndicator.getMatchedRecordCount());
        // Assert suspect records
        Assert.assertTrue(0l == recordMatchingIndicator.getSuspectRecordCount());
    }

}
