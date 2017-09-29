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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.editor.FormEditor;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableMenuGenerator;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-11 Detailled comment
 * 
 */
public abstract class AbstractAnalysisResultPageWithChart extends AbstractAnalysisResultPage {

    public AbstractAnalysisResultPageWithChart(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    protected void addMenuToChartComp(Object chartComp, DataExplorer dataExplorer, Analysis analysis,
            ChartDataEntity[] chartDataEntities) {
        Map<String, Object> menuMap = createMenuForAllDataEntity(((Composite) chartComp).getShell(), dataExplorer, analysis,
                chartDataEntities);
        // call chart service to create related mouse listener
        TOPChartUtils.getInstance().addMouseListenerForChart(chartComp, menuMap, true);
    }

    protected Map<String, Object> createMenuForAllDataEntity(Shell shell, DataExplorer dataExplorer, Analysis analysis,
            ChartDataEntity[] chartDataEntities) {
        Map<String, Object> menuMap = new HashMap<String, Object>();

        if (!analysis.getParameters().isStoreData()) {
            return menuMap;
        }

        for (ChartDataEntity oneDataEntity : chartDataEntities) {
            Indicator indicator = oneDataEntity.getIndicator();
            Menu menu = createMenu(shell, dataExplorer, analysis, oneDataEntity, getEditorName(indicator));
            ChartTableFactory.addJobGenerationMenu(menu, analysis, indicator);

            menuMap.put(oneDataEntity.getLabel(), menu);
        }

        return menuMap;
    }

    protected String getEditorName(Indicator indicator) {
        return indicator.getName();
    }

    /**
     * DOC yyin Comment method "createMenu".
     * 
     * @param shell
     * @param explorer
     * @param analysis
     * @param currentEngine
     * @param currentDataEntity
     * @param currentIndicator
     * @return
     */
    private Menu createMenu(final Shell shell, final IDataExplorer explorer, final Analysis analysis,
            final ChartDataEntity currentDataEntity, final String editorName1) {
        Menu menu = new Menu(shell, SWT.POP_UP);

        MenuItemEntity[] itemEntities = ChartTableMenuGenerator.generate(explorer, analysis, currentDataEntity);
        for (final MenuItemEntity itemEntity : itemEntities) {
            MenuItem item = new MenuItem(menu, SWT.PUSH);
            item.setText(itemEntity.geti18nLabel());
            item.setImage(itemEntity.getIcon());
            // TDQ-10069 msjian : support column set java engine chart drill down
            item.addSelectionListener(TOPChartUtils.getInstance().createSelectionAdapter(analysis, currentDataEntity,
                    editorName1, itemEntity, false));
            // TDQ-10069~
        }
        return menu;
    }

    protected Image getItemImage(MenuItemEntity menuItem) {
        return menuItem.getIcon();
    }
}
