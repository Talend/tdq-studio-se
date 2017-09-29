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
package org.talend.dataprofiler.core.migration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.Schema;

/**
 * 
 * DOC zshen class global comment. one table only have one primaryKey element and many foreignKey element,so filter the
 * *.prv file to check it.
 */
public class UniquePkAndFkTask extends AbstractWorksapceUpdateTask {

    protected static Logger log = Logger.getLogger(UniquePkAndFkTask.class);

    public UniquePkAndFkTask() {

    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean returnFlag = false;
        IFolder folder = ResourceManager.getConnectionFolder();
        IResource[] resource = null;

        try {
            if (folder != null && folder.exists())
                resource = folder.members();
        } catch (CoreException e1) {
            log.error(e1, e1);
        }
        if (resource != null && resource.length > 0) {
            for (IResource re : resource) {
                if (re instanceof IFile && FactoriesUtil.isProvFile(re.getFileExtension())) {
                    IFile file = (IFile) re;
                    boolean saveFalg = false;
                    Connection tdDataProvider = PrvResourceFileHelper.getInstance().findProvider(file);
                    List<orgomg.cwm.objectmodel.core.Package> dataManagerList = tdDataProvider.getDataPackage();
                    for (orgomg.cwm.objectmodel.core.Package firstLevelElement : dataManagerList) {
                        if (getTables(firstLevelElement).isOk()) {
                            saveFalg = true;// The file should be saved
                        }
                    }
                    if (saveFalg) {
                        PrvResourceFileHelper.getInstance().save(tdDataProvider);
                    }

                }

            }
            returnFlag = true;
        }
        return returnFlag;
    }

    /**
     * 
     * DOC zshen Comment method "checkPkAndFk".
     * 
     * @param element
     * @return merge pks or fks if their name is same each other
     */
    private Boolean checkPkAndFk(TdTable element) {
        boolean needToSave = false;
        // case pk
        if (TableHelper.getPrimaryKeys(element).size() > 1) {
            List<PrimaryKey> primaryKeyList = TableHelper.getPrimaryKeys(element);
            for (int i = 1; i < primaryKeyList.size(); i++) {
                PrimaryKey pk = primaryKeyList.get(i);
                TdColumn column = (TdColumn) pk.getFeature().get(pk.getFeature().size() - 1);
                ColumnHelper.removePrimaryKey(column);// remove old pk from column
                column.getUniqueKey().add(primaryKeyList.get(0));// add new pk to column
                primaryKeyList.get(0).getFeature().add(column);// add column to new pk
                TableHelper.removeTableKey(element, pk);// remove old pk from table
            }
            needToSave = true;
        }
        // case fk
        if (TableHelper.getForeignKeys(element).size() > 1) {
            Map<String, ForeignKey> foreignKeySet = new HashMap<String, ForeignKey>();
            List<ForeignKey> foreignKeyList = TableHelper.getForeignKeys(element);
            for (int i = 0; i < foreignKeyList.size(); i++) {
                ForeignKey fk = foreignKeyList.get(i);
                if (foreignKeySet.get(fk.getName()) != null) {
                    foreignKeySet.get(fk.getName()).getFeature().addAll(fk.getFeature());
                    StructuralFeature[] structFeatureArray = fk.getFeature().toArray(
                            new StructuralFeature[fk.getFeature().size()]);
                    for (StructuralFeature fkFeature : structFeatureArray) {
                        TdColumn fkColumn = (TdColumn) fkFeature;
                        fkColumn.getKeyRelationship().remove(fk);
                        fkColumn.getKeyRelationship().add(foreignKeySet.get(fk.getName()));
                    }
                    TableHelper.removeForeignKey(element, fk);// remove old fk from table
                    needToSave = true;
                } else {
                    foreignKeySet.put(fk.getName(), fk);
                }
            }

        }
        return Boolean.valueOf(needToSave);
    }

    private TypedReturnCode<List<TdTable>> getTables(ModelElement element) {
        TypedReturnCode<List<TdTable>> returnCode = new TypedReturnCode<List<TdTable>>(false);
        List<TdTable> tableList = new ArrayList<TdTable>();
        returnCode.setObject(tableList);
        if (element == null)
            return returnCode;
        if (element instanceof Catalog) {
            for (ModelElement subElement : ((Catalog) element).getOwnedElement()) {
                TypedReturnCode<List<TdTable>> newreturnCode = getTables(subElement);
                tableList.addAll(newreturnCode.getObject());
                if (newreturnCode.isOk()) {
                    returnCode.setOk(newreturnCode.isOk());
                }
            }
        } else if (element instanceof Schema) {
            for (ModelElement subElement : ((Schema) element).getOwnedElement()) {
                TypedReturnCode<List<TdTable>> newreturnCode = getTables(subElement);
                tableList.addAll(newreturnCode.getObject());
                if (newreturnCode.isOk()) {
                    returnCode.setOk(newreturnCode.isOk());
                }
            }
        } else if (element instanceof TdTable) {
            tableList.add((TdTable) element);
            if (checkPkAndFk((TdTable) element)) {
                returnCode.setOk(true);
            }
        }

        return returnCode;

    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        // MOD xqliu 2010-08-02 bug 14698, this task must be called after MergeMetadataTask done.
        // MOD xqliu 2010-07-26 bug 13826, this task must be called after MergeMetadataTask done.
        return createDate(2010, 6, 26);
        // return createDate(2010, 6, 9);
    }

}
