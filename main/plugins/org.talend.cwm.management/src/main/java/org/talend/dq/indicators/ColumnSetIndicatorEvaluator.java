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
package org.talend.dq.indicators;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.FileUtils;
import org.talend.fileprocess.FileInputDelimited;
import org.talend.utils.sql.ResultSetUtils;
import org.talend.utils.sql.TalendTypeConvert;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

import com.talend.csv.CSVReader;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class ColumnSetIndicatorEvaluator extends Evaluator<String> {

    private static Logger log = Logger.getLogger(ColumnSetIndicatorEvaluator.class);

    // MOD yyi 2011-02-22 17871:delimitefile
    protected boolean isDelimitedFile = false;

    protected TdXmlSchema tdXmlDocument;

    private boolean isBadlyFormFlatFile = false;

    public ColumnSetIndicatorEvaluator(Analysis analysis) {
        this.analysis = analysis;
        this.isDelimitedFile = analysis.getContext().getConnection() instanceof DelimitedFileConnection;
    }

    @Override
    protected ReturnCode executeSqlQuery(String sqlStatement) throws SQLException {
        ReturnCode ok = new ReturnCode(true);
        AnalysisResult anaResult = analysis.getResults();
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = anaResult.getIndicToRowMap();
        indicToRowMap.clear();
        if (isDelimitedFile) {
            ok = evaluateByDelimitedFile(sqlStatement, ok);
        } else {
            ok = evaluateBySql(sqlStatement, ok);
        }

        return ok;
    }

    /**
     * 
     * DOC qiongli Comment method "getAnalyzedElementsName".
     * 
     * @return
     */
    private List<String> getAnalyzedElementsName() {
        List<String> columnsName = new ArrayList<String>();
        List<ModelElement> analysisElementList = this.analysis.getContext().getAnalysedElements();
        for (ModelElement me : analysisElementList) {
            String name = ModelElementHelper.getName(me);
            if (name != null) {
                columnsName.add(name);
            }

        }
        return columnsName;
    }

    /**
     * 
     * orgnize EList 'objectLs' by SQL.
     * 
     * @param sqlStatement
     * @param ok
     * @return
     * @throws SQLException
     */
    private ReturnCode evaluateBySql(String sqlStatement, ReturnCode ok) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = createStatement();
            if (continueRun()) {
                if (log.isInfoEnabled()) {
                    log.info("Executing query: " + sqlStatement); //$NON-NLS-1$
                }
                statement.execute(sqlStatement);
            }
            // get the results
            resultSet = statement.getResultSet();
            List<String> columnNames = getAnalyzedElementsName();

            if (resultSet == null) {
                String mess = Messages.getString("Evaluator.NoResultSet", sqlStatement); //$NON-NLS-1$
                log.warn(mess);
                ok.setReturnCode(mess, false);
                return ok;
            }
            EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
            indicToRowMap.clear();
            while (resultSet.next()) {
                // MOD yyi 2012-04-11 TDQ-4916:Add memory control for java analysis.
                if (!continueRun()) {
                    break;
                }
                EList<Object> objectLs = new BasicEList<Object>();
                Iterator<String> it = columnNames.iterator();
                while (it.hasNext()) {
                    Object obj = ResultSetUtils.getBigObject(resultSet, it.next());
                    objectLs.add(obj);
                }
                if (objectLs.size() == 0) {
                    continue;
                }
                handleObjects(objectLs, resultSet);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            closeConnection();
        }

        return ok;
    }

    /**
     * 
     * orgnize EList 'objectLs' for DelimitedFile connection.
     * 
     * @param sqlStatement
     * @param returnCode
     * @return
     */
    private ReturnCode evaluateByDelimitedFile(String sqlStatement, ReturnCode returnCode) {
        DelimitedFileConnection fileConnection = (DelimitedFileConnection) analysis.getContext().getConnection();
        String path = JavaSqlFactory.getURL(fileConnection);

        String rowSeparator = JavaSqlFactory.getRowSeparatorValue(fileConnection);
        IPath iPath = new Path(path);
        File file = iPath.toFile();
        if (!file.exists()) {
            returnCode.setReturnCode(Messages.getString("ColumnSetIndicatorEvaluator.FileNotFound", file.getName()), false); //$NON-NLS-1$ 
            return returnCode;
        }
        CSVReader csvReader = null;
        try {
            List<ModelElement> analysisElementList = this.analysis.getContext().getAnalysedElements();
            EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
            indicToRowMap.clear();

            if (Escape.CSV.equals(fileConnection.getEscapeType())) {
                // use CsvReader to parse.
                csvReader = FileUtils.createCsvReader(file, fileConnection);
                this.useCsvReader(csvReader, file, fileConnection, analysisElementList);
            } else {
                // use TOSDelimitedReader in FileInputDelimited to parse.
                FileInputDelimited fileInputDelimited = AnalysisExecutorHelper.createFileInputDelimited(fileConnection);

                long currentRow = JavaSqlFactory.getHeadValue(fileConnection);
                int columsCount = 0;
                while (fileInputDelimited.nextRecord()) {
                    if (!continueRun()) {
                        break;
                    }
                    currentRow++;
                    if (columsCount == 0) {
                        columsCount = fileInputDelimited.getColumnsCountOfCurrentRow();
                    }
                    String[] rowValues = new String[columsCount];
                    for (int i = 0; i < columsCount; i++) {
                        rowValues[i] = fileInputDelimited.get(i);
                    }
                    orgnizeObjectsToHandel(path, rowValues, currentRow, analysisElementList, rowSeparator);
                }
                // TDQ-5851~
                fileInputDelimited.close();
            }
        } catch (Exception e) {
            log.error(e, e);
            returnCode.setReturnCode(e.getMessage(), false);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }
        }

        return returnCode;
    }

    private void useCsvReader(CSVReader csvReader, File file, DelimitedFileConnection dfCon,
            List<ModelElement> analysisElementList) throws Exception {

        FileUtils.initializeCsvReader(dfCon, csvReader);

        long currentRecord = 0;
        int limitValue = JavaSqlFactory.getLimitValue(dfCon);
        int headValue = JavaSqlFactory.getHeadValue(dfCon);
        for (int i = 0; i < headValue && csvReader.readNext(); i++) {
            // do nothing, just ignore the header part
        }

        while (csvReader.readNext()) {
            currentRecord++;
            if (!continueRun() || limitValue != -1 && currentRecord > limitValue) {
                break;
            }
            String[] rowValues = csvReader.getValues();
            this.orgnizeObjectsToHandel(dfCon.getFilePath(), rowValues, currentRecord, analysisElementList,
                    dfCon.getFieldSeparatorValue());

        }
    }

    /**
     * 
     * orgnize a List by a row,then call 'handleObjects(...)'.
     * 
     * @param rowValues
     * @param currentRow
     * @param analysisElementList
     * @param separator
     */
    private void orgnizeObjectsToHandel(String fileName, String[] rowValues, long currentRow,
            List<ModelElement> analysisElementList, String separator) {
        EList<Object> objectLs = new BasicEList<Object>();
        MetadataColumn mColumn = null;
        Object object = null;
        for (int i = 0; i < analysisElementList.size(); i++) {
            mColumn = (MetadataColumn) analysisElementList.get(i);
            Integer position = ColumnHelper.getColumnIndex(mColumn);
            // MOD qiongli 2011-4-2,bug 20033,warning with a badly form file
            if (position == null || position >= rowValues.length) {
                log.warn(Messages.getString("DelimitedFileIndicatorEvaluator.incorrectData", //$NON-NLS-1$
                        mColumn.getLabel(), currentRow, fileName));
                if (!isBadlyFormFlatFile) {
                    isBadlyFormFlatFile = true;
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openWarning(null,
                                    Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Title"), //$NON-NLS-1$
                                    Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Message")); //$NON-NLS-1$
                        }
                    });
                }
                continue;
            }
            object = TalendTypeConvert.convertToObject(mColumn.getTalendType(), rowValues[position], mColumn.getPattern());
            objectLs.add(object);

        }
        if (mColumn != null) {
            List<MetadataColumn> columnList = ColumnHelper.getColumnOwnerAsMetadataTable(mColumn).getColumns();
            handleObjects(objectLs, rowValues, columnList);
        }
    }

    /**
     * DOC qiongli Comment method "handleObjects".
     * 
     * @param objectLs
     * @throws SQLException
     */
    private void handleObjects(EList<Object> objectLs, ResultSet resultSet) throws SQLException {
        if (objectLs.size() == 0) {
            return;
        }
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        // EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
        // int recordIncrement = 0;
        for (Indicator indicator : indicators) {
            if (!this.continueRun()) {
                break;
            }
            if (ColumnsetPackage.eINSTANCE.getColumnSetMultiValueIndicator().isSuperTypeOf(indicator.eClass())) {
                indicator.handle(objectLs);
            }
        }
    }

    /**
     * handle Objects and store data for delimited file .
     * 
     * @param objectLs
     * @param rowValues
     * @param metadataColumn is one of analysedElements.it is used to get its Table then get the table's columns.
     */
    private void handleObjects(EList<Object> objectLs, String[] rowValues, List<MetadataColumn> columnList) {
        if (objectLs.size() == 0) {
            return;
        }

        EList<Indicator> indicators = analysis.getResults().getIndicators();
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
        int recordIncrement = 0;
        if (indicators != null) {
            for (Indicator indicator : indicators) {
                if (!this.continueRun()) {
                    break;
                }
                if (ColumnsetPackage.eINSTANCE.getColumnSetMultiValueIndicator().isSuperTypeOf(indicator.eClass())) {
                    indicator.handle(objectLs);
                    // feature 19192,store all rows value for RowCountIndicator
                    if (indicator instanceof SimpleStatIndicator) {
                        SimpleStatIndicator simpIndi = (SimpleStatIndicator) indicator;
                        for (Indicator leafIndicator : simpIndi.getLeafIndicators()) {
                            if (!this.continueRun()) {
                                break;
                            }
                            // MOD 20130517 yyin: TDQ-7279 Column Set Analysis on FIle/DB - Can't view rows for
                            // (distinct count, unique count, duplicate count, etc).
                            if (!analysis.getParameters().isStoreData()) {// ~
                                continue;
                            }
                            List<Object[]> valueObjectList = initDataSet(leafIndicator, indicToRowMap);

                            recordIncrement = valueObjectList.size();
                            Object[] valueObject = new Object[columnList.size()];
                            if (recordIncrement < analysis.getParameters().getMaxNumberRows()) {
                                for (int j = 0; j < columnList.size(); j++) {
                                    if (!this.continueRun()) {
                                        break;
                                    }
                                    Object newobject = PluginConstant.EMPTY_STRING;
                                    // if (recordIncrement < analysis.getParameters().getMaxNumberRows()) {
                                    if (j < rowValues.length) {
                                        newobject = rowValues[j];
                                    }
                                    if (recordIncrement < valueObjectList.size()) {
                                        valueObjectList.get(recordIncrement)[j] = newobject;
                                    } else {
                                        valueObject[j] = newobject;
                                        valueObjectList.add(valueObject);
                                    }
                                    // }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * ADD yyi 2011-02-22 17871:delimitefile
     * 
     * @see org.talend.dq.indicators.Evaluator#checkConnection()
     */
    @Override
    protected ReturnCode checkConnection() {
        if (isDelimitedFile) {
            return new ReturnCode();
        }
        return super.checkConnection();
    }

    /*
     * ADD yyi 2011-02-24 17871:delimitefile
     * 
     * @see org.talend.dq.indicators.Evaluator#closeConnection()
     */
    @Override
    protected ReturnCode closeConnection() {
        if (isDelimitedFile) {
            return new ReturnCode();
        }
        return super.closeConnection();
    }

    protected List<Object[]> initDataSet(Indicator indicator, EMap<Indicator, AnalyzedDataSet> indicToRowMap) {
        AnalyzedDataSet analyzedDataSet = indicToRowMap.get(indicator);
        List<Object[]> valueObjectList = null;
        if (analyzedDataSet == null) {
            analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
            indicToRowMap.put(indicator, analyzedDataSet);
            analyzedDataSet.setDataCount(analysis.getParameters().getMaxNumberRows());
            analyzedDataSet.setRecordSize(0);
        }
        valueObjectList = analyzedDataSet.getData();
        if (valueObjectList == null) {
            valueObjectList = new ArrayList<Object[]>();
            analyzedDataSet.setData(valueObjectList);
        }

        return valueObjectList;
    }

    /**
     * store data which from 'simpleIndicator.getListRows()' except RowCountIndicator.
     * 
     * @param indicToRowMap
     */
    private void storeDataSet() {
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
        for (Indicator indicator : analysis.getResults().getIndicators()) {
            if (indicator instanceof SimpleStatIndicator) {
                SimpleStatIndicator simpleIndicator = (SimpleStatIndicator) indicator;
                if (!analysis.getParameters().isStoreData()) {
                    break;
                }
                if (simpleIndicator.isUsedMapDBMode() && AnalysisHelper.isJavaExecutionEngine(analysis)) {
                    // nothing need to do
                } else {
                    List<Object[]> listRows = simpleIndicator.getListRows();
                    if (listRows == null || listRows.isEmpty()) {
                        break;
                    }
                    for (Indicator leafIndicator : simpleIndicator.getLeafIndicators()) {
                        if (leafIndicator instanceof RowCountIndicator) {
                            continue;
                        }
                        List<Object[]> dataList = new ArrayList<Object[]>();
                        AnalyzedDataSet analyzedDataSet = indicToRowMap.get(leafIndicator);
                        if (analyzedDataSet == null) {
                            analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
                            indicToRowMap.put(leafIndicator, analyzedDataSet);
                            analyzedDataSet.setDataCount(analysis.getParameters().getMaxNumberRows());
                            analyzedDataSet.setRecordSize(0);
                        }

                        for (int i = 0; i < listRows.size(); i++) {
                            // if (dataList.size() >= analyzedDataSet.getDataCount()) {
                            // break;
                            // }
                            Object[] object = listRows.get(i);
                            // the last element store the count value.
                            Object count = object[object.length > 0 ? object.length - 1 : 0];
                            if (leafIndicator instanceof DistinctCountIndicator) {
                                dataList.add(object);
                            } else if (leafIndicator instanceof UniqueCountIndicator) {
                                if (count != null && NumberUtils.isNumber(count + PluginConstant.EMPTY_STRING)) {
                                    if (Long.valueOf(count + PluginConstant.EMPTY_STRING).longValue() == 1) {
                                        dataList.add(object);
                                    }
                                }
                            } else if (leafIndicator instanceof DuplicateCountIndicator) {
                                if (count != null && NumberUtils.isNumber(count + PluginConstant.EMPTY_STRING)) {
                                    if (Long.valueOf(count + PluginConstant.EMPTY_STRING).longValue() > 1) {
                                        dataList.add(object);
                                    }
                                }
                            }
                        }
                        analyzedDataSet.setData(dataList);
                    }
                    // MOD sizhaoliu TDQ-7144 clear the listRows after usage for drill down
                    if (!simpleIndicator.isStoreData()) {
                        simpleIndicator.setListRows(new ArrayList<Object[]>());
                    }
                }
            }
            if (indicator instanceof AllMatchIndicator) {
                AllMatchIndicator allMatchIndicator = (AllMatchIndicator) indicator;
                if (!allMatchIndicator.isStoreData()) {
                    allMatchIndicator.setListRows(new ArrayList<Object[]>());
                }
            }
        }
    }

    public TdXmlSchema getTdXmlDocument() {
        return this.tdXmlDocument;
    }

    public void setTdXmlDocument(TdXmlSchema tdXmlDocument) {
        this.tdXmlDocument = tdXmlDocument;
    }

    @Override
    public ReturnCode evaluateIndicators(String sqlStatement, boolean closeConnection) {
        ReturnCode returnCode = super.evaluateIndicators(sqlStatement, closeConnection);
        storeDataSet();
        return returnCode;
    }
}
