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
package org.talend.dataprofiler.core.migration.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * @author yyi
 * 
 * This class helps to update the splited indicator files.
 */
public final class IndicatorDefinitionFileHelper {

    private static Logger log = Logger.getLogger(IndicatorDefinitionFileHelper.class);

    private IndicatorDefinitionFileHelper() {
    }

    /**
     * Get system indiactor by name.
     * 
     * @param name
     * @return IndicatorDefinition with the same name, null if the indiactor not find
     */
    public static IndicatorDefinition getSystemIndicatorByName(String name) {
        return DefinitionHandler.getInstance().getIndicatorDefinition(name);
    }

    /**
     * Add a sql expression for indicator.
     * 
     * @param definition
     * @param language
     * @param body
     * @return true if sql expression be added.
     */
    public static boolean addSqlExpression(IndicatorDefinition definition, String language, String body) {
        if (null == definition) {
            return false;
        }

        TdExpression e = BooleanExpressionHelper.createTdExpression(language, body);
        return definition.getSqlGenericExpression().add(e);
    }

    /**
     * Add a sql expression for indicator.
     * 
     * @param definition the definition of indicator
     * @param language the type of database
     * @param body the body of expression
     * @param modifyDate the time of the expression be modify
     * @param version the version of database
     * @return true if sql expression be added.
     */
    public static boolean addSqlExpressionTakeVersion(IndicatorDefinition definition, String language, String body,
            String modifyDate, String version) {
        if (null == definition) {
            return false;
        }

        TdExpression e = BooleanExpressionHelper.createTdExpression(language, body, version);
        e.setModificationDate(modifyDate);
        return definition.getSqlGenericExpression().add(e);
    }

    /**
     * keep the modify date
     * 
     * @param definition
     * @param language
     * @param body
     * @param modifyDate
     * @return
     */
    public static boolean addSqlExpression(IndicatorDefinition definition, String language, String body,
            String modifyDate) {
        return addSqlExpressionTakeVersion(definition, language, body, modifyDate, null);
    }

    /**
     * Add a sql expression for indicator.
     * 
     * @param definition
     * @param name
     * @param language
     * @param body
     * @param modifyDate
     * @return
     */
    public static boolean addSqlExpression(IndicatorDefinition definition, String name, String language, String body,
            String modifyDate) {
        if (null == definition) {
            return false;
        }

        TdExpression e = BooleanExpressionHelper.createTdExpression(language, body);
        e.setName(name);
        e.setModificationDate(modifyDate);
        return definition.getSqlGenericExpression().add(e);
    }

    /**
     * remove a sql expression for indicator.
     * 
     * @param definition
     * @param language
     * @param body
     * @return true if sql expression be removed.
     */
    public static boolean removeSqlExpression(IndicatorDefinition definition, String language, String... version) {
        if (null == definition) {
            return false;
        }
        TdExpression removeExpression = null;
        for (TdExpression e : definition.getSqlGenericExpression()) {
            if (findTheExpression(e, language, version)) {
                removeExpression = e;
                break;
            }
        }
        if (removeExpression != null) {
            return definition.getSqlGenericExpression().remove(removeExpression);
        }
        return false;

    }

    private static boolean findTheExpression(TdExpression e, String language, String[] version) {
        if (!e.getLanguage().equals(language)) {
            return false;
        }
        if (version == null || version[0] == null) {
            return e.getVersion() == null;
        } else {
            return version[0].equals(e.getVersion());
        }
    }

    /**
     * remove a sql expression by name.
     * 
     * @param definition
     * @param name
     * @return
     */
    public static boolean removeSqlExpressionByName(IndicatorDefinition definition, String name) {
        if (null == definition || null == name) {
            return false;
        }
        TdExpression removeExpression = null;
        for (TdExpression e : definition.getSqlGenericExpression()) {
            if (name.equals(e.getName())) {
                removeExpression = e;
            }
        }
        if (removeExpression != null) {
            return definition.getSqlGenericExpression().remove(removeExpression);
        }
        return false;

    }

    /**
     * Save an indicator.
     * 
     * @param definition
     * @return true if indicator saved.
     */
    public static boolean save(IndicatorDefinition definition) {
        return IndicatorResourceFileHelper.getInstance().save(definition).isOk();
    }

    /**
     * 
     * judge if exist a sq expression with specify language.
     * 
     * @param definition
     * @param language
     * @return
     */
    public static boolean isExistSqlExprWithLanguage(IndicatorDefinition definition, String language) {
        if (null == definition) {
            return false;
        }
        for (TdExpression e : definition.getSqlGenericExpression()) {
            if (e.getLanguage().equals(language)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExistCharacterMappingWithLanguage(IndicatorDefinition definition, String language) {
        if (null == definition) {
            return false;
        }
        for (CharactersMapping e : definition.getCharactersMapping()) {
            if (e.getLanguage().equals(language)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Added yyin TDQ-6099 20121227
     * 
     * @param definition
     * @param newLanguage
     * @param newCharactersToReplace
     * @param newReplacementCharacters
     * @return
     */
    public static boolean addCharacterMapping(IndicatorDefinition definition, String newLanguage, String name,
            String newCharactersToReplace, String newReplacementCharacters) {
        if (null == definition) {
            return false;
        }
        CharactersMapping newMapping = DefinitionFactory.eINSTANCE.createCharactersMapping();
        newMapping.setLanguage(newLanguage);
        newMapping.setCharactersToReplace(newCharactersToReplace);
        newMapping.setReplacementCharacters(newReplacementCharacters);
        newMapping.setName(name);

        return definition.getCharactersMapping().add(newMapping);
    }

    /**
     * find the IndicatorDefinition by the uuid in current project, if the IndicatorDefinition exist and don't include
     * any sql and java template and the AggregatedDefinitions is not empty then return true, else return false.
     * 
     * @param uuid the IndicatorDefinition's uuid
     * @return
     */
    private static boolean isSubCategoryIndicator(String uuid) {
        if (!StringUtils.isEmpty(uuid)) {
            IndicatorDefinition ind = DefinitionHandler.getInstance().getDefinitionById(uuid);
            if (ind != null) {
                return !UDIHelper.containsJavaUDI(ind) && ind.getSqlGenericExpression().isEmpty()
                        && !ind.getAggregatedDefinitions().isEmpty();
            }
        }
        return false;
    }

    /**
     * if the Indicator is TableOverview/ViewOverview/DatePatternFrequencyTable/....etc return true, else return false.
     * these indicators don't include any sql and java template and the AggregatedDefinitions is empty also, and don't
     * show them in the DQRepositoryView and import/export wizard.<br>
     * Technical indicators are not need to be displayed on UI.
     * 
     * @param uuid
     * @return
     */
    public static boolean isTechnialIndicator(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error(DefaultMessagesImpl.getString("IndicatorDefinitionFileHelper.NULL_UUID")); //$NON-NLS-1$
            return Boolean.FALSE;
        }
        String tableOverviewUuid = "_hgO7YMYUEd27NP4lvE0A4w"; //$NON-NLS-1$
        String viewOverviewUuid = "_lNIE0MbNEd2d_JPxxDRSfQ"; //$NON-NLS-1$
        String datePatternFrequencyTableUuid = "_OCTbwJR_Ed2XO-JvLwVAaa"; //$NON-NLS-1$
        String sumUuid = "_ccJgAhF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
        String ciWordPatternFrequencyUuid = "_frqV0D1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String ciWordPatternLowFrequencyUuid = "_kvvTsT1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String csWordPatternFrequencyUuid = "_UhvbED1SEeieEt49TD3y_Q"; //$NON-NLS-1$
        String csWordPatternLowFrequencyUuid = "_Z1RKMD1SEeieEt49TD3y_Q"; //$NON-NLS-1$

        boolean isTechUUID = tableOverviewUuid.compareTo(uuid) == 0 || viewOverviewUuid.compareTo(uuid) == 0
                || datePatternFrequencyTableUuid.compareTo(uuid) == 0 || sumUuid.compareTo(uuid) == 0
                || ciWordPatternFrequencyUuid.compareTo(uuid) == 0 || ciWordPatternLowFrequencyUuid.compareTo(uuid) == 0
                || csWordPatternFrequencyUuid.compareTo(uuid) == 0
                || csWordPatternLowFrequencyUuid.compareTo(uuid) == 0;

        return isTechUUID || isSubCategoryIndicator(uuid);
    }

    /**
     * 
     * update sql expression by language.
     * 
     * @param definition
     * @param language
     * @param newBody
     * @return
     */
    public static boolean updateSqlExpression(IndicatorDefinition definition, String language, String newBody) {
        if (null == definition) {
            return false;
        }
        for (TdExpression tdExp : definition.getSqlGenericExpression()) {
            if (tdExp.getLanguage().equals(language)) {
                tdExp.setBody(newBody);
                break;
            }
        }
        return true;
    }
}
