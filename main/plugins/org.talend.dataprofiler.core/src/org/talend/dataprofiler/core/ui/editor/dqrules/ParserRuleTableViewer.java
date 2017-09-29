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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.parserrule.ParserRuleLanguageEnum;
import org.talend.dataquality.rules.ParserRule;

/**
 * DOC klliu class global comment. bug 8791 2009-08-31.
 */
public class ParserRuleTableViewer {

    protected static Logger log = Logger.getLogger(ParserRuleTableViewer.class);

    private ParserRuleMasterDetailsPage masterPage;

    private TableViewer parserRuleTableViewer;

    private List<TdExpression> parserRuleTdExpression;

    private List<TdExpression> copyTdExpression;

    private Composite parentComposite;

    private ParserRule parserRule;

    private String[] headers = { "Name", "Type", "Value" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private int[] widths = { 100, 100, 300 };

    private Table ruleTable;

    private boolean isDirty = false;

    public ParserRuleTableViewer(Composite parent, ParserRuleMasterDetailsPage masterPage) {
        this(parent, masterPage.getCurrentModelElement());
        this.masterPage = masterPage;
    }

    public ParserRuleTableViewer(Composite parent, ParserRule parserRule) {
        this.parentComposite = parent;
        this.parserRule = parserRule;
        this.ruleTable = createTable(parent);
    }

    /**
     * DOC klliu Comment method "createTable".
     * 
     * @param parent
     */
    private Table createTable(Composite parent) {
        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        final Table table = new Table(parentComposite, style);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GridData gd = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(gd);

        for (int i = 0; i < headers.length; ++i) {
            TableColumn tableColumn = new TableColumn(table, SWT.LEFT, i);
            tableColumn.setText(headers[i]);
            tableColumn.setWidth(widths[i]);
        }

        parserRuleTableViewer = new TableViewer(table);
        parserRuleTableViewer.setUseHashlookup(true);
        parserRuleTableViewer.setColumnProperties(headers);

        CellEditor[] editors = new CellEditor[headers.length];
        for (int i = 0; i < editors.length; ++i) {
            switch (i) {
            case 0:
            case 2:
                editors[i] = new TextCellEditor(table);
                break;
            case 1:
                editors[i] = new ComboBoxCellEditor(table, ParserRuleLanguageEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            }
        }
        parserRuleTableViewer.setCellModifier(new TDExpresstionCellModifier(headers, parserRuleTableViewer));
        parserRuleTableViewer.setCellEditors(editors);
        parserRuleTableViewer.setColumnProperties(headers);
        parserRuleTableViewer.setContentProvider(new TdExpressionContentProvider());
        parserRuleTableViewer.setLabelProvider(new TdExpressionLabelProvider());
        parserRuleTableViewer.setInput(getParserRuleTdExpressions());

        table.setData(this);
        GridData tableGD = new GridData(GridData.FILL_BOTH);
        tableGD.heightHint = 130;
        table.setLayoutData(tableGD);

        return table;
    }

    /**
     * DOC klliu Comment method "addTdExpression".
     * 
     * @return
     */
    public TdExpression addTdExpression() {
        TdExpression tdExpression = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdExpression();

        tdExpression.setBody("\"\"");//$NON-NLS-1$
        tdExpression.setLanguage(DefaultMessagesImpl.getString("ParserRuleTableViewer.languageSelection"));//$NON-NLS-1$
        tdExpression.setName("\"\"");//$NON-NLS-1$

        TdExpressionContentProvider contentProvider = (TdExpressionContentProvider) parserRuleTableViewer.getContentProvider();
        List<TdExpression> movedElements = contentProvider.getMovedElements();
        movedElements.add(tdExpression);
        parserRuleTableViewer.setInput(movedElements);
        this.parserRuleTdExpression.add(tdExpression);
        setDirty(true);
        this.parserRuleTableViewer.refresh();
        return tdExpression;
    }

    public void copyTdExpression(List<TdExpression> copyTdExpressions) {
        copyTdExpression = new ArrayList<TdExpression>();
        copyTdExpression.addAll(copyTdExpressions);
    }

    public void pasteTdExpression() {
        if (copyTdExpression != null) {
            for (TdExpression tdExpression : copyTdExpression) {
                TdExpression newExpresstion = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdExpression();
                newExpresstion.setBody(tdExpression.getBody());
                newExpresstion.setLanguage(tdExpression.getLanguage());
                newExpresstion.setName(tdExpression.getName());
                TdExpressionContentProvider contentProvider = (TdExpressionContentProvider) parserRuleTableViewer
                        .getContentProvider();
                List<TdExpression> movedElements = contentProvider.getMovedElements();
                movedElements.add(newExpresstion);
                parserRuleTableViewer.setInput(movedElements);
                this.parserRuleTdExpression.add(newExpresstion);
            }
            setDirty(true);
            this.parserRuleTableViewer.refresh();
        }
    }

    /**
     * DOC klliu Comment method "getSelection".
     * 
     * @return
     */
    public ISelection getSelection() {
        return this.parserRuleTableViewer.getSelection();
    }

    /**
     * DOC klliu Comment method "removeTdExpression".
     * 
     * @param tdExpression
     */
    public void removeTdExpression(TdExpression tdExpression) {
        TdExpressionContentProvider contentProvider = (TdExpressionContentProvider) parserRuleTableViewer.getContentProvider();
        List<TdExpression> movedElements = contentProvider.getMovedElements();
        movedElements.remove(tdExpression);
        this.parserRuleTdExpression.remove(tdExpression);
        parserRuleTableViewer.setInput(movedElements);
        setDirty(true);
        parserRuleTableViewer.refresh();
    }

    /**
     * 
     * zshen remove all of expression on the tableViewer.
     */
    public void removeAllTdExpression() {
        List<TdExpression> movedElements = new ArrayList<TdExpression>();
        this.parserRuleTdExpression.clear();
        parserRuleTableViewer.setInput(movedElements);
        setDirty(true);
        parserRuleTableViewer.refresh();
    }

    /**
     * 
     * zshen add one expression List on the tableViewer.
     */
    public void addAllTdExpression(List<TdExpression> movedElements) {
        parserRuleTdExpression.addAll(movedElements);
        parserRuleTableViewer.setInput(movedElements);
        setDirty(true);
        parserRuleTableViewer.refresh();
    }

    /**
     * DOC klliu Comment method "moveTdExpression".
     * 
     * @param tdExpression
     */
    public void moveTdExpression(TdExpression tdExpression, int type) {
        LinkedList<Object> sorter = new LinkedList<Object>();
        TdExpressionContentProvider contentProvider = (TdExpressionContentProvider) parserRuleTableViewer.getContentProvider();
        List<TdExpression> movedElements = contentProvider.getMovedElements();
        sorter.addAll(movedElements);
        int indexOf = sorter.indexOf(tdExpression);
        if (type == 1) {
            if (!(indexOf - 1 < 0)) {
                sorter.remove(tdExpression);
                sorter.add(indexOf - 1, tdExpression);
            }
        } else {
            if (!(indexOf + 1 >= sorter.size())) {
                sorter.remove(tdExpression);
                sorter.add(indexOf + 1, tdExpression);
            }
        }
        parserRuleTableViewer.setInput(sorter);
    }

    /**
     * DOC klliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     */
    private final class TDExpresstionCellModifier implements ICellModifier {

        private List<String> columeNames;

        private TableViewer tableViewer;

        public TDExpresstionCellModifier(String[] columeNames, TableViewer tableViewer) {
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
            TdExpression tdExpression = (TdExpression) element;
            switch (columnIndex) {
            case 0:
                result = tdExpression == null ? "" : tdExpression.getName(); //$NON-NLS-1$
                break;
            case 1:
                //result = tdExpression == null ? "" : tdExpression.getLanguage(); //$NON-NLS-1$
                String stringValue = tdExpression.getLanguage();
                int i = ParserRuleLanguageEnum.getAllTypes().length - 1;
                while (!stringValue.equals(ParserRuleLanguageEnum.getAllTypes()[i]) && i > 0) {
                    --i;
                }
                result = new Integer(i);
                break;
            case 2:
                result = tdExpression == null ? "" : tdExpression.getBody(); //$NON-NLS-1$
                break;
            default:
                result = ""; //$NON-NLS-1$
            }
            return result;
        }

        public void modify(Object element, String property, Object value) {
            int columnIndex = this.columeNames.indexOf(property);

            TableItem tableItem = (TableItem) element;
            if (tableItem != null) {
                TdExpression tdExpression = (TdExpression) tableItem.getData();
                if (tdExpression != null) {
                    String valueString = String.valueOf(value).trim();

                    switch (columnIndex) {

                    case 0:
                        tdExpression.setName(valueString);
                        break;
                    case 1:
                        valueString = ParserRuleLanguageEnum.getAllTypes()[((Integer) value).intValue()].trim();
                        if (!tdExpression.getLanguage().equals(valueString)) {
                            tdExpression.setLanguage(valueString);
                        }
                        tdExpression.setLanguage(valueString);
                        break;
                    case 2:
                        tdExpression.setBody(valueString);
                        break;
                    default:
                    }
                    tableViewer.update(tdExpression, null);
                    setDirty(true);
                }
            }
        }

    }

    /**
     * DOC klliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     */
    private final static class TdExpressionContentProvider implements IStructuredContentProvider {

        private List<TdExpression> resorter;

        public Object[] getElements(Object inputElement) {
            if (inputElement != null) {
                if (inputElement instanceof List) {
                    resorter = new ArrayList<TdExpression>();
                    resorter.addAll((Collection<? extends TdExpression>) inputElement);
                    return ((List) inputElement).toArray();
                }
            }
            return null;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public List<TdExpression> getMovedElements() {
            return resorter;
        }
    }

    /**
     * DOC klliu IndicatorDefinitionMaterPage class global comment. Detailled comment
     * 
     */
    private static class TdExpressionLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = ""; //$NON-NLS-1$

            TdExpression tdExpression = (TdExpression) element;

            switch (columnIndex) {
            case 0:
                result = tdExpression == null ? "" : tdExpression.getName(); //$NON-NLS-1$
                break;
            case 1:
                if (tdExpression.getLanguage().equals("SELECT")) {//$NON-NLS-1$
                    result = ParserRuleLanguageEnum.Default.getLiteral().trim();
                    break;
                }
                result = tdExpression == null ? "" : tdExpression.getLanguage(); //$NON-NLS-1$
                break;
            case 2:
                result = tdExpression == null ? "" : tdExpression.getBody(); //$NON-NLS-1$
                break;
            default:
                result = ""; //$NON-NLS-1$

            }
            return result;

        }
    }

    public List<TdExpression> getParserRuleTdExpressions() {
        parserRuleTdExpression = parserRule.getSqlGenericExpression();
        return this.parserRuleTdExpression;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
        if (this.masterPage != null) {
            this.masterPage.setDirty(isDirty);
        }
    }

    public Table getRuleTable() {
        return ruleTable;
    }

    public TableViewer getParserRuleTableViewer() {
        return parserRuleTableViewer;
    }

}
