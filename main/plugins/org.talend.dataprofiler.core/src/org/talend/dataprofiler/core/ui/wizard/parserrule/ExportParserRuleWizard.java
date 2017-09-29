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
package org.talend.dataprofiler.core.ui.wizard.parserrule;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ExportFactory;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.utils.io.FilesUtils;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ExportParserRuleWizard extends Wizard {

    private static Logger log = Logger.getLogger(ExportParserRuleWizard.class);

    private IFolder folder;

    private IRepositoryNode parserRuleFolder;

    private ExportParserRuleWizardPage page;

    private boolean isForExchange;

    /**
     * DOC klliu ExportParserRuleWizard constructor comment.
     * 
     * @param parserRuleFolder2
     */
    public ExportParserRuleWizard(IRepositoryNode parserRuleFolder) {
        this.folder = WorkbenchUtils.getFolder((RepositoryNode) parserRuleFolder);
        this.parserRuleFolder = parserRuleFolder;
    }

    public ExportParserRuleWizard(IRepositoryNode parserRuleFolder, boolean isForExchange) {
        this.folder = WorkbenchUtils.getFolder((RepositoryNode) parserRuleFolder);
        this.parserRuleFolder = parserRuleFolder;
        this.isForExchange = isForExchange;
    }

    @Override
    public boolean performFinish() {

        String targetFile = page.getTargetFile();
        Object[] elements = page.getSelectedTree().getCheckedElements();

        List<ParserRule> parserRules = new ArrayList<ParserRule>();
        for (Object element : elements) {
            if (element instanceof RuleRepNode) {
                RuleRepNode ruleNode = (RuleRepNode) element;
                ParserRule parserRule = (ParserRule) ruleNode.getRule();
                parserRules.add(parserRule);
            }
        }

        if ("".equals(targetFile)) { //$NON-NLS-1$
            MessageDialog
                    .openError(
                            getShell(),
                            DefaultMessagesImpl.getString("ExportParserRuleWizard.Error"), DefaultMessagesImpl.getString("ExportParserRuleWizard.SpecifyValidResource")); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else {
            File resource = new File(targetFile);
            if (isForExchange) {
                // export for exchange
                for (Iterator<ParserRule> iterator = parserRules.iterator(); iterator.hasNext();) {
                    ParserRule parserRule = iterator.next();
                    ExportFactory.export(resource, folder, parserRule);
                    File parserRuleFile = new File(resource, ExportFactory.toLocalFileName(parserRule.getName() + ".csv")); //$NON-NLS-1$
                    if (parserRuleFile.isFile() && parserRuleFile.exists()) {
                        try {
                            FilesUtils.zip(parserRuleFile, parserRuleFile.getPath() + ".zip"); //$NON-NLS-1$
                            parserRuleFile.delete();

                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            } else {
                // Export to local
                ExportFactory.export(resource, folder, parserRules.toArray(new ParserRule[parserRules.size()]));
            }

            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.RULES_PARSER));
            CorePlugin.getDefault().refreshWorkSpace();

            return true;
        }

    }

    @Override
    public void addPages() {
        page = new ExportParserRuleWizardPage(folder, parserRuleFolder);
        addPage(page);
    }
}
