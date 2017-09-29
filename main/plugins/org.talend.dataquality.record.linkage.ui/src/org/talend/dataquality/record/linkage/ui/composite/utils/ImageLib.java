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
package org.talend.dataquality.record.linkage.ui.composite.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

/**
 * Store and lazy load Imaged. <br/>
 * 
 * $Id: ImageLib.java,v 1.5 2007/04/05 05:33:07 pub Exp $
 * 
 */
public final class ImageLib {

    protected static Logger log = Logger.getLogger(ImageLib.class);

    private static ImageRegistry imageRegistry;

    private static URL iconURL;

    public static final String SWITCH_IMAGE = "switch.png"; //$NON-NLS-1$

    // public static final String REFRESH_IMAGE = "refresh.gif"; //$NON-NLS-1$
    public static final String EXPLORE_IMAGE = "magnifier.png"; //$NON-NLS-1$

    public static final String REFRESH_IMAGE = "run_exc.gif"; //$NON-NLS-1$

    public static final String FOLDERNODE_IMAGE = "folder.png"; //$NON-NLS-1$

    public static final String FOLDER_WIZ_IMAGE = "folder_wiz.gif"; //$NON-NLS-1$

    public static final String FOLDER_NEW_IMAGE = "folder_new.png"; //$NON-NLS-1$

    public static final String PROJECT_ACTIVE = "prject_active.gif"; //$NON-NLS-1$

    public static final String REPORT_HISTORIZE = "report_go.png"; //$NON-NLS-1$

    public static final String NEW_CONNECTION = "new_alias.gif"; //$NON-NLS-1$

    public static final String DELETE_ACTION = "cross.png"; //$NON-NLS-1$

    public static final String ADD_ACTION = "add.gif"; //$NON-NLS-1$

    public static final String EDIT_ACTION = "edit.png"; //$NON-NLS-1$

    public static final String UP_ACTION = "up.gif"; //$NON-NLS-1$

    public static final String DOWN_ACTION = "down.gif"; //$NON-NLS-1$

    public static final String PASTE_ACTION = "paste.gif"; //$NON-NLS-1$

    public static final String COPY_ACTION = "copy.gif"; //$NON-NLS-1$

    public static final String SECTION_PREVIEW = "eye.png"; //$NON-NLS-1$

    public static final String ACTION_NEW_REPORT = "report_add.png"; //$NON-NLS-1$

    public static final String ACTION_NEW_ANALYSIS = "chart_bar_add.png"; //$NON-NLS-1$

    public static final String INDICATOR_OPTION = "page_white_gear.png"; //$NON-NLS-1$

    public static final String ANALYSIS_OBJECT = "chart_bar.png"; //$NON-NLS-1$

    public static final String REPORT_OBJECT = "report.png"; //$NON-NLS-1$

    public static final String TD_COLUMN = "TdColumn.gif"; //$NON-NLS-1$

    public static final String TD_DATAPROVIDER = "TdDataProvider.gif"; //$NON-NLS-1$

    public static final String MDM_CONNECTION = "mdm_metadata.png"; //$NON-NLS-1$

    public static final String EDITCONNECTION = "sample.gif"; //$NON-NLS-1$

    public static final String CREATE_SQL_ACTION = "new_sqlEditor.png"; //$NON-NLS-1$

    public static final String LICENSE_WIZ = "license_wiz.png"; //$NON-NLS-1$

    public static final String PATTERN_REG = "pattern.png"; //$NON-NLS-1$

    public static final String REGISTER_WIZ = "register_wiz.png"; //$NON-NLS-1$

    public static final String CONNECTION = "connection.gif"; //$NON-NLS-1$

    public static final String METADATA = "metadata.png"; //$NON-NLS-1$

    public static final String OPTION = "option.png"; //$NON-NLS-1$

    public static final String LIBRARIES = "libraries.png"; //$NON-NLS-1$

    public static final String DATA_PROFILING = "server_chart.png"; //$NON-NLS-1$

    public static final String EXPORT_REPORT = "export_rep.gif"; //$NON-NLS-1$

    public static final String LEVEL_WARNING = "level_warning.gif"; //$NON-NLS-1$

    public static final String WARN_OVR = "warn_ovr.gif"; //$NON-NLS-1$

    public static final String EMOTICON_SMILE = "emoticon_smile.png"; //$NON-NLS-1$

    public static final String EXCLAMATION = "exclamation.png"; //$NON-NLS-1$

    public static final String CATALOG = "catalog.jpg"; //$NON-NLS-1$

    public static final String ASC_SORT = "asc.gif"; //$NON-NLS-1$

    public static final String DESC_SORT = "desc.gif"; //$NON-NLS-1$

    public static final String SCHEMA = "schema.gif"; //$NON-NLS-1$

    public static final String TABLE = "TdTable.gif"; //$NON-NLS-1$

    public static final String VIEW = "view.gif"; //$NON-NLS-1$

    public static final String DQ_RULE = "dqrule_red.png"; //$NON-NLS-1$

    public static final String ADD_DQ = "add_dqrule.png";//$NON-NLS-1$

    public static final String ADD_PATTERN = "add_pattern.png"; //$NON-NLS-1$

    public static final String SAVE = "save.gif"; //$NON-NLS-1$

    public static final String IMPORT = "import.gif"; //$NON-NLS-1$

    public static final String EXPORT = "export.gif"; //$NON-NLS-1$

    /** PK icon from SQL Explorer. */
    public static final String PK_DECORATE = "pk_decorate.gif"; //$NON-NLS-1$

    public static final String EDIT_COPY = "copy.gif"; //$NON-NLS-1$

    /** index icon from SQL Explorer. */
    public static final String INDEX_VIEW = "index.gif"; //$NON-NLS-1$

    /** Collapse all icon. */
    public static final String COLLAPSE_ALL = "collapseall.gif"; //$NON-NLS-1$

    /** Expand all icon. */
    public static final String EXPAND_ALL = "expandall.gif"; //$NON-NLS-1$

    /** Icon for primary key. */
    public static final String PK_COLUMN = "pkColumn.gif"; //$NON-NLS-1$

    /** Icon for refresh workspace. */
    public static final String REFRESH_SPACE = "refresh.gif"; //$NON-NLS-1$

    /** Icon for Talend Exchange folder. */
    public static final String EXCHANGE = "ecosystem_view.png"; //$NON-NLS-1$

    /** Icon of indicator's definition. */
    public static final String IND_DEFINITION = "IndicatorDefinition.png"; //$NON-NLS-1$

    public static final String ADD_IND_DEFINITION = "IndicatorAdd.png"; //$NON-NLS-1$

    /** Icon of indicator's category. */
    public static final String IND_CATEGORY = "IndicatorCategory.gif"; //$NON-NLS-1$

    /** Icon for Pagination. */
    public static final String ICON_PAGE_LAST_LNK = "bottomb.gif"; //$NON-NLS-1$

    public static final String ICON_PAGE_FIRST_LNK = "topb.gif"; //$NON-NLS-1$

    public static final String ICON_PAGE_PREV_LNK = "prevb.gif"; //$NON-NLS-1$

    public static final String ICON_PAGE_NEXT_LNK = "nextb.gif"; //$NON-NLS-1$

    public static final String ICON_INFO = "info.gif"; //$NON-NLS-1$

    public static final String ICON_LOCK = "lock.gif"; //$NON-NLS-1$

    public static final String XML_DOC = "xmldoc.gif"; //$NON-NLS-1$

    public static final String XML_ELEMENT_DOC = "xmlele.gif"; //$NON-NLS-1$

    public static final String ICON_PROCESS = "process_icon.png"; //$NON-NLS-1$

    public static final String ICON_PROCESS_WIZARD = "process_wiz.png"; //$NON-NLS-1$

    public static final String ICON_ERROR_INFO = "error.gif"; //$NON-NLS-1$

    public static final String RECYCLEBIN_EMPTY = "recyclebinempty.png"; //$NON-NLS-1$

    public static final String RECYCLEBIN_OVERLAY = "recycle_bino_verlay.gif"; //$NON-NLS-1$

    public static final String RECYCLEBIN_FULL = "recyclebinfull.png"; //$NON-NLS-1$

    public static final String ICON_ERROR_VAR = "error_ovr.gif"; //$NON-NLS-1$

    public static final String ICON_ADD_VAR = "add_ovr.gif"; //$NON-NLS-1$

    public static final String FILE_DELIMITED = "filedelimited.gif"; //$NON-NLS-1$

    public static final String SOURCE_FILE = "editor.png"; //$NON-NLS-1$

    public static final String JAR_FILE = "jar_obj.gif"; //$NON-NLS-1$

    public static final String ADD_SYN = "synonym/book_add.png"; //$NON-NLS-1$

    public static final String DELETE_SYN = "synonym/book_delete.png"; //$NON-NLS-1$

    public static final String EDIT_SYN = "synonym/book_edit.png"; //$NON-NLS-1$

    public static final String FILTER_UP = "search_prev.gif"; //$NON-NLS-1$

    public static final String FILTER_DOWN = "search_next.gif"; //$NON-NLS-1$

    public static final String FILTER_RUN = "searchres.png"; //$NON-NLS-1$

    public static final String FILTER_CLOSE = "search_rem.gif"; //$NON-NLS-1$

    public static final String RULE_TEST = "test.gif"; //$NON-NLS-1$

    public static final String ICON_LOCK_BYOTHER = "locked_red_overlay.gif"; //$NON-NLS-1$

    public static final String TICK_IMAGE = "checked.gif"; //$NON-NLS-1$

    public static final String PK_ICON = "primary_key.png"; //$NON-NLS-1$

    public static final String PLUGIN_ID = "org.talend.dataquality.record.linkage.ui"; //$NON-NLS-1$

    public static final String MASTER_IMAGE = "star_tMatchGroup_master.png";//$NON-NLS-1$

    public static final Color COLOR_GREY = new Color(null, 56, 55, 58);

    /**
     * DOC bzhou ImageLib constructor comment.
     */
    private ImageLib() {

    }

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
        if (imageRegistry == null) {
            imageRegistry = new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
            iconURL = getIconLocation();
        }
    }

    /**
     * get current icons URL.
     * 
     * @return
     */
    private static URL getIconLocation() {
        URL installURL = Platform.getBundle(PLUGIN_ID).getEntry("/"); //$NON-NLS-1$
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

    /**
     * DOC bZhou Comment method "createInvalidIcon".
     * 
     * @param originalImgName
     * @return
     */
    public static ImageDescriptor createInvalidIcon(String originalImgName) {
        return createInvalidIcon(getImageDescriptor(originalImgName));
    }

    /**
     * DOC bZhou Comment method "createInvalidIcon".
     * 
     * @param originalImg
     * @return
     */
    public static ImageDescriptor createInvalidIcon(ImageDescriptor originalImg) {
        ImageDescriptor warnImg = getImageDescriptor(WARN_OVR);
        return originalImg != null ? createIcon(originalImg, warnImg) : null;
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImgName
     * @return
     */
    public static ImageDescriptor createLockedIcon(String originalImgName) {
        return createLockedIcon(getImageDescriptor(originalImgName));
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImg
     * @return
     */
    public static ImageDescriptor createLockedIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK);

        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    /**
     * DOC bZhou Comment method "createIcon".
     * 
     * @param originalImg
     * @param decorateImg
     * @return
     */
    public static ImageDescriptor createIcon(ImageDescriptor originalImg, ImageDescriptor decorateImg) {
        return createIcon(originalImg.createImage(), decorateImg);
    }

    /**
     * DOC xqliu Comment method "createIcon".
     * 
     * @param originalImg
     * @param decorateImg
     * @return
     */
    public static ImageDescriptor createIcon(Image originalImg, ImageDescriptor decorateImg) {
        return new DecorationOverlayIcon(originalImg, decorateImg, IDecoration.BOTTOM_RIGHT);
    }

    /**
     * DOC qiongli Comment method "createLockedIcon".
     * 
     * @param originalImgName
     * @return
     */
    public static ImageDescriptor createErrorIcon(String originalImgName) {
        return createErrorIcon(getImageDescriptor(originalImgName));
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImg
     * @return
     */
    public static ImageDescriptor createErrorIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_ERROR_VAR);

        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    /*
     * DOC qiongli Comment method "createAddedIcon".
     * 
     * @param originalImgName
     * 
     * @return
     */
    public static ImageDescriptor createAddedIcon(String originalImgName) {
        return createAddedIcon(getImageDescriptor(originalImgName));
    }

    /**
     * DOC qiongli Comment method "createAddedIcon".
     * 
     * @param originalImg
     * @return
     */
    public static ImageDescriptor createAddedIcon(ImageDescriptor originalImg) {
        ImageDescriptor addImg = getImageDescriptor(ICON_ADD_VAR);
        return originalImg != null ? new DecorationOverlayIcon(originalImg.createImage(), addImg, IDecoration.TOP_RIGHT) : null;
    }

    public static ImageDescriptor createLockedByOtherIcon(String originalImgName) {
        return createLockedByOtherIcon(getImageDescriptor(originalImgName));
    }

    public static ImageDescriptor createLockedByOtherIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK_BYOTHER);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    public static ImageDescriptor createLockedByOtherIcon(Image originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK_BYOTHER);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    public static ImageDescriptor createLockedByOwnIcon(String originalImgName) {
        return createLockedByOtherIcon(getImageDescriptor(originalImgName));
    }

    public static ImageDescriptor createLockedByOwnIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    public static ImageDescriptor createLockedByOwnIcon(Image originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

}
