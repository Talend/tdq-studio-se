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
package org.talend.dataprofiler.ecos.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dataprofiler.ecos.model.IRevision;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class EcosComponent implements IEcosComponent {

    private String author;

    private String name;

    private String description;

    private IEcosCategory categry;

    private String installedLocation;

    private IRevision installedRevision;

    private IRevision latestRevision;

    private List<IRevision> revisions;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getAuthor()
     */
    public String getAuthor() {
        return author;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getInstalledLocation()
     */
    public String getInstalledLocation() {
        return installedLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getInstalledRevision()
     */
    public IRevision getInstalledRevision() {
        return installedRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getLatestRevision()
     */
    public IRevision getLatestRevision() {
        return latestRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getName()
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getRevisions()
     */
    public List<IRevision> getRevisions() {
        if (revisions == null) {
            revisions = new ArrayList<IRevision>();
        }

        return revisions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#setAuthor(java.lang.String)
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#setDescription(java.lang.String)
     */
    public void setDescription(String value) {
        this.description = value;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#getCategory()
     */
    public IEcosCategory getCategry() {
        return categry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#setCategory(java.lang.String)
     */
    public void setCategry(IEcosCategory categry) {
        this.categry = categry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#setInstalledLocation(java.lang.String)
     */
    public void setInstalledLocation(String value) {
        this.installedLocation = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.ecos.model.IEcosComponent#setInstalledRevision(org.talend.dataprofiler.ecos.model.IRevision
     * )
     */
    public void setInstalledRevision(IRevision value) {
        this.installedRevision = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.ecos.model.IEcosComponent#setLatestRevision(org.talend.dataprofiler.ecos.model.IRevision)
     */
    public void setLatestRevision(IRevision value) {
        this.latestRevision = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosComponent#setName(java.lang.String)
     */
    public void setName(String value) {
        this.name = value;
    }

}
