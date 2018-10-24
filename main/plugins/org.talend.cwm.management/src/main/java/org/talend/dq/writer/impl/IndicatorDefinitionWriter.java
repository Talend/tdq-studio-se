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
package org.talend.dq.writer.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorDefinitionWriter extends AElementPersistance {

    private Logger log = Logger.getLogger(IndicatorDefinitionWriter.class);

    /**
     * DOC bZhou SYSIndicatorWriter constructor comment.
     */
    IndicatorDefinitionWriter() {
    }

    @Override
    public void addDependencies(ModelElement element) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#getFileExtension()
     */
    @Override
    protected String getFileExtension() {
        return FactoriesUtil.DEFINITION;
    }

    @Override
    public ReturnCode save(final Item item, final boolean careDependency) {
        ReturnCode rc = new ReturnCode();
        RepositoryWorkUnit<Object> repositoryWorkUnit =
                new RepositoryWorkUnit<Object>("save Indicator Definition item") { //$NON-NLS-1$

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        TDQIndicatorDefinitionItem indicatorItem = (TDQIndicatorDefinitionItem) item;
                        IndicatorDefinition indiDefinition = indicatorItem.getIndicatorDefinition();
                        String oldName = indiDefinition.getLabel();
                        indiDefinition.setLabel(indiDefinition.getName());
                        // updateProperty(indiDefinition);
                        // MOD yyi 2012-02-07 TDQ-4621:Update dependencies when careDependency is true.
                        if (careDependency) {
                            saveWithDependencies(indicatorItem, indiDefinition);
                            updateDependencies(indiDefinition);
                        } else {
                            saveWithoutDependencies(indicatorItem, indiDefinition);
                        }

                        checkNameUpdate(oldName, item);
                    }

                };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        try {
            repositoryWorkUnit.throwPersistenceExceptionIfAny();
        } catch (PersistenceException e) {
            log.error(e, e);
            rc.setOk(Boolean.FALSE);
            rc.setMessage(e.getMessage());
        }

        return rc;
    }

    /**
     * when the udi name is changed, it will be losted in the indicator selection dialog. so need to reload(TDQ-14249)
     * 
     * @param oldName
     * @param item
     */
    private void checkNameUpdate(String oldName, Item item) {
        String newName = item.getProperty().getLabel();
        if (!StringUtils.equals(newName, oldName)) {
            IndicatorDefinition oldIndicatorDefinition =
                    DefinitionHandler.getInstance().getIndicatorDefinition(oldName);
            IndicatorDefinition newIndicatorDefinition =
                    DefinitionHandler.getInstance().getIndicatorDefinition(newName);
            if (newIndicatorDefinition == null && oldIndicatorDefinition != null) {
                DefinitionHandler.getInstance().reloadAllUDIs();
            }
        }
    }

    @Override
    protected void notifyResourceChanges() {
        ProxyRepositoryManager.getInstance().save();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#updateDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    // @Override
    protected void updateDependencies(ModelElement element) {
        // update client dependency
        // if IndicatorDefinition have client depencency, add codes here
        IndicatorDefinition definition = (IndicatorDefinition) element;
        Property property = PropertyHelper.getProperty(definition);
        List<IRepositoryViewObject> listIndicatorDependency =
                DependenciesHandler.getInstance().getIndicatorDependency(new RepositoryViewObject(property));
        for (IRepositoryViewObject viewObject : listIndicatorDependency) {
            Item item = viewObject.getProperty().getItem();
            if (item instanceof TDQAnalysisItem) {
                try {
                    ProxyRepositoryFactory.getInstance().save(item);
                } catch (PersistenceException e) {
                    log.error(e, e);
                }
            }
        }
    }

    /**
     * Added 20130115 yyin TDQ-3249, make the system indicator display international. but for the user defined
     * indicator, no need.
     */
    @Override
    public Property initProperty(ModelElement modelElement) {
        Property property = super.initProperty(modelElement);

        // if the indicator is the user defined indicator, no need to internationalize it.
        if (isUserDefinedIndicator((IndicatorDefinition) modelElement)) {
            return property;
        }
        // For system indicator, make its display name international.(means its display name is in the messages file)
        // MOD sizhaoliu TDQ-7454 do not translate here, but during the initialization of RepositoryViewObjects
        property.setDisplayName(modelElement.getName());
        return property;
    }

    private boolean isUserDefinedIndicator(IndicatorDefinition indicator) {
        EList<IndicatorCategory> categories = indicator.getCategories();
        if (categories != null && categories.size() > 0) {
            String categoryLabel = categories.get(0).getLabel();
            if (categoryLabel != null && categoryLabel.startsWith("User Define")) {//$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * Added yyin 20130118 TDQ-3249, when importing, should also consider the international as init.
     */
    @Override
    public void updateProperty(ModelElement element) {
        super.updateProperty(element);
        Property property = PropertyHelper.getProperty(element);
        // if the indicator is the user defined indicator, no need to internationalize it.
        if (isUserDefinedIndicator((IndicatorDefinition) element)) {
            // Added TDQ-14249 rename udi, 20170920
            if (property != null && !StringUtils.equals(property.getDisplayName(), element.getName())) {
                property.setDisplayName(element.getName());
            }
            return;
        }
        if (property != null) {
            property.setDisplayName(org.talend.cwm.management.i18n.Messages.getString(element.getName().replace(' ',
                    '.')));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#removeDependencies(org.talend.core.model.properties.Item)
     */
    @Override
    protected ReturnCode removeDependencies(Item item) {
        return new ReturnCode(true);
    }
}
