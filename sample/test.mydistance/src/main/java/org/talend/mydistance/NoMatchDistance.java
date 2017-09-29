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

package org.talend.mydistance;

import org.talend.dataquality.record.linkage.attribute.AbstractAttributeMatcher;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;

/**
 * @author scorreia
 * 
 * Example of Matching distance which returns 1 when two strings don't match and 0 when the strings match.
 */
public class NoMatchDistance extends AbstractAttributeMatcher {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.attribute.IAttributeMatcher#getMatchType()
     */
    @Override
    public AttributeMatcherType getMatchType() {
        // a custom implementation should return this type AttributeMatcherType.custom
        return AttributeMatcherType.CUSTOM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.attribute.IAttributeMatcher#getMatchingWeight(java.lang.String,
     * java.lang.String)
     */
    @Override
    public double getWeight(String arg0, String arg1) {
        // Here goes the custom implementation of the matching distance between the two given strings.
        // the algorithm should return a value between 0 and 1.

        return (arg0.equalsIgnoreCase(arg1)) ? 0 : 1;
    }

}
