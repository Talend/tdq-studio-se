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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.forms.editor.FormEditor;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.ConnectionIndicator;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;

/**
 * @author rli
 */
public class ConnectionAnalysisDetailsPage extends AbstractFilterMetadataPage {

    private ConnectionRepNode connectionNode;

    public ConnectionAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        fillDataProvider();
    }

    @Override
    protected void fillDataProvider() {
        connectionNode = (ConnectionRepNode) getCurrentConnectionRepNode();
        if (connectionNode != null) {
            ConnectionItem item = (ConnectionItem) connectionNode.getObject().getProperty().getItem();
            tdDataProvider = item.getConnection();
        } else {
            EList<ModelElement> analysedElements = getCurrentModelElement().getContext().getAnalysedElements();
            tdDataProvider = null;
            if (analysedElements.size() > 0) {
                tdDataProvider = (Connection) analysedElements.get(0);
            }
        }

    }

    @Override
    protected List<Catalog> getCatalogs() {
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(tdDataProvider);
        return catalogs;
    }

    @Override
    public List<OverviewIndUIElement> getSchemaIndicators() {
        ConnectionIndicator conIndicator = (ConnectionIndicator) getCurrentModelElement().getResults().getIndicators().get(0);
        Connection analyzedElement = (Connection) conIndicator.getAnalyzedElement();
        EList<SchemaIndicator> schemaIndicators = conIndicator.getSchemaIndicators();
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        RepositoryNode connNode = RepositoryNodeHelper.recursiveFind(analyzedElement);
        for (Indicator indicator : schemaIndicators) {
            if (connNode != null) {
                for (IRepositoryNode schemaNode : connNode.getChildren()) {
                    String nodeUuid = ResourceHelper.getUUID(((MetadataSchemaRepositoryObject) schemaNode.getObject())
                            .getSchema());
                    String anaUuid = ResourceHelper.getUUID(indicator.getAnalyzedElement());
                    if (nodeUuid.equals(anaUuid)) {
                        OverviewIndUIElement cataUIEle = new OverviewIndUIElement();
                        cataUIEle.setNode(schemaNode);
                        cataUIEle.setOverviewIndicator(indicator);
                        cataUIEleList.add(cataUIEle);
                        break;
                    }
                }
            }
        }
        return cataUIEleList;
    }

    @Override
    public List<OverviewIndUIElement> getCatalogIndicators() {
        ConnectionIndicator conIndicator = (ConnectionIndicator) getCurrentModelElement().getResults().getIndicators().get(0);
        Connection analyzedElement = (Connection) conIndicator.getAnalyzedElement();
        EList<CatalogIndicator> catalogIndicators = conIndicator.getCatalogIndicators();
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        RepositoryNode connNode = RepositoryNodeHelper.recursiveFind(analyzedElement);
        for (Indicator indicator : catalogIndicators) {
            if (connNode != null) {
                for (IRepositoryNode catalogNode : connNode.getChildren()) {
                    Catalog catalog = ((MetadataCatalogRepositoryObject) catalogNode.getObject()).getCatalog();
                    String connUuid = ResourceHelper.getUUID(catalog);
                    String anaUuid = ResourceHelper.getUUID(indicator.getAnalyzedElement());

                    if (connUuid.equals(anaUuid)) {
                        OverviewIndUIElement cataUIEle = new OverviewIndUIElement();
                        cataUIEle.setNode(catalogNode);
                        cataUIEle.setOverviewIndicator(indicator);
                        cataUIEleList.add(cataUIEle);
                        break;
                    }
                }
            }
        }

        return cataUIEleList;
    }
}
