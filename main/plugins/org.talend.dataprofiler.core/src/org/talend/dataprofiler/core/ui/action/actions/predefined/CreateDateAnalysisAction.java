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
package org.talend.dataprofiler.core.ui.action.actions.predefined;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnWizard;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm.ICheckListener;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.impl.freq.FreqTimeSliceForm;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class CreateDateAnalysisAction extends AbstractPredefinedAnalysisAction {

    private IndicatorParameters parameters;

    public CreateDateAnalysisAction() {
        super(DefaultMessagesImpl.getString("CreateDateAnalysisAction.timeAnalysis"), null); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getPredefinedColumnIndicator()
     */
    @Override
    protected ModelElementIndicator[] getPredefinedColumnIndicator() {

        int count = 5;
        if (isTimeType()) {
            count = 3;
        }
        IndicatorEnum[] allwedEnumes = new IndicatorEnum[count];
        allwedEnumes[0] = IndicatorEnum.CountsIndicatorEnum;
        allwedEnumes[1] = IndicatorEnum.MinValueIndicatorEnum;
        allwedEnumes[2] = IndicatorEnum.MaxValueIndicatorEnum;
        if (count == 5) {
            allwedEnumes[3] = IndicatorEnum.LowFrequencyIndicatorEnum;
            allwedEnumes[4] = IndicatorEnum.FrequencyIndicatorEnum;
        }

        ModelElementIndicator[] returnColumnIndicator = composePredefinedColumnIndicator(allwedEnumes);

        if (parameters != null) {
            for (ModelElementIndicator columnIndicator : returnColumnIndicator) {
                for (Indicator indicator : columnIndicator.getIndicators()) {
                    if (indicator instanceof FrequencyIndicator) {
                        if (indicator.getParameters().getDateParameters() != null) {// TODO If we will never use the
                                                                                    // "parameters" parameter, why we
                                                                                    // add TimeSliceOptionPage and have
                                                                                    // this loop
                            indicator.getParameters().getDateParameters()
                                    .setDateAggregationType(parameters.getDateParameters().getDateAggregationType());
                        }
                    }
                }
            }
        }

        return returnColumnIndicator;
    }

    // Added yyin2012-05-14 TDQ-5241
    private boolean isTimeType() {
        for (IRepositoryNode repositoryNode : getColumns()) {
            MetadataColumn column = ((MetadataColumnRepositoryObject) repositoryNode.getObject()).getTdColumn();
            int javaSQLType = ((TdColumn) column).getSqlDataType().getJavaDataType();

            if (Java2SqlType.isTimeSQL(javaSQLType)) {
                return true;
            }
        }

        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#isAllowed()
     */
    @Override
    protected boolean isAllowed() {
        for (IRepositoryNode repositoryNode : getColumns()) {
            MetadataColumn column = ((MetadataColumnRepositoryObject) repositoryNode.getObject()).getTdColumn();

            int javaSQLType = TalendTypeConvert.convertToJDBCType(column.getTalendType());

            if (!Java2SqlType.isDateInSQL(javaSQLType)) {
                return false;
            }
        }
        if (!RepNodeUtils.isValidSelectionFromSameTable(getSelection().toList())) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#preDo()
     */
    @Override
    protected boolean preDo() {

        return true;
    }

    /**
     * DOC zqin CreateDateAnalysisAction class global comment. Detailled comment
     */
    public class TimeSliceOptionPage extends WizardPage {

        private ICheckListener listener;

        public TimeSliceOptionPage() {
            super(DefaultMessagesImpl.getString("CreateDateAnalysisAction.createNewAnalysis")); //$NON-NLS-1$
            setTitle(DefaultMessagesImpl.getString("CreateDateAnalysisAction.newAnalysis")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("CreateDateAnalysisAction.addOption")); //$NON-NLS-1$

            parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
            DateParameters dateParameters = parameters.getDateParameters();
            if (dateParameters == null) {
                dateParameters = IndicatorsFactory.eINSTANCE.createDateParameters();
                parameters.setDateParameters(dateParameters);
            }

            this.listener = new AbstractForm.ICheckListener() {

                public void checkPerformed(AbstractForm source) {
                    if (source.isStatusOnError()) {
                        TimeSliceOptionPage.this.setPageComplete(false);
                        setErrorMessage(source.getStatus());
                    } else {
                        TimeSliceOptionPage.this.setPageComplete(true);
                        setErrorMessage(null);
                        setMessage(source.getStatus(), source.getStatusLevel());
                    }
                }

            };
        }

        public void createControl(Composite parent) {

            Composite comp = new Composite(parent, SWT.NONE);
            comp.setLayout(new GridLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));

            FreqTimeSliceForm timeSliceForm = new FreqTimeSliceForm(comp, SWT.NONE, parameters);
            timeSliceForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            timeSliceForm.setListener(listener);

            setControl(comp);
        }

    }

    @Override
    protected WizardDialog getPredefinedDialog() {
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setConnectionRepNode(getColumns()[0]);
        ColumnWizard wizard = (ColumnWizard) WizardFactory.createAnalysisWizard(AnalysisType.MULTIPLE_COLUMN, parameter);
        wizard.setForcePreviousAndNextButtons(true);
        TimeSliceOptionPage page = new TimeSliceOptionPage();
        wizard.setExtenalPages(new WizardPage[] { page });

        return new OpeningHelpWizardDialog(null, wizard, FormEnum.TimeSlicesForm.getHelpHref(), page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getCategoryLabel()
     */
    @Override
    protected String getCategoryLabel() {
        return null;
    }
}
