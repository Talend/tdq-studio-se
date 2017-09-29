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
package org.talend.dataprofiler.core.ui.wizard.patterns;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.columnset.impl.AllMatchIndicatorImpl;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class SelectPatternsWizard extends Wizard {

    private AllMatchIndicatorImpl allMatchIndicator;

    private SimpleStatIndicator ssIndicator;

    private PatternsSelectPage patternSelectPage;

    private List<Map<Integer, RegexpMatchingIndicator>> oldTableInputList;

    private DataFilterType filterType = null;

    public SelectPatternsWizard(List<Indicator> indicatorsList) {
        for (Indicator theIndicator : indicatorsList) {
            if (theIndicator instanceof AllMatchIndicatorImpl) {
                allMatchIndicator = (AllMatchIndicatorImpl) theIndicator;
            } else if (theIndicator instanceof SimpleStatIndicator) {
                ssIndicator = (SimpleStatIndicator) theIndicator;
            } else {
                continue;
            }
        }
    }

    @Override
    public void addPages() {
        patternSelectPage = new PatternsSelectPage(this);
        patternSelectPage.setFilterType(filterType);
        patternSelectPage.setOldTableInputList(oldTableInputList);
        this.addPage(patternSelectPage);

    }

    @Override
    public boolean canFinish() {
        return super.canFinish();
    }

    @Override
    public void createPageControls(Composite pageContainer) {
        super.createPageControls(pageContainer);
    }

    @Override
    public String getWindowTitle() {
        return super.getWindowTitle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        Grid table = patternSelectPage.getTable();
        GridColumn[] gridColumns = table.getColumns();
        GridItem[] gridItems = table.getItems();
        for (GridItem item : gridItems) {
            for (GridColumn column : gridColumns) {
                int columnIndex = table.indexOf(column);
                if (!item.getChecked(columnIndex)) {
                    // allMatchIndicator.getAllChildIndicators().remove(((Map<?, ?>) item.getData()).get(columnIndex));
                    patternSelectPage.getTableInputList().get(table.indexOf(item)).put(columnIndex, null);
                }
            }
        }
        return true;
    }

    public PatternsSelectPage getPatternSelectPage() {
        return patternSelectPage;
    }

    public List<Map<Integer, RegexpMatchingIndicator>> getOldTableInputList() {
        return oldTableInputList;
    }

    public void setOldTableInputList(List<Map<Integer, RegexpMatchingIndicator>> oldTableInputList) {
        this.oldTableInputList = oldTableInputList;
    }

    public AllMatchIndicatorImpl getAllMatchIndicator() {
        return allMatchIndicator;
    }

    public SimpleStatIndicator getSsIndicator() {
        return ssIndicator;
    }

    public void setFilterType(DataFilterType filterType) {
        this.filterType = filterType;
    }
}
