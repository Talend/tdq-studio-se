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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguageFactory;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class UpdateOracleAverageLengthTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        // TODO Auto-generated method stub
        return createDate(2011, 9, 8);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        IndicatorDefinition aveWithBlank = IndicatorDefinitionFileHelper.getSystemIndicatorByName("Average Length With Blank"); //$NON-NLS-1$
        IndicatorDefinition aveWithNull = IndicatorDefinitionFileHelper.getSystemIndicatorByName("Average Length With Null"); //$NON-NLS-1$
        IndicatorDefinition aveWithBlankNull = IndicatorDefinitionFileHelper
                .getSystemIndicatorByName("Average Length With Blank and Null"); //$NON-NLS-1$

        String newExpWithBlank = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$
        setNewExpression(aveWithBlank, newExpWithBlank);
        setNewExpression(aveWithNull, newExpWithBlank);
        setNewExpression(aveWithBlankNull, newExpWithBlank);
        return true;
    }

    private void setNewExpression(IndicatorDefinition indDefinition, String newExpOracle) {
        if (indDefinition == null) {
            return;
        }
        EList<TdExpression> sqlExpressions = indDefinition.getSqlGenericExpression();
        String languge = null;
        for (TdExpression tdExp : sqlExpressions) {
            languge = tdExp.getLanguage();
            if (DbmsLanguageFactory.isOracle(languge)) {
                tdExp.setBody(newExpOracle);
            }
        }
        IndicatorDefinitionFileHelper.save(indDefinition);
    }

}
