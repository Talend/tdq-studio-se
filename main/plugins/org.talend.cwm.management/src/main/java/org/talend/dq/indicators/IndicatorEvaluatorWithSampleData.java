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
package org.talend.dq.indicators;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.helper.SampleDataUtils;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class IndicatorEvaluatorWithSampleData extends IndicatorEvaluator {

    /**
     * DOC zshen IndicatorEvaluatorWithSampleData constructor comment.
     * 
     * @param analysis
     */
    public IndicatorEvaluatorWithSampleData(Analysis analysis) {
        super(analysis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#createStatement()
     */
    @Override
    protected Statement createStatement() throws SQLException {
        return SampleDataUtils.getInstance().getSampleDataStatement(this.analysis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#selectCatalog(java.lang.String)
     */
    @Override
    public boolean selectCatalog(String catalogName) {
        // there is sample data mode with java engin so that not need selectCatalog
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.Evaluator#checkConnection()
     */
    @Override
    protected ReturnCode checkConnection() {
        // sample data mode need to check connection
        return new ReturnCode(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.IndicatorEvaluator#sortColumnName(java.util.Set, java.lang.String)
     */
    @Override
    public List<String> sortColumnName(Set<String> columns, String sqlStatement) {
        // for sqlStatement.mssqlOdbc case need to be make sure it is work well
        List<String> columnNameList = new ArrayList<String>();
        Iterator<String> iterator = columns.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            columnNameList.add(next);
        }
        return columnNameList;
    }

}
