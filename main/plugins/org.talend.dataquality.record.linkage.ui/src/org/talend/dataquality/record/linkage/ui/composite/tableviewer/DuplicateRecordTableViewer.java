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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.DuplicateRecordTableLabelProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAnalysisTableContentProvider;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.KeyDefinition;

/**
 * created by zhao on Aug 19, 2013 Detailled comment
 * 
 */
public class DuplicateRecordTableViewer extends AbstractMatchAnalysisTableViewer<Object> {

    private MatchAnalysisTableContentProvider contentProvider = null;

    private DuplicateRecordTableLabelProvider labelProvider = null;

    private List<String> tableHeaders = new ArrayList<String>();

    /**
     * DOC zhao DuplicateRecordTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DuplicateRecordTableViewer(Composite parent, int style) {
        super(parent, style, Boolean.TRUE);
        initHeaders();
        initTable(tableHeaders, new ArrayList<MetadataColumn>(), false);
    }

    /**
     * DOC zhao Comment method "initHeaders".
     */
    private void initHeaders() {
        tableHeaders.add(MatchAnalysisConstant.LABEL);
        tableHeaders.add(MatchAnalysisConstant.COUNT);
        tableHeaders.add(MatchAnalysisConstant.PERCENTAGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getDisplayWeight
     * ()
     */
    @Override
    protected int getHeaderDisplayWeight() {
        return 30;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableHeightHint
     * ()
     */
    @Override
    protected int getTableHeightHint() {
        return 140;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableLabelProvider
     * ()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        if (labelProvider == null) {
            labelProvider = new DuplicateRecordTableLabelProvider();
        }
        return labelProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * getTableContentProvider()
     */
    @Override
    protected IContentProvider getTableContentProvider() {
        if (contentProvider == null) {
            contentProvider = new MatchAnalysisTableContentProvider();
        }
        return contentProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableCellModifier
     * ()
     */
    @Override
    protected AbstractMatchCellModifier<Object> getTableCellModifier() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#addElement(java
     * .lang.String, org.talend.dataquality.analysis.Analysis)
     */
    @Override
    public boolean addElement(String columnName, Analysis analysis) {
        // No implementation
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#removeElement
     * (java.lang.String, org.talend.dataquality.analysis.Analysis)
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#addContextMenu()
     */
    @Override
    public void addContextMenu() {
        // No implementation

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * createNewKeyDefinition(java.lang.String)
     */
    @Override
    protected KeyDefinition createNewKeyDefinition(String columnName) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#removeElement
     * (java.lang.String, java.util.List)
     */
    @Override
    public void removeElement(String columnName, List<Object> keyList) {
        // don't need do anything

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getCellEditor
     * (java.util.List, java.util.Map)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnMap) {
        return null;
    }

}
