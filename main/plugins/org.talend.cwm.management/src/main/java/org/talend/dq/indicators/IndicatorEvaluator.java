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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.analysis.impl.AnalyzedDataSetImpl;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.FormatFreqPieIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.MaxLengthIndicator;
import org.talend.dataquality.indicators.MinLengthIndicator;
import org.talend.dataquality.indicators.PatternFreqIndicator;
import org.talend.dataquality.indicators.PatternLowFreqIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.mapdb.MapDBUtils;
import org.talend.dataquality.indicators.mapdb.TalendFormatDate;
import org.talend.dataquality.indicators.mapdb.TalendFormatTime;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.UDIHelper;
import org.talend.utils.collections.MultiMapHelper;
import org.talend.utils.sql.ResultSetUtils;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * @author scorreia
 * 
 * Computes indicators on columns with java engine. It means that we call indicator.handle(object) method.
 */
public class IndicatorEvaluator extends Evaluator<String> {

    private static Logger log = Logger.getLogger(IndicatorEvaluator.class);

    public IndicatorEvaluator(Analysis analysis) {
        this.analysis = analysis;
    }

    @Override
    protected ReturnCode executeSqlQuery(String sqlStatement) throws SQLException {
        ReturnCode ok = new ReturnCode(true);
        // check analyzed columns
        Set<String> columns = getAnalyzedElements();
        // feature 0010630 zshen:Make order unify which columns and columnName in the sqlStatement.mssqlOdbc need do
        // this
        List<String> columnlist = sortColumnName(columns, sqlStatement);
        if (columnlist.isEmpty()) {
            ok.setReturnCode(Messages.getString("IndicatorEvaluator.DefineAnalyzedColumns"), false); //$NON-NLS-1$
            return ok;
        }
        // ADD xqliu 2010-07-27 bug 13826
        Map<String, String> columnlistMap = buildColumnListMap(columnlist);
        // ~ 13826
        // create query statement
        // feature 0010630 zshen: Tables are not found when using Excel with ODBC connection
        Statement statement = createStatement();
        // MOD xqliu 2009-02-09 bug 6237
        if (continueRun()) {
            if (log.isInfoEnabled()) {
                log.info("Executing query: " + sqlStatement); //$NON-NLS-1$
            }
            statement.execute(sqlStatement);
        }
        // get the results
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            String mess = Messages.getString("Evaluator.NoResultSet", sqlStatement); //$NON-NLS-1$
            log.warn(mess);
            ok.setReturnCode(mess, false);
            statement.close();
            return ok;
        }
        // MOD qiongli TDQ-7282,check invalid judi.if there are invalid judis,return false code and show message later.
        ok = getMessageForInvalidJUDIs();
        int columnCount = resultSet.getMetaData().getColumnCount();
        int maxNumberRows = analysis.getParameters().getMaxNumberRows();

        // MOD mzhao feature: 12919, add capability to dill down data on Java engine.
        AnalysisResult anaResult = analysis.getResults();
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = anaResult.getIndicToRowMap();
        indicToRowMap.clear();
        int recordIncrement = 0;
        // --- for each row
        int columnListSize = columnlist.size();
        label: while (resultSet.next()) {
            // --- for each column
            // feature 0010630 zshen: dislodge the Qualifiers from name of the column
            for (int i = 0; i < columnListSize; i++) {
                // MOD xqliu 2010-07-27 bug 13826
                String col = columnlist.get(i);
                List<Indicator> indicators = getIndicators(col);
                col = columnlistMap.get(col);

                // --- get content of column
                Object object = ResultSetUtils.getBigObject(resultSet, col);

                // MOD zshen, when the type of object is TIMESTAMP then need getTimestamp(col) to get correct value,or
                // the value only is the name of type and can't be match with TIMESTAMP.
                // FIXME this will slow down a lot the computation
                if (object != null && !(object instanceof String) && object.toString().indexOf("TIMESTAMP") > -1) { //$NON-NLS-1$
                    object = resultSet.getTimestamp(col);
                }

                // TDQ-11299: fix the ClassCastException: java.sql.Date cannot be cast to java.lang.String
                if (object instanceof Date) {
                    if (object instanceof Time) {
                        object = new TalendFormatTime((Time) object);
                    } else {
                        object = new TalendFormatDate((Date) object);
                    }
                }
                // TDQ-11299~

                // --- give row to handle to indicators
                for (Indicator indicator : indicators) {
                    // MOD xqliu 2009-02-09 bug 6237
                    if (!continueRun()) {
                        break label;
                    }
                    // Added yyin 20120608 TDQ-3589
                    if (indicator instanceof DuplicateCountIndicator) {
                        ((DuplicateCountIndicator) indicator).handle(object, resultSet, columnCount);
                    } else { // ~
                        indicator.handle(object);
                        // ~MOD mzhao feature: 12919
                    }

                    AnalyzedDataSet analyzedDataSet = indicToRowMap.get(indicator);
                    if (analyzedDataSet == null) {
                        analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
                        indicToRowMap.put(indicator, analyzedDataSet);
                        analyzedDataSet.setDataCount(maxNumberRows);
                        analyzedDataSet.setRecordSize(0);
                    }
                    // should store data for dirll down
                    if (analysis.getParameters().isStoreData()) {
                        // current indicator is need to store the data
                        if (indicator.mustStoreRow()) {

                            List<Object[]> valueObjectList = initDataSet(indicator, indicToRowMap, object);
                            // MOD zshen add another loop to insert all of columnValue on the row into indicator.
                            recordIncrement = valueObjectList.size();
                            // MOD klliu 2011-06-30 bug 22523 whichever is Table or View,that finds columns should ues
                            // columnset
                            ColumnSet doSwitch = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement()
                                    .eContainer());
                            List<TdColumn> columnList = ColumnSetHelper.getColumns(doSwitch);
                            List<Object> inputRowList = new ArrayList<Object>();
                            for (int j = 0; j < columnCount; j++) {
                                String newcol = columnList.get(j).getName();
                                Object newobject = ResultSetUtils.getBigObject(resultSet, newcol);
                                // TDQ-10833 Format Drill down Date data by TalendFormatTime with
                                // "HH:mm:ss:SSS",TalendFormatDate with "yyyy-MM-dd HH:mm:ss:SSS".So that it is
                                // same format as result page.
                                if (newobject instanceof Date) {
                                    if (newobject instanceof Time) {
                                        newobject = new TalendFormatTime((Time) newobject);
                                    } else {
                                        newobject = new TalendFormatDate((Date) newobject);
                                    }
                                }
                                if (indicator.isUsedMapDBMode()) {
                                    inputRowList.add(newobject == null ? PluginConstant.NULL_STRING : newobject);
                                    continue;
                                } else {
                                    if (recordIncrement < maxNumberRows) {// decide whether current record is more than
                                                                          // max
                                                                          // Number else don't need to record more than
                                                                          // data.
                                        if (recordIncrement < valueObjectList.size()) {// decide whether need to
                                                                                       // increase
                                                                                       // current array.
                                            valueObjectList.get(recordIncrement)[j] = newobject;

                                        } else {
                                            Object[] valueObject = new Object[columnCount];
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
                            // ~
                        } else if (indicator instanceof UniqueCountIndicator
                                && analysis.getResults().getIndicToRowMap().get(indicator).getData() != null) {
                            List<Object[]> removeValueObjectList = analysis.getResults().getIndicToRowMap().get(indicator)
                                    .getData();
                            // MOD klliu 2011-06-30 bug 22523 whichever is Table or View,that finds columns should ues
                            // columnset
                            ColumnSet doSwitch = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement()
                                    .eContainer());
                            List<TdColumn> columnElementList = ColumnSetHelper.getColumns(doSwitch);
                            int offsetting = columnElementList.indexOf(indicator.getAnalyzedElement());
                            for (Object[] dataObject : removeValueObjectList) {
                                if (dataObject[offsetting].equals(object)) {
                                    removeValueObjectList.remove(dataObject);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- release resultset
        resultSet.close();
        // --- release statement
        statement.close();
        // --- close connection
        getConnection().close();

        return ok;
    }

    /**
     * DOC xqliu Comment method "buildColumnListMap". bug 13826
     * 
     * @param columnlist
     * @return
     */
    private Map<String, String> buildColumnListMap(List<String> columnlist) {
        Map<String, String> result = new HashMap<String, String>();
        for (String col : columnlist) {
            result.put(col, col.substring(col.lastIndexOf('.') + 1));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected List<Object[]> initDataSet(Indicator indicator, EMap<Indicator, AnalyzedDataSet> indicToRowMap, Object object) {
        AnalyzedDataSet analyzedDataSet = indicToRowMap.get(indicator);
        List<Object[]> valueObjectList = null;
        if (analyzedDataSet == null) {
            analyzedDataSet = AnalysisFactory.eINSTANCE.createAnalyzedDataSet();
            indicToRowMap.put(indicator, analyzedDataSet);
            analyzedDataSet.setDataCount(analysis.getParameters().getMaxNumberRows());
            analyzedDataSet.setRecordSize(0);
        }

        if (indicator instanceof FrequencyIndicator || indicator instanceof MinLengthIndicator
                || indicator instanceof MaxLengthIndicator) {
            Map<Object, List<Object[]>> valueObjectListMap = analyzedDataSet.getFrequencyData();
            if (valueObjectListMap == null) {
                valueObjectListMap = new HashMap<Object, List<Object[]>>();
                analyzedDataSet.setFrequencyData(valueObjectListMap);
            }
            String key = null;
            if (object == null) {
                key = SpecialValueDisplay.NULL_FIELD;
            } else if (indicator instanceof MinLengthIndicator || indicator instanceof MaxLengthIndicator) {
                key = String.valueOf(object.toString().length());
            } else if (object.equals(PluginConstant.EMPTY_STRING)) {
                key = SpecialValueDisplay.EMPTY_FIELD;
            } else if (indicator instanceof PatternLowFreqIndicator) {
                key = ((PatternLowFreqIndicator) indicator).convertCharacters(object.toString());
            } else if (indicator instanceof PatternFreqIndicator) {
                key = ((PatternFreqIndicator) indicator).convertCharacters(object.toString());
            } else if (indicator instanceof FormatFreqPieIndicator) {
                // MOD qiongli 2011-8-26,feature TDQ-3253.
                key = ((FormatFreqPieIndicator) indicator).getCurrentKey();
            } else {
                key = object.toString();
            }

            valueObjectList = valueObjectListMap.get(key);
            if (valueObjectList == null) {
                valueObjectList = new ArrayList<Object[]>();
                valueObjectListMap.put(key, valueObjectList);
            }
        } else if (indicator.isInValidRow() || indicator.isValidRow()) {
            List<Object> patternData = analyzedDataSet.getPatternData();
            if (patternData == null) {
                patternData = new ArrayList<Object>();
                patternData.add(new ArrayList<Object[]>());// mapping with AnalyzedDataSetImpl.VALID_VALUE
                patternData.add(new ArrayList<Object[]>());// mapping with AnalyzedDataSetImpl.INVALID_VALUE
                analyzedDataSet.setPatternData(patternData);
            }
            Object listObject = indicator.isInValidRow() ? patternData.get(AnalyzedDataSetImpl.INVALID_VALUE) : patternData
                    .get(AnalyzedDataSetImpl.VALID_VALUE);
            if (listObject instanceof ArrayList<?>) {
                valueObjectList = (ArrayList<Object[]>) listObject;
            }
        } else {
            valueObjectList = analyzedDataSet.getData();
            if (valueObjectList == null) {
                valueObjectList = new ArrayList<Object[]>();
                analyzedDataSet.setData(valueObjectList);
            }

        }

        return valueObjectList;
    }

    /**
     * 
     * @author zshen
     * @param columns
     * @param sqlStatement
     * @return the same order List which columnName in the sqlStatement
     */
    public List<String> sortColumnName(Set<String> columns, String sqlStatement) {
        List<String> columnNameList = new ArrayList<String>();
        Map<Integer, String> offset = new HashMap<Integer, String>();
        for (String col : columns) {
            int offsetCol = col.lastIndexOf('.') + 1;
            String colName = col.substring(offsetCol);
            DbmsLanguage createDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(analysis, ExecutionLanguage.SQL);
            int location = sqlStatement.indexOf(createDbmsLanguage.quote(colName));
            offset.put(location, col);
        }

        Integer[] keyArray = offset.keySet().toArray(new Integer[offset.keySet().size()]);
        int temp = 0;
        for (int i = 0; i < columns.size(); i++) {
            for (int j = keyArray.length - 1; j > i; j--) {
                if (keyArray[j] < keyArray[j - 1]) {
                    temp = keyArray[j];
                    keyArray[j] = keyArray[j - 1];
                    keyArray[j - 1] = temp;
                }
            }
            columnNameList.add(offset.get(keyArray[i]));

        }
        return columnNameList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#storeIndicator(java.lang.Object,
     * org.talend.dataquality.indicators.Indicator)
     * 
     * MOD scorreia 2009-04-24 overrided to solve bug 7093
     */
    @Override
    public boolean storeIndicator(String elementToAnalyze, Indicator indicator) {
        boolean ok = true;
        final List<Indicator> indicatorLeaves = IndicatorHelper.getIndicatorLeaves(indicator);
        for (Indicator leaf : indicatorLeaves) {
            if (!this.allIndicators.contains(leaf)
                    && !MultiMapHelper.addUniqueObjectToListMap(elementToAnalyze, leaf, elementToIndicators)) {
                ok = false;
            }
        }
        this.allIndicators.addAll(indicatorLeaves);
        return ok;
    }

    /**
     * 
     * check each UDI if has realted java expression(class) for Java Engine,remove it from elementToIndicators(no need
     * to compute),then populate the message
     * 
     * @param analysis
     * @return
     */
    protected ReturnCode getMessageForInvalidJUDIs() {
        ReturnCode ret = new ReturnCode(Boolean.TRUE);
        Set<String> invalidUdiNames = new HashSet<String>();
        Set<String> columns = elementToIndicators.keySet();
        Iterator<String> colIt = columns.iterator();
        while (colIt.hasNext()) {
            String nextCol = colIt.next();
            List<Indicator> indicators = elementToIndicators.get(nextCol);
            List<Indicator> needRemovedInds = new ArrayList<Indicator>();
            for (Indicator ind : indicators) {
                if (ind instanceof UserDefIndicator && UDIHelper.isJavaUDI(ind)
                        && !UDIHelper.isJUDIValid(ind.getIndicatorDefinition())) {
                    invalidUdiNames.add(ind.getName());
                    needRemovedInds.add(ind);
                }
            }
            if (!needRemovedInds.isEmpty()) {
                indicators.removeAll(needRemovedInds);
                allIndicators.removeAll(needRemovedInds);
            }
        }

        if (!invalidUdiNames.isEmpty()) {
            String message = invalidUdiNames.toString();
            ret.setReturnCode(Messages.getString("IndicatorEvaluator.NoExpressionFound", message), false); //$NON-NLS-1$

        }
        return ret;
    }

}
