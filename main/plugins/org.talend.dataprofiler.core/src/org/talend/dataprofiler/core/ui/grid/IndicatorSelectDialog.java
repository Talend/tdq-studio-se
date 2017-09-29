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
package org.talend.dataprofiler.core.ui.grid;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.DelimitedFileIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.utils.ModelElementIndicatorRule;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.nodes.indicator.IIndicatorNode;

/**
 * The indicator select dialog
 * 
 */
public class IndicatorSelectDialog extends TrayDialog implements IIndicatorSelectDialog {

    private ModelElementIndicator[] modelElementIndicators;

    private final String title;

    // ADD by zshen:need language to decide DatePatternFrequencyIndicator whether can be choose by user.
    private ExecutionLanguage language;

    private Label purposeLabel;

    private Label descriptionLabel;

    private IndicatorSelectGrid gridIndicator;

    private ColumnPreviewGrid gridPrview;

    private Composite redrewComp;

    private int limitNumber = 20;

    private List<Object[]> previewData;

    /**
     * IndicatorSelectDialog constructor.
     * 
     * @param parentShell
     * @param title
     * @param modelElementIndicators
     */
    public IndicatorSelectDialog(Shell parentShell, String title, ModelElementIndicator[] modelElementIndicators,
            List<Object[]> previewData) {
        super(parentShell);
        this.title = title;
        this.previewData = previewData;
        this.modelElementIndicators = modelElementIndicators;
        int shellStyle = getShellStyle();
        setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);// MOD zshen: obtain language.
        Object editorPart = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editorPart instanceof AnalysisEditor) {
            AnalysisEditor analyEditor = (AnalysisEditor) editorPart;
            if (analyEditor.getMasterPage() instanceof ColumnAnalysisDetailsPage) {
                this.language = ExecutionLanguage.get(((ColumnAnalysisDetailsPage) analyEditor.getMasterPage()).getExecCombo()
                        .getText());
            }
        }
        // ~

        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            modelElementIndicator.copyOldIndicatorEnum();
        }
    }

    /*
     * (non-Javadoc) Method declared in Window.
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null) {
            shell.setText(title);
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        redrewComp = (Composite) super.createDialogArea(parent);
        int style = SWT.NONE;
        style |= SWT.V_SCROLL;
        style |= SWT.H_SCROLL;
        style |= SWT.BORDER;
        style |= SWT.SINGLE;
        gridPrview = new ColumnPreviewGrid(this, redrewComp, style, modelElementIndicators, this.limitNumber, previewData);
        gridIndicator = new IndicatorSelectGrid(this, redrewComp, style, modelElementIndicators, gridPrview.getColumnsWidth(),
                previewData);
        gridIndicator.setHeaderVisible(false);
        gridIndicator.addObserver(gridPrview);
        gridPrview.addObserver(gridIndicator);
        gridPrview.getParent().addControlListener(new ControlAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse.swt.events.ControlEvent)
             */
            @Override
            public void controlResized(ControlEvent e) {
                GridData indicatorGridData = (GridData) gridIndicator.getLayoutData();
                GridData previewGridData = (GridData) gridPrview.getLayoutData();
                previewGridData.horizontalAlignment = SWT.FILL;
                previewGridData.minimumWidth = 650;
                indicatorGridData.horizontalAlignment = SWT.FILL;
                indicatorGridData.minimumWidth = 650;
                gridPrview.getParent().layout();

                if (gridIndicator.getVerticalBar().isVisible() ^ gridPrview.getVerticalBar().isVisible()) {
                    if (gridPrview.getVerticalBar().isVisible()) {
                        indicatorGridData.widthHint = gridPrview.getBounds().width - 70;
                        indicatorGridData.minimumWidth = gridPrview.getBounds().width - 70;
                        indicatorGridData.horizontalAlignment = SWT.BEGINNING;
                    } else {
                        previewGridData.widthHint = gridIndicator.getBounds().width - 70;
                        previewGridData.minimumWidth = gridIndicator.getBounds().width - 70;
                        previewGridData.horizontalAlignment = SWT.BEGINNING;
                    }
                    gridPrview.getParent().layout();
                }
            }

        });

        GridData previewGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        previewGridData.minimumWidth = 650;
        previewGridData.minimumHeight = gridPrview.getItemHeight() * 10 + gridPrview.getHeaderHeight();
        previewGridData.widthHint = Math.min(IndicatorSelectGrid.COLUMN_WIDTH * modelElementIndicators.length + 400,
                getParentShell().getClientArea().width - 350);
        previewGridData.heightHint = gridPrview.getHeaderHeight() + gridPrview.getItemHeight() * 10;

        GridData indicatorGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        indicatorGridData.minimumWidth = 650;
        indicatorGridData.minimumHeight = gridIndicator.getItemHeight() * 10;
        indicatorGridData.widthHint = Math.min(IndicatorSelectGrid.COLUMN_WIDTH * modelElementIndicators.length + 400,
                getParentShell().getClientArea().width - 350);
        indicatorGridData.heightHint = getParentShell().getClientArea().height - previewGridData.heightHint;

        gridIndicator.setLayoutData(indicatorGridData);
        gridPrview.setLayoutData(previewGridData);

        Composite buttomComp = new Composite(redrewComp, SWT.BORDER);
        buttomComp.setLayout(new GridLayout());
        buttomComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // added one checkbox to hide invalid item

        createHideInvalidItemButton(buttomComp);
        purposeLabel = new Label(buttomComp, SWT.WRAP);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(purposeLabel);
        descriptionLabel = new Label(buttomComp, SWT.WRAP);
        GridDataFactory.fillDefaults().minSize(400, 30).grab(true, true).applyTo(descriptionLabel);
        // redraw gridIndicator so that gridPreview will syn width of column
        gridIndicator.redraw();
        return redrewComp;
    }

    /**
     * DOC talend Comment method "createHideInvalidItemButton".
     * 
     * @param buttomComp
     */
    private void createHideInvalidItemButton(Composite buttomComp) {
        Button hideInvalidButton = new Button(buttomComp, SWT.CHECK);
        hideInvalidButton.setText(DefaultMessagesImpl.getString("IndicatorSelectDialog.hideIndicatorCheckBoxLabel")); //$NON-NLS-1$
        hideInvalidButton.setEnabled(hasColumnSelected());
        hideInvalidButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {

                gridIndicator.hideInvalidItem(((Button) e.getSource()).getSelection());
            }

        });

    }

    public boolean isMatchCurrentIndicator(ModelElementIndicator currentIndicator, IIndicatorNode indicatorNode) {
        boolean returnValueForCurrentIndicator = true;
        IIndicatorNode parentNode = indicatorNode.getParent();
        boolean isParentPhoneStatistics = parentNode != null && parentNode.getIndicatorInstance() != null
                && parentNode.getIndicatorInstance() instanceof PhoneNumbStatisticsIndicator;
        if (!ModelElementIndicatorRule.match(indicatorNode, currentIndicator, this.language)) {
            returnValueForCurrentIndicator = false;
        }
        Indicator indicatorInstance = indicatorNode.getIndicatorInstance();
        if (null != indicatorInstance && !(indicatorInstance instanceof DatePatternFreqIndicator)
                && null != indicatorInstance.getIndicatorDefinition()
                && indicatorInstance.getIndicatorDefinition().getSqlGenericExpression().size() < 1
                && ExecutionLanguage.SQL.equals(language) && !UDIHelper.isJavaUDI(indicatorInstance)
                && !indicatorNode.hasChildren() && !(currentIndicator instanceof DelimitedFileIndicator)
                && !isParentPhoneStatistics) {
            returnValueForCurrentIndicator = false;
        }
        return returnValueForCurrentIndicator;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        parent.setLayout(new GridLayout(4, false));
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        getButton(IDialogConstants.OK_ID).setEnabled(hasColumnSelected());
    }

    public ModelElementIndicator[] getResult() {
        return gridIndicator.getResult();
    }

    public void updateIndicatorInfo(GridItem item) {
        String DESCRIPTION = DefaultMessagesImpl.getString("IndicatorSelectDialog.description"); //$NON-NLS-1$
        String PURPOSE = DefaultMessagesImpl.getString("IndicatorSelectDialog.purpose"); //$NON-NLS-1$
        IIndicatorNode indicatorNode = ((IIndicatorNode) item.getData());
        if (indicatorNode != null) {
            Indicator indicator = indicatorNode.getIndicatorInstance();
            if (indicator != null) {
                IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
                if ("".equals(MetadataHelper.getPurpose(indicatorDefinition))) {
                    purposeLabel.setText(PURPOSE + " " + indicator.getName());
                } else {
                    purposeLabel.setText(PURPOSE + " " + MetadataHelper.getPurpose(indicatorDefinition));
                }
                String description = DESCRIPTION + " " + MetadataHelper.getDescription(indicatorDefinition);
                descriptionLabel.setText(description);
                return;
            }
        }
        purposeLabel.setText(PURPOSE + " " + item.getText());
        descriptionLabel.setText(DESCRIPTION + " " + item.getText());
    }

    /**
     * select/deselect all enabled indicators when Ctrl+Shift+[A|N] is down.
     * 
     * @param selected
     */
    public void selectAllIndicators(boolean selected) {
        gridIndicator.setAllIndicators(selected);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.IIndicatorSelectDialog#getDialogComposite()
     */
    public Control getDialogControl() {
        return this.getDialogArea();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.IIndicatorSelectDialog#getDialogComposite()
     */
    public Composite getDialogComposite() {
        return redrewComp;
    }

    public void setLimitNumber(int limit) {
        limitNumber = limit;
    }

    private boolean hasColumnSelected() {
        return this.modelElementIndicators != null && this.modelElementIndicators.length > 0;
    }

    public boolean isGridPreviewColumnMoved() {
        return gridPrview.isColumnMoved;
    }
}
