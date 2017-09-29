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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.core.IRepositoryContextService;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.mapdb.MapDBUtils;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.FileUtils;
import org.talend.fileprocess.FileInputDelimited;
import org.talend.utils.sql.TalendTypeConvert;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

import com.talend.csv.CSVReader;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DelimitedFileIndicatorEvaluator extends IndicatorEvaluator {

    protected DelimitedFileConnection delimitedFileconnection = null;

    private Logger log = Logger.getLogger(DelimitedFileIndicatorEvaluator.class);

    private boolean isBablyForm = false;

    /**
     * DOC qiongli DelimitedFileIndicatorEvaluator constructor comment.
     * 
     * @param analysis
     */
    public DelimitedFileIndicatorEvaluator(Analysis analysis) {
        super(analysis);
        this.analysis = analysis;
    }

    @Override
    protected ReturnCode executeSqlQuery(String sqlStatement) {
        ReturnCode returnCode = new ReturnCode(true);
        if (delimitedFileconnection == null) {
            delimitedFileconnection = (DelimitedFileConnection) analysis.getContext().getConnection();
        }
        if (delimitedFileconnection.isContextMode()) {
            IRepositoryContextService service = CoreRuntimePlugin.getInstance().getRepositoryContextService();
            delimitedFileconnection = (DelimitedFileConnection) service.cloneOriginalValueConnection(delimitedFileconnection);
        }

        String path = JavaSqlFactory.getURL(delimitedFileconnection);
        IPath iPath = new Path(path);

        File file = iPath.toFile();

        if (!file.exists()) {
            returnCode.setReturnCode(Messages.getString("DelimitedFileIndicatorEvaluator.CanNotFindFile"), false); //$NON-NLS-1$
            return returnCode;
        }

        List<ModelElement> analysisElementList = this.analysis.getContext().getAnalysedElements();
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = analysis.getResults().getIndicToRowMap();
        indicToRowMap.clear();

        List<MetadataColumn> columnElementList = new ArrayList<MetadataColumn>();
        for (int i = 0; i < analysisElementList.size(); i++) {
            MetadataColumn mColumn = (MetadataColumn) analysisElementList.get(i);
            MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable(mColumn);
            columnElementList = mTable == null ? columnElementList : mTable.getColumns();
            if (!columnElementList.isEmpty()) {
                break;
            }
        }
        ReturnCode readDataReturnCode = new ReturnCode(true);
        // use CsvReader to parse.
        if (Escape.CSV.equals(delimitedFileconnection.getEscapeType())) {
            readDataReturnCode = useCsvReader(file, analysisElementList, columnElementList, indicToRowMap);
        } else {
            readDataReturnCode = useDelimitedReader(analysisElementList, columnElementList, indicToRowMap);

        }

        // handle error message
        if (!readDataReturnCode.isOk()) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Title"), //$NON-NLS-1$
                            Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Message")); //$NON-NLS-1$
                }
            });
        }

        // Added yyin 20120608 TDQ-3589
        for (MetadataColumn col : columnElementList) {
            List<Indicator> indicators = getIndicators(col.getLabel());
            for (Indicator indicator : indicators) {
                if (indicator instanceof DuplicateCountIndicator) {
                    AnalyzedDataSet analyzedDataSet = indicToRowMap.get(indicator);
                    if (analyzedDataSet == null) {
                        analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
                        indicToRowMap.put(indicator, analyzedDataSet);
                        analyzedDataSet.setDataCount(analysis.getParameters().getMaxNumberRows());
                        analyzedDataSet.setRecordSize(0);
                    }
                    // indicator.finalizeComputation();
                    addResultToIndicatorToRowMap(indicator, indicToRowMap);
                }
            }
        }// ~

        return returnCode;
    }

    /**
     * DOC talend Comment method "useDelimitedReader".
     * 
     * @param file
     * @param delimitedFileconnection2
     * @param analysisElementList
     * @param columnElementList
     * @param indicToRowMap
     * @return
     */
    private ReturnCode useDelimitedReader(List<ModelElement> analysisElementList, List<MetadataColumn> columnElementList,
            EMap<Indicator, AnalyzedDataSet> indicToRowMap) {
        // use TOSDelimitedReader in FileInputDelimited to parse.
        ReturnCode returnCode = new ReturnCode(true);
        try {
            FileInputDelimited fileInputDelimited = createFileInputDelimited();

            long currentRow = JavaSqlFactory.getHeadValue(delimitedFileconnection);

            while (fileInputDelimited.nextRecord()) {
                if (!continueRun()) {
                    break;
                }
                currentRow++;
                int columsCount = fileInputDelimited.getColumnsCountOfCurrentRow();
                String[] rowValues = new String[columsCount];
                for (int i = 0; i < columsCount; i++) {
                    rowValues[i] = fileInputDelimited.get(i);
                }
                returnCode.setOk(returnCode.isOk()
                        && handleByARow(rowValues, currentRow, analysisElementList, columnElementList, indicToRowMap).isOk());
            }
            fileInputDelimited.close();
        } catch (IOException e) {
            log.error(e, e);
        }
        return returnCode;
    }

    /**
     * DOC zshen Comment method "createFileInputDelimited".
     * 
     * @return
     * @throws IOException
     */
    protected FileInputDelimited createFileInputDelimited() throws IOException {
        return AnalysisExecutorHelper.createFileInputDelimited(delimitedFileconnection);
    }

    /**
     * get the final result from duplicate indicator and set it into indicatorToRowMap Added yyin 20120608 TDQ-3589.
     * 
     * @param indicator
     * @param indicToRowMap
     */
    private void addResultToIndicatorToRowMap(Indicator indicator, EMap<Indicator, AnalyzedDataSet> indicToRowMap) {
        Map<Object, List<Object>> dupMap = ((DuplicateCountIndicator) indicator).getDuplicateMap();
        Set<Object> duplicateValues = ((DuplicateCountIndicator) indicator).getDuplicateValues();
        Iterator<Object> iterator = duplicateValues.iterator();
        int maxNumberRows = analysis.getParameters().getMaxNumberRows();

        while (iterator.hasNext()) {
            Object key = iterator.next();

            List<Object> valueList = dupMap.get(key);
            if (valueList == null) {
                continue;
            }
            List<Object[]> valueObjectList = initDataSet(indicator, indicToRowMap, key);
            // MOD zshen add another loop to insert all of columnValue on the row into indicator.
            int NumberOfRecord = valueObjectList.size();

            if (NumberOfRecord < maxNumberRows) {
                valueObjectList.add(valueList.toArray());
            } else {
                break;
            }
        }
    }

    private ReturnCode useCsvReader(File file, List<ModelElement> analysisElementList, List<MetadataColumn> columnElementList,
            EMap<Indicator, AnalyzedDataSet> indicToRowMap) {
        ReturnCode returnCode = new ReturnCode(true);
        int limitValue = getCsvReaderLimit();
        int headValue = JavaSqlFactory.getHeadValue(delimitedFileconnection);
        CSVReader csvReader = null;
        try {
            csvReader = FileUtils.createCsvReader(file, delimitedFileconnection);

            FileUtils.initializeCsvReader(delimitedFileconnection, csvReader);

            for (int i = 0; i < headValue && csvReader.readNext(); i++) {
                // do nothing, just ignore the header part
            }
            String[] rowValues = null;
            long currentRecord = 0;
            while (csvReader.readNext()) {
                currentRecord++;
                if (!continueRun() || limitValue != -1 && currentRecord > limitValue) {
                    break;
                }
                rowValues = csvReader.getValues();
                returnCode.setOk(returnCode.isOk()
                        && handleByARow(rowValues, currentRecord, analysisElementList, columnElementList, indicToRowMap).isOk());
            }
        } catch (IOException e) {
            log.error(e, e);
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

    /**
     * DOC zshen Comment method "getCsvReaderLimit".
     * 
     * @return
     */
    protected int getCsvReaderLimit() {
        return JavaSqlFactory.getLimitValue(delimitedFileconnection);
    }

    @Override
    protected ReturnCode closeConnection() {
        return new ReturnCode(true);

    }

    public DelimitedFileConnection getDelimitedFileconnection() {
        return this.delimitedFileconnection;
    }

    public void setDelimitedFileconnection(DelimitedFileConnection delimitedFileconnection) {
        this.delimitedFileconnection = delimitedFileconnection;
    }

    @Override
    protected ReturnCode checkConnection() {
        if (delimitedFileconnection == null) {
            return new ReturnCode(Messages.getString("Evaluator.openNullConnection"), false); //$NON-NLS-1$
        }
        return new ReturnCode(true);
    }

    private ReturnCode handleByARow(String[] rowValues, long currentRow, List<ModelElement> analysisElementList,
            List<MetadataColumn> columnElementList, EMap<Indicator, AnalyzedDataSet> indicToRowMap) {
        ReturnCode returnCode = new ReturnCode(true);
        Object object = null;
        int maxNumberRows = analysis.getParameters().getMaxNumberRows();
        int recordIncrement = 0;
        element: for (int i = 0; i < analysisElementList.size(); i++) {
            MetadataColumn mColumn = (MetadataColumn) analysisElementList.get(i);
            Integer position = ColumnHelper.getColumnIndex(mColumn);
            // warning with a file of badly form
            if (position == null || position >= rowValues.length) {
                log.warn(Messages.getString("DelimitedFileIndicatorEvaluator.incorrectData", //$NON-NLS-1$
                        mColumn.getLabel(), currentRow, delimitedFileconnection.getFilePath()));
                returnCode.setOk(false);
                continue;
            }

            object = TalendTypeConvert.convertToObject(mColumn.getTalendType(), rowValues[position], mColumn.getPattern());
            List<Indicator> indicators = getIndicators(mColumn.getLabel());
            for (Indicator indicator : indicators) {
                if (!continueRun()) {
                    break element;
                }
                // bug 19036,to irregularly data,still compute for RowCountIndicator
                if (object == null && !(indicator instanceof RowCountIndicator)) {
                    continue element;
                }
                // Added yyin 20120608 TDQ-3589
                if (indicator instanceof DuplicateCountIndicator) {
                    ((DuplicateCountIndicator) indicator).handle(object, rowValues);
                } else { // ~
                    indicator.handle(object);
                }
                AnalyzedDataSet analyzedDataSet = indicToRowMap.get(indicator);
                if (analyzedDataSet == null) {
                    analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
                    indicToRowMap.put(indicator, analyzedDataSet);
                    analyzedDataSet.setDataCount(maxNumberRows);
                    analyzedDataSet.setRecordSize(0);
                }
                // TDQ-9413: fix the drill down for file connection get no values
                // see IndicatorEvaluator line 166, the logic is almost the same
                if (analysis.getParameters().isStoreData()) {
                    if (indicator.mustStoreRow()) {
                        List<Object[]> valueObjectList = initDataSet(indicator, indicToRowMap, object);
                        recordIncrement = valueObjectList.size();

                        List<Object> inputRowList = new ArrayList<Object>();
                        for (int j = 0; j < rowValues.length; j++) {
                            Object newobject = rowValues[j];

                            if (indicator.isUsedMapDBMode()) {
                                inputRowList.add(newobject == null ? PluginConstant.NULL_STRING : newobject);
                                continue;
                            } else {
                                if (recordIncrement < maxNumberRows) {
                                    if (recordIncrement < valueObjectList.size()) {
                                        valueObjectList.get(recordIncrement)[j] = newobject;
                                    } else {
                                        Object[] valueObject = new Object[rowValues.length];
                                        valueObject[j] = newobject;
                                        valueObjectList.add(valueObject);
                                    }
                                } else {
                                    break;
                                }
                            }
                        }

                        if (indicator.isUsedMapDBMode()) {
                            MapDBUtils.handleDrillDownData(object, inputRowList, indicator);
                        }
                    } else if (indicator instanceof UniqueCountIndicator
                            && analysis.getResults().getIndicToRowMap().get(indicator).getData() != null) {
                        List<Object[]> removeValueObjectList = analysis.getResults().getIndicToRowMap().get(indicator).getData();
                        if (columnElementList.size() == 0) {
                            continue;
                        }
                        int offsetting = columnElementList.indexOf(indicator.getAnalyzedElement());
                        for (Object[] dataObject : removeValueObjectList) {
                            // Added yyin 20120611 TDQ5279
                            if (object instanceof Integer) {
                                if (object.equals(Integer.parseInt((String) dataObject[offsetting]))) {
                                    removeValueObjectList.remove(dataObject);
                                    break;
                                }
                            }// ~
                            if (dataObject[offsetting].equals(object)) {
                                removeValueObjectList.remove(dataObject);
                                break;
                            }
                        }
                    }
                }
            }

        }
        return returnCode;
    }

}
