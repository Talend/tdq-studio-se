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
package org.talend.resource;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.WhereRule;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class EResourceConstantTest {

    TDQBusinessRuleItem ruleItem;
    /**
     * DOC yyin Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ruleItem = PropertiesFactory.eINSTANCE.createTDQBusinessRuleItem();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.resource.EResourceConstant#getTypedConstant(org.talend.core.model.properties.Item)}. test for
     * parser rule
     */
    @Test
    public void testGetTypedConstantItem_1() {
        ParserRule parseRule = mock(ParserRule.class);
        ruleItem.setDqrule(parseRule);

        EResourceConstant rc = EResourceConstant.getTypedConstant(ruleItem);
        Assert.assertEquals(rc.getPath(), EResourceConstant.RULES_PARSER.getPath());
    }

    /**
     * Test method for
     * {@link org.talend.resource.EResourceConstant#getTypedConstant(org.talend.core.model.properties.Item)}. test for
     * where rule
     */
    @Test
    public void testGetTypedConstantItem_2() {
        WhereRule whereRule = mock(WhereRule.class);
        ruleItem.setDqrule(whereRule);

        EResourceConstant rc = EResourceConstant.getTypedConstant(ruleItem);
        Assert.assertEquals(rc.getPath(), EResourceConstant.RULES_SQL.getPath());
    }

    /**
     * default return is rule_sql
     */
    @Test
    public void testGetTypedConstantItem_3() {
        EResourceConstant rc = EResourceConstant.getTypedConstant(ruleItem);
        Assert.assertEquals(rc.getPath(), EResourceConstant.RULES_SQL.getPath());
    }
}
