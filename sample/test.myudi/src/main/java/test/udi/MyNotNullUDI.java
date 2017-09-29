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
 * A very simple example of a java implementation of a user defined indicator. This indicator returns a count and must
 * belong to the "user defined count" category defined in the Indicator editor. It implements the minimum number of
 * required methods.
 */
public class MyNotNullUDI extends UserDefIndicatorImpl {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        // nothing to do
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        if (data != null) {
            this.userCount++;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#reset()
     */
    @Override
    public boolean reset() {
        this.userCount = 0l;
        return true;
    }

}
