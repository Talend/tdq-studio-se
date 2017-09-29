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
package org.talend.dataprofiler.core.ui.wizard.analysis.connection;

import java.util.List;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.ConnectionIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author zqin
 */
public class ConnectionAnalysisWizard extends AnalysisFilterWizard {

    protected ConnAnalysisDPSelectionPage dpSelectionPage;

    public ConnectionAnalysisWizard(AnalysisFilterParameter parameter) {
        super(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(new AnalysisMetadataWizardPage());

        if (getParameter().getConnectionRepNode() == null) {
            dpSelectionPage = new ConnAnalysisDPSelectionPage();
            addPage(dpSelectionPage);
        }

        anaFilterPage = new ConnAnalysisFilterPage();
        addPage(anaFilterPage);
    }

    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();
        if (getAnalysisBuilder() != null) {
            IRepositoryNode connectionRepNode = getParameter().getConnectionRepNode();
            ConnectionItem item = (ConnectionItem) connectionRepNode.getObject().getProperty().getItem();
            Connection tdProvider = item.getConnection();
            getAnalysisBuilder().setAnalysisConnection(tdProvider);
            ConnectionIndicator indicator = SchemaFactory.eINSTANCE.createConnectionIndicator();
            // MOD xqliu 2009-1-21 feature 4715
            DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator);
            indicator.setAnalyzedElement(tdProvider);
            List<Schema> tdSchemas = ConnectionHelper.getSchema(tdProvider);
            if (tdSchemas.size() != 0) {
                addSchemaIndicator(tdSchemas, indicator);
            }
            List<Catalog> tdCatalogs = ConnectionHelper.getCatalogs(tdProvider);
            for (Catalog tdCatalog : tdCatalogs) {
                CatalogIndicator createCatalogIndicator = SchemaFactory.eINSTANCE.createCatalogIndicator();
                // MOD xqliu 2009-1-21 feature 4715
                DefinitionHandler.getInstance().setDefaultIndicatorDefinition(createCatalogIndicator);
                createCatalogIndicator.setAnalyzedElement(tdCatalog);
                // MOD xqliu 2009-11-30 bug 9114
                indicator.addCatalogIndicator(createCatalogIndicator);
                // ~
                addSchemaIndicator(CatalogHelper.getSchemas(tdCatalog), indicator);
            }
            getAnalysisBuilder().addElementToAnalyze(tdProvider, indicator);
        }
        return analysis;
    }
    // @Override
    // public ModelElement initCWMResourceBuilder() {
    //
    // Analysis analysis = (Analysis) super.initCWMResourceBuilder();
    // if (getAnalysisBuilder() != null) {
    // Connection tdProvider = getParameter().getTdDataProvider();
    // getAnalysisBuilder().setAnalysisConnection(tdProvider);
    //
    // ConnectionIndicator indicator = SchemaFactory.eINSTANCE.createConnectionIndicator();
    // // MOD xqliu 2009-1-21 feature 4715
    // DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator);
    // indicator.setAnalyzedElement(tdProvider);
    // List<Schema> tdSchemas = ConnectionHelper.getSchema(tdProvider);
    // if (tdSchemas.size() != 0) {
    // addSchemaIndicator(tdSchemas, indicator);
    // }
    // List<Catalog> tdCatalogs = ConnectionHelper.getCatalogs(tdProvider);
    // for (Catalog tdCatalog : tdCatalogs) {
    // CatalogIndicator createCatalogIndicator = SchemaFactory.eINSTANCE.createCatalogIndicator();
    // // MOD xqliu 2009-1-21 feature 4715
    // DefinitionHandler.getInstance().setDefaultIndicatorDefinition(createCatalogIndicator);
    // createCatalogIndicator.setAnalyzedElement(tdCatalog);
    // // MOD xqliu 2009-11-30 bug 9114
    // indicator.addCatalogIndicator(createCatalogIndicator);
    // // ~
    // addSchemaIndicator(CatalogHelper.getSchemas(tdCatalog), indicator);
    // }
    // getAnalysisBuilder().addElementToAnalyze(tdProvider, indicator);
    // }
    // return analysis;
    // }
}
