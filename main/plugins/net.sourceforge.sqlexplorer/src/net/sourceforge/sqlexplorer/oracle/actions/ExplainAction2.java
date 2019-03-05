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

package net.sourceforge.sqlexplorer.oracle.actions;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.dialogs.MessageDialog;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dbproduct.SQLConnection;
import net.sourceforge.sqlexplorer.dbproduct.Session;
import net.sourceforge.sqlexplorer.parsers.ParserException;
import net.sourceforge.sqlexplorer.parsers.QueryParser;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.sqleditor.actions.AbstractEditorAction;

public class ExplainAction2 extends AbstractEditorAction {

    public ExplainAction2() {
    }

    public String getText() {
        return Messages.getString("oracle.editor.actions.explain");
    }

    public String getToolTipText() {
        return getText();
    }

    public void run() {
        try {
            Session session = getSession();
            if (session == null) {
                return;
            }
            SQLConnection connection = null;
            ResultSet rs = null;
            boolean createPlanTable;
            boolean notFoundTable;
            connection = session.grabConnection();
            Statement st = connection.createStatement();
            createPlanTable = false;
            notFoundTable = true;
            try {
                rs = st.executeQuery("select statement_id from plan_table");
                notFoundTable = false;
                rs.close();
                rs = null;
            } catch (SQLException _ex) {
                createPlanTable = MessageDialog.openQuestion(null, Messages
                        .getString("oracle.editor.actions.explain.notFound.Title"), Messages
                        .getString("oracle.editor.actions.explain.notFound"));
            }
            st.close();
            st = null;
            if (notFoundTable && !createPlanTable) {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        SQLExplorerPlugin.error("Cannot close result set", e);
                    }
                if (connection != null)
                    session.releaseConnection(connection);
                return;
            }
            try {
                if (notFoundTable && createPlanTable) {
                    st = connection.createStatement();
                    st
                            .execute("CREATE TABLE PLAN_TABLE (  STATEMENT_ID                    VARCHAR2(30), TIMESTAMP                       DATE,  REMARKS                         VARCHAR2(80),  OPERATION                       VARCHAR2(30),  OPTIONS                         VARCHAR2(30),  OBJECT_NODE                     VARCHAR2(128),  OBJECT_OWNER                    VARCHAR2(30),  OBJECT_NAME                     VARCHAR2(30),  OBJECT_INSTANCE                 NUMBER(38),  OBJECT_TYPE                     VARCHAR2(30),  OPTIMIZER                       VARCHAR2(255),  SEARCH_COLUMNS                  NUMBER,  ID                              NUMBER(38),  PARENT_ID                       NUMBER(38),  POSITION                        NUMBER(38),  COST                            NUMBER(38),  CARDINALITY                     NUMBER(38),  BYTES                           NUMBER(38),  OTHER_TAG                       VARCHAR2(255),  PARTITION_START                 VARCHAR2(255),  PARTITION_STOP                  VARCHAR2(255),  PARTITION_ID                    NUMBER(38),  OTHER                           LONG,  DISTRIBUTION                    VARCHAR2(30))");
                    st.close();
                    st = null;
                }
                QueryParser qt = session.getDatabaseProduct().getQueryParser(_editor.getSQLToBeExecuted(),
                        _editor.getSQLLineNumber());
                qt.parse();
                (new ExplainExecution(_editor, qt)).schedule();
            } catch (SQLException e) {
                SQLExplorerPlugin.error("Error creating explain plan", e);
            } catch (ParserException e) {
                SQLExplorerPlugin.error("Cannot parse query", e);
            }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    SQLExplorerPlugin.error("Cannot close result set", e);
                }
            if (connection != null)
                session.releaseConnection(connection);
        } catch (Exception e) {
        }
    }

    static final String createPlanTableScript = "CREATE TABLE PLAN_TABLE (  STATEMENT_ID                    VARCHAR2(30), TIMESTAMP                       DATE,  REMARKS                         VARCHAR2(80),  OPERATION                       VARCHAR2(30),  OPTIONS                         VARCHAR2(30),  OBJECT_NODE                     VARCHAR2(128),  OBJECT_OWNER                    VARCHAR2(30),  OBJECT_NAME                     VARCHAR2(30),  OBJECT_INSTANCE                 NUMBER(38),  OBJECT_TYPE                     VARCHAR2(30),  OPTIMIZER                       VARCHAR2(255),  SEARCH_COLUMNS                  NUMBER,  ID                              NUMBER(38),  PARENT_ID                       NUMBER(38),  POSITION                        NUMBER(38),  COST                            NUMBER(38),  CARDINALITY                     NUMBER(38),  BYTES                           NUMBER(38),  OTHER_TAG                       VARCHAR2(255),  PARTITION_START                 VARCHAR2(255),  PARTITION_STOP                  VARCHAR2(255),  PARTITION_ID                    NUMBER(38),  OTHER                           LONG,  DISTRIBUTION                    VARCHAR2(30))";
}
