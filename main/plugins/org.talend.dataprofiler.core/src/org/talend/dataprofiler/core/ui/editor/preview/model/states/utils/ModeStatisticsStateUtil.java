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

import org.apache.commons.lang.StringUtils;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class ModeStatisticsStateUtil {

    /**
     * DOC yyin Comment method "createDataEntity".
     * 
     * @param unit
     * @param label
     * @return
     */
    public static ChartDataEntity createDataEntity(IndicatorUnit unit, String label) {
        ChartDataEntity entity = new ChartDataEntity();
        entity.setIndicator(unit.getIndicator());
        entity.setLabel(label);
        Object value = unit.getValue();
        entity.setValue(String.valueOf(value == null ? StringUtils.EMPTY : value));
        return entity;
    }

}
