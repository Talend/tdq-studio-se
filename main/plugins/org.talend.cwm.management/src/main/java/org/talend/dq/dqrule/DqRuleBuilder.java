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
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class DqRuleBuilder {

    static Logger log = Logger.getLogger(DqRuleBuilder.class);

    private boolean initialized = false;

    private WhereRule whereRule;

    private ParserRule parserRule;

    public boolean initializeParserRuleBuilder(String ruleName) {

        if (initialized) {
            log.warn(Messages.getString("DqRuleBuilder.Initialized")); //$NON-NLS-1$
            return false;
        }

        this.parserRule = RulesFactory.eINSTANCE.createParserRule();
        parserRule.setName(ruleName);
        IndicatorCategory ruleIndicatorCategory = DefinitionHandler.getInstance().getDQRuleIndicatorCategory();
        if (ruleIndicatorCategory != null) {
            parserRule.getCategories().add(ruleIndicatorCategory);
        }
        return true;
    }

    public ParserRule getParserRule() {
        return parserRule;
    }
    public boolean initializeDqRuleBuilder(String ruleName) {

        if (initialized) {
            log.warn(Messages.getString("DqRuleBuilder.Initialized")); //$NON-NLS-1$
            return false;
        }

        this.whereRule = RulesFactory.eINSTANCE.createWhereRule();
        whereRule.setName(ruleName);
        IndicatorCategory ruleIndicatorCategory = DefinitionHandler.getInstance().getDQRuleIndicatorCategory();
        if (ruleIndicatorCategory != null) {
            whereRule.getCategories().add(ruleIndicatorCategory);
        }
        return true;
    }

    public WhereRule getWhereRule() {
        return whereRule;
    }

}
