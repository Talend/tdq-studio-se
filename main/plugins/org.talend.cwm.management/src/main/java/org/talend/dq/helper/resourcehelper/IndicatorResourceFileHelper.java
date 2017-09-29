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
package org.talend.dq.helper.resourcehelper;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.util.DefinitionSwitch;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment MOD mzhao 13676,split system indicators. 2010-07-08
 */
public final class IndicatorResourceFileHelper extends ResourceFileMap {

    private static IndicatorResourceFileHelper instance;

    DefinitionSwitch<IndicatorDefinition> indicatorSwitch = new DefinitionSwitch<IndicatorDefinition>() {

        @Override
        public IndicatorDefinition caseIndicatorDefinition(IndicatorDefinition object) {
            return object;
        }

    };

    private IndicatorResourceFileHelper() {
        super();
    }

    public static IndicatorResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new IndicatorResourceFileHelper();
        }
        return instance;
    }

    public IndicatorDefinition findIndDefinition(IFile file) {
        if (checkFile(file)) {
            return (IndicatorDefinition) getModelElement(file);
        }

        return null;
    }

    /**
     * 
     * DOC zshen Fetch all system indicators.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<IndicatorDefinition> getAllSysIs() {
        IFolder parentFolder = ResourceManager.getSystemIndicatorFolder();
        return (List<IndicatorDefinition>) getAllElement(parentFolder);
    }

    /**
     * 
     * DOC mzhao Fetch all user defined indicators.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<IndicatorDefinition> getAllUDIs() {
        IFolder parentFolder = ResourceManager.getUDIFolder();
        return (List<IndicatorDefinition>) getAllElement(parentFolder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.DEFINITION.equalsIgnoreCase(file.getFileExtension());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getLibrariesFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public ModelElement doSwitch(EObject object) {
        return indicatorSwitch.doSwitch(object);
    }
}
