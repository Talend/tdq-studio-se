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

import java.awt.Font;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeFontFunction;
import edu.uci.ics.jung.graph.decorators.VertexFontFunction;

public final class FontHandler implements VertexFontFunction, EdgeFontFunction {

    protected boolean bold = false;

    Font f = new Font("Helvetica", Font.PLAIN, 12); //$NON-NLS-1$

    Font b = new Font("Helvetica", Font.BOLD, 12); //$NON-NLS-1$

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public Font getFont(Vertex v) {
        if (bold)
            return b;
        else
            return f;
    }

    public Font getFont(Edge e) {
        if (bold)
            return b;
        else
            return f;
    }
}
