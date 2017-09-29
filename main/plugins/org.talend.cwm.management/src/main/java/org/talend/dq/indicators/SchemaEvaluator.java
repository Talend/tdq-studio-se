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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.helpers.DataqualitySwitchHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class SchemaEvaluator extends AbstractSchemaEvaluator<Schema> {

    private static Logger log = Logger.getLogger(SchemaEvaluator.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.AbstractSchemaEvaluator#getDataManager()
     */
    @Override
    protected Connection getDataManager() {
        Schema schema = this.getAnalyzedElements().iterator().next();
        return schema != null ? ConnectionHelper.getTdDataProvider(schema) : null;
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
            SchemaIndicator schemaIndicator = DataqualitySwitchHelper.SCHEMA_SWITCH.doSwitch(indicator);
            if (schemaIndicator == null) {
                continue;
            }
            Schema schema = (Schema) schemaIndicator.getAnalyzedElement();
            String catName = schema.getName();
            // MOD yyi 2009-11-30 10187
            if (!checkSchema(schema)) {
                ok.setReturnCode(Messages.getString("Evaluator.schemaNotExist", catName), false); //$NON-NLS-1$
                return ok;
            }
            // ~
            // ADD xqliu 2010-01-06 bug 10919
            EObject container = schema.eContainer();
            if (container != null) {
                Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(container);
                if (catalog != null) {
                    catName = catalog.getName();
                    // MOD gdbu 2011-4-21 bug : 20578
                    if (!ConnectionUtils.isOdbcProgress(connection) && !ConnectionUtils.isOdbcOracle(connection)
                            && StringUtils.isNotEmpty(catName) && dbms().supportCatalogSelection()) {
                        connection.setCatalog(catName);
                    }
                    // ~20578
                }
            }
            // ~
            evalSchemaIndicLow(null, schemaIndicator, null, schema, ok);
        }
        return ok;
    }

}
