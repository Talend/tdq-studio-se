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

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class AnalysisSubFolderRepNode extends AnalysisFolderRepNode {

    private static Logger log = Logger.getLogger(AnalysisSubFolderRepNode.class);

    private List<IRepositoryNode> anaElement;

    /**
     * DOC talend AnalysisSubFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public AnalysisSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.nodes.AnalysisFolderRepNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        IRepositoryViewObject anaViewObject = this.getObject();
        anaElement = new ArrayList<IRepositoryNode>();
        if (anaViewObject == null) {
            anaViewObject = this.getParent().getObject();
            TDQAnalysisItem item = (TDQAnalysisItem) anaViewObject.getProperty().getItem();
            Analysis analysis = item.getAnalysis();
            AnalysisContext context = analysis.getContext();
            EList<ModelElement> analysedElements = context.getAnalysedElements();
            // AnalysisResult results = analysis.getResults();
            // EList<Indicator> indicators = results.getIndicators();
            for (ModelElement analyzedElement : analysedElements) {
                // ModelElement analyzedElement = indicator.getAnalyzedElement();
                IRepositoryNode elementNode = doSwichNode(analyzedElement, this);
                if (null == elementNode) {
                    // just when filtering 'elementNode' could be null
                    continue;
                }
                if (elementNode instanceof DBConnectionRepNode) {
                    List<IRepositoryNode> children = elementNode.getChildren();
                    anaElement.addAll(children);
                } else {
                    anaElement.add(elementNode);
                }
            }
            // MOD gdbu 2011-7-1 bug : 22204
            return filterResultsIfAny(anaElement);
        }
        return filterResultsIfAny(super.getChildren());
        // ~22204
    }

    /**
     * DOC klliu Comment method "doSwichNode".
     * 
     * @param analyzedElement
     * @return
     */
    private IRepositoryNode doSwichNode(ModelElement analyzedElement, AnalysisSubFolderRepNode childNodeFolder) {
        Property anaEleProperty = PropertyHelper.getProperty(analyzedElement);
        IRepositoryViewObject medataViewObject = null;
        try {
            medataViewObject = ProxyRepositoryFactory.getInstance().getLastVersion(anaEleProperty.getId());
        } catch (Exception e) {
            log.error(e);
        }

        // RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(analyzedElement);
        // IRepositoryViewObject medataViewObject = null;
        // if (recursiveFind != null) {
        // medataViewObject = recursiveFind.getObject();
        // }
        if (medataViewObject != null) {
            if (analyzedElement instanceof DatabaseConnection) {
                DBConnectionRepNode connNode = new DBConnectionRepNode(medataViewObject, this, ENodeType.REPOSITORY_ELEMENT,
                        getProject());
                connNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
                connNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CONNECTIONS);
                medataViewObject.setRepositoryNode(connNode);
                return connNode;
            } else if (analyzedElement instanceof Catalog) {
                RepositoryNode catalogNode = DQRepNodeCreateFactory.createDBCatalogRepNode(new MetadataCatalogRepositoryObject(
                        medataViewObject, (Catalog) analyzedElement), this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                catalogNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_CATALOG);
                catalogNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_CATALOG);
                medataViewObject.setRepositoryNode(catalogNode);
                return catalogNode;
            } else if (analyzedElement instanceof Schema) {
                RepositoryNode schemaNode = new DBSchemaRepNode(new MetadataSchemaRepositoryObject(medataViewObject,
                        (Schema) analyzedElement), this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                schemaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_SCHEMA);
                schemaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_SCHEMA);
                medataViewObject.setRepositoryNode(schemaNode);
                return schemaNode;
            } else if (analyzedElement instanceof TdTable) {
                DBTableRepNode tableNode = new DBTableRepNode(new TdTableRepositoryObject(medataViewObject,
                        (TdTable) analyzedElement), childNodeFolder, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                tableNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_TABLE);
                tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_TABLE);
                medataViewObject.setRepositoryNode(tableNode);
                return tableNode;
            } else if (analyzedElement instanceof TdView) {
                DBViewRepNode viewNode = new DBViewRepNode(
                        new TdViewRepositoryObject(medataViewObject, (TdView) analyzedElement), childNodeFolder,
                        ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                viewNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_VIEW);
                viewNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_VIEW);
                medataViewObject.setRepositoryNode(viewNode);
                return viewNode;
            } else if (analyzedElement instanceof MetadataColumn) {
                RepositoryNode columnNode = null;
                if (analyzedElement instanceof TdColumn) {
                    columnNode = new DBColumnRepNode(new MetadataColumnRepositoryObject(medataViewObject,
                            (MetadataColumn) analyzedElement), childNodeFolder, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                } else {
                    columnNode = new DFColumnRepNode(new MetadataColumnRepositoryObject(medataViewObject,
                            (MetadataColumn) analyzedElement), this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                }
                columnNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_COLUMN);
                columnNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_COLUMN);
                medataViewObject.setRepositoryNode(columnNode);
                return columnNode;
            }
        }

        return null;
    }

    public String getCount() {
        IRepositoryViewObject anaViewObject = this.getParent().getObject();
        if (anaViewObject.getProperty() != null) {
            TDQAnalysisItem item = (TDQAnalysisItem) anaViewObject.getProperty().getItem();
            Analysis analysis = item.getAnalysis();
            AnalysisContext context = analysis.getContext();
            EList<ModelElement> analysedElements = context.getAnalysedElements();
            if (analysedElements.size() == 1) {
                ModelElement modelElement = analysedElements.get(0);
                if (modelElement instanceof DatabaseConnection) {
                    EList<Package> dataPackage = ((DatabaseConnection) modelElement).getDataPackage();
                    return "(" + dataPackage.size() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            return "(" + analysedElements.size() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return "(0)"; //$NON-NLS-1$
    }

    @Override
    public boolean isVirtualFolder() {
        return this.getObject() == null;
    }

    @Override
    public String getLabel() {
        if (getObject() == null) {

            return this.getProperties(EProperties.LABEL).toString();
            // return this.getObject().getLabel();
        }
        return super.getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        if (getObject() == null) {
            return Messages.getString("AnalysisSubFolderRepNode.analyzedElement") + getCount(); //$NON-NLS-1$
        }
        return getLabelWithCount();
    }
}
