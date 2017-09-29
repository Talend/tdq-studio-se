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
package org.talend.dq.indicators.graph.tests;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.talend.dq.indicators.graph.GraphBuilder;
import org.talend.utils.color.AWTColorUtils;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.EdgeStrokeFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.GraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MyFirstMain {

    public static final String[][] DATA = { { "high", "overcast", "yes", "2" }, { "high", "rainy", "no", "1" },
            { "high", "rainy", "yes", "1200" }, { "high", "sunny", "no", "30" }, { "normal", "overcast", "yes", "22" },
            { "normal", "rainy", "no", "1" }, { "normal", "rainy", "yes", "200000" }, { "normal", "sunny", "yes", "27" },
            { "subnormal", "sunny", "yes", "7" }, { "subnormal", null, "yes", "2" } };

    
    
    /**
     * the visual component and renderer for the graph.
     */
    VisualizationViewer vv;



    private List<Object[]> allData;

    /**
     * DOC scorreia MyFirstMain constructor comment.
     */
    public MyFirstMain() {
    }

    public void run(final GraphBuilder graphbuilder) {
        
        
        Graph graph = graphbuilder.createMultiGraph(allData);

        PluggableRenderer pr = new PluggableRenderer();

        vv = new VisualizationViewer(new FRLayout(graph), pr);
        // vv = new VisualizationViewer(new CircleLayout(graph), pr);
        // vv = new VisualizationViewer(new SpringLayout(graph), pr);
        // vv = new VisualizationViewer(new KKLayout(graph), pr);
        vv.setBackground(Color.white);
        vv.setPickSupport(new ShapePickSupport());
        pr.setEdgeShapeFunction(new EdgeShape.Line());

        vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {

            int x;

            int y;

            Font font;

            FontMetrics metrics;

            int swidth;

            int sheight;

            String str = "My first Demo";

            public void paint(Graphics g) {
                Dimension d = vv.getSize();
                if (font == null) {
                    font = new Font(g.getFont().getName(), Font.BOLD, 30);
                    metrics = g.getFontMetrics(font);
                    swidth = metrics.stringWidth(str);
                    sheight = metrics.getMaxAscent() + metrics.getMaxDescent();
                    x = (d.width - swidth) / 2;
                    y = (int) (d.height - sheight * 1.5);
                }
                g.setFont(font);
                Color oldColor = g.getColor();
                g.setColor(Color.DARK_GRAY);
                g.drawString(str, x, y);
                g.setColor(oldColor);
            }

            public boolean useTransform() {
                return false;
            }
        });

        pr.setVertexPaintFunction(new VertexPaintFunction() {

            public Paint getFillPaint(Vertex v) {
                final Object userDatum = v.getUserDatum(GraphBuilder.COLUMN_IDX_KEY);
                if (userDatum != null) {
                    Integer colIndex = (Integer) userDatum;
                    return AWTColorUtils.getColor(colIndex);
                }
                return null;
            }

            public Paint getDrawPaint(Vertex v) {
                return Color.BLACK;
            }
        });

        pr.setVertexStringer(new VertexStringer() {
            public String getLabel(ArchetypeVertex v) {
                final Object userDatum = v.getUserDatum(GraphBuilder.V_LABEL_KEY);
                if (userDatum != null) {
                    return (String) userDatum;
                }
                return null;
            }
        });

        pr.setEdgeStrokeFunction(new EdgeStrokeFunction() {

            public Stroke getStroke(Edge e) {
                Integer weight = graphbuilder.getEdgeWeight().getNumber(e).intValue();
                // (Integer) e.getUserDatum(GraphBuilder.E_WEIGHT_KEY);
                // enlarge edges with small weight

                // return new BasicStroke(10 * weight / graphbuilder.getTotalWeight());
                return new BasicStroke(10.0f / weight);
            }
        });

        pr.setEdgePaintFunction(new EdgePaintFunction() {

            public Paint getFillPaint(Edge e) {
                return null;               
            }

            public Paint getDrawPaint(Edge e) {
                return getInternalPaint(e);
            }

            private Paint getInternalPaint(Edge e) {
                final Object userDatum = e.getUserDatum(GraphBuilder.E_ROWNUM_KEY);
                if (userDatum != null) {
                    Integer color = (Integer) userDatum;
                    return AWTColorUtils.getColor(color);
                }
                return Color.GRAY;
            }
        });

        pr.setEdgeStringer(new EdgeStringer() {
            public String getLabel(ArchetypeEdge arg0) {
                return String.valueOf(graphbuilder.getEdgeWeight().getNumber(arg0));
                // final Object userDatum = arg0.getUserDatum(GraphBuilder.E_LABEL_KEY);
                // if (userDatum != null) {
                // return (String) userDatum;
                // }
                // return null;
            }
        });
        // vv.addGraphMouseListener(new TestGraphMouseListener());

        // add my listener for ToolTips
        vv.setToolTipFunction(new DefaultToolTipFunction());

        // create a frome to hold the graph
        final JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final GraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);
        JMenuBar menu = new JMenuBar();
        menu.add(((DefaultModalGraphMouse) graphMouse).getModeMenu());
        panel.setCorner(menu);
        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });
        JButton reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                vv.getLayoutTransformer().setToIdentity();
                vv.getViewTransformer().setToIdentity();

            }
        });

        JPanel controls = new JPanel();
        controls.add(plus);
        controls.add(minus);
        controls.add(reset);
        content.add(controls, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    
    /**
     * Getter for allData.
     * 
     * @return the allData
     */
    public List<Object[]> getAllData() {
        return this.allData;
    }

    /**
     * Sets the allData.
     * 
     * @param allData the allData to set
     */
    public void setAllData(List<Object[]> allData) {
        this.allData = allData;
    }

    /**
     * DOC scorreia Comment method "createListObjects".
     * 
     * @return
     */
    public static List<Object[]> createListObjects() {
        List<Object[]> allData = new ArrayList<Object[]>();
        for (String[] str : DATA) {
            allData.add(str);
        }
        return allData;
    }

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        final MyFirstMain myFirstMain = new MyFirstMain();
        myFirstMain.setAllData(createListObjects());
        myFirstMain.run(new GraphBuilder());
    }
    
 

}
