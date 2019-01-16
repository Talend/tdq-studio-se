// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.resource.ResourceManager;
import org.talend.utils.security.CryptoHelper;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC hcheng class global comment. Delete the password stored in Tagged values of the dataprovider. Encrypt the
 * password stored in the Tagged values of the providerConnection.
 * 
 */
public class EncryptAndDecryptPasswordTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        IFolder folder = ResourceManager.getConnectionFolder();

        IResource[] resource = folder.members();
        if (resource.length > 0) {
            for (IResource re : resource) {
                if (re instanceof IFile) {
                    IFile file = (IFile) re;
                    Connection tdDataProvider = PrvResourceFileHelper.getInstance().findProvider(file);
                    if (tdDataProvider != null) {
                        encryptDataProvider(tdDataProvider);
                    }
                }

            }
        }

        return true;
    }

    /**
     * DOC hcheng Comment method "encryptDataProvider".
     * 
     * @param dataProvider
     * @return
     */
    public static Connection encryptDataProvider(Connection dataProvider) {
        // Delete DataProvider password tag
        List<TaggedValue> dpList = new ArrayList<TaggedValue>();
        EList<TaggedValue> taggedValues = dataProvider.getTaggedValue();
        dpList.addAll(taggedValues);
        if (dpList != null && dpList.size() > 0) {
            int dpSize = dpList.size();
            for (int j = 0; j < dpSize; ++j) {
                Object dpObj = dpList.get(j);
                if (dpObj != null) {
                    TaggedValue dp = (TaggedValue) dpObj;
                    if (TaggedValueHelper.PASSWORD.equals(dp.getTag())) {
                        taggedValues.remove(dp);
                    }
                }
            }
        }

        // Connection password encrypt
        Connection tdpConn = SwitchHelpers.CONNECTION_SWITCH.doSwitch(dataProvider);
        if (tdpConn != null) {
            EList<TaggedValue> tvList = tdpConn.getTaggedValue();
            if (tvList != null && tvList.size() > 0) {
                int tvSize = tvList.size();
                for (int j = 0; j < tvSize; ++j) {
                    Object tvObj = tvList.get(j);
                    if (tvObj != null) {
                        TaggedValue tv = (TaggedValue) tvObj;
                        if (TaggedValueHelper.PASSWORD.equals(tv.getTag())) {
                            tv.setValue(new CryptoHelper(CryptoHelper.PASSPHRASE).encrypt(tv.getValue()));
                        }
                    }
                }
            }
        }

        PrvResourceFileHelper.getInstance().save(dataProvider);
        return dataProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2009, 3, 3);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.migration.IWorkspaceMigrationTask# getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }
}
