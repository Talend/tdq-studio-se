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
package org.talend.dataprofiler.core.exception;

import org.eclipse.core.resources.IFile;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.PropertyHelper;

/**
 * created by zshen on Mar 20, 2013 Detailled comment
 * 
 */
public class ExceptionFactory {

    static private ExceptionFactory eInstance = null;

    protected void ExceptionFactory() {
    }

    public static ExceptionFactory getInstance() {
        if (eInstance == null) {
            eInstance = new ExceptionFactory();
        }
        return eInstance;

    }

    public BusinessException createBusinessException(IRepositoryViewObject repViewObj) {
        BusinessException businessException = new BusinessException();
        IFile itemFile = PropertyHelper.getItemFile(repViewObj.getProperty());
        return createBusinessException(itemFile.getName());
    }

    public BusinessException createBusinessException(String fileName) {
        BusinessException businessException = new BusinessException();
        businessException.setAdditonalMessage(DefaultMessagesImpl.getString("ExceptionFactory.corrupted_file_error", fileName));
        return businessException;
    }
}
