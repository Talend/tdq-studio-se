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
package org.talend.dataprofiler.core.ui.utils;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UdiLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof IFolder) {
            return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
        }

        if (element instanceof IFile) {
            IndicatorDefinition findUdi = IndicatorResourceFileHelper.getInstance().findIndDefinition((IFile) element);
            boolean validStatus = TaggedValueHelper.getValidStatus(findUdi);
            ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.IND_DEFINITION);
            if (!validStatus) {
                ImageDescriptor warnImg = PlatformUI.getWorkbench().getSharedImages()
                        .getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK);
                DecorationOverlayIcon icon = new DecorationOverlayIcon(imageDescriptor.createImage(), warnImg,
                        IDecoration.BOTTOM_RIGHT);
                imageDescriptor = icon;
            }
            return imageDescriptor.createImage();
        }

        if (element instanceof File) {
            return ImageLib.getImage(ImageLib.JAR_FILE);
        }

        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            IndicatorDefinition udi = IndicatorResourceFileHelper.getInstance().findIndDefinition(file);
            if (udi != null) {
                return udi.getName();
            }
        }

        if (element instanceof IFolder) {
            return ((IFolder) element).getName();
        }

        if (element instanceof File) {
            File file = (File) element;
            return file.getName();
        }

        return ""; //$NON-NLS-1$
    }

}
