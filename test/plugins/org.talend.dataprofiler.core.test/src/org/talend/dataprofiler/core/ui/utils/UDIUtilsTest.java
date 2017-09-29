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
package org.talend.dataprofiler.core.ui.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UserdefineFactory;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class UDIUtilsTest {

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.UDIUtils#createNewTdExpression(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCreateNewTdExpression() {
        TdExpression tdExpression = UDIUtils.createNewTdExpression("MySQL", "5.0.2", "select * from test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertNotNull(tdExpression);
        assertEquals("5.0.2", tdExpression.getVersion()); //$NON-NLS-1$
        assertEquals("MySQL", tdExpression.getLanguage()); //$NON-NLS-1$
        assertEquals("select * from test", tdExpression.getBody()); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.UDIUtils#isCurrentLanguageAndVersion(org.talend.cwm.relational.TdExpression, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testIsCurrentLanguageAndVersion() {
        TdExpression tdExpression = UDIUtils.createNewTdExpression("MySQL", "5.0.2", "select * from test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(UDIUtils.isCurrentLanguageAndVersion(tdExpression, "MySQL", "5.0.2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(UDIUtils.isCurrentLanguageAndVersion(tdExpression, "MySQL", "5.1.2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(UDIUtils.isCurrentLanguageAndVersion(tdExpression, "Oracle", "5.0.2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(UDIUtils.isCurrentLanguageAndVersion(tdExpression, "Oracle", null)); //$NON-NLS-1$ 
    }

    /**
     * Test method for {@link
     * org.talend.dataprofiler.core.ui.utils.UDIUtils#checkExistInList(EList<org.talend.cwm.relational.TdExpression>,
     * java.lang.String, java.lang.String)} .
     */
    @Test
    public void testCheckExistInList() {
        // create user define indicator
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        UDIndicatorDefinition indicatorDefinition = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName("user define"); //$NON-NLS-1$
        userDefIndicator.setName(indicatorDefinition.getName());
        userDefIndicator.setIndicatorDefinition(indicatorDefinition);

        TdExpression tdExpression = UDIUtils.createNewTdExpression("MySQL", "5.0.2", "select * from test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        indicatorDefinition.getViewRowsExpression().add(tdExpression);

        TdExpression tdExpression_2 = UDIUtils.createNewTdExpression("Oracle", "5.0.2", "select * from test"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        indicatorDefinition.getViewRowsExpression().add(tdExpression_2);

        assertTrue(UDIUtils.checkExistInList(indicatorDefinition.getViewRowsExpression(), "Oracle", "5.0.2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(UDIUtils.checkExistInList(indicatorDefinition.getViewRowsExpression(), "MySQL", "5.1.2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(UDIUtils.checkExistInList(indicatorDefinition.getViewRowsExpression(), "Oracle", null)); //$NON-NLS-1$ 
    }

}
