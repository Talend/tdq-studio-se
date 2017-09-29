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
package org.talend.dataprofiler.core.ui.editor.indicator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorEditorInput implements IEditorInput {

    private IndicatorDefinition indicatorDefinition;

    public IndicatorEditorInput(IndicatorDefinition indicatorDefinition) {
        this.indicatorDefinition = indicatorDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return ImageLib.getImageDescriptor(ImageLib.OPTION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName() {
        return this.indicatorDefinition == null ? "" : this.indicatorDefinition.getName(); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText() {
        return ""; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Getter for indicatorDefinition.
     * 
     * @return the indicatorDefinition
     */
    public IndicatorDefinition getIndicatorDefinition() {
        return indicatorDefinition;
    }

    /**
     * DOC zshen fixed the bug 10996: One more editors open for a system indicator
     * 
     * FIXME should also overwrite the hashCode().
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IndicatorEditorInput)) {
            return false;
        }
        IndicatorEditorInput other = (IndicatorEditorInput) obj;
        return indicatorDefinition.getLabel().equals(other.indicatorDefinition.getLabel());
    }

}
