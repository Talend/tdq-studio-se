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
package org.talend.dataquality.record.linkage.ui;

import java.util.List;
import java.util.TreeMap;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.service.IMatchRuleUIService;
import org.talend.dataquality.record.linkage.ui.composite.chart.MatchRuleDataChart;
import org.talend.dataquality.record.linkage.ui.composite.table.MatchRuleDataTable;
import org.talend.dataquality.record.linkage.ui.composite.utils.ImageLib;
import org.talend.dataquality.record.linkage.ui.section.MatchRuleCTabFolderRenderer;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;

public class MatchRuleUIService implements IMatchRuleUIService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#getImageByName(java.lang.String)
     */
    @Override
    public Image getImageByName(String imageName) {
        return ImageLib.getImage(imageName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#createMatchRuleCTabFolderRenderer()
     */
    @Override
    public CTabFolderRenderer createMatchRuleCTabFolderRenderer(CTabFolder configurationFolder) {
        return new MatchRuleCTabFolderRenderer(configurationFolder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#getAddImage()
     */
    @Override
    public Image getAddImage() {
        return getImageByName(ImageLib.ADD_ACTION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#getMatchRuleTable()
     */
    @Override
    public Composite createMatchRuleTable(SashForm parent, List<String> columnNames) {
        MatchRuleDataTable matchRuleDataTable = new MatchRuleDataTable(parent, columnNames.toArray(new String[0]));
        matchRuleDataTable.setUiService(this);
        return matchRuleDataTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#createMatchRuleDataChart(org.eclipse.swt.widgets.Composite,
     * java.util.List, java.util.List)
     */
    @Override
    public Composite createMatchRuleDataChart(Composite parent, List<String[]> viewData, List<String> columnNames) {
        TreeMap<Object, Long> groupSize2groupFrequency = computeGroupSize2FreqMap(viewData, columnNames);
        return new MatchRuleDataChart(parent, groupSize2groupFrequency);
    }

    /**
     * DOC zhao Comment method "computeGroupSize2FreqMap".
     *
     * @param viewData
     * @param columnNames
     * @return
     */
    public TreeMap<Object, Long> computeGroupSize2FreqMap(List<String[]> viewData, List<String> columnNames) {
        List<String> matchRowSchemaList = columnNames;
        int groupSizeColumnIndex = matchRowSchemaList.indexOf(MatchAnalysisConstant.GRP_SIZE);
        int masterColumnIndex = matchRowSchemaList.indexOf(MatchAnalysisConstant.MASTER);
        TreeMap<Object, Long> groupSize2groupFrequency = new TreeMap<Object, Long>();
        if (viewData != null) {

            for (String[] values : viewData) {
                if (Boolean.valueOf(values[masterColumnIndex])) { // Find the master row
                    if (null == groupSize2groupFrequency.get(values[groupSizeColumnIndex])) {
                        groupSize2groupFrequency.put(values[groupSizeColumnIndex], 1l);
                    } else {
                        long freq = groupSize2groupFrequency.get(values[groupSizeColumnIndex]) + 1;
                        groupSize2groupFrequency.put(values[groupSizeColumnIndex], freq);
                    }
                }
            }
        }
        return groupSize2groupFrequency;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#setNeedDisplayCount(int)
     */
    @Override
    public void setNeedDisplayCount(int count, Composite com) {
        if (com instanceof MatchRuleDataChart) {
            ((MatchRuleDataChart) com).setTimes(count);
        } else if (com instanceof MatchRuleDataTable) {
            ((MatchRuleDataTable) com).setDisGroupSize(count);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#refreshMatchRuleChart(java.util.List,
     * org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void refreshMatchRuleChart(List<String[]> viewData, Composite com, List<String> columnNames) {
        TreeMap<Object, Long> groupSize2groupFrequency = computeGroupSize2FreqMap(viewData, columnNames);
        if (com instanceof MatchRuleDataChart) {
            ((MatchRuleDataChart) com).refresh(groupSize2groupFrequency);
        } else if (com instanceof MatchRuleDataTable) {
            ((MatchRuleDataTable) com).refresh(viewData, groupSize2groupFrequency);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#layoutMatchRuleChart()
     */
    @Override
    public void layoutMatchRuleChart(Composite chart) {
        if (chart instanceof MatchRuleDataChart) {
            ((MatchRuleDataChart) chart).layout();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IMatchRuleUIService#disposeMatchRuleChart(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void disposeMatchRuleChart(Composite chart) {
        if (chart instanceof MatchRuleDataChart) {
            ((MatchRuleDataChart) chart).dispose();
        }
    }

}
