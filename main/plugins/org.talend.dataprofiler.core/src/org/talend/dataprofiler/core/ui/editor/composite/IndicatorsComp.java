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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnSetAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.CorrelationAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnSetIndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.indicator.IndicatorOptionsWizard;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.CountAvgNullIndicator;
import org.talend.dataquality.indicators.columnset.MinMaxDateIndicator;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.columnset.WeakCorrelationIndicator;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class IndicatorsComp extends AbstractPagePart {

    protected static Logger log = Logger.getLogger(IndicatorsComp.class);

    public static final String INDICATOR_KEY = "INDICATOR_KEY"; //$NON-NLS-1$

    private Composite parentComp;

    private Tree tree;

    private List<TdColumn> columnSetMultiValueList;

    private AbstractAnalysisMetadataPage masterPage;

    private ColumnSetMultiValueIndicator columnSetIndicator;

    public IndicatorsComp(Composite parent) {
        parentComp = parent;
        tree = createTree(parent);
        tree.setData(this);
        columnSetMultiValueList = new ArrayList<TdColumn>();
    }

    public IndicatorsComp(Composite parent, AbstractAnalysisMetadataPage masterPage) {
        this(parent);
        this.masterPage = masterPage;
        this.setDirty(false);
    }

    /**
     * @param parent
     */
    private Tree createTree(Composite parent) {
        final Tree newTree = new Tree(parent, SWT.MULTI | SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(newTree);

        newTree.setHeaderVisible(true);
        TreeColumn column1 = new TreeColumn(newTree, SWT.CENTER);
        column1.setWidth(190);
        column1.setText(DefaultMessagesImpl.getString("IndicatorsComp.Indicators")); //$NON-NLS-1$

        TreeColumn column2 = new TreeColumn(newTree, SWT.CENTER);
        column2.setWidth(80);
        column2.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.options")); //$NON-NLS-1$

        parent.layout();
        return newTree;
    }

    // input composite indicator
    public void setInput(Object... obj) {
        List<ColumnSetIndicatorUnit> indicatortList = new ArrayList<ColumnSetIndicatorUnit>();
        for (Object indicatorObj : obj) {
            // for SimpleStatIndicator, CountAvgNullIndicator, MinMaxDateIndicator, WeakCorrelationIndicator
            if (indicatorObj instanceof SimpleStatIndicator || indicatorObj instanceof CountAvgNullIndicator
                    || indicatorObj instanceof MinMaxDateIndicator || indicatorObj instanceof WeakCorrelationIndicator) {
                columnSetIndicator = (ColumnSetMultiValueIndicator) indicatorObj;
                for (Indicator indicator : IndicatorHelper.getIndicatorLeavesBySingleNode(columnSetIndicator)) {
                    IndicatorEnum indicatorEnum = IndicatorEnum.findIndicatorEnum(indicator.eClass());
                    indicatortList.add(new ColumnSetIndicatorUnit(indicatorEnum, indicator));
                }
                // MOD msjian TDQ-8860: we always show the allMatchIndicator in the Indicators section
            } else if (indicatorObj instanceof AllMatchIndicator) { // for AllMatchIndicator
                AllMatchIndicator allMatchIndicator = (AllMatchIndicator) indicatorObj;
                indicatortList.add(new ColumnSetIndicatorUnit(IndicatorEnum.AllMatchIndicatorEnum, allMatchIndicator));
                // TDQ-8860~
            }
            // ~
        }
        setElements(indicatortList.toArray(new ColumnSetIndicatorUnit[indicatortList.size()]));
    }

    /**
     * 
     * init elements for the tree
     * 
     * @param indicatorUnits
     */
    public void setElements(ColumnSetIndicatorUnit[] indicatorUnits) {
        this.tree.dispose();
        this.tree = createTree(this.parentComp);
        tree.setData(this);
        createIndicatorItems(indicatorUnits);
    }

    /**
     * 
     * 
     * 
     * @param indicatorUnits
     */
    private void createIndicatorItems(ColumnSetIndicatorUnit[] indicatorUnits) {
        for (ColumnSetIndicatorUnit indicatorUnit : indicatorUnits) {
            createOneUnit(indicatorUnit);
        }
    }

    /**
     * 
     * create element on the tree by indicatorUnit
     * 
     * @param indicator
     */
    public void createOneUnit(ColumnSetIndicatorUnit indicatorUnit) {
        final TreeItem indicatorItem = new TreeItem(this.tree, SWT.NONE);

        indicatorItem.setData(INDICATOR_KEY, indicatorUnit);

        indicatorItem.setImage(0, ImageLib.getImage(ImageLib.IND_DEFINITION));
        String indicatorName = indicatorUnit.getIndicatorName();
        String label = indicatorName == null ? "unknown indicator" : indicatorName;//$NON-NLS-1$
        indicatorItem.setText(0, label);

        TreeEditor optionEditor;
        optionEditor = new TreeEditor(tree);
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
                if (openIndicatorOptionDialog(Display.getCurrent().getActiveShell(), indicatorItem)) {
                    optionLabel.setImage(ImageLib.getImage(ImageLib.INDICATOR_OPTION_CHECKED));
                }
            }

        });

        optionEditor.minimumWidth = optionLabel.getImage().getBounds().width;
        optionEditor.horizontalAlignment = SWT.CENTER;
        optionEditor.setEditor(optionLabel, indicatorItem, 1);

        if (hasIndicatorParameters(indicatorUnit.getIndicator())) {
            optionLabel.setImage(ImageLib.getImage(ImageLib.INDICATOR_OPTION_CHECKED));
        }
    }

    public boolean openIndicatorOptionDialog(Shell shell, TreeItem indicatorItem) {
        if (isDirty()) {
            masterPage.doSave(null);
        }

        ColumnSetIndicatorUnit indicatorUnit = (ColumnSetIndicatorUnit) indicatorItem.getData(INDICATOR_KEY);
        if (indicatorUnit.isExsitingForm()) {
            IndicatorOptionsWizard wizard = new IndicatorOptionsWizard(indicatorUnit);
            String href = FormEnum.getFirstFormHelpHref(indicatorUnit);
            OpeningHelpWizardDialog optionDialog = new OpeningHelpWizardDialog(shell, wizard, href);
            if (Window.OK == optionDialog.open()) {
                masterPage.setDirty(wizard.isDirty());
                return hasIndicatorParameters(indicatorUnit.getIndicator());
            }
        } else {
            MessageDialogWithToggle.openInformation(shell, DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.information"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.nooption")); //$NON-NLS-1$ 
        }
        return false;
    }

    private boolean hasIndicatorParameters(Indicator indicator) {
        IndicatorParameters parameters = indicator.getParameters();
        if (parameters == null) {
            return false;
        }
        Domain indicatorValidDomain = parameters.getIndicatorValidDomain();
        if (indicatorValidDomain != null) {
            return true;
        }
        return false;
    }

    public List<TdColumn> getColumnSetMultiValueList() {
        return this.columnSetMultiValueList;
    }

    /**
     * Getter for analysis.
     * 
     * @return the analysis
     */
    public Analysis getAnalysis() {
        if (masterPage instanceof ColumnSetAnalysisDetailsPage) {
            return ((ColumnSetAnalysisDetailsPage) masterPage).getCurrentModelElement();
        } else if (masterPage instanceof CorrelationAnalysisDetailsPage) {
            return ((CorrelationAnalysisDetailsPage) masterPage).getCurrentModelElement();
        }
        return null;
    }

    public Tree getTree() {
        return tree;
    }

    @Override
    public void updateModelViewer() {
        if (masterPage instanceof ColumnSetAnalysisDetailsPage) {
            ((ColumnSetAnalysisDetailsPage) masterPage).recomputeIndicators();
        } else if (masterPage instanceof CorrelationAnalysisDetailsPage) {
            ((CorrelationAnalysisDetailsPage) masterPage).recomputeIndicators();
        }
        columnSetMultiValueList.clear();
    }
}
