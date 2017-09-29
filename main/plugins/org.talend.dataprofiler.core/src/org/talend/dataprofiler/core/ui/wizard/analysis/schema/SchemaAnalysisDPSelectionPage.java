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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisDPSelectionPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.SchemaContentProvider;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public class SchemaAnalysisDPSelectionPage extends AnalysisDPSelectionPage {

    private static String newAnaStr = DefaultMessagesImpl.getString("SchemaAnalysisPageStep0.newAnalysis"); //$NON-NLS-1$

    private static String chooseConnStr = DefaultMessagesImpl.getString("SchemaAnalysisPageStep0.chooseSchema"); //$NON-NLS-1$

    private static String connsStr = DefaultMessagesImpl.getString("SchemaAnalysisPageStep0.schemas"); //$NON-NLS-1$

    public SchemaAnalysisDPSelectionPage() {
        super(newAnaStr, chooseConnStr, connsStr, new SchemaContentProvider());
    }

    @Override
    protected void addListeners() {
        addListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (object instanceof Schema) {
                    advanceToNextPageOrFinish();
                }
            }

        });
        addListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                PackagesAnalyisParameter schemaPanameter = (PackagesAnalyisParameter) getConnectionParams();

                List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
                if (object instanceof DBSchemaRepNode) {
                    DBSchemaRepNode schemaNode = (DBSchemaRepNode) object;
                    Schema schema = ((MetadataSchemaRepositoryObject) schemaNode.getObject()).getSchema();
                    Connection tdProvider = ConnectionHelper.getTdDataProvider(SwitchHelpers.PACKAGE_SWITCH.doSwitch(schema));
                    RepositoryNode parent = schemaNode.getParent();

                    if (tdProvider != null && schemaPanameter != null) {
                        if (parent instanceof DBCatalogRepNode) {
                            schemaPanameter.setConnectionRepNode((DBConnectionRepNode) parent.getParent());
                        } else {
                            schemaPanameter.setConnectionRepNode((DBConnectionRepNode) parent);
                        }
                        schemaPanameter.setTdDataProvider(tdProvider);
                    }
                    nodes.add(schemaNode);
                    schemaPanameter.setPackages(nodes);
                    setPageComplete(true);
                } else {
                    setPageComplete(false);
                }
            }
        });
    }
}
