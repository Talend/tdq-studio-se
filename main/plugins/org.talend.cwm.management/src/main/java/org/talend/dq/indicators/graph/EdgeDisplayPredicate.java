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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import edu.uci.ics.jung.graph.Edge;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class EdgeDisplayPredicate implements Predicate {

    public final static int DEFAULT_WEIGHT = 0;

    protected GraphBuilder builder;

    private int degree = 0;

    private Set<Edge> edges;

    /**
     * DOC bZhou EdgeDisplayPredicate constructor comment.
     */
    public EdgeDisplayPredicate(GraphBuilder builder) {
        this.builder = builder;
    }

    /**
     * DOC bZhou Comment method "filterSmall".
     * 
     * @param degree
     */
    public void filterSmall(int degree) {
        edges = new HashSet<Edge>();
        this.degree = degree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object object) {
        Edge edge = (Edge) object;

        return builder != null && builder.getEdgeWidth(builder.getEdgeWeight().getNumber(edge).intValue()) > degree;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

}
