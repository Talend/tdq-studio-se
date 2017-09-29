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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern;

import java.util.List;

import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.SQLPatternExplorer;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class SQLPatternStatisticsTableState extends PatternStatisticsTableState {

    /**
     * DOC yyin SQLPatternStatisticsTableState constructor comment.
     * 
     * @param units
     */
    public SQLPatternStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
        // TODO Auto-generated constructor stub
    }

    @Override
    public DataExplorer getDataExplorer() {
        return new SQLPatternExplorer();
    }

}
