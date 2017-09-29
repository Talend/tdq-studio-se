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
package org.talend.dataquality.helpers;

import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.expressions.BooleanExpressionNode;
import org.talend.dataquality.expressions.ExpressionsFactory;
import orgomg.cwm.foundation.expressions.ExpressionNode;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * @author scorreia
 * 
 * Helper class for handling BooleanExpressionNode.
 */
public final class BooleanExpressionHelper {

    private BooleanExpressionHelper() {
    }

    /**
     * Default language used in expressions.
     */
    public static final String DEFAULT_LANGUAGE = "SQL"; //$NON-NLS-1$

    /**
     * Method "createBooleanExpressionNode".
     * 
     * @param body the body of the Expression
     * @return a BooleanExpressionNode with the given Expression.
     */
    public static BooleanExpressionNode createBooleanExpressionNode(String body) {
        BooleanExpressionNode expr = ExpressionsFactory.eINSTANCE.createBooleanExpressionNode();
        TdExpression expression = createTdExpression(DEFAULT_LANGUAGE, body);
        expr.setExpression(expression);
        return expr;
    }

    public static TdExpression createTdExpression(String language, String body) {
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(body);
        expression.setLanguage(language);
        return expression;
    }

    public static TdExpression createTdExpression(String language, String body, String version) {
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(body);
        expression.setLanguage(language);
        expression.setVersion(version);
        return expression;
    }

    public static ExpressionNode createExpressionNode(String language, String body) {
        ExpressionNode node = orgomg.cwm.foundation.expressions.ExpressionsFactory.eINSTANCE.createExpressionNode();
        node.setExpression(createTdExpression(language, body));
        return node;
    }

    /**
     * Method "getBody".
     * 
     * @param node (must not be null)
     * @return the body string of the expression contained in the given node
     */
    public static String getBody(ExpressionNode node) {
        Expression expression = node.getExpression();
        return (expression == null) ? null : expression.getBody();
    }

    /**
     * Method "createRegularExpression".
     * 
     * @param language
     * @param expression
     * @return a regular expression (no type is set. The caller should set the correct type)
     */
    public static RegularExpression createRegularExpression(String language, String expression) {
        RegularExpression regexp = PatternFactory.eINSTANCE.createRegularExpression();
        regexp.setExpression(createTdExpression(language, expression));
        return regexp;
    }

    /**
     * Method "createRegularExpression".
     * 
     * @param language
     * @param expression
     * @param exprType
     * @return a regular expression of the given type (SQL like or regular expression)
     */
    public static RegularExpression createRegularExpression(String language, String expression, ExpressionType exprType) {
        RegularExpression regexp = PatternFactory.eINSTANCE.createRegularExpression();
        regexp.setExpression(createTdExpression(language, expression));
        regexp.setExpressionType(exprType.getLiteral());
        return regexp;
    }

}
