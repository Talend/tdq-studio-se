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

import java.util.Collections;
import java.util.List;

import org.talend.dataprofiler.ecos.jobs.ComponentSearcher;
import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dataprofiler.ecos.model.IEcosComponent;

/**
 * @author jet
 * 
 */
public class EcosCategory implements IEcosCategory {

    String id;

    String name;

    int counter;

    String version;

    List<IEcosComponent> components = Collections.emptyList();

    boolean isOnFilter = false;

    public EcosCategory() {
        super();
    }

    public EcosCategory(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnFilter() {
        return isOnFilter;
    }

    public void setOnFilter(boolean isOnFilter) {
        this.isOnFilter = isOnFilter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosCategory#getComponent()
     */
    public List<IEcosComponent> getComponent() {
        // when on filter, get components from cache.
        if (components.isEmpty() && !isOnFilter) {
            components = ComponentSearcher.getAvailableComponentExtensions(version, this, isOnFilter);
        }
        return components;
    }

    public int getCounter() {
        return counter;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("name:").append(getName()).append("\n");//$NON-NLS-1$ //$NON-NLS-2$
        sb.append("counter:").append(getCounter()).append("\n");//$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.ecos.model.IEcosCategory#getComponent(boolean)
     */
    public List<IEcosComponent> getComponent(boolean onFilter) {
        this.setOnFilter(onFilter);
        return this.getComponent();
    }

}
