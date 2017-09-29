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
package org.talend.dataprofiler.core.ui.wizard.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.impl.AllMatchIndicatorImpl;
import org.talend.dataquality.indicators.impl.RegexpMatchingIndicatorImpl;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class PatternsSelectPage extends WizardPage {

    private SelectPatternsWizard selectPatternsWizard;

    private Grid table;

    private DataFilterType filterType = null;

    private List<Map<Integer, RegexpMatchingIndicator>> tableInputList;

    private List<Map<Integer, RegexpMatchingIndicator>> oldTableInputList;

    /**
     * DOC zshen PatternsSelectPage constructor comment.
     * 
     * @param pageName
     */
    protected PatternsSelectPage(SelectPatternsWizard selectPatternsWizard) {
        super(DefaultMessagesImpl.getString("PatternsSelectPage.title")); //$NON-NLS-1$
        setTitle(DefaultMessagesImpl.getString("PatternsSelectPage.title")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("PatternsSelectPage.message")); //$NON-NLS-1$

        this.selectPatternsWizard = selectPatternsWizard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        GridLayout parentGridLayout = new GridLayout();
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        parent.setLayout(parentGridLayout);

        List<ModelElement> analysisColumns = this.selectPatternsWizard.getSsIndicator().getAnalyzedColumns();
        // add tableView
        // MOD msjian TDQ-4631 2012-2-10: add horizontal scroll bar
        GridTableViewer tableView = new GridTableViewer(parent, SWT.H_SCROLL);
        // TDQ-4631~

        this.table = tableView.getGrid();
        this.table.setLayoutData(data);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (ModelElement element : analysisColumns) {
            GridColumn tableCum = new GridColumn(table, SWT.CHECK);
            String elementName = MetadataConnectionUtils.getMetadataColumnName(element);
            if (elementName != null) {
                tableCum.setText(elementName);
            }
        }

        tableView.setContentProvider(new PatternSelectContentProvider());
        tableView.setLabelProvider(new PatternSelectLabelProvider());

        this.tableInputList = organizeTableInput();
        tableView.setInput(tableInputList);

        for (GridColumn tableCum : table.getColumns()) {
            tableCum.pack();
        }
        // add data filter section.
        initDataFilterSection(parent);

        inItitemCheck();

        this.setControl(parent);
        this.setPageComplete(true);
    }

    private void initDataFilterSection(Composite parent) {

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;

        FillLayout dataFilterSectionGridLayout = new FillLayout();

        Composite dataFilterSection = new Composite(parent, SWT.NONE);
        dataFilterSection.setLayoutData(data);
        dataFilterSection.setLayout(dataFilterSectionGridLayout);

        DataFilterListener dataFilterListener = new DataFilterListener();
        Label displayLabel = new Label(dataFilterSection, SWT.CENTER | SWT.VERTICAL);
        displayLabel.setText(DefaultMessagesImpl.getString("PatternsSelectPage.display"));//$NON-NLS-1$
        Button dataRadio = null;
        for (DataFilterType newfilterType : DataFilterType.values()) {
            dataRadio = new Button(dataFilterSection, SWT.RADIO);
            dataRadio.setText(newfilterType.getTextName());
            dataRadio.addSelectionListener(dataFilterListener);
            // set previous selection
            if (filterType != null && newfilterType.equals(filterType)) {
                dataRadio.setSelection(true);
                // default selection
            } else if (filterType == null && newfilterType.equals(DataFilterType.ALL_DATA)) {
                dataRadio.setSelection(true);
            }

        }
    }

    /**
     * 
     * zshen Comment method "inItitemCheck". initialization the table to decide which pattern has been selected.
     */
    private void inItitemCheck() {
        int index = 0;
        for (GridItem theItem : this.table.getItems()) {
            for (int columnIndex = 0; columnIndex < this.table.getColumnCount(); columnIndex++) {
                if (this.oldTableInputList != null && this.tableInputList.size() <= this.oldTableInputList.size()
                        && this.oldTableInputList.get(index).get(columnIndex) != null) {
                    theItem.setChecked(columnIndex, true);
                } else if (this.tableInputList.get(index).get(columnIndex) == null) {
                    theItem.setCheckable(columnIndex, false);
                    theItem.setGrayed(columnIndex, true);

                }
            }
            index++;
        }
    }

    /**
     * zshen.
     * 
     * @return the input of table.
     */
    private List<Map<Integer, RegexpMatchingIndicator>> organizeTableInput() {
        List<Map<Integer, RegexpMatchingIndicator>> returnList = new ArrayList<Map<Integer, RegexpMatchingIndicator>>();
        AllMatchIndicatorImpl allMatchIndicator = this.selectPatternsWizard.getAllMatchIndicator();
        if (allMatchIndicator == null) {
            return returnList;
        }
        List<ModelElement> analysisColumns = this.selectPatternsWizard.getAllMatchIndicator().getAnalyzedColumns();
        List<RegexpMatchingIndicator> regexpMatchingIndicatorList = this.selectPatternsWizard.getAllMatchIndicator()
                .getCompositeRegexMatchingIndicators();

        for (RegexpMatchingIndicator regexIndicator : regexpMatchingIndicatorList) {

            int index = analysisColumns.indexOf(regexIndicator.getAnalyzedElement());
            Map<Integer, RegexpMatchingIndicator> returnMap = getMapLocation(returnList, index);
            // new HashMap<Integer, String>();
            returnMap.put(index, regexIndicator);
            if (returnMap.size() == 1) {
                returnList.add(returnMap);
            }
        }
        return returnList;
    }

    /**
     * 
     * DOC zshen Comment method "getMapLocation".
     * 
     * @return
     */
    private Map<Integer, RegexpMatchingIndicator> getMapLocation(List<Map<Integer, RegexpMatchingIndicator>> theList, int index) {
        for (Map<Integer, RegexpMatchingIndicator> mapElement : theList) {
            if (mapElement.get(index) != null) {
                continue;
            } else {
                return mapElement;
            }
        }
        return new HashMap<Integer, RegexpMatchingIndicator>();
    }

    /**
     * 
     * DOC zshen PatternsSelectPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class PatternSelectContentProvider implements IStructuredContentProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            Object[] elementsArray = new Object[] {};
            if (inputElement instanceof List<?>) {
                elementsArray = ((List<?>) inputElement).toArray();
            }
            return elementsArray;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 
     * DOC zshen PatternsSelectPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class PatternSelectLabelProvider implements ITableLabelProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if (element instanceof Map<?, ?> && ((Map<Integer, String>) element).get(columnIndex) != null) {

                return ImageLib.getImage(ImageLib.PATTERN_REG);
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            String elementName = null;
            if (element instanceof RegexpMatchingIndicatorImpl) {
                elementName = ((RegexpMatchingIndicatorImpl) element).getName();
            } else if (element instanceof Map<?, ?>) {
                Object tempVar = ((Map<?, ?>) element).get(columnIndex);
                if (tempVar instanceof RegexpMatchingIndicatorImpl) {
                    elementName = ((RegexpMatchingIndicatorImpl) tempVar).getName();
                }

            }

            // PatternsSelectPage.this.getTable().getColumn(columnIndex).pack();
            return elementName;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

    }

    public Grid getTable() {
        return table;
    }

    public List<Map<Integer, RegexpMatchingIndicator>> getTableInputList() {
        return tableInputList;
    }

    public List<Map<Integer, RegexpMatchingIndicator>> getOldTableInputList() {
        return oldTableInputList;
    }

    public void setOldTableInputList(List<Map<Integer, RegexpMatchingIndicator>> oldTableInputList) {
        this.oldTableInputList = oldTableInputList;
    }

    public DataFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(DataFilterType filterType) {
        this.filterType = filterType;
    }

    /**
     * 
     * zshen PatternsSelectPage class global comment. Detailled comment
     */
    private class DataFilterListener implements SelectionListener {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetSelected(SelectionEvent e) {
            Widget selectItem = e.widget;
            if (selectItem instanceof Button && (selectItem.getStyle() | SWT.RADIO) != 0) {
                for (DataFilterType type : DataFilterType.values()) {
                    if (type.getTextName().equals(((Button) selectItem).getText())) {
                        filterType = type;
                        break;
                    }
                }
            }

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetDefaultSelected(SelectionEvent e) {
            // TODO Auto-generated method stub

        }

    }
}
