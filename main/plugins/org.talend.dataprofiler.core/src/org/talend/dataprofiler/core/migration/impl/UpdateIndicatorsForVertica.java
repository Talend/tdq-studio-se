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

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

public class UpdateIndicatorsForVertica extends AbstractWorksapceUpdateTask {

    private final String patternMatchSQL = "SELECT COUNT(CASE WHEN REGEXP_LIKE(TO_CHAR(<%=__COLUMN_NAMES__%>),<%=__PATTERN_EXPR__%>) THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String patternFreqSQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) AS c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c DESC"; //$NON-NLS-1$

    private final String benFordSQL = "SELECT SUBSTR(TO_CHAR(<%=__COLUMN_NAMES__%>),1,1) , COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY SUBSTR(TO_CHAR(<%=__COLUMN_NAMES__%>),1,1) order by SUBSTR(TO_CHAR(<%=__COLUMN_NAMES__%>),1,1)"; //$NON-NLS-1$

    private final String lowPatternFreqSQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) AS c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c ASC"; //$NON-NLS-1$

    private final String patternMatchIndiDefId = "_yb-_8Dh8Ed2XmO7pl5Yuyg"; //$NON-NLS-1$

    private final String patternFreqIndiDefId = "_kQzTsJR-Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String lowPatternFreqIndiDefId = "_OCTbwJR_Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String benfordId = "_yRkFIezIEeG0fbygDv6UrQ"; //$NON-NLS-1$

    private final String charToReplace = "abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDEFGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789"; //$NON-NLS-1$

    private final String newReplaceChar = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999"; //$NON-NLS-1$

    private final String name = "Characters mapping on Vertica"; //$NON-NLS-1$

    private final String language = SupportDBUrlType.VERTICA.getLanguage();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2013, 8, 26);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /**
     * migrate these indicators for Vertica database:Pattern Frequency Table,Pattern Low Frequency Table,Regular
     * Expression Matching.
     */
    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        IndicatorDefinition patternMatchIndDef = definitionHandler.getDefinitionById(patternMatchIndiDefId);
        IndicatorDefinition patternFreqIndDef = definitionHandler.getDefinitionById(patternFreqIndiDefId);
        IndicatorDefinition patternLowPatternFreqIndDef = definitionHandler.getDefinitionById(lowPatternFreqIndiDefId);
        IndicatorDefinition benFordIndDef = definitionHandler.getDefinitionById(benfordId);
        boolean isNeedReload = false;
        if (patternMatchIndDef != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(patternMatchIndDef, language)) {
            IndicatorDefinitionFileHelper.addSqlExpression(patternMatchIndDef, language, patternMatchSQL);
            IndicatorDefinitionFileHelper.save(patternMatchIndDef);
            isNeedReload = true;
        }
        if (patternFreqIndDef != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(patternFreqIndDef, language)) {
            IndicatorDefinitionFileHelper.addSqlExpression(patternFreqIndDef, language, patternFreqSQL);
            IndicatorDefinitionFileHelper.addCharacterMapping(patternFreqIndDef, language, name, charToReplace, newReplaceChar);
            IndicatorDefinitionFileHelper.save(patternFreqIndDef);
            if (!isNeedReload) {
                isNeedReload = true;
            }
        }
        if (patternLowPatternFreqIndDef != null
                && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(patternLowPatternFreqIndDef, language)) {
            IndicatorDefinitionFileHelper.addSqlExpression(patternLowPatternFreqIndDef, language, lowPatternFreqSQL);
            IndicatorDefinitionFileHelper.addCharacterMapping(patternLowPatternFreqIndDef, language, name, charToReplace,
                    newReplaceChar);
            IndicatorDefinitionFileHelper.save(patternLowPatternFreqIndDef);
            if (!isNeedReload) {
                isNeedReload = true;
            }
        }

        if (benFordIndDef != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(benFordIndDef, language)) {
            IndicatorDefinitionFileHelper.addSqlExpression(benFordIndDef, language, benFordSQL);
            IndicatorDefinitionFileHelper.save(benFordIndDef);
            if (!isNeedReload) {
                isNeedReload = true;
            }
        }

        if (isNeedReload) {
            definitionHandler.reloadIndicatorsDefinitions();
        }
        return true;
    }

}
