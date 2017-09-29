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
package org.talend.dataprofiler.rcp.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 * $Id: Messages.java,v 1.2 2007/03/07 05:08:59 pub Exp $
 * 
 */
public final class Messages {

    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

    private static final String PLUGIN_ID = "org.talend.dataprofiler.rcp"; //$NON-NLS-1$

    private static ResourceBundle resource = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    }

    /**
     * DOC qzhang Comment method "getString".
     * 
     * @param key
     * @return
     */
    public static String getString(String key) {
        try {
            return getString(key, PLUGIN_ID, resource);
        } catch (MissingResourceException e) {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * DOC qzhang Comment method "getString".
     * 
     * @param key
     * @param resourceBundle
     * @param args
     * @return
     */
    public static String getString(String key, Object... args) {
        return getString(key, PLUGIN_ID, resource, args);
    }

    /**
     * Returns the i18n formatted message for <i>key</i> in the specified bundle.
     * 
     * @param key - the key for the desired string
     * @param resourceBundle - the ResourceBundle to search in
     * @return the string for the given key in the given resource bundle
     */
    public static String getString(String key, String pluginId, ResourceBundle resourceBundle) {
        if (resourceBundle == null) {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        try {
            if (pluginId != null) {
                // String babiliTranslation = BabiliTool.getBabiliTranslation(key, pluginId);
                // if (babiliTranslation != null) {
                // return babiliTranslation;
                // }
            }
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Returns the i18n formatted message for <i>key</i> and <i>args</i> in the specified bundle.
     * 
     * @param key - the key for the desired string
     * @param resourceBundle - the ResourceBundle to search in
     * @param args - arg to include in the string
     * @return the string for the given key in the given resource bundle
     */
    public static String getString(String key, String pluginId, ResourceBundle resourceBundle, Object[] args) {
        return MessageFormat.format(getString(key, pluginId, resourceBundle), args);
    }

}
