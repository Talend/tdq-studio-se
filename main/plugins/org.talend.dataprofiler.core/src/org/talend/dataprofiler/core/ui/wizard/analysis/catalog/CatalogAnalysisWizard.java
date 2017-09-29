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
package org.talend.dataprofiler.core.ui.wizard.analysis.catalog;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.AnalysisFilterWizard;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC rli class global comment. Detailled comment
 */
public class CatalogAnalysisWizard extends AnalysisFilterWizard {

    /**
     * DOC rli CatalogAnalysisWizard constructor comment.
     * 
     * @param parameter
     */
    public CatalogAnalysisWizard(AnalysisFilterParameter parameter) {
        super(parameter);
    }

    @Override
    public void addPages() {
        addPage(new AnalysisMetadataWizardPage());

        if (getParameter().getConnectionRepNode() == null) {
            addPage(new CatalogAnalysisDPSelectionPage());
        }

        anaFilterPage = new CatalogAnalysisFilterPage();
        addPage(anaFilterPage);
    }

    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();
        if (getAnalysisBuilder() != null) {
            // DOC klliu 15750 2011-1-05 add the repnode on parameter
            PackagesAnalyisParameter packageParameter = getParameter();
            IRepositoryNode connectionRepNode = getParameter().getConnectionRepNode();
            ConnectionItem item = (ConnectionItem) connectionRepNode.getObject().getProperty().getItem();
            Connection tdProvider = item.getConnection();
            getAnalysisBuilder().setAnalysisConnection(tdProvider);
            Indicator[] indicators = new Indicator[packageParameter.getPackages().size()];
            ModelElement[] modelElement = new ModelElement[packageParameter.getPackages().size()];
            int i = 0;
            for (IRepositoryNode node : packageParameter.getPackages()) {
                CatalogIndicator createCatalogIndicator = SchemaFactory.eINSTANCE.createCatalogIndicator();
                // MOD xqliu 2009-1-21 feature 4715
                DefinitionHandler.getInstance().setDefaultIndicatorDefinition(createCatalogIndicator);
                Catalog catalog = ((MetadataCatalogRepositoryObject) node.getObject()).getCatalog();
                modelElement[i] = catalog;
                createCatalogIndicator.setAnalyzedElement(catalog);
                indicators[i] = createCatalogIndicator;
                i++;
            }
            getAnalysisBuilder().addElementsToAnalyze(modelElement, indicators);
        }
        return analysis;
    }

    @Override
    public PackagesAnalyisParameter getParameter() {
        return (PackagesAnalyisParameter) super.getParameter();
    }
}
