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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;
import org.talend.dq.dbms.BigQueryDbmsLanguage;
import org.talend.dq.dbms.GenericSQLHandler;
import org.talend.dq.dbms.HiveDbmsLanguage;
import org.talend.dq.helper.ColumnDependencyHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ColumnDependencyExplorer extends DataExplorer {

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(MENU_VIEW_VALID_ROWS, getComment(MENU_VIEW_VALID_ROWS) + getValidRowsStatement());
        map.put(MENU_VIEW_VALID_VALUES, getComment(MENU_VIEW_VALID_VALUES) + getValidValuesStatement());
        map.put(MENU_VIEW_DETAILED_VALID_VALUES, getComment(MENU_VIEW_DETAILED_VALID_VALUES) + getDetailedValidValuesStatement());
        map.put(MENU_VIEW_INVALID_ROWS, getComment(MENU_VIEW_INVALID_ROWS) + getInvalidRowsStatement());
        map.put(MENU_VIEW_INVALID_VALUES, getComment(MENU_VIEW_INVALID_VALUES) + getInvalidValuesStatement());
        map.put(MENU_VIEW_DETAILED_INVALID_VALUES, getComment(MENU_VIEW_DETAILED_INVALID_VALUES)
                + getDetailedInvalidValuesStatement());
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.DataExplorer#NotShowMenu()
     */
    @Override
    protected boolean NotShowMenu() {
        // MOD qiongli 2012-8-14 TDQ-5907 Hive dosen't support these sql
        return dbmsLanguage instanceof HiveDbmsLanguage;
    }

    /**
     * DOC xqliu Comment method "getInvalidRowsStatement".
     * 
     * @return
     */
    private String getInvalidRowsStatement() {
        return getStatement(dbmsLanguage.getFDGenericInvalidRows());
    }

    private String getDetailedInvalidValuesStatement() {
        // MOD by zshen fixed 12039 to distinguish the same name of column.
        String genericSQL = dbmsLanguage.getFDGenericInvalidDetailedValues();
        ColumnDependencyIndicator cdIndicator = ((ColumnDependencyIndicator) this.indicator);
        TdColumn columnA = cdIndicator.getColumnA();
        TdColumn columnB = cdIndicator.getColumnB();
        if (columnA.getName().equals(columnB.getName())) {
            genericSQL = genericSQL.replaceFirst(GenericSQLHandler.COLUMN_NAMES_A, dbmsLanguage.quote(columnA.getName()) + " AS "//$NON-NLS-1$
                    + columnA.getName() + "_A");//$NON-NLS-1$
            genericSQL = genericSQL.replaceFirst(GenericSQLHandler.COLUMN_NAMES_B, dbmsLanguage.quote(columnB.getName()) + " AS "//$NON-NLS-1$
                    + columnA.getName() + "_B");//$NON-NLS-1$
        }
        return getStatement(genericSQL);
    }

    private String getDetailedValidValuesStatement() {
        // MOD by zshen fixed 12039 to distinguish the same name of column.
        String genericSQL = dbmsLanguage.getFDGenericValidDetailedValues();
        ColumnDependencyIndicator cdIndicator = ((ColumnDependencyIndicator) this.indicator);
        TdColumn columnA = cdIndicator.getColumnA();
        TdColumn columnB = cdIndicator.getColumnB();
        if (columnA.getName().equals(columnB.getName())) {
            genericSQL = genericSQL.replaceFirst(GenericSQLHandler.COLUMN_NAMES_A, dbmsLanguage.quote(columnA.getName()) + " AS "//$NON-NLS-1$
                    + columnA.getName() + "_A");//$NON-NLS-1$
            genericSQL = genericSQL.replaceFirst(GenericSQLHandler.COLUMN_NAMES_B, dbmsLanguage.quote(columnB.getName()) + " AS "//$NON-NLS-1$
                    + columnA.getName() + "_B");//$NON-NLS-1$
        }
        return getStatement(genericSQL);
    }

    private String getInvalidValuesStatement() {
        return getStatement(dbmsLanguage.getFDGenericInvalidValues());
    }

    private String getValidValuesStatement() {
        return getStatement(dbmsLanguage.getFDGenericValidValues());
    }

    /**
     * DOC xqliu Comment method "getValidRowsStatement".
     * 
     * @return
     */
    private String getValidRowsStatement() {
        return getStatement(dbmsLanguage.getFDGenericValidRows());
    }

    /**
     * DOC xqliu Comment method "getStatement".
     * 
     * @param genericSQL
     * @return
     */
    private String getStatement(String genericSQL) {
        ColumnDependencyIndicator cdIndicator = ((ColumnDependencyIndicator) this.indicator);
        TdColumn columnA = cdIndicator.getColumnA();
        TdColumn columnB = cdIndicator.getColumnB();

        GenericSQLHandler sqlHandler = new GenericSQLHandler(genericSQL);

        String fullyQualifiedTableName = getFullyQualifiedTableName(columnA);
        sqlHandler.replaceColumnA(dbmsLanguage.quote(columnA.getName())).replaceColumnB(dbmsLanguage.quote(columnB.getName()))
                .replaceTable(dbmsLanguage.quote(fullyQualifiedTableName));

        String instantiatedSQL = sqlHandler.getSqlString();

        List<String> whereClauses = new ArrayList<String>();
        String dataFilter = ContextHelper.getDataFilterWithoutContext(this.analysis);
        if (!StringUtils.isEmpty(dataFilter)) {
            whereClauses.add(dataFilter);
        }

        instantiatedSQL = dbmsLanguage.addWhereToSqlStringStatement(instantiatedSQL, whereClauses);

        // TDQ-15039: `test_tbd_4452`.`testalltypes_kmo`.`strCol`-->`testalltypes_kmo`.`strCol`
        if (dbmsLanguage instanceof BigQueryDbmsLanguage) {
            ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(columnA);
            instantiatedSQL = instantiatedSQL.replace(fullyQualifiedTableName + ".`", columnSetOwner.getName() + ".`");
        }
        
        return instantiatedSQL;
    }

    @Override
    public void setEnitty(ChartDataEntity entity) {
        this.entity = entity;
        this.indicator = entity.getIndicator();
        this.indicatorEnum = IndicatorEnum.findIndicatorEnum(indicator.eClass());
        this.columnName = ColumnDependencyHelper.getIndicatorName(entity.getIndicator());
    }
}
