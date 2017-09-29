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
package org.talend.dataprofiler.core.ui.chart.jung;

import java.awt.Color;
import java.awt.Paint;

import org.talend.dq.indicators.graph.EdgeDisplayPredicate;
import org.talend.dq.indicators.graph.EdgeWeightStrokeFunction;
import org.talend.dq.indicators.graph.GraphBuilder;
import org.talend.utils.color.AWTColorUtils;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.PluggableRenderer;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class LineRender extends PluggableRenderer {

    private static Color TALEND_GRAY = Color.decode("#A7A8AA");

    public LineRender(final GraphBuilder graphbuilder) {
        setEdgeShapeFunction(new EdgeShape.Line());

        setVertexPaintFunction(new VertexPaintFunction() {

            public Paint getFillPaint(Vertex v) {
                final Object userDatum = v.getUserDatum(GraphBuilder.COLUMN_IDX_KEY);
                if (userDatum != null) {
                    Integer colIndex = (Integer) userDatum;
                    return AWTColorUtils.getColor(colIndex);
                }
                return null;
            }

            public Paint getDrawPaint(Vertex v) {
                return TALEND_GRAY;
            }
        });

        setVertexStringer(new VertexStringer() {

            public String getLabel(ArchetypeVertex v) {
                final Object userDatum = v.getUserDatum(GraphBuilder.V_LABEL_KEY);
                if (userDatum != null) {
                    return (String) userDatum;
                }
                return null;
            }
        });

        // pr.setEdgeStrokeFunction(new EdgeStrokeFunction() {
        //
        // public Stroke getStroke(Edge e) {
        // Integer weight = graphbuilder.getEdgeWeight().getNumber(e).intValue();
        // // (Integer) e.getUserDatum(GraphBuilder.E_WEIGHT_KEY);
        // // enlarge edges with small weight
        //
        // // return new BasicStroke(10 * weight / graphbuilder.getTotalWeight());
        // return new BasicStroke(10.0f / weight);
        // }
        // });

        setEdgePaintFunction(new EdgePaintFunction() {

            public Paint getFillPaint(Edge e) {
                return null;
            }

            public Paint getDrawPaint(Edge e) {
                return getInternalPaint(e);
            }

            private Paint getInternalPaint(Edge e) {
                return TALEND_GRAY;
            }
        });

        setEdgeStringer(new EdgeStringer() {

            public String getLabel(ArchetypeEdge arg0) {
                return String.valueOf(graphbuilder.getEdgeWeight().getNumber(arg0));
                // final Object userDatum = arg0.getUserDatum(GraphBuilder.E_LABEL_KEY);
                // if (userDatum != null) {
                // return (String) userDatum;
                // }
                // return null;
            }
        });

        setEdgeStrokeFunction(new EdgeWeightStrokeFunction(graphbuilder));

        setEdgeIncludePredicate(new EdgeDisplayPredicate(graphbuilder));

        // pr.setVertexIncludePredicate(new VertexDisplayPredicate(false));
    }
}
