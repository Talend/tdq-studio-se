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
package org.talend.dataprofiler.core.pattern;

import java.util.regex.Matcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by talend on Mar 27, 2015 Detailled comment
 *
 */
public class USStateCodesPatternRegexTest {

    static private String regexp = PluginConstant.EMPTY_STRING;

    String[] stateNames = new String[] { "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
            "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
            "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
            "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
            "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas",
            "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming" };

    String[] stateAbbreviationNames = new String[] { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID",
            "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM",
            "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY" };

    String[] territoryNames = new String[] { "American Samoa", "District of Columbia", "Federated States of Micronesia", "Guam",
            "Marshall Islands", "Northern Mariana Islands", "Palau", "Puerto Rico", "Virgin Islands" };

    String[] territoryAbbreviationNames = new String[] { "AS", "DC", "FM", "GU", "MH", "MP", "PW", "PR", "VI" };

    String[] MilitaryNames = new String[] { "Armed Forces Africa", "Armed Forces Americas", "Armed Forces Canada",
            "Armed Forces Europe", "Armed Forces Middle East", "Armed Forces Pacific" };

    String[] MilitaryAbbreviationNames = new String[] { "AE", "AA", "AE", "AE", "AE", "AP" };

    String[] otherAbbreviationNames = new String[] { "CCTV", "ICBC", "UFO", "DOS", "DVD" };

    String[] otherNames = new String[] { "China Central Television", "CHINA CONSTUCTION BANK", "Unidentified Flying Object",
            "Disk Operating System", "Digital Versatile Disc" };

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DQStructureManager.getInstance().createDQStructure();
        IFolder patternRegexFolder = ResourceManager.getPatternRegexFolder();
        IFolder folder = patternRegexFolder.getFolder("address"); //$NON-NLS-1$
        IFile file = folder.getFile("US_State_Codes_0.1.pattern"); //$NON-NLS-1$
        Assert.assertTrue(file.exists());
        ModelElement modelElement = PatternResourceFileHelper.getInstance().getModelElement(file);
        if (Pattern.class.isInstance(modelElement)) {
            regexp = DomainHelper.getRegexp((Pattern) modelElement, DbmsLanguage.SQL);
            if (regexp.charAt(0) == '\'') {
                regexp = regexp.substring(1);
            }
            if (regexp.charAt(regexp.length() - 1) == '\'') {
                regexp = regexp.substring(0, regexp.length() - 2);
            }
        }
    }

    @Test
    public void testNormalCase() {
        java.util.regex.Pattern AbbreviationPattern = java.util.regex.Pattern.compile(regexp);
        for (int index = 0; index < stateAbbreviationNames.length; index++) {
            String abbreviation = stateAbbreviationNames[index];
            Matcher matcher = AbbreviationPattern.matcher(abbreviation);
            Assert.assertTrue("The abbreviation " + abbreviation + " can not be match which full name is " + stateNames[index],
                    matcher.find());

        }
    }

    @Test
    public void testAbnormalCase() {
        // territory
        java.util.regex.Pattern AbbreviationPattern = java.util.regex.Pattern.compile(regexp);
        for (int index = 0; index < territoryAbbreviationNames.length; index++) {
            String abbreviation = territoryAbbreviationNames[index];
            Matcher matcher = AbbreviationPattern.matcher(abbreviation);
            Assert.assertFalse("The abbreviation " + abbreviation + " should not be match which full name is "
                    + territoryNames[index], matcher.find());

        }
        // Military
        for (int index = 0; index < MilitaryAbbreviationNames.length; index++) {
            String abbreviation = MilitaryAbbreviationNames[index];
            Matcher matcher = AbbreviationPattern.matcher(abbreviation);
            Assert.assertFalse("The abbreviation " + abbreviation + " should not be match which full name is "
                    + MilitaryNames[index], matcher.find());

        }
        // others
        for (int index = 0; index < otherAbbreviationNames.length; index++) {
            String abbreviation = otherAbbreviationNames[index];
            Matcher matcher = AbbreviationPattern.matcher(abbreviation);
            Assert.assertFalse("The abbreviation " + abbreviation + " should not be match which full name is "
                    + otherNames[index], matcher.find());

        }
    }
}
