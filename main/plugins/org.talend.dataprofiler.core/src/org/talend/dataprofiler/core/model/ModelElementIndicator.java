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

import java.util.Collection;
import java.util.List;

import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public interface ModelElementIndicator {

    public boolean contains(IndicatorEnum indicatorEnum);

    public boolean containsAny(Collection<IndicatorEnum> indciatorEnums);

    public boolean tempContains(IndicatorEnum indicatorEnum);

    public boolean specialTempContains(Indicator indicator);

    public List<IndicatorEnum> getTempIndicator();

    /**
     * If the Indicator is a plain indicator, will remove the indicator value from field 'flatIndicatorEnumList' and
     * 'indicatorUnitMap', contains the parent and children of indicatorTypeMapping.getType();Else, will remove it from
     * specialIndicaortUnitList.
     * 
     * @param indicatorUnit
     */
    public void removeIndicatorUnit(IndicatorUnit indicatorUnit);

    public boolean hasIndicators();

    public Indicator[] getIndicators();

    public IndicatorUnit[] getIndicatorUnits();

    public void setIndicators(Indicator[] indicators);

    public IndicatorUnit addSpecialIndicator(IndicatorEnum indicatorEnum, Indicator indicator);

    public IndicatorUnit addTempSpecialIndicator(IndicatorEnum indicatorEnum, Indicator indicator);

    public void removeSpecialIndicator(Indicator indicator);

    public void removeTempSpecialIndicator(Indicator indicator);

    public void addTempIndicatorEnum(IndicatorEnum indicatorEnum);

    public void removeTempIndicatorEnum(IndicatorEnum indicatorEnum);

    public abstract void copyOldIndicatorEnum();

    /**
     * Store the tempory indicator to flatIndicatorEnumList.
     */
    public abstract void storeTempIndicator();

    public abstract Indicator[] getPatternIndicators();

    public IRepositoryNode getModelElementRepositoryNode();

    public int getJavaType();

    public String getElementName();
}
