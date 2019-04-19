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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import junit.framework.Assert;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ WorkbenchUtils.class, ResourceManager.class, RepositoryNodeHelper.class })
public class AnalysisHandleTest {

    private static final String VIEW_PATTERN_FILTER = "viewPatternFilter"; //$NON-NLS-1$

    private static final String TABLE_PATTERN_FILTER = "tablePatternFilter"; //$NON-NLS-1$

    Analysis oldAnalysis;

    AnalysisDuplicateHandle handle;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // init the analysis
        oldAnalysis = AnalysisHelper.createAnalysis("Analysis Data Filter"); //$NON-NLS-1$
        AnalysisParameters parameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        oldAnalysis.setParameters(parameters);

        // add two filters which type is ranges
        AnalysisHelper.setStringDataFilter(oldAnalysis, "filter1", 0); //$NON-NLS-1$
        AnalysisHelper.setStringDataFilter(oldAnalysis, "filter2", 1); //$NON-NLS-1$

        // add one filter which type is pattern and contains two patterns
        EList<Domain> dataFilters = oldAnalysis.getParameters().getDataFilter();
        RegularExpression tablePattern = BooleanExpressionHelper.createRegularExpression(null, TABLE_PATTERN_FILTER);
        addPatternToDomain(dataFilters.get(0), tablePattern, "Table Pattern"); //$NON-NLS-1$
        RegularExpression viewPattern = BooleanExpressionHelper.createRegularExpression(null, VIEW_PATTERN_FILTER);
        addPatternToDomain(dataFilters.get(0), viewPattern, "View Pattern"); //$NON-NLS-1$

        // create a handle
        IRepositoryNode node = mock(IRepositoryNode.class);
        IRepositoryViewObject object = mock(IRepositoryViewObject.class);
        when(node.getObject()).thenReturn(object);
        Property pro = mock(Property.class);
        when(object.getProperty()).thenReturn(pro);

        IPath itemPath = mock(IPath.class);
        PowerMockito.mockStatic(WorkbenchUtils.class);
        when(WorkbenchUtils.getFilePath(node)).thenReturn(itemPath);

        PowerMockito.mockStatic(ResourceManager.class);
        IWorkspaceRoot root = mock(IWorkspaceRoot.class);
        when(ResourceManager.getRoot()).thenReturn(root);
        when(root.getFile(itemPath)).thenReturn(null);

        PowerMockito.mockStatic(RepositoryNodeHelper.class);
        when(RepositoryNodeHelper.getModelElementFromRepositoryNode(node)).thenReturn(oldAnalysis);

        handle = new AnalysisDuplicateHandle();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // TODO
    }

    private static void addPatternToDomain(Domain domain, RegularExpression filter, String patternName) {
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName(patternName);
        pattern.getComponents().add(filter);
        domain.getPatterns().add(pattern);
        domain.getOwnedElement().add(pattern);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.actions.handle.AnalysisDuplicateHandle#update(orgomg.cwm.objectmodel.core.ModelElement, orgomg.cwm.objectmodel.core.ModelElement)}
     * .
     */
    @Test
    public void testUpdate() {
        Analysis newAnalysis = AnalysisHelper.createAnalysis("new ana"); //$NON-NLS-1$
        AnalysisParameters parameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        newAnalysis.setParameters(parameters);

        // make the new analysis use the old domain
        EList<Domain> oldDataFilters = oldAnalysis.getParameters().getDataFilter();
        assertEquals(2, oldDataFilters.size());
        EList<Domain> newFilter = parameters.getDataFilter();
        for (Domain domain : oldDataFilters) {
            newFilter.add(domain);
        }

        ModelElement copiedAna = handle.update(oldAnalysis, newAnalysis);

        EList<Domain> newDataFilters = ((Analysis) copiedAna).getParameters().getDataFilter();
        assertEquals(2, newDataFilters.size());

        // after update, the old domain should be replaced by a new one, but with same body& language
        for (int i = 0; i < newDataFilters.size(); i++) {
            Domain domain = newDataFilters.get(i);
            Domain oldDomain = oldDataFilters.get(i);
            Assert.assertNotSame(oldDomain, domain);
            assertTrue(domain.getName().equals("Analysis Data Filter")); //$NON-NLS-1$

            // the expressions are different instances, with same boby&language.
            if (domain.getRanges() != null && oldDomain.getRanges() != null) {
                Expression ex = domain.getRanges().get(0).getExpressions().getExpression();
                Expression oldex = oldDomain.getRanges().get(0).getExpressions().getExpression();
                Assert.assertNotSame(oldex, ex);
                assertTrue(ex.getBody().equals(oldex.getBody()));
                assertTrue(ex.getLanguage().equals(oldex.getLanguage()));
            }
        }

        // check the patterns
        String tablePattern = DomainHelper.getTablePattern(newDataFilters);
        assertEquals(TABLE_PATTERN_FILTER, tablePattern);
        String viewPattern = DomainHelper.getViewPattern(newDataFilters);
        assertEquals(VIEW_PATTERN_FILTER, viewPattern);

    }

}
