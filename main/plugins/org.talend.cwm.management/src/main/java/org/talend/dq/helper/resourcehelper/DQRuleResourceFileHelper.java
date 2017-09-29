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
package org.talend.dq.helper.resourcehelper;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dataquality.rules.util.RulesSwitch;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class DQRuleResourceFileHelper extends ResourceFileMap {

    private static Logger log = Logger.getLogger(DQRuleResourceFileHelper.class);

    private static DQRuleResourceFileHelper instance;

    RulesSwitch<WhereRule> rulesSwitch = new RulesSwitch<WhereRule>() {

        @Override
        public WhereRule caseWhereRule(WhereRule object) {
            return object;
        }

    };

    RulesSwitch<DQRule> dqRulesSwitch = new RulesSwitch<DQRule>() {

        @Override
        public DQRule caseDQRule(DQRule object) {
            return object;
        }

    };

    // add support for match rule
    RulesSwitch<MatchRuleDefinition> matchRulesSwitch = new RulesSwitch<MatchRuleDefinition>() {

        @Override
        public MatchRuleDefinition caseMatchRuleDefinition(MatchRuleDefinition object) {
            return object;
        }

        @Override
        public MatchRuleDefinition caseIndicatorDefinition(IndicatorDefinition object) {
            return (MatchRuleDefinition) object;
        }
    };

    private DQRuleResourceFileHelper() {
        super();
    }

    public static DQRuleResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new DQRuleResourceFileHelper();
        }
        return instance;
    }

    /**
     * 
     * DOC xqliu Comment method "findWhereRule".
     * 
     * @param file
     * @return
     */
    public DQRule findDQRule(IFile file) {
        if (checkFile(file)) {
            return (DQRule) getModelElement(file);
        }

        return null;
    }

    public WhereRule findWhereRule(IFile file) {
        if (checkFile(file)) {
            return (WhereRule) getModelElement(file);
        }

        return null;
    }

    public IndicatorDefinition findIndicatorDefinition(IFile file) {
        if (checkFile(file)) {
            return (IndicatorDefinition) getModelElement(file);
        }

        return null;
    }

    public MatchRuleDefinition findMatchRule(IFile file) {
        if (checkFile(file)) {
            return (MatchRuleDefinition) getModelElement(file);
        }

        return null;
    }

    public Set<String> getAllParserRlueNames(IFolder folder) {
        Set<String> list = new HashSet<String>();
        return getNestFolderParserRuleNames(list, folder);
    }

    private Set<String> getNestFolderParserRuleNames(Set<String> list, IFolder folder) {
        try {
            for (IResource resource : folder.members()) {
                if (resource instanceof IFile) {
                    DQRule findDQRule = findDQRule((IFile) resource);
                    if (findDQRule != null) {
                        list.add(findDQRule.getName());
                    }
                } else {
                    getNestFolderParserRuleNames(list, (IFolder) resource);
                }
            }
        } catch (CoreException e) {
            log.error(e, e);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.DQRULE.equalsIgnoreCase(file.getFileExtension());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public ModelElement doSwitch(EObject object) {
        DQRule rule = dqRulesSwitch.doSwitch(object);
        if (rule != null) {
            return rule;
        } else {// add support for match rule
            return matchRulesSwitch.doSwitch(object);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getRulesFolder();
    }

    /**
     * Getter for rulesSwitch.
     * 
     * @return the rulesSwitch
     */
    public RulesSwitch<WhereRule> getRulesSwitch() {
        return this.rulesSwitch;
    }
}
