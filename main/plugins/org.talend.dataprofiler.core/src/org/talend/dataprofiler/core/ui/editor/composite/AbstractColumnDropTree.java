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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.grid.utils.Observerable;
import org.talend.dataprofiler.core.ui.grid.utils.TDQObserver;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.wizard.indicator.IndicatorOptionsWizard;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.TextParameters;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * The interface class to handle the change when drop columns. MOD mzhao, refactor codes for feature:13040, 2010-05-21
 */
public abstract class AbstractColumnDropTree extends AbstractPagePart implements Observerable<ModelElement[]>,
        TDQObserver<Map<String, Integer>> {

    public static final String COLUMN_INDICATOR_KEY = "COLUMN_INDICATOR_KEY"; //$NON-NLS-1$

    public static final String TABLE_INDICATOR_KEY = "TABLE_INDICATOR_KEY"; //$NON-NLS-1$

    public static final String COLUMNVIEWER_KEY = "COLUMNVIEWER_KEY"; //$NON-NLS-1$

    public static final String INDICATOR_UNIT_KEY = "INDICATOR_UNIT_KEY"; //$NON-NLS-1$

    public static final String MODELELEMENT_INDICATOR_KEY = "MODELELEMENT_INDICATOR_KEY"; //$NON-NLS-1$

    public static final String ITEM_EDITOR_KEY = "ITEM_EDITOR_KEY"; //$NON-NLS-1$

    protected static final int WIDTH1_CELL = 75;

    public abstract boolean canDrop(IRepositoryNode reposNode);

    protected Tree tree;

    protected ModelElementIndicator[] modelElementIndicators;

    protected AbstractAnalysisMetadataPage absMasterPage = null;

    private List<TDQObserver<ModelElement[]>> Observers = null;

    protected TreeAdapter treeAdapter = new TreeAdapter() {

        @Override
        public void treeCollapsed(TreeEvent e) {

            ExpandableComposite theSuitedComposite = getTheSuitedComposite(e);
            if (theSuitedComposite != null && theSuitedComposite.isExpanded()) {
                theSuitedComposite.setExpanded(false);
            }

            layoutChartCompositeRefolwForm();
        }

        @Override
        public void treeExpanded(TreeEvent e) {

            ExpandableComposite theSuitedComposite = getTheSuitedComposite(e);
            if (theSuitedComposite != null && !theSuitedComposite.isExpanded()) {
                theSuitedComposite.setExpanded(true);
            } else {
                propertyChangeSupport.firePropertyChange(PluginConstant.EXPAND_TREE, null, e.item);
            }

            layoutChartCompositeRefolwForm();
        }

        /**
         * layout ChartComposite and Refolw Form.
         */
        public void layoutChartCompositeRefolwForm() {
            Composite comp = getMasterPage().getChartComposite();
            if (comp != null && !comp.isDisposed()) {
                comp.getParent().pack();
            }
        }

    };

    /**
     * Getter for MasterPage.
     *
     * @return the MasterPage
     */
    public AbstractAnalysisMetadataPage getMasterPage() {
        return this.absMasterPage;
    }

    /**
     * DOC msjian Comment method "getTheSuitedComposite".
     *
     * @param e
     * @return
     */
    public ExpandableComposite getTheSuitedComposite(SelectionEvent e) {
        return null;
    }

    protected String viewKey = null;

    /**
     * DOC qzhang Comment method "createOneUnit".
     *
     * @param treeItem
     * @param indicatorUnit
     */
    public void createOneUnit(final TreeItem treeItem, IndicatorUnit indicatorUnit) {
        final TreeItem indicatorItem = new TreeItem(treeItem, SWT.NONE);
        final IndicatorUnit unit = indicatorUnit;
        IndicatorEnum indicatorType = indicatorUnit.getType();

        indicatorItem.setData(MODELELEMENT_INDICATOR_KEY, treeItem.getData(MODELELEMENT_INDICATOR_KEY));
        indicatorItem.setData(INDICATOR_UNIT_KEY, unit);
        indicatorItem.setData(viewKey, this);

        indicatorItem.setImage(0, getIndicatorImage(unit));

        String indicatorName = getIndicatorName(indicatorUnit);
        String label = indicatorName != null ? indicatorName
                : DefaultMessagesImpl.getString("AnalysisTableTreeViewer.unknownIndicator");//$NON-NLS-1$
        indicatorItem.setText(0, label);

        TreeEditor optionEditor = new TreeEditor(tree);
        final Label optionLabel = createTreeItemLabel(tree, ImageLib.OPTION, "AnalysisColumnTreeViewer.options"); //$NON-NLS-1$
        optionLabel.setData(indicatorUnit);
        optionLabel.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             *
             * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt .events.MouseEvent)
             */
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
        // }

        TreeEditor delEditor = new TreeEditor(tree);
        Label delLabel = createTreeItemLabel(tree, ImageLib.DELETE_ACTION, "AnalysisColumnTreeViewer.delete"); //$NON-NLS-1$
        delLabel.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             *
             * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt .events.MouseEvent)
             */
            @Override
            public void mouseDown(MouseEvent e) {
                // current delLabel is behind of one indicator item.
                ModelElementIndicator meIndicator = (ModelElementIndicator) treeItem.getData(MODELELEMENT_INDICATOR_KEY);
                deleteIndicatorItems(meIndicator, unit);
                if (indicatorItem.getParentItem() != null && indicatorItem.getParentItem().getData(INDICATOR_UNIT_KEY) != null) {
                    setElements(modelElementIndicators);
                } else {
                    removeItemBranch(indicatorItem);
                }
            }

        });

        delEditor.minimumWidth = delLabel.getImage().getBounds().width;
        delEditor.horizontalAlignment = SWT.CENTER;
        // MOD mzhao feature 13040, column analysis have 5 columns, whereas columnset have 4 columns.
        if (AnalysisColumnTreeViewer.VIEWER_KEY.equals(viewKey)) {
            delEditor.setEditor(delLabel, indicatorItem, 4);
        } else if (AnalysisColumnSetTreeViewer.VIEWER_KEY.equals(viewKey)) {
            delEditor.setEditor(delLabel, indicatorItem, 3);
        }
        indicatorItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { optionEditor, delEditor });
        if (indicatorType.hasChildren()) {
            indicatorItem.setData(treeItem.getData(MODELELEMENT_INDICATOR_KEY));
            createIndicatorItems(indicatorItem, indicatorUnit.getChildren());
        }
        if (hasIndicatorParameters(indicatorUnit)) {
            optionLabel.setImage(ImageLib.getImage(ImageLib.INDICATOR_OPTION_CHECKED));
        }
    }

    protected abstract void setElements(ModelElementIndicator[] modelElementIndicator);

    public ModelElementIndicator[] getModelElementIndicator() {
        return this.modelElementIndicators;
    }

    protected void createIndicatorItems(final TreeItem treeItem, IndicatorUnit[] indicatorUnits) {
        for (IndicatorUnit indicatorUnit : indicatorUnits) {
            createOneUnit(treeItem, indicatorUnit);
        }
    }

    protected void removeItemBranch(TreeItem item) {
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
        }
        item.dispose();
        this.setDirty(true);
    }

    /**
     * remove special indicatorUnit which in the ModelElementIndicator.
     *
     * @param meIndicator remove from here
     * @param inidicatorUnit should be removed
     */
    protected void deleteIndicatorItems(ModelElementIndicator meIndicator, IndicatorUnit inidicatorUnit) {
        meIndicator.removeIndicatorUnit(inidicatorUnit);
    }

    /**
     *
     * remove all the indicatorUnit which in the ModelElementIndicator.
     *
     * @param meIndicator
     *
     */
    protected void deleteIndicatorItems(ModelElementIndicator meIndicator) {
        for (IndicatorUnit indiUnit : meIndicator.getIndicatorUnits()) {
            deleteIndicatorItems(meIndicator, indiUnit);
        }
    }

    /**
     * open Indicator Option Dialog.
     *
     * @param shell
     * @param indicatorItem
     * @return
     */
    public boolean openIndicatorOptionDialog(Shell shell, TreeItem indicatorItem) {
        IndicatorUnit indicatorUnit = (IndicatorUnit) indicatorItem.getData(INDICATOR_UNIT_KEY);
        if (FormEnum.isExsitingForm(indicatorUnit)) {
            IndicatorOptionsWizard wizard = new IndicatorOptionsWizard(indicatorUnit);
            String href = FormEnum.getFirstFormHelpHref(indicatorUnit);
            OpeningHelpWizardDialog optionDialog = new OpeningHelpWizardDialog(shell, wizard, href);
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
     * DOC msjian Comment method "openNoIndicatorOptionsMessageDialog".
     *
     * @param shell
     */
    public void openNoIndicatorOptionsMessageDialog(Shell shell) {
        MessageDialogWithToggle.openInformation(shell, DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.information"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.nooption")); //$NON-NLS-1$
    }

    /**
     * DOC xqliu Comment method "getIndicatorIamge".
     *
     * @param indicatorUnit
     * @return
     */
    private Image getIndicatorImage(IndicatorUnit indicatorUnit) {
        IndicatorEnum indicatorType = indicatorUnit.getType();
        if (indicatorType == IndicatorEnum.RegexpMatchingIndicatorEnum
                || indicatorType == IndicatorEnum.SqlPatternMatchingIndicatorEnum) {
            return ImageLib.getImage(ImageLib.PATTERN_REG);
        }
        return ImageLib.getImage(ImageLib.IND_DEFINITION);
    }

    /**
     * ADD yyi 2010-04-20 12173:update indicator name.
     *
     * @param unit
     * @return
     */
    protected String getIndicatorName(IndicatorUnit unit) {
        IndicatorEnum indicatorType = unit.getType();
        if (indicatorType == IndicatorEnum.RegexpMatchingIndicatorEnum
                || indicatorType == IndicatorEnum.SqlPatternMatchingIndicatorEnum) {
            Pattern pattern = unit.getIndicator().getParameters().getDataValidDomain().getPatterns().get(0);
            PatternRepNode recursiveFindPattern = RepositoryNodeHelper.recursiveFindPattern(pattern);
            if (recursiveFindPattern != null && !recursiveFindPattern.getProject().isMainProject()) {
                return pattern.getName() + recursiveFindPattern.getDisplayProjectName();
            }
            return pattern.getName();
        } else if (indicatorType == IndicatorEnum.UserDefinedIndicatorEnum) {
            IndicatorDefinition indicatorDefinition = unit.getIndicator().getIndicatorDefinition();
            if (indicatorDefinition.eIsProxy()) {
                indicatorDefinition = (IndicatorDefinition) EObjectHelper.resolveObject(indicatorDefinition);
            }
            SysIndicatorDefinitionRepNode recursiveFindNode =
                    RepositoryNodeHelper.recursiveFindIndicatorDefinition(indicatorDefinition);
            if (recursiveFindNode != null && !recursiveFindNode.getProject().isMainProject()) {
                return indicatorDefinition.getLabel() + recursiveFindNode.getDisplayProjectName();
            }
            return indicatorDefinition.getLabel();
        }
        return unit.getIndicatorName();
    }

    /**
     * DOC msjian Comment method "hasIndicatorParameters".
     *
     * @param indicatorUnit
     * @return
     */
    private boolean hasIndicatorParameters(IndicatorUnit indicatorUnit) {
        IndicatorParameters parameters = indicatorUnit.getIndicator().getParameters();
        if (parameters == null) {
            return false;
        }
        if (hideParameters(indicatorUnit)) {
            return false;
        }

        if (indicatorUnit.getIndicator() instanceof FrequencyIndicator) {
            // MOD hcheng bug 7377,2009-05-18,when bins is null,parameters not
            // set on tree
            if (parameters.getBins() == null) {
                return false;
            }
            // ~
            return true;
        }

        TextParameters tParameter = parameters.getTextParameter();
        if (tParameter != null && !hideTextParameters(indicatorUnit)) {
            return true;
        }
        DateParameters dParameters = parameters.getDateParameters();
        if (dParameters != null) {
            return true;
        }

        Domain indicatorValidDomain = parameters.getIndicatorValidDomain();
        if (indicatorValidDomain != null) {
            if (indicatorValidDomain.getRanges() != null && !indicatorValidDomain.getRanges().isEmpty()) {
                return true;
            }
        }
        Domain bins = parameters.getBins();
        if (bins != null) {
            return true;
        }

        return false;
    }

    public void dropModelElements(List<? extends IRepositoryNode> repositoryNode, int index) {
        int size = repositoryNode.size();
        ModelElementIndicator[] meIndicators = new ModelElementIndicator[size];
        for (int i = 0; i < size; i++) {
            IRepositoryNode repNode = repositoryNode.get(i);
            IRepositoryViewObject repViewObj = repNode.getObject();
            // TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(repViewObj);
            if (repNode instanceof DFColumnRepNode) {
                meIndicators[i] = ModelElementIndicatorHelper.createDFColumnIndicator(repNode);
            } else if (repViewObj != null && repViewObj instanceof MetadataColumnRepositoryObject) {
                meIndicators[i] = ModelElementIndicatorHelper.createColumnIndicator(repNode);
            }
        }
        addElements(meIndicators);
    }

    public abstract void addElements(final ModelElementIndicator[] elements);

    public ModelElementIndicator[] filterInputData(Object[] objs) {
        // Refactor yyin 20121122 TDQ-6329: if not needed, do not set the new selected objs to
        // this.modelElementIndicators directly
        this.modelElementIndicators = translateSelectedNodeIntoIndicator(objs);

        return this.modelElementIndicators;
    }

    // translate the selected nodes into related indicators, without set the values to this.modelElementIndicators
    // directly
    protected ModelElementIndicator[] translateSelectedNodeIntoIndicator(Object[] objs) {
        List<IRepositoryNode> reposList = RepNodeUtils.translateSelectedToStandardReposityoryNode(objs);
        if (reposList.size() == 0) {
            // MOD yyi 2012-02-29 TDQ-3605 Empty column table.
            // this.modelElementIndicators = new ModelElementIndicator[0];
            return new ModelElementIndicator[0];
        }
        // MOD qiongli 2011-1-7 feature 16796.
        boolean isDelimitedFile = false;
        if (objs != null && objs.length != 0) {
            // MOD klliu 2011-02-16 feature 15387
            isDelimitedFile = RepNodeUtils.isDelimitedFile(objs[0]);
            if (!(reposList.get(0) instanceof DBColumnRepNode || isDelimitedFile)) {
                return null;
            }
        }

        List<ModelElementIndicator> modelElementIndicatorList = new ArrayList<ModelElementIndicator>();
        for (ModelElementIndicator modelElementIndicator : getAllTheElementIndicator()) {
            if (reposList.contains(modelElementIndicator.getModelElementRepositoryNode())) {
                modelElementIndicatorList.add(modelElementIndicator);
                reposList.remove(modelElementIndicator.getModelElementRepositoryNode());
            }
        }

        for (IRepositoryNode repObj : reposList) {
            ModelElementIndicator temp = isDelimitedFile ? ModelElementIndicatorHelper.createDFColumnIndicator(repObj)
                    : ModelElementIndicatorHelper.createColumnIndicator(repObj);
            modelElementIndicatorList.add(temp);
        }

        return modelElementIndicatorList.toArray(new ModelElementIndicator[modelElementIndicatorList.size()]);
    }

    public ModelElementIndicator[] filterInputData(ModelElementIndicator[] objs) {
        if (objs != null && objs.length > 0) {
            this.modelElementIndicators = objs;
        }
        return this.modelElementIndicators;
    }

    public void setInput(Object[] objs) {
        ModelElementIndicator[] filterInputData = filterInputData(objs);
        if (filterInputData != null) {
            this.modelElementIndicators = filterInputData;
        }
        this.setElements(modelElementIndicators);
    }

    private boolean hideParameters(IndicatorUnit indicatorUnit) {
        EClass indicatorEclass = indicatorUnit.getIndicator().eClass();
        if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getDateFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getWeekFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMonthFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getQuarterFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getYearFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getDateLowFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getWeekLowFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMonthLowFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getQuarterLowFrequencyIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getYearLowFrequencyIndicator())
        // MOD yyi 2011-06-03 17740: enable thresholds for indicators
        // indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithNullIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithBlankIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithBlankNullIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithNullIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithBlankIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithBlankNullIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithNullIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithBlankIndicator())
        // || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithBlankNullIndicator())
        ) {
            return true;
        }
        return false;
    }

    /**
     * ADD yyi 2011-07-18 17740: hide text parameter tree node for splited length indicators
     *
     * @param indicatorUnit
     * @return
     */
    private boolean hideTextParameters(IndicatorUnit indicatorUnit) {
        EClass indicatorEclass = indicatorUnit.getIndicator().eClass();
        if (indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithNullIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithBlankIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthWithBlankNullIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithNullIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithBlankIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthWithBlankNullIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithNullIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithBlankIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getPhoneNumbStatisticsIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAvgLengthWithBlankNullIndicator())
                // MOD gdbu 2011-10-9 TDQ-3549
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMinLengthIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getMaxLengthIndicator())
                || indicatorEclass.equals(IndicatorsPackage.eINSTANCE.getAverageLengthIndicator())) {
            // ~TDQ-3549
            return true;
        }
        return false;
    }

    protected ModelElementIndicator[] getAllTheElementIndicator() {
        return this.getModelElementIndicator();
    }

    // Refactor: move some same code into this parent
    protected void createTreeDataminingItem(Tree newTree) {
        TreeColumn column2 = new TreeColumn(newTree, SWT.CENTER);
        column2.setWidth(120);
        column2.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.dataminingType")); //$NON-NLS-1$
        column2.setToolTipText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.columnTip")); //$NON-NLS-1$
    }

    // create the similar tree column
    protected void createTreeItem(Tree newTree, int width, String text) {
        TreeColumn column = new TreeColumn(newTree, SWT.CENTER);
        column.setWidth(width);
        column.setText(DefaultMessagesImpl.getString(text));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#addObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TalendObserver)
     */
    public boolean addObserver(TDQObserver<ModelElement[]> observer) {
        initObserverable();
        return Observers.add(observer);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#removeObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TalendObserver)
     */
    public boolean removeObserver(TDQObserver<ModelElement[]> observer) {
        if (Observers == null) {
            return false;
        }
        return Observers.remove(observer);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#clearObserver()
     */
    public void clearObserver() {
        if (Observers == null) {
            return;
        }
        Observers.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#notifyObservers()
     */
    public void notifyObservers() {
        if (Observers == null) {
            return;
        }
        for (TDQObserver<ModelElement[]> observer : Observers) {
            observer.update(ModelElementIndicatorHelper.getModelElementFromMEIndicator(getAllTheElementIndicator()));
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#initObserverable()
     */
    private void initObserverable() {
        if (Observers == null) {
            Observers = new ArrayList<TDQObserver<ModelElement[]>>();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.grid.utils.TDQObserver#update(java.lang.Object)
     */
    public void update(Map<String, Integer> columnIndexMap) {
        // the sub class must implement this
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.grid.utils.TDQObserver#update(int)
     */
    public void update(int EventType) {
        // do nothing until now
    }

    /**
     * DOC talend Comment method "reorderModelElement".
     *
     * @param currentModelElementIndicators
     */
    public ModelElementIndicator[] reorderModelElement(ModelElementIndicator[] currentModelElementIndicators,
            Map<String, Integer> columnIndexMap) {
        ModelElementIndicator[] sortedModelElements = new ModelElementIndicator[currentModelElementIndicators.length];
        for (ModelElementIndicator currModelElement : currentModelElementIndicators) {
            String columnName = currModelElement.getElementName();
            sortedModelElements[columnIndexMap.get(columnName)] = currModelElement;
        }
        return sortedModelElements;
    }

    /**
     * DOC rli Comment method "deleteTreeElements".
     *
     * @param deleteModelElementIndiciator
     */
    public void deleteModelElementItems(ModelElementIndicator deleteModelElementIndiciator) {
        ModelElementIndicator[] remainIndicators = new ModelElementIndicator[modelElementIndicators.length - 1];
        int i = 0;
        for (ModelElementIndicator indicator : modelElementIndicators) {
            if (deleteModelElementIndiciator.equals(indicator)) {
                continue;
            } else {
                remainIndicators[i] = indicator;
                i++;
            }
        }
        this.modelElementIndicators = remainIndicators;

        initializedConnection(this.modelElementIndicators);
    }

    /**
     * DOC msjian Comment method "initializedConnection". for 6560
     *
     * @param indicators
     */
    void initializedConnection(ModelElementIndicator[] indicators) {
        Analysis analysis = getMasterPage().getAnalysisHandler().getAnalysis();
        DataManager connection = analysis.getContext().getConnection();
        // Connection tdDataProvider = null;

        boolean enableWhereClauseFlag = true;
        boolean enableExecuteLanguageFlag = false;
        // ~
        if (indicators != null && indicators.length > 0) {
            if (connection == null) {
                connection = ModelElementIndicatorHelper.getTdDataProvider(indicators[0]);
                analysis.getContext().setConnection(connection);
            }
            if (connection != null && getMasterPage().getExecCombo() != null) {
                if (ConnectionUtils.isDelimitedFileConnection((DataProvider) connection)) {
                    getMasterPage().setWhereClauseDisabled();
                    // when the selected column is not DB type,will disable the execute engine combobox.
                    getMasterPage().changeExecuteLanguageToJava(true);

                    enableWhereClauseFlag = false;
                    enableExecuteLanguageFlag = false;
                } else {// when the selected column is back to DB type, should enable the execute engine combobox again.
                    getMasterPage().enableExecuteLanguage();
                }
            }
        }
        // MOD klliu if default ExecutionLanguage is java,it is not changed to SQL.2011-11-21
        String execLang = analysis.getParameters().getExecutionLanguage().getLiteral();
        if (execLang != null && ExecutionLanguage.JAVA.getLiteral().equals(execLang)
                && (ConnectionUtils.isDelimitedFileConnection((DataProvider) connection))) {
            enableExecuteLanguageFlag = false;
        }

        if (enableWhereClauseFlag) {
            getMasterPage().setWhereClauseEnable();
        }

        if (enableExecuteLanguageFlag) {
            getMasterPage().changeExecuteLanguageToSql(true);
        }
    }

}
