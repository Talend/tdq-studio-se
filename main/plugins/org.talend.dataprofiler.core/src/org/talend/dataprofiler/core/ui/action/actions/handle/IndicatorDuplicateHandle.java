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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.impl.UpdateUDIIndicatorsWithNewModelTask;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Duplicate an indicator definition
 */
public class IndicatorDuplicateHandle extends ModelElementDuplicateHandle {

    private Logger log = Logger.getLogger(IndicatorDuplicateHandle.class);

    private boolean isUserDefCategory = Boolean.FALSE;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.action.actions.handle.ModelElementDuplicateHandle#extractFolder(org.talend.core
     * .model.properties.Item, orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected IFolder extractFolder(Item oldItem, ModelElement oldObject) {
        return ResourceManager.getUDIFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.EMFResourceHandle#duplicate(java.lang.String)
     */
    @Override
    public ModelElement duplicateModelElement(ModelElement oldModelElement, String newName) {
        ModelElement duplicateModelElement = super.duplicateModelElement(oldModelElement, newName);
        IndicatorDefinition definition = (IndicatorDefinition) duplicateModelElement;

        // MOD klliu 2010-09-25 bug 15530 when duplicate the system indicator ,the definition must be reset the
        // category and the label name
        isUserDefCategory = IndicatorCategoryHelper.isUserDefCategory(UDIHelper.getUDICategory(definition));
        if (!isUserDefCategory) {
            updateCategory(duplicateModelElement, definition);
        }

        TaggedValueHelper.setValidStatus(true, definition);
        definition.setLabel(definition.getName());

        return definition;
    }

    @Override
    public Item duplicateItem(Item oldItem, String newName) throws BusinessException {
        Item duplicateItem = super.duplicateItem(oldItem, newName);
        // reload is needed after duplicate an indicator.
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        // update the udi model to new model, after have created the indicator definition item
        if (!isUserDefCategory) {
            updateUDIModel(newName, ((TDQIndicatorDefinitionItem) duplicateItem).getIndicatorDefinition());
        }

        return duplicateItem;
    }

    private void updateUDIModel(String newName, ModelElement duplicateModelElement) {
        URI uri = duplicateModelElement.eResource().getURI();
        IFile file = ResourceManager.getRoot().getFile(new Path(uri.toPlatformString(false)));

        File ifileToFile = WorkspaceUtils.ifileToFile(file);
        Map<String, String> initIndicatorReplaceMap = UpdateUDIIndicatorsWithNewModelTask.initIndicatorReplaceMap();
        if (FilesUtils.migrateFile(ifileToFile, initIndicatorReplaceMap, log)) {
            try {
                for (IRepositoryViewObject viewObject : ProxyRepositoryFactory.getInstance().getAll(
                        ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS)) {
                    ProxyRepositoryFactory.getInstance().reload(viewObject.getProperty());
                }
            } catch (PersistenceException e) {
                log.error(e);
            }
            ResourceService.refreshStructure();

            // add templates for udi only.
            List<IndicatorDefinition> indiDefinitions = DefinitionHandler.getInstance().getUserDefinedIndicatorDefinitions();
            for (IndicatorDefinition indiDefinition : indiDefinitions) {
                if (indiDefinition instanceof UDIndicatorDefinition) {
                    String name = indiDefinition.getLabel() != null ? indiDefinition.getLabel() : indiDefinition.getName();
                    if (name != null && name.equals(newName)) {
                        UDIndicatorDefinition udi = (UDIndicatorDefinition) indiDefinition;
                        udi = UDIUtils.createDefaultDrillDownList(udi);
                        ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().save(udi);
                        break;
                    }
                }
            }
        }
    }

    private void updateCategory(ModelElement duplicateModelElement, IndicatorDefinition definition) {
        IndicatorCategory category = null;
        // TDQ-13543: set all the frequency type indicator's category is UserDefinedFrequency
        if (IndicatorCategoryHelper.isFrequencyCategory(IndicatorCategoryHelper
                .getCategory((IndicatorDefinition) duplicateModelElement))) {
            category = DefinitionHandler.getInstance().getUserDefinedFrequencyIndicatorCategory();
        } else {
            category = DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory();
        }

        if (category != null) {
            UDIHelper.setUDICategory(definition, category);
        }
    }

}
