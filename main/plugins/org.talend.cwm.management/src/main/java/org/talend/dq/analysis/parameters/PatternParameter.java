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

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class PatternParameter extends ConnectionParameter {

    private String expression;

    private String language;

    /**
     * DOC bzhou PatternParameter constructor comment.
     * 
     * @param paramType
     */
    public PatternParameter() {
        super(EParameterType.PATTERN);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExpression() {
        return expression;
    }

    public String getLanguage() {
        return language;
    }
}
