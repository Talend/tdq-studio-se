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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.dataquality.record.linkage.ui.composite.utils.ImageLib;

/**
 * 
 * created by zshen on Nov 19, 2013 Detailled comment
 * 
 */
public class CustomMatcherLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof File) {
            return ImageLib.getImage(ImageLib.JAR_FILE);
        }

        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            return file.getName();
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
