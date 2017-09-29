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
package org.talend.cwm.db.connection;

import org.talend.dataquality.PluginConstant;

/**
 * DOC zshen class global comment. help to tidy xquery expression
 */
public final class XQueryExpressionUtil {

    public static final int ROWS_PER_PAGE = 100;

    private static StringBuffer expression = new StringBuffer();

    private static String tableNode;

    private static int startNum = 0;

    private static String whereClause;

    private static String columnNodeNameArraystr = null;

    private static String tableNodeWithFunction;

    private XQueryExpressionUtil() {

    }

    /**
     * 
     * DOC zshen Comment method "toParseXquery". get the key parameter for tableNode、startNum.....
     * 
     * @param xqueryStr
     */
    public static void toParseXquery(String xqueryStr) {
        int beginIndex = 0;
        int endIndex = 0;
        beginIndex = xqueryStr.indexOf("//") + 2;//$NON-NLS-1$
        endIndex = xqueryStr.indexOf(") let $_page_ := ", beginIndex);//$NON-NLS-1$
        tableNode = xqueryStr.substring(beginIndex, endIndex);
        // MOD qiongli 2012-6-20 bug TDQ-5139 in order to query accurately,need to use collection
        int rootNodeEndindex = tableNode.indexOf("/");
        if (rootNodeEndindex != -1) {
            tableNode = tableNode.substring(0, rootNodeEndindex);
        }

        // function.tableNodeWithFunction is a collection function with tableNode.
        int beginIndexTable = xqueryStr.indexOf("$_leres0_ :=") + 13;
        int endIndexTable = xqueryStr.indexOf(" let $_page_ :", beginIndex);
        tableNodeWithFunction = xqueryStr.substring(beginIndexTable, endIndexTable);
        // MOD klliu bug TDQ-3542 revert some code
        beginIndex = xqueryStr.indexOf(PluginConstant.COMMA_STRING, endIndex) + 1;
        endIndex = xqueryStr.indexOf(PluginConstant.COMMA_STRING, beginIndex);
        // ~
        startNum = Integer.parseInt(xqueryStr.substring(beginIndex, endIndex));
        beginIndex = xqueryStr.indexOf(")", endIndex) + 1; //$NON-NLS-1$
        endIndex = xqueryStr.indexOf("return", beginIndex); //$NON-NLS-1$
        whereClause = xqueryStr.substring(beginIndex, endIndex);
        beginIndex = xqueryStr.indexOf(" then ", endIndex) + 6; //$NON-NLS-1$
        endIndex = xqueryStr.indexOf(" else ", beginIndex); //$NON-NLS-1$
        columnNodeNameArraystr = xqueryStr.substring(beginIndex, endIndex);
    }

    /**
     * 
     * DOC zshen Comment method "getExpression".
     * 
     * @return return the xquery statment which use current key parameter.
     */
    public static String getExpression() {
        if (tableNode == null) {
            return null;
        }
        expression.delete(0, expression.length());
        expression.append("let $_leres0_ := ");//$NON-NLS-1$
        // MOD qiongli 2012-6-20 bug TDQ-5139 replace tableNode with tableNodeWithFunction.
        expression.append(tableNodeWithFunction);
        expression.append(" let $_page_ := for $");//$NON-NLS-1$
        expression.append(tableNode);
        expression.append(" in subsequence($_leres0_,");//$NON-NLS-1$
        expression.append(startNum);
        expression.append(PluginConstant.COMMA_STRING);
        expression.append(ROWS_PER_PAGE);
        expression.append(") return <result>{if ($");//$NON-NLS-1$
        expression.append(tableNode);
        expression.append(") then ");//$NON-NLS-1$
        //        expression.append(columnNodeNameArraystr == null ? "$" + tableNode : columnNodeNameArraystr);//$NON-NLS-1$
        expression.append("$" + tableNode);//$NON-NLS-1$

        expression.append(" else <null/>}</result> return insert-before($_page_,0,<totalCount>{count($_leres0_)}</totalCount>)");//$NON-NLS-1$

        return expression.toString();
    }

    public static String getTableNode() {
        return tableNode;
    }

    public static void setTableNode(String tableNodePar) {
        tableNode = tableNodePar;
    }

    public static int getStartNum() {
        return startNum;
    }

    public static void setStartNum(int startNumPar) {
        startNum = startNumPar;
    }

    public static String getNodeNameArraystr() {
        return columnNodeNameArraystr;
    }

    public static void setNodeNameArraystr(String nodeNameArraystr) {
        columnNodeNameArraystr = nodeNameArraystr;
    }

    /**
     * make the value range to increase DOC zshen Comment method "increamVernier".
     */
    public static void increaseVernier() {
        startNum += XQueryExpressionUtil.ROWS_PER_PAGE;
    }

    /**
     * make the value range to decrease DOC zshen Comment method "increamVernier".
     */
    public static void decreaseVernier() {
        if (startNum >= XQueryExpressionUtil.ROWS_PER_PAGE) {
            startNum -= XQueryExpressionUtil.ROWS_PER_PAGE;
        }
    }

    public static String getWhereClause() {
        return whereClause;
    }

    public static void setWhereClause(String whereClause) {
        XQueryExpressionUtil.whereClause = whereClause;
    }

}
