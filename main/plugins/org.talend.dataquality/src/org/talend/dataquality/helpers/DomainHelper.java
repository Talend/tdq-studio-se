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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.JavaUDIIndicatorParameter;
import org.talend.dataquality.domain.LiteralValue;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.domain.RealNumberValue;
import org.talend.dataquality.domain.TextValue;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.PatternPackage;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.i18n.Messages;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * @author scorreia
 * 
 * Helper class for Domain object.
 */
public final class DomainHelper {

    public static final String SQL_PATTERN_FOLDER = "SQL Patterns"; //$NON-NLS-1$

    public static final String PATTERN_FOLDER = "Patterns"; //$NON-NLS-1$

    private DomainHelper() {
    }

    /**
     * The available pattern types.
     */
    static enum PatternType {
        TABLE_PATTERN("Table Pattern"), //$NON-NLS-1$
        VIEW_PATTERN("View Pattern"), //$NON-NLS-1$
        EXPECTED_VALUE("Expected value"); //$NON-NLS-1$

        final String label;

        PatternType(String lab) {
            this.label = lab;
        }
    }

    /**
     * The available domain types.
     */
    static enum DomainType {
        ANALYSIS_DATA_FILTER("Analysis Data Filter"), //$NON-NLS-1$
        INDICATOR_EXPECTED_VALUE("Indicator expected value"); //$NON-NLS-1$

        String label;

        DomainType(String lab) {
            this.label = lab;
        }
    }

    private static final DomainFactory DOMAIN = DomainFactory.eINSTANCE;

    /**
     * Method "createDomain" creates a new Domain with the given name.
     * 
     * @param name the name of the domain (could be null)
     * @return the new domain.
     */
    public static Domain createDomain(String name) {
        Domain domain = DOMAIN.createDomain();
        domain.setName(name);
        return domain;
    }

    /**
     * Method "getRegexp".
     * 
     * @param pattern a pattern
     * @return the body of the regular expression applicable to this dbms or null
     */
    public static String getSQLRegexp(Pattern pattern) {
        return getRegexp(pattern, "SQL");
    }

    /**
     * DOC yyi Comment method "getJavaRegexp".
     * 
     * @param pattern
     * @return
     */
    public static String getJavaRegexp(Pattern pattern) {
        return getRegexp(pattern, "Java");
    }

    /**
     * DOC bZhou Comment method "getRegexp".
     * 
     * @param pattern
     * @param language
     * @return
     */
    public static String getRegexp(Pattern pattern, String language) {
        EList<PatternComponent> components = pattern.getComponents();
        for (PatternComponent patternComponent : components) {
            if (patternComponent != null) {
                Expression expression = getExpression(patternComponent, language);
                if (expression != null) {
                    return expression.getBody();
                }
            }
        }
        return null;
    }

    /**
     * Method "getExpression".
     * 
     * @param patternComponent
     * @param language a language (see DbmsLanguage)
     * @return the expression for the given language or null
     */
    public static Expression getExpression(PatternComponent patternComponent, String language) {
        if (patternComponent != null && patternComponent.eClass().equals(PatternPackage.eINSTANCE.getRegularExpression())) {
            RegularExpression regExp = (RegularExpression) patternComponent;
            Expression expr = regExp.getExpression();
            if (expr != null && language != null && language.equalsIgnoreCase(regExp.getExpression().getLanguage())) {
                return expr;
            }
        }
        // not a regular expression
        return null;
    }

    /**
     * Method "createIndicatorExpectedValueDomain".
     * 
     * @return a domain dedicated to storing the expected value. The created domain's name is already set.
     */
    public static Domain createIndicatorExpectedValueDomain() {
        return createDomain(DomainHelper.DomainType.INDICATOR_EXPECTED_VALUE.label);
    }

    /**
     * Method "addRangeRestriction" creates a new range restriction and adds it to the given domain.
     * 
     * @param domain the domain to which a new range restriction will be added
     * @return the newly created range restriction
     */
    public static RangeRestriction addRangeRestriction(Domain domain) {
        RangeRestriction rangeRestriction = DOMAIN.createRangeRestriction();
        domain.getRanges().add(rangeRestriction);
        return rangeRestriction;
    }

    /**
     * Method "createContiguousClosedBinsIntoDomain".
     * 
     * @param domainName (can be null)
     * @param nbBins the number of bins to create
     * @param min the minimal value
     * @param max the maximal value
     * @return the domain
     */
    public static Domain createContiguousClosedBinsIntoDomain(String domainName, int nbBins, double min, double max) {
        double[] bins = BinsHandler.generateClosedBins(min, max, nbBins);
        return createContiguousClosedBinsIntoDomain(domainName, bins);
    }

    /**
     * Method "createContiguousClosedBinsIntoDomain".
     * 
     * @param domainName the domain name (can be null)
     * @param bins the "closed" bins (the first value of array is the beginning of the bins and the last value of the
     * array is the end of bins)
     * @return the new Domain
     */
    public static Domain createContiguousClosedBinsIntoDomain(String domainName, double[] bins) {
        Domain domain = createDomain(domainName);
        for (int i = 0; i < bins.length - 1; i++) {
            double min = bins[i];
            double max = bins[i + 1];
            RangeRestriction rangeRestriction = createRealRangeRestriction(min, max);
            domain.getRanges().add(rangeRestriction);
        }
        return domain;
    }

    /**
     * Method "createRangeRestriction".
     * 
     * @param min the min value of the range
     * @param max the max value of the range
     * @return the new Range restriction.
     */
    public static RangeRestriction createRealRangeRestriction(double min, double max) {
        RangeRestriction rangeRestriction = DOMAIN.createRangeRestriction();
        rangeRestriction.setLowerValue(createRealNumberValue(null, min));
        rangeRestriction.setUpperValue(createRealNumberValue(null, max));
        return rangeRestriction;
    }

    public static RangeRestriction createStringRangeRestriction(String min, String max) {
        RangeRestriction rangeRestriction = DOMAIN.createRangeRestriction();
        rangeRestriction.setLowerValue(createStringValue(null, min));
        rangeRestriction.setUpperValue(createStringValue(null, max));
        return rangeRestriction;
    }

    /**
     * 
     * DOC klliu Comment method "createJavaUDIIndicatorParameter".
     * 
     * @param key
     * @param value
     * @return
     */
    public static JavaUDIIndicatorParameter createJavaUDIIndicatorParameter(String key, String value) {
        JavaUDIIndicatorParameter javaUDIIndicatorParameter = DOMAIN.createJavaUDIIndicatorParameter();
        javaUDIIndicatorParameter.setKey(key);
        javaUDIIndicatorParameter.setValue(value);
        return javaUDIIndicatorParameter;
    }

    public static TextValue createStringValue(String meaning, String value) {
        TextValue textValue = DomainFactory.eINSTANCE.createTextValue();
        textValue.setEncodeValueMeaning(meaning);
        textValue.setValue(value);
        return textValue;
    }

    /**
     * Method "createRealNumberValue".
     * 
     * @param meaning the meaning of the value (can be null)
     * @param value the value
     * @return a real number value
     */
    public static RealNumberValue createRealNumberValue(String meaning, double value) {
        RealNumberValue realNumberValue = DomainFactory.eINSTANCE.createRealNumberValue();
        realNumberValue.setEncodeValueMeaning(meaning);
        realNumberValue.setValue(value);
        return realNumberValue;
    }

    /**
     * Method "getNumberOfBins".
     * 
     * @param domain the domain containing bins
     * @return the number of bins for this domain
     */
    public static int getNumberOfBins(Domain domain) {
        assert domain != null;
        // TODO scorreia do check that the given domain is really a bins container.
        return domain.getRanges().size();
    }

    /**
     * Method "getMinBinValue".
     * 
     * @param domain
     * @return the lower value of the first bin.
     */
    public static double getMinBinValue(Domain domain) {
        EList<RangeRestriction> ranges = domain.getRanges();
        if (ranges.isEmpty()) {
            throw new IllegalArgumentException(Messages.getString("DomainHelper.NoRangeMin", domain.getName())); //$NON-NLS-1$
        }
        RangeRestriction r1 = ranges.get(0);
        if (r1 == null) {
            throw new IllegalArgumentException(Messages.getString("DomainHelper.FirstRangeMin", domain.getName())); //$NON-NLS-1$
        }
        return getRealValue(r1.getLowerValue());
    }

    /**
     * Method "getMaxBinValue".
     * 
     * @param domain
     * @return the higher value of the last bin.
     * @throws IllegalArgumentException
     */
    public static double getMaxBinValue(Domain domain) {
        EList<RangeRestriction> ranges = domain.getRanges();
        if (ranges.isEmpty()) {
            throw new IllegalArgumentException(Messages.getString("DomainHelper.NoRangeMax", domain.getName())); //$NON-NLS-1$
        }
        int lastIdx = ranges.size() - 1;
        RangeRestriction r1 = ranges.get(lastIdx);
        if (r1 == null) {
            throw new IllegalArgumentException(Messages.getString("DomainHelper.LastRangeMax", domain.getName())); //$NON-NLS-1$
        }
        return getRealValue(r1.getUpperValue());
    }

    public static String getMinValue(RangeRestriction range) {
        return getValueAsText(range.getLowerValue());
    }

    public static String getMaxValue(RangeRestriction range) {
        return getValueAsText(range.getUpperValue());
    }

    public static double getRealValue(LiteralValue object) {
        RealNumberValue upperValue = DataqualitySwitchHelper.REAL_NB_VALUE_SWITCH.doSwitch(object);
        if (upperValue == null) {
            throw new IllegalArgumentException(Messages.getString("DomainHelper.NotContainRealValue", object)); //$NON-NLS-1$
        }
        return upperValue.getValue();
    }

    private static String getValueAsText(LiteralValue object) {
        return DataqualitySwitchHelper.LITTERAL_VALUE_AS_TEXT_SWITCH.doSwitch(object);
    }

    /**
     * Method "getDomains".
     * 
     * @param objects some objects
     * @return only the Domain objects
     */
    public static List<Domain> getDomains(Collection<EObject> objects) {
        List<Domain> domains = new ArrayList<Domain>();
        for (EObject object : objects) {
            Domain domain = DataqualitySwitchHelper.DOMAIN_SWITCH.doSwitch(object);
            if (domain != null) {
                domains.add(domain);
            }
        }
        return domains;
    }

    /**
     * Method "getPatterns".
     * 
     * @param objects a list of objects
     * @return the Pattern objects extracted from the given list
     */
    public static List<Pattern> getPatterns(Collection<EObject> objects) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (EObject object : objects) {
            Pattern pattern = DataqualitySwitchHelper.PATTERN_SWITCH.doSwitch(object);
            if (pattern != null) {
                patterns.add(pattern);
            }
        }
        return patterns;
    }

    public static Domain setDataFilterTablePattern(final Collection<Domain> dataFilters, String tablePattern) {
        return setDataFilterPattern(dataFilters, PatternType.TABLE_PATTERN, DomainType.ANALYSIS_DATA_FILTER, tablePattern);
    }

    public static Domain setDataFilterViewPattern(final Collection<Domain> dataFilters, String viewPattern) {
        return setDataFilterPattern(dataFilters, PatternType.VIEW_PATTERN, DomainType.ANALYSIS_DATA_FILTER, viewPattern);
    }

    public static Domain setIndicatorExpectedValuePattern(final Collection<Domain> dataFilters, String filterPattern) {
        return setDataFilterPattern(dataFilters, PatternType.EXPECTED_VALUE, DomainType.INDICATOR_EXPECTED_VALUE, filterPattern);
    }

    /**
     * Method "setDataFilterPattern".
     * 
     * @param dataFilters a list of data filters
     * @param type
     * @param domainName
     * @param filterPattern
     * @return
     */
    private static Domain setDataFilterPattern(final Collection<Domain> dataFilters, PatternType type, DomainType domainName,
            String filterPattern) {
        RegularExpression filterExpr = BooleanExpressionHelper.createRegularExpression(null, filterPattern);

        for (Domain domain : dataFilters) {
            if (!domainName.label.equals(domain.getName())) {
                continue;
            }
            boolean exists = false;
            EList<Pattern> patterns = domain.getPatterns();
            for (Pattern pattern : patterns) {
                if (type.label.equals(pattern.getName())) {
                    exists = true;
                    pattern.getComponents().clear(); // remove previous expressions
                    pattern.getComponents().add(filterExpr);
                }
            }
            if (!exists) {
                // create pattern and set into the data filter
                addPatternToDomain(domain, filterExpr, filterPattern, type);
            }
            return domain;
        }
        // no data filter has not been set yet
        Domain domain = DomainFactory.eINSTANCE.createDomain();
        dataFilters.add(domain);
        domain.setName(domainName.label);
        addPatternToDomain(domain, filterExpr, filterPattern, type);
        return domain;
    }

    public static String getTablePattern(final Collection<Domain> dataFilters) {
        return getPattern(dataFilters, DomainType.ANALYSIS_DATA_FILTER, PatternType.TABLE_PATTERN);
    }

    public static String getViewPattern(final Collection<Domain> dataFilters) {
        return getPattern(dataFilters, DomainType.ANALYSIS_DATA_FILTER, PatternType.VIEW_PATTERN);
    }

    /**
     * Method "getIndicatorExpectedValue".
     * 
     * @param dataFilters
     * @return the expected value constraint found in the list of domains.
     */
    public static String getIndicatorExpectedValue(final Collection<Domain> dataFilters) {
        return getPattern(dataFilters, DomainType.INDICATOR_EXPECTED_VALUE, PatternType.EXPECTED_VALUE);
    }

    /**
     * Method "createPattern".
     * 
     * @param language
     * @param regexp
     * @return a new pattern from the given regular expression
     */
    public static Pattern createPattern(String language, String regexp) {
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.getComponents().add(BooleanExpressionHelper.createRegularExpression(language, regexp));
        return pattern;
    }

    private static String getPattern(final Collection<Domain> dataFilters, DomainType domainType, PatternType patternType) {
        if (dataFilters == null) {
            return null;
        }
        for (Domain domain : dataFilters) {
            if (!domainType.label.equals(domain.getName())) {
                continue;
            }
            EList<Pattern> patterns = domain.getPatterns();
            for (Pattern pattern : patterns) {
                if (patternType.label.equals(pattern.getName())) {
                    PatternComponent next = pattern.getComponents().iterator().next();
                    if (next == null) {
                        continue;
                    } else {
                        RegularExpression regexp = (RegularExpression) next;
                        Expression expression = regexp.getExpression();
                        if (expression != null) {
                            return expression.getBody();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Method "addPatternToDomain" creates a Pattern object with the given expression and stores it in the given domain.
     * Beware that it is also added to the owned elements of the Domain. This means that is contained in the domain and
     * cannot be shared among several domains.
     * 
     * @param domain
     * @param tableFilter
     * @param tablePattern
     */
    private static void addPatternToDomain(Domain domain, RegularExpression tableFilter, String tablePattern, PatternType type) {
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName(type.label);
        pattern.getComponents().add(tableFilter);
        domain.getPatterns().add(pattern);
        domain.getOwnedElement().add(pattern);
    }

    /**
     * Method "getExpressionType".
     * 
     * @param pattern
     * @return the expression type of the regular expression contained in the given pattern or null
     */
    public static String getExpressionType(Pattern pattern) {
        if (pattern != null) {
            EList<PatternComponent> components = pattern.getComponents();
            if (!components.isEmpty()) {
                PatternComponent component = components.get(0);
                if (component == null) {
                    return null;
                } else {
                    RegularExpression regexp = DataqualitySwitchHelper.REGULAR_EXPR_SWITCH.doSwitch(component);
                    if (regexp != null) {
                        String expressionType = regexp.getExpressionType();
                        if (StringUtils.isEmpty(expressionType)) {
                            // identify the expression from the path where it is stored (useful for old patterns created
                            // in
                            // version 1.1 and before)
                            final Resource resource = regexp.eResource();
                            if (resource != null) {
                                // do not replace these strings. They are the folder names in version 1.1
                                if (resource.getURI().toString().contains(SQL_PATTERN_FOLDER)) {
                                    expressionType = ExpressionType.SQL_LIKE.getLiteral();
                                } else if (resource.getURI().toString().contains(PATTERN_FOLDER)) {
                                    expressionType = ExpressionType.REGEXP.getLiteral();
                                }
                            }
                        }
                        return expressionType;
                    }
                }
            }
        }

        return null;
    }

}
