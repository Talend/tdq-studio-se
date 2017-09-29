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
package org.talend.dq.analysis.parameters;

import org.talend.dataquality.record.linkage.constant.RecordMatcherType;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class DQMatchRuleParameter extends ConnectionParameter {

    private RecordMatcherType defaultAlgorithmType = RecordMatcherType.simpleVSRMatcher;

    public DQMatchRuleParameter() {
        super(EParameterType.DQRULE);
    }

    /**
     * Sets the defaultAlgorithmType.
     * 
     * @param defaultAlgorithmType the defaultAlgorithmType to set
     */
    public void setDefaultAlgorithmType(RecordMatcherType defaultAlgorithmType) {
        this.defaultAlgorithmType = defaultAlgorithmType;
    }

    /**
     * Getter for defaultAlgorithmType.
     * 
     * @return the defaultAlgorithmType
     */
    public RecordMatcherType getDefaultAlgorithmType() {
        return this.defaultAlgorithmType;
    }

}
