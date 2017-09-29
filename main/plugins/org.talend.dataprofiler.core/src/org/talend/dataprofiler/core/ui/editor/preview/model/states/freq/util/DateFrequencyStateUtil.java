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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.FrequencyTypeStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class DateFrequencyStateUtil {

    public static ICustomerDataset getCustomerDataset(List<IndicatorUnit> units, int sortType) {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();

        for (IndicatorUnit unit : units) {
            if (unit.isExcuted()) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();

                ComparatorsFactory.sort(frequencyExt, sortType);

                int numOfShown = FrequencyTypeStateUtil.getNumberOfShown(unit, frequencyExt);

                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    String keyLabel = FrequencyTypeStateUtil.getKeyLabel(freqExt, 30);

                    customerdataset.addValue(freqExt.getValue(), unit.getIndicatorName(), keyLabel);

                    ChartDataEntity entity = createDataEntity(unit, freqExt, keyLabel);

                    customerdataset.addDataEntity(entity);
                }
            }
        }
        return customerdataset;
    }

    public static ChartDataEntity createDataEntity(IndicatorUnit unit, FrequencyExt freqExt, String keyLabel) {
        ChartDataEntity entity = new ChartDataEntity();
        entity.setIndicator(unit.getIndicator());
        entity.setKey(freqExt == null ? null : freqExt.getKey());
        entity.setLabelNull(freqExt == null || freqExt.getKey() == null);
        entity.setLabel(keyLabel);
        entity.setValue(String.valueOf(freqExt == null ? StringUtils.EMPTY : freqExt.getValue()));

        if (freqExt == null) {
            entity.setPercent(0.0);
        } else {
            entity.setPercent(freqExt.getFrequency());
        }
        return entity;
    }

    public static ChartDataEntity[] getDataEntity(List<IndicatorUnit> units, int sortType) {
        List<ChartDataEntity> dataEnities = new ArrayList<ChartDataEntity>();
        for (IndicatorUnit unit : units) {
            if (unit.isExcuted()) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();

                ComparatorsFactory.sort(frequencyExt, sortType);

                int numOfShown = FrequencyTypeStateUtil.getNumberOfShown(unit, frequencyExt);

                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    String keyLabel = FrequencyTypeStateUtil.getKeyLabel(freqExt, 30);
                    dataEnities.add(DateFrequencyStateUtil.createDataEntity(unit, freqExt, keyLabel));
                }
            }
        }

        return dataEnities.toArray(new ChartDataEntity[dataEnities.size()]);
    }
}
