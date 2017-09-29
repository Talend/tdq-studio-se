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

package org.talend.dataprofiler.core.model;

import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC mzhao class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class TdResourceModel extends Resource {

    private ModelElement modelElement = null;

    public TdResourceModel(IPath path, Workspace workspace, ModelElement modelElement) {
        super(path, workspace);
        this.modelElement = modelElement;
    }

    @Override
    public String getName() {
        return modelElement.getName();
    }

    // @Override
    // public IMarker createMarker(String type) throws CoreException {
    //
    // IMarker marker = new TdMarker(TdResourceModel.this);
    // Map<String, String> attMap = new HashMap<String, String>();
    // attMap.put(MarkerViewUtil.NAME_ATTRIBUTE, modelElement.getName());
    // attMap.put(IMarker.LINE_NUMBER, "120");
    // marker.setAttributes(attMap);
    // return marker;
    // }

    @Override
    public int getType() {
        // TODO Auto-generated method stub
        return IFile.FILE;
    }

    /**
     * 
     * DOC mzhao TdResourceModel class global comment. Detailled comment
     */
    // private class TdMarker implements IMarker {
    //
    // private Map<String, Object> attributeMap = null;
    //
    // private IResource resource = null;
    //
    // public TdMarker(IResource rs) {
    // this.resource = rs;
    // }
    //
    // public void delete() throws CoreException {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // public boolean exists() {
    // return false;
    // }
    //
    // public Object getAttribute(String attributeName) throws CoreException {
    // // TODO Auto-generated method stub
    // return attributeMap.get(attributeName);
    // }
    //
    // public int getAttribute(String attributeName, int defaultValue) {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // public String getAttribute(String attributeName, String defaultValue) {
    // // TODO Auto-generated method stub
    // String value = (String) attributeMap.get(attributeName);
    // if (value == null) {
    // value = defaultValue;
    // }
    //
    // return value;
    // }
    //
    // public boolean getAttribute(String attributeName, boolean defaultValue) {
    // if (attributeMap.get(attributeName) == null) {
    // return false;
    // } else {
    // return Boolean.parseBoolean(attributeMap.get(attributeName).toString());
    // }
    //
    // }
    //
    // public Map<String, Object> getAttributes() throws CoreException {
    // // TODO Auto-generated method stub
    // return attributeMap;
    // }
    //
    // public Object[] getAttributes(String[] attributeNames) throws CoreException {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // public long getCreationTime() throws CoreException {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // public long getId() {
    // // TODO Auto-generated method stub
    // return 0;
    // }
    //
    // public IResource getResource() {
    // // TODO Auto-generated method stub
    // return resource;
    // }
    //
    // public String getType() throws CoreException {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // public boolean isSubtypeOf(String superType) throws CoreException {
    // // TODO Auto-generated method stub
    // return false;
    // }
    //
    // public void setAttribute(String attributeName, int value) throws CoreException {
    // attributeMap.put(attributeName, value);
    //
    // }
    //
    // public void setAttribute(String attributeName, Object value) throws CoreException {
    // attributeMap.put(attributeName, value);
    //
    // }
    //
    // public void setAttribute(String attributeName, boolean value) throws CoreException {
    // attributeMap.put(attributeName, value);
    //
    // }
    //
    // public void setAttributes(Map attributes) throws CoreException {
    // attributeMap = attributes;
    //
    // }
    //
    // public void setAttributes(String[] attributeNames, Object[] values) throws CoreException {
    //
    // }
    //
    // public Object getAdapter(Class adapter) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    // }

}
