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
package org.talend.dataquality.record.linkage.ui.composite.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.talend.dq.analysis.data.preview.DataPreviewHandler;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * The data sample table on the column analysis
 *
 */
public class ColumnAnalysisDataSamTable extends DataSampleTable {

    private String dataFilter = StringUtils.EMPTY;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#createFixedColumns()
     */
    @Override
    protected Collection<? extends String> createFixedColumns(int columnSize) {
        return new ArrayList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#getFixedColumnCount()
     */
    @Override
    protected int getFixedColumnCount() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#isContainGID(java.util.List)
     */
    @Override
    protected boolean isContainGID(List<Object[]> results) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#initTablePanelLayoutPanel(org.eclipse
     * .ui.forms.widgets.Section, org.eclipse.swt.layout.GridData,
     * org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable.TControl)
     */
    @Override
    protected void initTablePanelLayoutPanel(Composite dataTableComp, GridData layoutDataFillBoth, TControl tControl) {
        // when open the editor, the dataSampleSection's width is 0, just set the layout fill both.
        tablePanel.setLayoutData(layoutDataFillBoth);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#getPreviewData()
     */
    @Override
    protected List<Object[]> createPreviewData(ModelElement[] columns) throws SQLException {
        DataPreviewHandler dataPreviewHandler = new DataPreviewHandler();
        dataPreviewHandler.setDataFilter(dataFilter);
        return dataPreviewHandler.createPreviewData(columns, getLimitNumber(), isShowRandomData());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#handleEmptyRow(orgomg.cwm.objectmodel
     * .core.ModelElement[], java.util.List)
     */
    @Override
    protected List<Object[]> handleEmptyRow(ModelElement[] columns, List<Object[]> listOfData) {
        return listOfData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.table.DataSampleTable#handleGID(org.eclipse.swt.widgets.Composite
     * , java.util.List)
     */
    @Override
    protected TControl handleGID(Composite parentContainer, List<Object[]> results) {
        return null;
    }

    /**
     * DOC talend Comment method "setDataFilter".
     * 
     * @param dataFilterString
     */
    public void setDataFilter(String dataFilterString) {
        dataFilter = dataFilterString;
    }

}
