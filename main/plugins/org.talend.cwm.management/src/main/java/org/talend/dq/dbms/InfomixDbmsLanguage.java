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
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class InfomixDbmsLanguage extends DbmsLanguage {

    final String replaceColumnAliase = "replace_column"; //$NON-NLS-1$

    final String soundexColumnAliase = "soundex_column_result"; //$NON-NLS-1$

    public static final String AS_REPLACE_COLUMN = "as replace_column";//$NON-NLS-1$

    private static final String COLON = ":"; //$NON-NLS-1$

    InfomixDbmsLanguage() {
        super(DbmsLanguage.INFOMIX);
    }

    InfomixDbmsLanguage(String dbmsType) {
        super(dbmsType);
    }

    InfomixDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalogDelimiter()
     */
    @Override
    protected String getCatalogDelimiter() {
        return COLON;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {

        return query.replaceFirst("SELECT", "SELECT FIRST " + n); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Deprecated
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
                + ",'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9')" + ",'7','9'),'8','9'),'0','9') as " + replaceColumnAliase; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        assert charsToReplace != null && replacementChars != null && charsToReplace.length() == replacementChars.length();
        for (int i = 0; i < charsToReplace.length(); i++) {
            final char charToReplace = charsToReplace.charAt(i);
            final char replacement = replacementChars.charAt(i);
            expression = replaceOneChar(expression, charToReplace, replacement);
        }
        return expression + " as " + replaceColumnAliase; //$NON-NLS-1$
    }

    @Override
    public String fillGenericQueryWithColumnTableAndAlias(String genericQuery, String columns, String table, String groupByAliases) {
        if (columns.indexOf(this.replaceColumnAliase) > -1) {
            String fromCaluse = "(SELECT " + columns + " FROM " + table + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return new GenericSQLHandler(genericQuery).replaceColumnTableAlias(this.replaceColumnAliase, fromCaluse,
                    this.replaceColumnAliase).getSqlString();
        } else if (table.indexOf(this.soundexColumnAliase) > -1) {
            while (genericQuery.toUpperCase().indexOf("SOUNDEX(" + GenericSQLHandler.COLUMN_NAMES + ")") > -1) { //$NON-NLS-1$ //$NON-NLS-2$
                genericQuery = genericQuery.toUpperCase().replace("SOUNDEX(" + GenericSQLHandler.COLUMN_NAMES + ")", //$NON-NLS-1$ //$NON-NLS-2$
                        this.soundexColumnAliase);
            }
        } else {
            groupByAliases = computeAliasesIndex(columns, groupByAliases);
        }
        return super.fillGenericQueryWithColumnTableAndAlias(genericQuery, columns, table, groupByAliases);

    }

    private String computeAliasesIndex(String columns, String groupByAliases) {
        String groupByIndex = groupByAliases;
        if (null == columns || columns.equals("*") || columns.equals(PluginConstant.EMPTY_STRING)) { //$NON-NLS-1$
            return groupByAliases;

        } else if (columns.indexOf(groupByAliases) > -1) {
            String[] aliasArray = groupByAliases.split(" , "); //$NON-NLS-1$
            if (aliasArray.length != 0) {
                groupByIndex = PluginConstant.EMPTY_STRING;
            }
            for (int i = 0; i < aliasArray.length; i++) {
                if (columns.contains(aliasArray[i])) {
                    groupByIndex += String.valueOf(i + 1);
                    if (i != aliasArray.length - 1) {
                        groupByIndex += PluginConstant.COMMA_STRING;
                    }
                }
            }
        }
        return groupByIndex;

    }

    @Override
    public String getSoundexFunction(String table, String colName) {
        String tableName = table;
        tableName = "(select " //$NON-NLS-1$
                + colName
                + ",first_char||second_char||rpad(replace_str2,1,'0')||rpad(replace(substring(rpad(replace_str2,100,'0') from 2),substring(rpad(replace_str2,100,'0') from 1 for 1),''),1,'0') as " //$NON-NLS-1$
                + this.soundexColumnAliase
                + " from (select " //$NON-NLS-1$
                + colName
                + ", first_char,rpad(replace_str,1,'0') as second_char,replace(substring(replace_str from 2),substring(replace_str from 1 for 1),'') as replace_str2 from(select " //$NON-NLS-1$
                + colName
                + ", substring(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(" //$NON-NLS-1$
                + colName
                + ",'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')) from 1 for 1)as first_char,rpad(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(substring(rpad(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(" //$NON-NLS-1$
                + colName
                + ",'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')),100,'0') from 2),'A','0'),'E','0'),'H','0'),'I','0'),'O','0'),'U','0'),'W','0'),'Y','0'),'B','1'),'F','1'),'P','1'),'V','1'),'C','2'),'G','2'),'J','2'),'K','2'),'Q','2'),'S','2'),'X','2'),'Z','2'),'D','3'),'T','3'),'L','4'),'M','5'),'N','5'),'R','6'),'0',''),100,'0') as replace_str from " //$NON-NLS-1$
                + table + ")))"; //$NON-NLS-1$
        return tableName;
    }

    @Override
    public String getFreqRowsStatement(String colName, String table, String key) {
        String sqlStatment = "select distinct t3.* from (select  " //$NON-NLS-1$
                + colName
                + " ,first_char||second_char||rpad(replace_str2,1,'0')||rpad(replace(substring(rpad(replace_str2,100,'0') from 2),substring(rpad(replace_str2,100,'0') from 1 for 1),''),1,'0') as soundex_column_result from (select  " //$NON-NLS-1$
                + colName
                + " , first_char,rpad(replace_str,1,'0') as second_char,replace(substring(replace_str from 2),substring(replace_str from 1 for 1),'') as replace_str2 from(select  " //$NON-NLS-1$
                + colName
                + " , substring(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( " //$NON-NLS-1$
                + colName
                + " ,'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')) from 1 for 1)as first_char,rpad(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(substring(rpad(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( " //$NON-NLS-1$
                + colName
                + " ,'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')),100,'0') from 2),'A','0'),'E','0'),'H','0'),'I','0'),'O','0'),'U','0'),'W','0'),'Y','0'),'B','1'),'F','1'),'P','1'),'V','1'),'C','2'),'G','2'),'J','2'),'K','2'),'Q','2'),'S','2'),'X','2'),'Z','2'),'D','3'),'T','3'),'L','4'),'M','5'),'N','5'),'R','6'),'0',''),100,'0') as replace_str from  " //$NON-NLS-1$
                + table
                + " ))) as t1,(select  " //$NON-NLS-1$
                + colName
                + " ,first_char||second_char||rpad(replace_str2,1,'0')||rpad(replace(substring(rpad(replace_str2,100,'0') from 2),substring(rpad(replace_str2,100,'0') from 1 for 1),''),1,'0') as soundex_column_result from (select  " //$NON-NLS-1$
                + colName
                + " , first_char,rpad(replace_str,1,'0') as second_char,replace(substring(replace_str from 2),substring(replace_str from 1 for 1),'') as replace_str2 from(select  " //$NON-NLS-1$
                + colName
                + " , substring(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( " //$NON-NLS-1$
                + colName
                + " ,'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')) from 1 for 1)as first_char,rpad(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(substring(rpad(UPPER(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( " //$NON-NLS-1$
                + colName
                + " ,'1',''),'2',''),'3',''),'4',''),'5',''),'6',''),'7',''),'8',''),'9',''),'0','')),100,'0') from 2),'A','0'),'E','0'),'H','0'),'I','0'),'O','0'),'U','0'),'W','0'),'Y','0'),'B','1'),'F','1'),'P','1'),'V','1'),'C','2'),'G','2'),'J','2'),'K','2'),'Q','2'),'S','2'),'X','2'),'Z','2'),'D','3'),'T','3'),'L','4'),'M','5'),'N','5'),'R','6'),'0',''),100,'0') as replace_str from  " //$NON-NLS-1$
                + table + " ))) as t2 ," //$NON-NLS-1$
                + table + " as t3 where t1." + colName + "='" + key //$NON-NLS-1$//$NON-NLS-2$
                + "' and t2.soundex_column_result=t1.soundex_column_result and t2." + colName + "=t3." + colName //$NON-NLS-1$//$NON-NLS-2$
                + "and t3." + colName + "='" + key + "'"//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                + PluginConstant.EMPTY_STRING;
        return sqlStatment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        return dateGrain.getName() + surroundWith('(', colName, ')');
    }

    /**
     * DOC yyi 2011-07-07 22246:view rows for average length for Oracle
     * 
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$ 
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or SUBSTR(" + columnName + ",0,1) not in ('0','1','2','3','4','5','6','7','8','9')";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalog(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected Catalog getCatalog(ModelElement columnSetOwner) {
        // get the schema first
        Schema schema = getSchema(columnSetOwner);
        // get the catalog according to the schema
        Catalog catalog = super.getCatalog(schema);
        return catalog;
    }

    @Override
    public String extractQuarter(String colName) {
        return "FLOOR(((" + extract(DateGrain.MONTH, colName) + "-1)/3)+1)"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String extractWeek(String colName) {
        String columnDate = "DATE(" + colName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        String day1 = "MDY(1, 1, YEAR(" + colName + "))"; //$NON-NLS-1$ //$NON-NLS-2$
        String nbdays = columnDate + "-" + day1; //$NON-NLS-1$
        return "1+FLOOR((" + nbdays + "+WEEKDAY(" + day1 + "))/7)"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRandomQuery(java.lang.String)
     */
    @Override
    public String getRandomQuery(String query) {
        // TODO: need to implement this
        return query;
    }
}
