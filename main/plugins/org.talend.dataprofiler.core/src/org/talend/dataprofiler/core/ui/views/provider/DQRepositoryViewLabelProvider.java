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
package org.talend.dataprofiler.core.ui.views.provider;

import java.sql.Driver;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.IReferencedProjectService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.exchange.ExchangeCategoryRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeComponentRepNode;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ContextRepNode;
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
import org.talend.dq.nodes.hadoopcluster.HDFSOfHCConnectionNode;
import org.talend.dq.nodes.hadoopcluster.HadoopClusterConnectionRepNode;
import org.talend.dq.nodes.hadoopcluster.HiveOfHCConnectionNode;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author rli
 */
public class DQRepositoryViewLabelProvider extends AdapterFactoryLabelProvider implements IFontProvider, IColorProvider {

    private static final Color STABLE_SECONDARY_ENTRY_COLOR = new Color(null, 100, 100, 100);

    private static final Color STABLE_PRIMARY_ENTRY_COLOR = new Color(null, 0, 0, 0);

    protected static final Color INACTIVE_ENTRY_COLOR = new Color(null, 200, 200, 200);

    private static final Color LOCKED_ENTRY = new Color(null, 200, 0, 0);

    private static final Color MERGED_REFERENCED_ITEMS_COLOR = new Color(null, 120, 120, 120);

    private Logger log = Logger.getLogger(DQRepositoryViewLabelProvider.class);

    public DQRepositoryViewLabelProvider() {
        super(MNComposedAdapterFactory.getAdapterFactory());
    }

    @Override
    public Image getImage(Object element) {
        Image image = super.getImage(element);

        if (element instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) element;
            if (node instanceof ReportAnalysisRepNode) {
                image = ImageLib.getImage(ImageLib.ANALYSIS_OBJECT);
            } else if (node instanceof ExchangeCategoryRepNode || node instanceof ExchangeComponentRepNode) {
                image = ImageLib.getImage(ImageLib.EXCHANGE);
            } else if (node instanceof RecycleBinRepNode) {
                image = ImageLib.getImage(ImageLib.RECYCLEBIN_EMPTY);
            } else {
                IRepositoryViewObject viewObject = node.getObject();
                ENodeType type = node.getType();
                if (type.equals(ENodeType.SYSTEM_FOLDER)) {
                    if (EResourceConstant.REFERENCED_PROJECT.getName().equals(node.getProperties(EProperties.LABEL))) {
                        image = ImageLib.getImage(ImageLib.REFERENCED_PROJECT);
                    } else {
                        String label = viewObject.getLabel();
                        if (label.equals(EResourceConstant.DATA_PROFILING.getName())) {
                            image = ImageLib.getImage(ImageLib.DATA_PROFILING);
                        } else if (label.equals(EResourceConstant.METADATA.getName())) {
                            image = ImageLib.getImage(ImageLib.METADATA);
                        } else if (node instanceof DBConnectionFolderRepNode) {
                            image = ImageLib.getImage(ImageLib.CONNECTION);
                        } else if (label.equals(EResourceConstant.FILEDELIMITED.getName())) {
                            image = ImageLib.getImage(ImageLib.FILE_DELIMITED);
                        } else if (label.equals(EResourceConstant.LIBRARIES.getName())) {
                            image = ImageLib.getImage(ImageLib.LIBRARIES);
                        } else if (label.equals(EResourceConstant.EXCHANGE.getName())) {
                            image = ImageLib.getImage(ImageLib.EXCHANGE);
                        } else if (label.equals(EResourceConstant.HADOOP_CLUSTER.getName())) {
                            image = ImageLib.getImage(ImageLib.HADOOP_CLUSTER);
                        } else if (label.equals(EResourceConstant.CONTEXT.getName())) {
                            image = ImageLib.getImage(ImageLib.CONTEXT);
                        } else {
                            image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
                        }
                    }
                } else if (type.equals(ENodeType.SIMPLE_FOLDER)) {
                    image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
                } else if (type.equals(ENodeType.REFERENCED_PROJECT)) {
                    image = ImageLib.getImage(ImageLib.REFERENCED_PROJECT);
                } else if (type.equals(ENodeType.REPOSITORY_ELEMENT)) {
                    // TDQ-7560 when the image is a overlay image,use originalImageName to get the corresponding one.
                    String originalImageName = null;
                    if (node instanceof DBConnectionRepNode) {
                        originalImageName = ImageLib.TD_DATAPROVIDER;
                        if (!RepositoryNodeHelper.isSupportedConnection(node) || isNeedAddDriverConnection(node)) {
                            image = ImageLib.createErrorIcon(originalImageName);
                        } else if (isInvalidJDBCConnection(node)) {
                            image = ImageLib.createInvalidIcon(originalImageName);
                        } else {
                            image = ImageLib.getImage(originalImageName);
                        }
                    } else if (node instanceof DFConnectionRepNode) {
                        originalImageName = ImageLib.FILE_DELIMITED;
                    } else if (node instanceof AnalysisRepNode) {
                        originalImageName = ImageLib.ANALYSIS_OBJECT;
                        image = addWarnIconIfNeeded(node, originalImageName);
                    } else if (node instanceof ReportRepNode) {
                        originalImageName = ImageLib.REPORT_OBJECT;
                        image = addWarnIconIfNeeded(node, originalImageName);
                    } else if (node instanceof SysIndicatorDefinitionRepNode) {
                        originalImageName = ImageLib.IND_DEFINITION;
                    } else if (node instanceof PatternRepNode) {
                        originalImageName = ImageLib.PATTERN_REG;
                    } else if (node instanceof RuleRepNode) {
                        if (((RuleRepNode) node).getRule() instanceof MatchRuleDefinition) {
                            originalImageName = ImageLib.MATCH_RULE_ICON;
                        } else {
                            originalImageName = ImageLib.DQ_RULE;
                        }
                    } else if (node instanceof SourceFileRepNode) {
                        originalImageName = ImageLib.SOURCE_FILE;
                    } else if (node instanceof HadoopClusterConnectionRepNode) {
                        originalImageName = ImageLib.HADOOP_CLUSTER;
                    } else if (node instanceof HDFSOfHCConnectionNode) {
                        originalImageName = ImageLib.HDFS;
                    } else if (node instanceof HiveOfHCConnectionNode) {
                        originalImageName = ImageLib.HIVE_LINK;
                    } else if (node instanceof ExchangeCategoryRepNode || node instanceof ExchangeComponentRepNode) {
                        originalImageName = ImageLib.EXCHANGE;
                    } else if (node instanceof ContextRepNode) {
                        originalImageName = ImageLib.CONTEXT;
                    } else if (node instanceof RepositoryNode) {
                        // MOD qiongli 2011-1-18 get image for nodes in recycle bin
                        Image imageNode = getImageByContentType((RepositoryNode) node);
                        if (image != null) {
                            image = imageNode;
                        }
                    }
                    if (originalImageName != null
                            && !(node instanceof DBConnectionRepNode || node instanceof AnalysisRepNode || node instanceof ReportRepNode)) {
                        image = ImageLib.getImage(originalImageName);
                    }
                    // MOD klliu 2010-04-11 20468: Unfolder "exchange",get many NPE
                    // exchange folder did not contain viewObject.
                    if (viewObject != null) {
                        // MOD yyi 2011-04-07 19696: "Lock element"
                        ERepositoryStatus status = ProxyRepositoryFactory.getInstance().getStatus(viewObject);

                        Context ctx = CoreRuntimePlugin.getInstance().getContext();
                        RepositoryContext rc = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);

                        // TUP-1918:for offline mode,its item is locked by default but need show the default image. This
                        // will be enhanced later by TDI-29265.
                        if (rc.isEditableAsReadOnly()) {
                            if (status == ERepositoryStatus.LOCK_BY_USER) {
                                status = ERepositoryStatus.DEFAULT;
                            }
                        }

                        if (ERepositoryStatus.DEFAULT != status && originalImageName != null) {
                            if (ERepositoryStatus.LOCK_BY_USER == status) {
                                image = ImageLib.createLockedByOwnIcon(originalImageName);
                            } else if (ERepositoryStatus.LOCK_BY_OTHER == status) {
                                image = ImageLib.createLockedByOtherIcon(originalImageName);
                            }
                        }
                    }
                } else if (type.equals(ENodeType.TDQ_REPOSITORY_ELEMENT)) {
                    if (node instanceof DBCatalogRepNode) {
                        image = ImageLib.getImage(ImageLib.CATALOG);
                    } else if (node instanceof DBSchemaRepNode) {
                        image = ImageLib.getImage(ImageLib.SCHEMA);
                    } else if (node instanceof DBTableFolderRepNode) {
                        image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
                    } else if (node instanceof DBViewFolderRepNode) {
                        image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
                    } else if (node instanceof DBTableRepNode || node instanceof DFTableRepNode) {
                        image = ImageLib.getImage(ImageLib.TABLE);
                    } else if (node instanceof DBViewRepNode) {
                        image = ImageLib.getImage(ImageLib.VIEW);
                    } else if (node instanceof DBColumnRepNode) {
                        if (((DBColumnRepNode) node).isKey()) {
                            image = ImageLib.getImage(ImageLib.PK_COLUMN);
                        } else {
                            image = ImageLib.getImage(ImageLib.TD_COLUMN);
                        }
                    } else if (node instanceof DFColumnRepNode) {
                        image = ImageLib.getImage(ImageLib.TD_COLUMN);
                    } else if (node instanceof DBColumnFolderRepNode || node instanceof DFColumnFolderRepNode) {
                        image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
                    } else if (node instanceof JrxmlTempleteRepNode) {
                        image = ImageLib.getImage(ImageLib.JRXML_ICON);
                    }
                }
            }
        }

        return image;
    }

    /**
     * if it is needed,add a over warning icon..eg., it is empty analysis or report; imported a MDM analysis or report.
     * 
     * @param image
     * @param node
     * @param originalImageName
     * @return
     */
    private Image addWarnIconIfNeeded(IRepositoryNode node, String originalImageName) {
        ModelElement modEle = RepositoryNodeHelper.getResourceModelElement(node);
        ERepositoryObjectType objectType = node.getObjectType();

        if (modEle != null) {
            if (ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT == objectType) {
                Analysis analysis = (Analysis) modEle;
                AnalysisContext context = analysis.getContext();
                if (context == null) {
                    return ImageLib.createInvalidIcon(originalImageName);
                }
                EList<ModelElement> analysedElements = context.getAnalysedElements();
                DataManager connection = context.getConnection();
                if (analysedElements.isEmpty() || connection instanceof MDMConnection) {
                    return ImageLib.createInvalidIcon(originalImageName);
                }
            } else if (ERepositoryObjectType.TDQ_REPORT_ELEMENT == objectType) {
                TdReport report = (TdReport) modEle;
                EList<AnalysisMap> analysisMap = report.getAnalysisMap();
                if (analysisMap.isEmpty()) {
                    return ImageLib.createInvalidIcon(originalImageName);
                }
                for (AnalysisMap anaMap : report.getAnalysisMap()) {
                    Analysis analysis = anaMap.getAnalysis();
                    if (analysis == null || analysis.getContext() == null) {
                        continue;
                    }
                    DataManager connection = analysis.getContext().getConnection();
                    if (connection instanceof MDMConnection) {
                        return ImageLib.createInvalidIcon(originalImageName);
                    }
                }
            }
        }
        return ImageLib.getImage(originalImageName);
    }

    @Override
    public String getText(Object element) {
        if (element != null && element instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) element;
            return RepositoryNodeHelper.getDisplayLabel(node);
        }
        String text = super.getText(element);
        return PluginConstant.EMPTY_STRING.equals(text) ? DefaultMessagesImpl.getString("DQRepositoryViewLabelProvider.noName") : text; //$NON-NLS-1$
    }

    /**
     * 
     * DOC qiongli Comment method "getImageByContentType".
     * 
     * @param repositoryNode
     * @return
     */
    private Image getImageByContentType(RepositoryNode repositoryNode) {
        ERepositoryObjectType type = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
        if (type == null) {
            return null;
        }
        if (type == ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT) {
            return ImageLib.getImage(ImageLib.ANALYSIS_OBJECT);
        } else if (type == ERepositoryObjectType.METADATA_CONNECTIONS) {
            return ImageLib.getImage(ImageLib.TD_DATAPROVIDER);
        } else if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            return ImageLib.getImage(ImageLib.FILE_DELIMITED);
        } else if (type == ERepositoryObjectType.TDQ_REPORT_ELEMENT) {
            return ImageLib.getImage(ImageLib.REPORT_OBJECT);
        } else if (type == ERepositoryObjectType.TDQ_INDICATOR_ELEMENT) {
            return ImageLib.getImage(ImageLib.IND_DEFINITION);
        } else if (type == ERepositoryObjectType.TDQ_PATTERN_ELEMENT) {
            return ImageLib.getImage(ImageLib.PATTERN_REG);
        } else if (type == ERepositoryObjectType.TDQ_RULES) {
            return ImageLib.getImage(ImageLib.DQ_RULE);
        } else if (type == ERepositoryObjectType.TDQ_RULES_PARSER) {
            return ImageLib.getImage(ImageLib.DQ_RULE);
        } else if (type == ERepositoryObjectType.TDQ_RULES_SQL) {
            return ImageLib.getImage(ImageLib.DQ_RULE);
        } else if (type == ERepositoryObjectType.TDQ_RULES_MATCHER) {
            return ImageLib.getImage(ImageLib.MATCH_RULE_ICON);
        } else if (type == ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT) {
            return ImageLib.getImage(ImageLib.SOURCE_FILE);
        } else if (type == ERepositoryObjectType.TDQ_EXCHANGE) {
            return ImageLib.getImage(ImageLib.EXCHANGE);
        } else if (type == ERepositoryObjectType.FOLDER) {
            return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
        } else if (type == ERepositoryObjectType.TDQ_JRAXML_ELEMENT) {
            return ImageLib.getImage(ImageLib.JRXML_ICON);
        } else if (type == HadoopClusterUtils.getDefault().getHadoopClusterType()) {
            return ImageLib.getImage(ImageLib.HADOOP_CLUSTER);
        } else if (type == HadoopClusterUtils.getDefault().getHDFSType()) {
            return ImageLib.getImage(ImageLib.HDFS);
        } else if (type == ERepositoryObjectType.CONTEXT) {
            return ImageLib.getImage(ImageLib.CONTEXT);
        }
        return null;

    }

    private boolean isNeedAddDriverConnection(IRepositoryNode repNode) {
        if (repNode.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
            ConnectionItem connectionItem = (ConnectionItem) repNode.getObject().getProperty().getItem();
            if (connectionItem.getConnection() instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) (connectionItem.getConnection());
                String dbType = dbConn.getDatabaseType();
                String driverClassName = JavaSqlFactory.getDriverClass(dbConn);
                if (dbType == null || driverClassName == null || PluginConstant.EMPTY_STRING.equals(driverClassName)) {
                    return true;
                }

                // firstly,find driver from cache.if not find,load jar by lib folder then find driver from
                // SQLExplorer driver.
                if (!(StringUtils.equalsIgnoreCase(EDatabaseTypeName.IMPALA.getXmlName(), dbType) || StringUtils
                        .equalsIgnoreCase(EDatabaseTypeName.HIVE.getXmlName(), dbType))) {
                    Driver driver = MetadataConnectionUtils.getDriverCache().get(driverClassName);
                    if (driver != null) {
                        return false;
                    }
                    // TDQ-11449 "SqlExplorerUtils.getDefault().getSqlexplorerService()" in a Thread will pop download
                    // "sqlExplorer" many times.no need call it at here, if don't have "sqlExplorer", return false.
                    return SqlExplorerUtils.getDefault().needAddDriverConnection(dbConn);
                }
            }

        }
        return false;
    }

    /**
     * ADD qiongli TDQ-5801 if it is a invalid jdbc connection.
     * 
     * @param repNode
     * @return
     */
    private boolean isInvalidJDBCConnection(IRepositoryNode repNode) {
        Property property = repNode.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            DatabaseConnectionItem connectionItem = (DatabaseConnectionItem) property.getItem();
            if (connectionItem != null) {
                DatabaseConnection connection = (DatabaseConnection) connectionItem.getConnection();
                String databaseType = connection.getDatabaseType();
                if (databaseType.equalsIgnoreCase(SupportDBUrlType.GENERICJDBCDEFAULTURL.getDBKey())
                        && ((connection.getDriverJarPath() == null) || (connection.getDriverJarPath()).trim().equals(
                                PluginConstant.EMPTY_STRING))) {
                    return true;
                }
            }
        }

        return false;
    }

    /*
     * yyi 2011-04-14 20362:connection modified
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getFont(java.lang.Object)
     */
    @Override
    public Font getFont(Object element) {
        boolean changeURL = false;
        if (element instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) element;
            if (node instanceof RecycleBinRepNode) {
                return super.getFont(element);
            }
            if (node.getObject() != null) {
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                case SYSTEM_FOLDER:
                    return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
                case REPOSITORY_ELEMENT:
                    if (node instanceof DBConnectionRepNode) {
                        ConnectionItem connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
                        if (connectionItem.getConnection() instanceof DatabaseConnection) {
                            changeURL = ConnectionHelper.isUrlChanged(connectionItem.getConnection());
                            if (changeURL) {
                                return getItalicFont();
                            }
                        }
                    }
                default:
                    return JFaceResources.getFontRegistry().defaultFont();
                }
            }
        }
        return super.getFont(element);
    }

    private Font getItalicFont() {
        Font italicFont = Display.getDefault().getSystemFont();
        FontData[] exfds = italicFont.getFontData();
        if (exfds.length > 0) {
            FontData fd = exfds[0];
            fd.setStyle(SWT.ITALIC);
            italicFont = new Font(italicFont.getDevice(), fd);
        }
        return italicFont;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getForeground(java.lang.Object)
     */
    @Override
    public Color getForeground(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        if (node instanceof RecycleBinRepNode) {
            return super.getForeground(element);
        }
        if (node.getObject() != null) {
            switch (node.getType()) {
            case REFERENCED_PROJECT:
                return STABLE_PRIMARY_ENTRY_COLOR;
            case STABLE_SYSTEM_FOLDER:
            case SYSTEM_FOLDER:
                if (node.getContentType() == ERepositoryObjectType.TDQ_DATA_PROFILING
                        || node.getContentType() == ERepositoryObjectType.METADATA
                        || node.getContentType() == ERepositoryObjectType.TDQ_SYSTEM_INDICATORS
                        || node.getContentType() == ERepositoryObjectType.TDQ_PATTERN_REGEX) {
                    return STABLE_PRIMARY_ENTRY_COLOR;
                }
                return STABLE_SECONDARY_ENTRY_COLOR;
            default:
                ERepositoryStatus repositoryStatus = node.getObject().getRepositoryStatus();
                if (repositoryStatus == ERepositoryStatus.LOCK_BY_OTHER) {
                    return LOCKED_ENTRY;
                }

                if (org.talend.core.PluginChecker.isRefProjectLoaded()) {
                    IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault()
                            .getService(IReferencedProjectService.class);
                    if (service != null && service.isMergeRefProject()) {
                        IRepositoryViewObject object = node.getObject();
                        if (object != null) {
                            org.talend.core.model.properties.Project mainProject = ProjectManager.getInstance()
                                    .getCurrentProject().getEmfProject();
                            String projectLabel = object.getProjectLabel();
                            if (!mainProject.getLabel().equals(projectLabel)) {
                                return MERGED_REFERENCED_ITEMS_COLOR;
                            }
                        }
                    }
                }
            }
        }
        return super.getForeground(element);
    }

}
