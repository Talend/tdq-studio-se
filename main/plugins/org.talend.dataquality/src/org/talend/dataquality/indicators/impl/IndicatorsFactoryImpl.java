/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
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
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.DateFrequencyIndicator;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.DateLowFrequencyIndicator;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.DefValueCountIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.EnumStatistics;
import org.talend.dataquality.indicators.FormatFreqPieIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IQRIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorValueType;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.InvalidRegCodeCountIndicator;
import org.talend.dataquality.indicators.LengthIndicator;
import org.talend.dataquality.indicators.LowFrequencyIndicator;
import org.talend.dataquality.indicators.LowerQuartileIndicator;
import org.talend.dataquality.indicators.MatchingAlgorithm;
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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IndicatorsFactoryImpl extends EFactoryImpl implements IndicatorsFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static IndicatorsFactory init() {
        try {
            IndicatorsFactory theIndicatorsFactory = (IndicatorsFactory)EPackage.Registry.INSTANCE.getEFactory(IndicatorsPackage.eNS_URI);
            if (theIndicatorsFactory != null) {
                return theIndicatorsFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new IndicatorsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorsFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case IndicatorsPackage.INDICATOR: return createIndicator();
            case IndicatorsPackage.ROW_COUNT_INDICATOR: return createRowCountIndicator();
            case IndicatorsPackage.MEAN_INDICATOR: return createMeanIndicator();
            case IndicatorsPackage.SUM_INDICATOR: return createSumIndicator();
            case IndicatorsPackage.COMPOSITE_INDICATOR: return createCompositeIndicator();
            case IndicatorsPackage.RANGE_INDICATOR: return createRangeIndicator();
            case IndicatorsPackage.BOX_INDICATOR: return createBoxIndicator();
            case IndicatorsPackage.FREQUENCY_INDICATOR: return createFrequencyIndicator();
            case IndicatorsPackage.BLANK_COUNT_INDICATOR: return createBlankCountIndicator();
            case IndicatorsPackage.INDICATOR_PARAMETERS: return createIndicatorParameters();
            case IndicatorsPackage.MEDIAN_INDICATOR: return createMedianIndicator();
            case IndicatorsPackage.VALUE_INDICATOR: return createValueIndicator();
            case IndicatorsPackage.MIN_VALUE_INDICATOR: return createMinValueIndicator();
            case IndicatorsPackage.MAX_VALUE_INDICATOR: return createMaxValueIndicator();
            case IndicatorsPackage.MODE_INDICATOR: return createModeIndicator();
            case IndicatorsPackage.NULL_COUNT_INDICATOR: return createNullCountIndicator();
            case IndicatorsPackage.DISTINCT_COUNT_INDICATOR: return createDistinctCountIndicator();
            case IndicatorsPackage.UNIQUE_COUNT_INDICATOR: return createUniqueCountIndicator();
            case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR: return createDuplicateCountIndicator();
            case IndicatorsPackage.IQR_INDICATOR: return createIQRIndicator();
            case IndicatorsPackage.TEXT_INDICATOR: return createTextIndicator();
            case IndicatorsPackage.MIN_LENGTH_INDICATOR: return createMinLengthIndicator();
            case IndicatorsPackage.MIN_LENGTH_WITH_NULL_INDICATOR: return createMinLengthWithNullIndicator();
            case IndicatorsPackage.MIN_LENGTH_WITH_BLANK_INDICATOR: return createMinLengthWithBlankIndicator();
            case IndicatorsPackage.MIN_LENGTH_WITH_BLANK_NULL_INDICATOR: return createMinLengthWithBlankNullIndicator();
            case IndicatorsPackage.MAX_LENGTH_INDICATOR: return createMaxLengthIndicator();
            case IndicatorsPackage.MAX_LENGTH_WITH_NULL_INDICATOR: return createMaxLengthWithNullIndicator();
            case IndicatorsPackage.MAX_LENGTH_WITH_BLANK_INDICATOR: return createMaxLengthWithBlankIndicator();
            case IndicatorsPackage.MAX_LENGTH_WITH_BLANK_NULL_INDICATOR: return createMaxLengthWithBlankNullIndicator();
            case IndicatorsPackage.AVERAGE_LENGTH_INDICATOR: return createAverageLengthIndicator();
            case IndicatorsPackage.AVG_LENGTH_WITH_NULL_INDICATOR: return createAvgLengthWithNullIndicator();
            case IndicatorsPackage.AVG_LENGTH_WITH_BLANK_INDICATOR: return createAvgLengthWithBlankIndicator();
            case IndicatorsPackage.AVG_LENGTH_WITH_BLANK_NULL_INDICATOR: return createAvgLengthWithBlankNullIndicator();
            case IndicatorsPackage.LENGTH_INDICATOR: return createLengthIndicator();
            case IndicatorsPackage.TEXT_PARAMETERS: return createTextParameters();
            case IndicatorsPackage.LOWER_QUARTILE_INDICATOR: return createLowerQuartileIndicator();
            case IndicatorsPackage.UPPER_QUARTILE_INDICATOR: return createUpperQuartileIndicator();
            case IndicatorsPackage.COUNTS_INDICATOR: return createCountsIndicator();
            case IndicatorsPackage.DATE_PARAMETERS: return createDateParameters();
            case IndicatorsPackage.SQL_PATTERN_MATCHING_INDICATOR: return createSqlPatternMatchingIndicator();
            case IndicatorsPackage.REGEXP_MATCHING_INDICATOR: return createRegexpMatchingIndicator();
            case IndicatorsPackage.LOW_FREQUENCY_INDICATOR: return createLowFrequencyIndicator();
            case IndicatorsPackage.PATTERN_FREQ_INDICATOR: return createPatternFreqIndicator();
            case IndicatorsPackage.PATTERN_LOW_FREQ_INDICATOR: return createPatternLowFreqIndicator();
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR: return createDefValueCountIndicator();
            case IndicatorsPackage.SOUNDEX_FREQ_INDICATOR: return createSoundexFreqIndicator();
            case IndicatorsPackage.SOUNDEX_LOW_FREQ_INDICATOR: return createSoundexLowFreqIndicator();
            case IndicatorsPackage.DATE_PATTERN_FREQ_INDICATOR: return createDatePatternFreqIndicator();
            case IndicatorsPackage.DATE_FREQUENCY_INDICATOR: return createDateFrequencyIndicator();
            case IndicatorsPackage.WEEK_FREQUENCY_INDICATOR: return createWeekFrequencyIndicator();
            case IndicatorsPackage.MONTH_FREQUENCY_INDICATOR: return createMonthFrequencyIndicator();
            case IndicatorsPackage.QUARTER_FREQUENCY_INDICATOR: return createQuarterFrequencyIndicator();
            case IndicatorsPackage.YEAR_FREQUENCY_INDICATOR: return createYearFrequencyIndicator();
            case IndicatorsPackage.BIN_FREQUENCY_INDICATOR: return createBinFrequencyIndicator();
            case IndicatorsPackage.DATE_LOW_FREQUENCY_INDICATOR: return createDateLowFrequencyIndicator();
            case IndicatorsPackage.WEEK_LOW_FREQUENCY_INDICATOR: return createWeekLowFrequencyIndicator();
            case IndicatorsPackage.MONTH_LOW_FREQUENCY_INDICATOR: return createMonthLowFrequencyIndicator();
            case IndicatorsPackage.QUARTER_LOW_FREQUENCY_INDICATOR: return createQuarterLowFrequencyIndicator();
            case IndicatorsPackage.YEAR_LOW_FREQUENCY_INDICATOR: return createYearLowFrequencyIndicator();
            case IndicatorsPackage.BIN_LOW_FREQUENCY_INDICATOR: return createBinLowFrequencyIndicator();
            case IndicatorsPackage.VALID_PHONE_COUNT_INDICATOR: return createValidPhoneCountIndicator();
            case IndicatorsPackage.POSSIBLE_PHONE_COUNT_INDICATOR: return createPossiblePhoneCountIndicator();
            case IndicatorsPackage.VALID_REG_CODE_COUNT_INDICATOR: return createValidRegCodeCountIndicator();
            case IndicatorsPackage.INVALID_REG_CODE_COUNT_INDICATOR: return createInvalidRegCodeCountIndicator();
            case IndicatorsPackage.WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR: return createWellFormNationalPhoneCountIndicator();
            case IndicatorsPackage.WELL_FORM_INTE_PHONE_COUNT_INDICATOR: return createWellFormIntePhoneCountIndicator();
            case IndicatorsPackage.WELL_FORM_E164_PHONE_COUNT_INDICATOR: return createWellFormE164PhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR: return createPhoneNumbStatisticsIndicator();
            case IndicatorsPackage.FORMAT_FREQ_PIE_INDICATOR: return createFormatFreqPieIndicator();
            case IndicatorsPackage.BENFORD_LAW_FREQUENCY_INDICATOR: return createBenfordLawFrequencyIndicator();
            case IndicatorsPackage.EAST_ASIA_PATTERN_FREQ_INDICATOR: return createEastAsiaPatternFreqIndicator();
            case IndicatorsPackage.EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR: return createEastAsiaPatternLowFreqIndicator();
            case IndicatorsPackage.CS_WORD_PATTERN_FREQ_INDICATOR: return createCSWordPatternFreqIndicator();
            case IndicatorsPackage.CS_WORD_PATTERN_LOW_FREQ_INDICATOR: return createCSWordPatternLowFreqIndicator();
            case IndicatorsPackage.CI_WORD_PATTERN_FREQ_INDICATOR: return createCIWordPatternFreqIndicator();
            case IndicatorsPackage.CI_WORD_PATTERN_LOW_FREQ_INDICATOR: return createCIWordPatternLowFreqIndicator();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case IndicatorsPackage.ENUM_STATISTICS:
                return createEnumStatisticsFromString(eDataType, initialValue);
            case IndicatorsPackage.DATAMINING_TYPE:
                return createDataminingTypeFromString(eDataType, initialValue);
            case IndicatorsPackage.DATE_GRAIN:
                return createDateGrainFromString(eDataType, initialValue);
            case IndicatorsPackage.MATCHING_ALGORITHM:
                return createMatchingAlgorithmFromString(eDataType, initialValue);
            case IndicatorsPackage.INDICATOR_VALUE_TYPE:
                return createIndicatorValueTypeFromString(eDataType, initialValue);
            case IndicatorsPackage.JAVA_SET:
                return createJavaSetFromString(eDataType, initialValue);
            case IndicatorsPackage.JAVA_HASH_MAP:
                return createJavaHashMapFromString(eDataType, initialValue);
            case IndicatorsPackage.JAVA_TREE_MAP:
                return createJavaTreeMapFromString(eDataType, initialValue);
            case IndicatorsPackage.OBJECT_ARRAY:
                return createObjectArrayFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case IndicatorsPackage.ENUM_STATISTICS:
                return convertEnumStatisticsToString(eDataType, instanceValue);
            case IndicatorsPackage.DATAMINING_TYPE:
                return convertDataminingTypeToString(eDataType, instanceValue);
            case IndicatorsPackage.DATE_GRAIN:
                return convertDateGrainToString(eDataType, instanceValue);
            case IndicatorsPackage.MATCHING_ALGORITHM:
                return convertMatchingAlgorithmToString(eDataType, instanceValue);
            case IndicatorsPackage.INDICATOR_VALUE_TYPE:
                return convertIndicatorValueTypeToString(eDataType, instanceValue);
            case IndicatorsPackage.JAVA_SET:
                return convertJavaSetToString(eDataType, instanceValue);
            case IndicatorsPackage.JAVA_HASH_MAP:
                return convertJavaHashMapToString(eDataType, instanceValue);
            case IndicatorsPackage.JAVA_TREE_MAP:
                return convertJavaTreeMapToString(eDataType, instanceValue);
            case IndicatorsPackage.OBJECT_ARRAY:
                return convertObjectArrayToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Indicator createIndicator() {
        IndicatorImpl indicator = new IndicatorImpl();
        return indicator;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RowCountIndicator createRowCountIndicator() {
        RowCountIndicatorImpl rowCountIndicator = new RowCountIndicatorImpl();
        return rowCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeanIndicator createMeanIndicator() {
        MeanIndicatorImpl meanIndicator = new MeanIndicatorImpl();
        return meanIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SumIndicator createSumIndicator() {
        SumIndicatorImpl sumIndicator = new SumIndicatorImpl();
        return sumIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeIndicator createCompositeIndicator() {
        CompositeIndicatorImpl compositeIndicator = new CompositeIndicatorImpl();
        return compositeIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeIndicator createRangeIndicator() {
        RangeIndicatorImpl rangeIndicator = new RangeIndicatorImpl();
        return rangeIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoxIndicator createBoxIndicator() {
        BoxIndicatorImpl boxIndicator = new BoxIndicatorImpl();
        return boxIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FrequencyIndicator createFrequencyIndicator() {
        FrequencyIndicatorImpl frequencyIndicator = new FrequencyIndicatorImpl();
        return frequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BlankCountIndicator createBlankCountIndicator() {
        BlankCountIndicatorImpl blankCountIndicator = new BlankCountIndicatorImpl();
        return blankCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorParameters createIndicatorParameters() {
        IndicatorParametersImpl indicatorParameters = new IndicatorParametersImpl();
        return indicatorParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MedianIndicator createMedianIndicator() {
        MedianIndicatorImpl medianIndicator = new MedianIndicatorImpl();
        return medianIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueIndicator createValueIndicator() {
        ValueIndicatorImpl valueIndicator = new ValueIndicatorImpl();
        return valueIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MinValueIndicator createMinValueIndicator() {
        MinValueIndicatorImpl minValueIndicator = new MinValueIndicatorImpl();
        return minValueIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MaxValueIndicator createMaxValueIndicator() {
        MaxValueIndicatorImpl maxValueIndicator = new MaxValueIndicatorImpl();
        return maxValueIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ModeIndicator createModeIndicator() {
        ModeIndicatorImpl modeIndicator = new ModeIndicatorImpl();
        return modeIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NullCountIndicator createNullCountIndicator() {
        NullCountIndicatorImpl nullCountIndicator = new NullCountIndicatorImpl();
        return nullCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DistinctCountIndicator createDistinctCountIndicator() {
        DistinctCountIndicatorImpl distinctCountIndicator = new DistinctCountIndicatorImpl();
        return distinctCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UniqueCountIndicator createUniqueCountIndicator() {
        UniqueCountIndicatorImpl uniqueCountIndicator = new UniqueCountIndicatorImpl();
        return uniqueCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DuplicateCountIndicator createDuplicateCountIndicator() {
        DuplicateCountIndicatorImpl duplicateCountIndicator = new DuplicateCountIndicatorImpl();
        return duplicateCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IQRIndicator createIQRIndicator() {
        IQRIndicatorImpl iqrIndicator = new IQRIndicatorImpl();
        return iqrIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TextIndicator createTextIndicator() {
        TextIndicatorImpl textIndicator = new TextIndicatorImpl();
        return textIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MinLengthIndicator createMinLengthIndicator() {
        MinLengthIndicatorImpl minLengthIndicator = new MinLengthIndicatorImpl();
        return minLengthIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MinLengthWithNullIndicator createMinLengthWithNullIndicator() {
        MinLengthWithNullIndicatorImpl minLengthWithNullIndicator = new MinLengthWithNullIndicatorImpl();
        return minLengthWithNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MinLengthWithBlankIndicator createMinLengthWithBlankIndicator() {
        MinLengthWithBlankIndicatorImpl minLengthWithBlankIndicator = new MinLengthWithBlankIndicatorImpl();
        return minLengthWithBlankIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MinLengthWithBlankNullIndicator createMinLengthWithBlankNullIndicator() {
        MinLengthWithBlankNullIndicatorImpl minLengthWithBlankNullIndicator = new MinLengthWithBlankNullIndicatorImpl();
        return minLengthWithBlankNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MaxLengthIndicator createMaxLengthIndicator() {
        MaxLengthIndicatorImpl maxLengthIndicator = new MaxLengthIndicatorImpl();
        return maxLengthIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MaxLengthWithNullIndicator createMaxLengthWithNullIndicator() {
        MaxLengthWithNullIndicatorImpl maxLengthWithNullIndicator = new MaxLengthWithNullIndicatorImpl();
        return maxLengthWithNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MaxLengthWithBlankIndicator createMaxLengthWithBlankIndicator() {
        MaxLengthWithBlankIndicatorImpl maxLengthWithBlankIndicator = new MaxLengthWithBlankIndicatorImpl();
        return maxLengthWithBlankIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MaxLengthWithBlankNullIndicator createMaxLengthWithBlankNullIndicator() {
        MaxLengthWithBlankNullIndicatorImpl maxLengthWithBlankNullIndicator = new MaxLengthWithBlankNullIndicatorImpl();
        return maxLengthWithBlankNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AverageLengthIndicator createAverageLengthIndicator() {
        AverageLengthIndicatorImpl averageLengthIndicator = new AverageLengthIndicatorImpl();
        return averageLengthIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvgLengthWithNullIndicator createAvgLengthWithNullIndicator() {
        AvgLengthWithNullIndicatorImpl avgLengthWithNullIndicator = new AvgLengthWithNullIndicatorImpl();
        return avgLengthWithNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvgLengthWithBlankIndicator createAvgLengthWithBlankIndicator() {
        AvgLengthWithBlankIndicatorImpl avgLengthWithBlankIndicator = new AvgLengthWithBlankIndicatorImpl();
        return avgLengthWithBlankIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvgLengthWithBlankNullIndicator createAvgLengthWithBlankNullIndicator() {
        AvgLengthWithBlankNullIndicatorImpl avgLengthWithBlankNullIndicator = new AvgLengthWithBlankNullIndicatorImpl();
        return avgLengthWithBlankNullIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LengthIndicator createLengthIndicator() {
        LengthIndicatorImpl lengthIndicator = new LengthIndicatorImpl();
        return lengthIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TextParameters createTextParameters() {
        TextParametersImpl textParameters = new TextParametersImpl();
        return textParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LowerQuartileIndicator createLowerQuartileIndicator() {
        LowerQuartileIndicatorImpl lowerQuartileIndicator = new LowerQuartileIndicatorImpl();
        return lowerQuartileIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UpperQuartileIndicator createUpperQuartileIndicator() {
        UpperQuartileIndicatorImpl upperQuartileIndicator = new UpperQuartileIndicatorImpl();
        return upperQuartileIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CountsIndicator createCountsIndicator() {
        CountsIndicatorImpl countsIndicator = new CountsIndicatorImpl();
        return countsIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DateParameters createDateParameters() {
        DateParametersImpl dateParameters = new DateParametersImpl();
        return dateParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SqlPatternMatchingIndicator createSqlPatternMatchingIndicator() {
        SqlPatternMatchingIndicatorImpl sqlPatternMatchingIndicator = new SqlPatternMatchingIndicatorImpl();
        return sqlPatternMatchingIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RegexpMatchingIndicator createRegexpMatchingIndicator() {
        RegexpMatchingIndicatorImpl regexpMatchingIndicator = new RegexpMatchingIndicatorImpl();
        return regexpMatchingIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LowFrequencyIndicator createLowFrequencyIndicator() {
        LowFrequencyIndicatorImpl lowFrequencyIndicator = new LowFrequencyIndicatorImpl();
        return lowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PatternFreqIndicator createPatternFreqIndicator() {
        PatternFreqIndicatorImpl patternFreqIndicator = new PatternFreqIndicatorImpl();
        return patternFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PatternLowFreqIndicator createPatternLowFreqIndicator() {
        PatternLowFreqIndicatorImpl patternLowFreqIndicator = new PatternLowFreqIndicatorImpl();
        return patternLowFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefValueCountIndicator createDefValueCountIndicator() {
        DefValueCountIndicatorImpl defValueCountIndicator = new DefValueCountIndicatorImpl();
        return defValueCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SoundexFreqIndicator createSoundexFreqIndicator() {
        SoundexFreqIndicatorImpl soundexFreqIndicator = new SoundexFreqIndicatorImpl();
        return soundexFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SoundexLowFreqIndicator createSoundexLowFreqIndicator() {
        SoundexLowFreqIndicatorImpl soundexLowFreqIndicator = new SoundexLowFreqIndicatorImpl();
        return soundexLowFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatePatternFreqIndicator createDatePatternFreqIndicator() {
        DatePatternFreqIndicatorImpl datePatternFreqIndicator = new DatePatternFreqIndicatorImpl();
        return datePatternFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DateFrequencyIndicator createDateFrequencyIndicator() {
        DateFrequencyIndicatorImpl dateFrequencyIndicator = new DateFrequencyIndicatorImpl();
        return dateFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WeekFrequencyIndicator createWeekFrequencyIndicator() {
        WeekFrequencyIndicatorImpl weekFrequencyIndicator = new WeekFrequencyIndicatorImpl();
        return weekFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MonthFrequencyIndicator createMonthFrequencyIndicator() {
        MonthFrequencyIndicatorImpl monthFrequencyIndicator = new MonthFrequencyIndicatorImpl();
        return monthFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QuarterFrequencyIndicator createQuarterFrequencyIndicator() {
        QuarterFrequencyIndicatorImpl quarterFrequencyIndicator = new QuarterFrequencyIndicatorImpl();
        return quarterFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public YearFrequencyIndicator createYearFrequencyIndicator() {
        YearFrequencyIndicatorImpl yearFrequencyIndicator = new YearFrequencyIndicatorImpl();
        return yearFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinFrequencyIndicator createBinFrequencyIndicator() {
        BinFrequencyIndicatorImpl binFrequencyIndicator = new BinFrequencyIndicatorImpl();
        return binFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DateLowFrequencyIndicator createDateLowFrequencyIndicator() {
        DateLowFrequencyIndicatorImpl dateLowFrequencyIndicator = new DateLowFrequencyIndicatorImpl();
        return dateLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WeekLowFrequencyIndicator createWeekLowFrequencyIndicator() {
        WeekLowFrequencyIndicatorImpl weekLowFrequencyIndicator = new WeekLowFrequencyIndicatorImpl();
        return weekLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MonthLowFrequencyIndicator createMonthLowFrequencyIndicator() {
        MonthLowFrequencyIndicatorImpl monthLowFrequencyIndicator = new MonthLowFrequencyIndicatorImpl();
        return monthLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QuarterLowFrequencyIndicator createQuarterLowFrequencyIndicator() {
        QuarterLowFrequencyIndicatorImpl quarterLowFrequencyIndicator = new QuarterLowFrequencyIndicatorImpl();
        return quarterLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public YearLowFrequencyIndicator createYearLowFrequencyIndicator() {
        YearLowFrequencyIndicatorImpl yearLowFrequencyIndicator = new YearLowFrequencyIndicatorImpl();
        return yearLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinLowFrequencyIndicator createBinLowFrequencyIndicator() {
        BinLowFrequencyIndicatorImpl binLowFrequencyIndicator = new BinLowFrequencyIndicatorImpl();
        return binLowFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValidPhoneCountIndicator createValidPhoneCountIndicator() {
        ValidPhoneCountIndicatorImpl validPhoneCountIndicator = new ValidPhoneCountIndicatorImpl();
        return validPhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PossiblePhoneCountIndicator createPossiblePhoneCountIndicator() {
        PossiblePhoneCountIndicatorImpl possiblePhoneCountIndicator = new PossiblePhoneCountIndicatorImpl();
        return possiblePhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValidRegCodeCountIndicator createValidRegCodeCountIndicator() {
        ValidRegCodeCountIndicatorImpl validRegCodeCountIndicator = new ValidRegCodeCountIndicatorImpl();
        return validRegCodeCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InvalidRegCodeCountIndicator createInvalidRegCodeCountIndicator() {
        InvalidRegCodeCountIndicatorImpl invalidRegCodeCountIndicator = new InvalidRegCodeCountIndicatorImpl();
        return invalidRegCodeCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormNationalPhoneCountIndicator createWellFormNationalPhoneCountIndicator() {
        WellFormNationalPhoneCountIndicatorImpl wellFormNationalPhoneCountIndicator = new WellFormNationalPhoneCountIndicatorImpl();
        return wellFormNationalPhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormIntePhoneCountIndicator createWellFormIntePhoneCountIndicator() {
        WellFormIntePhoneCountIndicatorImpl wellFormIntePhoneCountIndicator = new WellFormIntePhoneCountIndicatorImpl();
        return wellFormIntePhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormE164PhoneCountIndicator createWellFormE164PhoneCountIndicator() {
        WellFormE164PhoneCountIndicatorImpl wellFormE164PhoneCountIndicator = new WellFormE164PhoneCountIndicatorImpl();
        return wellFormE164PhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PhoneNumbStatisticsIndicator createPhoneNumbStatisticsIndicator() {
        PhoneNumbStatisticsIndicatorImpl phoneNumbStatisticsIndicator = new PhoneNumbStatisticsIndicatorImpl();
        return phoneNumbStatisticsIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FormatFreqPieIndicator createFormatFreqPieIndicator() {
        FormatFreqPieIndicatorImpl formatFreqPieIndicator = new FormatFreqPieIndicatorImpl();
        return formatFreqPieIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BenfordLawFrequencyIndicator createBenfordLawFrequencyIndicator() {
        BenfordLawFrequencyIndicatorImpl benfordLawFrequencyIndicator = new BenfordLawFrequencyIndicatorImpl();
        return benfordLawFrequencyIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EastAsiaPatternFreqIndicator createEastAsiaPatternFreqIndicator() {
        EastAsiaPatternFreqIndicatorImpl eastAsiaPatternFreqIndicator = new EastAsiaPatternFreqIndicatorImpl();
        return eastAsiaPatternFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EastAsiaPatternLowFreqIndicator createEastAsiaPatternLowFreqIndicator() {
        EastAsiaPatternLowFreqIndicatorImpl eastAsiaPatternLowFreqIndicator = new EastAsiaPatternLowFreqIndicatorImpl();
        return eastAsiaPatternLowFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CSWordPatternFreqIndicator createCSWordPatternFreqIndicator() {
        CSWordPatternFreqIndicatorImpl csWordPatternFreqIndicator = new CSWordPatternFreqIndicatorImpl();
        return csWordPatternFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CSWordPatternLowFreqIndicator createCSWordPatternLowFreqIndicator() {
        CSWordPatternLowFreqIndicatorImpl csWordPatternLowFreqIndicator = new CSWordPatternLowFreqIndicatorImpl();
        return csWordPatternLowFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CIWordPatternFreqIndicator createCIWordPatternFreqIndicator() {
        CIWordPatternFreqIndicatorImpl ciWordPatternFreqIndicator = new CIWordPatternFreqIndicatorImpl();
        return ciWordPatternFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CIWordPatternLowFreqIndicator createCIWordPatternLowFreqIndicator() {
        CIWordPatternLowFreqIndicatorImpl ciWordPatternLowFreqIndicator = new CIWordPatternLowFreqIndicatorImpl();
        return ciWordPatternLowFreqIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnumStatistics createEnumStatisticsFromString(EDataType eDataType, String initialValue) {
        EnumStatistics result = EnumStatistics.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertEnumStatisticsToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataminingType createDataminingTypeFromString(EDataType eDataType, String initialValue) {
        DataminingType result = DataminingType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDataminingTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DateGrain createDateGrainFromString(EDataType eDataType, String initialValue) {
        DateGrain result = DateGrain.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDateGrainToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MatchingAlgorithm createMatchingAlgorithmFromString(EDataType eDataType, String initialValue) {
        MatchingAlgorithm result = MatchingAlgorithm.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMatchingAlgorithmToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorValueType createIndicatorValueTypeFromString(EDataType eDataType, String initialValue) {
        IndicatorValueType result = IndicatorValueType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIndicatorValueTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public Set<Object> createJavaSetFromString(EDataType eDataType, String initialValue) {
        return (Set<Object>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertJavaSetToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public HashMap<Object, Long> createJavaHashMapFromString(EDataType eDataType, String initialValue) {
        return (HashMap<Object, Long>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertJavaHashMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public TreeMap<Object, Long> createJavaTreeMapFromString(EDataType eDataType, String initialValue) {
        return (TreeMap<Object, Long>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertJavaTreeMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> createObjectArrayFromString(EDataType eDataType, String initialValue) {
        return (List<Object[]>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertObjectArrayToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndicatorsPackage getIndicatorsPackage() {
        return (IndicatorsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static IndicatorsPackage getPackage() {
        return IndicatorsPackage.eINSTANCE;
    }

} //IndicatorsFactoryImpl
