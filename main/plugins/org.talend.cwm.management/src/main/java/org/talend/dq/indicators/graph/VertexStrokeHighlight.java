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

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexStrokeFunction;
import edu.uci.ics.jung.visualization.PickedInfo;

public final class VertexStrokeHighlight implements VertexStrokeFunction {

    protected boolean highlight = false;

    protected Stroke heavy = new BasicStroke(5);

    protected Stroke medium = new BasicStroke(3);

    protected Stroke light = new BasicStroke(1);

    protected PickedInfo pi;

    public VertexStrokeHighlight(PickedInfo pi) {
        this.pi = pi;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public Stroke getStroke(Vertex v) {
        if (highlight) {
            if (pi.isPicked(v))
                return heavy;
            else {
                for (Iterator<?> iter = v.getNeighbors().iterator(); iter.hasNext();) {
                    Vertex w = (Vertex) iter.next();
                    if (pi.isPicked(w))
                        return medium;
                }
                return light;
            }
        } else
            return light;
    }
}
