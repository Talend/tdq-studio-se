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
package org.talend.dq.indicators.preview.table;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.utils.format.StringFormatUtil;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class PatternChartDataEntity extends ChartDataEntity {

    private String numMatch;

    private String numNoMatch;

    public String getNumMatch() {
        return numMatch;
    }

    public void setNumMatch(String numMatch) {
        this.numMatch = numMatch;
    }

    public String getNumNoMatch() {
        return numNoMatch;
    }

    public void setNumNoMatch(String numNoMatch) {
        this.numNoMatch = numNoMatch;
    }

    public String getPerMatch() {
        Double match = Double.parseDouble(getNumMatch());
        Double percent = match / getSum();
        if (Double.isNaN(percent) || Double.isInfinite(percent)) {
            return PluginConstant.NA_STRING;
        }
        return StringFormatUtil.format(percent, StringFormatUtil.PERCENT).toString();
    }

    public String getPerNoMatch() {
        Double nomatch = Double.parseDouble(getNumNoMatch());
        Double percent = nomatch / getSum();
        if (Double.isNaN(percent) || Double.isInfinite(percent)) {
            return PluginConstant.NA_STRING;
        }
        return StringFormatUtil.format(percent, StringFormatUtil.PERCENT).toString();
    }

    private Double getSum() {
        return getIndicator().getCount().doubleValue();
    }

    @Override
    public String getRangeAsString() {
        StringBuilder msg = new StringBuilder();
        if (isOutOfRange(getNumMatch())) {
            msg.append("This value is outside the expected indicator's thresholds: " + range); //$NON-NLS-1$
            msg.append("\n"); //$NON-NLS-1$
        }
        if (isOutOfRange(getPerMatch())) {
            msg.append("This value is outside the expected indicator's thresholds in percent: " + range); //$NON-NLS-1$
        }

        // ADD msjian TDQ-4380(TDQ-4470) 2012-1-29: set the hint message when the value is not validate
        if (indicator instanceof WhereRuleIndicator) {
            String sql = ""; //$NON-NLS-1$
            EList<Expression> instantiatedExpressions = indicator.getInstantiatedExpressions();
            if (instantiatedExpressions != null && !instantiatedExpressions.isEmpty()) {
                sql = instantiatedExpressions.get(0).getBody();
            }
            String table = indicator.getAnalyzedElement().getName();
            if (isOutOfValue(getNumMatch())) {
                msg.append(Messages.getString("PatternChartDataEntity.notAvailableData", sql, table)); //$NON-NLS-1$
                msg.append("\n"); //$NON-NLS-1$
            }
            if (isOutOfValue(getPerMatch())) {
                msg.append(Messages.getString("PatternChartDataEntity.notAvailableData", sql, table)); //$NON-NLS-1$
            }
        }
        // TDQ-4380(TDQ-4470)~

        // MOD xqliu 2010-03-10 feature 10834
        String result = null;
        String temp = getToolTip();
        if (temp == null || PluginConstant.EMPTY_STRING.equals(temp.trim())) {
            result = msg.length() == 0 ? null : msg.toString();
        } else {
            result = msg.length() == 0 ? "Desc: " + temp : "Desc: " + temp + "\n" + msg.toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return result;
    }

    // ADD xqliu 2010-03-10 feature 10834
    private String toolTip;

    /**
     * Getter for toolTip.
     * 
     * @return the toolTip
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Sets the toolTip.
     * 
     * @param toolTip the toolTip to set
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    // ~10834

}
