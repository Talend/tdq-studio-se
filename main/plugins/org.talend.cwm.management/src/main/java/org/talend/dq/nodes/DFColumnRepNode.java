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

import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.TalendTypeConvert;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DFColumnRepNode extends ColumnRepNode {

    /**
     * DOC qiongli DFColumnRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DFColumnRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        metadataColumnRepositoryObject = (MetadataColumnRepositoryObject) object;
    }

    public MetadataColumn getMetadataColumn() {
        return this.metadataColumnRepositoryObject.getTdColumn();
    }

    @Override
    public String getLabel() {
        if (this.getMetadataColumn() != null) {
            // MOD gdbu 2011-7-1 bug : 22204
            return this.getMetadataColumn().getLabel();
            // ~22204
        }
        return super.getLabel();
    }

    public String getNodeDataType() {
        String convertToJavaType = TalendTypeConvert.convertToJavaType(this.getMetadataColumn().getTalendType());
        return convertToJavaType;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getId() + "(" + getNodeDataType() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
