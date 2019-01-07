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
package org.talend.dataquality.record.linkage.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AnaMatchKeyAndSurvTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorshipTableViewer;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchRule;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchKeyAndSurvivorTableComposite extends AbsMatchAnalysisTableComposite<MatchKeyAndSurvivorDefinition> {

    private Text matchIntervalText;

    private MatchRule matchRule;

    private boolean isShowInputColumn = Boolean.FALSE;

    /**
     * DOC yyin MatchKeyAndSurvivorTableComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MatchKeyAndSurvivorTableComposite(Composite parent, int style, MatchRule matchRule) {
        super(parent, style);
        this.matchRule = matchRule;
    }

    @Override
    protected void initHeaders() {
        headers.add(MatchAnalysisConstant.MATCH_KEY_NAME); // 14
        if (isShowInputColumn) {
            headers.add(MatchAnalysisConstant.INPUT_COLUMN);
        }
        headers.add(MatchAnalysisConstant.MATCHING_TYPE); // 12
        headers.add(MatchAnalysisConstant.CUSTOM_MATCHER); // 20
        headers.add(MatchAnalysisConstant.TOKENIZATION_TYPE); // 20
        headers.add(MatchAnalysisConstant.THRESHOLD); // 14
        headers.add(MatchAnalysisConstant.CONFIDENCE_WEIGHT); // 17
        headers.add(MatchAnalysisConstant.HANDLE_NULL); // 11
        headers.add(MatchAnalysisConstant.FUNCTION);
        headers.add(MatchAnalysisConstant.REFERENCE_COLUMN);
        headers.add(MatchAnalysisConstant.PARAMETER);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createTable() {
        tableViewer = createTableViewer();
        tableViewer.addPropertyChangeListener(this);
        tableViewer.initTable(headers, columnList, false);
        // Create match interval
        createMatchIntervalComposite();
        // setInput(matchRule);
    }

    /**
     * DOC zhao Comment method "createMatchIntervalComposite".
     * 
     */
    protected void createMatchIntervalComposite() {
        Composite matchIntervalComposite = new Composite(this, SWT.NONE);
        matchIntervalComposite.setLayout(new GridLayout(2, Boolean.TRUE));
        Label matchIntervalLabel = new Label(matchIntervalComposite, SWT.NONE);
        matchIntervalLabel.setText(DefaultMessagesImpl.getString("MatchRuleTableComposite.MATCH_INTERVAL")); //$NON-NLS-1$
        matchIntervalText = new Text(matchIntervalComposite, SWT.BORDER);
        matchIntervalText.setText(String.valueOf(matchRule.getMatchInterval()));
        GridData layoutData = new GridData();
        layoutData.widthHint = 80;
        matchIntervalText.setLayoutData(layoutData);
        matchIntervalText.addModifyListener(new ModifyListener() {

            Double oldValue = matchRule.getMatchInterval();

            @Override
            public void modifyText(ModifyEvent e) {
                try {
                    String newValue = matchIntervalText.getText();
                    Double value = Double.valueOf(newValue);
                    if (value != oldValue) {
                        matchRule.setMatchInterval(value);
                        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, oldValue, value);
                        oldValue = value;
                    }
                } catch (Exception exc) {
                    // Invalid input
                }
            }
        });

    }

    public void setMatchIntervalText(String newText) {
        matchIntervalText.setText(newText);
    }

    @Override
    protected int getTableStyle() {
        int style = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        return style;
    }

    /**
     * Getter for matchRule.
     * 
     * @return the matchRule
     */
    public MatchRule getMatchRule() {
        return this.matchRule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite#createTableViewer()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AbstractMatchAnalysisTableViewer createTableViewer() {
        if (isShowInputColumn) {
            // Analysis editor
            return new AnaMatchKeyAndSurvTableViewer(this, getTableStyle(), isAddColumn(), matchRule);
        } else {
            return new MatchKeyAndSurvivorshipTableViewer(this, getTableStyle(), isAddColumn(), matchRule);
        }
    }

    /**
     * Sets the isShowInputColumn.
     * 
     * @param isShowInputColumn the isShowInputColumn to set
     */
    public void setShowInputColumn(boolean isShowInputColumn) {
        this.isShowInputColumn = isShowInputColumn;
    }

}
