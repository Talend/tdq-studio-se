package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

/**
 * Added expression for mariaDB 10
 */
public class UpdateRexMatchForMariaDB10Task extends UpdateRexMatchIndAndPatternForMysql8Task {

    @Override
    public Date getOrder() {
        return createDate(2019, 4, 17);
    }

    @Override
    protected String getIndicatorExpression() {
        return "SELECT COUNT(CASE WHEN <%=__COLUMN_NAMES__%> REGEXP <%=__PATTERN_EXPR__%> THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$
    }

    @Override
    protected String getDatabaseVersion() {
        return "10.0"; //$NON-NLS-1$
    }

    @Override
    protected boolean updatePattern() {
        return false;
    }

}
