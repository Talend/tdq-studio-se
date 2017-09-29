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
package org.talend.dataquality.helpers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.indicators.Indicator;
import org.talend.i18n.Messages;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * Creates an Analysis and its attribute from the given informations. Use one AnalysisBuilder per Analysis to create.
 * 
 */
public class AnalysisBaseBuilder {

    /**
     * 
     */
    private static final String ANALYSIS_NOT_INITIALIZED = "Analysis has not been initialized. Call initializeAnalysis() method before."; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(AnalysisBaseBuilder.class);

    private boolean initialized = false;

    protected Analysis analysis;

    /**
     * Method "initializeAnalysis" creates an Analysis from the given name.
     * 
     * @param analysisName
     * @return
     */
    public boolean initializeAnalysis(String analysisName, AnalysisType type) {
        if (initialized) {
            log.warn(Messages.getString("AnalysisBuilder.ANALYSISALREADYINITIALIZED"));//$NON-NLS-1$
            return false;
        }

        // create the analysis
        this.analysis = AnalysisHelper.createAnalysis(analysisName);

        // set the creation date
        Date date = new Date(System.currentTimeMillis());
        analysis.setCreationDate(date);

        // prepare the context
        analysis.setContext(createAnalysisContext());

        // prepare the parameters
        analysis.setParameters(createAnalysisParameters(type));

        // prepare result
        analysis.setResults(createAnalysisResult());

        initialized = true;
        return initialized;
    }

    private AnalysisParameters createAnalysisParameters(AnalysisType type) {
        AnalysisParameters context = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        context.setAnalysisType(type);
        return context;
    }

    private AnalysisContext createAnalysisContext() {
        AnalysisContext context = AnalysisFactory.eINSTANCE.createAnalysisContext();
        return context;
    }

    private AnalysisResult createAnalysisResult() {
        AnalysisResult result = AnalysisFactory.eINSTANCE.createAnalysisResult();
        // create its metadata
        ExecutionInformations executionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        result.setResultMetadata(executionInformations);
        return result;
    }

    public boolean setAnalysisConnection(DataManager dataManager) {
        if (!initialized) {
            log.error(ANALYSIS_NOT_INITIALIZED);
            return false;
        }
        AnalysisContext context = analysis.getContext();
        if (context == null) {
            log.error(Messages.getString("AnalysisBuilder.CONTEXTISNULL"));//$NON-NLS-1$
            return false;
        }
        context.setConnection(dataManager);
        return true;
    }

    /**
     * Method "addElementToAnalyze". The association between the element and the indicators is not done in this method.
     * It must be done before.
     * 
     * @param element the element to analyze (for several columns, it is a QueryColumnSet)
     * @param indicator the indicator used for analyzing the element (or part of it).
     * @return true if ok
     */
    public boolean addElementToAnalyze(ModelElement element, Indicator... indicators) {

        // add the element to the context
        analysis.getContext().getAnalysedElements().add(element);

        for (Indicator indicator : indicators) {
            // do not attach element to indicators (because they should be attached outside in the case of a column
            // set.)
            // indicator.setAnalyzedElement(element);
            // store indicators in results
            analysis.getResults().getIndicators().add(indicator);
        }

        return true;
    }

    /**
     * DOC abensalem Comment method "addAndLinkElementToAnalyze". Link the analysed element to the indicators.
     * 
     * @param element the element to analyze (for several columns, it is a QueryColumnSet)
     * @param indicators the indicator used for analyzing the element (or part of it).
     * @return true if ok
     */
    public boolean addAndLinkElementToAnalyze(ModelElement element, Indicator... indicators) {

        // add the element to the context
        analysis.getContext().getAnalysedElements().add(element);
        EList<Indicator> indicatorList = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            boolean exists = false;
            Indicator existingIndicator = null;
            for (Indicator i : indicatorList) {
                if (i.getAnalyzedElement().equals(element) && indicator.getName().equals(i.getName())) {
                    existingIndicator = i;
                    exists = true;
                    break;
                }
            }

            if (exists) {
                indicatorList.remove(existingIndicator);
            }
            // attach element to indicators
            indicator.setAnalyzedElement(element);
            // store indicators in results
            indicatorList.add(indicator);
        }

        return true;
    }

    public boolean addElementsToAnalyze(ModelElement[] elements, Indicator... indicators) {

        for (ModelElement element : elements) {
            // store element in context
            addElementToContext(element);
        }
        for (Indicator indicator : indicators) {
            // do not attach element to indicators (because they should be attached outside in the case of a column
            // set.)
            // indicator.setAnalyzedElement(element);
            // store indicators in results
            analysis.getResults().getIndicators().add(indicator);
        }

        return true;

    }

    /**
     * Method "addFilterOnData". Several filter can be added for one analysis.
     * 
     * @param dataFilter the data filter
     * @return
     */
    public boolean addFilterOnData(Domain dataFilter) {
        return analysis.getParameters().getDataFilter().add(dataFilter);
    }

    /**
     * DOC scorreia Comment method "addElementToContext".
     * 
     * @param element
     */
    private void addElementToContext(ModelElement element) {
        // add the element to the context
        analysis.getContext().getAnalysedElements().add(element);
    }

    /**
     * Set analysis.
     * 
     * @param analysis
     */
    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    /**
     * Method "getAnalysis".
     * 
     * @return the analysis built and filled in other methods.
     */
    public Analysis getAnalysis() {
        return this.analysis;
    }

}
