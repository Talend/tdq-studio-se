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
package org.talend.dataquality.record.linkage.ui.composite.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.record.linkage.ui.composite.table.SortState;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchRuleAnlaysisUtilsTest extends TestCase {

    private SortState sortState = null;

    private List<Object[]> sortData = null;

    private List<ModelElement> columns = null;

    private TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();

    private TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Override
    protected void setUp() throws Exception {
        sortState = new SortState(0);
        sortState.setSelectedColumnIndex(0);
        sortState.setSelectedColumnName("column_0");//$NON-NLS-1$ 

        sortData = new ArrayList<Object[]>();
        columns = new ArrayList<ModelElement>();

        tdColumn.setSqlDataType(sqlDataType);
        columns.add(tdColumn);
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @Override
    protected void tearDown() throws Exception {
        sortData = null;
        columns = null;
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_int() {
        sortState.getNextSortDirection();// ASC
        Integer[] intArray = { new Integer(3) };
        Integer[] intArray2 = { new Integer(2) };
        Integer[] intArray4 = { new Integer(4) };
        Integer[] intArray3 = { new Integer(1) };
        sortData.add(intArray);
        sortData.add(intArray2);
        sortData.add(intArray4);
        sortData.add(intArray3);

        sqlDataType.setName("INT");
        sqlDataType.setJavaDataType(Types.INTEGER);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertTrue(((Integer) sortResult.get(0)[0]).compareTo(1) == 0);
        Assert.assertTrue(((Integer) sortResult.get(1)[0]).compareTo(2) == 0);
        Assert.assertTrue(((Integer) sortResult.get(2)[0]).compareTo(3) == 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Integer) sortResult.get(0)[0]).intValue(), 4);
        Assert.assertEquals(((Integer) sortResult.get(1)[0]).intValue(), 3);
        Assert.assertEquals(((Integer) sortResult.get(2)[0]).intValue(), 2);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_double() {
        sortState.getNextSortDirection();// ASC
        Double[] array = { new Double(1.3) };
        Double[] array2 = { new Double(2.5) };
        Double[] array4 = { new Double(4.7) };
        Double[] array3 = { new Double(1.6) };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("DOUBLE");
        sqlDataType.setJavaDataType(Types.DOUBLE);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertTrue(((Double) sortResult.get(0)[0]).compareTo(1.3) == 0);
        Assert.assertTrue(((Double) sortResult.get(1)[0]).compareTo(1.6) == 0);
        Assert.assertTrue(((Double) sortResult.get(2)[0]).compareTo(2.5) == 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Double) sortResult.get(0)[0]).doubleValue(), 4.7);
        Assert.assertEquals(((Double) sortResult.get(1)[0]).doubleValue(), 2.5);
        Assert.assertEquals(((Double) sortResult.get(2)[0]).doubleValue(), 1.6);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_DATE() {
        sortState.getNextSortDirection();// ASC
        Date[] array = { new Date(2000, 1, 1) };
        Date[] array2 = { new Date(2005, 1, 1) };
        Date[] array4 = { new Date(2003, 1, 1) };
        Date[] array3 = { new Date(2004, 1, 1) };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("DATE");
        sqlDataType.setJavaDataType(Types.DATE);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((Date) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((Date) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((Date) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Date) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((Date) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((Date) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_TIMESTAMP() {
        sortState.getNextSortDirection();// ASC
        Timestamp[] array = { new Timestamp(2000) };
        Timestamp[] array2 = { new Timestamp(6000) };
        Timestamp[] array4 = { new Timestamp(3000) };
        Timestamp[] array3 = { new Timestamp(5000) };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("TIMESTAMP");
        sqlDataType.setJavaDataType(Types.TIMESTAMP);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((Timestamp) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((Timestamp) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((Timestamp) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Timestamp) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((Timestamp) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((Timestamp) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_long() {
        sortState.getNextSortDirection();// ASC
        Long[] array = { new Long(2000) };
        Long[] array2 = { new Long(6000) };
        Long[] array4 = { new Long(3000) };
        Long[] array3 = { new Long(5000) };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("BIGINT");
        sqlDataType.setJavaDataType(Types.BIGINT);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((Long) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((Long) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((Long) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Long) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((Long) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((Long) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_BigDecimal() {
        sortState.getNextSortDirection();// ASC
        BigDecimal[] array = { new BigDecimal("20") };
        BigDecimal[] array2 = { new BigDecimal("60") };
        BigDecimal[] array4 = { new BigDecimal("30") };
        BigDecimal[] array3 = { new BigDecimal("50") };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("BIGINT");
        sqlDataType.setJavaDataType(Types.BIGINT);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((BigDecimal) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((BigDecimal) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((BigDecimal) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((BigDecimal) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((BigDecimal) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((BigDecimal) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_BOOLEAN() {
        sortState.getNextSortDirection();// ASC
        Boolean[] array = { Boolean.TRUE };
        Boolean[] array2 = { Boolean.FALSE };
        Boolean[] array4 = { Boolean.TRUE };
        Boolean[] array3 = { Boolean.FALSE };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("BOOLEAN");
        sqlDataType.setJavaDataType(Types.BOOLEAN);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((Boolean) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((Boolean) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((Boolean) sortResult.get(2)[0]).compareTo(array4[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((Boolean) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((Boolean) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((Boolean) sortResult.get(2)[0]).compareTo(array3[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_String() {
        sortState.getNextSortDirection();// ASC
        String[] array = { "abbbbb" };
        String[] array2 = { "zzzzz" };
        String[] array4 = { "hhhhhh" };
        String[] array3 = { "kkkkk" };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("String");
        sqlDataType.setJavaDataType(Types.VARCHAR);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_GroupSize() {
        // when the sort column is the group size, should compare as int
        sortState.getNextSortDirection();// ASC

        String[] array = { "1" };
        String[] array2 = { "5" };
        String[] array4 = { "2" };
        String[] array3 = { "3" };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("String");
        sqlDataType.setJavaDataType(Types.VARCHAR);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils#sortDataByColumn(org.talend.dataquality.record.linkage.ui.composite.table.SortState, java.util.List, java.util.List)}
     * .
     */
    public void testSortDataByColumn_StringAsDouble() {
        // when the sort column is the group size, should compare as int
        sortState.getNextSortDirection();// ASC

        String[] array = { "1.33" };
        String[] array2 = { "5.22" };
        String[] array4 = { "2.77" };
        String[] array3 = { "3.66" };
        sortData.add(array);
        sortData.add(array2);
        sortData.add(array4);
        sortData.add(array3);

        sqlDataType.setName("String");
        sqlDataType.setJavaDataType(Types.REAL);

        List<Object[]> sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(4, sortResult.size());
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array4[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array3[0]), 0);

        sortState.setSelectedColumnIndex(0);
        sortState.getNextSortDirection();// DESC
        sortResult = MatchRuleAnlaysisUtils.sortDataByColumn(sortState, sortData, columns);
        Assert.assertEquals(((String) sortResult.get(0)[0]).compareTo(array2[0]), 0);
        Assert.assertEquals(((String) sortResult.get(1)[0]).compareTo(array3[0]), 0);
        Assert.assertEquals(((String) sortResult.get(2)[0]).compareTo(array4[0]), 0);
    }
}
