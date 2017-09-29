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
 * DOC yyin class global comment. Detailled comment
 */
public class CreateBenforLawIndicatorTask extends CreateSystemIndicatorTask {

    String fraudStr = " <categories xmi:id=\"_2aGLUOzJEeG0fbygDv6UrQ\" name=\"Fraud Detection\" label=\"Fraud Detection\">\r\n" //$NON-NLS-1$
            + "    <taggedValue xmi:id=\"_I31R8OzKEeG0fbygDv6UrQ\" tag=\"Description\" value=\"contains fraud detection indicators\"/>\r\n" //$NON-NLS-1$
            + "    <taggedValue xmi:id=\"_LHMsgOzKEeG0fbygDv6UrQ\" tag=\"Purpose\" value=\"detect possible instances of fraud\"/>\r\n" //$NON-NLS-1$
            + "  </categories>"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 8, 31);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.impl.CreateSystemIndicatorTask#getFoldername()
     */
    @Override
    protected String getFoldername() {
        return "Fraud Detection"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.impl.CreateSystemIndicatorTask#getCategoryString()
     */
    @Override
    protected String getCategoryString() {
        return fraudStr;
    }

}
