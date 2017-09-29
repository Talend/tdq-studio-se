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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.ModelElementMatchEngine;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.DQMatchEngineFactory;
import org.talend.cwm.compare.factory.IComparisonLevel;
import org.talend.cwm.compare.factory.IUIHandler;
import org.talend.cwm.compare.factory.update.AddTdRelationalSwitch;
import org.talend.cwm.compare.factory.update.RemoveTdRelationalSwitch;
import org.talend.cwm.compare.factory.update.UpdateTdRelationalSwitch;
import org.talend.cwm.compare.merge.DQReferenceMerger;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.nodes.foldernode.AbstractDatabaseFolderNode;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.util.CoreSwitch;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractComparisonLevel implements IComparisonLevel {

    protected static final boolean CASE_INSENSTIVE = true;

    // MOD klliu bug 14689 2010-08-04
    protected CoreSwitch<Package> packageSwitch;

    protected UpdateTdRelationalSwitch updateRelationalStructSwitch = new UpdateTdRelationalSwitch();

    protected RemoveTdRelationalSwitch removeRelationalSwitch = new RemoveTdRelationalSwitch();

    protected AddTdRelationalSwitch addRelationalSwitch = new AddTdRelationalSwitch();

    private boolean removeElementConfirm = false;

    protected Object selectedObj;

    private AbstractDatabaseFolderNode dbFolderNode = null;

    protected Connection oldDataProvider;

    protected Connection tempReloadProvider;

    protected Map<String, Object> options;

    protected Connection copyedDataProvider;

    protected IUIHandler guiHandler;

    protected EMFSharedResources util = EMFSharedResources.getInstance();

    public AbstractComparisonLevel(Object selObj) {
        if (selObj instanceof AbstractDatabaseFolderNode) {
            AbstractDatabaseFolderNode fNode = (AbstractDatabaseFolderNode) selObj;
            Package ctatlogSwtich = SwitchHelpers.PACKAGE_SWITCH.doSwitch((EObject) fNode.getParent());
            ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch((EObject) fNode.getParent());
            if (ctatlogSwtich != null) {
                this.selectedObj = ctatlogSwtich;
            } else if (columnSet != null) {

                this.selectedObj = columnSet;
            }
            this.dbFolderNode = fNode;
        } else {
            selectedObj = selObj;
        }
    }

    @Override
    public Connection reloadCurrentLevelElement() throws ReloadCompareException {
        if (!isValid()) {
            return null;
        }
        oldDataProvider = upperCaseConnection(findDataProvider());
        if (oldDataProvider == null) {
            return null;
        }
        IFile tempFile = createTempConnectionFile();

        if (compareWithReloadObject()) {

            // before save, reset the old the taggedvalues
            resetTaggedValues();

            saveReloadResult();
        }
        deleteTempConnectionFile(tempFile);
        return oldDataProvider;
    }

    /**
     * reset the targedvalues
     */
    protected void resetTaggedValues() throws ReloadCompareException {
        // Copy db type and db version tagged values .
        String databaseType = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_NAME, tempReloadProvider);
        String productVersion = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_VERSION, tempReloadProvider);
        TaggedValueHelper.setTaggedValue(oldDataProvider, TaggedValueHelper.DB_PRODUCT_NAME, databaseType);
        TaggedValueHelper.setTaggedValue(oldDataProvider, TaggedValueHelper.DB_PRODUCT_VERSION, productVersion);
    }

    /**
     * Delete temp file
     */
    private void deleteTempConnectionFile(IFile tempFile) {
        DQStructureComparer.deleteFile(tempFile);
    }

    @Override
    public void popComparisonUI(IUIHandler uiHandler) throws ReloadCompareException {
        this.guiHandler = uiHandler;
        if (!isValid()) {
            return;
        }
        oldDataProvider = upperCaseConnection(findDataProvider());
        if (oldDataProvider == null) {
            return;
        }
        // DQStructureComparer.deleteCopiedResourceFile();
        // DQStructureComparer.deleteNeedReloadElementFile();
        createTempConnectionFile();
        createCopyedProvider();
        // MOD mzhao 2009-01-20 Extract method openDiffCompareEditor to class
        // DQStructureComparer.
        // MOD mzhao 2009-03-09 add param dbname for displaying datasource(db
        // name) in compare
        // editor.

        Object needReloadObject = dbFolderNode == null ? selectedObj : dbFolderNode;
        // DQStructureComparer.openDiffCompareEditor(getLeftResource(), getRightResource(), options, guiHandler,
        // DQStructureComparer.getDiffResourceFile(), oldDataProvider.getName(), needReloadObject, false);

        testInit();

    }

    @SuppressWarnings("deprecation")
    protected void createCopyedProvider() throws ReloadCompareException {
        IFile selectedFile = WorkspaceUtils.getModelElementResource(oldDataProvider);
        IFile createNeedReloadElementsFile = DQStructureComparer.getNeedReloadElementsFile();
        IFile copyedFile = DQStructureComparer.copyedToDestinationFile(selectedFile, createNeedReloadElementsFile);
        TypedReturnCode<Connection> returnValue = DqRepositoryViewService.readFromFile(copyedFile);
        if (returnValue.isOk()) {
            copyedDataProvider = returnValue.getObject();
        } else {
            DQStructureComparer.deleteFile(createNeedReloadElementsFile);
            throw new ReloadCompareException(returnValue.getMessage());
        }
    }

    /**
     * DOC rli Comment method "createTempConnectionFile".
     * 
     * @throws ReloadCompareException
     */
    protected IFile createTempConnectionFile() throws ReloadCompareException {
        IFile tempConnectionFile = DQStructureComparer.getTempRefreshFile();
        // MOD mzhao ,Extract method getRefreshedDataProvider to class
        // DQStructureComparer for common use.
        TypedReturnCode<Connection> returnProvider = DQStructureComparer.getRefreshedDataProvider(oldDataProvider);
        if (!returnProvider.isOk()) {
            throw new ReloadCompareException(returnProvider.getMessage());
        }
        tempReloadProvider = upperCaseConnection(returnProvider.getObject());
        tempReloadProvider.setComponent(oldDataProvider.getComponent());
        // MOD mzhao bug:9012 2009-09-08
        ElementWriterFactory.getInstance().createDataProviderWriter()
                .create(tempReloadProvider, tempConnectionFile.getFullPath(), false);
        tempReloadProvider.setComponent(null);
        oldDataProvider.getComponent();
        return tempConnectionFile;
    }

    private void testInit() throws ReloadCompareException {

        URI uri = URI.createPlatformResourceURI(DQStructureComparer.getNeedReloadElementsFile().getFullPath().toString(), false);
        Resource leftResource = EMFSharedResources.getInstance().getResource(uri, true);
        uri = URI.createPlatformResourceURI(DQStructureComparer.getTempRefreshFile().getFullPath().toString(), false);
        Resource rightResource = EMFSharedResources.getInstance().getResource(uri, true);

        // openDiffCompareEditor(leftResource, rightResource, options);
    }

    protected abstract EObject getSavedReloadObject() throws ReloadCompareException;

    protected abstract Resource getRightResource() throws ReloadCompareException;

    protected void saveReloadResult() {

        if (oldDataProvider.getName() == null) {
            oldDataProvider.setName(PropertyHelper.getProperty(oldDataProvider).getLabel());
        }

        // MOD klliu bug 15940 201-09-30
        // MOD msjian 2011-5-23 bug 20875: fix the issue2 error(because two times updateDependecy)
        ElementWriterFactory.getInstance().createDataProviderWriter().save(oldDataProvider, true);
        // PrvResourceFileHelper.getInstance().save(oldDataProvider);
    }

    /**
     * Method "findDataProvider".
     * 
     * @return the data provider of the selected object
     */
    protected abstract Connection findDataProvider();

    protected boolean isValid() {
        return true;
    }

    /**
     * remove the jrxml from the ResourceSet before doMatch, if the ResourceSet is null, return empty Map. Should call
     * this method only before doMatch.
     * 
     * @return
     */
    protected Map<ResourceSet, List<Resource>> removeJrxmlsFromResourceSet() {
        Map<ResourceSet, List<Resource>> map = new HashMap<ResourceSet, List<Resource>>();

        ResourceSet resourceSet = oldDataProvider.eResource() == null ? null : oldDataProvider.eResource().getResourceSet();
        if (resourceSet != null) {
            map.put(resourceSet, ResourceManager.removeJrxmls(resourceSet));
        }

        return map;
    }

    /**
     * add the jrxml into the ResourceSet after doMatch. Should call this method only after doMatch
     * 
     * @param rsJrxmlMap
     */
    protected void addJrxmlsIntoResourceSet(Map<ResourceSet, List<Resource>> rsJrxmlMap) {
        if (!rsJrxmlMap.isEmpty()) {
            ResourceSet resourceSet = rsJrxmlMap.keySet().iterator().next();
            List<Resource> jrxmlResources = rsJrxmlMap.get(resourceSet);
            if (resourceSet != null && !jrxmlResources.isEmpty()) {
                resourceSet.getResources().addAll(jrxmlResources);
            }
        }
    }

    /**
     * Compare the old selected object with reload object(rightResource), and updated the content of old selected
     * object.
     * 
     * @param rightResource
     * @return
     * @throws ReloadCompareException
     */
    protected boolean compareWithReloadObject() throws ReloadCompareException {
        Map<ResourceSet, List<Resource>> rsJrxmlMap = removeJrxmlsFromResourceSet();
        EMFCompare comparator = createDefaultEMFCompare();
        IComparisonScope scope = new DefaultComparisonScope(oldDataProvider, getSavedReloadObject(), null);
        Comparison compare = comparator.compare(scope);

        // add the jrxml into the ResourceSet after doMatch
        addJrxmlsIntoResourceSet(rsJrxmlMap);
        EList<Diff> differences = compare.getDifferences();
        for (Diff diff : differences) {
            // ignore the move Kind
            if (diff.getKind() == DifferenceKind.MOVE) {
                continue;
            }
            copyRightToLeft(diff);
        }
        return true;
    }

    protected abstract Resource getLeftResource() throws ReloadCompareException;

    protected void popRemoveElementConfirm() {
        if (!removeElementConfirm) {
            final Connection provider = oldDataProvider;
            if (guiHandler != null) {
                guiHandler.popRemoveElement(provider);
            }
            removeElementConfirm = true;
        }
    }

    protected Resource upperCaseResource(Resource res) {
        if (CASE_INSENSTIVE) {
            EList<EObject> contents = res.getContents();
            for (EObject eo : contents) {
                upperCase(eo);
            }
        }
        return res;
    }

    protected Connection upperCaseConnection(Connection connection) {
        if (CASE_INSENSTIVE) {
            EList<ModelElement> ownedElement = connection.getOwnedElement();
            for (ModelElement me : ownedElement) {
                upperCase(me);
            }
        }
        return connection;
    }

    private void upperCase(EObject eObject) {
        Package pckg = SwitchHelpers.PACKAGE_SWITCH.doSwitch(eObject);
        ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(eObject);
        TdColumn column = SwitchHelpers.COLUMN_SWITCH.doSwitch(eObject);
        if (column != null) {
            column.setName(column.getName() == null ? "" : column.getName().toUpperCase());//$NON-NLS-1$
            column.setLabel(column.getLabel() == null ? "" : column.getLabel().toUpperCase());//$NON-NLS-1$
            column.getSqlDataType().setName(
                    column.getSqlDataType().getName() == null ? "" : column.getSqlDataType().getName().toUpperCase());//$NON-NLS-1$
            column.setContentType(column.getContentType() == null ? "" : column.getContentType().toUpperCase());//$NON-NLS-1$
            column.setSourceType(column.getSourceType() == null ? "" : column.getSourceType().toUpperCase());//$NON-NLS-1$
        } else if (pckg != null) {
            pckg.setName(pckg.getName() == null ? "" : pckg.getName().toUpperCase());//$NON-NLS-1$
        } else if (columnSet != null) {
            columnSet.setName(columnSet.getName() == null ? "" : columnSet.getName().toUpperCase());//$NON-NLS-1$
        }
    }

    /**
     * 
     * create a EMFCompare with default configuration.
     * 
     * @return
     */
    protected EMFCompare createDefaultEMFCompare() {
        // Configure EMF Compare
        IEObjectMatcher matcher = ModelElementMatchEngine.createDQEObjectMatcher(UseIdentifiers.NEVER);
        DefaultEqualityHelperFactory equalityHelperFactory = new DefaultEqualityHelperFactory();
        IComparisonFactory comparisonFactory = new DefaultComparisonFactory(equalityHelperFactory);
        IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
        // IMatchEngine.Factory.Registry matchEngineRegistry =
        // EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry();
        DQMatchEngineFactory matchEngineFactoryImpl = new DQMatchEngineFactory(matcher, comparisonFactory);
        matchEngineRegistry.add(matchEngineFactoryImpl);

        Builder builder = EMFCompare.builder();
        builder.setDiffEngine(diffEngineWithFilter());
        EMFCompare comparator = builder.setMatchEngineFactoryRegistry(matchEngineRegistry).build();
        return comparator;

    }

    /**
     * 
     * ignore the difference of id(not xmi id) Atrribute.
     * 
     * @return
     */
    protected IDiffEngine diffEngineWithFilter() {
        IDiffProcessor diffProcessor = new DiffBuilder();
        IDiffEngine diffEngine = new DefaultDiffEngine(diffProcessor) {

            @Override
            protected FeatureFilter createFeatureFilter() {
                return new FeatureFilter() {

                    // @Override
                    @Override
                    public boolean checkForOrderingChanges(EStructuralFeature feature) {
                        return false;
                    }

                    @Override
                    protected boolean isIgnoredAttribute(EAttribute attribute) {
                        return "id".equals(attribute.getName()) || attribute.isID() || super.isIgnoredAttribute(attribute); //$NON-NLS-1$
                    }

                };
            }
        };
        return diffEngine;
    }

    /**
     * DOC qiongli Comment method "copyRightToLeft".
     * 
     * @param diff
     */
    protected void copyRightToLeft(Diff diff) {
        // copy right to left
        Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
        DQReferenceMerger dqReferenceMerger = new DQReferenceMerger();
        dqReferenceMerger.setRanking(50);
        registry.add(dqReferenceMerger);
        final IMerger merger = registry.getHighestRankingMerger(diff);
        merger.copyRightToLeft(diff, new BasicMonitor());
    }

}
