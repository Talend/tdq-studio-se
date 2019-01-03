// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor.jarFileCellEditor;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAnalysisTableContentProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.MatchAndSurvivorDefinitionCellModifer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.MatchKeyAndSurvivorLabelProvider;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * DOC yyin class global comment. Detailled comment
 * 
 */
public class MatchKeyAndSurvivorshipTableViewer extends AbstractMatchAnalysisTableViewer<MatchKeyAndSurvivorDefinition> {

    protected MatchRule matchRule = null;

    public MatchKeyAndSurvivorshipTableViewer(Composite parent, int style, boolean isAddColumn, MatchRule matchRule) {
        super(parent, style, isAddColumn);
        this.matchRule = matchRule;
    }

    @Override
    protected IContentProvider getTableContentProvider() {
        return new MatchAnalysisTableContentProvider();
    }

    @Override
    protected AbstractMatchCellModifier<MatchKeyAndSurvivorDefinition> getTableCellModifier() {
        return new MatchAndSurvivorDefinitionCellModifer(this);
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
        return false;
        // if (matchRule == null) {
        //            log.error(DefaultMessagesImpl.getString("MatchRuleTableViewer.NULL_MATCH_RULE_INSTANCE") + analysis.getName()); //$NON-NLS-1$
        // return false;
        // }
        // return addElement(columnName, matchRule.getMatchKeys());
    }

    @Override
    public void removeElement(String columnName, List<MatchKeyAndSurvivorDefinition> keyList) {
        Iterator<MatchKeyAndSurvivorDefinition> matchKeyIterator = keyList.iterator();
        while (matchKeyIterator.hasNext()) {
            MatchKeyAndSurvivorDefinition keyDef = matchKeyIterator.next();
            if (StringUtils.equals(keyDef.getColumn(), columnName)) {
                this.removeElement(keyDef, keyList);
                break;
            }
        }
    }

    @Override
    public void removeElement(MatchKeyAndSurvivorDefinition msDedefinition,
            List<MatchKeyAndSurvivorDefinition> matchKeys) {
        Iterator<MatchKeyAndSurvivorDefinition> matchKeyIterator = matchKeys.iterator();
        while (matchKeyIterator.hasNext()) {
            MatchKeyAndSurvivorDefinition keyDef = matchKeyIterator.next();
            if (StringUtils.equals(keyDef.getMatchKey().getName(), msDedefinition.getMatchKey().getName())) {
                super.removeElement(keyDef, matchKeys);
                // link the added MatchKeyAndSurvivorDefinition's match and survivor key with matchRuleDef's matchkey
                // and
                // survivorkey list;
                SurvivorshipKeyDefinition survivorShipKey = msDedefinition.getSurvivorShipKey();
                MatchRuleDefinition matchRuleDef = (MatchRuleDefinition) survivorShipKey.eContainer();
                ((MatchRule) msDedefinition.getMatchKey().eContainer()).getMatchKeys().remove(
                        msDedefinition.getMatchKey());
                matchRuleDef.getSurvivorshipKeys().remove(survivorShipKey);
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

    @Override
    public void addContextMenu() {
        MatchRuleActionGroup<MatchKeyAndSurvivorDefinition> actionGroup =
                new MatchRuleActionGroup<MatchKeyAndSurvivorDefinition>(this);
        actionGroup.fillContextMenu(new MenuManager());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorshipTableViewer#
     * createNewKeyDefinition(java.lang.String)
     */
    @Override
    protected MatchKeyAndSurvivorDefinition createNewKeyDefinition(String columnName) {
        MatchKeyDefinition matchKeyDefinition = MatchRuleAnlaysisUtils.createDefaultMatchRow(columnName);
        SurvivorshipKeyDefinition survivorshipKeyDefinition = createNewSurvivorshipKeyDefinition(columnName);
        MatchKeyAndSurvivorDefinition matchKeySurvDef = new MatchKeyAndSurvivorDefinition();
        matchKeySurvDef.setMatchKey(matchKeyDefinition);
        matchKeySurvDef.setSurvivorShipKey(survivorshipKeyDefinition);

        matchRule.getMatchKeys().add(matchKeySurvDef.getMatchKey());
        ((MatchRuleDefinition) matchRule.eContainer()).getSurvivorshipKeys().add(matchKeySurvDef.getSurvivorShipKey());
        return matchKeySurvDef;
    }

    private SurvivorshipKeyDefinition createNewSurvivorshipKeyDefinition(String columnName) {
        SurvivorshipKeyDefinition skd = RulesFactory.eINSTANCE.createSurvivorshipKeyDefinition();
        skd.setName(columnName);
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(SurvivorShipAlgorithmEnum.getTypeByIndex(3).getComponentValueName());
        skd.setFunction(createAlgorithmDefinition);
        skd.setAllowManualResolution(true);
        return skd;
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
            // used for MDM T-swoosh
            switch (i) {
            case 1:
                editors[i] = new ComboBoxCellEditor(innerTable, AttributeMatcherType.getAllTypes(), SWT.READ_ONLY);
                break;
            case 2:
                editors[i] = new jarFileCellEditor(innerTable, SWT.READ_ONLY);
                break;
            case 3:// MatchAnalysisConstant.TOKENIZATION_TYPE
                editors[i] = new ComboBoxCellEditor(innerTable, TokenizedResolutionMethod.getAllTypes(), SWT.READ_ONLY);
                break;
            case 6:
                editors[i] = new ComboBoxCellEditor(innerTable, HandleNullEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 7:
                editors[i] = new ComboBoxCellEditor(innerTable, SurvivorShipAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;

            default:
                editors[i] = new TextCellEditor(innerTable);
            }

        }
        return editors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#moveUpFromModel
     * (java.lang.Object, java.util.List, int)
     */
    @Override
    protected void moveUpFromModel(MatchKeyAndSurvivorDefinition keyDef, List<MatchKeyAndSurvivorDefinition> keyList,
            int indexForElement) {
        super.moveUpFromModel(keyDef, keyList, indexForElement);
        // Move up Match key
        matchRule.getMatchKeys().remove(keyDef.getMatchKey());
        matchRule.getMatchKeys().add(indexForElement - 2, keyDef.getMatchKey());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer#moveDownFromModel
     * (java.lang.Object, java.util.List, int)
     */
    @Override
    protected void moveDownFromModel(MatchKeyAndSurvivorDefinition keyDef, List<MatchKeyAndSurvivorDefinition> keyList,
            int indexForElement) {
        super.moveDownFromModel(keyDef, keyList, indexForElement);
        // modify model for match key.
        matchRule.getMatchKeys().remove(keyDef.getMatchKey());
        if (indexForElement == keyList.size()) {
            matchRule.getMatchKeys().add(keyDef.getMatchKey());
        } else {
            matchRule.getMatchKeys().add(indexForElement, keyDef.getMatchKey());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getTableLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new MatchKeyAndSurvivorLabelProvider(isAddColumn());
    }

}
