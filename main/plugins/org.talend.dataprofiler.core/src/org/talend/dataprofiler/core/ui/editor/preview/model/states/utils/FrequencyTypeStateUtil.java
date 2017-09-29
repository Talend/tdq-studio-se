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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.InternalEObject;
import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.CountsIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.FrequencyStatisticsExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2014-12-2 Detailled comment
 * 
 */
public class FrequencyTypeStateUtil {

    /**
     * DOC bZhou Comment method "isWithRowCountIndicator".
     * 
     * If have RowCountIndicator in the indicator list, return true, otherwise, return false.
     * 
     * @return
     */
    public static boolean isWithRowCountIndicator(List<IndicatorUnit> units) {
        if (!units.isEmpty()) {
            Indicator indicator = units.get(0).getIndicator();
            return isWithRowCountIndicator(indicator);
        }
        return false;
    }

    public static boolean isWithRowCountIndicator(Indicator indicator) {
        ModelElement currentAnalyzedElement = indicator.getAnalyzedElement();
        InternalEObject eIndicator = (InternalEObject) indicator;
        AnalysisResult result = (AnalysisResult) eIndicator.eContainer();
        // MOD msjian TDQ-5960: fix a NPE
        if (result == null) {
            return false;
        }
        EList<Indicator> indicators = result.getIndicators();
        if (indicators != null) {
            for (Indicator indi : indicators) {
                ModelElement analyzedElement = indi.getAnalyzedElement();
                if (analyzedElement == currentAnalyzedElement) {
                    if (indi instanceof RowCountIndicator) {
                        return true;
                    } else if (indi instanceof CountsIndicator) {
                        CountsIndicator cindi = (CountsIndicator) indi;
                        return cindi.getRowCountIndicator() != null;
                    }
                }
            }
        }
        return false;
    }

    public static ChartDataEntity createChartEntity(Indicator indicator, FrequencyExt freqExt, String keyLabel,
            boolean isWithRowCountIndicator) {
        ChartDataEntity entity = new ChartDataEntity();
        entity.setIndicator(indicator);
        // MOD mzhao feature:6307 display soundex distinct count and real count.
        entity.setKey(freqExt == null ? null : freqExt.getKey());
        entity.setLabelNull(freqExt == null || freqExt.getKey() == null);
        entity.setLabel(keyLabel);
        entity.setValue(String.valueOf(freqExt == null ? StringUtils.EMPTY : freqExt.getValue()));

        if (freqExt == null) {
            entity.setPercent(0.0);
        } else if (indicator instanceof BenfordLawFrequencyIndicator) {
            entity.setPercent(freqExt.getFrequency());
        } else {
            Double percent = isWithRowCountIndicator ? freqExt.getFrequency() : Double.NaN;
            entity.setPercent(percent);
        }
        return entity;
    }

    /**
     * DOC yyin Comment method "getKeyLabel".
     * 
     * @param freqExt
     * @param maxKeyLength the max length for keyLable,in general,30 for Tableviewer,200 for chart.
     * @return
     */
    public static String getKeyLabel(FrequencyExt freqExt, int maxKeyLength) {
        String keyLabel = String.valueOf(freqExt.getKey());
        if ("null".equals(keyLabel)) { //$NON-NLS-1$
            keyLabel = SpecialValueDisplay.NULL_FIELD;
        }
        if ("".equals(keyLabel)) { //$NON-NLS-1$
            keyLabel = SpecialValueDisplay.EMPTY_FIELD;
        }
        // TDQ-10785: when the data is too long we show the first maxKeyLength characters for table and chart
        if (keyLabel.length() > maxKeyLength) {
            keyLabel = keyLabel.substring(0, maxKeyLength) + "...(" + keyLabel.length() + " characters)"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        // TDQ-10785~
        return keyLabel;
    }

    /**
     * DOC yyin Comment method "getNumberOfShown".
     * 
     * @param unit
     * @param frequencyExt
     * @return
     */
    public static int getNumberOfShown(IndicatorUnit unit, FrequencyExt[] frequencyExt) {
        int numOfShown = frequencyExt.length;
        IndicatorParameters parameters = unit.getIndicator().getParameters();
        if (parameters != null) {
            if (parameters.getTopN() < numOfShown) {
                numOfShown = parameters.getTopN();
            }
        }
        return numOfShown;
    }

    public static DataExplorer getDataExplorer() {
        return new FrequencyStatisticsExplorer();
    }
}
