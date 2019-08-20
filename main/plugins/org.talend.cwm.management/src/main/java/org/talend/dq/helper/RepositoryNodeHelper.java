// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.MetadataXmlElementType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionImpl;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.model.repository.helper.RepositoryObjectTypeHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.nodes.AnalysisFolderRepNode;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.ContextFolderRepNode;
import org.talend.dq.nodes.ContextRepNode;
import org.talend.dq.nodes.ContextSubFolderRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBConnectionSubFolderRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFConnectionFolderRepNode;
import org.talend.dq.nodes.DFConnectionRepNode;
import org.talend.dq.nodes.DFConnectionSubFolderRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.JrxmlTempFolderRepNode;
import org.talend.dq.nodes.JrxmlTempSubFolderNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.dq.nodes.PatternRegexFolderRepNode;
import org.talend.dq.nodes.PatternRegexSubFolderRepNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.PatternSqlFolderRepNode;
import org.talend.dq.nodes.PatternSqlSubFolderRepNode;
import org.talend.dq.nodes.RecycleBinRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportFolderRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.RulesMatcherFolderRepNode;
import org.talend.dq.nodes.RulesMatcherSubFolderRepNode;
import org.talend.dq.nodes.RulesParserFolderRepNode;
import org.talend.dq.nodes.RulesParserSubFolderRepNode;
import org.talend.dq.nodes.RulesSQLFolderRepNode;
import org.talend.dq.nodes.RulesSQLSubFolderRepNode;
import org.talend.dq.nodes.SourceFileFolderRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.nodes.SysIndicatorFolderRepNode;
import org.talend.dq.nodes.UserDefIndicatorFolderRepNode;
import org.talend.dq.nodes.UserDefIndicatorSubFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HiveOfHCConnectionNode;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.record.RecordFile;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * Helper class for RepositoryNode.
 */
public final class RepositoryNodeHelper {

    public static final String FILE_DELIMITED_CONNECTION = "FileDelimited Connection"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(RepositoryNodeHelper.class);

    public static final String DQRESPOSITORYVIEW = "org.talend.dataprofiler.core.ui.views.DQRespositoryView"; //$NON-NLS-1$

    public static final String DQRESPOSITORY_DETAIL_VIEW =
            "org.talend.dataprofiler.core.ui.views.RespositoryDetailView"; //$NON-NLS-1$

    public static final String DI_REPOSITORY_NAME = "Repository"; //$NON-NLS-1$

    private static RecycleBinRepNode recycleBinRepNode;

    private static List<IRepositoryNode> allFilteredNodeList = new ArrayList<IRepositoryNode>();

    private static IRepositoryNode filteredNode = null;// Record the current filter position

    public static final String UNSUPPORTED = "(Unsupported)"; //$NON-NLS-1$

    public static final String PREFIX_TDQ = "TDQ_"; //$NON-NLS-1$

    /**
     * the follows' uuids<br>
     * <br>
     * BE Code postal.pattern <br>
     * Companies_House.pattern <br>
     * DE Postleitzahl (postal code).pattern <br>
     * FR Code postal.pattern <br>
     * Postal_code_or_Pin_code_of_India.pattern <br>
     * Swiss_Zip_Code_validation.pattern <br>
     * US_State_Codes.pattern <br>
     * US_Zipcode_Validation.pattern <br>
     * Valid_UK_Post_Codes_Upper_and_Lower_Case.pattern
     */
    private static final String[] PATTERN_ADDRESS_UUIDS = {
            "_NwUnsD4LEd20H7qFpzB9dg", "_KErdkIyeEd6Dle_8xC0lwg", "_rK7EgD4LEd20H7qFpzB9dg", "_EJ_moD4LEd20H7qFpzB9dg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_Ego7UIygEd6Dle_8xC0lwg", "__GBswIyfEd6Dle_8xC0lwg", "_pWgP8LkgEeSVYtNTxh_-xg", "_n2tqUIybEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_fi29wIybEd6Dle_8xC0lwg" }; //$NON-NLS-1$

    /**
     * the follows' uuids<br>
     * <br>
     * Properties_Keys.pattern
     */
    private static final String[] PATTERN_CODE_UUIDS = { "_DVfAwAMtEd6stMai_3cDIQ" }; //$NON-NLS-1$

    /**
     * the follows' uuids<br>
     * <br>
     * Hex_Color.pattern
     */
    private static final String[] PATTERN_COLOR_UUIDS = { "_ly9QgcBQEd2Zap05uF3mKA" }; //$NON-NLS-1$

    /**
     * the follows' uuids<br>
     * <br>
     * Austria_VAT_Number.pattern <br>
     * Bulgaria_Vat_Number.pattern <br>
     * FR_VAT_Number.pattern <br>
     * Gender.pattern
     */
    private static final String[] PATTERN_CUSTOMER_UUIDS = { "_sHQnkIydEd6Dle_8xC0lwg", "_b95-YIydEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$
            "_ga6fofbIEd2c5L1gaJyUtQ", "_TXHDQceCEd2zV_ZhgRMOzA" }; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * the follows' uuids<br>
     * <br>
     * 24 Hour Time.pattern <br>
     * Current_Century_Date.pattern <br>
     * Datetime_mm_dd_yyyy_hh_mm.pattern <br>
     * Datetime_mm_dd_yyyy_hh_mm_ss.pattern <br>
     * Date_ddMMM.pattern <br>
     * Date_DD_MMM_YYYY.pattern <br>
     * Date_MM_DD_YY.pattern <br>
     * Date_MM_DD_YYYY.pattern <br>
     * Extended Hour Time.pattern <br>
     * FR_Date.pattern <br>
     * ISO Date.pattern <br>
     * Month.pattern <br>
     * Week Day.pattern
     */
    private static final String[] PATTERN_DATE_UUIDS = { "_nj3TsD4KEd20H7qFpzB9dg", "_khsAgIyhEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$
            "_WAPQoIyeEd6Dle_8xC0lwg", "_pne9AIyeEd6Dle_8xC0lwg", "_4A6EMIyfEd6Dle_8xC0lwg", "_ypukIIyfEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_fLaxUIyfEd6Dle_8xC0lwg", "_pX0T4IyfEd6Dle_8xC0lwg", "_hytsED4YEd24WPpU35gV1A", "_tZlYsPbMEd2c5L1gaJyUtQ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_M4zogD4ZEd24WPpU35gV1A", "_ZNaNsD4ZEd24WPpU35gV1A", "_Wub7wD4XEd24WPpU35gV1A" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * the follows' uuids<br>
     * <br>
     * Email Address.pattern <br>
     * IP_Address.pattern <br>
     * Website_URL.pattern <br>
     * Website_validation.pattern
     */
    private static final String[] PATTERN_INTERNET_UUIDS = { "_rC5TcD4XEd24WPpU35gV1A", "_pOs9MAyEEd6OfvNiP9lrnQ", //$NON-NLS-1$ //$NON-NLS-2$
            "_yaXdsYZMEd2G3OfabUsfOQ", "_0_J_4IyeEd6Dle_8xC0lwg" }; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * the follows' uuids<br>
     * <br>
     * American Express Card Number.pattern <br>
     * Bank_Routing_Transit_Number_(RTN).pattern <br>
     * ContainsNumbers.pattern <br>
     * Currency_16,3.pattern <br>
     * FR_SSN.pattern <br>
     * Integer.pattern <br>
     * International_Passport.pattern <br>
     * ISBN_Checker.pattern <br>
     * Istat_Code.pattern <br>
     * Longitude_Validation.pattern <br>
     * Master Card Number.pattern <br>
     * Non_Zero_1st_Digit.pattern <br>
     * Numeric.pattern <br>
     * Swedish_Personal_Nr_(Personnummer).pattern <br>
     * Swedish_personnummer_with_accepted_foreigners.pattern <br>
     * UK_Vehicle_Registration_Plate_Number_Plate.pattern <br>
     * US SSN.pattern
     */
    private static final String[] PATTERN_NUMBER_UUIDS = { "_FcDG0D17Ed2p44yVAaDvbQ", "_j5FFMIydEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$
            "_Lnh0kGUuEd2RuP05jDphQg", "_8TNRwIydEd6Dle_8xC0lwg", "_OuO-UPbPEd2c5L1gaJyUtQ", "_0dzx4D4bEd24WPpU35gV1A", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_PRL0IIydEd6Dle_8xC0lwg", "_WGxL4IydEd6Dle_8xC0lwg", "_ZYFdkIybEd6Dle_8xC0lwg", "_CwBEgIybEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_6YWv4D16Ed2p44yVAaDvbQ", "_D7IDEAulEeKxJpyXB0egyQ", "_OAAOQD1iEd2irYhnXOOajA", "_07nrEIybEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "_shG84IybEd6Dle_8xC0lwg", "_8U2icIybEd6Dle_8xC0lwg", "_BpSRwD15Ed2p44yVAaDvbQ" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * the follows' uuids<br>
     * <br>
     * 10_Digit_US_Phone_Number.pattern<br>
     * Complex_Australian_Phone_Number.pattern<br>
     * FR_Phone_Number.pattern<br>
     * FR_Phone_Number_(International).pattern<br>
     * FR_Phone_Number_(Local_or_International).pattern<br>
     * German_Phone_number_(International).pattern<br>
     * German_Phone_number_(Local ).pattern<br>
     * German_Phone_number_(Local_or_International ).pattern<br>
     * International_phone_number.pattern<br>
     * Mobile_number_of_India.pattern<br>
     * Phone_Brazil.pattern<br>
     * Phone_Number.pattern<br>
     * UK Phone Number.pattern <br>
     * US_Phone_Number.pattern
     */
    private static final String[] PATTERN_PHONE_UUIDS =
            {
                    "_QbsC8IyeEd6Dle_8xC0lwg", "_CnD1cIyeEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$
                    "_SPiUIPbKEd2c5L1gaJyUtQ", "_9LBjMIygEd6Dle_8xC0lwg", "_DqoD4IyhEd6Dle_8xC0lwg", "_W6xUUIygEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    "_NW0R0IygEd6Dle_8xC0lwg", "_eVNsoIygEd6Dle_8xC0lwg", "_Is95MIydEd6Dle_8xC0lwg", "__RSB4IycEd6Dle_8xC0lwg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    "_mRzFgIycEd6Dle_8xC0lwg", "_S7m6oIyhEd6Dle_8xC0lwg", "_IIVOAD4XEd24WPpU35gV1A", "_bdjpwLr8Ed2z8Ya9pb9seQ" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * the follows' uuids<br>
     * <br>
     * Blank_Text.pattern <br>
     * Empty_Text.pattern <br>
     * Home_Row_Text.pattern <br>
     * Linux_Path_Verify.pattern <br>
     * No_special_chart.pattern <br>
     * Random_Consonants.pattern <br>
     * Starts_with_blank.pattern <br>
     * Starts_with_space.pattern <br>
     * Starts_with_uppercase.pattern <br>
     * Uppercased_Single_Word.pattern
     */
    private static final String[] PATTERN_TEXT_UUIDS =
            {
                    "_N7yi0Gk3Ed2I1bT57-sHUw", "_vZ6_IGk6Ed2I1bT57-sHUw", //$NON-NLS-1$ //$NON-NLS-2$
                    "_e4vUoYyZEd6Dle_8xC0lwg", "_IqpTEIyfEd6Dle_8xC0lwg", "_6rm3QIycEd6Dle_8xC0lwg", "_gDqDcP9eEd2mwaQf90ozSg", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    "_18GtIIyaEd6Dle_8xC0lwg", "_scYqYIyaEd6Dle_8xC0lwg", "_53rX8AMvEeKk6OZDw5mXOQ", "_FyTwIHnTEd2weu9skTiCqA" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * the follows' uuids<br>
     * <br>
     * gmail.pattern<br>
     * yahoo.pattern
     */

    private static final List<?> PATTERN_REGEX_ADDRESS_UUIDS = Arrays.asList(PATTERN_ADDRESS_UUIDS);

    private static final List<?> PATTERN_REGEX_CODE_UUIDS = Arrays.asList(PATTERN_CODE_UUIDS);

    private static final List<?> PATTERN_REGEX_COLOR_UUIDS = Arrays.asList(PATTERN_COLOR_UUIDS, PATTERN_CUSTOMER_UUIDS);

    private static final List<?> PATTERN_REGEX_DATE_UUIDS = Arrays.asList(PATTERN_DATE_UUIDS);

    private static final List<?> PATTERN_REGEX_INTERNET_UUIDS = Arrays.asList(PATTERN_INTERNET_UUIDS);

    private static final List<?> PATTERN_REGEX_NUMBER_UUIDS = Arrays.asList(PATTERN_NUMBER_UUIDS);

    private static final List<?> PATTERN_REGEX_PHONE_UUIDS = Arrays.asList(PATTERN_PHONE_UUIDS);

    private static final List<?> PATTERN_REGEX_TEXT_UUIDS = Arrays.asList(PATTERN_TEXT_UUIDS);

    public static RecycleBinRepNode getRecycleBinRepNode() {
        if (recycleBinRepNode == null) {
            recycleBinRepNode = initRecycleBinRepNode();
        }
        return recycleBinRepNode;
    }

    /**
     * TODO: until now, we only consider the recycle bin node in the current main project in this method
     *
     * @return RecycleBinRepNode
     */
    private static RecycleBinRepNode initRecycleBinRepNode() {
        CommonViewer commonViewer = getDQCommonViewer(true);
        if (commonViewer != null) {
            TreeItem[] items = commonViewer.getTree().getItems();
            for (TreeItem item : items) {
                DQRepositoryNode node = (DQRepositoryNode) item.getData();
                if (node.isBin() && node.getProject().isMainProject()) {
                    return (RecycleBinRepNode) node;
                }
            }
        }
        return null;
    }

    public static void setRecycleBinRepNode(RecycleBinRepNode recycleBinRepNode) {
        RepositoryNodeHelper.recycleBinRepNode = recycleBinRepNode;
    }

    private RepositoryNodeHelper() {
    }

    /**
     *
     * Get the path of current node
     *
     * @param node
     * @return
     */
    public static IPath getPath(IRepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node.isBin()) {
            return new Path(PluginConstant.EMPTY_STRING);
        }
        if (node.getType() == null) {
            return null;
        }
        switch (node.getType()) {
        case SYSTEM_FOLDER:
            ERepositoryObjectType contentType = node.getContentType();
            if (contentType == null) {
                Item item = node.getObject().getProperty().getItem();
                contentType = ERepositoryObjectType.getItemType(item);
            }
            return new Path(ERepositoryObjectType.getFolderName(contentType));
        case SIMPLE_FOLDER:
            String label = PluginConstant.EMPTY_STRING;
            if (node.getObject() != null) {
                label = node.getObject().getProperty().getLabel();
            }
            if (node instanceof ReportSubFolderRepNode) {
                ReportSubFolderRepNode reportSubFolderRepNode = (ReportSubFolderRepNode) node;
                label = reportSubFolderRepNode.getLabel();
            }
            return getPath(node.getParent()).append(label);
        default:
        }
        return getPath(node.getParent());
    }

    /**
     *
     * Get the first system path of current node
     *
     * @param node
     * @return
     */
    public static IPath getFirstSystemPath(IRepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node.isBin()) {
            return new Path(PluginConstant.EMPTY_STRING);
        }
        if (node.getType() == null) {
            return null;
        }
        switch (node.getType()) {
        case SYSTEM_FOLDER:
            ERepositoryObjectType contentType = node.getContentType();
            if (contentType == null) {
                Item item = node.getObject().getProperty().getItem();
                contentType = ERepositoryObjectType.getItemType(item);
            }
            return new Path(ERepositoryObjectType.getFolderName(contentType));
        default:
            return getFirstSystemPath(node.getParent());
        }
    }

    public static ERepositoryObjectType retrieveRepObjectTypeByPath(String path) {
        if (EResourceConstant.DATA_PROFILING.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_DATA_PROFILING;
        } else if (EResourceConstant.ANALYSIS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT;
        } else if (EResourceConstant.REPORTS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_REPORT_ELEMENT;
        } else if (EResourceConstant.LIBRARIES.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_LIBRARIES;
        } else if (EResourceConstant.EXCHANGE.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_EXCHANGE;
        } else if (EResourceConstant.INDICATORS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_INDICATOR_ELEMENT;
        } else if (EResourceConstant.SYSTEM_INDICATORS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_SYSTEM_INDICATORS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_ADVANCED_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_ADVANCED_STATISTICS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_BUSINESS_RULES.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_BUSINESS_RULES;
        } else if (EResourceConstant.SYSTEM_INDICATORS_CORRELATION.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_CORRELATION;
        } else if (EResourceConstant.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY;
        } else if (EResourceConstant.SYSTEM_INDICATORS_OVERVIEW.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_OVERVIEW;
        } else if (EResourceConstant.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_PATTERN_MATCHING.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_MATCHING;
        } else if (EResourceConstant.SYSTEM_INDICATORS_ROW_COMPARISON.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_ROW_COMPARISON;
        } else if (EResourceConstant.SYSTEM_INDICATORS_SIMPLE_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SIMPLE_STATISTICS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_SOUNDEX.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SOUNDEX;
        } else if (EResourceConstant.SYSTEM_INDICATORS_SUMMARY_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SUMMARY_STATISTICS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS;
        } else if (EResourceConstant.SYSTEM_INDICATORS_TEXT_STATISTICS.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_TEXT_STATISTICS;
        } else if (EResourceConstant.USER_DEFINED_INDICATORS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS;
        } else if (EResourceConstant.JRXML_TEMPLATE.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_JRAXML_ELEMENT;
        } else if (EResourceConstant.PATTERNS.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_PATTERN_ELEMENT;
        } else if (EResourceConstant.PATTERN_REGEX.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_PATTERN_REGEX;
        } else if (EResourceConstant.PATTERN_SQL.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_PATTERN_SQL;
        } else if (EResourceConstant.RULES.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_RULES;
        } else if (EResourceConstant.RULES_SQL.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_RULES_SQL;
        } else if (EResourceConstant.RULES_MATCHER.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_RULES_MATCHER;
        } else if (EResourceConstant.RULES_PARSER.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_RULES_PARSER;
        } else if (EResourceConstant.SOURCE_FILES.getPath().equals(path)) {
            return ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT;
        } else if (EResourceConstant.METADATA.getPath().equals(path)) {
            return ERepositoryObjectType.METADATA;
        } else if (EResourceConstant.DB_CONNECTIONS.getPath().equals(path)) {
            return ERepositoryObjectType.METADATA_CONNECTIONS;
        } else if (EResourceConstant.FILEDELIMITED.getPath().equals(path)) {
            return ERepositoryObjectType.METADATA_FILE_DELIMITED;
        } else if (EResourceConstant.HADOOP_CLUSTER.getPath().equals(path)
                && HadoopClusterUtils.getDefault().isServiceInstalled()) {
            return HadoopClusterUtils.getDefault().getHadoopClusterType();
        } else if (EResourceConstant.SYSTEM_INDICATORS_FRAUDDETECTION.getPath().equals(path)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION;
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "getSystemIndicatorFolderRepositoryType".
     *
     * @param label
     * @return
     */
    public static ERepositoryObjectType getSystemIndicatorFolderRepositoryType(String label) {
        if (ERepositoryObjectType.getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_ADVANCED_STATISTICS).endsWith(
                label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_ADVANCED_STATISTICS;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_BUSINESS_RULES)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_BUSINESS_RULES;
        } else if (ERepositoryObjectType.getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_CORRELATION).endsWith(
                label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_CORRELATION;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_OVERVIEW)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_OVERVIEW;
        } else if (ERepositoryObjectType.getFolderName(
                ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS).endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_MATCHING)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_MATCHING;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_ROW_COMPARISON)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_ROW_COMPARISON;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_SIMPLE_STATISTICS)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SIMPLE_STATISTICS;
        } else if (ERepositoryObjectType.getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_SOUNDEX).endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SOUNDEX;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_SUMMARY_STATISTICS)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_SUMMARY_STATISTICS;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_TEXT_STATISTICS)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_TEXT_STATISTICS;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS;
        } else if (ERepositoryObjectType
                .getFolderName(ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION)
                .endsWith(label)) {
            return ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION;
        }
        return null;
    }

    public static List<DBColumnRepNode> getColumnNodeList(Object[] objs) {
        List<DBColumnRepNode> nodeList = new ArrayList<DBColumnRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBColumnRepNode) {
                nodeList.add((DBColumnRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasColumnNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBColumnRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DBTableRepNode> getTableNodeList(Object[] objs) {
        List<DBTableRepNode> nodeList = new ArrayList<DBTableRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBTableRepNode) {
                nodeList.add((DBTableRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasTableNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBTableRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DBViewRepNode> getViewNodeList(Object[] objs) {
        List<DBViewRepNode> nodeList = new ArrayList<DBViewRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBViewRepNode) {
                nodeList.add((DBViewRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasViewNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBViewRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DBSchemaRepNode> getSchemaNodeList(Object[] objs) {
        List<DBSchemaRepNode> nodeList = new ArrayList<DBSchemaRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBSchemaRepNode) {
                nodeList.add((DBSchemaRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasSchemaNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBSchemaRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DBCatalogRepNode> getCatalogNodeList(Object[] objs) {
        List<DBCatalogRepNode> nodeList = new ArrayList<DBCatalogRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBCatalogRepNode) {
                nodeList.add((DBCatalogRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasCatalogNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBCatalogRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<DBConnectionRepNode> getDbConnectionNodeList(Object[] objs) {
        List<DBConnectionRepNode> nodeList = new ArrayList<DBConnectionRepNode>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof DBConnectionRepNode) {
                nodeList.add((DBConnectionRepNode) obj);
            }
        }
        return nodeList;
    }

    public static boolean hasDbConnectionNode(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof DBConnectionRepNode) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<TdColumn> getTdColumnList(Object[] objs) {
        List<TdColumn> list = new ArrayList<TdColumn>();
        for (Object obj : objs) {
            if (obj != null && obj instanceof TdColumn) {
                list.add((TdColumn) obj);
            }
        }
        return list;
    }

    public static boolean hasTdColumn(Object[] objs) {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (obj != null && obj instanceof TdColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * find RepositoryNode by property, if no RepositoryNode, return null.
     *
     * @param property
     * @return
     */
    public static RepositoryNode recursiveFind(Property property) {
        Item item = property.getItem();
        if (item instanceof ContextItem) {
            return recursiveFindContext(property);
        }
        ModelElement resourceModelElement = getResourceModelElement(item);
        if (resourceModelElement != null) {
            return recursiveFind(resourceModelElement);
        }

        return null;
    }

    /**
     * find RepositoryNode by ModelElement, if no RepositoryNode, return null.
     *
     * @param modelElement
     * @param isCreate whether create new one when can't find the node from RepositoryView
     * @return
     */
    public static DQRepositoryNode recursiveFind(ModelElement modelElement) {
        if (modelElement instanceof Analysis) {
            return recursiveFindAnalysis((Analysis) modelElement);
        } else if (modelElement instanceof TdReport) {
            return recursiveFindReport((Report) modelElement);
        } else if (modelElement instanceof DatabaseConnection) {
            return recursiveFindDatabaseConnection((DatabaseConnection) modelElement);
        } else if (modelElement instanceof Catalog) {
            return recursiveFindCatalog((Catalog) modelElement);
        } else if (modelElement instanceof Schema) {
            return recursiveFindSchema((Schema) modelElement);
        } else if (modelElement instanceof TdTable) {
            return recursiveFindTdTable((TdTable) modelElement);
        } else if (modelElement instanceof TdView) {
            return recursiveFindTdView((TdView) modelElement);
        } else if (modelElement instanceof TdColumn) {
            return recursiveFindTdColumn((TdColumn) modelElement);
        } else if (modelElement instanceof DelimitedFileConnection) {
            return recursiveFindDFConnection((DelimitedFileConnection) modelElement);
        } else if (modelElement instanceof MetadataTable) {
            // can we use this type? it is duplicate with tdView and tdTable.
            return recursiveFindMetadataTable((MetadataTable) modelElement);
        } else if (modelElement instanceof MetadataColumn) {
            return recursiveFindMetadataColumn((MetadataColumn) modelElement);
        } else if (modelElement instanceof Pattern) {
            return recursiveFindPattern((Pattern) modelElement);
        } else if (modelElement instanceof IndicatorDefinition) {
            if (modelElement instanceof WhereRule) {
                return recursiveFindRuleSql((WhereRule) modelElement);
            } else if (modelElement instanceof ParserRule) {
                return recursiveFindRuleParser((ParserRule) modelElement);
            } else if (modelElement instanceof MatchRuleDefinition) {
                return recursiveFindMatcherRule((MatchRuleDefinition) modelElement);
            } else {
                return recursiveFindIndicatorDefinition((IndicatorDefinition) modelElement);
            }
            // ADD msjian TDQ-4209 2012-02-03: find the *.jrxml and *.sql file
        } else if (modelElement instanceof IFile) {
            return recursiveFindFile((IFile) modelElement);
            // TDQ-4209 ~
        }
        return null;
        // !!!following codes are testing codes, please don't delete them, thanks!!!
        // RepositoryNode repNode = recursiveFindByUuid(uuid, nodes);
        // if (repNode != null) {
        // System.out.println("[class name: " + repNode.getClass().getName() + "][uuid: " + uuid + "][id: " +
        // repNode.getId()
        // + "][label: " + repNode.getLabel() + "]");
        // } else {
        // System.out.println("[class name: " + modelElement.getClass().getName() + "][uuid: " + uuid + "][name: "
        // + modelElement.getName() + "] NOT FOUND!!!");
        // }
        // return repNode;
    }

    /**
     * create a new RepositoryNode when the node is hide on the repositoryView.
     *
     * @param findModelElement
     * @throws PersistenceException
     */
    public static RepositoryNode createRepositoryNode(ModelElement findModelElement) {
        RepositoryNode returnNode = null;
        if (findModelElement == null) {
            return returnNode;
        }
        Property property = PropertyHelper.getProperty(findModelElement);
        IRepositoryViewObject lastVersion = null;
        if (property == null) {
            log.error("Can not find property from modelElement: " + findModelElement.getName()); //$NON-NLS-1$
            return returnNode;
        } else {
            try {
                lastVersion = ProxyRepositoryFactory.getInstance().getLastVersion(property.getId());
            } catch (PersistenceException e) {
                log.error(e, e);
                return returnNode;
            }
            if (lastVersion == null) {
                log.error("Can not find lastVersion from property: " + property.getDisplayName()); //$NON-NLS-1$
                return returnNode;
            }
        }
        if (findModelElement instanceof TdColumn) {
            returnNode = createMetadataColumnRepNode((TdColumn) findModelElement, lastVersion);
        } else if (findModelElement instanceof TdTable) {
            returnNode = createDBTableRepNode((TdTable) findModelElement, lastVersion);
        } else if (findModelElement instanceof TdView) {
            returnNode = createDBViewRepNode((TdView) findModelElement, lastVersion);
        }
        return returnNode;
    }

    /**
     * DOC talend Comment method "createDBViewRepNode".
     *
     * @param findModelElement
     * @param lastVersion
     * @return
     */
    private static RepositoryNode createDBViewRepNode(TdView findModelElement, IRepositoryViewObject lastVersion) {
        TdViewRepositoryObject tdViewRepositoryObject = new TdViewRepositoryObject(lastVersion, findModelElement);
        tdViewRepositoryObject.setId(findModelElement.getName());
        tdViewRepositoryObject.setLabel(findModelElement.getName());
        DBViewRepNode dbViewRepNode =
                new DBViewRepNode(tdViewRepositoryObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        dbViewRepNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_COLUMN);
        dbViewRepNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_COLUMN);
        tdViewRepositoryObject.setRepositoryNode(dbViewRepNode);
        return dbViewRepNode;
    }

    /**
     * DOC talend Comment method "createDBTableRepNode".
     *
     * @param findModelElement
     * @return
     */
    private static RepositoryNode createDBTableRepNode(TdTable findModelElement, IRepositoryViewObject lastVersion) {
        TdTableRepositoryObject tdTableRepositoryObject = new TdTableRepositoryObject(lastVersion, findModelElement);
        tdTableRepositoryObject.setId(findModelElement.getName());
        tdTableRepositoryObject.setLabel(findModelElement.getName());
        DBTableRepNode dbTableRepNode =
                new DBTableRepNode(tdTableRepositoryObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        dbTableRepNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_COLUMN);
        dbTableRepNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_COLUMN);
        tdTableRepositoryObject.setRepositoryNode(dbTableRepNode);
        return dbTableRepNode;
    }

    /**
     * create a temp RepNode there is a lack of parent attribute
     *
     * @param findColumn
     */
    private static DBColumnRepNode createMetadataColumnRepNode(TdColumn findColumn, IRepositoryViewObject lastVersion) {
        MetadataColumnRepositoryObject metadataColumn = new MetadataColumnRepositoryObject(lastVersion, findColumn);
        metadataColumn.setId(findColumn.getName());
        metadataColumn.setLabel(findColumn.getLabel());
        DBColumnRepNode columnNode = new DBColumnRepNode(metadataColumn, null, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        columnNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_COLUMN);
        columnNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_COLUMN);
        metadataColumn.setRepositoryNode(columnNode);
        return columnNode;
    }

    /**
     * DOC msjian Comment method "recursiveFindFile".
     *
     * @param file
     * @return
     */
    public static DQRepositoryNode recursiveFindFile(IFile file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getName();

        List<?> fileRepNodes = null;
        if (fileName.toLowerCase().endsWith(PluginConstant.JRXML_STRING)) {
            fileRepNodes = getJrxmlFileRepNodes(getLibrariesFolderNode(EResourceConstant.JRXML_TEMPLATE), true);

        } else if (fileName.toLowerCase().endsWith(PluginConstant.SQL_STRING)) {
            fileRepNodes = getSourceFileRepNodes(getLibrariesFolderNode(EResourceConstant.SOURCE_FILES), true);
        }

        if (fileRepNodes != null) {
            for (int i = 0; i < fileRepNodes.size(); i++) {
                DQRepositoryNode childNode = (DQRepositoryNode) fileRepNodes.get(i);

                String childNodeFileName = PluginConstant.EMPTY_STRING;
                if (childNode instanceof JrxmlTempleteRepNode) {
                    childNodeFileName =
                            childNode
                                    .getObject()
                                    .getProperty()
                                    .eResource()
                                    .getURI()
                                    .lastSegment()
                                    .replaceAll(PluginConstant.PROPERTIES_STRING, PluginConstant.JRXML_STRING);
                } else if (childNode instanceof SourceFileRepNode) {
                    childNodeFileName =
                            childNode
                                    .getObject()
                                    .getProperty()
                                    .eResource()
                                    .getURI()
                                    .lastSegment()
                                    .replaceAll(PluginConstant.PROPERTIES_STRING, PluginConstant.SQL_STRING);
                }
                if (fileName.equals(childNodeFileName)) {
                    return childNode;
                }
            }
        }

        return null;
    }

    /**
     * DOC klliu Comment method "recursiveFindRuleParser".
     *
     * @param modelElement
     * @return
     */
    public static RuleRepNode recursiveFindRuleParser(ParserRule rule) {
        if (rule == null) {
            return null;
        }
        String uuid = getUUID(rule);
        if (uuid == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(rule);

        IRepositoryNode librariesFolderNode = getParserRuleFolderNode(rule);
        List<RuleRepNode> ruleRepNodes = getRuleRepNodes(librariesFolderNode, true, true);
        if (ruleRepNodes.size() > 0) {
            for (RuleRepNode childNode : ruleRepNodes) {
                if (uuid.equals(getUUID(childNode.getRule()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getRule());
                    // TDQ-17346 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isSameProject(Project nodeInWhichProject, Project inWhichProject) {
        return nodeInWhichProject.getTechnicalLabel().equals(inWhichProject.getTechnicalLabel());
    }
    /**
     * DOC msjian Comment method "getParserRuleFolderNode".
     *
     * @param rule
     * @return
     */
    private static IRepositoryNode getParserRuleFolderNode(ParserRule rule) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getLibrariesFolderNode(EResourceConstant.RULES_PARSER);
        } else {
            Project inWhichProject = getInWhichProject(rule);
            return getLibrariesFolderNode(EResourceConstant.RULES_PARSER, inWhichProject);
        }
    }

    /**
     * recursiveFind MatcherRule
     *
     * @param modelElement
     * @return
     */
    public static RuleRepNode recursiveFindMatcherRule(MatchRuleDefinition rule) {
        if (rule == null) {
            return null;
        }
        String uuid = getUUID(rule);
        if (uuid == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(rule);
        IRepositoryNode librariesFolderNode = getMatchRuleFolderNode(rule);
        List<RuleRepNode> ruleRepNodes = getRuleRepNodes(librariesFolderNode, true, true);
        if (ruleRepNodes.size() > 0) {
            for (RuleRepNode childNode : ruleRepNodes) {
                if (uuid.equals(getUUID(childNode.getRule()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getRule());
                    // TDQ-17346 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getMatchRuleFolderNode".
     *
     * @param rule
     * @return
     */
    private static IRepositoryNode getMatchRuleFolderNode(MatchRuleDefinition rule) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getLibrariesFolderNode(EResourceConstant.RULES_MATCHER);
        } else {
            Project inWhichProject = getInWhichProject(rule);
            return getLibrariesFolderNode(EResourceConstant.RULES_MATCHER, inWhichProject);
        }
    }

    public static List<DBConnectionRepNode> getDBConnectionRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<DBConnectionRepNode> result = new ArrayList<DBConnectionRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof DBConnectionFolderRepNode || parrentNode instanceof DBConnectionSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof DBConnectionRepNode) {
                        result.add((DBConnectionRepNode) inode);
                    } else if (inode instanceof DBConnectionFolderRepNode
                            || inode instanceof DBConnectionSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getDBConnectionRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<DFConnectionRepNode> getDFConnectionRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<DFConnectionRepNode> result = new ArrayList<DFConnectionRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof DFConnectionFolderRepNode || parrentNode instanceof DFConnectionSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof DFConnectionRepNode) {
                        result.add((DFConnectionRepNode) inode);
                    } else if (inode instanceof DFConnectionFolderRepNode
                            || inode instanceof DFConnectionSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getDFConnectionRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    // MOD qiongli 2011-4-6,bug 20218,Add parameter 'withDeleted'.
    public static List<AnalysisRepNode> getAnalysisRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<AnalysisRepNode> result = new ArrayList<AnalysisRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof AnalysisFolderRepNode || parrentNode instanceof AnalysisSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof AnalysisRepNode) {
                        result.add((AnalysisRepNode) inode);
                    } else if (inode instanceof AnalysisFolderRepNode || inode instanceof AnalysisSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getAnalysisRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<ReportRepNode> getReportRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<ReportRepNode> result = new ArrayList<ReportRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof ReportFolderRepNode || parrentNode instanceof ReportSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof ReportRepNode) {
                        result.add((ReportRepNode) inode);
                    } else if (inode instanceof ReportFolderRepNode || inode instanceof ReportSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getReportRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<SysIndicatorDefinitionRepNode> getIndicatorDefinitionRepNodes(IRepositoryNode parrentNode,
            boolean recursiveFind, boolean withDeleted) {
        List<SysIndicatorDefinitionRepNode> result = new ArrayList<SysIndicatorDefinitionRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof SysIndicatorFolderRepNode
                        || parrentNode instanceof UserDefIndicatorFolderRepNode || parrentNode instanceof UserDefIndicatorSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof SysIndicatorDefinitionRepNode) {
                        result.add((SysIndicatorDefinitionRepNode) inode);
                    } else if (inode instanceof SysIndicatorFolderRepNode
                            || inode instanceof UserDefIndicatorFolderRepNode
                            || inode instanceof UserDefIndicatorSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getIndicatorDefinitionRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<PatternRepNode> getPatternRepNodes(IRepositoryNode parentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<PatternRepNode> result = new ArrayList<PatternRepNode>();
        if (parentNode != null
                && (parentNode instanceof PatternRegexFolderRepNode
                        || parentNode instanceof PatternRegexSubFolderRepNode
                        || parentNode instanceof PatternSqlFolderRepNode || parentNode instanceof PatternSqlSubFolderRepNode)) {
            List<IRepositoryNode> children = parentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof PatternRepNode) {
                        result.add((PatternRepNode) inode);
                    } else if (inode instanceof PatternRegexFolderRepNode
                            || inode instanceof PatternRegexSubFolderRepNode
                            || inode instanceof PatternSqlFolderRepNode || inode instanceof PatternSqlSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getPatternRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<RuleRepNode> getRuleRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<RuleRepNode> result = new ArrayList<RuleRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof RulesSQLFolderRepNode || parrentNode instanceof RulesSQLSubFolderRepNode
                        || parrentNode instanceof RulesParserSubFolderRepNode
                        || parrentNode instanceof RulesParserFolderRepNode
                        || parrentNode instanceof RulesMatcherSubFolderRepNode || parrentNode instanceof RulesMatcherFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof RuleRepNode) {
                        result.add((RuleRepNode) inode);
                    } else if (inode instanceof RulesSQLFolderRepNode || inode instanceof RulesSQLSubFolderRepNode
                            || inode instanceof RulesParserSubFolderRepNode
                            || inode instanceof RulesMatcherSubFolderRepNode) {
                        if (recursiveFind) {
                            result.addAll(getRuleRepNodes(inode, recursiveFind, withDeleted));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static DBConnectionRepNode recursiveFindDatabaseConnection(DatabaseConnection dbConn) {
        if (dbConn == null) {
            return null;
        }
        String uuid = getUUID(dbConn);
        if (uuid == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(dbConn);
        IRepositoryNode MetadataDBConnectionFolderNode = getMetadataDBConnectionFolderNode(dbConn);
        List<DBConnectionRepNode> dbConnectionRepNodes =
                getDBConnectionRepNodes(MetadataDBConnectionFolderNode, true, true);
        if (dbConnectionRepNodes.size() > 0) {
            for (DBConnectionRepNode childNode : dbConnectionRepNodes) {
                if (uuid.equals(getUUID(childNode.getDatabaseConnection()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getDatabaseConnection());
                    // TDQ-16396 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getMetadataDBConnectionFolderNode".
     *
     * @param dbConn
     * @return
     */
    private static IRepositoryNode getMetadataDBConnectionFolderNode(DatabaseConnection dbConn) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
        } else {
            Project inWhichProject = getInWhichProject(dbConn);
            return getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS, inWhichProject);
        }
    }

    public static DFConnectionRepNode recursiveFindDFConnection(DelimitedFileConnection dfConn) {
        if (dfConn == null) {
            return null;
        }
        String uuid = getUUID(dfConn);
        if (uuid == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(dfConn);
        IRepositoryNode MetadataFileDelimitedFolderNode = getMetadataFileDelimitedFolderNode(dfConn);
        List<DFConnectionRepNode> dfConnectionRepNodes =
                getDFConnectionRepNodes(MetadataFileDelimitedFolderNode, true, true);
        if (dfConnectionRepNodes.size() > 0) {
            for (DFConnectionRepNode childNode : dfConnectionRepNodes) {
                if (uuid.equals(getUUID(childNode.getDfConnection()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getDfConnection());
                    // TDQ-16396 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getMetadataFileDelimitedFolderNode".
     *
     * @param dfConn
     * @return
     */
    private static IRepositoryNode getMetadataFileDelimitedFolderNode(DelimitedFileConnection dfConn) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getMetadataFolderNode(EResourceConstant.FILEDELIMITED);
        } else {
            Project inWhichProject = getInWhichProject(dfConn);
            return getMetadataFolderNode(EResourceConstant.FILEDELIMITED, inWhichProject);
        }
    }

    /**
     * find a ContextRepNode by contextItem and label
     *
     * @param property a context node property
     * @return
     */
    public static ContextRepNode recursiveFindContext(Property property) {
        if (property == null) {
            return null;
        }
        // compare the label instead of UUID. since the UUID will be same when duplicate a context
        String label = property.getLabel();
        if (StringUtils.isEmpty(label)) {
            return null;
        }
        IRepositoryNode contextFolderNode = getContextFolderNode(property);
        List<ContextRepNode> contextRepNodes = getContextRepNodes(contextFolderNode, true, true);
        if (!contextRepNodes.isEmpty()) {
            for (ContextRepNode childNode : contextRepNodes) {
                if (label.equals(childNode.getLabel())) {
                    return childNode;
                }
            }
        }
        return null;
    }

    public static AnalysisRepNode recursiveFindAnalysis(Analysis analysis) {
        if (analysis == null) {
            return null;
        }
        String uuid = getUUID(analysis);
        if (uuid == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(analysis);

        IRepositoryNode DataProfilingAnalysisFolderNode = getDataProfilingAnalysisFolderNode(analysis);
        // MOD qiongli 2011-4-6,bug 20218,add parameter withDeleted(true), contain child is in recycle bin.
        List<AnalysisRepNode> analysisRepNodes = getAnalysisRepNodes(DataProfilingAnalysisFolderNode, true, true);
        if (analysisRepNodes.size() > 0) {
            for (AnalysisRepNode childNode : analysisRepNodes) {
                if (uuid.equals(getUUID(childNode.getAnalysis()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getAnalysis());
                    // TDQ-16396 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getDataProfilingAnalysisFolderNode".
     *
     * @param analysis
     * @return
     */
    private static IRepositoryNode getDataProfilingAnalysisFolderNode(Analysis analysis) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getDataProfilingFolderNode(EResourceConstant.ANALYSIS);
        } else {
            org.talend.core.model.general.Project project = getInWhichProject(analysis);
            return getDataProfilingFolderNode(EResourceConstant.ANALYSIS, project);
        }
    }

    private static IRepositoryNode getContextFolderNode(Property property) {
        Project currentProject;
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            currentProject = ProjectManager.getInstance().getCurrentProject();
        } else {
            org.talend.core.model.properties.Project project = ProjectManager.getInstance().getProject(property);
            currentProject = new org.talend.core.model.general.Project(project);
        }
        return getRootNode(ERepositoryObjectType.CONTEXT, true, currentProject);
    }

    /**
     *
     * @param parrentNode
     * @param recursiveFind
     * @param withDeleted
     * @return find all context repositoryNodes
     */
    public static List<ContextRepNode> getContextRepNodes(IRepositoryNode parrentNode, boolean recursiveFind,
            boolean withDeleted) {
        List<ContextRepNode> result = new ArrayList<ContextRepNode>();
        if (parrentNode != null
                && (parrentNode instanceof ContextFolderRepNode || parrentNode instanceof ContextSubFolderRepNode)) {
            List<IRepositoryNode> children = parrentNode.getChildren(withDeleted);
            if (!children.isEmpty()) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof ContextRepNode) {
                        result.add((ContextRepNode) inode);
                    } else if (recursiveFind
                            && (inode instanceof ContextFolderRepNode || inode instanceof ContextSubFolderRepNode)) {
                        result.addAll(getContextRepNodes(inode, recursiveFind, withDeleted));
                    }
                }
            }
        }
        return result;
    }

    /**
     * the modelelement can belong to current project or referenced project.
     *
     * @param analysis
     * @return
     */
    public static org.talend.core.model.general.Project getInWhichProject(ModelElement modelElement) {
        if (modelElement instanceof DatabaseConnection || modelElement instanceof DelimitedFileConnection
                || modelElement instanceof MetadataColumn || modelElement instanceof MetadataTable
                || modelElement instanceof Package) {
            if (modelElement.eIsProxy()) {
                modelElement = (ModelElement) EObjectHelper.resolveObject(modelElement);
            }
            // TDQ-12245: fix a NPE when the modelElement is ref project model
            String projectName = EObjectHelper.getURI(modelElement).segment(1);
            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                if (project.getTechnicalLabel().equals(projectName)) {
                    return project;
                }
            }
        }
        Property property = PropertyHelper.getProperty(modelElement);
        org.talend.core.model.properties.Project project = ProjectManager.getInstance().getProject(property);
        return new org.talend.core.model.general.Project(project);
    }

    public static ReportRepNode recursiveFindReport(Report report) {
        if (report == null) {
            return null;
        }
        String uuid = getUUID(report);
        if (uuid == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(report);

        IRepositoryNode DataProfilingReportFolderNode = getDataProfilingReportFolderNode(report);
        List<ReportRepNode> reportRepNodes = getReportRepNodes(DataProfilingReportFolderNode, true, true);
        if (reportRepNodes.size() > 0) {
            for (ReportRepNode childNode : reportRepNodes) {
                if (uuid.equals(getUUID(childNode.getReport()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getReport());
                    // TDQ-16396 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getDataProfilingReportFolderNodew".
     *
     * @param report
     * @return
     */
    private static IRepositoryNode getDataProfilingReportFolderNode(Report report) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getDataProfilingFolderNode(EResourceConstant.REPORTS);
        } else {
            org.talend.core.model.general.Project project = getInWhichProject(report);
            return getDataProfilingFolderNode(EResourceConstant.REPORTS, project);
        }
    }

    public static SysIndicatorDefinitionRepNode recursiveFindIndicatorDefinition(IndicatorDefinition indDef) {
        if (indDef == null) {
            return null;
        }
        String uuid = getUUID(indDef);
        if (uuid == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(indDef);
        List<SysIndicatorDefinitionRepNode> indicatorDefinitionRepNodes;
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            indicatorDefinitionRepNodes =
                    getIndicatorDefinitionRepNodes(getLibrariesFolderNode(EResourceConstant.SYSTEM_INDICATORS), true,
                            true);
            indicatorDefinitionRepNodes.addAll(getIndicatorDefinitionRepNodes(
                    getLibrariesFolderNode(EResourceConstant.USER_DEFINED_INDICATORS), true, true));
        } else {
            indicatorDefinitionRepNodes =
                    getIndicatorDefinitionRepNodes(
                            getLibrariesFolderNode(EResourceConstant.SYSTEM_INDICATORS, inWhichProject), true, true);
            indicatorDefinitionRepNodes.addAll(getIndicatorDefinitionRepNodes(
                    getLibrariesFolderNode(EResourceConstant.USER_DEFINED_INDICATORS, inWhichProject), true, true));
        }

        if (indicatorDefinitionRepNodes.size() > 0) {
            for (SysIndicatorDefinitionRepNode childNode : indicatorDefinitionRepNodes) {
                // TDQ-17346 msjian: find the exact node with matching its project
                if (uuid.equals(getUUID(childNode.getIndicatorDefinition()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getIndicatorDefinition());
                    if(isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    public static PatternRepNode recursiveFindPattern(Pattern pattern) {
        if (pattern == null) {
            return null;
        }
        String uuid = getUUID(pattern);
        if (uuid == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(pattern);
        List<PatternRepNode> patternRepNodes = getPatternNodesIncludeRefPro(inWhichProject);

        if (patternRepNodes.size() > 0) {
            for (PatternRepNode childNode : patternRepNodes) {
                if (uuid.equals(getUUID(childNode.getPattern()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getPattern());
                    // TDQ-17346 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    public static RuleRepNode recursiveFindRuleSql(DQRule rule) {
        if (rule == null) {
            return null;
        }
        String uuid = getUUID(rule);
        if (uuid == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(rule);

        IRepositoryNode ruleSQLFolderNode = getRuleSQLFolderNode(rule);
        List<RuleRepNode> ruleRepNodes = getRuleRepNodes(ruleSQLFolderNode, true, true);
        if (ruleRepNodes.size() > 0) {
            for (RuleRepNode childNode : ruleRepNodes) {
                if (uuid.equals(getUUID(childNode.getRule()))) {
                    Project nodeInWhichProject = getInWhichProject(childNode.getRule());
                    // TDQ-17346 msjian: find the exact node with matching its project
                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                        return childNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "getRuleSQLFolderNode".
     *
     * @param rule
     * @return
     */
    private static IRepositoryNode getRuleSQLFolderNode(DQRule rule) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            return getLibrariesFolderNode(EResourceConstant.RULES_SQL);
        } else {
            Project inWhichProject = getInWhichProject(rule);
            return getLibrariesFolderNode(EResourceConstant.RULES_SQL, inWhichProject);
        }
    }

    public static DBCatalogRepNode recursiveFindCatalog(Catalog catalog) {
        if (catalog == null) {
            return null;
        }
        String uuidCatalog = getUUID(catalog);
        if (uuidCatalog == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(catalog);

        IRepositoryNode connNode = recursiveFind(ConnectionHelper.getTdDataProvider(catalog));
        if (connNode == null) {
            return null;
        }
        List<IRepositoryNode> children = connNode.getChildren();
        if (children.size() > 0) {
            IRepositoryNode iRepositoryNode = children.get(0);
            if (iRepositoryNode != null && iRepositoryNode instanceof DBCatalogRepNode) {
                for (IRepositoryNode childNode : children) {
                    if (childNode != null && childNode instanceof DBCatalogRepNode) {
                        DBCatalogRepNode dbCatalogRepNode = (DBCatalogRepNode) childNode;
                        if (uuidCatalog.equals(getUUID(dbCatalogRepNode.getCatalog()))) {
                            Project nodeInWhichProject = getInWhichProject(dbCatalogRepNode.getCatalog());
                            // TDQ-16396 msjian: find the exact node with matching its project
                            if (isSameProject(nodeInWhichProject, inWhichProject)) {
                                return dbCatalogRepNode;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * DOC gdbu Comment method "getUUID". Rewrite ResourceHelper.getUUID() method , the difference is that the new
     * method includes the handling proxy situation. ADD for bug TDQ-3735.
     *
     * @param object
     * @return
     */
    public static String getUUID(EObject object) {
        if (null != object && object.eIsProxy()) {
            object = EObjectHelper.resolveObject(object);
        }
        return ResourceHelper.getUUID(object);
    }

    public static DBSchemaRepNode recursiveFindSchema(Schema schema) {
        if (schema == null) {
            return null;
        }
        String uuidSchema = getUUID(schema);
        if (uuidSchema == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(schema);
        Catalog catalog = CatalogHelper.getParentCatalog(schema);
        // Schema's parent is catalog (MS SQL Server)
        if (catalog != null) {
            DBCatalogRepNode catalogNode = recursiveFindCatalog(catalog);
            List<IRepositoryNode> children = catalogNode.getChildren();
            if (children.size() > 0) {
                IRepositoryNode iRepositoryNode = children.get(0);
                if (iRepositoryNode != null && iRepositoryNode instanceof DBSchemaRepNode) {
                    for (IRepositoryNode childNode : children) {
                        if (childNode != null && childNode instanceof DBSchemaRepNode) {
                            DBSchemaRepNode dbSchemaRepNode = (DBSchemaRepNode) childNode;
                            if (uuidSchema.equals(getUUID(dbSchemaRepNode.getSchema()))) {
                                // TDQ-16396 msjian: find the exact node with matching its project
                                Project nodeInWhichProject = getInWhichProject(dbSchemaRepNode.getSchema());
                                if (isSameProject(nodeInWhichProject, inWhichProject)) {
                                    return dbSchemaRepNode;
                                }
                            }
                        }
                    }
                }
            }
        }
        // schema's parent is connection (e.g Oracle)
        IRepositoryNode connNode = recursiveFind(ConnectionHelper.getTdDataProvider(schema));
        if (connNode != null) {
            List<IRepositoryNode> children = connNode.getChildren();
            if (children.size() > 0) {
                IRepositoryNode iRepositoryNode = children.get(0);
                if (iRepositoryNode != null && iRepositoryNode instanceof DBSchemaRepNode) {
                    for (IRepositoryNode childNode : children) {
                        if (childNode != null && childNode instanceof DBSchemaRepNode) {
                            DBSchemaRepNode dbSchemaRepNode = (DBSchemaRepNode) childNode;
                            if (uuidSchema.equals(getUUID(dbSchemaRepNode.getSchema()))) {
                                return dbSchemaRepNode;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static DBTableRepNode recursiveFindTdTable(TdTable tdTable) {
        if (tdTable == null) {
            return null;
        }
        String uuidTdTable = getUUID(tdTable);
        if (uuidTdTable == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(tdTable);
        IRepositoryNode schemaOrCatalogNode = recursiveFind(ColumnSetHelper.getParentCatalogOrSchema(tdTable));
        if (schemaOrCatalogNode == null) {
            return null;
        }
        List<IRepositoryNode> childrens = schemaOrCatalogNode.getChildren();
        List<IRepositoryNode> children = null;
        if (!childrens.isEmpty()) {
            children = childrens.get(0).getChildren();
        }
        if (null != children && children.size() > 0) {
            IRepositoryNode iRepositoryNode = children.get(0);
            if (iRepositoryNode != null && iRepositoryNode instanceof DBTableRepNode) {
                for (IRepositoryNode childNode : children) {
                    if (childNode != null && childNode instanceof DBTableRepNode) {
                        DBTableRepNode dbTableRepNode = (DBTableRepNode) childNode;
                        if (uuidTdTable.equals(getUUID(dbTableRepNode.getTdTable()))) {
                            Project nodeInWhichProject = getInWhichProject(dbTableRepNode.getTdTable());
                            // TDQ-16396 msjian: find the exact node with matching its project
                            if (isSameProject(nodeInWhichProject, inWhichProject)) {
                                return dbTableRepNode;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static DBViewRepNode recursiveFindTdView(TdView tdView) {
        if (tdView == null) {
            return null;
        }
        String uuidTdView = getUUID(tdView);
        if (uuidTdView == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(tdView);

        IRepositoryNode schemaOrCatalogNode = recursiveFind(ColumnSetHelper.getParentCatalogOrSchema(tdView));
        if (schemaOrCatalogNode == null) {
            return null;
        }
        List<IRepositoryNode> children = schemaOrCatalogNode.getChildren().get(1).getChildren();
        if (children.size() > 0) {
            IRepositoryNode iRepositoryNode = children.get(0);
            if (iRepositoryNode != null && iRepositoryNode instanceof DBViewRepNode) {
                for (IRepositoryNode childNode : children) {
                    if (childNode != null && childNode instanceof DBViewRepNode) {
                        DBViewRepNode dbViewRepNode = (DBViewRepNode) childNode;
                        if (uuidTdView.equals(getUUID(dbViewRepNode.getTdView()))) {
                            Project nodeInWhichProject = getInWhichProject(dbViewRepNode.getTdView());
                            // TDQ-16396 msjian: find the exact node with matching its project
                            if (isSameProject(nodeInWhichProject, inWhichProject)) {
                                return dbViewRepNode;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static DBColumnRepNode recursiveFindTdColumn(TdColumn tdColumn) {
        if (tdColumn == null) {
            return null;
        }

        String uuidTdColumn = getUUID(tdColumn);
        if (uuidTdColumn == null) {
            return null;
        }

        Project inWhichProject = getInWhichProject(tdColumn);
        IRepositoryNode columnSetNode = recursiveFind(ColumnHelper.getColumnOwnerAsColumnSet(tdColumn));
        if (columnSetNode == null) {
            return null;
        }
        // MOD gdbu 2011-7-18 bug 23161
        List<IRepositoryNode> childrens = columnSetNode.getChildren();
        if (childrens.size() == 0) {
            return null;
        }

        List<IRepositoryNode> children = childrens.get(0).getChildren();
        if (children.size() == 0) {
            return null;
        }
        // ~23161
        IRepositoryNode iRepositoryNode = children.get(0);
        if (iRepositoryNode != null && iRepositoryNode instanceof DBColumnRepNode) {
            for (IRepositoryNode childNode : children) {
                if (childNode != null && childNode instanceof DBColumnRepNode) {
                    DBColumnRepNode dbColumnRepNode = (DBColumnRepNode) childNode;
                    if (uuidTdColumn.equals(getUUID(dbColumnRepNode.getTdColumn()))) {
                        Project nodeInWhichProject = getInWhichProject(dbColumnRepNode.getTdColumn());
                        // TDQ-16396 msjian: find the exact node with matching its project
                        if (isSameProject(nodeInWhichProject, inWhichProject)) {
                            return dbColumnRepNode;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static DFTableRepNode recursiveFindMetadataTable(MetadataTable metadataTable) {
        if (metadataTable == null) {
            return null;
        }
        String uuidMetadataTable = getUUID(metadataTable);
        if (uuidMetadataTable == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(metadataTable);
        if (metadataTable.getNamespace() instanceof RecordFile) {
            Connection tdDataProvider = ConnectionHelper.getTdDataProvider(metadataTable);
            if (tdDataProvider != null) {
                IRepositoryNode connNode = recursiveFind(tdDataProvider);
                if (connNode != null) {
                    List<IRepositoryNode> children = connNode.getChildren();
                    if (children.size() > 0) {
                        for (IRepositoryNode childNode : children) {
                            if (childNode != null && childNode instanceof DFTableRepNode) {
                                DFTableRepNode dfTableRepNode = (DFTableRepNode) childNode;
                                if (uuidMetadataTable.equals(getUUID(dfTableRepNode.getMetadataTable()))) {
                                    Project nodeInWhichProject = getInWhichProject(dfTableRepNode.getMetadataTable());
                                    // TDQ-16396 msjian: find the exact node with matching its project
                                    if (isSameProject(nodeInWhichProject, inWhichProject)) {
                                        return dfTableRepNode;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static DFColumnRepNode recursiveFindMetadataColumn(MetadataColumn metadataColumn) {
        if (metadataColumn == null) {
            return null;
        }
        String uuidMetadataColumn = getUUID(metadataColumn);
        if (uuidMetadataColumn == null) {
            return null;
        }
        Project inWhichProject = getInWhichProject(metadataColumn);

        IRepositoryNode columnSetNode = recursiveFind(ColumnHelper.getColumnOwnerAsMetadataTable(metadataColumn));
        if (columnSetNode == null) {
            return null;
        }
        List<IRepositoryNode> children = columnSetNode.getChildren().get(0).getChildren();
        if (children.size() > 0) {
            for (IRepositoryNode childNode : children) {
                if (childNode != null && childNode instanceof DFColumnRepNode) {
                    DFColumnRepNode dfColumnRepNode = (DFColumnRepNode) childNode;
                    if (uuidMetadataColumn.equals(getUUID(dfColumnRepNode.getMetadataColumn()))) {
                        Project nodeInWhichProject = getInWhichProject(dfColumnRepNode.getMetadataColumn());
                        // TDQ-16396 msjian: find the exact node with matching its project
                        if (isSameProject(nodeInWhichProject, inWhichProject)) {
                            return dfColumnRepNode;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * find a node from recycle bin.
     *
     * @param modelElement
     * @return
     */
    public static RepositoryNode recursiveFindRecycleBin(ModelElement modelElement) {
        if (modelElement == null) {
            return null;
        }
        String uuid = getUUID(modelElement);
        RepositoryNode recyBinNode = getRecycleBinRepNode();
        if (uuid == null || recyBinNode == null) {
            return null;
        }
        List<IRepositoryNode> children = recyBinNode.getChildren();
        return recursiveFindByUuid(uuid, children);
    }

    // !!!following codes are useful codes, please don't delete them, thanks!!!
    // public static RepositoryNode recursiveFind(ModelElement modelElement) {
    // if (modelElement instanceof Analysis) {
    // Analysis analysis = (Analysis) modelElement;
    // List<IRepositoryNode> dataprofilingNode = getDataProfilingRepositoryNodes(true);
    // for (IRepositoryNode anaNode : dataprofilingNode) {
    // Item itemTemp = ((IRepositoryViewObject) anaNode.getObject()).getProperty().getItem();
    // if (itemTemp instanceof TDQAnalysisItem) {
    // TDQAnalysisItem item = (TDQAnalysisItem) itemTemp;
    // if (ResourceHelper.getUUID(analysis).equals(ResourceHelper.getUUID(item.getAnalysis()))) {
    // return (RepositoryNode) anaNode;
    // }
    // } else if (itemTemp instanceof FolderItem) {
    // List<TDQAnalysisItem> anaItems = getAnalysisItemsFromFolderItem((FolderItem) itemTemp);
    // for (TDQAnalysisItem anaItem : anaItems) {
    // if (ResourceHelper.getUUID(analysis).equals(ResourceHelper.getUUID(anaItem.getAnalysis()))) {
    // return (RepositoryNode) anaNode;
    // }
    // }
    // }
    // }
    // } else if (modelElement instanceof TdReport) {
    // TdReport report = (TdReport) modelElement;
    // List<IRepositoryNode> dataprofilingNode = getDataProfilingRepositoryNodes(true);
    // for (IRepositoryNode patternNode : dataprofilingNode) {
    // Item itemTemp = ((IRepositoryViewObject) patternNode.getObject()).getProperty().getItem();
    // if (itemTemp instanceof TDQReportItem) {
    // TDQReportItem item = (TDQReportItem) itemTemp;
    // if (ResourceHelper.getUUID(report).equals(ResourceHelper.getUUID(item.getReport()))) {
    // return (RepositoryNode) patternNode;
    // }
    // } else if (itemTemp instanceof FolderItem) {
    // List<TDQReportItem> reportItems = getReportItemsFromFolderItem((FolderItem) itemTemp);
    // for (TDQReportItem patternItem : reportItems) {
    // if (ResourceHelper.getUUID(report).equals(ResourceHelper.getUUID(patternItem.getReport()))) {
    // return (RepositoryNode) patternNode;
    // }
    // }
    // }
    // }
    // } else if (modelElement instanceof TdColumn) {
    // TdColumn column = (TdColumn) modelElement;
    // IRepositoryNode columnSetNode = recursiveFind(ColumnHelper.getColumnOwnerAsColumnSet(column));
    // for (IRepositoryNode columnNode : columnSetNode.getChildren().get(0).getChildren()) {
    // TdColumn columnOnUI = (TdColumn) ((MetadataColumnRepositoryObject) columnNode.getObject()).getTdColumn();
    // if (ResourceHelper.getUUID(column).equals(ResourceHelper.getUUID(columnOnUI))) {
    // return (RepositoryNode) columnNode;
    // }
    // }
    // } else if (modelElement instanceof TdTable) {
    // TdTable table = (TdTable) modelElement;
    // IRepositoryNode schemaOrCatalogNode = recursiveFind(ColumnSetHelper.getParentCatalogOrSchema(modelElement));
    // for (IRepositoryNode tableNode : schemaOrCatalogNode.getChildren().get(0).getChildren()) {
    // TdTable tableOnUI = (TdTable) ((TdTableRepositoryObject) tableNode.getObject()).getTdTable();
    // if (ResourceHelper.getUUID(table).equals(ResourceHelper.getUUID(tableOnUI))) {
    // return (RepositoryNode) tableNode;
    // }
    // }
    // } else if (modelElement instanceof TdView) {
    // TdView view = (TdView) modelElement;
    // IRepositoryNode schemaOrCatalogNode = recursiveFind(ColumnSetHelper.getParentCatalogOrSchema(modelElement));
    // for (IRepositoryNode viewNode : schemaOrCatalogNode.getChildren().get(1).getChildren()) {
    // TdView viewOnUI = (TdView) ((TdViewRepositoryObject) viewNode.getObject()).getTdView();
    // if (ResourceHelper.getUUID(view).equals(ResourceHelper.getUUID(viewOnUI))) {
    // return (RepositoryNode) viewNode;
    // }
    // }
    // } else if (modelElement instanceof MetadataColumn) {
    // // MOD qiongli 2011-1-12 for delimted file
    // MetadataColumn column = (MetadataColumn) modelElement;
    // IRepositoryNode columnSetNode = recursiveFind(ColumnHelper.getColumnOwnerAsMetadataTable(column));
    // for (IRepositoryNode columnNode : columnSetNode.getChildren().get(0).getChildren()) {
    // MetadataColumn columnOnUI = ((MetadataColumnRepositoryObject) columnNode.getObject()).getTdColumn();
    // if (ResourceHelper.getUUID(column).equals(ResourceHelper.getUUID(columnOnUI))) {
    // return (RepositoryNode) columnNode;
    // }
    // }
    // } else if (modelElement instanceof MetadataTable) {
    // // MOD qiongli 2011-1-12 for delimted file
    // MetadataTable table = (MetadataTable) modelElement;
    // if (table.getNamespace() instanceof RecordFile) {
    // IRepositoryNode connNode = recursiveFind(ConnectionHelper.getTdDataProvider(table));
    // for (IRepositoryNode tableNode : connNode.getChildren()) {
    // MetadataTable tableOnUI = (MetadataTable) ((MetadataTableRepositoryObject) tableNode.getObject()).getTable();
    // if (ResourceHelper.getUUID(table).equals(ResourceHelper.getUUID(tableOnUI))) {
    // return (RepositoryNode) tableNode;
    // }
    // }
    // }
    // } else if (modelElement instanceof Catalog) {
    // Catalog catalog = (Catalog) modelElement;
    // IRepositoryNode connNode = recursiveFind(ConnectionHelper.getTdDataProvider(catalog));
    // for (IRepositoryNode catalogNode : connNode.getChildren()) {
    // Catalog catalogOnUI = ((MetadataCatalogRepositoryObject) catalogNode.getObject()).getCatalog();
    // if (ResourceHelper.getUUID(catalog).equals(ResourceHelper.getUUID(catalogOnUI))) {
    // return (RepositoryNode) catalogNode;
    // }
    // }
    // } else if (modelElement instanceof Schema) {
    // Schema schema = (Schema) modelElement;
    // Catalog catalog = CatalogHelper.getParentCatalog(schema);
    // // Schema's parent is catalog (MS SQL Server)
    // if (catalog != null) {
    // IRepositoryNode catalogNode = recursiveFind(catalog);
    // for (IRepositoryNode schemaNode : catalogNode.getChildren()) {
    // Schema schemaOnUI = ((MetadataSchemaRepositoryObject) schemaNode.getObject()).getSchema();
    // if (ResourceHelper.getUUID(schema).equals(ResourceHelper.getUUID(schemaOnUI))) {
    // return (RepositoryNode) schemaNode;
    // }
    // }
    // }
    // // schema's parent is connection (e.g Oracle)
    // IRepositoryNode connNode = recursiveFind(ConnectionHelper.getTdDataProvider(schema));
    // for (IRepositoryNode schemaNode : connNode.getChildren()) {
    // Schema schemaOnUI = ((MetadataSchemaRepositoryObject) schemaNode.getObject()).getSchema();
    // if (ResourceHelper.getUUID(schema).equals(ResourceHelper.getUUID(schemaOnUI))) {
    // return (RepositoryNode) schemaNode;
    // }
    // }
    // } else if (modelElement instanceof Connection) {
    //
    // Connection connection = (Connection) modelElement;
    // List<IRepositoryNode> connsNode = getConnectionRepositoryNodes(true);
    // for (IRepositoryNode connNode : connsNode) {
    // Item itemTemp = ((IRepositoryViewObject) connNode.getObject()).getProperty().getItem();
    // if (itemTemp instanceof ConnectionItem) {
    // ConnectionItem item = (ConnectionItem) itemTemp;
    // if (connection.eIsProxy()) {
    // ResourceSet resourceSet = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider()
    // .getResourceManager().resourceSet;
    // connection = (Connection) EcoreUtil.resolve(connection, resourceSet);
    // }
    // if (ResourceHelper.getUUID(connection).equals(ResourceHelper.getUUID(item.getConnection()))) {
    // return (RepositoryNode) connNode;
    // }
    // } else if (itemTemp instanceof FolderItem) {
    // List<ConnectionItem> connItems = getConnectionItemsFromFolderItem((FolderItem) itemTemp);
    // for (ConnectionItem connItem : connItems) {
    // if (ResourceHelper.getUUID(connection).equals(ResourceHelper.getUUID(connItem.getConnection()))) {
    // return (RepositoryNode) connNode;
    // }
    // }
    // }
    // }
    // } else if (modelElement instanceof Pattern) {
    // Pattern pattern = (Pattern) modelElement;
    // List<IRepositoryNode> patternsNode = getPatternsRepositoryNodes(true);
    // for (IRepositoryNode patternNode : patternsNode) {
    // Item itemTemp = ((IRepositoryViewObject) patternNode.getObject()).getProperty().getItem();
    // if (itemTemp instanceof TDQPatternItem) {
    // TDQPatternItem item = (TDQPatternItem) itemTemp;
    // if (ResourceHelper.getUUID(pattern).equals(ResourceHelper.getUUID(item.getPattern()))) {
    // return (RepositoryNode) patternNode;
    // }
    // } else if (itemTemp instanceof FolderItem) {
    // List<TDQPatternItem> patternItems = getPatternsItemsFromFolderItem((FolderItem) itemTemp);
    // for (TDQPatternItem patternItem : patternItems) {
    // if (ResourceHelper.getUUID(pattern).equals(ResourceHelper.getUUID(patternItem.getPattern()))) {
    // return (RepositoryNode) patternNode;
    // }
    // }
    // }
    // }
    // } else if (modelElement instanceof IndicatorDefinition) {
    // IndicatorDefinition udi = (IndicatorDefinition) modelElement;
    // List<IRepositoryNode> udisNode = getUdisRepositoryNodes(true);
    // for (IRepositoryNode udiNode : udisNode) {
    // Item itemTemp = ((IRepositoryViewObject) udiNode.getObject()).getProperty().getItem();
    // if (itemTemp instanceof TDQIndicatorDefinitionItem) {
    // TDQIndicatorDefinitionItem item = (TDQIndicatorDefinitionItem) itemTemp;
    // if (ResourceHelper.getUUID(udi).equals(ResourceHelper.getUUID(item.getIndicatorDefinition()))) {
    // return (RepositoryNode) udiNode;
    // }
    // } else if (itemTemp instanceof TDQBusinessRuleItem) {
    // TDQBusinessRuleItem item = (TDQBusinessRuleItem) itemTemp;
    // if (ResourceHelper.getUUID(udi).equals(ResourceHelper.getUUID(item.getDqrule()))) {
    // return (RepositoryNode) udiNode;
    // }
    // } else if (itemTemp instanceof FolderItem) {
    // List<TDQIndicatorDefinitionItem> udiItems = getIndicatorItemsFromFolderItem((FolderItem) itemTemp);
    // for (TDQIndicatorDefinitionItem udiItem : udiItems) {
    // if (ResourceHelper.getUUID(udi).equals(ResourceHelper.getUUID(udiItem.getIndicatorDefinition()))) {
    // return (RepositoryNode) udiNode;
    // }
    // }
    // }
    // }
    // } else if (modelElement instanceof TdXmlElementType) {
    // TdXmlElementType xmlElementType = (TdXmlElementType) modelElement;
    // TdXmlSchema ownedDocument = xmlElementType.getOwnedDocument();
    // if (ownedDocument != null) {
    // RepositoryNode xmlSchemaNode = recursiveFind(ownedDocument);
    // if (xmlSchemaNode != null) {
    // return recursiveFindXmlElementType(xmlSchemaNode.getChildren(), xmlElementType);
    // }
    // }
    // } else if (modelElement instanceof TdXmlSchema) {
    // TdXmlSchema xmlSchema = (TdXmlSchema) modelElement;
    // EList<DataManager> dataManager = xmlSchema.getDataManager();
    // if (dataManager.size() > 0) {
    // RepositoryNode connNode = recursiveFind(dataManager.get(0));
    // if (connNode != null) {
    // for (IRepositoryNode xmlSchemaNode : connNode.getChildren()) {
    // if (xmlSchemaNode instanceof MDMSchemaRepNode) {
    // TdXmlSchema tdXmlSchemaOnUi = ((MDMSchemaRepNode) xmlSchemaNode).getTdXmlSchema();
    // if (ResourceHelper.getUUID(xmlSchema).equals(ResourceHelper.getUUID(tdXmlSchemaOnUi))) {
    // return (RepositoryNode) xmlSchemaNode;
    // }
    // }
    // }
    // }
    // }
    // }
    // return null;
    // }

    // /**
    // * get all the ConnectionItems from FolderItem (recursive).
    // *
    // * @param folderItem
    // * @return
    // */
    // private static List<ConnectionItem> getConnectionItemsFromFolderItem(FolderItem folderItem) {
    // List<ConnectionItem> list = new ArrayList<ConnectionItem>();
    // EList objs = folderItem.getChildren();
    // for (Object obj : objs) {
    // if (obj instanceof FolderItem) {
    // list.addAll(getConnectionItemsFromFolderItem((FolderItem) obj));
    // } else if (obj instanceof ConnectionItem) {
    // list.add((ConnectionItem) obj);
    // }
    // }
    // return list;
    // }
    //
    // private static List<TDQAnalysisItem> getAnalysisItemsFromFolderItem(FolderItem folderItem) {
    // List<TDQAnalysisItem> list = new ArrayList<TDQAnalysisItem>();
    // EList objs = folderItem.getChildren();
    // for (Object obj : objs) {
    // if (obj instanceof FolderItem) {
    // list.addAll(getAnalysisItemsFromFolderItem((FolderItem) obj));
    // } else if (obj instanceof TDQAnalysisItem) {
    // list.add((TDQAnalysisItem) obj);
    // }
    // }
    // return list;
    // }
    //
    // private static List<TDQReportItem> getReportItemsFromFolderItem(FolderItem folderItem) {
    // List<TDQReportItem> list = new ArrayList<TDQReportItem>();
    // EList objs = folderItem.getChildren();
    // for (Object obj : objs) {
    // if (obj instanceof FolderItem) {
    // list.addAll(getReportItemsFromFolderItem((FolderItem) obj));
    // } else if (obj instanceof TDQAnalysisItem) {
    // list.add((TDQReportItem) obj);
    // }
    // }
    // return list;
    // }
    //
    // private static List<TDQPatternItem> getPatternsItemsFromFolderItem(FolderItem folderItem) {
    // List<TDQPatternItem> list = new ArrayList<TDQPatternItem>();
    // EList objs = folderItem.getChildren();
    // for (Object obj : objs) {
    // if (obj instanceof FolderItem) {
    // list.addAll(getPatternsItemsFromFolderItem((FolderItem) obj));
    // } else if (obj instanceof TDQAnalysisItem) {
    // list.add((TDQPatternItem) obj);
    // }
    // }
    // return list;
    // }
    //
    // private static List<TDQIndicatorDefinitionItem> getIndicatorItemsFromFolderItem(FolderItem folderItem) {
    // List<TDQIndicatorDefinitionItem> list = new ArrayList<TDQIndicatorDefinitionItem>();
    // EList objs = folderItem.getChildren();
    // for (Object obj : objs) {
    // if (obj instanceof FolderItem) {
    // list.addAll(getIndicatorItemsFromFolderItem((FolderItem) obj));
    // } else if (obj instanceof TDQIndicatorDefinitionItem) {
    // list.add((TDQIndicatorDefinitionItem) obj);
    // }
    // }
    // return list;
    // }

    /**
     * ADD mzhao 15750 , build dq metadata tree, get connection root node. (note: include the metadata connection nodes
     * from the reference projects).
     *
     * @param withDeleted
     * @param isContainFileConnections, if true, will contain the file connections
     * @return
     */
    public static List<IRepositoryNode> getConnectionRepositoryNodes(boolean withDeleted,
            boolean isContainFileConnections) {
        List<IRepositoryNode> connNodes = new ArrayList<IRepositoryNode>();

        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            RepositoryNode node = getRootNode(ERepositoryObjectType.METADATA);
            if (node != null) {
                List<IRepositoryNode> childrens = node.getChildren(withDeleted);
                for (IRepositoryNode subNode : childrens) {
                    if (subNode instanceof DBConnectionFolderRepNode) {
                        connNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                    } else if (subNode instanceof DFConnectionFolderRepNode && isContainFileConnections) {
                        connNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                    }
                }
            }
        } else {
            // get all the metadata types from the all the projects including the reference project.
            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                DQRepositoryNode node = (DQRepositoryNode) getRootNode(ERepositoryObjectType.METADATA, true, project);
                if (node != null) {
                    List<IRepositoryNode> childrens = node.getChildren(withDeleted);
                    for (IRepositoryNode subNode : childrens) {
                        if (subNode instanceof DBConnectionFolderRepNode) {
                            connNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                        } else if (subNode instanceof DFConnectionFolderRepNode && isContainFileConnections) {
                            connNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                        }
                    }
                }
            }
        }
        return connNodes;
    }

    public static List<IRepositoryNode> getDBConnectionRepositoryNodes(boolean withDeleted) {
        RepositoryNode node = getRootNode(ERepositoryObjectType.METADATA);
        List<IRepositoryNode> connNodes = new ArrayList<IRepositoryNode>();
        if (node != null) {
            List<IRepositoryNode> childrens = node.getChildren();
            for (IRepositoryNode subNode : childrens) {
                if (subNode instanceof DBConnectionFolderRepNode) {
                    connNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                }
            }
        }
        return connNodes;
    }

    public static IRepositoryNode getMetadataFolderNode(EResourceConstant folderConstant) {
        return getMetadataFolderNode(folderConstant, ProjectManager.getInstance().getCurrentProject());
    }

    /**
     *
     * zshen Comment method "getRepositoryFolderNode".
     *
     * @param folderConstant
     * @return one RepositoryFolderNode which corresponding to the value of folderConstant
     */
    public static IRepositoryNode getMetadataFolderNode(EResourceConstant folderConstant,
            org.talend.core.model.general.Project project) {
        String[] folderPathArray = folderConstant.getPath().split("/");//$NON-NLS-1$
        if (folderPathArray.length <= 0) {
            return null;
        }
        IRepositoryNode node = getRootNode(ERepositoryObjectType.METADATA, true, project);
        if (node != null) {
            for (int i = 1; folderPathArray.length > i; i++) {
                for (IRepositoryNode childNode : node.getChildren()) {
                    if (childNode.getObject().getLabel().equalsIgnoreCase(folderPathArray[i])) {
                        node = childNode;
                        break;
                    }
                }
            }
        }
        return node;
    }

    public static IRepositoryNode getDataProfilingFolderNode(EResourceConstant folderConstant) {
        return getDataProfilingFolderNode(folderConstant, ProjectManager.getInstance().getCurrentProject());
    }

    public static IRepositoryNode getDataProfilingFolderNode(EResourceConstant folderConstant,
            org.talend.core.model.general.Project project) {
        String[] folderPathArray = folderConstant.getPath().split("/");//$NON-NLS-1$
        if (folderPathArray.length <= 0) {
            return null;
        }

        IRepositoryNode node = getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true, project);
        if (node != null) {
            for (int i = 1; folderPathArray.length > i; i++) {
                for (IRepositoryNode childNode : node.getChildren()) {
                    if (childNode.getObject().getLabel().equalsIgnoreCase(folderPathArray[i])) {
                        node = childNode;
                        break;
                    }
                }
            }
        }
        return node;
    }

    public static IRepositoryNode getLibrariesFolderNode(EResourceConstant folderConstant) {
        return getLibrariesFolderNode(folderConstant, ProjectManager.getInstance().getCurrentProject());
    }

    public static IRepositoryNode getLibrariesFolderNode(EResourceConstant folderConstant,
            org.talend.core.model.general.Project project) {
        String[] folderPathArray = folderConstant.getPath().split("/");//$NON-NLS-1$
        if (folderPathArray.length <= 0) {
            return null;
        }

        IRepositoryNode node = getRootNode(ERepositoryObjectType.TDQ_LIBRARIES, true, project);
        if (node != null) {
            for (int i = 1; folderPathArray.length > i; i++) {
                for (IRepositoryNode childNode : node.getChildren(false)) {
                    if (childNode.getObject().getLabel().equalsIgnoreCase(folderPathArray[i])) {
                        node = childNode;
                        break;
                    }
                }
            }
        }
        return node;
    }

    public static List<IRepositoryNode> getDataProfilingRepositoryNodes(boolean withDeleted) {
        RepositoryNode node = getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING);// t.DATA_PROFILING.getName());
        List<IRepositoryNode> dataProfilingNodes = new ArrayList<IRepositoryNode>();
        if (node != null) {
            List<IRepositoryNode> childrens = node.getChildren();
            for (IRepositoryNode subNode : childrens) {
                if (subNode instanceof AnalysisFolderRepNode || subNode instanceof ReportFolderRepNode) {
                    dataProfilingNodes.addAll(getModelElementFromFolder(subNode, withDeleted));
                }
            }
        }
        return dataProfilingNodes;
    }

    /**
     * NOTE: this method only can get pattern nodes in local project, not include the nodes in ref project.
     * 
     * @param withDeleted
     * @return
     */
    public static List<IRepositoryNode> getPatternNodes(boolean withDeleted) {
        RepositoryNode node = getRootNode(ERepositoryObjectType.TDQ_LIBRARIES);// .LIBRARIES.getName());
        List<IRepositoryNode> patternsNodes = new ArrayList<IRepositoryNode>();
        if (node != null) {
            List<IRepositoryNode> childrens = node.getChildren();
            for (IRepositoryNode subNode : childrens) {
                if (EResourceConstant.PATTERNS.getName().equals((subNode.getObject().getLabel()))) {
                    List<IRepositoryNode> subChildren = subNode.getChildren();
                    for (IRepositoryNode patternsNode : subChildren) {
                        if (patternsNode instanceof PatternRegexFolderRepNode
                                || patternsNode instanceof PatternSqlFolderRepNode) {
                            patternsNodes.addAll(getModelElementFromFolder(patternsNode, withDeleted));
                        }
                    }
                    return patternsNodes;
                }
            }
        }
        return patternsNodes;
    }

    /**
     * DOC msjian Comment method "getPatternRepNodesWithRefProject".
     * 
     * @param inWhichProject
     * @return
     */
    private static List<PatternRepNode> getPatternNodesIncludeRefPro(Project inWhichProject) {
        List<PatternRepNode> patternRepNodes;
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            patternRepNodes = getPatternRepNodes(getLibrariesFolderNode(EResourceConstant.PATTERN_REGEX), true, true);
            patternRepNodes
                    .addAll(getPatternRepNodes(getLibrariesFolderNode(EResourceConstant.PATTERN_SQL), true, true));
        } else {
            patternRepNodes = getPatternRepNodes(
                    getLibrariesFolderNode(EResourceConstant.PATTERN_REGEX, inWhichProject), true, true);
            patternRepNodes
                    .addAll(getPatternRepNodes(getLibrariesFolderNode(EResourceConstant.PATTERN_SQL, inWhichProject),
                            true, true));
        }
        return patternRepNodes;
    }

    /**
     *
     * get all of UDI
     *
     * @param withDeleted care about which move into recycle bin
     * @return the list of UDI
     */
    public static List<IRepositoryNode> getUdisRepositoryNodes(boolean withDeleted) {
        RepositoryNode node = getRootNode(ERepositoryObjectType.TDQ_LIBRARIES);// EResourceConstant.LIBRARIES.getName());
        List<IRepositoryNode> udisNodes = new ArrayList<IRepositoryNode>();
        if (node != null) {
            List<IRepositoryNode> childrens = node.getChildren();
            for (IRepositoryNode subNode : childrens) {
                if (EResourceConstant.INDICATORS.getName().equals((subNode.getObject().getLabel()))) {
                    List<IRepositoryNode> subChildren = subNode.getChildren();
                    for (IRepositoryNode udisNode : subChildren) {
                        if (udisNode instanceof UserDefIndicatorFolderRepNode) {
                            udisNodes.addAll(getModelElementFromFolder(udisNode, withDeleted));
                        }
                    }

                }

            }
        }
        return udisNodes;
    }

    /**
     *
     * Add zshen 15750 get all the Connection Node from one folder node.
     *
     * @param folderNode any node
     * @return
     */
    private static List<IRepositoryNode> getModelElementFromFolder(IRepositoryNode folderNode, boolean withDelted) {
        // MOD qiongli 2011-2-23,bug 17588 ,add param withDeleted.
        List<IRepositoryNode> repositoryNodeList = new ArrayList<IRepositoryNode>();

        if (isFolderNode(folderNode.getType())) {
            for (IRepositoryNode thefolderNode : folderNode.getChildren(withDelted)) {
                repositoryNodeList.addAll(getModelElementFromFolder(thefolderNode, withDelted));
            }
        } else {
            repositoryNodeList.add(folderNode);
        }
        return repositoryNodeList;
    }

    /**
     *
     * Add zshen 15750 Decided whether one node is a Folder Node.
     *
     * @param nodeType the Type of nodes
     * @return
     */
    private static boolean isFolderNode(ENodeType nodeType) {

        switch (nodeType) {
        case SYSTEM_FOLDER:
        case SIMPLE_FOLDER:
            return true;
        default:
            return false;
        }
    }

    /**
     * get the metadata element from a node, if there have not metadata element, return null.
     *
     * @param repositoryNode
     * @return
     */
    public static ModelElement getMetadataElement(IRepositoryNode repositoryNode) {
        ISubRepositoryObject metadataObject = null;
        if (repositoryNode instanceof DBTableFolderRepNode || repositoryNode instanceof DBViewFolderRepNode
                || repositoryNode instanceof DBColumnFolderRepNode) {
            metadataObject = (ISubRepositoryObject) repositoryNode.getParent().getObject();
        } else if (repositoryNode.getObject() instanceof ISubRepositoryObject) {
            metadataObject = (ISubRepositoryObject) repositoryNode.getObject();
        }
        if (metadataObject != null) {
            return metadataObject.getModelElement();
        }
        if (repositoryNode instanceof DBConnectionRepNode || repositoryNode instanceof DFConnectionRepNode) {
            return ((ConnectionItem) repositoryNode.getObject().getProperty().getItem()).getConnection();
        }
        return null;
    }

    /**
     * DOC klliu Comment method "getRootNode".
     *
     * @return
     */
    public static RepositoryNode getRootNode(ERepositoryObjectType nodeName) {
        // MOD klliu bug 19138 In DI that can't find MDMConnectionFolderRepNode when create MDM connection
        // ~2011-03-22
        // FIXME: why we need this check?? we have the same two returns.
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            IWorkbenchPart activePart = activePage == null ? null : activePage.getActivePart();
            if (activePart != null && activePart.getTitle().equals(DI_REPOSITORY_NAME)) {
                return getRootNode(nodeName, true);
            }
        }
        // ~
        return getRootNode(nodeName, true);
    }

    public static RepositoryNode getRootNode(ERepositoryObjectType nodeType, boolean open,
            org.talend.core.model.general.Project project) {
        DQRepositoryNode node = null;
        FolderItem folderItem = ProxyRepositoryFactory.getInstance().getFolderItem(project, nodeType, Path.EMPTY);
        CommonViewer commonViewer = getDQCommonViewer(open);
        if (commonViewer != null) {
            TreeItem[] items = commonViewer.getTree().getItems();
            for (TreeItem item : items) {
                node = (DQRepositoryNode) item.getData();
                DQRepositoryNode matchNode = getMatchNode(project, node, folderItem);
                if (matchNode != null) {
                    return matchNode;
                }
            }
            log.error(Messages.getString("RepositoryNodeHelper.canNotFindRootNode") + nodeType.getLabel()); //$NON-NLS-1$
        }
        return node;
    }

    /**
     * DOC msjian Comment method "checkNodeMatch".
     *
     * @param project
     * @param node
     * @param folderItem
     */
    private static DQRepositoryNode getMatchNode(org.talend.core.model.general.Project project, DQRepositoryNode node,
            FolderItem folderItem) {
        // have reference project node, get its children' children
        if (ERepositoryObjectType.REFERENCED_PROJECTS.getLabel().equals(node.getProperties(EProperties.LABEL))) {
            for (IRepositoryNode node1 : node.getChildren()) {
                for (IRepositoryNode node2 : node1.getChildren()) {
                    DQRepositoryNode matchNode = getMatchNode(project, (DQRepositoryNode) node2, folderItem);
                    if (matchNode != null) {
                        return matchNode;
                    }
                }
            }
        } else {
            if (isNodeMatch(project, node, folderItem)) {
                return node;
            }
        }
        return null;
    }

    /**
     * DOC msjian Comment method "isNodeMatch".
     *
     * @param project
     * @param node
     * @param folderItem
     * @return boolean
     */
    private static boolean isNodeMatch(org.talend.core.model.general.Project project, DQRepositoryNode node,
            FolderItem folderItem) {
        // MOD qiongli 2011-2-16,bug 18642.filter recycle bin node.
        if (node.isBin()) {
            return false;
        }

        if (folderItem != null) {
            // sometimes the systemFolderItem will be recreate so we compare label attribute instead of compare
            // id(for example, SvnBaseRepositoryFactory#updateProject() line:1277)
            boolean isSysFolder =
                    ENodeType.SYSTEM_FOLDER == node.getType()
                            || FolderType.SYSTEM_FOLDER_LITERAL == folderItem.getType();
            if (isSysFolder) {
                String viewFolderLabel = folderItem.getProperty().getLabel();
                String folderLabel = node.getObject().getProperty().getLabel();
                if (viewFolderLabel.equals(folderLabel)) {
                    // the node's project is the same as the project parameter
                    if (project.getTechnicalLabel().equals(node.getProject().getEmfProject().getTechnicalLabel())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get the RepositoryNode according to the ERepositoryObjectType(the ERepositoryObjectType should not be
     * RECYCLE_BIN, because there doesn't exist a physical folder, so there will return null and log an error message;
     * use getRecycleBinRepNode() to get the RecycleBinNode).
     *
     * @param nodeType the node's ERepositoryObjectType
     * @param open if the DQView is not show, show it or not
     * @return
     */
    public static RepositoryNode getRootNode(ERepositoryObjectType nodeType, boolean open) {
        return getRootNode(nodeType, open, ProjectManager.getInstance().getCurrentProject());
    }

    /**
     *
     * get recycle bin node.
     *
     * @return
     * @deprecated use {@link #getRecycleBinRepNode()} instead
     */
    @Deprecated
    public static RepositoryNode getRecycleBinRootNode() {
        RepositoryNode node = null;
        CommonViewer commonViewer = getDQCommonViewer(true);
        if (commonViewer != null) {
            TreeItem[] items = commonViewer.getTree().getItems();
            for (TreeItem item : items) {
                node = (RepositoryNode) item.getData();
                if (node.isBin()) {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * DOC klliu 15750 Comment method "getDQRespositoryView".
     *
     * @return
     */
    public static CommonViewer getDQCommonViewer() {
        IViewPart part = null;
        CommonViewer commonViewer = null;
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
                part = activePage.findView(DQRESPOSITORYVIEW);
                if (part == null) {
                    return null;
                }
                CommonNavigator dqView = (CommonNavigator) part;
                commonViewer = dqView.getCommonViewer();
            }
        }
        return commonViewer;
    }

    public static boolean isOpenDQCommonViewer() {
        if (getDQCommonViewer() == null) {
            return false;
        }
        return true;

    }

    public static CommonViewer getDQCommonViewer(boolean open) {
        IViewPart part = null;
        CommonViewer commonViewer = null;
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
                part = activePage.findView(DQRESPOSITORYVIEW);
                if (part == null) {
                    if (open) {
                        try {
                            part = activePage.showView(DQRESPOSITORYVIEW);

                            // open detail view at the same time
                            IViewPart detailPart = activePage.findView(DQRESPOSITORY_DETAIL_VIEW);
                            if (detailPart == null) {
                                activePage.showView(DQRESPOSITORY_DETAIL_VIEW);
                            }
                        } catch (PartInitException e) {
                            e.printStackTrace();
                        }
                        if (part == null) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                CommonNavigator dqView = (CommonNavigator) part;
                commonViewer = dqView.getCommonViewer();
            }
        }
        return commonViewer;
    }

    public static boolean canOpenEditor(IRepositoryNode node) {
        return node instanceof AnalysisRepNode || node instanceof SysIndicatorDefinitionRepNode
                || node instanceof PatternRepNode || node instanceof JrxmlTempleteRepNode
                || node instanceof SourceFileRepNode || node instanceof RuleRepNode
                || node instanceof ReportRepNode
                || node instanceof ReportFileRepNode || node instanceof ReportAnalysisRepNode
                || node instanceof HiveOfHCConnectionNode;
    }

    public static List<IRepositoryNode> getNamedColumnSetNodes(IRepositoryNode node) {
        ArrayList<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        if (node instanceof DBCatalogRepNode || node instanceof DBSchemaRepNode || node instanceof DBTableFolderRepNode
                || node instanceof DBViewFolderRepNode) {
            List<IRepositoryNode> childrens = node.getChildren();
            for (IRepositoryNode children : childrens) {
                list.addAll(getNamedColumnSetNodes(children));
            }
        } else if (node instanceof DBTableRepNode || node instanceof DBViewRepNode) {
            list.add(node);
        }
        return list;
    }

    public static List<IRepositoryNode> getRepositoryNodeList(Object[] objs) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        for (Object obj : objs) {
            if (obj instanceof IRepositoryNode) {
                list.add((IRepositoryNode) obj);
            }
        }
        return list;
    }

    public static List<IRepositoryNode> getRepositoryNodeList(Object[] objs, List<ENodeType> nodeTypes) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        for (Object obj : objs) {
            if (obj instanceof IRepositoryNode) {
                IRepositoryNode node = (IRepositoryNode) obj;
                for (ENodeType nodeType : nodeTypes) {
                    if (nodeType.equals(node.getType())) {
                        list.add(node);
                        break;
                    }
                }
            }
        }
        return list;
    }

    /**
     * get the (Resource) ModelElement from a node(include: connection, analysis, business rule, indicator definition,
     * pattern, report), if there have not ModelElement return null.
     *
     * @param node
     * @return
     */
    public static ModelElement getResourceModelElement(IRepositoryNode node) {
        if (node != null) {
            ENodeType nodeType = node.getType();
            if (ENodeType.REPOSITORY_ELEMENT.equals(nodeType) || ENodeType.TDQ_REPOSITORY_ELEMENT.equals(nodeType)) {
                IRepositoryViewObject object = node.getObject();
                if (object != null) {
                    Property property = object.getProperty();
                    if (property != null) {
                        return getResourceModelElement(property.getItem());
                    }
                }
            }
        }
        return null;
    }

    /**
     * get the (Resource) ModelElement from a item(include: connection, analysis, business rule, indicator definition,
     * pattern, report), if there have not ModelElement return null.
     *
     * @param item
     * @return
     */
    public static ModelElement getResourceModelElement(Item item) {
        if (item == null) {
            return null;
        }
        if (item instanceof TDQItem) {
            if (item instanceof TDQAnalysisItem) {
                return ((TDQAnalysisItem) item).getAnalysis();
            } else if (item instanceof TDQBusinessRuleItem) {
                return ((TDQBusinessRuleItem) item).getDqrule();
            } else if (item instanceof TDQIndicatorDefinitionItem) {
                return ((TDQIndicatorDefinitionItem) item).getIndicatorDefinition();
            } else if (item instanceof TDQPatternItem) {
                return ((TDQPatternItem) item).getPattern();
            } else if (item instanceof TDQReportItem) {
                return ((TDQReportItem) item).getReport();
            } else if (item instanceof TDQMatchRuleItem) {
                return ((TDQMatchRuleItem) item).getMatchRule();
            }
        } else if (item instanceof ConnectionItem) {
            return ((ConnectionItem) item).getConnection();
        }
        return null;
    }

    /**
     * get the (Sub) ModelElement from a node(include: catalog, schema, table, view, column, xmlSchema, xmlElement), if
     * there have not ModelElement return null.
     *
     * @param node
     * @return
     */
    public static ModelElement getSubModelElement(IRepositoryNode node) {
        if (node != null) {
            if (node instanceof DBCatalogRepNode) {
                return ((DBCatalogRepNode) node).getCatalog();
            } else if (node instanceof DBSchemaRepNode) {
                return ((DBSchemaRepNode) node).getSchema();
            } else if (node instanceof DBTableRepNode) {
                return ((DBTableRepNode) node).getTdTable();
            } else if (node instanceof DBViewRepNode) {
                return ((DBViewRepNode) node).getTdView();
            } else if (node instanceof DBColumnRepNode) {
                return ((DBColumnRepNode) node).getTdColumn();
            } else if (node instanceof DFColumnRepNode) {
                return ((DFColumnRepNode) node).getMetadataColumn();
            } else if (node instanceof DFTableRepNode) {
                return ((DFTableRepNode) node).getMetadataTable();
            }
        }
        return null;
    }

    /**
     * get ModelElement from IRepositoryNode, if there no ModelElement, return null.
     *
     * @param node
     * @return
     */
    public static ModelElement getModelElementFromRepositoryNode(IRepositoryNode node) {
        ModelElement metadataElement = getMetadataElement(node);
        if (metadataElement != null) {
            return metadataElement;
        } else {
            metadataElement = getResourceModelElement(node);
            if (metadataElement != null) {
                return metadataElement;
            } else {
                metadataElement = getSubModelElement(node);
                if (metadataElement != null) {
                    return metadataElement;
                }
            }
        }
        return null;
    }

    /**
     * judge whether the node has been delete
     */
    public static boolean isStateDeleted(Object node) {
        if (node instanceof IRepositoryNode) {
            return isStateDeleted((IRepositoryNode) node);
        }
        return false;
    }

    /**
     *
     * if logical delete state is true .
     *
     * @param node
     * @return
     */
    public static boolean isStateDeleted(IRepositoryNode node) {
        final IRepositoryViewObject viewObject = node.getObject();
        final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        // TDQ's ISubRepositoryObject will return a null when call getAbstractMetadataObject()
        if (viewObject instanceof ISubRepositoryObject) {
            ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) viewObject;
            if (subRepositoryObject.getAbstractMetadataObject() == null) {
                return false;
            }
        }

        if (node instanceof ReportAnalysisRepNode || node instanceof ReportFileRepNode) {
            return false;
        } else {
            if (viewObject != null && factory.getStatus(viewObject) == ERepositoryStatus.DELETED) {
                return true;
            }
        }

        return false;
    }

    /**
     * find the RepositoryNode according to the ModelElement's uuid.
     *
     * @param uuid
     * @return a RepositoryNode or null
     * @deprecated this method is too slow, please use recursiveFindByUuid(String uuid, List<IRepositoryNode> nodes)
     * instead
     */
    @Deprecated
    public static RepositoryNode recursiveFindByUuid(String uuid) {
        return recursiveFind(uuid, getTdqRootNodes());
    }

    /**
     * find the RepositoryNode according to the ModelElement's uuid which be included in the nodes and those children.
     *
     * @param uuid
     * @param nodes
     * @return a RepositoryNode or null
     */
    public static RepositoryNode recursiveFindByUuid(String uuid, List<IRepositoryNode> nodes) {
        if (uuid != null && nodes != null) {
            for (IRepositoryNode node : nodes) {
                ModelElement modelElement = getModelElementFromRepositoryNode(node);
                if (modelElement != null && uuid.equals(getUUID(modelElement))) {
                    return (RepositoryNode) node;
                } else {
                    if (needFindInChildren(node)) {
                        RepositoryNode recursiveFind = recursiveFindByUuid(uuid, node.getChildren());
                        if (recursiveFind != null) {
                            return recursiveFind;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * should to find ModelElement in the node's children or not.
     *
     * @param node
     * @return
     */
    private static boolean needFindInChildren(IRepositoryNode node) {
        boolean result = true;
        String exchangeFolderClassName = "org.talend.dataprofiler.core.ui.exchange.ExchangeFolderRepNode";//$NON-NLS-1$
        if (node instanceof AnalysisRepNode || node instanceof ReportRepNode
                || exchangeFolderClassName.equals(node.getClass().getName())
                || node instanceof SysIndicatorDefinitionRepNode || node instanceof PatternRepNode
                || node instanceof RuleRepNode || node instanceof SourceFileFolderRepNode
                || node instanceof RecycleBinRepNode || node instanceof DBColumnRepNode
                || node instanceof DFColumnRepNode || node instanceof JrxmlTempFolderRepNode) {
            result = false;
        }
        return result;
    }

    /**
     * find the RepositoryNode according to the nodeId.
     *
     * @param nodeId
     * @return a RepositoryNode or null
     */
    public static RepositoryNode recursiveFind(String nodeId) {
        return recursiveFind(nodeId, getTdqRootNodes());
    }

    /**
     * find the RepositoryNode according to the nodeId which be included in the nodes and those children..
     *
     * @param nodeId
     * @return a RepositoryNode or null
     */
    public static RepositoryNode recursiveFind(String nodeId, List<IRepositoryNode> nodes) {
        assert nodeId != null;
        assert nodes != null;
        for (IRepositoryNode node : nodes) {
            if (nodeId.equals(node.getId())) {
                return (RepositoryNode) node;
            } else {
                if (needFindInChildren(node)) {
                    RepositoryNode recursiveFind = recursiveFind(nodeId, node.getChildren());
                    if (recursiveFind != null) {
                        return recursiveFind;
                    }
                }
            }
        }
        return null;
    }

    public static List<IRepositoryNode> getTdqRootNodes() {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        list.add(getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true));
        list.add(getRootNode(ERepositoryObjectType.TDQ_LIBRARIES, true));
        list.add(getRootNode(ERepositoryObjectType.METADATA, true));
        return list;
    }

    /**
     *
     * Add qiongli:find all REPOSITORY_ELEMENT by folderNode.
     *
     * @param folderNode
     * @param withDelted
     * @return
     */
    public static List<IRepositoryNode> getRepositoryElementFromFolder(IRepositoryNode folderNode, boolean withDelted) {
        List<IRepositoryNode> elementNodes = new ArrayList<IRepositoryNode>();
        if (isFolderNode(folderNode.getType())) {
            for (IRepositoryNode subNode : folderNode.getChildren(withDelted)) {
                elementNodes.addAll(getRepositoryElementFromFolder(subNode, withDelted));
            }
        } else {
            elementNodes.add(folderNode);
        }
        return elementNodes;

    }

    public static String getLocker(RepositoryNode node) {
        if (node != null) {
            return getLocker((RepositoryViewObject) node.getObject());
        }
        return PluginConstant.EMPTY_STRING;
    }

    public static String getLocker(RepositoryViewObject viewObject) {
        if (viewObject != null) {
            Property property = viewObject.getProperty();
            if (property != null) {
                Item item = property.getItem();
                if (item != null) {
                    return ProxyRepositoryFactory.getInstance().getLockInfo(item).getUser();
                }
            }
        }
        return PluginConstant.EMPTY_STRING;
    }

    /**
     * get JrxmlTempleteRepNodes which under the parentNode.
     *
     * @param parentNode
     * @param recursive
     * @return
     */
    public static List<JrxmlTempleteRepNode> getJrxmlFileRepNodes(IRepositoryNode parentNode, boolean recursive) {
        List<JrxmlTempleteRepNode> result = new ArrayList<JrxmlTempleteRepNode>();
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode node : children) {
            if (node instanceof JrxmlTempleteRepNode) {
                result.add((JrxmlTempleteRepNode) node);
            } else if (node instanceof JrxmlTempFolderRepNode || node instanceof JrxmlTempSubFolderNode) {
                if (recursive) {
                    result.addAll(getJrxmlFileRepNodes(node, recursive));
                }
            }
        }
        return result;
    }

    /**
     * get SourceFileRepNodes which under the parentNode.
     *
     * @param parentNode
     * @param recursive
     * @return
     */
    public static List<SourceFileRepNode> getSourceFileRepNodes(IRepositoryNode parentNode, boolean recursive) {
        List<SourceFileRepNode> result = new ArrayList<SourceFileRepNode>();
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode node : children) {
            if (node instanceof SourceFileRepNode) {
                result.add((SourceFileRepNode) node);
            } else if (node instanceof SourceFileFolderRepNode || node instanceof SourceFileSubFolderNode) {
                if (recursive) {
                    result.addAll(getSourceFileRepNodes(node, recursive));
                }
            }
        }
        return result;
    }

    /**
     * get RepositoryNode which contains a ModelElment(include: Analysis, Report, IndicatorDefinition, Pattern, DqRule)
     * under the parentNode.
     *
     * @param parentNode
     * @param recursive
     * @return
     */
    public static List<RepositoryNode> getModelElementRepNodes(RepositoryNode parentNode, boolean recursive) {
        List<RepositoryNode> result = new ArrayList<RepositoryNode>();
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode node : children) {
            ModelElement modelElementFromRepositoryNode = RepositoryNodeHelper.getModelElementFromRepositoryNode(node);
            if (modelElementFromRepositoryNode != null) {
                result.add((RepositoryNode) node);
            } else {
                boolean isFolder = false;
                if (node instanceof AnalysisFolderRepNode) {
                    AnalysisFolderRepNode anaFolderRepNode = (AnalysisFolderRepNode) node;
                    isFolder = !anaFolderRepNode.isVirtualFolder();
                } else if (node instanceof ReportFolderRepNode) {
                    ReportFolderRepNode repFolderRepNode = (ReportFolderRepNode) node;
                    isFolder = !repFolderRepNode.isVirtualFolder();
                } else if (node instanceof UserDefIndicatorFolderRepNode || node instanceof PatternRegexFolderRepNode
                        || node instanceof PatternRegexSubFolderRepNode || node instanceof PatternSqlFolderRepNode
                        || node instanceof PatternSqlSubFolderRepNode || node instanceof RulesSQLFolderRepNode) {
                    isFolder = true;
                }
                if (isFolder && recursive) {
                    result.addAll(getModelElementRepNodes((RepositoryNode) node, recursive));
                }
            }
        }
        return result;
    }

    /**
     * get the ModelElement uuid list from RepositoryNode list.
     *
     * @param nodes
     * @return
     */
    public static List<String> getUuids(List<? extends IRepositoryNode> nodes) {
        List<String> result = new ArrayList<String>();
        for (IRepositoryNode node : nodes) {
            ModelElement metadataElement = RepositoryNodeHelper.getModelElementFromRepositoryNode(node);
            if (metadataElement != null) {
                String uuid = getUUID(metadataElement);
                if (uuid != null) {
                    result.add(uuid);
                }
            }
        }
        return result;
    }

    /**
     * get the ERepositoryObjectType of the RepositoryNode.
     *
     * @param node
     * @return
     */
    public static ERepositoryObjectType getERepositoryObjectType(RepositoryNode node) {

        return null;
    }

    /**
     * get the relative path of the RepositoryNode.
     *
     * @param node
     * @return
     */
    public static IPath getRelativePath(RepositoryNode node) {
        return null;
    }

    /**
     * judge the nodeList contains the node, the node must own a ModelElement.
     *
     * @param nodeList
     * @param node
     * @return
     */
    public static boolean containsModelElementNode(List<IRepositoryNode> nodeList, IRepositoryNode node) {
        if (nodeList == null || nodeList.size() == 0 || node == null) {
            return false;
        } else {
            ModelElement meNode = RepositoryNodeHelper.getModelElementFromRepositoryNode(node);
            if (meNode != null) {
                String uuid = getUUID(meNode);
                if (uuid != null) {
                    for (IRepositoryNode repNode : nodeList) {
                        ModelElement meRepNode = RepositoryNodeHelper.getModelElementFromRepositoryNode(repNode);
                        if (meRepNode != null) {
                            if (uuid.equals(getUUID(meRepNode))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static IRepositoryNode getParentNodeForColumnNode(IRepositoryNode node) {
        if (node instanceof DBColumnRepNode || node instanceof DFColumnRepNode) {
            return node.getParent().getParent();
        }
        return null;

    }

    /**
     *
     * DOC klliu Comment method "getTableFilter".
     *
     * @param catalog
     * @param schema
     * @return
     */
    public static String getTableFilter(Catalog catalog, Schema schema) {
        String tableFilter = null;
        if (catalog != null) {
            tableFilter = ColumnSetHelper.getTableFilter(catalog);
            return tableFilter;
        }
        if (schema != null) {
            tableFilter = ColumnSetHelper.getTableFilter(schema);
            return tableFilter;
        }
        return tableFilter;
    }

    /**
     *
     * DOC klliu Comment method "getViewFilter".
     *
     * @param catalog
     * @param schema
     * @return
     */
    public static String getViewFilter(Catalog catalog, Schema schema) {
        String viewFilter = null;
        if (catalog != null) {
            viewFilter = ColumnSetHelper.getViewFilter(catalog);
            return viewFilter;
        }
        if (schema != null) {
            viewFilter = ColumnSetHelper.getViewFilter(schema);
            return viewFilter;
        }
        return viewFilter;
    }

    /**
     *
     * DOC klliu Comment method "filterTables".
     *
     * @param tables
     * @param columnSetPattern
     * @return
     */
    public static List<TdTable> filterTables(List<TdTable> tables, String columnSetPattern) {
        String[] patterns = cleanPatterns(columnSetPattern.split(",")); //$NON-NLS-1$
        List<NamedColumnSet> filterMatchingColumnSets = filterMatchingColumnSets(tables, patterns);
        List<TdTable> filterTables = new ArrayList<TdTable>();
        for (NamedColumnSet columnSet : filterMatchingColumnSets) {
            TdTable table = (TdTable) columnSet;
            filterTables.add(table);
        }
        return filterTables;

    }

    /**
     *
     * DOC klliu Comment method "filterViews".
     *
     * @param views
     * @param columnSetPattern
     * @return
     */
    public static List<TdView> filterViews(List<TdView> views, String columnSetPattern) {
        String[] patterns = cleanPatterns(columnSetPattern.split(",")); //$NON-NLS-1$
        List<NamedColumnSet> filterMatchingColumnSets = filterMatchingColumnSets(views, patterns);
        List<TdView> filterViews = new ArrayList<TdView>();
        for (NamedColumnSet columnSet : filterMatchingColumnSets) {
            TdView view = (TdView) columnSet;
            filterViews.add(view);
        }
        return filterViews;

    }

    public static List<TdColumn> filterColumns(List<TdColumn> columns, String columnSetPattern) {
        String[] patterns = cleanPatterns(columnSetPattern.split(",")); //$NON-NLS-1$
        List<TdColumn> filterMatchingColumnSets = filterMatchingColumns(columns, patterns);
        List<TdColumn> filterColumns = new ArrayList<TdColumn>();
        for (TdColumn column : filterMatchingColumnSets) {
            TdColumn table = column;
            filterColumns.add(table);
        }
        return filterColumns;

    }

    /**
     * DOC klliu Comment method "filterMatchingColumns".
     *
     * @param columns
     * @param patterns
     * @return
     */
    private static List<TdColumn> filterMatchingColumns(List<TdColumn> columns, String[] patterns) {
        List<TdColumn> resetColumns = new ArrayList<TdColumn>();
        int size = 0;
        for (TdColumn t : columns) {
            for (String pattern : patterns) {
                String regex = pattern.replaceAll("%", ".*").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
                String name = t.getName().toLowerCase();
                // MOD gdbu 2011-1-13 TDQ-4129 Change the way of matching.
                if (isMatch(regex, name)) {
                    resetColumns.add(t);
                    size++;
                    if (size > 2000) {
                        return resetColumns;
                    }
                    break;
                }
            }
        }
        return resetColumns;
    }

    // Refactor: extract same judge filter match together, instead of using it separately which cause issues
    private static boolean isMatch(String regex, String name) {
        return java.util.regex.Pattern.compile(regex).matcher(name).find();
    }

    /**
     *
     * This method is used for filtering packages by patterns.
     *
     * @param dataPackage
     * @param packageFilter
     * @return
     */
    public static List<IRepositoryNode> filterPackages(List<IRepositoryNode> afterGlobalFilter, String packageFilter) {
        int size = 0;
        String[] patterns = cleanPatterns(packageFilter.split(",")); //$NON-NLS-1$
        List<IRepositoryNode> filterMatchingPackages = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode dbPackage : afterGlobalFilter) {
            for (String pattern : patterns) {
                String regex = pattern.replaceAll("%", ".*").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
                String name = dbPackage.getLabel().toLowerCase();
                if (isMatch(regex, name)) {
                    filterMatchingPackages.add(dbPackage);
                    size++;
                    if (size > 2000) {
                        return filterMatchingPackages;
                    }
                    break;
                }
            }
        }
        return filterMatchingPackages;
    }

    private static String[] cleanPatterns(String[] split) {
        ArrayList<String> ret = new ArrayList<String>();
        for (String s : split) {
            if (s != null && !"".equals(s) && !ret.contains(s)) { //$NON-NLS-1$
                ret.add(s);
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    private static <T extends NamedColumnSet> List<NamedColumnSet> filterMatchingColumnSets(List<T> columnSets,
            String[] patterns) {
        List<NamedColumnSet> retColumnSets = new ArrayList<NamedColumnSet>();
        int size = 0;
        for (NamedColumnSet t : columnSets) {
            for (String pattern : patterns) {
                String regex = pattern.replaceAll("%", ".*").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
                String name = t.getName().toLowerCase();
                if (isMatch(regex, name)) {
                    // if (name.matches(regex)) {
                    retColumnSets.add(t);
                    size++;
                    if (size > 2000) {
                        return retColumnSets;
                    }
                    break;
                }
            }
        }
        return retColumnSets;
    }

    public static List<IRepositoryNode> sortChildren(List<IRepositoryNode> dqNodes) {

        Object[] sortedChildren = RepositoryNodeHelper.sortChildren(dqNodes.toArray());
        List<IRepositoryNode> sortChildrenList = new ArrayList<IRepositoryNode>();
        for (Object obj : sortedChildren) {
            IRepositoryNode dqNode = (IRepositoryNode) obj;
            sortChildrenList.add(dqNode);
        }
        return sortChildrenList;
    }

    @SuppressWarnings("unchecked")
    private static Object[] sortChildren(Object[] objects) {
        if (objects == null || objects.length <= 1) {
            return objects;
        }
        Arrays.sort(objects, buildComparator());
        return objects;
    }

    @SuppressWarnings("rawtypes")
    private static Comparator buildComparator() {
        return new RepositoryNodeComparator();
    }

    /**
     * DOC gdbu Comment method "getFirstFilteredNode".
     *
     * @return
     */
    public static IRepositoryNode getFirstFilteredNode() {
        if (!allFilteredNodeList.isEmpty()) {
            for (IRepositoryNode iNode : allFilteredNodeList) {
                IRepositoryNode comparedNode = compareNodeLabelWithFilterStr(iNode);
                if (null != comparedNode) {
                    return comparedNode;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * DOC gdbu Comment method "getNextFilteredNode".
     *
     * @param repoNode
     * @return
     */
    public static IRepositoryNode getNextFilteredNode(IRepositoryNode repoNode) {
        if (!allFilteredNodeList.isEmpty()) {
            IRepositoryNode findFilteredNode = findNextFilteredNode(repoNode);
            if (null != findFilteredNode) {
                return findFilteredNode;
            } else {
                return repoNode;
            }
        } else {
            return null;
        }
    }

    private static IRepositoryNode findNextFilteredNode(IRepositoryNode dqNode) {
        boolean findGivenNode = false;
        for (int i = 0; i < allFilteredNodeList.size(); i++) {
            if (allFilteredNodeList.get(i).equals(dqNode) || findGivenNode) {
                if (!findGivenNode) {
                    findGivenNode = true;
                    continue;
                }
                // if (allFilteredNodeList.get(i).getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                // return allFilteredNodeList.get(i);
                // }
                IRepositoryNode comparedNode = compareNodeLabelWithFilterStr(allFilteredNodeList.get(i));
                if (null != comparedNode) {
                    return comparedNode;
                }
            }
        }
        return null;
    }

    /**
     * DOC gdbu Comment method "getPreviouFilteredNode".
     *
     * @param repoNode
     * @return
     */
    public static IRepositoryNode getPreviouFilteredNode(IRepositoryNode repoNode) {
        if (!allFilteredNodeList.isEmpty()) {
            IRepositoryNode findFilteredNode = findPreviouFilteredNode(repoNode);
            if (null != findFilteredNode) {
                return findFilteredNode;
            } else {
                return repoNode;
            }
        } else {
            return null;
        }
    }

    private static IRepositoryNode findPreviouFilteredNode(IRepositoryNode dqNode) {
        boolean findGivenNode = false;
        for (int i = allFilteredNodeList.size() - 1; i >= 0; i--) {
            if (allFilteredNodeList.get(i).equals(dqNode) || findGivenNode) {
                if (!findGivenNode) {
                    findGivenNode = true;
                    continue;
                }
                // if (allFilteredNodeList.get(i).getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                // return allFilteredNodeList.get(i);
                // }
                IRepositoryNode comparedNode = compareNodeLabelWithFilterStr(allFilteredNodeList.get(i));
                if (null != comparedNode) {
                    return comparedNode;
                }
            }
        }
        return null;
    }

    public static IRepositoryNode compareNodeLabelWithFilterStr(IRepositoryNode iNode) {

        if (iNode instanceof DBCatalogRepNode || iNode instanceof DBSchemaRepNode || iNode instanceof RecycleBinRepNode) {
            if (iNode.getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                return iNode;
            }
        } else {

            if (iNode.getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                return iNode;
            }
            if (null != iNode.getObject()) {
                if (iNode.getObject().getProperty().getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                    return iNode;
                }
            }
            if ((iNode instanceof DFColumnRepNode)
                    && iNode.getLabel().toLowerCase().contains(DQRepositoryNode.getFilterStr())) {
                return iNode;
            }
        }
        return null;
    }

    /**
     *
     * DOC gdbu Comment method "fillTreeList". Filter's entry method.
     *
     * @param monitor
     */
    public static void fillTreeList(IProgressMonitor monitor) {

        allFilteredNodeList.clear();

        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);

        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        list.add(getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true));
        list.add(getRootNode(ERepositoryObjectType.TDQ_LIBRARIES, true));
        list.add(getRootNode(ERepositoryObjectType.METADATA, true));
        list.add(getRecycleBinRepNode());
        // for the reference project NOT merge mode
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        List<Project> allReferencedProjects = ProjectManager.getInstance().getAllReferencedProjects();
        if (org.talend.core.PluginChecker.isRefProjectLoaded() && currentProject != null
                && allReferencedProjects.size() > 0) {
            if (!ProxyRepositoryManager.getInstance().isMergeRefProject()) {
                for (Project refProject : allReferencedProjects) {
                    list.add(getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true, refProject));
                    list.add(getRootNode(ERepositoryObjectType.TDQ_LIBRARIES, true, refProject));
                    list.add(getRootNode(ERepositoryObjectType.METADATA, true, refProject));
                }
            }
        }

        for (IRepositoryNode iRepositoryNode : list) {
            allFilteredNodeList.addAll(getTreeList(iRepositoryNode));
            if (null != monitor) {
                monitor.worked(2);
            }
        }
        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);
    }

    /**
     * ADD gdbu 2011-11-15 TDQ-3969
     *
     * DOC gdbu Comment method "regainRecycleBinFilteredNode". updates recycle bin node.
     */
    public static void regainRecycleBinFilteredNode() {
        List<IRepositoryNode> recycleBinTreeList = getRecycleBinFilteredNodes();
        // recycleBinTreeList contains some nodes the allFilteredNodeList haven't contain.
        allFilteredNodeList.removeAll(recycleBinTreeList);
        allFilteredNodeList.addAll(recycleBinTreeList);
    }

    /**
     * ADD gdbu 2011-11-15 TDQ-3969
     *
     * DOC gdbu Comment method "getRecycleBinFilteredNodes". This method could return a list contains recycle bin node
     * and all of it's children.
     *
     * @return
     */
    private static List<IRepositoryNode> getRecycleBinFilteredNodes() {
        return getTreeList(getRecycleBinRepNode());
    }

    /**
     * ADD gdbu 2011-11-17 TDQ-3969
     *
     * DOC gdbu Comment method "removeChildrenNodesWhenFiltering". When delete an element in the tree , also need delete
     * this element in filter-list.
     *
     * @param node
     * @return
     */
    public static List<IRepositoryNode> removeChildrenNodesWhenFiltering(IRepositoryNode node) {

        List<IRepositoryNode> removeNodes = new ArrayList<IRepositoryNode>();
        removeNodes.add(node);
        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);
        List<IRepositoryNode> children = node.getChildren();
        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);
        for (IRepositoryNode iRepositoryNode : children) {
            removeNodes.addAll(removeChildrenNodesWhenFiltering(iRepositoryNode));
        }
        getAllFilteredNodeList().remove(node);
        return removeNodes;
    }

    /**
     * ADD gdbu 2011-11-24 TDQ-4068 To get all children nodes.
     *
     * DOC gdbu Comment method "findRecycleBinNodeWhenFiltering".
     *
     * @param recycleBinNodes
     * @return
     */
    public static Collection<IRepositoryNode> findAllChildrenNodes(Collection<IRepositoryNode> recycleBinNodes) {
        Set<IRepositoryNode> allChindrenNode = new HashSet<IRepositoryNode>();
        allChindrenNode.addAll(recycleBinNodes);
        for (IRepositoryNode iRepositoryNode : recycleBinNodes) {
            allChindrenNode.addAll(findAllChildrenNodes(iRepositoryNode.getChildren()));
        }
        return allChindrenNode;
    }

    /**
     * if there exist noeds which have been filtered out when try to delete/empty RecycleBin, show dialog to the user,
     * let user to select going ahead or not.
     *
     * @param needToDeleteNodes
     * @param shownNodes
     * @return
     */
    public static boolean canDeleteWhenFiltering(Collection<IRepositoryNode> needToDeleteNodes,
            Collection<IRepositoryNode> shownNodes) {
        if (needToDeleteNodes.size() != shownNodes.size()) {
            // If some nodes is filtered out, ask the user whether to continue to delete.
            boolean openQuestion =
                    MessageDialog.openQuestion(null, Messages.getString("RepositoryNodeHelper.delete.title"),//$NON-NLS-1$
                            Messages.getString("RepositoryNodeHelper.ContainsFilteredNodes"));//$NON-NLS-1$
            return openQuestion;
        } else {
            // Haven't filtered out nodes , continue to empty Recycle Bin .
            return true;
        }
    }

    /**
     *
     * DOC gdbu Comment method "getTreeList".
     *
     * @param node
     * @return
     */
    private static List<IRepositoryNode> getTreeList(IRepositoryNode node) {
        List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();
        children.add(node);
        List<IRepositoryNode> childrenNode = node.getChildren();
        if (childrenNode.size() > 0) {
            for (IRepositoryNode iNode : sortChildren(childrenNode)) {
                children.addAll(getTreeList(iNode));
            }
        }
        return children;
    }

    public static IRepositoryNode getFilteredNode() {
        return filteredNode;
    }

    public static void setFilteredNode(IRepositoryNode fNode) {
        filteredNode = fNode;
    }

    public static List<IRepositoryNode> getAllFilteredNodeList() {
        return allFilteredNodeList;
    }

    // ADDED yyin 201204 TDQ-4977
    /**
     * get Connection Type to display for the conection combo list.
     *
     * @param node
     * @return
     */
    public static String getConnectionType(IRepositoryNode node) {
        return ((Connection) getModelElementFromRepositoryNode(node)).getConnectionTypeName();
    }

    public static String getConnectionType(DataManager dataManager) {
        return ((Connection) dataManager).getConnectionTypeName();
    }

    /**
     *
     * find the nearest system folder node by the given node(.eg.,give an AnalysisRepNode,will find the root node
     * AnalysisFolderNode).
     *
     * @param node
     * @return
     */
    public static RepositoryNode findNearestSystemFolderNode(RepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node.getType() == ENodeType.SYSTEM_FOLDER) {
            return node;
        }
        return findNearestSystemFolderNode(node.getParent());

    }

    /**
     * get the IFolder according to the RepositoryNode, if the RepositoryNode is not a folder, return null.
     *
     * @param node
     * @return an IFolder or null
     */
    public static IFolder getIFolder(IRepositoryNode node) {
        IFolder folder = null;
        if (node != null) {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            IRepositoryViewObject object = node.getObject();
            if (object instanceof Folder) {
                IPath path = getPath(node);
                folder =
                        root
                                .getFolder(new Path(currentProject.getTechnicalLabel() + IPath.SEPARATOR
                                        + path.toString()));
            }
        }
        return folder;
    }

    /**
     * get the IFile according to the RepositoryNode.
     *
     * @param node
     * @return an IFile or null
     */
    public static IFile getIFile(IRepositoryNode node) {
        IFile file = null;
        if (node != null) {
            try {
                ModelElement me = getModelElementFromRepositoryNode(node);
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                if (me != null) {
                    // TDQItem except TDQFileItem
                    URI uri = me.eResource().getURI();
                    String platformString = uri.toPlatformString(Boolean.TRUE);
                    if (platformString == null) {
                        platformString = uri.toFileString();
                    }
                    file = root.getFile(new Path(platformString));
                } else if (node.getObject() != null) {
                    // TDQFileItem
                    Property property = node.getObject().getProperty();
                    // when the node is force deleted
                    if (property != null) {
                        Item item = property.getItem();
                        URI uri = item.eResource().getURI();
                        String propPathStr = uri.toPlatformString(Boolean.TRUE);
                        if (propPathStr == null) {
                            propPathStr = uri.toFileString();
                        }
                        int lastIndexOf = propPathStr.lastIndexOf("."); //$NON-NLS-1$
                        String itemPathStr =
                                propPathStr.substring(0, lastIndexOf) + "." + getTDQFileItemExtension(item); //$NON-NLS-1$
                        file = root.getFile(new Path(itemPathStr));
                    }
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        return file;
    }

    /**
     * DOC xqliu Comment method "getFileExtension".
     *
     * @param item
     * @return
     */
    private static String getTDQFileItemExtension(Item item) {
        // set default file extension '.item'. of course it adpts to context node.
        String result = FactoriesUtil.ITEM_EXTENSION;
        if (item instanceof TDQSourceFileItem) {
            result = FactoriesUtil.SQL;
        } else if (item instanceof TDQJrxmlItem) {
            result = FactoriesUtil.JRXML;
        }
        return result;
    }

    /**
     * restore Connection which is corrupted.
     *
     * @param property
     */
    public static void restoreCorruptedConn(Property property) {
        if (!(property.getItem() instanceof ConnectionItem)) {
            return;
        }
        ConnectionItem connItem = (ConnectionItem) property.getItem();
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(connItem);
        // connection
        Connection createConnection = RepositoryObjectTypeHelper.CreateConnectionFromType(itemType);

        if (!(ConnectionImpl.class == connItem.getConnection().getClass())) {
            return;
        }
        connItem.setConnection(createConnection);
        try {

            FolderItem folderItem =
                    ProxyRepositoryFactory.getInstance().getFolderItem(
                            ProjectManager.getInstance().getCurrentProject(), itemType, Path.EMPTY);
            Object refreshFolderItem = refreshFolderItem(folderItem, property);
            if (refreshFolderItem == null) {
                log.debug("Can not replace the connection from it's FolderItem : " + connItem.eResource().getURI()); //$NON-NLS-1$
            }
            ProxyRepositoryFactory.getInstance().save(connItem);
            EMFSharedResources.getInstance().reloadResource(connItem.eResource().getURI());
        } catch (PersistenceException e) {
            log.error(e, e);
        }

    }

    /**
     * replace old Connection which contain in it.
     *
     * @param folderItem
     * @param property
     * @return which should be remove object. null mean that replace is faile.
     */
    private static Object refreshFolderItem(FolderItem folderItem, Property property) {
        if (folderItem == null) {
            return null;
        }
        EList<Object> children = folderItem.getChildren();
        Object removeOne = null;
        for (Object item : children) {
            if (item instanceof ConnectionItem) {
                if (((ConnectionItem) item).getProperty().getId().equals(property.getId())) {
                    removeOne = item;
                    break;
                }
            } else if (item instanceof FolderItem) {
                removeOne = refreshFolderItem(folderItem, property);
                if (removeOne != null) {
                    return removeOne;
                }
            }

        }
        children.remove(removeOne);
        children.add(property.getItem());
        return removeOne;
    }

    /**
     * if the IRepositoryNode is locked by current user, return true, else return false.
     *
     * @param node
     * @return
     * @deprecated use instead of
     * {@link org.talend.dq.helper.ProxyRepositoryManager#isLockByUserOwn(org.talend.core.model.properties.Item)}
     */
    @Deprecated
    public static boolean isLockByUser(IRepositoryNode node) {
        boolean isLock = false;
        if (node != null) {
            try {
                ProxyRepositoryFactory.getInstance().initialize();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

            IRepositoryViewObject objectToCopy = node.getObject();
            // added by hqzhang, update the propery since it has not been updated after initialize()
            objectToCopy.getProperty();
            if (ProxyRepositoryFactory.getInstance().getStatus(objectToCopy) == ERepositoryStatus.LOCK_BY_USER) {
                isLock = true;
            }
        }
        return isLock;
    }

    /**
     * if the ModelElement is locked by current user, return true, else return false.
     *
     * @param modelElement
     * @return
     * @deprecated use instead of
     * {@link org.talend.dq.helper.ProxyRepositoryManager#isLockByUserOwn(org.talend.core.model.properties.Item)}
     */
    @Deprecated
    public static boolean isLockByUser(ModelElement modelElement) {
        return isLockByUser(recursiveFind(modelElement));
    }

    /**
     * if the IRepositoryNode is locked by other user, return true, else return false.
     *
     * @param node
     * @return
     * @deprecated use instead of
     * {@link org.talend.dq.helper.ProxyRepositoryManager#isLockByOthers(org.talend.core.model.properties.Item)}
     */
    @Deprecated
    public static boolean isLockByOther(IRepositoryNode node) {
        boolean isLock = true;
        if (node != null) {
            try {
                ProxyRepositoryFactory.getInstance().initialize();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

            IRepositoryViewObject objectToCopy = node.getObject();
            // added by hqzhang, update the propery since it has not been updated after initialize()
            objectToCopy.getProperty();
            if (ProxyRepositoryFactory.getInstance().getStatus(objectToCopy) != ERepositoryStatus.LOCK_BY_OTHER) {
                isLock = false;
            }
        }
        return isLock;
    }

    /**
     * if the ModelElement is locked by other user, return true, else return false.
     *
     * @param modelElement
     * @return
     * @deprecated use instead of
     * {@link org.talend.dq.helper.ProxyRepositoryManager#isLockByOthers(org.talend.core.model.properties.Item)}
     */
    @Deprecated
    public static boolean isLockByOther(ModelElement modelElement) {
        return isLockByOther(recursiveFind(modelElement));
    }

    /**
     * plus the label +"_"+ version +"." + file extension
     *
     * @param node
     * @return
     */
    public static String getFileNameOfTheNode(IRepositoryNode node) {
        Property property = node.getObject().getProperty();
        String fullName = property.getLabel() + "_" + property.getVersion() + PluginConstant.DOT_STRING//$NON-NLS-1$
                + property.getItem().getFileExtension();

        return fullName;
    }

    /**
     *
     * Get selected names and split with "/". .e.g,onnection/catalog/schema/table.
     *
     * @param columnNode
     * @return
     */
    public static String getAnalyzeDataNames(IRepositoryNode columnNode) {

        ModelElement mod = RepositoryNodeHelper.getModelElementFromRepositoryNode(columnNode);
        if (mod == null) {
            return PluginConstant.EMPTY_STRING;
        }
        MetadataColumn metadataColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(mod);
        if (metadataColumn == null) {
            return PluginConstant.EMPTY_STRING;
        }
        List<String> nameLs = new ArrayList<String>();
        EObject eContainer = metadataColumn.eContainer();
        MetadataTable mdTable = SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(eContainer);
        if (mdTable != null) {
            nameLs.add(mdTable.getLabel());
            Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(mdTable);
            Connection conn = null;
            // DelimitedFile conn doesn't have Catalog and Schema.
            if (parentCatalogOrSchema == null) {
                conn = ConnectionHelper.getTdDataProvider(mdTable);
            } else {
                conn = ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);
                Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(parentCatalogOrSchema);
                Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(parentCatalogOrSchema);
                if (schema != null) {
                    nameLs.add(schema.getName());
                    catalog = CatalogHelper.getParentCatalog(schema);
                    if (catalog != null) {
                        nameLs.add(catalog.getName());
                    }
                } else if (catalog != null) {
                    nameLs.add(catalog.getName());
                }
            }
            if (conn != null) {
                nameLs.add(conn.getName());
            }
        }

        return sortNames(nameLs, PluginConstant.SLASH_STRING);
    }

    /**
     *
     * Sort the List Strings from back to front and split them with split.
     *
     * @param nameLs
     * @param splitStr
     * @return
     */
    private static String sortNames(List<String> nameLs, String splitStr) {
        StringBuffer strs = new StringBuffer();
        if (!nameLs.isEmpty()) {
            for (int i = nameLs.size() - 1; i >= 0; i--) {
                strs.append(nameLs.get(i));
                if (i != 0) {
                    strs.append(splitStr);
                }
            }
        }
        return strs.toString();

    }

    public static RepositoryNode getDBConnectionRootNode() {
        RepositoryNode metaRootNode = RepositoryNodeHelper.getRootNode(ERepositoryObjectType.METADATA);
        if (metaRootNode != null) {
            List<IRepositoryNode> childrens = metaRootNode.getChildren();
            for (IRepositoryNode subNode : childrens) {
                if (subNode instanceof DBConnectionFolderRepNode) {
                    return (RepositoryNode) subNode;
                }
            }
        }
        return null;
    }

    public static String getDisplayLabel(IRepositoryNode node) {
        if (node instanceof DQRepositoryNode) {
            if (node instanceof DBConnectionRepNode) {
                if (!isSupportedConnection(node)) {
                    return node.getObject().getLabel() + UNSUPPORTED;
                }
            }
            if (EResourceConstant.REFERENCED_PROJECT.getName().equals(node.getProperties(EProperties.LABEL))) {
                return (String) node.getProperties(EProperties.LABEL);
            }

            // change the TDQ'side system folder nodes display label only
            // fix TDQ-10466: for exchange node get object is null
            if (node.getObject() != null) {
                ENodeType type = node.getType();
                if (type != null && type == ENodeType.SYSTEM_FOLDER) {
                    String label = node.getObject().getLabel();
                    if (label != null) {
                        if (label.equals(EResourceConstant.ANALYSIS.getName())
                                || label.equals(EResourceConstant.REPORTS.getName())) {
                            return ((DQRepositoryNode) node).getDisplayTextWithProjectName();
                        } else {
                            return Messages.getString("RepositoryNodeHelper." + label.replaceAll(" ", "_"));
                        }
                    }
                }
            }
            return ((DQRepositoryNode) node).getDisplayTextWithProjectName();
        }

        return ""; //$NON-NLS-1$
    }

    public static boolean isSupportedConnection(IRepositoryNode repNode) {
        ERepositoryObjectType objectType = repNode.getObjectType();

        if (objectType == ERepositoryObjectType.METADATA_CONNECTIONS
                || ConnectionUtils.isTcompJdbc(objectType.getLabel())) {
            ConnectionItem connectionItem = null;
            try {
                connectionItem = (ConnectionItem) repNode.getObject().getProperty().getItem();
            } catch (Exception e) {
                log.warn(e);
                return false;
            }
            return MetadataConnectionUtils.isTDQSupportDBTemplate(connectionItem.getConnection());
        }

        return false;
    }

    /**
     * DOC msjian Comment method "setPropertiesForNode".
     *
     * @param node
     * @param eRepositoryObjectType
     */
    public static void setPropertiesForNode(DQRepositoryNode node, ERepositoryObjectType eRepositoryObjectType) {
        node.setProperties(EProperties.CONTENT_TYPE, eRepositoryObjectType);
        node.setProperties(EProperties.LABEL, eRepositoryObjectType);
    }

    /**
     * get the connection node display name in the connection combo list of analysis editor.
     *
     * @param conNode
     * @return displayName
     */
    public static String getAnalysisConComboDisplayName(IRepositoryNode conNode) {
        String displayName = conNode.getObject().getProperty().getDisplayName();
        // TDQ-10655: make the format of display is: label+(@reference project name)
        DQRepositoryNode dqRepositoryNode = (DQRepositoryNode) conNode;
        if (!dqRepositoryNode.getProject().isMainProject()) {
            displayName += dqRepositoryNode.getDisplayProjectName();
        }
        return displayName;
    }

    /**
     * get All the Column level Nodes.
     *
     * @param selectedNodes
     * @return
     */
    public static IRepositoryNode[] getAllColumnNodes(Object[] selectedNodes) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();

        if (selectedNodes == null || selectedNodes.length == 0) {
            return list.toArray(new IRepositoryNode[list.size()]);
        }

        Object firstElement = selectedNodes[0];
        if (firstElement instanceof IRepositoryNode) {
            IRepositoryNode repNode = (IRepositoryNode) firstElement;
            IRepositoryViewObject repViewObject = repNode.getObject();
            if (repViewObject instanceof MetadataColumnRepositoryObject
                    || repViewObject instanceof MetadataXmlElementType) {
                IRepositoryNode[] column = new IRepositoryNode[selectedNodes.length];
                for (int i = 0; i < selectedNodes.length; i++) {
                    column[i] = (IRepositoryNode) selectedNodes[i];
                }
                return column;
            } else if (repViewObject instanceof MetadataTableRepositoryObject) {
                for (Object currentObj : selectedNodes) {
                    IRepositoryNode columnSetNode = (IRepositoryNode) currentObj;
                    List<IRepositoryNode> children = columnSetNode.getChildren();
                    if (children.size() > 0) {
                        list.addAll(children.get(0).getChildren());
                    }
                }
                return list.toArray(new IRepositoryNode[list.size()]);
            }
        } else if (firstElement instanceof TdTable) {
            TdTable table = (TdTable) firstElement;
            EList<MetadataColumn> columns = table.getColumns();
            for (MetadataColumn column : columns) {
                RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(column);
                list.add(recursiveFind);
            }
            return list.toArray(new IRepositoryNode[list.size()]);
        } else if (firstElement instanceof WhereRuleChartDataEntity) {
            // ADD msjian 2012-2-9 TDQ-4470: get columns from the join conditions
            EList<JoinElement> joinConditions =
                    ((WhereRuleChartDataEntity) firstElement).getIndicator().getJoinConditions();
            if (joinConditions != null && joinConditions.size() > 0) {
                JoinElement joinElement = joinConditions.get(0);
                list.add(RepositoryNodeHelper.recursiveFind(joinElement.getColA()));
                list.add(RepositoryNodeHelper.recursiveFind(joinElement.getColB()));
                return list.toArray(new IRepositoryNode[list.size()]);
            }
            // TDQ-4470 ~
        } else if (firstElement instanceof TdView) {
            // Added yyin 20120522 TDQ-4945, support tdView
            TdView view = (TdView) firstElement;
            EList<MetadataColumn> columns = view.getColumns();
            for (MetadataColumn column : columns) {
                RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(column);
                list.add(recursiveFind);
            }
            return list.toArray(new IRepositoryNode[list.size()]);
        } // ~
        return null;
    }

    public static ColumnSet getColumnOwner(RepositoryNode node) {
        if (node == null) {
            return null;
        }
        MetadataColumnRepositoryObject columnObject = (MetadataColumnRepositoryObject) node.getObject();
        TdColumn column = ((TdColumn) columnObject.getTdColumn());

        if (column != null && column.eIsProxy()) {
            column = (TdColumn) EObjectHelper.resolveObject(column);
        }
        return ColumnHelper.getColumnOwnerAsColumnSet(column);
    }

    public static RepositoryNode getNodeListWithReferenceProject(ERepositoryObjectType type) {
        if (!ProxyRepositoryManager.getInstance().isLocalProject() && !ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                    .getCurrentProject());
            node.getChildren().clear();

            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                RepositoryNode metaRootNode = RepositoryNodeHelper.getRootNode(type, true, project);
                if (metaRootNode != null) {
                    node.getChildren().addAll(metaRootNode.getChildren());
                }
            }
            return node;
        } else {
            return RepositoryNodeHelper.getRootNode(type, true);
        }
    }
}
