/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.talend.dataquality.indicators.*;
import org.talend.dataquality.indicators.AverageLengthIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.AvgLengthWithNullIndicator;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.BinFrequencyIndicator;
import org.talend.dataquality.indicators.BinLowFrequencyIndicator;
import org.talend.dataquality.indicators.BlankCountIndicator;
import org.talend.dataquality.indicators.BoxIndicator;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.CountsIndicator;
import org.talend.dataquality.indicators.DateFrequencyIndicator;
import org.talend.dataquality.indicators.DateLowFrequencyIndicator;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.DefValueCountIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.FormatFreqPieIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IQRIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.InvalidRegCodeCountIndicator;
import org.talend.dataquality.indicators.LengthIndicator;
import org.talend.dataquality.indicators.LowFrequencyIndicator;
import org.talend.dataquality.indicators.LowerQuartileIndicator;
import org.talend.dataquality.indicators.MatchingIndicator;
import org.talend.dataquality.indicators.MaxLengthIndicator;
import org.talend.dataquality.indicators.MaxLengthWithBlankIndicator;
import org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.MaxLengthWithNullIndicator;
import org.talend.dataquality.indicators.MaxValueIndicator;
import org.talend.dataquality.indicators.MeanIndicator;
import org.talend.dataquality.indicators.MedianIndicator;
import org.talend.dataquality.indicators.MinLengthIndicator;
import org.talend.dataquality.indicators.MinLengthWithBlankIndicator;
import org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.MinLengthWithNullIndicator;
import org.talend.dataquality.indicators.MinValueIndicator;
import org.talend.dataquality.indicators.ModeIndicator;
import org.talend.dataquality.indicators.MonthFrequencyIndicator;
import org.talend.dataquality.indicators.MonthLowFrequencyIndicator;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.PatternFreqIndicator;
import org.talend.dataquality.indicators.PatternLowFreqIndicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator;
import org.talend.dataquality.indicators.PossiblePhoneCountIndicator;
import org.talend.dataquality.indicators.QuarterFrequencyIndicator;
import org.talend.dataquality.indicators.QuarterLowFrequencyIndicator;
import org.talend.dataquality.indicators.RangeIndicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.SoundexFreqIndicator;
import org.talend.dataquality.indicators.SoundexLowFreqIndicator;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;
import org.talend.dataquality.indicators.SumIndicator;
import org.talend.dataquality.indicators.TextIndicator;
import org.talend.dataquality.indicators.TextParameters;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.UpperQuartileIndicator;
import org.talend.dataquality.indicators.ValidPhoneCountIndicator;
import org.talend.dataquality.indicators.ValidRegCodeCountIndicator;
import org.talend.dataquality.indicators.ValueIndicator;
import org.talend.dataquality.indicators.WeekFrequencyIndicator;
import org.talend.dataquality.indicators.WeekLowFrequencyIndicator;
import org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator;
import org.talend.dataquality.indicators.YearFrequencyIndicator;
import org.talend.dataquality.indicators.YearLowFrequencyIndicator;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.indicators.IndicatorsPackage
 * @generated
 */
public class IndicatorsAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static IndicatorsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorsAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = IndicatorsPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IndicatorsSwitch<Adapter> modelSwitch =
        new IndicatorsSwitch<Adapter>() {
            @Override
            public Adapter caseIndicator(Indicator object) {
                return createIndicatorAdapter();
            }
            @Override
            public Adapter caseRowCountIndicator(RowCountIndicator object) {
                return createRowCountIndicatorAdapter();
            }
            @Override
            public Adapter caseMeanIndicator(MeanIndicator object) {
                return createMeanIndicatorAdapter();
            }
            @Override
            public Adapter caseSumIndicator(SumIndicator object) {
                return createSumIndicatorAdapter();
            }
            @Override
            public Adapter caseCompositeIndicator(CompositeIndicator object) {
                return createCompositeIndicatorAdapter();
            }
            @Override
            public Adapter caseRangeIndicator(RangeIndicator object) {
                return createRangeIndicatorAdapter();
            }
            @Override
            public Adapter caseBoxIndicator(BoxIndicator object) {
                return createBoxIndicatorAdapter();
            }
            @Override
            public Adapter caseFrequencyIndicator(FrequencyIndicator object) {
                return createFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseBlankCountIndicator(BlankCountIndicator object) {
                return createBlankCountIndicatorAdapter();
            }
            @Override
            public Adapter caseIndicatorParameters(IndicatorParameters object) {
                return createIndicatorParametersAdapter();
            }
            @Override
            public Adapter caseMedianIndicator(MedianIndicator object) {
                return createMedianIndicatorAdapter();
            }
            @Override
            public Adapter caseValueIndicator(ValueIndicator object) {
                return createValueIndicatorAdapter();
            }
            @Override
            public Adapter caseMinValueIndicator(MinValueIndicator object) {
                return createMinValueIndicatorAdapter();
            }
            @Override
            public Adapter caseMaxValueIndicator(MaxValueIndicator object) {
                return createMaxValueIndicatorAdapter();
            }
            @Override
            public Adapter caseModeIndicator(ModeIndicator object) {
                return createModeIndicatorAdapter();
            }
            @Override
            public Adapter caseNullCountIndicator(NullCountIndicator object) {
                return createNullCountIndicatorAdapter();
            }
            @Override
            public Adapter caseDistinctCountIndicator(DistinctCountIndicator object) {
                return createDistinctCountIndicatorAdapter();
            }
            @Override
            public Adapter caseUniqueCountIndicator(UniqueCountIndicator object) {
                return createUniqueCountIndicatorAdapter();
            }
            @Override
            public Adapter caseDuplicateCountIndicator(DuplicateCountIndicator object) {
                return createDuplicateCountIndicatorAdapter();
            }
            @Override
            public Adapter caseIQRIndicator(IQRIndicator object) {
                return createIQRIndicatorAdapter();
            }
            @Override
            public Adapter caseTextIndicator(TextIndicator object) {
                return createTextIndicatorAdapter();
            }
            @Override
            public Adapter caseMinLengthIndicator(MinLengthIndicator object) {
                return createMinLengthIndicatorAdapter();
            }
            @Override
            public Adapter caseMinLengthWithNullIndicator(MinLengthWithNullIndicator object) {
                return createMinLengthWithNullIndicatorAdapter();
            }
            @Override
            public Adapter caseMinLengthWithBlankIndicator(MinLengthWithBlankIndicator object) {
                return createMinLengthWithBlankIndicatorAdapter();
            }
            @Override
            public Adapter caseMinLengthWithBlankNullIndicator(MinLengthWithBlankNullIndicator object) {
                return createMinLengthWithBlankNullIndicatorAdapter();
            }
            @Override
            public Adapter caseMaxLengthIndicator(MaxLengthIndicator object) {
                return createMaxLengthIndicatorAdapter();
            }
            @Override
            public Adapter caseMaxLengthWithNullIndicator(MaxLengthWithNullIndicator object) {
                return createMaxLengthWithNullIndicatorAdapter();
            }
            @Override
            public Adapter caseMaxLengthWithBlankIndicator(MaxLengthWithBlankIndicator object) {
                return createMaxLengthWithBlankIndicatorAdapter();
            }
            @Override
            public Adapter caseMaxLengthWithBlankNullIndicator(MaxLengthWithBlankNullIndicator object) {
                return createMaxLengthWithBlankNullIndicatorAdapter();
            }
            @Override
            public Adapter caseAverageLengthIndicator(AverageLengthIndicator object) {
                return createAverageLengthIndicatorAdapter();
            }
            @Override
            public Adapter caseAvgLengthWithNullIndicator(AvgLengthWithNullIndicator object) {
                return createAvgLengthWithNullIndicatorAdapter();
            }
            @Override
            public Adapter caseAvgLengthWithBlankIndicator(AvgLengthWithBlankIndicator object) {
                return createAvgLengthWithBlankIndicatorAdapter();
            }
            @Override
            public Adapter caseAvgLengthWithBlankNullIndicator(AvgLengthWithBlankNullIndicator object) {
                return createAvgLengthWithBlankNullIndicatorAdapter();
            }
            @Override
            public Adapter caseLengthIndicator(LengthIndicator object) {
                return createLengthIndicatorAdapter();
            }
            @Override
            public Adapter caseTextParameters(TextParameters object) {
                return createTextParametersAdapter();
            }
            @Override
            public Adapter caseLowerQuartileIndicator(LowerQuartileIndicator object) {
                return createLowerQuartileIndicatorAdapter();
            }
            @Override
            public Adapter caseUpperQuartileIndicator(UpperQuartileIndicator object) {
                return createUpperQuartileIndicatorAdapter();
            }
            @Override
            public Adapter caseCountsIndicator(CountsIndicator object) {
                return createCountsIndicatorAdapter();
            }
            @Override
            public Adapter caseDateParameters(DateParameters object) {
                return createDateParametersAdapter();
            }
            @Override
            public Adapter casePatternMatchingIndicator(PatternMatchingIndicator object) {
                return createPatternMatchingIndicatorAdapter();
            }
            @Override
            public Adapter caseSqlPatternMatchingIndicator(SqlPatternMatchingIndicator object) {
                return createSqlPatternMatchingIndicatorAdapter();
            }
            @Override
            public Adapter caseRegexpMatchingIndicator(RegexpMatchingIndicator object) {
                return createRegexpMatchingIndicatorAdapter();
            }
            @Override
            public Adapter caseMatchingIndicator(MatchingIndicator object) {
                return createMatchingIndicatorAdapter();
            }
            @Override
            public Adapter caseLowFrequencyIndicator(LowFrequencyIndicator object) {
                return createLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter casePatternFreqIndicator(PatternFreqIndicator object) {
                return createPatternFreqIndicatorAdapter();
            }
            @Override
            public Adapter casePatternLowFreqIndicator(PatternLowFreqIndicator object) {
                return createPatternLowFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseDefValueCountIndicator(DefValueCountIndicator object) {
                return createDefValueCountIndicatorAdapter();
            }
            @Override
            public Adapter caseSoundexFreqIndicator(SoundexFreqIndicator object) {
                return createSoundexFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseSoundexLowFreqIndicator(SoundexLowFreqIndicator object) {
                return createSoundexLowFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseDatePatternFreqIndicator(DatePatternFreqIndicator object) {
                return createDatePatternFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseDateFrequencyIndicator(DateFrequencyIndicator object) {
                return createDateFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseWeekFrequencyIndicator(WeekFrequencyIndicator object) {
                return createWeekFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseMonthFrequencyIndicator(MonthFrequencyIndicator object) {
                return createMonthFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseQuarterFrequencyIndicator(QuarterFrequencyIndicator object) {
                return createQuarterFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseYearFrequencyIndicator(YearFrequencyIndicator object) {
                return createYearFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseBinFrequencyIndicator(BinFrequencyIndicator object) {
                return createBinFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseDateLowFrequencyIndicator(DateLowFrequencyIndicator object) {
                return createDateLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseWeekLowFrequencyIndicator(WeekLowFrequencyIndicator object) {
                return createWeekLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseMonthLowFrequencyIndicator(MonthLowFrequencyIndicator object) {
                return createMonthLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseQuarterLowFrequencyIndicator(QuarterLowFrequencyIndicator object) {
                return createQuarterLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseYearLowFrequencyIndicator(YearLowFrequencyIndicator object) {
                return createYearLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseBinLowFrequencyIndicator(BinLowFrequencyIndicator object) {
                return createBinLowFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseValidPhoneCountIndicator(ValidPhoneCountIndicator object) {
                return createValidPhoneCountIndicatorAdapter();
            }
            @Override
            public Adapter casePossiblePhoneCountIndicator(PossiblePhoneCountIndicator object) {
                return createPossiblePhoneCountIndicatorAdapter();
            }
            @Override
            public Adapter caseValidRegCodeCountIndicator(ValidRegCodeCountIndicator object) {
                return createValidRegCodeCountIndicatorAdapter();
            }
            @Override
            public Adapter caseInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator object) {
                return createInvalidRegCodeCountIndicatorAdapter();
            }
            @Override
            public Adapter caseWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator object) {
                return createWellFormNationalPhoneCountIndicatorAdapter();
            }
            @Override
            public Adapter caseWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator object) {
                return createWellFormIntePhoneCountIndicatorAdapter();
            }
            @Override
            public Adapter caseWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator object) {
                return createWellFormE164PhoneCountIndicatorAdapter();
            }
            @Override
            public Adapter casePhoneNumbStatisticsIndicator(PhoneNumbStatisticsIndicator object) {
                return createPhoneNumbStatisticsIndicatorAdapter();
            }
            @Override
            public Adapter caseFormatFreqPieIndicator(FormatFreqPieIndicator object) {
                return createFormatFreqPieIndicatorAdapter();
            }
            @Override
            public Adapter caseBenfordLawFrequencyIndicator(BenfordLawFrequencyIndicator object) {
                return createBenfordLawFrequencyIndicatorAdapter();
            }
            @Override
            public Adapter caseEastAsiaPatternFreqIndicator(EastAsiaPatternFreqIndicator object) {
                return createEastAsiaPatternFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseEastAsiaPatternLowFreqIndicator(EastAsiaPatternLowFreqIndicator object) {
                return createEastAsiaPatternLowFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseCSWordPatternFreqIndicator(CSWordPatternFreqIndicator object) {
                return createCSWordPatternFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseCSWordPatternLowFreqIndicator(CSWordPatternLowFreqIndicator object) {
                return createCSWordPatternLowFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseCIWordPatternFreqIndicator(CIWordPatternFreqIndicator object) {
                return createCIWordPatternFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseCIWordPatternLowFreqIndicator(CIWordPatternLowFreqIndicator object) {
                return createCIWordPatternLowFreqIndicatorAdapter();
            }
            @Override
            public Adapter caseElement(Element object) {
                return createElementAdapter();
            }
            @Override
            public Adapter caseModelElement(ModelElement object) {
                return createModelElementAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.Indicator <em>Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.Indicator
     * @generated
     */
    public Adapter createIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.RowCountIndicator <em>Row Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.RowCountIndicator
     * @generated
     */
    public Adapter createRowCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MeanIndicator <em>Mean Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MeanIndicator
     * @generated
     */
    public Adapter createMeanIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.SumIndicator <em>Sum Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.SumIndicator
     * @generated
     */
    public Adapter createSumIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CompositeIndicator <em>Composite Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CompositeIndicator
     * @generated
     */
    public Adapter createCompositeIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.RangeIndicator <em>Range Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.RangeIndicator
     * @generated
     */
    public Adapter createRangeIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.BoxIndicator <em>Box Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.BoxIndicator
     * @generated
     */
    public Adapter createBoxIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.FrequencyIndicator <em>Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.FrequencyIndicator
     * @generated
     */
    public Adapter createFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.BlankCountIndicator <em>Blank Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.BlankCountIndicator
     * @generated
     */
    public Adapter createBlankCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.IndicatorParameters <em>Indicator Parameters</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.IndicatorParameters
     * @generated
     */
    public Adapter createIndicatorParametersAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MedianIndicator <em>Median Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MedianIndicator
     * @generated
     */
    public Adapter createMedianIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.ValueIndicator <em>Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.ValueIndicator
     * @generated
     */
    public Adapter createValueIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MinValueIndicator <em>Min Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MinValueIndicator
     * @generated
     */
    public Adapter createMinValueIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MaxValueIndicator <em>Max Value Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MaxValueIndicator
     * @generated
     */
    public Adapter createMaxValueIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.ModeIndicator <em>Mode Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.ModeIndicator
     * @generated
     */
    public Adapter createModeIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.NullCountIndicator <em>Null Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.NullCountIndicator
     * @generated
     */
    public Adapter createNullCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DistinctCountIndicator <em>Distinct Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DistinctCountIndicator
     * @generated
     */
    public Adapter createDistinctCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.UniqueCountIndicator <em>Unique Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.UniqueCountIndicator
     * @generated
     */
    public Adapter createUniqueCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DuplicateCountIndicator <em>Duplicate Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator
     * @generated
     */
    public Adapter createDuplicateCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.IQRIndicator <em>IQR Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.IQRIndicator
     * @generated
     */
    public Adapter createIQRIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.TextIndicator <em>Text Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.TextIndicator
     * @generated
     */
    public Adapter createTextIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MinLengthIndicator <em>Min Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MinLengthIndicator
     * @generated
     */
    public Adapter createMinLengthIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MinLengthWithNullIndicator <em>Min Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MinLengthWithNullIndicator
     * @generated
     */
    public Adapter createMinLengthWithNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MinLengthWithBlankIndicator <em>Min Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MinLengthWithBlankIndicator
     * @generated
     */
    public Adapter createMinLengthWithBlankIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator <em>Min Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator
     * @generated
     */
    public Adapter createMinLengthWithBlankNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MaxLengthIndicator <em>Max Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MaxLengthIndicator
     * @generated
     */
    public Adapter createMaxLengthIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MaxLengthWithNullIndicator <em>Max Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MaxLengthWithNullIndicator
     * @generated
     */
    public Adapter createMaxLengthWithNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MaxLengthWithBlankIndicator <em>Max Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MaxLengthWithBlankIndicator
     * @generated
     */
    public Adapter createMaxLengthWithBlankIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator <em>Max Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator
     * @generated
     */
    public Adapter createMaxLengthWithBlankNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.AverageLengthIndicator <em>Average Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.AverageLengthIndicator
     * @generated
     */
    public Adapter createAverageLengthIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.AvgLengthWithNullIndicator <em>Avg Length With Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.AvgLengthWithNullIndicator
     * @generated
     */
    public Adapter createAvgLengthWithNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.AvgLengthWithBlankIndicator <em>Avg Length With Blank Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.AvgLengthWithBlankIndicator
     * @generated
     */
    public Adapter createAvgLengthWithBlankIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator <em>Avg Length With Blank Null Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator
     * @generated
     */
    public Adapter createAvgLengthWithBlankNullIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.LengthIndicator <em>Length Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.LengthIndicator
     * @generated
     */
    public Adapter createLengthIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.TextParameters <em>Text Parameters</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.TextParameters
     * @generated
     */
    public Adapter createTextParametersAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.LowerQuartileIndicator <em>Lower Quartile Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.LowerQuartileIndicator
     * @generated
     */
    public Adapter createLowerQuartileIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.UpperQuartileIndicator <em>Upper Quartile Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.UpperQuartileIndicator
     * @generated
     */
    public Adapter createUpperQuartileIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CountsIndicator <em>Counts Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CountsIndicator
     * @generated
     */
    public Adapter createCountsIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DateParameters <em>Date Parameters</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DateParameters
     * @generated
     */
    public Adapter createDateParametersAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.PatternMatchingIndicator <em>Pattern Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.PatternMatchingIndicator
     * @generated
     */
    public Adapter createPatternMatchingIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.SqlPatternMatchingIndicator <em>Sql Pattern Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.SqlPatternMatchingIndicator
     * @generated
     */
    public Adapter createSqlPatternMatchingIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.RegexpMatchingIndicator <em>Regexp Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.RegexpMatchingIndicator
     * @generated
     */
    public Adapter createRegexpMatchingIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MatchingIndicator <em>Matching Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MatchingIndicator
     * @generated
     */
    public Adapter createMatchingIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.LowFrequencyIndicator <em>Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.LowFrequencyIndicator
     * @generated
     */
    public Adapter createLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.PatternFreqIndicator <em>Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.PatternFreqIndicator
     * @generated
     */
    public Adapter createPatternFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.PatternLowFreqIndicator <em>Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.PatternLowFreqIndicator
     * @generated
     */
    public Adapter createPatternLowFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DefValueCountIndicator <em>Def Value Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DefValueCountIndicator
     * @generated
     */
    public Adapter createDefValueCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.SoundexFreqIndicator <em>Soundex Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.SoundexFreqIndicator
     * @generated
     */
    public Adapter createSoundexFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.SoundexLowFreqIndicator <em>Soundex Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.SoundexLowFreqIndicator
     * @generated
     */
    public Adapter createSoundexLowFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DatePatternFreqIndicator <em>Date Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DatePatternFreqIndicator
     * @generated
     */
    public Adapter createDatePatternFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DateFrequencyIndicator <em>Date Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DateFrequencyIndicator
     * @generated
     */
    public Adapter createDateFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.WeekFrequencyIndicator <em>Week Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.WeekFrequencyIndicator
     * @generated
     */
    public Adapter createWeekFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MonthFrequencyIndicator <em>Month Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MonthFrequencyIndicator
     * @generated
     */
    public Adapter createMonthFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.QuarterFrequencyIndicator <em>Quarter Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.QuarterFrequencyIndicator
     * @generated
     */
    public Adapter createQuarterFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.YearFrequencyIndicator <em>Year Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.YearFrequencyIndicator
     * @generated
     */
    public Adapter createYearFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.BinFrequencyIndicator <em>Bin Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.BinFrequencyIndicator
     * @generated
     */
    public Adapter createBinFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.DateLowFrequencyIndicator <em>Date Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.DateLowFrequencyIndicator
     * @generated
     */
    public Adapter createDateLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.WeekLowFrequencyIndicator <em>Week Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.WeekLowFrequencyIndicator
     * @generated
     */
    public Adapter createWeekLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.MonthLowFrequencyIndicator <em>Month Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.MonthLowFrequencyIndicator
     * @generated
     */
    public Adapter createMonthLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.QuarterLowFrequencyIndicator <em>Quarter Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.QuarterLowFrequencyIndicator
     * @generated
     */
    public Adapter createQuarterLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.YearLowFrequencyIndicator <em>Year Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.YearLowFrequencyIndicator
     * @generated
     */
    public Adapter createYearLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.BinLowFrequencyIndicator <em>Bin Low Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.BinLowFrequencyIndicator
     * @generated
     */
    public Adapter createBinLowFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.ValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.ValidPhoneCountIndicator
     * @generated
     */
    public Adapter createValidPhoneCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.PossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.PossiblePhoneCountIndicator
     * @generated
     */
    public Adapter createPossiblePhoneCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.ValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.ValidRegCodeCountIndicator
     * @generated
     */
    public Adapter createValidRegCodeCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.InvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.InvalidRegCodeCountIndicator
     * @generated
     */
    public Adapter createInvalidRegCodeCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator
     * @generated
     */
    public Adapter createWellFormNationalPhoneCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator
     * @generated
     */
    public Adapter createWellFormIntePhoneCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator
     * @generated
     */
    public Adapter createWellFormE164PhoneCountIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator <em>Phone Numb Statistics Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator
     * @generated
     */
    public Adapter createPhoneNumbStatisticsIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.FormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.FormatFreqPieIndicator
     * @generated
     */
    public Adapter createFormatFreqPieIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.BenfordLawFrequencyIndicator <em>Benford Law Frequency Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.BenfordLawFrequencyIndicator
     * @generated
     */
    public Adapter createBenfordLawFrequencyIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator <em>East Asia Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator
     * @generated
     */
    public Adapter createEastAsiaPatternFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator <em>East Asia Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator
     * @generated
     */
    public Adapter createEastAsiaPatternLowFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CSWordPatternFreqIndicator <em>CS Word Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CSWordPatternFreqIndicator
     * @generated
     */
    public Adapter createCSWordPatternFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CSWordPatternLowFreqIndicator <em>CS Word Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CSWordPatternLowFreqIndicator
     * @generated
     */
    public Adapter createCSWordPatternLowFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CIWordPatternFreqIndicator <em>CI Word Pattern Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CIWordPatternFreqIndicator
     * @generated
     */
    public Adapter createCIWordPatternFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.CIWordPatternLowFreqIndicator <em>CI Word Pattern Low Freq Indicator</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.CIWordPatternLowFreqIndicator
     * @generated
     */
    public Adapter createCIWordPatternLowFreqIndicatorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Element <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Element
     * @generated
     */
    public Adapter createElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.ModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.ModelElement
     * @generated
     */
    public Adapter createModelElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //IndicatorsAdapterFactory
