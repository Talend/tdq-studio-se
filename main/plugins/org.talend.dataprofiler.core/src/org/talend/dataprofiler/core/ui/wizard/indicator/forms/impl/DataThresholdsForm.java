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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.CheckValueUtils;
import org.talend.dataprofiler.core.ui.utils.DateTimeDialog;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class DataThresholdsForm extends AbstractIndicatorForm {

    protected static final String LOWER_LESS_HIGHER = UIMessages.MSG_LOWER_LESS_HIGHER;

    protected Text lowerText, higherText;

    protected Group group;

    private boolean isRangeForDate;

    private boolean isDatetime;

    private Button lowerBTN, higherBTN;

    private Button lowerDelBTN, higherDelBTN;

    public DataThresholdsForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
        // MOD qiongli 2011-11-25,TDQ-4033,consider the date type.
        Indicator currentIndicator = (Indicator) parameters.eContainer();
        IndicatorEnum currentIndicatorType = IndicatorEnum.findIndicatorEnum(currentIndicator.eClass());
        ModelElement analyzedElement = currentIndicator.getAnalyzedElement();
        if (null != analyzedElement) {
            int sqlType = 0;
            if (SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(analyzedElement) != null) {
                isRangeForDate = false;
                isDatetime = false;
            } else if (SwitchHelpers.COLUMN_SWITCH.doSwitch(analyzedElement) == null
                    && SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(analyzedElement) != null) {
                MetadataColumn mColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(analyzedElement);
                sqlType = TalendTypeConvert.convertToJDBCType(mColumn.getTalendType());
                isRangeForDate = Java2SqlType.isDateInSQL(sqlType) && currentIndicatorType == IndicatorEnum.RangeIndicatorEnum;
            } else {
                sqlType = ((TdColumn) analyzedElement).getSqlDataType().getJavaDataType();
                isRangeForDate = Java2SqlType.isDateInSQL(sqlType) && currentIndicatorType == IndicatorEnum.RangeIndicatorEnum;
            }
            if (isRangeForDate) {
                isDatetime = Java2SqlType.isDateTimeSQL(sqlType);
            }
        }
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        group = new Group(this, SWT.NONE);
        int colsForLayout = 2;
        if (isRangeForDate) {
            colsForLayout = 4;
        }
        group.setLayout(new GridLayout(colsForLayout, false));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText(DefaultMessagesImpl.getString("DataThresholdsForm.setThresholds")); //$NON-NLS-1$

        GridData gdText = new GridData(GridData.FILL_HORIZONTAL);

        Label lowerLabel = new Label(group, SWT.NONE);
        lowerLabel.setText(DefaultMessagesImpl.getString("DataThresholdsForm.lowerThreshold")); //$NON-NLS-1$
        lowerText = new Text(group, SWT.BORDER);
        lowerText.setLayoutData(gdText);

        // MOD qiongli 2011-11-25,TDQ-4033,consider the date type.
        if (isRangeForDate) {
            lowerBTN = new Button(group, SWT.PUSH);
            lowerBTN.setText("..."); //$NON-NLS-1$
            lowerBTN.setToolTipText(DefaultMessagesImpl.getString("IndicatorThresholdsForm.SelectLowerThreshold")); //$NON-NLS-1$
            lowerDelBTN = new Button(group, SWT.PUSH);
            lowerDelBTN.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
            lowerDelBTN.setToolTipText(DefaultMessagesImpl.getString("IndicatorThresholdsForm.RemoveLowerThreshold")); //$NON-NLS-1$

            lowerText.setEditable(false);
            lowerText.setEnabled(false);
        }

        Label higherLabel = new Label(group, SWT.NONE);
        higherLabel.setText(DefaultMessagesImpl.getString("DataThresholdsForm.higherThreshold")); //$NON-NLS-1$
        higherText = new Text(group, SWT.BORDER);
        higherText.setLayoutData(gdText);

        if (isRangeForDate) {
            higherBTN = new Button(group, SWT.PUSH);
            higherBTN.setText("..."); //$NON-NLS-1$
            higherBTN.setToolTipText(DefaultMessagesImpl.getString("IndicatorThresholdsForm.SelectHigherThreshold")); //$NON-NLS-1$
            higherDelBTN = new Button(group, SWT.PUSH);
            higherDelBTN.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
            higherDelBTN.setToolTipText(DefaultMessagesImpl.getString("IndicatorThresholdsForm.RemoveHigherThreshold")); //$NON-NLS-1$

            higherText.setEditable(false);
            higherText.setEnabled(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        lowerText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                String min = lowerText.getText();
                String max = higherText.getText();
                if (isRangeForDate) {
                    ReturnCode rc0 = checkDateFilds(min, max);
                    if (rc0.isOk()) {
                        updateStatus(IStatus.OK, MSG_OK);
                    } else {
                        updateStatus(IStatus.ERROR, rc0.getMessage());
                    }
                } else {
                    if (!min.trim().equals("") && !CheckValueUtils.isRealNumberValue(min)) { //$NON-NLS-1$
                        updateStatus(IStatus.ERROR, MSG_ONLY_NUMBER);
                    } else if (CheckValueUtils.isAoverB(min, max)) {
                        updateStatus(IStatus.ERROR, UIMessages.MSG_LOWER_LESS_HIGHER);
                    } else {
                        updateStatus(IStatus.OK, MSG_OK);
                    }
                }
            }

        });

        higherText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                String max = higherText.getText();
                String min = lowerText.getText();
                if (isRangeForDate) {
                    ReturnCode rc0 = checkDateFilds(min, max);
                    if (rc0.isOk()) {
                        updateStatus(IStatus.OK, MSG_OK);
                    } else {
                        updateStatus(IStatus.ERROR, rc0.getMessage());
                    }
                } else {
                    if (!max.trim().equals("") && !CheckValueUtils.isRealNumberValue(max)) { //$NON-NLS-1$
                        updateStatus(IStatus.ERROR, MSG_ONLY_NUMBER);
                    } else if (CheckValueUtils.isAoverB(min, max)) {
                        updateStatus(IStatus.ERROR, UIMessages.MSG_LOWER_LESS_HIGHER);
                    } else {
                        updateStatus(IStatus.OK, MSG_OK);
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
        String[] dataThreshold = IndicatorHelper.getDataThreshold(parameters);
        if (dataThreshold != null) {
            lowerText.setText(dataThreshold[0]);

            higherText.setText(dataThreshold[1]);
        }
    }

    @Override
    public FormEnum getFormEnum() {
        return FormEnum.DataThresholdsForm;
    }

    @Override
    public boolean performFinish() {
        String min = lowerText.getText();
        String max = higherText.getText();

        if ("".equals(min) && "".equals(max)) { //$NON-NLS-1$ //$NON-NLS-2$
            parameters.setDataValidDomain(null);
        } else {
            IndicatorHelper.setDataThreshold(parameters, min, max);
            Indicator indicator = (Indicator) parameters.eContainer();
            IndicatorHelper.propagateDataThresholdsInChildren(indicator);
        }

        return true;
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
     * Add qiongli 2011-11-25 TDQ-4033.
     */
    @Override
    protected void addUtilsButtonListeners() {

        if (isRangeForDate) {
            lowerBTN.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    DateTimeDialog dialog = new DateTimeDialog(null, isDatetime);
                    if (Window.OK == dialog.open()) {
                        lowerText.setText(dialog.getSelectDate());
                    }
                }
            });

            higherBTN.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    DateTimeDialog dialog = new DateTimeDialog(null, isDatetime);
                    if (Window.OK == dialog.open()) {
                        higherText.setText(dialog.getSelectDate());
                    }
                }
            });

            lowerDelBTN.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    lowerText.setText(""); //$NON-NLS-1$
                }
            });

            higherDelBTN.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    higherText.setText(""); //$NON-NLS-1$
                }
            });
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        return false;
    }

    protected ReturnCode checkDateFilds(String min, String max) {

        ReturnCode rc = new ReturnCode(true);
        String statusLabelText = PluginConstant.EMPTY_STRING;
        if ((!CheckValueUtils.isDateValue(min) && !CheckValueUtils.isEmpty(min))
                || (!CheckValueUtils.isDateValue(max) && !CheckValueUtils.isEmpty(max))) {

            rc.setOk(false);
            statusLabelText += MSG_ONLY_DATE + System.getProperty("line.separator"); //$NON-NLS-1$
        }
        if (CheckValueUtils.isAoverB(min, max)) {
            rc.setOk(false);
            statusLabelText += UIMessages.MSG_LOWER_LESS_HIGHER + System.getProperty("line.separator"); //$NON-NLS-1$
        }

        rc.setMessage(statusLabelText);
        return rc;
    }
}
