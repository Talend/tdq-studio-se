// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;

/**
 * 
 * DOC mzhao Svn repository service.
 */
public abstract class AbstractSvnRepositoryService implements IService {

    public boolean isReadonly() {
        // TOP can create/modify/delete everything.
        return false;
    }

    //Added TDQ-12180
    public void addIfImportOverride(IPath desPath){
        
    }
}
