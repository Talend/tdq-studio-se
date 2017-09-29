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

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * created by yyin on 2014-2-18 : add the sql expressions for the database:Netezza, in the system indicators of: some
 * Text indicators, Pattern Frequency Statistics, Soundex, and Benford indicator; and add the default one in pattern
 * finder and soundex who does not have the default one before
 * 
 */
public class AddNetezzaExpressionInIndicatorsTask extends AbstractWorksapceUpdateTask {

    private static final String AVERAGE_LENGTH = "Average Length"; //$NON-NLS-1$

    // related sql expression
    private final String AVERAGE_LENGTH_SQL = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL = "Average Length With Blank and Null"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_SQL = "SELECT SUM(CHAR_LENGTH(CASE WHEN   CHAR_LENGTH( TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'')) ) =0  THEN '' ELSE  ISNULL(<%=__COLUMN_NAMES__%>,'') END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_NULL = "Average Length With Null"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_NULL_SQL = "SELECT SUM(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH = "Maximal Length"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_SQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_NULL = "Maximal Length With Null"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_NULL_SQL = "SELECT MAX(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL = "Maximal Length With Blank and Null";//$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL = "SELECT MAX(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$

    private static final String MINIMAL_LENGTH = "Minimal Length"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_SQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL = "Minimal Length With Blank and Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL = "SELECT MIN(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_NULL = "Minimal Length With Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_NULL_SQL = "SELECT MIN(CHAR_LENGTH(ISNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(ISNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String BENFORD_LAW = "Benford Law Frequency";//$NON-NLS-1$

    private static final String BENFORD_LAW_SQL = "SELECT cast(SUBSTR(<%=__COLUMN_NAMES__%>,1,1) as char), COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 1 order by 1";//$NON-NLS-1$

    private static final String SOUNDEX_LOW_FREQUENCY = "Soundex Low Frequency Table";//$NON-NLS-1$

    private static final String SOUNDEX_LOW_FREQUENCY_DEFAULT = "SELECT MAX(<%=__COLUMN_NAMES__%>), SOUNDEX(<%=__COLUMN_NAMES__%>), COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY SOUNDEX(<%=__COLUMN_NAMES__%>) ORDER BY d,c ASC"; //$NON-NLS-1$;

    private static final String SOUNDEX_LOW_FREQUENCY_SQL = "SELECT MAX(<%=__COLUMN_NAMES__%>), NYSIIS(<%=__COLUMN_NAMES__%>), COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY NYSIIS(<%=__COLUMN_NAMES__%>) ORDER BY d,c ASC";//$NON-NLS-1$

    private static final String SOUNDEX_FREQUENCY = "Soundex Frequency Table";//$NON-NLS-1$

    private static final String SOUNDEX_FREQUENCY_DEFAULT = "SELECT MAX(<%=__COLUMN_NAMES__%>), SOUNDEX(<%=__COLUMN_NAMES__%>), COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY SOUNDEX(<%=__COLUMN_NAMES__%>) ORDER BY d DESC,c DESC";//$NON-NLS-1$

    private static final String SOUNDEX_FREQUENCY_SQL = "SELECT MAX(<%=__COLUMN_NAMES__%>), NYSIIS(<%=__COLUMN_NAMES__%>) , COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 2 ORDER BY d DESC,c DESC";//$NON-NLS-1$

    private static final String PATTERN_LOW_FREQUENCY = "Pattern Low Frequency Table"; //$NON-NLS-1$

    private static final String PATTERN_LOW_FREQUENCY_DEFAULT = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c ASC"; //$NON-NLS-1$

    private static final String PATTERN_LOW_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c ASC";//$NON-NLS-1$

    private static final String PATTERN_FREQUENCY = "Pattern Frequency Table";//$NON-NLS-1$

    private static final String PATTERN_FREQUENCY_DEFAULT = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c DESC";//$NON-NLS-1$

    private static final String PATTERN_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) AS c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c DESC";//$NON-NLS-1$

    private final String Netezza = SupportDBUrlType.NETEZZADEFAULTURL.getLanguage();

    private final String SQL = "SQL";//$NON-NLS-1$

    private final String CHAR_TOREPLACE = "abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789";//$NON-NLS-1$

    private final String CHAR_REPLACE = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999";//$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;

        // the following indicator only need to add Netezza expression
        result = result && addExpression(AVERAGE_LENGTH, AVERAGE_LENGTH_SQL, Netezza, false);
        result = result
                && addExpression(AVERAGE_LENGTH_WITH_BLANK_AND_NULL, AVERAGE_LENGTH_WITH_BLANK_AND_NULL_SQL, Netezza, false);
        result = result && addExpression(AVERAGE_LENGTH_WITH_NULL, AVERAGE_LENGTH_WITH_NULL_SQL, Netezza, false);
        result = result && addExpression(MAXIMAL_LENGTH, MAXIMAL_LENGTH_SQL, Netezza, false);
        result = result && addExpression(MAXIMAL_LENGTH_WITH_NULL, MAXIMAL_LENGTH_WITH_NULL_SQL, Netezza, false);
        result = result
                && addExpression(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL, MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL, Netezza, false);

        result = result && addExpression(MINIMAL_LENGTH, MINIMAL_LENGTH_SQL, Netezza, false);
        result = result
                && addExpression(MINIMAL_LENGTH_WITH_BLANK_AND_NULL, MINIMAL_LENGTH_WITH_BLANK_AND_NULL_SQL, Netezza, false);
        result = result && addExpression(MINIMAL_LENGTH_WITH_NULL, MINIMAL_LENGTH_WITH_NULL_SQL, Netezza, false);
        result = result && addExpression(BENFORD_LAW, BENFORD_LAW_SQL, Netezza, false);

        // next: the following indicators need to add default expression, and Netezza expression
        result = result && addExpression(SOUNDEX_LOW_FREQUENCY, SOUNDEX_LOW_FREQUENCY_DEFAULT, SQL, false);
        result = result && addExpression(SOUNDEX_FREQUENCY, SOUNDEX_FREQUENCY_DEFAULT, SQL, false);
        result = result && addExpression(SOUNDEX_LOW_FREQUENCY, SOUNDEX_LOW_FREQUENCY_SQL, Netezza, false);
        result = result && addExpression(SOUNDEX_FREQUENCY, SOUNDEX_FREQUENCY_SQL, Netezza, false);

        // for pattern frequency, also need to add character map(default, and Netezza)
        result = result && addExpression(PATTERN_LOW_FREQUENCY, PATTERN_LOW_FREQUENCY_DEFAULT, SQL, true);
        result = result && addExpression(PATTERN_FREQUENCY, PATTERN_FREQUENCY_DEFAULT, SQL, true);
        result = result && addExpression(PATTERN_LOW_FREQUENCY, PATTERN_LOW_FREQUENCY_SQL, Netezza, true);
        result = result && addExpression(PATTERN_FREQUENCY, PATTERN_FREQUENCY_SQL, Netezza, true);

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return result;
    }

    private boolean addExpression(String indicatorName, String body, String language, boolean withMap) {
        IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName(indicatorName);
        if (indiDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(indiDefinition, language)) {
            IndicatorDefinitionFileHelper.addSqlExpression(indiDefinition, language, body);
            if (withMap) {
                IndicatorDefinitionFileHelper.addCharacterMapping(indiDefinition, language, StringUtils.EMPTY, CHAR_TOREPLACE,
                        CHAR_REPLACE);
            }

            return IndicatorDefinitionFileHelper.save(indiDefinition);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2014, 02, 18);
    }

}
