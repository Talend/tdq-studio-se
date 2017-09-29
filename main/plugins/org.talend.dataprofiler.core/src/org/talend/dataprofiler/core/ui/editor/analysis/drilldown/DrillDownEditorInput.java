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
package org.talend.dataprofiler.core.ui.editor.analysis.drilldown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.indicator.ColumnFilter;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.utils.DrillDownUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.analysis.impl.AnalyzedDataSetImpl;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.LengthIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.ColumnSetDBMap;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.indicators.mapdb.DBSet;
import org.talend.dataquality.indicators.validation.IDataValidationFactory;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DrillDownEditorInput implements IEditorInput {

    private Analysis analysis;

    private Indicator currIndicator;

    private MenuItemEntity menuItemEntity;

    private ChartDataEntity dataEntity;

    /**
     * Getter for dataEntity.
     * 
     * @return the dataEntity
     */
    public ChartDataEntity getDataEntity() {
        return this.dataEntity;
    }

    /**
     * Sets the dataEntity.
     * 
     * @param dataEntity the dataEntity to set
     */
    public void setDataEntity(ChartDataEntity dataEntity) {
        this.dataEntity = dataEntity;
    }

    private String[] columnHeader = null;

    private String[][] columnValue = null;

    public DrillDownEditorInput() {

    }

    public DrillDownEditorInput(Analysis analysis, ChartDataEntity dataEntity, MenuItemEntity menuItemEntity) {
        this.analysis = analysis;
        this.currIndicator = dataEntity.getIndicator();
        this.menuItemEntity = menuItemEntity;
        this.dataEntity = dataEntity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return currIndicator.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText() {
        return analysis.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public Indicator getCurrIndicator() {
        return currIndicator;
    }

    public void setCurrIndicator(Indicator currIndicator) {
        this.currIndicator = currIndicator;
    }

    public String getMenuType() {
        return this.menuItemEntity.getLabel();
    }

    public String getSelectValue() {
        return this.dataEntity.getLabel();
    }

    public String getComputeValue() {
        if (DrillDownUtils.judgeMenuType(this.getMenuType(), DrillDownUtils.MENU_INVALID_TYPE)) {
            return ((PatternChartDataEntity) this.dataEntity).getNumNoMatch();
        } else if (DrillDownUtils.judgeMenuType(this.getMenuType(), DrillDownUtils.MENU_VALID_TYPE)) {
            return ((PatternChartDataEntity) this.dataEntity).getNumMatch();
        }
        return this.dataEntity.getValue();
    }

    /**
     * 
     * DataSet is used to be the input on the export wizard. unchecked is for the type of mapDB else will have a warning
     * 
     * @param controller
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getDataSetForMapDB(int pageSize) {
        List<String> columnElementList = filterAdaptColumnHeader();
        columnHeader = new String[columnElementList.size()];
        int headerIndex = 0;
        for (String columnElement : columnElementList) {
            columnHeader[headerIndex++] = columnElement;
        }
        AbstractDB<?> mapDB = getMapDB();
        if (mapDB == null) {
            return null;
        }
        AnalysisType analysisType = analysis.getParameters().getAnalysisType();
        if (AnalysisType.COLUMN_SET == analysisType) {
            Long size = getCurrentIndicatorResultSize();
            if (ColumnSetDBMap.class.isInstance(mapDB)) {
                return SqlExplorerUtils.getDefault().createMapDBColumnSetDataSet(columnHeader, (ColumnSetDBMap) mapDB, size,
                        IDataValidationFactory.INSTANCE.createValidation(currIndicator), pageSize);

            }
        }

        if (DBSet.class.isInstance(mapDB)) {
            return SqlExplorerUtils.getDefault().createMapDBSetDataSet(columnHeader, (DBSet<Object>) mapDB, pageSize);
        } else {
            ColumnFilter columnFilter = getColumnFilter();
            Long itemSize = getItemSize(mapDB);
            return SqlExplorerUtils.getDefault().createMapDBDataSet(columnHeader, (DBMap<Object, List<Object>>) mapDB, pageSize,
                    columnFilter, itemSize);
        }
    }

    /**
     * Get the MapDB which used to drill down data
     * 
     * @return
     */
    public AbstractDB<Object> getMapDB() {
        return DrillDownUtils.getMapDB(dataEntity, analysis, menuItemEntity);
    }

    public Object getDataSet() {
        List<String> columnElementList = filterAdaptColumnHeader();
        columnHeader = new String[columnElementList.size()];
        int headerIndex = 0;
        for (String columnElement : columnElementList) {
            columnHeader[headerIndex++] = columnElement;
        }
        List<Object[]> newColumnElementList = filterAdaptDataList();
        if (newColumnElementList.size() <= 0) {
            columnValue = new String[0][0];
            return SqlExplorerUtils.getDefault().createDataSet(columnHeader, columnValue);
        }
        // MOD qiongli 2011-4-8,bug 19192.delimited file may has diffrent number of columns for every row.
        if (DrillDownUtils.judgeMenuType(getMenuType(), DrillDownUtils.MENU_VALUE_TYPE)) {
            columnValue = new String[newColumnElementList.size()][newColumnElementList.get(0).length];
        } else {
            columnValue = new String[newColumnElementList.size()][columnElementList.size()];
        }
        int rowIndex = 0;
        for (Object[] tableRow : newColumnElementList) {
            int columnIndex = 0;
            for (Object tableValue : tableRow) {
                if (tableValue == null) {
                    if (newColumnElementList.get(0).length != columnElementList.size()) {
                        continue;
                    }
                }// ~
                columnValue[rowIndex][columnIndex++] = tableValue == null ? "<null>" : tableValue.toString(); //$NON-NLS-1$
            }
            rowIndex++;
        }
        return SqlExplorerUtils.getDefault().createDataSet(columnHeader, columnValue);
    }

    /**
     * Get the result of current indicator.
     * 
     * @return if view values then return result of current indicator else return the size of the mapDB
     */
    public Long getItemSize(AbstractDB<?> mapDB) {
        if (isColumnSetIndicator()) {
            return getColumnSetIndicatorResultSize();
        } else {
            return mapDB == null ? 0l : Long.valueOf(mapDB.size());
        }
    }

    /**
     * Create columnFilter for current columnSet. It will be used when we can drill down both current column and whole
     * of the row
     * 
     * @return
     */
    public ColumnFilter getColumnFilter() {
        Integer[] columnIndexArray = getColumnIndexArray();
        ColumnFilter filter = null;
        if (columnIndexArray != null) {
            filter = new ColumnFilter(columnIndexArray);
        }
        return filter;
    }

    /**
     * Get the result of current indicator.
     * 
     * @return
     */
    private Long getCurrentIndicatorResultSize() {
        Long itemsSize = 0l;
        if (isColumnSetIndicator()) {
            itemsSize = getColumnSetIndicatorResultSize();
        } else {
            itemsSize = getColumnIndicatorResultsize();
        }
        return itemsSize;
    }

    /**
     * DOC talend Comment method "getColumnIndicatorResultsize".
     * 
     * @return
     */
    private Long getColumnIndicatorResultsize() {
        return currIndicator.getIntegerValue();
    }

    /**
     * DOC talend Comment method "isColumnSetIndicator".
     * 
     * @return
     */
    private boolean isColumnSetIndicator() {
        Analysis analysis = this.getAnalysis();
        AnalysisType analysisType = analysis.getParameters().getAnalysisType();
        return AnalysisType.COLUMN_SET == analysisType;
    }

    /**
     * DOC talend Comment method "getColumnSetIndicatorResultSize".
     * 
     * @param itemsSize
     * @return
     */
    private Long getColumnSetIndicatorResultSize() {
        Long itemsSize = 0l;
        SimpleStatIndicator simpleStatIndicator = null;
        // Find simpleStatIndicator from result of analysis
        for (Indicator indicator : analysis.getResults().getIndicators()) {
            if (SimpleStatIndicator.class.isInstance(indicator)) {
                simpleStatIndicator = (SimpleStatIndicator) indicator;
                break;
            }
        }
        // Get the Result from simpleStatIndicator by currIndicator
        if (simpleStatIndicator != null) {
            if (DuplicateCountIndicator.class.isInstance(currIndicator)) {
                itemsSize = simpleStatIndicator.getDuplicateCount();
            } else if (DistinctCountIndicator.class.isInstance(currIndicator)) {
                itemsSize = simpleStatIndicator.getDistinctCount();
            } else if (UniqueCountIndicator.class.isInstance(currIndicator)) {
                itemsSize = simpleStatIndicator.getUniqueCount();
            } else if (RowCountIndicator.class.isInstance(currIndicator)) {
                itemsSize = simpleStatIndicator.getCount();
            }
        }
        return itemsSize;
    }

    public Indicator getGenerateMapDBIndicator() {
        AnalysisType analysisType = analysis.getParameters().getAnalysisType();
        if (AnalysisType.COLUMN_SET == analysisType) {
            for (Indicator indicator : analysis.getResults().getIndicators()) {
                if (SimpleStatIndicator.class.isInstance(indicator)) {
                    return indicator;
                }
            }
        }
        return this.currIndicator;
    }

    public boolean computeColumnValueLength(List<Object[]> newColumnElementList) {
        List<Integer> maxLength = new ArrayList<Integer>();
        for (Object[] columnValue : newColumnElementList) {
            maxLength.add(columnValue.length);
        }
        // ADD msjian 2011-6-20 22549: fixed another NoSuchElementException
        if (maxLength.size() == 0 || maxLength == null) {
            return true;
        }
        return Collections.max(maxLength) == Collections.min(maxLength);
    }

    /**
     * 
     * 
     * @return get the data which will be displayed on the drill down editor.
     */
    public List<Object[]> filterAdaptDataList() {
        // get columnValue
        List<Object[]> newColumnElementList = new ArrayList<Object[]>();
        AnalyzedDataSet analysisDataSet = this.getAnalysis().getResults().getIndicToRowMap().get(currIndicator);
        // TDQ-10545 : when restart studio and without run analysis, analysisDataSet is null
        if (analysisDataSet == null) {
            return newColumnElementList;
        }
        // TDQ-10545~
        if (analysisDataSet.getData() != null && analysisDataSet.getData().size() > 0) {
            List<Object[]> dataList = analysisDataSet.getData();
            newColumnElementList.addAll(getDesignatedData(dataList));
        } else if (analysisDataSet.getFrequencyData() != null && analysisDataSet.getFrequencyData().size() > 0) {
            String selectValue = this.getSelectValue();
            if (currIndicator instanceof LengthIndicator) {
                selectValue = ((LengthIndicator) currIndicator).getLength().toString();
            }
            // MOD yyi 2011-12-14 TDQ-4166:View rows for Date Pattern Frequency Indicator.
            if (currIndicator instanceof DatePatternFreqIndicator) {
                for (Object expression : analysisDataSet.getFrequencyData().keySet()) {
                    if (Pattern.matches(((DatePatternFreqIndicator) currIndicator).getRegex(selectValue), expression.toString())) {
                        newColumnElementList.addAll(analysisDataSet.getFrequencyData().get(expression));
                    }
                }
            } else {
                // MOD msjian TDQ-4617 2012-2-8: fixed a NPE
                List<Object[]> list = analysisDataSet.getFrequencyData().get(selectValue);
                if (list != null && list.size() > 0) {
                    newColumnElementList.addAll(list);
                }
                // TDQ-4617 ~
            }
        } else if (analysisDataSet.getPatternData() != null && analysisDataSet.getPatternData().size() > 0) {
            if (DrillDownUtils.judgeMenuType(getMenuType(), DrillDownUtils.MENU_INVALID_TYPE)) {
                newColumnElementList.addAll(getDesignatedData((List<Object[]>) analysisDataSet.getPatternData().get(
                        AnalyzedDataSetImpl.INVALID_VALUE)));
            } else if (DrillDownUtils.judgeMenuType(getMenuType(), DrillDownUtils.MENU_VALID_TYPE)) {
                newColumnElementList.addAll(getDesignatedData((List<Object[]>) analysisDataSet.getPatternData().get(
                        AnalyzedDataSetImpl.VALID_VALUE)));
            }
        }
        return newColumnElementList;
    }

    /**
     * 
     * DOC zshen Comment method "getDesignatedData".
     * 
     * @return make column mapping with data
     */
    private List<Object[]> getDesignatedData(List<Object[]> dataList) {

        ModelElement analysisElement = currIndicator.getAnalyzedElement();

        List<Object[]> returnDataList = new ArrayList<Object[]>();
        if (dataList == null || dataList.size() < 0) {
            return returnDataList;
        }
        if (DrillDownUtils.judgeMenuType(this.getMenuType(), DrillDownUtils.MENU_VALUE_TYPE)) {
            int offset = 0;
            // MOD qiongli 2011-3-3 feature 19192 drill down for columnSet with jave engine.
            if (analysisElement == null && currIndicator.eContainer() instanceof SimpleStatIndicator) {
                returnDataList = dataList;

            } else {
                if (analysisElement instanceof MetadataColumn) {
                    MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable((MetadataColumn) analysisElement);
                    List<MetadataColumn> columnElementList = mTable.getColumns();
                    offset = columnElementList.indexOf(analysisElement);
                }
                // Added yyin 20120608 TDQ-3589
                if (currIndicator instanceof DuplicateCountIndicator) {
                    for (Object obj : ((DuplicateCountIndicator) currIndicator).getDuplicateValues()) {
                        Object[] newObj = new Object[1];
                        newObj[0] = obj;
                        returnDataList.add(newObj);
                    }
                    return returnDataList;
                }// ~
                for (Object[] obj : dataList) {
                    Object[] newObj = new Object[1];
                    newObj[0] = obj[offset];
                    returnDataList.add(newObj);
                }
            }

        } else {
            returnDataList = dataList;
        }
        return returnDataList;
    }

    /**
     * 
     * create column header for columnSet analysis.
     * 
     * @param simpInd
     * @param columnElementList
     */
    private List<String> columnHeaderForColumnSet(SimpleStatIndicator simpInd) {

        List<String> columnElementList = new ArrayList<String>();
        if (simpInd.getAnalyzedColumns().size() == 0) {
            return columnElementList;
        }
        if (DrillDownUtils.judgeMenuType(this.getMenuType(), DrillDownUtils.MENU_VALUE_TYPE)) {
            for (ModelElement mod : simpInd.getAnalyzedColumns()) {
                columnElementList.add(ModelElementHelper.getName(mod));
            }
        } else {
            boolean isDelimitedFile = false;
            for (ModelElement mColumn : simpInd.getAnalyzedColumns()) {
                TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(mColumn);
                if (tdColumn == null) {
                    isDelimitedFile = true;
                }
                break;
            }
            if (isDelimitedFile) {
                List<MetadataColumn> columnList = ColumnHelper.getColumnOwnerAsMetadataTable(simpInd.getAnalyzedColumns().get(0))
                        .getColumns();
                for (MetadataColumn mdColumn : columnList) {
                    columnElementList.add(mdColumn.getLabel());
                }
            } else {
                List<TdColumn> columnList = TableHelper.getColumns(SwitchHelpers.TABLE_SWITCH.doSwitch((simpInd
                        .getAnalyzedColumns().get(0).eContainer())));
                for (TdColumn tdColumn : columnList) {
                    columnElementList.add(tdColumn.getLabel());
                }
            }
        }
        return columnElementList;

    }

    /**
     * 
     * DOC zshen Comment method "filterAdaptColumnHeader".
     * 
     * @returnget the name of column which will be displayed on the drill down editor.
     */
    public List<String> filterAdaptColumnHeader() {
        // get columnHeader

        Indicator indicator = this.getCurrIndicator();
        ModelElement analysisElement = indicator.getAnalyzedElement();
        String menuType = this.getMenuType();
        List<String> columnElementList = new ArrayList<String>();
        // MOD qiongli 2011-3-3,feature 19192 ,drill down for columnSet with java engine .
        if (analysisElement == null && indicator.eContainer() instanceof SimpleStatIndicator) {
            columnElementList = columnHeaderForColumnSet((SimpleStatIndicator) indicator.eContainer());

        } else {
            // MOD qiongli 2011-1-9 feature 16796
            if (DrillDownUtils.judgeMenuType(menuType, DrillDownUtils.MENU_VALUE_TYPE)) {

                columnElementList.add(ModelElementHelper.getName(indicator.getAnalyzedElement()));
            } else if (analysisElement instanceof TdColumn) {
                for (TdColumn column : getColumnsByTdColumn((TdColumn) analysisElement)) {
                    columnElementList.add(column.getName());
                }

            } else if (analysisElement instanceof MetadataColumn) {
                MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable((MetadataColumn) analysisElement);
                for (MetadataColumn mColumn : mTable.getColumns()) {
                    columnElementList.add(mColumn.getLabel());
                }
            }
        }

        return columnElementList;
    }

    /**
     * Get index of column whiche will be used on the dirll down. Note that One indicator only belong one column so that
     * the array of retrun value just contain one element.
     * 
     * And if we create new map for view values menu rather than used same map with view rows menu then method can be
     * removed
     * 
     * @return
     */
    public Integer[] getColumnIndexArray() {
        if (!DrillDownUtils.judgeMenuType(this.getMenuType(), DrillDownUtils.MENU_VALUE_TYPE)
                || !UniqueCountIndicator.class.isInstance(currIndicator)) {
            return null;
        }
        List<Integer> indexArray = new ArrayList<Integer>();
        Indicator indicator = this.getCurrIndicator();
        ModelElement analysisElement = indicator.getAnalyzedElement();
        int index = 0;
        if (analysisElement instanceof TdColumn) {
            List<TdColumn> columns = getColumnsByTdColumn((TdColumn) analysisElement);
            for (TdColumn column : columns) {
                if (column.getName().equals(analysisElement.getName())) {
                    indexArray.add(index);
                    // Note that One indicator only belong one column so that
                    // break at here.
                    break;
                }
                index++;
            }

        } else if (analysisElement instanceof MetadataColumn) {
            MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable((MetadataColumn) analysisElement);
            for (MetadataColumn mColumn : mTable.getColumns()) {
                if (mColumn.getLabel().equals(analysisElement.getName())) {
                    indexArray.add(index);
                    // Note that One indicator only belong one column so that
                    // break at here.
                    break;
                }
                index++;
            }
        }

        return indexArray.toArray(new Integer[indexArray.size()]);
    }

    /**
     * DOC talend Comment method "getColumnsByIndicator".
     * 
     * @param analysisElement
     * @return
     */
    protected List<TdColumn> getColumnsByTdColumn(TdColumn analysisElement) {
        EObject eContainer = analysisElement.eContainer();
        if (eContainer == null) {
            return new ArrayList<TdColumn>();
        }

        ColumnSet columSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(eContainer);
        if (columSet == null) {
            return new ArrayList<TdColumn>();
        }
        List<TdColumn> columns = ColumnSetHelper.getColumns(columSet);
        return columns;
    }

    public boolean isDataSpill() {
        return analysis.getResults().getIndicToRowMap().get(currIndicator).getDataCount() < Double.parseDouble(getComputeValue());
    }

}
