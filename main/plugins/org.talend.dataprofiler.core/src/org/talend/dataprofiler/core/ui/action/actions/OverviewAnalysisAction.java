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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class OverviewAnalysisAction extends Action implements ICheatSheetAction {

    private static final int WIDTH = 600;

    private static final int HEIGHT = 450;

    private DBConnectionRepNode connNode = null;

    private DBCatalogRepNode cataNode = null;

    private DBSchemaRepNode schemaNode = null;

    private List<IRepositoryNode> nodes;

    private List<IRepositoryNode> catalogs = new ArrayList<IRepositoryNode>();

    // FIXME remove it.
    private Package[] packageObjs;

    public OverviewAnalysisAction() {
        super(DefaultMessagesImpl.getString("OverviewAnalysisAction.OverviewAnalysis")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    public OverviewAnalysisAction(Package[] packageObjs) {
        this();
        this.packageObjs = packageObjs;
    }

    public OverviewAnalysisAction(List<IRepositoryNode> nodes) {
        this();
        this.nodes = nodes;
    }

    @Override
    public void run() {
        PackagesAnalyisParameter packaFilterParameter = new PackagesAnalyisParameter();
        catalogs.clear();
        for (IRepositoryNode node : nodes) {
            if (node instanceof DBConnectionRepNode) {
                packaFilterParameter.setConnectionRepNode((DBConnectionRepNode) node);
                connNode = (DBConnectionRepNode) node;
            } else if (node instanceof DBCatalogRepNode) {
                IRepositoryNode parent = ((DBCatalogRepNode) node).getParent();
                packaFilterParameter.setConnectionRepNode((DBConnectionRepNode) parent);
                catalogs.add(node);
                packaFilterParameter.setPackages(catalogs);
                cataNode = (DBCatalogRepNode) node;
            } else if (node instanceof DBSchemaRepNode) {
                IRepositoryNode parent = ((DBSchemaRepNode) node).getParent();
                schemaNode = (DBSchemaRepNode) node;
                if (parent instanceof DBCatalogRepNode) {
                    IRepositoryNode connNode = ((DBCatalogRepNode) parent).getParent();
                    packaFilterParameter.setConnectionRepNode((DBConnectionRepNode) connNode);
                    catalogs.add(schemaNode);
                    packaFilterParameter.setPackages(catalogs);
                } else {
                    catalogs.add(schemaNode);
                    packaFilterParameter.setConnectionRepNode((DBConnectionRepNode) parent);
                    packaFilterParameter.setPackages(catalogs);

                }
            }
        }
        Wizard wizard = null;
        if (connNode != null) {
            packaFilterParameter.setConnectionRepNode(connNode);
            wizard = WizardFactory.createAnalysisWizard(AnalysisType.CONNECTION, packaFilterParameter);
        } else if (cataNode != null) {
            wizard = WizardFactory.createAnalysisWizard(AnalysisType.CATALOG, packaFilterParameter);
        } else if (schemaNode != null) {
            wizard = WizardFactory.createAnalysisWizard(AnalysisType.SCHEMA, packaFilterParameter);
        }

        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(WIDTH, HEIGHT);
        dialog.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        run();
    }
}
