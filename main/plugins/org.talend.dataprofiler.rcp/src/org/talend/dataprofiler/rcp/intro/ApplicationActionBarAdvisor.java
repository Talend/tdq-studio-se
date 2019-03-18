// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.rcp.intro;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.cheatsheets.actions.CheatSheetCategoryBasedSelectionAction;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.talend.dataprofiler.core.ui.perspective.ChangePerspectiveAction;
import org.talend.dataprofiler.core.ui.perspective.PerspectiveMenuManager;
import org.talend.dataprofiler.rcp.i18n.Messages;
import org.talend.dataprofiler.rcp.intro.linksbar.LinkToolbarLabel;
import org.talend.dataprofiler.rcp.intro.linksbar.LinksToolbarItem;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
@SuppressWarnings("restriction")
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;

    private IWorkbenchAction preferenceAction;

    private NewAboutAction aboutAction;

    private IWorkbenchAction saveAction;

    private IWorkbenchAction saveAllAction;

    private IWorkbenchAction helpAction;

    private IWorkbenchAction welcomeAction;

    private IWorkbenchAction resetPerspectiveAction;

    private IWorkbenchAction colseAction;

    private IWorkbenchAction colseAllAction;

    private IWorkbenchWindow window;

    private IWorkbenchAction savePerspectiveAsAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
    protected void makeActions(IWorkbenchWindow workbenchWindow) {
        this.window = workbenchWindow;
        exitAction = ActionFactory.QUIT.create(workbenchWindow);
        register(exitAction);

        colseAction = ActionFactory.CLOSE.create(workbenchWindow);
        colseAllAction = ActionFactory.CLOSE_ALL.create(workbenchWindow);

        saveAction = ActionFactory.SAVE.create(workbenchWindow);
        register(saveAction);

        saveAllAction = ActionFactory.SAVE_ALL.create(workbenchWindow);
        register(saveAllAction);

        register(ActionFactory.DELETE.create(workbenchWindow));

        preferenceAction = ActionFactory.PREFERENCES.create(workbenchWindow);
        register(preferenceAction);

        welcomeAction = ActionFactory.INTRO.create(workbenchWindow);
        register(welcomeAction);

        helpAction = ActionFactory.HELP_CONTENTS.create(workbenchWindow);
        register(helpAction);

        aboutAction = new NewAboutAction(workbenchWindow);

        resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(workbenchWindow);
        register(resetPerspectiveAction);

        savePerspectiveAsAction = ActionFactory.SAVE_PERSPECTIVE.create(workbenchWindow);
        register(resetPerspectiveAction);
    }

    @Override
    protected void fillMenuBar(IMenuManager menuBar) {
        this.beforefillMenuBar();
        MenuManager fileMenu = new MenuManager(
                Messages.getString("ApplicationActionBarAdvisor.File"), IWorkbenchActionConstants.M_FILE); //$NON-NLS-1$
        MenuManager windowMenu = new MenuManager(
                Messages.getString("ApplicationActionBarAdvisor.Window"), IWorkbenchActionConstants.M_WINDOW); //$NON-NLS-1$
        MenuManager helpMenu = new MenuManager(
                Messages.getString("ApplicationActionBarAdvisor.Help"), IWorkbenchActionConstants.M_HELP); //$NON-NLS-1$

        menuBar.add(fileMenu);
        menuBar.add(windowMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);

        fileMenu.add(colseAction);
        fileMenu.add(colseAllAction);
        fileMenu.add(new Separator());

        // File
        fileMenu.add(saveAction);
        fileMenu.add(saveAllAction);
        fileMenu.add(exitAction);

        // Window
        MenuManager perspMenu = new PerspectiveMenuManager();
        menuBar.add(windowMenu);
        windowMenu.add(perspMenu);
        windowMenu.add(preferenceAction);
        windowMenu.add(resetPerspectiveAction);
        windowMenu.add(new ShowViewAction());
        windowMenu.add(savePerspectiveAsAction);

        // Help
        helpMenu.add(welcomeAction);
        helpMenu.add(helpAction);
        helpMenu.add(aboutAction);

        // ADD qiongli 2010-6-3,bug 0012874
        CheatSheetCategoryBasedSelectionAction cscAction = new CheatSheetCategoryBasedSelectionAction();
        cscAction.setText(Messages.getString("ApplicationActionBarAdvisor.CheatSheets"));//$NON-NLS-1$
        helpMenu.add(cscAction);
    }

    private static final String[] ACTIONSETID = new String[] { "org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo", //$NON-NLS-1$
            "org.eclipse.ui.edit.text.actionSet.annotationNavigation", "org.eclipse.ui.NavigateActionSet", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.ui.WorkingSetActionSet", "org.eclipse.ui.edit.text.actionSet.navigation", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.search.searchActionSet", "org.eclipse.ui.edit.text.actionSet.openExternalFile", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.jdt.ui.actions.GoToPackage", "org.eclipse.jdt.ui.actions.GoToType", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.jdt.ui.actions.OpenExternalJavaDoc", "org.eclipse.jdt.ui.actions.OpenSuperImplementation", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.jdt.ui.actions.CopyQualifiedName", "org.eclipse.jdt.ui.actions.Open", //$NON-NLS-1$ //$NON-NLS-2$
            "org.eclipse.jdt.ui.actions.OpenTypeHierarchy", "org.eclipse.jdt.ui.actions.OpenCallHierarchy", //$NON-NLS-1$ //$NON-NLS-2$
            "org.talend.repository.bootTalendActionSet" }; //$NON-NLS-1$

    private void beforefillMenuBar() {
        this.removeAction();
    }

    /**
     * remove the unnecessary actions.
     */
    private void removeAction() {
        ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
        IActionSetDescriptor[] actionSets = reg.getActionSets();
        List<String> list = Arrays.asList(ACTIONSETID);
        for (IActionSetDescriptor actionSet : actionSets) {
            if (list.contains(actionSet.getId())) {
                IExtension ext = actionSet.getConfigurationElement().getDeclaringExtension();
                reg.removeExtension(ext, new Object[] { actionSet });
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
     */
    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "switch_persp")); //$NON-NLS-1$
        toolbar.add(new ChangePerspectiveAction(true));
        coolBar.add(new ToolBarContributionItem(toolbar, "save")); //$NON-NLS-1$
        toolbar.add(ActionFactory.SAVE.create(window));

        // add feature:15174
        // Workbench3xImplementation4CoolBar.createLinksToolbarItem(coolBar);
        IToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.RIGHT);

        toolBarManager
                .add(new ImageAction(this.window, "icons/demo.png", LinksToolbarItem.LEARN_ORIG_URL, //$NON-NLS-1$
                        Messages.getString("LinksToolbarItem_0"))); //$NON-NLS-1$
        toolBarManager
                .add(new LinkToolbarLabel(LinksToolbarItem.LEARN_URL, Messages.getString("LinksToolbarItem_0"))); //$NON-NLS-1$
        toolBarManager
                .add(new ImageAction(this.window, "icons/irc_protocol.png", LinksToolbarItem.ASK_ORIG_URL, //$NON-NLS-1$
                        Messages.getString("LinksToolbarItem_7"))); //$NON-NLS-1$
        toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.ASK_URL, Messages.getString("LinksToolbarItem_7"))); //$NON-NLS-1$

        toolBarManager
                .add(new ImageAction(this.window, "icons/wizard.png", LinksToolbarItem.UPGRADE_ORIG_URL, //$NON-NLS-1$
                        Messages.getString("LinksToolbarItem_11"))); //$NON-NLS-1$
        toolBarManager
                .add(new LinkToolbarLabel(LinksToolbarItem.UPGRADE_URL,
                        Messages.getString("LinksToolbarItem_11"))); //$NON-NLS-1$

        coolBar.add(new ToolBarContributionItem(toolBarManager, LinksToolbarItem.COOLITEM_LINKS_ID));
    }

    private class NewAboutAction extends Action {

        IWorkbenchAction aboutAction;

        public NewAboutAction(IWorkbenchWindow workbenchWindow) {
            super(""); //$NON-NLS-1$
            aboutAction = ActionFactory.ABOUT.create(workbenchWindow);
            register(aboutAction);
            this.setText(aboutAction.getText());
            this.setToolTipText(aboutAction.getToolTipText());
        }

        @Override
        public void run() {
            aboutAction.run();
        }
    }
}
