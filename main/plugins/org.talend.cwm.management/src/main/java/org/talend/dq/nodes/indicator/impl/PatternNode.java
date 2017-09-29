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

import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.DomainFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dq.nodes.indicator.AbstractNode;
import org.talend.dq.nodes.indicator.IIndicatorNode;

/**
 * created by talend on Dec 30, 2014 Detailled comment
 * 
 */
public class PatternNode extends AbstractNode {

    private Pattern pattern = null;

    /**
     * DOC talend PatternNode constructor comment.
     * 
     * @param label
     */
    public PatternNode(String label, Pattern pattern) {
        super(label);
        this.pattern = pattern;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#getChildren()
     */
    public IIndicatorNode[] getChildren() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#isIndicatorEnumNode()
     */
    public boolean isIndicatorEnumNode() {
        return true;
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
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#getImageName()
     */
    public String getImageName() {
        return "pattern.png"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.AbstractNode#getIndicatorInstance()
     */
    @Override
    public Indicator getIndicatorInstance() {
        Indicator patternIndicatorInstance = super.getIndicatorInstance();
        IndicatorParameters createIndicatorParameter = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        createDomain.getPatterns().add(pattern);
        createIndicatorParameter.setDataValidDomain(createDomain);
        patternIndicatorInstance.setParameters(createIndicatorParameter);

        return patternIndicatorInstance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#createNewIndicatorInstance()
     */
    @Override
    public Indicator createNewIndicatorInstance() {
        Indicator patternIndicatorInstance = super.createNewIndicatorInstance();
        IndicatorParameters createIndicatorParameter = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        Domain createDomain = DomainFactory.eINSTANCE.createDomain();
        createDomain.getPatterns().add(pattern);
        createIndicatorParameter.setDataValidDomain(createDomain);
        patternIndicatorInstance.setParameters(createIndicatorParameter);

        return patternIndicatorInstance;
    }

}
