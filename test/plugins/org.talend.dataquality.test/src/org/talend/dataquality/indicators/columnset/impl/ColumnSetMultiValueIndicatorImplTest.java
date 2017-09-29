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
package org.talend.dataquality.indicators.columnset.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.talend.ResourceUtils;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.XmlFactory;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by xqliu on Apr 24, 2013 Detailled comment
 */
public class ColumnSetMultiValueIndicatorImplTest {

    private static final String COUNT_STAR = "COUNT(*)"; //$NON-NLS-1$

    private static final String COLUMN_NAME_11 = "ColumnName11"; //$NON-NLS-1$

    private static final String COLUMN_NAME_12 = "ColumnName12"; //$NON-NLS-1$

    private static final String COLUMN_NAME_2 = "ColumnName2"; //$NON-NLS-1$

    private static final String COLUMN_NAME_3 = "ColumnName3"; //$NON-NLS-1$

    private static final String COLUMN_NAME_4 = "ColumnName4"; //$NON-NLS-1$

    /**
     * Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.ColumnSetMultiValueIndicatorImpl#getColumnHeaders()}.
     */
    @Test
    public void testGetColumnHeaders() {
        ColumnSetMultiValueIndicatorImpl columnSetMultiValueIndicatorImpl = new ColumnSetMultiValueIndicatorImpl();
        // init an empty AnalyzedColumns List
        EList<ModelElement> analyzedColumns = columnSetMultiValueIndicatorImpl.getAnalyzedColumns();
        // if the AnalyzedColumns List is empty, the column headers will include only one column COUNT(*)
        EList<String> columnHeaders = columnSetMultiValueIndicatorImpl.getColumnHeaders();
        Assert.assertFalse(columnHeaders.isEmpty());
        Assert.assertTrue(columnHeaders.size() == 1);
        Assert.assertTrue(COUNT_STAR.equals(columnHeaders.get(0)));

        // add 5 columns into the AnalyzedColumns List
        analyzedColumns.add(createColumn11());
        analyzedColumns.add(createColumn12());
        analyzedColumns.add(createColumn2());
        analyzedColumns.add(createColumn3());
        analyzedColumns.add(createColumn4());
        // the column headers will contains 6 columns, and the order will be the same with AnalyzedColumns, the last
        // column will be COUNT(*)
        columnHeaders = columnSetMultiValueIndicatorImpl.getColumnHeaders();
        Assert.assertFalse(columnHeaders.isEmpty());
        Assert.assertTrue(columnHeaders.size() == 6);
        Assert.assertTrue(COLUMN_NAME_11.equals(columnHeaders.get(0)));
        Assert.assertTrue(COLUMN_NAME_12.equals(columnHeaders.get(1)));
        Assert.assertTrue(COLUMN_NAME_2.equals(columnHeaders.get(2)));
        Assert.assertTrue(COLUMN_NAME_3.equals(columnHeaders.get(3)));
        Assert.assertTrue(COLUMN_NAME_4.equals(columnHeaders.get(4)));
        Assert.assertTrue(COUNT_STAR.equals(columnHeaders.get(5)));
    }

    /**
     * create a TdXmlElementType, the DatamingType is Other.
     * 
     * @return
     */
    private ModelElement createColumn4() {
        TdXmlElementType tdXmlElementType = XmlFactory.eINSTANCE.createTdXmlElementType();
        tdXmlElementType.setName(COLUMN_NAME_4);
        tdXmlElementType.setContentType(DataminingType.OTHER.getLiteral());
        return tdXmlElementType;
    }

    /**
     * create a MetadataColumn, the DatamingType is Unstructured Text.
     * 
     * @return
     */
    private ModelElement createColumn3() {
        MetadataColumn mdColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        mdColumn.setName(COLUMN_NAME_3);
        EList<TaggedValue> taggedValue = mdColumn.getTaggedValue();
        TaggedValue tv = CoreFactory.eINSTANCE.createTaggedValue();
        tv.setTag(TaggedValueHelper.DATA_CONTENT_TYPE_TAGGED_VAL);
        tv.setValue(DataminingType.UNSTRUCTURED_TEXT.getLiteral());
        taggedValue.add(tv);
        return mdColumn;
    }

    /**
     * create a TdColumn, the DatamingType is Interval.
     * 
     * @return
     */
    private ModelElement createColumn2() {
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn.setName(COLUMN_NAME_2);
        tdColumn.setContentType(DataminingType.INTERVAL.getLiteral());
        TdSqlDataType tdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdSqlDataType.setJavaDataType(Types.DATE);
        tdColumn.setSqlDataType(tdSqlDataType);
        return tdColumn;
    }

    /**
     * create a TdColumn, the DatamingType is Nominal, the getSqlDataType is DATE.
     * 
     * @return
     */
    private ModelElement createColumn12() {
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn.setName(COLUMN_NAME_12);
        tdColumn.setContentType(DataminingType.NOMINAL.getLiteral());
        TdSqlDataType tdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdSqlDataType.setJavaDataType(Types.DATE);
        tdColumn.setSqlDataType(tdSqlDataType);
        return tdColumn;
    }

    /**
     * create a TdColumn, the DatamingType is Nominal, the getSqlDataType is NUMERIC.
     * 
     * @return
     */
    private ModelElement createColumn11() {
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        tdColumn.setName(COLUMN_NAME_11);
        tdColumn.setContentType(DataminingType.NOMINAL.getLiteral());
        TdSqlDataType tdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdSqlDataType.setJavaDataType(Types.NUMERIC);
        tdColumn.setSqlDataType(tdSqlDataType);
        return tdColumn;
    }

    /**
     * when do not use mapdb Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.ColumnSetMultiValueIndicatorImpl#handle(EList)}.
     */
    @Test
    public void testHandleElist_1() {

        ColumnSetMultiValueIndicatorImpl createColumnSetMultiValueIndicator = (ColumnSetMultiValueIndicatorImpl) ColumnsetFactory.eINSTANCE
                .createColumnSetMultiValueIndicator();
        createColumnSetMultiValueIndicator.setUsedMapDBMode(false);
        for (EList<Object> datas : initdatas()) {
            createColumnSetMultiValueIndicator.handle(datas);
        }
        boolean computeResult = createColumnSetMultiValueIndicator.finalizeComputation();
        Assert.assertEquals(true, computeResult);
        Assert.assertEquals(6l, createColumnSetMultiValueIndicator.getCount().longValue());
        Assert.assertEquals(3l, createColumnSetMultiValueIndicator.getDistinctCount().longValue());
        Assert.assertEquals(2l, createColumnSetMultiValueIndicator.getDuplicateCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getUniqueCount().longValue());
    }

    /**
     * when use mapdb Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.ColumnSetMultiValueIndicatorImpl#handle(EList)}.
     */
    @Test
    public void testHandleElist_2() {

        ColumnSetMultiValueIndicatorImpl createColumnSetMultiValueIndicator = (ColumnSetMultiValueIndicatorImpl) ColumnsetFactory.eINSTANCE
                .createColumnSetMultiValueIndicator();
        createColumnSetMultiValueIndicator.setUsedMapDBMode(true);
        ResourceUtils.createAnalysis(createColumnSetMultiValueIndicator);
        createColumnSetMultiValueIndicator.reset();

        for (EList<Object> datas : initdatas()) {
            createColumnSetMultiValueIndicator.handle(datas);
        }
        boolean computeResult = createColumnSetMultiValueIndicator.finalizeComputation();
        Assert.assertEquals(true, computeResult);
        Assert.assertEquals(6l, createColumnSetMultiValueIndicator.getCount().longValue());
        Assert.assertEquals(3l, createColumnSetMultiValueIndicator.getDistinctCount().longValue());
        Assert.assertEquals(2l, createColumnSetMultiValueIndicator.getDuplicateCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getUniqueCount().longValue());
    }

    /**
     * when do not use mapdb Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.ColumnSetMultiValueIndicatorImpl#handle(EList)}. when
     * setUsedMapDBMode
     */
    @Test
    public void testHandleElistIsEmpty_1() {

        ColumnSetMultiValueIndicatorImpl createColumnSetMultiValueIndicator = (ColumnSetMultiValueIndicatorImpl) ColumnsetFactory.eINSTANCE
                .createColumnSetMultiValueIndicator();
        createColumnSetMultiValueIndicator.setUsedMapDBMode(false);
        EList<Object> elist = new BasicEList<Object>();
        createColumnSetMultiValueIndicator.handle(elist);

        boolean computeResult = createColumnSetMultiValueIndicator.finalizeComputation();
        Assert.assertEquals(true, computeResult);
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getDistinctCount().longValue());
        Assert.assertEquals(0l, createColumnSetMultiValueIndicator.getDuplicateCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getUniqueCount().longValue());
    }

    /**
     * when use mapdb Test method for
     * {@link org.talend.dataquality.indicators.columnset.impl.ColumnSetMultiValueIndicatorImpl#handle(EList)}.
     */
    @Test
    public void testHandleElistIsEmpty_2() {

        ColumnSetMultiValueIndicatorImpl createColumnSetMultiValueIndicator = (ColumnSetMultiValueIndicatorImpl) ColumnsetFactory.eINSTANCE
                .createColumnSetMultiValueIndicator();
        createColumnSetMultiValueIndicator.setUsedMapDBMode(true);
        ResourceUtils.createAnalysis(createColumnSetMultiValueIndicator);
        createColumnSetMultiValueIndicator.reset();

        EList<Object> elist = new BasicEList<Object>();
        createColumnSetMultiValueIndicator.handle(elist);

        boolean computeResult = createColumnSetMultiValueIndicator.finalizeComputation();
        Assert.assertEquals(true, computeResult);
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getDistinctCount().longValue());
        Assert.assertEquals(0l, createColumnSetMultiValueIndicator.getDuplicateCount().longValue());
        Assert.assertEquals(1l, createColumnSetMultiValueIndicator.getUniqueCount().longValue());
    }

    /**
     * DOC talend Comment method "init".
     * 
     * @param datas
     */
    private List<EList<Object>> initdatas() {
        List<EList<Object>> datas = new ArrayList<EList<Object>>();
        EList<Object> elist = new BasicEList<Object>();
        elist.add("3"); //$NON-NLS-1$
        elist.add("test3"); //$NON-NLS-1$
        datas.add(elist);
        elist = new BasicEList<Object>();
        elist.add("2"); //$NON-NLS-1$
        elist.add("test2"); //$NON-NLS-1$
        datas.add(elist);
        elist = new BasicEList<Object>();
        elist.add("1"); //$NON-NLS-1$
        elist.add("test1"); //$NON-NLS-1$
        datas.add(elist);
        elist = new BasicEList<Object>();
        elist.add("3"); //$NON-NLS-1$
        elist.add("test3"); //$NON-NLS-1$
        datas.add(elist);
        elist = new BasicEList<Object>();
        elist.add("2"); //$NON-NLS-1$
        elist.add("test2"); //$NON-NLS-1$
        datas.add(elist);
        elist = new BasicEList<Object>();
        elist.add("3"); //$NON-NLS-1$
        elist.add("test3"); //$NON-NLS-1$
        datas.add(elist);

        return datas;

    }
}
