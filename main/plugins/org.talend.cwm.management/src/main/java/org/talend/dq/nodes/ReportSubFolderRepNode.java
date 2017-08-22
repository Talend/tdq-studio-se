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
package org.talend.dq.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ReportFileHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ReportSubFolderRepNode extends ReportFolderRepNode {

    private static Logger log = Logger.getLogger(ReportSubFolderRepNode.class);

    private ReportSubFolderType reportSubFolderType;

    public ReportSubFolderType getReportSubFolderType() {
        return this.reportSubFolderType;
    }

    public void setReportSubFolderType(ReportSubFolderType reportSubFolderType) {
        this.reportSubFolderType = reportSubFolderType;
    }

    private Report report;

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    private List<IRepositoryNode> reportSubFolderChildren = new ArrayList<IRepositoryNode>();

    public List<IRepositoryNode> getReportSubFolderChildren() {
        return this.reportSubFolderChildren;
    }

    /**
     * DOC klliu ReportSubFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public ReportSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        this.reportSubFolderType = ReportSubFolderType.SUB_FOLDER;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        if (this.getReport() != null) {
            try {
                if (ReportSubFolderType.ANALYSIS.equals(getReportSubFolderType())) {
                    buildChildrenAnalysis(ReportHelper.getAnalyses(this.getReport()));
                } else if (ReportSubFolderType.GENERATED_DOCS.equals(getReportSubFolderType())) {
                    IResource[] repFiles = ReportFileHelper.getReportGeneratedDocs(ResourceFileMap.findCorrespondingFile(this
                            .getReport()));
                    // MOD msjian TDQ-5128 2012-5-4: fixed when the user delete a file from file system display error
                    buildChildrenReportFile(repFiles);
                    // if (repFiles == null || repFiles.length == 0) {
                    // loadChildrenLocalFolder();
                    // } else {
                    // buildChildrenReportFile(repFiles);
                    // }
                    // TDQ-5128~
                }
            } catch (Exception e) {
                log.warn(e, e);
            }
            // MOD gdbu 2011-7-1 bug : 22204
            return filterResultsIfAny(this.getReportSubFolderChildren());
        } else {
            return filterResultsIfAny(super.getChildren());
            // ~22204
        }
    }

    /**
     * build RepositoryNode(Analysis) children according to IResource array.
     * 
     * @param analyses
     * @return
     */
    private List<IRepositoryNode> buildChildrenAnalysis(List<Analysis> analyses) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        for (Analysis analysis : analyses) {
            // MOD gdbu 2011-8-18 TDQ-3301
            Property anaEleProperty = PropertyHelper.getProperty(analysis);
            IRepositoryViewObject viewObject = null;
            if (anaEleProperty != null) {
                viewObject = new RepositoryViewObject(anaEleProperty);
            } else {
                log.error("Analysis [" + analysis.getName() + "] is proxy"); //$NON-NLS-1$//$NON-NLS-2$
            }

            if (null == viewObject) {
                continue;
            }
            // ~TDQ-3301

            ReportAnalysisRepNode node = new ReportAnalysisRepNode(viewObject, this, ENodeType.TDQ_REPOSITORY_ELEMENT,
                    getProject());
            node.setReport(this.getReport());
            node.setId(this.getReport().getName() + analysis.getName());
            nodes.add(node);
        }
        // MOD qiongli TDQ-4773,should clear outside the "if" conditon.
        this.getReportSubFolderChildren().clear();
        if (nodes.size() > 0) {
            this.getReportSubFolderChildren().addAll(nodes);
        }
        return this.getReportSubFolderChildren();
    }

    /**
     * build RepositoryNode(Report File) children according to IResource array.
     * 
     * @param repFiles
     * @return
     */
    private List<IRepositoryNode> buildChildrenReportFile(IResource[] repFiles) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        for (IResource res : repFiles) {
            ReportFileRepNode node = new ReportFileRepNode(null, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
            node.setResource(res);
            node.setId(res.getFullPath().toOSString());
            nodes.add(node);
        }
        // MOD qiongli TDQ-4773,should clear outside the "if" conditon.
        this.getReportSubFolderChildren().clear();
        if (nodes.size() > 0) {
            this.getReportSubFolderChildren().addAll(nodes);
        }
        return this.getReportSubFolderChildren();
    }

    public String getCount() {
        int count = getReportSubFolderChildren().size();
        if (count == 0) {
            count = getChildren().size();
        }
        return "(" + count + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String getLabel() {
        if (ReportSubFolderType.ANALYSIS.equals(getReportSubFolderType())
                || ReportSubFolderType.GENERATED_DOCS.equals(getReportSubFolderType())) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        if (this.getObject() != null) {
            return this.getObject().getLabel();
        }
        if (getObjectType() == null) {
            return ERepositoryObjectType.TDQ_FOLDER_NODE.toString();
        }
        return super.getLabel();
    }

    /**
     * ReportSubFolder's type enum.
     */
    public enum ReportSubFolderType {

        SUB_FOLDER("report.subFolder", "report.subFolder.alias"), //$NON-NLS-1$ //$NON-NLS-2$
        ANALYSIS("report.analysis", "report.analysis.alias"), //$NON-NLS-1$ //$NON-NLS-2$
        GENERATED_DOCS("report.generatedDocs", "report.generatedDocs.alias"); //$NON-NLS-1$ //$NON-NLS-2$

        private String key;

        private String alias;

        public String getKey() {
            return this.key;
        }

        public String getAlias() {
            return this.alias;
        }

        ReportSubFolderType(String key) {
            this(key, key);
        }

        ReportSubFolderType(String key, String alias) {
            this.key = key;
            this.alias = alias;
        }

        public static ReportSubFolderType getTypeFromKey(String key) {
            for (ReportSubFolderType type : ReportSubFolderType.values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    @Override
    public boolean isVirtualFolder() {
        return this.getReport() != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        if (!ReportSubFolderType.SUB_FOLDER.equals(getReportSubFolderType())) {
            if (ReportSubFolderType.GENERATED_DOCS.equals(getReportSubFolderType())) {
                return (String) getProperties(EProperties.LABEL);
            }
            return (String) getProperties(EProperties.LABEL) + getCount();
        }
        return super.getDisplayText();
    }
}
