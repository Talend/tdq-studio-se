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
package org.talend.dataprofiler.core.pattern;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.wizard.Wizard;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.utils.io.FilesUtils;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ExportPatternsWizard extends Wizard {

    private static Logger log = Logger.getLogger(ExportPatternsWizard.class);

    private IFolder folder;

    private ExportPatternsWizardPage page;

    private boolean isForExchange;

    private RepositoryNode node;

    public ExportPatternsWizard(IFolder folder, boolean isForExchange) {
        this.folder = folder;
        this.isForExchange = isForExchange;
    }

    /**
     * DOC klliu ExportPatternsWizard constructor comment.
     * 
     * @param node
     * @param isForExchange
     */
    public ExportPatternsWizard(RepositoryNode node, boolean isForExchange) {
        this.node = node;
        this.isForExchange = isForExchange;
        this.folder = WorkbenchUtils.getFolder(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        String targetFile = page.getTargetFile();
        Object[] elements = page.getSelectedPatternsTree().getCheckedElements();

        List<Pattern> seletedPatterns = new ArrayList<Pattern>();
        for (Object element : elements) {
            // MOD klliu 2011-03-08 bug 18657
            if (element instanceof PatternRepNode) {
                PatternRepNode patternNode = (PatternRepNode) element;
                Pattern pattern = patternNode.getPattern();
                seletedPatterns.add(pattern);
            }
        }

        if ("".equals(targetFile)) { //$NON-NLS-1$
            MessageDialog
                    .openError(
                            getShell(),
                            DefaultMessagesImpl.getString("ExportPatternsWizard.Error"), DefaultMessagesImpl.getString("ExportPatternsWizard.SpecifyValidResource")); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else {
            File resource = new File(targetFile);

            if (isForExchange) {
                ExportFactory.export(resource, folder, seletedPatterns.toArray(new Pattern[seletedPatterns.size()]));

                for (Iterator<Pattern> iterator = seletedPatterns.iterator(); iterator.hasNext();) {
                    Pattern pattern = iterator.next();
                    File patternFile = new File(resource, ExportFactory.toLocalFileName(pattern.getName() + ".csv")); //$NON-NLS-1$
                    if (patternFile.isFile() && patternFile.exists()) {
                        try {
                            FilesUtils.zip(patternFile, patternFile.getPath() + ".zip"); //$NON-NLS-1$
                            patternFile.delete();

                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }

            } else {

                boolean isContinue = true;
                if (resource.exists()) {
                    isContinue = MessageDialogWithToggle.openConfirm(null,
                            DefaultMessagesImpl.getString("ExportPatternsWizard.waring"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("ExportPatternsWizard.fileAlreadyExist")); //$NON-NLS-1$
                }

                if (!isContinue) {
                    return false;
                }
                ExportFactory.export(resource, folder, seletedPatterns.toArray(new Pattern[seletedPatterns.size()]));
            }

            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.PATTERNS));
            CorePlugin.getDefault().refreshWorkSpace();

            return true;
        }
    }

    @Override
    public void addPages() {
        page = new ExportPatternsWizardPage(node, isForExchange);
        addPage(page);
    }

}
