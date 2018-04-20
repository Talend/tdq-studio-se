// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.helper;

import java.io.File;

import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * Record the order of our main tree nodes
 */
public enum EMainNodesTreeType {

    DATAPROFILING(ERepositoryObjectType.TDQ_DATA_PROFILING),
    LIBRARIES(ERepositoryObjectType.TDQ_LIBRARIES),
    CONTEXTS(ERepositoryObjectType.CONTEXT),
    METADATA(ERepositoryObjectType.METADATA),
    RECYCLE_BIN(ERepositoryObjectType.RECYCLE_BIN),
    OTHERS(null);

    private ERepositoryObjectType type = null;

    private EMainNodesTreeType(ERepositoryObjectType type) {
        this.type = type;
    }

    /**
     * 
     * Get enum type by ERepositoryObjectType
     * 
     * @param type
     * @return
     */
    public static EMainNodesTreeType findByType(ERepositoryObjectType type) {
        for (EMainNodesTreeType nodeType : values()) {
            if (nodeType.type == type) {
                return nodeType;
            }
        }
        return OTHERS;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    protected ERepositoryObjectType getType() {
        return this.type;
    }

    public static EMainNodesTreeType findByFile(File file) {
        for (EMainNodesTreeType nodeType : values()) {
            if (file.getPath().contains(nodeType.type.getFolder())) {
                return nodeType;
            }
        }
        return OTHERS;

    }

}
