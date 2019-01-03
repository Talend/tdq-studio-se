// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.action.RemoveMatchKeyDefinitionAction;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.ui.section.GroupStatisticsRowCompartor;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.KeyDefinition;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public abstract class AbstractMatchAnalysisTableViewer<T> extends TableViewer {

    private static final int DEFAULT_TABLE_HEIGHT_HIME = 130;

    protected Table innerTable = null;

    boolean isAddColumn = true;

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * DOC zshen MatchAnalysisTabveViewer constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AbstractMatchAnalysisTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style);
        innerTable = this.getTable();
        this.isAddColumn = isAddColumn;
        initListener();
    }

    /**
     * DOC zshen Comment method "initListener".
     */
    private void initListener() {
        innerTable.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                char character = e.character;
                if (SWT.DEL == character) {
                    new RemoveMatchKeyDefinitionAction<T>(AbstractMatchAnalysisTableViewer.this).run();
                    listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // don't need to implement is now

            }

        });
    }

    public void initTable(List<String> headers) {
        initTable(headers, null, false);
    }

    /**
     * 
     * DOC zshen Comment method "initTable".
     * 
     * @param headers the name of column
     * @param columnMap all of columns which can be used by current Table
     * @param pixelDataOfHeaders the width of the column
     */
    public void initTable(List<String> headers, List<MetadataColumn> columnMap, boolean withSorter) {
        TableLayout tLayout = new TableLayout();
        innerTable.setLayout(tLayout);
        innerTable.setHeaderVisible(true);
        innerTable.setLinesVisible(true);
        GridData gd = new GridData(GridData.FILL_BOTH);
        innerTable.setLayoutData(gd);

        for (int index = 0; index < headers.size(); index++) {
            final int sortIndex = index;
            String columnTechnologyLabel = headers.get(index);
            String columnLabel = getInternationalizedLabel(columnTechnologyLabel);
            if (columnLabel != null) {
                if (columnLabel.length() == 1) {
                    columnLabel = columnLabel + PluginConstant.SPACE_STRING + PluginConstant.SPACE_STRING;
                }
                tLayout.addColumnData(new ColumnPixelData(columnLabel.length() * getHeaderDisplayWeight(index)));
                final TableViewerColumn tableColumn = new TableViewerColumn(this, SWT.LEFT);
                tableColumn.getColumn().setText(columnLabel);
                addEditingSupportForColumn(tableColumn, columnTechnologyLabel);
                if (withSorter) {
                    final ViewerComparator comparator = getComparator();
                    tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            ((GroupStatisticsRowCompartor) comparator).setColumn(sortIndex);
                            int dir = getTable().getSortDirection();
                            if (getTable().getSortColumn() == tableColumn.getColumn()) {
                                dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                            } else {
                                dir = SWT.DOWN;
                            }
                            getTable().setSortDirection(dir);
                            getTable().setSortColumn(tableColumn.getColumn());
                            refresh();
                        }
                    });
                }
            }
        }

        CellEditor[] editors = getCellEditor(headers, columnMap);
        // add menu
        addContextMenu();
        AbstractMatchCellModifier<T> cellModifier = getTableCellModifier();
        if (cellModifier != null) {
            cellModifier.setColumnMap(columnMap);
            this.setCellModifier(cellModifier);
        }
        this.setCellEditors(editors);
        this.setColumnProperties(headers.toArray(new String[headers.size()]));
        this.setContentProvider(getTableContentProvider());
        this.setLabelProvider(getTableLabelProvider());
        GridData tableGD = new GridData(GridData.FILL_BOTH);
        tableGD.heightHint = getTableHeightHint();
        innerTable.setLayoutData(tableGD);

    }

    /**
     * Add EditingSupport for current column
     * 
     * @param tableColumn
     * @param columnTechnologyLabel
     */
    protected void addEditingSupportForColumn(TableViewerColumn tableColumn, String columnTechnologyLabel) {
        // sub class should implement it if needed
    }

    private String getInternationalizedLabel(String str) {
        // if the str is %, just return it, need not to do internationalization
        return MatchAnalysisConstant.PERCENTAGE.equals(str) ? str : DefaultMessagesImpl.getString(str);
    }

    /**
     * 
     * @return
     */
    protected int getTableHeightHint() {
        return DEFAULT_TABLE_HEIGHT_HIME; // 130 by defaut.
    }

    /**
     * DOC zshen Comment method "selectAllItem".
     * 
     * @param bkdList
     */
    public void selectAllItem(List<KeyDefinition> bkdList) {
        this.setSelectionToWidget(bkdList, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ColumnViewer#update(java.lang.Object, java.lang.String[])
     */
    @Override
    public void update(Object element, String[] properties) {
        super.update(element, properties);
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
    }

    public void noticeColumnSelectChange() {
        listeners.firePropertyChange(MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH, true, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.AbstractTableViewer#remove(java.lang.Object)
     */
    @Override
    public void remove(Object element) {
        super.remove(element);
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
    }

    /**
     * DOC zhao Comment method "addContextMenu".
     */
    abstract public void addContextMenu();

    /**
     * Getter for isAddColumn.
     * 
     * @return the isAddColumn
     */
    public boolean isAddColumn() {
        return this.isAddColumn;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * DOC zshen Comment method "getDisplayWeight".
     * 
     * @return
     */
    abstract protected int getHeaderDisplayWeight();

    /**
     * get the header display weight according to the index.
     * 
     * @param index
     * @return
     */
    protected int getHeaderDisplayWeight(int index) {
        return getHeaderDisplayWeight();
    }

    /**
     * DOC zshen Comment method "getTableLabelProvider".
     * 
     * @return
     */
    abstract protected IBaseLabelProvider getTableLabelProvider();

    /**
     * DOC zshen Comment method "getTableContentProvider".
     * 
     * @return
     */
    abstract protected IContentProvider getTableContentProvider();

    /**
     * DOC zshen Comment method "getTableCellModifier".
     * 
     * @return
     */
    abstract protected AbstractMatchCellModifier<T> getTableCellModifier();

    /**
     * DOC zshen Comment method "getCellEditor".
     * 
     * @param headers
     * @return
     */
    abstract protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnMap);

    /**
     * 
     * add new Element
     * 
     * @param columnName the name of column
     * @param analysis the context of this add operation perform on.
     */
    abstract public boolean addElement(String columnName, Analysis analysis);

    /**
     * 
     * add new Element
     * 
     * @param columnName the name of column
     * @param analysis the context of this add operation perform on.
     */
    public boolean addElement(String columnName, List<T> keyList) {
        T keyDef = createNewKeyDefinition(columnName);
        keyList.add(keyDef);
        add(keyDef);
        return true;
    }

    /**
     * 
     * add new Element
     * 
     * @param columnName the name of column
     * @param analysis the context of this add operation perform on.
     */
    public boolean addElement(T keyDef, List<T> keyList) {
        keyList.add(keyDef);
        add(keyDef);
        return true;
    }

    /**
     * create a new KeyDefinition
     * 
     * @param columnName
     * @return
     */
    abstract protected T createNewKeyDefinition(String columnName);

    /**
     * remove Element
     * 
     * @param columnName the name of column
     */
    abstract public void removeElement(String columnName, List<T> keyList);

    /**
     * remove Element
     * 
     * @param columnName the element of column
     */
    public void removeElement(T keyDef, List<T> keyList) {
        Iterator<T> keysIterator = keyList.iterator();
        while (keysIterator.hasNext()) {
            T tmpKeyDef = keysIterator.next();
            if (keyDef.equals(tmpKeyDef)) {
                keyList.remove(keyDef);
                // Update table view.
                remove(keyDef);
                listeners.firePropertyChange(MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH, true, false);
                break;
            }
        }
    }

    /**
     * 
     * move up element
     * 
     * @param keyDef
     * @param matchRuleDef
     */
    public void moveUpElement(T keyDef, List<T> keyList) {
        Iterator<T> keysIterator = keyList.iterator();
        while (keysIterator.hasNext()) {
            T tmpKeyDef = keysIterator.next();
            if (keyDef.equals(tmpKeyDef)) {
                int indexForElement = indexForElement(tmpKeyDef);
                if (indexForElement - 2 >= 0) {
                    // modify model
                    moveUpFromModel(keyDef, keyList, indexForElement);
                    // modify table viewer
                    remove(keyDef);
                    insert(keyDef, indexForElement - 1);
                }
                break;
            }
        }
    }

    /**
     * DOC zhao Comment method "moveFromModel".
     * 
     * @param keyDef
     * @param keyList
     * @param indexForElement
     */
    protected void moveUpFromModel(T keyDef, List<T> keyList, int indexForElement) {
        keyList.remove(keyDef);
        keyList.add(indexForElement - 2, keyDef);
    }

    /**
     * 
     * move down element
     * 
     * @param keyDef
     * @param matchRuleDef
     */
    public void moveDownElement(T keyDef, List<T> keyList) {
        Iterator<T> keysIterator = keyList.iterator();
        while (keysIterator.hasNext()) {
            T tmpKeyDef = keysIterator.next();
            if (keyDef.equals(tmpKeyDef)) {
                int indexForElement = indexForElement(tmpKeyDef);
                if (indexForElement < keyList.size()) {
                    moveDownFromModel(keyDef, keyList, indexForElement);
                    // modify table viewer
                    remove(keyDef);
                    insert(keyDef, indexForElement + 1);
                }
                break;
            }
        }
    }

    /**
     * DOC zhao Comment method "moveDownFromModel".
     * 
     * @param keyDef
     * @param keyList
     * @param indexForElement
     */
    protected void moveDownFromModel(T keyDef, List<T> keyList, int indexForElement) {
        // modify model
        keyList.remove(keyDef);
        if (indexForElement == keyList.size()) {
            keyList.add(keyDef);
        } else {
            keyList.add(indexForElement, keyDef);
        }
    }

    protected ComboBoxCellEditor createColumnCombEditor(List<MetadataColumn> columnList,
            Predicate<MetadataColumn> filter) {
        List<String> colArrayList = new ArrayList<String>();
        for (MetadataColumn metaCol : columnList) {
            if (filter == null || filter.test(metaCol)) {
                colArrayList.add(metaCol.getName() == null ? StringUtils.EMPTY : metaCol.getName());
            }
        }
        String[] colArray = colArrayList.toArray(new String[colArrayList.size()]);

        ComboBoxCellEditor comboEditor = new ComboBoxCellEditor(innerTable, colArray, SWT.READ_ONLY);
        if (colArray.length > 0) {
            comboEditor.setValue(0);
        }

        return comboEditor;
    }

}
