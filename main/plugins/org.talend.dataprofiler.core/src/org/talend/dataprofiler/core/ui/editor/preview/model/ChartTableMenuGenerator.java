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
package org.talend.dataprofiler.core.ui.editor.preview.model;

import java.util.Map;

import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class ChartTableMenuGenerator {

    public static MenuItemEntity[] generate(IDataExplorer explorer, Analysis analysis, ChartDataEntity entity) {

        if (explorer != null) {
            explorer.setAnalysis(analysis);
            explorer.setEnitty(entity);
            Map<String, String> queryMap = explorer.getQueryMap();
            MenuItemEntity[] returnList = new MenuItemEntity[queryMap.size()];

            int i = 0;
            for (String key : queryMap.keySet()) {
                MenuItemEntity itemEntity = new MenuItemEntity(key, null, queryMap.get(key));
                returnList[i] = itemEntity;
                i++;
            }

            return returnList;
        } else {
            return new MenuItemEntity[0];
        }
    }
}
