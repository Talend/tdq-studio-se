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
package org.talend.dq.indicators.ext;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class PatternMatchingExt {

    private long matchingValueCount;

    private long notMatchingValueCount;

    /**
     * DOC zqin Comment method "getMatchingValueCount".
     * 
     * @return
     */
    public long getMatchingValueCount() {
        return matchingValueCount;
    }

    /**
     * DOC zqin Comment method "setMatchingValueCount".
     * 
     * @param matchingValueCount
     */
    public void setMatchingValueCount(long matchingValueCount) {
        this.matchingValueCount = matchingValueCount;
    }

    /**
     * DOC zqin Comment method "getNotMatchingValueCount".
     * 
     * @return
     */
    public long getNotMatchingValueCount() {
        return notMatchingValueCount;
    }

    /**
     * DOC zqin Comment method "setNotMatchingValueCount".
     * 
     * @param notMatchingValueCount
     */
    public void setNotMatchingValueCount(long notMatchingValueCount) {
        this.notMatchingValueCount = notMatchingValueCount;
    }

}
