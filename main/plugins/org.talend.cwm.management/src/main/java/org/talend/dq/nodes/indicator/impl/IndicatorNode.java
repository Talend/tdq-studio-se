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
package org.talend.dq.nodes.indicator.impl;

import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.nodes.indicator.AbstractNode;
import org.talend.dq.nodes.indicator.IIndicatorNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * @author rli
 * 
 */
public class IndicatorNode extends AbstractNode {

    public IndicatorNode(IndicatorEnum indicatorEnum) {
        super(indicatorEnum);
    }

    public IndicatorNode(String label) {
        super(label);
    }

    public boolean isIndicatorEnumNode() {
        return true;
    }

    public IIndicatorNode[] getChildren() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.AbstractIndicatorNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.AbstracNode#getLabel()
     */
    @Override
    public String getLabel() {
        if (getIndicatorInstance() != null) {
            IndicatorDefinition define = getIndicatorInstance().getIndicatorDefinition();
            if (define != null) {
                // MOD yyin 20130118 make it international
                return InternationalizationUtil.getDefinitionInternationalizationLabel(PropertyHelper.getProperty(define));
            }
        }

        return PluginConstant.EMPTY_STRING;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#getImage()
     */
    public String getImageName() {
        return "IndicatorDefinition.png"; //$NON-NLS-1$
    }
}
