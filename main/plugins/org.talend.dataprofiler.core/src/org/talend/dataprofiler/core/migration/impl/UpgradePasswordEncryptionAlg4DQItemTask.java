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

import org.eclipse.emf.common.util.EList;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.reports.TdReport;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
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

    @Override
    protected boolean doExecute() throws Exception {
        // TDQ-18623 msjian : for context file, consider the password
        List<IRepositoryViewObject> allContextObject =
                ProxyRepositoryFactory.getInstance().getAll(ERepositoryObjectType.CONTEXT);

        for (IRepositoryViewObject object : allContextObject) {
            ContextItem contextItem = (ContextItem) object.getProperty().getItem();

            List<ContextType> contextTypeList = contextItem.getContext();
            boolean modify = false;
            if (contextTypeList != null) {
                for (ContextType type : contextTypeList) {
                    List<ContextParameterType> paramTypes = type.getContextParameter();
                    if (paramTypes != null) {
                        for (ContextParameterType param : paramTypes) {
                            String value = param.getValue();
                            if (value != null && PasswordEncryptUtil.isPasswordType(param.getType())) {
                                String decryptValue = PasswordMigrationUtil.decryptPassword(value);
                                if (decryptValue != null) {
                                    param.setRawValue(decryptValue);
                                    modify = true;
                                }
                            }
                        }
                    }
                }
            }
            if (modify) {
                ProxyRepositoryFactory.getInstance().save(contextItem, true);
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
        List<? extends ModelElement> allReportElement = RepResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allReportElement) {
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

        // for analysis, consider the password type context variables
        List<? extends ModelElement> allAnalysisElement = AnaResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allAnalysisElement) {
            if (me instanceof Analysis) {
                boolean modify = false;
                Analysis ana = (Analysis) me;

                EList<ContextType> anaContextList = ana.getContextType();
                for (ContextType type : anaContextList) {
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
                    ElementWriterFactory.getInstance().createAnalysisWrite().save(me);
                }
            }
        }


        return true;
    }
}
