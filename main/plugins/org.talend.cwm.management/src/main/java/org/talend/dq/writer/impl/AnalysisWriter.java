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
package org.talend.dq.writer.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.rules.DQRule;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * This class saves the analysis.
 */
public class AnalysisWriter extends AElementPersistance {

    static Logger log = Logger.getLogger(AnalysisWriter.class);

    /**
     * DOC bZhou AnalysisWriter constructor comment.
     */
    AnalysisWriter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    public void addDependencies(ModelElement element) {
        Analysis analysis = (Analysis) element;

        List<IndicatorDefinition> udis = AnalysisHelper.getUserDefinedIndicators(analysis);
        try {
            for (IndicatorDefinition udi : udis) {
                if (udi == null) {
                    continue;
                }
                InternalEObject iudi = (InternalEObject) udi;
                if (!iudi.eIsProxy()) {
                    DQRepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(udi);
                    // only do save when the dependency is not reference project node
                    if (recursiveFind != null && recursiveFind.getProject().isMainProject()) {

                        TypedReturnCode<Dependency> dependencyReturn = DependenciesHandler.getInstance().setDependencyOn(
                                analysis, udi);
                        if (dependencyReturn.isOk()) {
                            Property property = PropertyHelper.getProperty(udi);
                            if (property != null) {
                                TDQItem udiItem = (TDQItem) property.getItem();
                                if (udiItem instanceof TDQIndicatorDefinitionItem) {
                                    ((TDQIndicatorDefinitionItem) udiItem).setIndicatorDefinition(udi);
                                } else if (udiItem instanceof TDQBusinessRuleItem) {
                                    ((TDQBusinessRuleItem) udiItem).setDqrule((DQRule) udi);
                                }
                            }
                            ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager()
                                    .saveResource(udi.eResource());
                        }
                    }
                }
            }

            List<Pattern> patterns = AnalysisHelper.getPatterns(analysis);
            for (Pattern pattern : patterns) {
                InternalEObject iptn = (InternalEObject) pattern;
                if (!iptn.eIsProxy()) {
                    DQRepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(pattern);
                    // only do save when the dependency is not reference project node
                    if (recursiveFind != null && recursiveFind.getProject().isMainProject()) {
                        TypedReturnCode<Dependency> dependencyReturn = DependenciesHandler.getInstance().setDependencyOn(
                                analysis, pattern);
                        if (dependencyReturn.isOk()) {
                            Property property = PropertyHelper.getProperty(pattern);
                            if (property != null && property.getItem() instanceof TDQPatternItem) {
                                TDQPatternItem patternItem = (TDQPatternItem) property.getItem();
                                patternItem.setPattern(pattern);
                            }
                            ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager()
                                    .saveResource(pattern.eResource());

                            // EMFUtil.saveSingleResource(pattern.eResource());
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addResourceContent(orgomg.cwm.objectmodel.core.ModelElement)
     * 
     * @deprecated
     */
    @Override
    protected void addResourceContent(ModelElement element) {
        Resource resource = element.eResource();
        if (resource != null) {
            EList<EObject> resourceContents = resource.getContents();
            EList<Domain> dataFilter = AnalysisHelper.getDataFilter((Analysis) element);
            List<Domain> domains = DomainHelper.getDomains(resourceContents);
            resourceContents.removeAll(domains);

            if (dataFilter != null) {
                // scorreia save them in their own file? -> no, it's ok to save them
                // in the analysis file.
                for (Domain domain : dataFilter) {
                    if (!resourceContents.contains(domain)) {
                        resourceContents.add(domain);
                    }
                }
            }
        }
    }

    @Override
    public void addResourceContent(Resource resource, ModelElement element) {
        super.addResourceContent(resource, element);

        if (resource != null) {
            EList<EObject> resourceContents = resource.getContents();

            EList<Domain> dataFilter = AnalysisHelper.getDataFilter((Analysis) element);
            if (dataFilter != null) {
                // scorreia save them in their own file? -> no, it's ok to save them
                // in the analysis file.
                for (Domain domain : dataFilter) {
                    if (!resourceContents.contains(domain)) {
                        resourceContents.add(domain);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#getFileExtension()
     */
    @Override
    protected String getFileExtension() {
        return FactoriesUtil.ANA;
    }

    /**
     * Save analysis item and update the dependencies(optional). <B>To update dependencies of the analysis the
     * #careDependency must be set as TRUE.</B>
     * 
     * @see org.talend.dq.writer.AElementPersistance#save(org.talend.core.model.properties.Item, boolean[])
     */
    @Override
    public ReturnCode save(final Item item, final boolean careDependency) {
        ReturnCode rc = new ReturnCode();
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("save Analysis item") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                // MOD yyi 2012-02-07 TDQ-4621:Update dependencies(connection) when careDependency is true.
                TDQAnalysisItem anaItem = (TDQAnalysisItem) item;
                Analysis analysis = anaItem.getAnalysis();
                if (careDependency) {
                    saveWithDependencies(anaItem, analysis);
                } else {
                    saveWithoutDependencies(anaItem, analysis);
                }

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

    @Override
    protected void notifyResourceChanges() {
        ProxyRepositoryManager.getInstance().save();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#removeDependencies(org.talend.core.model.properties.Item)
     */
    @Override
    protected ReturnCode removeDependencies(Item item) {
        ReturnCode rc = new ReturnCode();
        TDQAnalysisItem anaItem = (TDQAnalysisItem) item;
        Analysis analysis = anaItem.getAnalysis();
        List<Property> clintDependency = DependenciesHandler.getInstance().getClintDependency(anaItem.getProperty());
        List<ModelElement> clientDepListByResultList = DependenciesHandler.getInstance().getClientDepListByResult(analysis);
        for (Property currentClient : clintDependency) {
            ModelElement modelElement = PropertyHelper.getModelElement(currentClient);
            if (!clientDepListByResultList.contains(modelElement)) {
                boolean isSuccess = DependenciesHandler.getInstance().removeDependenciesBetweenModel(analysis, modelElement);
                if (isSuccess) {
                    try {
                        ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager()
                                .saveResource(modelElement.eResource());
                    } catch (PersistenceException e) {
                        log.error(e, e);
                        rc.setOk(false);
                        rc.setMessage(e.getMessage());
                    }

                } else {
                    rc.setOk(false);
                    rc.setMessage(Messages.getString("AnalysisWriter.CanNotFindDependencyElement", analysis.getName(), //$NON-NLS-1$
                            modelElement.getName()));
                }
            }
        }
        return rc;
    }

}
