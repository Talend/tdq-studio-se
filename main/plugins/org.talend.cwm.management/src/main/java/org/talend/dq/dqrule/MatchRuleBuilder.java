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
package org.talend.dq.dqrule;

import org.apache.log4j.Logger;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dq.analysis.parameters.DQMatchRuleParameter;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class MatchRuleBuilder {

    static Logger log = Logger.getLogger(MatchRuleBuilder.class);

    private boolean initialized = false;

    private MatchRuleDefinition matchRule = null;

    public boolean initializeDqRuleBuilder(String ruleName) {

        if (initialized) {
            log.warn(Messages.getString("DqRuleBuilder.Initialized")); //$NON-NLS-1$
            return false;
        }

        this.matchRule = RulesFactory.eINSTANCE.createMatchRuleDefinition();
        matchRule.setName(ruleName);
        // IndicatorCategory ruleIndicatorCategory = DefinitionHandler.getInstance().getDQRuleIndicatorCategory();
        // if (ruleIndicatorCategory != null) {
        // matchRule.getCategories().add(ruleIndicatorCategory);
        // }
        return true;
    }

    public boolean initializeDqRuleBuilder(DQMatchRuleParameter parameter) {

        boolean initializeDqRuleBuilder = initializeDqRuleBuilder(parameter.getName());
        if (initializeDqRuleBuilder && matchRule != null) {
            matchRule.setRecordLinkageAlgorithm(parameter.getDefaultAlgorithmType().name());
        }
        return initializeDqRuleBuilder;
    }

    public MatchRuleDefinition getMatchRule() {
        return matchRule;
    }
}
