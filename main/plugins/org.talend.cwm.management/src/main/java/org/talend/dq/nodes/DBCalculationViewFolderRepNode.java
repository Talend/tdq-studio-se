// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import java.util.List;

import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdTable;
import org.talend.repository.model.RepositoryNode;

import orgomg.cwm.resource.relational.Schema;

public class DBCalculationViewFolderRepNode extends DBTableFolderRepNode {

    /**
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public DBCalculationViewFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public String getLabel() {
        if (hasChildren()) {
            return Messages.getString("DBTableFolderRepNode.CalculationViewWithCount", this.getChildrenCount()); //$NON-NLS-1$
        }
        return Messages.getString("DBTableFolderRepNode.CalculationView"); //$NON-NLS-1$
    }

    @Override
    protected List<TdTable> createTableUnderSchema(IRepositoryViewObject metadataObject, List<TdTable> tables,
            String filterCharacter) throws Exception {
        return super.createTableUnderSchema(metadataObject, tables, filterCharacter);
    }

    @Override
    protected List<TdTable> getExistEmelents(Schema parent) {
        return PackageHelper.getCalculationViews(parent);
    }

    @Override
    protected List<TdTable> loadElementWhenEmpty(boolean isLoad, Schema parent) throws Exception {
        return DqRepositoryViewService.getCalculationViews(getConnection(), parent, null, isLoad, true);
    }

    @Override
    protected String getTableType() {
        return ETableTypes.TABLETYPE_CALCULATION_VIEW.getName();
    }

}
