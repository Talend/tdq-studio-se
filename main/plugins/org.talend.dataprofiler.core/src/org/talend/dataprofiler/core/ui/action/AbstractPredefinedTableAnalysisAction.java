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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.BusinessRuleAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.core.ui.wizard.analysis.table.TableAnalysisWizard;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public abstract class AbstractPredefinedTableAnalysisAction extends Action {

    private TreeSelection selection;

    private Wizard wizard;

    private final ImageDescriptor defaultImage = ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS);

    public AbstractPredefinedTableAnalysisAction(String name, ImageDescriptor image) {
        super(name);
        if (image == null) {
            super.setImageDescriptor(defaultImage);
        } else {
            super.setImageDescriptor(image);
        }
    }

    public TreeSelection getSelection() {
        return selection;
    }

    public void setSelection(TreeSelection selection) {
        this.selection = selection;
    }

    protected NamedColumnSet[] getTablesAndViews() {
        List<NamedColumnSet> list = new ArrayList<NamedColumnSet>();

        Object[] array = getSelection().toArray();
        for (Object obj : array) {
            if (obj instanceof DBTableRepNode) {
                list.add(((DBTableRepNode) obj).getTdTable());
            } else if (obj instanceof DBViewRepNode) {
                list.add(((DBViewRepNode) obj).getTdView());
            }

        }

        return list.toArray(new NamedColumnSet[list.size()]);

    }

    protected Connection getTdDataProvidor() {
        Object obj = getSelection().getFirstElement();
        if (obj instanceof DBTableRepNode) {
            return ConnectionHelper.getTdDataProvider(PackageHelper.getParentPackage(((DBTableRepNode) obj).getTdTable()));
        } else if (obj instanceof DBViewRepNode) {
            return ConnectionHelper.getTdDataProvider(PackageHelper.getParentPackage(((DBViewRepNode) obj).getTdView()));
        }
        return null;
    }

    protected WizardDialog getStandardAnalysisWizardDialog() {
        return getStandardAnalysisWizardDialog(AnalysisType.TABLE);
    }

    protected WizardDialog getStandardAnalysisWizardDialog(AnalysisType type) {
        return getStandardAnalysisWizardDialog(type, null);
    }

    protected WizardDialog getStandardAnalysisWizardDialog(AnalysisType type, AnalysisParameter parameter) {
        // MOD by zshen for bug 12657
        wizard = WizardFactory.createAnalysisWizard(type, parameter);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);
        return dialog;
    }

    protected BusinessRuleAnalysisDetailsPage getMasterPage() {
        AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        if (editor != null) {
            return (BusinessRuleAnalysisDetailsPage) editor.getMasterPage();
        } else {
            return null;
        }
    }

    protected TableIndicator[] composePredefinedTableIndicator(IndicatorEnum[] allowedEnum) {
        NamedColumnSet[] tablesAndViews = getTablesAndViews();
        TableIndicator[] predefinedTableIndicator = new TableIndicator[tablesAndViews.length];
        for (int i = 0; i < tablesAndViews.length; i++) {
            NamedColumnSet tableOrView = tablesAndViews[i];
            TableIndicator tableIndicator = new TableIndicator(tableOrView);
            predefinedTableIndicator[i] = tableIndicator;
        }
        return predefinedTableIndicator;
    }

    @Override
    public void run() {
        if (preDo()) {
            WizardDialog dialog = getPredefinedDialog();
            if (dialog == null) {
                dialog = getStandardAnalysisWizardDialog();
            }
            dialog.setPageSize(500, 340);
            if (wizard instanceof TableAnalysisWizard) {
                TableAnalysisWizard taw = (TableAnalysisWizard) wizard;
                taw.setNamedColumnSet(getTablesAndViews());
                taw.setTdDataProvider(getTdDataProvidor());
                taw.setShowTableSelectPage(false);
            }
            dialog.open();
        }
    }

    protected abstract TableIndicator[] getPredefinedTableIndicator();

    protected abstract WizardDialog getPredefinedDialog();

    protected abstract boolean isAllowed();

    protected abstract boolean preDo();
}
