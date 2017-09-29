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
package org.talend.cwm.compare.ui.actions;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.commons.emf.EMFUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dq.writer.EMFSharedResources;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
@SuppressWarnings("deprecation")
public class RefreshActionDelegate implements IObjectActionDelegate {

    protected static Logger log = Logger.getLogger(RefreshActionDelegate.class);

    protected static final EMFUtil EMFUTIL = EMFSharedResources.getSharedEmfUtil();

    protected IWorkbenchPart workbenchpart;

    protected Object selectedObject;

    protected IFile selectedFileObject;

    protected Catalog selectedCatalog;

    protected Schema selectedSchema;

    protected Connection selectedDataProvider;

    private static final boolean CAT_WITH_PRV = true;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.workbenchpart = targetPart;
    }

    @Override
    public void run(IAction action) {
        // synchronize();
    }

    @SuppressWarnings("unused")
    private void synchronizeDirect() {
        EMFUtil util = EMFSharedResources.getSharedEmfUtil();
        ResourceSet resourceSet = util.getResourceSet();
        URI uri = URI.createPlatformResourceURI(selectedFileObject.getFullPath().toString(), false);
        final Resource resource = resourceSet.createResource(uri);
        Collection<? extends ModelElement> schemata = ConnectionHelper.getSchema(selectedDataProvider);
        if (CAT_WITH_PRV) {
            resource.getContents().addAll(schemata);
        }
    }

    // private void synchronize() {
    // // Load Model from Prv File
    // // Creates the resourceSet where we'll load the models
    // final ResourceSet resourceSet = new ResourceSetImpl();
    // try {
    // final EObject alreadySavedModel = ModelUtils.load(selectedFileObject.toString(), resourceSet);
    //
    // Connection connection = null;
    // if (alreadySavedModel instanceof Connection) {
    // connection = (Connection) alreadySavedModel;
    // }
    // Connection newConn = null;
    // if (connection != null) {
    // String dbUrl = JavaSqlFactory.getURL(connection);
    // String driverClassName = JavaSqlFactory.getDriverClass(connection);
    // EList<EObject> pcObjects = connection.eContents();
    // ListIterator<EObject> liEObject = pcObjects.listIterator();
    //
    // Properties parameters = new Properties();
    //
    // while (liEObject.hasNext()) {
    // EObject eo = liEObject.next();
    // if (eo instanceof TaggedValue) {
    // String tag = ((TaggedValue) eo).getTag();
    // System.out.print(tag);
    // String value = ((TaggedValue) eo).getValue();
    //                        System.out.println(" " + value); //$NON-NLS-1$
    // parameters.put(tag, value);
    // }
    //
    // }
    //
    // System.out.println(dbUrl);
    // System.out.println(driverClassName);
    // DBConnectionParameter dbcp = new DBConnectionParameter();
    //
    // dbcp.setName(connection.getName());
    // dbcp.setAuthor(MetadataHelper.getAuthor(connection));
    // dbcp.setDescription(MetadataHelper.getDescription(connection));
    // dbcp.setPurpose(MetadataHelper.getPurpose(connection));
    // dbcp.setStatus(MetadataHelper.getDevStatus(connection));
    //
    // dbcp.setDriverClassName(driverClassName);
    // dbcp.setJdbcUrl(dbUrl);
    // dbcp.setParameters(parameters);
    // IMetadataConnection metadataConnection = MetadataFillFactory.getDBInstance(connection).fillUIParams(
    // ParameterUtil.toMap(dbcp));
    // newConn = MetadataFillFactory.getDBInstance(connection).fillUIConnParams(metadataConnection, null);
    // }
    // if (newConn == null) {
    // log.error(Messages.getString(
    //                        "RefreshActionDelegate.errorUnableCreateNewCon", connection == null ? "" : connection.getName()));//$NON-NLS-1$ //$NON-NLS-2$
    // return;
    // }
    // // System.out.println(alreadySavedModel.toString());
    // IFolder folder = ResourceManager.getConnectionFolder();
    // FolderProvider fp = new FolderProvider();
    // fp.setFolderResource(folder);
    // IFile file2 = loadDataProviderAndStructureInMemory(newConn, fp);
    //
    // final EObject model2 = ModelUtils.load(file2.toString(), resourceSet);
    //
    // // MODSCA 2008-03-31 add option for ignoring some elements
    // Map<String, Object> options = new HashMap<String, Object>();
    // options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
    // options.put(MatchOptions.OPTION_SEARCH_WINDOW, 500);
    //
    // // Creates the match then the diff model for those two models
    // final MatchModel match = MatchService.doMatch(alreadySavedModel, model2, options);
    // final DiffModel diff = DiffService.doDiff(match, false);
    //
    // EList<UnmatchElement> unMatchedElements = match.getUnmatchedElements();
    // for (Object object : unMatchedElements) {
    // UnmatchElement unMatched = (UnmatchElement) object;
    // ModelElement modelElt = (ModelElement) unMatched.getElement();
    //                System.out.println("Unmatched elt= " + modelElt.getName()); //$NON-NLS-1$
    // }
    // @SuppressWarnings("unused")
    // EList<DiffElement> ownedElements = diff.getOwnedElements();
    //
    // // Prints the results
    // try {
    // System.out.println(ModelUtils.serialize(match));
    // System.out.println(ModelUtils.serialize(diff));
    // } catch (IOException e) {
    // log.error(e, e);
    // }
    //
    // // Serializes the result as "result.emfdiff" in the directory this
    // // class has been called from.
    //            String outputFile = "out/result.emfdiff"; //$NON-NLS-1$
    //            System.out.println("saving emfdiff as \"" + outputFile + "\""); //$NON-NLS-1$ //$NON-NLS-2$
    // final ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
    // snapshot.setDate(Calendar.getInstance().getTime());
    // snapshot.setMatch(match);
    // snapshot.setDiff(diff);
    // // MOD scorreia 2010-01-29: we may need to set the file.encoding property here.
    // ModelUtils.save(snapshot, outputFile);
    //
    // } catch (IOException e) {
    // System.out.print(e.getMessage());
    // // log.error(e, e);
    // } catch (InterruptedException e) {
    // System.out.print(e.getMessage());
    // log.error(e, e);
    // }
    // }

    private IFile loadDataProviderAndStructureInMemory(Connection dataProvider, FolderProvider folderProvider) {
        IPath folderPath = ((folderProvider != null) && folderProvider.getFolder() != null) ? folderProvider.getFolderResource()
                .getFullPath() : null;
        if (folderPath == null) { // do not serialize data
            System.out.println("Data provider not serialized: no folder given."); //$NON-NLS-1$
            return null;
        }

        // --- add resources in resource set
        EMFUtil util = EMFSharedResources.getSharedEmfUtil();
        ResourceSet resourceSet = util.getResourceSet();

        String fileName = ".refresh.prv"; //$NON-NLS-1$
        IFile file = folderProvider == null ? null : folderProvider.getFolderResource().getFile(fileName);
        // File file = new File(dataproviderFilename);
        if (file != null && file.exists()) {
            try {
                file.delete(true, null);
            } catch (CoreException e) {
                log.error(e, e);
            }
        }
        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
        final Resource resource = resourceSet.createResource(uri);
        @SuppressWarnings("unused")
        boolean ok = resource.getContents().add(dataProvider);

        // save each catalog is its own file
        Collection<? extends ModelElement> catalogs = ConnectionHelper.getCatalogs(dataProvider);
        if (CAT_WITH_PRV) {
            ok = resource.getContents().addAll(catalogs);
        } else {
            // ok = addElementsToOwnResources(catalogs,
            // folderProvider.getFolderResource(), util);
        }

        // save each schema is its own file
        Collection<? extends ModelElement> schemata = ConnectionHelper.getSchema(dataProvider);
        if (CAT_WITH_PRV) {
            ok = resource.getContents().addAll(schemata);
        } else {
            // ok = addElementsToOwnResources(schemata,
            // folderProvider.getFolderResource(), util);
            // util.save();
        }
        Iterator<? extends ModelElement> it = schemata.iterator();
        while (it.hasNext()) {
            Schema tdschema = (Schema) it.next();
            Collection<? extends ModelElement> tables = SchemaHelper.getTables(tdschema);
            ok = resource.getContents().addAll(tables);
            Collection<? extends ModelElement> views = SchemaHelper.getViews(tdschema);
            ok = resource.getContents().addAll(views);

        }

        EMFUtil.saveSingleResource(resource);

        return file;
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection != null && selection instanceof TreeSelection) {
            TreeSelection ts = (TreeSelection) selection;
            TreePath[] treepaths = ts.getPaths();
            if (treepaths.length >= 1) {
                int count = treepaths[0].getSegmentCount();
                for (int i = 0; i < count; i++) {
                    Object o = treepaths[0].getSegment(i);
                    if (o instanceof IFile) {
                        if (((IFile) o).getFileExtension().toLowerCase().equals("prv")) { //$NON-NLS-1$
                            selectedFileObject = (IFile) o;
                        }
                    } else if (o instanceof Schema) {
                        selectedSchema = (Schema) o;
                    } else if (o instanceof Catalog) {
                        selectedCatalog = (Catalog) o;
                    } else if (o instanceof Connection) {
                        selectedDataProvider = (Connection) o;
                    }
                }
            }
            selectedObject = ts.getFirstElement();
        }
    }
}
