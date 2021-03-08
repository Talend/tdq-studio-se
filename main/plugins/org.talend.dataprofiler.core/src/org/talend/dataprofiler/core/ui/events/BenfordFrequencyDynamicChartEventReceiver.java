// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.events;

import java.util.ArrayList;
import java.util.List;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BenfordLawFrequencyState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.BenfordLawFrequencyStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.impl.BenfordLawFrequencyIndicatorImpl;
import org.talend.dq.indicators.ext.FrequencyExt;

/**
 * created by yyin on 2014-7-11 Detailled comment
 *
 */
public class BenfordFrequencyDynamicChartEventReceiver extends FrequencyDynamicChartEventReceiver {

    private Object secondDataset = null;

    private List<String> dotChartLabels = new ArrayList<>();

    @Override
    public boolean handle(Object value) {
        return super.handle(value);
    }

    @Override
    protected void addValueToDataset(Object customerdataset, FrequencyExt freqExt, String keyLabel) {
        // the value of the bar
        TOPChartUtils.getInstance().addValueToCategoryDataset(customerdataset, freqExt.getFrequency(), "1", keyLabel); //$NON-NLS-1$
        // the value of the line
        if (secondDataset != null) {// when the graph is hiden, the secondDataset is null
            if (!BenfordLawFrequencyIndicatorImpl.INVALID.equals(keyLabel) && !"0".equals(keyLabel)) { //$NON-NLS-1$
                TOPChartUtils.getInstance().addValueToCategoryDataset(secondDataset,
                        BenfordLawFrequencyState.formalValues[Integer.valueOf(keyLabel) - 1], "Expected(%)", keyLabel);//$NON-NLS-1$
            } else {
                TOPChartUtils.getInstance().addValueToCategoryDataset(secondDataset, BenfordLawFrequencyState.formalValues[9],
                        "Expected(%)", keyLabel);//$NON-NLS-1$
            }
            dotChartLabels.add(keyLabel);
        }
    }

    @Override
    protected FrequencyExt[] handleFrequency(FrequencyExt[] frequencyExt) {
        FrequencyExt[] tempFreq = frequencyExt;
        ComparatorsFactory.sort(tempFreq, ComparatorsFactory.BENFORDLAW_FREQUENCY_COMPARATOR_ID);

        return BenfordLawFrequencyStateUtil.recomputerForBenfordLaw(tempFreq);
    }

    /**
     * Getter for secondDataset.
     *
     * @return the secondDataset
     */
    public Object getSecondDataset() {
        return secondDataset;
    }

    /**
     * Sets the secondDataset.
     *
     * @param secondDataset the secondDataset to set
     */
    public void setSecondDataset(Object secondDataset) {
        this.secondDataset = secondDataset;
    }

    @Override
    public void clearValue() {
        if (secondDataset != null) {// when the graph is hiden, the secondDataset is null
            TOPChartUtils.getInstance().clearDataset(secondDataset);
        }
        dotChartLabels.clear();
        super.clearValue();
    }

    @Override
    protected void createChart() {
        Object chart = TOPChartUtils
                .getInstance()
                .createBenfordChart(DefaultMessagesImpl.getString("BenfordLawFrequencyState.value"), //$NON-NLS-1$
                        DefaultMessagesImpl.getString("BenfordLawFrequencyState.AxisY"), getDataset(), dotChartLabels, //$NON-NLS-1$
                        BenfordLawFrequencyState.formalValues,
                        DefaultMessagesImpl.getString("BenfordLawFrequencyState.value")); //$NON-NLS-1$
        TOPChartUtils.getInstance().decoratePatternMatching(chart);

        if (this.parentChartComposite != null && !parentChartComposite.isDisposed()) {
            TOPChartUtils.getInstance().refrechChart(this.parentChartComposite, chart);
        }
    }

    @Override
    protected void updateLastTimeDataSet(Object customerdataset, FrequencyExt freqExt, String keyLabel) {
        // do nothing here
    }

    @Override
    public Object getDataset() {
        Object customerdataset = super.getDataset();
        if (customerdataset instanceof CustomerDefaultCategoryDataset) {
            return ((CustomerDefaultCategoryDataset) customerdataset).getDataset();
        } else {
            ICustomerDataset customerDataset = TOPChartUtils.getInstance().getCustomerDataset(customerdataset);
            if (customerDataset != null && customerDataset instanceof CustomerDefaultCategoryDataset) {
                return ((CustomerDefaultCategoryDataset) customerDataset).getDataset();
            }
            return null;
        }
    }

}
