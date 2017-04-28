// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.management.i18n;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author scorreia
 * 
 * This class must be run as a plugin junit. In the launch configuration, remove the .nl in order to test with the
 * messages.properties file and not with a message_XX.properties file.
 */
public class MessagesTest {

    private static final String ARG = "salut"; //$NON-NLS-1$

    private static String[][] keyToExpected = {
            { "DefinitionHandler.IndicatorsDefinition", "Indicators' definition not loaded!" }, //$NON-NLS-1$//$NON-NLS-2$
            { "AbstractTableBuilder.NoTypeGiven", "No type given. Type must be set to either tables, views, system tables..." }, //$NON-NLS-1$//$NON-NLS-2$
            { "EIndicatorChartType.PatternFrequencyStatistics", "Pattern Frequency" }, //$NON-NLS-1$//$NON-NLS-2$
            { "Evaluator.NoConnectionFoundInMetadata", "DB Connection \"{0}\" is not available in Metadata." }, //$NON-NLS-1$//$NON-NLS-2$
            { "JavaSqlFactory.UnableDecryptPassword", //$NON-NLS-1$
                    "Unable to decrypt given password correctly. It's probably due to a change in the passphrase used in encryption." } //$NON-NLS-1$

    };

    private static String[][] keyToExpectedWithArgs = {
            { "Evaluator.NoConnectionFoundInMetadata", "DB Connection \"" + ARG + "\" is not available in Metadata." }, //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
            { "DefinitionHandler.IndicatorsDefinition", "Indicators' definition not loaded!" }, //$NON-NLS-1$//$NON-NLS-2$
            { "AbstractTableBuilder.NoTypeGiven", "No type given. Type must be set to either tables, views, system tables..." }, //$NON-NLS-1$//$NON-NLS-2$
            { "EIndicatorChartType.PatternFrequencyStatistics", "Pattern Frequency" }, //$NON-NLS-1$//$NON-NLS-2$
            { "JavaSqlFactory.UnableDecryptPassword", //$NON-NLS-1$
                    "Unable to decrypt given password correctly. It's probably due to a change in the passphrase used in encryption." } //$NON-NLS-1$

    };

    /**
     * Test method for {@link org.talend.cwm.management.i18n.Messages#getString(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testGetStringString() {
        // for (String[] element : keyToExpected) {
        // String key = element[0];
        // String expected = element[1];
        // String mess = Messages.getString(key);
        // System.out.println(mess);
        //            Assert.assertEquals("Failed to get the correct result for key " + key, expected, mess); //$NON-NLS-1$
        // }
    }

    /**
     * Test method for {@link org.talend.cwm.management.i18n.Messages#getString(java.lang.String, java.lang.Object[])}.
     */
    @Ignore
    @Test
    public void testGetStringStringObjectArray() {
        // for (String[] keyToExpectedWithArg : keyToExpectedWithArgs) {
        //
        // String key = keyToExpectedWithArg[0];
        // String arg = ARG;
        // String expected = keyToExpectedWithArg[1];
        // String mess = Messages.getString(key, arg);
        // System.out.println(mess);
        //            Assert.assertEquals("Failed to get the correct result for key " + key, expected, mess); //$NON-NLS-1$
        // }

    }

}
