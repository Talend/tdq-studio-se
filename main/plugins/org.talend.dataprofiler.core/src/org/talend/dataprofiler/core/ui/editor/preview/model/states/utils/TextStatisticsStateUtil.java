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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.utils;

import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.TextStatisticsExplorer;

/**
 * created by yyin on 2014-12-2 Detailled comment
 * 
 */
public class TextStatisticsStateUtil {

    public static DataExplorer getDataExplorer() {
        return new TextStatisticsExplorer();
    }
}
