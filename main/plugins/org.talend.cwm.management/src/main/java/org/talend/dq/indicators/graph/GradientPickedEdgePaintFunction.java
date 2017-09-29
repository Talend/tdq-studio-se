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

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections.Predicate;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.GradientEdgePaintFunction;
import edu.uci.ics.jung.graph.predicates.SelfLoopEdgePredicate;
import edu.uci.ics.jung.visualization.HasGraphLayout;
import edu.uci.ics.jung.visualization.PickedInfo;
import edu.uci.ics.jung.visualization.transform.LayoutTransformer;

public class GradientPickedEdgePaintFunction extends GradientEdgePaintFunction {

    private PickedInfo pi;

    private EdgePaintFunction defaultFunc;

    private final Predicate self_loop = SelfLoopEdgePredicate.getInstance();

    protected boolean fill_edge = false;

    public GradientPickedEdgePaintFunction(EdgePaintFunction defaultEdgePaintFunction, HasGraphLayout vv,
            LayoutTransformer transformer, PickedInfo pi) {
        super(Color.WHITE, Color.BLACK, vv, transformer);
        this.defaultFunc = defaultEdgePaintFunction;
        this.pi = pi;
    }

    public void useFill(boolean b) {
        fill_edge = b;
    }

    public Paint getDrawPaint(Edge e) {
            return defaultFunc.getDrawPaint(e);

    }

    protected Color getColor2(Edge e) {
        return pi.isPicked(e) ? Color.CYAN : c2;
    }

    public Paint getFillPaint(Edge e) {
        if (self_loop.evaluate(e) || !fill_edge)
            return null;
        else
            return getDrawPaint(e);
    }

}
