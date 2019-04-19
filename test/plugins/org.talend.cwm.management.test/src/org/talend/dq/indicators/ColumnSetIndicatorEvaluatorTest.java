// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.commons.utils.StringUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalyzedDataSet;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.helper.ParameterUtil;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC msjian class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LanguageManager.class, ConnectionUtils.class, ResourceBundle.class, Messages.class, ConnectionUtils.class,
        LanguageManager.class, ParameterUtil.class, StringUtils.class, ColumnHelper.class, JavaSqlFactory.class })
public class ColumnSetIndicatorEvaluatorTest {

    public static String empty = ""; //$NON-NLS-1$

    public static String realFile = "text.txt"; //$NON-NLS-1$

    public static String realFieldSeparator = ";"; //$NON-NLS-1$

    public static String realEncoding = "US-ASCII"; //$NON-NLS-1$

    public static String context_fd1_File = "context.fd1_File"; //$NON-NLS-1$

    public static String context_fd1_FieldSeparator = "context.fd1_FieldSeparator"; //$NON-NLS-1$

    public static String context_fd1_Encoding = "context.fd1_Encoding"; //$NON-NLS-1$

    public static int realHeading = 1;

    public static String zero = "0"; //$NON-NLS-1$

    public static String context_fd1_Header = "context.fd1_Header"; //$NON-NLS-1$

    public static String context_fd1_RowSeparator = "context.fd1_RowSeparator"; //$NON-NLS-1$

    public static String realRowSeparator = "\n"; //$NON-NLS-1$

    /**
     * Test method for {@link org.talend.dq.indicators.ColumnSetIndicatorEvaluator#executeSqlQuery(String)} .
     */
    @Test
    public void testExecuteSqlQuery_file() throws Exception {
        Analysis analysis = mock(Analysis.class);
        stub(method(DelimitedFileIndicatorEvaluator.class, "handleByARow")); //$NON-NLS-1$
        stub(method(DelimitedFileIndicatorEvaluator.class, "addResultToIndicatorToRowMap", Indicator.class, EMap.class)); //$NON-NLS-1$
        AnalysisContext context = mock(AnalysisContext.class);
        when(analysis.getContext()).thenReturn(context);

        DelimitedFileConnection deliFileConn = mock(DelimitedFileConnection.class);
        when(context.getConnection()).thenReturn(deliFileConn);
        when(deliFileConn.isContextMode()).thenReturn(true);

        when(deliFileConn.getFilePath()).thenReturn(context_fd1_File);
        when(deliFileConn.getFieldSeparatorValue()).thenReturn(context_fd1_FieldSeparator);
        when(deliFileConn.getEncoding()).thenReturn(context_fd1_Encoding);
        IPath iPath = mock(IPath.class);
        File file = new File(realFile);
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        String str = "id;Cocust(Tests);owner_id\n" + "1;yellow;3301\n" + "2;blue;3302\n" + "4;red;3307\n" + "5;white;4563\n"
                + "6;pink2;457883\n" + "7;blank;231233\n";
        output.write(str);
        output.close();
        when(iPath.toFile()).thenReturn(file);
        assertTrue(file.exists());

        PowerMockito.mockStatic(JavaSqlFactory.class);
        when(JavaSqlFactory.getURL(deliFileConn)).thenReturn(realFile);
        when(JavaSqlFactory.getFieldSeparatorValue(deliFileConn)).thenReturn(realFieldSeparator);
        when(JavaSqlFactory.getEncoding(deliFileConn)).thenReturn(realEncoding);

        PowerMockito.mockStatic(ResourceBundle.class);
        ResourceBundle bundle = mock(ResourceBundle.class);
        when(ResourceBundle.getBundle("Messages")).thenReturn(bundle); //$NON-NLS-1$

        PowerMockito.mockStatic(Messages.class);
        when(Messages.getString(anyString())).thenReturn("testString"); //$NON-NLS-1$

        AnalysisResult results = mock(AnalysisResult.class);
        when(analysis.getResults()).thenReturn(results);
        EMap<Indicator, AnalyzedDataSet> indicToRowMap = mock(EMap.class);
        when(results.getIndicToRowMap()).thenReturn(indicToRowMap);

        when(deliFileConn.getHeaderValue()).thenReturn(context_fd1_Header);
        when(deliFileConn.getFooterValue()).thenReturn(zero);
        when(deliFileConn.getLimitValue()).thenReturn(zero);
        when(deliFileConn.getEscapeType()).thenReturn(Escape.DELIMITED);
        when(deliFileConn.getRowSeparatorValue()).thenReturn(context_fd1_RowSeparator);
        when(deliFileConn.isSplitRecord()).thenReturn(false);
        when(deliFileConn.isRemoveEmptyRow()).thenReturn(false);

        when(JavaSqlFactory.getHeadValue(deliFileConn)).thenReturn(realHeading);
        when(JavaSqlFactory.getRowSeparatorValue(deliFileConn)).thenReturn(realRowSeparator);

        PowerMockito.mockStatic(LanguageManager.class);
        when(LanguageManager.getCurrentLanguage()).thenReturn(ECodeLanguage.JAVA);

        PowerMockito.mockStatic(ParameterUtil.class);
        when(ParameterUtil.trimParameter(realFile)).thenReturn(realFile);
        when(ParameterUtil.trimParameter(realEncoding)).thenReturn(realEncoding);

        PowerMockito.mockStatic(StringUtils.class);
        when(StringUtils.loadConvert(realFieldSeparator, ECodeLanguage.JAVA.getName())).thenReturn(realFieldSeparator);
        when(ParameterUtil.trimParameter(realFieldSeparator)).thenReturn(realFieldSeparator);

        when(StringUtils.loadConvert(realRowSeparator, ECodeLanguage.JAVA.getName())).thenReturn(realRowSeparator);
        when(ParameterUtil.trimParameter(realRowSeparator)).thenReturn(realRowSeparator);

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
        when(ColumnHelper.getColumnOwnerAsMetadataTable(mc2)).thenReturn(mTable);

        ColumnSetIndicatorEvaluator evaluator = new ColumnSetIndicatorEvaluator(analysis);
        ColumnSetIndicatorEvaluator spyEvaluator = Mockito.spy(evaluator);
        Mockito.doReturn(true).when(spyEvaluator).continueRun();
        ReturnCode rc = spyEvaluator.executeSqlQuery(empty);
        assertTrue(rc.isOk());

    }

}
