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
package org.talend.dq.analysis.explore;

import java.util.Map;

import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * @author scorreia
 * 
 * Interface for exploring data from a specific indicator.
 */
public interface IDataExplorer {

    /**
     * Method "setAnalysis".
     * 
     * @param analysis the analysis from which the exploration must be done.
     * @return false if the given analysis cannot be explored
     */
    public boolean setAnalysis(Analysis analysis);

    /**
     * DOC Zqin Comment method "setEnitty".
     * 
     * @param entity
     */
    public void setEnitty(ChartDataEntity entity);

    /**
     * DOC Zqin Comment method "getQueryMap".
     * 
     * @return
     */
    public Map<String, String> getQueryMap();
}
