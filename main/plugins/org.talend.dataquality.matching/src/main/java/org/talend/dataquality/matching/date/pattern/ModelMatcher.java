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
package org.talend.dataquality.matching.date.pattern;

import java.util.regex.Pattern;

/**
 * 
 * @author Hallam Mohamed Amine
 * @date 11/08/2009
 */
public class ModelMatcher implements java.lang.Comparable<ModelMatcher> {

    // model is the pattern like "MM DD YY"
    private String model;

    private int score;

    private final String regex;

    private final Pattern compiledPattern;

    public ModelMatcher(String model, String regex) {
        this.model = model;
        this.regex = regex;
        this.score = 0;

        this.compiledPattern = Pattern.compile(regex);
    }

    public String getRegex() {
        return this.regex;
    }

    public String getModel() {
        return this.model;
    }

    /**
     * @param expression the string to be matched
     * @return true:match , false : don't match
     */
    public boolean matches(String expression) {
        return compiledPattern.matcher(expression).matches();
    }

    public int getScore() {
        return this.score;
    }

    // comparison method used to sort modelMatchers
    public int compareTo(ModelMatcher other) {
        if (this.getScore() > other.getScore())
            return -1;
        else if (this.getScore() == other.getScore())
            return 0;
        else {
            return 1;
        }
    }

    /**
     * Method "increment" increases the score (number of matches).
     */
    public void increment() {
        this.score++;
    }
}
