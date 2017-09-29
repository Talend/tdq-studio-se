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
package org.talend.dataprofiler.core.ui.editor.indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataprofiler.core.ui.dialog.ExpressionEditDialog;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorDefinitionMaterPage extends AbstractMetadataFormPage {

    private static Logger log = Logger.getLogger(IndicatorDefinitionMaterPage.class);

    private static final String ADDITIONAL_FUNCTIONS_SPLIT = PluginConstant.SEMICOLON_STRING;

    protected Section definitionSection;

    protected Composite definitionComp;

    protected List<String> allDBTypeList;

    protected Map<CCombo, TdExpression> tempExpressionMap;

    protected Map<CCombo, TdExpression> tempViewRowsExpressionMap;

    protected Map<CCombo, TdExpression> tempViewValidRowsExpressionMap;

    protected Map<CCombo, TdExpression> tempViewInvalidRowsExpressionMap;

    protected Map<CCombo, TdExpression> tempViewValidValuesExpressionMap;

    protected Map<CCombo, TdExpression> tempViewInvalidValuesExpressionMap;

    protected Map<CCombo, Composite> widgetMap;

    protected Composite expressionComp;

    protected IndicatorCategory category;

    private Section additionalFunctionsSection;

    private Composite additionalFunctionsComp;

    private Composite afExpressionComp;

    private Map<String, AggregateDateExpression> afExpressionMap, afExpressionMapTemp;

    private List<String> remainDBTypeListAF;

    private boolean hasAggregateExpression, hasDateExpression, hasCharactersMapping;

    // ADD xqliu 2010-03-23 feature 11201
    private List<TdExpression> tempExpressionList;

    // ADD klliu 2011-08-08 bug 0022994
    protected Composite dataBaseComp;

    private Composite dataBaseTitleComp;

    private static final String BODY_AGGREGATE = "AVG({0});COUNT({0});SUM(CASE WHEN {0} IS NULL THEN 1 ELSE 0 END)"; //$NON-NLS-1$

    private static final String BODY_DATE = "MIN({0});MAX({0});COUNT({0});SUM(CASE WHEN {0} IS NULL THEN 1 ELSE 0 END)"; //$NON-NLS-1$

    private Section charactersMappingSection;

    private Composite charactersMappingComp;

    private Composite charactersMappingLineComp;

    private Map<String, CharactersMapping> charactersMappingMap, charactersMappingMapTemp;

    private List<String> remainDBTypeListCM;

    private static final String BODY_CHARACTERS_TO_REPLACE = "abcdefghijklmnopqrstuvwxyzçâêîôûéèùïöüABCDE"//$NON-NLS-1$
            + "FGHIJKLMNOPQRSTUVWXYZÇÂÊÎÔÛÉÈÙÏÖÜ0123456789"; //$NON-NLS-1$

    private static final String BODY_REPLACEMENT_CHARACTERS = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAA"//$NON-NLS-1$
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999"; //$NON-NLS-1$

    private SysIndicatorDefinitionRepNode indicatorRepNode;

    /**
     * DOC bZhou IndicatorDefinitionMaterPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public IndicatorDefinitionMaterPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getIntactElemenetName()
     */
    @Override
    public String getIntactElemenetName() {
        // The title of indicatorDefinition should display an internationalzation String
        String intactElemenetName = super.getIntactElemenetName();
        String internationalizationLabel = StringUtils.EMPTY;
        Property property = getProperty();
        if (property != null && getCurrentModelElement() != null
                && DefinitionPackage.eINSTANCE.getIndicatorDefinition().equals(getCurrentModelElement().eClass())) {
            // system indicatorDefinition need to be internationalization
            internationalizationLabel = InternationalizationUtil.getDefinitionInternationalizationLabel(property.getLabel());
            if (StringUtils.EMPTY.equals(internationalizationLabel)) {
                return intactElemenetName;
            }
        } else {
            return intactElemenetName;
        }
        return internationalizationLabel;
    }

    /**
     * DOC talend Comment method "isEastAsiaPatternFequencyStatics".
     * 
     * @param indicatorDefinition
     * @return
     */
    private boolean isEastAsiaPatternFequencyStatics(IndicatorDefinition indicatorDefinition) {
        String uuid = ResourceHelper.getUUID(indicatorDefinition);
        // EastAsiaPatternFrequency and EastAsiaPatternLowFrequency
        if ("_JMeW0F0TEeWGcZIHtEALuw".equals(uuid) || "_GIAgQF0TEeWGcZIHtEALuw".equals(uuid)) { //$NON-NLS-1$//$NON-NLS-2$
            return true;
        }
        return false;
    }

    public SysIndicatorDefinitionRepNode getIndicatorDefinitionRepNode() {
        return this.indicatorRepNode;
    }

    protected void removeJavaType() {
        allDBTypeList.remove(PatternLanguageType.JAVA.getLiteral());
    }

    /**
     * init Temp Maps.
     */
    public void initTempMaps() {
        tempExpressionMap = initTempMap(tempExpressionMap);
        tempViewValidRowsExpressionMap = initTempMap(tempViewValidRowsExpressionMap);
        tempViewInvalidRowsExpressionMap = initTempMap(tempViewInvalidRowsExpressionMap);
        tempViewValidValuesExpressionMap = initTempMap(tempViewValidValuesExpressionMap);
        tempViewInvalidValuesExpressionMap = initTempMap(tempViewInvalidValuesExpressionMap);
        tempViewRowsExpressionMap = initTempMap(tempViewRowsExpressionMap);
    }

    /**
     * when change the indicator catogroy, remove all except the old java type.
     */
    public void removeFromTempMapsExceptJava() {
        Iterator<CCombo> it = tempExpressionMap.keySet().iterator();
        while (it.hasNext()) {
            CCombo cb = it.next();
            if (!cb.getText().equals(PatternLanguageType.JAVA.getName())) {
                it.remove();
            }
        }

        tempViewValidRowsExpressionMap = initTempMap(tempViewValidRowsExpressionMap);
        tempViewInvalidRowsExpressionMap = initTempMap(tempViewInvalidRowsExpressionMap);
        tempViewValidValuesExpressionMap = initTempMap(tempViewValidValuesExpressionMap);
        tempViewInvalidValuesExpressionMap = initTempMap(tempViewInvalidValuesExpressionMap);
        tempViewRowsExpressionMap = initTempMap(tempViewRowsExpressionMap);
    }

    /**
     * init Temp Map.
     */
    private Map<CCombo, TdExpression> initTempMap(Map<CCombo, TdExpression> tmp) {
        Map<CCombo, TdExpression> temp = tmp;
        if (temp == null) {
            temp = new HashMap<CCombo, TdExpression>();
        } else {
            temp.clear();
        }
        return temp;
    }

    /**
     * init TempExpressionList.
     * 
     * @param def
     */
    private void initTempExpressionList(IndicatorDefinition def) {
        if (tempExpressionList == null) {
            tempExpressionList = new ArrayList<TdExpression>();
        } else {
            tempExpressionList.clear();
        }
        if (def != null) {
            EList<TdExpression> expressions = def.getSqlGenericExpression();
            for (TdExpression exp : expressions) {
                tempExpressionList.add(cloneExpression(exp));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#createFormContent(org.eclipse.ui.forms.IManagedForm
     * )
     */
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.formTitle")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.formMedata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.formDescript")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        // MOD by zshen move CategorySection before of DefinitionSection on UDI Editor
        createIndicatorContent();
        form.reflow(true);
    }

    protected void createIndicatorContent() {
        createDefinitionSection();
        if (IndicatorCategoryHelper.isCorrelation(category)) {
            createAdditionalFunctionsSection();
        }
        if (this.hasCharactersMapping) {
            createCharactersMappingSection();
        }
    }

    /**
     * DOC xqliu Comment method "createCharactersMappingSection".
     * 
     * @param topComp
     */
    private void createCharactersMappingSection() {
        charactersMappingSection = createSection(form, topComp,
                DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.CharactersMappingSectionTitle"), null); //$NON-NLS-1$
        charactersMappingComp = createCharactersMappingComp();
        charactersMappingSection.setClient(charactersMappingComp);
    }

    /**
     * DOC xqliu Comment method "createCharactersMappingComp".
     * 
     * @param charactersMappingSection
     * @return
     */
    private Composite createCharactersMappingComp() {
        Composite composite = toolkit.createComposite(charactersMappingSection);
        composite.setLayout(new GridLayout());

        charactersMappingLineComp = new Composite(composite, SWT.NONE);
        charactersMappingLineComp.setLayout(new GridLayout());
        charactersMappingLineComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        if (getCurrentModelElement() != null) {
            EList<CharactersMapping> charactersMappings = getCurrentModelElement().getCharactersMapping();

            if (charactersMappings != null && charactersMappings.size() > 0) {
                for (CharactersMapping charactersMapping : charactersMappings) {
                    recordCharactersMapping(charactersMapping);
                }
            }

            charactersMappingMapTemp.clear();
            for (String key : charactersMappingMap.keySet()) {
                charactersMappingMapTemp.put(key, cloneCharactersMapping(charactersMappingMap.get(key)));
            }

            for (String language : charactersMappingMapTemp.keySet()) {
                createNewCharactersMappingLine(language, charactersMappingMapTemp.get(language));
            }
        }

        createCMAddButton(composite);

        return composite;
    }

    /**
     * DOC xqliu Comment method "createCMAddButton".
     * 
     * @param composite
     */
    private void createCMAddButton(Composite composite) {
        final Button addButton = new Button(composite, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.CENTER;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                remainDBTypeListCM.clear();
                remainDBTypeListCM.addAll(allDBTypeList);
                for (CharactersMapping cm : charactersMappingMapTemp.values()) {
                    String language = cm.getLanguage();
                    String languageName = PatternLanguageType.findNameByLanguage(language);
                    remainDBTypeListCM.remove(languageName);
                }
                if (remainDBTypeListCM.size() == 0) {
                    MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.warning"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternExpression")); //$NON-NLS-1$
                    return;
                }

                String language = PatternLanguageType.findLanguageByName(remainDBTypeListCM.get(0));
                CharactersMapping cm = DefinitionFactory.eINSTANCE.createCharactersMapping();
                cm.setLanguage(language);
                cm.setCharactersToReplace(BODY_CHARACTERS_TO_REPLACE);
                cm.setReplacementCharacters(BODY_REPLACEMENT_CHARACTERS);
                createNewCharactersMappingLine(language, cm);
                charactersMappingMapTemp.put(language, cm);
                charactersMappingSection.setExpanded(true);
                setDirty(true);
            }
        });

    }

    /**
     * DOC xqliu Comment method "createNewCharactersMappingLine".
     * 
     * @param language
     * @param charactersMapping
     */
    private void createNewCharactersMappingLine(String language, final CharactersMapping charactersMapping) {
        final Composite cmComp = new Composite(charactersMappingLineComp, SWT.NONE);
        cmComp.setLayout(new GridLayout(2, false));

        final Composite cmLanguageComp = new Composite(cmComp, SWT.NONE);
        cmLanguageComp.setLayout(new GridLayout());
        cmLanguageComp.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        final CCombo combo = new CCombo(cmLanguageComp, SWT.BORDER);
        GridData comboGridData = new GridData();
        comboGridData.widthHint = 150;
        comboGridData.verticalAlignment = SWT.TOP;
        combo.setLayoutData(comboGridData);
        combo.setEditable(false);
        combo.setItems(remainDBTypeListCM.toArray(new String[remainDBTypeListCM.size()]));
        if (language == null) {
            combo.setText(remainDBTypeListCM.get(0));
        } else {
            combo.setText(PatternLanguageType.findNameByLanguage(language));
        }
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String lang = combo.getText();
                charactersMapping.setLanguage(PatternLanguageType.findLanguageByName(lang));
                setDirty(true);
            }
        });

        final Composite cmBodyComp = new Composite(cmComp, SWT.NONE);
        cmBodyComp.setLayout(new GridLayout(2, false));
        cmBodyComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        String[] headers = { "Characters to replace", "Replacement characters" };//$NON-NLS-1$//$NON-NLS-2$
        int[] widths = { 300, 300 };
        buildCharactersMappingLineComp(cmBodyComp, charactersMapping, style, headers, widths);

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(cmComp);
    }

    /**
     * DOC xqliu Comment method "buildCharactersMappingLineComp".
     * 
     * @param cmBodyComp
     * @param charactersMapping
     * @param style
     * @param headers
     * @param widths
     */
    private void buildCharactersMappingLineComp(Composite cmBodyComp, CharactersMapping charactersMapping, int style,
            String[] headers, int[] widths) {
        Table table = new Table(cmBodyComp, style);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        for (int i = 0; i < headers.length; ++i) {
            TableColumn tableColumn = new TableColumn(table, SWT.LEFT, i);
            tableColumn.setText(headers[i]);
            tableColumn.setWidth(widths[i]);
        }
        TableViewer tableViewer = new TableViewer(table);

        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(headers);

        CellEditor[] editors = new CellEditor[headers.length];
        for (int i = 0; i < editors.length; ++i) {
            editors[i] = new TextCellEditor(table);
        }
        tableViewer.setCellEditors(editors);
        tableViewer.setCellModifier(new CharactersMappingCellModifier(headers, tableViewer));
        tableViewer.setContentProvider(new CommonContentProvider());
        tableViewer.setLabelProvider(new CharactersMappingLabelProvider());
        tableViewer.setInput(charactersMapping);

        createDelButton(cmBodyComp, charactersMapping.getLanguage());
    }

    /**
     * DOC talend Comment method "createDelButton".
     * 
     * @param cmBodyComp
     */
    private void createDelButton(final Composite parent, final String language) {
        Button delButton = new Button(parent, SWT.PUSH);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setToolTipText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.deleteCharacterMapping")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;
        delButton.setLayoutData(labelGd);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                parent.getParent().dispose();
                // String languageName = PatternLanguageType.findNameByLanguage(language);
                charactersMappingMapTemp.remove(language);
                charactersMappingSection.setExpanded(true);
                setDirty(true);
            }

        });

    }

    /**
     * DOC xqliu Comment method "cloneCharactersMapping".
     * 
     * @param charactersMapping
     * @return
     */
    private CharactersMapping cloneCharactersMapping(CharactersMapping charactersMapping) {
        if (charactersMapping != null) {
            CharactersMapping newCharactersMapping = DefinitionFactory.eINSTANCE.createCharactersMapping();
            newCharactersMapping.setLanguage(charactersMapping.getLanguage());
            newCharactersMapping.setCharactersToReplace(charactersMapping.getCharactersToReplace());
            newCharactersMapping.setReplacementCharacters(charactersMapping.getReplacementCharacters());
            return newCharactersMapping;
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "recordCharactersMapping".
     * 
     * @param charactersMappingMap
     * @param charactersMapping
     */
    private void recordCharactersMapping(CharactersMapping charactersMapping) {
        String language = null;
        if (charactersMapping != null) {
            language = charactersMapping.getLanguage();
        }
        if (language != null) {
            charactersMappingMap.put(language, charactersMapping);
        }
    }

    /**
     * DOC xqliu Comment method "createAdditionalFunctionsSection".
     * 
     */
    private void createAdditionalFunctionsSection() {
        additionalFunctionsSection = createSection(form, topComp,
                DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.AdditionalFunctionsSectionTitle"), null); //$NON-NLS-1$
        additionalFunctionsComp = createAdditionalFunctionsComp();
        additionalFunctionsSection.setClient(additionalFunctionsComp);
    }

    /**
     * DOC xqliu Comment method "createAdditionalFunctionsComp".
     * 
     * @return
     */
    private Composite createAdditionalFunctionsComp() {
        Composite composite = toolkit.createComposite(additionalFunctionsSection);
        composite.setLayout(new GridLayout());

        afExpressionComp = new Composite(composite, SWT.NONE);
        afExpressionComp.setLayout(new GridLayout());
        afExpressionComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        if (getCurrentModelElement() != null) {
            EList<TdExpression> aggregate1argFunctions = getCurrentModelElement().getAggregate1argFunctions();
            EList<TdExpression> date1argFunctions = getCurrentModelElement().getDate1argFunctions();

            if (aggregate1argFunctions != null && aggregate1argFunctions.size() > 0) {
                for (TdExpression expression : aggregate1argFunctions) {
                    recordAFExpression(afExpressionMap, expression, null);
                }
            }

            if (date1argFunctions != null && date1argFunctions.size() > 0) {
                for (TdExpression expression : date1argFunctions) {
                    recordAFExpression(afExpressionMap, null, expression);
                }
            }

            afExpressionMapTemp.clear();
            for (String key : afExpressionMap.keySet()) {
                afExpressionMapTemp.put(key, afExpressionMap.get(key).clone());
            }

            for (String language : afExpressionMapTemp.keySet()) {
                createNewAFExpressLine(language, afExpressionMapTemp.get(language));
            }
        }

        createAFAddButton(composite);

        return composite;
    }

    /**
     * DOC xqliu Comment method "createNewAFExpressLine".
     * 
     * @param language
     * @param aggregateDateExpression
     */
    private void createNewAFExpressLine(String language, final AggregateDateExpression aggregateDateExpression) {
        final Composite expressComp = new Composite(afExpressionComp, SWT.NONE);
        expressComp.setLayout(new GridLayout(2, false));

        final Composite expressionLanguageComp = new Composite(expressComp, SWT.NONE);
        expressionLanguageComp.setLayout(new GridLayout());
        expressionLanguageComp.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        new Label(expressionLanguageComp, SWT.NONE);
        final CCombo combo = new CCombo(expressionLanguageComp, SWT.BORDER);
        GridData comboGridData = new GridData();
        comboGridData.widthHint = 150;
        comboGridData.verticalAlignment = SWT.TOP;
        combo.setLayoutData(comboGridData);
        combo.setEditable(false);
        combo.setItems(remainDBTypeListAF.toArray(new String[remainDBTypeListAF.size()]));
        if (language == null) {
            combo.setText(remainDBTypeListAF.get(0));
        } else {
            combo.setText(PatternLanguageType.findNameByLanguage(language));
        }
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String lang = combo.getText();
                aggregateDateExpression.setLanguage(PatternLanguageType.findLanguageByName(lang));
                aggregateDateExpression.setModificationDate(UDIUtils.getCurrentDateTime());
                setDirty(true);
            }
        });

        final Composite expressionBodyComp = new Composite(expressComp, SWT.NONE);
        expressionBodyComp.setLayout(new GridLayout(1, false));
        expressionBodyComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

        if (aggregateDateExpression.haveAggregateExpression()) {
            String title = "bubble chart functions";//$NON-NLS-1$
            String[] headers = { "horizontal axis", "vertical axis", "bubble size" };//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            int[] widths = { 120, 120, 240 };
            buildAggregateDateComp(expressionBodyComp, aggregateDateExpression, style, title, headers, widths);
        }

        if (aggregateDateExpression.haveDateExpression()) {
            String title = "gantt chart functions";//$NON-NLS-1$
            String[] headers = { "lower value", "upper value", "total", "highlighted values" };//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
            int[] widths = { 120, 120, 120, 240 };
            buildAggregateDateComp(expressionBodyComp, aggregateDateExpression, style, title, headers, widths);
        }

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(expressComp);
    }

    /**
     * DOC xqliu Comment method "buildAggregateDateComp".
     * 
     * @param expressionBodyComp
     * @param aggregateDateExpression
     * @param style
     * @param title
     * @param headers
     * @param widths
     */
    private void buildAggregateDateComp(final Composite expressionBodyComp,
            final AggregateDateExpression aggregateDateExpression, int style, String title, String[] headers, int[] widths) {
        Label label = new Label(expressionBodyComp, SWT.LEFT);
        label.setText(title);

        Table table = new Table(expressionBodyComp, style);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        for (int i = 0; i < headers.length; ++i) {
            TableColumn tableColumn = new TableColumn(table, SWT.LEFT, i);
            tableColumn.setText(headers[i]);
            tableColumn.setWidth(widths[i]);
        }

        TableViewer tableViewer = new TableViewer(table);

        tableViewer.setUseHashlookup(true);
        tableViewer.setColumnProperties(headers);

        CellEditor[] editors = new CellEditor[headers.length];
        for (int i = 0; i < editors.length; ++i) {
            editors[i] = new TextCellEditor(table);
            ((Text) editors[i].getControl()).addVerifyListener(

            new VerifyListener() {

                public void verifyText(VerifyEvent e) {
                    e.doit = !ADDITIONAL_FUNCTIONS_SPLIT.equals(e.text);
                }
            });
        }
        tableViewer.setCellEditors(editors);

        if (aggregateDateExpression.haveAggregateExpression()) {
            tableViewer.setCellModifier(new AggregateCellModifier(headers, tableViewer));
            tableViewer.setContentProvider(new CommonContentProvider());
            tableViewer.setLabelProvider(new AggregateLabelProvider());
            tableViewer.setInput(new AggregateVO(aggregateDateExpression.getAggregateExpression()));
        }

        if (aggregateDateExpression.haveDateExpression()) {
            tableViewer.setCellModifier(new DateCellModifier(headers, tableViewer));
            tableViewer.setContentProvider(new CommonContentProvider());
            tableViewer.setLabelProvider(new DateLabelProvider());
            tableViewer.setInput(new DateVO(aggregateDateExpression.getDateExpression()));
        }
    }

    /**
     * DOC xqliu Comment method "recordAFExpression".
     * 
     * @param expressionMap
     * @param aggregateExpression
     * @param dateExpression
     */
    private void recordAFExpression(Map<String, AggregateDateExpression> expressionMap, TdExpression aggregateExpression,
            TdExpression dateExpression) {
        String language = null;
        if (aggregateExpression != null) {
            language = aggregateExpression.getLanguage();
        } else if (dateExpression != null) {
            language = dateExpression.getLanguage();
        }
        if (language != null) {
            AggregateDateExpression aggregateDateExpression = expressionMap.get(language);
            if (aggregateDateExpression == null) {
                AggregateDateExpression expression = new AggregateDateExpression();
                expression.setAggregateExpression(aggregateExpression);
                expression.setDateExpression(dateExpression);
                expressionMap.put(language, expression);
            } else {
                if (aggregateExpression != null) {
                    aggregateDateExpression.setAggregateExpression(aggregateExpression);
                } else if (dateExpression != null) {
                    aggregateDateExpression.setDateExpression(dateExpression);
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "createAFAddButton".
     * 
     * @param composite
     */
    private void createAFAddButton(Composite composite) {
        final Button addButton = new Button(composite, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.CENTER;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                remainDBTypeListAF.clear();
                remainDBTypeListAF.addAll(allDBTypeList);
                for (AggregateDateExpression ade : afExpressionMapTemp.values()) {
                    String language = ade.getLanguage();
                    String languageName = PatternLanguageType.findNameByLanguage(language);
                    remainDBTypeListAF.remove(languageName);
                }
                if (remainDBTypeListAF.size() == 0) {
                    MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.warning"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternExpression")); //$NON-NLS-1$
                    return;
                }

                String language = PatternLanguageType.findLanguageByName(remainDBTypeListAF.get(0));
                TdExpression expression = BooleanExpressionHelper.createTdExpression(language, PluginConstant.EMPTY_STRING);
                expression.setModificationDate(UDIUtils.getCurrentDateTime());
                AggregateDateExpression ade = new AggregateDateExpression();
                if (hasAggregateExpression) {
                    expression.setBody(BODY_AGGREGATE);
                    ade.setAggregateExpression(expression);
                }
                if (hasDateExpression) {
                    expression.setBody(BODY_DATE);
                    ade.setDateExpression(expression);
                }
                createNewAFExpressLine(language, ade);
                afExpressionMapTemp.put(language, ade);
                additionalFunctionsSection.setExpanded(true);
                setDirty(true);
            }
        });

    }

    /**
     * create Definition Section.
     */
    protected void createDefinitionSection() {
        definitionSection = createSection(form, topComp,
                DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.definition"), DefaultMessagesImpl //$NON-NLS-1$
                        .getString("IndicatorDefinitionMaterPage.definitionDecription")); //$NON-NLS-1$
        definitionComp = createDefinitionComp();
        definitionSection.setClient(definitionComp);
    }

    /**
     * DOC bZhou Comment method "createPatternDefinitionComp". MOD mzhao feature 11128 Be able to add Java UDI,
     * 2010-01-27
     * 
     * @param definitionSection
     * 
     * @return
     */
    protected Composite createDefinitionComp() {
        Composite composite = toolkit.createComposite(definitionSection);
        composite.setLayout(new GridLayout());

        expressionComp = new Composite(composite, SWT.NONE);
        expressionComp.setLayout(new GridLayout());
        expressionComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        // base sql composite
        dataBaseComp = new Composite(expressionComp, SWT.NONE);
        dataBaseComp.setLayout(new GridLayout());
        dataBaseComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        // ADD xqliu 2010-02-26 bug 11201
        if (tempExpressionList.size() > 0) {// || !checkJavaUDIBeforeOpen()) {
            createDatabaseTitleComp();
        }
        // ADD klliu 2010-06-02 bug 13451: Class name of Java User Define Indicator must be validated
        // MOD backport klliu2010-06-10
        if (tempExpressionMap.size() == 0) {
            if (getCurrentModelElement() != null) {
                // MOD xqliu 2010-03-23 feature 11201
                for (TdExpression expression : tempExpressionList) {
                    createNewLineWithExpression(expression);
                }
                // createNewLineWithJavaUDI();
            }
        }
        createAddButton(composite);

        return composite;
    }

    /**
     * create Database Title Composite.
     * 
     */
    protected void createDatabaseTitleComp() {
        dataBaseTitleComp = new Composite(dataBaseComp, SWT.NONE);
        dataBaseTitleComp.setLayout(new GridLayout(3, false));

        // database Label
        Label databaseLabel = new Label(dataBaseTitleComp, SWT.NONE);
        databaseLabel.setText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.database")); //$NON-NLS-1$
        databaseLabel.setLayoutData(new GridData());
        ((GridData) databaseLabel.getLayoutData()).widthHint = 160;

        // dbversion Label
        Label dbversionLabel = new Label(dataBaseTitleComp, SWT.NONE);
        dbversionLabel.setText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.dbVersion")); //$NON-NLS-1$
        dbversionLabel.setLayoutData(new GridData(GridData.BEGINNING));
        ((GridData) dbversionLabel.getLayoutData()).widthHint = 38;

        // sqlTemplate Label
        Label sqlTemplateLabel = new Label(dataBaseTitleComp, SWT.NONE);
        sqlTemplateLabel.setText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.sqlTemplate")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(dataBaseTitleComp);
    }

    /**
     * 
     * DOC mzhao IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    protected class NeedToSetDirtyListener implements ModifyListener {

        public void modifyText(ModifyEvent e) {
            setDirty(true);
        }
    }

    private void createNewLineWithExpression(final TdExpression expression) {
        final Composite lineComp = new Composite(dataBaseComp, SWT.NONE);
        lineComp.setLayout(new GridLayout(3, false));
        final CCombo combo = new CCombo(lineComp, SWT.BORDER);

        putTdExpressToTempMap(combo, expression);

        combo.setLayoutData(new GridData());
        ((GridData) combo.getLayoutData()).widthHint = 150;
        combo.setEditable(false);
        // MOD xqliu 2010-02-25 feature 11201
        combo.setItems(allDBTypeList.toArray(new String[allDBTypeList.size()]));
        // ~
        String language = expression.getLanguage();
        if (language == null) {
            // MOD xqliu 2010-02-25 feature 11201
            combo.setText(allDBTypeList.get(0));
            // ~
        } else {
            combo.setText(PatternLanguageType.findNameByLanguage(language));
        }
        combo.addSelectionListener(new LangCombSelectionListener());
        Composite detailComp = new Composite(lineComp, SWT.NONE);
        detailComp.setLayout(new GridLayout(4, false));

        createDataBaseLineComponent(combo, expression, detailComp);

        widgetMap.put(combo, detailComp);
        updateOtherCombos(combo);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(detailComp);
    }

    /**
     * DOC msjian Comment method "putTdExpressToTempMap".
     * 
     * @param expression
     * @param combo
     */
    public void putTdExpressToTempMap(final CCombo combo, final TdExpression expression) {
        tempExpressionMap.put(combo, expression);
    }

    public void removeFromTempMap(final CCombo combo) {
        tempExpressionMap.remove(combo);
    }

    /**
     * set To Temp Map.
     * 
     * @param expression
     * @param combo
     * @param list
     * @param map
     * @return
     */
    public Map<CCombo, TdExpression> setToTempMap(final TdExpression expression, final CCombo combo, EList<TdExpression> list,
            Map<CCombo, TdExpression> map) {
        Map<CCombo, TdExpression> temp = map;
        String language = expression.getLanguage();
        String version = expression.getVersion();
        if (list != null) {
            for (TdExpression tdExp : list) {
                if (tdExp.getLanguage().equals(language)
                        && ((tdExp.getVersion() == null && version == null) || (tdExp.getVersion() != null && tdExp.getVersion()
                                .equals(version)))) {
                    temp.put(combo, tdExp);
                    break;
                }
            }
        }
        return temp;
    }

    /**
     * DOC bZhou Comment method "creatNewLine". MOD mzhao feature 11128 Be able to add Java UDI, 2010-01-27
     * 
     * @param expression
     */
    private void createNewLine() {
        if (dataBaseTitleComp == null || dataBaseTitleComp.isDisposed()) {
            createDatabaseTitleComp();
        }
        final Composite lineComp = new Composite(dataBaseComp, SWT.NONE);
        lineComp.setLayout(new GridLayout(5, false));
        final CCombo combo = new CCombo(lineComp, SWT.BORDER);
        combo.setLayoutData(new GridData());
        ((GridData) combo.getLayoutData()).widthHint = 150;

        combo.setEditable(false);
        // MOD xqliu 2010-02-25 feature 11201
        combo.setItems(allDBTypeList.toArray(new String[allDBTypeList.size()]));
        // ~
        combo.select(0);

        combo.addSelectionListener(new LangCombSelectionListener());

        // MOD TDQ-6824 msjian 2013-2-8: when create expression, we should set correct language
        TdExpression expression = BooleanExpressionHelper.createTdExpression(
                PatternLanguageType.findLanguageByName(combo.getText()), null);
        // TDQ-6824~
        String oldLanguage = expression.getLanguage();
        expression.setModificationDate(UDIUtils.getCurrentDateTime());
        removeDisposedComboFromTempExpMap();

        putTdExpressToTempMap(combo, expression);

        updateLineAndOtherCombos(combo, expression, oldLanguage);
    }

    /**
     * DOC msjian Comment method "updateLineAndOtherCombos".
     * 
     * @param combo
     * @param expression
     * @param oldLanguage
     */
    public void updateLineAndOtherCombos(final CCombo combo, TdExpression expression, String oldLanguage) {
        // MOD xqliu 2010-03-23 feature 11201
        updateLineForExpression(combo, expression, oldLanguage);
        // ~11201
        updateOtherCombos(combo);
    }

    /**
     * 
     * DOC mzhao IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    protected class ExpressTextModListener implements ModifyListener {

        private CCombo combo;

        public ExpressTextModListener(CCombo combo) {
            this.combo = combo;
        }

        public void modifyText(ModifyEvent e) {
            Text patternText = (Text) e.getSource();
            TdExpression expression = tempExpressionMap.get(combo);
            expression.setBody(patternText.getText());
            expression.setModificationDate(UDIUtils.getCurrentDateTime());
            setDirty(true);
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    private class DbVersionTextModListener implements ModifyListener {

        private CCombo combo;

        public DbVersionTextModListener(CCombo combo) {
            this.combo = combo;
        }

        public void modifyText(ModifyEvent e) {
            // MOD xqliu 2010-04-01 bug 11892
            setDirty(true);

            Text dbVersionText = (Text) e.getSource();
            List<TdExpression> modifyList = new ArrayList<TdExpression>();
            modifyList.add(tempExpressionMap.get(this.combo));
            // update other Temp Maps
            if (getCurrentModelElement() instanceof UDIndicatorDefinition) {
                if (IndicatorCategoryHelper.isUserDefMatching(category)) {
                    modifyList.add(tempViewValidRowsExpressionMap.get(this.combo));
                    modifyList.add(tempViewInvalidRowsExpressionMap.get(this.combo));
                    modifyList.add(tempViewValidValuesExpressionMap.get(this.combo));
                    modifyList.add(tempViewInvalidValuesExpressionMap.get(this.combo));

                } else {
                    modifyList.add(tempViewRowsExpressionMap.get(this.combo));
                }
            }

            for (TdExpression exp : modifyList) {
                if (exp != null) {
                    exp.setVersion(dbVersionText.getText().trim());
                    exp.setModificationDate(UDIUtils.getCurrentDateTime());
                }
            }
            // ~11892
        }

    }

    /**
     * Language Combo Selection Listener class
     * 
     */
    protected class LangCombSelectionListener extends SelectionAdapter {

        @Override
        public void widgetSelected(SelectionEvent e) {
            CCombo combo = (CCombo) e.getSource();
            String name = combo.getText();
            List<TdExpression> modifyList = new ArrayList<TdExpression>();
            TdExpression expression = tempExpressionMap.get(combo);
            String oldLanguage = expression.getLanguage();
            String oldSQLTemplate = expression.getBody();
            modifyList.add(expression);

            // check whether the java type exist first
            if (isJavaExist(combo)) {
                MessageUI.openError(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.isRepeated", combo.getText())); //$NON-NLS-1$
                combo.setText(PatternLanguageType.findNameByLanguage(oldLanguage));
                return;
            }

            if (oldLanguage.equals(PatternLanguageType.JAVA.getName())) {
                doDeleteOnlyForJava();
            }

            for (TdExpression exp : modifyList) {
                String newLanguage = PatternLanguageType.findLanguageByName(name);
                if (exp == null) {
                    exp = BooleanExpressionHelper.createTdExpression(newLanguage, oldSQLTemplate);
                    putTdExpressToTempMap(combo, exp);
                } else {
                    if (oldLanguage.equals(PatternLanguageType.JAVA.getName())) {
                        exp.setBody(null);
                    }
                    exp.setLanguage(newLanguage);
                }
                exp.setModificationDate(UDIUtils.getCurrentDateTime());
            }

            // do update line and Other Combos
            updateLineAndOtherCombos(combo, expression, oldLanguage);

            // then update other Temp Maps
            if (getCurrentModelElement() instanceof UDIndicatorDefinition) {
                if (IndicatorCategoryHelper.isUserDefMatching(category)) {
                    modifyList.add(tempViewValidRowsExpressionMap.get(combo));
                    modifyList.add(tempViewInvalidRowsExpressionMap.get(combo));
                    modifyList.add(tempViewValidValuesExpressionMap.get(combo));
                    modifyList.add(tempViewInvalidValuesExpressionMap.get(combo));

                } else {
                    modifyList.add(tempViewRowsExpressionMap.get(combo));
                }
            }

            removeDisposedComboFromTempExpMap();

            setDirty(true);
        }
    }

    /**
     * DOC msjian Comment method "updateLineForExpression".
     * 
     * @param combo
     * @param expression
     * @param oldLanguage
     */
    protected void updateLineForExpression(final CCombo combo, TdExpression expression, String oldLanguage) {
        // MOD klliu 2011-07-09 bug 22994: Headers are wrong for Java option in indicator editor.
        if (dataBaseTitleComp == null || dataBaseTitleComp.isDisposed()) {
            createDatabaseTitleComp();
        }
        Composite detailComp = widgetMap.get(combo);
        if (detailComp != null) {
            detailComp.dispose();
        }
        if (!oldLanguage.equals(PatternLanguageType.JAVA.getName())) {
            detailComp = new Composite(combo.getParent(), SWT.NONE);
            widgetMap.put(combo, detailComp);
            detailComp.setLayout(new GridLayout(4, false));
            // ADD xqliu 2010-04-02 feature 11201
            createDataBaseLineComponent(combo, expression, detailComp);
            // ~
        } else {
            updateDatabaseLineForJava(combo, expression);
        }
        definitionSection.setExpanded(false);
        definitionSection.setExpanded(true);
    }

    /**
     * update DatabaseLine For Java.
     * 
     * @param combo
     * @param expression
     */
    protected void updateDatabaseLineForJava(final CCombo combo, TdExpression expression) {
        // only needed in UDI master page
    }

    /**
     * create DataBase Line Component.
     * 
     * @param combo
     * @param expression
     * @param detailComp
     */
    protected void createDataBaseLineComponent(final CCombo combo, TdExpression expression, Composite detailComp) {
        final Text dbVersionText = new Text(detailComp, SWT.BORDER);
        dbVersionText.setText(expression.getVersion() == null ? PluginConstant.EMPTY_STRING : expression.getVersion());
        dbVersionText.setLayoutData(new GridData(GridData.BEGINNING));
        ((GridData) dbVersionText.getLayoutData()).widthHint = 30;
        dbVersionText.addModifyListener(new DbVersionTextModListener(combo));

        final Text expressionText = new Text(detailComp, SWT.BORDER);
        expressionText.setLayoutData(new GridData(GridData.FILL_BOTH));
        ((GridData) expressionText.getLayoutData()).widthHint = 600;
        // MOD xqliu 2010-03-23 feature 11201
        expressionText.setText(expression.getBody() == null ? PluginConstant.EMPTY_STRING : expression.getBody());
        // ~11201
        expressionText.addModifyListener(new ExpressTextModListener(combo));
        // ADD msjian TDQ-6841: set the pattern text can not input when the indicator is UDI
        // if (definition instanceof UDIndicatorDefinition) {
        expressionText.setEditable(isPatternTextEditable());
        // }
        // TDQ-6841~

        // TDQ-7868: when createExpressionEditButton, use the current version value in dbVersionText
        createExpressionEditButton(detailComp, expressionText, combo, dbVersionText);
        createExpressionDelButton(detailComp, combo);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(detailComp);
        detailComp.getParent().layout();
    }

    /**
     * DOC yyin Comment method "isPatternTextEditable".
     * 
     * @return boolean
     */
    protected boolean isPatternTextEditable() {
        return true;
    }

    /**
     * DOC xqliu Comment method "updateOtherCombos". MOD xqliu 2010-03-23 feature 11201
     * 
     * @param combo
     */
    protected void updateOtherCombos(CCombo combo) {
        // MOD xqliu 2010-03-23 feature 11201
        // rebuildRemainDBTypeList();
        // ~11201
        Collection<CCombo> allCombos = tempExpressionMap.keySet();
        for (CCombo cb : allCombos) {
            if (!combo.isDisposed() && combo != cb) {
                String tx = cb.getText();
                // MOD xqliu 2010-03-23 feature 11201
                cb.setItems(allDBTypeList.toArray(new String[allDBTypeList.size()]));
                // ~11201
                cb.setText(tx);
            }
        }

    }

    /**
     * create Expression Delete Button.
     * 
     * @param expressComp
     * @param languageCombo
     */
    protected void createExpressionDelButton(final Composite expressComp, final CCombo languageCombo) {
        Button delButton = new Button(expressComp, SWT.PUSH);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setToolTipText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.deleteExpression")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;
        delButton.setLayoutData(labelGd);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doDelete(expressComp, languageCombo.getText());
            }

        });
    }

    /**
     * the delete action for delete button.
     * 
     * @param expressComp
     * @param language
     * 
     */
    protected void doDelete(final Composite expressComp, String language) {
        // remove title line comp and combo line
        Composite lineComp = expressComp.getParent();
        Composite parent = lineComp.getParent();
        lineComp.dispose();
        if (parent.getChildren().length == 1) {
            Control[] children = parent.getChildren();
            children[0].dispose();
        }
        disposeExpressionChild();
        // ~
        removeDisposedComboFromTempExpMap();

        if (language.equals(PatternLanguageType.JAVA.getName())) {
            doDeleteOnlyForJava();
        }

        definitionSection.setExpanded(false);
        definitionSection.setExpanded(true);
        setDirty(true);
    }

    /**
     * do Delete Only For java type.
     */
    protected void doDeleteOnlyForJava() {
        // do nothing here
    }

    protected void disposeExpressionChild() {
        int dataBaseLength = dataBaseComp == null ? 0 : dataBaseComp.getChildren().length;
        if (dataBaseLength == 0) {
            createDatabaseTitleComp();
        }
    }

    /**
     * DOC yyi 2009-09-11 Feature:9030.
     * 
     * @param expressComp
     * @param patternText
     * 
     * @return
     */
    private void createExpressionEditButton(Composite expressComp, final Text patternText, final CCombo combo,
            final Text dbVersionText) {
        Button editButton = new Button(expressComp, SWT.PUSH);
        editButton.setText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.editExpression")); //$NON-NLS-1$
        editButton.setToolTipText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.editExpression")); //$NON-NLS-1$
        editButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                final ExpressionEditDialog editDialog = initExpresstionEditDialog(combo, dbVersionText.getText(),
                        patternText.getText());
                if (Dialog.OK == editDialog.open()) {
                    patternText.setText(editDialog.getTempExpression().getBody());
                    handleSelectExpression(combo, editDialog);
                    setDirty(true);
                }
            }

        });

    }

    protected ExpressionEditDialog initExpresstionEditDialog(final CCombo combo, final String version, String patternText) {
        TdExpression tdExpression = getTdExpression(combo, version);
        String language = tdExpression.getLanguage();

        ExpressionEditDialog editDialog = new ExpressionEditDialog(null, patternText, false, cloneExpression(tdExpression));
        editDialog.setVersion(version);
        editDialog.setLanguage(language);
        editDialog.setCategory(category);

        return editDialog;
    }

    /**
     * get TdExpression.
     * 
     * @param combo
     * @param version
     * @return TdExpression
     */
    protected TdExpression getTdExpression(final CCombo combo, final String version) {
        TdExpression tdExpression = null;
        // if it is dirty, get value from tempMap, else from definition
        if (isDirty()) {
            tdExpression = tempExpressionMap.get(combo);
        } else {
            tdExpression = getCurrentLanguageExp(getCurrentModelElement().getSqlGenericExpression(),
                    PatternLanguageType.findLanguageByName(combo.getText()), version);
        }
        return tdExpression;
    }

    protected void handleSelectExpression(final CCombo combo, final ExpressionEditDialog editDialog) {
        tempExpressionMap.put(combo, editDialog.getTempExpression());
    }

    /**
     * get Current Language Expression.
     * 
     * @param tempViewValidRowsExpList
     * @param language
     * @param version
     */
    public TdExpression getCurrentLanguageExp(List<TdExpression> tempViewValidRowsExpList, String language, String version) {
        if (tempViewValidRowsExpList != null) {
            for (TdExpression tdExp : tempViewValidRowsExpList) {
                if (UDIUtils.isCurrentLanguageAndVersion(tdExp, language, version)) {
                    return tdExp;
                }
            }
        }
        TdExpression createTdExpression = BooleanExpressionHelper.createTdExpression(language, PluginConstant.EMPTY_STRING,
                version);
        createTdExpression.setModificationDate(UDIUtils.getCurrentDateTime());
        return createTdExpression;
    }

    /**
     * create Add Button.
     * 
     * @param parent Composite
     */
    private void createAddButton(final Composite parent) {
        final Button addButton = new Button(parent, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.CENTER;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        // MOD qiongli 2011-8-15 feature TDQ-1894:disable for phone nuber indicator
        if (getCurrentModelElement() != null && category != null && IndicatorCategoryHelper.isPhoneNumberCategory(category)) {
            addButton.setEnabled(false);
        }
        addButton.addSelectionListener(new SelectionAdapter() {

            // MOD mzhao feature 11128 Be able to add Java UDI, 2010-01-27
            @Override
            public void widgetSelected(SelectionEvent e) {
                createNewLine();
                definitionSection.setExpanded(true);
                setDirty(true);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((IndicatorEditor) getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        ReturnCode rc = canSave();
        if (!rc.isOk()) {
            return;
        }
        boolean needReloadJUDIJar = false;
        // ADD xqliu 2010-02-25 feature 11201
        rc = checkBeforeSave();
        if (rc.isOk()) {
            CCombo javaUDICombo = updateExpressions();

            saveUDIValues(javaUDICombo);

            if (hasAggregateExpression) {
                updateAggregateExpression();
            }

            if (hasDateExpression) {
                updateDateExpression();
            }

            if (hasCharactersMapping) {
                if (!updateCharactersMapping()) {
                    return;
                }
            }
            rc = UDIHelper.validate(getCurrentModelElement());
        }

        if (rc.isOk()) {
            super.doSave(monitor);
            this.isDirty = false;
            // Mod TDQ-7474, only when rc is ok, should save the definition
            // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
            TDQIndicatorDefinitionItem indicatorDefinitionItem = (TDQIndicatorDefinitionItem) getCurrentRepNode().getObject()
                    .getProperty().getItem();
            ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().save(indicatorDefinitionItem, true);

            if (UDIHelper.isJUDIValid(getCurrentModelElement()) && needReloadJUDIJar) {
                UDIHelper.clearJAVAUDIMAPByIndicatorDefinition(getCurrentModelElement());
            }
        } else {
            MessageUI.openError(rc.getMessage());
        }
    }

    /**
     * currently mainly used for save some UDI special related values.
     * 
     * @param javaUDICombo
     */
    protected void saveUDIValues(CCombo javaUDICombo) {
        // save some other related values, current only need in UDI

    }

    /**
     * check whether the java type is exist.
     * 
     * @param combo
     * @return boolean
     */
    protected boolean isJavaExist(CCombo combo) {
        return false;
    }

    /**
     * check the tempExpressionMap contains java expression.
     * 
     * @return boolean
     */
    protected boolean checkContainJavaInTempExpressionMap() {
        for (TdExpression value : tempExpressionMap.values()) {
            if (value.getLanguage().equals(PatternLanguageType.JAVA.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC msjian Comment method "updateExpressions".
     * 
     * @return
     */
    private CCombo updateExpressions() {
        EList<TdExpression> expressions = getCurrentModelElement().getSqlGenericExpression();
        expressions.clear();
        Iterator<CCombo> it = tempExpressionMap.keySet().iterator();

        CCombo javaUDICombo = null;
        while (it.hasNext()) {
            CCombo cb = it.next();
            // MOD MOD mzhao feature 11128 Be able to add Java UDI, 2010-01-28
            if (cb.getText().equals(PatternLanguageType.JAVA.getName())) {
                javaUDICombo = cb;
            } else {
                TdExpression exp = tempExpressionMap.get(cb);
                if (exp.getBody() != null && !PluginConstant.EMPTY_STRING.equals(exp.getBody())) {
                    // MOD xqliu 2010-04-01 bug 11892
                    TdExpression cloneExpression = cloneExpression(exp);
                    expressions.add(cloneExpression);
                    // ~11892
                }
            }
        }
        return javaUDICombo;
    }

    private boolean updateCharactersMapping() {
        EList<CharactersMapping> charactersMappings = getCurrentModelElement().getCharactersMapping();
        charactersMappings.clear();
        for (CharactersMapping cm : charactersMappingMapTemp.values()) {
            String c = cm.getCharactersToReplace();
            String r = cm.getReplacementCharacters();
            if (checkMappingString(c, r)) {
                charactersMappings.add(cm);
            } else {
                MessageUI.openError("[" + cm.getLanguage()//$NON-NLS-1$
                        + "] INPUT ERROR:\nThe length of two inputed strings are not equal.");//$NON-NLS-1$
                return false;
            }
        }
        return true;
    }

    private void updateDateExpression() {
        EList<TdExpression> date1argFunctions = getCurrentModelElement().getDate1argFunctions();
        date1argFunctions.clear();
        for (AggregateDateExpression ade : afExpressionMapTemp.values()) {
            TdExpression expression = ade.getDateExpression();
            if (expression.getBody() != null && !PluginConstant.EMPTY_STRING.equals(expression.getBody())) {
                date1argFunctions.add(expression);
            }
        }
    }

    private void updateAggregateExpression() {
        EList<TdExpression> aggregate1argFunctions = getCurrentModelElement().getAggregate1argFunctions();
        aggregate1argFunctions.clear();
        for (AggregateDateExpression ade : afExpressionMapTemp.values()) {
            TdExpression expression = ade.getAggregateExpression();
            if (expression.getBody() != null && !PluginConstant.EMPTY_STRING.equals(expression.getBody())) {
                aggregate1argFunctions.add(expression);
            }
        }
    }

    /**
     * DOC msjian Comment method "saveFromTempMapToDefinition".
     * 
     * @param expressions
     * @return
     */
    public EList<TdExpression> saveFromTempMapToDefinition(EList<TdExpression> expressions, Map<CCombo, TdExpression> map) {
        EList<TdExpression> temp = expressions;
        temp.clear();
        Iterator<CCombo> it = map.keySet().iterator();
        while (it.hasNext()) {
            CCombo cb = it.next();
            TdExpression exp = map.get(cb);
            if (exp.getBody() != null && !PluginConstant.EMPTY_STRING.equals(exp.getBody())) {
                TdExpression cloneExpression = cloneExpression(exp);
                temp.add(cloneExpression);
            }
        }
        return temp;
    }

    /**
     * DOC xqliu Comment method "checkBeforeSave". ADD xqliu 2010-02-25 feature 11201
     * 
     * @return
     */
    protected ReturnCode checkBeforeSave() {
        ReturnCode rc = null;

        // Added TDQ-7474 20131213 yyin
        // check the size of the tempExpressionMap, if size=0(means no expression), return false
        if (tempExpressionMap.size() == 0) {
            rc = new ReturnCode();
            rc.setOk(false);
            rc.setMessage(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.validateNoExpression"));//$NON-NLS-1$
            return rc;
        }// ~

        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        if (!checkWhithspace()) {
            rc = new ReturnCode();
            rc.setOk(false);
            rc.setMessage(DefaultMessagesImpl.getString("AbstractMetadataFormPage.whitespace"));//$NON-NLS-1$
            return rc;
        }

        ReturnCode checkCorrectForExpression = checkCorrectForExpression();
        if (!checkCorrectForExpression.isOk()) {
            return checkCorrectForExpression;
        }

        return checkCorrectForCharacterMapping();
    }

    /**
     * DOC talend Comment method "checkDuplicateLanguage".
     * 
     * @param rc
     * @return
     */
    private ReturnCode checkCorrectForCharacterMapping() {
        ReturnCode rc = new ReturnCode();
        Map<String, Integer> languageCountMap = new HashMap<String, Integer>();
        Iterator<CharactersMapping> charMappIterator = charactersMappingMapTemp.values().iterator();
        while (charMappIterator.hasNext()) {
            CharactersMapping cm = charMappIterator.next();
            String language = cm.getLanguage();
            Integer languageCount = languageCountMap.get(language);
            if (languageCount == null) {
                languageCount = 1;
            } else {
                languageCount++;
            }
            languageCountMap.put(language, languageCount);
        }

        Iterator<String> iterator = languageCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Integer integer = languageCountMap.get(key);
            if (integer > 1) {
                rc.setOk(false);
                rc.setMessage(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.isRepeatedCharMapp", key));//$NON-NLS-1$
                return rc;
            }
        }
        return rc;
    }

    /**
     * DOC talend Comment method "checkDuplicateLanguage".
     * 
     * @param rc
     * @return
     */
    private ReturnCode checkCorrectForExpression() {
        ReturnCode rc = new ReturnCode();
        Map<String, Integer> languageVersionCountMap = new HashMap<String, Integer>();
        Iterator<CCombo> it = tempExpressionMap.keySet().iterator();
        while (it.hasNext()) {
            CCombo cb = it.next();
            TdExpression expression = tempExpressionMap.get(cb);
            String language = expression.getLanguage();
            String version = expression.getVersion();
            if (!language.equals(PatternLanguageType.JAVA.getLiteral())) {
                String body = expression.getBody();
                if (null == body || body.length() + 1 < UDIHelper.MIN_EXPRESSION_LENGTH) {
                    rc.setOk(false);
                    rc.setMessage(DefaultMessagesImpl.getString("IndicatorDefinition.validateTooShort"));//$NON-NLS-1$
                    return rc;
                }
            }
            if (version != null && !PluginConstant.EMPTY_STRING.equals(version)) {
                language = language + " V" + expression.getVersion();//$NON-NLS-1$
            }
            Integer integer = languageVersionCountMap.get(language);
            if (integer == null) {
                languageVersionCountMap.put(language, 1);
            } else {
                languageVersionCountMap.put(language, integer + 1);
            }
        }
        Iterator<String> iterator = languageVersionCountMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Integer integer = languageVersionCountMap.get(key);
            if (integer > 1) {
                rc.setOk(false);
                rc.setMessage(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.isRepeatedExp", key));//$NON-NLS-1$
                return rc;
            }
        }
        return rc;
    }

    /**
     * if CCombo is disposed,remove it from tempExpressionMap.
     */
    private void removeDisposedComboFromTempExpMap() {
        Iterator<CCombo> it = tempExpressionMap.keySet().iterator();
        List<CCombo> disposedKey = new ArrayList<CCombo>();
        while (it.hasNext()) {
            CCombo cb = it.next();
            if (cb.isDisposed()) {
                disposedKey.add(cb);
            }
        }
        for (CCombo combo : disposedKey) {
            removeFromTempMap(combo);
        }
    }

    /**
     * DOC bZhou Comment method "checkMappingString".
     * 
     * @param c
     * @param r
     * @return
     */
    private boolean checkMappingString(String c, String r) {
        return !PluginConstant.EMPTY_STRING.equals(c) && !PluginConstant.EMPTY_STRING.equals(r) && c.length() == r.length();
    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private static final class AggregateDateExpression implements Cloneable {

        private TdExpression aggregateExpression;

        private TdExpression dateExpression;

        public TdExpression getAggregateExpression() {
            return aggregateExpression;
        }

        public void setAggregateExpression(TdExpression aggregateExpression) {
            this.aggregateExpression = aggregateExpression;
        }

        public TdExpression getDateExpression() {
            return dateExpression;
        }

        public void setDateExpression(TdExpression dateExpression) {
            this.dateExpression = dateExpression;
        }

        public AggregateDateExpression() {
        }

        public void setLanguage(String language) {
            if (aggregateExpression != null) {
                aggregateExpression.setLanguage(language);
            }
            if (dateExpression != null) {
                dateExpression.setLanguage(language);
            }
        }

        public String getLanguage() {
            if (aggregateExpression != null) {
                return aggregateExpression.getLanguage();
            }
            if (dateExpression != null) {
                return dateExpression.getLanguage();
            }
            return PluginConstant.EMPTY_STRING;
        }

        public boolean haveAggregateExpression() {
            return aggregateExpression != null;
        }

        public boolean haveDateExpression() {
            return dateExpression != null;
        }

        @Override
        protected AggregateDateExpression clone() {
            AggregateDateExpression ade = null;
            try {
                ade = (AggregateDateExpression) super.clone();
                if (getAggregateExpression() != null) {
                    ade.setAggregateExpression(BooleanExpressionHelper.createTdExpression(getAggregateExpression().getLanguage(),
                            getAggregateExpression().getBody()));
                }
                if (getDateExpression() != null) {
                    ade.setDateExpression(BooleanExpressionHelper.createTdExpression(getDateExpression().getLanguage(),
                            getDateExpression().getBody()));
                }
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage());
            }
            return ade;
        }

        public void setModificationDate(String dateValue) {
            if (aggregateExpression != null) {
                aggregateExpression.setModificationDate(dateValue);
            }
            if (dateExpression != null) {
                dateExpression.setModificationDate(dateValue);
            }
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private final class AggregateVO {

        String horizontalAxis, verticalAxis, bubbleSize;

        TdExpression aggreagetExpression;

        public String getHorizontalAxis() {
            return horizontalAxis;
        }

        public void setHorizontalAxis(String horizontalAxis) {
            this.horizontalAxis = horizontalAxis;
            updateAggregateExpressionBody();
        }

        public String getVerticalAxis() {
            return verticalAxis;
        }

        public void setVerticalAxis(String verticalAxis) {
            this.verticalAxis = verticalAxis;
            updateAggregateExpressionBody();
        }

        public String getBubbleSize() {
            return bubbleSize;
        }

        public void setBubbleSize(String bubbleSize) {
            this.bubbleSize = bubbleSize;
            updateAggregateExpressionBody();
        }

        private void updateAggregateExpressionBody() {
            if (this.aggreagetExpression != null) {
                if (PluginConstant.EMPTY_STRING.equals(this.getHorizontalAxis())
                        && PluginConstant.EMPTY_STRING.equals(this.getVerticalAxis())
                        && PluginConstant.EMPTY_STRING.equals(this.getBubbleSize())) {
                    this.aggreagetExpression.setBody(PluginConstant.EMPTY_STRING);
                } else {
                    this.aggreagetExpression.setBody(this.getHorizontalAxis() + ADDITIONAL_FUNCTIONS_SPLIT
                            + this.getVerticalAxis() + ADDITIONAL_FUNCTIONS_SPLIT + this.getBubbleSize());
                }
            }
        }

        public AggregateVO(TdExpression aggregateExpression) {
            this.aggreagetExpression = aggregateExpression;
            if (this.aggreagetExpression != null) {
                String body = this.aggreagetExpression.getBody();
                if (body != null) {
                    String[] values = body.split(ADDITIONAL_FUNCTIONS_SPLIT);
                    if (values.length > 2) {
                        this.setHorizontalAxis(values[0]);
                        this.setVerticalAxis(values[1]);
                        this.setBubbleSize(values[2]);
                    } else {
                        this.setHorizontalAxis(PluginConstant.EMPTY_STRING);
                        this.setVerticalAxis(PluginConstant.EMPTY_STRING);
                        this.setBubbleSize(PluginConstant.EMPTY_STRING);
                    }
                }
            }
        }

        public String getBody() {
            return this.aggreagetExpression == null ? PluginConstant.EMPTY_STRING
                    : this.aggreagetExpression.getBody() == null ? PluginConstant.EMPTY_STRING : this.aggreagetExpression
                            .getBody();
        }

        public String getLanguage() {
            return this.aggreagetExpression == null ? PluginConstant.EMPTY_STRING
                    : this.aggreagetExpression.getLanguage() == null ? PluginConstant.EMPTY_STRING : this.aggreagetExpression
                            .getLanguage();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof AggregateVO) {
                AggregateVO vo = (AggregateVO) obj;
                return this.getBody().equals(vo.getBody()) && this.getLanguage().equals(vo.getLanguage());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.getLanguage().concat(this.getBody()).hashCode();
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    private final class AggregateCellModifier implements ICellModifier {

        private List<String> columeNames;

        private TableViewer tableViewer;

        public AggregateCellModifier(String[] columeNames, TableViewer tableViewer) {
            super();
            this.columeNames = new ArrayList<String>();
            for (String columnName : columeNames) {
                this.columeNames.add(columnName);
            }
            this.tableViewer = tableViewer;
        }

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {
            int columnIndex = columeNames.indexOf(property);

            Object result = null;
            AggregateVO vo = (AggregateVO) element;

            switch (columnIndex) {
            case 0: // Horizontal Axis
                result = vo.getHorizontalAxis();
                break;
            case 1: // Vertical Axis
                result = vo.getVerticalAxis();
                break;
            case 2: // Bubble Size
                result = vo.getBubbleSize();
                break;
            default:
                result = PluginConstant.EMPTY_STRING;
            }
            return result;
        }

        public void modify(Object element, String property, Object value) {
            int columnIndex = this.columeNames.indexOf(property);

            Table table = (Table) element;
            AggregateVO vo = (AggregateVO) table.getItem(0).getData();
            String valueString = ((String) value).trim();

            switch (columnIndex) {
            case 0: // Horizontal Axis
                vo.setHorizontalAxis(valueString);
                break;
            case 1: // Vertical Axis
                vo.setVerticalAxis(valueString);
                break;
            case 2: // Bubble Size
                vo.setBubbleSize(valueString);
                break;
            default:
            }
            tableViewer.setInput(vo);
            IndicatorDefinitionMaterPage.this.setDirty(true);
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private final class CommonContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            Object[] results = new Object[1];
            results[0] = inputElement;
            return results;
        }

        public void dispose() {
            // until now. do nothing
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // until now. do nothing
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class AggregateLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = PluginConstant.EMPTY_STRING;
            AggregateVO vo = (AggregateVO) element;
            switch (columnIndex) {
            case 0:
                result = vo.getHorizontalAxis();
                break;
            case 1:
                result = vo.getVerticalAxis();
                break;
            case 2:
                result = vo.getBubbleSize();
                break;
            default:
                break;
            }
            return result;
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private final class DateVO {

        String lowerValue, upperValue, total, highlightedValues;

        TdExpression dateExpression;

        public String getLowerValue() {
            return lowerValue;
        }

        public void setLowerValue(String lowerValue) {
            this.lowerValue = lowerValue;
            updateDateExpressionBody();
        }

        public String getUpperValue() {
            return upperValue;
        }

        public void setUpperValue(String upperValue) {
            this.upperValue = upperValue;
            updateDateExpressionBody();
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
            updateDateExpressionBody();
        }

        public String getHighlightedValues() {
            return highlightedValues;
        }

        public void setHighlightedValues(String highlightedValues) {
            this.highlightedValues = highlightedValues;
            updateDateExpressionBody();
        }

        private void updateDateExpressionBody() {
            if (this.dateExpression != null) {
                if (PluginConstant.EMPTY_STRING.equals(this.getLowerValue())
                        && PluginConstant.EMPTY_STRING.equals(this.getUpperValue())
                        && PluginConstant.EMPTY_STRING.equals(this.getTotal())
                        && PluginConstant.EMPTY_STRING.equals(this.getHighlightedValues())) {
                    this.dateExpression.setBody(PluginConstant.EMPTY_STRING);
                } else {
                    this.dateExpression.setBody(this.getLowerValue() + ADDITIONAL_FUNCTIONS_SPLIT + this.getUpperValue()
                            + ADDITIONAL_FUNCTIONS_SPLIT + this.getTotal() + ADDITIONAL_FUNCTIONS_SPLIT
                            + this.getHighlightedValues());
                }
            }

        }

        public DateVO(TdExpression dateExpression) {
            this.dateExpression = dateExpression;
            if (this.dateExpression != null) {
                String body = this.dateExpression.getBody();
                if (body != null) {
                    String[] values = body.split(ADDITIONAL_FUNCTIONS_SPLIT);
                    if (values.length > 3) {
                        this.setLowerValue(values[0]);
                        this.setUpperValue(values[1]);
                        this.setTotal(values[2]);
                        this.setHighlightedValues(values[3]);
                    } else {
                        this.setLowerValue(PluginConstant.EMPTY_STRING);
                        this.setUpperValue(PluginConstant.EMPTY_STRING);
                        this.setTotal(PluginConstant.EMPTY_STRING);
                        this.setHighlightedValues(PluginConstant.EMPTY_STRING);
                    }
                }
            }
        }

        public String getBody() {
            return this.dateExpression == null ? PluginConstant.EMPTY_STRING
                    : this.dateExpression.getBody() == null ? PluginConstant.EMPTY_STRING : this.dateExpression.getBody();
        }

        public String getLanguage() {
            return this.dateExpression == null ? PluginConstant.EMPTY_STRING
                    : this.dateExpression.getLanguage() == null ? PluginConstant.EMPTY_STRING : this.dateExpression.getLanguage();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof DateVO) {
                DateVO vo = (DateVO) obj;
                return this.getBody().equals(vo.getBody()) && this.getLanguage().equals(vo.getLanguage());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.getLanguage().concat(this.getBody()).hashCode();
        }

        // public void setModificationDate(String dateValue) {
        // if (dateExpression != null) {
        // dateExpression.setModificationDate(dateValue);
        // }
        // }
        //
        // public String getModificationDate() {
        // return dateExpression == null ? null : dateExpression.getModificationDate();
        // }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    private final class DateCellModifier implements ICellModifier {

        private List<String> columeNames;

        private TableViewer tableViewer;

        public DateCellModifier(String[] columeNames, TableViewer tableViewer) {
            super();
            this.columeNames = new ArrayList<String>();
            for (String columnName : columeNames) {
                this.columeNames.add(columnName);
            }
            this.tableViewer = tableViewer;
        }

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {
            int columnIndex = columeNames.indexOf(property);

            Object result = null;
            DateVO vo = (DateVO) element;

            switch (columnIndex) {
            case 0: // Lower Value
                result = vo.getLowerValue();
                break;
            case 1: // Upper Value
                result = vo.getUpperValue();
                break;
            case 2: // Total
                result = vo.getTotal();
                break;
            case 3: // Highlighted Values
                result = vo.getHighlightedValues();
                break;
            default:
                result = PluginConstant.EMPTY_STRING;
            }
            return result;
        }

        public void modify(Object element, String property, Object value) {
            int columnIndex = this.columeNames.indexOf(property);

            Table table = (Table) element;
            DateVO vo = (DateVO) table.getItem(0).getData();
            String valueString = ((String) value).trim();

            switch (columnIndex) {
            case 0: // Lower Value
                vo.setLowerValue(valueString);
                break;
            case 1: // Upper Value
                vo.setUpperValue(valueString);
                break;
            case 2: // Total
                vo.setTotal(valueString);
                break;
            case 3: // Highlighted Values
                vo.setHighlightedValues(valueString);
                break;
            default:
            }
            tableViewer.setInput(vo);
            IndicatorDefinitionMaterPage.this.setDirty(true);
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class DateLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = PluginConstant.EMPTY_STRING;
            DateVO vo = (DateVO) element;
            switch (columnIndex) {
            case 0:
                result = vo.getLowerValue();
                break;
            case 1:
                result = vo.getUpperValue();
                break;
            case 2:
                result = vo.getTotal();
                break;
            case 3:
                result = vo.getHighlightedValues();
                break;
            default:
                break;
            }
            return result;
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    private final class CharactersMappingCellModifier implements ICellModifier {

        private List<String> columeNames;

        private TableViewer tableViewer;

        public CharactersMappingCellModifier(String[] columeNames, TableViewer tableViewer) {
            super();
            this.columeNames = new ArrayList<String>();
            for (String columnName : columeNames) {
                this.columeNames.add(columnName);
            }
            this.tableViewer = tableViewer;
        }

        public boolean canModify(Object element, String property) {
            return true;
        }

        public Object getValue(Object element, String property) {
            int columnIndex = columeNames.indexOf(property);

            Object result = null;
            CharactersMapping cm = (CharactersMapping) element;

            switch (columnIndex) {
            case 0:
                result = cm.getCharactersToReplace();
                break;
            case 1:
                result = cm.getReplacementCharacters();
                break;
            default:
                result = PluginConstant.EMPTY_STRING;
            }
            return result;
        }

        public void modify(Object element, String property, Object value) {
            int columnIndex = this.columeNames.indexOf(property);

            Table table = (Table) element;
            CharactersMapping cm = (CharactersMapping) table.getItem(0).getData();
            String valueString = ((String) value).trim();

            switch (columnIndex) {
            case 0:
                cm.setCharactersToReplace(valueString);
                break;
            case 1:
                cm.setReplacementCharacters(valueString);
                break;
            default:
            }
            tableViewer.setInput(cm);
            IndicatorDefinitionMaterPage.this.setDirty(true);
        }

    }

    /**
     * DOC xqliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class CharactersMappingLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = PluginConstant.EMPTY_STRING;
            CharactersMapping cm = (CharactersMapping) element;
            switch (columnIndex) {
            case 0:
                result = cm.getCharactersToReplace();
                break;
            case 1:
                result = cm.getReplacementCharacters();
                break;
            default:
                break;
            }
            return result;
        }

    }

    /**
     * clone Expression.
     * 
     * @param exp
     * @return
     */
    public static final TdExpression cloneExpression(TdExpression exp) {
        if (exp == null) {
            return null;
        }
        TdExpression copy = EcoreUtil.copy(exp);
        HashMap<String, String> expressionVariableMap = exp.getExpressionVariableMap();
        if (expressionVariableMap != null) {
            copy.setExpressionVariableMap((HashMap<String, String>) expressionVariableMap.clone());
        }
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        ReturnCode rc = canModifyName(ERepositoryObjectType.TDQ_INDICATOR_ELEMENT);
        if (!rc.isOk()) {
            MessageDialogWithToggle.openError(null,
                    DefaultMessagesImpl.getString("AbstractMetadataFormPage.saveFailed"), rc.getMessage()); //$NON-NLS-1$
        }
        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public SysIndicatorDefinitionRepNode getCurrentRepNode() {
        return this.indicatorRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public IndicatorDefinition getCurrentModelElement() {
        return indicatorRepNode.getIndicatorDefinition();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#init(org.eclipse.ui.forms.editor.FormEditor)
     */
    @Override
    protected void init(FormEditor editor) {
        currentEditor = (IndicatorEditor) editor;
        this.indicatorRepNode = getIndicatorRepNodeFromInput(currentEditor.getEditorInput());

        String[] databaseTypes = PatternLanguageType.getAllLanguageTypes();
        // initialize user defined indicator category

        allDBTypeList = new ArrayList<String>();
        allDBTypeList.addAll(Arrays.asList(databaseTypes));
        // MOD klliu 13104: Do not allow the user to add a java language in the system indicators
        removeJavaType();

        // MOD xqliu 2010-03-23 feature 11201
        remainDBTypeListAF = new ArrayList<String>();
        remainDBTypeListAF.addAll(allDBTypeList);

        remainDBTypeListCM = new ArrayList<String>();
        remainDBTypeListCM.addAll(allDBTypeList);

        // MOD xqliu 2010-03-23 feature 11201
        initTempMaps();

        if (widgetMap == null) {
            widgetMap = new HashMap<CCombo, Composite>();
        } else {
            widgetMap.clear();
        }

        if (getCurrentModelElement() != null && getCurrentModelElement().getCategories().size() > 0) {
            category = getCurrentModelElement().getCategories().get(0);
        }

        if (getCurrentModelElement() != null) {
            hasAggregateExpression = getCurrentModelElement().getAggregate1argFunctions().size() > 0;
            hasDateExpression = getCurrentModelElement().getDate1argFunctions().size() > 0;
            hasCharactersMapping = getCurrentModelElement().getCharactersMapping().size() > 0;
            hasCharactersMapping = hasCharactersMapping || isEastAsiaPatternFequencyStatics(getCurrentModelElement());
        }
        afExpressionMap = new HashMap<String, AggregateDateExpression>();
        afExpressionMapTemp = new HashMap<String, AggregateDateExpression>();

        charactersMappingMap = new HashMap<String, CharactersMapping>();
        charactersMappingMapTemp = new HashMap<String, CharactersMapping>();

        // ADD xqliu 2010-03-23 feature 11201
        initTempExpressionList(getCurrentModelElement());

    }

    /**
     * get PatternRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private SysIndicatorDefinitionRepNode getIndicatorRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                IndicatorDefinition indicatorDefinition = IndicatorResourceFileHelper.getInstance().findIndDefinition(file);
                indicatorDefinition = (IndicatorDefinition) EObjectHelper.resolveObject(indicatorDefinition);
                return RepositoryNodeHelper.recursiveFindIndicatorDefinition(indicatorDefinition);
            }
        } else if (editorInput instanceof IndicatorDefinitionItemEditorInput) {
            return ((IndicatorDefinitionItemEditorInput) editorInput).getRepNode();
        }
        return null;
    }

}
