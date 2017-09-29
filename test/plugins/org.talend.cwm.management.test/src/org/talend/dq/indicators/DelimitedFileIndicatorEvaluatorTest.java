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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.indicators.Indicator;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
@PrepareForTest({ ColumnHelper.class, LanguageManager.class, JavaSqlFactory.class })
public class DelimitedFileIndicatorEvaluatorTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Ignore
    @Test
    public void testExecuteSqlQuery_delimetd() throws Exception {
        String empty = ""; //$NON-NLS-1$
        Analysis analysis = mock(Analysis.class);
        DelimitedFileIndicatorEvaluator delFileIndiEvaluator = new DelimitedFileIndicatorEvaluator(analysis);
        DelimitedFileIndicatorEvaluator spyEvaluator = Mockito.spy(delFileIndiEvaluator);
        stub(method(DelimitedFileIndicatorEvaluator.class, "handleByARow")); //$NON-NLS-1$
        stub(method(DelimitedFileIndicatorEvaluator.class, "addResultToIndicatorToRowMap", Indicator.class, EMap.class)); //$NON-NLS-1$
        AnalysisContext context = mock(AnalysisContext.class);
        when(analysis.getContext()).thenReturn(context);
        DelimitedFileConnection deliFileConn = mock(DelimitedFileConnection.class);
        when(context.getConnection()).thenReturn(deliFileConn);
        when(deliFileConn.isContextMode()).thenReturn(false);
        String path = "test.txt"; //$NON-NLS-1$

        PowerMockito.mockStatic(JavaSqlFactory.class);
        when(JavaSqlFactory.getURL(deliFileConn)).thenReturn(path);
        IPath iPath = mock(IPath.class);
        File file = new File(path);
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        String str = "id;Cocust(Tests);owner_id\n" + "1;yellow;3301\n" + "2;blue;3302\n" + " 4;red;3307\n" + "5;white;4563\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "6;pink2;457883\n" + "7;blank;231233\n"; //$NON-NLS-1$ //$NON-NLS-2$
        output.write(str);
        output.close();
        when(iPath.toFile()).thenReturn(file);
        when(deliFileConn.getFieldSeparatorValue()).thenReturn(";"); //$NON-NLS-1$
        when(deliFileConn.getEncoding()).thenReturn("US-ASCII"); //$NON-NLS-1$
        AnalysisResult results = mock(AnalysisResult.class);
        when(analysis.getResults()).thenReturn(results);
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = mock(EMap.class);
        when(results.getIndicToRowMap()).thenReturn(indicToRowMap);

        List<ModelElement> columnElementList = new BasicEList<ModelElement>();
        List<MetadataColumn> columnElementList2 = new BasicEList<MetadataColumn>();
        MetadataColumn mc0 = mock(MetadataColumn.class);
        MetadataColumn mc1 = mock(MetadataColumn.class);
        MetadataColumn mc2 = mock(MetadataColumn.class);
        columnElementList.add(mc0);
        columnElementList.add(mc1);
        columnElementList.add(mc2);
        columnElementList2.add(mc0);
        columnElementList2.add(mc1);
        columnElementList2.add(mc2);
        EList<ModelElement> eLs = (EList<ModelElement>) columnElementList;
        when(context.getAnalysedElements()).thenReturn(eLs);
        PowerMockito.mockStatic(ColumnHelper.class);
        MetadataTable mTable = mock(MetadataTable.class);
        when(mTable.getColumns()).thenReturn((EList<MetadataColumn>) columnElementList2);
        when(ColumnHelper.getColumnOwnerAsMetadataTable(mc0)).thenReturn(mTable);
        when(ColumnHelper.getColumnOwnerAsMetadataTable(mc1)).thenReturn(mTable);
        when(deliFileConn.getHeaderValue()).thenReturn(empty);
        when(deliFileConn.getFooterValue()).thenReturn(empty);
        when(deliFileConn.getLimitValue()).thenReturn(empty);
        when(deliFileConn.getEscapeType()).thenReturn(Escape.DELIMITED);
        when(deliFileConn.getRowSeparatorValue()).thenReturn("\\n"); //$NON-NLS-1$
        when(deliFileConn.isRemoveEmptyRow()).thenReturn(false);
        when(deliFileConn.isSplitRecord()).thenReturn(false);

        PowerMockito.mockStatic(LanguageManager.class);
        when(LanguageManager.getCurrentLanguage()).thenReturn(ECodeLanguage.JAVA);

        Mockito.doReturn(true).when(spyEvaluator).continueRun();
        spyEvaluator.executeSqlQuery(empty);
    }
}
