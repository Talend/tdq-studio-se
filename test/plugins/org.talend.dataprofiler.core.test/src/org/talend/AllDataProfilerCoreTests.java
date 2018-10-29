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
package org.talend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.talend.core.service.TalendCWMServiceTest;
import org.talend.dataprofiler.core.CorePluginTest;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelperTest;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelperTest;
import org.talend.dataprofiler.core.migration.impl.CreateBenforLawIndicatorTaskTest;
import org.talend.dataprofiler.core.migration.impl.SplitSysIndicatorTaskTest;
import org.talend.dataprofiler.core.migration.impl.UpdateIndicatorForHiveTaskTest;
import org.talend.dataprofiler.core.migration.impl.UpdateMsSqlToJdbcTaskTest;
import org.talend.dataprofiler.core.pattern.ExportFactoryTest;
import org.talend.dataprofiler.core.pattern.ImportFactoryTest;
import org.talend.dataprofiler.core.pattern.USStateCodesPatternRegexTest;
import org.talend.dataprofiler.core.service.TDQResourceChangeHandlerTest;
import org.talend.dataprofiler.core.service.TOPRepositoryServiceTest;
import org.talend.dataprofiler.core.ui.MultiColAnalysisCreationTest;
import org.talend.dataprofiler.core.ui.action.actions.ExportConnectionToTOSActionRealTest;
import org.talend.dataprofiler.core.ui.action.actions.ImportObjectTest;
import org.talend.dataprofiler.core.ui.action.actions.handle.AnalysisHandleTest;
import org.talend.dataprofiler.core.ui.action.actions.handle.DBConnectionDuplicateHandleTest;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProviderRealProjectTest;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProviderTest;
import org.talend.dataprofiler.core.ui.action.provider.SemanticDiscoveryActionProviderTest;
import org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInputRealTest;
import org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInputTest;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnTreeViewerTest;
import org.talend.dataprofiler.core.ui.editor.preview.InidcatorUnitTest;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactoryTest;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BenfordLawFrequencyStateTest;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticsStateTest;
import org.talend.dataprofiler.core.ui.imex.ExportWizardPageTest;
import org.talend.dataprofiler.core.ui.imex.FileTreeLabelProviderTest;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecordTest;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactoryTest;
import org.talend.dataprofiler.core.ui.utils.ModelElementIndicatorRuleTest;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtilsTest;
import org.talend.dataprofiler.core.ui.utils.UDIUtilsTest;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtilsTest;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewLabelProviderTest;
import org.talend.dataprofiler.core.ui.wizard.analysis.column.MatchWizardTest;
import org.talend.dq.indicators.TextIndicatorForNetezzaTest;
import org.talend.metadata.managment.SwitchContextGroupNameImplTest;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(Suite.class)
@SuiteClasses({ TalendCWMServiceTest.class, WorkspaceResourceHelperTest.class, IndicatorDefinitionFileHelperTest.class,
        CreateBenforLawIndicatorTaskTest.class, SplitSysIndicatorTaskTest.class, UpdateIndicatorForHiveTaskTest.class,
        UpdateMsSqlToJdbcTaskTest.class, ExportFactoryTest.class, ImportFactoryTest.class,
        TDQResourceChangeHandlerTest.class, TOPRepositoryServiceTest.class, AnalysisHandleTest.class,
        DBConnectionDuplicateHandleTest.class, ExportConnectionToTOSActionRealTest.class, ImportObjectTest.class,
        AbstractCommonActionProviderRealProjectTest.class, AbstractCommonActionProviderTest.class,
        DrillDownEditorInputTest.class, AnalysisColumnTreeViewerTest.class, BenfordLawFrequencyStateTest.class,
        PatternStatisticsStateTest.class, ChartTableFactoryTest.class, InidcatorUnitTest.class,
        ExportWizardPageTest.class, ResourceViewLabelProviderTest.class, MultiColAnalysisCreationTest.class,
        CorePluginTest.class, SwitchContextGroupNameImplTest.class, ComparatorsFactoryTest.class,
        ModelElementIndicatorRuleTest.class, RepNodeUtilsTest.class, UDIUtilsTest.class, WorkbenchUtilsTest.class,
        FileTreeLabelProviderTest.class, TextIndicatorForNetezzaTest.class, USStateCodesPatternRegexTest.class,
        DrillDownEditorInputRealTest.class, ItemRecordTest.class, MatchWizardTest.class,
        SemanticDiscoveryActionProviderTest.class })
public class AllDataProfilerCoreTests {

}
