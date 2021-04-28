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
package org.talend.dq.analysis.explore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.RowMatchingIndicator;
import org.talend.dq.dbms.BigQueryDbmsLanguage;
import org.talend.dq.dbms.HiveDbmsLanguage;
import org.talend.dq.helper.ContextHelper;

import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC hcheng class global comment. Detailled comment
 */
public class RowMatchExplorer extends DataExplorer {
	
	private boolean ignoreNull = false;
	
	

    @Override
	public boolean setAnalysis(Analysis analysis) {
    	ignoreNull = TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_IGNORE_NULL, analysis);
		return super.setAnalysis(analysis);
	}

	@Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new HashMap<String, String>();
        // MOD qiongli 2012-8-14 TDQ-5907 Hive dosen't support 'NOT IN'
        if (!(dbmsLanguage instanceof HiveDbmsLanguage)) {
            map.put(MENU_VIEW_MATCH_ROWS, getComment(MENU_VIEW_MATCH_ROWS) + getRowsMatchStatement());
        }
        map.put(MENU_VIEW_NOT_MATCH_ROWS, getComment(MENU_VIEW_NOT_MATCH_ROWS) + getRowsNotMatchStatement());
        map.put(MENU_VIEW_ROWS, getComment(MENU_VIEW_ROWS) + getAllRowsStatement());
        return map;
    }

    /**
     * get Rows for NotMatched Statement.
     *
     * @return
     */
    public String getRowsNotMatchStatement() {

        ColumnSet tablea = (ColumnSet) indicator.getAnalyzedElement();
        String tableA = tablea.getName();
        String query = "SELECT A.*" + dbmsLanguage.from();//$NON-NLS-1$
        if (ColumnsetPackage.eINSTANCE.getRowMatchingIndicator() == indicator.eClass()) {
            ColumnSet tableb = ColumnHelper.getColumnOwnerAsColumnSet(((RowMatchingIndicator) indicator).getColumnSetB().get(0));
            String tableB = tableb.getName();
            EList<TdColumn> columnSetA = ((RowMatchingIndicator) indicator).getColumnSetA();
            EList<TdColumn> columnSetB = ((RowMatchingIndicator) indicator).getColumnSetB();

            String clauseA = " (SELECT *" + dbmsLanguage.from() + getFullyQualifiedTableName(tablea);//$NON-NLS-1$
            String clauseB = " (SELECT *" + dbmsLanguage.from() + getFullyQualifiedTableName(tableb);//$NON-NLS-1$
            String where = null;
            String onClause = " ON ";//$NON-NLS-1$
            String realWhereClause = dbmsLanguage.where();
            if (ignoreNull) {
           	 	onClause = " WHERE ";//$NON-NLS-1$
           	 	query = StringUtils.EMPTY;
           	 	clauseB = " SELECT *" + dbmsLanguage.from() + getFullyQualifiedTableName(tableb);//$NON-NLS-1$
            }
            for (int i = 0; i < columnSetA.size(); i++) {
                where = dbmsLanguage.and();
                if (i == 0) {
                    where = dbmsLanguage.where();
                } else {
                    onClause += where;
                    realWhereClause += where;
                }

                realWhereClause += " B" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetB.get(i).getName())//$NON-NLS-1$
                        + dbmsLanguage.isNull();

                onClause += this.ignoreNull ? " (" : "";
                onClause += " (A" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetA.get(i).getName()) + "=" + " B"//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                        + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetB.get(i).getName()) + ") ";//$NON-NLS-1$
                if(ignoreNull) {
                	onClause += " OR (A" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetA.get(i).getName()) + "  IS NULL " + dbmsLanguage.and() + //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                			" B" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetB.get(i).getName()) + " IS NULL )) " ;//$NON-NLS-1$
                }
            }
            if (ignoreNull) {
            	clauseA += ") A";//$NON-NLS-1$
            	clauseB += " B ";//$NON-NLS-1$
            } else {
            clauseA += (tableA.equals(tableB) ? whereDataFilter(tableA,
                    (getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_A
                            : AnalysisHelper.DATA_FILTER_B)) : whereDataFilter(tableA, null))
                    + ") A";//$NON-NLS-1$

            clauseB += (tableB.equals(tableA) ? whereDataFilter(tableB,
                    (getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_B
                            : AnalysisHelper.DATA_FILTER_A)) : whereDataFilter(tableB, null)) + ") B";
			
            }
			if (!ignoreNull) {
				// MOD qiongli 2012-8-14 TDQ-5907.
				if (dbmsLanguage instanceof HiveDbmsLanguage) {
					query += clauseA + " LEFT OUTER JOIN " + clauseB + onClause + realWhereClause;//$NON-NLS-1$
				} else {
					query += clauseA + " LEFT JOIN " + clauseB + onClause + realWhereClause;//$NON-NLS-1$
				}
			} else {
				query += "SELECT * " + dbmsLanguage.from() + getFullyQualifiedTableName(tablea) //$NON-NLS-1$
						+ " A WHERE  NOT EXISTS (( " + clauseB + onClause + "))";//$NON-NLS-1$
				query += (tableA.equals(tableB) ? andDataFilter(tableA,
						(getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_A
								: AnalysisHelper.DATA_FILTER_B)) : andDataFilter(tableA, null));

			}
        }
        return getComment(MENU_VIEW_NOT_MATCH_ROWS) + query;
    }

    /**
     * get Rows for Matched Statement.
     *
     * @return
     */
    public String getRowsMatchStatement() {
        ColumnSet tablea = (ColumnSet) indicator.getAnalyzedElement();
        String tableA = tablea.getName();
        String query = PluginConstant.EMPTY_STRING;
        if (ColumnsetPackage.eINSTANCE.getRowMatchingIndicator() == indicator.eClass()) {
            ColumnSet tableb = ColumnHelper.getColumnOwnerAsColumnSet(((RowMatchingIndicator) indicator).getColumnSetB().get(0));
            String tableB = tableb.getName();
            EList<TdColumn> columnSetA = ((RowMatchingIndicator) indicator).getColumnSetA();
            EList<TdColumn> columnSetB = ((RowMatchingIndicator) indicator).getColumnSetB();

            String fullyQualifiedTableAName = getFullyQualifiedTableName(tablea);
            String clauseA = " (SELECT *" + dbmsLanguage.from() + fullyQualifiedTableAName;//$NON-NLS-1$
            String clauseB = " (SELECT *" + dbmsLanguage.from() + getFullyQualifiedTableName(tableb);//$NON-NLS-1$
            String where = null;
            String onClause = " ON ";//$NON-NLS-1$
            if (ignoreNull) {
           	 onClause = " WHERE ";//$NON-NLS-1$
           	 clauseB = " SELECT *" + dbmsLanguage.from() + getFullyQualifiedTableName(tableb);//$NON-NLS-1$
           }
           
            for (int i = 0; i < columnSetA.size(); i++) {
                where = dbmsLanguage.and();
                if (i == 0) {
                    where = dbmsLanguage.where();
                } else {
                    onClause += where;
                }
                //TDQ-19030 ((A.`id`= B.`id2`) OR (A.`id` is null AND B.`id2` is null))
                onClause += this.ignoreNull ? " (" : "";
                onClause += " (A" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetA.get(i).getName()) + "=" + " B"//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                        + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetB.get(i).getName()) + ") " ;//$NON-NLS-1$
                if(ignoreNull) {
                	onClause += " OR (A" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetA.get(i).getName()) + "  IS NULL " + dbmsLanguage.and() + //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                			" B" + dbmsLanguage.getDelimiter() + dbmsLanguage.quote(columnSetB.get(i).getName()) + " IS NULL )) " ;//$NON-NLS-1$
                }
            }
            if (ignoreNull) {
            	clauseA += ") A";//$NON-NLS-1$
            	clauseB += " B ";//$NON-NLS-1$
            } else {
            clauseA += (tableA.equals(tableB) ? whereDataFilter(tableA,
                    (getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_A
                            : AnalysisHelper.DATA_FILTER_B)) : whereDataFilter(tableA, null))
                    + ") A";//$NON-NLS-1$

            clauseB += (tableB.equals(tableA) ? whereDataFilter(tableB,
                    (getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_B
                            : AnalysisHelper.DATA_FILTER_A)) : whereDataFilter(tableB, null))
                    + ") B";//$NON-NLS-1$
            }
			String clause = PluginConstant.EMPTY_STRING;
			if (!ignoreNull) {
				query = "SELECT * FROM " + fullyQualifiedTableAName;//$NON-NLS-1$

				String columnNameByAlias = PluginConstant.EMPTY_STRING;
				for (int i = 0; i < columnSetA.size(); i++) {
					columnNameByAlias += " A" + dbmsLanguage.getDelimiter() //$NON-NLS-1$
							+ dbmsLanguage.quote(columnSetA.get(i).getName());
					if (i != columnSetA.size() - 1) {
						columnNameByAlias += ","; //$NON-NLS-1$
					}
				}
				clause = "(SELECT " + columnNameByAlias + dbmsLanguage.from() + clauseA + " JOIN " + clauseB + onClause //$NON-NLS-1$//$NON-NLS-2$
						+ ")";//$NON-NLS-1$
		           String fullColumnAName = "("; //$NON-NLS-1$

		            for (int j = 0; j < columnSetA.size(); j++) {
		                fullColumnAName += fullyQualifiedTableAName + PluginConstant.DOT_STRING
		                        + dbmsLanguage.quote(columnSetA.get(j).getName());
		                if (j != columnSetA.size() - 1) {
		                    fullColumnAName += ","; //$NON-NLS-1$
		                } else {
		                    fullColumnAName += ")"; //$NON-NLS-1$
		                }
		            }
		            clause = dbmsLanguage.where() + "(" + fullColumnAName + dbmsLanguage.in() + clause;//$NON-NLS-1$
		            query += clause;
		            query += ") ";//$NON-NLS-1$

			} else {
				clause = "SELECT * " + dbmsLanguage.from() + fullyQualifiedTableAName + " A WHERE EXISTS (( " + clauseB + onClause +"))";//$NON-NLS-1$//$NON-NLS-2$
				query += clause;
			}
			query += (tableA.equals(tableB) ? andDataFilter(tableA,
					(getdataFilterIndex(null) == AnalysisHelper.DATA_FILTER_A ? AnalysisHelper.DATA_FILTER_A
							: AnalysisHelper.DATA_FILTER_B)) : andDataFilter(tableA, null));

            // `test_tbd_4452`.`testalltypes_kmo`.`strCol`-->`testalltypes_kmo`.`strCol`
            if (dbmsLanguage instanceof BigQueryDbmsLanguage) {
                query = query.replace(fullyQualifiedTableAName + ".`", tableA + ".`");
            }
        }

        return getComment(MENU_VIEW_MATCH_ROWS) + query;
    }
    /**
     * get All Rows Statement.
     *
     * @return
     */
    public String getAllRowsStatement() {
        ColumnSet tablea = (ColumnSet) indicator.getAnalyzedElement();
        String tableA = tablea.getName();
        ColumnSet tableb = ColumnHelper.getColumnOwnerAsColumnSet(((RowMatchingIndicator) indicator).getColumnSetB().get(0));
        String tableB = tableb.getName();
        return getComment(MENU_VIEW_ROWS)
                + "SELECT * " + dbmsLanguage.from() + getFullyQualifiedTableName(tablea) + whereDataFilter(tableA.equals(tableB) ? null : tableA, null); //$NON-NLS-1$
    }

    /**
     *
     * DOC zshen 2010-01-15 Comment method "getdataFilterIndex".
     *
     * @param tableOrViewName the name of table or view.if null get index of current indicator in analysis
     * @return the index for datafilter. return -1 when can't find
     */
    private int getdataFilterIndex(Object nameOrIndicator) {
        if (nameOrIndicator == null) {
            nameOrIndicator = this.indicator;
        }
        Iterator<Indicator> iter = this.analysis.getResults().getIndicators().iterator();
        int result = 0;
        Object currentObj = null;
        while (iter.hasNext()) {
            Indicator indicator = iter.next();
            if (nameOrIndicator instanceof String) {
                currentObj = indicator.getAnalyzedElement().getName();
            } else {
                currentObj = indicator;
            }
            if (currentObj.equals(nameOrIndicator)) {
                return result;
            } else {
                result++;
            }
        }
        return -1;

    }

    /**
     *
     * DOC zshen Comment method "andDataFilter".
     *
     * @param tableOrViewName the name of table or view
     * @return DataFilter clause
     */
    private String andDataFilter(String tableOrViewName, Integer index) {
        String andTable = null;
        if (index == null) {
            andTable = ContextHelper.getDataFilterWithoutContext(analysis, getdataFilterIndex(tableOrViewName));
        } else {
            andTable = ContextHelper.getDataFilterWithoutContext(analysis, index.intValue());
        }
        if (null != andTable && !andTable.equals(PluginConstant.EMPTY_STRING)) {
            andTable = dbmsLanguage.and() + andTable;
        }
        if (andTable == null) {
            andTable = PluginConstant.EMPTY_STRING;
        }
        return andTable;

    }

    /**
     *
     * DOC zshen Comment method "andDataFilter".
     *
     * @param tableOrViewName the name of table or view.
     * @param index have known index.
     * @return DataFilter clause
     */
    private String whereDataFilter(Object tableOrViewName, Integer index) {
        String andTable = null;
        if (index == null) {
            andTable = ContextHelper.getDataFilterWithoutContext(analysis, getdataFilterIndex(tableOrViewName));
        } else {
            andTable = ContextHelper.getDataFilterWithoutContext(analysis, index.intValue());
        }
        if (null != andTable && !andTable.equals(PluginConstant.EMPTY_STRING)) {
            andTable = dbmsLanguage.where() + andTable;
        }
        if (andTable == null) {
            andTable = PluginConstant.EMPTY_STRING;
        }
        return andTable;

    }
}
