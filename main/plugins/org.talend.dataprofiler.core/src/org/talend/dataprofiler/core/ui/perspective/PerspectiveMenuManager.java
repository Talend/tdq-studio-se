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
package org.talend.dataprofiler.core.ui.perspective;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * Menu manager for perspective switching. <br/>
 */
public class PerspectiveMenuManager extends MenuManager {	
	
    /**
     * Constructs a new PerspectiveMenuManager.
     */
    public PerspectiveMenuManager() {
        super("&Perspective", DefaultMessagesImpl.getString("PerspectiveMenuManager.perspective"));  //$NON-NLS-1$ //$NON-NLS-2$
        

        addMenuListener(new MenuFiller());
        
        setRemoveAllWhenShown(true);
        add(new ChangePerspectiveAction(DefaultMessagesImpl.getString("PerspectiveMenuManager.dummy"))); //$NON-NLS-1$
    }

    /**
     * @author rli
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class PerspectiveData {
		private String refPerspectiveId;
		private String dataprofilerPerspectiveName;

		public PerspectiveData(String refPerspectiveId,
				String dataprofilerPerspectiveName) {
			this.refPerspectiveId = refPerspectiveId;
			this.dataprofilerPerspectiveName = dataprofilerPerspectiveName;
		}

		public String getRefPerspectiveId() {
			return refPerspectiveId;
		}

		public String getDataprofilerPerspectiveName() {
			return dataprofilerPerspectiveName;
		}
	}
    
    public PerspectiveData[] getTalendPerspectives(IConfigurationElement[] talendPpElements) {
        String talendPpId = null;
        List<PerspectiveData> talendPpList = new ArrayList<PerspectiveData>();
        for (int i = 0; i < talendPpElements.length; i++) {
            talendPpId = talendPpElements[i].getAttribute("refPerspectiveId"); //$NON-NLS-1$
            if (talendPpId == null) {
                continue;
            } else {
				talendPpList.add(new PerspectiveData(talendPpId,
						talendPpElements[i].getAttribute("dataprofilerPerspectiveName"))); //$NON-NLS-1$
			}
        }
        return talendPpList.toArray(new PerspectiveData[talendPpList.size()]);
    }

    /**
     * @author rli
     *
     */
    private  class MenuFiller implements IMenuListener {
        /**
         * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
         */
        public void menuAboutToShow(IMenuManager manager) {
            IPerspectiveRegistry registry = PlatformUI.getWorkbench().getPerspectiveRegistry();
            
            IWorkbench workbench = PlatformUI.getWorkbench();
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            String activePersp = page.getPerspective().getId();
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
            IConfigurationElement[] talendPpElements = extensionRegistry
                    .getExtensionPoint("org.talend.dataprofiler.core.dataprofilerperspectives").getConfigurationElements(); //$NON-NLS-1$
            PerspectiveData[] perspectiveIds = getTalendPerspectives(talendPpElements);
            
            for (PerspectiveData perspectiveData : perspectiveIds) {
                // Search perspective name & icon
                IPerspectiveDescriptor desc = registry.findPerspectiveWithId(perspectiveData.getRefPerspectiveId());
                if (desc != null) {
                    ChangePerspectiveAction perspAction = new ChangePerspectiveAction(perspectiveData.getRefPerspectiveId());
                    perspAction.setText(perspectiveData.getDataprofilerPerspectiveName());
                    perspAction.setToolTipText(desc.getDescription());
                    perspAction.setImageDescriptor(desc.getImageDescriptor());
                    perspAction.setChecked(desc.getId().equals(activePersp));                
                    manager.add(perspAction);
                }
            }
            // MOD qiongli 2011-2-17,feature 17168.make it show the menu item 'Perspective--other...'
            manager.add(ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(workbench.getActiveWorkbenchWindow()));
        }
    }
}
