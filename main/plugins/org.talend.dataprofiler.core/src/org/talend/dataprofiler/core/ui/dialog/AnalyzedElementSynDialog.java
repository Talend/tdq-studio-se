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
package org.talend.dataprofiler.core.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.analysis.Analysis;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC mzhao Analyzed element synchronization dialog. ADDED 2009-06-02 Feature:5887
 */
public abstract class AnalyzedElementSynDialog extends SelectionStatusDialog implements ISelectionChangedListener {

    protected TreeViewer sTreeViewer;

    protected ITreeContentProvider sContentProvider;

    protected ILabelProvider sLabelProvider;

    protected Analysis analysis;

    protected List<SynTreeModel> modelInput = null;

    protected Connection newDataProvider;

    protected Map<ModelElement, ModelElement> synedEleMap;

    public AnalyzedElementSynDialog(Shell parent, Analysis analysis, Connection newDataProvider) {
        super(parent);
        setTitle(DefaultMessagesImpl.getString("AnalyzedElementSynDialog.SynWithNewConnection")); //$NON-NLS-1$
        initTableProvider();
        this.newDataProvider = newDataProvider;
        this.analysis = analysis;
        modelInput = new ArrayList<SynTreeModel>();
        synedEleMap = new HashMap<ModelElement, ModelElement>();
    }

    @Override
    protected void computeResult() {
    }

    public void selectionChanged(SelectionChangedEvent event) {
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout2 = new GridLayout();
        gridLayout2.marginWidth = 0;
        gridLayout2.horizontalSpacing = 0;
        gridLayout2.marginHeight = 0;
        composite.setLayout(gridLayout2);
        Composite infoComp = new Composite(composite, SWT.NONE);
        final GridData gdInfoComp = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdInfoComp.heightHint = 22;
        infoComp.setLayoutData(gdInfoComp);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        infoComp.setLayout(gridLayout);

        final Label label = new Label(infoComp, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
        label.setImage(ImageLib.getImage(ImageLib.ICON_INFO));

        final Label label1 = new Label(infoComp, SWT.NONE);
        label1.setRegion(null);
        final GridData gdLabel1 = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdLabel1.verticalIndent = 1;
        label1.setLayoutData(gdLabel1);
        label1.setText(DefaultMessagesImpl.getString("AnalyzedElementSynDialog.RemoveAnalyeElement")); //$NON-NLS-1$
        // label
        // .setText(
        // "  Which of following analyzed element(s) do you want to keep?");
        Composite synModelComp = new Composite(composite, SWT.NONE);
        final GridData gdsynModelComp = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdsynModelComp.heightHint = 129;
        synModelComp.setLayoutData(gdsynModelComp);
        final GridLayout gridLayout1 = new GridLayout();
        gridLayout1.marginWidth = 0;
        gridLayout1.marginHeight = 0;
        gridLayout1.horizontalSpacing = 0;
        synModelComp.setLayout(gridLayout1);
        sTreeViewer = createSynTreeViewer(synModelComp);
        createTableHeader();
        sTreeViewer.setInput(modelInput);
        return composite;
    }

    private void createTableHeader() {
        sTreeViewer.getTree().setHeaderVisible(true);
        sTreeViewer.getTree().setLinesVisible(true);
        TreeColumn column1 = new TreeColumn(sTreeViewer.getTree(), SWT.CENTER);
        column1.setWidth(200);
        column1.setText(DefaultMessagesImpl.getString("AnalyzedElementSynDialog.Name")); //$NON-NLS-1$
        TreeColumn column2 = new TreeColumn(sTreeViewer.getTree(), SWT.CENTER);
        column2.setWidth(500);
        column2.setText(DefaultMessagesImpl.getString("AnalyzedElementSynDialog.Status")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sTreeViewer.getTree());
    }

    /**
     * 
     * DOC mzhao Create synchronized analyzed table.
     * 
     * @param parent
     * @return
     */
    protected TreeViewer createSynTreeViewer(Composite parent) {
        TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.BORDER);
        final Tree tree = viewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        try {
            viewer.setLabelProvider(this.sLabelProvider);
            viewer.setContentProvider(this.sContentProvider);
            viewer.addSelectionChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewer;
    }

    public List<SynTreeModel> getSynInputModel() {
        reloadInputModel();
        return modelInput;
    }

    public Map<ModelElement, ModelElement> getSynedEleMap() {
        return synedEleMap;
    }

    private void initTableProvider() {
        sLabelProvider = new AnaColSynLabelProvider();
        sContentProvider = new DBTreeViewContentProvider();

    }

    protected abstract void reloadInputModel();

    /**
     * 
     * DOC mzhao AnalyzedElementSynDialog class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    public class SynTreeModel {

        private ModelElement oldDataProvElement = null;

        private ModelElement newDataProvElement = null;

        private ModelElement currentAnaElement = null;

        public SynTreeModel(ModelElement currentElement) {
            this.currentAnaElement = currentElement;
        }

        public String getLabel() {
            return currentAnaElement.getName();
        }

        public String getAsynDecription() {
            return DefaultMessagesImpl.getString("AnalyzedElementSynDialog.NotExistInNewConn", oldDataProvElement.getName()); //$NON-NLS-1$
        }

        public void setNewDataProvElement(ModelElement newDataProvElement) {
            this.newDataProvElement = newDataProvElement;
        }

        public ModelElement getNewDataProvElement() {
            return newDataProvElement;
        }

        public void setOldDataProvElement(ModelElement oldDataProvElement) {
            this.oldDataProvElement = oldDataProvElement;
        }

        public ModelElement getOldDataProvElement() {
            return oldDataProvElement;
        }
    }

    /**
     * 
     * DOC mzhao AnalyzedColumnsSynDialog class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class DBTreeViewContentProvider implements ITreeContentProvider {

        /**
         * @param adapterFactory
         */
        public DBTreeViewContentProvider() {
            super();
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement != null && parentElement instanceof SynTreeModel) {
                SynTreeModel synTreeModel = (SynTreeModel) parentElement;
                return new Object[] { synTreeModel };
            }
            return null;
        }

        public boolean hasChildren(Object element) {
            return false;
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                return ((List<?>) inputElement).toArray();
            }
            return new Object[] { inputElement };
        }

        public void dispose() {

        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        public Object getParent(Object element) {
            return null;
        }

    }

    /**
     * 
     * DOC zhao AnalyzedColumnsSynDialog class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class AnaColSynLabelProvider extends LabelProvider implements ITableLabelProvider {

        /*
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java .lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0)
                return null;
            return null;
        }

        /*
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java. lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            SynTreeModel synTreeModel = (SynTreeModel) element;
            switch (columnIndex) {
            case 0:
                return synTreeModel.getLabel();
            case 1:
                return synTreeModel.getAsynDecription();
            default:
                return ""; //$NON-NLS-1$

            }
        }

        /*
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        public String getText(Object element) {
            return getColumnText(element, 0); // needed to make the sorter work
        }
    }

}
