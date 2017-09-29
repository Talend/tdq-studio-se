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
package org.talend.dataprofiler.ecos.model;

import java.util.Date;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IRevision {

    /**
     * DOC bZhou Comment method "getId".
     * 
     * @return
     */
    int getId();

    /**
     * DOC bZhou Comment method "setId".
     * 
     * @param value
     */
    void setId(int value);

    /**
     * DOC bZhou Comment method "getName".
     * 
     * @return
     */
    String getName();

    /**
     * DOC bZhou Comment method "setName".
     * 
     * @param value
     */
    void setName(String value);

    /**
     * DOC bZhou Comment method "getDescription".
     * 
     * @return
     */
    String getDescription();

    /**
     * DOC bZhou Comment method "setDescription".
     * 
     * @param value
     */
    void setDescription(String value);

    /**
     * DOC bZhou Comment method "getUrl".
     * 
     * @return
     */
    String getUrl();

    /**
     * DOC bZhou Comment method "setUrl".
     * 
     * @param value
     */
    void setUrl(String value);

    /**
     * DOC bZhou Comment method "getDate".
     * 
     * @return
     */
    Date getDate();

    /**
     * DOC bZhou Comment method "setDate".
     * 
     * @param value
     */
    void setDate(Date value);

    /**
     * DOC bZhou Comment method "getComponent".
     * 
     * @return
     */
    IEcosComponent getComponent();

    /**
     * DOC bZhou Comment method "setComponent".
     * 
     * @param value
     */
    void setComponent(IEcosComponent value);

    /**
     * DOC bZhou Comment method "getFileName".
     * 
     * @return
     */
    String getFileName();

    /**
     * DOC bZhou Comment method "setFileName".
     * 
     * @param value
     */
    void setFileName(String value);
}
