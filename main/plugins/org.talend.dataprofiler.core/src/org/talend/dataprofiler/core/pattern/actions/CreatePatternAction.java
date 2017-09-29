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

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IHelpResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.CreatePatternWizard;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dq.analysis.parameters.PatternParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class CreatePatternAction extends Action implements ICheatSheetAction {

    private IFolder folder;

    private ExpressionType type;

    private String expression;

    private String lanuage;

    private String purpose;

    public CreatePatternAction() {

    }

    /**
     * DOC qzhang AddSqlFileAction constructor comment.
     * 
     * @param folder
     * @param type
     */
    public CreatePatternAction(IFolder folder, ExpressionType type) {
        switch (type) {
        case SQL_LIKE:
            setText(DefaultMessagesImpl.getString("CreatePatternAction.newSQLPattern")); //$NON-NLS-1$
            break;
        default:
            setText(DefaultMessagesImpl.getString("CreatePatternAction.newRegularPattern")); //$NON-NLS-1$
            break;
        }
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_PATTERN));
        this.folder = folder;
        this.type = type;
    }

    public CreatePatternAction(IFolder folder, ExpressionType type, String expression, String lanuage) {
        this(folder, type);
        this.expression = expression;
        this.lanuage = lanuage;
    }

    public CreatePatternAction(IFolder folder, ExpressionType type, String expression, String purpose, String lanuage) {
        this(folder, type, expression, lanuage);
        this.purpose = purpose;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (folder.exists()) {
            PatternParameter parameter = new PatternParameter();
            FolderProvider folderProvider = new FolderProvider();
            folderProvider.setFolderResource(folder);
            parameter.setFolderProvider(folderProvider);

            CreatePatternWizard fileWizard;
            if (this.expression != null && this.lanuage != null) {
                fileWizard = (CreatePatternWizard) WizardFactory.createPatternWizard(type, parameter, expression, lanuage);
            } else {
                fileWizard = (CreatePatternWizard) WizardFactory.createPatternWizard(type, parameter);
            }
            fileWizard.setPurpose(purpose);
            IContext context = HelpSystem.getContext(HelpPlugin.getDefault().getPatternHelpContextID());
            IHelpResource[] relatedTopics = context.getRelatedTopics();
            String href = relatedTopics[0].getHref();
            switch (type) {
            case SQL_LIKE:
                href = relatedTopics[1].getHref();
                break;
            default:
                break;
            }
            WizardDialog dialog = new OpeningHelpWizardDialog(Display.getDefault().getActiveShell(), fileWizard, href);
            fileWizard.setWindowTitle(getText());
            if (WizardDialog.OK == dialog.open())
                ProxyRepositoryManager.getInstance().save();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        if (params == null || params.length == 0) {
            return;
        }
        Integer patternType = null;
        if (NumberUtils.isNumber(params[0])) {
            patternType = NumberUtils.toInt(params[0]);
        }
        if (patternType != null) {
            switch (patternType.intValue()) {
            case ExpressionType.SQL_LIKE_VALUE:
                this.type = ExpressionType.SQL_LIKE;
                this.folder = ResourceManager.getPatternSQLFolder();
                setText(DefaultMessagesImpl.getString("CreatePatternAction.newSQLPattern")); //$NON-NLS-1$
                break;
            case ExpressionType.REGEXP_VALUE:
                this.type = ExpressionType.REGEXP;
                this.folder = ResourceManager.getPatternRegexFolder();
                setText(DefaultMessagesImpl.getString("CreatePatternAction.newRegularPattern")); //$NON-NLS-1$
                break;
            default:
            }
        }
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_PATTERN));
        this.run();
    }
}
