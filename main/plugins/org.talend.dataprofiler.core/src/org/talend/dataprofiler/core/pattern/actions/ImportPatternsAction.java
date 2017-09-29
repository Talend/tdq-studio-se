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
package org.talend.dataprofiler.core.pattern.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IHelpResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ImportPatternsWizard;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ImportPatternsAction extends Action {

    protected static Logger log = Logger.getLogger(ImportPatternsAction.class);

    private RepositoryNode node;

    private IFolder folder;

    private ExpressionType type;

    /**
     * DOC qzhang ImportPatternsAction constructor comment.
     * 
     * @deprecated
     */
    public ImportPatternsAction(IFolder folder, ExpressionType type) {
        setText(DefaultMessagesImpl.getString("ImportPatternsAction.importPatternOne")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
        this.folder = folder;
        this.type = type;
    }

    /**
     * DOC xqliu ImportPatternsAction constructor comment.
     * 
     * @param node
     * @param type
     */
    public ImportPatternsAction(RepositoryNode node, ExpressionType type) {
        setText(DefaultMessagesImpl.getString("ImportPatternsAction.importPatternOne")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
        this.node = node;
        this.folder = WorkbenchUtils.getFolder(node);
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ImportPatternsWizard wizard = new ImportPatternsWizard(folder, type);

        IContext context = HelpSystem.getContext(HelpPlugin.getDefault().getPatternHelpContextID());
        IHelpResource[] relatedTopics = context.getRelatedTopics();
        String href = relatedTopics[2].getHref();

        WizardDialog dialog = new OpeningHelpWizardDialog(null, wizard, href);
        wizard.setWindowTitle(getText());
        if (WizardDialog.OK == dialog.open()) {
            if (this.node != null) {
                CorePlugin.getDefault().refreshDQView(this.node);
            }
        }
    }
}
