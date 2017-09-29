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
package org.talend.dataprofiler.core.ui.wizard.analysis.schema;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.AnalysisFilterWizard;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dq.analysis.parameters.AnalysisFilterParameter;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC rli class global comment. Detailled comment
 */
public class SchemaAnalysisWizard extends AnalysisFilterWizard {

    private SchemaAnalysisDPSelectionPage tableAnaDPSelectionPage = null;

    /**
     * DOC rli SchemaAnalysisWizard constructor comment.
     * 
     * @param parameter
     */
    public SchemaAnalysisWizard(AnalysisFilterParameter parameter) {
        super(parameter);
    }

    @Override
    public void addPages() {
        addPage(new AnalysisMetadataWizardPage());
        if (getParameter().getConnectionRepNode() == null) {
            tableAnaDPSelectionPage = new SchemaAnalysisDPSelectionPage();
            addPage(tableAnaDPSelectionPage);
        }

        anaFilterPage = new SchemaAnalysisFilterPage();
        addPage(anaFilterPage);
    }

    @Override
    public ModelElement initCWMResourceBuilder() {
        Analysis analysis = (Analysis) super.initCWMResourceBuilder();

        if (getAnalysisBuilder() != null) {
            // MOD klliu 15750 2011-1-05 add the repnode on parameter
            PackagesAnalyisParameter packageParameter = getParameter();
            IRepositoryNode connectionRepNode = getParameter().getConnectionRepNode();
            ConnectionItem item = (ConnectionItem) connectionRepNode.getObject().getProperty().getItem();
            Connection tdProvider = item.getConnection();
            // Connection tdProvider = packageParameter.getTdDataProvider();
            getAnalysisBuilder().setAnalysisConnection(tdProvider);
            Indicator[] indicators = new Indicator[packageParameter.getPackages().size()];
            ModelElement[] modelElement = new ModelElement[packageParameter.getPackages().size()];
            int i = 0;
            for (IRepositoryNode node : packageParameter.getPackages()) {
                SchemaIndicator createSchemaIndicator = SchemaFactory.eINSTANCE.createSchemaIndicator();
                DBSchemaRepNode catalogNode = (DBSchemaRepNode) node;
                Schema schema = ((MetadataSchemaRepositoryObject) catalogNode.getObject()).getSchema();
                modelElement[i] = schema;
                DefinitionHandler.getInstance().setDefaultIndicatorDefinition(createSchemaIndicator);
                createSchemaIndicator.setAnalyzedElement(schema);
                indicators[i] = createSchemaIndicator;
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
