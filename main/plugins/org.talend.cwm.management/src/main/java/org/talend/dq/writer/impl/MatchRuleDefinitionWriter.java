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
package org.talend.dq.writer.impl;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by zshen on Aug 20, 2013 Detailled comment
 * 
 */
public class MatchRuleDefinitionWriter extends AElementPersistance {

    Logger log = Logger.getLogger(MatchRuleDefinitionWriter.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#notifyResourceChanges()
     */
    @Override
    protected void notifyResourceChanges() {
        ProxyRepositoryManager.getInstance().save();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected void addDependencies(ModelElement element) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#getFileExtension()
     */
    @Override
    protected String getFileExtension() {
        return FactoriesUtil.DQRULE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#save(org.talend.core.model.properties.Item, boolean)
     */
    @Override
    public ReturnCode save(final Item item, final boolean careDependency) {
        ReturnCode rc = new ReturnCode();
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("save MatchRule Definition item") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                TDQMatchRuleItem matchRuleItem = (TDQMatchRuleItem) item;
                MatchRuleDefinition matchRuleDefinition = matchRuleItem.getMatchRule();
                if (careDependency) {
                    saveWithDependencies(matchRuleItem, matchRuleDefinition);
                } else {
                    saveWithoutDependencies(matchRuleItem, matchRuleDefinition);
                }

            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        try {
            repositoryWorkUnit.throwPersistenceExceptionIfAny();
        } catch (PersistenceException e) {
            log.error(e, e);
            rc.setOk(Boolean.FALSE);
            rc.setMessage(e.getMessage());
        }
        return rc;
    }

    /**
     * copy the block/match keys from matchRule to ruleDefinition.
     * 
     * @param ruleDefinition: created one
     * @param matchRule: exported one from the match analysis
     */
    public void copy(MatchRuleDefinition ruleDefinition, MatchRuleDefinition matchRule) {

        // copy blocking keys
        ruleDefinition.getBlockKeys().clear();
        if (matchRule.getBlockKeys() != null && matchRule.getBlockKeys().size() > 0) {
            for (BlockKeyDefinition blockKey : matchRule.getBlockKeys()) {
                BlockKeyDefinition copy = EcoreUtil.copy(blockKey);
                // should not empty the column value when export it
                ruleDefinition.getBlockKeys().add(copy);
            }
        }
        // copy match keys in each match rules
        ruleDefinition.getMatchRules().clear();
        if (matchRule.getMatchRules() != null && matchRule.getMatchRules().size() > 0) {
            for (MatchRule oneMatchRule : matchRule.getMatchRules()) {
                ruleDefinition.getMatchRules().add(EObjectHelper.deepCopy(oneMatchRule));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#removeDependencies(org.talend.core.model.properties.Item)
     */
    @Override
    protected ReturnCode removeDependencies(Item item) {
        return new ReturnCode(true);
    }

}
