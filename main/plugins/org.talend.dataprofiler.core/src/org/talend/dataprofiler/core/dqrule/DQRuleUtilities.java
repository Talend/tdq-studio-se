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
package org.talend.dataprofiler.core.dqrule;

import org.talend.core.model.general.Project;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.IndicatorSqlFactory;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class DQRuleUtilities {

    private DQRuleUtilities() {
    }

    /**
     * DOC xqliu Comment method "createIndicatorUnit".
     *
     * @param fe
     * @param tableIndicator
     * @param analysis
     * @return
     */
    public static TableIndicatorUnit createIndicatorUnit(RuleRepNode ruleRepNode, TableIndicator tableIndicator, Analysis analysis) {
        IndicatorDefinition whereRule = ruleRepNode.getRule();
        for (Indicator indicator : tableIndicator.getIndicators()) {
            if (whereRule.getName().equals(indicator.getName())) {
                // TDQ-13646 msjian : check if they are also in the same project.
                Project project = ruleRepNode.getProject();
                DQRepositoryNode findNode =
                        RepositoryNodeHelper.recursiveFind(indicator.getIndicatorDefinition());
                if (RepositoryNodeHelper.isSameProject(project, findNode.getProject())) {
                    return null;
                }
                // TDQ-13646~
            }
        }

        WhereRuleIndicator[] compositeWhereRuleIndicator = createCompositeWhereRuleIndicator(tableIndicator.getColumnSet(),
                whereRule);
        IndicatorEnum type = IndicatorEnum.findIndicatorEnum(compositeWhereRuleIndicator[0].eClass());
        TableIndicatorUnit addIndicatorUnit = tableIndicator.addSpecialIndicator(whereRule, type, compositeWhereRuleIndicator[0]);
        DependenciesHandler.getInstance().setUsageDependencyOn(analysis, whereRule);

        return addIndicatorUnit;
    }

    /**
     * @return 0 based index is WhereRuleIndicator
     */
    public static WhereRuleIndicator[] createCompositeWhereRuleIndicator(ModelElement anaElement, IndicatorDefinition whereRuleDef) {
        WhereRuleIndicator wrIndicator = IndicatorSqlFactory.eINSTANCE.createWhereRuleIndicator();
        wrIndicator.setAnalyzedElement(anaElement);
        wrIndicator.setIndicatorDefinition(whereRuleDef);

        return new WhereRuleIndicator[] { wrIndicator };

    }

}
