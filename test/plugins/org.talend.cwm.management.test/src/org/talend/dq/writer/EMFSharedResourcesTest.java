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
package org.talend.dq.writer;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.impl.PropertiesFactoryImpl;
import org.talend.dq.helper.UnitTestBuildHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2016年7月11日 Detailled comment
 *
 */
public class EMFSharedResourcesTest {

    Project currentProject;

    AnalysisRepNode anaNode;

    @Before
    public void setUp() throws Exception {
        System.setProperty("studio.version", "6.3");
        UnitTestBuildHelper.initProjectStructure();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            tdqService.createDQStructor();
        }

        currentProject = ProjectManager.getInstance().getCurrentProject();
    }

    /**
     * Test method for
     * {@link org.talend.dq.writer.EMFSharedResources#reloadModelElementInNode(org.talend.repository.model.IRepositoryNode)}
     * .
     */
    @Test
    public void testReloadModelElementInNode() {
        IRepositoryViewObject viewObject = UnitTestBuildHelper.buildRepositoryViewObjectSystemFolder(
                currentProject.getEmfProject(), currentProject.getAuthor(), ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);

        RepositoryNode node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
        viewObject.setRepositoryNode(node);

        Item anaItem = createRealAnalysis("analysis_emfreload", null, false); //$NON-NLS-1$

        IRepositoryViewObject object = null;
        try {
            object = ProxyRepositoryFactory.getInstance().getLastVersion(anaItem.getProperty().getId());
        } catch (PersistenceException e1) {
            fail("testReloadModelElementInNode fail: " + e1.getMessage()); //$NON-NLS-1$
        }

        anaNode = new AnalysisRepNode(object, node, ENodeType.REPOSITORY_ELEMENT, currentProject);
        anaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        anaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);

        URI uri = anaItem.eResource().getURI();
        String fileExtension = anaItem.getFileExtension();
        String removeEnd = StringUtils.removeEnd(uri.path(), "." + FactoriesUtil.PROPERTIES_EXTENSION); //$NON-NLS-1$
        Analysis analysis = ((TDQAnalysisItem) anaItem).getAnalysis();

        try {
            ProxyRepositoryFactory.getInstance().unloadResources(uri.scheme() + ":" + removeEnd + "." + fileExtension); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (PersistenceException e) {
            fail("testReloadModelElementInNode fail: " + e.getMessage()); //$NON-NLS-1$
        }

        // ModelElement modelElement = PropertyHelper.getModelElement(anaItem.getProperty());
        assertTrue(analysis.eIsProxy());

        ModelElement reloadModelElement = EMFSharedResources.getInstance().reloadModelElementInNode(anaNode);
        assertFalse(reloadModelElement.eIsProxy());

    }

    private Item createRealAnalysis(String name, IFolder folder, Boolean isDelete) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        Analysis analysis1 = AnalysisHelper.createAnalysis(name);
        TDQAnalysisItem item1 = PropertiesFactoryImpl.eINSTANCE.createTDQAnalysisItem();
        org.talend.core.model.properties.Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId(EcoreUtil.generateUUID());
        property1.setItem(item1);
        property1.setLabel(analysis1.getName());
        item1.setProperty(property1);
        item1.setAnalysis(analysis1);
        ItemState itemState = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(isDelete);
        item1.setState(itemState);
        AnalysisResult analysisResult1 = AnalysisFactory.eINSTANCE.createAnalysisResult();
        analysis1.setResults(analysisResult1);
        try {
            ProxyRepositoryFactory.getInstance().create(item1, createPath, false);
        } catch (PersistenceException e) {
            fail(e.getMessage());
        }
        return item1;
    }

    @After
    public void tearDown() throws Exception {
        ProxyRepositoryFactory.getInstance().deleteObjectLogical(anaNode.getObject());
    }

}
