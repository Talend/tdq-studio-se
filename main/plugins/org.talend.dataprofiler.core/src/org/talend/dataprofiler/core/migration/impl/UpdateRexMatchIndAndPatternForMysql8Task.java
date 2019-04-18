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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;

/**
 * Add a regex for Mysql 8,update pattern Complex_Australian_Phone_Number
 */
public class UpdateRexMatchIndAndPatternForMysql8Task extends AbstractWorksapceUpdateTask {

    protected String regulaExpMatchLabel = "Regular Expression Matching"; //$NON-NLS-1$

    private String indicatorMySqlEx8p =
            "SELECT COUNT(CASE WHEN REGEXP_LIKE(<%=__COLUMN_NAMES__%>,<%=__PATTERN_EXPR__%>,'c') THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // old pattern miss '\', it is :'^\\({0,1}((0|\\+61)(2|4|3|7|8)){0,1}\){0,1}(\ |-){0,1}[0-9]{2}(\ |-){0,1}[0-9]{2}(\
    // |-){0,1}[0-9]{1}(\ |-){0,1}[0-9]{3}$'
    private String newRegexForPattern =
            "'^\\\\({0,1}((0|\\\\+61)(2|4|3|7|8)){0,1}\\\\){0,1}(\\ |-){0,1}[0-9]{2}(\\ |-){0,1}[0-9]{2}(\\ |-){0,1}[0-9]{1}(\\ |-){0,1}[0-9]{3}$'";

    @Override
    public Date getOrder() {
        return createDate(2018, 9, 29);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean succuss = true;
        // Add an indicator for Mysql8
        IndicatorDefinition indicatorDefinition =
                DefinitionHandler.getInstance().getIndicatorDefinition(regulaExpMatchLabel);
        if (indicatorDefinition != null) {
            EList<TdExpression> sqlGenericExpression = indicatorDefinition.getSqlGenericExpression();
            boolean foundMysql8Expression = false;
            String version = getDatabaseVersion();
            if (sqlGenericExpression != null && !sqlGenericExpression.isEmpty()) {
                for (TdExpression expression : sqlGenericExpression) {
                    if (expression != null && getDataType().getLanguage().equals(expression.getLanguage())
                            && version.equals(expression.getVersion())) {
                        foundMysql8Expression = true;
                        break;
                    }
                }
            }
            if (!foundMysql8Expression) {
                TdExpression expressionForMysql8 = BooleanExpressionHelper
                        .createTdExpression(getDataType().getLanguage(), getIndicatorExpression(), version);
                indicatorDefinition.getSqlGenericExpression().add(expressionForMysql8);
                succuss = IndicatorDefinitionFileHelper.save(indicatorDefinition);
                DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
            }
        }
        if (!updatePattern()) {
            return succuss;
        }
        // update a pattern Complex_Australian_Phone_Number_0.1.pattern which miss a '\'
        IFolder folder = ResourceManager.getPatternRegexFolder().getFolder("phone"); //$NON-NLS-1$
        if (folder.exists()) {
            IFile patternFile = folder.getFile("Complex_Australian_Phone_Number_0.1.pattern"); //$NON-NLS-1$
            // only do it when the pattern has existed.
            if (patternFile.exists()) {
                Pattern pattern = PatternResourceFileHelper.getInstance().findPattern(patternFile);
                if (pattern != null) {
                    EList<PatternComponent> components = pattern.getComponents();
                    for (PatternComponent patternComponent : components) {
                        TdExpression expression = ((RegularExpression) patternComponent).getExpression();
                        String language = expression.getLanguage();
                        if (PatternLanguageType.MYSQL.getLiteral().equals(language)) {
                            expression.setBody(newRegexForPattern);
                            succuss = ElementWriterFactory.getInstance().createPatternWriter().save(pattern).isOk();
                        }
                    }
                }
            }

        }
        return succuss;

    }

    protected boolean updatePattern() {
        return true;
    }

    protected String getIndicatorExpression() {
        return indicatorMySqlEx8p;
    }

    protected String getDatabaseVersion() {
        return "8.0"; //$NON-NLS-1$
    }

    protected SupportDBUrlType getDataType() {
        return SupportDBUrlType.MYSQLDEFAULTURL;
    }
}
