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

package org.talend.dq.analysis.explore;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * 
 * DOC mzhao Get sql query statement for correlation. (apply to multiColumnSetValue indicator)
 */
public final class MultiColumnSetValueExplorer extends DataExplorer {

    private static final String SELECT_ALL_FROM = "SELECT * FROM "; //$NON-NLS-1$

    private static MultiColumnSetValueExplorer instance = null;

    private MultiColumnSetValueExplorer() {

    }

    public static MultiColumnSetValueExplorer getInstance() {
        if (instance == null) {
            instance = new MultiColumnSetValueExplorer();
        }
        return instance;
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {
        return null;
    }

    public String getQueryStirng(ModelElement column, Analysis ana, EList<ModelElement> nominalList, String columnName,
            String columnValue) {
        setAnalysis(ana);
        // MOD by hcheng for 6530
        String queryString = SELECT_ALL_FROM + getFullyQualifiedTableName(column);
        int col = columnName.indexOf(" "); //$NON-NLS-1$
        int val = columnValue.indexOf("|"); //$NON-NLS-1$
        for (ModelElement nominal : nominalList) {
            final TdColumn tdColumn = (TdColumn) nominal;

            if (col > 0 && val > 0) {
                String[] name = columnName.split(" "); //$NON-NLS-1$
                String[] value = columnValue.split(" | "); //$NON-NLS-1$

                for (int i = 0; i < name.length; i++) {
                    String where = dbmsLanguage.and();
                    if (i == 0) {
                        where = dbmsLanguage.where();
                    }
                    queryString = buildWhereClause(queryString, tdColumn, name[i], value[i], where);
                }
            } else {
                queryString = buildWhereClause(queryString, tdColumn, columnName.trim(), columnValue, dbmsLanguage.where());
            }
        }
        return queryString;

    }

    private String buildWhereClause(String queryString, final TdColumn tdColumn, String name, String value, String where) {
        if (tdColumn.getName().equals(name) && !value.equals("null")) { //$NON-NLS-1$
            if (Java2SqlType.isTextInSQL(tdColumn.getSqlDataType().getJavaDataType())) {
                queryString += where + dbmsLanguage.quote(name) + dbmsLanguage.equal() + "'" + value + "'"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                queryString += where + dbmsLanguage.quote(name) + dbmsLanguage.equal() + value;
            }
        } else if (tdColumn.getName().equals(name) && value.equals("null")) { //$NON-NLS-1$
            queryString += where + dbmsLanguage.quote(name) + dbmsLanguage.isNull();
        }
        return queryString;
    }
}
