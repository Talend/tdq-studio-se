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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.eclipse.emf.common.util.EList;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.DomainHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * duplicate an analysis model element
 */
public class AnalysisDuplicateHandle extends ModelElementDuplicateHandle {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.action.actions.duplicate.DuplicateEMFResourceHandle#update(orgomg.cwm.objectmodel
     * .core.ModelElement, orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected ModelElement update(ModelElement oldObject, ModelElement newObject) {
        ModelElement tempObject = newObject;
        tempObject = super.update(oldObject, tempObject);
        // copy the client dependency if any
        newObject.getClientDependency().addAll(oldObject.getClientDependency());

        // Added yyin 2012-10-10 TDQ-6236, create a new domain instead of using the domain in the original object
        EList<Domain> dataFilters = ((Analysis) tempObject).getParameters().getDataFilter();
        if (!dataFilters.isEmpty()) {// if the old already be copied into the duplicated one, replace it with a new one
            // MOD msjian TDQ-6513 TDQ-5180 2013-1-4: fix duplicate action lost filters
            int dataFiltersSize = dataFilters.size();
            String tablePattern = DomainHelper.getTablePattern(dataFilters);
            String viewPattern = DomainHelper.getViewPattern(dataFilters);

            dataFilters.clear();
            for (int i = 0; i < dataFiltersSize; i++) {
                Domain domain = DomainHelper.createDomain("Analysis Data Filter"); //$NON-NLS-1$
                dataFilters.add(domain);

                // except overview analysis, the data filter type is range
                String dataFilterStr = AnalysisHelper.getStringDataFilter((Analysis) oldObject, i);
                if (dataFilterStr != null) {
                    AnalysisHelper.setStringDataFilter((Analysis) tempObject, dataFilterStr, i);
                }
            }

            // the overview analysis, the data filter type is pattern
            if (tablePattern != null) {
                DomainHelper.setDataFilterTablePattern(dataFilters, tablePattern);
            }
            if (viewPattern != null) {
                DomainHelper.setDataFilterViewPattern(dataFilters, viewPattern);
            }
            // TDQ-6513 TDQ-5180~
        }
        // ~6236

        return tempObject;
    }

    protected boolean needSaveDepend() {
        return true;
    }
}
