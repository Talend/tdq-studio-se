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
package org.talend.dataprofiler.core.migration.helper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.StringUtils;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.resource.ResourceManager;

/**
 * @author scorreia
 * 
 * This class helps to update the .Talend.definition file.
 */
public class TalendDefinitionFileUpdate {

    private static Logger log = Logger.getLogger(TalendDefinitionFileUpdate.class);

    private static final String TALENDDEFINITIONFILENAME = ".Talend.definition"; //$NON-NLS-1$

    /**
     * A map where the keys are the old strings to replace and the values are the new strings.
     */
    private final Map<String, String> old2new = new HashMap<String, String>();

    public boolean add(String oldStr, String newStr) {
        return this.old2new.put(oldStr, newStr) == null;
    }

    /**
     * Method "replace".
     * 
     * @return true if ok
     */
    public boolean replace(String migrationTaskName) {
        IFolder librariesFolder = ResourceManager.getLibrariesFolder();
        IFile definitionFile = librariesFolder.getFile(TALENDDEFINITIONFILENAME);

        if (definitionFile.exists()) {
            File file = new File(definitionFile.getLocationURI());
            try {
                String content = FileUtils.readFileToString(file, EMFUtil.ENCODING);
                for (String oldString : old2new.keySet()) {
                    String newString = old2new.get(oldString);
                    if (log.isInfoEnabled()) {
                        log.info(DefaultMessagesImpl.getString(
                                "TalendDefinitionFileUpdate_MigLog", migrationTaskName, oldString, newString));//$NON-NLS-1$
                    }
                    content = StringUtils.replace(content, oldString, newString);
                }
                FileUtils.writeStringToFile(file, content, EMFUtil.ENCODING);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }

        return true;
    }

    /**
     * Method "indexOf".
     * 
     * @return the index of the first occurrence of the character in the character sequence represented by this object,
     * or <code>-1</code> if the character does not occur.
     */
    public int indexOf(String str) {
        IFolder librariesFolder = ResourceManager.getLibrariesFolder();
        IFile definitionFile = librariesFolder.getFile(TALENDDEFINITIONFILENAME);

        if (definitionFile.exists()) {
            File file = new File(definitionFile.getLocationURI());
            try {
                String content = FileUtils.readFileToString(file, EMFUtil.ENCODING);
                return content.indexOf(str);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return -1;
            }
        }
        return -1;
    }
}
