// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.preview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.actions.CreatePatternAction;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.service.IDatabaseJobService;
import org.talend.dataprofiler.core.service.IJobService;
import org.talend.dataprofiler.core.ui.action.actions.CreateDuplicatesAnalysisAction;
import org.talend.dataprofiler.core.ui.dialog.ColumnsMapSelectionDialog;
import org.talend.dataprofiler.core.ui.utils.DrillDownUtils;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.EastAsiaPatternFreqIndicator;
import org.talend.dataquality.indicators.EastAsiaPatternLowFreqIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PatternFreqIndicator;
import org.talend.dataquality.indicators.PatternLowFreqIndicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.PossiblePhoneCountIndicator;
import org.talend.dataquality.indicators.SqlPatternMatchingIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.ValidPhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.util.ColumnsetSwitch;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dataquality.indicators.util.IndicatorsSwitch;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.analysis.explore.PatternExplorer;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.pattern.PatternTransformer;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC zqin class global comment. Detailled comment
 */
public final class ChartTableFactory {

    private ChartTableFactory() {
    }

    /**
     * add contextual menu for job generation.
     * 
     * @param menu
     * @param analysis
     * @param currentIndicator
     */
    public static void addJobGenerationMenu(final Menu menu, final Analysis analysis,
            final Indicator currentIndicator) {
        if (ProxyRepositoryManager.getInstance().isReadOnly()) {
            return;
        }
        Connection tdDataProvider = (Connection) analysis.getContext().getConnection();
        boolean isDelimitedFileAnalysis = ConnectionUtils.isDelimitedFileConnection(tdDataProvider);
        boolean isHiveConnection = ConnectionHelper.isHive(tdDataProvider);
        boolean isVertica = ConnectionHelper.isVertica(tdDataProvider);

        if (PluginChecker.isTDCPLoaded() && !(isDelimitedFileAnalysis || isHiveConnection)) {
            final IDatabaseJobService service =
                    (IDatabaseJobService) GlobalServiceRegister.getDefault().getService(IJobService.class);
            if (service != null) {
                service.setIndicator(currentIndicator);
                service.setAnalysis(analysis);
                MenuItem item = null;
                if (isDUDIndicator(currentIndicator)
                        && AnalysisType.COLUMN_SET != analysis.getParameters().getAnalysisType()) {
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(DefaultMessagesImpl.getString("ChartTableFactory.RemoveDuplicate")); //$NON-NLS-1$
                } else if (isPatternMatchingIndicator(currentIndicator) && !isVertica) {
                    // TDQ--8864 forbidden the genarate job menu for SQL pattern, remove this after TDQ-8875 is DONE
                    if (!isSQLPatternMatchingIndicator(currentIndicator)) {
                        item = new MenuItem(menu, SWT.PUSH);
                        item.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.generateJob"));//$NON-NLS-1$
                    }
                } else if (isAllMatchIndicator(currentIndicator)) {
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(DefaultMessagesImpl.getString("ChartTableFactory.gen_etl_job_row")); //$NON-NLS-1$
                } else if (isPhonseNumberIndicator(currentIndicator)) {
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(DefaultMessagesImpl.getString("ChartTableFactory.gen_std_phone_job")); //$NON-NLS-1$
                } else if (isDqRule(currentIndicator)) {
                    item = new MenuItem(menu, SWT.PUSH);
                    item.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.generateJob"));//$NON-NLS-1$
                }

                if (item != null) {
                    item.setImage(ImageLib.getImage(ImageLib.ICON_PROCESS));
                    item.addSelectionListener(new SelectionAdapter() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            service.executeJob();
                        }
                    });
                }
            }
        }
    }

    public static void addMenuAndTip(final TableViewer tbViewer, final IDataExplorer explorer,
            final Analysis analysis) {
        final Table table = tbViewer.getTable();
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                // MOD xqliu 2009-05-11 bug 6561
                if (table.getMenu() != null) {
                    table.getMenu().setVisible(false);
                }

                if (e.button == 3) {

                    StructuredSelection selection = (StructuredSelection) tbViewer.getSelection();
                    final ChartDataEntity dataEntity = (ChartDataEntity) selection.getFirstElement();
                    final Indicator indicator = dataEntity != null ? dataEntity.getIndicator() : null;
                    Boolean readOnly = ProxyRepositoryManager.getInstance().isReadOnly();

                    if (indicator != null && dataEntity != null) {
                        Menu menu = new Menu(table.getShell(), SWT.POP_UP);
                        table.setMenu(menu);

                        MenuItemEntity[] itemEntities =
                                ChartTableMenuGenerator.generate(explorer, analysis, dataEntity);

                        if (ExecutionLanguage.SQL == analysis.getParameters().getExecutionLanguage()) {
                            boolean showExtraMenu = false;
                            for (final MenuItemEntity itemEntity : itemEntities) {
                                MenuItem item = new MenuItem(menu, SWT.PUSH);
                                item.setText(itemEntity.geti18nLabel());
                                item.setImage(itemEntity.getIcon());
                                item.addSelectionListener(new SelectionAdapter() {

                                    @Override
                                    public void widgetSelected(SelectionEvent e1) {
                                        // TDQ-8637 pop a message when it is pattern and no implemnt Regex function in
                                        // DBMSLanguage.
                                        if (isPatternMatchingIndicator(indicator) && !((PatternExplorer) explorer)
                                                .isImplementRegexFunction(itemEntity.getLabel())) {
                                            MessageDialog
                                                    .openInformation(null, itemEntity.getLabel(), DefaultMessagesImpl
                                                            .getString("ChartTableFactory.NoSupportPatternTeradata"));//$NON-NLS-1$
                                            return;
                                        }

                                        String query = itemEntity.getQuery();
                                        String editorName = indicator.getName();
                                        Connection tdDataProvider = (Connection) analysis.getContext().getConnection();
                                        SqlExplorerUtils.getDefault().runInDQViewer(tdDataProvider, query, editorName);
                                    }
                                });

                                // ADD msjian 2012-2-9 TDQ-4470: add the create column analysis menu using the join
                                // condition columns
                                if (IndicatorHelper.isWhereRuleIndicator(indicator)) {
                                    // MOD yyin 20121126 TDQ-6477,show the menu only when Join condition exists
                                    WhereRuleIndicator ind = (WhereRuleIndicator) indicator;
                                    EList<JoinElement> joinConditions = ind.getJoinConditions();
                                    if (joinConditions.size() > 0) {
                                        showExtraMenu = true;
                                    }
                                }
                                // TDQ-4470~

                                if (isPatternFrequencyIndicator(indicator)
                                        && !isEastAsiaPatternFrequencyIndicator(indicator)) {
                                    createMenuOfGenerateRegularPattern(analysis, menu, dataEntity);
                                }
                            }
                            // show extra menu to create simple analysis, help user to find the duplicated rows

                            if (showExtraMenu && !readOnly) {
                                MenuItem itemCreateWhereRule = new MenuItem(menu, SWT.PUSH);
                                itemCreateWhereRule
                                        .setText(DefaultMessagesImpl
                                                .getString("ChartTableFactory.JoinConditionColumnsAnalysis")); //$NON-NLS-1$
                                itemCreateWhereRule.addSelectionListener(new SelectionAdapter() {

                                    @Override
                                    public void widgetSelected(SelectionEvent e1) {
                                        final StructuredSelection selectionOne =
                                                (StructuredSelection) tbViewer.getSelection();
                                        // MOD xqliu 2012-05-11 TDQ-5314
                                        Object firstElement = selectionOne.getFirstElement();
                                        if (firstElement instanceof WhereRuleChartDataEntity) {
                                            // get the WhereRuleIndicator
                                            WhereRuleChartDataEntity wrChartDataEntity =
                                                    (WhereRuleChartDataEntity) firstElement;
                                            WhereRuleIndicator wrInd =
                                                    (WhereRuleIndicator) wrChartDataEntity.getIndicator();
                                            // run the CreateDuplicatesAnalysisAction
                                            CreateDuplicatesAnalysisAction action =
                                                    new CreateDuplicatesAnalysisAction(buildColumnsMap(wrInd));
                                            action.run();
                                        }
                                        // ~ TDQ-5314
                                    }

                                    /**
                                     * DOC xqliu Comment method "buildColumnsMap".
                                     *
                                     * @param wrInd
                                     * @return
                                     */
                                    private Map<ColumnSet, List<TdColumn>> buildColumnsMap(WhereRuleIndicator wrInd) {
                                        Map<ColumnSet, List<TdColumn>> map = new HashMap<ColumnSet, List<TdColumn>>();
                                        // get all columns from the WhereRuleIndicator
                                        List<TdColumn> columns = new ArrayList<TdColumn>();
                                        EList<JoinElement> joinConditions = wrInd.getJoinConditions();
                                        for (JoinElement joinElement : joinConditions) {
                                            // add left column
                                            TdColumn tempColumn = (TdColumn) joinElement.getColA();
                                            if (!columns.contains(tempColumn)) {
                                                columns.add(tempColumn);
                                            }
                                            // add right column
                                            tempColumn = (TdColumn) joinElement.getColB();
                                            if (!columns.contains(tempColumn)) {
                                                columns.add(tempColumn);
                                            }
                                        }
                                        // build the map
                                        for (TdColumn column : columns) {
                                            ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(column);
                                            List<TdColumn> list = map.get(columnSet);
                                            if (list == null) {
                                                list = new ArrayList<TdColumn>();
                                                map.put(columnSet, list);
                                            }
                                            list.add(column);
                                        }
                                        // get the user selected map
                                        return getUserSelectedMap(map);
                                    }

                                    /**
                                     * DOC xqliu Comment method "getUserSelectedMap".
                                     *
                                     * @param map
                                     * @return
                                     */
                                    private Map<ColumnSet, List<TdColumn>>
                                            getUserSelectedMap(Map<ColumnSet, List<TdColumn>> map) {
                                        Map<ColumnSet, List<TdColumn>> userMap =
                                                new HashMap<ColumnSet, List<TdColumn>>();
                                        // get the column nodes
                                        List<RepositoryNode> columnNodes = getColumnNodes(map);
                                        // get the connection node
                                        RepositoryNode rootNode = getConnectionNode(map);
                                        // show the dialog, let user select the columns
                                        if (!columnNodes.isEmpty() && rootNode != null) {
                                            ColumnsMapSelectionDialog dialog = new ColumnsMapSelectionDialog(null,
                                                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                                    DefaultMessagesImpl
                                                            .getString("ColumnsMapSelectionDialog.columnSelection"), //$NON-NLS-1$
                                                    columnNodes, rootNode, DefaultMessagesImpl
                                                            .getString("ColumnsMapSelectionDialog.columnSelections")); //$NON-NLS-1$
                                            dialog.setAllMap(map);
                                            if (dialog.open() == Window.OK) {
                                                userMap = dialog.getUserMap();
                                            }
                                        }
                                        return userMap;
                                    }

                                    /**
                                     * DOC xqliu Comment method "getConnectionNode".
                                     *
                                     * @param map
                                     * @return
                                     */
                                    private RepositoryNode getConnectionNode(Map<ColumnSet, List<TdColumn>> map) {
                                        RepositoryNode node = null;
                                        if (map != null && !map.isEmpty()) {
                                            Connection connection =
                                                    ConnectionHelper.getConnection(map.keySet().iterator().next());
                                            if (connection != null) {
                                                node = RepositoryNodeHelper.recursiveFind(connection);
                                            }
                                        }
                                        return node;
                                    }

                                    /**
                                     * DOC xqliu Comment method "getColumnNodes".
                                     *
                                     * @param map
                                     * @return
                                     */
                                    private List<RepositoryNode> getColumnNodes(Map<ColumnSet, List<TdColumn>> map) {
                                        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
                                        if (map != null && !map.isEmpty()) {
                                            List<TdColumn> columns = new ArrayList<TdColumn>();
                                            Set<ColumnSet> keySet = map.keySet();
                                            for (ColumnSet cs : keySet) {
                                                columns.addAll(map.get(cs));
                                            }
                                            if (!columns.isEmpty()) {
                                                for (TdColumn column : columns) {
                                                    nodes.add(RepositoryNodeHelper.recursiveFind(column));
                                                }
                                            }
                                        }
                                        return nodes;
                                    }
                                });
                            }
                        } else {

                            if (analysis.getParameters().isStoreData()) { // if allow drill down
                                if (indicator.isUsedMapDBMode()) {
                                    DrillDownUtils
                                            .createDrillDownMenuForMapDB(dataEntity, menu, itemEntities, analysis);
                                } else {
                                    DrillDownUtils.createDrillDownMenuForJava(dataEntity, menu, itemEntities, analysis);
                                }
                                if (isPatternFrequencyIndicator(indicator)
                                        && !isEastAsiaPatternFrequencyIndicator(indicator)) {
                                    for (final MenuItemEntity itemEntity : itemEntities) {
                                        createMenuOfGenerateRegularPattern(analysis, menu, dataEntity);
                                    }
                                }

                            }
                            // MOD by zshen feature 11574:add menu "Generate regular pattern" to date pattern
                            if (isDatePatternFrequencyIndicator(indicator) && !readOnly) {
                                final DatePatternFreqIndicator dateIndicator = (DatePatternFreqIndicator) indicator;
                                MenuItem itemCreatePatt = new MenuItem(menu, SWT.PUSH);
                                itemCreatePatt
                                        .setText(DefaultMessagesImpl
                                                .getString("ChartTableFactory.GenerateRegularPattern")); //$NON-NLS-1$
                                itemCreatePatt.setImage(ImageLib.getImage(ImageLib.PATTERN_REG));
                                itemCreatePatt.addSelectionListener(new SelectionAdapter() {

                                    @Override
                                    public void widgetSelected(SelectionEvent e1) {
                                        DbmsLanguage language = DbmsLanguageFactory.createDbmsLanguage(analysis);
                                        IFolder folder = ResourceManager.getPatternRegexFolder();
                                        String model = dataEntity.getLabel();
                                        String regex = dateIndicator.getRegex(model);
                                        new CreatePatternAction(folder, ExpressionType.REGEXP, "'" + regex + "'", //$NON-NLS-1$ //$NON-NLS-2$
                                                model == null ? "" : "match \"" + model + "\"", language.getDbmsName()) //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
                                                        .run();
                                    }

                                });
                            }
                        }

                        addJobGenerationMenu(menu, analysis, indicator);

                        // ~11574
                        menu.setVisible(true);
                    }
                }
            }

        });

        // add tooltip
        TableUtils.addTooltipForTable(table);
        TableUtils.addActionTooltip(table);
    }

    /**
     * DOC yyin Comment method "createMenuOfGenerateRegularPattern".
     * 
     * @param analysis
     * @param menu
     * @param itemEntity
     */
    public static void createMenuOfGenerateRegularPattern(final Analysis analysis, Menu menu,
            final ChartDataEntity dataEntity) {
        if (!ProxyRepositoryManager.getInstance().isReadOnly()) {
            MenuItem itemCreatePatt = new MenuItem(menu, SWT.PUSH);
            itemCreatePatt.setText(DefaultMessagesImpl.getString("ChartTableFactory.GenerateRegularPattern")); //$NON-NLS-1$
            itemCreatePatt.setImage(ImageLib.getImage(ImageLib.PATTERN_REG));
            itemCreatePatt.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e1) {
                    String query = dataEntity.getKey() == null ? dataEntity.getLabel() : dataEntity.getKey().toString();
                    createPattern(analysis, query);
                }
            });
        }
    }

    /**
     * DOC bZhou Comment method "createPattern".
     * 
     * @param analysis
     * @param itemEntity
     * @param pattTransformer
     */
    public static void createPattern(Analysis analysis, String query) {
        DbmsLanguage language = DbmsLanguageFactory.createDbmsLanguage(analysis);
        PatternTransformer pattTransformer = new PatternTransformer(language);
        String regex = pattTransformer.getRegexp(query);
        IFolder folder = ResourceManager.getPatternRegexFolder();
        new CreatePatternAction(folder, ExpressionType.REGEXP, "'" + regex + "'", language.getDbmsName()).run(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * DOC bZhou Comment method "isAllMatchIndicator".
     * 
     * @param indicator
     * @return
     */
    public static boolean isAllMatchIndicator(Indicator indicator) {
        ColumnsetSwitch<Indicator> iSwitch = new ColumnsetSwitch<Indicator>() {

            @Override
            public Indicator caseAllMatchIndicator(AllMatchIndicator object) {
                return object;
            }
        };
        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC bZhou Comment method "isDUDIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not Duplicated,Uniqure,Distinct indicator.
     */
    public static boolean isDUDIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator caseDuplicateCountIndicator(DuplicateCountIndicator object) {
                return object;
            };

            @Override
            public Indicator caseUniqueCountIndicator(UniqueCountIndicator object) {
                return object;
            };

            @Override
            public Indicator caseDistinctCountIndicator(DistinctCountIndicator object) {
                return object;
            };
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC bZhou Comment method "isPatternMatchingIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not pattern matching indicator.
     */
    public static boolean isPatternMatchingIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator casePatternMatchingIndicator(PatternMatchingIndicator object) {
                return object;
            }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    public static boolean isSQLPatternMatchingIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator caseSqlPatternMatchingIndicator(SqlPatternMatchingIndicator object) {
                return object;
            }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC zshen Comment method "isFrequenceIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not Frequence indicator.
     */

    public static boolean isFrequenceIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator caseFrequencyIndicator(FrequencyIndicator object) {
                return object;
            }
        };
        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC bZhou Comment method "isPatternFrequencyIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not pattern frequency indicator.
     */
    public static boolean isPatternFrequencyIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator casePatternFreqIndicator(PatternFreqIndicator object) {
                return object;
            }

            @Override
            public Indicator casePatternLowFreqIndicator(PatternLowFreqIndicator object) {
                return object;
            }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC bZhou Comment method "isPatternFrequencyIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not pattern frequency indicator.
     */
    public static boolean isEastAsiaPatternFrequencyIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.dataquality.indicators.util.IndicatorsSwitch#caseEastAsiaPatternFreqIndicator(org.talend.
             * dataquality.indicators.EastAsiaPatternFreqIndicator)
             */
            @Override
            public Indicator caseEastAsiaPatternFreqIndicator(EastAsiaPatternFreqIndicator object) {
                return object;
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.dataquality.indicators.util.IndicatorsSwitch#caseEastAsiaPatternLowFreqIndicator(org.talend
             * .dataquality.indicators.EastAsiaPatternLowFreqIndicator)
             */
            @Override
            public Indicator caseEastAsiaPatternLowFreqIndicator(EastAsiaPatternLowFreqIndicator object) {
                return object;
            }

        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC zshen Comment method "isDatePatternFrequencyIndicator".
     * 
     * @param indicator
     * @return false if the indicator is not Date pattern frequency indicator.
     */
    public static boolean isDatePatternFrequencyIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator caseDatePatternFreqIndicator(DatePatternFreqIndicator object) {
                return object;
            }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * Added yyin TDQ-4829 20120717 To add the new feature: generate job from DQ rule
     * 
     * @param indicator
     * @return
     */
    public static boolean isDqRule(Indicator indicator) {
        if (indicator == null || indicator.getAnalyzedElement() == null) {
            return false;
        }
        // only support 7 kinds of db: mysql, oracle with sid, oracle with service name, oracle oci,
        // postgressql,mssql,mssql2005/2008
        String[] supportDB = { EDatabaseTypeName.MYSQL.getDisplayName(), EDatabaseTypeName.PSQL.getDisplayName(),
                EDatabaseTypeName.ORACLEFORSID.getDisplayName(), EDatabaseTypeName.ORACLESN.getDisplayName(),
                EDatabaseTypeName.ORACLE_OCI.getDisplayName(), EDatabaseTypeName.MSSQL.getDisplayName(),
                EDatabaseTypeName.MSSQL05_08.getDisplayName(), EDatabaseTypeName.AMAZON_AURORA.getDisplayName() };
        TdTable table = SwitchHelpers.TABLE_SWITCH.doSwitch(indicator.getAnalyzedElement());
        if (table == null) {
            return false;
        }
        Connection tdDataProvider = TableHelper.getFirstConnection(table);
        if (tdDataProvider instanceof DatabaseConnection) {
            String type = ((DatabaseConnection) tdDataProvider).getDatabaseType();
            boolean isSupport = false;
            for (String support : supportDB) {
                if (support.equals(type)) {
                    isSupport = true;
                }
            }
            if (!isSupport) {
                return false;
            }
        }

        // RulesSwitch<DQRule> dqRulesSwitch = new RulesSwitch<DQRule>() {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public WhereRuleIndicator caseIndicator(Indicator object) {
                if (object instanceof WhereRuleIndicator) {
                    return (WhereRuleIndicator) object;
                }
                return null;
            }
            // @Override
            // public DQRule caseDQRule(DQRule object) {
            // return object;
            // }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

    /**
     * DOC Administrator Comment method "isPhonseNumberIndicator".
     * 
     * @param indicator
     * @return
     */
    public static boolean isPhonseNumberIndicator(Indicator indicator) {
        IndicatorsSwitch<Indicator> iSwitch = new IndicatorsSwitch<Indicator>() {

            @Override
            public Indicator casePossiblePhoneCountIndicator(PossiblePhoneCountIndicator object) {
                // TODO Auto-generated method stub
                return object;
            }

            @Override
            public Indicator caseValidPhoneCountIndicator(ValidPhoneCountIndicator object) {
                // TODO Auto-generated method stub
                return object;
            }

            @Override
            public Indicator caseWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator object) {
                // TODO Auto-generated method stub
                return object;
            }

            @Override
            public Indicator caseWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator object) {
                // TODO Auto-generated method stub
                return object;
            }

            @Override
            public Indicator caseWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator object) {
                // TODO Auto-generated method stub
                return object;
            }
        };

        return iSwitch.doSwitch(indicator) != null;
    }

}
