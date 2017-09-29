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
package org.talend.dq.analysis.explore;

/**
 * Use special where clause for sql patterns, as: 'column name' like 'A%'; or column name not like 'a%' Added TDQ-8864
 * 20140421 yyin
 */
public class SQLPatternExplorer extends PatternExplorer {

    public SQLPatternExplorer() {
        super();
    }

    @Override
    protected String getRegexNotLike(String regexPatternString) {
        return columnName + dbmsLanguage.not() + dbmsLanguage.like() + regexPatternString;
    }

    @Override
    protected String getRegexLike(String regexPatternString) {
        return columnName + dbmsLanguage.like() + regexPatternString;
    }
}
