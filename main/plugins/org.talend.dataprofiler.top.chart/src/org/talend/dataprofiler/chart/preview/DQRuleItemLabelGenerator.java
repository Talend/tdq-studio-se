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
package org.talend.dataprofiler.chart.preview;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.DataUtilities;
import org.jfree.data.category.CategoryDataset;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class DQRuleItemLabelGenerator extends StandardCategoryItemLabelGenerator {

    private static final long serialVersionUID = 1L;

    public DQRuleItemLabelGenerator(String labelFormat, NumberFormat formatter) {
        super(labelFormat, formatter);
    }

    /**
     * DOC Administrator DQRuleItemLabelGenerator constructor comment.
     * 
     * @param labelFormat
     * @param formatter
     */
    public DQRuleItemLabelGenerator(String labelFormat, DateFormat formatter) {
        super(labelFormat, formatter);
    }

    @Override
    protected Object[] createItemArray(CategoryDataset dataset, int row, int column) {

        Object[] result = new Object[4];
        result[0] = dataset.getRowKey(row).toString();
        result[1] = dataset.getColumnKey(column).toString();
        Number value = dataset.getValue(row, column);
        if (value != null && !value.equals(Double.NaN)) {
            if (super.getNumberFormat() != null) {
                result[2] = super.getNumberFormat().format(value);
            } else if (super.getDateFormat() != null) {
                result[2] = super.getDateFormat().format(value);
            }

            double total = DataUtilities.calculateColumnTotal(dataset, column);
            double percent = value.doubleValue() / total;
            // MOD qiongli bug 21589,override for just changeing this line.avoid 99.99% to show 100%
            // result[3] = this.percentFormat.format(percent);
            result[3] = stringformat(percent, 0).toString();
        } else {
            result[2] = "-"; //$NON-NLS-1$
            result[3] = "-"; //$NON-NLS-1$
        }

        return result;

    }

    /**
     * DOC yyin Comment method "stringformat".
     * 
     * @param percent
     * @param i
     * @return
     */
    private Object stringformat(Object percent, int i) {
        // ADD msjian TDQ-10793: when there is no data, the percent value is NaN
        if (Double.isNaN((double) percent)) {
            return String.valueOf(Double.NaN);
        }
        // TDQ-10793~

        BigDecimal zero = new BigDecimal(0);
        BigDecimal temp = new BigDecimal(percent.toString());
        BigDecimal min = new BigDecimal(10E-5);
        BigDecimal max = new BigDecimal(9999 * 10E-5);
        boolean isUseScientific = false;
        if (temp.compareTo(min) == -1 && temp.compareTo(zero) == 1) {
            isUseScientific = true;
        } else if (temp.compareTo(max) == 1 && temp.compareTo(new BigDecimal(1)) == -1) {
            percent = max.toString();
        }
        DecimalFormat format = (DecimalFormat) DecimalFormat.getPercentInstance(Locale.ENGLISH);
        format.applyPattern("0.00%"); //$NON-NLS-1$

        if (isUseScientific) {
            format.applyPattern("0.###E0%"); //$NON-NLS-1$
        }
        return format.format(new Double(percent.toString()));
    }
}
