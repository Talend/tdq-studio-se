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
package org.talend.dataquality.record.linkage.ui.composite;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.KeyDefinition;

/**
 * created by zhao on Aug 16, 2013 Abstract table compoiste. Blocking key, matching key survivorship key table are
 * intended to be extended.
 * 
 */
public abstract class AbsMatchAnalysisTableComposite<T> extends Composite implements PropertyChangeListener {

    protected List<String> headers = new ArrayList<String>();

    protected AbstractMatchAnalysisTableViewer<T> tableViewer = null;

    private boolean isAddColumn = true;

    protected List<MetadataColumn> columnList = new ArrayList<MetadataColumn>();

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * DOC zshen MatchRuleComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AbsMatchAnalysisTableComposite(Composite parent, int style) {
        super(parent, style);

    }

    /**
     * create Table when you want to display it
     */
    public void createContent() {
        initHeaders();
        createTable();
    }

    /**
     * Sets the columnMap.
     * 
     * @param columnMap the columnMap to set
     */
    public void setColumnList(List<MetadataColumn> columnList) {
        this.columnList.addAll(columnList);
    }

    /**
     * DOC zshen Comment method "initHeaders".
     */
    abstract protected void initHeaders();

    abstract protected AbstractMatchAnalysisTableViewer<T> createTableViewer();

    /**
     * DOC zshen Comment method "createTable".
     */
    protected abstract void createTable();

    protected int getTableStyle() {
        int style = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        return style;
    }

    public void setInput(List<T> inputs) {
        tableViewer.setInput(inputs);
    }

    public Object getInput() {
        return tableViewer.getInput();
    }

    public Boolean addKeyDefinition(String column, List<T> keyDefs) {
        return tableViewer.addElement(column, keyDefs);
    }

    public Boolean addKeyDefinition(T keyDef, List<T> keyList) {
        return tableViewer.addElement(keyDef, keyList);
    }

    public void removeKeyDefinition(String column, List<T> keyDefs) {
        tableViewer.removeElement(column, keyDefs);
    }

    public void removeKeyDefinition(T keyDef, List<T> keyDefs) {
        tableViewer.removeElement(keyDef, keyDefs);
    }

    public void moveUpKeyDefinition(T keyDef, List<T> keyDefs) {
        tableViewer.moveUpElement(keyDef, keyDefs);
    }

    public void moveDownKeyDefinition(T keyDef, List<T> keyDefs) {
        tableViewer.moveDownElement(keyDef, keyDefs);
    }

    public ISelection getSelectItems() {
        return tableViewer.getSelection();
    }

    public void serViewerSorter(ViewerSorter sorter) {
        tableViewer.setSorter(sorter);
    }

    public void selectAllItem(List<KeyDefinition> bkdList) {
        tableViewer.selectAllItem(bkdList);
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
    public void setAddColumn(boolean isAddColumn) {
        this.isAddColumn = isAddColumn;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (MatchAnalysisConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
        } else if (MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH.equals(evt.getPropertyName())) {
            listeners.firePropertyChange(MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH, true, false);
        }
    }

}
