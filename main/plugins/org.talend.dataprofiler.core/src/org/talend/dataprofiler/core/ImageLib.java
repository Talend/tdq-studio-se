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
package org.talend.dataprofiler.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.exchange.ExchangeCategoryRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeComponentRepNode;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnFolderRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFConnectionRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.RecycleBinRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.nodes.indicator.impl.IndicatorCategoryNode;
import org.talend.dq.nodes.indicator.impl.IndicatorNode;
import org.talend.dq.nodes.indicator.impl.PatternNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.resource.EResourceConstant;

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

    public static final String RUN_IMAGE = "run_exc.gif"; //$NON-NLS-1$

    /**
     * {@link IndicatorCategoryNode#getImageName()}
     */
    public static final String FOLDERNODE_IMAGE = "folder.png"; //$NON-NLS-1$

    public static final String FOLDER_WIZ_IMAGE = "folder_wiz.gif"; //$NON-NLS-1$

    public static final String FOLDER_NEW_IMAGE = "folder_new.png"; //$NON-NLS-1$

    public static final String PROJECT_ACTIVE = "prject_active.png"; //$NON-NLS-1$

    public static final String NEW_CONNECTION = "new_alias.gif"; //$NON-NLS-1$

    public static final String DELETE_ACTION = "cross.png"; //$NON-NLS-1$

    public static final String ADD_ACTION = "add.gif"; //$NON-NLS-1$

    public static final String UP_ACTION = "up.png"; //$NON-NLS-1$

    public static final String DOWN_ACTION = "down.png"; //$NON-NLS-1$

    public static final String PASTE_ACTION = "paste.png"; //$NON-NLS-1$

    public static final String COPY_ACTION = "copy.gif"; //$NON-NLS-1$

    public static final String SECTION_PREVIEW = "eye.png"; //$NON-NLS-1$

    public static final String ACTION_NEW_REPORT = "report_add.png"; //$NON-NLS-1$

    public static final String ACTION_NEW_ANALYSIS = "chart_bar_add.png"; //$NON-NLS-1$

    public static final String INDICATOR_OPTION_CHECKED = "option_checked.png"; //$NON-NLS-1$

    public static final String ANALYSIS_OBJECT = "chart_bar.png"; //$NON-NLS-1$

    public static final String REPORT_OBJECT = "report.png"; //$NON-NLS-1$

    public static final String TD_COLUMN = "TdColumn.gif"; //$NON-NLS-1$

    public static final String TD_DATAPROVIDER = "TdDataProvider.gif"; //$NON-NLS-1$

    public static final String MDM_CONNECTION = "mdm_metadata.png"; //$NON-NLS-1$

    public static final String EDITCONNECTION = "sample.gif"; //$NON-NLS-1$

    public static final String CREATE_SQL_ACTION = "new_sqlEditor.png"; //$NON-NLS-1$

    public static final String LICENSE_WIZ = "license_wiz.png"; //$NON-NLS-1$

    /** {@link PatternNode#getImageName()} */
    public static final String PATTERN_REG = "pattern.png"; //$NON-NLS-1$

    public static final String REGISTER_WIZ = "register_wiz.png"; //$NON-NLS-1$

    public static final String CONNECTION = "connection.gif"; //$NON-NLS-1$

    public static final String METADATA = "metadata.png"; //$NON-NLS-1$

    public static final String OPTION = "option.png"; //$NON-NLS-1$

    public static final String LIBRARIES = "libraries.png"; //$NON-NLS-1$

    public static final String DATA_PROFILING = "server_chart.png"; //$NON-NLS-1$

    public static final String EXPORT_REPORT = "export_rep.png"; //$NON-NLS-1$

    public static final String LEVEL_WARNING = "level_warning.png"; //$NON-NLS-1$

    public static final String WARN_OVR = "warn_ovr.gif"; //$NON-NLS-1$

    public static final String CATALOG = "catalog.png"; //$NON-NLS-1$

    public static final String ASC_SORT = "asc.gif"; //$NON-NLS-1$

    public static final String DESC_SORT = "desc.gif"; //$NON-NLS-1$

    public static final String SCHEMA = "schema.gif"; //$NON-NLS-1$

    public static final String TABLE = "TdTable.gif"; //$NON-NLS-1$

    public static final String VIEW = "view.gif"; //$NON-NLS-1$

    public static final String DQ_RULE = "dqrule_red.png"; //$NON-NLS-1$

    public static final String ADD_DQ = "add_dqrule.png";//$NON-NLS-1$

    public static final String ADD_PATTERN = "add_pattern.png"; //$NON-NLS-1$

    public static final String SAVE = "save.gif"; //$NON-NLS-1$

    public static final String IMPORT = "import.png"; //$NON-NLS-1$

    public static final String EXPORT = "export.png"; //$NON-NLS-1$

    /** PK icon from SQL Explorer. */
    public static final String PK_DECORATE = "pk_decorate.gif"; //$NON-NLS-1$

    public static final String EDIT_COPY = "copy.gif"; //$NON-NLS-1$

    public static final String DUPLICATE = "duplicate.png"; //$NON-NLS-1$

    /** index icon from SQL Explorer. */
    public static final String INDEX_VIEW = "index.gif"; //$NON-NLS-1$

    /** Collapse all icon. */
    public static final String COLLAPSE_ALL = "collapseall.png"; //$NON-NLS-1$

    /** Expand all icon. */
    public static final String EXPAND_ALL = "expandall.png"; //$NON-NLS-1$

    /** Icon for primary key. */
    public static final String PK_COLUMN = "pkColumn.gif"; //$NON-NLS-1$

    /** Icon for refresh workspace. */
    public static final String REFRESH_SPACE = "refresh.gif"; //$NON-NLS-1$

    /** Icon for Talend Exchange folder. */
    public static final String EXCHANGE = "ecosystem_view.png"; //$NON-NLS-1$

    /**
     * Icon of indicator's definition. {@link IndicatorNode#getImageName()}
     * */
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

    public static final String JRXML_ICON = "jrxml.png"; //$NON-NLS-1$

    public static final String JRXML_WHITE_ICON = "jrxml_white.png"; //$NON-NLS-1$

    public static final String XML_ELEMENT_DOC = "xmlele.gif"; //$NON-NLS-1$

    public static final String ICON_PROCESS = "process_icon.png"; //$NON-NLS-1$

    public static final String ICON_PROCESS_WIZARD = "process_wiz.png"; //$NON-NLS-1$

    public static final String ICON_ERROR_INFO = "error.png"; //$NON-NLS-1$

    public static final String RECYCLEBIN_EMPTY = "recyclebinempty.png"; //$NON-NLS-1$

    public static final String RECYCLEBIN_OVERLAY = "recycle_bino_verlay.gif"; //$NON-NLS-1$

    public static final String RECYCLEBIN_FULL = "recyclebinfull.png"; //$NON-NLS-1$

    public static final String ICON_ERROR_VAR = "error_ovr.gif"; //$NON-NLS-1$

    public static final String ICON_ADD_VAR = "add_ovr.gif"; //$NON-NLS-1$

    public static final String FILE_DELIMITED = "filedelimited.png"; //$NON-NLS-1$

    public static final String SOURCE_FILE = "editor.png"; //$NON-NLS-1$

    public static final String JAR_FILE = "jar_obj.png"; //$NON-NLS-1$

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

    public static final String IMPORT_MATCH_RULE_ICON = "match_rule_import.png"; //$NON-NLS-1$

    public static final String EXPORT_MATCH_RULE_ICON = "match_rule_export.png"; //$NON-NLS-1$

    public static final String MATCH_RULE_ICON = "match_rule.png"; //$NON-NLS-1$

    public static final String MATCH_RULE_WHITE_ICON = "match_rule_white.png"; //$NON-NLS-1$

    public static final String APPLICATION_HOME = "application_home.png"; //$NON-NLS-1$

    public static final String EXPORT_WIZARD = "export_wiz.gif"; //$NON-NLS-1$

    public static final String HADOOP_CLUSTER = "hadoop-logo.png"; //$NON-NLS-1$

    public static final String HIVE_LINK = "db_link.png"; //$NON-NLS-1$

    public static final String HDFS = "HDFS_icon16.png"; //$NON-NLS-1$

    public static final String REFERENCED_PROJECT = "referenced.png"; //$NON-NLS-1$

    public static final String CHECK_MARK_PNG = "check.png";//$NON-NLS-1$

    public static final String RED_WARNING_PNG = "red_warning.png";//$NON-NLS-1$

    public static final String WARNING_PNG = "warning.png";//$NON-NLS-1$

    public static final String CONTEXT = "contexts.png";//$NON-NLS-1$

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
        CorePlugin amcPlugin = CorePlugin.getDefault();
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
        URL installURL = CorePlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
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
    public static ImageDescriptor createInvalidIconDes(String originalImgName) {
        return getOverlayIconDes(originalImgName, WARN_OVR);
    }

    /**
     * DOC bZhou Comment method "createInvalidIcon".
     * 
     * @param originalImgName
     * @return
     */
    public static Image createInvalidIcon(String originalImgName) {
        return getOverlayIcon(originalImgName, WARN_OVR);
    }

    /**
     * DOC bZhou Comment method "createInvalidIcon".
     * 
     * @param originalImg
     * @return
     * @deprecated use createInvalidIcon(String originalImgName) and avoid to create image ervery time.
     */
    @Deprecated
    public static ImageDescriptor createInvalidIcon(ImageDescriptor originalImg) {
        ImageDescriptor warnImg = getImageDescriptor(WARN_OVR);
        return originalImg != null ? createIcon(originalImg, warnImg) : null;
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImgName
     * @return
     * @deprecated use createLockedByOwnIcon(String originalImgName) and avoid to create image ervery time.
     */
    @Deprecated
    public static ImageDescriptor createLockedIcon(String originalImgName) {
        return getOverlayIconDes(originalImgName, ICON_LOCK);
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImg
     * @return
     * @deprecated use createLockedByOwnIcon(String originalImgName) and avoid to create image ervery time.
     */
    @Deprecated
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
     * @deprecated use getOverlayIcon(String originalImgName, String overImgName) and avoid to create image ervery time.
     */
    @Deprecated
    public static ImageDescriptor createIcon(ImageDescriptor originalImg, ImageDescriptor decorateImg) {
        return createIcon(originalImg.createImage(), decorateImg);
    }

    /**
     * 
     * make the original image name and overlay image name as a key, find the image from ImageLib.imageRegistry by this
     * key. if not found,create a new Overlay image and put it's ImageDescriptor into imageRegistry.
     * 
     * @param originalName
     * @param overImgName
     * @return the ImageDescriptor
     */
    public static ImageDescriptor getOverlayIconDes(String originalName, String overImgName) {
        if (getOverlayIcon(originalName, overImgName) != null) {
            String orininal_over_name = originalName + PluginConstant.UNDER_LINE + overImgName;
            return imageRegistry.getDescriptor(orininal_over_name);
        } else {
            return null;
        }

    }

    /**
     * 
     * make the original image name and overlay image name as a key, find the image from ImageLib.imageRegistry by this
     * key. if not found,create a new Overlay image and put it's ImageDescriptor into imageRegistry.
     * 
     * @param originalName
     * @param overImgName
     * @return the image
     */
    public static Image getOverlayIcon(String originalName, String overImgName) {
        String orininal_over_name = originalName + PluginConstant.UNDER_LINE + overImgName;
        if (imageRegistry == null) {
            initialize();
        }
        Image originalOverImg = imageRegistry.get(orininal_over_name);
        ImageDescriptor descriptor = imageRegistry.getDescriptor(orininal_over_name);
        if (originalOverImg == null || descriptor == null) {
            ImageDescriptor orignalImgDes = getImageDescriptor(originalName);
            ImageDescriptor overImgDes = getImageDescriptor(overImgName);
            if (orignalImgDes != null && overImgDes != null) {
                ImageDescriptor originalOverImgDes = createIcon(orignalImgDes.createImage(), overImgDes);
                imageRegistry.put(orininal_over_name, originalOverImgDes);
                originalOverImg = imageRegistry.get(orininal_over_name);
            }
        }
        if (originalOverImg != null && originalOverImg.isDisposed() && descriptor != null) {
            descriptor = imageRegistry.getDescriptor(orininal_over_name);
            originalOverImg = descriptor.createImage();
        }
        return originalOverImg;

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
     * DOC bzhou ImageLib class global comment. Detailled comment
     */
    public enum CWMImageEnum {
        Connection(DefaultMessagesImpl.getString("ImageLib.connection"), getImage(CONNECTION)), //$NON-NLS-1$
        Catalog(DefaultMessagesImpl.getString("ImageLib.catalog"), getImage(CATALOG)), //$NON-NLS-1$
        Schema(DefaultMessagesImpl.getString("ImageLib.schema"), getImage(SCHEMA)), //$NON-NLS-1$
        Table(DefaultMessagesImpl.getString("ImageLib.table"), getImage(TABLE)), //$NON-NLS-1$
        View(DefaultMessagesImpl.getString("ImageLib.view"), getImage(VIEW)), //$NON-NLS-1$
        Column(DefaultMessagesImpl.getString("ImageLib.column"), getImage(TD_COLUMN)); //$NON-NLS-1$

        private String label;

        private Image img;

        private CWMImageEnum(String label, Image img) {
            this.label = label;
            this.img = img;
        }

        public Image getImg() {
            return img;
        }

        public String getLabel() {
            return label;
        }

        public static Image getImageByLabel(String label) {
            for (CWMImageEnum cwmImage : values()) {
                if (cwmImage.getLabel().equalsIgnoreCase(label)) {
                    return cwmImage.getImg();
                }
            }

            return null;
        }
    }

    /**
     * DOC qiongli Comment method "createLockedIcon".
     * 
     * @param originalImgName
     * @return
     */
    public static Image createErrorIcon(String originalImgName) {
        return getOverlayIcon(originalImgName, ICON_ERROR_VAR);
    }

    /**
     * DOC bZhou Comment method "createLockedIcon".
     * 
     * @param originalImg
     * @return
     * @deprecated use createErrorIcon(String originalImgName) and avoid to create image ervery time.
     */
    @Deprecated
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
        return getOverlayIconDes(originalImgName, ICON_ADD_VAR);
    }

    /**
     * DOC qiongli Comment method "createAddedIcon".
     * 
     * @param originalImg
     * @return
     * @deprecated use createAddedIcon(String originalImgName) and avoid to create image ervery time.
     */
    @Deprecated
    public static ImageDescriptor createAddedIcon(ImageDescriptor originalImg) {
        ImageDescriptor addImg = getImageDescriptor(ICON_ADD_VAR);
        return originalImg != null ? new DecorationOverlayIcon(originalImg.createImage(), addImg, IDecoration.TOP_RIGHT) : null;
    }

    public static Image createLockedByOtherIcon(String originalImgName) {
        return getOverlayIcon(originalImgName, ICON_LOCK_BYOTHER);
    }

    /**
     * 
     * @deprecated use createLockedByOtherIcon(String originalImgName) and avoid to create image ervery
     */
    @Deprecated
    public static ImageDescriptor createLockedByOtherIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK_BYOTHER);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    /**
     * 
     * @deprecated use createLockedByOtherIcon(String originalImgName) and avoid to create image ervery
     */
    @Deprecated
    public static ImageDescriptor createLockedByOtherIcon(Image originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK_BYOTHER);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    public static Image createLockedByOwnIcon(String originalImgName) {
        return getOverlayIcon(originalImgName, ICON_LOCK);
    }

    /**
     * 
     * @deprecated use createLockedByOwnIcon(String originalImgName) and avoid to create image ervery
     */
    @Deprecated
    public static ImageDescriptor createLockedByOwnIcon(ImageDescriptor originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    /**
     * 
     * @deprecated use createLockedByOwnIcon(String originalImgName) and avoid to create image ervery
     */
    @Deprecated
    public static ImageDescriptor createLockedByOwnIcon(Image originalImg) {
        ImageDescriptor lockImg = getImageDescriptor(ICON_LOCK);
        return originalImg != null ? createIcon(originalImg, lockImg) : null;
    }

    public static ImageDescriptor getImageDescriptorByRepositoryNode(IRepositoryNode node) {
        return getImageDescriptor(getImageNameByRepositoryNode(node));
    }

    public static String getImageNameByRepositoryNode(IRepositoryNode node) {
        String imageName = null;
        IRepositoryViewObject viewObject = node.getObject();
        ENodeType type = node.getType();
        if (node instanceof ReportAnalysisRepNode) {
            imageName = ImageLib.ANALYSIS_OBJECT;
        } else if (node instanceof RecycleBinRepNode) {
            imageName = ImageLib.RECYCLEBIN_EMPTY;
        } else if (type.equals(ENodeType.SYSTEM_FOLDER)) {
            String label = viewObject.getLabel();
            if (label.equals(EResourceConstant.DATA_PROFILING.getName())) {
                imageName = ImageLib.DATA_PROFILING;
            } else if (label.equals(EResourceConstant.METADATA.getName())) {
                imageName = ImageLib.METADATA;
            } else if (node instanceof DBConnectionFolderRepNode) {
                imageName = ImageLib.CONNECTION;
            } else if (label.equals(EResourceConstant.FILEDELIMITED.getName())) {
                imageName = ImageLib.FILE_DELIMITED;
            } else if (label.equals(EResourceConstant.HADOOP_CLUSTER.getName())) {
                imageName = ImageLib.HADOOP_CLUSTER;
            } else if (label.equals(EResourceConstant.LIBRARIES.getName())) {
                imageName = ImageLib.LIBRARIES;
            } else if (label.equals(EResourceConstant.EXCHANGE.getName())) {
                imageName = ImageLib.EXCHANGE;
            } else {
                imageName = ImageLib.FOLDERNODE_IMAGE;
            }
        } else if (type.equals(ENodeType.SIMPLE_FOLDER)) {
            imageName = ImageLib.FOLDERNODE_IMAGE;
        } else if (type.equals(ENodeType.REPOSITORY_ELEMENT)) {
            if (node instanceof DBConnectionRepNode) {
                imageName = ImageLib.TD_DATAPROVIDER;
            } else if (node instanceof DFConnectionRepNode) {
                imageName = ImageLib.FILE_DELIMITED;
            } else if (node instanceof AnalysisRepNode) {
                imageName = ImageLib.ANALYSIS_OBJECT;
            } else if (node instanceof ReportRepNode) {
                imageName = ImageLib.REPORT_OBJECT;
            } else if (node instanceof SysIndicatorDefinitionRepNode) {
                imageName = ImageLib.IND_DEFINITION;
            } else if (node instanceof PatternRepNode) {
                imageName = ImageLib.PATTERN_REG;
            } else if (node instanceof RuleRepNode) {
                imageName = ImageLib.DQ_RULE;
            } else if (node instanceof SourceFileRepNode) {
                imageName = ImageLib.SOURCE_FILE;
            } else if (node instanceof ExchangeCategoryRepNode || node instanceof ExchangeComponentRepNode) {
                imageName = ImageLib.EXCHANGE;
            } else if (node instanceof JrxmlTempleteRepNode) {
                imageName = ImageLib.JRXML_ICON;
            }
        } else if (type.equals(ENodeType.TDQ_REPOSITORY_ELEMENT)) {
            if (node instanceof DBCatalogRepNode) {
                imageName = ImageLib.CATALOG;
            } else if (node instanceof DBSchemaRepNode) {
                imageName = ImageLib.SCHEMA;
            } else if (node instanceof DBTableFolderRepNode) {
                imageName = ImageLib.FOLDERNODE_IMAGE;
            } else if (node instanceof DBViewFolderRepNode) {
                imageName = ImageLib.FOLDERNODE_IMAGE;
            } else if (node instanceof DBTableRepNode || node instanceof DFTableRepNode) {
                imageName = ImageLib.TABLE;
            } else if (node instanceof DBViewRepNode) {
                imageName = ImageLib.VIEW;
            } else if (node instanceof DBColumnRepNode) {
                if (((DBColumnRepNode) node).isKey()) {
                    imageName = ImageLib.PK_COLUMN;
                } else {
                    imageName = ImageLib.TD_COLUMN;
                }
            } else if (node instanceof DFColumnRepNode) {
                imageName = ImageLib.TD_COLUMN;
            } else if (node instanceof DBColumnFolderRepNode || node instanceof DFColumnFolderRepNode) {
                imageName = ImageLib.FOLDERNODE_IMAGE;
            }
        }
        return imageName;
    }
}
