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
package org.talend.dataprofiler.core.ui.wizard.matchrule;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.dqrules.BusinessRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor;
import org.talend.dataprofiler.core.ui.editor.matchrule.MatchRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizard;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.analysis.parameters.ConnectionParameter;
import org.talend.dq.analysis.parameters.DQMatchRuleParameter;
import org.talend.dq.dqrule.MatchRuleBuilder;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.dq.writer.impl.MatchRuleDefinitionWriter;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class NewMatchRuleWizard extends AbstractWizard {

    private NewMatchRuleMetadataPage newMatchRuleMetadataPage = null;

    private DQMatchRuleParameter parameter;

    private MatchRuleDefinition matchRule = null;

    private boolean isExport = false;

    private String helpContextId = "org.talend.help.match_rule";//$NON-NLS-1$

    public NewMatchRuleWizard(DQMatchRuleParameter parameter) {
        this.parameter = parameter;
        setHelpAvailable(!PluginChecker.isOnlyTopLoaded());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        newMatchRuleMetadataPage = new NewMatchRuleMetadataPage();
        if (isExport) {
            newMatchRuleMetadataPage.setTitle(DefaultMessagesImpl.getString("NewMatchRuleWizard.exportPage1_1")); //$NON-NLS-1$
        } else {
            newMatchRuleMetadataPage.setTitle(DefaultMessagesImpl.getString("NewMatchRuleWizard.createPage1_1")); //$NON-NLS-1$
        }
        newMatchRuleMetadataPage.setDescription(DefaultMessagesImpl.getString("NewMatchRuleWizard.defineProp")); //$NON-NLS-1$
        newMatchRuleMetadataPage.setHelpContextId(helpContextId);
        this.addPage(newMatchRuleMetadataPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.ICWMResouceAdapter#initCWMResourceBuilder()
     */
    public ModelElement initCWMResourceBuilder() {
        if (matchRule != null) {
            MatchRuleDefinition copiedMatchRuleDefinition = EcoreUtil.copy(matchRule);
            copiedMatchRuleDefinition.setName(parameter.getName());
            return copiedMatchRuleDefinition;
        }
        MatchRuleBuilder ruleBuilder = new MatchRuleBuilder();
        boolean ruleInitialized = ruleBuilder.initializeDqRuleBuilder(parameter);
        if (ruleInitialized) {
            return ruleBuilder.getMatchRule();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.ICWMResouceAdapter#createAndSaveCWMFile(orgomg.cwm.objectmodel.core.
     * ModelElement)
     */
    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement repositoryObject) {
        MatchRuleDefinition newMatchRule = (MatchRuleDefinition) repositoryObject;

        TaggedValueHelper.setValidStatus(true, newMatchRule);

        // Added for export an match rule created by the match analysis, which need to replace the rules in createdItem,
        MatchRuleDefinitionWriter matchRuleWriter = ElementWriterFactory.getInstance().createdMatchRuleWriter();
        if (isExport) {
            matchRuleWriter.copy(newMatchRule, this.matchRule);
        }
        IFolder folder = parameter.getFolderProvider().getFolderResource();
        TypedReturnCode<Object> createdItem = matchRuleWriter.create(newMatchRule, folder);

        return createdItem;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#openEditor(org.talend.repository.model
     * .IRepositoryNode )
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        BusinessRuleItemEditorInput matchRuleEditorInput = new BusinessRuleItemEditorInput(repNode);
        CorePlugin.getDefault().openEditor(matchRuleEditorInput, DQRuleEditor.class.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.AbstractWizard#openEditor(org.talend.dataquality.properties.TDQMatchRuleItem
     * )
     */
    @Override
    public void openEditor(TDQMatchRuleItem item) {
        MatchRuleItemEditorInput matchRuleEditorInput = new MatchRuleItemEditorInput(item);
        CorePlugin.getDefault().openEditor(matchRuleEditorInput, DQRuleEditor.class.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getResourceFileMap()
     */
    @Override
    protected ResourceFileMap getResourceFileMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getParameter()
     */
    @Override
    public ConnectionParameter getParameter() {
        return parameter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getEditorName()
     */
    @Override
    protected String getEditorName() {
        return DQRuleEditor.class.getName();
    }

    public void setMatchRule(MatchRuleDefinition matchRule) {
        this.matchRule = matchRule;
        isExport = true;
    }

    // the context id will change from different entry: new match rule, or export match rule
    public void setHelpContextId(String newContextId) {
        helpContextId = newContextId;
    }

    @Override
    public boolean performFinish() {
        boolean finished = super.performFinish();
        // when export the match rule, the selected node must not be the match rule folder, so, it can not be refreshed
        // and shown in repository view:
        if (isExport) {
            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getRootNode(ERepositoryObjectType.TDQ_LIBRARIES));
        }
        return finished;
    }

}
