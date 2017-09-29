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
package org.talend.dataprofiler.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * This class can store the all the Indicators of one TdTable, and provide the method to access all indicator.
 */
public class TableIndicator {

    private static Logger log = Logger.getLogger(TableIndicator.class);

    private final List<IndicatorEnum> countsEnumChildren = Arrays.asList(IndicatorEnum.CountsIndicatorEnum.getChildren());

    private List<IndicatorEnum> indicatorEnumList = new ArrayList<IndicatorEnum>();

    private Map<IndicatorEnum, TableIndicatorUnit> indicatorUnitMap = new HashMap<IndicatorEnum, TableIndicatorUnit>();

    private TableIndicatorUnit[] indicatorUnits;

    private List<TableIndicatorUnit> specialIndicatorUnitList;

    private List<IndicatorEnum> tempIndicatorEnums = new ArrayList<IndicatorEnum>();

    private NamedColumnSet columnSet;

    /**
     * Getter for columnSet.
     * 
     * @return the columnSet
     */
    public NamedColumnSet getColumnSet() {
        return columnSet;
    }

    /**
     * Sets the columnSet.
     * 
     * @param columnSet the columnSet to set
     */
    public void setColumnSet(NamedColumnSet columnSet) {
        this.columnSet = columnSet;
    }

    /**
     * DOC bZhou Comment method "isTable".
     * 
     * @return
     */
    public boolean isTable() {
        return SwitchHelpers.TABLE_SWITCH.doSwitch(columnSet) != null;
    }

    /**
     * DOC bZhou Comment method "isView".
     * 
     * @return
     */
    public boolean isView() {
        return SwitchHelpers.VIEW_SWITCH.doSwitch(columnSet) != null;
    }

    /**
     * DOC bZhou TableIndicator constructor comment.
     * 
     * @param columnSet
     */
    public TableIndicator(NamedColumnSet columnSet) {
        this.columnSet = columnSet;
    }

    public boolean hasIndicators() {
        return !(this.indicatorEnumList.size() == 0);
    }

    public TableIndicatorUnit[] getIndicatorUnits() {
        if (indicatorUnits == null && (this.specialIndicatorUnitList == null || this.specialIndicatorUnitList.size() == 0)) {
            return new TableIndicatorUnit[0];
        }
        List<TableIndicatorUnit> unitList = new ArrayList<TableIndicatorUnit>();
        if (indicatorUnits != null && indicatorUnits.length > 0) {
            for (TableIndicatorUnit unit : indicatorUnits) {
                unitList.add(unit);
            }
        }
        if (this.specialIndicatorUnitList != null && this.specialIndicatorUnitList.size() > 0) {
            unitList.addAll(specialIndicatorUnitList);
        }
        return unitList.toArray(new TableIndicatorUnit[unitList.size()]);
    }

    public Indicator[] getIndicators() {
        List<Indicator> indicatorList = new ArrayList<Indicator>();
        for (TableIndicatorUnit indicatorUnit : getIndicatorUnits()) {
            indicatorList.add(indicatorUnit.getIndicator());
        }
        return indicatorList.toArray(new Indicator[indicatorList.size()]);
    }

    public void setIndicators(Indicator[] indicators) {
        this.clear();
        for (Indicator oneIndicator : indicators) {
            IndicatorEnum findIndicatorEnum = IndicatorEnum.findIndicatorEnum(oneIndicator.eClass());
            if (IndicatorEnum.isPlainIndicatorEnum(findIndicatorEnum)) {
                this.indicatorEnumList.add(findIndicatorEnum);
                createIndicatorUnit(findIndicatorEnum, oneIndicator);
            } else {
                this.addSpecialIndicator(findIndicatorEnum, oneIndicator);
            }
        }
        processIndicator();
    }

    private void processIndicator() {
        List<IndicatorEnum> enumList = new ArrayList<IndicatorEnum>();
        listCopy(enumList, indicatorEnumList);
        Iterator<IndicatorEnum> iterator = indicatorEnumList.iterator();
        List<IndicatorEnum> currentCountsChildren = new ArrayList<IndicatorEnum>();
        while (iterator.hasNext()) {
            IndicatorEnum indEnum = iterator.next();
            if (countsEnumChildren.contains(indEnum)) {
                currentCountsChildren.add(indEnum);
                continue;
            }
            if ((indEnum != IndicatorEnum.CountsIndicatorEnum) && indEnum.hasChildren()) {
                for (IndicatorEnum childrenEnum : indEnum.getChildren()) {
                    enumList.remove(childrenEnum);
                }
            }
        }
        if (currentCountsChildren.size() == countsEnumChildren.size()
                && indicatorEnumList.contains(IndicatorEnum.CountsIndicatorEnum)) {
            enumList.removeAll(currentCountsChildren);
        } else {
            enumList.remove(IndicatorEnum.CountsIndicatorEnum);
        }
        indicatorUnits = createCategoryIndicatorUnits(enumList.toArray(new IndicatorEnum[enumList.size()]));
    }

    private TableIndicatorUnit[] createCategoryIndicatorUnits(IndicatorEnum[] indicatorEnums) {
        List<TableIndicatorUnit> indicatorUnitList = new ArrayList<TableIndicatorUnit>();
        TableIndicatorUnit indicatorUnit;
        for (IndicatorEnum indicatorEnum : indicatorEnums) {
            if (!IndicatorEnum.isPlainIndicatorEnum(indicatorEnum)) {
                continue;
            }
            indicatorUnit = getIndicatorUnit(indicatorEnum);
            indicatorUnitList.add(indicatorUnit);
        }
        return indicatorUnitList.toArray(new TableIndicatorUnit[indicatorUnitList.size()]);
    }

    private TableIndicatorUnit getIndicatorUnit(IndicatorEnum indicatorEnum) {
        TableIndicatorUnit indicatorUnit = this.indicatorUnitMap.get(indicatorEnum);
        if (indicatorUnit == null) {
            indicatorUnit = createIndicatorUnit(indicatorEnum, null);
        }
        return indicatorUnit;
    }

    private void listCopy(List<IndicatorEnum> dest, List<IndicatorEnum> src) {
        dest.clear();
        for (IndicatorEnum indicatorEnum : src) {
            dest.add(indicatorEnum);
        }
    }

    private void clear() {
        indicatorEnumList.clear();
        indicatorUnitMap.clear();
        if (specialIndicatorUnitList != null) {
            specialIndicatorUnitList.clear();
        }
    }

    private TableIndicatorUnit createIndicatorUnit(IndicatorEnum indicatorEnum, Indicator indicator) {
        return createIndicatorUnit(null, indicatorEnum, indicator);
    }

    private TableIndicatorUnit createIndicatorUnit(IFile fe, IndicatorEnum indicatorEnum, Indicator indicator) {
        Indicator indicatorNew = indicator;
        if (indicator == null) {
            IndicatorsFactory factory = IndicatorsFactory.eINSTANCE;
            indicatorNew = (Indicator) factory.create(indicatorEnum.getIndicatorType());
            indicatorNew.setAnalyzedElement(getColumnSet());

        }
        if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator)) {
            log.error(DefaultMessagesImpl.getString("TableIndicator.couldnotSetDef") + indicatorNew.getName()); //$NON-NLS-1$
        }
        TableIndicatorUnit indicatorUnit = new TableIndicatorUnit(indicatorEnum, indicatorNew, this);
        this.indicatorUnitMap.put(indicatorEnum, indicatorUnit);
        return indicatorUnit;

    }

    /**
     * 
     * DOC xqliu Comment method "createTableIndicatorWithRowCountIndicator".
     * 
     * @param table
     * @return
     */
    public static TableIndicator createTableIndicatorWithRowCountIndicator(NamedColumnSet set) {
        TableIndicator tableIndicator = new TableIndicator(set);

        RowCountIndicator createIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
        DefinitionHandler.getInstance().setDefaultIndicatorDefinition(createIndicator);
        createIndicator.setAnalyzedElement(set);
        Indicator[] indicators = new Indicator[] { createIndicator };
        tableIndicator.setIndicators(indicators);

        return tableIndicator;
    }

    private TableIndicatorUnit createSpecialIndicatorUnit(IndicatorDefinition whereRule, IndicatorEnum indicatorEnum,
            Indicator indicator) {
        Indicator indicatorNew = indicator;
        if (indicatorNew == null) {
            IndicatorSqlFactory factory = IndicatorSqlFactory.eINSTANCE;
            indicatorNew = (Indicator) factory.create(indicatorEnum.getIndicatorType());
            if (whereRule != null && indicatorNew instanceof WhereRuleIndicator) {
                indicatorNew.setAnalyzedElement(getColumnSet());
                indicatorNew.setIndicatorDefinition(whereRule);
            }
        }
        if (!indicatorEnumList.contains(indicatorEnum)) {
            this.indicatorEnumList.add(indicatorEnum);
        }
        if (this.specialIndicatorUnitList == null) {
            this.specialIndicatorUnitList = new ArrayList<TableIndicatorUnit>();
        }
        TableIndicatorUnit indicatorUnit = new TableIndicatorUnit(indicatorEnum, indicatorNew, this);
        specialIndicatorUnitList.add(indicatorUnit);
        return indicatorUnit;
    }

    /**
     * Remove the specialIndicatorUnit from specialIndicatorList, if there exist more than one special Indicator which
     * has same IndicatorEnumn type, the type of IndicatorUnit will be not removed from flatIndicatorEnumList.
     * 
     * @param indicatorUnit
     */
    private void removeSpecialIndicatorUnit(TableIndicatorUnit indicatorUnit) {
        IndicatorEnum indicatorEnumn = indicatorUnit.getType();
        this.specialIndicatorUnitList.remove(indicatorUnit);
        for (TableIndicatorUnit unit : specialIndicatorUnitList) {
            if (unit.getType() == indicatorEnumn) {
                return;
            } else {
                continue;
            }
        }
        this.indicatorEnumList.remove(indicatorEnumn);
    }

    public TableIndicatorUnit addSpecialIndicator(IndicatorEnum indicatorEnum, Indicator indicator) {
        return addSpecialIndicator(null, indicatorEnum, indicator);
    }

    public TableIndicatorUnit addSpecialIndicator(IndicatorDefinition whereRule, IndicatorEnum indicatorEnum, Indicator indicator) {
        return createSpecialIndicatorUnit(whereRule, indicatorEnum, indicator);
    }

    /**
     * DOC xqliu Comment method "removeIndicatorUnit".
     * 
     * @param indicatorUnit
     */
    public void removeIndicatorUnit(TableIndicatorUnit indicatorUnit) {
        IndicatorEnum indicatorEnum = indicatorUnit.getType();
        if (IndicatorEnum.isPlainIndicatorEnum(indicatorEnum)) {
            removePlainIndicatorUnit(indicatorEnum);
        } else {
            removeSpecialIndicatorUnit(indicatorUnit);
        }
    }

    private void removePlainIndicatorUnit(IndicatorEnum indicatorEnum) {
        this.indicatorEnumList.remove(indicatorEnum);
        indicatorUnitMap.remove(indicatorEnum);
        this.processIndicator();
    }

    public void addTempIndicatorEnum(IndicatorEnum indicatorEnum) {
        if (!tempIndicatorEnums.contains(indicatorEnum)) {
            tempIndicatorEnums.add(indicatorEnum);
        }
    }

    public void storeTempIndicator() {
        for (IndicatorEnum indEnum : tempIndicatorEnums) {
            if (!indicatorEnumList.contains(indEnum)) {
                this.indicatorEnumList.add(indEnum);
            }
        }
        // remove the deleted IndicatorEnum from current indicatorsEnums;
        Iterator<IndicatorEnum> iterator = indicatorEnumList.iterator();
        while (iterator.hasNext()) {
            IndicatorEnum next = iterator.next();
            if (!tempIndicatorEnums.contains(next)) {
                iterator.remove();
            }
        }
        // clear tempIndicatorEnums
        tempIndicatorEnums.clear();
    }

    public void removeTempIndicatorEnum(IndicatorEnum indicatorEnum) {
        tempIndicatorEnums.remove(indicatorEnum);
    }

    public void copyOldIndicatorEnum() {
        listCopy(tempIndicatorEnums, indicatorEnumList);
    }
}
