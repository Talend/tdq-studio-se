/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.i18n.Messages;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Regexp Matching Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class RegexpMatchingIndicatorImpl extends PatternMatchingIndicatorImpl implements RegexpMatchingIndicator {

    private static Logger log = Logger.getLogger(RegexpMatchingIndicatorImpl.class);

    // FIXME never use now. error message should not be specific to this indicator.
    // add klliu 2010-06-12 bug 13695
    private String javaPatternMessage;

    private Long invalidCount = 0l;

    private Long invalidValueCount = 0l;

    private Long validValueCount = 0l;

    public String getJavaPatternMessage() {
        return javaPatternMessage;
    }

    public void setJavaPatternMessage(String javaPatternMessage) {
        this.javaPatternMessage = javaPatternMessage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected RegexpMatchingIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.REGEXP_MATCHING_INDICATOR;
    }

    private String regex = null;

    private java.util.regex.Pattern pattern = null;

    protected String datePattern = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#prepare()
     */
    @Override
    public boolean prepare() {
        // the same with in DatePatternFreqIndicatorImpl
        // TDQ-14467: set date pattern only for delimited file.
        MetadataColumn mdColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(this.getAnalyzedElement());
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(this.getAnalyzedElement());
        if (tdColumn == null && mdColumn != null && "id_Date".equals(mdColumn.getTalendType())) {
            // get date pattern from the column
            String pattern = mdColumn.getPattern();
            if (StringUtils.isEmpty(pattern)) {
                pattern = "yyyy-MM-dd";
            } else {
                pattern = StringUtils.replace(pattern, "\"", StringUtils.EMPTY);
            }
            // the datePattern only for DelimitedFile connection.
            this.datePattern = pattern;
        }
        // TDQ-14467~

        this.regex = getRegex();
        if (regex == null) {
            return false;
        }
        // MOD klliu 2010-06-12 bug 13695
        if (regex.equals(this.getJavaPatternMessage())) {
            return false;
        }
        try {
            pattern = java.util.regex.Pattern.compile(regex);
        } catch (java.util.regex.PatternSyntaxException e) {
            log.error(Messages.getString("Using_regular_expression", this.getName(), regex), e);
            return false;
        }
        if (log.isInfoEnabled()) {
            log.info(Messages.getString("Using_regular_expression", this.getName(), regex));
        }
        return super.prepare();
    }

    /**
     * DOC scorreia Comment method "getRegex".
     * 
     * this mehtod only for job Action
     * 
     * 1) only in Java Engine will call this method 2) get Java Regex first, if don't have Java Regex then get Default
     * Regex, if don't have Default Regex then return null
     * 
     * @return
     */
    @Override
    public String getRegex() {
        // MOD klliu 2010-06-12 bug 13695
        if (this.parameters != null) {
            final Domain dataValidDomain = parameters.getDataValidDomain();
            if (dataValidDomain != null) {
                final EList<Pattern> patterns = dataValidDomain.getPatterns();
                for (Pattern p : patterns) {
                    if (p != null) {
                        // MOD yyi 2009-09-29 Feature: 9289
                        String r = DomainHelper.getJavaRegexp(p);
                        if (r == null) { // if don't have Java Regex, get the Default Regex
                            r = DomainHelper.getSQLRegexp(p);
                        }

                        if (r != null) {
                            if (r.startsWith("'") && r.endsWith("'")) {
                                // remove enclosing singles quotes which are used for SQL only (not java)
                                r = r.substring(1, r.length() - 1);
                            }
                            return r;
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        if (count != null && matchingValueCount != null) {
            this.setNotMatchingValueCount(count - matchingValueCount);
        }
        return super.finalizeComputation();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        boolean ok = super.handle(data);
        this.setValidRow(false);
        this.setInValidRow(false);
        if (data != null) {
            // ADD msjian TDQ-14467: format the date for file connection.
            if (data instanceof Date && !StringUtils.isEmpty(datePattern)) {
                data = DateFormatUtils.format((Date) data, datePattern);
            }
            // TDQ-14467~
            String body = String.valueOf(data);
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                this.matchingValueCount++;
                this.setValidRow(true);
            } else {
                this.setInValidRow(true);
            }

        } else {
            this.setInValidRow(true);
        }
        if (checkDrillDownCount()) {
            this.mustStoreRow = true;
        }
        return ok;
    }

    /**
     * "checkDrillDownCount".
     * 
     * @return
     */
    private boolean checkDrillDownCount() {
        if (this.validRow) {
            return this.checkMustStoreCurrentRow(matchingValueCount - 1) || this.checkMustStoreCurrentRow(validValueCount);
        } else {
            return this.checkMustStoreCurrentRow(invalidValueCount) || this.checkMustStoreCurrentRow(invalidCount);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getMapDB(java.lang.String)
     */
    @Override
    public AbstractDB getMapDB(String dbName) {
        if (isUsedMapDBMode()) {
            // get invalidDrillDownValues set
            if (StandardDBName.invalidDrillDownValues.name().equals(dbName)) {
                // create new DBSet
                return initValueForDBSet(StandardDBName.invalidDrillDownValues.name());
            }
        }
        return super.getMapDB(dbName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handleDrillDownData(java.lang.Object, java.util.List)
     */
    @Override
    public void handleDrillDownData(Object masterObject, List<Object> inputRowList) {
        if (validRow) {
            // store drill dwon data for view valid values
            if (this.checkMustStoreCurrentRow(matchingValueCount - 1)) {
                super.handleDrillDownData(masterObject, inputRowList);
            }
            // store drill dwon data for view valid values
            if (this.checkMustStoreCurrentRow(validValueCount)) {
                if (!drillDownValuesSet.contains(masterObject)) {
                    validValueCount++;
                    drillDownValuesSet.add(masterObject);
                }
            }
        } else {
            // store drill dwon data for view invalid values
            if (this.checkMustStoreCurrentRow(invalidValueCount)) {
                Set<Object> drillDownValuesSet = initValueForDBSet(StandardDBName.invalidDrillDownValues.name());
                if (!drillDownValuesSet.contains(masterObject)) {
                    invalidValueCount++;
                    drillDownValuesSet.add(masterObject);
                }
            }
            // store drill dwon data for view invalid rows
            if (this.checkMustStoreCurrentRow(invalidCount)) {
                invalidCount++;
                Map<Object, List<Object>> drillDownRowsMap = initValueForDBMap(StandardDBName.invalidDrillDown.name());
                drillDownRowsMap.put(invalidCount - 1, inputRowList);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.MatchingIndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        invalidCount = 0l;
        invalidValueCount = 0l;
        validValueCount = 0l;
        return super.reset();
    }

} // RegexpMatchingIndicatorImpl
