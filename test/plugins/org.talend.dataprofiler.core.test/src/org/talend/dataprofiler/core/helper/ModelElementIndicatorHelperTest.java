// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.helper;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataprofiler.core.model.impl.ColumnIndicatorImpl;
import org.talend.dataprofiler.core.model.impl.DelimitedFileIndicatorImpl;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DQRepositoryNode;


/**
 * test for class ModelElementIndicatorHelper
 */
public class ModelElementIndicatorHelperTest {


    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator)}.
     * 
     * case1 full name case
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorCase1() {
        // short column name case
        ColumnIndicatorImpl colIndicator = createColumnIndicator("testColName", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator);
        Assert
                .assertEquals("The modelElementDisplayName should be testColName (String)", "testColName (String)",
                        modelElementDisplayName);
        // long column name case
        colIndicator = createColumnIndicator("testColNam_columnAnotherPart", "String");
        modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam_columnAnotherPart (String)",
                        "testColNam_columnAnotherPart (String)",
                        modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator)}.
     * 
     * case8 file connection full name case
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorCase7() {
        // short column name case
        DelimitedFileIndicatorImpl dfColIndicator = createDelimitedFileIndicator("testColName", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(dfColIndicator);
        Assert
                .assertEquals("The modelElementDisplayName should be testColName (String)", "testColName (String)",
                        modelElementDisplayName);
        // ling column name case
        dfColIndicator = createDelimitedFileIndicator("testColNam_columnAnotherPart", "String");
        modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(dfColIndicator);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam_columnAnotherPart (String)",
                        "testColNam_columnAnotherPart (String)",
                        modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case 2 column name more than 21
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase2() {
        ColumnIndicatorImpl colIndicator = createColumnIndicator("testColNam_columnAnotherPart", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator, false);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam...therPart (String)",
                        "testColNam...therPart (String)",
                        modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case3 column name equals 21
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase3() {
        ColumnIndicatorImpl colIndicator = createColumnIndicator("testColNam_columnAnot", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator, false);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam_columnAnot (String)",
                        "testColNam_columnAnot (String)", modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case4 column name less than 21
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase4() {
        ColumnIndicatorImpl colIndicator = createColumnIndicator("testColNam_columnAno", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator, false);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam_columnAno (String)",
                        "testColNam_columnAno (String)", modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case5 column name is empty
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase5() {
        ColumnIndicatorImpl colIndicator = createColumnIndicator("", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator, false);
        Assert.assertEquals("The modelElementDisplayName should be (String)", " (String)", modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case6 column name is null
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase6() {
        ColumnIndicatorImpl colIndicator = createColumnIndicator(null, "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(colIndicator, false);
        Assert.assertEquals("The modelElementDisplayName should be null", "null", modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case7 file connection more than 21 case
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase7() {
        DelimitedFileIndicatorImpl dfIndicator = createDelimitedFileIndicator("testColNam_columnAnotherPart", "String");
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(dfIndicator, false);
        Assert
                .assertEquals("The modelElementDisplayName should be testColNam...therPart (String)",
                        "testColNam...therPart (String)", modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getCutOutData(String, String, String)
     * 
     * case1 Null parameter case
     */
    @Test
    public void testGetCutOutData() {
        // 1. full column name is null
        String modelElementDisplayName = ModelElementIndicatorHelper.getCutOutData(null, "String", "fullColumnName");
        Assert
                .assertEquals("The modelElementDisplayName should be fullColumnName",
                        "fullColumnName", modelElementDisplayName);
        // 2. data Type name is null
        modelElementDisplayName =
                ModelElementIndicatorHelper
                        .getCutOutData("firstNameColumnfullColumnName", null, "firstNameColumnfullColumnName (string)");
        Assert
                .assertEquals("The modelElementDisplayName should be firstNameC...lumnName (unknown)",
                        "firstNameC...lumnName (unknown)", modelElementDisplayName);

        // 2. column name is null
        modelElementDisplayName =
                ModelElementIndicatorHelper.getCutOutData(null, "String", "firstNameColumnfullColumnName (string)");
        Assert.assertEquals("The modelElementDisplayName should be empty", StringUtils.EMPTY, modelElementDisplayName);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper#getModelElementDisplayName(org.talend.dataprofiler.core.model.ModelElementIndicator,Boolean
     * isFullName)}.
     * 
     * case8 NPE case
     */
    @Test
    public void testGetModelElementDisplayNameModelElementIndicatorBooleanCase8() {
        // 1 modelElementIndicator is null
        String modelElementDisplayName = ModelElementIndicatorHelper.getModelElementDisplayName(null, false);
        Assert
                .assertEquals("The modelElementDisplayName should be EMPTY", StringUtils.EMPTY,
                        modelElementDisplayName);
        // 2 metdataColumn is null
        DelimitedFileIndicatorImpl dfIndicator = createDelimitedFileIndicator("testColNam_columnAnotherPart", "String");
        ((MetadataColumnRepositoryObject) dfIndicator.getModelElementRepositoryNode().getObject()).setTdColumn(null);
        Assert.assertEquals("The modelElementDisplayName should be EMPTY", StringUtils.EMPTY, modelElementDisplayName);

        // 3 tdColumn is null
        ColumnIndicatorImpl colIndicator = createColumnIndicator(null, "String");
        ((MetadataColumnRepositoryObject) colIndicator.getModelElementRepositoryNode().getObject()).setTdColumn(null);
        Assert.assertEquals("The modelElementDisplayName should be EMPTY", StringUtils.EMPTY, modelElementDisplayName);
    }

    private ColumnIndicatorImpl createColumnIndicator(String colName, String typeName) {
        DQRepositoryNode dqNode = new DQRepositoryNode(null, null, null, null);
        TdColumn createTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdSqlDataType createTdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        createTdSqlDataType.setName(typeName);
        createTdColumn.setSqlDataType(createTdSqlDataType);
        createTdColumn.setName(colName);
        MetadataColumnRepositoryObject metadataColumnRepObject =
                new MetadataColumnRepositoryObject(null, createTdColumn);
        DBColumnRepNode dbColumnRepNode = new DBColumnRepNode(metadataColumnRepObject, dqNode, null, null);
        ColumnIndicatorImpl colIndicator = new ColumnIndicatorImpl(dbColumnRepNode);
        return colIndicator;
    }

    private DelimitedFileIndicatorImpl createDelimitedFileIndicator(String colName, String typeName) {
        DQRepositoryNode dqNode = new DQRepositoryNode(null, null, null, null);
        TdColumn createTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdSqlDataType createTdSqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        createTdSqlDataType.setName(typeName);
        createTdColumn.setSqlDataType(createTdSqlDataType);
        createTdColumn.setName(colName);
        createTdColumn.setTalendType("id_String");
        MetadataColumnRepositoryObject metadataColumnRepObject =
                new MetadataColumnRepositoryObject(null, createTdColumn);
        DBColumnRepNode dbColumnRepNode = new DBColumnRepNode(metadataColumnRepObject, dqNode, null, null);
        DelimitedFileIndicatorImpl delimitedFileIndicator = new DelimitedFileIndicatorImpl(dbColumnRepNode);
        return delimitedFileIndicator;
    }



}
