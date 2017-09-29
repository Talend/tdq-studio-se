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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
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
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.helper.ViewHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DQDBFolderRepositoryNode;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC rli class global comment. Detailled comment
 */
public class CatalogSchemaComparisonLevel extends AbstractComparisonLevel {

    private boolean isCompareTabel;

    private boolean isCompareView;

    private Map<String, String> tmpTableTaggedValuesMap = new HashMap<String, String>();

    public CatalogSchemaComparisonLevel(RepositoryNode dbFolderNode) {
        super(dbFolderNode);
        if (dbFolderNode instanceof DBTableFolderRepNode) {
            isCompareTabel = true;
        }

        if (dbFolderNode instanceof DBViewFolderRepNode) {
            isCompareView = true;
        }
    }

    /**
     * get the Package from the Object.
     * 
     * @param object
     * @return a Package or null
     */
    private Package getPackageFromObject(Object object) {
        Package result = null;
        if (object instanceof RepositoryNode) {
            result = getPackageFromRepositoryNode((RepositoryNode) object);
        } else if (object instanceof Package) {
            result = (Package) object;
        }
        return result;
    }

    /**
     * get the Package from the RepositoryNode.
     * 
     * @param node
     * @return a Package or null
     */
    private Package getPackageFromRepositoryNode(RepositoryNode node) {
        Package result = null;
        if (node instanceof DBTableFolderRepNode) {
            DBTableFolderRepNode tableFolderRepNode = (DBTableFolderRepNode) node;
            result = tableFolderRepNode.getPackage();
        } else if (node instanceof DBViewFolderRepNode) {
            DBViewFolderRepNode viewFolderRepNode = (DBViewFolderRepNode) node;
            result = viewFolderRepNode.getPackage();
        } else {
            result = (Package) RepositoryNodeHelper.getMetadataElement((RepositoryNode) selectedObj);
        }
        return result;
    }

    @Override
    protected Connection findDataProvider() {
        Connection provider = null;
        if (selectedObj instanceof RepositoryNode) {
            if (selectedObj instanceof DBTableFolderRepNode || selectedObj instanceof DBViewFolderRepNode) {
                provider = ConnectionHelper.getTdDataProvider(getPackageFromObject(selectedObj));
            }
            Item connItem = null;
            IRepositoryViewObject object = ((RepositoryNode) selectedObj).getObject();
            if (null != object) {
                connItem = object.getProperty().getItem();
            } else {
                connItem = ((RepositoryNode) selectedObj).getParent().getObject().getProperty().getItem();
            }

            provider = ((ConnectionItem) connItem).getConnection();
        } else if (selectedObj instanceof Package) {
            provider = ConnectionHelper.getTdDataProvider((Package) selectedObj);
        }

        return provider;
    }

    @Override
    protected boolean compareWithReloadObject() throws ReloadCompareException {
        // remove the jrxml from the ResourceSet before doMatch
        Map<ResourceSet, List<Resource>> rsJrxmlMap = removeJrxmlsFromResourceSet();
        EMFCompare comparator = createDefaultEMFCompare();
        IComparisonScope scope = new DefaultComparisonScope(getPackageFromObject(selectedObj), getSavedReloadObject(), null);
        Comparison compare = comparator.compare(scope);

        // add the jrxml into the ResourceSet after doMatch
        addJrxmlsIntoResourceSet(rsJrxmlMap);
        EList<Diff> differences = compare.getDifferences();
        for (Diff diff : differences) {
            // ignore the move Kind
            if (diff.getKind() == DifferenceKind.MOVE) {
                continue;
            }
            // ignore others except TdTable and TdView and Related TaggetValue
            if (diff instanceof ReferenceChange) {
                EObject value = ((ReferenceChange) diff).getValue();
                boolean isIgnore = true;
                if (value instanceof TaggedValue) {
                    TdTable tableContianer = SwitchHelpers.TABLE_SWITCH.doSwitch(value.eContainer());
                    if (tableContianer != null) {
                        isIgnore = false;
                    }
                } else if (isValidTableHandle(value) || isValidViewHandle(value)) {
                    isIgnore = false;
                }
                if (!isIgnore) {
                    copyRightToLeft(diff);
                }
            }

        }
        return true;
    }

    @Override
    protected EObject getSavedReloadObject() throws ReloadCompareException {
        Package selectedPackage = getPackageFromObject(selectedObj);

        // ADD TDQ-11146 msjian: to save the TaggedValues before clear
        if (PluginChecker.isTDQLoaded()) {
            tmpTableTaggedValuesMap = new HashMap<String, String>();
            List<TdTable> tables = PackageHelper.getTables(selectedPackage);
            for (TdTable table : tables) {
                String tmpTableConceptName = TaggedValueHelper.getValueString(TaggedValueHelper.CONCEPT_NAME, table);
                if (StringUtils.isNotBlank(tmpTableConceptName)) {
                    tmpTableTaggedValuesMap.put(table.getName(), tmpTableConceptName);
                }
            }

            List<TdView> views = PackageHelper.getViews(selectedPackage);
            for (TdView view : views) {
                String tmpViewConceptName = TaggedValueHelper.getValueString(TaggedValueHelper.CONCEPT_NAME, view);
                if (StringUtils.isNotBlank(tmpViewConceptName)) {
                    tmpTableTaggedValuesMap.put(view.getName(), tmpViewConceptName);
                }
            }
        }
        // TDQ-11146~

        // MOD mzhao 2009-01-20 Extract method findMatchedPackage to
        // DQStructureComparer class
        // for common use.
        Package findMatchPackage = DQStructureComparer.findMatchedPackage(selectedPackage, tempReloadProvider);
        reloadElementOfPackage(findMatchPackage);
        return findMatchPackage;
    }

    @Override
    protected Resource getLeftResource() throws ReloadCompareException {
        Package selectedPackage = getPackageFromObject(selectedObj);
        // if (selectedObj instanceof RepositoryNode) {
        // selectedPackage = (Package) RepositoryNodeHelper.getMetadataElement((RepositoryNode) selectedObj);
        // } else {
        // selectedPackage = (Package) selectedObj;
        // }
        // MOD mzhao 2009-01-20 Extract method findMatchedPackage to
        // DQStructureComparer class
        // for common use.
        Package findMatchPackage = DQStructureComparer.findMatchedPackage(selectedPackage, copyedDataProvider);
        List<ColumnSet> columnSets = new ArrayList<ColumnSet>();

        if (isCompareTabel) {
            columnSets.addAll(PackageHelper.getTables(findMatchPackage));
        }

        if (isCompareView) {
            columnSets.addAll(PackageHelper.getViews(findMatchPackage));
        }

        Resource leftResource = copyedDataProvider.eResource();

        // ComparatorsFactory.sort(columnSets,
        // ComparatorsFactory.MODELELEMENT_COMPARATOR_ID);
        leftResource.getContents().clear();
        for (ColumnSet columnSet : columnSets) {
            // MOD mzhao 2009-01-20 Extract method clearSubNode to
            // DQStructureComparer class
            // for common use.
            DQStructureComparer.clearSubNode(columnSet);
            leftResource.getContents().add(columnSet);
        }
        // }
        EMFSharedResources.getInstance().saveResource(leftResource);
        return upperCaseResource(leftResource);
    }

    @Override
    protected Resource getRightResource() throws ReloadCompareException {
        Package selectedPackage = getPackageFromObject(selectedObj);
        // if (selectedObj instanceof RepositoryNode) {
        // selectedPackage = (Package) RepositoryNodeHelper.getMetadataElement((RepositoryNode) selectedObj);
        // } else {
        // selectedPackage = (Package) selectedObj;
        // }
        // MOD Extract method findMatchedPackage to DQStructureComparer class
        // for common use.
        Package toReloadObj = DQStructureComparer.findMatchedPackage(selectedPackage, tempReloadProvider);

        Resource rightResource = null;
        rightResource = tempReloadProvider.eResource();
        rightResource.getContents().clear();

        List<ColumnSet> columnSetList = reloadElementOfPackage(toReloadObj);
        if (isCompareTabel) {
            for (ColumnSet columnset : TableHelper.getTables(columnSetList)) {
                DQStructureComparer.clearSubNode(columnset);
                rightResource.getContents().add(columnset);
            }
        }

        if (isCompareView) {
            for (ColumnSet columnset : ViewHelper.getViews(columnSetList)) {
                DQStructureComparer.clearSubNode(columnset);
                rightResource.getContents().add(columnset);
            }
        }

        EMFSharedResources.getInstance().saveResource(rightResource);
        return upperCaseResource(rightResource);
    }

    /**
     * DOC rli Comment method "reloadElementOfPackage".
     * 
     * @param toReloadObj
     * @return
     * @throws ReloadCompareException
     */
    private List<ColumnSet> reloadElementOfPackage(Package toReloadObj) throws ReloadCompareException {
        List<ColumnSet> columnSetList = new ArrayList<ColumnSet>();
        try {
            // MOD by msjian bug 2011-5-16 20875, the reload element is not added correctly
            Catalog catalogObj = SwitchHelpers.CATALOG_SWITCH.doSwitch(toReloadObj);
            Schema schemaObj = SwitchHelpers.SCHEMA_SWITCH.doSwitch(toReloadObj);
            if (schemaObj != null) {
                List<ModelElement> elementList = schemaObj.getOwnedElement();
                if (elementList != null && elementList.size() > 0) {
                    elementList.clear();
                }
                List<TdTable> tables = DqRepositoryViewService.getTables(tempReloadProvider, schemaObj, null, true, true);
                SchemaHelper.addTables(tables, schemaObj);
                columnSetList.addAll(tables);

                List<TdView> views = DqRepositoryViewService.getViews(tempReloadProvider, schemaObj, null, true, true);
                SchemaHelper.addViews(views, schemaObj);
                columnSetList.addAll(views);

            } else {
                List<ModelElement> elementList = catalogObj.getOwnedElement();
                if (elementList != null && elementList.size() > 0) {
                    elementList.clear();
                }
                List<TdTable> tables = DqRepositoryViewService.getTables(tempReloadProvider, catalogObj, null, true, true);
                CatalogHelper.addTables(tables, catalogObj);
                columnSetList.addAll(tables);

                List<TdView> views = DqRepositoryViewService.getViews(tempReloadProvider, catalogObj, null, true, true);
                CatalogHelper.addViews(views, catalogObj);
                columnSetList.addAll(views);
            }
            // else {
            // List<TdTable> tables = DqRepositoryViewService.getTables(tempReloadProvider, (Schema) toReloadObj, null,
            // true);
            // SchemaHelper.addTables(tables, (Schema) toReloadObj);
            // columnSetList.addAll(tables);
            //
            // List<TdView> views = DqRepositoryViewService.getViews(tempReloadProvider, (Schema) toReloadObj, null,
            // true);
            // SchemaHelper.addViews(views, (Schema) toReloadObj);
            // columnSetList.addAll(views);
            //
            // }
        } catch (Exception e1) {
            throw new ReloadCompareException(e1);
        }

        // EMFSharedResources.getInstance().saveResource(tempReloadProvider.eResource());
        return columnSetList;

    }

    /**
     * DOC bZhou Comment method "isValidViewHandle".
     * 
     * @param columnSetSwitch
     * @return
     */
    private boolean isValidViewHandle(EObject object) {
        TdView view = SwitchHelpers.VIEW_SWITCH.doSwitch(object);
        return isCompareView && view != null;
    }

    /**
     * DOC bZhou Comment method "isValidTableHandle".
     * 
     * @param columnSetSwitch
     * @return
     */
    private boolean isValidTableHandle(EObject object) {
        TdTable table = SwitchHelpers.TABLE_SWITCH.doSwitch(object);
        return isCompareTabel && table != null;
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
            Package selectedPackage = getPackageFromObject(selectedObj);

            List<TdTable> tables = PackageHelper.getTables(selectedPackage);
            for (TdTable table : tables) {
                // reset the table's tagged values "CONCEPT_NAME".
                String tmpTableConceptName = tmpTableTaggedValuesMap.get(table.getName());
                if (tmpTableConceptName != null) {
                    TaggedValueHelper.setTaggedValue(table, TaggedValueHelper.CONCEPT_NAME, tmpTableConceptName);
                }
            }

            List<TdView> views = PackageHelper.getViews(selectedPackage);
            for (TdView view : views) {
                // reset the view's tagged values "CONCEPT_NAME".
                String tmpViewConceptName = tmpTableTaggedValuesMap.get(view.getName());
                if (tmpViewConceptName != null) {
                    TaggedValueHelper.setTaggedValue(view, TaggedValueHelper.CONCEPT_NAME, tmpViewConceptName);
                }
            }
        }
    }

    @Override
    public Connection reloadCurrentLevelElement() throws ReloadCompareException {
        if (selectedObj instanceof DQDBFolderRepositoryNode) {
            DQDBFolderRepositoryNode columnFolderRepNode = (DQDBFolderRepositoryNode) selectedObj;
            columnFolderRepNode.setReload(true);
        }
        return super.reloadCurrentLevelElement();
    }

}
