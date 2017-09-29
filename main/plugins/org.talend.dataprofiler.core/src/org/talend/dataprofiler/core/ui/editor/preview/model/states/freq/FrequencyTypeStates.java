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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq;

import java.util.List;

import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.AbstractChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.FrequencyTypeStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public abstract class FrequencyTypeStates extends AbstractChartTypeStates {

    private boolean isSupportDynamicChart = false;

    public FrequencyTypeStates(List<IndicatorUnit> units) {
        super(units);
    }

    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createBarChartByKCD(DefaultMessagesImpl.getString("TopChartFactory.count"), dataset); //$NON-NLS-1$
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        boolean withRowCountIndicator = isWithRowCountIndicator();

        for (IndicatorUnit unit : units) {
            if (unit.isExcuted() && !this.isSupportDynamicChart) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();

                sortIndicator(frequencyExt);

                int numOfShown = FrequencyTypeStateUtil.getNumberOfShown(unit, frequencyExt);

                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    String keyLabel = FrequencyTypeStateUtil.getKeyLabel(freqExt, 200);

                    setValueToDataset(customerdataset, freqExt, keyLabel);

                    ChartDataEntity entity = FrequencyTypeStateUtil.createChartEntity(unit.getIndicator(), freqExt, keyLabel,
                            withRowCountIndicator);

                    customerdataset.addDataEntity(entity);
                }
            } else {
                ChartDataEntity entity = FrequencyTypeStateUtil.createChartEntity(unit.getIndicator(), null,
                        SpecialValueDisplay.EMPTY_FIELD, false);
                FrequencyExt fre = new FrequencyExt();
                fre.setValue(0l);
                fre.setFrequency(0d);
                setValueToDataset(customerdataset, fre, unit.getIndicatorName());

                customerdataset.addDataEntity(entity);
            }
        }
        return customerdataset;
    }

    /**
     * extract the method for change the addvalue parameter for benford law
     * 
     * @param customerdataset
     * @param freqExt
     * @param keyLabel
     */
    protected void setValueToDataset(CustomerDefaultCategoryDataset customerdataset, FrequencyExt freqExt, String keyLabel) {
        customerdataset.addValue(freqExt.getValue(), "1", keyLabel); //$NON-NLS-1$
    }

    public String getReferenceLink() {
        return null;
    }

    protected boolean isWithRowCountIndicator() {
        return FrequencyTypeStateUtil.isWithRowCountIndicator(units);
    }


    
    /**
     * Sets the isSupportDynamicChart.
     * 
     * @param isSupportDynamicChart the isSupportDynamicChart to set
     */
    public void setSupportDynamicChart(boolean isSupportDynamicChart) {
        this.isSupportDynamicChart = isSupportDynamicChart;
    }

    protected abstract void sortIndicator(FrequencyExt[] frequencyExt);

    protected abstract String getTitle();


}
