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
package org.talend.cwm.compare.factory.comparisonlevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.i18n.DefaultMessagesImpl;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DQDBFolderRepositoryNode;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC rli class global comment. Detailled comment
 */
public class TableViewComparisonLevel extends AbstractTableComparisonLevel {

    protected static Logger log = Logger.getLogger(TableViewComparisonLevel.class);

    private static final List<TdColumn> EMPTY_COLUMN_LIST = Collections.emptyList();

    private String tmpConceptName = StringUtils.EMPTY;

    private Map<String, String[]> tmpTaggedValuesMap = new HashMap<String, String[]>();

    public TableViewComparisonLevel(DBColumnFolderRepNode dbFolderNode) {
        super(dbFolderNode);
    }

    public TableViewComparisonLevel(ColumnSet columnSet) {
        super(null);
        selectedObj = columnSet;

    }

    @Override
    protected boolean compareWithReloadObject() throws ReloadCompareException {
        // remove the jrxml from the ResourceSet before doMatch
        Map<ResourceSet, List<Resource>> rsJrxmlMap = removeJrxmlsFromResourceSet();
        DBColumnFolderRepNode columnFolderRepNode = (DBColumnFolderRepNode) selectedObj;

        // Compare the two models
        EMFCompare comparator = createDefaultEMFCompare();
        IComparisonScope scope = new DefaultComparisonScope(columnFolderRepNode.getColumnSet(), getSavedReloadObject(), null);
        Comparison compare = comparator.compare(scope);
        // add the jrxml into the ResourceSet after doMatch
        addJrxmlsIntoResourceSet(rsJrxmlMap);

        EList<Diff> differences = compare.getDifferences();
        for (Diff diff : differences) {
            // ignore the move Kind
            if (diff.getKind() == DifferenceKind.MOVE) {
                continue;
            }
            // copy right to left
            copyRightToLeft(diff);
        }

        return true;
    }

    @Override
    protected Connection findDataProvider() {
        Connection provider = null;
        if (selectedObj instanceof DBColumnFolderRepNode) {
            DBColumnFolderRepNode columnFolderRepNode = (DBColumnFolderRepNode) selectedObj;
            ColumnSet columnSet = columnFolderRepNode.getColumnSet();
            if (columnSet != null) {
                Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(columnSet);
                provider = ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);
            }
        } else if (selectedObj instanceof RepositoryNode) {
            Item connItem = ((RepositoryNode) selectedObj).getObject().getProperty().getItem();
            provider = ((ConnectionItem) connItem).getConnection();
        } else {
            ColumnSet columnSet = (ColumnSet) selectedObj;
            Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(columnSet);
            provider = ConnectionHelper.getTdDataProvider(parentCatalogOrSchema);
        }

        return provider;
    }

    /*
     * To add the connection check : if the connection is not available, throw exception
     */
    @Override
    protected IFile createTempConnectionFile() throws ReloadCompareException {

        // TDQ-9263 add the connection check
        ReturnCode connectionAvailable = ConnectionUtils.isConnectionAvailable(oldDataProvider);
        if (!connectionAvailable.isOk()) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("TableViewComparisonLevel.ConnectionNotAvaiable")); //$NON-NLS-1$
        }

        return super.createTempConnectionFile();
    }

    @Override
    public Connection reloadCurrentLevelElement() throws ReloadCompareException {
        if (selectedObj instanceof DQDBFolderRepositoryNode) {
            DQDBFolderRepositoryNode columnFolderRepNode = (DQDBFolderRepositoryNode) selectedObj;
            columnFolderRepNode.setReload(true);
        }
        return super.reloadCurrentLevelElement();
    }

    @Override
    protected EObject getSavedReloadObject() throws ReloadCompareException {
        ColumnSet selectedColumnSet = getCurrentColumnSet();
        ColumnSet toReloadcolumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, tempReloadProvider);

        // ADD TDQ-11146 msjian: to save the TaggedValues before clear
        if (PluginChecker.isTDQLoaded()) {
            tmpConceptName = TaggedValueHelper.getValueString(TaggedValueHelper.CONCEPT_NAME, toReloadcolumnSet);
            EList<Feature> feature = toReloadcolumnSet.getFeature();
            tmpTaggedValuesMap = new HashMap<String, String[]>();
            for (Feature fea : feature) {
                String[] values = new String[] { StringUtils.EMPTY, StringUtils.EMPTY };
                values[0] = TaggedValueHelper.getValueString(TaggedValueHelper.SEMANTIC_NAME, fea);
                values[1] = TaggedValueHelper.getValueString(TaggedValueHelper.CONTENT_TYPE, fea);
                if (StringUtils.isNotBlank(values[0]) || StringUtils.isNotBlank(values[1])) {
                    tmpTaggedValuesMap.put(fea.getName(), values);
                }
            }
        }
        // TDQ-11146~

        // MOD scorreia 2009-01-29 clear content of findMatchedColumnSet
        ColumnSetHelper.setColumns(toReloadcolumnSet, EMPTY_COLUMN_LIST);
        toReloadcolumnSet.getOwnedElement().clear();
        try {
            DqRepositoryViewService.getColumns(tempReloadProvider, toReloadcolumnSet, true);
            // MOD mzhao 2009-11-12 save to resoure after reload.
            util.saveResource(toReloadcolumnSet.eResource());
        } catch (Exception e1) {
            throw new ReloadCompareException(e1);
        }
        // MOD scorreia 2009-01-29 columns are stored in the table
        // ColumnSetHelper.setColumns(toReloadcolumnSet, columns);
        return toReloadcolumnSet;
    }

    @Override
    protected Resource getLeftResource() throws ReloadCompareException {
        ColumnSet selectedColumnSet = null;
        if (selectedObj instanceof DBColumnFolderRepNode) {
            DBColumnFolderRepNode columnFolderRepNode = (DBColumnFolderRepNode) selectedObj;
            selectedColumnSet = columnFolderRepNode.getColumnSet();
        } else if (selectedObj instanceof RepositoryNode) {
            selectedColumnSet = (ColumnSet) RepositoryNodeHelper.getMetadataElement((RepositoryNode) selectedObj);
        } else {
            selectedColumnSet = (ColumnSet) selectedObj;
        }
        ColumnSet findMatchedColumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, copyedDataProvider);
        List<TdColumn> columnList = new ArrayList<TdColumn>();
        columnList.addAll(ColumnSetHelper.getColumns(findMatchedColumnSet));
        Resource leftResource = copyedDataProvider.eResource();
        leftResource.getContents().clear();
        for (TdColumn column : columnList) {
            DQStructureComparer.clearSubNode(column);
            leftResource.getContents().add(column);
        }
        EMFSharedResources.getInstance().saveResource(leftResource);
        return upperCaseResource(leftResource);
    }

    @Override
    protected Resource getRightResource() throws ReloadCompareException {
        ColumnSet selectedColumnSet = null;
        if (selectedObj instanceof RepositoryNode) {
            selectedColumnSet = (ColumnSet) RepositoryNodeHelper.getMetadataElement((RepositoryNode) selectedObj);
        } else if (selectedObj instanceof DBColumnFolderRepNode) {
            DBColumnFolderRepNode columnFolderRepNode = (DBColumnFolderRepNode) selectedObj;
            selectedColumnSet = columnFolderRepNode.getColumnSet();
        } else {
            selectedColumnSet = (ColumnSet) selectedObj;
        }
        ColumnSet findMatchedColumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, tempReloadProvider);
        List<TdColumn> columns = null;
        try {
            // MOD scorreia 2009-01-29 clear content of findMatchedColumnSet
            ColumnSetHelper.setColumns(findMatchedColumnSet, EMPTY_COLUMN_LIST);
            columns = DqRepositoryViewService.getColumns(tempReloadProvider, findMatchedColumnSet, true);
        } catch (Exception e1) {
            throw new ReloadCompareException(e1);
        }
        // MOD scorreia 2009-01-29 columns are stored in the table
        // ColumnSetHelper.addColumns(findMatchedColumnSet, columns);

        URI uri = tempReloadProvider.eResource().getURI();
        Resource rightResource = null;
        rightResource = EMFSharedResources.getInstance().getResource(uri, true);
        if (rightResource == null) {
            throw new ReloadCompareException(DefaultMessagesImpl.getString("TableViewComparisonLevel.NoFactoryFoundForURI", uri)); //$NON-NLS-1$
        }
        rightResource.getContents().clear();
        for (TdColumn column : columns) {
            DQStructureComparer.clearSubNode(column);
            rightResource.getContents().add(column);
        }
        EMFSharedResources.getInstance().saveResource(rightResource);
        return upperCaseResource(rightResource);
    }

    /**
     * DOC msjian Comment method "getCurrentColumnSet".
     * 
     * @return
     */
    private ColumnSet getCurrentColumnSet() {
        ColumnSet columnSet = null;
        if (selectedObj instanceof DBColumnFolderRepNode) {
            DBColumnFolderRepNode columnFolderRepNode = (DBColumnFolderRepNode) selectedObj;
            columnSet = columnFolderRepNode.getColumnSet();
        } else if (selectedObj instanceof ColumnSet) {
            columnSet = (ColumnSet) selectedObj;
        }
        return columnSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.compare.factory.comparisonlevel.AbstractComparisonLevel#resetTaggedValues()
     */
    @Override
    protected void resetTaggedValues() throws ReloadCompareException {
        super.resetTaggedValues();

        if (PluginChecker.isTDQLoaded()) {
            ColumnSet selectedColumnSet = getCurrentColumnSet();
            ColumnSet oldColumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, oldDataProvider);
            // reset table's taggedvalues "CONCEPT_NAME".
            TaggedValueHelper.setTaggedValue(oldColumnSet, TaggedValueHelper.CONCEPT_NAME, tmpConceptName);

            EList<Feature> oldFeatures = oldColumnSet.getFeature();
            for (Feature fea : oldFeatures) {
                // reset the column's tagged values "SEMATIC_NAME", "Content Type".
                String[] strings = tmpTaggedValuesMap.get(fea.getName());
                if (strings != null) {
                    TaggedValueHelper.setTaggedValue(fea, TaggedValueHelper.SEMANTIC_NAME, strings[0]);
                    TaggedValueHelper.setTaggedValue(fea, TaggedValueHelper.CONTENT_TYPE, strings[1]);
                }
            }
        }
    }
}
