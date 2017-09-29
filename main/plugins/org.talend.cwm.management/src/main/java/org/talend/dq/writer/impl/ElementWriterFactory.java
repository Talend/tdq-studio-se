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

import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.helper.ModelElementIdentifier;
import org.talend.dq.writer.AElementPersistance;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class ElementWriterFactory {

    private static ElementWriterFactory instance;

    public ElementWriterFactory() {
    }

    /**
     * DOC bZhou Comment method "getInstance".
     *
     * @return
     */
    public static ElementWriterFactory getInstance() {
        if (instance == null) {
            instance = new ElementWriterFactory();
        }
        return instance;
    }

    /**
     * DOC bZhou Comment method "createAnalysisWrite".
     *
     * @return
     */
    public AnalysisWriter createAnalysisWrite() {
        return new AnalysisWriter();
    }

    /**
     * DOC bZhou Comment method "createReportWriter".
     *
     * @return
     */
    public ReportWriter createReportWriter() {
        return new ReportWriter();
    }

    /**
     * DOC bZhou Comment method "createDataProviderWriter".
     *
     * @return
     */
    public DataProviderWriter createDataProviderWriter() {
        return new DataProviderWriter();
    }

    /**
     * DOC bZhou Comment method "createPatternWriter".
     *
     * @return
     */
    public PatternWriter createPatternWriter() {
        return new PatternWriter();
    }

    /**
     * DOC bZhou Comment method "createdRuleWriter".
     *
     * @return
     */
    public DQRuleWriter createdRuleWriter() {
        return new DQRuleWriter();
    }

    /**
     * 
     * create MatchRule writer
     * 
     * @return
     */
    public MatchRuleDefinitionWriter createdMatchRuleWriter() {
        return new MatchRuleDefinitionWriter();
    }

    /**
     * DOC bZhou Comment method "createSoftwareSystemWriter".
     *
     * @return
     */
    public SoftwareSystemWriter createSoftwareSystemWriter() {
        return new SoftwareSystemWriter();
    }

    /**
     * DOC bZhou Comment method "createIndicatorDefinitionWriter".
     *
     * @return
     */
    public IndicatorDefinitionWriter createIndicatorDefinitionWriter() {
        return new IndicatorDefinitionWriter();
    }

    /**
     * DOC bZhou Comment method "create".
     *
     * @param fileExtension
     * @return
     */
    public AElementPersistance create(String fileExtension) {

        if (FactoriesUtil.ANA.equals(fileExtension)) {
            return createAnalysisWrite();
        } else if (FactoriesUtil.REP.equals(fileExtension)) {
            return createReportWriter();
        } else if (FactoriesUtil.PROV.equals(fileExtension)) {
            return createDataProviderWriter();
        } else if (FactoriesUtil.PATTERN.equals(fileExtension)) {
            return createPatternWriter();
        } else if (FactoriesUtil.DQRULE.equals(fileExtension)) {
            return createdRuleWriter();
        } else if (FactoriesUtil.SOFTWARE_SYSTEM.equals(fileExtension)) {
            return createSoftwareSystemWriter();
        } else if (FactoriesUtil.DEFINITION.equals(fileExtension)) {
            return createIndicatorDefinitionWriter();
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "create".
     *
     * @param element
     * @return
     */
    public AElementPersistance create(ModelElement element) {
        if (ModelElementIdentifier.isAnalysis(element)) {
            return createAnalysisWrite();
        } else if (ModelElementIdentifier.isReport(element)) {
            return createReportWriter();
        } else if (ModelElementIdentifier.isDataProvider(element)) {
            return createDataProviderWriter();
        } else if (ModelElementIdentifier.isPattern(element)) {
            return createPatternWriter();
        } else if (ModelElementIdentifier.isID(element)) {
            if (ModelElementIdentifier.isDQRule(element) || ModelElementIdentifier.isMatchRule(element)) {
                return createdRuleWriter();
            }
            return createIndicatorDefinitionWriter();
        } else if (ModelElementIdentifier.isSoftware(element)) {
            return createSoftwareSystemWriter();
        }
        return null;
    }

    /**
     * create a Writer by the type of Item
     */
    public AElementPersistance create(Item currItem) {
        if (currItem instanceof TDQAnalysisItem) {
            return createAnalysisWrite();
        } else if (currItem instanceof TDQReportItem) {
            return createReportWriter();
        } else if (currItem instanceof ConnectionItem) {
            return createDataProviderWriter();
        } else if (currItem instanceof TDQPatternItem) {
            return createPatternWriter();
        } else if (currItem instanceof TDQIndicatorDefinitionItem) {
            return createIndicatorDefinitionWriter();
        } else if (currItem instanceof TDQBusinessRuleItem) {
            return createdRuleWriter();
        }
        return null;
    }
}
