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
package org.talend.dataprofiler.core.ui.wizard.analysis;

import org.eclipse.core.runtime.IPath;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IHelpResource;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ViewerDataFactory;
import org.talend.dataprofiler.core.model.nodes.analysis.AnalysisTypeNode;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.AnalysisTypeContentProvider;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.AnalysisTypeLabelProvider;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.analysis.parameters.FuncationDependencyParameter;
import org.talend.dq.analysis.parameters.NamedColumnSetAnalysisParameter;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * @author zqin
 */
public class NewWizardSelectionPage extends AbstractAnalysisWizardPage {

    private TreeViewer typeTreeViewer;

    private Wizard selectedWizard;

    private FolderProvider currentFolderProvider;

    public FolderProvider getCurrentFolderProvider() {
        return this.currentFolderProvider;
    }

    public NewWizardSelectionPage() {
        setTitle(DefaultMessagesImpl.getString("NewWizardSelectionPage.wizard")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("NewWizardSelectionPage.Analysis")); //$NON-NLS-1$
        setCanFinishEarly(false);
        setPageComplete(false);
        setHasPages(true);
    }

    public NewWizardSelectionPage(RepositoryNode node) {
        super();
        if (node != null) {
            initCurrentFolderProvider(RepositoryNodeHelper.getPath(node));
        }
    }

    /**
     * DOC xqliu Comment method "initCurrentFolderProvider".
     * 
     * @param path
     */
    private void initCurrentFolderProvider(IPath path) {
        if (path != null) {
            this.currentFolderProvider = new FolderProvider();
            this.currentFolderProvider.setFolderResource(ResourceManager.getRootProject().getFolder(path));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gdLayout = new GridLayout(1, true);
        container.setLayout(gdLayout);

        PatternFilter filter = new PatternFilter();
        FilteredTree tree = new FilteredTree(container, SWT.BORDER, filter, true);

        typeTreeViewer = tree.getViewer();
        typeTreeViewer.setContentProvider(new AnalysisTypeContentProvider());
        typeTreeViewer.setLabelProvider(new AnalysisTypeLabelProvider());
        typeTreeViewer.setInput(ViewerDataFactory.createTreeData());

        addListeners();

        setControl(container);
    }

    protected void addListeners() {
        typeTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                AnalysisTypeNode node = (AnalysisTypeNode) ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (node == null) {
                    return;
                }
                AnalysisTypeNode parent = (AnalysisTypeNode) node.getParent();
                IContext context = HelpSystem.getContext(HelpPlugin.getDefault().getAnalysisHelpContextID());
                IHelpResource[] relatedTopics = context.getRelatedTopics();
                String href = null;

                String currentLiteral = node.getLiteral();
                if (AnalysisLabelParameter.FUNCTIONAL_DEPENDENCY_ANALYSIS.equals(currentLiteral)) {
                    currentLiteral = AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY.getLiteral();
                } else if (AnalysisLabelParameter.CONNECTION_OVERVIEW_ANALYSIS.equals(currentLiteral)) {
                    currentLiteral = AnalysisType.CONNECTION.getLiteral();
                } else if (AnalysisLabelParameter.CATALOG_OVERVIEW_ANALYSIS.equals(currentLiteral)) {
                    currentLiteral = AnalysisType.CATALOG.getLiteral();
                } else if (AnalysisLabelParameter.SCHEMA_OVERVIEW_ANALYSIS.equals(currentLiteral)) {
                    currentLiteral = AnalysisType.SCHEMA.getLiteral();
                }
                AnalysisType currentType = AnalysisType.get(currentLiteral);
                if (parent == null) {
                    setPageComplete(false);

                    // MOD hcheng 2009-06-05 add help in analysis wizard
                    switch (currentType) {
                    case CONNECTION:
                        href = relatedTopics[0].getHref();
                        break;
                    case COLUMNS_COMPARISON:
                        href = relatedTopics[4].getHref();
                        break;
                    case TABLE:
                        href = relatedTopics[6].getHref();
                        break;
                    case MULTIPLE_COLUMN:
                        href = relatedTopics[11].getHref();
                        break;
                    case COLUMN_CORRELATION:
                        href = relatedTopics[18].getHref();
                        break;
                    default:
                    }
                } else {
                    AnalysisParameter parameter = null;
                    AnalysisType parentType = AnalysisType.get(parent.getLiteral());

                    FolderProvider folderProvider = getCurrentFolderProvider() == null ? ((CreateNewAnalysisWizard) getWizard())
                            .getCurrentFolderProvider() : getCurrentFolderProvider();
                    switch (parentType) {
                    case COLUMN_CORRELATION:
                        AnalysisParameter correlationColumnParam = new AnalysisLabelParameter();
                        ((AnalysisLabelParameter) correlationColumnParam).setCategoryLabel(node.getName());
                        correlationColumnParam.setFolderProvider(folderProvider);
                        parameter = correlationColumnParam;
                        // TDQ-10498 split column correlation page to three pages.
                        // Added those final string is to judge different analysis type.
                        // We are not have those analysisType on the EMF model, And this issue is about display only.
                        // So that don't choose modify EMF model

                        if (AnalysisLabelParameter.NUMBERIC_CORRELATION.equals(currentLiteral)) {
                            href = relatedTopics[19].getHref();
                        } else if (AnalysisLabelParameter.DATE_CORRELATION.equals(currentLiteral)) {
                            href = relatedTopics[20].getHref();
                        } else if (AnalysisLabelParameter.NOMINAL_CORRELATION.equals(currentLiteral)) {
                            href = relatedTopics[21].getHref();
                        } else {
                            href = relatedTopics[18].getHref();
                        }
                        break;
                    case MULTIPLE_COLUMN:
                        AnalysisParameter multipleColumnParam = new AnalysisLabelParameter();
                        ((AnalysisLabelParameter) multipleColumnParam).setCategoryLabel(node.getName());
                        multipleColumnParam.setFolderProvider(folderProvider);
                        parameter = multipleColumnParam;
                        if (AnalysisLabelParameter.SEMANTIC_DISCOVERY_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[12].getHref();
                        } else if (AnalysisLabelParameter.BASIC_COLUMN_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[13].getHref();
                        } else if (AnalysisLabelParameter.NOMINAL_VALUES_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[14].getHref();
                        } else if (AnalysisLabelParameter.PATTERN_FREQUENCY_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[15].getHref();
                        } else if (AnalysisLabelParameter.DISCRETE_DATA_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[16].getHref();
                        } else if (AnalysisLabelParameter.SUMMARY_STATISTICS_ANALYSIS.equals(currentLiteral)) {
                            href = relatedTopics[17].getHref();
                        } else {
                            href = relatedTopics[13].getHref();
                        }
                        break;
                    case COLUMNS_COMPARISON:
                        AnalysisParameter anaParam = new AnalysisParameter();
                        anaParam.setFolderProvider(folderProvider);
                        parameter = anaParam;
                        href = relatedTopics[5].getHref();
                        break;
                    case CONNECTION:
                        switch (currentType) {
                        case CONNECTION:
                        default:
                            AnalysisFilterParameter connParam = new AnalysisFilterParameter();
                            connParam.setFolderProvider(folderProvider);
                            parameter = connParam;
                            href = relatedTopics[1].getHref();
                            parentType = currentType;
                            break;
                        case CATALOG:
                            PackagesAnalyisParameter catalogParam = new PackagesAnalyisParameter();
                            catalogParam.setFolderProvider(folderProvider);
                            parameter = catalogParam;
                            href = relatedTopics[2].getHref();
                            parentType = currentType;
                            break;
                        case SCHEMA:
                            PackagesAnalyisParameter schemaParam = new PackagesAnalyisParameter();
                            schemaParam.setFolderProvider(folderProvider);
                            parameter = schemaParam;
                            href = relatedTopics[3].getHref();
                            parentType = currentType;
                            break;
                        }
                        break;
                    case TABLE:
                        switch (currentType) {
                        case MATCH_ANALYSIS:
                            parameter = new AnalysisParameter();
                            parameter.setFolderProvider(folderProvider);
                            href = relatedTopics[8].getHref();
                            parentType = currentType;
                            break;
                        case COLUMN_SET:
                            AnalysisParameter corrParam = new AnalysisParameter();
                            corrParam.setFolderProvider(folderProvider);
                            parameter = corrParam;
                            href = relatedTopics[10].getHref();
                            parentType = currentType;
                            break;
                        case TABLE_FUNCTIONAL_DEPENDENCY:
                            FuncationDependencyParameter funcationDependency = new FuncationDependencyParameter();
                            funcationDependency.setFolderProvider(folderProvider);
                            parameter = funcationDependency;
                            href = relatedTopics[9].getHref();
                            parentType = currentType;
                            break;
                        default:
                            NamedColumnSetAnalysisParameter tableParam = new NamedColumnSetAnalysisParameter();
                            tableParam.setFolderProvider(folderProvider);
                            parameter = tableParam;
                            href = relatedTopics[7].getHref();
                            break;
                        }
                        break;
                    default:
                        parameter = new AnalysisParameter();
                    }

                    selectedWizard = WizardFactory.createAnalysisWizard(parentType, parameter);
                    setPageComplete(true);
                }
                // MOD by hcheng,0007483: Add help in analysis wizard
                OpeningHelpWizardDialog dialog = (OpeningHelpWizardDialog) getWizard().getContainer();
                dialog.setHref(href);
                dialog.showHelp();
            }

        });

        typeTreeViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {

                AnalysisTypeNode node = (AnalysisTypeNode) ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (node.getParent() == null) {
                    typeTreeViewer.expandToLevel(node, 1);
                } else {
                    advanceToNextPageOrFinish();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
     */
    @Override
    public IWizardPage getNextPage() {
        if (selectedWizard == null) {
            return null;
        }
        if (selectedWizard.getStartingPage() == null) {
            selectedWizard.addPages();
        }

        return selectedWizard.getStartingPage();
    }

    /**
     * @return the selectedWizard
     */
    public Wizard getSelectedWizard() {
        return this.selectedWizard;
    }
}
