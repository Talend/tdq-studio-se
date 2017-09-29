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
package org.talend.dataprofiler.core.ui.views.provider;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.manager.DQStructureMessage;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author rli
 * @deprecated use DQRepositoryViewLabelProvider
 */
@Deprecated
public class ResourceViewLabelProvider extends WorkbenchLabelProvider implements ICommonLabelProvider {

    private static Logger log = Logger.getLogger(ResourceViewLabelProvider.class);

    public void init(ICommonContentExtensionSite aConfig) {
        // don't need do anything
    }

    @Override
    protected ImageDescriptor decorateImage(ImageDescriptor input, Object element) {
        ImageDescriptor image = super.decorateImage(input, element);

        if (element instanceof IFile) {
            IFile file = (IFile) element;
            String fileExtension = file.getFileExtension();
            // MOD qiongli 2010-9-7,bug 14698,add 'try...catch'
            try {
                if (FactoriesUtil.isPatternFile(fileExtension)) {
                    image = ImageLib.getImageDescriptor(ImageLib.PATTERN_REG);

                    Pattern pattern = PatternResourceFileHelper.getInstance().findPattern(file);
                    if (pattern != null) {
                        if (!TaggedValueHelper.getValidStatus(pattern)) {
                            image = ImageLib.createInvalidIconDes(ImageLib.PATTERN_REG);
                        }
                    }
                } else if (FactoriesUtil.isReportFile(fileExtension)) {
                    image = ImageLib.getImageDescriptor(ImageLib.REPORT_OBJECT);
                } else if (FactoriesUtil.isUDIFile(fileExtension)) {
                    image = ImageLib.getImageDescriptor(ImageLib.IND_DEFINITION);

                    IndicatorDefinition udi = IndicatorResourceFileHelper.getInstance().findIndDefinition(file);
                    if (udi != null) {
                        boolean validStatus = TaggedValueHelper.getValidStatus(udi) | UDIHelper.isUDIValid(udi);

                        if (!validStatus) {
                            image = ImageLib.createInvalidIconDes(ImageLib.IND_DEFINITION);
                        }
                    }
                } else if (FactoriesUtil.isAnalysisFile(fileExtension)) {
                    // ADD qiongli 2010-8-9,feature 14252
                    Analysis analysis = AnaResourceFileHelper.getInstance().findAnalysis(file);
                    if (analysis != null) {
                        AnalysisHandler analysisHandler = new AnalysisHandler();
                        analysisHandler.setAnalysis(analysis);
                        if (analysisHandler.getResultMetadata().getExecutionNumber() != 0) {
                            if (!analysisHandler.getResultMetadata().isLastRunOk()) {
                                image = ImageLib.createErrorIcon(image);
                            } else if (analysisHandler.getResultMetadata().isOutThreshold()) {
                                image = ImageLib.createInvalidIcon(image);
                            }
                        }
                    }
                }
            } catch (Exception exc) {
                log.error(exc, exc);
                image = ImageLib.getImageDescriptor(ImageLib.DELETE_ACTION);
            }

            if (FactoriesUtil.isEmfFile(fileExtension)) {
                Property property = PropertyHelper.getProperty(file);
                if (property != null) {
                    Item item = property.getItem();
                    Boolean lockByOthers = ProxyRepositoryManager.getInstance().isLockByOthers(item);
                    Boolean lockByUserOwn = ProxyRepositoryManager.getInstance().isLockByUserOwn(item);
                    if (lockByOthers || lockByUserOwn) {
                        log.info(property.getLabel() + " is locked"); //$NON-NLS-1$
                        image = ImageLib.createLockedIcon(image);
                    }
                }
            }

        } else if (element instanceof IFolder) {
            IFolder folder = (IFolder) element;
            if (ResourceManager.isMetadataFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.METADATA);
            } else if (ResourceManager.isLibrariesFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.LIBRARIES);
            } else if (ResourceManager.isDataProfilingFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.DATA_PROFILING);
            } else if (ResourceManager.isDBConnectionFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.CONNECTION);
            } else if (ResourceManager.isExchangeFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.EXCHANGE);
            } else if (ResourceManager.isFileDelimitedFolder(folder)) {
                image = ImageLib.getImageDescriptor(ImageLib.FILE_DELIMITED);
            }
        }
        return image;
    }

    public String getDescription(Object anElement) {

        if (anElement instanceof IResource) {
            return ((IResource) anElement).getFullPath().makeRelative().toString();
        }
        return null;
    }

    public void restoreState(IMemento aMemento) {
        // don't need do anything
    }

    public void saveState(IMemento aMemento) {
        // don't need do anything
    }

    @Override
    protected String decorateText(String input, Object element) {
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            if (log.isDebugEnabled()) {
                log.debug("Loading file " + file.getLocation()); //$NON-NLS-1$
            }
            // MOD qiongli ,bug 14698,add 'try...catch'
            ModelElement mElement = null;
            try {
                mElement = ModelElementFileFactory.getModelElement(file);
            } catch (Exception exc) {
                log.error(exc, exc);
            }
            if (mElement != null) {
                Property property = ProxyRepositoryFactory.getInstance().getProperty(mElement);
                return DqRepositoryViewService.buildElementName(property);
            }
        }
        String tempInput = DQStructureMessage.getString(super.decorateText(input, element));

        if (element instanceof IFolder) {
            if (tempInput.startsWith(RepositoryNodeHelper.PREFIX_TDQ)) {
                tempInput = tempInput.replaceFirst(RepositoryNodeHelper.PREFIX_TDQ, ""); //$NON-NLS-1$
            }

            IFolder folder = (IFolder) element;
            if (ResourceManager.isAnalysisFolder(folder)) {
                tempInput += "(" + getFileCount(folder, new String[] { "ana" }) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } else if (ResourceManager.isReportsFolder(folder)) {
                tempInput += "(" + getFileCount(folder, new String[] { "rep" }) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }

        return super.decorateText(tempInput, element);
    }

    protected int getFileCount(IFolder parent, String[] filterExtensions) {
        int i = 0;
        List<String> extensions = Arrays.asList(filterExtensions);
        try {
            IResource[] members = parent.members();
            // MOD qiongli,feature 9486.except the logical delete resources
            IFile propFile = null;
            for (IResource resource : members) {
                if (resource instanceof IFile) {
                    if (extensions.contains(((IFile) resource).getFileExtension())) {
                        propFile = ResourcesPlugin
                                .getWorkspace()
                                .getRoot()
                                .getFile(
                                        ((IFile) resource).getFullPath().removeFileExtension()
                                                .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION));
                        if (propFile.exists()) {
                            Property property = PropertyHelper.getProperty(propFile);
                            if (!property.getItem().getState().isDeleted()) {
                                i++;
                            }
                        }

                    }
                    // MOD by zshen for bug 13755
                } else if (resource instanceof IFolder) {
                    i += getFileCount((IFolder) resource, filterExtensions);
                }
                // ~13755
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return i;
    }

}
