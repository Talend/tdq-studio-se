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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.JavaUDIIndicatorParameter;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.IndicatorParameters;

/**
 * DOC klliu class global comment. Detailled comment figure 13429 2010-08-03
 */
public class JavaUDIParametersForm extends AbstractIndicatorForm {

    private List<JavaUDIIndicatorParameter> content = new ArrayList<JavaUDIIndicatorParameter>();

    private static final String NAME_PROPERTY = "Parameter Key"; //$NON-NLS-1$

    private static final String VALUE_PROPERTY = "Parameter Value"; //$NON-NLS-1$

    private TableViewer viewer;

    private Table table;

    /**
     * DOC klliu JavaUDIParametersForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param parameters
     */
    public JavaUDIParametersForm(Composite parent, int style, IndicatorParameters parameters) {
        super(parent, style, parameters);
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm#getFormEnum()
     */
    @Override
    public FormEnum getFormEnum() {
        return FormEnum.JavaUDIParametersForm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm#performFinish()
     */
    @Override
    public boolean performFinish() {
        boolean isNull = content.isEmpty();
        if (!isNull) {
            Domain indicatorValidDomain = parameters.getIndicatorValidDomain();
            if (indicatorValidDomain == null) {
                indicatorValidDomain = DomainHelper.createDomain("JAVA_UDI_PARAMETERS"); //$NON-NLS-1$
                parameters.setIndicatorValidDomain(indicatorValidDomain);
            }
            parameters.getIndicatorValidDomain().getJavaUDIIndicatorParameter().clear();
            parameters.getIndicatorValidDomain().getJavaUDIIndicatorParameter().addAll(content);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        // this.setLayout(new FillLayout());
        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(1, false));
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setText(DefaultMessagesImpl.getString("JavaUDIParametersForm.replace")); //$NON-NLS-1$
        buildControls(group);
    }

    /**
     * DOC klliu Comment method "buildControls".
     * 
     * @param group
     */
    private void buildControls(Group group) {
        table = new Table(group, SWT.FULL_SELECTION);
        GridData data = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(data);
        viewer = buildAndLayoutTable(table);

        attachContentProvider(viewer);
        attachLabelProvider(viewer);
        attachCellEditors(viewer, table);

        MenuManager popupMenu = new MenuManager();
        IAction newRowAction = new NewRowAction();
        popupMenu.add(newRowAction);
        IAction delRowAction = new DelRowAction();
        popupMenu.add(delRowAction);
        Menu menu = popupMenu.createContextMenu(table);
        table.setMenu(menu);
        table.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                checkFieldsValue();
            }
        });
        viewer.setInput(content);
        // viewer.addSelectionChangedListener(new listener)
        createDefinitionParametersButton(group, viewer);
    }

    private void createDefinitionParametersButton(Composite comp, final TableViewer parView) {
        Composite composite = new Composite(comp, SWT.NONE);
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = SWT.CENTER;
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(gd);
        final Button addButton = new Button(composite, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        labelGd.horizontalAlignment = SWT.RIGHT;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                JavaUDIIndicatorParameter newItem = DomainHelper.createJavaUDIIndicatorParameter("key", "value"); //$NON-NLS-1$ //$NON-NLS-2$
                content.add(newItem);
                viewer.refresh();
                checkFieldsValue();
            }
        });
        final Button romveButton = new Button(composite, SWT.NONE);
        romveButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        romveButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.del")); //$NON-NLS-1$
        GridData reGd = new GridData();
        reGd.horizontalAlignment = SWT.LEFT;
        reGd.widthHint = 65;
        romveButton.setLayoutData(reGd);
        romveButton.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                IStructuredSelection selection = (IStructuredSelection) parView.getSelection();
                Object o = selection.getFirstElement();
                if (o instanceof JavaUDIIndicatorParameter) {
                    content.remove(o);
                    viewer.refresh(content);
                    checkFieldsValue();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#checkFieldsValue()
     */

    @Override
    protected boolean checkFieldsValue() {
        if (content.isEmpty()) { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, MSG_EMPTY);
            return false;
        }

        updateStatus(IStatus.OK, MSG_OK);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        EList<JavaUDIIndicatorParameter> javaUDIIndicatorParameter = parameters.getIndicatorValidDomain()
                .getJavaUDIIndicatorParameter();
        for (JavaUDIIndicatorParameter judip : javaUDIIndicatorParameter) {
            content.add(judip);
        }
        viewer.refresh(content);
    }

    /**
     * DOC klliu class global comment. Detailled comment figure 13429 2010-08-03
     */
    private class NewRowAction extends Action {

        public NewRowAction() {
            super("Insert New Row"); //$NON-NLS-1$
        }

        public void run() {
            JavaUDIIndicatorParameter newItem = DomainHelper.createJavaUDIIndicatorParameter("key", "value"); //$NON-NLS-1$ //$NON-NLS-2$
            content.add(newItem);
            viewer.refresh();
            checkFieldsValue();
        }
    }

    /**
     * DOC klliu class global comment. Detailled comment figure 13429 2010-08-03
     */
    private class DelRowAction extends Action {

        public DelRowAction() {
            super("Delete Row"); //$NON-NLS-1$
        }

        public void run() {
            IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
            Object o = selection.getFirstElement();
            if (o instanceof JavaUDIIndicatorParameter) {
                content.remove(o);
                viewer.refresh(content);
                checkFieldsValue();
            }
        }
    }

    /**
     * 
     * DOC klliu Comment method "attachLabelProvider". figure 13429 2010-08-03
     * 
     * @param viewer
     */
    private void attachLabelProvider(TableViewer viewer) {
        viewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                switch (columnIndex) {
                case 0:
                    return ((JavaUDIIndicatorParameter) element).getKey();
                case 1:
                    return ((JavaUDIIndicatorParameter) element).getValue();
                default:
                    return "Invalid column: " + columnIndex; //$NON-NLS-1$
                }
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener lpl) {
            }
        });
    }

    /**
     * 
     * DOC klliu Comment method "attachContentProvider".figure 13429 2010-08-03.
     * 
     * @param viewer
     */
    private void attachContentProvider(TableViewer viewer) {
        viewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return ((List<?>) inputElement).toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
    }

    /**
     * 
     * DOC klliu Comment method "buildAndLayoutTable".figure 13429 2010-08-03.
     * 
     * @param table
     * @return
     */
    private TableViewer buildAndLayoutTable(final Table table) {
        TableViewer tableViewer = new TableViewer(table);

        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(50, 75, true));
        layout.addColumnData(new ColumnWeightData(50, 75, true));
        table.setLayout(layout);

        TableColumn nameColumn = new TableColumn(table, SWT.CENTER);
        nameColumn.setText("Parameter Key"); //$NON-NLS-1$
        TableColumn valColumn = new TableColumn(table, SWT.CENTER);
        valColumn.setText("Parameter Value"); //$NON-NLS-1$
        table.setHeaderVisible(true);
        return tableViewer;
    }

    /**
     * 
     * DOC klliu Comment method "attachCellEditors".figure 13429 2010-08-03.
     * 
     * @param viewer
     * @param parent
     */
    private void attachCellEditors(final TableViewer viewer, Composite parent) {
        viewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return true;
            }

            public Object getValue(Object element, String property) {
                if (NAME_PROPERTY.equals(property)) {
                    return ((JavaUDIIndicatorParameter) element).getKey();
                } else {
                    return ((JavaUDIIndicatorParameter) element).getValue();
                }
            }

            public void modify(Object element, String property, Object value) {
                TableItem tableItem = (TableItem) element;
                JavaUDIIndicatorParameter data = (JavaUDIIndicatorParameter) tableItem.getData();
                if (NAME_PROPERTY.equals(property)) {
                    data.setKey(value.toString());
                } else {
                    data.setValue(value.toString());
                }
                viewer.refresh(data);
                checkFieldsValue();
            }
        });

        viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new TextCellEditor(parent) });

        viewer.setColumnProperties(new String[] { NAME_PROPERTY, VALUE_PROPERTY });
    }
}
