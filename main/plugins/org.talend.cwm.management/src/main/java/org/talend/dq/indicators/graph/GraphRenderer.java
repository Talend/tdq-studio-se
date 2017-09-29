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


import edu.uci.ics.jung.graph.decorators.ConstantEdgeStringer;
import edu.uci.ics.jung.graph.decorators.ConstantVertexStringer;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.PluggableRenderer;

/**
 * @author scorreia
 * 
 * Getting renderers.
 */
public class GraphRenderer {

    PluggableRenderer pr;

    protected EdgeWeightStrokeFunction ewcs;

    protected VertexStrokeHighlight vsh;

    protected VertexStringer vs;

    protected VertexStringer vs_none;

    protected EdgeStringer es;

    protected EdgeStringer es_none;

    protected FontHandler ff;

    protected VertexShapeSizeAspect vssa;

    protected DirectionDisplayPredicate show_edge;

    protected DirectionDisplayPredicate show_arrow;

    protected VertexDisplayPredicate show_vertex;

    protected GradientPickedEdgePaintFunction edgePaint;

   public PluggableRenderer createPluggableRenderer() {
       // create decorators
        ff = new FontHandler();
        vs_none = new ConstantVertexStringer(null);
        es_none = new ConstantEdgeStringer(null);
        // vssa = new VertexShapeSizeAspect(voltages);
        show_edge = new DirectionDisplayPredicate(true, true);
        show_arrow = new DirectionDisplayPredicate(true, false);
        show_vertex = new VertexDisplayPredicate(false);

        pr = new PluggableRenderer();
        
        pr.setVertexStrokeFunction(vsh);
        pr.setVertexStringer(vs_none);
        pr.setVertexFontFunction(ff);
        pr.setVertexShapeFunction(vssa);
        pr.setVertexIncludePredicate(show_vertex);

        pr.setEdgePaintFunction(edgePaint);
        pr.setEdgeStringer(es_none);
        pr.setEdgeFontFunction(ff);
        pr.setEdgeStrokeFunction(ewcs);
        pr.setEdgeIncludePredicate(show_edge);
        pr.setEdgeShapeFunction(new EdgeShape.Line());
        pr.setEdgeArrowPredicate(show_arrow);

        return pr;
    }
}
