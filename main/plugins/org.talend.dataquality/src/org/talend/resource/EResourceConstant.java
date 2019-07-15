// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.TDQItem;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorsDefinitions;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dataquality.properties.util.PropertiesSwitch;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.WhereRule;
import org.talend.designer.business.model.business.Context;

import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public enum EResourceConstant {

    // These three resource constant is compatible with version before 4.1.
    OLD_METADATA("TDQ_Metadata", "TDQ_Metadata", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    OLD_DB_CONNECTIONS("DB Connections", "TDQ_Metadata/DB Connections", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    OLD_MDM_CONNECTIONS("MDM Connections", "TDQ_Metadata/MDM Connections", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    FOLDER("", ""), //$NON-NLS-1$ $NON-NLS-2$
    DATA_PROFILING("TDQ_Data Profiling", "TDQ_Data Profiling", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    LIBRARIES("TDQ_Libraries", "TDQ_Libraries", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    METADATA("metadata", "metadata", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    ANALYSIS("Analyses", "TDQ_Data Profiling/Analyses", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    TEMP_MAPDB(".Work_MapDB", ".Work_MapDB", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    REPORTS("Reports", "TDQ_Data Profiling/Reports", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    EXCHANGE("Exchange", "TDQ_Libraries/Exchange", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    INDICATORS("Indicators", "TDQ_Libraries/Indicators", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    USER_DEFINED_INDICATORS("User Defined Indicators",//$NON-NLS-1$
                            "TDQ_Libraries/Indicators/User Defined Indicators",//$NON-NLS-1$
                            ResourceConstant.READONLY),
    // MOD zhsne 18724: Java UDI enhancements add lib folder under UDI folder.
    USER_DEFINED_INDICATORS_LIB("lib", "TDQ_Libraries/Indicators/User Defined Indicators/lib", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    SYSTEM_INDICATORS("System Indicators", "TDQ_Libraries/Indicators/System Indicators", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    SYSTEM_INDICATORS_ADVANCED_STATISTICS("Advanced Statistics",//$NON-NLS-1$
                                          "TDQ_Libraries/Indicators/System Indicators/Advanced Statistics",//$NON-NLS-1$
                                          ResourceConstant.READONLY),
    SYSTEM_INDICATORS_BUSINESS_RULES("Business Rules",//$NON-NLS-1$
                                     "TDQ_Libraries/Indicators/System Indicators/Business Rules",//$NON-NLS-1$
                                     ResourceConstant.READONLY),
    SYSTEM_INDICATORS_CORRELATION("Correlation",//$NON-NLS-1$
                                  "TDQ_Libraries/Indicators/System Indicators/Correlation",//$NON-NLS-1$
                                  ResourceConstant.READONLY),
    SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY("Functional Dependency",//$NON-NLS-1$
                                            "TDQ_Libraries/Indicators/System Indicators/Functional Dependency",//$NON-NLS-1$
                                            ResourceConstant.READONLY),
    SYSTEM_INDICATORS_OVERVIEW("Overview", "TDQ_Libraries/Indicators/System Indicators/Overview", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS("Pattern Frequency Statistics",//$NON-NLS-1$
                                                    "TDQ_Libraries/Indicators/System Indicators/Pattern Frequency Statistics",//$NON-NLS-1$
                                                    ResourceConstant.READONLY),
    SYSTEM_INDICATORS_PATTERN_MATCHING("Pattern Matching",//$NON-NLS-1$
                                       "TDQ_Libraries/Indicators/System Indicators/Pattern Matching",//$NON-NLS-1$
                                       ResourceConstant.READONLY),
    SYSTEM_INDICATORS_ROW_COMPARISON("Row Comparison",//$NON-NLS-1$
                                     "TDQ_Libraries/Indicators/System Indicators/Row Comparison",//$NON-NLS-1$
                                     ResourceConstant.READONLY),
    SYSTEM_INDICATORS_SIMPLE_STATISTICS("Simple Statistics",//$NON-NLS-1$
                                        "TDQ_Libraries/Indicators/System Indicators/Simple Statistics",//$NON-NLS-1$
                                        ResourceConstant.READONLY),
    SYSTEM_INDICATORS_SOUNDEX("Soundex", "TDQ_Libraries/Indicators/System Indicators/Soundex", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    SYSTEM_INDICATORS_SUMMARY_STATISTICS("Summary Statistics",//$NON-NLS-1$
                                         "TDQ_Libraries/Indicators/System Indicators/Summary Statistics",//$NON-NLS-1$
                                         ResourceConstant.READONLY),
    SYSTEM_INDICATORS_TEXT_STATISTICS("Text Statistics",//$NON-NLS-1$
                                      "TDQ_Libraries/Indicators/System Indicators/Text Statistics",//$NON-NLS-1$
                                      ResourceConstant.READONLY),
    SYSTEM_INDICATORS_PHONENUMBER_STATISTICS("Phone Number Statistics",//$NON-NLS-1$
                                             "TDQ_Libraries/Indicators/System Indicators/Phone Number Statistics",//$NON-NLS-1$
                                             ResourceConstant.READONLY),
    SYSTEM_INDICATORS_FRAUDDETECTION("Fraud Detection",//$NON-NLS-1$
                                     "TDQ_Libraries/Indicators/System Indicators/Fraud Detection", ResourceConstant.READONLY), //$NON-NLS-1$
    JRXML_TEMPLATE("JRXML Template", "TDQ_Libraries/JRXML Template", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    PATTERNS("Patterns", "TDQ_Libraries/Patterns", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    PATTERN_REGEX("Regex", "TDQ_Libraries/Patterns/Regex", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    PATTERN_SQL("SQL", "TDQ_Libraries/Patterns/SQL", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    RULES("Rules", "TDQ_Libraries/Rules", ResourceConstant.READONLY, ResourceConstant.NO_SUBFOLDER), //$NON-NLS-1$ $NON-NLS-2$
    RULES_SQL("SQL", "TDQ_Libraries/Rules/SQL", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    RULES_MATCHER("Match", "TDQ_Libraries/Rules/Match", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    RULES_PARSER("Parser", "TDQ_Libraries/Rules/Parser", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    SOURCE_FILES("Source Files", "TDQ_Libraries/Source Files", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    DB_CONNECTIONS("connections", "metadata/connections", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    MDM_CONNECTIONS("MDMconnections", "metadata/MDMconnections", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    FILEDELIMITED("fileDelimited", "metadata/fileDelimited", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$
    REPORTING_DB("TDQ_reporting_db", "REPORTING_DB", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    HADOOP_CLUSTER("hadoopcluster", "metadata/hadoop/hadoopcluster", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    REFERENCED_PROJECT("Referenced project", "Referenced project", ResourceConstant.READONLY), //$NON-NLS-1$ $NON-NLS-2$

    CONTEXT("context", "context", ResourceConstant.READONLY); //$NON-NLS-1$ $NON-NLS-2$

    private String name;

    private String path;

    private QualifiedName[] qualifications;

    EResourceConstant(String name, String path, QualifiedName... qualifications) {
        this.name = name;
        this.path = path;
        this.qualifications = qualifications;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for qualifications.
     *
     * @return the qualifications
     */
    public QualifiedName[] getQualifications() {
        return qualifications;
    }

    /**
     * DOC bzhou Comment method "getPathes".
     *
     * @return
     */
    public static IPath[] getPathes() {
        List<IPath> allPathes = new ArrayList<IPath>();

        for (EResourceConstant constant : values()) {
            if (constant != FOLDER) {
                allPathes.add(new Path(constant.getPath()));
            }
        }

        return allPathes.toArray(new Path[allPathes.size()]);
    }

    /**
     *
     * This method is to find the matched Qualifications constant by path.
     *
     * @param path
     * @return
     */
    public static QualifiedName[] findQualificationsByPath(String path) {
        for (EResourceConstant constant : values()) {
            if (StringUtils.equals(path, constant.getPath())) {
                return constant.getQualifications();
            }
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "getTypedConstantList".
     *
     * @return
     */
    public static List<EResourceConstant> getTypedConstantList() {
        List<EResourceConstant> constantList = new ArrayList<EResourceConstant>();

        constantList.add(ANALYSIS);
        constantList.add(REPORTS);
        constantList.add(DB_CONNECTIONS);
        // constantList.add(MDM_CONNECTIONS);
        constantList.add(RULES_SQL);
        constantList.add(INDICATORS);
        constantList.add(JRXML_TEMPLATE);
        constantList.add(PATTERNS);
        constantList.add(SOURCE_FILES);
        constantList.add(CONTEXT);

        return constantList;
    }

    /**
     * DOC bZhou Comment method "getTypedConstant".
     *
     * @param element
     * @return
     */
    public static EResourceConstant getTypedConstant(ModelElement element) {

        EResourceConstant constatnt = null;

        if (element instanceof Analysis) {
            constatnt = ANALYSIS;
        } else if (element instanceof Report) {
            constatnt = REPORTS;
        } else if (element instanceof IndicatorDefinition) {
            if (element instanceof WhereRule) {
                constatnt = RULES_SQL;
            } else if (element instanceof MatchRuleDefinition) {
                constatnt = RULES_MATCHER;
            } else if (element instanceof ParserRule) {
                constatnt = RULES_PARSER;
            } else {
                constatnt = USER_DEFINED_INDICATORS;
            }
        } else if (element instanceof IndicatorsDefinitions) {
            constatnt = LIBRARIES;
        } else if (element instanceof Pattern) {
            Pattern pattern = (Pattern) element;
            String expressionType = DomainHelper.getExpressionType(pattern);
            boolean isSQLPattern = (ExpressionType.SQL_LIKE.getLiteral().equals(expressionType));
            if (isSQLPattern) {
                constatnt = PATTERN_SQL;
            } else {
                constatnt = PATTERN_REGEX;
            }
        } else if (element instanceof DatabaseConnection) {
            constatnt = DB_CONNECTIONS;
        } else if (element instanceof Context) {
            constatnt = CONTEXT;
        }

        return constatnt;
    }

    /**
     * DOC bZhou Comment method "getTypedConstant".
     *
     * @param item
     * @return
     */
    public static EResourceConstant getTypedConstant(Item item) {
        Object object = new org.talend.core.model.properties.util.PropertiesSwitch() {

            @Override
            public Object caseDatabaseConnectionItem(DatabaseConnectionItem object) {
                return DB_CONNECTIONS;
            }

            @Override
            public Object caseMDMConnectionItem(MDMConnectionItem object) {
                return MDM_CONNECTIONS;
            }

            @Override
            public Object caseFolderItem(FolderItem object) {
                return FOLDER;
            }

            @Override
            public Object caseDelimitedFileConnectionItem(DelimitedFileConnectionItem object) {
                return FILEDELIMITED;
            }

            @Override
            public Object caseContextItem(ContextItem object) {
                return CONTEXT;
            }

        }.doSwitch(item);

        if (object == null) {
            object = new PropertiesSwitch<Object>() {

                @Override
                public Object caseTDQReportItem(TDQReportItem object) {
                    return REPORTS;
                }

                @Override
                public Object caseTDQAnalysisItem(TDQAnalysisItem object) {
                    return ANALYSIS;
                }

                @Override
                public Object caseTDQBusinessRuleItem(TDQBusinessRuleItem object) {
                    DQRule rule = object.getDqrule();
                    if (rule != null && rule instanceof ParserRule) { // dqrule
                        return RULES_PARSER;
                    }
                    return RULES_SQL;
                }

                @Override
                public Object caseTDQIndicatorDefinitionItem(TDQIndicatorDefinitionItem object) {
                    return INDICATORS;
                }

                @Override
                public Object caseTDQPatternItem(TDQPatternItem object) {
                    return PATTERNS;
                }

                @Override
                public Object caseTDQJrxmlItem(TDQJrxmlItem object) {
                    return JRXML_TEMPLATE;
                }

                @Override
                public Object caseTDQSourceFileItem(TDQSourceFileItem object) {
                    return SOURCE_FILES;
                }

                /*
                 * (non-Javadoc)
                 *
                 * @see
                 * org.talend.dataquality.properties.util.PropertiesSwitch#caseTDQMatchRuleItem(org.talend.dataquality
                 * .properties.TDQMatchRuleItem)
                 */
                @Override
                public Object caseTDQMatchRuleItem(TDQMatchRuleItem object) {
                    return RULES_MATCHER;
                }

            }.doSwitch(item);
        }

        if (object == null && item instanceof TDQItem) {
            final String filename = ((TDQItem) item).getFilename();

            EResourceConstant constatnt = null;
            if (filename != null) {
                if (filename.endsWith(FactoriesUtil.ANA)) {
                    constatnt = ANALYSIS;
                } else if (filename.endsWith(FactoriesUtil.REP)) {
                    constatnt = REPORTS;
                } else if (filename.endsWith(FactoriesUtil.DQRULE)) {
                    constatnt = RULES_SQL;
                } else if (filename.endsWith(FactoriesUtil.DEFINITION)) {
                    constatnt = INDICATORS;
                } else if (filename.endsWith(FactoriesUtil.JRXML)) {
                    constatnt = JRXML_TEMPLATE;
                } else if (filename.endsWith(FactoriesUtil.PATTERN)) {
                    constatnt = PATTERNS;
                } else if (filename.endsWith(FactoriesUtil.SQL)) {
                    constatnt = SOURCE_FILES;
                }
            }

            object = constatnt;
        }
        return object != null ? (EResourceConstant) object : null;
    }

    /**
     * DOC bZhou Comment method "isMDMConnection".
     *
     * @param provider
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean isMDMConnection(DataProvider provider) {
        return SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(provider) == null ? false : true;
    }

    /**
     *
     * This mehtod is to get the top level folder constant of TOP.
     *
     * @return An array of EResourceConstant.
     */
    public static EResourceConstant[] getTopConstants() {
        return new EResourceConstant[] { DATA_PROFILING, LIBRARIES, METADATA };
    }

    public static EResourceConstant[] getTdqConstants() {
        return new EResourceConstant[] { DATA_PROFILING, LIBRARIES, METADATA, CONTEXT };
    }

    public static EResourceConstant[] getReferenceNeededConstants() {
        return new EResourceConstant[] { ANALYSIS, REPORTS, USER_DEFINED_INDICATORS, USER_DEFINED_INDICATORS_LIB,
                DB_CONNECTIONS, FILEDELIMITED, SYSTEM_INDICATORS_SIMPLE_STATISTICS,
                SYSTEM_INDICATORS_ADVANCED_STATISTICS, SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS,
                SYSTEM_INDICATORS_PATTERN_MATCHING, SYSTEM_INDICATORS_PHONENUMBER_STATISTICS, SYSTEM_INDICATORS_SOUNDEX,
                SYSTEM_INDICATORS_SUMMARY_STATISTICS, SYSTEM_INDICATORS_TEXT_STATISTICS, SYSTEM_INDICATORS_CORRELATION,
                SYSTEM_INDICATORS_ROW_COMPARISON, SYSTEM_INDICATORS_BUSINESS_RULES, SYSTEM_INDICATORS_FRAUDDETECTION,
                RULES_PARSER, RULES_SQL, RULES_MATCHER, PATTERN_REGEX, PATTERN_SQL, JRXML_TEMPLATE, SOURCE_FILES,
                CONTEXT };
    }

    /**
     * DOC bZhou Comment method "getTopConstantIndexFromPath".
     *
     * @param inputPath
     * @return
     */
    public static int getTopConstantIndexFromPath(IPath inputPath) {
        int constantIndex = 0;
        for (int i = 0; i < inputPath.segmentCount(); i++) {
            String segment = inputPath.segment(i);

            for (EResourceConstant topConstant : EResourceConstant.getTopConstants()) {
                if (topConstant.getPath().equals(segment)) {
                    constantIndex = i;
                    break;
                }
            }

            if (constantIndex != 0) {
                break;
            }
        }

        return constantIndex;
    }

    /**
     *
     * This method is to see whether the path is a current workspace constant path.
     *
     * @param path
     * @return true if it is, otherwise false.
     */
    public static boolean isConstantPath(IPath path) {
        if (path != null) {
            for (IPath tmp : getPathes()) {
                if (tmp.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }
}
