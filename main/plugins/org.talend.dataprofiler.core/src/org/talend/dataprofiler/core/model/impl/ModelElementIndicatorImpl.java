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
package org.talend.dataprofiler.core.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataquality.indicators.AverageLengthIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.AvgLengthWithNullIndicator;
import org.talend.dataquality.indicators.BlankCountIndicator;
import org.talend.dataquality.indicators.BoxIndicator;
import org.talend.dataquality.indicators.CountsIndicator;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.DefValueCountIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IQRIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.InvalidRegCodeCountIndicator;
import org.talend.dataquality.indicators.LowerQuartileIndicator;
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
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator;
import org.talend.dataquality.indicators.PossiblePhoneCountIndicator;
import org.talend.dataquality.indicators.RangeIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.TextIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.UpperQuartileIndicator;
import org.talend.dataquality.indicators.ValidPhoneCountIndicator;
import org.talend.dataquality.indicators.ValidRegCodeCountIndicator;
import org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator;
import org.talend.dataquality.indicators.impl.FormatFreqPieIndicatorImpl;
import org.talend.dataquality.indicators.impl.WellFormIntePhoneCountIndicatorImpl;
import org.talend.dataquality.indicators.impl.WellFormNationalPhoneCountIndicatorImpl;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.Java2SqlType;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public abstract class ModelElementIndicatorImpl implements ModelElementIndicator {

    private static Logger log = Logger.getLogger(ModelElementIndicatorImpl.class);

    private IRepositoryNode repositoryNode;

    private final List<IndicatorEnum> countsEnumChildren = Arrays.asList(IndicatorEnum.CountsIndicatorEnum.getChildren());

    private final List<IndicatorEnum> boxEnumChildren = Arrays.asList(IndicatorEnum.BoxIIndicatorEnum.getChildren());

    private final List<IndicatorEnum> textEnumChildren = Arrays.asList(IndicatorEnum.TextIndicatorEnum.getChildren());

    // private TdColumn tdColumn;

    private List<IndicatorEnum> tempIndicatorEnums = new ArrayList<IndicatorEnum>();

    private List<IndicatorUnit> tempSpecialIndicatorUnitList = new ArrayList<IndicatorUnit>();

    // the indicatorEnums number equal plat IndicatorEnum(not hierrachy) numbers.
    private List<IndicatorEnum> flatIndicatorEnumList = new ArrayList<IndicatorEnum>();

    private Map<IndicatorEnum, IndicatorUnit> plainIndicatorUnitMap = new HashMap<IndicatorEnum, IndicatorUnit>();

    private Map<String, IndicatorUnit> specialIndicatorUnitMap = new HashMap<String, IndicatorUnit>();

    private IndicatorUnit[] plainIndicatorUnits;

    private List<IndicatorUnit> specialIndicatorUnitList = new ArrayList<IndicatorUnit>();

    public ModelElementIndicatorImpl() {
    }

    public ModelElementIndicatorImpl(RepositoryNode reposNode) {
        this.repositoryNode = reposNode;
    }

    public boolean contains(IndicatorEnum indicatorEnum) {
        return this.flatIndicatorEnumList.contains(indicatorEnum) || specialIndicatorEnumContains(indicatorEnum);
    }

    /**
     * DOC zshen Comment method "specialIndicatorEnumContains".
     * 
     * @param indicatorEnum
     * @return
     */
    private boolean specialIndicatorEnumContains(IndicatorEnum indicatorEnum) {
        for (IndicatorUnit indiUnit : this.specialIndicatorUnitList) {
            if (indicatorEnum == indiUnit.getType()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAny(Collection<IndicatorEnum> indciatorEnums) {
        if (indciatorEnums == null) {
            return false;
        }
        for (IndicatorEnum theEnum : indciatorEnums) {
            if (this.contains(theEnum)) {
                return true;
            }
        }
        return false;
    }

    public boolean tempContains(IndicatorEnum indicatorEnum) {
        return this.tempIndicatorEnums.contains(indicatorEnum);
    }

    /**
     * Judge special indicator whether belong to temp list
     */
    public boolean specialTempContains(Indicator indicator) {
        if (indicator == null) {
            return false;
        }
        return this.specialIndicatorUnitList.contains(specialIndicatorUnitMap.get(indicator.getName()));
    }

    public List<IndicatorEnum> getTempIndicator() {
        return tempIndicatorEnums;
    }

    /**
     * If the Indicator is a plain indicator, will remove the indicator value from field 'flatIndicatorEnumList' and
     * 'indicatorUnitMap', contains the parent and children of indicatorTypeMapping.getType();Else, will remove it from
     * specialIndicaortUnitList.
     * 
     * @param indicatorUnit
     */
    public void removeIndicatorUnit(IndicatorUnit indicatorUnit) {
        IndicatorEnum indicatorEnum = indicatorUnit.getType();
        if (IndicatorEnum.isPlainIndicatorEnum(indicatorEnum)) {
            removePlainIndicatorUnit(indicatorEnum);
        } else {
            removeSpecialIndicatorUnit(indicatorUnit);
        }
    }

    /**
     * Remove the specialIndicatorUnit from specialIndicatorList, if there exist more than one InicatorIndicator which
     * has same IndicatorEnumn type, the type of IndicatorUnit will be not removed from flatIndicatorEnumList.
     * 
     * @param indicatorUnit
     */
    private void removeSpecialIndicatorUnit(IndicatorUnit indicatorUnit) {
        IndicatorEnum indicatorEnumn = indicatorUnit.getType();
        this.specialIndicatorUnitList.remove(indicatorUnit);
        for (IndicatorUnit unit : specialIndicatorUnitList) {
            if (unit.getType() == indicatorEnumn) {
                return;
            } else {
                continue;
            }
        }
        this.flatIndicatorEnumList.remove(indicatorEnumn);

    }

    private void removePlainIndicatorUnit(IndicatorEnum indicatorEnum) {

        this.flatIndicatorEnumList.remove(indicatorEnum);
        plainIndicatorUnitMap.remove(indicatorEnum);
        this.removeChildrenEnumMap(indicatorEnum);
        this.removeParentEnumMap(indicatorEnum);
        this.processCategoryIndicator();
    }

    private void removeChildrenEnumMap(IndicatorEnum indicatorEnum) {
        if (indicatorEnum.hasChildren()) {
            for (IndicatorEnum childEnum : indicatorEnum.getChildren()) {
                this.flatIndicatorEnumList.remove(childEnum);
                plainIndicatorUnitMap.remove(childEnum);
                this.removeChildrenEnumMap(childEnum);
            }
        }
    }

    private void removeParentEnumMap(IndicatorEnum indicatorEnum) {
        if (indicatorEnum.hasParent()) {
            this.flatIndicatorEnumList.remove(indicatorEnum.getParent());
            plainIndicatorUnitMap.remove(indicatorEnum.getParent());
            this.removeParentEnumMap(indicatorEnum.getParent());
        }
    }

    public boolean hasIndicators() {
        return this.flatIndicatorEnumList.size() != 0 || this.specialIndicatorUnitList.size() != 0;
    }

    public Indicator[] getIndicators() {
        List<Indicator> indicatorList = new ArrayList<Indicator>();
        IndicatorUnit[] indicatorUnits = this.getIndicatorUnits();

        for (IndicatorUnit indicatorUnit : indicatorUnits) {
            indicatorList.add(indicatorUnit.getIndicator());
        }
        return indicatorList.toArray(new Indicator[indicatorList.size()]);
    }

    public IndicatorUnit[] getIndicatorUnits() {
        if (plainIndicatorUnits == null && (this.specialIndicatorUnitList == null || this.specialIndicatorUnitList.size() == 0)) {
            return new IndicatorUnit[0];
        }
        List<IndicatorUnit> unitList = new ArrayList<IndicatorUnit>();
        if (plainIndicatorUnits != null && plainIndicatorUnits.length > 0) {
            for (IndicatorUnit unit : plainIndicatorUnits) {
                unitList.add(unit);
            }
        }
        if (this.specialIndicatorUnitList != null && this.specialIndicatorUnitList.size() > 0) {
            unitList.addAll(specialIndicatorUnitList);
        }
        return unitList.toArray(new IndicatorUnit[unitList.size()]);
    }

    // MOD klliu 2010-06-03 init Indicators of the gate
    public void setIndicators(Indicator[] indicators) {
        clear();
        for (Indicator oneIndicator : indicators) {
            IndicatorEnum findIndicatorEnum = IndicatorEnum.findIndicatorEnum(oneIndicator.eClass());
            if (findIndicatorEnum == null) {
                log.error("enum not found for indicator: " + oneIndicator.getName() + " of type " + oneIndicator.getClass()); //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            }
            if (IndicatorEnum.isPlainIndicatorEnum(findIndicatorEnum)) {
                this.flatIndicatorEnumList.add(findIndicatorEnum);
                fillCategoryIndicators(findIndicatorEnum, oneIndicator);
            } else {
                this.addSpecialIndicator(findIndicatorEnum, oneIndicator);
            }
        }
        processCategoryIndicator();

    }

    /**
     * Fill the plain indicator value to the corresponding indicator's property, and fill the value to field
     * 'flatIndicatorEnumList' and 'plainIndicatorUnitMap'.
     * 
     * @param indicatorEnum
     * @param indicator
     */
    private void fillCategoryIndicators(IndicatorEnum indicatorEnum, Indicator indicator) {

        // add indicatorEnum to flatIndicatorEnumList
        if (indicatorEnum.hasChildren()) {
            for (IndicatorEnum indEnum : indicatorEnum.getChildren()) {
                if (this.flatIndicatorEnumList.contains(indEnum)) {
                    continue;
                }
                this.flatIndicatorEnumList.add(indEnum);
            }
        }
        switch (indicatorEnum) {
        case CountsIndicatorEnum:
            CountsIndicator countsIndicator = (CountsIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.CountsIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.CountsIndicatorEnum, countsIndicator));

            // add indicatorUnit to indicatorUnitMap
            this.plainIndicatorUnitMap.put(IndicatorEnum.BlankCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.BlankCountIndicatorEnum, countsIndicator.getBlankCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.DistinctCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.DistinctCountIndicatorEnum,
                            countsIndicator.getDistinctCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.DuplicateCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.DuplicateCountIndicatorEnum,
                            countsIndicator.getDuplicateCountIndicator()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.RowCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.RowCountIndicatorEnum, countsIndicator.getRowCountIndicator()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.NullCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.NullCountIndicatorEnum, countsIndicator.getNullCountIndicator()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.UniqueIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.UniqueIndicatorEnum, countsIndicator.getUniqueCountIndicator()));
            // MOD klliu bug 13411 2010-06-03
            this.plainIndicatorUnitMap
                    .put(IndicatorEnum.DefValueCountIndicatorEnum,
                            createPlainIndicatorUnit(IndicatorEnum.DefValueCountIndicatorEnum,
                                    countsIndicator.getDefaultValueIndicator()));
            break;
        case TextIndicatorEnum:
            TextIndicator textIndicator = (TextIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.TextIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.TextIndicatorEnum, textIndicator));

            // add indicatorUnit to indicatorUnitMap
            this.plainIndicatorUnitMap.put(IndicatorEnum.MinLengthIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MinLengthIndicatorEnum, textIndicator.getMinLengthIndicator()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.MaxLengthIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MaxLengthIndicatorEnum, textIndicator.getMaxLengthIndicator()));
            this.plainIndicatorUnitMap
                    .put(IndicatorEnum.AverageLengthIndicatorEnum,
                            createPlainIndicatorUnit(IndicatorEnum.AverageLengthIndicatorEnum,
                                    textIndicator.getAverageLengthIndicator()));
            // MOD mzhao
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MinLengthWithBlankIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MinLengthWithBlankIndicatorEnum,
                            textIndicator.getMinLengthWithBlankIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MinLengthWithBlankNullIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MinLengthWithBlankNullIndicatorEnum,
                            textIndicator.getMinLengthWithBlankNullIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MinLengthWithNullIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MinLengthWithNullIndicatorEnum,
                            textIndicator.getMinLengthWithNullIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MaxLengthWithBlankIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MaxLengthWithBlankIndicatorEnum,
                            textIndicator.getMaxLengthWithBlankIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MaxLengthWithBlankNullIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MaxLengthWithBlankNullIndicatorEnum,
                            textIndicator.getMaxLengthWithBlankNullIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.MaxLengthWithNullIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MaxLengthWithNullIndicatorEnum,
                            textIndicator.getMaxLengthWithNullIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.AverageLengthWithBlankIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.AverageLengthWithBlankIndicatorEnum,
                            textIndicator.getAvgLengthWithBlankIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.AverageLengthWithNullBlankIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.AverageLengthWithNullBlankIndicatorEnum,
                            textIndicator.getAvgLengthWithBlankNullIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.AverageLengthWithNullIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.AverageLengthWithNullIndicatorEnum,
                            textIndicator.getAvgLengthWithNullIndicator()));

            break;
        case BoxIIndicatorEnum:
            BoxIndicator boxtIndicator = (BoxIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.BoxIIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.BoxIIndicatorEnum, boxtIndicator));

            this.plainIndicatorUnitMap.put(IndicatorEnum.MeanIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MeanIndicatorEnum, boxtIndicator.getMeanIndicator()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.MedianIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MedianIndicatorEnum, boxtIndicator.getMedianIndicator()));
            this.fillCategoryIndicators(IndicatorEnum.RangeIndicatorEnum, boxtIndicator.getRangeIndicator());
            this.fillCategoryIndicators(IndicatorEnum.IQRIndicatorEnum, boxtIndicator.getIQR());
            break;
        case IQRIndicatorEnum:
            IQRIndicator iqrIndicator = (IQRIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.IQRIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.IQRIndicatorEnum, iqrIndicator));

            this.plainIndicatorUnitMap.put(IndicatorEnum.LowerQuartileIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.LowerQuartileIndicatorEnum, iqrIndicator.getLowerValue()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.UpperQuartileIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.UpperQuartileIndicatorEnum, iqrIndicator.getUpperValue()));
            break;
        case RangeIndicatorEnum:
            RangeIndicator rangeIndicator = (RangeIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.RangeIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.RangeIndicatorEnum, rangeIndicator));

            this.plainIndicatorUnitMap.put(IndicatorEnum.MaxValueIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MaxValueIndicatorEnum, rangeIndicator.getUpperValue()));
            this.plainIndicatorUnitMap.put(IndicatorEnum.MinValueIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.MinValueIndicatorEnum, rangeIndicator.getLowerValue()));
            break;
        case PhoneNumbStatisticsIndicatorEnum:
            PhoneNumbStatisticsIndicator phoneNumbIndicator = (PhoneNumbStatisticsIndicator) indicator;
            this.plainIndicatorUnitMap.put(IndicatorEnum.PhoneNumbStatisticsIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.PhoneNumbStatisticsIndicatorEnum, phoneNumbIndicator));

            // add indicatorUnit to indicatorUnitMap
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.ValidPhoneCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.ValidPhoneCountIndicatorEnum,
                            phoneNumbIndicator.getValidPhoneCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.PossiblePhoneCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.PossiblePhoneCountIndicatorEnum,
                            phoneNumbIndicator.getPossiblePhoneCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.ValidRegCodeCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.ValidRegCodeCountIndicatorEnum,
                            phoneNumbIndicator.getValidRegCodeCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.InvalidRegCodeCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.InvalidRegCodeCountIndicatorEnum,
                            phoneNumbIndicator.getInvalidRegCodeCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.WellFormE164PhoneCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.WellFormE164PhoneCountIndicatorEnum,
                            phoneNumbIndicator.getWellFormE164PhoneCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.WellFormIntePhoneCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.WellFormIntePhoneCountIndicatorEnum,
                            phoneNumbIndicator.getWellFormIntePhoneCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.WellFormNationalPhoneCountIndicatorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.WellFormNationalPhoneCountIndicatorEnum,
                            phoneNumbIndicator.getWellFormNationalPhoneCountIndicator()));
            this.plainIndicatorUnitMap.put(
                    IndicatorEnum.FormatFreqPieIndictorEnum,
                    createPlainIndicatorUnit(IndicatorEnum.FormatFreqPieIndictorEnum,
                            phoneNumbIndicator.getFormatFreqPieIndicator()));
            break;
        default:
            this.plainIndicatorUnitMap.put(indicatorEnum, createPlainIndicatorUnit(indicatorEnum, indicator));
            break;
        }

    }

    /**
     * DOC rli Comment method "clear".
     */
    private void clear() {
        flatIndicatorEnumList.clear();
        plainIndicatorUnitMap.clear();
        if (specialIndicatorUnitList != null) {
            specialIndicatorUnitList.clear();
        }
    }

    public IndicatorUnit addSpecialIndicator(IndicatorEnum indicatorEnum, Indicator indicator) {
        return createSpecialIndicatorUnit(indicatorEnum, indicator);
    }

    /**
     * Add indicator into temp Special indicator list
     */
    public IndicatorUnit addTempSpecialIndicator(IndicatorEnum indicatorEnum, Indicator indicator) {
        return createTempSpecialIndicatorUnit(indicatorEnum, indicator);
    }

    /**
     * Remove indicator from special indicator list
     */
    public void removeSpecialIndicator(Indicator indicator) {
        IndicatorUnit indicatorUnit = this.specialIndicatorUnitMap.get(indicator.getName());
        if (indicatorUnit != null) {
            this.specialIndicatorUnitList.remove(indicatorUnit);
        }
    }

    /**
     * Remove indicator from temp special indicator list
     */
    public void removeTempSpecialIndicator(Indicator indicator) {
        IndicatorUnit indicatorUnit = this.specialIndicatorUnitMap.get(indicator.getName());
        if (indicatorUnit != null) {
            this.tempSpecialIndicatorUnitList.remove(indicatorUnit);
        }
    }

    public void addTempIndicatorEnum(IndicatorEnum indicatorEnum) {
        if (!tempIndicatorEnums.contains(indicatorEnum)) {
            tempIndicatorEnums.add(indicatorEnum);
            if (indicatorEnum == IndicatorEnum.RangeIndicatorEnum || indicatorEnum == IndicatorEnum.IQRIndicatorEnum) {
                for (IndicatorEnum child : indicatorEnum.getChildren()) {
                    if (!tempIndicatorEnums.contains(child)) {
                        tempIndicatorEnums.add(child);
                    }
                }
            }
        }
    }

    public void removeTempIndicatorEnum(IndicatorEnum indicatorEnum) {
        tempIndicatorEnums.remove(indicatorEnum);
        IndicatorEnum parentEnum = indicatorEnum.getParent();
        if (parentEnum != null
                && (parentEnum == IndicatorEnum.RangeIndicatorEnum || parentEnum == IndicatorEnum.IQRIndicatorEnum)) {
            tempIndicatorEnums.remove(parentEnum);
        }
    }

    public void copyOldIndicatorEnum() {
        listCopy(tempIndicatorEnums, flatIndicatorEnumList);
        listCopy(tempSpecialIndicatorUnitList, specialIndicatorUnitList);
    }

    /**
     * Store the tempory indicator to flatIndicatorEnumList.
     */
    public void storeTempIndicator() {
        for (IndicatorEnum indEnum : tempIndicatorEnums) {
            if (!flatIndicatorEnumList.contains(indEnum)) {
                this.flatIndicatorEnumList.add(indEnum);
            }
        }
        // remove the deleted IndicatorEnum from current indicatorsEnums;
        Iterator<IndicatorEnum> iterator = flatIndicatorEnumList.iterator();
        while (iterator.hasNext()) {
            IndicatorEnum next = iterator.next();
            if (!tempIndicatorEnums.contains(next)) {
                iterator.remove();
            }
        }

        processCategoryIndicator();
        StoreTempSpecialIndicator();
        // specialIndicatorUnitList = createSpacialIndicatorUnits();
        // clear tempIndicatorEnums
        tempIndicatorEnums.clear();
        tempSpecialIndicatorUnitList.clear();
    }

    /**
     * DOC talend Comment method "StoreTempSpecialIndicator".
     */
    private void StoreTempSpecialIndicator() {
        this.specialIndicatorUnitList.clear();
        this.specialIndicatorUnitList.addAll(tempSpecialIndicatorUnitList);
    }

    /**
     * Handle the category IndicatorEnum.
     */
    private void processCategoryIndicator() {
        List<IndicatorEnum> categoryEnumList = new ArrayList<IndicatorEnum>();
        listCopy(categoryEnumList, flatIndicatorEnumList);
        Iterator<IndicatorEnum> iterator = flatIndicatorEnumList.iterator();
        List<IndicatorEnum> currentCountsChildren = new ArrayList<IndicatorEnum>();
        List<IndicatorEnum> currentBOXChildren = new ArrayList<IndicatorEnum>();
        // MOD qiongli 2012-4-25 TDQ-2699 consider TextIndicatorEnum.
        List<IndicatorEnum> currentTextChildren = new ArrayList<IndicatorEnum>();
        while (iterator.hasNext()) {
            IndicatorEnum indEnum = iterator.next();
            if (countsEnumChildren.contains(indEnum)) {
                currentCountsChildren.add(indEnum);
                continue;
            }
            if (boxEnumChildren.contains(indEnum)) {
                currentBOXChildren.add(indEnum);
            }
            if (textEnumChildren.contains(indEnum)) {
                currentTextChildren.add(indEnum);
            }
            if (null != indEnum && (indEnum != IndicatorEnum.CountsIndicatorEnum) && (indEnum != IndicatorEnum.BoxIIndicatorEnum)
                    && indEnum != IndicatorEnum.TextIndicatorEnum && indEnum.hasChildren()) {
                for (IndicatorEnum childrenEnum : indEnum.getChildren()) {
                    categoryEnumList.remove(childrenEnum);
                }
            }

        }
        if (currentCountsChildren.size() == countsEnumChildren.size()
                && flatIndicatorEnumList.contains(IndicatorEnum.CountsIndicatorEnum)) {
            categoryEnumList.removeAll(currentCountsChildren);
        } else {
            categoryEnumList.remove(IndicatorEnum.CountsIndicatorEnum);
        }
        if (currentBOXChildren.size() == boxEnumChildren.size()
                && flatIndicatorEnumList.contains(IndicatorEnum.BoxIIndicatorEnum)) {
            categoryEnumList.removeAll(currentBOXChildren);
        } else {
            categoryEnumList.remove(IndicatorEnum.BoxIIndicatorEnum);
        }
        if (currentTextChildren.size() == textEnumChildren.size()
                && flatIndicatorEnumList.contains(IndicatorEnum.TextIndicatorEnum)) {
            categoryEnumList.removeAll(currentTextChildren);
        } else {
            categoryEnumList.remove(IndicatorEnum.TextIndicatorEnum);
        }
        plainIndicatorUnits = createCategoryIndicatorUnits(categoryEnumList.toArray(new IndicatorEnum[categoryEnumList.size()]));

    }

    /**
     * create indicatorMappings according category indicatorEnum.
     * 
     * @param categoryEnums
     * @return
     */
    private IndicatorUnit[] createCategoryIndicatorUnits(IndicatorEnum[] categoryEnums) {
        List<IndicatorUnit> indicatorUnitList = new ArrayList<IndicatorUnit>();
        IndicatorUnit indicatorUnit;
        for (IndicatorEnum categoryEnum : categoryEnums) {
            if (null == categoryEnum) {
                continue;
            }
            if (!IndicatorEnum.isPlainIndicatorEnum(categoryEnum)) {
                // if (tempSpecialIndicatorEnumList == null) {
                // tempSpecialIndicatorEnumList = new ArrayList<IndicatorEnum>();
                // }
                // tempSpecialIndicatorEnumList.add(categoryEnum);
                continue;
            }
            indicatorUnit = getPlainIndicatorUnit(categoryEnum);
            switch (categoryEnum) {
            case CountsIndicatorEnum:
                CountsIndicator countsIndicator = (CountsIndicator) indicatorUnit.getIndicator();
                countsIndicator.setBlankCountIndicator((BlankCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.BlankCountIndicatorEnum).getIndicator());
                countsIndicator.setDistinctCountIndicator((DistinctCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.DistinctCountIndicatorEnum).getIndicator());
                countsIndicator.setDuplicateCountIndicator((DuplicateCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.DuplicateCountIndicatorEnum).getIndicator());
                countsIndicator.setRowCountIndicator((RowCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.RowCountIndicatorEnum).getIndicator());
                countsIndicator.setNullCountIndicator((NullCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.NullCountIndicatorEnum).getIndicator());
                countsIndicator.setUniqueCountIndicator((UniqueCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.UniqueIndicatorEnum).getIndicator());
                // MOD klliu bug 13411 2010-06-03
                countsIndicator.setDefaultValueIndicator((DefValueCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.DefValueCountIndicatorEnum).getIndicator());
                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.CountsIndicatorEnum.getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;

            case TextIndicatorEnum:
                TextIndicator textIndicator = (TextIndicator) indicatorUnit.getIndicator();
                textIndicator.setMinLengthIndicator((MinLengthIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MinLengthIndicatorEnum).getIndicator());
                textIndicator.setMaxLengthIndicator((MaxLengthIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MaxLengthIndicatorEnum).getIndicator());
                textIndicator.setAverageLengthIndicator((AverageLengthIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.AverageLengthIndicatorEnum).getIndicator());

                // MOD yyi 2010-08-05
                textIndicator.setAvgLengthWithBlankIndicator((AvgLengthWithBlankIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.AverageLengthWithBlankIndicatorEnum).getIndicator());
                textIndicator.setAvgLengthWithNullIndicator((AvgLengthWithNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.AverageLengthWithNullIndicatorEnum).getIndicator());
                textIndicator.setAvgLengthWithBlankNullIndicator((AvgLengthWithBlankNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.AverageLengthWithNullBlankIndicatorEnum).getIndicator());
                textIndicator.setMinLengthWithBlankIndicator((MinLengthWithBlankIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MinLengthWithBlankIndicatorEnum).getIndicator());
                textIndicator.setMinLengthWithNullIndicator((MinLengthWithNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MinLengthWithNullIndicatorEnum).getIndicator());
                textIndicator.setMinLengthWithBlankNullIndicator((MinLengthWithBlankNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MinLengthWithBlankNullIndicatorEnum).getIndicator());
                textIndicator.setMaxLengthWithBlankIndicator((MaxLengthWithBlankIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MaxLengthWithBlankIndicatorEnum).getIndicator());
                textIndicator.setMaxLengthWithNullIndicator((MaxLengthWithNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MaxLengthWithNullIndicatorEnum).getIndicator());
                textIndicator.setMaxLengthWithBlankNullIndicator((MaxLengthWithBlankNullIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.MaxLengthWithBlankNullIndicatorEnum).getIndicator());
                // ~

                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.TextIndicatorEnum.getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;
            case BoxIIndicatorEnum:
                BoxIndicator boxtIndicator = (BoxIndicator) indicatorUnit.getIndicator();
                boxtIndicator.setRangeIndicator((RangeIndicator) getPlainIndicatorUnit(IndicatorEnum.RangeIndicatorEnum)
                        .getIndicator());
                boxtIndicator.setIQR((IQRIndicator) getPlainIndicatorUnit(IndicatorEnum.IQRIndicatorEnum).getIndicator());
                boxtIndicator.setMeanIndicator((MeanIndicator) getPlainIndicatorUnit(IndicatorEnum.MeanIndicatorEnum)
                        .getIndicator());
                boxtIndicator.setMedianIndicator((MedianIndicator) getPlainIndicatorUnit(IndicatorEnum.MedianIndicatorEnum)
                        .getIndicator());
                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.BoxIIndicatorEnum.getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;
            case IQRIndicatorEnum:
                IQRIndicator iqrIndicator = (IQRIndicator) indicatorUnit.getIndicator();
                iqrIndicator.setLowerValue((LowerQuartileIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.LowerQuartileIndicatorEnum).getIndicator());
                iqrIndicator.setUpperValue((UpperQuartileIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.UpperQuartileIndicatorEnum).getIndicator());
                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.IQRIndicatorEnum.getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;
            case RangeIndicatorEnum:
                RangeIndicator rangeIndicator = (RangeIndicator) indicatorUnit.getIndicator();
                rangeIndicator.setLowerValue((MinValueIndicator) getPlainIndicatorUnit(IndicatorEnum.MinValueIndicatorEnum)
                        .getIndicator());
                rangeIndicator.setUpperValue((MaxValueIndicator) getPlainIndicatorUnit(IndicatorEnum.MaxValueIndicatorEnum)
                        .getIndicator());
                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.RangeIndicatorEnum.getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;
            case PhoneNumbStatisticsIndicatorEnum:
                PhoneNumbStatisticsIndicator phoneNumbIndicator = (PhoneNumbStatisticsIndicator) indicatorUnit.getIndicator();
                phoneNumbIndicator.setValidPhoneCountIndicator((ValidPhoneCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.ValidPhoneCountIndicatorEnum).getIndicator());
                phoneNumbIndicator.setPossiblePhoneCountIndicator((PossiblePhoneCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.PossiblePhoneCountIndicatorEnum).getIndicator());
                phoneNumbIndicator.setValidRegCodeCountIndicator((ValidRegCodeCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.ValidRegCodeCountIndicatorEnum).getIndicator());
                phoneNumbIndicator.setInvalidRegCodeCountIndicator((InvalidRegCodeCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.InvalidRegCodeCountIndicatorEnum).getIndicator());
                phoneNumbIndicator.setWellFormE164PhoneCountIndicator((WellFormE164PhoneCountIndicator) getPlainIndicatorUnit(
                        IndicatorEnum.WellFormE164PhoneCountIndicatorEnum).getIndicator());
                phoneNumbIndicator
                        .setWellFormIntePhoneCountIndicator((WellFormIntePhoneCountIndicatorImpl) getPlainIndicatorUnit(
                                IndicatorEnum.WellFormIntePhoneCountIndicatorEnum).getIndicator());
                phoneNumbIndicator
                        .setWellFormNationalPhoneCountIndicator((WellFormNationalPhoneCountIndicatorImpl) getPlainIndicatorUnit(
                                IndicatorEnum.WellFormNationalPhoneCountIndicatorEnum).getIndicator());
                phoneNumbIndicator.setFormatFreqPieIndicator((FormatFreqPieIndicatorImpl) getPlainIndicatorUnit(
                        IndicatorEnum.FormatFreqPieIndictorEnum).getIndicator());
                indicatorUnit.setChildren(createCategoryIndicatorUnits(IndicatorEnum.PhoneNumbStatisticsIndicatorEnum
                        .getChildren()));
                indicatorUnitList.add(indicatorUnit);
                break;
            default:
                indicatorUnitList.add(indicatorUnit);
                break;
            }

        }
        return indicatorUnitList.toArray(new IndicatorUnit[indicatorUnitList.size()]);
    }

    /**
     * This method will get IndicatorUnit from indicatorUnitMap, if can't get exist object, it will be create a new
     * IndicatorUnit.
     * 
     * @param indicatorEnum
     * @return
     */
    private IndicatorUnit getPlainIndicatorUnit(IndicatorEnum indicatorEnum) {
        IndicatorUnit indicatorUnit = this.plainIndicatorUnitMap.get(indicatorEnum);
        if (indicatorUnit == null) {
            indicatorUnit = createPlainIndicatorUnit(indicatorEnum, null);
        }
        return indicatorUnit;
    }

    /**
     * This method will get IndicatorUnit from indicatorUnitMap, if can't get exist object, it will be create a new
     * IndicatorUnit.
     * 
     * @param indicatorEnum
     * @return
     */
    private IndicatorUnit getSpecialIndicatorUnit(IndicatorEnum indicatorEnum) {
        IndicatorUnit indicatorUnit = this.specialIndicatorUnitMap.get(indicatorEnum);
        if (indicatorUnit == null) {
            indicatorUnit = createSpecialIndicatorUnit(indicatorEnum, null);
        }
        return indicatorUnit;
    }

    /**
     * Create a new IndicatorUnit according to indicatorEnum and indicator, if the parameter indicator is null, will
     * create a new indicator .
     * 
     * @param indicatorEnum
     * @param indicator
     * @return
     */
    private IndicatorUnit createPlainIndicatorUnit(IndicatorEnum indicatorEnum, Indicator indicator) {
        Indicator tempIndicator = indicator;
        if (tempIndicator == null) {
            EFactoryImpl factory = (EFactoryImpl) indicatorEnum.getIndicatorType().getEPackage().getEFactoryInstance();
            tempIndicator = (Indicator) factory.create(indicatorEnum.getIndicatorType());
            // MOD scorreia 2008-09-18: bug 5131 fixed: set indicator's definition when the indicator is created.
            if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(tempIndicator)) {
                log.error("Could not set the definition of the given indicator :" + tempIndicator.getName()); //$NON-NLS-1$
            }

            // for 4225, the frequency indicator need be initialized
            int sqlType = getJavaType();
            if (tempIndicator instanceof FrequencyIndicator && Java2SqlType.isDateInSQL(sqlType)) {
                IndicatorParameters parameters = tempIndicator.getParameters();
                if (parameters == null) {
                    parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
                    tempIndicator.setParameters(parameters);
                }
                DateParameters dateParameters = parameters.getDateParameters();
                // MOD qiongli 2011-11-8 TDQ-3864,set DateParameters outside of patternIndicator and
                // PatternLowFreqIndicator.make this indicator running result same as Java engine.
                // MOD msjian TDQ-5357 2012-5-17: fixed the result of "Frequency Table" indicator is same to
                // "Year Frequency Table" when applying on "Time" type
                if (dateParameters == null
                        && !(indicatorEnum == IndicatorEnum.PatternFreqIndicatorEnum
                                || indicatorEnum == IndicatorEnum.PatternLowFreqIndicatorEnum
                                || indicatorEnum == IndicatorEnum.FrequencyIndicatorEnum || indicatorEnum == IndicatorEnum.LowFrequencyIndicatorEnum)) {

                    dateParameters = IndicatorsFactory.eINSTANCE.createDateParameters();
                    parameters.setDateParameters(dateParameters);
                }
                // TDQ-5357~

                // MOD scorreia 2008-06-19 default is already set in the model
                // dateParameters.setDateAggregationType(DateGrain.YEAR);
            }
        }
        IndicatorUnit indicatorUnit = new ColumnIndicatorUnit(indicatorEnum, tempIndicator, this);
        this.plainIndicatorUnitMap.put(indicatorEnum, indicatorUnit);
        return indicatorUnit;

    }

    public int getJavaType() {
        return 0;
    }

    private IndicatorUnit createSpecialIndicatorUnit(IndicatorEnum indicatorEnum, Indicator indicator) {
        Indicator tempIndicator = indicator;
        if (tempIndicator == null) {
            EFactoryImpl factory = (EFactoryImpl) indicatorEnum.getIndicatorType().getEPackage().getEFactoryInstance();
            tempIndicator = (Indicator) factory.create(indicatorEnum.getIndicatorType());
            // MOD scorreia 2008-09-18: bug 5131 fixed: set indicator's definition when the indicator is created.
            if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(tempIndicator)) {
                log.error(DefaultMessagesImpl.getString("ModelElementIndicatorImpl_COULDNOTSETDEF_GIVEN_IND0") + tempIndicator.getName()); //$NON-NLS-1$
            }
        }
        // if (!flatIndicatorEnumList.contains(indicatorEnum)) {
        // this.flatIndicatorEnumList.add(indicatorEnum);
        // }
        if (this.specialIndicatorUnitList == null) {
            this.specialIndicatorUnitList = new ArrayList<IndicatorUnit>();
        }
        IndicatorUnit indicatorUnit = new ColumnIndicatorUnit(indicatorEnum, tempIndicator, this);
        specialIndicatorUnitList.add(indicatorUnit);
        this.specialIndicatorUnitMap.put(tempIndicator.getName(), indicatorUnit);
        return indicatorUnit;
    }

    private IndicatorUnit createTempSpecialIndicatorUnit(IndicatorEnum indicatorEnum, Indicator indicator) {
        Indicator tempIndicator = indicator;
        if (tempIndicator == null) {
            EFactoryImpl factory = (EFactoryImpl) indicatorEnum.getIndicatorType().getEPackage().getEFactoryInstance();
            tempIndicator = (Indicator) factory.create(indicatorEnum.getIndicatorType());
            // MOD scorreia 2008-09-18: bug 5131 fixed: set indicator's definition when the indicator is created.
            if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(tempIndicator)) {
                log.error(DefaultMessagesImpl.getString("ModelElementIndicatorImpl_COULDNOTSETDEF_GIVEN_IND0") + tempIndicator.getName()); //$NON-NLS-1$
            }
        }
        // if (!flatIndicatorEnumList.contains(indicatorEnum)) {
        // this.flatIndicatorEnumList.add(indicatorEnum);
        // }
        if (this.specialIndicatorUnitList == null) {
            this.specialIndicatorUnitList = new ArrayList<IndicatorUnit>();
        }
        for (IndicatorUnit currentUnit : tempSpecialIndicatorUnitList) {
            if (tempIndicator.getName().equalsIgnoreCase(currentUnit.getIndicator().getName())) {
                return currentUnit;
            }
        }
        IndicatorUnit indicatorUnit = new ColumnIndicatorUnit(indicatorEnum, tempIndicator, this);

        tempSpecialIndicatorUnitList.add(indicatorUnit);
        this.specialIndicatorUnitMap.put(tempIndicator.getName(), indicatorUnit);
        return indicatorUnit;
    }

    private <T> void listCopy(List<T> dest, List<T> src) {
        dest.clear();
        for (T element : src) {
            dest.add(element);
        }
    }

    public Indicator[] getPatternIndicators() {
        List<Indicator> patternIndicators = new ArrayList<Indicator>();

        for (Indicator indicator : getIndicators()) {
            if (IndicatorsPackage.eINSTANCE.getPatternMatchingIndicator().isSuperTypeOf(indicator.eClass())) {
                patternIndicators.add(indicator);
            }
        }

        return patternIndicators.toArray(new Indicator[patternIndicators.size()]);
    }

    public IRepositoryNode getModelElementRepositoryNode() {
        return this.repositoryNode;
    }

    protected void setModelElement(IRepositoryNode repositoryNode) {
        this.repositoryNode = repositoryNode;
    }

    public String getElementName() {
        return getModelElementRepositoryNode().getObject().getLabel();
    }
}
