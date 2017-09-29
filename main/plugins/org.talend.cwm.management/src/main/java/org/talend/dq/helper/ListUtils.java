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
package org.talend.dq.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ListUtils {

    private ListUtils() {
        // do not use
    }

    /**
     * 
     * zshen Comment method "castList".
     * 
     * @param <T>
     * @param clazz the target class you want to transform.
     * @param c the source Collection you want to transform.
     * @return a list which only contain you want getting type of element.
     */
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }
}
