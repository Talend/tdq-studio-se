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
package org.talend.dataprofiler.migration.helper;

import org.talend.utils.ProductVersion;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class VersionComparator {

    private ProductVersion v1;

    private ProductVersion v2;

    public VersionComparator(ProductVersion v1, ProductVersion v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     * DOC bZhou Comment method "isLower".
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static boolean isLower(ProductVersion v1, ProductVersion v2) {
        assert v1 != null;
        assert v2 != null;

        return v1.compareTo(v2) < 0;
    }

    /**
     * DOC bZhou Comment method "isHigher".
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static boolean isHigher(ProductVersion v1, ProductVersion v2) {
        assert v1 != null;
        assert v2 != null;

        return v1.compareTo(v2) > 0;
    }

    /**
     * DOC bZhou Comment method "isEqual".
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static boolean isEqual(ProductVersion v1, ProductVersion v2) {
        assert v1 != null;
        assert v2 != null;

        return v1.compareTo(v2) == 0;
    }

    /**
     * DOC bZhou Comment method "isLower".
     * 
     * @return
     */
    public boolean isLower() {
        return isLower(v1, v2);
    }

    /**
     * DOC bZhou Comment method "isHigher".
     * 
     * @return
     */
    public boolean isHigher() {
        return isHigher(v1, v2);
    }

    /**
     * DOC bZhou Comment method "isEqual".
     * 
     * @return
     */
    public boolean isEqual() {
        return isEqual(v1, v2);
    }

    /**
     * Getter for v1.
     * 
     * @return the v1
     */
    public ProductVersion getV1() {
        return this.v1;
    }

    /**
     * Getter for v2.
     * 
     * @return the v2
     */
    public ProductVersion getV2() {
        return this.v2;
    }
}
