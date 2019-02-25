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
package org.talend.dataprofiler.core.ui.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataprofiler.core.pattern.actions.CreatePatternAction;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternMasterDetailsPage;
import org.talend.dataprofiler.core.ui.utils.CheckValueUtils;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.pattern.impl.RegularExpressionImpl;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * The View use to test the text whether match the specification regular text.
 */

public class PatternTestView extends ViewPart {

    private static final String NO_DATABASE_SELECTEDED = DefaultMessagesImpl.getString("PatternTestView.database"); //$NON-NLS-1$

    public static final String ID = "org.talend.dataprofiler.core.ui.views.PatternTestView"; //$NON-NLS-1$

    public static final String VIEW_CONTEXT_ID = "org.talend.dataprofiler.core.ui.views.PatternTestView.viewScope"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(PatternTestView.class);

    private CCombo dbCombo;

    private static Text functionNameText;

    private Text testText, regularText;

    Button sqlButton, testButton;

    private Label textAreaLabel, regularLabel;

    Button buttonJava, buttonSql;

    Composite butPane;

    List<IRepositoryNode> listTdDataProviders = new ArrayList<IRepositoryNode>();

    private Button saveButton;

    private Label emoticonLabel;

    private Label resultLabel;

    private Label functionLabel;

    private Pattern pattern;

    private Button createPatternButton;

    private RegularExpression regularExpression;

    private PatternMasterDetailsPage editorPage;

    private boolean isJavaEngine = false;

    private Composite imgCom;

    @Override
    public void createPartControl(final Composite parent) {
        // css
        CoreUIPlugin.setCSSClass(parent, this.getClass().getSimpleName());
        createPatternTestCom(parent);

    }

    protected void createPatternTestCom(final Composite parent) {
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        final Composite mainComposite = new Composite(scrolledComposite, SWT.NULL);
        scrolledComposite.setContent(mainComposite);
        GridLayout blay = new GridLayout();
        mainComposite.setLayout(blay);

        // create coboCom line
        final Composite coboCom = new Composite(mainComposite, SWT.NULL);
        imgCom = new Composite(mainComposite, SWT.NONE);
        final Composite textCom = new Composite(mainComposite, SWT.NULL);
        final Composite buttonsCom = new Composite(mainComposite, SWT.FILL | SWT.NULL);
        mainComposite.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event event) {
                GridData formData = new GridData(GridData.FILL_HORIZONTAL);
                formData.heightHint = 105;
                textCom.setLayoutData(formData);
                buttonsCom.setLayoutData(formData);
            }

        });
        // ~
        // set cobo com layout
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        coboCom.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        // MOD qiongli feature 16799: Add java in Pattern Test View
        buttonJava = new Button(coboCom, SWT.RADIO);
        buttonJava.setText(ExecutionLanguage.JAVA.getLiteral());
        data = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        buttonJava.setLayoutData(data);
        buttonJava.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isJavaEngine = true;
                sqlButton.setEnabled(false);
                dbCombo.setEnabled(false);
                functionNameText.setEnabled(false);
            }

        });
        buttonSql = new Button(coboCom, SWT.RADIO);
        data = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        buttonSql.setText(DefaultMessagesImpl.getString("PatternTestView.Connections")); //$NON-NLS-1$
        buttonSql.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isJavaEngine = false;
                sqlButton.setEnabled(true);
                dbCombo.setEnabled(true);
                functionNameText.setEnabled(true);
            }

        });
        buttonSql.setSelection(true);
        buttonSql.setEnabled(!isJavaEngine);
        buttonSql.setLayoutData(data);
        dbCombo = new CCombo(coboCom, SWT.DROP_DOWN | SWT.BORDER);
        dbCombo.setEditable(false);
        data = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        dbCombo.setLayoutData(data);

        // MOD gdbu 2011-5-31 bug : 19119
        Label dummyLabel = new Label(coboCom, SWT.NONE);
        dummyLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        functionLabel = new Label(coboCom, SWT.NONE);
        functionLabel.setText(DefaultMessagesImpl.getString("PatternTestView.FunctionName"));//$NON-NLS-1$ 
        functionNameText = new Text(coboCom, SWT.BORDER);
        functionNameText.setText(PluginConstant.EMPTY_STRING);

        GridData functionNameTextGD = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
        data = new GridData(GridData.FILL, GridData.BEGINNING, false, false);

        functionNameText.setLayoutData(data);
        functionLabel.setLayoutData(functionNameTextGD);
        functionNameText.setVisible(false);
        functionLabel.setVisible(false);
        // ~19119
        GridData comData = new GridData(GridData.FILL_HORIZONTAL);
        comData.heightHint = 50;
        coboCom.setLayoutData(comData);

        // create image com
        layout = new GridLayout();
        layout.numColumns = 2;
        imgCom.setLayout(layout);

        emoticonLabel = new Label(imgCom, SWT.NONE);
        GridData gd = new GridData();
        gd.heightHint = 18;
        gd.widthHint = 18;
        emoticonLabel.setLayoutData(gd);

        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 18;
        // gd.widthHint = 65;
        // MOD TDQ-4599,mzhao, don't truncate the label.
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = GridData.FILL;
        resultLabel = new Label(imgCom, SWT.NONE);
        resultLabel.setLayoutData(gd);

        GridData imgData = new GridData(GridData.FILL_HORIZONTAL);
        // imgData.heightHint = 0;
        imgData.horizontalAlignment = GridData.FILL;
        imgData.grabExcessHorizontalSpace = true;
        imgCom.setLayoutData(imgData);
        // ~

        // create text com
        layout = new GridLayout();
        layout.numColumns = 2;
        textCom.setLayout(layout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalAlignment = GridData.BEGINNING;
        // MOD gdbu 2011-5-31 bug : 19119
        textAreaLabel = new Label(textCom, SWT.NONE);
        textAreaLabel.setText(DefaultMessagesImpl.getString("PatternTestView.area")); //$NON-NLS-1$
        testText = AutoScrollBarTextFactory.getDefault().createText(textCom, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 40;
        testText.setLayoutData(data);
        testText.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.enterHere")); //$NON-NLS-1$

        GridData textAreaLabelGD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        // textAreaLabelGD.widthHint = 180;
        textAreaLabel.setLayoutData(textAreaLabelGD);

        regularLabel = new Label(textCom, SWT.WRAP);
        regularLabel.setText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameter")); //$NON-NLS-1$ 
        regularLabel.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameter"));//$NON-NLS-1$ 
        GridData regularLabelGD = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        regularLabelGD.heightHint = 40;
        regularLabelGD.widthHint = 70;
        // regularLabelGD.widthHint = 260;
        regularLabel.setLayoutData(regularLabelGD);
        // ~19119

        this.regularText = AutoScrollBarTextFactory.getDefault().createText(textCom, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        this.regularText.setLayoutData(data);
        regularText.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.enterHereAgainst")); //$NON-NLS-1$
        regularText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                boolean enabled = (!regularText.getText().equals(PluginConstant.EMPTY_STRING))
                        && (CheckValueUtils.isPatternValue(regularText.getText()));
                if (pattern != null) {
                    saveButton.setEnabled(enabled);
                }
                createPatternButton.setEnabled(enabled);
            }

        });
        GridData textData = new GridData(GridData.FILL_BOTH);
        textData.heightHint = 400;
        textCom.setLayoutData(textData);
        // ~~~~~~

        GridLayout llayout = new GridLayout();
        llayout.numColumns = 4;
        buttonsCom.setLayout(llayout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 25;
        data.widthHint = 92;
        sqlButton = new Button(buttonsCom, SWT.PUSH);
        sqlButton.setText(DefaultMessagesImpl.getString("PatternTestView.SQL")); //$NON-NLS-1$
        sqlButton.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.generatedSQLStatement")); //$NON-NLS-1$
        sqlButton.setLayoutData(data);
        sqlButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openSQLEditor();
            }
        });
        createPatternButton = new Button(buttonsCom, SWT.PUSH);
        createPatternButton.setText(DefaultMessagesImpl.getString("PatternTestView.createPattern")); //$NON-NLS-1$
        createPatternButton.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.CreateNewPattern")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 25;
        data.widthHint = 92;
        createPatternButton.setLayoutData(data);
        createPatternButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String language = null;
                if (regularExpression != null) {
                    language = regularExpression.getExpression().getLanguage();
                } else {
                    DbmsLanguage dbmsLanguage = getDbmsLanguage();
                    // MOD gdbu 2011-5-13 bug : 19119
                    if (dbmsLanguage != null) {
                        dbmsLanguage.setRegularExpressionFunction(getFunctionName());
                        language = dbmsLanguage.getDbmsName();
                    } else {
                        // ADD msjian 2012-2-10 TDQ-4600: fix set the default language is "Java"
                        language = SupportDBUrlType.JAVADEFAULTURL.getDBKey();
                        // TDQ-4600~
                    }
                    // ~19119
                }
                // MOD mzhao 2009-03-13 Feature 6066 Move all folders into one
                // project.
                // MOD mzhao TDQ-4734 set relative customer pattern folder, same level to the pattern to be replicated.
                IFolder customizedPattFolder = ResourceManager.getPatternRegexFolder();
                if (pattern != null && pattern.eResource() != null) {
                    IContainer oriPattContainer = WorkspaceUtils.getModelElementResource(pattern).getParent();
                    if (oriPattContainer instanceof IFolder) {
                        customizedPattFolder = (IFolder) oriPattContainer;
                    }
                }
                new CreatePatternAction(customizedPattFolder, ExpressionType.REGEXP, regularText.getText(), language).run();
            }
        });
        createPatternButton.setEnabled(false);
        saveButton = new Button(buttonsCom, SWT.PUSH);
        saveButton.setText(DefaultMessagesImpl.getString("PatternTestView.Save")); //$NON-NLS-1$
        saveButton.setEnabled(false);
        saveButton.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.SaveRegularExpression")); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 25;
        data.widthHint = 92;
        saveButton.setLayoutData(data);
        saveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                savePattern();
            }
        });
        testButton = new Button(buttonsCom, SWT.PUSH);
        data = new GridData(GridData.FILL_HORIZONTAL);
        testButton.setLayoutData(data);
        testButton.setText(DefaultMessagesImpl.getString("PatternTestView.test")); //$NON-NLS-1$
        testButton.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.ValidateEnteredString")); //$NON-NLS-1$
        testButton.setLayoutData(data);
        testButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                testRegularText();
            }

        });
        // MOD gdbu TDQ-4153 2011-12-19 Initialize the position error of composite.
        expandImageComposite();
        // ~TDQ-4153
        data = new GridData(GridData.FILL_HORIZONTAL);
        buttonsCom.setLayoutData(data);
        scrolledComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        mainComposite.layout();
        activateContext();

        // MOD gdbu 2011-5-31 bug : 19119
        fillComboData();
        // ~19119
    }

    /**
     * DOC klliu Comment method "expandImageComposite".
     * 
     * @param expand
     */
    protected void expandImageComposite() {
        imgCom.getParent().layout();
    }

    /**
     * Activate a context that this view uses. It will be tied to this view activation events and will be removed when
     * the view is disposed.
     */
    private void activateContext() {
        IContextService contextService = (IContextService) getSite().getService(IContextService.class);
        contextService.activateContext(VIEW_CONTEXT_ID);

        TestRegularAction testRegularAction = new TestRegularAction();
        IHandlerService service = (IHandlerService) getViewSite().getService(IHandlerService.class);
        service.activateHandler(testRegularAction.getActionDefinitionId(), new ActionHandler(testRegularAction));
    }

    /**
     * DOC rli PatternTestView class global comment. Detailled comment
     */
    public class TestRegularAction extends Action {

        public TestRegularAction() {
            this.setActionDefinitionId("org.talend.dataprofiler.core.testRegular"); //$NON-NLS-1$
        }

        @Override
        public void run() {
            testRegularText();
        }
    }

    /**
     * Test the text by the regular text of regularText.
     */
    private void testRegularText() {
        // MOD qiongli 2011-1-7.Add java in Pattern Test View
        if (isJavaEngine) {
            String regexStr = regularText.getText();
            if (regexStr.length() >= 2 && regexStr.startsWith("'") && regexStr.endsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
                regexStr = regexStr.substring(1, regexStr.length() - 1);
            }
            try {
                boolean flag = java.util.regex.Pattern.compile(regexStr).matcher(testText.getText()).find();
                if (flag) {
                    emoticonLabel.setImage(ImageLib.getImage(ImageLib.CHECK_MARK_PNG));
                    resultLabel.setText(DefaultMessagesImpl.getString("PatternTestView.Match")); //$NON-NLS-1$
                    return;
                } else {
                    emoticonLabel.setImage(ImageLib.getImage(ImageLib.RED_WARNING_PNG));
                    resultLabel.setText(DefaultMessagesImpl.getString("PatternTestView.nonMatch")); //$NON-NLS-1$
                    return;
                }
            } catch (java.util.regex.PatternSyntaxException e) {// TDQ-5650 show the error message if any exception
                emoticonLabel.setImage(ImageLib.getImage(ImageLib.ICON_ERROR_INFO));
                resultLabel.setText(e.getLocalizedMessage());
                return;
            }
        } else {
            for (IRepositoryNode connRepNode : listTdDataProviders) {
                ConnectionItem connItem = (ConnectionItem) connRepNode.getObject().getProperty().getItem();
                Connection tddataprovider = connItem.getConnection();
                if (tddataprovider.getName().equals(dbCombo.getText())) {
                    DbmsLanguage createDbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(tddataprovider);
                    String selectRegexpTestString = null;
                    // MOD gdbu 2011-5-31 bug : 19119
                    if (null != createDbmsLanguage) {
                        createDbmsLanguage.setRegularExpressionFunction(getFunctionName());
                        selectRegexpTestString = createDbmsLanguage.getSelectRegexpTestString(testText.getText(),
                                regularText.getText());
                    }
                    // ~19119

                    // TDQ-8637 if the 'selectRegexpTestString' is null,means doesn't implement method
                    // DbmsLanguage.regexLike().should return.
                    if (selectRegexpTestString == null) {
                        MessageDialog.openInformation(DisplayUtils.getDefaultShell(false),
                                DefaultMessagesImpl.getString("PatternTestView.NoSupportTitle"), //$NON-NLS-1$
                                DefaultMessagesImpl.getString("PatternTestView.NoSupportPatternTest")); //$NON-NLS-1$
                        return;
                    }
                    TypedReturnCode<java.sql.Connection> rcConn = JavaSqlFactory.createConnection(tddataprovider);
                    Statement createStatement = null;
                    try {
                        if (!rcConn.isOk()) {
                            throw new DataprofilerCoreException(rcConn.getMessage());
                        }
                        java.sql.Connection connection = rcConn.getObject();
                        createStatement = connection.createStatement();
                        ResultSet resultSet = createStatement.executeQuery(selectRegexpTestString);
                        while (resultSet.next()) {
                            String okString = resultSet.getString(1);
                            // MOD msjian 2011-11-15 TDQ-3967: in the postgres db, the match return value is "t"
                            if ("1".equalsIgnoreCase(okString) //$NON-NLS-1$
                                    || (createDbmsLanguage != null && (createDbmsLanguage.getDbmsName().equals(
                                            SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage()) && "t".equalsIgnoreCase(okString)))) { //$NON-NLS-1$
                                // TDQ-3967 ~
                                emoticonLabel.setImage(ImageLib.getImage(ImageLib.CHECK_MARK_PNG));
                                resultLabel.setText(DefaultMessagesImpl.getString("PatternTestView.Match")); //$NON-NLS-1$
                                return;
                            }
                        }
                        emoticonLabel.setImage(ImageLib.getImage(ImageLib.RED_WARNING_PNG));
                        resultLabel.setText(DefaultMessagesImpl.getString("PatternTestView.nonMatch")); //$NON-NLS-1$
                        return;
                    } catch (Exception exception) {
                        log.error(exception, exception);
                        // bug TDQ-2066-->TDQ-3594 for mysql
                        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                        MessageBoxExceptionHandler.process(exception, shell);
                        // ~
                        emoticonLabel.setImage(null);
                        return;
                    } finally {
                        if (createStatement != null) {
                            try {
                                createStatement.close();
                            } catch (SQLException e) {
                                // do nothing until now
                            }
                        }
                        ConnectionUtils.closeConnection(rcConn.getObject());
                    }
                }
            }
        }

        MessageDialog.openWarning(DisplayUtils.getDefaultShell(false), "", NO_DATABASE_SELECTEDED); //$NON-NLS-1$
    }

    /**
     * Set the pattern and regularExpression value to the corresponding field, it can be called after the control
     * created.
     * 
     * @param pattern
     * @param regularExpression
     */
    public void setPatternExpression(PatternMasterDetailsPage editorPage, Pattern editorPattern,
            RegularExpression regularExpression) {
        this.editorPage = editorPage;
        pattern = editorPattern;
        this.regularExpression = regularExpression;
        String body = regularExpression.getExpression().getBody();
        this.regularText.setText(body == null ? "" : body); //$NON-NLS-1$
        this.saveButton.setEnabled(true);
        this.createPatternButton.setEnabled(true);

        if (regularExpression != null) {
            String language = regularExpression.getExpression().getLanguage();

            isJavaEngine = StringUtils.equalsIgnoreCase(language, PatternLanguageType.JAVA.getName());

            buttonJava.setSelection(isJavaEngine);
            buttonSql.setEnabled(!isJavaEngine);
            buttonSql.setSelection(!isJavaEngine);
            dbCombo.setEnabled(buttonSql.getSelection());

        }
    }

    public String getRegularText() {
        return this.regularText.getText();
    }

    @Override
    public void setFocus() {

    }

    /**
     * DOC rli Comment method "openSQLEditor".
     */
    private void openSQLEditor() {
        DbmsLanguage dbmsLanguage = this.getDbmsLanguage();
        if (dbmsLanguage != null) {
            // MOD gdbu 2011-6-13 bug : 19119
            dbmsLanguage.setRegularExpressionFunction(getFunctionName());
            // ~19119
            String selectRegexpTestString = dbmsLanguage.getSelectRegexpTestString(testText.getText(), regularText.getText());

            // MOD sizhaoliu TDQ-5237 changed the way to open SQL Explorer for Pattern Test View
            SqlExplorerUtils.getDefault().runInDQViewer(getDBConnectionFromDBName(dbCombo.getText()), selectRegexpTestString,
                    SqlExplorerUtils.SQLEDITOR_ID);
        } else {
            MessageDialog.openWarning(DisplayUtils.getDefaultShell(false), "", NO_DATABASE_SELECTEDED); //$NON-NLS-1$
        }

    }

    /**
     * If the pattern is not null, will save it and update the corresponding pattern editor content.
     */
    private void savePattern() {
        // If the pattern is not null, will update the pattern editor content.
        if (pattern != null) {
            String expressionLanguage = this.regularExpression.getExpression().getLanguage();
            DbmsLanguage dbmsLanguage = this.getDbmsLanguage();
            // MOD gdbu 2011-5-31 bug : 19119
            // MOD gdbu 2011-6-13 bug : 21695
            if (null != dbmsLanguage) {
                dbmsLanguage.setRegularExpressionFunction(getFunctionName());
            }
            // ~21695
            // ~19119
            // MOD qiongli 2011-1-7 featrue 16799.
            boolean isLanguageMatched = false;
            if (isJavaEngine && expressionLanguage.equals(ExecutionLanguage.JAVA.getLiteral()) || dbmsLanguage != null
                    && (dbmsLanguage.getDbmsName().equalsIgnoreCase(expressionLanguage))) {
                isLanguageMatched = true;
            }
            if (!isLanguageMatched) {
                String messageInfo = DefaultMessagesImpl
                        .getString(
                                "PatternTestView.modifiedTheRegularExpression", expressionLanguage, dbmsLanguage.getDbmsName(), expressionLanguage, expressionLanguage, dbmsLanguage.getDbmsName()); //$NON-NLS-1$
                MessageDialog messageDialog = new MessageDialog(DisplayUtils.getDefaultShell(false),
                        DefaultMessagesImpl.getString("PatternTestView.warning"), null, messageInfo, MessageDialog.WARNING, //$NON-NLS-1$
                        new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
                int result = messageDialog.open();
                if (result == MessageDialog.OK) {
                    regularExpression.getExpression().setBody(regularText.getText());
                } else {
                    EList<PatternComponent> components = this.pattern.getComponents();
                    boolean isContainLanguage = false;
                    for (int i = 0; i < components.size(); i++) {
                        RegularExpressionImpl regularExpress = (RegularExpressionImpl) components.get(i);
                        // set the regular text to the corresponding regular
                        // expression
                        if (dbmsLanguage.getDbmsName().equalsIgnoreCase(regularExpress.getExpression().getLanguage())) {
                            regularExpress.getExpression().setBody(regularText.getText());
                            isContainLanguage = true;
                            break;
                        }
                    }

                    // Not find the corresponding regular expression, will new a
                    // expression.
                    if (!isContainLanguage) {
                        RegularExpression newRegularExpress = BooleanExpressionHelper.createRegularExpression(
                                dbmsLanguage.getDbmsName(), regularText.getText());
                        this.pattern.getComponents().add(newRegularExpress);
                    }
                }
            } else {
                regularExpression.getExpression().setBody(regularText.getText());
            }

            EMFUtil.saveSingleResource(pattern.eResource());
            editorPage.updatePatternDefinitonSection();
            // MessageDialog.openInformation(DisplayUtils.getDefaultShell(false), "Success",
            // "Success to save the pattern '" +
            // pattern.getName()
            // + "'");
            saveButton.setEnabled(false);
        }
    }

    /**
     * DOC rli Comment method "getDbmsLanguage".
     * 
     * @param language
     * @return
     */
    private DbmsLanguage getDbmsLanguage() {
        // MOD qiongli 2011-1-7
        if (isJavaEngine) {
            return null;
        }
        for (IRepositoryNode connRepNode : listTdDataProviders) {
            ConnectionItem connItem = (ConnectionItem) connRepNode.getObject().getProperty().getItem();
            Connection tddataprovider = connItem.getConnection();
            if (tddataprovider.getName().equals(dbCombo.getText())) {
                return DbmsLanguageFactory.createDbmsLanguage(tddataprovider);
            }
        }
        return null;
    }

    /**
     * MOD gdbu 2011-5-31 bug : 19119
     * 
     * DOC gdbu Comment method "fillComboData".
     * 
     * In this method, we have done several things , e.g. 1:get the combo's data , and then set those data in combo.
     * 2:according to the value of combo(dbCombo.getText()) to determine if display the
     * functionInfo(functionText,functionLabel). 3:add SelectionListener to combo.
     * 
     */
    private void fillComboData() {
        listTdDataProviders = RepositoryNodeHelper.getDBConnectionRepositoryNodes(true);
        List<String> items = new ArrayList<String>();
        for (IRepositoryNode tdDataProvider : listTdDataProviders) {
            // MOD msjian TDQ-6186 2012-10-12: hiden the Hive database
            if (tdDataProvider instanceof DBConnectionRepNode) {
                String dbType = ((DBConnectionRepNode) tdDataProvider).getDatabaseConnection().getDatabaseType();
                if (!dbType.equals(SupportDBUrlType.HIVEDEFAULTURL.getLanguage())) {
                    items.add(tdDataProvider.getLabel());
                }
            }
            // TDQ-6186~
        }
        if (!items.isEmpty()) {
            dbCombo.setItems(items.toArray(new String[0]));
            if (dbCombo.getText().equals(PluginConstant.EMPTY_STRING)) {
                dbCombo.setText(items.get(0));
            }
            if (!dbCombo.getText().equals(PluginConstant.EMPTY_STRING)) {
                Connection dbConnectionName = getDBConnectionFromDBName(dbCombo.getText());
                if (dbConnectionName != null) {
                    setFunctionTextVisibleFromDBCOnn(dbConnectionName);
                }
            }
        }
        createDBComboListener();
    }

    /**
     * MOD gdbu 2011-5-31 bug : 19119
     * 
     * DOC gdbu Comment method "createDBComboListiner".
     * 
     * used to add dbCombo's Selectionlistener.
     */
    private void createDBComboListener() {
        dbCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
                widgetSelected(arg0);
            }

            public void widgetSelected(SelectionEvent arg0) {
                for (IRepositoryNode node : listTdDataProviders) {
                    Item item = node.getObject().getProperty().getItem();
                    if (item instanceof ConnectionItem) {
                        setFunctionTextVisibleFromDBCOnn(((ConnectionItem) item).getConnection());
                    }
                }
            }
        });
    }

    /**
     * MOD gdbu 2011-5-31 bug : 19119
     * 
     * DOC gdbu Comment method "setFunctionTextVisibleFromDBCOnn".
     * 
     * @param tdDataProvider
     */
    private void setFunctionTextVisibleFromDBCOnn(Connection tdDataProvider) {
        if (tdDataProvider.getName().equals(dbCombo.getText())) {

            DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(tdDataProvider);
            if (dbmsLanguage.regexLike(testText.getText(), regularText.getText()) == null) {
                setFunctionInfoVisible(true);
            } else {
                setFunctionInfoVisible(false);
            }
        }
    }

    /**
     * MOD gdbu 2011-5-31 bug : 19119
     * 
     * DOC gdbu Comment method "setFunctionInfoVisible".
     * 
     * used to set functionLabel(Label) and functionName(Text)'s visible.
     * 
     * @param visible
     */
    private void setFunctionInfoVisible(boolean visible) {
        functionLabel.setVisible(visible);
        functionNameText.setVisible(visible);
        if (visible) {
            textAreaLabel.setText(DefaultMessagesImpl.getString("PatternTestView.areaParameterOne")); //$NON-NLS-1$
            textAreaLabel.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.areaParameterOne"));//$NON-NLS-1$
            regularLabel.setText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameter")); //$NON-NLS-1$ 
            regularLabel.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameterTwo")); //$NON-NLS-1$ 
        } else {
            textAreaLabel.setText(DefaultMessagesImpl.getString("PatternTestView.area")); //$NON-NLS-1$ 
            textAreaLabel.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.area")); //$NON-NLS-1$ 
            regularLabel.setText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameter")); //$NON-NLS-1$ 
            regularLabel.setToolTipText(DefaultMessagesImpl.getString("PatternTestView.regularExpressionParameter"));//$NON-NLS-1$
        }
        GridData formData = new GridData(GridData.FILL_HORIZONTAL);
        formData.widthHint = 37;
        functionNameText.getParent().layout();
    }

    /**
     * MOD gdbu 2011-5-31 bug : 19119
     * 
     * DOC gdbu Comment method "getDBConnectionFromDBName".
     * 
     * @param dbName
     * @return
     */
    private Connection getDBConnectionFromDBName(String dbName) {
        for (IRepositoryNode node : listTdDataProviders) {
            if (node.getLabel().equals(dbName)) {
                Item item = node.getObject().getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    return ((ConnectionItem) item).getConnection();
                }
            }
        }
        return null;
    }

    /**
     * DOC gdbu Comment method "getFunctionName".
     * 
     * @return
     */
    public static String getFunctionName() {
        return functionNameText.getText();
    }
}
