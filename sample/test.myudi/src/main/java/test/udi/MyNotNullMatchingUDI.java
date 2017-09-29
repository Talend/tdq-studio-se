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

import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;

/**
 * @author scorreia
 * 
 * A very simple example of a java implementation of a user defined indicator. This indicator returns a matching count.
 * It implements the minimum number of required methods.
 */
public class MyNotNullMatchingUDI extends UserDefIndicatorImpl {


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#finalizeComputation()
     */
    public boolean finalizeComputation() {
        // compute non matching value
        this.notMatchingValueCount = count - matchingValueCount;
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
        if (data != null) {
            this.matchingValueCount++;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#reset()
     */
    public boolean reset() {
        this.matchingValueCount = 0L;
        return super.reset();
    }


}
