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
package org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.CheckValueUtils;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.domain.RealNumberValue;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class BinsDesignerForm extends AbstractIndicatorForm {

    private Text minValue, maxValue, numbOfBins;

    private Button addSlice, delSlice;

    private Button isSetRange;

    private TableViewer tableViewer;

    public BinsDesignerForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        this.setLayout(new GridLayout(2, false));

        GridData gdText = new GridData(GridData.FILL_HORIZONTAL);

        Label minValueText = new Label(this, SWT.NONE);
        minValueText.setText(DefaultMessagesImpl.getString("BinsDesignerForm.minimalValue")); //$NON-NLS-1$
        minValue = new Text(this, SWT.BORDER);
        minValue.setLayoutData(gdText);

        Label maxValueText = new Label(this, SWT.NONE);
        maxValueText.setText(DefaultMessagesImpl.getString("BinsDesignerForm.maximalValue")); //$NON-NLS-1$
        maxValue = new Text(this, SWT.BORDER);
        maxValue.setLayoutData(gdText);

        Label numOfBinsText = new Label(this, SWT.NONE);
        numOfBinsText.setText(DefaultMessagesImpl.getString("BinsDesignerForm.numberOfBins")); //$NON-NLS-1$

        GridData gdTxt = new GridData();
        gdTxt.widthHint = 50;
        numbOfBins = new Text(this, SWT.BORDER);
        numbOfBins.setLayoutData(gdTxt);

        Composite rangeComp = new Composite(this, SWT.NONE);
        rangeComp.setLayout(new GridLayout());
        GridData gdComp = new GridData(GridData.FILL_BOTH);
        gdComp.horizontalSpan = 2;
        rangeComp.setLayoutData(gdComp);

        isSetRange = new Button(rangeComp, SWT.CHECK);
        isSetRange.setText(DefaultMessagesImpl.getString("BinsDesignerForm.setManually")); //$NON-NLS-1$

        tableViewer = new TableViewer(rangeComp, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        tableViewer.setLabelProvider(new BinsLableProvider());
        tableViewer.setContentProvider(new BinsContentProvider());

        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        GridData tableData = new GridData(GridData.FILL_HORIZONTAL);
        tableData.heightHint = 80;
        table.setLayoutData(tableData);

        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setText(DefaultMessagesImpl.getString("BinsDesignerForm.low")); //$NON-NLS-1$
        column1.setWidth(150);
        column1.setAlignment(SWT.CENTER);
        TableColumn column2 = new TableColumn(table, SWT.NONE);
        column2.setWidth(150);
        column2.setText(DefaultMessagesImpl.getString("BinsDesignerForm.data")); //$NON-NLS-1$
        column2.setAlignment(SWT.CENTER);
        TableColumn column3 = new TableColumn(table, SWT.NONE);
        column3.setWidth(150);
        column3.setText(DefaultMessagesImpl.getString("BinsDesignerForm.high")); //$NON-NLS-1$
        column3.setAlignment(SWT.CENTER);

        tableViewer.setColumnProperties(new String[] { "low", "data", "high" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        CellEditor[] cellEditor = new CellEditor[3];
        cellEditor[0] = new TextCellEditor(table);
        cellEditor[1] = null;
        cellEditor[2] = new TextCellEditor(table);

        tableViewer.setCellEditors(cellEditor);
        tableViewer.setCellModifier(new SliceCellModifier());

        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.CENTER;
        Composite operationBTNComp = new Composite(rangeComp, SWT.NONE);
        operationBTNComp.setLayout(new FillLayout());
        operationBTNComp.setLayoutData(gd);

        addSlice = new Button(operationBTNComp, SWT.NONE);
        addSlice.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addSlice.setEnabled(false);

        delSlice = new Button(operationBTNComp, SWT.NONE);
        delSlice.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delSlice.setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        // MOD hcheng for 7377,only when minValue,maxValue,numbOfBins are set, the wizard can finish
        minValue.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String mintxt = minValue.getText();
                String maxtxt = maxValue.getText();
                String bintxt = numbOfBins.getText();

                if (mintxt != "") { //$NON-NLS-1$
                    if (!CheckValueUtils.isRealNumberValue(mintxt)) {
                        updateStatus(IStatus.ERROR, MSG_ONLY_REAL_NUMBER);
                    } else if (!maxtxt.equals("") && CheckValueUtils.isAoverB(mintxt, maxtxt)) { //$NON-NLS-1$
                        updateStatus(IStatus.ERROR, UIMessages.MSG_LOWER_LESS_HIGHER);
                    } else if (!maxtxt.equals("") && !bintxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.OK, MSG_OK);
                    } else {
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    }

                } else {
                    if (!maxtxt.equals("") || !bintxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    } else {
                        updateStatus(IStatus.OK, UIMessages.MSG_INDICATOR_WIZARD);
                    }
                }
            }

        });

        maxValue.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String mintxt = minValue.getText();
                String maxtxt = maxValue.getText();
                String bintxt = numbOfBins.getText();
                if (maxtxt != "") { //$NON-NLS-1$
                    if (!CheckValueUtils.isRealNumberValue(maxtxt)) {
                        updateStatus(IStatus.ERROR, MSG_ONLY_REAL_NUMBER);
                    } else if (!mintxt.equals("") && CheckValueUtils.isAoverB(mintxt, maxtxt)) { //$NON-NLS-1$
                        updateStatus(IStatus.ERROR, UIMessages.MSG_LOWER_LESS_HIGHER);
                    } else if (!mintxt.equals("") && !bintxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.OK, MSG_OK);
                    } else {
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    }
                } else {
                    if (!mintxt.equals("") || !bintxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    } else {
                        updateStatus(IStatus.OK, UIMessages.MSG_INDICATOR_WIZARD);
                    }
                }
            }

        });

        numbOfBins.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String mintxt = minValue.getText();
                String maxtxt = maxValue.getText();
                String numbtxt = numbOfBins.getText();

                if (numbtxt != "") { //$NON-NLS-1$
                    if (!CheckValueUtils.isNumberValue(numbtxt)) {
                        updateStatus(IStatus.ERROR, MSG_ONLY_NUMBER);
                    } else if (!mintxt.equals("") && !maxtxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.OK, MSG_OK);
                    } else {
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    }
                } else {
                    if (!mintxt.equals("") || !maxtxt.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        updateStatus(IStatus.ERROR, MSG_EMPTY);
                    } else {
                        updateStatus(IStatus.OK, UIMessages.MSG_INDICATOR_WIZARD);
                    }

                }
            }

        });

        isSetRange.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                boolean flag = ((Button) e.getSource()).getSelection();
                boolean validSelect = !CheckValueUtils.isEmpty(numbOfBins.getText(), minValue.getText(), maxValue.getText());

                if (flag && validSelect) {
                    addSlice.setEnabled(true);
                    delSlice.setEnabled(true);

                    minValue.setEnabled(false);
                    maxValue.setEnabled(false);
                    numbOfBins.setEnabled(false);

                    int numb = Integer.parseInt(numbOfBins.getText());
                    double min = Double.parseDouble(minValue.getText());
                    double max = Double.parseDouble(maxValue.getText());
                    Domain customerDomin = DomainHelper.createContiguousClosedBinsIntoDomain("", numb, min, max); //$NON-NLS-1$
                    tableViewer.setInput(customerDomin.getRanges());

                } else {
                    addSlice.setEnabled(false);
                    delSlice.setEnabled(false);

                    minValue.setEnabled(true);
                    maxValue.setEnabled(true);
                    numbOfBins.setEnabled(true);

                    tableViewer.setInput(""); //$NON-NLS-1$
                }
            }

        });

        addSlice.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (tableViewer.getInput() instanceof List) {
                    List<RangeRestriction> inputList = (List<RangeRestriction>) tableViewer.getInput();

                    // create a default range restriction, the min = 0, the max = 0
                    RangeRestriction newRange = DomainHelper.createRealRangeRestriction(0, 0);
                    inputList.add(newRange);

                    tableViewer.setInput(inputList);
                }
            }

        });

        delSlice.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean flag = !tableViewer.getSelection().isEmpty();

                if (flag) {
                    IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
                    RangeRestriction range = (RangeRestriction) selection.getFirstElement();
                    if (tableViewer.getInput() instanceof List) {
                        List<RangeRestriction> inputList = (List<RangeRestriction>) tableViewer.getInput();
                        inputList.remove(range);

                        tableViewer.setInput(inputList);
                    }
                }
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        Domain domain = parameters.getBins();

        if (domain != null) {
            minValue.setText(String.valueOf(DomainHelper.getMinBinValue(domain)));
            maxValue.setText(String.valueOf(DomainHelper.getMaxBinValue(domain)));
            numbOfBins.setText(String.valueOf(DomainHelper.getNumberOfBins(domain)));

            EList<RangeRestriction> ranges = domain.getRanges();

            if (!ranges.isEmpty()) {
                addSlice.setEnabled(true);
                delSlice.setEnabled(true);

                minValue.setEnabled(false);
                maxValue.setEnabled(false);
                numbOfBins.setEnabled(false);
                isSetRange.setSelection(true);

                tableViewer.setInput(ranges);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean performFinish() {
        if (minValue.getText().equals("") || maxValue.getText().equals("") || numbOfBins.getText().equals("0") || numbOfBins.getText().equals("")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            parameters.setBins(null);

        } else if (!minValue.getText().equals("") && !maxValue.getText().equals("") && !numbOfBins.getText().equals("") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                && !numbOfBins.getText().equals("0")) { //$NON-NLS-1$
            double min = Double.parseDouble(minValue.getText());
            double max = Double.parseDouble(maxValue.getText());
            int numb = Integer.parseInt(numbOfBins.getText());

            Object inputList = tableViewer.getInput();
            Domain domain = DomainHelper.createDomain("test"); //$NON-NLS-1$

            if (inputList != null && (inputList instanceof List)) {
                List<RangeRestriction> eInputList = (List<RangeRestriction>) inputList;
                domain.getRanges().addAll(eInputList);
                parameters.setBins(domain);
            } else {
                domain = DomainHelper.createContiguousClosedBinsIntoDomain("test", numb, min, max); //$NON-NLS-1$
                parameters.setBins(domain);
            }
        }

        return true;
    }

    @Override
    public FormEnum getFormEnum() {
        return FormEnum.BinsDesignerForm;
    }

    @Override
    protected boolean checkFieldsValue() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {

    }

    @Override
    protected void updateStatus(int status, String statusLabelText) {
        super.updateStatus(status, statusLabelText);
        if (status == IStatus.ERROR) {
            isSetRange.setEnabled(false);
        } else {
            isSetRange.setEnabled(true);
        }
    }

    /**
     * DOC zqin BinsDesignerForm class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class BinsLableProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            RangeRestriction range = (RangeRestriction) element;
            switch (columnIndex) {
            case 0:
                return DomainHelper.getMinValue(range);
            case 1:
                return PluginConstant.LESS_OR_EQUAL + " value <"; //$NON-NLS-1$
            case 2:
                return DomainHelper.getMaxValue(range);
            default:
                return ""; //$NON-NLS-1$
            }
        }
    }

    /**
     * DOC zqin BinsDesignerForm class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class BinsContentProvider implements IStructuredContentProvider {

        @SuppressWarnings("unchecked")
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                return ((List<RangeRestriction>) inputElement).toArray();
            }

            return new Object[0];
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /**
     * DOC zqin BinsDesignerForm class global comment. Detailled comment
     */
    class SliceCellModifier implements ICellModifier {

        public boolean canModify(Object element, String property) {
            if (property.equals("data")) { //$NON-NLS-1$
                return false;
            }

            return true;
        }

        public Object getValue(Object element, String property) {
            RangeRestriction range = (RangeRestriction) element;

            if (property.equals("low")) { //$NON-NLS-1$
                double lowerValue = DomainHelper.getRealValue(range.getLowerValue());
                return String.valueOf(lowerValue);
            } else if (property.equals("high")) { //$NON-NLS-1$
                double upperValue = DomainHelper.getRealValue(range.getUpperValue());
                return String.valueOf(upperValue);
            }

            return ""; //$NON-NLS-1$
        }

        public void modify(Object element, String property, Object value) {

            if (CheckValueUtils.isRealNumberValue(value.toString())) {
                updateStatus(IStatus.OK, MSG_OK);
                TableItem item = (TableItem) element;
                RangeRestriction range = (RangeRestriction) item.getData();

                RealNumberValue realNumberValue = DomainHelper.createRealNumberValue(null, Double.valueOf(value.toString()));
                if (property.equals("low")) { //$NON-NLS-1$
                    range.setLowerValue(realNumberValue);
                } else if (property.equals("high")) { //$NON-NLS-1$
                    range.setUpperValue(realNumberValue);
                }
            } else {
                updateStatus(IStatus.ERROR, MSG_ONLY_REAL_NUMBER);
            }

            tableViewer.refresh();
        }
    }
}
