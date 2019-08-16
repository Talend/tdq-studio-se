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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.dqrule.DQRuleUtilities;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.action.actions.TdAddTaskAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.CreateColumnAnalysisAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.PreviewAction;
import org.talend.dataprofiler.core.ui.dialog.IndicatorCheckedTreeSelectionDialog;
import org.talend.dataprofiler.core.ui.dialog.composite.TooltipTree;
import org.talend.dataprofiler.core.ui.editor.AbstractAnalysisActionHandler;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.BusinessRuleAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.dqrules.BusinessRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.RespositoryDetailView;
import org.talend.dataprofiler.core.ui.views.TableViewerDND;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataprofiler.core.ui.wizard.indicator.TableIndicatorOptionsWizard;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.TextParameters;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class AnalysisTableTreeViewer extends AbstractTableDropTree {

    protected static Logger log = Logger.getLogger(AnalysisTableTreeViewer.class);

    public static final String VIEWER_KEY = "org.talend.dataprofiler.core.ui.editor.composite.AnalysisTableTreeViewer"; //$NON-NLS-1$

    private Composite parentComp;

    private BusinessRuleAnalysisDetailsPage masterPage;

    private TableIndicator[] tableIndicators;

    // ADD xqliu 2009-04-30 bug 6808
    private Map<Object, TreeItem> indicatorTreeItemMap;

    /**
     * DOC xqliu AnalysisTableTreeViewer constructor comment.
     *
     * @param parent
     */
    public AnalysisTableTreeViewer(Composite parent) {
        parentComp = parent;
        this.tree = createTree(parent);
        // ADD xqliu 2009-04-30 bug 6808
        indicatorTreeItemMap = new HashMap<Object, TreeItem>();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#getMasterPage()
     */
    @Override
    public AbstractAnalysisMetadataPage getMasterPage() {
        return masterPage;
    }

    /**
     * DOC xqliu AnalysisTableTreeViewer constructor comment.
     *
     * @param tree
     * @param tableMasterDetailsPage
     */
    public AnalysisTableTreeViewer(Composite parent, BusinessRuleAnalysisDetailsPage masterPage) {
        this(parent);
        this.masterPage = masterPage;
        this.setElements(masterPage.getCurrentTableIndicators());
        this.setDirty(false);
    }

    private Tree createTree(Composite parent) {
        final Tree newTree = new TooltipTree(parent, SWT.MULTI | SWT.BORDER) {

            @Override
            protected boolean isValidItem(TreeItem item) {
                if (item == null) {
                    return false;
                }
                Object itemData = item.getData(INDICATOR_UNIT_KEY);
                if (itemData != null) {
                    TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
                    if (indicatorUnit != null && !(indicatorUnit.getIndicator() instanceof CompositeIndicator)) {
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }
            }

            @Override
            protected String getItemTooltipText(TreeItem item) {
                String expCont = isExpressionNull(item);
                if (expCont == null) {
                    return DefaultMessagesImpl.getString("AnalysisTableTreeViewer.queryNotGen"); //$NON-NLS-1$
                }
                return expCont;
            }
        };
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(newTree);

        newTree.setHeaderVisible(true);
        createTreeItem(newTree, 190, "AnalysisTableTreeViewer.analyzedTables"); //$NON-NLS-1$
        createTreeItem(newTree, 80, "AnalysisTableTreeViewer.dqrule"); //$NON-NLS-1$
        createTreeItem(newTree, 80, "AnalysisColumnTreeViewer.operation"); //$NON-NLS-1$

        parent.layout();

        AbstractAnalysisActionHandler actionHandler = new AbstractAnalysisActionHandler(parent) {

            @Override
            protected void handleRemove() {
                removeSelectedElements(newTree);
            }

        };

        parent.setData(AbstractMetadataFormPage.ACTION_HANDLER, actionHandler);
        TableViewerDND.installDND(newTree);
        this.addTreeListener(newTree);
        newTree.setData(this);
        return newTree;
    }

    private void addTreeListener(final Tree tree) {
        tree.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TableTreeMenuProvider tableTreeMenuProvider = new TableTreeMenuProvider(tree);
                tableTreeMenuProvider.createTreeMenu();
                tableTreeMenuProvider.showDetailView(tree);
            }

        });

        tree.addTreeListener(treeAdapter);

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TreeItem[] treeSelection = tree.getSelection();

                if (treeSelection.length > 0) {
                    TreeItem item = treeSelection[0];
                    Object indicatorobj = item.getData(INDICATOR_UNIT_KEY);
                    Object tableobj = item.getData(TABLE_INDICATOR_KEY);
                    if (tableobj != null && indicatorobj == null) {
                        // open DQ Rule selector
                        showAddDQRuleDialog(item, (TableIndicator) tableobj);
                    } else if (tableobj != null && indicatorobj != null) {
                        // open indicator option wizard
                        openIndicatorOptionDialog(null, item);
                    }
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#getTheSuitedComposite(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public ExpandableComposite getTheSuitedComposite(SelectionEvent e) {
        Composite[] previewChartCompsites = masterPage.getPreviewChartCompsites();
        if (previewChartCompsites == null) {
            return null;
        }

        Object obj = e.item.getData(TABLE_INDICATOR_KEY);
        if (obj instanceof TableIndicator) {
            TableIndicator tableIndicator = (TableIndicator) obj;
            for (Composite comp : previewChartCompsites) {
                if (comp.getData() == tableIndicator) {
                    return (ExpandableComposite) comp;
                }
            }
        }

        return null;
    }

    public void setElements(final Object elements) {
        this.tree.dispose();
        this.tree = createTree(this.parentComp);
        tree.setData(VIEWER_KEY, this);
        this.tableIndicators = (TableIndicator[]) elements;
        addItemElements((TableIndicator[]) elements);
        // MOD mzhao 2009-05-5, bug 6587.
        updateBindConnection(masterPage, tableIndicators, tree);
    }

    private void addItemElements(final TableIndicator[] elements) {
        for (final TableIndicator tableIndicator : elements) {
            final TreeItem treeItem = new TreeItem(tree, SWT.NONE);
            if (tableIndicator.isTable()) {
                treeItem.setImage(ImageLib.getImage(ImageLib.TABLE));
            } else if (tableIndicator.isView()) {
                treeItem.setImage(ImageLib.getImage(ImageLib.VIEW));
            }

            ModelElement columnSet = tableIndicator.getColumnSet();
            if (columnSet.eIsProxy()) { // try to resolve the proxy
                columnSet = (ModelElement) EObjectHelper.resolveObject(columnSet);
            }
            String columnSetName = columnSet.getName();
            if (columnSetName == null) {
                columnSetName = DefaultMessagesImpl.getString("AnalysisTableTreeViewer.unknown"); //$NON-NLS-1$
            }
            treeItem.setText(0, columnSetName);
            treeItem.setData(TABLE_INDICATOR_KEY, tableIndicator);

            TreeEditor addDQRuleEditor = new TreeEditor(tree);
            Label addDQRuleLabl = createTreeItemLabel(tree, ImageLib.ADD_DQ, "AnalysisColumnTreeViewer.addDQRule"); //$NON-NLS-1$
            addDQRuleLabl.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    showAddDQRuleDialog(treeItem, tableIndicator);
                }

            });
            addDQRuleEditor.minimumWidth = addDQRuleLabl.getImage().getBounds().width;
            addDQRuleEditor.setEditor(addDQRuleLabl, treeItem, 1);

            TreeEditor delLabelEditor = new TreeEditor(tree);
            Label delLabel = createTreeItemLabel(tree, ImageLib.DELETE_ACTION, "AnalysisColumnTreeViewer.delete"); //$NON-NLS-1$
            delLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    deleteTableItems(tableIndicator);
                    if (treeItem.getParentItem() != null && treeItem.getParentItem().getData(INDICATOR_UNIT_KEY) != null) {
                        setElements(tableIndicators);
                    } else {
                        deleteIndicatorItems(tableIndicator);
                        removeItemBranch(treeItem);
                        indicatorTreeItemMap.remove(tableIndicator);
                    }
                    // MOD mzhao 2005-05-05 bug 6587.
                    // MOD mzhao 2009-06-8, bug 5887.
                    // updateBindConnection(masterPage, getTableIndicator(),
                    // tree);
                }
            });
            delLabelEditor.minimumWidth = delLabel.getImage().getBounds().width;
            delLabelEditor.horizontalAlignment = SWT.CENTER;
            delLabelEditor.setEditor(delLabel, treeItem, 2);
            treeItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { addDQRuleEditor, delLabelEditor });
            if (tableIndicator.hasIndicators()) {
                createIndicatorItems(treeItem, tableIndicator.getIndicatorUnits());
            }
            treeItem.setExpanded(true);
            // ADD xqliu 2009-04-30 bug 6808
            this.indicatorTreeItemMap.put(tableIndicator, treeItem);
        }
        this.setDirty(true);
    }

    /**
     * DOC xqliu Comment method "showAddDQRuleDialog".
     *
     * @param treeItem
     * @param tableIndicator
     */
    private void showAddDQRuleDialog(final TreeItem treeItem, final TableIndicator tableIndicator) {
        // MOD xqliu 2009-04-30 bug 6808
        IndicatorCheckedTreeSelectionDialog dialog = new IndicatorCheckedTreeSelectionDialog(null,
                new DQRepositoryViewLabelProvider(), new ResourceViewContentProvider());
        dialog.setInput(AnalysisUtils.getSelectDialogInputData(EResourceConstant.RULES_SQL));
        dialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                for (Object ruleNode : selection) {
                    if (ruleNode instanceof RuleRepNode) {
                        IndicatorDefinition findWhereRule = ((RuleRepNode) ruleNode).getRule();
                        boolean validStatus = TaggedValueHelper.getValidStatus(findWhereRule);
                        if (!validStatus) {
                            return new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, DefaultMessagesImpl
                                    .getString("AnalysisTableTreeViewer.chooseValidDQRules")); //$NON-NLS-1$
                        }
                    }
                }

                return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
            }

        });

        dialog.setContainerMode(true);
        dialog.setTitle(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.dqruleSelector")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.dqrules")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        dialog.create();
        // MOD xqliu 2009-04-30 bug 6808
        Object[] ownedWhereRuleNodes = getOwnedWhereRuleNodes(tableIndicator);
        dialog.setCheckedElements(ownedWhereRuleNodes);
        if (dialog.open() == Window.OK) {
            Object[] result = dialog.getResult();
            removeUncheckedWhereRuleIndicator(ownedWhereRuleNodes, result, tableIndicator);
            Object[] results = clearAddedResult(ownedWhereRuleNodes, result);
            for (Object obj : results) {
                if (obj instanceof RuleRepNode) {
                    RuleRepNode node = (RuleRepNode) obj;
                    TableIndicatorUnit addIndicatorUnit = DQRuleUtilities
                            .createIndicatorUnit(node, tableIndicator, getAnalysis());
                    if (addIndicatorUnit != null) {
                        createOneUnit(treeItem, addIndicatorUnit);
                        setDirty(true);
                    } else {
                        IndicatorDefinition whereRule = node.getRule();
                        MessageUI.openError(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.ErrorWhenAddWhereRule",//$NON-NLS-1$
                                whereRule.getName()));
                    }
                }
            }
        }
        // ~
    }

    /**
     * DOC xqliu Comment method "removeUncheckedWhereRuleIndicator". ADD xqliu 2009-04-30 bug 6808
     *
     * @param ownedWhereRuleNodes
     * @param results
     * @param tableIndicator
     */
    private void removeUncheckedWhereRuleIndicator(Object[] ownedWhereRuleNodes, Object[] results, TableIndicator tableIndicator) {
        ArrayList<Object> removeList = new ArrayList<Object>();
        for (Object node : ownedWhereRuleNodes) {
            boolean remove = true;
            for (Object result : results) {
                if (node.equals(result)) {
                    remove = false;
                    break;
                }
            }
            if (remove) {
                removeList.add(node);
            }
        }
        TableIndicatorUnit[] indicatorUnits = tableIndicator.getIndicatorUnits();
        for (TableIndicatorUnit unit : indicatorUnits) {
            IndicatorDefinition indicatorDefinition = unit.getIndicator().getIndicatorDefinition();
            if (indicatorDefinition instanceof WhereRule) {
                WhereRule wr = (WhereRule) indicatorDefinition;
                RuleRepNode recursiveFindNode = RepositoryNodeHelper.recursiveFindRuleSql(wr);
                for (Object obj : removeList) {
                    RuleRepNode node = (RuleRepNode) obj;
                    if (recursiveFindNode.equals(node)) {
                        // the order can not be changed
                        removeItemBranch(this.indicatorTreeItemMap.get(unit));
                        deleteIndicatorItems(tableIndicator, unit);
                        // ~the order can not be changed
                        break;
                    }
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "clearAddedResult". ADD xqliu 2009-04-30 bug 6808
     *
     * @param addedResults
     * @param result
     * @return
     */
    private Object[] clearAddedResult(Object[] addedResults, Object[] results) {
        ArrayList<Object> ret = new ArrayList<Object>();
        for (Object result : results) {
            boolean add = true;
            for (Object addedResult : addedResults) {
                if (result.equals(addedResult)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                ret.add(result);
            }
        }
        return ret.toArray();
    }

    /**
     * DOC xqliu Comment method "getOwnedWhereRuleFiles". ADD xqliu 2009-04-30 bug 6808
     *
     * @param tableIndicator
     * @param whereRuleFolder
     * @return
     */
    private Object[] getOwnedWhereRuleNodes(TableIndicator tableIndicator) {
        ArrayList<RuleRepNode> ret = new ArrayList<RuleRepNode>();
        Indicator[] indicators = tableIndicator.getIndicators();
        for (Indicator indicator : indicators) {
            if (IndicatorHelper.isWhereRuleIndicator(indicator)) {
                Object obj = indicator.getIndicatorDefinition();
                if (obj != null && obj instanceof WhereRule) {
                    WhereRule wr = (WhereRule) obj;
                    RuleRepNode recursiveFindRuleSql = RepositoryNodeHelper.recursiveFindRuleSql(wr);
                    if (recursiveFindRuleSql != null) {
                        ret.add(recursiveFindRuleSql);
                    }
                }
            }
        }
        return ret.toArray();
    }

    private void createIndicatorItems(TreeItem treeItem, TableIndicatorUnit[] indicatorUnits) {
        for (TableIndicatorUnit indicatorUnit : indicatorUnits) {
            createOneUnit(treeItem, indicatorUnit);
        }
    }

    public void createOneUnit(final TreeItem treeItem, TableIndicatorUnit indicatorUnit) {
        final TreeItem indicatorItem = new TreeItem(treeItem, SWT.NONE);
        final TableIndicatorUnit unit = indicatorUnit;
        IndicatorEnum type = indicatorUnit.getType();
        final IndicatorEnum indicatorEnum = type;
        indicatorItem.setData(TABLE_INDICATOR_KEY, treeItem.getData(TABLE_INDICATOR_KEY));
        indicatorItem.setData(INDICATOR_UNIT_KEY, unit);
        indicatorItem.setData(VIEWER_KEY, this);
        String label = indicatorUnit.getIndicatorName();
        label = label != null ? label : DefaultMessagesImpl.getString("AnalysisTableTreeViewer.unknownIndicator");//$NON-NLS-1$

        DQRepositoryNode recursiveFindNode =
                RepositoryNodeHelper.recursiveFind(indicatorUnit.getIndicator().getIndicatorDefinition());
        if (recursiveFindNode != null && !recursiveFindNode.getProject().isMainProject()) {
            label = label + recursiveFindNode.getDisplayProjectName();
        }

        if (IndicatorEnum.WhereRuleIndicatorEnum.compareTo(type) == 0) {
            indicatorItem.setImage(0, ImageLib.getImage(ImageLib.DQ_RULE));
        } else if (IndicatorEnum.RowCountIndicatorEnum.compareTo(type) == 0) {
            indicatorItem.setImage(0, ImageLib.getImage(ImageLib.IND_DEFINITION));
        }
        indicatorItem.setText(0, label);

        TreeEditor optionEditor = new TreeEditor(tree);
        final Label optionLabel = createTreeItemLabel(tree, ImageLib.OPTION, "AnalysisTableTreeViewer.options"); //$NON-NLS-1$
        optionLabel.setData(indicatorUnit);
        optionLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                boolean hasIndicatorParameters = openIndicatorOptionDialog(Display.getCurrent().getActiveShell(), indicatorItem);
                if (hasIndicatorParameters) {
                    optionLabel.setImage(ImageLib.getImage(ImageLib.INDICATOR_OPTION_CHECKED));
                }
            }

        });

        optionEditor.minimumWidth = optionLabel.getImage().getBounds().width;
        optionEditor.horizontalAlignment = SWT.CENTER;
        optionEditor.setEditor(optionLabel, indicatorItem, 1);

        TreeEditor delEditor = null;
        if (!(unit.getIndicator() instanceof RowCountIndicator)) {
            delEditor = new TreeEditor(tree);
            Label delLabel = createTreeItemLabel(tree, ImageLib.DELETE_ACTION, "AnalysisTableTreeViewer.delete"); //$NON-NLS-1$
            delLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    deleteIndicatorItems((TableIndicator) treeItem.getData(TABLE_INDICATOR_KEY), unit);
                    if (indicatorItem.getParentItem() != null
                            && indicatorItem.getParentItem().getData(INDICATOR_UNIT_KEY) != null) {
                        setElements(tableIndicators);
                    } else {
                        removeItemBranch(indicatorItem);
                        indicatorTreeItemMap.remove(unit);
                    }
                }

            });

            delEditor.minimumWidth = delLabel.getImage().getBounds().width;
            delEditor.horizontalAlignment = SWT.CENTER;
            delEditor.setEditor(delLabel, indicatorItem, 2);
        }

        if (delEditor == null) {
            indicatorItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { optionEditor });
        } else {
            indicatorItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { optionEditor, delEditor });
        }
        if (indicatorEnum != null && indicatorEnum.hasChildren()) {
            indicatorItem.setData(treeItem.getData(TABLE_INDICATOR_KEY));
            createIndicatorItems(indicatorItem, indicatorUnit.getChildren());
        }
        if (hasIndicatorParameters(indicatorUnit)) {
            optionLabel.setImage(ImageLib.getImage(ImageLib.INDICATOR_OPTION_CHECKED));
        }
        // ADD xqliu 2009-04-30 bug 6808
        this.indicatorTreeItemMap.put(unit, indicatorItem);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#openIndicatorOptionDialog(org.eclipse
     * .swt.widgets.Shell, org.eclipse.swt.widgets.TreeItem)
     */
    @Override
    public boolean openIndicatorOptionDialog(Shell shell, TreeItem indicatorItem) {
        if (isDirty()) {
            masterPage.doSave(null);
        }

        TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) indicatorItem.getData(INDICATOR_UNIT_KEY);
        if (FormEnum.isExsitingForm(indicatorUnit)) {
            TableIndicatorOptionsWizard wizard = new TableIndicatorOptionsWizard(indicatorUnit);
            String href = FormEnum.getFirstFormHelpHref(indicatorUnit);
            OpeningHelpWizardDialog optionDialog = new OpeningHelpWizardDialog(shell, wizard, href);
            optionDialog.create();
            if (Window.OK == optionDialog.open()) {
                setDirty(wizard.isDirty());
                return hasIndicatorParameters(indicatorUnit);
            }
        } else {
            openNoIndicatorOptionsMessageDialog(shell);
        }
        return false;
    }

    /**
     * DOC msjian Comment method "hasIndicatorParameters".
     *
     * @param indicatorUnit
     * @return
     */
    private boolean hasIndicatorParameters(TableIndicatorUnit indicatorUnit) {
        IndicatorParameters parameters = indicatorUnit.getIndicator().getParameters();
        if (parameters == null) {
            return false;
        }
        if (indicatorUnit.getIndicator() instanceof FrequencyIndicator) {
            return true;
        }

        TextParameters tParameter = parameters.getTextParameter();
        if (tParameter != null) {
            return true;
        }
        DateParameters dParameters = parameters.getDateParameters();
        if (dParameters != null) {
            return true;
        }

        Domain dataValidDomain = parameters.getDataValidDomain();
        if (dataValidDomain != null) {
            return true;
        }
        Domain indicatorValidDomain = parameters.getIndicatorValidDomain();
        if (indicatorValidDomain != null) {
            return true;
        }
        Domain bins = parameters.getBins();
        if (bins != null) {
            return true;
        }
        return false;
    }

    @Override
    protected void removeItemBranch(TreeItem item) {
        if (item == null) {
            return;
        }
        TreeEditor[] editors = (TreeEditor[]) item.getData(ITEM_EDITOR_KEY);
        if (editors != null) {
            for (TreeEditor editor : editors) {
                editor.getEditor().dispose();
                editor.dispose();
            }
        }
        if (item.getItemCount() == 0) {
            item.dispose();
            this.setDirty(true);
            return;
        }
        TreeItem[] items = item.getItems();
        for (TreeItem item2 : items) {
            removeItemBranch(item2);
            removeTreeItem(item2);
        }
        item.dispose();
        this.setDirty(true);
    }

    /**
     * DOC xqliu Comment method "removeTreeItem". ADD xqliu 2009-04-30 bug 6808
     *
     * @param treeItem
     */
    private void removeTreeItem(TreeItem treeItem) {
        ArrayList<Object> removeList = new ArrayList<Object>();
        Iterator<Object> iterator = this.indicatorTreeItemMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (treeItem.equals(indicatorTreeItemMap.get(obj))) {
                removeList.add(obj);
            }
        }
        for (Object obj : removeList) {
            this.indicatorTreeItemMap.remove(obj);
        }
    }

    private void deleteTableItems(TableIndicator deleteTableIndiciator) {
        TableIndicator[] remainIndicators = new TableIndicator[tableIndicators.length - 1];
        int i = 0;
        for (TableIndicator indicator : tableIndicators) {
            if (deleteTableIndiciator.equals(indicator)) {
                continue;
            } else {
                remainIndicators[i] = indicator;
                i++;
            }
        }
        this.tableIndicators = remainIndicators;
    }

    public TableIndicator[] getTableIndicator() {
        return this.tableIndicators;
    }

    /**
     * DOC xqliu Comment method "setInput".
     *
     * @param objs
     */
    @Override
    public void setInput(Object[] objs) {
        List<DBTableRepNode> tableNodeList = RepositoryNodeHelper.getTableNodeList(objs);
        List<TableIndicator> tableIndicatorList = new ArrayList<TableIndicator>();

        // MOD by zshen for 2011.06.13 add the support for the view.
        List<DBViewRepNode> viewNodeList = RepositoryNodeHelper.getViewNodeList(objs);
        if (tableNodeList.size() == 0 && viewNodeList.size() == 0) {
            // feature 22206 : fixed another bug, when deselect all, the view is not changed
            this.tableIndicators = tableIndicatorList.toArray(new TableIndicator[tableIndicatorList.size()]);
            this.setElements(tableIndicators);
            return;
        }
        List<RepositoryNode> setList = new ArrayList<RepositoryNode>();
        Connection tdProvider = null;

        for (DBTableRepNode tableNode : tableNodeList) {
            if (tdProvider == null) {
                tdProvider = ConnectionHelper.getTdDataProvider(TableHelper.getParentCatalogOrSchema(tableNode.getTdTable()));
            }

            if (tdProvider == null) {
                MessageUI.openError(DefaultMessagesImpl.getString(
                        "AnalysisTableTreeViewer.TableProviderIsNull", tableNode.getLabel())); //$NON-NLS-1$
            } else if (this.getAnalysis().getContext().getConnection() != null
                    && !tdProvider.equals(this.getAnalysis().getContext().getConnection())) {
                MessageUI.openError(DefaultMessagesImpl.getString(
                        "AnalysisTableTreeViewer.TableDataProviderIsInvalid", tableNode.getLabel())); //$NON-NLS-1$
            } else {
                setList.add(tableNode);
            }
        }
        for (DBViewRepNode tableNode : viewNodeList) {
            if (tdProvider == null) {
                tdProvider = ConnectionHelper.getTdDataProvider(TableHelper.getParentCatalogOrSchema(tableNode.getTdView()));
            }
            if (tdProvider == null) {
                MessageUI.openError(DefaultMessagesImpl.getString(
                        "AnalysisTableTreeViewer.TableProviderIsNull", tableNode.getLabel())); //$NON-NLS-1$
            } else if (this.getAnalysis().getContext().getConnection() != null
                    && !tdProvider.equals(this.getAnalysis().getContext().getConnection())) {
                MessageUI.openError(DefaultMessagesImpl.getString(
                        "AnalysisTableTreeViewer.TableDataProviderIsInvalid", tableNode.getLabel())); //$NON-NLS-1$
            } else {
                setList.add(tableNode);
            }
        }

        for (TableIndicator tableIndicator : tableIndicators) {
            // ADDED yyin 20120606 TDQ-5343
            NamedColumnSet selectedTable = tableIndicator.getColumnSet();
            DQRepositoryNode tableNode = null;
            if (selectedTable instanceof TdTable) {
                tableNode = RepositoryNodeHelper.recursiveFindTdTable(((TdTable) selectedTable));
            } else if (selectedTable instanceof TdView) {
                tableNode = RepositoryNodeHelper.recursiveFindTdView(((TdView) selectedTable));
            }
            // ~
            if (setList.contains(tableNode)) {
                tableIndicatorList.add(tableIndicator);
                setList.remove(tableNode);
            }
        }
        for (RepositoryNode set : setList) {
            TableIndicator tableIndicator = null;
            if (set instanceof DBViewRepNode) {
                tableIndicator = TableIndicator.createTableIndicatorWithRowCountIndicator(((DBViewRepNode) set).getTdView());
            } else if (set instanceof DBTableRepNode) {
                tableIndicator = TableIndicator.createTableIndicatorWithRowCountIndicator(((DBTableRepNode) set).getTdTable());
            } else {
                continue;
            }
            tableIndicatorList.add(tableIndicator);
        }
        this.tableIndicators = tableIndicatorList.toArray(new TableIndicator[tableIndicatorList.size()]);
        this.setElements(tableIndicators);
    }

    /**
     * DOC xqliu Comment method "getTree".
     *
     * @return
     */
    public Tree getTree() {
        return this.tree;
    }

    private String isExpressionNull(TreeItem item) {
        String expressContent = null;
        TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
        TableIndicator tableIndicator = (TableIndicator) item.getData(TABLE_INDICATOR_KEY);
        NamedColumnSet set = tableIndicator.getColumnSet();
        Connection dataprovider = ConnectionHelper.getTdDataProvider(ColumnSetHelper.getParentCatalogOrSchema(set));

        DbmsLanguage dbmsLang = DbmsLanguageFactory.createDbmsLanguage(dataprovider);
        Expression expression = dbmsLang.getInstantiatedExpression(indicatorUnit.getIndicator());
        if (expression != null) {
            expressContent = expression.getBody();
        }
        return expressContent;
    }

    /**
     *
     * DOC xqliu Comment method "addElements".
     *
     * @param elements
     */
    public void addElements(final TableIndicator[] elements) {
        TableIndicator[] newsArray = new TableIndicator[this.tableIndicators.length + elements.length];
        System.arraycopy(this.tableIndicators, 0, newsArray, 0, this.tableIndicators.length);
        for (int i = 0; i < elements.length; i++) {
            newsArray[this.tableIndicators.length + i] = elements[i];
        }
        this.tableIndicators = newsArray;
        this.addItemElements(elements);
        // MOD mzhao 2009-05-5, bug 6587.
        updateBindConnection(masterPage, getTableIndicator(), tree);
    }

    /**
     * DOC xqliu Comment method "getAnalysis".
     *
     * @return
     */
    public Analysis getAnalysis() {
        return this.masterPage.getAnalysisHandler().getAnalysis();
    }

    @Override
    public void dropTables(List<NamedColumnSet> sets, int index) {
        int size = sets.size();
        TableIndicator[] tIndicators = new TableIndicator[size];
        for (int i = 0; i < size; i++) {
            NamedColumnSet set = sets.get(i);
            TableIndicator tableIndicator = TableIndicator.createTableIndicatorWithRowCountIndicator(set);
            tIndicators[i] = tableIndicator;
        }
        this.addElements(tIndicators);
    }

    @Override
    public void dropWhereRules(Object data, List<RuleRepNode> nodes, int index) {
        this.dropWhereRules(data, nodes, index, null);
    }

    public void dropWhereRules(Object data, List<RuleRepNode> nodes, int index, TreeItem item) {
        TreeItem treeItem = null;
        if (item == null) {
            if (getTree().getItemCount() > 0) {
                treeItem = getTree().getItem(0);
            }
        } else {
            treeItem = item;
        }
        if (data != null && treeItem != null && nodes.size() > 0) {
            Analysis analysis = getAnalysis();
            for (RuleRepNode node : nodes) {
                TableIndicatorUnit addIndicatorUnit = DQRuleUtilities.createIndicatorUnit(node, (TableIndicator) data, analysis);
                if (addIndicatorUnit != null) {
                    createOneUnit(treeItem, addIndicatorUnit);
                    setDirty(true);
                }
            }
        }
    }

    @Override
    public boolean canDrop(NamedColumnSet set) {
        Connection tdProvider = ConnectionHelper.getTdDataProvider(TableHelper.getParentCatalogOrSchema(set));
        if (tdProvider == null) {
            return false;
        } else if (this.getAnalysis().getContext().getConnection() != null
                && !tdProvider.equals(this.getAnalysis().getContext().getConnection())) {
            return false;
        }

        List<NamedColumnSet> existSets = new ArrayList<NamedColumnSet>();

        for (TableIndicator tableIndicator : getTableIndicator()) {
            existSets.add(tableIndicator.getColumnSet());
        }
        if (existSets.contains(set)) {
            return false;
        }
        return true;
    }

    private void deleteIndicatorItems(TableIndicator tableIndicator, TableIndicatorUnit inidicatorUnit) {
        tableIndicator.removeIndicatorUnit(inidicatorUnit);
        this.indicatorTreeItemMap.remove(inidicatorUnit);
    }

    /**
     * delete all TableIndicatorUnit which contain in the tableIndicator.
     */
    private void deleteIndicatorItems(TableIndicator tableIndicator) {
        for (TableIndicatorUnit indiUnit : tableIndicator.getIndicatorUnits()) {
            tableIndicator.removeIndicatorUnit(indiUnit);
            this.indicatorTreeItemMap.remove(indiUnit);
        }
    }

    private void removeSelectedElements(Tree newTree) {
        TreeItem[] selection = newTree.getSelection();
        if (isRowCountIndicator(selection)) {
            return;
        }

        boolean branchIndicatorExist = false;
        for (TreeItem item : selection) {
            TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
            if (indicatorUnit != null) {
                deleteIndicatorItems((TableIndicator) item.getData(TABLE_INDICATOR_KEY), indicatorUnit);
            } else {
                deleteTableItems((TableIndicator) item.getData(TABLE_INDICATOR_KEY));
            }
            if (item.getParentItem() != null && item.getParentItem().getData(INDICATOR_UNIT_KEY) != null) {
                branchIndicatorExist = true;
                continue;
            } else {
                removeItemBranch(item);
                removeTreeItem(item);
            }

        }
        if (branchIndicatorExist) {
            setElements(tableIndicators);
        }
        // MOD mzhao 2005-05-05 bug 6587.
        // MOD mzhao 2009-06-8, bug 5887.
        // updateBindConnection(masterPage, getTableIndicator(), tree);
    }

    /**
     * DOC xqliu AnalysisTableTreeViewer class global comment. Detailled comment
     */
    class TableTreeMenuProvider {

        private Tree tree;

        public TableTreeMenuProvider(Tree tree) {
            this.tree = tree;
        }

        /**
         * DOC xqliu Comment method "createTreeMenu".
         */
        public void createTreeMenu() {
            Menu oldMenu = tree.getMenu();
            if (oldMenu != null && !oldMenu.isDisposed()) {
                oldMenu.dispose();
            }
            Menu menu = new Menu(tree);

            if (isSelectedTable(tree.getSelection())) {
                MenuItem createColumnAnalysisMenuItem = new MenuItem(menu, SWT.CASCADE);
                createColumnAnalysisMenuItem.setText(DefaultMessagesImpl.getString("CreateColumnAnalysisAction.columnAnalysis")); //$NON-NLS-1$
                createColumnAnalysisMenuItem.setImage(ImageLib.getImage(ImageLib.ACTION_NEW_ANALYSIS));
                createColumnAnalysisMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        createColumnAnalysis(tree);
                    }
                });

                MenuItem previewMenuItem = new MenuItem(menu, SWT.CASCADE);
                previewMenuItem.setText(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.previewDQElement")); //$NON-NLS-1$
                previewMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
                previewMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        previewSelectedElements(tree);
                    }

                });

                MenuItem showLocationMenuItem = new MenuItem(menu, SWT.CASCADE);
                showLocationMenuItem.setText(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.showDQElement")); //$NON-NLS-1$
                showLocationMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
                showLocationMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        showSelectedElements(tree);
                    }

                });
            }

            if (isSelectedIndicator(tree.getSelection())) {
                MenuItem showQueryMenuItem = new MenuItem(menu, SWT.CASCADE);
                showQueryMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.viewQuery")); //$NON-NLS-1$
                showQueryMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
                showQueryMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        viewQueryForSelectedElement(tree);

                    }
                });
            }

            if (isSelectedWhereRuleIndicator(tree.getSelection())) {
                MenuItem editWhereRUleMenuItem = new MenuItem(menu, SWT.CASCADE);
                editWhereRUleMenuItem.setText(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.editDQRule")); //$NON-NLS-1$
                editWhereRUleMenuItem.setImage(ImageLib.getImage(ImageLib.DQ_RULE));
                editWhereRUleMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        editWhereRule(tree);
                    }

                });
            }

            MenuItem addTaskItem = new MenuItem(menu, SWT.CASCADE);
            addTaskItem.setText(DefaultMessagesImpl.getString("AnalysisTableTreeViewer.AddTask")); //$NON-NLS-1$
            addTaskItem.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
            addTaskItem.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    TreeItem[] selection = tree.getSelection();
                    if (selection.length > 0) {
                        TreeItem treeItem = selection[0];
                        TableIndicator tableIndicator = (TableIndicator) treeItem.getData(TABLE_INDICATOR_KEY);
                        NamedColumnSet set = tableIndicator.getColumnSet();
                        ModelElement me = getAnalysis();
                        me.setName(set.getName());
                        (new TdAddTaskAction(tree.getShell(), me)).run();
                    }

                }
            });

            if (!isRowCountIndicator(tree.getSelection())) {
                MenuItem deleteMenuItem = new MenuItem(menu, SWT.CASCADE);
                deleteMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.removeElement")); //$NON-NLS-1$
                deleteMenuItem.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
                deleteMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        removeSelectedElements(tree);
                    }

                });
            }

            tree.setMenu(menu);
        }

        private void editWhereRule(Tree tree) {
            TreeItem[] selection = tree.getSelection();
            if (selection.length > 0) {
                TreeItem treeItem = selection[0];
                TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) treeItem.getData(INDICATOR_UNIT_KEY);
                WhereRuleIndicator indicator = (WhereRuleIndicator) indicatorUnit.getIndicator();
                WhereRule whereRule = (WhereRule) indicator.getIndicatorDefinition();
                // Property property = PropertyHelper.getProperty(whereRule);
                // Item item = property.getItem();
                RuleRepNode node = RepositoryNodeHelper.recursiveFindRuleSql(whereRule);
                BusinessRuleItemEditorInput itemEditorInput = new BusinessRuleItemEditorInput(node);
                // ~
                IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    activePage.openEditor(itemEditorInput, "org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor"); //$NON-NLS-1$
                } catch (PartInitException e1) {
                    log.error(e1, e1);
                }
            }
        }

        private void createColumnAnalysis(Tree newTree) {
            TreeItem[] items = newTree.getSelection();
            if (items.length > 0) {
                TreePath[] paths = new TreePath[items.length];

                for (int i = 0; i < items.length; i++) {
                    TableIndicator tableIndicator = (TableIndicator) items[i].getData(TABLE_INDICATOR_KEY);
                    paths[i] = new TreePath(new Object[] { tableIndicator.getColumnSet() });
                }
                CreateColumnAnalysisAction analysisAction = new CreateColumnAnalysisAction();
                analysisAction.setSelection(new TreeSelection(paths));
                analysisAction.run();
            }
        }

        private void previewSelectedElements(Tree newTree) {
            TreeItem[] items = newTree.getSelection();
            NamedColumnSet[] sets = new NamedColumnSet[items.length];

            for (int i = 0; i < items.length; i++) {
                TableIndicator tableIndicator = (TableIndicator) items[i].getData(TABLE_INDICATOR_KEY);
                sets[i] = tableIndicator.getColumnSet();
            }

            new PreviewAction(sets[0]).run();
        }

        private void viewQueryForSelectedElement(Tree newTree) {
            TreeItem[] selection = newTree.getSelection();
            for (TreeItem item : selection) {
                TableIndicator tableIndicator = (TableIndicator) item.getData(TABLE_INDICATOR_KEY);
                NamedColumnSet set = tableIndicator.getColumnSet();
                Connection dataprovider = ConnectionHelper.getTdDataProvider(TableHelper.getParentCatalogOrSchema(set));
                Object temp = item.getData(INDICATOR_UNIT_KEY);
                if (temp != null) {
                    TableIndicatorUnit indicatorUnit = (TableIndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
                    DbmsLanguage dbmsLang = DbmsLanguageFactory.createDbmsLanguage(dataprovider);
                    Expression expression = dbmsLang.getInstantiatedExpression(indicatorUnit.getIndicator());
                    if (expression == null) {
                        MessageDialogWithToggle
                                .openWarning(
                                        null,
                                        DefaultMessagesImpl.getString("AnalysisTableTreeViewer.Warn"), DefaultMessagesImpl.getString("AnalysisTableTreeViewer.NoQueryDefined")); //$NON-NLS-1$ //$NON-NLS-2$
                        return;
                    }

                    SqlExplorerUtils.getDefault().runInDQViewer(dataprovider, expression.getBody(), set.getName());
                }
            }
        }

        private void showDetailView(Tree newTree) {
            TreeItem[] selection = newTree.getSelection();
            if (selection.length > 0) {
                RespositoryDetailView detailView = CorePlugin.getDefault().getRespositoryDetailView();
                if (detailView == null) {
                    return;
                }

                DQRespositoryView dqview = CorePlugin.getDefault().getRepositoryView();
                RepositoryNode node = getSelectedNode(selection);
                detailView.selectionChanged(dqview, new StructuredSelection(node));
            }
        }

        private void showSelectedElements(Tree newTree) {
            TreeItem[] selection = newTree.getSelection();

            if (selection.length > 0) {
                // if DqRepository view is not openning we will not do anything
                DQRespositoryView dqview = CorePlugin.getDefault().findAndOpenRepositoryView();
                if (dqview == null) {
                    return;
                }
                RepositoryNode node = getSelectedNode(selection);
                dqview.showSelectedElements(node);
            }
        }

        /**
         * DOC msjian Comment method "getSelectedNode".
         *
         * @param selection
         * @return
         */
        private RepositoryNode getSelectedNode(TreeItem[] selection) {
            TableIndicator tableIndicator = (TableIndicator) selection[0].getData(TABLE_INDICATOR_KEY);
            NamedColumnSet set = tableIndicator.getColumnSet();
            // ProxyRepositoryViewObject.fetchAllRepositoryViewObjects(true, true);
            RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(set);
            if (recursiveFind == null) {
                recursiveFind = RepositoryNodeHelper.createRepositoryNode(set);
            }
            return recursiveFind;
        }

        private boolean isSelectedTable(TreeItem[] items) {
            for (TreeItem item : items) {
                if (item.getData(INDICATOR_UNIT_KEY) != null) {
                    return false;
                }
            }

            return true;
        }

        private boolean isSelectedIndicator(TreeItem[] items) {

            if (isSelectedTable(items)) {
                return false;
            }

            return true;
        }

        private boolean isSelectedWhereRuleIndicator(TreeItem[] items) {
            if (!isSelectedIndicator(items)) {
                return false;
            }

            for (TreeItem item : items) {
                TableIndicatorUnit unit = (TableIndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
                if (unit != null) {

                    Indicator indicator = unit.getIndicator();
                    if (!(indicator instanceof WhereRuleIndicator)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * DOC xqliu Comment method "isRowCountIndicator".
     *
     * @param selection
     * @return
     */
    public boolean isRowCountIndicator(TreeItem[] selection) {
        if (selection != null && selection.length > 0) {
            for (TreeItem ti : selection) {
                Object obj = ti.getData(INDICATOR_UNIT_KEY);
                if (obj != null && obj instanceof TableIndicatorUnit) {
                    TableIndicatorUnit tiu = (TableIndicatorUnit) ti.getData(INDICATOR_UNIT_KEY);
                    if (tiu.getIndicator() != null && tiu.getIndicator() instanceof RowCountIndicator) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void updateModelViewer() {
        masterPage.recomputeIndicators();
        tableIndicators = masterPage.getCurrentTableIndicators();
        setElements(tableIndicators);
    }
}
