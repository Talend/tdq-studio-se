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
package org.talend.dataquality.factories;

import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;

/**
 * @author scorreia
 * 
 * This factory helps to create PatternMatching indicators.
 */
public final class PatternIndicatorFactory {

    private PatternIndicatorFactory() {
    }

    /**
     * Method "createRegexpMatchingIndicator".
     * 
     * @param pattern the pattern from which the RegexpMatchingIndicator is created
     * @return the new RegexpMatchingIndicator with the same name as the pattern
     */
    public static RegexpMatchingIndicator createRegexpMatchingIndicator(Pattern pattern) {
        RegexpMatchingIndicator patternMatchingIndicator = IndicatorsFactory.eINSTANCE.createRegexpMatchingIndicator();
        IndicatorParameters indicParams = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain validData = DomainFactory.eINSTANCE.createDomain();
        validData.getPatterns().add(pattern);
        indicParams.setDataValidDomain(validData);
        patternMatchingIndicator.setParameters(indicParams);
        patternMatchingIndicator.setName(pattern.getName());
        return patternMatchingIndicator;
    }

    /**
     * Method "createSqlPatternMatchingIndicator".
     * 
     * @param pattern the pattern from which the SqlPatternMatchingIndicator is created
     * @return the new SqlPatternMatchingIndicator with the same name as the pattern
     */
    public static SqlPatternMatchingIndicator createSqlPatternMatchingIndicator(Pattern pattern) {
        SqlPatternMatchingIndicator patternMatchingIndicator = IndicatorsFactory.eINSTANCE.createSqlPatternMatchingIndicator();
        IndicatorParameters indicParams = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain validData = DomainFactory.eINSTANCE.createDomain();
        validData.getPatterns().add(pattern);
        indicParams.setDataValidDomain(validData);
        patternMatchingIndicator.setParameters(indicParams);
        patternMatchingIndicator.setName(pattern.getName());
        return patternMatchingIndicator;
    }
}
