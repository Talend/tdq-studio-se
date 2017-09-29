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
package org.talend.dataprofiler.core.service;

import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IDatabaseJobService extends IJobService {

    /**
     * DOC bZhou Comment method "setIndicator".
     * 
     * @param indicator
     */
    public void setIndicator(Indicator indicator);

    /**
     * DOC bZhou Comment method "setAnalysis".
     * 
     * @param analysis
     */
    public void setAnalysis(Analysis analysis);
}
