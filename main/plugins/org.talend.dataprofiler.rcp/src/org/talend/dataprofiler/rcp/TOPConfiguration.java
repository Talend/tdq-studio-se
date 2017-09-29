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
package org.talend.dataprofiler.rcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.branding.IActionBarHelper;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class TOPConfiguration implements IBrandingConfiguration {

    protected IActionBarHelper helper;

    protected IActionBarConfigurer actionBarConfigurer;

    private String title = ""; //$NON-NLS-1$

    private boolean useMainLoginCheck = true;

    private boolean useProductRegistration = true;

    @Override
    public IActionBarHelper getHelper() {
        return this.helper;
    }

    @Override
    public void setHelper(IActionBarHelper helper) {
        this.helper = helper;
    }

    public IActionBarConfigurer getActionBarConfigurer() {
        return this.actionBarConfigurer;
    }

    public void setActionBarConfigurer(IActionBarConfigurer actionBarConfigurer) {
        this.actionBarConfigurer = actionBarConfigurer;
    }

    @Override
    public void fillMenuBar(IMenuManager menuBar) {
        if (helper != null) {
            helper.fillMenuBar(menuBar);
        }
    }

    @Override
    public void fillCoolBar(ICoolBarManager coolBar) {
        if (helper != null) {
            helper.fillCoolBar(coolBar);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getHiddenRepositoryCategory()
     */
    @Override
    public List<IRepositoryNode> getHiddenRepositoryCategory(IRepositoryNode parent, String type) {

        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();

        return nodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#initPerspective(org.eclipse.ui.IPageLayout)
     */
    @Override
    public void initPerspective(IPageLayout layout) {
        String componentSettingViewerId = "org.talend.designer.core.ui.views.properties.ComponentSettingsView";//$NON-NLS-1$
        String navigatorId = "org.eclipse.ui.views.ResourceNavigator"; //$NON-NLS-1$
        String outlineId = "org.eclipse.ui.views.ContentOutline"; //$NON-NLS-1$
        String codeId = "org.talend.designer.core.codeView"; //$NON-NLS-1$
        String repositoryId = IRepositoryView.VIEW_ID;

        String runProcessViewId = "org.talend.designer.runprocess.ui.views.processview"; //$NON-NLS-1$
        String problemsViewId = "org.talend.designer.core.ui.views.ProblemsView"; //$NON-NLS-1$
        String modulesViewId = "org.talend.designer.codegen.perlmodule.ModulesView"; //$NON-NLS-1$
        String schedulerViewId = "org.talend.scheduler.views.Scheduler"; //$NON-NLS-1$
        String contextsViewId = "org.talend.designer.core.ui.views.ContextsView"; //$NON-NLS-1$
        String gefPaletteViewId = "org.eclipse.gef.ui.palette_view"; //$NON-NLS-1$
        String jobSettingsViewId = "org.talend.designer.core.ui.views.jobsettings.JobSettingsView"; //$NON-NLS-1$
        String jobHierarchyViewId = "org.talend.designer.core.ui.hierarchy.JobHierarchyViewPart"; //$NON-NLS-1$

        // leftTopLayout
        IFolderLayout leftTopLayout = layout.createFolder("navigatorLayout", IPageLayout.LEFT, new Float(0.3), //$NON-NLS-1$
                IPageLayout.ID_EDITOR_AREA);
        leftTopLayout.addView(repositoryId);
        leftTopLayout.addView(navigatorId);

        // leftBottomLayout
        IFolderLayout leftBottomLayout = layout.createFolder("outlineCodeLayout", IPageLayout.BOTTOM, new Float(0.6), //$NON-NLS-1$
                repositoryId);
        leftBottomLayout.addView(outlineId);
        leftBottomLayout.addView(codeId);

        IFolderLayout rightTopLayout = layout.createFolder("paletteLayout", IPageLayout.RIGHT, new Float(0.8), //$NON-NLS-1$
                IPageLayout.ID_EDITOR_AREA);
        rightTopLayout.addView(gefPaletteViewId);

        // bottomLayout
        IFolderLayout bottomLayout = layout.createFolder("bottomLayout", IPageLayout.BOTTOM, new Float(0.6), //$NON-NLS-1$
                IPageLayout.ID_EDITOR_AREA);
        // bottomLayout.addView(propertyId);
        bottomLayout.addView(jobSettingsViewId);
        bottomLayout.addView(contextsViewId);
        bottomLayout.addView(componentSettingViewerId);

        bottomLayout.addView(runProcessViewId);
        bottomLayout.addView(problemsViewId);
        bottomLayout.addView(modulesViewId);
        bottomLayout.addView(schedulerViewId);
        bottomLayout.addView(jobHierarchyViewId);
        bottomLayout.addPlaceholder("*");//$NON-NLS-1$

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getAvailableComponents()
     */
    @Override
    public String[] getAvailableComponents() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getAvailableLanguages()
     */
    @Override
    public String[] getAvailableLanguages() {
        return new String[] { ECodeLanguage.JAVA.getName() };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getJobEditorSettings()
     */
    @Override
    public Map<String, Object> getJobEditorSettings() {
        // no specific settings by default.
        return new HashMap<String, Object>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#isUseMailLoginCheck()
     */
    @Override
    public boolean isUseMailLoginCheck() {
        return useMainLoginCheck;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#isUseProductRegistration()
     */
    @Override
    public boolean isUseProductRegistration() {
        return useProductRegistration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#isAllowDebugMode()
     */
    @Override
    public boolean isAllowDebugMode() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#isUseDemoProjects()
     */
    @Override
    public boolean isUseDemoProjects() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getAdditionalTitle()
     */
    @Override
    public String getAdditionalTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#setAdditionalTitle(java.lang.String)
     */
    @Override
    public void setAdditionalTitle(String title) {
        this.title = title;
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        return "org.talend.dataprofiler.DataProfilingPerspective"; //$NON-NLS-1$
    }

    public String getTISProductNameForWelcome() {
        return "Integration Suite Studio"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#setUseMailLoginCheck(boolean)
     */
    @Override
    public void setUseMailLoginCheck(boolean useMainLoginCheck) {
        this.useMainLoginCheck = useMainLoginCheck;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#setUseProductRegistration(boolean)
     */
    @Override
    public void setUseProductRegistration(boolean useProductRegistration) {
        this.useProductRegistration = useProductRegistration;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#getJobDesignName()
     */
    public String getJobDesignName() {
        return ERepositoryObjectType.PROCESS.name();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.branding.IBrandingConfiguration#isOnlyRemoteConnection()
     */
    @Override
    public boolean isOnlyRemoteConnection() {
        return false;
    }

    @Override
    public boolean isAllowChengeVersion() {
        return true;
    }
}
