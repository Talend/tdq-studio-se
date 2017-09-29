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

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class AnalysisRepNode extends DQRepositoryNode {

    /**
     * AnalysisRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     * 
     */
    public AnalysisRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public Analysis getAnalysis() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return ((TDQAnalysisItem) property.getItem()).getAnalysis();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        List<IRepositoryNode> anaElement = new ArrayList<IRepositoryNode>();
        RepositoryNode parent = this.getParent();
        if (!(parent instanceof ReportSubFolderRepNode)) {
            AnalysisSubFolderRepNode childNodeFolder = new AnalysisSubFolderRepNode(null, this, ENodeType.SIMPLE_FOLDER,
                    getProject());
            childNodeFolder.setProperties(EProperties.LABEL, "analyzed elements"); //$NON-NLS-1$
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            anaElement.add(childNodeFolder);
        }
        // MOD gdbu 2011-7-1 bug : 22204
        return filterResultsIfAny(anaElement);
        // ~22204
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
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
