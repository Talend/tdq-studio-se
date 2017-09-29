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

import java.util.Date;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class CreatePhoneNumIndicatorTask extends CreateSystemIndicatorTask {

    String phoneNumStr = " <categories xmi:id=\"_Ohyz4bEEEeCVE-ofo1XCug\" name=\"Phone Number Statistics\" label=\"Phone Number Statistics\">\r\n" //$NON-NLS-1$
            + "    <taggedValue xmi:id=\"_ZSFGoLEEEeCVE-ofo1XCug\" tag=\"Description\" value=\"do some phone number statistics by several counting methods\"/>\r\n" //$NON-NLS-1$
            + "    <taggedValue xmi:id=\"_rEFIoLEEEeCVE-ofo1XCug\" tag=\"Purpose\" value=\"evaluate different phone number of records\"/>\r\n" //$NON-NLS-1$
            + "  </categories>"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2011, 10, 25);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.impl.CreateSystemIndicatorTask#getFoldername()
     */
    @Override
    protected String getFoldername() {
        return "Phone Number Statistics"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.impl.CreateSystemIndicatorTask#getCategoryString()
     */
    @Override
    protected String getCategoryString() {
        return phoneNumStr;
    }

}
