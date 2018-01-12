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
package org.talend.dataprofiler.core.service;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.IDriverService;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlStore;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.SqlExplorerUtils;

/**
 * 
 * zshen help to get driver for top.
 * 
 * 
 */
public class TOPDriverService implements IDriverService {

    public TOPDriverService() {
    }

    /**
     * zshen get driver by metadataConnection information.
     * 
     * @param metadataConnection contain the information which about driver.
     * @return if can't find the driver will get a null.
     */
    public Driver getDriver(IMetadataConnection metadataConnection) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        return SqlExplorerUtils.getDefault().getDriver(metadataConnection);
    }

    public List<String> getTDQSupportDBTemplate() {
        List<String> dqList = new ArrayList<String>();
        dqList.addAll(Arrays.asList(SupportDBUrlStore.getInstance().getDBTypes()));
        IGenericDBService dbService = CWMPlugin.getDefault().getGenericDBService();
        if (dbService != null) {
            List<ERepositoryObjectType> extraTypes = dbService.getExtraTypes();
            for (ERepositoryObjectType type : extraTypes) {
                dqList.add(type.getLabel());
            }
        }
        return dqList;
    }
}
