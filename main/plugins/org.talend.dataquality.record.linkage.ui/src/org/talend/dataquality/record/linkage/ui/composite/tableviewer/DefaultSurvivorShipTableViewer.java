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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.action.MatchRuleActionGroup;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.DefaultSurvivorshipLabelProvider;
import org.talend.dataquality.record.linkage.utils.DefaultSurvivorShipDataTypeEnum;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class DefaultSurvivorShipTableViewer extends AbstractMatchAnalysisTableViewer<DefaultSurvivorshipDefinition> {

    /**
     * the weight for eache column in this TableViewer.
     */
    int[] weights = { 12, 10, 30 };

    /**
     * DOC HHB SurvivorShipTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public DefaultSurvivorShipTableViewer(Composite parent, int style) {
        super(parent, style, false);
    }

    @Override
    public void addContextMenu() {
        MatchRuleActionGroup actionGroup = new MatchRuleActionGroup(this);
        actionGroup.fillContextMenu(new MenuManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableLabelProvider
     * ()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new DefaultSurvivorshipLabelProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * getTableContentProvider()
     */
    @Override
    protected IContentProvider getTableContentProvider() {
        return new ArrayContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableCellModifier
     * ()
     */
    @Override
    protected AbstractMatchCellModifier<DefaultSurvivorshipDefinition> getTableCellModifier() {
        return new DefaultSurvivorShipCellModifier(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getCellEditor
     * (java.util.List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            {
                switch (i) {

                case 0:
                    editors[i] = new ComboBoxCellEditor(innerTable, DefaultSurvivorShipDataTypeEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 1:
                    editors[i] = new ComboBoxCellEditor(innerTable, SurvivorShipAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 2:
                    editors[i] = new TextCellEditor(innerTable);

                }
            }
        }
        return editors;
    }

    @Override
    public boolean addElement(String columnName, Analysis analysis) {
        return false;
    }

    @Override
    protected DefaultSurvivorshipDefinition createNewKeyDefinition(String columnName) {
        DefaultSurvivorshipDefinition skd = RulesFactory.eINSTANCE.createDefaultSurvivorshipDefinition();
        skd.setDataType(DefaultSurvivorShipDataTypeEnum.getTypeByIndex(0).getValue());
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(SurvivorShipAlgorithmEnum.getTypeByIndex(3).getComponentValueName());
        skd.setFunction(createAlgorithmDefinition);
        return skd;
    }

    public void removeElement(DefaultSurvivorshipDefinition keyDef, MatchRuleDefinition matchRuleDef) {
        List<DefaultSurvivorshipDefinition> skdList = matchRuleDef.getDefaultSurvivorshipDefinitions();
        Iterator<DefaultSurvivorshipDefinition> keyIterator = skdList.iterator();
        while (keyIterator.hasNext()) {
            DefaultSurvivorshipDefinition tmpKeyDef = keyIterator.next();
            if (StringUtils.equals(keyDef.getDataType(), tmpKeyDef.getDataType())) {
                skdList.remove(keyDef);
                // Update table view.
                remove(keyDef);
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * getHeaderDisplayWeight()
     */
    @Override
    protected int getHeaderDisplayWeight() {
        return 12;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * getHeaderDisplayWeight(int)
     */
    @Override
    protected int getHeaderDisplayWeight(int index) {
        if (index < weights.length) {
            return weights[index];
        }
        return super.getHeaderDisplayWeight(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#removeElement
     * (java.lang.String, java.util.List)
     */
    @Override
    public void removeElement(String columnName, List<DefaultSurvivorshipDefinition> keyList) {
        // don't need do anything

    }

}
