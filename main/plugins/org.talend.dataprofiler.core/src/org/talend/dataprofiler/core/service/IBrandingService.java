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

import java.io.IOException;
import java.net.URL;

/**
 * DOC rli class global comment. Detailled comment
 */
public interface IBrandingService extends IService {

    public String getFullProductName();

    public URL getLicenseFile() throws IOException;

    /**
     * DOC qzhang Comment method "getShortProductName".
     * 
     * @return
     */
    public String getShortProductName();

    /**
     * DOC qzhang Comment method "getCorporationName".
     * 
     * @return
     */
    public String getCorporationName();

    public String getAcronym();
}
