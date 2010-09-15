// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.migration.AWorkspaceTask;
import org.talend.dq.helper.PropertyHelper;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.resource.ResourceManager;

/**
 * 
 * DOC zshen class global comment. Detailled comment change the properties "FileName" to a Reference of Connection
 */
public class ExchangeFileNameToReferenceTask extends AWorkspaceTask {

    protected static Logger log = Logger.getLogger(ExchangeFileNameToReferenceTask.class);

    public static final String DB_CONNECTION = "TDQ_Metadata/DB Connections";

    public static final String MDM_CONNECTION = "TDQ_Metadata/MDM Connections";

    private List<File> mergeFolders = new ArrayList<File>();

    private Map<String, String> replaceStringMap;

    public ExchangeFileNameToReferenceTask() {
        // TODO Auto-generated constructor stub
    }

    /**
     * DOC bZhou Comment method "addMergeFolder".
     * 
     * Add a folder to list to do merge.
     * 
     * @param file
     */
    public void addMergeFolder(File file) {
        mergeFolders.add(file);
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean returnFlag = true;
        if (mergeFolders.isEmpty()) {
            mergeFolders.add(ResourceManager.getRootProject().getFolder(new Path(DB_CONNECTION)).getLocation().toFile());
            mergeFolders.add(ResourceManager.getRootProject().getFolder(new Path(MDM_CONNECTION)).getLocation().toFile());
        }
        List<File> resources = new ArrayList<File>();

        for (File theFile : mergeFolders) {
            if (theFile != null && theFile.exists()
                    && (DB_CONNECTION.endsWith(theFile.getName()) || MDM_CONNECTION.endsWith(theFile.getName()))) {
                for (File newFile : theFile.listFiles()) {
                    if (newFile.isDirectory()) {
                        resources.addAll(iteratorResource(newFile));
                    } else if (newFile.getName().toLowerCase().endsWith(FactoriesUtil.PROPERTIES_EXTENSION.toLowerCase())) {
                        resources.add(newFile);
                    }
                }
                // resources.addAll(Arrays.asList(theFile.listFiles()));
            }

        }
        if (resources.size() > 0) {
            for (File resource : resources) {
                if (resource.isFile()
                        && resource.getName().toLowerCase().endsWith(FactoriesUtil.PROPERTIES_EXTENSION.toLowerCase())) {

                    Property property = PropertyHelper.getProperty(resource);

                    Item item = property.getItem();
                    if (item instanceof TDQItem) {
                        String fileName = ((TDQItem) item).getFilename();
                        File theFile = new File(resource.getParent() + File.separator + fileName);

                        File desFile = new File(resource.getParent() + File.separator
                                + fileName.substring(0, fileName.lastIndexOf('.') + 1) + FactoriesUtil.ITEM_EXTENSION);
                        theFile.renameTo(desFile);

                        URI uri = URI.createFileURI(desFile.getAbsolutePath());

                        Resource prvResource = new ResourceSetImpl().getResource(uri, true);

                        if (prvResource != null) {
                            Collection<Connection> tdDataProviders = ConnectionHelper.getTdDataProviders(prvResource
                                    .getContents());
                            Iterator<Connection> it = tdDataProviders.iterator();

                            Connection connection = null;
                            while (it.hasNext()) {
                                connection = it.next();
                                break;
                            }

                            if (connection != null) {

                                ConnectionItem connectionItem = org.talend.core.model.properties.PropertiesFactory.eINSTANCE
                                        .createConnectionItem();
                                connectionItem.setConnection(connection);
                                property.setItem(connectionItem);
                                property.eResource().getContents().add(connectionItem);
                                ProxyRepositoryFactory.getInstance().save(property);
                            }
                        }

                        // TypedReturnCode<Connection> returnCode =
                        // PrvResourceFileHelper.getInstance().findProvider(theFile);
                        // if (returnCode.isOk()) {
                        // returnFlag &= PrvResourceFileHelper.getInstance().create(returnCode.getObject(),
                        // (IFolder) theFile.getParent()).isOk();
                        // }
                    } else {
                        continue;
                    }

                }

                // tDQDbFolder.move(dbFolder.getFullPath(), true, null);
                // tDQMdmFolder.move(mdmFolder.getFullPath(), true, null);

            }
            // for (IResource theResource : tDQDbFolder.members()) {
            // theResource.move(dbFolder.getFullPath().append(theResource.getName()), true, null);
            // }
            // for (IResource theResource : tDQMdmFolder.members()) {
            // theResource.move(mdmFolder.getFullPath().append(theResource.getName()), true, null);
            // }

            File fileAnalysis = new File(ResourceManager.getAnalysisFolder().getRawLocationURI());
            File fileRule = new File(ResourceManager.getRulesFolder().getRawLocationURI());
            try {
                String[] anaFileExtentionNames = { FactoriesUtil.ANA };
                String[] rulesFileEctentionNames = { FactoriesUtil.DQRULE };
                returnFlag &= FilesUtils.migrateFolder(fileAnalysis, anaFileExtentionNames, this.getReplaceStringMap(), log)
                        && FilesUtils.migrateFolder(fileRule, rulesFileEctentionNames, this.getReplaceStringMap(), log);

            } catch (Exception e) {
                returnFlag = false;
                log.error(e, e);
            }

        }
        return returnFlag;
    }

    private List<File> iteratorResource(File theFolder) {
        List<File> fileList = new ArrayList<File>();
        for (File subFile : theFolder.listFiles()) {
            if (subFile.isFile() && isPropertyFile(subFile)) {
                fileList.add(subFile);
            } else if (subFile.isDirectory()) {
                fileList.addAll(iteratorResource(subFile));
            }
        }
        return fileList;
    }

    private boolean isPropertyFile(File propertyFile) {
        if (propertyFile.isFile()
                && propertyFile.getName().toLowerCase().endsWith("." + FactoriesUtil.PROPERTIES_EXTENSION.toLowerCase())) {
            return true;
        }
        return false;
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        return createDate(2010, 8, 13);
    }

    public Map<String, String> getReplaceStringMap() {
        if (this.replaceStringMap == null) {
            this.replaceStringMap = initReplaceStringMap();
        }
        return this.replaceStringMap;
    }

    private Map<String, String> initReplaceStringMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(".prv", ".item");

        return result;
    }
}
