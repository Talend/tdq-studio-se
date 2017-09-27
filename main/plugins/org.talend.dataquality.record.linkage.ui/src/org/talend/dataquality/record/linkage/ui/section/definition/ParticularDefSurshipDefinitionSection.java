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
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.ParticularDefaultSurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.sorter.KeyDefinitionTableViewerSorter;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.utils.sugars.ReturnCode;

/**
 * Section of ParticularDefaultSurvivorship table
 */
public class ParticularDefSurshipDefinitionSection extends AbstractMatchAnaysisTableSection {

    /**
     * ParticularDefaultSurvivorshipSection constructor method.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param newAnalysis
     */
    public ParticularDefSurshipDefinitionSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit, null);
        super.setIsNeedSubChart(false);
    }

    private ParticularDefaultSurvivorshipTableComposite tableComposite;

    private MatchRuleDefinition matchRuleDef;

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
        tableComposite = getTableComposite(ruleComp);
        tableComposite.addPropertyChangeListener(this);
        tableComposite.setLayout(gridLayout);
        tableComposite.setLayoutData(data);
        if (columnMap != null) {
            ArrayList<MetadataColumn> columnList = new ArrayList<MetadataColumn>();
            columnList.addAll(columnMap.keySet());
            tableComposite.setColumnList(columnList);
        }
        tableComposite.createContent();
        section.setExpanded(true);
        tableComposite.serViewerSorter(new KeyDefinitionTableViewerSorter<ParticularDefaultSurvivorshipDefinitions>(
                this.matchRuleDef.getParticularDefaultSurvivorshipDefinitions()));
        initTableInput();

        return ruleComp;
    }

    /**
     * Get table composite which contain table viewer
     * 
     * @param ruleComp
     * @return
     */
    protected ParticularDefaultSurvivorshipTableComposite getTableComposite(Composite ruleComp) {
        return new ParticularDefaultSurvivorshipTableComposite(ruleComp, SWT.NO_FOCUS);
    }

    protected void initTableInput() {
        tableComposite.setTableViewerInput(matchRuleDef.getParticularDefaultSurvivorshipDefinitions());
    }

    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.SURVIVIORSHIP_PARTICULAR_DEFAULT_DEFINITION_SECTION_NAME;
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
        tableComposite.addKeyDefinition(StringUtils.EMPTY, matchRuleDef.getParticularDefaultSurvivorshipDefinitions());
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
    }

    @Override
    public void removeTableItem() {
        boolean success = false;
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            Iterator<ParticularDefaultSurvivorshipDefinitions> iterator = ((StructuredSelection) selectItems).iterator();
            while (iterator.hasNext()) {
                ParticularDefaultSurvivorshipDefinitions next = iterator.next();
                tableComposite.removeKeyDefinition(next, matchRuleDef.getParticularDefaultSurvivorshipDefinitions());
                success = true;
            }
            if (success) {
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
            }
        }
    }

    public void removeParticularKey(String columnName) {
        tableComposite.removeKeyDefinition(columnName, matchRuleDef.getParticularDefaultSurvivorshipDefinitions());
    }

    /**
     * Getter for particular defaultSurvivorshipKeys.
     * 
     * @return the particular defaultSurvivorshipKeys
     */
    public List<ParticularDefaultSurvivorshipDefinitions> getParticularDefaultSurvivorshipKeys() {
        List<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipDefKeys = new ArrayList<ParticularDefaultSurvivorshipDefinitions>();
        particularDefaultSurvivorshipDefKeys.addAll(EcoreUtil.copyAll(this.matchRuleDef
                .getParticularDefaultSurvivorshipDefinitions()));
        return particularDefaultSurvivorshipDefKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#moveDownTableItem()
     */
    @Override
    public void moveDownTableItem() {
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            if (selectItems.isEmpty()) {
                return;
            }
            List<ParticularDefaultSurvivorshipDefinitions> currentElements = this.matchRuleDef
                    .getParticularDefaultSurvivorshipDefinitions();
            List<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipKeyDefinitionlist = ((StructuredSelection) selectItems)
                    .toList();
            for (int index = particularDefaultSurvivorshipKeyDefinitionlist.size() - 1; 0 <= index; index--) {
                if (!isSameWithCurrentModel(
                        currentElements.get(currentElements.size() - particularDefaultSurvivorshipKeyDefinitionlist.size()
                                + index), particularDefaultSurvivorshipKeyDefinitionlist.get(index))) {
                    continue;
                }
                ParticularDefaultSurvivorshipDefinitions next = particularDefaultSurvivorshipKeyDefinitionlist.get(index);
                tableComposite.moveDownKeyDefinition(next, currentElements);
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
    public void moveUpTableItem() {
        ISelection selectItems = tableComposite.getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            if (selectItems.isEmpty()) {
                return;
            }
            List<ParticularDefaultSurvivorshipDefinitions> currentElements = this.matchRuleDef
                    .getParticularDefaultSurvivorshipDefinitions();
            List<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipKeyDefinitionlist = ((StructuredSelection) selectItems)
                    .toList();
            for (int index = 0; index < particularDefaultSurvivorshipKeyDefinitionlist.size(); index++) {
                if (!isSameWithCurrentModel(currentElements.get(index), particularDefaultSurvivorshipKeyDefinitionlist.get(index))) {
                    continue;
                }
                ParticularDefaultSurvivorshipDefinitions next = particularDefaultSurvivorshipKeyDefinitionlist.get(index);
                tableComposite.moveUpKeyDefinition(next, currentElements);
            }
            tableComposite.selectAllItem(((StructuredSelection) selectItems).toList());
        }
    }

    /**
     * import the DefaultSurvivorshipFunctions, if overwrite, clear the DefaultSurvivorshipFunctions before import.
     * 
     * @param matchRuleDef
     * @param overwrite
     */
    @SuppressWarnings("unchecked")
    public void importParticularSurvivorshipFunctions(MatchRuleDefinition matchRuleDef, boolean overwrite) {
        EList<ParticularDefaultSurvivorshipDefinitions> functions = null;
        Object input = tableComposite.getInput();
        if (input != null) {
            functions = (EList<ParticularDefaultSurvivorshipDefinitions>) input;
        }
        if (functions == null) {
            functions = new BasicEList<ParticularDefaultSurvivorshipDefinitions>();
        }
        if (overwrite) {
            functions.clear();
        }
        for (ParticularDefaultSurvivorshipDefinitions def : matchRuleDef.getParticularDefaultSurvivorshipDefinitions()) {
            setColumnValueIfMatch(def);
            functions.add(EcoreUtil.copy(def));
        }
        tableComposite.setInput(functions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#getMappingName(org.talend.dataquality
     * .rules.KeyDefinition)
     */
    @Override
    protected String getMappingName(KeyDefinition keyDefinition) {
        return keyDefinition.getColumn();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#checkResultStatus()
     */
    @Override
    public ReturnCode checkResultStatus() {
        ReturnCode returnCode = new ReturnCode(true);
        List<String> uniqueNameList = new ArrayList<String>();
        List<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipKeys = this
                .getParticularDefaultSurvivorshipKeys();
        for (ParticularDefaultSurvivorshipDefinitions pdsd : particularDefaultSurvivorshipKeys) {
            String currentName = pdsd.getColumn();

            if (uniqueNameList.contains(currentName)) {
                returnCode.setOk(false);
                returnCode.setMessage(DefaultMessagesImpl.getString(
                        "BlockingKeySection.duplicateKeys.message", getSectionName() + "--" + currentName)); //$NON-NLS-1$ //$NON-NLS-2$
                return returnCode;
            }
            uniqueNameList.add(currentName);

            if (checkColumnNameIsEmpty(pdsd)) {
                returnCode.setOk(false);
                returnCode.setMessage(DefaultMessagesImpl.getString("BlockingKeySection.emptyColumn.message", getSectionName())); //$NON-NLS-1$ 
                return returnCode;
            }
        }
        return returnCode;
    }

    /**
     * DOC zshen Comment method "checkColumnName".
     * 
     * @param bdk
     */
    protected boolean checkColumnNameIsEmpty(BlockKeyDefinition bdk) {
        String columnName = bdk.getColumn();
        if (columnName == null || columnName.equals(StringUtils.EMPTY)) {
            return true;
        }
        return false;
    }
}
