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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisDPSelectionPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.CatalogContentProvider;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC mzhao class global comment. Detailled comment
 * 
 * @author mzhao
 * 
 */
public class CatalogAnalysisDPSelectionPage extends AnalysisDPSelectionPage {

    protected static Logger log = Logger.getLogger(CatalogAnalysisDPSelectionPage.class);

    private static String newAnaStr = DefaultMessagesImpl.getString("CatologAnalysisPageStep0.newAnalysis"); //$NON-NLS-1$

    private static String chooseConnStr = DefaultMessagesImpl.getString("CatologAnalysisPageStep0.chooseCatalog"); //$NON-NLS-1$

    private static String connsStr = DefaultMessagesImpl.getString("CatologAnalysisPageStep0.catalogs"); //$NON-NLS-1$

    /**
     * @param pageName
     */
    public CatalogAnalysisDPSelectionPage() {
        super(newAnaStr, chooseConnStr, connsStr, new CatalogContentProvider()); //$NON-NLS-1$
        setPageComplete(false);
    }

    @Override
    protected void addListeners() {

        addListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (object instanceof Catalog) {
                    advanceToNextPageOrFinish();
                }
            }

        });
        addListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                try {
                    Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                    PackagesAnalyisParameter catalogPanameter = (PackagesAnalyisParameter) getConnectionParams();
                    List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
                    if (object instanceof DBCatalogRepNode) {
                        DBCatalogRepNode catalogNode = (DBCatalogRepNode) object;
                        Catalog catalog = ((MetadataCatalogRepositoryObject) catalogNode.getObject()).getCatalog();
                        Connection tdProvider = ConnectionHelper
                                .getTdDataProvider(SwitchHelpers.PACKAGE_SWITCH.doSwitch(catalog));
                        nodes.add(catalogNode);
                        if (tdProvider != null && catalogPanameter != null) {
                            catalogPanameter.setTdDataProvider(tdProvider);
                            catalogPanameter.setConnectionRepNode((DBConnectionRepNode) catalogNode.getParent());
                            catalogPanameter.setPackages(nodes);
                        }
                        setPageComplete(true);
                    } else {
                        setPageComplete(false);
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }
            }

        });
    }
}
