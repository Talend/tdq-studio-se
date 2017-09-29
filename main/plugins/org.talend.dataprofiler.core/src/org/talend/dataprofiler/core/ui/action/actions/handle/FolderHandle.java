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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.talend.core.model.properties.Property;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class FolderHandle {

    private Property property;

    private String pathStr;

    private static Logger log = Logger.getLogger(FolderHandle.class);

    /**
     * DOC bZhou FolderHandle constructor comment.
     * 
     * @param property
     */
    public FolderHandle(Property property) {
        this.property = property;
        this.pathStr = property.getItem().getState().getPath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IDeletionHandle#delete()
     */

    /**
     * DOC bZhou Comment method "delsubFolderForever".
     * 
     * @param fo
     * @throws Exception
     */
    private void delsubFolderForever(IFolder fo) throws Exception {
        IResource[] members = fo.members();
        for (IResource member : members) {
            if (member.getType() == IResource.FOLDER) {
                IFolder subFolder = (IFolder) member;
                // MOD qiongli 2010-8-5,bug 14697;20109-13,bug 14697
                if (subFolder.members().length != 0) {
                    delsubFolderForever(subFolder);
                } else {
                    subFolder.delete(true, null);
                }
            }
        }
        if (fo.members().length == 0) {
            fo.delete(true, null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IDeletionHandle#getDependencies()
     */
    public List<ModelElement> getDependencies() {
        // TODO Auto-generated method stub
        return null;
    }

    /**


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IActionHandle#getProperty()
     */
    public Property getProperty() {
        return this.property;
    }
}
