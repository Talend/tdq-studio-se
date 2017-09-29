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
package org.talend.dq.analysis.explore;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;

import java.util.ResourceBundle;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import orgomg.cwm.foundation.softwaredeployment.DataManager;

/**
 * DOC yyin class global comment. Detailled comment
 */
@PrepareForTest({ DbmsLanguageFactory.class, Messages.class })
public class DataExplorerTestHelper {

    // use this before new a Explorer
    public static void initDataExplorer() {
        ResourceBundle rb2 = mock(ResourceBundle.class);
        stub(method(ResourceBundle.class, "getBundle", String.class)).toReturn(rb2);
        PowerMockito.mockStatic(Messages.class);
        when(Messages.getString(anyString())).thenReturn("unit test");

    }

    public static Analysis getAnalysis(Indicator indicator, DbmsLanguage dbmsLanguage) {
        Analysis analysis = mock(Analysis.class);
        AnalysisContext context = mock(AnalysisContext.class);
        when(analysis.getContext()).thenReturn(context);
        DataManager dataManager = mock(DataManager.class);
        when(context.getConnection()).thenReturn(dataManager);

        // mock setEntity
        when(indicator.eClass()).thenReturn(null);

        // MOCKING STATIC METHODS
        stub(method(DbmsLanguageFactory.class, "createDbmsLanguage", DataManager.class, ExecutionLanguage.class)).toReturn(
                dbmsLanguage);

        return analysis;
    }
}
