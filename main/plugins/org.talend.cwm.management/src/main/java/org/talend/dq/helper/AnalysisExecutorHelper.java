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
package org.talend.dq.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.StringUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.fileprocess.FileInputDelimited;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentPackage;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC yyin class global comment. Detailled comment
 */
public final class AnalysisExecutorHelper {

    private static Logger log = Logger.getLogger(AnalysisExecutorHelper.class);

    private static ITDQRepositoryService tdqRepositoryService = null;

    public static ITDQRepositoryService getTDQService() {
        if (tdqRepositoryService == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                tdqRepositoryService = (ITDQRepositoryService) org.talend.core.GlobalServiceRegister.getDefault().getService(
                        ITDQRepositoryService.class);
            }
        }
        return tdqRepositoryService;
    }

    /**
     * get full name as: db.catalog.table, if has catalog/schema
     * 
     * @param analyzedElement only for TdColumn and ColumnSet.
     * @param dbmsLanguage
     * @return
     * @deprecated instead of it by {@link DbmsLanguage#getQueryColumnSetWithPrefix(ColumnSet) or
     * DbmsLanguage#getQueryColumnSetWithPrefix(TdColumn)}
     */
    @Deprecated
    public static String getTableName(ModelElement analyzedElement, DbmsLanguage dbmsLanguage) {
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analyzedElement);
        if (tdColumn != null) {
            return dbmsLanguage.getQueryColumnSetWithPrefix(tdColumn);
        }
        ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(analyzedElement);
        if (columnSet != null) {
            return dbmsLanguage.getQueryColumnSetWithPrefix(columnSet);
        }
        log.error(Messages.getString("AnalysisExecutorHelper.TableEmpty")); //$NON-NLS-1$
        return PluginConstant.EMPTY_STRING;
    }

    /**
     * 
     * DOC qiongli Comment method "findColumnSetOwner".
     * 
     * @param column
     * @return
     * @deprecated instead of it by {@link ColumnHelper#getColumnOwnerAsColumnSet(ModelElement)}
     */
    @Deprecated
    public static ModelElement findColumnSetOwner(ModelElement column) {
        EObject owner = column.eContainer();
        ColumnSet set = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(owner);
        MetadataTable mdColumn = SwitchHelpers.METADATA_TABLE_SWITCH.doSwitch(owner);

        if (null == set && mdColumn != null) {
            return mdColumn;
        } else if (null != set) {
            return set;
        }
        return column;
    }

    public static String getQuotedCatalogName(ModelElement analyzedElement, DbmsLanguage dbmsLanguage) {
        final Catalog parentCatalog = CatalogHelper.getParentCatalog(analyzedElement);
        return parentCatalog == null ? null : dbmsLanguage.quote(parentCatalog.getName());
    }

    public static String getQuotedSchemaName(ModelElement columnSetOwner, DbmsLanguage dbmsLanguage) {
        final Schema parentSchema = SchemaHelper.getParentSchema(columnSetOwner);
        return (parentSchema == null) ? null : dbmsLanguage.quote(parentSchema.getName());
    }

    public static FileInputDelimited createFileInputDelimited(DelimitedFileConnection delimitedFileconnection) throws IOException {
        return createFileInputDelimited(delimitedFileconnection, Integer.MAX_VALUE);
    }

    public static FileInputDelimited createFileInputDelimited(DelimitedFileConnection delimitedFileconnection, int limit)
            throws IOException {
        String rowSeparator = JavaSqlFactory.getRowSeparatorValue(delimitedFileconnection);
        String encoding = JavaSqlFactory.getEncoding(delimitedFileconnection);
        String fieldSeparatorValue = JavaSqlFactory.getFieldSeparatorValue(delimitedFileconnection);

        boolean isSpliteRecord = delimitedFileconnection.isSplitRecord();
        boolean isSkipeEmptyRow = delimitedFileconnection.isRemoveEmptyRow();
        String languageName = LanguageManager.getCurrentLanguage().getName();

        int limitValue = JavaSqlFactory.getLimitValue(delimitedFileconnection);
        if (limitValue <= 0) {
            limitValue = limit;
        }
        int headValue = JavaSqlFactory.getHeadValue(delimitedFileconnection);
        int footValue = JavaSqlFactory.getFooterValue(delimitedFileconnection);
        String path = JavaSqlFactory.getURL(delimitedFileconnection);

        return new FileInputDelimited(ParameterUtil.trimParameter(path), ParameterUtil.trimParameter(encoding),
                ParameterUtil.trimParameter(StringUtils.loadConvert(fieldSeparatorValue, languageName)),
                ParameterUtil.trimParameter(StringUtils.loadConvert(rowSeparator, languageName)), isSkipeEmptyRow, headValue,
                footValue, limitValue < limit ? limitValue : limit, -1, isSpliteRecord);
    }

    /**
     * Method "check" checks that the analysis can be run.
     * 
     * @param analysis the analysis to prepare
     * @return true if ok.
     */
    public static ReturnCode check(Analysis analysis) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);

        // --- check existence of context
        AnalysisContext context = analysis.getContext();
        if (context == null) {
            rc.setMessage(Messages.getString("AnalysisExecutor.ContextNull", analysis.getName())); //$NON-NLS-1$
            rc.setOk(Boolean.FALSE);
            return rc;
        }
        // --- check that there exists at least on element to analyze
        if (context.getAnalysedElements().size() == 0) {
            rc.setMessage(Messages.getString("ColumnAnalysisExecutor.AnalysisHaveAtLeastOneColumn")); //$NON-NLS-1$
            rc.setOk(Boolean.FALSE);
            return rc;
        }
        // --- check that the connection has been set
        DataManager connection = context.getConnection();
        if (connection == null) {
            rc.setMessage(Messages.getString("AnalysisExecutor.NoConnectionFound", analysis.getName())); //$NON-NLS-1$
            rc.setOk(Boolean.FALSE);
            return rc;
        }
        if (log.isInfoEnabled()) {
            if (SoftwaredeploymentPackage.eINSTANCE.getDataProvider().isInstance(connection)) {
                // MOD 20130225 TDQ-6632 the name of the item should be given (not the pathname)
                log.info(Messages.getString("AnalysisExecutor.CONNECTIONTO", connection.getName()));//$NON-NLS-1$
            }
        }

        AnalysisResult results = analysis.getResults();
        if (results == null) {
            rc.setMessage(Messages.getString("AnalysisExecutor.AnalysisnotNotPrepareCorrect", analysis.getName())); //$NON-NLS-1$
            rc.setOk(Boolean.FALSE);
            return rc;
        }

        // --- check the the dependeny files are exists ADDED mzhao TDQ-10428---
        rc = checkDependentFiles(analysis);

        return rc;
    }

    /**
     * 
     * Check the dependent file's existance. <br>
     * 1. If exist, do "hot" content copy from dependent file to built-in. <br>
     * 2. If not exist 1) built-in content is not empty, do nothing, 2) built-in content is empty, ReturnCode = false
     * and return. <br>
     * 3. Load indicator from built-in content.
     * 
     * @param analysis
     * @return
     */
    private static ReturnCode checkDependentFiles(Analysis analysis) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        List<Indicator> indicators = analysis.getResults().getIndicators();
        if (indicators.size() == 0) {
            rc.setOk(false);
            rc.setMessage(Messages.getString("AnalysisExecutor.AnalysisNoIndicators", analysis.getName())); //$NON-NLS-1$
            return rc;
        }
        // Loop indicators , check the dependeny file's existence.
        for (Indicator indicator : indicators) {
            if (indicator.getBuiltInIndicatorDefinition() != null) {
                // Built-in indicator already exist.
                continue;
            }
            // check pattern matching indicator
            rc = checkPatternMatchingIndicator(indicator);
            if (!rc.isOk()) {
                break;
            }
            // Check Indicators
            rc = checkIndicator(indicator);
            if (!rc.isOk()) {
                break;
            }

        }
        return rc;
    }

    /**
     * DOC zhao Comment method "checkUserDefineIndicator".
     * 
     * @param indicator
     * @return
     */
    private static ReturnCode checkIndicator(Indicator indicator) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        if (indicator instanceof PatternMatchingIndicator) {
            return rc; // Won't check if the indicator is pattern matching indicator. The rest
                       // (including UDI ) can be
                       // checked in this method.
        }
        if (indicator instanceof CompositeIndicator) {
            List<Indicator> allChildIndicators = ((CompositeIndicator) indicator).getAllChildIndicators();
            for (Indicator ind : allChildIndicators) {
                rc = checkIndicatorWithChild(ind); // Check indicators with children.
                if (!rc.isOk()) {
                    return rc;
                }
            }
        } else {
            rc = checkIndicatorWithChild(indicator);
        }
        return rc;
    }

    /**
     * DOC zhao Comment method "checkIndicatorWithChild".
     * 
     * @param indicator
     * @return
     */
    private static ReturnCode checkIndicatorWithChild(Indicator indicator) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        if (indicator.getBuiltInIndicatorDefinition() != null) {
            return rc;
        }
        // Get indicator definition from dependent file
        IndicatorDefinition dependentDefinition = indicator.getIndicatorDefinition();
        if (isDependentFileExist(dependentDefinition)) {
            IndicatorDefinition deepCopiedDefinition;
            if (dependentDefinition instanceof WhereRule) {
                deepCopiedDefinition = copyWhereRule((WhereRule) dependentDefinition);
            } else {
                deepCopiedDefinition = EObjectHelper.deepCopy(dependentDefinition);
            }
            // Hot copy to built-in definition.
            deepCopiedDefinition.getSupplierDependency().clear();
            // TDQ-10737 Because 'BuiltInIndicatorDefinition' is a containment reference, need to Clear these 3 non-containment
            // reference list
            deepCopiedDefinition.getCategories().clear();
            deepCopiedDefinition.getAggregatedDefinitions().clear();
            deepCopiedDefinition.getSubCategories().clear();
            indicator.setBuiltInIndicatorDefinition(deepCopiedDefinition);
            EMFUtil.saveResource(indicator.eResource());
        } else {
            IndicatorDefinition builtInDefinition = indicator.getBuiltInIndicatorDefinition();
            if (builtInDefinition == null) {
                rc.setMessage(Messages.getString("AnalysisExecutor.BuiltInNoIndicators")); //$NON-NLS-1$
                rc.setOk(false);
                return rc;
            } else {
                indicator.setIndicatorDefinition(null);
            }
        }
        return rc;
    }

    /**
     * When deep copy, if the where rule contains some joins, it will copy all related tables(extended in related db),
     * but this is not what we want, so we use a temp list to store the joins, and then clear the joins before deep copy
     * to avoid copy many useless things, and restore the joins after deep copy.
     * 
     * @param dependentDefinition
     */
    private static IndicatorDefinition copyWhereRule(WhereRule dependentDefinition) {
        // firstly clear the dependency of the analysis
        dependentDefinition.getSupplierDependency().clear();
        // then , record the joins in a temp list
        EList<JoinElement> joins = dependentDefinition.getJoins();
        List<JoinElement> copyJoins = new ArrayList<JoinElement>();
        if (!joins.isEmpty()) {
            for (JoinElement element : joins) {
                copyJoins.add(element);
            }
            dependentDefinition.getJoins().clear();
        }
        IndicatorDefinition deepCopiedDefinition = EObjectHelper.deepCopy(dependentDefinition);
        // after deep copy, restore the joins.
        if (!copyJoins.isEmpty()) {
            ((WhereRule) deepCopiedDefinition).getJoins().addAll(copyJoins);
            dependentDefinition.getJoins().addAll(copyJoins);
        }
        return deepCopiedDefinition;
    }

    /**
     * Check pattern matching indicator
     * 
     * @param indicator
     * @return
     */
    private static ReturnCode checkPatternMatchingIndicator(Indicator indicator) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        if (!(indicator instanceof PatternMatchingIndicator)) {
            return rc; // Won't check if the indicator is not pattern matching indicator.
        }
        if (indicator instanceof AllMatchIndicator) {
            List<RegexpMatchingIndicator> compositeIndicators = ((AllMatchIndicator) indicator)
                    .getCompositeRegexMatchingIndicators();
            for (Indicator ind : compositeIndicators) {
                rc = checkMatchingIndicator(ind); // Pattern matching & all match indicator from column set analysis.
                if (!rc.isOk()) {
                    return rc;
                }
            }
        } else {
            rc = checkMatchingIndicator(indicator);
        }

        return rc;
    }

    /**
     * DOC zhao Comment method "checkMatchingIndicator".
     * 
     * @param indicator
     * @return
     */
    private static ReturnCode checkMatchingIndicator(Indicator indicator) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);
        Domain domain = indicator.getParameters().getDataValidDomain();
        if (!domain.getBuiltInPatterns().isEmpty()) {
            return rc;
        }
        List<Pattern> patterns = domain.getPatterns();
        // check pattern matching indicator files' existence.
        if (!patterns.isEmpty() && isDependentFileExist(patterns.toArray(new Pattern[patterns.size()]))) {
            // Hot copy the pattern from separate file into built in.
            hotCopyPatterns(indicator, patterns);
        } else {
            List<Pattern> builtInPatterns = indicator.getParameters().getDataValidDomain().getBuiltInPatterns();
            if (builtInPatterns.isEmpty()) {
                rc.setMessage(Messages.getString("AnalysisExecutor.BuiltInNoPatterns")); //$NON-NLS-1$
                rc.setOk(false);
                return rc;
            } else {
                // Use built-in pattern instead.
                patterns.clear();

            }
        }
        return rc;
    }

    /**
     * See if the dependent file existed or not.
     * 
     * @param modelElements
     * @return
     */
    private static boolean isDependentFileExist(ModelElement... modelElements) {
        for (ModelElement me : modelElements) {
            if (me == null || me.eIsProxy() || me.getName() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * DOC zhao Comment method "hotCopyPatterns".
     * 
     * @param indicator
     * @param patterns
     */
    private static void hotCopyPatterns(Indicator indicator, List<Pattern> patterns) {
        Set<Pattern> deepCopiedPatterns = new HashSet<Pattern>();
        for (Pattern pattern : patterns) {
            Pattern deepCopiedPattern = EObjectHelper.deepCopy(pattern);
            deepCopiedPattern.getSupplierDependency().clear();
            deepCopiedPatterns.add(deepCopiedPattern);
        }
        indicator.getParameters().getDataValidDomain().getBuiltInPatterns().clear();
        indicator.getParameters().getDataValidDomain().getBuiltInPatterns().addAll(deepCopiedPatterns);
        EMFUtil.saveResource(indicator.eResource());
    }

    public static long setExecutionDateInAnalysisResult(Analysis analysis) {
        final ExecutionInformations resultMetadata = analysis.getResults().getResultMetadata();
        final long startime = System.currentTimeMillis();
        resultMetadata.setExecutionDate(new Date(startime));
        return startime;
    }

    /**
     * set the execution info of analysis.
     * 
     * @param analysis - which need to update the execution number
     * @param isRunAnaResultok - the running result of the analysis is ok or not
     * @param errorMessage - the error message
     */
    public static void setExecutionInfoInAnalysisResult(Analysis analysis, boolean isRunAnaResultok, String errorMessage) {
        final ExecutionInformations resultMetadata = analysis.getResults().getResultMetadata();
        resultMetadata.setLastRunOk(isRunAnaResultok);
        resultMetadata.setMessage(errorMessage);
        resultMetadata.setExecutionNumber(resultMetadata.getExecutionNumber() + 1);
        if (isRunAnaResultok) {
            resultMetadata.setLastExecutionNumberOk(resultMetadata.getLastExecutionNumberOk() + 1);
        }
    }

    /**
     * get the correct indicator name.
     * 
     * @param indicator
     * @return
     */
    public static String getIndicatorName(Indicator indicator) {
        String indName = PluginConstant.EMPTY_STRING;
        if (indicator != null) {
            // set the default indicator name.
            indName = org.apache.commons.lang.StringUtils.defaultString(indicator.getName());
            // TDQ-11831: fix the indicator drill down open sql editor to use the correct indicator name.after we
            // change "Frenquency table" to "Value frenquency"
            if (indicator instanceof PatternMatchingIndicator || indicator instanceof UserDefIndicator) {
                // do nothing here, just use the default indicator name.
            } else {
                Property property = PropertyHelper.getProperty(indicator.getIndicatorDefinition());
                if (property != null) {
                    indName = InternationalizationUtil.getDefinitionInternationalizationLabel(property);
                }
            }
        }
        return indName;
    }
}
