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
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.constant.TokenizedResolutionMethod;
import org.talend.dataquality.record.linkage.ui.action.MatchRuleActionGroup;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor.jarFileCellEditor;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAnalysisTableContentProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchRuleLabelProvider;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;

/**
 * created by zshen on Jul 31, 2013 Detailled comment
 * 
 */
public class MatchRuleTableViewer extends AbstractMatchAnalysisTableViewer<MatchKeyDefinition> {

    private static Logger log = Logger.getLogger(MatchRuleTableViewer.class);

    private MatchRule matchRule = null;

    /**
     * DOC zshen MatchRuleTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MatchRuleTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style, isAddColumn);

    }

    /**
     * DOC zshen Comment method "getCellEditor".
     * 
     * @param headers
     * @return
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            switch (i) {
            case 1:// MatchAnalysisConstant.INPUT_COLUMN
                String[] cols = new String[columnList.size()];
                int idx = 0;
                for (MetadataColumn metaCol : columnList) {
                    cols[idx++] = metaCol.getName() == null ? "" : metaCol.getName();
                }
                editors[i] = new ComboBoxCellEditor(innerTable, cols, SWT.READ_ONLY);
                break;
            case 2:// MatchAnalysisConstant.MATCHING_TYPE
                editors[i] = new ComboBoxCellEditor(innerTable, AttributeMatcherType.getAllTypes(), SWT.READ_ONLY);
                break;
            case 3:// MatchAnalysisConstant.CUSTOM_MATCHER
                editors[i] = new jarFileCellEditor(innerTable, SWT.READ_ONLY);
                break;
            case 4:// MatchAnalysisConstant.TOKENIZATION_TYPE
                editors[i] = new ComboBoxCellEditor(innerTable, TokenizedResolutionMethod.getAllTypes(), SWT.READ_ONLY);
                break;
            case 6:// MatchAnalysisConstant.HANDLE_NULL
                editors[i] = new ComboBoxCellEditor(innerTable, HandleNullEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            default:// MatchAnalysisConstant.MATCH_KEY_NAME and MatchAnalysisConstant.CONFIDENCE_WEIGHT
                editors[i] = new TextCellEditor(innerTable);
            }
        }
        return editors;
    }

    /**
     * DOC zshen Comment method "getTableLabelProvider".
     * 
     * @return
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new MatchRuleLabelProvider();
    }

    /**
     * DOC zshen Comment method "getTableContentProvider".
     * 
     * @return
     */
    @Override
    protected IContentProvider getTableContentProvider() {
        return new MatchAnalysisTableContentProvider();
    }

    /**
     * DOC zshen Comment method "getCellModifier".
     * 
     * @return
     */
    @Override
    protected AbstractMatchCellModifier<MatchKeyDefinition> getTableCellModifier() {
        return new MatchRuleCellModifier(this);
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
        if (matchRule == null) {
            log.error(DefaultMessagesImpl.getString("MatchRuleTableViewer.NULL_MATCH_RULE_INSTANCE") + analysis.getName()); //$NON-NLS-1$
            return false;
        }
        return addElement(columnName, matchRule.getMatchKeys());
    }

    @Override
    public void removeElement(String columnName, List<MatchKeyDefinition> matchKeys) {
        Iterator<MatchKeyDefinition> matchKeyIterator = matchKeys.iterator();
        while (matchKeyIterator.hasNext()) {
            MatchKeyDefinition keyDef = matchKeyIterator.next();
            if (StringUtils.equals(keyDef.getColumn(), columnName)) {
                this.removeElement(keyDef, matchKeys);
                break;
            }
        }
    }

    /**
     * use this value to compute the vaule of column width
     * 
     * @return
     */
    @Override
    protected int getHeaderDisplayWeight() {
        return 10;
    }

    /**
     * DOC zhao Comment method "setMatchRule".
     * 
     * @param matchRule2
     */
    public void setMatchRule(MatchRule matchRule) {
        this.matchRule = matchRule;
    }

    @Override
    public void addContextMenu() {
        MatchRuleActionGroup actionGroup = new MatchRuleActionGroup(this);
        actionGroup.fillContextMenu(new MenuManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#
     * createNewKeyDefinition(java.lang.String)
     */
    @Override
    protected MatchKeyDefinition createNewKeyDefinition(String columnName) {
        return MatchRuleAnlaysisUtils.createDefaultMatchRow(columnName);
    }

}
