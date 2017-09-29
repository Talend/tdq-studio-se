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
package org.talend.dataprofiler.chart.util;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.browser.WebBrowserEditor;
import org.eclipse.ui.internal.browser.WebBrowserEditorInput;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public final class ChartUtils {

    private static final Logger log = Logger.getLogger(ChartUtils.class);

    private ChartUtils() {

    }

    public static ImageDescriptor convertToImage(JFreeChart chart, int maxWidth, int maxHeight) throws IOException {
        BufferedImage image = chart.createBufferedImage(maxWidth, maxHeight);
        ImageDescriptor descriptor = bufferedToDescriptorOptimized(image);
        return descriptor;
    }

    public static ImageDescriptor bufferedToDescriptorOptimized(final BufferedImage image) throws IOException {
        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream pipedInputStream = new PipedInputStream();
        output.connect(pipedInputStream);

        try {
            new Thread() {

                @Override
                public void run() {
                    try {
                        ChartUtilities.writeBufferedImageAsPNG(output, image, false, 0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }.start();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
        }

        ImageData img = new ImageData(pipedInputStream);
        ImageDescriptor descriptor = ImageDescriptor.createFromImageData(img);
        return descriptor;
    }

    /**
     * Create a AWT_SWT bridge composite for displaying the <CODE>ChartPanel</CODE>.
     * 
     * @param composite
     * @param gd
     * @param chartPanel
     */
    public static ChartPanel createAWTSWTComp(Composite composite, GridData gd, JFreeChart chart) {

        ChartPanel chartPanel = new ChartPanel(chart);
        Composite frameComp = new Composite(composite, SWT.EMBEDDED);
        frameComp.setLayout(new GridLayout());
        frameComp.setLayoutData(gd);
        frameComp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

        Frame frame = SWT_AWT.new_Frame(frameComp);
        frame.setLayout(new java.awt.GridLayout());
        frame.add(chartPanel);
        frame.validate();

        return chartPanel;
    }

    /**
     * DOC Administrator Comment method "openReferenceLink".
     * 
     * @param httpurl
     * */
    public static void openReferenceLink(String httpurl) {

        if (httpurl != null) {
            try {
                WebBrowserEditor.open(new WebBrowserEditorInput(new URL(httpurl)));
            } catch (MalformedURLException e1) {
                log.log(Level.WARN, e1.getMessage());
            }
        }
    }

}
