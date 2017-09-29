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

import java.util.List;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IEcosComponent {

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
     * DOC bZhou Comment method "getCategory".
     * 
     * @return
     */
    IEcosCategory getCategry();

    /**
     * DOC bZhou Comment method "setCategory".
     * 
     * @param category
     */
    void setCategry(IEcosCategory category);

    /**
     * DOC bZhou Comment method "getRevisions".
     * 
     * @return
     */
    List<IRevision> getRevisions();

    /**
     * DOC bZhou Comment method "getInstalledRevision".
     * 
     * @return
     */
    IRevision getInstalledRevision();

    /**
     * DOC bZhou Comment method "setInstalledRevision".
     * 
     * @param value
     */
    void setInstalledRevision(IRevision value);

    /**
     * DOC bZhou Comment method "getLatestRevision".
     * 
     * @return
     */
    IRevision getLatestRevision();

    /**
     * DOC bZhou Comment method "setLatestRevision".
     * 
     * @param value
     */
    void setLatestRevision(IRevision value);

    /**
     * DOC bZhou Comment method "getInstalledLocation".
     * 
     * @return
     */
    String getInstalledLocation();

    /**
     * DOC bZhou Comment method "setInstalledLocation".
     * 
     * @param value
     */
    void setInstalledLocation(String value);

    /**
     * DOC bZhou Comment method "getAuthor".
     * 
     * @return
     */
    String getAuthor();

    /**
     * DOC bZhou Comment method "setAuthor".
     * 
     * @param value
     */
    void setAuthor(String value);
}
