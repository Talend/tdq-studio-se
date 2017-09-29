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

import org.talend.dataquality.analysis.AnalysisType;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalysisParameter extends ConnectionParameter {

    private String analysisTypeName;

    public AnalysisParameter() {
        super(EParameterType.ANALYSIS);
    }

    /**
     * Getter for analysisTypeName.
     * 
     * @return the analysisTypeName
     */
    public String getAnalysisTypeName() {
        return analysisTypeName;
    }

    /**
     * Sets the analysisTypeName.
     * 
     * @param analysisTypeName the analysisTypeName to set
     */
    public void setAnalysisTypeName(String typeName) {
        analysisTypeName = typeName;
    }

    /**
     * DOC bzhou Comment method "getAnalysisType".
     * 
     * @return
     */
    public AnalysisType getAnalysisType() {
        return analysisTypeName != null ? AnalysisType.get(analysisTypeName) : null;
    }
}
