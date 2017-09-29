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
package org.talend.dq.pattern;

import org.apache.log4j.Logger;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class PatternBuilder {

    static Logger log = Logger.getLogger(PatternBuilder.class);

    private Pattern pattern;

    private boolean initialized = false;

    /**
     * DOC bzhou Comment method "initializePattern".
     * 
     * @param patternName
     * @return
     */
    public boolean initializePattern(String patternName) {
        if (initialized) {
            log.warn("Pattern already initialized. ");//$NON-NLS-1$
            return false;
        }

        this.pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName(patternName);

        return true;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
