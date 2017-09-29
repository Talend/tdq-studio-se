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
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.forms.editor.FormEditor;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC rli class global comment. Detailled comment
 */
public class CatalogAnalysisDetailsPage extends AbstractFilterMetadataPage {

    private List<CatalogIndicator> catalogIndicatorList = new ArrayList<CatalogIndicator>();

    private List<Catalog> catalogs = new ArrayList<Catalog>();

    /**
     * DOC rli CatalogMasterDetailsPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public CatalogAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractFilterMetadataPage#fillDataProvider()
     */
    @Override
    protected void fillDataProvider() {
        EList<ModelElement> analysedElements = getCurrentModelElement().getContext().getAnalysedElements();
        tdDataProvider = null;
        if (analysedElements.size() > 0) {
            ModelElement modelElement = analysedElements.get(0);
            tdDataProvider = ConnectionHelper.getTdDataProvider((Package) modelElement);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractFilterMetadataPage#getCatalogIndicators()
     */
    @Override
    protected List<OverviewIndUIElement> getCatalogIndicators() {
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        EList<Indicator> indicators = getCurrentModelElement().getResults().getIndicators();
        catalogIndicatorList.clear();
        IRepositoryNode connNode = getCurrentConnectionRepNode();
        for (Indicator indicator : indicators) {
            if (connNode != null) {
                for (IRepositoryNode catalogNode : connNode.getChildren()) {
                    String nodeUuid = ResourceHelper.getUUID(((MetadataCatalogRepositoryObject) catalogNode.getObject())
                            .getCatalog());
                    String anaUuid = ResourceHelper.getUUID(indicator.getAnalyzedElement());
                    if (nodeUuid.equals(anaUuid)) {
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

    @Override
    protected List<Catalog> getCatalogs() {
        catalogs.clear();
        EList<ModelElement> analysedElements = getCurrentModelElement().getContext().getAnalysedElements();
        for (ModelElement element : analysedElements) {
            catalogs.add((Catalog) element);
        }
        return catalogs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractFilterMetadataPage#getSchemaIndicators()
     */
    @Override
    protected List<OverviewIndUIElement> getSchemaIndicators() {
        return Collections.emptyList();
    }
}
