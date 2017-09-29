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
package org.talend.dataprofiler.core.ui.events;

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
        super.clearValue();
    }
}
