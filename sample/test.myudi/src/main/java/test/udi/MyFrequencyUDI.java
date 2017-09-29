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
package test.udi;

import java.util.HashMap;

import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;

/**
 * @author scorreia
 * 
 * A very simple example of a java implementation of a user defined indicator. This indicator returns a matching count.
 * It implements the minimum number of required methods.
 */
public class MyFrequencyUDI extends UserDefIndicatorImpl {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#finalizeComputation()
     */
    public boolean finalizeComputation() {
        // nothing to do
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#handle(java.lang.Object)
     */
    public boolean handle(Object data) {
        // you can call super method in order to automatically increase the number of count
        // super.handle(data);
        // you can also increment count yourself here
        this.count++;

        // increment frequency of data
        Long c = this.valueToFreq.get(data);
        if (c == null) {
            // add value to map
            this.valueToFreq.put(data, 1L);
        } else {
            // already exists: increment number of occurences
            c++;
            this.valueToFreq.put(data, c);
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
