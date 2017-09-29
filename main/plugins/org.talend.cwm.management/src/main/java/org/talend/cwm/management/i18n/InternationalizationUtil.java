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
package org.talend.cwm.management.i18n;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.properties.Property;
import org.talend.dataquality.PluginConstant;

/**
 * get internationalization string util class
 * 
 */

public class InternationalizationUtil {

    /**
     * 
     * get Internationalization Label of SystemIndicatorDefinition
     * 
     * 
     * @param originalName the value of parameter should be lable of property(for "Row_Count.definition"
     * indicatorDefinition it should be "Row_Count")
     * @return if originalName is null we will return EMPTY string else will return internationalization string
     */
    public static String getDefinitionInternationalizationLabel(String originalName) {
        return getInternationalizationLabel(originalName);
    }

    private static String getInternationalizationLabel(String originalName) {
        return originalName == null ? StringUtils.EMPTY : Messages.getString(originalName);
    }

    /**
     * 
     * get Internationalization Label of Category. We must do a replace operation is because of we can only get
     * "Advanced Statistics" from frequency indicatorDefinition but we can not use it to do a interational key.So we
     * replace it from "Advanced Statistics" to "Advanced_Statistics" then do international operation.
     * 
     * It will be used on Category of User Definition Indicator and presistence report file by now.
     * 
     * @param originalName the value of parameter should be lable of category(for
     * "Bin_Low_Frequency_Table_0.1.definition" indicatorDefinition it should be "Advanced Statistics")
     * 
     * @return if originalName is null we will return EMPTY string else will return internationalization string
     */
    public static String getCategoryInternationalizationLabel(String originalName) {
        return getInternationalizationLabel(originalName == null ? null : originalName.replace(PluginConstant.SPACE_STRING,
                PluginConstant.UNDER_LINE));
    }

    /**
     * 
     * get Internationalization Label of SystemIndicatorDefinition
     * 
     * @param property the property of indicatorDefinition
     * @return if property is null or label of property is null we will return EMPTY string else will return
     * internationalization string
     */
    public static String getDefinitionInternationalizationLabel(Property property) {
        if (property == null) {
            return StringUtils.EMPTY;
        }
        String originalName = property.getLabel();
        return getDefinitionInternationalizationLabel(originalName);
    }
}
