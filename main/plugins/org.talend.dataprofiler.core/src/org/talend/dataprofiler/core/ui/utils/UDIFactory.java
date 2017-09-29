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

import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class UDIFactory {

    private UDIFactory() {

    }

    public static Indicator createUserDefIndicator(IndicatorDefinition indicatorDefinition) {
        UserDefIndicator userDefIndicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        IndicatorParameters indicParams = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        userDefIndicator.setParameters(indicParams);
        userDefIndicator.setName(indicatorDefinition.getName());
        return userDefIndicator;
    }

    @Deprecated
    public static Indicator createIndicator(IndicatorDefinition indicatorDefinition) {
        IndicatorCategory category = UDIHelper.getUDICategory(indicatorDefinition);
        Indicator indicator = null;
        if (category != null) {
            if (category.equals(DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory())) {
                indicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
            } else if (category.equals(DefinitionHandler.getInstance().getUserDefinedMatchIndicatorCategory())) {
                indicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
            } else if (category.equals(DefinitionHandler.getInstance().getUserDefinedFrequencyIndicatorCategory())) {
                indicator = IndicatorsFactory.eINSTANCE.createFrequencyIndicator();
            }

            if (indicator != null) {
                IndicatorParameters indicParams = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
                indicator.setParameters(indicParams);
                indicator.setName(indicatorDefinition.getName());
            }
        }
        return indicator;
    }

    /**
     * DOC xqliu Comment method "createUserDefIndicator".
     * 
     * @param indicatorDefinition
     * @param pattern
     * @return
     */
    public static Indicator createUserDefIndicator(IndicatorDefinition indicatorDefinition, Pattern pattern) {
        UserDefIndicator indicator = IndicatorSqlFactory.eINSTANCE.createUserDefIndicator();
        IndicatorParameters indicParams = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain validData = DomainFactory.eINSTANCE.createDomain();
        validData.getPatterns().add(pattern);
        indicParams.setDataValidDomain(validData);
        indicator.setParameters(indicParams);
        indicator.setName(UDIHelper.getMatchingIndicatorName(indicatorDefinition, pattern));
        return indicator;
    }
}
