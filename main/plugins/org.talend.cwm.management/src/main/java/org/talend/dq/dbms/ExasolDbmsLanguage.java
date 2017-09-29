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
package org.talend.dq.dbms;

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class ExasolDbmsLanguage extends DbmsLanguage {

    ExasolDbmsLanguage() {
        super(DbmsLanguage.EXASOLUTION);
    }

    /**
     * DOC bzhou MySQLDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param dbVersion
     */
    ExasolDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
        return StringUtils.repeat("REPLACE(", 59) + expression //$NON-NLS-1$
                + ",'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A')" //$NON-NLS-1$
                + ",'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A')" //$NON-NLS-1$
                + ",'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A')" //$NON-NLS-1$
                + ",'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a')" //$NON-NLS-1$
                + ",'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a')" //$NON-NLS-1$
                + ",'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a')" //$NON-NLS-1$
                + ",'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a')" //$NON-NLS-1$
                + ",'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9')" + ",'7','9'),'8','9'),'0','9')"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        assert charsToReplace != null && replacementChars != null && charsToReplace.length() == replacementChars.length();
        for (int i = 0; i < charsToReplace.length(); i++) {
            final char charToReplace = charsToReplace.charAt(i);
            final char replacement = replacementChars.charAt(i);
            expression = replaceOneChar(expression, charToReplace, replacement);
        }
        return expression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String getRegularExpressionFunction() {
        return "REGEXP_LIKE"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        String toNumberToChar = "TO_NUMBER(TO_CHAR("; //$NON-NLS-1$
        switch (dateGrain.getValue()) {
        case DateGrain.DAY_VALUE:
            return toNumberToChar + colName + ", 'DD'))"; //$NON-NLS-1$
        case DateGrain.WEEK_VALUE:
            return toNumberToChar + colName + ", 'IW'))"; //$NON-NLS-1$
        case DateGrain.MONTH_VALUE:
            return toNumberToChar + colName + ",'MM'))"; //$NON-NLS-1$
        case DateGrain.QUARTER_VALUE:
            return toNumberToChar + colName + ",'Q'))"; //$NON-NLS-1$
        case DateGrain.YEAR_VALUE:
            return toNumberToChar + colName + ", 'YYYY'))"; //$NON-NLS-1$
        default:
            return dateGrain.getName() + surroundWith('(', colName, ')');
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#regexLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexLike(String element, String regex) {
        return surroundWithSpaces(element + surroundWithSpaces(getRegularExpressionFunction()) + regex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexNotLike(String element, String regex) {
        return surroundWithSpaces(element + surroundWithSpaces(this.not() + getRegularExpressionFunction()) + regex);
    }

}
