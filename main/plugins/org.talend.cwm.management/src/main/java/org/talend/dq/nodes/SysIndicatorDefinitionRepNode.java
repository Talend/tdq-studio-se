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
package org.talend.dq.nodes;

import java.util.List;

import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class SysIndicatorDefinitionRepNode extends DQRepositoryNode {

    boolean isSystemIndicator = false;

    /**
     * IndicatorDefinitionRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public SysIndicatorDefinitionRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public IndicatorDefinition getIndicatorDefinition() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return ((TDQIndicatorDefinitionItem) property.getItem()).getIndicatorDefinition();
        }
        return null;
    }

    public void setSystemIndicator(boolean isSystemIndicator) {
        this.isSystemIndicator = isSystemIndicator;
    }

    public boolean isSystemIndicator() {
        return this.isSystemIndicator;
    }

    /**
     * MOD 20130122 yyin TDQ-3249, make the system indicator display international. but for the user defined indicator,
     * no need.
     */
    @Override
    public String getLabel() {
        if (this.getIndicatorDefinition() != null && !this.getIndicatorDefinition().eIsProxy()) {
            if (this.isSystemIndicator) {
                Property property = PropertyHelper.getProperty(this.getIndicatorDefinition());
                // MOD sizhaoliu TDQ-7454 internationalize the display name here
                return InternationalizationUtil.getDefinitionInternationalizationLabel(property.getLabel());
            }
            if (this.getIndicatorDefinition().getName() != null) {
                return this.getIndicatorDefinition().getName();
            }
        }
        return super.getLabel();
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        return filterResultsIfAny(super.getChildren());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel() + " " + getObject().getVersion(); //$NON-NLS-1$
    }
}
