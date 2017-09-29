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
package org.talend.dataprofiler.core.pattern;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EmfFileResourceUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternPackage;
import org.talend.dataquality.helpers.DataqualitySwitchHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.model.emf.CwmResource;
import orgomg.cwm.objectmodel.core.ModelElement;

public class PatternDefinitionHandler {

    private static Logger log = Logger.getLogger(DefinitionHandler.class);

    private static PatternDefinitionHandler instance;

    private List<Pattern> patternDefinitions = new ArrayList<Pattern>();

    private Map<String, String> patternToIdMap = new HashMap<String, String>();

    public static PatternDefinitionHandler getInstance() {
        if (instance == null) {
            instance = new PatternDefinitionHandler();

            instance.initializePatternDefinitions();
        }
        return instance;
    }

    public Pattern getPatternById(String definitionId) {
        for (Pattern patDef : this.getPatternDefinitions()) {
            CwmResource resource = (CwmResource) patDef.eResource();
            EObject object = resource.getEObject(definitionId);
            if (object != null && PatternPackage.eINSTANCE.getPattern().equals(object.eClass())) {
                return (Pattern) object;
            }
        }
        return null;
    }

    public String getIdByPattern(Pattern pattern) {
        String id = patternToIdMap.get(pattern.getName());
        if (id == null) {
            id = ResourceHelper.getUUID(pattern);
            patternToIdMap.put(pattern.getName(), id);
        }
        return id;
    }

    public Pattern getPatternDefinitionBySemanticLabel(String name) {
        for (Pattern patternDefinition : this.getPatternDefinitions()) {
            if (patternDefinition != null && patternDefinition.getName() != null
                    && patternDefinition.getName().replace(" ", "_").compareTo(name) == 0) {
                return patternDefinition;
            }
        }
        return null;
    }

    public List<Pattern> getPatternDefinitions() {
        if (patternDefinitions == null || patternDefinitions.isEmpty()) {
            initializePatternDefinitions();
        } else {
            // resolve the IndicatorDefinition if need and only when it isn't initialized.
            patternDefinitions = resolve(patternDefinitions);
            if (patternDefinitions == null) {
                throw new RuntimeException(Messages.getString("DefinitionHandler.IndicatorsDefinition")); //$NON-NLS-1$
            }
        }
        return patternDefinitions;
    }

    /**
     * resolve the IndicatorDefinition if it is proxy.
     * 
     * @param definitions
     * @return
     */
    private List<Pattern> resolve(List<Pattern> definitions) {
        List<Pattern> result = new ArrayList<Pattern>();
        if (definitions != null && !definitions.isEmpty()) {
            for (Pattern indDef : definitions) {
                if (indDef.eIsProxy()) {
                    indDef = (Pattern) EObjectHelper.resolveObject(indDef);
                }
                result.add(indDef);
            }
        }
        return result;
    }

    private void initializePatternDefinitions() {
        this.patternDefinitions.clear();
        if (Platform.isRunning()) {
            patternDefinitions.addAll((List<Pattern>) PatternResourceFileHelper.getInstance().getAllElement());
        } else {
            patternDefinitions.addAll((List<Pattern>) getAllPatternModelElements(DefinitionHandler.getInstance().getTdqLibPath()
                    + "Patterns" + File.separator + "Regex")); //$NON-NLS-1$
        }
    }

    private List<? extends ModelElement> getAllPatternModelElements(String parentFolder) {
        List<ModelElement> elementList = new ArrayList<ModelElement>();
        try {
            List<File> allFiles = searchAllFiles(new File(parentFolder));
            // MOD qiongli 2011-4-19.bug 20566,avoid NPE
            ModelElement mod = null;
            for (File file : allFiles) {
                mod = getPatternModelElement(file);
                if (mod != null) {
                    // MOD msjian TDQ-4672 2012-2-17: modify another issue
                    elementList.add(mod);
                    // TDQ-4672~
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return elementList;
    }

    private List<File> searchAllFiles(File file) {
        List<File> fileList = new ArrayList<File>();
        if (file.exists()) {
            for (File child : file.listFiles()) {
                if (child.isDirectory()) {
                    fileList.addAll(searchAllFiles(child));
                    continue;
                }

                if (checkFile(child)) {
                    fileList.add(child);
                }
            }
        }
        return fileList;
    }

    private final ModelElement getPatternModelElement(File file) {
        if (file != null) {
            Resource resource = EmfFileResourceUtil.getInstance().getFileResource(file.getAbsolutePath());

            EList<EObject> contents = resource.getContents();
            if (contents.isEmpty()) {
                log.error("no content in: " + resource);//$NON-NLS-1$
            }

            for (EObject object : contents) {
                ModelElement switchObject = DataqualitySwitchHelper.PATTERN_SWITCH.doSwitch(object);

                if (switchObject != null) {
                    return switchObject;
                }
            }
        }
        return null;
    }

    private boolean checkFile(File file) {
        String path = file.getAbsolutePath();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        return file != null && FactoriesUtil.PATTERN.equalsIgnoreCase(extension);
    }
}
