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
package org.talend.dataquality.record.linkage.ui.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.action.RefreshChartAction;
import org.talend.dataquality.record.linkage.ui.composite.utils.ImageLib;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dq.helper.EObjectHelper;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by zshen on Jul 31, 2013 Detailled comment
 * 
 */
public abstract class AbstractMatchAnaysisTableSection extends AbstractSectionComposite implements ITableEditOperation {

    protected List<Object[]> matchRows = new ArrayList<Object[]>();

    // <column instance, column index>
    protected Map<MetadataColumn, String> columnMap = null;

    protected Analysis analysis = null;

    protected Boolean isNeedSubChart = true;

    private Composite sectionClient = null;

    private boolean isAddColumn = false;

    private Composite subTableContent = null;

    private Composite subTableContentParent = null;

    private Composite subChartContent = null;

    /**
     * @param parent
     * @param style
     */
    public AbstractMatchAnaysisTableSection(final ScrolledForm form, Composite parent, int style, FormToolkit toolkit,
            Analysis newAnalysis) {
        super(form, parent, style, toolkit);
        this.analysis = newAnalysis;
    }

    /**
     * DOC zshen Comment method "createContext".
     */
    public Composite createContent() {
        section.setText(getSectionName());
        sectionClient = toolkit.createComposite(section, SWT.NONE);
        sectionClient.setLayout(new GridLayout(getGridColumnNum(), true));
        sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        subTableContentParent = toolkit.createComposite(sectionClient, SWT.NONE);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = getTableDefaultHeight();
        subTableContentParent.setLayoutData(data);

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;

        subTableContentParent.setLayout(gridLayout);

        subTableContent = createSubContent(subTableContentParent);
        createButtons(subTableContent);
        if (isNeedSubChart()) {
            subChartContent = toolkit.createComposite(sectionClient, SWT.NONE);
            subChartContent.setLayout(gridLayout);
            subChartContent.setLayoutData(data);
            createSubChart(subChartContent);
        }
        section.setClient(sectionClient);
        return sectionClient;
    }

    /**
     * DOC zshen Comment method "getTableDefaultHeight".
     * 
     * @return
     */
    protected int getTableDefaultHeight() {
        return 300;
    }

    public void redrawnContent() {
        if (sectionClient != null && !sectionClient.isDisposed()) {
            sectionClient.dispose();
        }
        createContent();
        section.layout();
    }

    public void redrawnSubTableContent() {
        if (subTableContent != null && !subTableContent.isDisposed()) {
            subTableContent.dispose();
        }
        subTableContent = createSubContent(subTableContentParent);
        createButtons(subTableContent);
        subTableContentParent.layout();

    }

    /**
     * Getter for isNeedSubChart.
     * 
     * @return the isNeedSubChart
     */
    public Boolean isNeedSubChart() {
        return this.isNeedSubChart;
    }

    /**
     * Sets the isNeedSubChart.
     * 
     * @param isNeedSubChart the isNeedSubChart to set
     */
    public void setIsNeedSubChart(Boolean isNeedASubChart) {
        this.isNeedSubChart = isNeedASubChart;
    }

    /**
     * DOC zshen Comment method "getColumnNum".
     * 
     * @return
     */
    private int getGridColumnNum() {
        if (isNeedSubChart()) {
            return 2;
        } else {
            return 1;
        }
    }

    protected void createButtons(Composite currentSectionClient) {
        if (isNeedSubChart) {
            createRefreshButton(currentSectionClient);
        } else {
            createEditOperationButtons(currentSectionClient);
        }
    }

    protected void createMoveButton(Composite buttonsComposite, GridData labelGd) {

        final Button upButton = new Button(buttonsComposite, SWT.NONE);
        upButton.setToolTipText(DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.MoveUp_button_tooltip")); //$NON-NLS-1$
        upButton.setImage(ImageLib.getImage(ImageLib.UP_ACTION));
        upButton.setLayoutData(labelGd);
        upButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveUpTableItem();
            }
        });

        final Button downButton = new Button(buttonsComposite, SWT.NONE);
        downButton.setToolTipText(DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.MoveDown_button_tooltip")); //$NON-NLS-1$
        downButton.setImage(ImageLib.getImage(ImageLib.DOWN_ACTION));
        downButton.setLayoutData(labelGd);
        downButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveDownTableItem();
            }
        });

    }

    /**
     * DOC zshen Comment method "createEditOperationButtons".
     * 
     * @param sectionClient
     */
    private void createEditOperationButtons(Composite parent) {
        Composite buttonsComposite = new Composite(parent, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(7, true));

        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;

        createAddAndRemoveButton(buttonsComposite, labelGd);

        createMoveButton(buttonsComposite, labelGd);
        // TODO will be implemented later (copy and paste)

    }

    /**
     * DOC zshen Comment method "createAddAndRemoveButton".
     * 
     * @param buttonsComposite
     * @param labelGd
     */
    private void createAddAndRemoveButton(Composite buttonsComposite, GridData labelGd) {
        final Button addButton = new Button(buttonsComposite, SWT.NONE);
        addButton.setToolTipText(DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.add_button_tooltip")); //$NON-NLS-1$
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addTableItem();
            }
        });

        final Button delButton = new Button(buttonsComposite, SWT.NONE);
        delButton.setToolTipText(DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.delete_button_tooltip")); //$NON-NLS-1$
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setLayoutData(labelGd);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeTableItem();
            }
        });

    }

    /**
     * DOC zshen Comment method "createButtons".
     */
    private void createRefreshButton(Composite currentSectionClient) {

        Composite buttonsComposite = new Composite(currentSectionClient, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(10, true));
        buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        Composite commonButtonsComposite = new Composite(buttonsComposite, SWT.NONE);
        commonButtonsComposite.setLayout(new GridLayout(10, true));
        commonButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 8, 1));

        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;
        createAddAndRemoveButton(commonButtonsComposite, labelGd);
        createMoveButton(commonButtonsComposite, labelGd);

        Composite refreshButtonComposite = new Composite(buttonsComposite, SWT.NONE);
        refreshButtonComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
        refreshButtonComposite.setLayout(new GridLayout(1, true));
        labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.RIGHT;
        String buttonText = DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.visulize_button_text"); //$NON-NLS-1$
        labelGd.widthHint = buttonText.length() * 10;
        final Button refresh = new Button(refreshButtonComposite, SWT.NONE);
        refresh.setToolTipText(DefaultMessagesImpl.getString("AbstractMatchAnaysisTableSection.visulize_button_tooltip")); //$NON-NLS-1$
        refresh.setText(buttonText);
        refresh.setLayoutData(labelGd);
        refresh.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                new RefreshChartAction(AbstractMatchAnaysisTableSection.this).run();
            }
        });

    }

    /**
     * need clear the old data every time
     * 
     */
    public void setDataInput(List<Object[]> allData) {
        matchRows.clear();
        matchRows.addAll(allData);
    }

    public void setColumnNameInput(Map<MetadataColumn, String> newColumnMap) {
        this.columnMap = newColumnMap;
    }

    public void updateColumnPosition(TdColumn column, int newPosition) {
        columnMap.remove(column);
        columnMap.put(column, String.valueOf(newPosition));
    }

    /**
     * Getter for isAddColumn.
     * 
     * @return the isAddColumn
     */
    public boolean isAddColumn() {
        return this.isAddColumn;
    }

    /**
     * Sets the isAddColumn.
     * 
     * @param isAddColumn the isAddColumn to set
     */
    public void setAddColumn(boolean isNeedAddColumn) {
        this.isAddColumn = isNeedAddColumn;
    }

    /**
     * DOC zshen Comment method "shouldMoveUp".
     * 
     * @param blockKeyDefinition
     * @param blockKeyDefinition2
     * @return
     */
    protected boolean isSameWithCurrentModel(Object blockKeyDefinition, Object blockKeyDefinition2) {
        if (blockKeyDefinition.equals(blockKeyDefinition2)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#pasteTableItem()
     */
    @Override
    public void pasteTableItem() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#copyTableItem()
     */
    @Override
    public void copyTableItem() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#moveDownTableItem()
     */
    @Override
    public void moveDownTableItem() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#moveUpTableItem()
     */
    @Override
    public void moveUpTableItem() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#removeTableItem()
     */
    @Override
    public void removeTableItem() {
        // not need implement
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#addTableItem()
     */
    @Override
    public void addTableItem() {
        // not need implement
    }

    /**
     * DOC zshen Comment method "createSubChart". TODO re-order the protected and abstract key word. let "protected"
     * first.
     * 
     * @param sectionClient
     */
    abstract protected void createSubChart(Composite parent);

    /**
     * DOC zshen Comment method "createSubContent".
     * 
     * @param sectionClient
     */
    abstract protected Composite createSubContent(Composite parent);

    /**
     * DOC zshen Comment method "getSectionName".
     * 
     * @return
     */
    abstract protected String getSectionName();

    public abstract void refreshChart();

    public abstract Boolean isKeyDefinitionAdded(String columnName) throws Exception;

    /**
     * check if the key's name is same with some columns name
     * 
     * @param keyName
     * @return
     */
    public boolean hasSameColumnWithKeyName(String keyName) {
        if (this.columnMap == null) {
            return false;
        }
        for (MetadataColumn column : columnMap.keySet()) {
            if (StringUtils.equalsIgnoreCase(getColumnName(column), keyName)) {
                return true;
            }
        }
        return false;// columnMap.containsKey(keyName);
    }

    public void addColumn(MetadataColumn column, int index) {
        if (this.columnMap == null) {
            columnMap = new HashMap<MetadataColumn, String>();
        }
        columnMap.put(column, String.valueOf(index));
    }

    /**
     * When import:1) use key's name & key's column to compare, 2) set it empty when no match. If the key's column =
     * some column name, keep it; when key's name and key's column both no match, set it empty
     * 
     * @param keyDefinition
     */
    protected void setColumnValueIfMatch(KeyDefinition keyDefinition) {
        String columnName = StringUtils.EMPTY;
        if (this.columnMap != null) {
            for (MetadataColumn column : columnMap.keySet()) {
                if (StringUtils.equalsIgnoreCase(getColumnName(column), getMappingName(keyDefinition))) {
                    columnName = column.getLabel();
                    break;
                }
            }
        }
        keyDefinition.setColumn(columnName);
    }

    /**
     * Get the name which mapping to real column name
     * 
     * @param keyDefinition
     * @return
     */
    protected String getMappingName(KeyDefinition keyDefinition) {
        return keyDefinition.getName();
    }

    // For the columns from file connection, the name is null
    private String getColumnName(MetadataColumn column) {
        if (StringUtils.isBlank(column.getName())) {
            return column.getLabel();
        } else {
            return column.getName();
        }
    }

    /**
     * 
     * check dirty status of current Section
     * 
     * @return
     */
    public ReturnCode checkResultStatus() {
        ReturnCode rc = new ReturnCode(true);
        return rc;

    }

    /**
     * check whether column name is Null or empty
     * 
     * @param bdk
     */
    protected boolean checkColumnNameIsEmpty(KeyDefinition kd) {
        String columnName = kd.getColumn();
        if (columnName == null || columnName.equals(StringUtils.EMPTY)) {
            return true;
        }
        return false;
    }

    public void resolveAnalysis() {
        if (this.analysis.eIsProxy()) {
            analysis = (Analysis) EObjectHelper.resolveObject(analysis);
        }
    }

    public void clearChart() {
        // do nothing at here.
    }
}
