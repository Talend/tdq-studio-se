// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package net.sourceforge.sqlexplorer.oracle.actions;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 *
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.oracle.actions.explain.ExplainNode;
import net.sourceforge.sqlexplorer.oracle.actions.explain.ExplainPlanActionGroup;
import net.sourceforge.sqlexplorer.parsers.Query;
import net.sourceforge.sqlexplorer.parsers.QueryParser;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.plugin.editors.SQLEditor;
import net.sourceforge.sqlexplorer.sqleditor.results.ResultsTab;
import net.sourceforge.sqlexplorer.sqlpanel.AbstractSQLExecution;

public class ExplainExecution extends AbstractSQLExecution {

    static class TreeLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            ExplainNode en = (ExplainNode) element;
            if (columnIndex == 0)
                return en.toString();
            if (columnIndex == 1) {
                int cost = en.getCost();
                if (cost != -1)
                    return (new StringBuilder()).append(cost).toString();
                else
                    return "";
            }
            if (columnIndex == 2) {
                int card = en.getCardinality();
                if (card != -1)
                    return (new StringBuilder()).append(card).toString();
                else
                    return "";
            } else {
                return "";
            }
        }

        TreeLabelProvider() {
        }
    }

    public ExplainExecution(SQLEditor editor, QueryParser queryParser) {
        super(editor, queryParser);
        setProgressMessage(Messages.getString("SQLResultsView.ConnectionWait"));
    }

    private void displayResults(final ExplainNode node, final Query query) {
        getEditor().getSite().getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                ResultsTab resultsTab = allocateResultsTab(query);
                try {
                    Composite composite = resultsTab.getParent();
                    GridLayout gLayout = new GridLayout();
                    gLayout.numColumns = 2;
                    gLayout.marginLeft = 0;
                    gLayout.horizontalSpacing = 0;
                    gLayout.verticalSpacing = 0;
                    gLayout.marginWidth = 0;
                    gLayout.marginHeight = 0;
                    composite.setLayout(gLayout);
                    composite.setData("parenttab", resultsTab.getTabItem());
                    Composite pp = new Composite(composite, 0);
                    pp.setLayout(new FillLayout());
                    pp.setLayoutData(new GridData(1808));
                    TreeViewer tv = new TreeViewer(pp, 0x10800);
                    Tree table = tv.getTree();
                    table.setLinesVisible(true);
                    table.setHeaderVisible(true);
                    TreeColumn tc = new TreeColumn(table, 0);
                    tc.setText("");
                    tc = new TreeColumn(table, 0);
                    tc.setText("Cost");
                    tc = new TreeColumn(table, 0);
                    tc.setText("Cardinality");
                    TableLayout tableLayout = new TableLayout();
                    tableLayout.addColumnData(new ColumnWeightData(6, 150, true));
                    tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
                    tableLayout.addColumnData(new ColumnWeightData(1, 50, true));
                    table.setLayout(tableLayout);
                    tv.setContentProvider(new ITreeContentProvider() {

                        public void dispose() {
                        }

                        public Object[] getChildren(Object parentElement) {
                            return ((ExplainNode) parentElement).getChildren();
                        }

                        public Object[] getElements(Object inputElement) {
                            ExplainNode nd = (ExplainNode) inputElement;
                            return nd.getChildren();
                        }

                        public Object getParent(Object element) {
                            return ((ExplainNode) element).getParent();
                        }

                        public boolean hasChildren(Object element) {
                            return ((ExplainNode) element).getChildren().length > 0;
                        }

                        public void inputChanged(Viewer viewer1, Object obj, Object obj1) {
                        }

                    });
                    tv.setLabelProvider(new TreeLabelProvider() {

                    });
                    tv.setInput(node);
                    tv.refresh();
                    tv.expandAll();
                    for (int i = 0; i < table.getColumnCount(); i++)
                        table.getColumn(i).pack();

                    final ExplainPlanActionGroup actionGroup = new ExplainPlanActionGroup(tv, node.getChildren()[0]);
                    MenuManager menuManager = new MenuManager("ExplainPlanContextMenu");
                    menuManager.setRemoveAllWhenShown(true);
                    org.eclipse.swt.widgets.Menu contextMenu = menuManager.createContextMenu(table);
                    tv.getControl().setMenu(contextMenu);
                    menuManager.addMenuListener(new IMenuListener() {

                        public void menuAboutToShow(IMenuManager manager) {
                            actionGroup.fillContextMenu(manager);
                        }
                    });
                    composite.layout();
                    composite.redraw();
                } catch (Exception e) {
                    if (resultsTab != null) {
                        Composite composite = resultsTab.getParent();
                        String message = e.getMessage();
                        Label errorLabel = new Label(composite, 4);
                        errorLabel.setText(message);
                        errorLabel.setLayoutData(new GridData(4, 128, true, false));
                    }
                    SQLExplorerPlugin.error("Error creating explain tab", e);
                }
            }
        });
    }

    protected void doExecution(IProgressMonitor monitor) throws Exception {
        int numErrors;
        SQLException lastSQLException;
        Query query;
        numErrors = 0;
        lastSQLException = null;
        query = null;
        Iterator iter = getQueryParser().iterator();
        String id_;
        query = (Query) iter.next();
        if (monitor.isCanceled()) {
            return;
        }
        _stmt = _connection.createStatement();
        id_ = Integer.toHexString((new Random()).nextInt()).toUpperCase();
        _stmt.execute((new StringBuilder("delete plan_table where statement_id='")).append(id_).append("'").toString());
        _stmt.close();
        _stmt = null;
        if (monitor.isCanceled())
            return;
        ResultSet rs;
        ExplainNode nd_parent;
        try {
            _stmt = _connection.createStatement();
            _stmt
                    .execute((new StringBuilder("EXPLAIN PLAN SET statement_id = '"))
                            .append(id_)
                            .append("' FOR ")
                            .append(query.getQuerySql())
                            .toString());
            _stmt.close();
            _stmt = null;
            if (monitor.isCanceled())
                return;
        } catch (SQLException e) {
            debugLogQuery(query, e);
            boolean stopOnError =
                    SQLExplorerPlugin.getDefault().getPreferenceStore().getBoolean("SQLEditor.StopOnError");
            logException(e, query, stopOnError);
            closeStatements();
            if (stopOnError)
                throw e;
            numErrors++;
            lastSQLException = e;
        } catch (Exception e) {
            closeStatements();
            throw e;
        }
        _prepStmt = _connection
                .prepareStatement(
                        "select level, object_type,operation,options,object_owner,object_name,optimizer,cardinality ,cost,id,parent_id,level  from  plan_table  start with id = 0 and statement_id=?  connect by prior id=parent_id and statement_id=?");
        _prepStmt.setString(1, id_);
        _prepStmt.setString(2, id_);
        rs = _prepStmt.executeQuery();
        if (monitor.isCanceled())
            return;
        HashMap mp = new HashMap();
        int level;
        ExplainNode nd;
        for (; rs.next(); nd.setLevel(level)) {
            String object_type = rs.getString("object_type");
            String operation = rs.getString("operation");
            String options = rs.getString("options");
            String object_owner = rs.getString("object_owner");
            String object_name = rs.getString("object_name");
            String optimizer = rs.getString("optimizer");
            int cardinality = rs.getInt("cardinality");
            if (rs.wasNull())
                cardinality = -1;
            int cost = rs.getInt("cost");
            if (rs.wasNull())
                cost = -1;
            int parentID = rs.getInt("parent_id");
            int id = rs.getInt("id");
            level = rs.getInt("level");
            nd = null;
            if (id == 0) {
                ExplainNode dummy = new ExplainNode(null);
                mp.put(new Integer(-1), dummy);
                dummy.setId(-1);
                nd = new ExplainNode(dummy);
                dummy.add(nd);
                nd.setId(0);
                mp.put(new Integer(0), nd);
            } else {
                nd_parent = (ExplainNode) mp.get(new Integer(parentID));
                nd = new ExplainNode(nd_parent);
                nd_parent.add(nd);
                mp.put(new Integer(id), nd);
            }
            nd.setCardinality(cardinality);
            nd.setCost(cost);
            nd.setObject_name(object_name);
            nd.setObject_owner(object_owner);
            nd.setObject_type(object_type);
            nd.setOperation(operation);
            nd.setOptimizer(optimizer);
            nd.setOptions(options);
            nd.setId(id);
        }

        rs.close();
        _prepStmt.close();
        _prepStmt = null;
        nd_parent = (ExplainNode) mp.get(new Integer(-1));
        if (monitor.isCanceled())
            return;
        displayResults(nd_parent, query);
        debugLogQuery(query, null);
        query = null;
        if (numErrors == 1)
            throw lastSQLException;
        if (numErrors > 1)
            MessageDialog
                    .openError(getEditor().getSite().getShell(), "SQL Error",
                            "One or more of your SQL statements failed - check the Messages log for details");
        return;
    }

    private void closeStatements() {
        if (_stmt != null)
            try {
                _stmt.close();
                _stmt = null;
            } catch (Exception e) {
                SQLExplorerPlugin.error("Error closing statement.", e);
            }
        if (_prepStmt != null)
            try {
                _prepStmt.close();
                _prepStmt = null;
            } catch (Exception e) {
                SQLExplorerPlugin.error("Error closing statement.", e);
            }
    }

    protected void doStop() throws Exception {
        Exception t = null;
        if (_stmt != null) {
            try {
                _stmt.cancel();
            } catch (Exception e) {
                t = e;
                SQLExplorerPlugin.error("Error cancelling statement.", e);
            }
            try {
                _stmt.close();
                _stmt = null;
            } catch (Exception e) {
                SQLExplorerPlugin.error("Error closing statement.", e);
            }
        }
        if (_prepStmt != null) {
            try {
                _prepStmt.cancel();
            } catch (Exception e) {
                t = e;
                SQLExplorerPlugin.error("Error cancelling statement.", e);
            }
            try {
                _prepStmt.close();
                _prepStmt = null;
            } catch (Exception e) {
                SQLExplorerPlugin.error("Error closing statement.", e);
            }
        }
        if (t != null)
            throw t;
        else
            return;
    }

    private PreparedStatement _prepStmt;

    private Statement _stmt;

}
