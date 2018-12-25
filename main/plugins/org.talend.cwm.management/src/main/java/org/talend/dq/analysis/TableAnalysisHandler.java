// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.RelationalPackage;
import orgomg.cwm.resource.relational.Table;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableAnalysisHandler extends AnalysisHandler {

    private static Logger log = Logger.getLogger(TableAnalysisHandler.class);

    /**
     * Method "addTableToAnalyze".
     * 
     * @param table
     * @return
     */
    public boolean addTableToAnalyze(TdTable table) {
        assert analysis != null;
        assert analysis.getContext() != null;
        return analysis.getContext().getAnalysedElements().add(table);
    }

    /**
     * DOC xqliu Comment method "addTableToAnalyze".
     * 
     * @param table
     * @return
     */
    public boolean addTableToAnalyze(Collection<TdTable> table) {
        assert analysis != null;
        assert analysis.getContext() != null;
        return analysis.getContext().getAnalysedElements().addAll(table);
    }

    public boolean addIndicator(NamedColumnSet set, Indicator... indicators) {
        if (!analysis.getContext().getAnalysedElements().contains(set)) {
            analysis.getContext().getAnalysedElements().add(set);
        }

        for (Indicator indicator : indicators) {
            // store first level of indicators in result.
            analysis.getResults().getIndicators().add(indicator);
            initializeIndicator(indicator, set);
        }
        DataManager connection = analysis.getContext().getConnection();
        if (connection == null) {
            // try to get one
            log.error(Messages.getString("ColumnCorrelationAnalysisHandler.CONNNOTBEENSETINANALYSIS"));//$NON-NLS-1$
            connection = ConnectionHelper.getTdDataProvider(PackageHelper.getParentPackage((MetadataTable) set));
            analysis.getContext().setConnection(connection);
        }
        TypedReturnCode<Dependency> rc = DependenciesHandler.getInstance().setDependencyOn(analysis, connection);
        return rc.isOk();
    }

    private void initializeIndicator(Indicator indicator, NamedColumnSet set) {
        indicator.setAnalyzedElement(set);
        // Make sure that indicator definition is set
        if (indicator.getIndicatorDefinition() == null) {
            DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator);
        }
        // xqliu case of composite indicators, add children to result.
        if (indicator instanceof CompositeIndicator) {
            for (Indicator child : ((CompositeIndicator) indicator).getChildIndicators()) {
                initializeIndicator(child, set); // recurse
            }
        }
    }

    /**
     * Method "getIndicators".
     * 
     * @param table
     * @return the indicators attached to this table
     */
    public Collection<Indicator> getIndicators(NamedColumnSet set) {
        Collection<Indicator> indics = new ArrayList<Indicator>();
        EList<Indicator> allIndics = analysis.getResults().getIndicators();
        for (Indicator indicator : allIndics) {
            if (indicator.getAnalyzedElement() != null && indicator.getAnalyzedElement().equals(set)) {
                indics.add(indicator);
            }
        }
        return indics;
    }

    /**
     * Method "getIndicatorLeaves" returns the indicators for the given table at the leaf level.
     * 
     * @param table
     * @return the indicators attached to this table
     */
    public Collection<Indicator> getIndicatorLeaves(TdTable table) {
        // get the leaf indicators
        Collection<Indicator> leafIndics = IndicatorHelper.getIndicatorLeaves(analysis.getResults());
        // filter only indicators for this table
        Collection<Indicator> indics = new ArrayList<Indicator>();
        for (Indicator indicator : leafIndics) {
            if (indicator.getAnalyzedElement() != null && indicator.getAnalyzedElement().equals(table)) {
                indics.add(indicator);
            }
        }
        return indics;
    }

    /**
     * Method "setStringDataFilter".
     * 
     * @param datafilterString
     * @return true when a new data filter is created, false if it is only updated
     */
    public boolean setStringDataFilter(String datafilterString) {
        return AnalysisHelper.setStringDataFilter(analysis, datafilterString);
    }

    public String getStringDataFilter() {
        return AnalysisHelper.getStringDataFilter(analysis);
    }

    public EList<ModelElement> getAnalyzedTables() {
        return analysis.getContext().getAnalysedElements();
    }
    
    @Override
    public String getCatalogNames() {
        List<String> catalogNames = new ArrayList<String>();
        
        for (ModelElement element : getAnalyzedColumns()) {
            if (element instanceof Table) {
                ColumnSet columnSet = (ColumnSet) element;
                Package schema = ColumnSetHelper.getParentCatalogOrSchema(columnSet);
                if (schema == null) {
                    continue;
                }
                if (RelationalPackage.eINSTANCE.getCatalog().equals(schema.eClass()) && !catalogNames.contains(schema.getName())) {
                    catalogNames.add(schema.getName());
                } else {
                    Package catalog = ColumnSetHelper.getParentCatalogOrSchema(schema);
                    if (catalog != null && !catalogNames.contains(schema.getName())) {
                        catalogNames.add(catalog.getName());
                    }
                }
            }
        }
        if (!catalogNames.isEmpty()) {
            StringBuilder strBuilder = new StringBuilder();
            for (String catalog : catalogNames) {
                strBuilder.append(catalog);
                strBuilder.append(PluginConstant.SPACE_STRING);
            }
            return strBuilder.toString();
        }
        return PluginConstant.EMPTY_STRING;
    }

}
