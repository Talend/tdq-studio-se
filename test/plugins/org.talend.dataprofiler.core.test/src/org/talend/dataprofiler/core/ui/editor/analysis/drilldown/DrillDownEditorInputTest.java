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


/**
 * DOC yyin class global comment. Detailled comment
 */
// @PrepareForTest({ ConnectionUtils.class, ColumnHelper.class, DrillDownUtils.class })
// TODO need to replace mock mechanism with common junit
public class DrillDownEditorInputTest {

    // @Rule
    // public PowerMockRule powerMockRule = new PowerMockRule();
    //
    // private DrillDownEditorInput ddInput;
    //
    // Indicator currIndicator;
    //
    // /**
    // * DOC yyin Comment method "setUp".
    // *
    // * @throws java.lang.Exception
    // */
    // @Before
    // public void setUp() throws Exception {
    //
    // }
    //
    // /**
    // * DOC yyin Comment method "tearDown".
    // *
    // * @throws java.lang.Exception
    // */
    // @After
    // public void tearDown() throws Exception {
    // }
    //
    // /**
    // * Test method for
    // * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getDataSet()}. before the
    // * modify, when size 1 != size 2, exception will be throw. after modified, this status can be executed
    // successfully.
    // */
    // @Test
    // public void testGetDataSet() {
    // Analysis analysis = mock(Analysis.class);
    // currIndicator = mock(Indicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    // when(menuItemEntity.getLabel()).thenReturn("view rows");
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    // TdColumn analysisElement = mock(TdColumn.class);
    // when(currIndicator.getAnalyzedElement()).thenReturn(analysisElement);
    // DrillDownEditorInput spydd = spy(ddInput);
    // // size 1=2
    // List<String> colList = new ArrayList<String>();
    // colList.add("id");
    // colList.add("name");
    // doReturn(colList).when(spydd).filterAdaptColumnHeader();
    // // when(spydd.filterAdaptColumnHeader()).thenReturn(colList);
    //
    // // size 2=3 for each object
    // List<Object[]> newList = new ArrayList<Object[]>();
    // Object[] o1 = { "1", null, "name" };
    // Object[] o2 = { "2", null, "name2" };
    // newList.add(o1);
    // newList.add(o2);
    // doReturn(newList).when(spydd).filterAdaptDataList();
    // // when(spydd.filterAdaptDataList()).thenReturn(newList);
    // // XmlPackage pack = mock(XmlPackage.class);
    // // EClass ec = mock(EClass.class);
    // // when(analysisElement.eClass()).thenReturn(ec);
    // // when(ec.eContainer()).thenReturn(pack);
    // // when(ec.getClassifierID()).thenReturn(XmlPackage.TD_XML_ELEMENT_TYPE);
    // // TdXmlElementType xmlElement = mock(TdXmlElementType.class);
    // // List<TdXmlElementType> xlist = new ArrayList<TdXmlElementType>();
    // // xlist.add(xmlElement);
    // //
    // // PowerMockito.mockStatic(ConnectionUtils.class);
    // // when(ConnectionUtils.getXMLElements(xmlElement)).thenReturn(xlist);
    //
    // // Assert.assertNotNull(spydd.getDataSet());
    // }
    //
    // /**
    // * Test method for
    // * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnIndexArray()}.
    // * case1 "view values" file connection currentIndicator is uniqueIndicator
    // */
    // @Test
    // public void testGetColumnIndexArray1() {
    // Analysis analysis = mock(Analysis.class);
    // currIndicator = mock(UniqueCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // MetadataColumn analysisElement = ConnectionFactory.eINSTANCE.createMetadataColumn();
    //        analysisElement.setName("column2"); //$NON-NLS-1$
    // when(currIndicator.getAnalyzedElement()).thenReturn(analysisElement);
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // MetadataColumn column1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
    //        column1.setLabel("column1"); //$NON-NLS-1$
    // MetadataColumn column2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
    //        column2.setLabel("column2"); //$NON-NLS-1$
    //
    // MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
    // metadataTable.getColumns().add(column1);
    // metadataTable.getColumns().add(column2);
    // PowerMockito.mockStatic(ColumnHelper.class);
    // Mockito.when(ColumnHelper.getColumnOwnerAsMetadataTable(analysisElement)).thenReturn(metadataTable);
    // Integer[] columnIndexArray = spydd.getColumnIndexArray();
    //
    // Assert.assertEquals(1, columnIndexArray.length);
    // Assert.assertEquals(1, columnIndexArray[0].intValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnIndexArray()}.
    // * case2 "view values" database connection currentIndicator is uniqueIndicator
    // */
    // @Test
    // public void testGetColumnIndexArray2() {
    // Analysis analysis = mock(Analysis.class);
    // currIndicator = mock(UniqueCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // TdColumn analysisElement = RelationalFactory.eINSTANCE.createTdColumn();
    //        analysisElement.setName("column2"); //$NON-NLS-1$
    // when(currIndicator.getAnalyzedElement()).thenReturn(analysisElement);
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // TdColumn column1 = RelationalFactory.eINSTANCE.createTdColumn();
    //        column1.setName("column1"); //$NON-NLS-1$
    // TdColumn column2 = RelationalFactory.eINSTANCE.createTdColumn();
    //        column2.setName("column2"); //$NON-NLS-1$
    //
    // TdTable tdTable = RelationalFactory.eINSTANCE.createTdTable();
    // tdTable.getFeature().add(column1);
    // tdTable.getFeature().add(column2);
    // List<TdColumn> columnList = new ArrayList<TdColumn>();
    // columnList.add(column1);
    // columnList.add(column2);
    // PowerMockito.mockStatic(ColumnHelper.class);
    // Mockito.when(ColumnHelper.getColumnOwnerAsTdTable(analysisElement)).thenReturn(tdTable);
    // Mockito.when(ColumnHelper.getColumns(tdTable.getFeature())).thenReturn(columnList);
    // Integer[] columnIndexArray = spydd.getColumnIndexArray();
    //
    // Assert.assertEquals(1, columnIndexArray.length);
    // Assert.assertEquals(1, columnIndexArray[0].intValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnIndexArray()}.
    // * case3 "view rows" currentIndicator is not uniqueIndicator then reuturn null
    // */
    // @Test
    // public void testGetColumnIndexArray3() {
    // Analysis analysis = mock(Analysis.class);
    // currIndicator = mock(Indicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view rows"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Integer[] columnIndexArray = spydd.getColumnIndexArray();
    //
    // Assert.assertNull(columnIndexArray);
    // }
    //
    // /**
    // * Test method for
    // * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnIndexArray()}.
    // * case4 "view values" currentIndicator is duplicateIndicator reuturn null
    // */
    // @Test
    // public void testGetColumnIndexArray4() {
    // Analysis analysis = mock(Analysis.class);
    // currIndicator = mock(DuplicateCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Integer[] columnIndexArray = spydd.getColumnIndexArray();
    //
    // Assert.assertNull(columnIndexArray);
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case1 columnSet duplicate
    // */
    // @Test
    // public void testGetItemSize1() {
    // final Long distinctCount = 10l;
    // final Long uniqueCount = 5l;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN_SET);
    // analysis.setParameters(analysisParameters);
    // AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
    // analysis.setResults(analysisResult);
    // SimpleStatIndicator simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();
    // analysisResult.getIndicators().add(simpleStatIndicator);
    // simpleStatIndicator.setDistinctCount(distinctCount);
    // simpleStatIndicator.setUniqueCount(uniqueCount);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = mock(DuplicateCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(distinctCount.longValue() - uniqueCount.longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case2 columnSet DistinctCount
    // */
    // @Test
    // public void testGetItemSize2() {
    // final Long distinctCount = 10l;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN_SET);
    // analysis.setParameters(analysisParameters);
    // AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
    // analysis.setResults(analysisResult);
    // SimpleStatIndicator simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();
    // analysisResult.getIndicators().add(simpleStatIndicator);
    // simpleStatIndicator.setDistinctCount(distinctCount);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = mock(DistinctCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(distinctCount.longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case3 columnSet UniqueCountIndicator
    // */
    // @Test
    // public void testGetItemSize3() {
    // final Long uniqueCount = 5l;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN_SET);
    // analysis.setParameters(analysisParameters);
    // AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
    // analysis.setResults(analysisResult);
    // SimpleStatIndicator simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();
    // analysisResult.getIndicators().add(simpleStatIndicator);
    // simpleStatIndicator.setUniqueCount(uniqueCount);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = mock(UniqueCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(uniqueCount.longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case4 columnSet RowCountIndicator
    // */
    // @Test
    // public void testGetItemSize4() {
    // final Long rowCount = 20l;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN_SET);
    // analysis.setParameters(analysisParameters);
    // AnalysisResult analysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
    // analysis.setResults(analysisResult);
    // SimpleStatIndicator simpleStatIndicator = ColumnsetFactory.eINSTANCE.createSimpleStatIndicator();
    // analysisResult.getIndicators().add(simpleStatIndicator);
    // simpleStatIndicator.setCount(rowCount);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = mock(RowCountIndicator.class);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(rowCount.longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case5 column RowCountIndicator
    // */
    // @Test
    // public void testGetItemSize5() {
    // final Long rowCount = 20l;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN);
    // analysis.setParameters(analysisParameters);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    // Mockito.when(abstractDB.size()).thenReturn(20);
    //
    // currIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
    // currIndicator.setCount(rowCount);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(rowCount.longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for
    // * {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getItemSize(org.talend.dataquality.indicators.mapdb.AbstractDB)}
    // * . case6 view rows
    // */
    // @Test
    // public void testGetItemSize6() {
    // final Integer size = 1000;
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN);
    // analysis.setParameters(analysisParameters);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    // Mockito.when(abstractDB.size()).thenReturn(size);
    // currIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
    // currIndicator.setCount(1000l);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view rows"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    //
    // Long itemSize = spydd.getItemSize(abstractDB);
    //
    // Assert.assertEquals(Long.valueOf(size).longValue(), itemSize.longValue());
    // }
    //
    // /**
    // * Test method for {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getMapDB()}
    // * . case1 view rows case
    // */
    // @Test
    // public void testGetMapDBCase1() {
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN);
    // analysis.setParameters(analysisParameters);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
    // currIndicator.setCount(1000l);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view rows"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // PowerMockito.mockStatic(DrillDownUtils.class);
    // Mockito.when(DrillDownUtils.getMapDB(dataEntity, analysis, menuItemEntity)).thenReturn(abstractDB);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    // AbstractDB<Object> mapDB = spydd.getMapDB();
    //
    // Assert.assertEquals(abstractDB, mapDB);
    //
    // }
    //
    // /**
    // * Test method for {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getMapDB()}
    // * . case2 view values case
    // */
    // @Test
    // public void testGetMapDBCase2() {
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN);
    // analysis.setParameters(analysisParameters);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
    // currIndicator.setCount(1000l);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // PowerMockito.mockStatic(DrillDownUtils.class);
    // Mockito.when(DrillDownUtils.getMapDB(dataEntity, analysis, menuItemEntity)).thenReturn(abstractDB);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    // AbstractDB<Object> mapDB = spydd.getMapDB();
    //
    // Assert.assertEquals(abstractDB, mapDB);
    //
    // }
    //
    // /**
    // * Test method for {@link
    // org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getMapDB()}
    // * . case3 view invalid case
    // */
    // @Test
    // public void testGetMapDBCase3() {
    // Analysis analysis = AnalysisFactory.eINSTANCE.createAnalysis();
    // AnalysisParameters analysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
    // analysisParameters.setAnalysisType(AnalysisType.COLUMN);
    // analysis.setParameters(analysisParameters);
    //
    // AbstractDB<Object> abstractDB = Mockito.mock(AbstractDB.class);
    //
    // currIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
    // currIndicator.setCount(1000l);
    // MenuItemEntity menuItemEntity = mock(MenuItemEntity.class);
    //        when(menuItemEntity.getLabel()).thenReturn("view values"); //$NON-NLS-1$
    //
    // ChartDataEntity dataEntity = mock(ChartDataEntity.class);
    // when(dataEntity.getIndicator()).thenReturn(currIndicator);
    //
    // ddInput = new DrillDownEditorInput(analysis, dataEntity, menuItemEntity);
    //
    // PowerMockito.mockStatic(DrillDownUtils.class);
    // Mockito.when(DrillDownUtils.getMapDB(dataEntity, analysis, menuItemEntity)).thenReturn(abstractDB);
    //
    // DrillDownEditorInput spydd = spy(ddInput);
    // AbstractDB<Object> mapDB = spydd.getMapDB();
    //
    // Assert.assertEquals(abstractDB, mapDB);
    //
    // }
}
