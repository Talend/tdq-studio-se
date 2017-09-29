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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataprofiler.core.ui.dialog.ExpressionEditDialog;
import org.talend.dataprofiler.core.ui.dialog.JavaUdiJarSelectDialog;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.resource.ResourceManager;
import org.talend.utils.classloader.TalendURLClassLoader;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * the Master Page for the user define indicator(UDI)
 */
public class UDIMasterPage extends IndicatorDefinitionMaterPage {

    private static Logger log = Logger.getLogger(UDIMasterPage.class);

    private static final String REMOVE_BUTTON_TEXT = DefaultMessagesImpl.getString("PatternMasterDetailsPage.del"); //$NON-NLS-1$

    private static final String DEFINITION_PARAMETER_SECTION_DESCRIPTION = DefaultMessagesImpl
            .getString("IndicatorDefinitionMaterPage.parametersDecription"); //$NON-NLS-1$

    private static final String DEFINITION_PARAMETER_SECTION_TITLE = DefaultMessagesImpl
            .getString("IndicatorDefinitionMaterPage.parameters"); //$NON-NLS-1$

    private static final String CATEGORY_SECTION_DESCRIPTION = DefaultMessagesImpl
            .getString("UDIMasterPage.CategorySectionDescription"); //$NON-NLS-1$

    private static final String CATEGORY_SECTION_TITLE = DefaultMessagesImpl.getString("UDIMasterPage.CategorySectionTitle"); //$NON-NLS-1$

    private static final String JAVA_CLASS = DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.javaClass"); //$NON-NLS-1$

    private static final String JARS = DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.jars"); //$NON-NLS-1$

    private static final String LANGUAGE = DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.language"); //$NON-NLS-1$

    private static final String PARAMETERS_VALUE = "Parameters Value"; //$NON-NLS-1$

    private static final String PARAMETERS_KEY = "Parameters Key"; //$NON-NLS-1$

    private Combo comboCategory;

    private Label labelDetail;

    private Composite categoryComp;

    private Section parametersSection;

    private Composite parametersComp;

    private Section categorySection;

    // used to when change the category changed, whether need the confirm dialog
    private boolean needConfirm = true;

    private IndicatorDefinitionParameter element = null;

    private TableViewer parameterTableViewer;

    // ADD klliu 2010-06-03 bug 13451
    private String classNameForSave;

    private String jarPathForSave;

    private Composite javaLanguageComp;

    private Composite javaTitleComp;

    // Add klliu figure 13429
    private List<IndicatorDefinitionParameter> tempParameters;

    /**
     * UDIMasterPage constructor .
     * 
     * @param editor
     * @param id
     * @param title
     */
    public UDIMasterPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#initialize(org.eclipse.ui.forms
     * .editor.FormEditor)
     */
    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);

        if (getCurrentModelElement() != null && getCurrentModelElement().getCategories().size() > 0) {
            category = getCurrentModelElement().getCategories().get(0);
        } else {
            category = DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory();
        }

        // ADD klliu 2010-07-14 feature 13429
        initTempIndicatorDefinitionParameter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#removeJavaType()
     */
    @Override
    protected void removeJavaType() {
        // no need to remove the java type from the db type list
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#isPatternTextEditable()
     */
    @Override
    protected boolean isPatternTextEditable() {
        return false;
    }

    /**
     * init Temp Indicator Definition Parameters.
     * 
     */
    private void initTempIndicatorDefinitionParameter() {
        if (getCurrentModelElement() != null) {
            tempParameters = cloneIndicatorDefParameter(getCurrentModelElement().getIndicatorDefinitionParameter());
        } else {
            tempParameters.clear();
        }
    }

    /**
     * clone Indicator Definition Parameters.
     * 
     * @param indicatorDefParameter
     * @return
     */
    private List<IndicatorDefinitionParameter> cloneIndicatorDefParameter(
            EList<IndicatorDefinitionParameter> indicatorDefParameter) {
        List<IndicatorDefinitionParameter> result = new ArrayList<IndicatorDefinitionParameter>();
        for (IndicatorDefinitionParameter param : indicatorDefParameter) {
            result.add(param.clone());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#createIndicatorContent()
     */
    @Override
    protected void createIndicatorContent() {
        createCategorySection();
        createDefinitionSection();
        createDefinitionParametersSection();
    }

    /**
     * create Definition Parameters Section.
     */
    private void createDefinitionParametersSection() {
        parametersSection = createSection(form, topComp, DEFINITION_PARAMETER_SECTION_TITLE,
                DEFINITION_PARAMETER_SECTION_DESCRIPTION);
        parametersComp = createDefinitionParametersComp();
        parametersSection.setClient(parametersComp);
    }

    /**
     * create Definition Parameters Composite.
     * 
     * @return
     */
    private Composite createDefinitionParametersComp() {
        Composite composite = toolkit.createComposite(parametersSection);
        GridData parData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(parData);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        parameterTableViewer = new TableViewer(composite);
        createDefiniationParameterColumns();
        IndicatorParametersContentProvider provider = new IndicatorParametersContentProvider();
        parameterTableViewer.setContentProvider(provider);
        parameterTableViewer.setLabelProvider(new IndicatorParametersLabelProvider());
        parameterTableViewer.setInput(tempParameters);
        createDefinitionParametersButton(composite);
        return composite;
    }

    /**
     * create Buttons for Definition Parameters.
     * 
     * @param comp
     */
    private void createDefinitionParametersButton(Composite comp) {
        Composite composite = toolkit.createComposite(comp);
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = SWT.CENTER;
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(gd);
        final Button addButton = new Button(composite, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        labelGd.horizontalAlignment = SWT.RIGHT;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                IndicatorDefinitionParameter ip = DefinitionFactory.eINSTANCE.createIndicatorDefinitionParameter();
                ip.setKey("paraKey");//$NON-NLS-1$
                ip.setValue("paraValue");//$NON-NLS-1$
                tempParameters.add(ip);
                if (parameterTableViewer != null) {
                    parameterTableViewer.refresh(tempParameters);
                    setDirty(true);
                }
            }
        });
        final Button romoveButton = new Button(composite, SWT.NONE);
        romoveButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        romoveButton.setToolTipText(REMOVE_BUTTON_TEXT);
        GridData reGd = new GridData();
        reGd.horizontalAlignment = SWT.LEFT;
        reGd.widthHint = 65;
        romoveButton.setLayoutData(reGd);
        romoveButton.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                IStructuredSelection selection = (IStructuredSelection) parameterTableViewer.getSelection();
                Object o = selection.getFirstElement();
                if (o instanceof IndicatorDefinitionParameter) {
                    element = (IndicatorDefinitionParameter) o;
                    tempParameters.remove(element);
                    parameterTableViewer.refresh(tempParameters);
                    setDirty(true);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#createDefinitionComp()
     */
    @Override
    protected Composite createDefinitionComp() {
        Composite comp = super.createDefinitionComp();
        createNewLineForJavaLanguage();
        return comp;
    }

    /**
     * create a New Line for Java language.
     */
    private void createNewLineForJavaLanguage() {
        // MOD klliu 2011-08-08 bug 22994: Headers are wrong for Java option in indicator editor.
        if (javaLanguageComp == null && checkContainsJavaDefinition()) {
            createJavaTitleComp();
        }
        // ~
        EList<TaggedValue> tvs = getCurrentModelElement().getTaggedValue();
        String classNameStr = null;
        String jarPathStr = PluginConstant.EMPTY_STRING;
        for (TaggedValue tv : tvs) {
            if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                classNameStr = tv.getValue();
                continue;
            }
            if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                jarPathStr = tv.getValue();
            }
        }
        if (classNameStr == null) {
            return;
        }

        final Composite lineComp = new Composite(javaLanguageComp, SWT.NONE);
        lineComp.setLayout(new GridLayout(2, false));

        final CCombo javaCombo = createJavaComboBox(lineComp);
        javaCombo.setText(PatternLanguageType.JAVA.getName());

        putTdExpressToTempMap(javaCombo,
                BooleanExpressionHelper.createTdExpression(PatternLanguageType.findLanguageByName(javaCombo.getText()), null));
        final Composite detailComp = new Composite(javaCombo.getParent(), SWT.NONE);
        widgetMap.put(javaCombo, detailComp);
        detailComp.setLayout(new GridLayout(4, false));

        final Text jarPathText = createJarPathText(detailComp);
        jarPathText.setText(jarPathStr);
        jarPathText.addModifyListener(new NeedToSetDirtyListener());

        final Text classNameText = createClassNameText(detailComp, jarPathText);
        classNameText.setText(classNameStr);
        classNameText.addModifyListener(new NeedToSetDirtyListener());

        createEditButtonForJavaLine(detailComp, jarPathText, classNameText);

        javaCombo.setData(TaggedValueHelper.CLASS_NAME_TEXT, classNameText);
        javaCombo.setData(TaggedValueHelper.JAR_FILE_PATH, jarPathText);

        createExpressionDelButton(detailComp, javaCombo);

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(detailComp);
    }

    /**
     * createJavaComboBox used for create/update java line.
     * 
     * @param lineComp
     * @return
     */
    private CCombo createJavaComboBox(final Composite lineComp) {
        final CCombo javaCombo = new CCombo(lineComp, SWT.BORDER);
        javaCombo.setLayoutData(new GridData());
        ((GridData) javaCombo.getLayoutData()).widthHint = 150;
        javaCombo.setEditable(false);
        javaCombo.setItems(allDBTypeList.toArray(new String[allDBTypeList.size()]));
        javaCombo.addSelectionListener(new LangCombSelectionListener());
        return javaCombo;
    }

    /**
     * create JarPath Text used for create/update java line.
     * 
     * @param detailComp
     * @return
     */
    private Text createJarPathText(final Composite detailComp) {
        final Text jarPathText = new Text(detailComp, SWT.BORDER);
        jarPathText.setLayoutData(new GridData(GridData.FILL_BOTH));
        ((GridData) jarPathText.getLayoutData()).widthHint = 350;
        return jarPathText;
    }

    /**
     * update Line For Java Language.
     * 
     * @param combo
     */
    private void updateLineForJava(final CCombo combo) {
        if (javaTitleComp == null || javaTitleComp.isDisposed()) {
            createJavaTitleComp();
        }
        final Composite lineComp = new Composite(javaLanguageComp, SWT.NONE);
        lineComp.setLayout(new GridLayout(2, false));
        // create a new java combo
        final CCombo javaCombo = createJavaComboBox(lineComp);
        javaCombo.select(combo.getSelectionIndex());
        // ~~~~
        Composite detailComp = new Composite(lineComp, SWT.NONE);
        detailComp.setLayout(new GridLayout(5, false));

        final Text jarPathText = createJarPathText(detailComp);
        jarPathText.addModifyListener(new NeedToSetDirtyListener());

        final Text classNameText = createClassNameText(detailComp, jarPathText);
        classNameText.addModifyListener(new NeedToSetDirtyListener());
        classNameText.addModifyListener(new ExpressTextModListener(javaCombo));

        createEditButtonForJavaLine(detailComp, jarPathText, classNameText);

        javaCombo.setData(TaggedValueHelper.CLASS_NAME_TEXT, classNameText);
        javaCombo.setData(TaggedValueHelper.JAR_FILE_PATH, jarPathText);

        createExpressionDelButton(detailComp, javaCombo);

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(detailComp);
        detailComp.getParent().layout();
        // remove the data base combo in the widgetMap then add new javacombo
        widgetMap.put(javaCombo, detailComp);
        widgetMap.remove(combo);
        removeFromTempMap(combo);
        TdExpression expression = tempExpressionMap.get(combo);
        if (expression == null) {
            expression = BooleanExpressionHelper.createTdExpression(PatternLanguageType.findLanguageByName(javaCombo.getText()),
                    null);
            putTdExpressToTempMap(javaCombo, expression);
        }
        combo.getParent().dispose();

        if (dataBaseComp.getChildren().length == 1) {
            Control[] children = dataBaseComp.getChildren();
            children[0].dispose();
        }
        definitionSection.setExpanded(false);
        definitionSection.setExpanded(true);
    }

    /**
     * create Edit Button For Java Line .
     * 
     * @param detailComp
     * @param jarPathText
     * @param classNameText
     */
    private void createEditButtonForJavaLine(Composite detailComp, final Text jarPathText, final Text classNameText) {
        Button button = new Button(detailComp, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.FILL_BOTH));
        button.setText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.editExpression")); //$NON-NLS-1$
        button.setToolTipText(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.editExpression")); //$NON-NLS-1$
        // ((GridData) button.getLayoutData()).widthHint = 100;
        button.addSelectionListener(new SelectionAdapter() {

            // MOD by zshen for bug 18724 2011.02.23
            @Override
            public void widgetSelected(SelectionEvent e) {
                // MOD msjian 2011-8-9 TDQ-3199 fixed: define a new method use the exsit sourse, because there are two
                // places used the same sourse
                openJarSelectDialog(jarPathText, classNameText);
            }
        });
    }

    /**
     * create ClassName Text.
     * 
     * @param detailComp
     * @param jarPathText
     * @return
     */
    private Text createClassNameText(Composite detailComp, final Text jarPathText) {
        final Text classNameText = new Text(detailComp, SWT.BORDER);
        classNameText.setLayoutData(new GridData(GridData.FILL_BOTH));
        ((GridData) classNameText.getLayoutData()).widthHint = 250;
        // MOD klliu 2010-05-31 13451: Class name of Java User Define Indicator must be validated
        classNameText.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                classNameForSave = classNameText.getText().toString();
                jarPathForSave = jarPathText.getText().toString();
            }
        });
        return classNameText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#updateDatabaseLineForJava(org.eclipse
     * .swt.custom.CCombo, org.talend.cwm.relational.TdExpression)
     */
    @Override
    protected void updateDatabaseLineForJava(final CCombo combo, TdExpression expression) {
        Composite detailComp;
        final Composite lineComp = new Composite(dataBaseComp, SWT.NONE);
        lineComp.setLayout(new GridLayout(3, false));
        final CCombo dataBaseCombo = createJavaComboBox(lineComp);
        dataBaseCombo.select(combo.getSelectionIndex());
        putTdExpressToTempMap(dataBaseCombo, expression);
        detailComp = new Composite(lineComp, SWT.NONE);
        detailComp.setLayout(new GridLayout(4, false));
        widgetMap.put(dataBaseCombo, detailComp);
        createDataBaseLineComponent(dataBaseCombo, expression, detailComp);
        // remove the combo in the widgetMap then add new javacombo
        widgetMap.remove(combo);

        removeFromTempMap(combo);
        // line comp dispose
        combo.getParent().dispose();
        // java title dispose
        if (javaLanguageComp.getChildren().length == 1) {
            Control[] children = javaLanguageComp.getChildren();
            children[0].dispose();
        }
    }

    /**
     * open Jar Select Dialog(TDQ-3199 fixed: Make it convenient to delete the jar which is used already).
     * 
     * @param jarPathText
     * @param classNameText
     */
    private void openJarSelectDialog(Text jarPathText, Text classNameText) {
        String jarpathStr = jarPathText.getText();
        JavaUdiJarSelectDialog selectDialog = UDIUtils.createUdiJarCheckedTreeSelectionDialog(getCurrentModelElement(),
                ResourceManager.getUDIJarFolder(), jarpathStr.split("\\|\\|"));//$NON-NLS-1$
        selectDialog.setControls(jarPathText, classNameText);
        if (Window.OK == selectDialog.open()) {
            classNameText.setText(selectDialog.getSelectResult());
        }
        // MOD klliu 2010-05-31 13451: Class name of Java User Define Indicator must be validated
        validateJavaUDI(classNameText, jarPathText);
        ProxyRepositoryManager.getInstance().save();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#disposeExpressionChild()
     */
    @Override
    protected void disposeExpressionChild() {
        int languageLength = javaLanguageComp == null ? 0 : javaLanguageComp.getChildren().length;
        int dataBaseLength = dataBaseComp == null ? 0 : dataBaseComp.getChildren().length;
        if (languageLength == 0 && dataBaseLength == 0) {
            createDatabaseTitleComp();
        }
        if (languageLength == 1) {
            Control[] children = javaLanguageComp.getChildren();
            children[0].dispose();
        }
    }

    /**
     * create Java Title Composite.
     * 
     */
    private void createJavaTitleComp() {
        // MOD klliu 2011-07-09 bug 22994: Headers are wrong for Java option in indicator editor.
        javaLanguageComp = new Composite(expressionComp, SWT.NONE);
        javaLanguageComp.setLayout(new GridLayout());
        javaLanguageComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        javaTitleComp = new Composite(javaLanguageComp, SWT.NONE);
        javaTitleComp.setLayout(new GridLayout(3, false));

        // language Label
        Label languageLabel = new Label(javaTitleComp, SWT.NONE);
        languageLabel.setText(LANGUAGE);
        languageLabel.setLayoutData(new GridData());
        ((GridData) languageLabel.getLayoutData()).widthHint = 160;

        // jar Label
        Label jarLabel = new Label(javaTitleComp, SWT.NONE);
        jarLabel.setText(JARS);
        jarLabel.setLayoutData(new GridData(GridData.BEGINNING));
        ((GridData) jarLabel.getLayoutData()).widthHint = 355;

        // class Label
        Label classLabel = new Label(javaTitleComp, SWT.NONE);
        classLabel.setText(JAVA_CLASS);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(javaTitleComp);
    }

    /**
     * Class name of Java User Define Indicator must be validated
     * 
     * @param classNameText
     * @param jarPathText
     */
    private void validateJavaUDI(Text classNameText, Text jarPathText) {
        // if (!isSystemIndicator()) {
        classNameForSave = classNameText.getText().toString();
        jarPathForSave = jarPathText.getText().toString();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#doDeleteOnlyForJava()
     */
    @Override
    protected void doDeleteOnlyForJava() {
        classNameForSave = ""; //$NON-NLS-1$
        jarPathForSave = ""; //$NON-NLS-1$
    }

    /**
     * create Definiation Parameter Columns.
     * 
     */
    private void createDefiniationParameterColumns() {

        String[] titles = { PARAMETERS_KEY, PARAMETERS_VALUE };
        int[] bounds = { 200, 200 };
        for (int i = 0; i < titles.length; i++) {
            TableViewerColumn column = new TableViewerColumn(parameterTableViewer, SWT.NONE);
            column.getColumn().setText(titles[i]);
            column.getColumn().setWidth(bounds[i]);
            column.getColumn().setResizable(false);
            column.getColumn().setMoveable(true);
        }
        Table table = parameterTableViewer.getTable();
        table.setLayout(new FillLayout(SWT.VERTICAL | SWT.V_SCROLL));
        GridData tableData = new GridData(GridData.FILL_VERTICAL);
        tableData.horizontalSpan = 2;
        tableData.heightHint = 150;
        table.setLayoutData(tableData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableLayout tl = new TableLayout();
        tl.addColumnData(new ColumnWeightData(60));
        tl.addColumnData(new ColumnWeightData(60));
        table.setLayout(tl);
        attachDefiniationParameterCellEditors(table, titles);
    }

    /**
     * create Category Section.
     */
    private void createCategorySection() {
        categorySection = createSection(form, topComp, CATEGORY_SECTION_TITLE, CATEGORY_SECTION_DESCRIPTION);
        categoryComp = createCategoryComp();
        categorySection.setClient(categoryComp);
    }

    /**
     * create Category composite.
     * 
     * @return
     */
    private Composite createCategoryComp() {
        Composite composite = toolkit.createComposite(categorySection);
        composite.setLayout(new GridLayout(2, false));

        Collection<String> categories = DefinitionHandler.getInstance().getUserDefinedIndicatorCategoryLabels();
        comboCategory = new Combo(composite, SWT.READ_ONLY);
        GridData data = new GridData();
        data.verticalAlignment = GridData.BEGINNING;
        comboCategory.setLayoutData(data);
        comboCategory.setItems(categories.toArray(new String[categories.size()]));
        if (categories.size() > 0 && category == null) {
            category = DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory();
        }
        comboCategory.setText(InternationalizationUtil.getCategoryInternationalizationLabel(category.getLabel()));
        comboCategory.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (needConfirm) {
                    // TDQ-13543: we always do change the category
                    setDirty(true);
                    category = DefinitionHandler.getInstance().getIndicatorCategoryByLabel(comboCategory.getText());
                    updateIndicatorCategoryDetail();

                    if (dataBaseComp != null) {
                        Control[] children = dataBaseComp.getChildren();
                        // TDQ-13543: only when have definitions, confirm whether lost them
                        if (children != null && children.length > 0) {

                            boolean openQuestion = MessageDialogWithToggle.openQuestion(Display.getCurrent().getActiveShell(),
                                    DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.changeCategoryTitle"), //$NON-NLS-1$
                                    DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.changeCategory")); //$NON-NLS-1$
                            if (openQuestion) {
                                // clear all the temp maps except the java type
                                removeFromTempMapsExceptJava();

                                for (Control con : children) {
                                    con.dispose();
                                }

                                definitionSection.setExpanded(false);
                                definitionSection.setExpanded(true);
                            }
                        }
                    }
                }
            }

        });

        // ADD yyi 2009-09-23 Feature 9059
        createIndicatorCategoryDetail(composite);
        updateIndicatorCategoryDetail();
        return composite;
    }

    /**
     * update Indicator Category Detail(yyi 2009-09-23 Feature 9059).
     */
    protected void updateIndicatorCategoryDetail() {
        String categoryLabel = comboCategory.getText();
        if (StringUtils.isNotBlank(categoryLabel)) {
            IndicatorCategory ic = DefinitionHandler.getInstance().getIndicatorCategoryByLabel(categoryLabel);
            if (ic != null) {
                String purposeString = PluginConstant.EMPTY_STRING;
                String descriptionString = PluginConstant.EMPTY_STRING;
                for (TaggedValue value : ic.getTaggedValue()) {
                    if ("Purpose".equals(value.getTag())) {//$NON-NLS-1$
                        purposeString = DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.Purpose") + value.getValue();//$NON-NLS-1$
                    } else if ("Description".equals(value.getTag())) { //$NON-NLS-1$
                        descriptionString = DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.Description")//$NON-NLS-1$
                                + value.getValue();
                    }
                }
                labelDetail.setText(purposeString + System.getProperty("line.separator") + System.getProperty("line.separator")//$NON-NLS-1$//$NON-NLS-2$
                        + descriptionString);
            }
        }
    }

    /**
     * create Indicator Category Detail.
     * 
     * @param composite
     */
    private void createIndicatorCategoryDetail(Composite composite) {
        Composite compoDetail = new Composite(composite, SWT.NONE);
        compoDetail.setLayout(new GridLayout(1, false));
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 150;
        data.widthHint = 300;
        compoDetail.setLayoutData(data);

        labelDetail = new Label(compoDetail, SWT.WRAP);
        labelDetail.setLayoutData(data);
    }

    /**
     * attach Definiation Parameter Cell Editors.
     * 
     * @param table
     * @param titles
     */
    private void attachDefiniationParameterCellEditors(Composite table, String[] titles) {
        parameterTableViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return true;
            }

            public Object getValue(Object element, String property) {
                if (PARAMETERS_KEY.equals(property)) {
                    return ((IndicatorDefinitionParameter) element).getKey();
                } else if (PARAMETERS_VALUE.equals(property)) {
                    return ((IndicatorDefinitionParameter) element).getValue();
                }
                return null;
            }

            public void modify(Object element, String property, Object value) {
                TableItem tableItem = (TableItem) element;
                IndicatorDefinitionParameter data = (IndicatorDefinitionParameter) tableItem.getData();
                if (PARAMETERS_KEY.equals(property)) {
                    if (!data.getKey().equals(value.toString())) {
                        data.setKey(value.toString());
                        parameterTableViewer.refresh(data);
                        setDirty(true);
                    }
                } else if (PARAMETERS_VALUE.equals(property)) {
                    if (!data.getValue().equals(value)) {
                        data.setValue((String) value);
                        parameterTableViewer.refresh(data);
                        setDirty(true);
                    }
                }
            }
        });
        parameterTableViewer.setColumnProperties(titles);
        parameterTableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) });
    }

    @Override
    public void putTdExpressToTempMap(final CCombo combo, final TdExpression expression) {
        super.putTdExpressToTempMap(combo, expression);
        UDIndicatorDefinition definition2 = (UDIndicatorDefinition) getCurrentModelElement();
        if (IndicatorCategoryHelper.isUserDefMatching(category)) {
            tempViewValidRowsExpressionMap = setToTempMap(expression, combo, definition2.getViewValidRowsExpression(),
                    tempViewValidRowsExpressionMap);

            tempViewInvalidRowsExpressionMap = setToTempMap(expression, combo, definition2.getViewInvalidRowsExpression(),
                    tempViewInvalidRowsExpressionMap);

            tempViewValidValuesExpressionMap = setToTempMap(expression, combo, definition2.getViewValidValuesExpression(),
                    tempViewValidValuesExpressionMap);

            tempViewInvalidValuesExpressionMap = setToTempMap(expression, combo, definition2.getViewInvalidValuesExpression(),
                    tempViewInvalidValuesExpressionMap);
        } else {
            // get view rows tdExpress list, and set currect tdexpress to temp map
            tempViewRowsExpressionMap = setToTempMap(expression, combo, definition2.getViewRowsExpression(),
                    tempViewRowsExpressionMap);
        }
    }

    @Override
    public void removeFromTempMap(final CCombo combo) {
        super.removeFromTempMap(combo);
        if (getCurrentModelElement() instanceof UDIndicatorDefinition) {

            if (IndicatorCategoryHelper.isUserDefMatching(category)) {
                tempViewValidRowsExpressionMap.remove(combo);
                tempViewInvalidRowsExpressionMap.remove(combo);
                tempViewValidValuesExpressionMap.remove(combo);
                tempViewInvalidValuesExpressionMap.remove(combo);

            } else {
                tempViewRowsExpressionMap.remove(combo);
            }
        }
    }

    @Override
    protected ExpressionEditDialog initExpresstionEditDialog(final CCombo combo, final String version, String patternText) {
        TdExpression tdExpression = getTdExpression(combo, version);
        String language = tdExpression.getLanguage();

        ExpressionEditDialog editDialog = new ExpressionEditDialog(null, patternText, true, cloneExpression(tdExpression));
        editDialog.setVersion(version);
        editDialog.setLanguage(language);
        editDialog.setCategory(category);
        if (IndicatorCategoryHelper.isUserDefMatching(category)) {
            EList<TdExpression> viewValidRowsExpression = ((UDIndicatorDefinition) getCurrentModelElement())
                    .getViewValidRowsExpression();
            TdExpression viewValidRows = getCurrentLanguageExp(viewValidRowsExpression, language, version);
            if (isDirty()) {
                viewValidRows = tempViewValidRowsExpressionMap.get(combo);
            }
            editDialog.setTempViewValidRowsExp(cloneExpression(viewValidRows));

            EList<TdExpression> viewInvalidRowsExpression = ((UDIndicatorDefinition) getCurrentModelElement())
                    .getViewInvalidRowsExpression();
            TdExpression viewInvalidRows = getCurrentLanguageExp(viewInvalidRowsExpression, language, version);
            if (isDirty()) {
                viewInvalidRows = tempViewInvalidRowsExpressionMap.get(combo);
            }
            editDialog.setTempViewInvalidRowsExp(cloneExpression(viewInvalidRows));

            EList<TdExpression> viewValidValuesExpression = ((UDIndicatorDefinition) getCurrentModelElement())
                    .getViewValidValuesExpression();
            TdExpression viewValidValues = getCurrentLanguageExp(viewValidValuesExpression, language, version);
            if (isDirty()) {
                viewValidValues = tempViewValidValuesExpressionMap.get(combo);
            }
            editDialog.setTempViewValidValuesExp(cloneExpression(viewValidValues));

            EList<TdExpression> viewInvalidValuesExpression = ((UDIndicatorDefinition) getCurrentModelElement())
                    .getViewInvalidValuesExpression();
            TdExpression viewInvalidValues = getCurrentLanguageExp(viewInvalidValuesExpression, language, version);
            if (isDirty()) {
                viewInvalidValues = tempViewInvalidValuesExpressionMap.get(combo);
            }
            editDialog.setTempViewInvalidValuesExp(cloneExpression(viewInvalidValues));

        } else {
            EList<TdExpression> viewRowsExpression = ((UDIndicatorDefinition) getCurrentModelElement()).getViewRowsExpression();
            TdExpression viewRows = getCurrentLanguageExp(viewRowsExpression, language, version);
            if (isDirty()) {
                viewRows = tempViewRowsExpressionMap.get(combo);
            }
            editDialog.setTempViewRowsExp(cloneExpression(viewRows));
        }
        return editDialog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#handleSelectExpression(org.eclipse
     * .swt.custom.CCombo, org.talend.dataprofiler.core.ui.dialog.ExpressionEditDialog)
     */
    @Override
    protected void handleSelectExpression(final CCombo combo, final ExpressionEditDialog editDialog) {
        super.handleSelectExpression(combo, editDialog);
        if (IndicatorCategoryHelper.isUserDefMatching(category)) {
            tempViewValidRowsExpressionMap.put(combo, editDialog.getTempViewValidRowsExp());
            tempViewInvalidRowsExpressionMap.put(combo, editDialog.getTempViewInvalidRowsExp());
            tempViewValidValuesExpressionMap.put(combo, editDialog.getTempViewValidValuesExp());
            tempViewInvalidValuesExpressionMap.put(combo, editDialog.getTempViewInvalidValuesExp());

        } else {
            // get view rows tdExpress list, and set currect tdexpress to temp map
            tempViewRowsExpressionMap.put(combo, editDialog.getTempViewRowsExp());
        }
    }

    @Override
    protected void saveUDIValues(CCombo javaUDICombo) {
        // save some other related values, current only need in UDI
        saveUDIExpression();

        saveTaggedValues(javaUDICombo);

        // Save difinition UDI Parameters
        saveDefinitionParameters();
    }

    /**
     * save UDI Expressions.
     */
    private void saveUDIExpression() {
        UDIndicatorDefinition def = (UDIndicatorDefinition) getCurrentModelElement();
        EList<TdExpression> viewValidRowsExpression = def.getViewValidRowsExpression();
        viewValidRowsExpression.clear();
        viewValidRowsExpression = saveFromTempMapToDefinition(viewValidRowsExpression, tempViewValidRowsExpressionMap);

        EList<TdExpression> viewInvalidRowsExpression = def.getViewInvalidRowsExpression();
        viewInvalidRowsExpression.clear();
        viewInvalidRowsExpression = saveFromTempMapToDefinition(viewInvalidRowsExpression, tempViewInvalidRowsExpressionMap);

        EList<TdExpression> viewValidValuesExpression = def.getViewValidValuesExpression();
        viewValidValuesExpression.clear();
        viewValidValuesExpression = saveFromTempMapToDefinition(viewValidValuesExpression, tempViewValidValuesExpressionMap);

        EList<TdExpression> viewInvalidValuesExpression = def.getViewInvalidValuesExpression();
        viewInvalidValuesExpression.clear();
        viewInvalidValuesExpression = saveFromTempMapToDefinition(viewInvalidValuesExpression, tempViewInvalidValuesExpressionMap);

        EList<TdExpression> viewRowsExpression = def.getViewRowsExpression();
        viewRowsExpression.clear();
        viewRowsExpression = saveFromTempMapToDefinition(viewRowsExpression, tempViewRowsExpressionMap);

        if (category != null) {
            UDIHelper.setUDICategory(getCurrentModelElement(), category);
        }

    }

    /**
     * save TaggedValues.
     * 
     * @param javaUDICombo
     */
    private void saveTaggedValues(CCombo javaUDICombo) {
        // Save Java UDI
        EList<TaggedValue> tvs = getCurrentModelElement().getTaggedValue();
        if (javaUDICombo != null) {
            boolean isNewTaggedValue = true;
            for (TaggedValue tv : tvs) {
                if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                    String newTagValue = ((Text) javaUDICombo.getData(TaggedValueHelper.CLASS_NAME_TEXT)).getText();
                    tv.setValue(newTagValue);
                    isNewTaggedValue = false;
                    continue;
                }
                if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                    String newTagValue = ((Text) javaUDICombo.getData(TaggedValueHelper.JAR_FILE_PATH)).getText();
                    tv.setValue(newTagValue);
                }
            }
            if (isNewTaggedValue) {
                TaggedValue classNameTV = TaggedValueHelper.createTaggedValue(TaggedValueHelper.CLASS_NAME_TEXT,
                        ((Text) javaUDICombo.getData(TaggedValueHelper.CLASS_NAME_TEXT)).getText());
                TaggedValue jarPathTV = TaggedValueHelper.createTaggedValue(TaggedValueHelper.JAR_FILE_PATH,
                        ((Text) javaUDICombo.getData(TaggedValueHelper.JAR_FILE_PATH)).getText());
                getCurrentModelElement().getTaggedValue().add(classNameTV);
                getCurrentModelElement().getTaggedValue().add(jarPathTV);
            }
        } else {
            // Remove Java UDI tagged values if there have.
            TaggedValue tvCN = null;
            TaggedValue tvJARP = null;
            for (TaggedValue tv : tvs) {
                if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                    tvCN = tv;
                    continue;
                }
                if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                    tvJARP = tv;
                }
            }
            if (tvCN != null) {
                tvs.remove(tvCN);
            }
            if (tvJARP != null) {
                tvs.remove(tvJARP);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#isJavaExist(org.eclipse.swt
     * .custom.CCombo)
     */
    @Override
    protected boolean isJavaExist(CCombo combo) {
        if (combo.getText().equals(PatternLanguageType.JAVA.getName())) {
            if (javaLanguageComp != null && checkContainJavaInTempExpressionMap()) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#updateLineAndOtherCombos(org.eclipse
     * .swt.custom.CCombo, org.talend.cwm.relational.TdExpression, java.lang.String)
     */
    @Override
    public void updateLineAndOtherCombos(final CCombo combo, TdExpression expression, String oldLanguage) {
        if (combo.getText().equals(PatternLanguageType.JAVA.getName())) {
            updateLineForJava(combo);
        } else {
            // MOD xqliu 2010-03-23 feature 11201
            updateLineForExpression(combo, expression, oldLanguage);
            // ~11201
        }
        updateOtherCombos(combo);
    }

    /**
     * set ClassName And JarPath For Java.
     */
    private void setClassNameAndJarPathForJava() {
        EList<TaggedValue> tvs = getCurrentModelElement().getTaggedValue();
        if (classNameForSave == null || jarPathForSave == null) {
            for (TaggedValue tv : tvs) {
                if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                    this.classNameForSave = tv.getValue();
                    continue;
                }
                if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                    this.jarPathForSave = tv.getValue();
                }
            }
        }
    }

    /**
     * save Definition Parameters
     * 
     * @param IndicatorDefinition
     */
    private void saveDefinitionParameters() {
        EList<IndicatorDefinitionParameter> params = getCurrentModelElement().getIndicatorDefinitionParameter();
        if (params != null) {
            params.clear();
            params.addAll(tempParameters);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionMaterPage#checkBeforeSave()
     */
    @Override
    protected ReturnCode checkBeforeSave() {
        ReturnCode rc = super.checkBeforeSave();

        if (tempParameters != null) {
            // detecting the IndicatorDefinitionParameter whether include duplicate keywords
            Map<String, Integer> paraMap = new HashMap<String, Integer>();
            for (IndicatorDefinitionParameter para : tempParameters) {
                String key = para.getKey();
                Integer keyCount = paraMap.get(key);
                if (keyCount == null) {
                    paraMap.put(key, Integer.valueOf(1));
                } else {
                    paraMap.put(key, Integer.valueOf(keyCount.intValue() + 1));
                }
            }

            if (paraMap.size() != tempParameters.size()) {
                StringBuffer duplicateKeywords = new StringBuffer();
                for (String key : paraMap.keySet()) {
                    Integer value = paraMap.get(key);
                    if (value.intValue() > 1) {
                        duplicateKeywords.append("\n" + key); //$NON-NLS-1$
                    }
                }
                rc.setOk(false);
                rc.setMessage(DefaultMessagesImpl.getString(
                        "IndicatorDefinitionMaterPage.includeDuplicateKeywords", duplicateKeywords.toString()));//$NON-NLS-1$
                return rc;
            }

            // detecting the IndicatorDefinitionParameter whether include special characters
            for (IndicatorDefinitionParameter para : tempParameters) {
                String key = para.getKey();
                String value = para.getValue();

                if (!StringUtils.isBlank(key)
                        && (key.indexOf(UDIHelper.PARA_SEPARATE_1) > -1 || key.indexOf(UDIHelper.PARA_SEPARATE_2) > -1)) {
                    rc.setOk(false);
                    rc.setMessage(DefaultMessagesImpl
                            .getString(
                                    "IndicatorDefinitionMaterPage.includeSpecialCharacter", "\n" + UDIHelper.PARA_SEPARATE_1 + "\n" + UDIHelper.PARA_SEPARATE_2));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    return rc;
                }

                if (!StringUtils.isBlank(value)
                        && (value.indexOf(UDIHelper.PARA_SEPARATE_1) > -1 || value.indexOf(UDIHelper.PARA_SEPARATE_2) > -1)) {
                    rc.setOk(false);
                    rc.setMessage(DefaultMessagesImpl
                            .getString(
                                    "IndicatorDefinitionMaterPage.includeSpecialCharacter", "\n" + UDIHelper.PARA_SEPARATE_1 + "\n" + UDIHelper.PARA_SEPARATE_2));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    return rc;
                }
            }
        }

        // ADD klliu 2010-06-01 bug 13451: Class name of Java User Define Indicator must be validated
        if (!checkJavaDefinitionBeforeSave()) {
            ((IndicatorEditor) this.getEditor()).setSaveActionButtonState(false);
            rc.setOk(false);
            rc.setMessage(DefaultMessagesImpl.getString("IndicatorDefinitionMaterPage.classPathError"));//$NON-NLS-1$
            return rc;
        }
        return rc;
    }

    /**
     * check Java Definition Before Save.
     * 
     * @return boolean
     */
    private boolean checkJavaDefinitionBeforeSave() {
        boolean isHaveJavaComb = checkIsHaveJavaComb();
        boolean isHaveJavaTag = checkContainsJavaDefinition();
        boolean isHaveSqlExpr = checkIsHaveSqlExpression();

        setClassNameAndJarPathForJava();

        boolean cj2e = isClassNameExistInJars();
        if (isHaveJavaComb == true && isHaveSqlExpr == true && isHaveJavaTag == false) {
            return cj2e;
        } else if (isHaveJavaComb == false && isHaveSqlExpr == true) {
            return true;
        } else if (isHaveJavaComb == true && isHaveSqlExpr == false) {
            return cj2e;
        } else if (isHaveJavaComb == true && isHaveSqlExpr == true) {
            return cj2e;
        }
        return true;

    }

    /**
     * check Contains Java Definition.
     * 
     * @return boolean
     */
    private boolean checkContainsJavaDefinition() {
        EList<TaggedValue> tvs = getCurrentModelElement().getTaggedValue();
        for (TaggedValue tv : tvs) {
            if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT) || tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check Is Have Sql expression.
     * 
     * @return boolean
     */
    private boolean checkIsHaveSqlExpression() {
        EList<TdExpression> expression = getCurrentModelElement().getSqlGenericExpression();
        if (!expression.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * check Is Have Java Comb.
     * 
     * @return boolean
     */
    private boolean checkIsHaveJavaComb() {
        Iterator<CCombo> it = tempExpressionMap.keySet().iterator();
        while (it.hasNext()) {
            CCombo cb = it.next();
            // MOD MOD mzhao feature 11128 Be able to add Java UDI, 2010-01-28
            if (!cb.isDisposed() && cb.getText().equals(PatternLanguageType.JAVA.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * check whether the ClassName Exist In Jars.
     * 
     * @return boolean
     */
    private boolean isClassNameExistInJars() {
        if (classNameForSave != null && jarPathForSave != null && !classNameForSave.trim().equals(PluginConstant.EMPTY_STRING)
                && !jarPathForSave.trim().equals(PluginConstant.EMPTY_STRING)) {
            // MOD by zshen for bug 18724 2011.02.23
            for (IFile file : UDIUtils.getContainJarFile(jarPathForSave)) {
                TalendURLClassLoader cl;
                try {
                    // Note that the 2nd parameter (classloader) is needed to load class UserDefinitionIndicator from
                    // org.talend.dataquality plugin.
                    cl = new TalendURLClassLoader(new URL[] { file.getLocation().toFile().toURI().toURL() },
                            IndicatorDefinitionMaterPage.class.getClassLoader());
                    Class<?> theClass = cl.findClass(classNameForSave);
                    if (theClass != null) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    log.error(e1.getStackTrace());
                } catch (ClassNotFoundException e1) {
                    log.error(e1.getStackTrace());
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
