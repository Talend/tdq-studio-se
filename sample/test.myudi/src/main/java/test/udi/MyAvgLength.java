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
 * @author mzhao
 * 
 * A very simple example of a java implementation of a user defined indicator. This indicator returns a user defined
 * real value. It implements the minimum number of required methods.
 */
public class MyAvgLength extends UserDefIndicatorImpl {

    private double length = 0;

    @Override
    public boolean reset() {
        super.reset();
        length = 0;
        return true;
    }

    @Override
    public boolean handle(Object data) {
        super.handle(data);
        // an indicator which computes the average text length on data which are more than 2 characters (this means that
        // text values with less than 2 characters are not taken into account).
        int dataLength = (data != null) ? data.toString().length() : 0;
        if (dataLength > 2) {
            length += dataLength;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        value = String.valueOf(this.length / (this.getCount() - this.getNullCount()));
        return super.finalizeComputation();
    }

}
