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
package org.talend.dq.analysis.explore;

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * return the where clause for benford law indicator, but for different DB type, the clause is different.
 */
public class BenfordLawFrequencyExplorer extends FrequencyStatisticsExplorer {

    @Override
    protected String getInstantiatedClause() {
        // TDQ-11422: when the analysis have not run, the entity.getKey() is null
        if (entity.getKey() != null && entity.getKey().toString().equals("invalid")) {//$NON-NLS-1$
            return dbmsLanguage.getInvalidClauseBenFord(this.columnName);
        }
        Object value = "'" + entity.getKey() + "%'"; //$NON-NLS-1$ //$NON-NLS-2$

        String clause = entity.isLabelNull() ? getColumnName() + dbmsLanguage.isNull() : getColumnName() + dbmsLanguage.like()
                + value;

        if (isInformix()) {
            return " SUBSTR(" + getColumnName() + ",0,1)" + dbmsLanguage.like() + value; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return clause;
    }

    /**
     * DOC yyin Comment method "isInformix".
     * 
     * @return
     */
    private boolean isInformix() {
        if (dbmsLanguage.getDbmsName().startsWith(SupportDBUrlType.INFORMIXDEFAULTURL.getLanguage())) {
            return true;
        }
        return false;
    }

    /**
     * DOC yyin Comment method "getColumnName".
     * 
     * @return
     */
    private String getColumnName() {
        return dbmsLanguage.castColumnNameToChar(columnName);
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(MENU_VIEW_ROWS, getComment(MENU_VIEW_ROWS) + getFreqRowsStatement());
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.DataExplorer#NotShowMenu()
     */
    @Override
    protected boolean NotShowMenu() {
        if (AnalysisHelper.isJavaExecutionEngine(this.analysis)) {
            return true;
        }
        // no need menu on a Date type column.
        ModelElement analyzedElement = indicator.getAnalyzedElement();
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analyzedElement);
        if (tdColumn != null && Java2SqlType.isDateInSQL(tdColumn.getSqlDataType().getJavaDataType())) {
            return true;
        }
        return false;
    }
}
