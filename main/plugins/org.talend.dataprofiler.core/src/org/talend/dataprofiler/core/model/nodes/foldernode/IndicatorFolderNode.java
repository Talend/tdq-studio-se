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
package org.talend.dataprofiler.core.model.nodes.foldernode;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.analysis.category.CategoryHandler;
import org.talend.dq.nodes.foldernode.AbstractFolderNode;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorFolderNode extends AbstractFolderNode implements IWorkbenchAdapter {

    /**
     * DOC bZhou IndicatorFolderNode constructor comment.
     * 
     * @param name
     */
    public IndicatorFolderNode(String name) {
        super(name);
        setParent(ResourceManager.getLibrariesFolder().getFolder("Indicators")); //$NON-NLS-1$
        loadChildren();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.foldernode.AbstractFolderNode#loadChildren()
     */
    @Override
    public void loadChildren() {
        Map<IndicatorCategory, List<IndicatorDefinition>> categoriesIDMaps = CategoryHandler.getCategoriesIDMaps();
        if (categoriesIDMaps != null) {
            setChildren(categoriesIDMaps.keySet().toArray(new IndicatorCategory[categoriesIDMaps.keySet().size()]));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object o) {
        return getChildren();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
     */
    public ImageDescriptor getImageDescriptor(Object object) {
        return ImageLib.getImageDescriptor(ImageLib.FOLDERNODE_IMAGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
     */
    public String getLabel(Object o) {
        return getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
     */
    public Object getParent(Object o) {
        return getParent();
    }

}
