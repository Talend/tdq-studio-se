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

import edu.uci.ics.jung.graph.DirectedEdge;
import edu.uci.ics.jung.graph.UndirectedEdge;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class DirectionDisplayPredicate implements Predicate {

    protected boolean show_d;

    protected boolean show_u;

    public DirectionDisplayPredicate(boolean show_d, boolean show_u) {
        this.show_d = show_d;
        this.show_u = show_u;
    }

    public void showDirected(boolean b) {
        show_d = b;
    }

    public void showUndirected(boolean b) {
        show_u = b;
    }

    public boolean evaluate(Object arg0) {
        if (arg0 instanceof DirectedEdge && show_d)
            return true;
        if (arg0 instanceof UndirectedEdge && show_u)
            return true;
        return false;
    }
}
