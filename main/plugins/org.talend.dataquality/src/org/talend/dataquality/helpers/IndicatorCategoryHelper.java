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
package org.talend.dataquality.helpers;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class IndicatorCategoryHelper {

    private IndicatorCategoryHelper() {
    }

    public static final String USER_DEFINED_COUNT_CATEGORY = "_b5F7QHqTEd67hM2eKD3QgQ";

    public static final String USER_DEFINED_FREQUENCY_CATEGORY = "_Frd2gHsXEd63r-VLO3_0OQ";

    public static final String USER_DEFINED_MATCH_CATEGORY = "_Ba7OYXsXEd63r-VLO3_0OQ";

    public static final String USER_DEFINED_REAL_VALUE_CATEGORY = "_wxkncJ6tEd6t2KtWPIcDmg";

    public static final String USER_DEFINED_COMPARISON_CATEGORY = "_yQJQ0HsXEd63r-VLO3_0OQ";

    public static final String USER_DEFINED_NOMINAL_CORRELATION_CATEGORY = "_1mapEHsXEd63r-VLO3_0OQ";

    public static final String USER_DEFINED_INTERVAL_CORRELATION_CATEGORY = "_6giZsHsXEd63r-VLO3_0OQ";

    public static final String USER_DEFINED_TIME_CORRELATION_CATEGORY = "_-fpTkHsXEd63r-VLO3_0OQ";

    public static final String SYSTEM_CORRELATION = "_-DfJgLrcEd2PGrJOyhNk-w";

    public static final String SYSTEM_PHONENUMBER = "_Ohyz4bEEEeCVE-ofo1XCug";

    public static final String SYSTEM_ADVANCED_STATISTICS_CATEGORY = "_ccKuIRF2Ed2PKb6nEJEvhw";

    public static final String SYSTEM_SOUNDEX_CATEGORY = "_entEUPbCEd298st2mPm9SA";

    public static final String SYSTEM_PATTERN_FREQUENCY_STATISTICS_CATEGORY = "_0n3BsJR-Ed2XO-JvLwVAwg";

    public static final String SYSTEM_FRAND_DETECTION_CATEGORY = "_2aGLUOzJEeG0fbygDv6UrQ";

    /**
     * if the IndicatorCategory is one kind of User Defined Category return true, else return false. If
     * indicatorCategory is null return false.
     * 
     * @param indicatorCategory
     * @return
     */
    public static final boolean isUserDefCategory(IndicatorCategory indicatorCategory) {
        return isUserDefCount(indicatorCategory) || isUserDefFrequency(indicatorCategory) || isUserDefMatching(indicatorCategory)
                || isUserDefRealValue(indicatorCategory);
    }

    public static final boolean isUserDefCount(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return USER_DEFINED_COUNT_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static final boolean isUserDefFrequency(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return USER_DEFINED_FREQUENCY_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static final boolean isUserDefMatching(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return USER_DEFINED_MATCH_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static final boolean isUserDefRealValue(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return USER_DEFINED_REAL_VALUE_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static final boolean isCorrelation(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return SYSTEM_CORRELATION.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static IndicatorCategory getCategory(IndicatorDefinition indicatorDefinition) {
        if (indicatorDefinition != null) {
            EList<IndicatorCategory> categories = indicatorDefinition.getCategories();
            if (categories != null && categories.size() > 0) {
                return categories.get(0);
            }
        }
        return null;
    }

    /**
     * set the IndicatorCategory for the IndicatorDefinition.
     * 
     * @param indicatorDefinition
     * @param indicatorCategory
     */
    public static void setCategory(IndicatorDefinition indicatorDefinition, IndicatorCategory indicatorCategory) {
        if (indicatorDefinition != null && indicatorCategory != null) {
            EList<IndicatorCategory> categories = indicatorDefinition.getCategories();
            categories.clear();
            categories.add(indicatorCategory);
        }
    }

    public static IndicatorCategory getUDICategory(Indicator indicator) {
        if (indicator != null) {
            return getCategory(indicator.getIndicatorDefinition());
        }
        return null;
    }

    /**
     * 
     * DOC qiongli Comment method "isPhoneNumberCategory".
     * 
     * @param indicatorCategory
     * @return
     */
    public static final boolean isPhoneNumberCategory(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return SYSTEM_PHONENUMBER.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }

    public static final boolean isFrequencyCategory(IndicatorCategory indicatorCategory) {
        if (indicatorCategory != null) {
            return SYSTEM_ADVANCED_STATISTICS_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory))
                    || SYSTEM_FRAND_DETECTION_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory))
                    || SYSTEM_PATTERN_FREQUENCY_STATISTICS_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory))
                    || SYSTEM_SOUNDEX_CATEGORY.equals(ResourceHelper.getUUID(indicatorCategory));
        }
        return false;
    }
}
