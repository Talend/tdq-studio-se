// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicator.userdefine;

import java.util.HashMap;

import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;

/**
 * DOC yyin 201204 This Class provide the function to compute a group of data by Benford's Law and output the leading
 * digits with its distribution in this dataset. Then the user can use it to compare with the standard, to detect
 * possible cases of Fraud.
 * 
 * related SQL: SELECT cast(LEFT(<%=__COLUMN_NAMES__%>,1) as char), COUNT(*) c FROM <%=__TABLE_NAME__%> t
 * <%=__WHERE_CLAUSE__%> GROUP BY LEFT(<%=__COLUMN_NAMES__%>,1) order by LEFT(<%=__COLUMN_NAMES__%>,1)
 */
public class BenfordLawFrequencyIndicator extends UserDefIndicatorImpl {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#finalizeComputation()
     */
    public boolean finalizeComputation() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#handle(java.lang.Object)
     */
    public boolean handle(Object data) {
        if (this.valueToFreq == null) {
            this.valueToFreq = new HashMap<Object, Long>();
        }
        this.count++;

        if (data == null)
            return true;

        Integer leadDigit =
                Integer.valueOf(String.valueOf(data).substring(0, String.valueOf(data).offsetByCodePoints(0, 1)));

        // increment frequency of leading digit in data
        Long c = this.valueToFreq.get(leadDigit);
        if (c == null) {
            // add value to map
            this.valueToFreq.put(leadDigit, 1L);
            c = 1L;
        } else {
            // already exists: increment number of occurences
            c++;
            this.valueToFreq.put(leadDigit, c);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#reset()
     */
    public boolean reset() {
        super.reset(); // reset the number of count
        this.valueToFreq = new HashMap<Object, Long>(); // should be done in super class
        return true;
    }

}
