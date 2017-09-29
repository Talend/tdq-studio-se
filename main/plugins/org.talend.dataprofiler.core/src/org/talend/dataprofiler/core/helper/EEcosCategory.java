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
package org.talend.dataprofiler.core.helper;

import org.apache.commons.lang.StringUtils;
import org.talend.resource.EResourceConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public enum EEcosCategory {

    SQL("SQL", EResourceConstant.PATTERN_SQL), //$NON-NLS-1$
    REGEX("Regex", EResourceConstant.PATTERN_REGEX), //$NON-NLS-1$
    INDICATOR("Indicator", EResourceConstant.USER_DEFINED_INDICATORS), //$NON-NLS-1$
    RULES("ParserRule", EResourceConstant.RULES_PARSER); //$NON-NLS-1$

    private String name;

    private EResourceConstant resource;

    EEcosCategory(String name, EResourceConstant resource) {
        this.name = name;
        this.resource = resource;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for resource.
     * 
     * @return the resource
     */
    public EResourceConstant getResource() {
        return this.resource;
    }

    /**
     * DOC bZhou Comment method "getEcosCategory".
     * 
     * @param categoryName
     * @return
     */
    public static EEcosCategory getEcosCategory(String categoryName) {
        for (EEcosCategory category : values()) {
            if (StringUtils.equals(categoryName, category.name)) {
                return category;
            }
        }

        return null;
    }
}
