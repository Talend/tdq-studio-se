// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.CSWordPatternLowFreqIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.statistics.frequency.recognition.TypoUnicodePatternRecognizer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CS Word Pattern Low Freq Indicator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class CSWordPatternLowFreqIndicatorImpl extends FrequencyIndicatorImpl implements CSWordPatternLowFreqIndicator {

    private TypoUnicodePatternRecognizer typoUnicodePatternRecognizer = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CSWordPatternLowFreqIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.CS_WORD_PATTERN_LOW_FREQ_INDICATOR;
    }

    @Override
    public boolean prepare() {
        typoUnicodePatternRecognizer = TypoUnicodePatternRecognizer.withCase();
        return super.prepare();
    }

    @Override
    public boolean handle(Object data) {
        if (data == null) {
            return false;
        }
        String sentence = data.toString();
        Object pattern = typoUnicodePatternRecognizer.getValuePattern(sentence).toArray()[0];
        return super.handle(pattern);

    }

    @Override
    protected String getFrequencyLabel(Object name) {
        Object object = typoUnicodePatternRecognizer.getValuePattern(name.toString()).toArray()[0];
        return object == null ? null : object.toString();
    }
} //CSWordPatternLowFreqIndicatorImpl
