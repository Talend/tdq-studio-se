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

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.rcp.i18n.Messages;

/**
 * Displays a window for view selection. <br/>
 * 
 * $Id: ShowViewAction.java,v 1.1 2007/03/07 05:08:59 pub Exp $
 * 
 */
@SuppressWarnings("restriction")
public class ShowViewAction extends Action {
    
    private IEclipseContext activeContext;

    private Params params;
    
    /**
     * Constructs a new ShowViewAction.
     */
    public ShowViewAction() {
        super(Messages.getString("ShowViewAction.showView")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        Params parameters = getParams();
        final ShowViewDialog dialog = new ShowViewDialog(window.getShell(), parameters.getApplication(), parameters.getWindow(),
                parameters.getModelService(), parameters.getPartService(), getEclipseContext());
        dialog.open();

        if (dialog.getReturnCode() == Window.CANCEL) {
            return;
        }

        final MPartDescriptor[] descriptors = dialog.getSelection();
        for (int i = 0; i < descriptors.length; ++i) {
            try {
                page.showView(descriptors[i].getElementId());
            } catch (PartInitException e) {
                StatusUtil.handleStatus(e.getStatus(),
                        Messages.getString("WorkbenchMessages.ShowView_errorTitle") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
                        StatusManager.SHOW);
            }
        }
    }
    
    
    private Params getParams() {
        if (params == null) {
            try {
                params = new Params();
                ContextInjectionFactory.inject(params, getEclipseContext());
                // params = ContextInjectionFactory.make(Params.class, getEclipseContext());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return params;
    }

    private IEclipseContext getEclipseContext() {
        if (activeContext == null) {
            IWorkbench workbench = PlatformUI.getWorkbench();
            activeContext = ((IEclipseContext) workbench.getActiveWorkbenchWindow().getService(IEclipseContext.class))
                    .getActiveLeaf();
        }
        return activeContext;
    }

    private static class Params {

        @Inject
        private MApplication application;

        @Inject
        private MWindow window;

        @Inject
        private EModelService modelService;

        @Inject
        private EPartService partService;

        public MApplication getApplication() {
            return application;
        }

        public void setApplication(MApplication application) {
            this.application = application;
        }

        public MWindow getWindow() {
            return window;
        }

        public void setWindow(MWindow window) {
            this.window = window;
        }

        public EModelService getModelService() {
            return modelService;
        }

        public void setModelService(EModelService modelService) {
            this.modelService = modelService;
        }

        public EPartService getPartService() {
            return partService;
        }

        public void setPartService(EPartService partService) {
            this.partService = partService;
        }

    }
}
