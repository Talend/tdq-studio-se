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
package org.talend.dq.helper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.impl.IndicatorSqlFactoryImpl;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by talend on Sep 13, 2012 Detailled comment
 * 
 */
public class UDIHelperTest {

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        //
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#getUDICategory(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    @Ignore
    public void testGetUDICategoryIndicatorDefinition() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#getUDICategory(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testGetUDICategoryIndicator() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#setUDICategory(org.talend.dataquality.indicators.definition.IndicatorDefinition, org.talend.dataquality.indicators.definition.IndicatorCategory)}
     * .
     */
    @Test
    @Ignore
    public void testSetUDICategoryIndicatorDefinitionIndicatorCategory() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#setUDICategory(org.talend.dataquality.indicators.definition.IndicatorDefinition, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testSetUDICategoryIndicatorDefinitionString() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#setUDICategory(org.talend.dataquality.indicators.Indicator, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testSetUDICategoryIndicatorString() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#createUDI(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testCreateUDI() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#getAllIndicatorNames(org.eclipse.core.resources.IFolder)}.
     */
    @Test
    @Ignore
    public void testGetAllIndicatorNames() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#getMatchingIndicatorName(org.talend.dataquality.indicators.definition.IndicatorDefinition, org.talend.dataquality.domain.pattern.Pattern)}
     * .
     */
    @Test
    @Ignore
    public void testGetMatchingIndicatorName() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isCount(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testIsCount() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isRealValue(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testIsRealValue() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isFrequency(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testIsFrequency() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isMatching(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testIsMatching() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#isCategory(org.talend.dataquality.indicators.Indicator, org.talend.dataquality.indicators.definition.IndicatorCategory)}
     * .
     */
    @Test
    @Ignore
    public void testIsCategoryIndicatorIndicatorCategory() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#isCategory(org.talend.dataquality.indicators.definition.IndicatorDefinition, org.talend.dataquality.indicators.definition.IndicatorCategory)}
     * .
     */
    @Test
    @Ignore
    public void testIsCategoryIndicatorDefinitionIndicatorCategory() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isUDI(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testIsUDI() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#isUDIValid(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    @Ignore
    public void testIsUDIValid() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#validate(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    @Ignore
    public void testValidate() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#adaptToJavaUDI(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    @Ignore
    public void testAdaptToJavaUDI() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#isJavaUDI(org.talend.dataquality.indicators.Indicator)}.
     */
    @Test
    public void testIsJavaUDI() {

        // prepare data
        IndicatorDefinition javaIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        TaggedValue calssNameTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "calssName"); //$NON-NLS-1$
        TaggedValue jarPathTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "jarPath"); //$NON-NLS-1$
        Indicator javaIndicator = IndicatorSqlFactoryImpl.eINSTANCE.createJavaUserDefIndicator();
        javaIndicator.setIndicatorDefinition(javaIndicatorDefinition);
        javaIndicatorDefinition.getTaggedValue().add(calssNameTaggedValue);
        javaIndicatorDefinition.getTaggedValue().add(jarPathTaggedValue);

        // testMethod
        assert (UDIHelper.isJavaUDI(javaIndicator));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#containsJavaUDI(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testContainsJavaUDI1() {
        // prepare data
        IndicatorDefinition javaIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        TaggedValue calssNameTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "calssName"); //$NON-NLS-1$
        TaggedValue jarPathTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "jarPath"); //$NON-NLS-1$
        javaIndicatorDefinition.getTaggedValue().add(calssNameTaggedValue);
        javaIndicatorDefinition.getTaggedValue().add(jarPathTaggedValue);

        // testMethod
        assertTrue(UDIHelper.containsJavaUDI(javaIndicatorDefinition));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#containsJavaUDI(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testContainsJavaUDI2() {
        // prepare data
        IndicatorDefinition javaIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        TaggedValue calssNameTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "calssName"); //$NON-NLS-1$
        javaIndicatorDefinition.getTaggedValue().add(calssNameTaggedValue);

        // testMethod
        assertFalse(UDIHelper.containsJavaUDI(javaIndicatorDefinition));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#containsJavaUDI(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testContainsJavaUDI3() {
        // prepare data
        IndicatorDefinition javaIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        TaggedValue jarPathTaggedValue = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT, "jarPath"); //$NON-NLS-1$
        javaIndicatorDefinition.getTaggedValue().add(jarPathTaggedValue);

        // testMethod
        assertFalse(UDIHelper.containsJavaUDI(javaIndicatorDefinition));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.UDIHelper#containsJavaUDI(org.talend.dataquality.indicators.definition.IndicatorDefinition)}
     * .
     */
    @Test
    public void testContainsJavaUDI4() {
        // prepare data
        IndicatorDefinition javaIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        // testMethod
        assertFalse(UDIHelper.containsJavaUDI(javaIndicatorDefinition));
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#getLibJarFileList()}.
     */
    @Test
    @Ignore
    public void testGetLibJarFileList() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.UDIHelper#getContainJarFile(java.lang.String)}.
     */
    @Test
    @Ignore
    public void testGetContainJarFile() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

}
