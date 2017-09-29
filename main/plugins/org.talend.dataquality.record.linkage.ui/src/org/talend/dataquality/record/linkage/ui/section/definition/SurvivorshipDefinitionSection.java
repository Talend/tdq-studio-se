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
package org.talend.dataquality.record.linkage.ui.section.definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataquality.record.linkage.ui.composite.SurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.sorter.KeyDefinitionTableViewerSorter;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by HHB on 2013-8-23 Detailled comment
 * 
 */
public class SurvivorshipDefinitionSection extends AbstractMatchAnaysisTableSection {

    private SurvivorshipTableComposite tableComposite;

    private MatchRuleDefinition matchRuleDef;

    /**
     * DOC HHB SurvivorshipDefinitionTableSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public SurvivorshipDefinitionSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit, null);
        super.setIsNeedSubChart(false);

    }

    @Override
    protected void createSubChart(Composite sectionClient) {
        // do nothing
    }

    @Override
    protected Composite createSubContent(Composite sectionClient) {
        Composite ruleComp = toolkit.createComposite(sectionClient, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        ruleComp.setLayoutData(data);

        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        ruleComp.setLayout(gridLayout);
        tableComposite = new SurvivorshipTableComposite(ruleComp, SWT.NO_FOCUS);
        tableComposite.addPropertyChangeListener(this);
        tableComposite.setLayout(gridLayout);
        tableComposite.setLayoutData(data);
        tableComposite.createContent();
        section.setExpanded(true);
        tableComposite.serViewerSorter(new KeyDefinitionTableViewerSorter<SurvivorshipKeyDefinition>(this.matchRuleDef
                .getSurvivorshipKeys()));
        initTableInput();
        return ruleComp;
    }

    private void initTableInput() {
        tableComposite.setInput(matchRuleDef.getSurvivorshipKeys());
    }

    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.SURVIVIORSHIP_DEFINITION_SECTION_NAME;
    }

    @Override
    public void refreshChart() {
        // do nothing
    }

    @Override
    public Boolean isKeyDefinitionAdded(String columnName) throws Exception {
        return false;
    }

    public void setMatchRuleDef(MatchRuleDefinition matchRuleDef) {
        this.matchRuleDef = matchRuleDef;

    }

    @Override
    public void addTableItem() {
        tableComposite.addKeyDefinition(StringUtils.EMPTY, matchRuleDef.getSurvivorshipKeys());
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
    }

    @Override
    public void removeTableItem() {
        boolean success = false;
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            Iterator<SurvivorshipKeyDefinition> iterator = ((StructuredSelection) selectItems).iterator();
            while (iterator.hasNext()) {
                SurvivorshipKeyDefinition next = iterator.next();
                tableComposite.removeKeyDefinition(next, matchRuleDef.getSurvivorshipKeys());
                success = true;
            }
            if (success) {
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
            }
        }
    }

    public void removeAllSurvivorship() {
        getSurvivorshipKeys().clear();
        redrawnSubTableContent();
    }

    /**
     * Getter for survivorshipKeys.
     * 
     * @return the survivorshipKeys
     */
    public List<SurvivorshipKeyDefinition> getSurvivorshipKeys() {
        return this.matchRuleDef.getSurvivorshipKeys();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#moveDownTableItem()
     */
    @Override
    public void moveUpTableItem() {
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            if (selectItems.isEmpty()) {
                return;
            }
            List<SurvivorshipKeyDefinition> currentElements = this.matchRuleDef.getSurvivorshipKeys();
            List<SurvivorshipKeyDefinition> survivorshipKeyDefinitionlist = ((StructuredSelection) selectItems).toList();
            for (int index = 0; index < survivorshipKeyDefinitionlist.size(); index++) {
                if (!isSameWithCurrentModel(currentElements.get(index), survivorshipKeyDefinitionlist.get(index))) {
                    continue;
                }
                SurvivorshipKeyDefinition next = survivorshipKeyDefinitionlist.get(index);
                tableComposite.moveUpKeyDefinition(next, currentElements);
            }
            tableComposite.selectAllItem(((StructuredSelection) selectItems).toList());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#moveUpTableItem()
     */
    @Override
    public void moveDownTableItem() {
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            if (selectItems.isEmpty()) {
                return;
            }
            List<SurvivorshipKeyDefinition> currentElements = this.matchRuleDef.getSurvivorshipKeys();
            List<SurvivorshipKeyDefinition> survivorshipKeyDefinitionlist = ((StructuredSelection) selectItems).toList();
            for (int index = survivorshipKeyDefinitionlist.size() - 1; 0 <= index; index--) {
                if (!isSameWithCurrentModel(
                        currentElements.get(currentElements.size() - survivorshipKeyDefinitionlist.size() + index),
                        survivorshipKeyDefinitionlist.get(index))) {
                    continue;
                }
                SurvivorshipKeyDefinition next = survivorshipKeyDefinitionlist.get(index);
                tableComposite.moveDownKeyDefinition(next, currentElements);
            }
            tableComposite.selectAllItem(((StructuredSelection) selectItems).toList());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#checkResultStatus()
     */
    @Override
    public ReturnCode checkResultStatus() {
        ReturnCode returnCode = new ReturnCode(false);
        List<String> uniqueNameList = new ArrayList<String>();
        List<String> duplicateNameList = new ArrayList<String>();
        for (SurvivorshipKeyDefinition ssdk : getSurvivorshipKeys()) {
            String currentName = ssdk.getName();
            if (currentName.equals(StringUtils.EMPTY)) {
                returnCode.setMessage(DefaultMessagesImpl.getString("BlockingKeySection.emptyKeys.message", getSectionName())); //$NON-NLS-1$
                return returnCode;
            }
            boolean currentNameIsDuplicate = false;
            for (String uniqueName : uniqueNameList) {
                if (currentName.equals(uniqueName)) {
                    duplicateNameList.add(currentName);
                    currentNameIsDuplicate = true;
                }
            }
            if (!currentNameIsDuplicate) {
                uniqueNameList.add(currentName);
            }
        }
        if (duplicateNameList.size() > 0) {
            returnCode.setMessage(DefaultMessagesImpl.getString("BlockingKeySection.duplicateKeys.message", getSectionName())); //$NON-NLS-1$
            return returnCode;
        } else {
            returnCode.setOk(true);
            return returnCode;
        }
    }

}
