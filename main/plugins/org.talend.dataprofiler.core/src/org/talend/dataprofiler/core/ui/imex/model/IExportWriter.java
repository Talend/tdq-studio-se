// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.imex.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IExportWriter {

    /**
     * DOC bZhou Comment method "computeInput".
     *
     * @param path
     * @return
     */
    public ItemRecord computeInput(IPath path);

    /**
     * DOC bZhou Comment method "setBasePath".
     *
     * @param basePath
     */
    public void setBasePath(IPath path);

    /**
     * DOC bZhou Comment method "getBasePath".
     *
     * @return
     */
    public IPath getBasePath();

    /**
     * DOC bZhou Comment method "check".
     *
     * @return
     */
    public List<String> check();

    /**
     * DOC bZhou Comment method "mapping".
     *
     * @param record
     * @return
     */
    public Map<IPath, IPath> mapping(ItemRecord record);

    /**
     * DOC bZhou Comment method "write".
     *
     * @param recored
     * @throws IOException
     * @throws CoreException
     */
    public void write(IPath resPath, IPath desPath) throws IOException, CoreException;

    /**
     * DOC bZhou Comment method "write".
     *
     * @param records
     * @param monitor
     */
    public void write(ItemRecord[] records, IProgressMonitor monitor);

    /**
     * DOC bZhou Comment method "finish".
     *
     * @param records
     * @throws IOException
     * @throws CoreException
     */
    public void finish(ItemRecord[] records) throws IOException, CoreException;

    public void finish(ItemRecord[] records, List<IProject> projects) throws IOException, CoreException;
}
