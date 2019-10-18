// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.reports.TdReport;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.util.TalendFileSwitch;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.resourcehelper.ContextResourceFileHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.security.PasswordMigrationUtil;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * TDQ-16616 msjian: Migration to new encryption/decryption scheme.
 */
public class UpgradePasswordEncryptionAlg4DQItemTask extends AbstractWorksapceUpdateTask {

    public Date getOrder() {
        return createDate(2019, 9, 16);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    TalendFileSwitch contextSwitch = new TalendFileSwitch() {

        @Override
        public ContextType caseContextType(ContextType object) {
            return object;
        };
    };

    @Override
    protected boolean doExecute() throws Exception {
        // for context file, consider the password
        List<IFile> contextList = ContextResourceFileHelper.getInstance().getAllContexts();
        for (IFile contextFile : contextList) {
            boolean modify = false;
            ContextType type = null;
            Resource fileResource = ContextResourceFileHelper.getInstance().getFileResource(contextFile);
            EList<EObject> contents = fileResource.getContents();
            for (EObject object : contents) {
                Object doSwitch = contextSwitch.doSwitch(object);
                type = doSwitch != null ? (ContextType) doSwitch : null;
            }
            if (type == null) {
                continue;
            }
            List<ContextParameterType> paramTypes = type.getContextParameter();
            if (paramTypes != null) {
                for (ContextParameterType param : paramTypes) {
                    String value = param.getValue();
                    if (value != null && PasswordEncryptUtil.isPasswordType(param.getType())) {
                        param.setRawValue(PasswordMigrationUtil.decryptPassword(value));
                        modify = true;
                    }
                }
            }
            if (modify) {
                Property property = PropertyHelper.getProperty(contextFile);
                if (property != null) {
                    ProxyRepositoryFactory.getInstance().save(property.getItem(), true);
                }
            }
        }

        // for database connection, consider the password
        List<? extends ModelElement> allConnectionElement = PrvResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allConnectionElement) {
            if (me instanceof DatabaseConnection) {
                DatabaseConnection dbConnection = (DatabaseConnection) me;
                // for context mode database connection, keep it
                if (dbConnection.isContextMode()) {
                    continue;
                }
                String oldPass = dbConnection.getPassword();
                if (oldPass != null) {
                    dbConnection.setPassword(PasswordMigrationUtil.encryptPasswordIfNeeded(oldPass));
                    ElementWriterFactory.getInstance().createDataProviderWriter().save(me);
                }
                
            }
        }

        // for report datamart part, consider the password
        List<? extends ModelElement> allElement = RepResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allElement) {
            if (me instanceof TdReport) {
                boolean modify = false;
                TdReport report = (TdReport) me;

                TaggedValue oldPassword = TaggedValueHelper
                        .getTaggedValue(TaggedValueHelper.REP_DBINFO_PASSWORD, report.getTaggedValue());
                String oldPass = oldPassword.getValue();
                if (oldPass != null) {
                    // for context mode datamart connection, keep it
                    if (!ContextHelper.isContextVar(oldPass)) {
                        String newPassword = PasswordMigrationUtil.encryptPasswordIfNeeded(oldPass);
                        TaggedValueHelper.setTaggedValue(report, TaggedValueHelper.REP_DBINFO_PASSWORD, newPassword); // after
                        modify = true;
                    }
                }

                // for report context part, consider the password
                EList<ContextType> repContextList = report.getContext();
                for (ContextType type : repContextList) {
                    List<ContextParameterType> paramTypes = type.getContextParameter();
                    if (paramTypes != null) {
                        for (ContextParameterType param : paramTypes) {
                            String value = param.getValue();
                            if (value != null && PasswordEncryptUtil.isPasswordType(param.getType())) {
                                param.setRawValue(PasswordMigrationUtil.decryptPassword(value));
                                modify = true;
                            }
                        }
                    }
                }
                if (modify) {
                    ElementWriterFactory.getInstance().createReportWriter().save(me);
                }
            }
        }

        return true;
    }
}
