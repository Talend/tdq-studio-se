/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.IndicatorsPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Benford Law Frequency Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class BenfordLawFrequencyIndicatorImpl extends FrequencyIndicatorImpl implements BenfordLawFrequencyIndicator {

    private boolean isChecked = false;

    public static String INVALID = "invalid"; //$NON-NLS-1$

    public static String ZERO = "0"; //$NON-NLS-1$

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected BenfordLawFrequencyIndicatorImpl() {
        super();
    }

    /**
     * handle some special cases for SQL engine:
     * <ul>
     * <li>when there are null values;</li>
     * <li>when there miss some numbers between 1~9;</li>
     * <li>when the column is double, the leading digit maybe 0;</li>
     * <li>when the column is string type, when the leading is not 1~9, all counted into "invalid" one.</li>
     * </ul>
     */
    private void handleSpecialCharacterAndMissingValues() {
        if (isChecked && this.valueToFreq.size() < 1) {
            return;
        }

        // check for invalid
        long counted = 0L;
        List<Object> invalid = new ArrayList<Object>();
        List<Object> lengthMore = new ArrayList<Object>();
        for (Object val : this.valueToFreq.keySet()) {
            if (isInvalid(val) < 0) {
                invalid.add(val);
                Long freq = this.valueToFreq.get(val);
                counted = (freq == null) ? counted : counted + freq;
            } else if (String.valueOf(val).codePoints().count() > 1) {
                // check the length, should only = 1, if >1, cut it
                lengthMore.add(val);
            }

        }
        // combine all invalid into one <"invalid",counted>
        if (invalid.size() > 0) {
            for (Object val : invalid) {
                this.valueToFreq.remove(val);
            }
            this.valueToFreq.put(INVALID, counted);
        }

        // check the length, should only = 1, if >1, cut it
        if (lengthMore.size() > 0) {
            for (Object val : lengthMore) {
                String k = String.valueOf(val).substring(0, String.valueOf(val).offsetByCodePoints(0, 1));
                Long freq = this.valueToFreq.get(val);
                this.valueToFreq.remove(val);
                this.valueToFreq.put(k, freq);
            }
        }

        // check from 1~9, if miss, add it as <number, 0L>
        for (int i = 1; i < 10; i++) {
            Long value = this.valueToFreq.get(String.valueOf(i));
            if (value == null) {
                this.valueToFreq.put(String.valueOf(i), 0L);
            }
        }

        isChecked = true;
    }

    /**
     * if the val is 1~9 then return 1; if it is "0" then return 0; if it is non-number then return -1
     * 
     * @param val
     * @return
     */
    private int isInvalid(Object val) {
        // MOD msjian TDQ-6123: fix a IndexOutOfBoundsException
        String strValue = String.valueOf(val);
        if (strValue.length() < 1) {
            return -1;
        }
        char lead = strValue.charAt(0);
        // TDQ-6123~
        if (Character.isDigit(lead)) {
            if ('0' == lead) {
                return 0;
            }
            return 1;
        }
        return -1;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.BENFORD_LAW_FREQUENCY_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> count the occur times for 1~9 at the beginning of every numbers. <!-- end-user-doc -->
     * 
     * 
     * @generated NOT
     */
    @Override
    public boolean handle(Object data) {
        this.count++;

        if (data == null) {
            setValue(INVALID);
            return true;
        }

        int isInvalid = isInvalid(data);
        if (isInvalid == 0) {
            setValue(ZERO);
        } else if (isInvalid < 0) {
            setValue(INVALID);
        } else {
            setValue(String.valueOf(data).substring(0, String.valueOf(data).offsetByCodePoints(0, 1)));
        }

        return true;
    }

    private void setValue(String key) {
        // increment frequency of leading digit in data
        Long c = this.valueToFreq.get(key);
        if (c == null) {
            this.valueToFreq.put(key, 1L);
        } else {
            c++;
            this.valueToFreq.put(key, c);
        }
    }

    /**
     * Added yyin 20121008, TDQ-6233, the default limit=10, change it to 50 (0-9, a-z,null)
     */
    @Override
    public IndicatorParameters getParameters() {
        if (parameters == null) {
            parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
            parameters.setTopN(50);
        }
        return parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#storeSqlResults(java.util.List)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        boolean ok = super.storeSqlResults(objects);
        if (ok) {
            this.handleSpecialCharacterAndMissingValues();
        }
        return ok;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        boolean ok = super.reset();
        // initialize map with expected digits 1..9
        for (int i = 1; i <= 9; i++) {
            this.valueToFreq.put(String.valueOf(i), 0L);
        }
        if (this.valueToFreq.get(INVALID) != null) {
            this.valueToFreq.put(INVALID, 0L);
        }
        if (this.valueToFreq.get(ZERO) != null) {
            this.valueToFreq.put(ZERO, 0L);
        }
        return ok;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        HashMap<Object, Long> map = new HashMap<Object, Long>();
        for (int i = 0; i < 10; i++) {
            Long value = this.valueToFreq.get(String.valueOf(i));
            if (value == null) {
                continue;
            } else {
                map.put(String.valueOf(i), value);
            }
        }
        Long value = this.valueToFreq.get(INVALID);
        if (value != null) {
            map.put(INVALID, value);
        }
        this.valueToFreq.clear();
        this.setValueToFreq(map);
        return true;
    }

} // BenfordLawFrequencyIndicatorImpl
