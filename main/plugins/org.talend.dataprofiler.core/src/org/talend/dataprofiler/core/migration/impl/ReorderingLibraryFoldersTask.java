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
package org.talend.dataprofiler.core.migration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.DataqualitySwitchHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * 
 * DOC mzhao 2009-07-01 feature 7482.
 */
public class ReorderingLibraryFoldersTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(ReorderingLibraryFoldersTask.class);

    private static final String SQL_PATTERNS = "SQL Patterns"; //$NON-NLS-1$

    private static final String DQ_RULES = "DQ Rules"; //$NON-NLS-1$

    private static final String JRXML_REPORTS = "JRXML Reports"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        IFolder libraryFolder = ResourceManager.getLibrariesFolder();

        // Patterns -> Patterns/Regex
        IFolder patternFolder = ResourceManager.getPatternFolder();
        IFolder regexFolder = ResourceManager.getPatternRegexFolder();
        if (!regexFolder.exists()) {
            IFolder newRegexSubfolder = createSubfolder(patternFolder, EResourceConstant.PATTERN_REGEX.getName());
            moveItems(patternFolder, newRegexSubfolder);
        }

        // SQL Patterns -> Patterns/SQL
        IFolder sqlFolder = ResourceManager.getPatternSQLFolder();
        if (!sqlFolder.exists()) {
            IFolder oldSqlPatternsFolder = libraryFolder.getFolder(SQL_PATTERNS);
            IFolder newSqlSubfolder = createSubfolder(patternFolder, EResourceConstant.PATTERN_SQL.getName());
            moveItems(oldSqlPatternsFolder, newSqlSubfolder);
            oldSqlPatternsFolder.delete(true, null);
        }

        // DQ Rules -> Rules/SQL
        IFolder dqRuleFolder = ResourceManager.getRulesSQLFolder();
        if (!dqRuleFolder.exists()) {
            IFolder oldDqRulesFolder = libraryFolder.getFolder(DQ_RULES);
            IFolder newRulesFolder = createSubfolder(libraryFolder, EResourceConstant.RULES.getName());
            IFolder newRulesSQLSubfolder = createSubfolder(newRulesFolder, EResourceConstant.RULES_SQL.getName());
            moveItems(oldDqRulesFolder, newRulesSQLSubfolder);
            oldDqRulesFolder.delete(true, null);
        }

        // JRXML Reports -> JRXML Template
        IFolder jrxmlFolder = ResourceManager.getJRXMLFolder();
        if (!jrxmlFolder.exists()) {
            IFolder oldJrxmlFolder = libraryFolder.getFolder(JRXML_REPORTS);
            IFolder newJrxmlFolder = createSubfolder(libraryFolder, EResourceConstant.JRXML_TEMPLATE.getName());
            moveItems(oldJrxmlFolder, newJrxmlFolder);
            oldJrxmlFolder.delete(true, null);
        }

        // Refresh project
        ResourceService.refreshStructure();

        return true;
    }

    /**
     * DOC scorreia Comment method "createSubfolder".
     * 
     * @param newPatternFolder
     * @return
     * @throws CoreException
     */
    private IFolder createSubfolder(IFolder newPatternFolder, final String folderName) throws CoreException {
        return DQStructureManager.getInstance().createNewFolder(newPatternFolder, folderName);
    }

    private void moveItems(IFolder oldSubFolder, IFolder newSubfolder) throws CoreException {

        if (!oldSubFolder.exists()) {
            return;
        }

        for (IResource oldResource : oldSubFolder.members()) {
            if (newSubfolder.getName().equals(oldResource.getName())) {
                continue;
            }

            // cannot simply copy EMF files: need to keep the links between files when moving them. See bug 9461
            if (oldResource instanceof IFolder) {
                IFolder oldFolder = (IFolder) oldResource;

                IFolder newFolder = DQStructureManager.getInstance().createNewFolder(newSubfolder, oldFolder.getName());

                moveItems(oldFolder, newFolder);
                // delete folder
                oldFolder.delete(true, null);
            }

            if (oldResource instanceof IFile) {
                IFile file = (IFile) oldResource;
                final ModelElement eltFromLibraryFolder = getModelElement(file);
                final EList<Dependency> supplierDependency = eltFromLibraryFolder.getSupplierDependency();
                if (supplierDependency.isEmpty()) {
                    // simple copy of file is enough
                    oldResource.copy(newSubfolder.getFolder(oldResource.getName()).getFullPath(), true, null);
                } else {
                    // handle dependent analyses
                    List<Analysis> analyses = new ArrayList<Analysis>();
                    for (Dependency dependency : supplierDependency) {
                        URI newUri = URI.createPlatformResourceURI(newSubfolder.getFullPath().toOSString(), true);
                        // move pattern
                        EMFUtil.changeUri(eltFromLibraryFolder.eResource(), newUri);
                        final EList<ModelElement> clientAnalyses = dependency.getClient();
                        for (ModelElement modelElement : clientAnalyses) {
                            Analysis analysis = DataqualitySwitchHelper.ANALYSIS_SWITCH.doSwitch(modelElement);

                            if (analysis != null) {
                                analyses.add(analysis);
                            }
                        }
                    }

                    // clean the dependencies that do not refer to an existing object.
                    for (Analysis analysis : analyses) {
                        final EList<Dependency> clientDependency = analysis.getClientDependency();
                        List<Dependency> newClientDeps = new ArrayList<Dependency>();
                        for (Dependency dependency : clientDependency) {
                            if (!dependency.eIsProxy()) {
                                newClientDeps.add(dependency);
                            }
                        }
                        clientDependency.clear();
                        clientDependency.addAll(newClientDeps);
                        AnaResourceFileHelper.getInstance().save(analysis);
                    }
                }

                remove(file);
            }
        }
    }

    /**
     * DOC bZhou Comment method "remove".
     * 
     * @param file
     */
    private void remove(IFile file) throws CoreException {
        file.delete(true, null);
    }

    /**
     * DOC scorreia Comment method "getEMFObject".
     * 
     * @param file
     * @param folderProperty
     * @return
     */
    private ModelElement getModelElement(IFile file) {
        String fileExtension = file.getFileExtension();
        if (FactoriesUtil.isPatternFile(fileExtension)) {
            return PatternResourceFileHelper.getInstance().findPattern(file);
        } else if (FactoriesUtil.isDQRuleFile(fileExtension)) {
            return DQRuleResourceFileHelper.getInstance().findWhereRule(file);
        }
        log.error(DefaultMessagesImpl.getString("ReorderingLibraryFoldersTask_LogErr", file.getFullPath().toOSString())); //$NON-NLS-1$
        return null;

    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.STUCTRUE;
    }

    public Date getOrder() {
        return createDate(2009, 07, 01);
    }

}
