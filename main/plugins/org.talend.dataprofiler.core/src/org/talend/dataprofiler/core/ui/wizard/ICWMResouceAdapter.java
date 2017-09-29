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
package org.talend.dataprofiler.core.ui.wizard;

import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public interface ICWMResouceAdapter {

    /**
     * DOC bzhou Comment method "checkMetadata".
     * 
     * check the metadata user defined is valid or not. <br>
     * 
     * @return
     */
    public ReturnCode checkMetadata();

    /**
     * DOC bzhou Comment method "initCWMResourceBuilder".
     * 
     * initialized instance of CWM resource object. <br>
     * 
     * @return
     */
    public ModelElement initCWMResourceBuilder();

    /**
     * DOC bzhou Comment method "fillMetadataToCWMResource".
     * 
     * add metadata information to CWM resource object. <br>
     * 
     * @param repositoryObject
     */
    public void fillMetadataToCWMResource(ModelElement repositoryObject);

    /**
     * DOC bzhou Comment method "createCWMResourceFile".
     * 
     * create file resource and store the CWM object. <br>
     * 
     * @return
     */
    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement repositoryObject);
}
