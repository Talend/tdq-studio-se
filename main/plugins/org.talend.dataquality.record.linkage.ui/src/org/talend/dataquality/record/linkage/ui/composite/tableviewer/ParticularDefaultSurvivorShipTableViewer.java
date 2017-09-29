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
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorshipUtils;
import org.talend.dataquality.record.linkage.ui.action.MatchRuleActionGroup;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.editingSupport.FunctionEditingSupport;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.ParticularDefaultSurvivorshipLabelProvider;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.dataquality.rules.RulesFactory;

/**
 * Class of Particular DefaultSurvivorShip table viewer
 */
public class ParticularDefaultSurvivorShipTableViewer extends
        AbstractMatchAnalysisTableViewer<ParticularDefaultSurvivorshipDefinitions> {

    /**
     * the weight for eache column in this TableViewer.
     */
    int[] weights = { 12, 10, 30 };

    /**
     * DOC zshen ParticularDefaultSurvivorShipTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public ParticularDefaultSurvivorShipTableViewer(Composite parent, int style) {
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
        return new ParticularDefaultSurvivorshipLabelProvider();
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
    protected AbstractMatchCellModifier<ParticularDefaultSurvivorshipDefinitions> getTableCellModifier() {
        return new ParticularDefaultSurvivorShipCellModifier(this);
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
                    String[] cols = new String[columnList.size()];
                    int idx = 0;
                    for (MetadataColumn metaCol : columnList) {
                        cols[idx++] = metaCol.getName() == null ? StringUtils.EMPTY : metaCol.getName();
                    }
                    editors[i] = new ComboBoxCellEditor(innerTable, cols, SWT.READ_ONLY);
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
    protected ParticularDefaultSurvivorshipDefinitions createNewKeyDefinition(String columnName) {
        ParticularDefaultSurvivorshipDefinitions pskd = RulesFactory.eINSTANCE.createParticularDefaultSurvivorshipDefinitions();
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(SurvivorShipAlgorithmEnum.getTypeByIndex(0).getValue());
        // MOD TDQ-11774 set a default value for parameter
        createAlgorithmDefinition.setAlgorithmParameters(SurvivorshipUtils.DEFAULT_CONCATENATE_PARAMETER);
        pskd.setFunction(createAlgorithmDefinition);
        return pskd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#removeElement(java.lang
     * .Object, java.util.List)
     */
    @Override
    public void removeElement(ParticularDefaultSurvivorshipDefinitions keyPDef,
            List<ParticularDefaultSurvivorshipDefinitions> keyList) {
        if (!keyList.remove(keyPDef)) {
            Iterator<ParticularDefaultSurvivorshipDefinitions> keyIterator = keyList.iterator();
            while (keyIterator.hasNext()) {
                ParticularDefaultSurvivorshipDefinitions tmpKeyDef = keyIterator.next();
                if (StringUtils.equals(keyPDef.getColumn(), tmpKeyDef.getColumn())) {
                    keyList.remove(keyPDef);
                    // Update table view.
                    remove(keyPDef);
                    break;
                }
            }
        } else {
            remove(keyPDef);
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
    public void removeElement(String columnName, List<ParticularDefaultSurvivorshipDefinitions> keyList) {
        Iterator<ParticularDefaultSurvivorshipDefinitions> blockKeyIterator = keyList.iterator();
        while (blockKeyIterator.hasNext()) {
            ParticularDefaultSurvivorshipDefinitions keyDef = blockKeyIterator.next();
            if (StringUtils.equals(keyDef.getColumn(), columnName)) {
                this.removeElement(keyDef, keyList);
                break;
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#addEditingSupportForColumn
     * (org.eclipse.swt.widgets.TableColumn, java.lang.String)
     */
    @Override
    protected void addEditingSupportForColumn(TableViewerColumn tableViewerColumn, String columnTechnologyLabel) {
        if (MatchAnalysisConstant.FUNCTION.equals(columnTechnologyLabel)) {
            EditingSupport support = new FunctionEditingSupport(tableViewerColumn.getViewer());
            tableViewerColumn.setEditingSupport(support);
        }
    }

}
