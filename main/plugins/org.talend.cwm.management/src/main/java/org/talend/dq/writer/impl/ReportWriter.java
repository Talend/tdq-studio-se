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
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * @author scorreia
 * 
 * This class saves the analysis.
 */
public class ReportWriter extends AElementPersistance {

    static Logger log = Logger.getLogger(ReportWriter.class);

    /**
     * DOC bZhou ReportWriter constructor comment.
     */
    ReportWriter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    public void addDependencies(ModelElement element) {
        TdReport report = (TdReport) element;
        List<Analysis> analyses = ReportHelper.getAnalyses(report);
        for (Analysis ana : analyses) {
            // TDQ-7999,filter the proxy.
            if (!ana.eIsProxy()) {
                AnalysisRepNode recursiveFindAnalysis = RepositoryNodeHelper.recursiveFindAnalysis(ana);
                // only do save when the dependency is not reference project node
                if (recursiveFindAnalysis != null && recursiveFindAnalysis.getProject().isMainProject()) {
                    TypedReturnCode<Dependency> dependencyReturn = DependenciesHandler.getInstance().setDependencyOn(report, ana);
                    if (dependencyReturn.isOk()) {
                        try {
                            Property property = PropertyHelper.getProperty(ana);
                            if (property != null) {
                                Item item = property.getItem();
                                if (item instanceof TDQAnalysisItem) {
                                    TDQAnalysisItem anaItem = (TDQAnalysisItem) item;
                                    anaItem.setAnalysis(ana);
                                }
                            }
                            ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager()
                                    .saveResource(ana.eResource());
                        } catch (PersistenceException e) {
                            log.error(e, e);
                        }
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
        return FactoriesUtil.REP;
    }

    @Override
    public ReturnCode save(final Item item, final boolean careDependency) {
        ReturnCode rc = new ReturnCode();
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("save Report item") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                TDQReportItem repItem = (TDQReportItem) item;
                Report report = repItem.getReport();
                // MOD yyi 2012-02-07 TDQ-4621:Update dependencies(connection) when careDependency is true.
                if (careDependency) {
                    saveWithDependencies(repItem, report);
                } else {
                    saveWithoutDependencies(repItem, report);
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
        TDQReportItem anaItem = (TDQReportItem) item;
        TdReport report = (TdReport) anaItem.getReport();
        List<Property> clintDependency = DependenciesHandler.getInstance().getClintDependency(anaItem.getProperty());
        EList<AnalysisMap> analysisMap = report.getAnalysisMap();
        for (Property currentClient : clintDependency) {
            ModelElement modelElement = PropertyHelper.getModelElement(currentClient);
            if (!analysisMap.contains(modelElement)) {
                boolean isSuccess = DependenciesHandler.getInstance().removeDependenciesBetweenModel(report, modelElement);
                if (isSuccess) {
                    rc.setOk(EMFUtil.saveSingleResource(modelElement.eResource()));

                } else {
                    rc.setOk(false);
                    rc.setMessage(Messages.getString("ReportWriter.CanNotFindDependencyElement", report.getName(), //$NON-NLS-1$
                            modelElement.getName()));
                }
            }
        }
        return rc;
    }
}
