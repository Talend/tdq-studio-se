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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextListener;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;

/**
 * created by xqliu on Aug 19, 2013 Detailled comment
 * 
 */
public class ContextComposite extends Composite {

    private static final int COLUMN_WIDTH = 200;

    private static final int HINT_HEIGHT = 220;

    private ComboViewer contextComboViewer;

    /**
     * Getter for contextComboViewer.
     * 
     * @return the contextComboViewer
     */
    public ComboViewer getContextComboViewer() {
        return this.contextComboViewer;
    }

    private TableViewer contextTableViewer;

    private SupportContextEditor currentEditor;

    private IContextListener contextListener;

    /**
     * DOC xqliu ReportContextComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ContextComposite(SupportContextEditor currentEditor, Composite parent, int style) {
        super(parent, style);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        Composite contextGroup = this;

        // layout = new GridLayout();
        // contextGroup.setLayout(layout);
        // contextGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        contextComboViewer = new ComboViewer(contextGroup, SWT.BORDER | SWT.READ_ONLY);
        contextComboViewer.setContentProvider(new ArrayContentProvider());
        contextComboViewer.setLabelProvider(new ContextNameLabelProvider());
        contextComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Table contextTable = new Table(contextGroup, SWT.BORDER);
        contextTable.setLinesVisible(true);
        contextTable.setHeaderVisible(true);

        TableColumn colName = new TableColumn(contextTable, SWT.NONE);
        colName.setText(DefaultMessagesImpl.getString("ContextComposite.nameCol")); //$NON-NLS-1$
        colName.setWidth(COLUMN_WIDTH);
        TableColumn colValue = new TableColumn(contextTable, SWT.NONE);
        colValue.setText(DefaultMessagesImpl.getString("ContextComposite.valueCol")); //$NON-NLS-1$
        colValue.setWidth(COLUMN_WIDTH);

        contextTableViewer = new TableViewer(contextTable);
        contextTableViewer.setContentProvider(new ArrayContentProvider());
        contextTableViewer.setLabelProvider(new ContextParameterLabelProvider());
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = HINT_HEIGHT;
        contextTableViewer.getControl().setLayoutData(data);
        setCurrentEditror(currentEditor);
    }

    private void setCurrentEditror(final SupportContextEditor currentEditor) {
        this.currentEditor = currentEditor;

        getInformationsFromContextManager(currentEditor.getContextManager());

        contextListener = new IContextListener() {

            public void contextsChanged() {
                if (!ContextComposite.this.isDisposed()) {
                    IContextManager contextManager = currentEditor.getContextManager();
                    getInformationsFromContextManager(contextManager);
                    // if the ContextManager is modified, set the report editor dirty
                    if (contextManager instanceof JobContextManager) {
                        JobContextManager jobContextManager = (JobContextManager) contextManager;
                        if (jobContextManager.isModified() && currentEditor.getMasterPage() != null) {
                            currentEditor.getMasterPage().setDirty(true);
                        }
                    }
                }
            }
        };

        if (this.currentEditor.getContextManager() != null) {
            this.currentEditor.getContextManager().addContextListener(contextListener);
        }
        contextComboViewer.addSelectionChangedListener(contextComboListener);
    }

    protected void getInformationsFromContextManager(IContextManager contextManager) {
        if (contextManager == null) {
            return;
        }
        List<IContext> internalContextList = new ArrayList<IContext>();
        IContext newSelectedCopiedContext = null;

        if (this.currentEditor != null && !StringUtils.isEmpty(this.currentEditor.getLastRunContextGroupName())) {
            for (IContext context : contextManager.getListContext()) {
                IContext copiedContext = context.clone();
                internalContextList.add(copiedContext);
                if (this.currentEditor.getLastRunContextGroupName().equals(context.getName())) {
                    newSelectedCopiedContext = copiedContext;
                }
            }
        } else {
            for (IContext context : contextManager.getListContext()) {
                IContext copiedContext = context.clone();
                internalContextList.add(copiedContext);
                if (contextManager.getDefaultContext().equals(context)) {
                    newSelectedCopiedContext = copiedContext;
                }
            }
        }
        Collections.sort(internalContextList, new ContextCompare());
        contextComboViewer.setInput(internalContextList);

        if (newSelectedCopiedContext != null) {
            setContextComboSelection(new StructuredSelection(newSelectedCopiedContext));
            contextTableViewer.setInput(newSelectedCopiedContext.getContextParameterList());
        } else {
            IContext element = internalContextList.get(0);
            setContextComboSelection(new StructuredSelection(element));
            contextTableViewer.setInput(element.getContextParameterList());
            // should update the report editor's last run context group name here
            this.currentEditor.setLastRunContextGroupName(element.getName());
        }
    }

    public void setContextComboSelection(StructuredSelection selection) {
        contextComboViewer.removeSelectionChangedListener(contextComboListener);
        contextComboViewer.setSelection(selection);
        contextComboViewer.addSelectionChangedListener(contextComboListener);
    }

    ISelectionChangedListener contextComboListener = new ISelectionChangedListener() {

        public void selectionChanged(final SelectionChangedEvent event) {
            Object input = null;
            if (!event.getSelection().isEmpty()) {
                IContext selectedContext = (IContext) ((IStructuredSelection) event.getSelection()).getFirstElement();
                input = selectedContext.getContextParameterList();
                ContextComposite.this.currentEditor.setLastRunContextGroupName(selectedContext.getName());
                ContextComposite.this.currentEditor.getMasterPage().setDirty(true);
            }
            contextTableViewer.setInput(input);
        }

    };

    /**
     * the compare for the IContext.
     */
    private class ContextCompare implements java.util.Comparator<IContext> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(IContext o1, IContext o2) {
            String name1 = o1.getName().toUpperCase();
            String name2 = o2.getName().toUpperCase();
            return name1.compareTo(name2);
        }
    }

    /**
     * LabelProvider for a context combo.
     */
    private static class ContextNameLabelProvider extends LabelProvider {

        /**
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(final Object element) {
            if (element instanceof IContext) {
                return ((IContext) element).getName();
            }
            return super.getText(element);
        }
    }

    /**
     * LabelProvider for a context table.
     */
    private static class ContextParameterLabelProvider extends LabelProvider implements ITableLabelProvider {

        /**
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(final Object element, final int columnIndex) {
            String text;
            IContextParameter parameter = (IContextParameter) element;
            switch (columnIndex) {
            case 0:
                text = parameter.getName();
                break;
            case 1:
                if (ContextParameterUtils.isPasswordType(parameter)) {
                    String temp = parameter.getValue();
                    text = ""; //$NON-NLS-1$
                    for (int i = 0; i < temp.length(); ++i) {
                        text += "*"; //$NON-NLS-1$
                    }
                } else {
                    text = parameter.getValue();
                }
                break;
            default:
                text = super.getText(element);
            }
            return text;
        }

        /**
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(final Object element, final int columnIndex) {
            return null;
        }
    }
}
