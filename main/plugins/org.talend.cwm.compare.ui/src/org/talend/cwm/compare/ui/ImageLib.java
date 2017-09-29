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
package org.talend.cwm.compare.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Store and lazy load Imaged. <br/>
 * 
 * $Id: ImageLib.java,v 1.5 2007/04/05 05:33:07 pub Exp $
 * 
 */
public class ImageLib {

    protected static Logger log = Logger.getLogger(ImageLib.class);

    private static ImageRegistry imageRegistry;

    private static URL iconURL;

    public static final String UPDATE_IMAGE = "update.gif"; //$NON-NLS-1$

    public static final String COMPARE_IMAGE = "compare_view.gif"; //$NON-NLS-1$

    /**
     * get <code>ImageDescriptor</code> with special imageName.
     * 
     * @param imageName
     * @return
     */
    public static ImageDescriptor getImageDescriptor(String imageName) {
        if (imageRegistry == null) {
            initialize();
        }
        ImageDescriptor imageDesc = imageRegistry.getDescriptor(imageName);
        if (imageDesc == null) {
            addImage(imageName);
            return imageRegistry.getDescriptor(imageName);
        }
        return imageDesc;
    }

    /**
     * get <code>Image</code> with special imageName.
     * 
     * @param imageName
     * @return
     */
    public static Image getImage(String imageName) {
        if (imageRegistry == null) {
            initialize();
        }
        if (imageRegistry == null) {
            return null;
        }
        Image image = imageRegistry.get(imageName);
        if (image == null) {
            addImage(imageName);
            return imageRegistry.get(imageName);
        }
        return image;
    }

    /**
     * initialize the fieds.
     */
    static void initialize() {
        Activator amcPlugin = Activator.getDefault();
        if (amcPlugin != null) {
            imageRegistry = amcPlugin.getImageRegistry();
            iconURL = getIconLocation();
        }
    }

    /**
     * get current icons URL.
     * 
     * @return
     */
    private static URL getIconLocation() {
        URL installURL = Activator.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
        try {
            return new URL(installURL, "icons/"); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            log.error(e, e);
            return null;
        }
    }

    /**
     * store the image with special name(the name with suffix,such as "sample.gif").
     * 
     * @param iconName
     */
    public static void addImage(String iconName) {
        try {
            ImageDescriptor descriptor = ImageDescriptor.createFromURL(new URL(iconURL, iconName));
            imageRegistry.put(iconName, descriptor);
        } catch (MalformedURLException e) {
            // skip, but try to go on to the next one...
        }
    }

}
