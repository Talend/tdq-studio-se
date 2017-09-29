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
package org.talend.dataprofiler.help;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * DOC zqin class global comment. Detailled comment
 */
public enum BookMarkEnum {
    MySQLRegular("http://dev.mysql.com/doc/refman/5.0/en/regexp.html", "MySQL Regular Expressions"), //$NON-NLS-1$ //$NON-NLS-2$
    OracleRegular("http://docs.oracle.com/cd/E11882_01/appdev.112/e41502/adfns_regexp.htm", "Oracle Regular Expressions"), //$NON-NLS-1$ //$NON-NLS-2$
    SQLServer2005Regular(
            "https://blogs.msdn.microsoft.com/sqlclr/2005/06/29/working-with-regular-expressions/", "SQL Server 2005 Regular Expressions"), //$NON-NLS-1$ //$NON-NLS-2$
    PostgreSQLRegular("http://www.postgresql.org/docs/current/static/functions-matching.html", "PostgreSQL Regular Expressions"), //$NON-NLS-1$ //$NON-NLS-2$
    BoxPlot("http://en.wikipedia.org/wiki/Box_plot", "Box Plot Graphic"), //$NON-NLS-1$ //$NON-NLS-2$
    EclipseSQLExplorer("http://eclipsesql.sourceforge.net/index.php", "Eclipse SQL Explorer"), //$NON-NLS-1$ //$NON-NLS-2$
    TOSDownloadPage("http://www.talend.com/download/", "Talend.com Download Page"), //$NON-NLS-1$ //$NON-NLS-2$
    TOSForum("https://community.talend.com/", "Talend.com Forum"), //$NON-NLS-1$ //$NON-NLS-2$
    TOSBugtracker("http://jira.talendforge.org", "Talend.com Bugtracker"); //$NON-NLS-1$ //$NON-NLS-2$

    private String href;

    private String label;

    public String getHref() {
        return href;
    }

    public String getLabel() {
        return label;
    }

    private BookMarkEnum(String href, String label) {
        this.href = href;
        this.label = label;
    }

    private static final BookMarkEnum[] VALUES_ARRAY = new BookMarkEnum[] { MySQLRegular, OracleRegular, SQLServer2005Regular,
            PostgreSQLRegular, BoxPlot, EclipseSQLExplorer, TOSDownloadPage, TOSForum, TOSBugtracker };

    public static final List<BookMarkEnum> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
}
