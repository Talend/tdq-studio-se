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

import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.softwaredeployment.util.SoftwaredeploymentSwitch;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.util.XmlSwitch;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.util.AnalysisSwitch;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.util.PatternSwitch;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.util.DefinitionSwitch;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.indicators.sql.util.IndicatorSqlSwitch;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.util.RulesSwitch;
import orgomg.cwm.foundation.softwaredeployment.SoftwareSystem;
import orgomg.cwmx.analysis.informationreporting.Report;
import orgomg.cwmx.analysis.informationreporting.util.InformationreportingSwitch;

/**
 *
 * This class is a utility class to decide what's the real type of a model element.
 *
 * Designed by zqin, 2010-6-25
 */
public final class ModelElementIdentifier {

    private ModelElementIdentifier() {
        //
    }

    public static final AnalysisSwitch<Analysis> ANALYSIS = new AnalysisSwitch<Analysis>() {

        @Override
        public Analysis caseAnalysis(Analysis object) {
            return object;
        };
    };

    public static final InformationreportingSwitch<Report> REPORT = new InformationreportingSwitch<Report>() {

        @Override
        public Report caseReport(Report object) {
            return object;
        };
    };

    public static final PatternSwitch<Pattern> PATTERN = new PatternSwitch<Pattern>() {

        @Override
        public Pattern casePattern(Pattern object) {
            return object;
        };
    };

    public static final DefinitionSwitch<IndicatorDefinition> INDICATOR_DEFINITION = new DefinitionSwitch<IndicatorDefinition>() {

        @Override
        public IndicatorDefinition caseIndicatorDefinition(IndicatorDefinition object) {
            return object;
        };
    };

    public static final RulesSwitch<MatchRuleDefinition> MATCH_RULE = new RulesSwitch<MatchRuleDefinition>() {

        @Override
        public MatchRuleDefinition caseMatchRuleDefinition(MatchRuleDefinition object) {
            return object;
        };
    };

    public static final RulesSwitch<DQRule> DQ_RULE = new RulesSwitch<DQRule>() {

        @Override
        public DQRule caseDQRule(DQRule object) {
            return object;
        };
    };

    public static final SoftwaredeploymentSwitch<SoftwareSystem> SOFTWARE = new SoftwaredeploymentSwitch<SoftwareSystem>() {

        @Override
        public SoftwareSystem caseSoftwareSystem(SoftwareSystem object) {
            return object;
        };
    };

    public static final XmlSwitch<TdXmlSchema> XMLDOC = new XmlSwitch<TdXmlSchema>() {

        @Override
        public TdXmlSchema caseTdXmlSchema(TdXmlSchema object) {
            return object;
        };
    };

    public static final IndicatorSqlSwitch<UserDefIndicator> UDI = new IndicatorSqlSwitch<UserDefIndicator>() {

        @Override
        public UserDefIndicator caseUserDefIndicator(UserDefIndicator object) {
            return object;
        };
    };

    /**
     * DOC bZhou Comment method "isAnalysis".
     *
     * Decide it's an analysis or not.
     *
     * @param element
     * @return
     */
    public static boolean isAnalysis(EObject element) {
        return ANALYSIS.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isReport".
     *
     * Decide it's a report or not.
     *
     * @param element
     * @return
     */
    public static boolean isReport(EObject element) {
        return REPORT.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isPattern".
     *
     * Decide it's a pattern or not.
     *
     * @param element
     * @return
     */
    public static boolean isPattern(EObject element) {
        return PATTERN.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isID".
     *
     * Decide it's an indicator defintion or not.
     *
     * @param element
     * @return
     */
    public static boolean isID(EObject element) {
        return INDICATOR_DEFINITION.doSwitch(element) != null;
    }

    /**
     *
     *
     * Decide it's an match rule or not.
     *
     * @param element
     * @return
     */
    public static boolean isMatchRule(EObject element) {
        return MATCH_RULE.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isDQRule".
     *
     * Decide it's a dq rule or not.
     *
     * @param element
     * @return
     */
    public static boolean isDQRule(EObject element) {
        return DQ_RULE.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isDataProvider".
     *
     * Decide it's a data provider or not.
     *
     * @param element
     * @return
     */
    public static boolean isDataProvider(EObject element) {
        return SwitchHelpers.CONNECTION_SWITCH.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isXMLProvider".
     *
     * Decide it's a XML provider or not.
     *
     * @param element
     * @return
     */
    public static boolean isXMLProvider(EObject element) {
        return XMLDOC.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isSoftware".
     *
     * Decide it's a software system or not.
     *
     * @param element
     * @return
     */
    public static boolean isSoftware(EObject element) {
        return SOFTWARE.doSwitch(element) != null;
    }

    /**
     * DOC bZhou Comment method "isUDID".
     *
     * Decide it's a user defined indicator or not.
     *
     * @param element
     * @return
     */
    public static boolean isUDID(EObject element) {
        return UDI.doSwitch(element) != null;
    }
}
