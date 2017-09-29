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
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC rli class global comment. Detailled comment
 */
public class SchemaAnalysisDetailsPage extends AbstractFilterMetadataPage {

    public SchemaAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    @Override
    protected void fillDataProvider() {
        EList<ModelElement> analysedElements = getCurrentModelElement().getContext().getAnalysedElements();
        tdDataProvider = null;
        if (analysedElements.size() > 0) {
            ModelElement modelElement = analysedElements.get(0);
            tdDataProvider = ConnectionHelper.getTdDataProvider((Package) modelElement);
        }
    }

    @Override
    protected List<OverviewIndUIElement> getCatalogIndicators() {
        return Collections.emptyList();
    }

    @Override
    protected List<Catalog> getCatalogs() {
        return Collections.emptyList();
    }

    @Override
    protected List<OverviewIndUIElement> getSchemaIndicators() {
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        EList<Indicator> indicators = getCurrentModelElement().getResults().getIndicators();
        for (Indicator indicator : indicators) {
            RepositoryNode schemaNode = RepositoryNodeHelper.recursiveFind(indicator.getAnalyzedElement());
            OverviewIndUIElement cataUIEle = new OverviewIndUIElement();
            cataUIEle.setNode(schemaNode);
            cataUIEle.setOverviewIndicator(indicator);
            cataUIEleList.add(cataUIEle);
        }
        return cataUIEleList;
    }

    /**
     * if the current analysis is schema overview, there are no catalogs, but only schemas. so, the number of schemas
     * here equals to the number of the indicators.
     * 
     * @param tdCatalogs
     * @return
     */
    @Override
    protected int getSchamas(List<Catalog> tdCatalogs) {
        return getCurrentModelElement().getResults().getIndicators().size();
    }
}
