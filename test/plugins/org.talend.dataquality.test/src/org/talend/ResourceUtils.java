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
package org.talend;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.Indicator;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class ResourceUtils {

    public static void setUUID(EObject parent, EObject eobject) {
        if (eobject.eResource() == null) {
            XMLResource xmlResource = parent != null && parent.eResource() != null ? (XMLResource) parent.eResource()
                    : new XMLResourceImpl();
            xmlResource.getContents().add(eobject);
        }
        Resource res = eobject.eResource();
        if (res instanceof XMLResource) {
            ((XMLResource) res).setID(eobject, EcoreUtil.generateUUID());
        }
    }

    public static MetadataColumn createMetadataColumn(String columnName) {
        MetadataColumn creatMetadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        setUUID(null, creatMetadataColumn);
        // creatMetadataColumn.setId(ProxyRepositoryFactory.getInstance().getNextId());
        creatMetadataColumn.setName(columnName);
        creatMetadataColumn.setLabel(columnName);
        return creatMetadataColumn;
    }

    public static Analysis createAnalysis(Indicator indicator) {
        AnalysisParameters createAnalysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        setUUID(null, createAnalysisParameters);
        createAnalysisParameters.setAnalysisType(AnalysisType.MULTIPLE_COLUMN);
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        setUUID(null, createAnalysis);
        AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        setUUID(null, createAnalysisContext);
        MetadataColumn createMetadataColumn = createMetadataColumn("columnName"); //$NON-NLS-1$
        createAnalysisContext.getAnalysedElements().add(createMetadataColumn);
        createAnalysis.setContext(createAnalysisContext);
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        setUUID(null, createAnalysisResult);
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        setUUID(null, createExecutionInformations);
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(indicator);
        indicator.setAnalyzedElement(createMetadataColumn);
        createAnalysis.setResults(createAnalysisResult);
        createAnalysis.setParameters(createAnalysisParameters);
        createAnalysis.setName("anaName"); //$NON-NLS-1$
        MetadataHelper.setAuthor(createAnalysis, "shenze"); //$NON-NLS-1$
        createAnalysis.setCreationDate(new Date(System.currentTimeMillis()));
        setUUID(null, indicator);
        return createAnalysis;
    }

}
