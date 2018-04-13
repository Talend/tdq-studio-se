/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
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
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.indicators.IndicatorsPackage
 * @generated
 */
public class IndicatorsSwitch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static IndicatorsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorsSwitch() {
        if (modelPackage == null) {
            modelPackage = IndicatorsPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case IndicatorsPackage.INDICATOR: {
                Indicator indicator = (Indicator)theEObject;
                T result = caseIndicator(indicator);
                if (result == null) result = caseModelElement(indicator);
                if (result == null) result = caseElement(indicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.ROW_COUNT_INDICATOR: {
                RowCountIndicator rowCountIndicator = (RowCountIndicator)theEObject;
                T result = caseRowCountIndicator(rowCountIndicator);
                if (result == null) result = caseIndicator(rowCountIndicator);
                if (result == null) result = caseModelElement(rowCountIndicator);
                if (result == null) result = caseElement(rowCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MEAN_INDICATOR: {
                MeanIndicator meanIndicator = (MeanIndicator)theEObject;
                T result = caseMeanIndicator(meanIndicator);
                if (result == null) result = caseSumIndicator(meanIndicator);
                if (result == null) result = caseIndicator(meanIndicator);
                if (result == null) result = caseModelElement(meanIndicator);
                if (result == null) result = caseElement(meanIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.SUM_INDICATOR: {
                SumIndicator sumIndicator = (SumIndicator)theEObject;
                T result = caseSumIndicator(sumIndicator);
                if (result == null) result = caseIndicator(sumIndicator);
                if (result == null) result = caseModelElement(sumIndicator);
                if (result == null) result = caseElement(sumIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.COMPOSITE_INDICATOR: {
                CompositeIndicator compositeIndicator = (CompositeIndicator)theEObject;
                T result = caseCompositeIndicator(compositeIndicator);
                if (result == null) result = caseIndicator(compositeIndicator);
                if (result == null) result = caseModelElement(compositeIndicator);
                if (result == null) result = caseElement(compositeIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.RANGE_INDICATOR: {
                RangeIndicator rangeIndicator = (RangeIndicator)theEObject;
                T result = caseRangeIndicator(rangeIndicator);
                if (result == null) result = caseCompositeIndicator(rangeIndicator);
                if (result == null) result = caseIndicator(rangeIndicator);
                if (result == null) result = caseModelElement(rangeIndicator);
                if (result == null) result = caseElement(rangeIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.BOX_INDICATOR: {
                BoxIndicator boxIndicator = (BoxIndicator)theEObject;
                T result = caseBoxIndicator(boxIndicator);
                if (result == null) result = caseCompositeIndicator(boxIndicator);
                if (result == null) result = caseIndicator(boxIndicator);
                if (result == null) result = caseModelElement(boxIndicator);
                if (result == null) result = caseElement(boxIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.FREQUENCY_INDICATOR: {
                FrequencyIndicator frequencyIndicator = (FrequencyIndicator)theEObject;
                T result = caseFrequencyIndicator(frequencyIndicator);
                if (result == null) result = caseIndicator(frequencyIndicator);
                if (result == null) result = caseModelElement(frequencyIndicator);
                if (result == null) result = caseElement(frequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.BLANK_COUNT_INDICATOR: {
                BlankCountIndicator blankCountIndicator = (BlankCountIndicator)theEObject;
                T result = caseBlankCountIndicator(blankCountIndicator);
                if (result == null) result = caseIndicator(blankCountIndicator);
                if (result == null) result = caseModelElement(blankCountIndicator);
                if (result == null) result = caseElement(blankCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.INDICATOR_PARAMETERS: {
                IndicatorParameters indicatorParameters = (IndicatorParameters)theEObject;
                T result = caseIndicatorParameters(indicatorParameters);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MEDIAN_INDICATOR: {
                MedianIndicator medianIndicator = (MedianIndicator)theEObject;
                T result = caseMedianIndicator(medianIndicator);
                if (result == null) result = caseIndicator(medianIndicator);
                if (result == null) result = caseModelElement(medianIndicator);
                if (result == null) result = caseElement(medianIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.VALUE_INDICATOR: {
                ValueIndicator valueIndicator = (ValueIndicator)theEObject;
                T result = caseValueIndicator(valueIndicator);
                if (result == null) result = caseIndicator(valueIndicator);
                if (result == null) result = caseModelElement(valueIndicator);
                if (result == null) result = caseElement(valueIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MIN_VALUE_INDICATOR: {
                MinValueIndicator minValueIndicator = (MinValueIndicator)theEObject;
                T result = caseMinValueIndicator(minValueIndicator);
                if (result == null) result = caseValueIndicator(minValueIndicator);
                if (result == null) result = caseIndicator(minValueIndicator);
                if (result == null) result = caseModelElement(minValueIndicator);
                if (result == null) result = caseElement(minValueIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MAX_VALUE_INDICATOR: {
                MaxValueIndicator maxValueIndicator = (MaxValueIndicator)theEObject;
                T result = caseMaxValueIndicator(maxValueIndicator);
                if (result == null) result = caseValueIndicator(maxValueIndicator);
                if (result == null) result = caseIndicator(maxValueIndicator);
                if (result == null) result = caseModelElement(maxValueIndicator);
                if (result == null) result = caseElement(maxValueIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MODE_INDICATOR: {
                ModeIndicator modeIndicator = (ModeIndicator)theEObject;
                T result = caseModeIndicator(modeIndicator);
                if (result == null) result = caseFrequencyIndicator(modeIndicator);
                if (result == null) result = caseIndicator(modeIndicator);
                if (result == null) result = caseModelElement(modeIndicator);
                if (result == null) result = caseElement(modeIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.NULL_COUNT_INDICATOR: {
                NullCountIndicator nullCountIndicator = (NullCountIndicator)theEObject;
                T result = caseNullCountIndicator(nullCountIndicator);
                if (result == null) result = caseIndicator(nullCountIndicator);
                if (result == null) result = caseModelElement(nullCountIndicator);
                if (result == null) result = caseElement(nullCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DISTINCT_COUNT_INDICATOR: {
                DistinctCountIndicator distinctCountIndicator = (DistinctCountIndicator)theEObject;
                T result = caseDistinctCountIndicator(distinctCountIndicator);
                if (result == null) result = caseIndicator(distinctCountIndicator);
                if (result == null) result = caseModelElement(distinctCountIndicator);
                if (result == null) result = caseElement(distinctCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.UNIQUE_COUNT_INDICATOR: {
                UniqueCountIndicator uniqueCountIndicator = (UniqueCountIndicator)theEObject;
                T result = caseUniqueCountIndicator(uniqueCountIndicator);
                if (result == null) result = caseIndicator(uniqueCountIndicator);
                if (result == null) result = caseModelElement(uniqueCountIndicator);
                if (result == null) result = caseElement(uniqueCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR: {
                DuplicateCountIndicator duplicateCountIndicator = (DuplicateCountIndicator)theEObject;
                T result = caseDuplicateCountIndicator(duplicateCountIndicator);
                if (result == null) result = caseIndicator(duplicateCountIndicator);
                if (result == null) result = caseModelElement(duplicateCountIndicator);
                if (result == null) result = caseElement(duplicateCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.IQR_INDICATOR: {
                IQRIndicator iqrIndicator = (IQRIndicator)theEObject;
                T result = caseIQRIndicator(iqrIndicator);
                if (result == null) result = caseRangeIndicator(iqrIndicator);
                if (result == null) result = caseCompositeIndicator(iqrIndicator);
                if (result == null) result = caseIndicator(iqrIndicator);
                if (result == null) result = caseModelElement(iqrIndicator);
                if (result == null) result = caseElement(iqrIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.TEXT_INDICATOR: {
                TextIndicator textIndicator = (TextIndicator)theEObject;
                T result = caseTextIndicator(textIndicator);
                if (result == null) result = caseCompositeIndicator(textIndicator);
                if (result == null) result = caseIndicator(textIndicator);
                if (result == null) result = caseModelElement(textIndicator);
                if (result == null) result = caseElement(textIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MIN_LENGTH_INDICATOR: {
                MinLengthIndicator minLengthIndicator = (MinLengthIndicator)theEObject;
                T result = caseMinLengthIndicator(minLengthIndicator);
                if (result == null) result = caseLengthIndicator(minLengthIndicator);
                if (result == null) result = caseIndicator(minLengthIndicator);
                if (result == null) result = caseModelElement(minLengthIndicator);
                if (result == null) result = caseElement(minLengthIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MIN_LENGTH_WITH_NULL_INDICATOR: {
                MinLengthWithNullIndicator minLengthWithNullIndicator = (MinLengthWithNullIndicator)theEObject;
                T result = caseMinLengthWithNullIndicator(minLengthWithNullIndicator);
                if (result == null) result = caseMinLengthIndicator(minLengthWithNullIndicator);
                if (result == null) result = caseLengthIndicator(minLengthWithNullIndicator);
                if (result == null) result = caseIndicator(minLengthWithNullIndicator);
                if (result == null) result = caseModelElement(minLengthWithNullIndicator);
                if (result == null) result = caseElement(minLengthWithNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MIN_LENGTH_WITH_BLANK_INDICATOR: {
                MinLengthWithBlankIndicator minLengthWithBlankIndicator = (MinLengthWithBlankIndicator)theEObject;
                T result = caseMinLengthWithBlankIndicator(minLengthWithBlankIndicator);
                if (result == null) result = caseMinLengthIndicator(minLengthWithBlankIndicator);
                if (result == null) result = caseLengthIndicator(minLengthWithBlankIndicator);
                if (result == null) result = caseIndicator(minLengthWithBlankIndicator);
                if (result == null) result = caseModelElement(minLengthWithBlankIndicator);
                if (result == null) result = caseElement(minLengthWithBlankIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MIN_LENGTH_WITH_BLANK_NULL_INDICATOR: {
                MinLengthWithBlankNullIndicator minLengthWithBlankNullIndicator = (MinLengthWithBlankNullIndicator)theEObject;
                T result = caseMinLengthWithBlankNullIndicator(minLengthWithBlankNullIndicator);
                if (result == null) result = caseMinLengthIndicator(minLengthWithBlankNullIndicator);
                if (result == null) result = caseLengthIndicator(minLengthWithBlankNullIndicator);
                if (result == null) result = caseIndicator(minLengthWithBlankNullIndicator);
                if (result == null) result = caseModelElement(minLengthWithBlankNullIndicator);
                if (result == null) result = caseElement(minLengthWithBlankNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MAX_LENGTH_INDICATOR: {
                MaxLengthIndicator maxLengthIndicator = (MaxLengthIndicator)theEObject;
                T result = caseMaxLengthIndicator(maxLengthIndicator);
                if (result == null) result = caseLengthIndicator(maxLengthIndicator);
                if (result == null) result = caseIndicator(maxLengthIndicator);
                if (result == null) result = caseModelElement(maxLengthIndicator);
                if (result == null) result = caseElement(maxLengthIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MAX_LENGTH_WITH_NULL_INDICATOR: {
                MaxLengthWithNullIndicator maxLengthWithNullIndicator = (MaxLengthWithNullIndicator)theEObject;
                T result = caseMaxLengthWithNullIndicator(maxLengthWithNullIndicator);
                if (result == null) result = caseMaxLengthIndicator(maxLengthWithNullIndicator);
                if (result == null) result = caseLengthIndicator(maxLengthWithNullIndicator);
                if (result == null) result = caseIndicator(maxLengthWithNullIndicator);
                if (result == null) result = caseModelElement(maxLengthWithNullIndicator);
                if (result == null) result = caseElement(maxLengthWithNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MAX_LENGTH_WITH_BLANK_INDICATOR: {
                MaxLengthWithBlankIndicator maxLengthWithBlankIndicator = (MaxLengthWithBlankIndicator)theEObject;
                T result = caseMaxLengthWithBlankIndicator(maxLengthWithBlankIndicator);
                if (result == null) result = caseMaxLengthIndicator(maxLengthWithBlankIndicator);
                if (result == null) result = caseLengthIndicator(maxLengthWithBlankIndicator);
                if (result == null) result = caseIndicator(maxLengthWithBlankIndicator);
                if (result == null) result = caseModelElement(maxLengthWithBlankIndicator);
                if (result == null) result = caseElement(maxLengthWithBlankIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MAX_LENGTH_WITH_BLANK_NULL_INDICATOR: {
                MaxLengthWithBlankNullIndicator maxLengthWithBlankNullIndicator = (MaxLengthWithBlankNullIndicator)theEObject;
                T result = caseMaxLengthWithBlankNullIndicator(maxLengthWithBlankNullIndicator);
                if (result == null) result = caseMaxLengthIndicator(maxLengthWithBlankNullIndicator);
                if (result == null) result = caseLengthIndicator(maxLengthWithBlankNullIndicator);
                if (result == null) result = caseIndicator(maxLengthWithBlankNullIndicator);
                if (result == null) result = caseModelElement(maxLengthWithBlankNullIndicator);
                if (result == null) result = caseElement(maxLengthWithBlankNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.AVERAGE_LENGTH_INDICATOR: {
                AverageLengthIndicator averageLengthIndicator = (AverageLengthIndicator)theEObject;
                T result = caseAverageLengthIndicator(averageLengthIndicator);
                if (result == null) result = caseLengthIndicator(averageLengthIndicator);
                if (result == null) result = caseIndicator(averageLengthIndicator);
                if (result == null) result = caseModelElement(averageLengthIndicator);
                if (result == null) result = caseElement(averageLengthIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.AVG_LENGTH_WITH_NULL_INDICATOR: {
                AvgLengthWithNullIndicator avgLengthWithNullIndicator = (AvgLengthWithNullIndicator)theEObject;
                T result = caseAvgLengthWithNullIndicator(avgLengthWithNullIndicator);
                if (result == null) result = caseAverageLengthIndicator(avgLengthWithNullIndicator);
                if (result == null) result = caseLengthIndicator(avgLengthWithNullIndicator);
                if (result == null) result = caseIndicator(avgLengthWithNullIndicator);
                if (result == null) result = caseModelElement(avgLengthWithNullIndicator);
                if (result == null) result = caseElement(avgLengthWithNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.AVG_LENGTH_WITH_BLANK_INDICATOR: {
                AvgLengthWithBlankIndicator avgLengthWithBlankIndicator = (AvgLengthWithBlankIndicator)theEObject;
                T result = caseAvgLengthWithBlankIndicator(avgLengthWithBlankIndicator);
                if (result == null) result = caseAverageLengthIndicator(avgLengthWithBlankIndicator);
                if (result == null) result = caseLengthIndicator(avgLengthWithBlankIndicator);
                if (result == null) result = caseIndicator(avgLengthWithBlankIndicator);
                if (result == null) result = caseModelElement(avgLengthWithBlankIndicator);
                if (result == null) result = caseElement(avgLengthWithBlankIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.AVG_LENGTH_WITH_BLANK_NULL_INDICATOR: {
                AvgLengthWithBlankNullIndicator avgLengthWithBlankNullIndicator = (AvgLengthWithBlankNullIndicator)theEObject;
                T result = caseAvgLengthWithBlankNullIndicator(avgLengthWithBlankNullIndicator);
                if (result == null) result = caseAverageLengthIndicator(avgLengthWithBlankNullIndicator);
                if (result == null) result = caseLengthIndicator(avgLengthWithBlankNullIndicator);
                if (result == null) result = caseIndicator(avgLengthWithBlankNullIndicator);
                if (result == null) result = caseModelElement(avgLengthWithBlankNullIndicator);
                if (result == null) result = caseElement(avgLengthWithBlankNullIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.LENGTH_INDICATOR: {
                LengthIndicator lengthIndicator = (LengthIndicator)theEObject;
                T result = caseLengthIndicator(lengthIndicator);
                if (result == null) result = caseIndicator(lengthIndicator);
                if (result == null) result = caseModelElement(lengthIndicator);
                if (result == null) result = caseElement(lengthIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.TEXT_PARAMETERS: {
                TextParameters textParameters = (TextParameters)theEObject;
                T result = caseTextParameters(textParameters);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.LOWER_QUARTILE_INDICATOR: {
                LowerQuartileIndicator lowerQuartileIndicator = (LowerQuartileIndicator)theEObject;
                T result = caseLowerQuartileIndicator(lowerQuartileIndicator);
                if (result == null) result = caseMinValueIndicator(lowerQuartileIndicator);
                if (result == null) result = caseValueIndicator(lowerQuartileIndicator);
                if (result == null) result = caseIndicator(lowerQuartileIndicator);
                if (result == null) result = caseModelElement(lowerQuartileIndicator);
                if (result == null) result = caseElement(lowerQuartileIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.UPPER_QUARTILE_INDICATOR: {
                UpperQuartileIndicator upperQuartileIndicator = (UpperQuartileIndicator)theEObject;
                T result = caseUpperQuartileIndicator(upperQuartileIndicator);
                if (result == null) result = caseMaxValueIndicator(upperQuartileIndicator);
                if (result == null) result = caseValueIndicator(upperQuartileIndicator);
                if (result == null) result = caseIndicator(upperQuartileIndicator);
                if (result == null) result = caseModelElement(upperQuartileIndicator);
                if (result == null) result = caseElement(upperQuartileIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.COUNTS_INDICATOR: {
                CountsIndicator countsIndicator = (CountsIndicator)theEObject;
                T result = caseCountsIndicator(countsIndicator);
                if (result == null) result = caseCompositeIndicator(countsIndicator);
                if (result == null) result = caseIndicator(countsIndicator);
                if (result == null) result = caseModelElement(countsIndicator);
                if (result == null) result = caseElement(countsIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DATE_PARAMETERS: {
                DateParameters dateParameters = (DateParameters)theEObject;
                T result = caseDateParameters(dateParameters);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.PATTERN_MATCHING_INDICATOR: {
                PatternMatchingIndicator patternMatchingIndicator = (PatternMatchingIndicator)theEObject;
                T result = casePatternMatchingIndicator(patternMatchingIndicator);
                if (result == null) result = caseMatchingIndicator(patternMatchingIndicator);
                if (result == null) result = caseIndicator(patternMatchingIndicator);
                if (result == null) result = caseModelElement(patternMatchingIndicator);
                if (result == null) result = caseElement(patternMatchingIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.SQL_PATTERN_MATCHING_INDICATOR: {
                SqlPatternMatchingIndicator sqlPatternMatchingIndicator = (SqlPatternMatchingIndicator)theEObject;
                T result = caseSqlPatternMatchingIndicator(sqlPatternMatchingIndicator);
                if (result == null) result = casePatternMatchingIndicator(sqlPatternMatchingIndicator);
                if (result == null) result = caseMatchingIndicator(sqlPatternMatchingIndicator);
                if (result == null) result = caseIndicator(sqlPatternMatchingIndicator);
                if (result == null) result = caseModelElement(sqlPatternMatchingIndicator);
                if (result == null) result = caseElement(sqlPatternMatchingIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.REGEXP_MATCHING_INDICATOR: {
                RegexpMatchingIndicator regexpMatchingIndicator = (RegexpMatchingIndicator)theEObject;
                T result = caseRegexpMatchingIndicator(regexpMatchingIndicator);
                if (result == null) result = casePatternMatchingIndicator(regexpMatchingIndicator);
                if (result == null) result = caseMatchingIndicator(regexpMatchingIndicator);
                if (result == null) result = caseIndicator(regexpMatchingIndicator);
                if (result == null) result = caseModelElement(regexpMatchingIndicator);
                if (result == null) result = caseElement(regexpMatchingIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MATCHING_INDICATOR: {
                MatchingIndicator matchingIndicator = (MatchingIndicator)theEObject;
                T result = caseMatchingIndicator(matchingIndicator);
                if (result == null) result = caseIndicator(matchingIndicator);
                if (result == null) result = caseModelElement(matchingIndicator);
                if (result == null) result = caseElement(matchingIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.LOW_FREQUENCY_INDICATOR: {
                LowFrequencyIndicator lowFrequencyIndicator = (LowFrequencyIndicator)theEObject;
                T result = caseLowFrequencyIndicator(lowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(lowFrequencyIndicator);
                if (result == null) result = caseIndicator(lowFrequencyIndicator);
                if (result == null) result = caseModelElement(lowFrequencyIndicator);
                if (result == null) result = caseElement(lowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.PATTERN_FREQ_INDICATOR: {
                PatternFreqIndicator patternFreqIndicator = (PatternFreqIndicator)theEObject;
                T result = casePatternFreqIndicator(patternFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(patternFreqIndicator);
                if (result == null) result = caseIndicator(patternFreqIndicator);
                if (result == null) result = caseModelElement(patternFreqIndicator);
                if (result == null) result = caseElement(patternFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.PATTERN_LOW_FREQ_INDICATOR: {
                PatternLowFreqIndicator patternLowFreqIndicator = (PatternLowFreqIndicator)theEObject;
                T result = casePatternLowFreqIndicator(patternLowFreqIndicator);
                if (result == null) result = caseLowFrequencyIndicator(patternLowFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(patternLowFreqIndicator);
                if (result == null) result = caseIndicator(patternLowFreqIndicator);
                if (result == null) result = caseModelElement(patternLowFreqIndicator);
                if (result == null) result = caseElement(patternLowFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR: {
                DefValueCountIndicator defValueCountIndicator = (DefValueCountIndicator)theEObject;
                T result = caseDefValueCountIndicator(defValueCountIndicator);
                if (result == null) result = caseIndicator(defValueCountIndicator);
                if (result == null) result = caseModelElement(defValueCountIndicator);
                if (result == null) result = caseElement(defValueCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.SOUNDEX_FREQ_INDICATOR: {
                SoundexFreqIndicator soundexFreqIndicator = (SoundexFreqIndicator)theEObject;
                T result = caseSoundexFreqIndicator(soundexFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(soundexFreqIndicator);
                if (result == null) result = caseIndicator(soundexFreqIndicator);
                if (result == null) result = caseModelElement(soundexFreqIndicator);
                if (result == null) result = caseElement(soundexFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.SOUNDEX_LOW_FREQ_INDICATOR: {
                SoundexLowFreqIndicator soundexLowFreqIndicator = (SoundexLowFreqIndicator)theEObject;
                T result = caseSoundexLowFreqIndicator(soundexLowFreqIndicator);
                if (result == null) result = caseSoundexFreqIndicator(soundexLowFreqIndicator);
                if (result == null) result = caseLowFrequencyIndicator(soundexLowFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(soundexLowFreqIndicator);
                if (result == null) result = caseIndicator(soundexLowFreqIndicator);
                if (result == null) result = caseModelElement(soundexLowFreqIndicator);
                if (result == null) result = caseElement(soundexLowFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DATE_PATTERN_FREQ_INDICATOR: {
                DatePatternFreqIndicator datePatternFreqIndicator = (DatePatternFreqIndicator)theEObject;
                T result = caseDatePatternFreqIndicator(datePatternFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(datePatternFreqIndicator);
                if (result == null) result = caseIndicator(datePatternFreqIndicator);
                if (result == null) result = caseModelElement(datePatternFreqIndicator);
                if (result == null) result = caseElement(datePatternFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DATE_FREQUENCY_INDICATOR: {
                DateFrequencyIndicator dateFrequencyIndicator = (DateFrequencyIndicator)theEObject;
                T result = caseDateFrequencyIndicator(dateFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(dateFrequencyIndicator);
                if (result == null) result = caseIndicator(dateFrequencyIndicator);
                if (result == null) result = caseModelElement(dateFrequencyIndicator);
                if (result == null) result = caseElement(dateFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.WEEK_FREQUENCY_INDICATOR: {
                WeekFrequencyIndicator weekFrequencyIndicator = (WeekFrequencyIndicator)theEObject;
                T result = caseWeekFrequencyIndicator(weekFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(weekFrequencyIndicator);
                if (result == null) result = caseIndicator(weekFrequencyIndicator);
                if (result == null) result = caseModelElement(weekFrequencyIndicator);
                if (result == null) result = caseElement(weekFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MONTH_FREQUENCY_INDICATOR: {
                MonthFrequencyIndicator monthFrequencyIndicator = (MonthFrequencyIndicator)theEObject;
                T result = caseMonthFrequencyIndicator(monthFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(monthFrequencyIndicator);
                if (result == null) result = caseIndicator(monthFrequencyIndicator);
                if (result == null) result = caseModelElement(monthFrequencyIndicator);
                if (result == null) result = caseElement(monthFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.QUARTER_FREQUENCY_INDICATOR: {
                QuarterFrequencyIndicator quarterFrequencyIndicator = (QuarterFrequencyIndicator)theEObject;
                T result = caseQuarterFrequencyIndicator(quarterFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(quarterFrequencyIndicator);
                if (result == null) result = caseIndicator(quarterFrequencyIndicator);
                if (result == null) result = caseModelElement(quarterFrequencyIndicator);
                if (result == null) result = caseElement(quarterFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.YEAR_FREQUENCY_INDICATOR: {
                YearFrequencyIndicator yearFrequencyIndicator = (YearFrequencyIndicator)theEObject;
                T result = caseYearFrequencyIndicator(yearFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(yearFrequencyIndicator);
                if (result == null) result = caseIndicator(yearFrequencyIndicator);
                if (result == null) result = caseModelElement(yearFrequencyIndicator);
                if (result == null) result = caseElement(yearFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.BIN_FREQUENCY_INDICATOR: {
                BinFrequencyIndicator binFrequencyIndicator = (BinFrequencyIndicator)theEObject;
                T result = caseBinFrequencyIndicator(binFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(binFrequencyIndicator);
                if (result == null) result = caseIndicator(binFrequencyIndicator);
                if (result == null) result = caseModelElement(binFrequencyIndicator);
                if (result == null) result = caseElement(binFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.DATE_LOW_FREQUENCY_INDICATOR: {
                DateLowFrequencyIndicator dateLowFrequencyIndicator = (DateLowFrequencyIndicator)theEObject;
                T result = caseDateLowFrequencyIndicator(dateLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(dateLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(dateLowFrequencyIndicator);
                if (result == null) result = caseIndicator(dateLowFrequencyIndicator);
                if (result == null) result = caseModelElement(dateLowFrequencyIndicator);
                if (result == null) result = caseElement(dateLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.WEEK_LOW_FREQUENCY_INDICATOR: {
                WeekLowFrequencyIndicator weekLowFrequencyIndicator = (WeekLowFrequencyIndicator)theEObject;
                T result = caseWeekLowFrequencyIndicator(weekLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(weekLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(weekLowFrequencyIndicator);
                if (result == null) result = caseIndicator(weekLowFrequencyIndicator);
                if (result == null) result = caseModelElement(weekLowFrequencyIndicator);
                if (result == null) result = caseElement(weekLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.MONTH_LOW_FREQUENCY_INDICATOR: {
                MonthLowFrequencyIndicator monthLowFrequencyIndicator = (MonthLowFrequencyIndicator)theEObject;
                T result = caseMonthLowFrequencyIndicator(monthLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(monthLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(monthLowFrequencyIndicator);
                if (result == null) result = caseIndicator(monthLowFrequencyIndicator);
                if (result == null) result = caseModelElement(monthLowFrequencyIndicator);
                if (result == null) result = caseElement(monthLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.QUARTER_LOW_FREQUENCY_INDICATOR: {
                QuarterLowFrequencyIndicator quarterLowFrequencyIndicator = (QuarterLowFrequencyIndicator)theEObject;
                T result = caseQuarterLowFrequencyIndicator(quarterLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(quarterLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(quarterLowFrequencyIndicator);
                if (result == null) result = caseIndicator(quarterLowFrequencyIndicator);
                if (result == null) result = caseModelElement(quarterLowFrequencyIndicator);
                if (result == null) result = caseElement(quarterLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.YEAR_LOW_FREQUENCY_INDICATOR: {
                YearLowFrequencyIndicator yearLowFrequencyIndicator = (YearLowFrequencyIndicator)theEObject;
                T result = caseYearLowFrequencyIndicator(yearLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(yearLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(yearLowFrequencyIndicator);
                if (result == null) result = caseIndicator(yearLowFrequencyIndicator);
                if (result == null) result = caseModelElement(yearLowFrequencyIndicator);
                if (result == null) result = caseElement(yearLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.BIN_LOW_FREQUENCY_INDICATOR: {
                BinLowFrequencyIndicator binLowFrequencyIndicator = (BinLowFrequencyIndicator)theEObject;
                T result = caseBinLowFrequencyIndicator(binLowFrequencyIndicator);
                if (result == null) result = caseLowFrequencyIndicator(binLowFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(binLowFrequencyIndicator);
                if (result == null) result = caseIndicator(binLowFrequencyIndicator);
                if (result == null) result = caseModelElement(binLowFrequencyIndicator);
                if (result == null) result = caseElement(binLowFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.VALID_PHONE_COUNT_INDICATOR: {
                ValidPhoneCountIndicator validPhoneCountIndicator = (ValidPhoneCountIndicator)theEObject;
                T result = caseValidPhoneCountIndicator(validPhoneCountIndicator);
                if (result == null) result = caseIndicator(validPhoneCountIndicator);
                if (result == null) result = caseModelElement(validPhoneCountIndicator);
                if (result == null) result = caseElement(validPhoneCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.POSSIBLE_PHONE_COUNT_INDICATOR: {
                PossiblePhoneCountIndicator possiblePhoneCountIndicator = (PossiblePhoneCountIndicator)theEObject;
                T result = casePossiblePhoneCountIndicator(possiblePhoneCountIndicator);
                if (result == null) result = caseIndicator(possiblePhoneCountIndicator);
                if (result == null) result = caseModelElement(possiblePhoneCountIndicator);
                if (result == null) result = caseElement(possiblePhoneCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.VALID_REG_CODE_COUNT_INDICATOR: {
                ValidRegCodeCountIndicator validRegCodeCountIndicator = (ValidRegCodeCountIndicator)theEObject;
                T result = caseValidRegCodeCountIndicator(validRegCodeCountIndicator);
                if (result == null) result = caseIndicator(validRegCodeCountIndicator);
                if (result == null) result = caseModelElement(validRegCodeCountIndicator);
                if (result == null) result = caseElement(validRegCodeCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.INVALID_REG_CODE_COUNT_INDICATOR: {
                InvalidRegCodeCountIndicator invalidRegCodeCountIndicator = (InvalidRegCodeCountIndicator)theEObject;
                T result = caseInvalidRegCodeCountIndicator(invalidRegCodeCountIndicator);
                if (result == null) result = caseIndicator(invalidRegCodeCountIndicator);
                if (result == null) result = caseModelElement(invalidRegCodeCountIndicator);
                if (result == null) result = caseElement(invalidRegCodeCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR: {
                WellFormNationalPhoneCountIndicator wellFormNationalPhoneCountIndicator = (WellFormNationalPhoneCountIndicator)theEObject;
                T result = caseWellFormNationalPhoneCountIndicator(wellFormNationalPhoneCountIndicator);
                if (result == null) result = caseIndicator(wellFormNationalPhoneCountIndicator);
                if (result == null) result = caseModelElement(wellFormNationalPhoneCountIndicator);
                if (result == null) result = caseElement(wellFormNationalPhoneCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.WELL_FORM_INTE_PHONE_COUNT_INDICATOR: {
                WellFormIntePhoneCountIndicator wellFormIntePhoneCountIndicator = (WellFormIntePhoneCountIndicator)theEObject;
                T result = caseWellFormIntePhoneCountIndicator(wellFormIntePhoneCountIndicator);
                if (result == null) result = caseIndicator(wellFormIntePhoneCountIndicator);
                if (result == null) result = caseModelElement(wellFormIntePhoneCountIndicator);
                if (result == null) result = caseElement(wellFormIntePhoneCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.WELL_FORM_E164_PHONE_COUNT_INDICATOR: {
                WellFormE164PhoneCountIndicator wellFormE164PhoneCountIndicator = (WellFormE164PhoneCountIndicator)theEObject;
                T result = caseWellFormE164PhoneCountIndicator(wellFormE164PhoneCountIndicator);
                if (result == null) result = caseIndicator(wellFormE164PhoneCountIndicator);
                if (result == null) result = caseModelElement(wellFormE164PhoneCountIndicator);
                if (result == null) result = caseElement(wellFormE164PhoneCountIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR: {
                PhoneNumbStatisticsIndicator phoneNumbStatisticsIndicator = (PhoneNumbStatisticsIndicator)theEObject;
                T result = casePhoneNumbStatisticsIndicator(phoneNumbStatisticsIndicator);
                if (result == null) result = caseCompositeIndicator(phoneNumbStatisticsIndicator);
                if (result == null) result = caseIndicator(phoneNumbStatisticsIndicator);
                if (result == null) result = caseModelElement(phoneNumbStatisticsIndicator);
                if (result == null) result = caseElement(phoneNumbStatisticsIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.FORMAT_FREQ_PIE_INDICATOR: {
                FormatFreqPieIndicator formatFreqPieIndicator = (FormatFreqPieIndicator)theEObject;
                T result = caseFormatFreqPieIndicator(formatFreqPieIndicator);
                if (result == null) result = caseFrequencyIndicator(formatFreqPieIndicator);
                if (result == null) result = caseIndicator(formatFreqPieIndicator);
                if (result == null) result = caseModelElement(formatFreqPieIndicator);
                if (result == null) result = caseElement(formatFreqPieIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.BENFORD_LAW_FREQUENCY_INDICATOR: {
                BenfordLawFrequencyIndicator benfordLawFrequencyIndicator = (BenfordLawFrequencyIndicator)theEObject;
                T result = caseBenfordLawFrequencyIndicator(benfordLawFrequencyIndicator);
                if (result == null) result = caseFrequencyIndicator(benfordLawFrequencyIndicator);
                if (result == null) result = caseIndicator(benfordLawFrequencyIndicator);
                if (result == null) result = caseModelElement(benfordLawFrequencyIndicator);
                if (result == null) result = caseElement(benfordLawFrequencyIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.EAST_ASIA_PATTERN_FREQ_INDICATOR: {
                EastAsiaPatternFreqIndicator eastAsiaPatternFreqIndicator = (EastAsiaPatternFreqIndicator)theEObject;
                T result = caseEastAsiaPatternFreqIndicator(eastAsiaPatternFreqIndicator);
                if (result == null) result = casePatternFreqIndicator(eastAsiaPatternFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(eastAsiaPatternFreqIndicator);
                if (result == null) result = caseIndicator(eastAsiaPatternFreqIndicator);
                if (result == null) result = caseModelElement(eastAsiaPatternFreqIndicator);
                if (result == null) result = caseElement(eastAsiaPatternFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR: {
                EastAsiaPatternLowFreqIndicator eastAsiaPatternLowFreqIndicator = (EastAsiaPatternLowFreqIndicator)theEObject;
                T result = caseEastAsiaPatternLowFreqIndicator(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = casePatternLowFreqIndicator(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = caseLowFrequencyIndicator(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = caseIndicator(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = caseModelElement(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = caseElement(eastAsiaPatternLowFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.CS_WORD_PATTERN_FREQ_INDICATOR: {
                CSWordPatternFreqIndicator csWordPatternFreqIndicator = (CSWordPatternFreqIndicator)theEObject;
                T result = caseCSWordPatternFreqIndicator(csWordPatternFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(csWordPatternFreqIndicator);
                if (result == null) result = caseIndicator(csWordPatternFreqIndicator);
                if (result == null) result = caseModelElement(csWordPatternFreqIndicator);
                if (result == null) result = caseElement(csWordPatternFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.CS_WORD_PATTERN_LOW_FREQ_INDICATOR: {
                CSWordPatternLowFreqIndicator csWordPatternLowFreqIndicator = (CSWordPatternLowFreqIndicator)theEObject;
                T result = caseCSWordPatternLowFreqIndicator(csWordPatternLowFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(csWordPatternLowFreqIndicator);
                if (result == null) result = caseIndicator(csWordPatternLowFreqIndicator);
                if (result == null) result = caseModelElement(csWordPatternLowFreqIndicator);
                if (result == null) result = caseElement(csWordPatternLowFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.CI_WORD_PATTERN_FREQ_INDICATOR: {
                CIWordPatternFreqIndicator ciWordPatternFreqIndicator = (CIWordPatternFreqIndicator)theEObject;
                T result = caseCIWordPatternFreqIndicator(ciWordPatternFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(ciWordPatternFreqIndicator);
                if (result == null) result = caseIndicator(ciWordPatternFreqIndicator);
                if (result == null) result = caseModelElement(ciWordPatternFreqIndicator);
                if (result == null) result = caseElement(ciWordPatternFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case IndicatorsPackage.CI_WORD_PATTERN_LOW_FREQ_INDICATOR: {
                CIWordPatternLowFreqIndicator ciWordPatternLowFreqIndicator = (CIWordPatternLowFreqIndicator)theEObject;
                T result = caseCIWordPatternLowFreqIndicator(ciWordPatternLowFreqIndicator);
                if (result == null) result = caseFrequencyIndicator(ciWordPatternLowFreqIndicator);
                if (result == null) result = caseIndicator(ciWordPatternLowFreqIndicator);
                if (result == null) result = caseModelElement(ciWordPatternLowFreqIndicator);
                if (result == null) result = caseElement(ciWordPatternLowFreqIndicator);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIndicator(Indicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Row Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Row Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRowCountIndicator(RowCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Mean Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Mean Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMeanIndicator(MeanIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sum Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sum Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSumIndicator(SumIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeIndicator(CompositeIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRangeIndicator(RangeIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Box Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Box Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoxIndicator(BoxIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFrequencyIndicator(FrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Blank Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Blank Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBlankCountIndicator(BlankCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Indicator Parameters</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Indicator Parameters</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIndicatorParameters(IndicatorParameters object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Median Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Median Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMedianIndicator(MedianIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValueIndicator(ValueIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Min Value Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Min Value Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMinValueIndicator(MinValueIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Max Value Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Max Value Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaxValueIndicator(MaxValueIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Mode Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Mode Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModeIndicator(ModeIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Null Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Null Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNullCountIndicator(NullCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Distinct Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Distinct Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDistinctCountIndicator(DistinctCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unique Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unique Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUniqueCountIndicator(UniqueCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Duplicate Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Duplicate Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDuplicateCountIndicator(DuplicateCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>IQR Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>IQR Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIQRIndicator(IQRIndicator object) {
        return null;
    }
    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextIndicator(TextIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>UpperQuartile Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>UpperQuartile Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     *  MODRLI
     */
    public T caseUpperQuartileIndicator(UpperQuartileIndicator object) {
        return null;
    }
    /**
     * Returns the result of interpreting the object as an instance of '<em>Counts Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Counts Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCountsIndicator(CountsIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Date Parameters</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Date Parameters</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDateParameters(DateParameters object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pattern Matching Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pattern Matching Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePatternMatchingIndicator(PatternMatchingIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sql Pattern Matching Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sql Pattern Matching Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSqlPatternMatchingIndicator(SqlPatternMatchingIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Regexp Matching Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Regexp Matching Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRegexpMatchingIndicator(RegexpMatchingIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Matching Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Matching Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatchingIndicator(MatchingIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLowFrequencyIndicator(LowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pattern Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pattern Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePatternFreqIndicator(PatternFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pattern Low Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pattern Low Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePatternLowFreqIndicator(PatternLowFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Def Value Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Def Value Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefValueCountIndicator(DefValueCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Soundex Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Soundex Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSoundexFreqIndicator(SoundexFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Soundex Low Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Soundex Low Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSoundexLowFreqIndicator(SoundexLowFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Date Pattern Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Date Pattern Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDatePatternFreqIndicator(DatePatternFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Date Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Date Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDateFrequencyIndicator(DateFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Week Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Week Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWeekFrequencyIndicator(WeekFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Month Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Month Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMonthFrequencyIndicator(MonthFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Quarter Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Quarter Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuarterFrequencyIndicator(QuarterFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Year Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Year Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseYearFrequencyIndicator(YearFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bin Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bin Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinFrequencyIndicator(BinFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Date Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Date Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDateLowFrequencyIndicator(DateLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Week Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Week Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWeekLowFrequencyIndicator(WeekLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Month Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Month Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMonthLowFrequencyIndicator(MonthLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Quarter Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Quarter Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuarterLowFrequencyIndicator(QuarterLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Year Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Year Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseYearLowFrequencyIndicator(YearLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bin Low Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bin Low Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinLowFrequencyIndicator(BinLowFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Valid Phone Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Valid Phone Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValidPhoneCountIndicator(ValidPhoneCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Possible Phone Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Possible Phone Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePossiblePhoneCountIndicator(PossiblePhoneCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Valid Reg Code Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Valid Reg Code Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValidRegCodeCountIndicator(ValidRegCodeCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Invalid Reg Code Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invalid Reg Code Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Well Form National Phone Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Well Form National Phone Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Well Form Inte Phone Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Well Form Inte Phone Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Well Form E164 Phone Count Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Well Form E164 Phone Count Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Phone Numb Statistics Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Phone Numb Statistics Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePhoneNumbStatisticsIndicator(PhoneNumbStatisticsIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Format Freq Pie Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Format Freq Pie Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFormatFreqPieIndicator(FormatFreqPieIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Benford Law Frequency Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Benford Law Frequency Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBenfordLawFrequencyIndicator(BenfordLawFrequencyIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>East Asia Pattern Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>East Asia Pattern Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEastAsiaPatternFreqIndicator(EastAsiaPatternFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>East Asia Pattern Low Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>East Asia Pattern Low Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEastAsiaPatternLowFreqIndicator(EastAsiaPatternLowFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CS Word Pattern Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CS Word Pattern Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCSWordPatternFreqIndicator(CSWordPatternFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CS Word Pattern Low Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CS Word Pattern Low Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCSWordPatternLowFreqIndicator(CSWordPatternLowFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CI Word Pattern Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CI Word Pattern Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCIWordPatternFreqIndicator(CIWordPatternFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CI Word Pattern Low Freq Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CI Word Pattern Low Freq Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCIWordPatternLowFreqIndicator(CIWordPatternLowFreqIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>LowerQuartile Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>LowerQuartile Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * MODRLI
     */
    public T caseLowerQuartileIndicator(LowerQuartileIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Min Length Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Min Length Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMinLengthIndicator(MinLengthIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Min Length With Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Min Length With Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMinLengthWithNullIndicator(MinLengthWithNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Min Length With Blank Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Min Length With Blank Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMinLengthWithBlankIndicator(MinLengthWithBlankIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Min Length With Blank Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Min Length With Blank Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMinLengthWithBlankNullIndicator(MinLengthWithBlankNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Max Length Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Max Length Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaxLengthIndicator(MaxLengthIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Max Length With Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Max Length With Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaxLengthWithNullIndicator(MaxLengthWithNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Max Length With Blank Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Max Length With Blank Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaxLengthWithBlankIndicator(MaxLengthWithBlankIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Max Length With Blank Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Max Length With Blank Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaxLengthWithBlankNullIndicator(MaxLengthWithBlankNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Average Length Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Average Length Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAverageLengthIndicator(AverageLengthIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Avg Length With Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Avg Length With Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAvgLengthWithNullIndicator(AvgLengthWithNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Avg Length With Blank Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Avg Length With Blank Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAvgLengthWithBlankIndicator(AvgLengthWithBlankIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Avg Length With Blank Null Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Avg Length With Blank Null Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAvgLengthWithBlankNullIndicator(AvgLengthWithBlankNullIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Length Indicator</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Length Indicator</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLengthIndicator(LengthIndicator object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Parameters</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Parameters</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextParameters(TextParameters object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelElement(ModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }

} //IndicatorsSwitch
