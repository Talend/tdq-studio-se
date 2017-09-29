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
package org.talend.dataprofiler.core.ui.wizard.analysis.column;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Test;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.parameters.AnalysisParameter;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class MatchWizardTest {

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.wizard.analysis.column.MatchWizard#initCWMResourceBuilder()}.
     */
    @Test
    public void testInitCWMResourceBuilder() {
        AnalysisParameter anaParameter = new AnalysisParameter();
        anaParameter.setName("analysis1"); //$NON-NLS-1$
        anaParameter.setAnalysisTypeName(AnalysisType.COLUMN.getName());
        MatchWizard matchWizard = new MatchWizard(anaParameter);

        // Default value should be 10000
        Analysis ana = (Analysis) matchWizard.initCWMResourceBuilder();
        AnalysisHandler anaHandler = AnalysisHandler.createHandler(ana);
        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
        int maxRows = preferenceStore.getInt(PluginConstant.MAX_NB_ROWS_ANALYSIS_EDITOR);
        Assert.assertEquals(String.valueOf(maxRows), anaHandler.getDefaultLoadedRowCount());
        // after setting the value, result should be changed too
        String rowCountValue = "100"; //$NON-NLS-1$
        TaggedValueHelper.setTaggedValue(ana, TaggedValueHelper.PREVIEW_ROW_NUMBER, rowCountValue);
        anaHandler = AnalysisHandler.createHandler(ana);
        Assert.assertEquals(rowCountValue, anaHandler.getDefaultLoadedRowCount());
    }

}
