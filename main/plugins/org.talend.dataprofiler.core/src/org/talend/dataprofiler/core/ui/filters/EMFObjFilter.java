// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.model.nodes.foldernode.AnaElementFolderNode;
import org.talend.resource.ResourceManager;

import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.util.CoreSwitch;

/**
 * @author rli
 *
 */
public class EMFObjFilter extends AbstractViewerFilter {

    public static final int FILTER_ID = 1;

    private CoreSwitch<Dependency> dependencySwitch;

    public EMFObjFilter() {
        super();
        dependencySwitch = new CoreSwitch<Dependency>() {

            public Dependency caseDependency(Dependency object) {
                return object;
            }
        };
    }

    public int getId() {
        return FILTER_ID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (parentElement instanceof TreePath) {
            TreePath path = (TreePath) parentElement;
            if (path.getLastSegment() instanceof AnaElementFolderNode) {
                return true;
            }
        } else if (parentElement instanceof IFolder) {
            IFolder folder = (IFolder) parentElement;
            if ("Exchange".equals(folder.getName())) { //$NON-NLS-1$
                return false;
            }
        }
        if (element instanceof EObject) {
            EObject eObj = (EObject) element;
            if (SwitchHelpers.CONNECTION_SWITCH.doSwitch(eObj) != null || dependencySwitch.doSwitch(eObj) != null) {
                return false;
            }
            if (eObj instanceof Component) {
                return false;
            }
        } else if (element instanceof IFile) {
            IFile file = (IFile) element;
            if (file.getName().indexOf(".") == 0) { //$NON-NLS-1$
                return false;
            }
            if (FactoriesUtil.PROPERTIES_EXTENSION.equals(file.getFileExtension())) {
                return false;
            }
            if (ResourceManager.getMetadataFolder().getLocation().isPrefixOf(file.getLocation())) {
                return false;
            }
            return file.getFileExtension() != null;
        }

        // else if (element instanceof SysIndicatorDefinitionRepNode
        // && ((SysIndicatorDefinitionRepNode) element).isSystemIndicator()) {
        // List<TdExpression> indiExpression = ((SysIndicatorDefinitionRepNode) element).getIndicatorDefinition()
        // .getSqlGenericExpression();
        // if (indiExpression == null || indiExpression.size() == 0) {
        // return false;
        // }
        // }

        return true;
    }
}
