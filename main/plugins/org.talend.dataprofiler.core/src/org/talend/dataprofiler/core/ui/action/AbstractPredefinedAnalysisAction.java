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
package org.talend.dataprofiler.core.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataXmlElementTypeRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.utils.ModelElementIndicatorRule;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zqin class global comment. Detailled comment
 */
public abstract class AbstractPredefinedAnalysisAction extends Action {

    private StructuredSelection selection;

    private final ImageDescriptor defaultImage = ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS);

    public AbstractPredefinedAnalysisAction(String name, ImageDescriptor image) {
        super(name);
        if (image == null) {
            super.setImageDescriptor(defaultImage);
        } else {
            super.setImageDescriptor(image);
        }
    }

    public StructuredSelection getSelection() {
        return selection;
    }

    public void setSelection(StructuredSelection selection) {
        this.selection = selection;
    }

    protected IRepositoryNode[] getColumns() {
        return RepositoryNodeHelper.getAllColumnNodes(getSelection().toArray());
    }

    /**
     * DOC qzhang Comment method "getStandardAnalysisWizardDialog".
     * 
     * @return
     */
    protected WizardDialog getStandardAnalysisWizardDialog() {
        return getStandardAnalysisWizardDialog(AnalysisType.MULTIPLE_COLUMN);
    }

    protected WizardDialog getStandardAnalysisWizardDialog(AnalysisType type) {
        // MOD klliu 2011-02-17 feature 15387
        AnalysisLabelParameter parameter = null;
        Object firstElement = this.selection.getFirstElement();

        if (firstElement instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) firstElement;
            Item item = node.getObject().getProperty().getItem();
            if (item instanceof ConnectionItem) {
                ConnectionItem connectionItem = (ConnectionItem) item;
                Connection connection = connectionItem.getConnection();

                IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
                parameter = new AnalysisLabelParameter();
                parameter.setCategoryLabel(getCategoryLabel());
                parameter.setColumnNodes(getColumns());
                parameter.setConnectionRepNode(repositoryNode);

                return getStandardAnalysisWizardDialog(type, parameter);
            }
        } else if (firstElement instanceof TdTable) {
            Connection connection = ConnectionHelper.getConnection((TdTable) firstElement);
            IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
            parameter = new AnalysisLabelParameter();
            parameter.setCategoryLabel(getCategoryLabel());
            parameter.setColumnNodes(getColumns());
            parameter.setConnectionRepNode(repositoryNode);
            return getStandardAnalysisWizardDialog(type, parameter);
        } else if (firstElement instanceof TdView) { // Added yyin 20120522 TDQ-4945, support tdView
            Connection connection = ConnectionHelper.getConnection((TdView) firstElement);
            IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
            parameter = new AnalysisLabelParameter();
            parameter.setCategoryLabel(getCategoryLabel());
            parameter.setColumnNodes(getColumns());
            parameter.setConnectionRepNode(repositoryNode);
            return getStandardAnalysisWizardDialog(type, parameter);
            // ~4945
        }
        // if (firstElement instanceof DBTableRepNode) {
        // DBTableRepNode tableNode = (DBTableRepNode) firstElement;
        // TdTableRepositoryObject viewObject = (TdTableRepositoryObject) tableNode.getObject();
        // Item item = viewObject.getProperty().getItem();
        // DatabaseConnectionItem databaseConnectionItem = (DatabaseConnectionItem) item;
        // Connection connection = databaseConnectionItem.getConnection();
        //
        // IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
        // parameter = new AnalysisParameter();
        // parameter.setConnectionRepNode((DBConnectionRepNode) repositoryNode);
        // return getStandardAnalysisWizardDialog(type, parameter);
        // } else if (firstElement instanceof TdTable) {
        // Connection connection = ConnectionHelper.getConnection((TdTable) firstElement);
        // IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
        // parameter = new AnalysisParameter();
        // parameter.setConnectionRepNode((DBConnectionRepNode) repositoryNode);
        // return getStandardAnalysisWizardDialog(type, parameter);
        // } else if (firstElement instanceof DBColumnRepNode) {
        // // MOD klliu 2011-02-23 feature 15387
        // DBColumnRepNode columnNode = (DBColumnRepNode) firstElement;
        // MetadataColumnRepositoryObject viewObject = (MetadataColumnRepositoryObject) columnNode.getObject();
        // Item item = viewObject.getProperty().getItem();
        // DatabaseConnectionItem databaseConnectionItem = (DatabaseConnectionItem) item;
        // Connection connection = databaseConnectionItem.getConnection();
        //
        // IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
        // parameter = new AnalysisParameter();
        // parameter.setConnectionRepNode((DBConnectionRepNode) repositoryNode);
        // return getStandardAnalysisWizardDialog(type, parameter);
        // }

        return getStandardAnalysisWizardDialog(type, null);
    }

    protected WizardDialog getStandardAnalysisWizardDialog(AnalysisType type, AnalysisParameter parameter) {
        Wizard wizard = WizardFactory.createAnalysisWizard(type, parameter);
        wizard.setForcePreviousAndNextButtons(true);

        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        return dialog;
    }

    protected ColumnAnalysisDetailsPage getMasterPage() {
        AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();

        if (editor != null) {
            return (ColumnAnalysisDetailsPage) editor.getMasterPage();
        } else {
            return null;
        }
    }

    protected ModelElementIndicator[] composePredefinedColumnIndicator(IndicatorEnum[] allowedEnum) {
        IRepositoryNode[] columns = getColumns();
        ModelElementIndicator[] predefinedColumnIndicator = new ModelElementIndicator[columns.length];
        ExecutionLanguage language = ExecutionLanguage.get(getMasterPage().getExecCombo().getText());
        for (int i = 0; i < columns.length; i++) {
            IRepositoryNode columnNode = columns[i];
            ModelElementIndicator columnIndicator = ModelElementIndicatorHelper.createModelElementIndicator(columnNode);

            for (IndicatorEnum oneEnum : allowedEnum) {
                columnIndicator.addTempIndicatorEnum(oneEnum);
                if (oneEnum.getChildren() != null) {
                    for (IndicatorEnum childEnum : oneEnum.getChildren()) {
                        // MOD by zshen:need language to decide DatePatternFrequencyIndicator whether can be choose by
                        // user.
                        IRepositoryViewObject object = columnNode.getObject();

                        ModelElement element = null;
                        if (object instanceof MetadataColumnRepositoryObject) {
                            element = ((MetadataColumnRepositoryObject) object).getTdColumn();
                        } else if (object instanceof MetadataXmlElementTypeRepositoryObject) {
                            element = ((MetadataXmlElementTypeRepositoryObject) object).getModelElement();
                        }

                        if (element != null && ModelElementIndicatorRule.patternRule(childEnum, element, language)) {
                            columnIndicator.addTempIndicatorEnum(childEnum);
                        }
                    }
                }
            }

            columnIndicator.storeTempIndicator();

            predefinedColumnIndicator[i] = columnIndicator;
        }

        return predefinedColumnIndicator;
    }

    @Override
    public void run() {

        // do something before open the wizard
        if (preDo()) {
            WizardDialog dialog = getPredefinedDialog();
            if (dialog == null) {
                dialog = getStandardAnalysisWizardDialog();
            }
            dialog.setPageSize(500, 340);

            if (dialog.open() == Window.OK) {
                ModelElementIndicator[] predefinedColumnIndicator = getPredefinedColumnIndicator();
                if (predefinedColumnIndicator != null) {
                    ColumnAnalysisDetailsPage masterPage = getMasterPage();
                    if (masterPage != null) {
                        masterPage.refreshPreviewTable(predefinedColumnIndicator, false);
                        masterPage.refreshTheTree(predefinedColumnIndicator);
                        if (needChangeExecuteLanguageToJava()) {
                            masterPage.changeExecuteLanguageToJava(true);
                        }
                        masterPage.doSave(null);
                    }
                }
            }
        }
    }

    protected abstract ModelElementIndicator[] getPredefinedColumnIndicator();

    protected abstract String getCategoryLabel();

    protected abstract WizardDialog getPredefinedDialog();

    protected abstract boolean isAllowed();

    protected abstract boolean preDo();

    protected boolean needChangeExecuteLanguageToJava() {
        return false;
    }
}
