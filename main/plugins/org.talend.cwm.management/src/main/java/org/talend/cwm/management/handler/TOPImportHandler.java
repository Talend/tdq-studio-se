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
package org.talend.cwm.management.handler;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.items.importexport.handlers.imports.ImportRepTypeHandler;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class TOPImportHandler extends ImportRepTypeHandler {

    public TOPImportHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.ImportRepTypeHandler#setInitializationData(org.eclipse
     * .core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        super.setInitializationData(config, propertyName, data);
        // TUP-1374 create dq structure if the structure doesn't exsit.
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(ITDQItemService.class);
            if (tdqService != null) {
                tdqService.createDQStructor();
            }
        }
    }

    /*
     * (non-Javadoc) .
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler#computeItemRecord(org.talend.repository
     * .items.importexport.manager.ResourcesManager, org.eclipse.core.runtime.IPath)
     */
    @Override
    public ImportItem computeImportItem(ResourcesManager resManager, IPath path) {
        ImportItem itemRecord = new ImportItem(path);

        // Load the DQ specific resource such as patterns and rules.
        // Load resource only
        super.loadResource(resManager, itemRecord);

        IPath propertyPath = path.removeFileExtension().addFileExtension(FileConstants.PROPERTIES_EXTENSION);
        ResourceSet resSet = itemRecord.getResourceSet();
        itemRecord = new ImportItem(propertyPath);
        itemRecord.setResourceSet(resSet);

        // Load property resource.
        super.computeProperty(resManager, itemRecord);
        Item item = itemRecord.getItem();
        // only resolveAll EMF-Model items.
        if (item != null && !(item instanceof FileItem)) {
            EcoreUtil.resolveAll(itemRecord.getResourceSet());
        }

        return itemRecord;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler#computeProperty(org.talend.repository
     * .items.importexport.manager.ResourcesManager, org.talend.repository.items.importexport.handlers.model.ImportItem)
     */
    @Override
    protected void computeProperty(ResourcesManager manager, ImportItem itemRecord) {
        // Empty implementation
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler#isValidFile(org.eclipse.core.runtime
     * .IPath)
     */
    @Override
    protected boolean isValidFile(IPath path) {
        String[] exts = getResourceNeededExtensions();
        for (String ext : exts) {
            if (path.lastSegment().endsWith(ext)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler#getResourceNeededExtensions()
     */
    @Override
    protected String[] getResourceNeededExtensions() {
        return new String[] { FileConstants.ITEM_EXTENSION, FileConstants.ANA_EXTENSION, FileConstants.REP_EXTENSION,
                FileConstants.PAT_EXTENSION, FileConstants.RULE_EXTENSION, FileConstants.DEF_EXTENSION,
                FileConstants.SQL_EXTENSION };
    }
}
