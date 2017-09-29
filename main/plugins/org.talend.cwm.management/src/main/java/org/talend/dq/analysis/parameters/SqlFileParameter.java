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
package org.talend.dq.analysis.parameters;

import org.talend.cwm.management.api.FolderProvider;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class SqlFileParameter {

    private String fileName;

    private FolderProvider folderProvider;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFolderProvider(FolderProvider folderProvider) {
        this.folderProvider = folderProvider;
    }

    public String getFileName() {
        return fileName;
    }

    public FolderProvider getFolderProvider() {
        return folderProvider;
    }
}
