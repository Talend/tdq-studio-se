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
import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.ui.action.MatchRuleActionGroup;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.BlockingKeyTableLabelProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAnalysisTableContentProvider;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.RulesFactory;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public class BlockingKeyTableViewer extends AbstractMatchAnalysisTableViewer<BlockKeyDefinition> {

    private static Logger log = Logger.getLogger(BlockingKeyTableViewer.class);

    /**
     * DOC zshen BlockingKeyTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     */
    public BlockingKeyTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style, isAddColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getTableLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new BlockingKeyTableLabelProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getTableContentProvider()
     */
    @Override
    protected IContentProvider getTableContentProvider() {
        return new MatchAnalysisTableContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getTableCellModifier()
     */
    @Override
    protected AbstractMatchCellModifier<BlockKeyDefinition> getTableCellModifier() {
        return new BlockingKeyCellModeifier(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getDisplayWeight()
     */
    @Override
    protected int getHeaderDisplayWeight() {
        return 12;
    }

    /**
     * 
     * add new Element
     * 
     * @param columnName the name of column
     * @param analysis the context of this add operation perform on.
     */
    @Override
    public boolean addElement(String columnName, Analysis analysis) {
        RecordMatchingIndicator recordMatchingIndiator = MatchRuleAnlaysisUtils.getRecordMatchIndicatorFromAna(analysis);
        if (recordMatchingIndiator == null) {
            log.error(DefaultMessagesImpl.getString("BlockingKeyTableViewer.NULL_RECORD_MATCHING", analysis.getName())); //$NON-NLS-1$
            return Boolean.FALSE;
        }
        return addElement(columnName, recordMatchingIndiator.getBuiltInMatchRuleDefinition().getBlockKeys());

    }

    /**
     * DOC zshen Comment method "createDefaultRow".
     * 
     * @param columnName
     * @return
     */
    @Override
    protected BlockKeyDefinition createNewKeyDefinition(String columnName) {
        BlockKeyDefinition createBlockKeyDefinition = RulesFactory.eINSTANCE.createBlockKeyDefinition();
        createBlockKeyDefinition.setName(columnName);
        createBlockKeyDefinition.setColumn(columnName);
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(BlockingKeyPreAlgorithmEnum.getTypeByIndex(0).getComponentValueName());
        createAlgorithmDefinition.setAlgorithmParameters(StringUtils.EMPTY);
        createBlockKeyDefinition.setPreAlgorithm(createAlgorithmDefinition);

        createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(BlockingKeyAlgorithmEnum.getTypeByIndex(0).getComponentValueName());
        createAlgorithmDefinition.setAlgorithmParameters(StringUtils.EMPTY);
        createBlockKeyDefinition.setAlgorithm(createAlgorithmDefinition);

        createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(BlockingKeyPostAlgorithmEnum.getTypeByIndex(0).getComponentValueName());
        createAlgorithmDefinition.setAlgorithmParameters(StringUtils.EMPTY);
        createBlockKeyDefinition.setPostAlgorithm(createAlgorithmDefinition);
        return createBlockKeyDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTabveViewer#removeElement
     * (java.lang.String)
     */
    @Override
    public void removeElement(String columnName, List<BlockKeyDefinition> bkdList) {
        Iterator<BlockKeyDefinition> blockKeyIterator = bkdList.iterator();
        while (blockKeyIterator.hasNext()) {
            BlockKeyDefinition keyDef = blockKeyIterator.next();
            if (StringUtils.equals(keyDef.getColumn(), columnName)) {
                this.removeElement(keyDef, bkdList);
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#addContextMenu()
     */
    @Override
    public void addContextMenu() {
        MatchRuleActionGroup actionGroup = new MatchRuleActionGroup(this);
        actionGroup.fillContextMenu(new MenuManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getCellEditor(java.util.List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            switch (i) {

            case 1:
                String[] cols = new String[columnList.size()];
                int idx = 0;
                for (MetadataColumn metaCol : columnList) {
                    cols[idx++] = metaCol.getName() == null ? "" : metaCol.getName();
                }
                editors[i] = new ComboBoxCellEditor(innerTable, cols, SWT.READ_ONLY);
                break;
            case 2:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyPreAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 4:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 6:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyPostAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            default:
                editors[i] = new TextCellEditor(innerTable);
            }
        }
        return editors;
    }

}
