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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof IFolder) {
            return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
        }

        if (element instanceof IFile && FactoriesUtil.DQRULE.equals(((IFile) element).getFileExtension())) {
            IFile file = (IFile) element;
            ModelElement me = DQRuleResourceFileHelper.getInstance().getModelElement(file);
            // add support for match rule
            ModelElement modelElement = DQRuleResourceFileHelper.getInstance().getModelElement(file);
            ModelElement rule = DQRuleResourceFileHelper.getInstance().doSwitch(modelElement);
            if (rule != null && rule instanceof MatchRuleDefinition) {
                ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.MATCH_RULE_ICON);
                return imageDescriptor.createImage();
            }// ~

            ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.DQ_RULE);
            if (rule != null && rule instanceof WhereRule) {
                boolean validStatus = TaggedValueHelper.getValidStatus(rule);
                if (!validStatus) {
                    ImageDescriptor warnImg = PlatformUI.getWorkbench().getSharedImages()
                            .getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK);
                    DecorationOverlayIcon icon = new DecorationOverlayIcon(imageDescriptor.createImage(), warnImg,
                            IDecoration.BOTTOM_RIGHT);
                    imageDescriptor = icon;
                }
            }
            return imageDescriptor.createImage();
        }

        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IFile && FactoriesUtil.DQRULE.equals(((IFile) element).getFileExtension())) {
            IFile file = (IFile) element;
            ModelElement modelElement = DQRuleResourceFileHelper.getInstance().getModelElement(file);
            if (DQRuleResourceFileHelper.getInstance().doSwitch(modelElement) != null) {
                return modelElement.getName();
            }
            return DQRuleResourceFileHelper.getInstance().getModelElement(file).getName();

        }

        if (element instanceof IFolder) {
            return ((IFolder) element).getName();
        }

        return ""; //$NON-NLS-1$
    }
}
