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
package org.talend.dataprofiler.core.service;

import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.pref.IndicatorSettingsPage;
import org.talend.dataquality.indicators.impl.IndicatorsFactoryImpl;
import org.talend.dataquality.service.IIndicatorDefaultValueService;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class IndicatorDefaultValueService implements IIndicatorDefaultValueService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.service.IIndicatorDefaultValueService#getLowFrequencyLimitResult()
     */
    public int getLowFrequencyLimitResult() {
        int defaultValue = CorePlugin.getDefault().getPreferenceStore()
                .getInt(IndicatorSettingsPage.LOW_FREQUENCY_TABLE_RESULT_LIMIT_KEY);
        return defaultValue == 0 ? IndicatorsFactoryImpl.eINSTANCE.createIndicatorParameters().getTopN() : defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.service.IIndicatorDefaultValueService#getFrequencyLimitResult()
     */
    public int getFrequencyLimitResult() {
        int defaultValue = CorePlugin.getDefault().getPreferenceStore()
                .getInt(IndicatorSettingsPage.FREQUENCY_TABLE_RESULT_LIMIT_KEY);
        return defaultValue == 0 ? IndicatorsFactoryImpl.eINSTANCE.createIndicatorParameters().getTopN() : defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.service.IIndicatorDefaultValueService#setLowFrequencyLimitResult(int)
     */
    public void setLowFrequencyLimitResult(int limit) {
        CorePlugin.getDefault().getPreferenceStore().setValue(IndicatorSettingsPage.LOW_FREQUENCY_TABLE_RESULT_LIMIT_KEY, limit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.service.IIndicatorDefaultValueService#setFrequencyLimitResult(int)
     */
    public void setFrequencyLimitResult(int limit) {
        CorePlugin.getDefault().getPreferenceStore().setValue(IndicatorSettingsPage.FREQUENCY_TABLE_RESULT_LIMIT_KEY, limit);

    }

}
