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
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.nodes.ReportSubFolderRepNode.ReportSubFolderType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ReportRepNode extends DQRepositoryNode {

    public static final String ANA_FLODER = "Analyzes";//$NON-NLS-1$

    public static final String GEN_FLODER = "Generated Documents";//$NON-NLS-1$

    /**
     * ReportRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public ReportRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public Report getReport() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return ((TDQReportItem) property.getItem()).getReport();
        }
        return null;
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        List<IRepositoryNode> childrenNodes = new ArrayList<IRepositoryNode>();
        ReportSubFolderRepNode anaNodeFolder = new ReportSubFolderRepNode(null, this, ENodeType.SIMPLE_FOLDER, getProject());
        anaNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
        anaNodeFolder.setProperties(EProperties.LABEL, ANA_FLODER);
        anaNodeFolder.setReportSubFolderType(ReportSubFolderType.ANALYSIS);
        anaNodeFolder.setReport(this.getReport());
        childrenNodes.add(anaNodeFolder);
        ReportSubFolderRepNode grenNodeFolder = new ReportSubFolderRepNode(null, this, ENodeType.SIMPLE_FOLDER, getProject());
        grenNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_REPORT_ELEMENT);
        grenNodeFolder.setProperties(EProperties.LABEL, GEN_FLODER);
        grenNodeFolder.setReportSubFolderType(ReportSubFolderType.GENERATED_DOCS);
        grenNodeFolder.setReport(this.getReport());
        childrenNodes.add(grenNodeFolder);
        return childrenNodes;
    }

    @Override
    public String getLabel() {
        if (this.getReport() != null && this.getReport().getName() != null) {
            return this.getReport().getName();
        }
        return super.getLabel();
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
