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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.dataquality.indicators.AverageLengthIndicator;
import org.talend.dataquality.indicators.BlankCountIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.MaxLengthIndicator;
import org.talend.dataquality.indicators.MeanIndicator;
import org.talend.dataquality.indicators.MedianIndicator;
import org.talend.dataquality.indicators.MinLengthIndicator;
import org.talend.dataquality.indicators.ModeIndicator;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.SumIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.impl.SumIndicatorImpl;
import org.talend.dataquality.indicators.util.IndicatorsSwitch;
import org.talend.model.emf.CwmResource;
import org.talend.utils.properties.PropertiesLoader;
import org.talend.utils.properties.TypedProperties;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.time.TimeTracer;

import orgomg.cwm.foundation.datatypes.DatatypesFactory;
import orgomg.cwm.foundation.datatypes.QueryExpression;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class IndicatorEvaluationMain {

    protected static Logger log = Logger.getLogger(IndicatorEvaluationMain.class);

    private static final char SEP = ',';

    private static final IndicatorsSwitch<SumIndicator> MY_SWITCH = new IndicatorsSwitch<SumIndicator>() {

        @Override
        public SumIndicator caseSumIndicator(SumIndicator object) {
            return object;
        }
    };

    private static final char DOT = '.';

    private static Evaluator<String> evaluator = new IndicatorEvaluator(null);

    private IndicatorEvaluationMain() {
    }

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {

        TypedProperties connectionParams = PropertiesLoader.getProperties(IndicatorEvaluator.class, "db.properties");
        String driverClassName = connectionParams.getProperty("driver");
        String dbUrl = connectionParams.getProperty("url");
        try {
            TimeTracer tt = new TimeTracer("Indicator evaluation", null);
            tt.start();
            // create connection
            Connection connection = ConnectionUtils.createConnection(dbUrl, driverClassName, connectionParams);

            String database = "test";
            String tableName = "my_test";

            // --- columns to analyze
            String[] columnsArray = new String[] { "my_int" // 0
                    , "my_double" // 1
                    , "my_text" // 2
                    , "my_date" // 4
                    , "my_string" // 3
                    , "my_int_null" // 5
            };

            List<String> columns = Arrays.asList(columnsArray);

            // store in file
            File file = new File("out/columnTest_0.1.ana");
            EMFUtil util = new EMFUtil();
            Resource resource = util.getResourceSet().createResource(URI.createFileURI(file.getAbsolutePath()));
            rContents = resource.getContents();

            evaluator.setConnection(connection);

            // --- create indicators
            RowCountIndicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
            NullCountIndicator nullCountIndicator = IndicatorsFactory.eINSTANCE.createNullCountIndicator();
            DistinctCountIndicator distinctCountIndicator = IndicatorsFactory.eINSTANCE.createDistinctCountIndicator();
            DistinctCountIndicator distinctCountIndicator2 = IndicatorsFactory.eINSTANCE.createDistinctCountIndicator();
            UniqueCountIndicator uniqueCountIndicator = IndicatorsFactory.eINSTANCE.createUniqueCountIndicator();
            DuplicateCountIndicator duplicateCountIndicator = IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator();

            BlankCountIndicator blankCountIndicator = IndicatorsFactory.eINSTANCE.createBlankCountIndicator();

            MinLengthIndicator minLengthIndicator = IndicatorsFactory.eINSTANCE.createMinLengthIndicator();
            MaxLengthIndicator maxLengthIndicator = IndicatorsFactory.eINSTANCE.createMaxLengthIndicator();
            AverageLengthIndicator averageLengthIndicator = IndicatorsFactory.eINSTANCE.createAverageLengthIndicator();
            AverageLengthIndicator averageLengthIndicator2 = IndicatorsFactory.eINSTANCE.createAverageLengthIndicator();

            ModeIndicator modeIndicator = IndicatorsFactory.eINSTANCE.createModeIndicator();
            FrequencyIndicator textFrequencyIndicator = IndicatorsFactory.eINSTANCE.createFrequencyIndicator();
            // store in freq indic
            // textFrequencyIndicator.setDistinctCountIndicator(distinctCountIndicator);
            // textFrequencyIndicator.setDistinctCountIndicator(distinctCountIndicator2);
            // textFrequencyIndicator.setUniqueCountIndicator(uniqueCountIndicator);
            // textFrequencyIndicator.setDuplicateCountIndicator(duplicateCountIndicator);
            // textFrequencyIndicator.setModeIndicator(modeIndicator);

            MeanIndicator doubleMeanIndicator = IndicatorsFactory.eINSTANCE.createMeanIndicator();
            MeanIndicator integerMeanIndicator = IndicatorsFactory.eINSTANCE.createMeanIndicator();
            MedianIndicator medianIndicator = IndicatorsFactory.eINSTANCE.createMedianIndicator();

            SumIndicator integerSumIndicator = IndicatorsFactory.eINSTANCE.createSumIndicator();

            addIndicator(columnsArray[0], medianIndicator);
            addIndicator(columnsArray[1], doubleMeanIndicator);
            addIndicator(columnsArray[2], blankCountIndicator);
            addIndicator(columnsArray[5], nullCountIndicator);
            // addIndicator(columnsArray[2], textFrequencyIndicator);
            // addIndicator(columnsArray[2], distinctCountIndicator); // probably not useful?
            // addIndicator(columnsArray[2], uniqueCountIndicator); // probably not useful?
            // addIndicator(columnsArray[2], duplicateCountIndicator); // probably not useful?
            // addIndicator(columnsArray[2], modeIndicator); // probably not useful?
            addIndicator(columnsArray[3], rowCountIndicator);
            addIndicator(columnsArray[5], integerSumIndicator);
            addIndicator(columnsArray[5], integerMeanIndicator);
            addIndicator(columnsArray[2], averageLengthIndicator);
            addIndicator(columnsArray[3], averageLengthIndicator2);
            addIndicator(columnsArray[3], minLengthIndicator);
            addIndicator(columnsArray[3], maxLengthIndicator);

            // build query on columns
            // TODO scorreia add filter somewhere here...
            String selectCols = sqlSelectColumns(database, tableName, columns);

            // --- create a description of the column set
            QueryExpression queryExpression = DatatypesFactory.eINSTANCE.createQueryExpression();
            queryExpression.setBody(selectCols);
            queryExpression.setLanguage("SQL"); // TODO scorreia externalize this as a constant
            tt.start("compute");
            evaluator.setFetchSize(10000);
            evaluator.evaluateIndicators(selectCols, true);
            tt.end("compute");

            // Print indicators the median
            System.out.println("Median=" + medianIndicator.getMedian());
            System.out.println("# Unique values= " + textFrequencyIndicator.getUniqueValueCount());
            System.out.println("# Distinct values= " + textFrequencyIndicator.getDistinctValueCount());

            for (String col : columns) {
                printIndicators(evaluator.getIndicators(col));
            }
            tt.start("save");
            util.save();
            tt.end("saved in " + file.getAbsolutePath());
            tt.end();

            CwmResource cwmR = (CwmResource) resource;
            String id = cwmR.getID(medianIndicator);
            System.out.println("ecore util.getId= " + EcoreUtil.getID(medianIndicator));
            System.out.println("uuId= " + id);
            // test reload this file
            // LoadSerialData.main(args);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        }
    }

    private static EList<EObject> rContents;

    private static void addIndicator(String column, Indicator indicator) {
        evaluator.storeIndicator(column, indicator);
        rContents.add(indicator);
    }

    /**
     * DOC scorreia Comment method "printIndicators".
     * 
     * @param indicators
     */
    private static void printIndicators(List<Indicator> indicators) {
        for (Indicator indicator : indicators) {
            System.out.println(indicator);
            Object intIndic = MY_SWITCH.doSwitch(indicator);
            if (intIndic != null) {
                System.out.println(((SumIndicatorImpl) intIndic).getSumStr());
            }
        }
    }

    /**
     * DOC scorreia Comment method "sqlSelectColumns".
     * 
     * @param database
     * @param tableName
     * @param columns
     * @return
     */
    private static String sqlSelectColumns(String database, String tableName, List<String> columns) {
        StringBuffer buf = new StringBuffer();
        buf.append("select ");
        final int size = columns.size();
        int i = 0;
        for (String col : columns) {
            i++;
            buf.append(col);
            if (i != size) {
                buf.append(SEP);
            } else {
                buf.append(" from ");
            }
        }
        buf.append(database);
        buf.append(DOT);
        buf.append(tableName);
        return buf.toString();
    }

    static String sqlDistinctValuesCountStatement(String databaseName, String tableName, String columnName) {
        return "select count(distinct " + columnName + ") from " + databaseName + DOT + tableName;
    }

}
