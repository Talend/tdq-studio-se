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
package org.talend.dq.helper.resourcehelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.util.PatternSwitch;
import org.talend.resource.ResourceManager;

/**
 * DOC rli class global comment. Detailled comment
 */
public final class PatternResourceFileHelper extends ResourceFileMap<Pattern> {

    private static PatternResourceFileHelper instance;

    PatternSwitch<Pattern> patternSwitch = new PatternSwitch<Pattern>() {

        @Override
        public Pattern casePattern(Pattern object) {
            return object;
        }
    };

    private PatternResourceFileHelper() {
        super();
    }

    public static PatternResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new PatternResourceFileHelper();
        }
        return instance;
    }

    /**
     * DOC bZhou Comment method "findPattern".
     * 
     * @param file
     * @return
     */
    public Pattern findPattern(IFile file) {
        if (checkFile(file)) {
            return getModelElement(file);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.PATTERN.equalsIgnoreCase(file.getFileExtension());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getPatternFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public Pattern doSwitch(EObject object) {
        return patternSwitch.doSwitch(object);
    }
}
