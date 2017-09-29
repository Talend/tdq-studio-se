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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern;


import org.junit.Assert;
import org.junit.Test;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;
import org.talend.dataquality.indicators.impl.IndicatorsFactoryImpl;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.indicators.sql.impl.IndicatorSqlFactoryImpl;
import org.talend.dq.indicators.ext.PatternMatchingExt;


/**
 * DOC zshen  class global comment. Detailled comment
 */
public class PatternStatisticeStateUtilTest {


    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticeStateUtil#getUnitValue(org.talend.dataquality.indicators.Indicator, java.lang.Object)}
     * .
     * case1:normal RegexpMatchingIndicator case
     */
    @Test
    public void testGetUnitValueCase1() {
        Object pMatchExt = null;
        RegexpMatchingIndicator regexpMatchingIndicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        regexpMatchingIndicator.setComputed(true);
        regexpMatchingIndicator.setMatchingValueCount(40l);
        regexpMatchingIndicator.setNotMatchingValueCount(50l);
        PatternMatchingExt unitValue = PatternStatisticeStateUtil.getUnitValue(regexpMatchingIndicator, pMatchExt);
        Assert.assertEquals("Matching count value shoud be 40l", 40l, unitValue.getMatchingValueCount()); //$NON-NLS-1$
        Assert.assertEquals("Not matching count value shoud be 50l", 50l, unitValue.getNotMatchingValueCount()); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticeStateUtil#getUnitValue(org.talend.dataquality.indicators.Indicator, java.lang.Object)}
     * .
     * case2:normal UDI case
     */
    @Test
    public void testGetUnitValueCase2() {
        PatternMatchingExt pMatchExt = null;
        UserDefIndicator userDefIndicator = IndicatorSqlFactoryImpl.eINSTANCE.createUserDefIndicator();
        userDefIndicator.setComputed(true);
        userDefIndicator.setMatchingValueCount(40l);
        userDefIndicator.setNotMatchingValueCount(50l);
        PatternMatchingExt unitValue = PatternStatisticeStateUtil.getUnitValue(userDefIndicator, pMatchExt);
        Assert.assertEquals("Matching count value shoud be 40l", 40l, unitValue.getMatchingValueCount()); //$NON-NLS-1$
        Assert.assertEquals("Not matching count value shoud be 50l", 50l, unitValue.getNotMatchingValueCount()); //$NON-NLS-1$
        Assert.assertNotEquals("unitValue should not same with pMatchExt but it is not now", pMatchExt, unitValue); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticeStateUtil#getUnitValue(org.talend.dataquality.indicators.Indicator, java.lang.Object)}
     * .
     * case3:normal SqlPatternMatchingIndicator case
     */
    @Test
    public void testGetUnitValueCase3() {
        PatternMatchingExt pMatchExt = null;
        SqlPatternMatchingIndicator sqlMatchingIndicator = IndicatorsFactoryImpl.eINSTANCE.createSqlPatternMatchingIndicator();
        sqlMatchingIndicator.setComputed(true);
        sqlMatchingIndicator.setMatchingValueCount(40l);
        sqlMatchingIndicator.setNotMatchingValueCount(50l);
        PatternMatchingExt unitValue = PatternStatisticeStateUtil.getUnitValue(sqlMatchingIndicator, pMatchExt);
        Assert.assertEquals("Matching count value shoud be 40l", 40l, unitValue.getMatchingValueCount()); //$NON-NLS-1$
        Assert.assertEquals("Not matching count value shoud be 50l", 50l, unitValue.getNotMatchingValueCount()); //$NON-NLS-1$
        Assert.assertNotEquals("unitValue should not same with pMatchExt but it is not now", pMatchExt, unitValue); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticeStateUtil#getUnitValue(org.talend.dataquality.indicators.Indicator, java.lang.Object)}
     * .
     * case4:Input value is PatternMatchingExt so that return directly
     */
    @Test
    public void testGetUnitValueCase4() {
        PatternMatchingExt pMatchExt = new PatternMatchingExt();
        pMatchExt.setMatchingValueCount(40l);
        pMatchExt.setNotMatchingValueCount(50l);
        RegexpMatchingIndicator regexpMatchingIndicator = IndicatorsFactoryImpl.eINSTANCE.createRegexpMatchingIndicator();
        regexpMatchingIndicator.setComputed(true);
        PatternMatchingExt unitValue = PatternStatisticeStateUtil.getUnitValue(regexpMatchingIndicator, pMatchExt);
        Assert.assertEquals("Matching count value shoud be 40l", 40l, unitValue.getMatchingValueCount()); //$NON-NLS-1$
        Assert.assertEquals("Not matching count value shoud be 50l", 50l, unitValue.getNotMatchingValueCount()); //$NON-NLS-1$
        Assert.assertEquals("unitValue should same with pMatchExt but it is not now", pMatchExt, unitValue); //$NON-NLS-1$
    }

}
