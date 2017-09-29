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
package org.talend.dataprofiler.core.ui.editor.dqrules;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.composite.JoinConditionTableViewer;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.views.WhereClauseDND;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleMasterDetailsPage extends AbstractMetadataFormPage implements PropertyChangeListener {

    final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

    protected RuleRepNode ruleRepNode;

    private Section dqRuleDefinitionSection;

    private Section joinConditionSection;

    private Text whereText;

    private Text criticalityLevelText;

    private List<JoinElement> tempJoinElements;

    private Composite joinElementComp;

    private JoinConditionTableViewer joinConditionTableViewer;

    /**
     * DOC xqliu DQRuleMasterDetailsPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public DQRuleMasterDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    public List<JoinElement> getTempJoinElements() {
        return tempJoinElements;
    }

    public Text getCriticalityLevelText() {
        return criticalityLevelText;
    }

    public Text getWhereText() {
        return whereText;
    }

    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((DQRuleEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
            // MOD xqliu 2009-06-25 bug 7687
            this.firePropertyChange(IEditorPart.PROP_DIRTY);
            // ~
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((DQRuleEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.dqRuleSettings")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.DQRuleMetadata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.setProperties")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        resetJoinElements();

        createDQRuleDefinitionSection(topComp);
        createJoinConditionSection(topComp);
    }

    /**
     * DOC xqliu Comment method "creatDQRuleDefinitionSection".
     * 
     * @param comp
     */
    private void createDQRuleDefinitionSection(Composite comp) {
        dqRuleDefinitionSection = createSection(form, comp,
                DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.dqRuleDefinition"), null); //$NON-NLS-1$

        Label label = new Label(dqRuleDefinitionSection, SWT.WRAP);
        label.setText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.text")); //$NON-NLS-1$
        dqRuleDefinitionSection.setDescriptionControl(label);

        createDQRuleDefinitionComp();
    }

    /**
     * DOC xqliu Comment method "createDQRuleDefinitionComp".
     * 
     * @return
     */
    private Composite createDQRuleDefinitionComp() {

        Composite newComp = toolkit.createComposite(dqRuleDefinitionSection);
        newComp.setLayout(new GridLayout());

        Composite container = new Composite(newComp, SWT.NONE);
        GridLayout gdLayout = new GridLayout(2, false);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);
        container.setLayout(gdLayout);

        Label label1 = new Label(container, SWT.NONE);
        label1.setText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.criticalityLevel")); //$NON-NLS-1$

        GridData data;
        criticalityLevelText = new Text(container, SWT.BORDER);
        criticalityLevelText.setText("" + getCurrentModelElement().getCriticalityLevel()); //$NON-NLS-1$
        data = new GridData(GridData.FILL_HORIZONTAL);
        criticalityLevelText.setLayoutData(data);

        Label label2 = new Label(container, SWT.NONE);
        label2.setText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.whereClause")); //$NON-NLS-1$
        label2.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        whereText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        WhereClauseDND.installDND(whereText);
        whereText.setText(getCurrentModelElement().getWhereExpression());
        data = new GridData(GridData.FILL_BOTH);
        data.heightHint = 180;
        whereText.setLayoutData(data);
        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        addWhitespaceValidate(criticalityLevelText, whereText);
        addModifyListeners();

        dqRuleDefinitionSection.setClient(newComp);
        return newComp;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        if (!checkWhithspace()) {
            MessageUI.openError(DefaultMessagesImpl.getString("AbstractMetadataFormPage.whitespace")); //$NON-NLS-1$
            return;
        }
        if (!canSave().isOk()) {
            return;
        }

        super.doSave(monitor);
        if (saveDQRule()) {
            this.isDirty = false;
        }
    }

    /**
     * DOC xqliu Comment method "saveDQRule".
     * 
     * @return
     */
    private boolean saveDQRule() {
        boolean ret = false;
        this.tempJoinElements = cleanJoins(tempJoinElements);
        if (checkValus()) {
            TaggedValueHelper.setValidStatus(true, getCurrentModelElement());
            getCurrentModelElement().setCriticalityLevel(Integer.valueOf(getCriticalityLevelText().getText()));
            getCurrentModelElement().setWhereExpression(whereText.getText());
            getCurrentModelElement().getJoins().clear();
            getCurrentModelElement().getJoins().addAll(this.tempJoinElements);

            TDQBusinessRuleItem whereRuleItem = (TDQBusinessRuleItem) getCurrentRepNode().getObject().getProperty().getItem();

            // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
            ReturnCode rc = ElementWriterFactory.getInstance().createdRuleWriter().save(whereRuleItem, true);

            ret = rc.isOk();
            this.joinConditionTableViewer.updateModelViewer();
        }
        return ret;

    }

    /**
     * DOC xqliu Comment method "cleanJoins".
     * 
     * @param joinElements
     * @return
     */
    private List<JoinElement> cleanJoins(List<JoinElement> joinElements) {
        List<JoinElement> retJoinElements = new ArrayList<JoinElement>();
        for (JoinElement je : joinElements) {
            if (je.getColA() != null && je.getColB() != null) {
                retJoinElements.add(je);
            }
        }
        return retJoinElements;
    }

    /**
     * DOC xqliu Comment method "checkValus".
     * 
     * @return
     */
    private boolean checkValus() {
        boolean ret = true;
        String msg = ""; //$NON-NLS-1$
        String cl = getCriticalityLevelText().getText();
        if (cl == null || "".equals(cl)) { //$NON-NLS-1$
            ret = false;
            msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.criticalityLevelIsEmpty") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            try {
                int i = Integer.valueOf(cl).intValue();
                if (!(i >= PluginConstant.CRITICALITY_LEVEL_MIN && i <= PluginConstant.CRITICALITY_LEVEL_MAX)) {
                    ret = false;
                    msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.criticalityLevelIsInvalid") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            } catch (NumberFormatException e) {
                ret = false;
                msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.criticalityLevelIsInvalid") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        String wh = getWhereText().getText();
        if (wh == null || "".equals(wh)) { //$NON-NLS-1$
            ret = false;
            msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.whereClauseIsEmpty") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            if (wh.startsWith(PluginConstant.WHERE)) {
                ret = false;
                msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.whereClauseIsInvalid") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        for (JoinElement join : this.tempJoinElements) {
            String tableAliasA = join.getTableAliasA();
            String tableAliasB = join.getTableAliasB();
            if (tableAliasA == null || "".equals(tableAliasA) || tableAliasB == null || "".equals(tableAliasB)) { //$NON-NLS-1$ //$NON-NLS-2$
                ret = false;
                msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.emptyTableAlias") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                break;
            } else if (tableAliasA.equals(tableAliasB)) {
                ret = false;
                String tableA = ColumnHelper.getColumnSetOwner(join.getColA()).getName();
                String tableB = ColumnHelper.getColumnSetOwner(join.getColB()).getName();
                msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.sameTableAlias", tableA, tableB) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                if (!checkAlias(tableAliasA)) {
                    ret = false;
                    msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.invalidAliasName", tableAliasA) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                if (!checkAlias(tableAliasB)) {
                    ret = false;
                    msg += DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.invalidAliasName", tableAliasB) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        if (!ret) {
            MessageUI.openWarning(msg);
        }
        return ret;
    }

    /**
     * DOC xqliu Comment method "checkAlias".
     * 
     * @param alias
     * @return
     */
    private boolean checkAlias(String alias) {
        // not number
        try {
            Double.valueOf(alias);
            return false;
        } catch (NumberFormatException e) {
        }

        String regEx = "^\\w+$"; //$NON-NLS-1$
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(alias);
        return m.find();
    }

    /**
     * DOC xqliu Comment method "addModifyListeners".
     */
    protected void addModifyListeners() {
        whereText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });
        criticalityLevelText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });
    }

    /**
     * DOC xqliu Comment method "createJoinConditionSection".
     * 
     * @param comp
     */
    private void createJoinConditionSection(Composite comp) {
        joinConditionSection = createSection(form, comp,
                DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.joinCondition"), null); //$NON-NLS-1$

        Label label = new Label(joinConditionSection, SWT.WRAP);
        label.setText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.textJoinCondition")); //$NON-NLS-1$
        joinConditionSection.setDescriptionControl(label);

        createJoinConditionComp();
    }

    /**
     * DOC xqliu Comment method "createJoinConditionComp".
     * 
     * @return
     */
    private Composite createJoinConditionComp() {

        Composite newComp = toolkit.createComposite(joinConditionSection);
        newComp.setLayout(new GridLayout());
        // MOD xqliu 2009-08-31 bug 8791
        if (true) {
            joinConditionTableViewer = new JoinConditionTableViewer(newComp, this);
            joinConditionTableViewer.setDirty(false);
            joinConditionTableViewer.addPropertyChangeListener(this);
            createButtons(newComp, joinConditionTableViewer);
        } else {
            joinElementComp = new Composite(newComp, SWT.NONE);
            joinElementComp.setLayout(new GridLayout());
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(joinElementComp);
            for (int i = 0; i < tempJoinElements.size(); i++) {
                creatNewJoinElementLine(tempJoinElements.get(i));
            }
            createAddButton(newComp);
        }
        // ~
        joinConditionSection.setClient(newComp);
        return newComp;
    }

    /**
     * DOC xqliu Comment method "createButtons".
     * 
     * @param parent
     */
    private void createButtons(Composite parent, final JoinConditionTableViewer jcTableViewer) {
        Composite buttonsComposite = new Composite(parent, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(2, false));

        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;

        final Button addButton = new Button(buttonsComposite, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                jcTableViewer.addJoinElement();
            }
        });

        final Button delButton = new Button(buttonsComposite, SWT.NONE);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setLayoutData(labelGd);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                JoinElement join = (JoinElement) ((IStructuredSelection) jcTableViewer.getSelection()).getFirstElement();
                if (join != null) {
                    jcTableViewer.removeJoinElement(join);
                }
            }
        });
    }

    /**
     * DOC xqliu Comment method "createAddButton".
     * 
     * @param parent top composite
     */
    private void createAddButton(Composite parent) {
        final Button addButton = new Button(parent, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.CENTER;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                JoinElement newJoinElement = RulesFactory.eINSTANCE.createJoinElement();
                newJoinElement.setOperator(PluginConstant.DEFAULT_OPERATOR);
                creatNewJoinElementLine(newJoinElement);
                tempJoinElements.add(newJoinElement);
                joinConditionSection.setExpanded(true);
                setDirty(true);
            }
        });
    }

    /**
     * DOC xqliu Comment method "creatNewJoinElementLine".
     * 
     * @param joinElement
     */
    private void creatNewJoinElementLine(JoinElement joinElement) {
        final Composite expressComp = new Composite(joinElementComp, SWT.NONE);
        expressComp.setLayout(new GridLayout(6, false));
        final JoinElement fje = joinElement;
        boolean flag = false;
        if (fje.getColA() != null && fje.getColB() != null && fje.getOperator() != null) {
            flag = true;
        }

        final Label labelL = new Label(expressComp, SWT.NONE);
        labelL.setImage(ImageLib.getImage(ImageLib.TD_COLUMN));
        GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(labelL);

        final Text textL = new Text(expressComp, SWT.BORDER);
        textL.setEditable(false);
        textL.setText(flag ? fje.getColA().getName() : PluginConstant.EMPTY_STRING);
        GridDataFactory.fillDefaults().span(1, 1).grab(true, false).applyTo(textL);
        ((GridData) textL.getLayoutData()).widthHint = 100;

        DropTarget targetL = new DropTarget(textL, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
        targetL.setTransfer(new Transfer[] { transfer });
        targetL.addDropListener(new ColumnDropTargetListener(fje, ColumnDropTargetListener.LEFT));

        final CCombo combo = new CCombo(expressComp, SWT.BORDER);
        combo.setEditable(false);
        combo.setItems(PluginConstant.OPERATORS);
        combo.setText(flag ? fje.getOperator() : PluginConstant.DEFAULT_OPERATOR);
        GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(combo);
        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                fje.setOperator(combo.getText());
                setDirty(true);
            }
        });

        final Label labelR = new Label(expressComp, SWT.NONE);
        labelR.setImage(ImageLib.getImage(ImageLib.TD_COLUMN));
        GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(labelR);

        final Text textR = new Text(expressComp, SWT.BORDER);
        textR.setEditable(false);
        textR.setText(flag ? fje.getColB().getName() : PluginConstant.EMPTY_STRING);
        GridDataFactory.fillDefaults().span(1, 1).grab(true, false).applyTo(textR);
        ((GridData) textR.getLayoutData()).widthHint = 100;

        DropTarget targetR = new DropTarget(textR, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
        targetR.setTransfer(new Transfer[] { transfer });
        targetR.addDropListener(new ColumnDropTargetListener(fje, ColumnDropTargetListener.RIGHT));

        Button delButton = new Button(expressComp, SWT.NONE);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setToolTipText(DefaultMessagesImpl.getString("DQRuleMasterDetailsPage.delete")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(delButton);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tempJoinElements.remove(fje);
                expressComp.dispose();
                joinConditionSection.setExpanded(true);
                setDirty(true);
            }
        });

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(expressComp);
    }

    private void resetJoinElements() {
        if (tempJoinElements == null) {
            tempJoinElements = new ArrayList<JoinElement>();
        } else {
            tempJoinElements.clear();
        }
        EList<JoinElement> joins = getCurrentModelElement().getJoins();
        for (JoinElement join : joins) {
            tempJoinElements.add(cloneJoin(join));
        }
    }

    /**
     * DOC xqliu Comment method "cloneJoin".
     * 
     * @param joinElement
     * @return
     */
    private JoinElement cloneJoin(JoinElement joinElement) {
        if (joinElement != null) {
            JoinElement newJoinElement = RulesFactory.eINSTANCE.createJoinElement();
            newJoinElement.setColA(joinElement.getColA());
            newJoinElement.setColB(joinElement.getColB());
            newJoinElement.setColumnAliasA(joinElement.getColumnAliasA());
            newJoinElement.setColumnAliasB(joinElement.getColumnAliasB());
            newJoinElement.setOperator(joinElement.getOperator());
            newJoinElement.setTableAliasA(joinElement.getTableAliasA());
            newJoinElement.setTableAliasB(joinElement.getTableAliasB());
            return newJoinElement;
        }
        return null;
    }

    /**
     * DOC xqliu DQRuleMasterDetailsPage class global comment. Detailled comment
     */
    private class ColumnDropTargetListener implements DropTargetListener {

        private JoinElement joinElement;

        private int index;

        public static final int LEFT = 1;

        public static final int RIGHT = 2;

        public JoinElement getJoinElement() {
            if (joinElement == null) {
                joinElement = RulesFactory.eINSTANCE.createJoinElement();
                joinElement.setOperator(PluginConstant.DEFAULT_OPERATOR);
            }
            return joinElement;
        }

        public int getIndex() {
            if (index != LEFT && index != RIGHT) {
                index = LEFT;
            }
            return index;
        }

        public ColumnDropTargetListener(JoinElement je, int index) {
            super();
            this.joinElement = je;
            this.index = index;
        }

        public void dragEnter(DropTargetEvent event) {
            if (event.detail == DND.DROP_DEFAULT) {
                event.detail = DND.DROP_COPY;
            }
        }

        public void dragOver(DropTargetEvent event) {
            event.feedback = DND.FEEDBACK_NONE;
        }

        public void dragOperationChanged(DropTargetEvent event) {
            if (event.detail == DND.DROP_DEFAULT) {
                event.detail = DND.DROP_COPY;
            }
        }

        public void dragLeave(DropTargetEvent event) {
        }

        public void dropAccept(DropTargetEvent event) {
        }

        public void drop(DropTargetEvent event) {
            if (transfer.isSupportedType(event.currentDataType)) {
                if (event.data instanceof TreeSelection) {
                    TreeSelection ts = (TreeSelection) event.data;
                    if (ts.getFirstElement() instanceof TdColumn) {
                        TdColumn column = (TdColumn) ts.getFirstElement();
                        setColumn(column);

                        DropTarget target = (DropTarget) event.widget;
                        Text text = (Text) target.getControl();
                        text.setText(column.getName());

                        setDirty(true);
                    }
                }
            }
        }

        public void setColumn(TdColumn column) {
            switch (getIndex()) {
            case LEFT:
                getJoinElement().setColA(column);
                // MOD scorreia 2009-04-08 removed alias setting
                // getJoinElement().setColumnAliasA(column.getName());
                // getJoinElement().setTableAliasA(ColumnHelper.getColumnSetOwner(column).getName());
                break;
            case RIGHT:
                getJoinElement().setColB(column);
                // MOD scorreia 2009-04-08 removed alias setting
                // getJoinElement().setColumnAliasB(column.getName());
                // getJoinElement().setTableAliasB(ColumnHelper.getColumnSetOwner(column).getName());
                break;
            default:
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        ReturnCode rc = canModifyName(ERepositoryObjectType.TDQ_RULES);
        if (!rc.isOk()) {
            MessageDialogWithToggle.openError(null,
                    DefaultMessagesImpl.getString("AbstractMetadataFormPage.saveFailed"), rc.getMessage()); //$NON-NLS-1$
        }
        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public WhereRule getCurrentModelElement() {
        return (WhereRule) ruleRepNode.getRule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public RuleRepNode getCurrentRepNode() {
        return ruleRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#init(org.eclipse.ui.forms.editor.FormEditor)
     */
    @Override
    protected void init(FormEditor editor) {
        currentEditor = (DQRuleEditor) editor;
        ruleRepNode = getWhereRuleRepNodeFromInput(currentEditor.getEditorInput());
    }

    /**
     * get PatternRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private RuleRepNode getWhereRuleRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                WhereRule whereRule = DQRuleResourceFileHelper.getInstance().findWhereRule(file);
                whereRule = (WhereRule) EObjectHelper.resolveObject(whereRule);
                return RepositoryNodeHelper.recursiveFindRuleSql(whereRule);
            }
        } else if (editorInput instanceof BusinessRuleItemEditorInput) {
            return ((BusinessRuleItemEditorInput) editorInput).getRepNode();
        }
        return null;
    }

}
