// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IImportWriter {

    /**
     * DOC bZhou Comment method "computeInput".
     * 
     * @param path
     * @return the item record or null
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
     * Recompute the statu of records
     * 
     * @param elements
     * @param checkExisted
     * @return
     */
    public ItemRecord[] populate(ItemRecord[] elements, boolean checkExisted);

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
     * Write down all elements into worksapce.
     * 
     * @param records All records which will write into worksapce
     * @param monitor The monitor of whole process
     * @param isOverwrite True if conflict record should be overwrite else false
     */
    public void write(ItemRecord[] records, IProgressMonitor monitor, boolean isOverwrite);

    /**
     * DOC bZhou Comment method "migration".
     * 
     * @param monitor
     */
    public void migration(IProgressMonitor monitor);

    /**
     * DOC bZhou Comment method "finish".
     * 
     * @param records
     * @param monitor
     * @throws IOException
     * @throws CoreException
     */
    public void finish(ItemRecord[] records, IProgressMonitor monitor) throws IOException, CoreException;

    /**
     * DOC xqliu Comment method "postFinish".
     * 
     * @throws IOException
     */
    public void postFinish() throws IOException;

    /**
     * TDQ-16176 each time need to delete the temp folder.
     * 
     * @throws IOException
     */
    public void clearTempFolder() throws IOException;
}
