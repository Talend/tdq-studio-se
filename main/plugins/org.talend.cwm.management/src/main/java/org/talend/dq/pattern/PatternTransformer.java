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
package org.talend.dq.pattern;

import org.talend.dq.dbms.DbmsLanguage;

/**
 * @author scorreia
 * 
 * This class helps to transform patterns found in data into regular expressions.
 */
public class PatternTransformer {

    private static final String BEGIN = "^"; //$NON-NLS-1$

    private static final String END = "$"; //$NON-NLS-1$

    private char lowerCase = 'a';

    private char upperCase = 'A';

    private char numeric = '9';
    
    private String specialChar = ".*?^${}()\\+|[]"; //$NON-NLS-1$

    private DbmsLanguage dbmsLanguage;
    
    
    /**
     * Getter for dbmsLanguage.
     * 
     * @return the dbmsLanguage
     */
    public DbmsLanguage getDbmsLanguage() {
        return this.dbmsLanguage;
    }

    public PatternTransformer(DbmsLanguage dbms) {
        assert dbms != null;
        this.dbmsLanguage = dbms;
    }

    private String getRegexpPattern(char inputCharacter) {
        boolean isJava = !this.dbmsLanguage.isSql();
        if (lowerCase == inputCharacter) {
            return isJava ? "[a-z]" : "[[:lower:]]"; //$NON-NLS-1$
        }
        if (upperCase == inputCharacter) {
            return isJava ? "[A-Z]" : "[[:upper:]]"; //$NON-NLS-1$
        }
        if (numeric == inputCharacter) {
            return isJava ? "[0-9]" : "[[:digit:]]"; //$NON-NLS-1$
        }
        if (specialChar.contains(String.valueOf(inputCharacter))) {
            return "\\" + inputCharacter; //$NON-NLS-1$
        }
        return String.valueOf(inputCharacter);        
    }

    /**
     * Method "getRegexp".
     * 
     * @param input a pattern string where each letter has the meaning of a character class
     * @return a regular expression built from the input pattern
     */
    public String getRegexp(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(BEGIN);
        final int length = input.length();
        for (int i = 0; i < length; i++) {
            builder.append(this.getRegexpPattern(input.charAt(i)));
        }
        builder.append(END);
        return builder.toString();
    }
    
    /**
     * Getter for lowerCase.
     * 
     * @return the lowerCase
     */
    public char getLowerCase() {
        return this.lowerCase;
    }

    /**
     * Sets the lowerCase.
     * 
     * @param lowerCase the lowerCase to set
     */
    public void setLowerCase(char lowerCase) {
        this.lowerCase = lowerCase;
    }

    /**
     * Getter for upperCase.
     * 
     * @return the upperCase
     */
    public char getUpperCase() {
        return this.upperCase;
    }

    /**
     * Sets the upperCase.
     * 
     * @param upperCase the upperCase to set
     */
    public void setUpperCase(char upperCase) {
        this.upperCase = upperCase;
    }

    /**
     * Getter for numeric.
     * 
     * @return the numeric
     */
    public char getNumeric() {
        return this.numeric;
    }

    /**
     * Sets the numeric.
     * 
     * @param numeric the numeric to set
     */
    public void setNumeric(char numeric) {
        this.numeric = numeric;
    }
   
}
