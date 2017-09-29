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
package org.talend.dq.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
// @RunWith(PowerMockRunner.class)
@PrepareForTest({ EObjectHelper.class, ModelElementHelper.class, RepositoryNodeHelper.class })
public class EObjectHelperTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    /**
     * Test method for
     * {@link org.talend.dq.helper.EObjectHelper#getFirstDependency(org.talend.repository.model.IRepositoryNode)}.
     */
    @Test
    public void testGetFirstDependency() {
        // test when the node is null
        List<ModelElement> firstDependency = EObjectHelper.getFirstDependency(null);
        assertTrue(firstDependency.isEmpty());

        IRepositoryNode tableNode = mock(IRepositoryNode.class);

        MetadataTable mod = mock(MetadataTable.class);
        when(mod.getName()).thenReturn("mod"); //$NON-NLS-1$
        PowerMockito.mockStatic(RepositoryNodeHelper.class);
        when(RepositoryNodeHelper.getMetadataElement(tableNode)).thenReturn(mod);

        Connection connection = mock(Connection.class);
        PowerMockito.mockStatic(ModelElementHelper.class);
        when(ModelElementHelper.getConnection(mod)).thenReturn(connection);

        EList<Dependency> supplierDependency = new BasicEList<Dependency>();
        when(connection.getSupplierDependency()).thenReturn(supplierDependency);

        // when there is no SupplierDependency for this node
        assertTrue(EObjectHelper.getFirstDependency(tableNode).isEmpty());

        // add some analysis to use this table node
        Analysis analysis = mock(Analysis.class);
        Dependency dependency = mock(Dependency.class);
        when(dependency.getKind()).thenReturn(DependenciesHandler.USAGE);

        EList<ModelElement> analysisList = new BasicEList<ModelElement>();
        analysisList.add(analysis);
        when(dependency.getClient()).thenReturn(analysisList);
        when(analysis.eIsProxy()).thenReturn(false);

        supplierDependency.add(dependency);
        when(connection.getSupplierDependency()).thenReturn(supplierDependency);

        AnalysisParameters parameters = mock(AnalysisParameters.class);
        when(parameters.getExecutionLanguage()).thenReturn(ExecutionLanguage.SQL);
        when(analysis.getParameters()).thenReturn(parameters);
        AnalysisContext context = mock(AnalysisContext.class);
        when(analysis.getContext()).thenReturn(context);

        EList<ModelElement> modList = new BasicEList<ModelElement>();
        modList.add(mod);
        when(ModelElementHelper.compareUUID(mod, mod)).thenReturn(true);
        MetadataTable mod1 = mock(MetadataTable.class);
        when(mod1.getName()).thenReturn("mod1"); //$NON-NLS-1$
        when(mod1.eIsProxy()).thenReturn(false);
        modList.add(mod1);

        MetadataTable mod2 = mock(MetadataTable.class);
        when(mod2.getName()).thenReturn("mod2"); //$NON-NLS-1$
        when(mod2.eIsProxy()).thenReturn(false);
        modList.add(mod2);

        when(context.getAnalysedElements()).thenReturn(modList);
        assertFalse(EObjectHelper.getFirstDependency(tableNode).isEmpty());
        assertEquals(analysis, EObjectHelper.getFirstDependency(tableNode).get(0));
    }
}
