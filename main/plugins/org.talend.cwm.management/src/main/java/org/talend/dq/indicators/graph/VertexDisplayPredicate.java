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
package org.talend.dq.indicators.graph;

import org.apache.commons.collections.Predicate;

import edu.uci.ics.jung.graph.Vertex;

public final class VertexDisplayPredicate implements Predicate {

    public final static int DEFAULT_DEGREE = 0;

    protected boolean filter_small;

    protected int degree;

    public VertexDisplayPredicate(boolean filter) {
        this.filter_small = filter;
    }

    /**
     * DOC bZhou Comment method "filterSmall".
     * 
     * @param isFilter
     */
    public void filterSmall(boolean isFilter) {
        this.filter_small = isFilter;
    }

    /**
     * DOC bZhou Comment method "filterSmall".
     * 
     * @param isFilter
     * @param degree
     */
    public void filterSmall(boolean isFilter, int degree) {
        this.filter_small = isFilter;
        this.degree = degree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object object) {
        Vertex v = (Vertex) object;
        if (filter_small) {
            if (degree == 0) {
                return (v.degree() >= DEFAULT_DEGREE);
            }

            return (v.degree() >= degree);
        }

        return true;
    }
}
