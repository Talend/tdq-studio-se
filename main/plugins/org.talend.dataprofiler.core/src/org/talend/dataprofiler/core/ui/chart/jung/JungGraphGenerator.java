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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.PluginConstant;
import org.talend.dq.indicators.graph.EdgeDisplayPredicate;
import org.talend.dq.indicators.graph.GraphBuilder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.ISOMLayout;
import edu.uci.ics.jung.visualization.PersistentLayout;
import edu.uci.ics.jung.visualization.PersistentLayoutImpl;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.GraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class JungGraphGenerator {

    protected static Logger log = Logger.getLogger(JungGraphGenerator.class);

    private static final int DOUBLE_CLICK_COUNT = 2;

    private static final String PERSIST_LAYOUT_FILE_NAME = "persistLayout.out"; //$NON-NLS-1$

    private Graph graph;

    private GraphBuilder graphbuilder;

    private PluggableRenderer pr, sr;

    private VisualizationViewer vv, sv;

    private Frame frame;

    private JFrame helpDialog;

    private boolean isPreview = false;

    /**
     * DOC bzhou JungGraphGenerator constructor comment.
     */
    public JungGraphGenerator(GraphBuilder graphbuilder, List<Object[]> listRows) {
        this.graphbuilder = graphbuilder;
        graph = graphbuilder.createMultiGraph(listRows);
    }

    // ADD yyi 2009-09-09 feature 8834
    public Composite generate(Composite parent, boolean isWithHelp, boolean isPreview) {
        this.isPreview = isPreview;
        return generate(parent, isWithHelp);
    }

    public Composite generate(Composite parent, boolean isWithHelp) {
        Composite frameComp = createAWTSWTComposite(parent);

        frame = SWT_AWT.new_Frame(frameComp);
        frame.setTitle(DefaultMessagesImpl.getString("JungGraphGenerator.NominalAnalysis")); //$NON-NLS-1$

        pr = new LineRender(graphbuilder);
        sr = new LineRender(graphbuilder);

        PersistentLayout layout = new PersistentLayoutImpl(new ISOMLayout(graph));
        VisualizationModel vm = new DefaultVisualizationModel(layout);
        vv = new VisualizationViewer(vm, pr);
        sv = new SatelliteVisualizationViewer(vv, vm, sr, new Dimension(200, 200));

        configureVViewer(vv);

        // create contollers
        JPanel controllers = createToolControllers(vv);

        // MOD yyi 2009-09-09 feature 8834
        if (this.isPreview)
            controllers.setVisible(false);

        JPanel panel = new GraphZoomScrollPane(vv);

        if (isWithHelp) {
            helpDialog = createSatelliteDialog(vv, sv);
            controllers.add(createShowSatelliteCheck(helpDialog));
        }

        frame.add(panel);
        frame.add(controllers, BorderLayout.SOUTH);
        frame.validate();

        addListeners();

        return frameComp;
    }

    /**
     * DOC bZhou Comment method "createShowSatelliteCheck".
     * 
     * @param dialog
     * @return
     */
    private JCheckBox createShowSatelliteCheck(final JFrame dialog) {
        final JCheckBox showSatellite = new JCheckBox(DefaultMessagesImpl.getString("JungGraphGenerator.SatelliteView")); //$NON-NLS-1$
        showSatellite.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(showSatellite.isSelected());
            }
        });
        return showSatellite;
    }

    /**
     * DOC bZhou Comment method "createToolControllers".
     * 
     * @param vv
     * @return
     */
    private JPanel createToolControllers(final VisualizationViewer vv) {
        final GraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);

        JPanel pSlider = createSlider();

        JCheckBox inverse = createInverseCheck(vv);

        ScalingControl scaler = new CrossoverScalingControl();
        JButton plus = createPlusScaler(vv, scaler);
        JButton minus = createMinusScaler(vv, scaler);

        JButton reset = createResetBTN(vv);

        // set mode seleciton box
        JCheckBox checkBox = createModeSwitcher(graphMouse);

        final PersistentLayout pl = (PersistentLayout) vv.getGraphLayout();
        JButton persist = createPersistBTN(pl, PERSIST_LAYOUT_FILE_NAME);
        JButton restore = createRestoreBTN(vv, pl, PERSIST_LAYOUT_FILE_NAME);

        JPanel jp2 = new JPanel();
        jp2.setLayout(new java.awt.GridLayout(2, 1));
        jp2.add(inverse);
        jp2.add(checkBox);

        JPanel jp3 = new JPanel();
        jp3.setLayout(new java.awt.GridLayout(2, 1));
        jp3.add(persist);
        jp3.add(restore);

        JPanel controls = new JPanel();
        controls.add(pSlider);
        controls.add(plus);
        controls.add(minus);
        controls.add(reset);
        controls.add(jp2);
        controls.add(jp3);

        return controls;
    }

    /**
     * DOC bZhou Comment method "createRestoreBTN".
     * 
     * @param vv
     * @param pl
     * @param fileName
     * @return
     */
    private JButton createRestoreBTN(final VisualizationViewer vv, final PersistentLayout pl, final String fileName) {
        JButton restore = new JButton(DefaultMessagesImpl.getString("JungGraphGenerator.RestoreLayout")); //$NON-NLS-1$
        restore.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    pl.restore(fileName);
                    vv.repaint();
                } catch (Exception e1) {
                    log.error(e1, e1);
                }
            }
        });
        return restore;
    }

    /**
     * DOC bZhou Comment method "createPersistBTN".
     * 
     * @param pl
     * @param fileName
     * @return
     */
    private JButton createPersistBTN(final PersistentLayout pl, final String fileName) {
        JButton persist = new JButton(DefaultMessagesImpl.getString("JungGraphGenerator.SaveLayout")); //$NON-NLS-1$
        persist.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // open file select dialog.
                // JFileChooser fileChooser = new JFileChooser();
                // fileChooser.setDialogTitle("Choose a file");
                // int result = fileChooser.showOpenDialog((JButton) e.getSource());
                //
                // if (result == JFileChooser.APPROVE_OPTION) {
                // File file = fileChooser.getSelectedFile();
                // }

                try {
                    pl.persist(fileName);
                } catch (IOException e1) {
                    log.error(e1, e1);
                }
            }
        });
        return persist;
    }

    /**
     * DOC bZhou Comment method "createModeSwitcher".
     * 
     * @param graphMouse
     * @return
     */
    private JCheckBox createModeSwitcher(final GraphMouse graphMouse) {
        final JCheckBox checkBox = new JCheckBox(DefaultMessagesImpl.getString("JungGraphGenerator.Pick")); //$NON-NLS-1$
        checkBox.addActionListener(new ActionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                DefaultModalGraphMouse modalGraphMouse = (DefaultModalGraphMouse) graphMouse;
                if (checkBox.isSelected()) {
                    modalGraphMouse.setMode(Mode.PICKING);
                } else {
                    modalGraphMouse.setMode(Mode.TRANSFORMING);
                }
            }
        });
        return checkBox;
    }

    /**
     * DOC bZhou Comment method "createResetBTN".
     * 
     * @param vv
     * @return
     */
    private JButton createResetBTN(final VisualizationViewer vv) {
        JButton reset = new JButton(DefaultMessagesImpl.getString("JungGraphGenerator.Reset")); //$NON-NLS-1$
        reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                vv.getLayoutTransformer().setToIdentity();
                vv.getViewTransformer().setToIdentity();

            }
        });
        return reset;
    }

    /**
     * DOC bZhou Comment method "createMinusScaler".
     * 
     * @param vv
     * @param scaler
     * @return
     */
    private JButton createMinusScaler(final VisualizationViewer vv, final ScalingControl scaler) {
        JButton minus = new JButton("-");//$NON-NLS-1$
        minus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });
        return minus;
    }

    /**
     * DOC bZhou Comment method "createPlusScaler".
     * 
     * @param vv
     * @param scaler
     * @return
     */
    private JButton createPlusScaler(final VisualizationViewer vv, final ScalingControl scaler) {
        JButton plus = new JButton("+"); //$NON-NLS-1$
        plus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        return plus;
    }

    /**
     * DOC bZhou Comment method "createInverseCheck".
     * 
     * @param vv
     * @return
     */
    private JCheckBox createInverseCheck(final VisualizationViewer vv) {
        final JCheckBox inverse = new JCheckBox(DefaultMessagesImpl.getString("JungGraphGenerator.InverseEdgeWeight")); //$NON-NLS-1$
        inverse.addActionListener(new ActionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                graphbuilder.setProportionalWidth(inverse.isSelected());
                vv.repaint();
                log.info(DefaultMessagesImpl.getString("JungGraphGenerator.Inverse")); //$NON-NLS-1$
            }
        });
        return inverse;
    }

    /**
     * DOC bZhou Comment method "createSlider".
     * 
     * @return
     */
    private JPanel createSlider() {
        JPanel pSlider = new JPanel();
        pSlider.setLayout(new BoxLayout(pSlider, BoxLayout.Y_AXIS));
        pSlider.setBorder(new TitledBorder(DefaultMessagesImpl.getString("JungGraphGenerator.FilterEdgeWeight"))); //$NON-NLS-1$

        final JSlider slider = new JSlider(0, 10, 0);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setSnapToTicks(true);
        slider.addChangeListener(new ChangeListener() {

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
             */
            public void stateChanged(ChangeEvent e) {
                int weight = slider.getValue();
                EdgeDisplayPredicate edgePredicate = (EdgeDisplayPredicate) pr.getEdgeIncludePredicate();
                edgePredicate.filterSmall(weight);
                // ((VertexDisplayPredicate) pr.getVertexIncludePredicate()).filterSmall(true, weight);
                // log.info("sliding..." + degree);
            }
        });
        pSlider.add(slider);
        return pSlider;
    }

    /**
     * DOC bZhou Comment method "configureVViewer".
     * 
     * @param vv
     */
    private void configureVViewer(final VisualizationViewer vv) {
        // vv = new VisualizationViewer(new CircleLayout(graph), pr);
        // vv = new VisualizationViewer(new SpringLayout(graph), pr);
        // vv = new VisualizationViewer(new KKLayout(graph), pr);
        vv.setBackground(Color.white);
        vv.setPickSupport(new ShapePickSupport());

        vv.addPostRenderPaintable(new VisualizationViewer.Paintable() {

            int x;

            int y;

            Font font;

            FontMetrics metrics;

            int swidth;

            int sheight;

            String str = PluginConstant.EMPTY_STRING; //$NON-NLS-1$

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

        // vv.addGraphMouseListener(new TestGraphMouseListener());

        // add my listener for ToolTips
        vv.setToolTipFunction(new DefaultToolTipFunction());
    }

    /**
     * DOC bZhou Comment method "createSatelliteView".
     * 
     * @param vv
     * @param sv
     * @return
     */
    private JFrame createSatelliteDialog(final VisualizationViewer vv, VisualizationViewer sv) {
        final JFrame dialog = new JFrame();
        dialog.setTitle(DefaultMessagesImpl.getString("JungGraphGenerator.Satellite")); //$NON-NLS-1$

        ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = createPlusScaler(vv, scaler);

        JButton minus = createMinusScaler(vv, scaler);

        JButton reset = createResetBTN(vv);
        JButton help = createHelpBTN();

        JPanel controls = new JPanel(new java.awt.GridLayout(2, 2));
        controls.add(plus);
        controls.add(minus);
        controls.add(reset);
        controls.add(help);

        dialog.add(sv);
        dialog.add(controls, BorderLayout.SOUTH);
        return dialog;
    }

    /**
     * DOC bZhou Comment method "createHelpBTN".
     * 
     * @return
     */
    private JButton createHelpBTN() {
        final JButton help = new JButton(DefaultMessagesImpl.getString("JungGraphGenerator.Help")); //$NON-NLS-1$
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String title = DefaultMessagesImpl.getString("JungGraphGenerator.Instruction");
                String message = DefaultMessagesImpl.getString("JungGraphGenerator.Instructions");
                showHelpMessage(null, title, message);
            }

            private void showHelpMessage(Component parent, String title, String message) {
                JOptionPane.showMessageDialog(null, title, message, JOptionPane.PLAIN_MESSAGE);
            }
        });
        return help;
    }

    private Composite createAWTSWTComposite(Composite parent) {
        Composite frameComp = new Composite(parent, SWT.EMBEDDED);
        frameComp.setLayout(new GridLayout());
        frameComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        frameComp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

        return frameComp;
    }

    /**
     * DOC BZhou Comment method "addListeners".
     */
    private void addListeners() {
        vv.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == DOUBLE_CLICK_COUNT) {
                    // show in full screem.
                    showFullDialog();
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    // show menu.
                    PopupMenu menu = new PopupMenu();
                    MenuItem item = new MenuItem(DefaultMessagesImpl.getString("JungGraphGenerator.ShowFullscreen")); //$NON-NLS-1$
                    menu.add(item);
                    vv.add(menu);
                    menu.show(vv, e.getX(), e.getY());

                    item.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0) {
                            showFullDialog();

                        }
                    });
                }
            }

            /**
             * DOC BZhou Comment method "showFullDialog".
             */
            private void showFullDialog() {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {

                        new JungGraphChartDialog(JungGraphGenerator.this).open();
                    }
                });
            }
        });

    }

}
