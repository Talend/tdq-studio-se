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

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorshipUtils;
import org.talend.dataquality.record.linkage.ui.action.MatchRuleActionGroup;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAnalysisTableContentProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.SurvivorshipLabelProvider;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class SurvivorShipTableViewer extends AbstractMatchAnalysisTableViewer<SurvivorshipKeyDefinition> {

    /**
     * DOC HHB SurvivorShipTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public SurvivorShipTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style, isAddColumn);
    }

    @Override
    protected int getHeaderDisplayWeight() {
        return 10;
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
        return new SurvivorshipLabelProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * getTableContentProvider()
     */
    @Override
    protected IContentProvider getTableContentProvider() {
        return new MatchAnalysisTableContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#getTableCellModifier
     * ()
     */
    @Override
    protected AbstractMatchCellModifier<SurvivorshipKeyDefinition> getTableCellModifier() {
        return new SurvivorShipCellModifier(this);
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
            if (isAddColumn() && i == 1) {
                editors[i] = new TextCellEditor(innerTable);

            } else {
                switch (i) {

                case 0:
                    editors[i] = new TextCellEditor(innerTable);
                    break;
                case 2:
                    editors[i] = new ComboBoxCellEditor(innerTable, SurvivorShipAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 3:
                    editors[i] = new CheckboxCellEditor(innerTable);
                    break;
                default:

                }
            }
        }
        return editors;
    }

    @Override
    public boolean addElement(String columnName, Analysis analysis) {
        return false;
    }

    /*
     * (non-Javadoc) columnList.toArray(new String[0])
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * createNewKeyDefinition(java.lang.String)
     */
    @Override
    protected SurvivorshipKeyDefinition createNewKeyDefinition(String columnName) {
        SurvivorshipKeyDefinition skd = RulesFactory.eINSTANCE.createSurvivorshipKeyDefinition();
        skd.setName(MatchAnalysisConstant.SURVIVORSHIP_KEY_DEFAULT_VALUE);
        skd.setColumn(columnName);
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(SurvivorShipAlgorithmEnum.getTypeByIndex(0).getValue());
        // MOD TDQ-11774 set a default value for parameter
        createAlgorithmDefinition.setAlgorithmParameters(SurvivorshipUtils.DEFAULT_CONCATENATE_PARAMETER);
        skd.setFunction(createAlgorithmDefinition);
        skd.setAllowManualResolution(true);
        return skd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#removeElement
     * (java.lang.String, java.util.List)
     */
    @Override
    public void removeElement(String columnName, List<SurvivorshipKeyDefinition> keyList) {
        // don't need to do anything

    }

}
