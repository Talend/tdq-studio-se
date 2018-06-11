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
package org.talend.dataprofiler.core.ui.exchange;

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ExchangeComponentRepNode extends DQRepositoryNode {

    private final IEcosComponent ecosComponent;

    private String label;

    /**
     * ExchangeComponentRepNode constructor.
     * 
     * @param ecosComponent
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public ExchangeComponentRepNode(IEcosComponent ecosComponent, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(null, parent, type, inWhichProject);
        this.type = type;
        this.ecosComponent = ecosComponent;
        this.setId(ecosComponent.getName());
        this.label = ecosComponent.getName();
    }

    public IEcosComponent getEcosComponent() {
        return this.ecosComponent;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label == null ? "" : this.label;//$NON-NLS-1$ 
    }

    @Override
    public IImage getIcon() {
        return null;
    }

    @Override
    public Image getImage() {
        return ImageLib.getImage(ImageLib.EXCHANGE);
    }

}
