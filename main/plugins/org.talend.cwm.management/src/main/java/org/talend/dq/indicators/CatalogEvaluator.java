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
package org.talend.dq.indicators;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.helpers.DataqualitySwitchHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.SchemaFactory;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class CatalogEvaluator extends AbstractSchemaEvaluator<Catalog> {

    private static Logger log = Logger.getLogger(CatalogEvaluator.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.AbstractSchemaEvaluator#getDataManager()
     */
    @Override
    protected Connection getDataManager() {
        Catalog catalog = this.getAnalyzedElements().iterator().next();
        return catalog != null ? ConnectionHelper.getTdDataProvider(catalog) : null;
    }

    @Override
    protected ReturnCode executeSqlQuery(String sqlStatement) throws SQLException {
        ReturnCode ok = new ReturnCode(true);
        // --- preconditions
        DataProvider dataProvider = this.getDataManager();

        if (this.elementToIndicators.values().isEmpty()) {
            String msg = Messages.getString("Evaluator.NoInidcator1"); //$NON-NLS-1$
            log.error(msg);
            ok.setReturnCode(msg, false);
            return ok;
        }
        Indicator[] indics = this.getAllIndicators();
        if (indics.length == 0) {
            String msg = Messages.getString("Evaluator.NoInidcator2", dataProvider); //$NON-NLS-1$
            log.error(msg);
            ok.setReturnCode(msg, false);
            return ok;
        }
        for (Indicator indicator : indics) {
            CatalogIndicator catalogIndicator = DataqualitySwitchHelper.CATALOG_SWITCH.doSwitch(indicator);
            if (catalogIndicator == null) {
                continue;
            }
            Catalog catalog = (Catalog) catalogIndicator.getAnalyzedElement();
            String catName = catalog.getName();
            // MOD yyi 2009-11-30 10187
            if (!checkCatalog(catName)) {
                ok.setReturnCode(Messages.getString("Evaluator.catalogNotExist", catName), false); //$NON-NLS-1$
                return ok;
            }
            // ~
            // MOD qiongli 2012-8-9,Method 'Method not supported' not supported for HiveConnection
            if (dbms().supportCatalogSelection()) {
                connection.setCatalog(catName);
            }

            List<Schema> schemas = CatalogHelper.getSchemas(catalog);
            if (schemas.isEmpty()) { // no schema
                evalCatalogIndic(catalogIndicator, catalog, ok);
            } else {
                catalogIndicator.setAnalyzedElement(catalog);
                catalogIndicator.setSchemaCount(schemas.size());
                // --- create SchemaIndicator for each pair of catalog schema
                for (Schema tdSchema : schemas) {
                    // --- create SchemaIndicator for each catalog
                    SchemaIndicator schemaIndic = SchemaFactory.eINSTANCE.createSchemaIndicator();
                    // MOD xqliu 2009-1-21 feature 4715
                    DefinitionHandler.getInstance().setDefaultIndicatorDefinition(schemaIndic);
                    evalSchemaIndicLow(catalogIndicator, schemaIndic, catalog, tdSchema, ok);
                }

            }
        }
        return ok;
    }

}
