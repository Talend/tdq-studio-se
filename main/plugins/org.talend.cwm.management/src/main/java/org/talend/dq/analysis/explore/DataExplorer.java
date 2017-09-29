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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Abstract class to be used by data explorer subclasses.
 */
public abstract class DataExplorer implements IDataExplorer {

    private static Logger log = Logger.getLogger(DataExplorer.class);

    public static final String MENU_VIEW_VALUES = "View values"; //$NON-NLS-1$

    public static final String MENU_VIEW_ROWS = "View rows"; //$NON-NLS-1$

    public static final String MENU_VIEW_INVALID_ROWS = "View invalid rows"; //$NON-NLS-1$

    public static final String MENU_VIEW_VALID_ROWS = "View valid rows"; //$NON-NLS-1$

    public static final String MENU_VIEW_VALID_VALUES = "View valid values"; //$NON-NLS-1$

    public static final String MENU_VIEW_DETAILED_VALID_VALUES = "View detailed valid values"; //$NON-NLS-1$

    public static final String MENU_VIEW_DETAILED_INVALID_VALUES = "View detailed invalid values"; //$NON-NLS-1$

    public static final String MENU_VIEW_INVALID_VALUES = "View invalid values"; //$NON-NLS-1$

    public static final String MENU_VIEW_MATCH_ROWS = "View match rows"; //$NON-NLS-1$

    public static final String MENU_VIEW_NOT_MATCH_ROWS = "View not match rows"; //$NON-NLS-1$

    public static final String MENU_ROWS_IN_RANGE = "View rows in range"; //$NON-NLS-1$

    public static final String MENU_ROWS_OUTSIDE_RANGE = "View rows outside of the range"; //$NON-NLS-1$

    protected static final String SELECT_ALL = "SELECT * "; //$NON-NLS-1$

    private static final String SELECT = "SELECT "; //$NON-NLS-1$

    private static final String SELECT_DISTINCT = "SELECT DISTINCT "; //$NON-NLS-1$

    protected Analysis analysis;

    protected Indicator indicator;

    protected DbmsLanguage dbmsLanguage;

    protected IndicatorEnum indicatorEnum;

    private static final boolean SHOW_COMMENT_BEFORE_STATEMENT = true;

    /**
     * Column name with quotes.
     */
    protected String columnName;

    protected ChartDataEntity entity;

    /**
     * DOC scorreia DataExplorer constructor comment.
     */
    public DataExplorer() {
        super();
    }

    /**
     * get the comment of analysis, if analysis is null, return "".
     * 
     * @param showing
     * @return
     */
    protected String getComment(String showing) {
        if (!SHOW_COMMENT_BEFORE_STATEMENT) {
            return PluginConstant.EMPTY_STRING;
        }
        StringBuffer sb = new StringBuffer();
        if (this.analysis != null) {
            String anaName = StringUtils.defaultString(this.analysis.getName());
            AnalysisType analysisType = AnalysisHelper.getAnalysisType(this.analysis);
            String anaType = analysisType == null ? PluginConstant.EMPTY_STRING : StringUtils.defaultString(analysisType
                    .getLiteral());
            String anaPurpose = AnalysisHelper.getPurpose(this.analysis);
            String anaDescription = AnalysisHelper.getDescription(this.analysis);
            String aeName = PluginConstant.EMPTY_STRING;
            String indName = AnalysisExecutorHelper.getIndicatorName(indicator);
            if (this.indicator != null) {
                ModelElement analyzedElement = this.indicator.getAnalyzedElement();
                if (analyzedElement != null) {
                    aeName = StringUtils.defaultString(analyzedElement.getName());
                }
            }

            showing = StringUtils.defaultString(showing);
            //            sb.append("/*\n"); //$NON-NLS-1$
            //            sb.append("Analysis: " + anaName + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("Type of Analysis: " + anaType + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("Purpose: " + anaPurpose + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("Description: " + anaDescription + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("AnalyzedElement: " + aeName + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("Indicator: " + indName + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("Showing: " + showing + "\n"); //$NON-NLS-1$  //$NON-NLS-2$
            //            sb.append("*/\n"); //$NON-NLS-1$

            sb.append("-- " + Messages.getString("DataExplorer.AnalysisLabel") + " " + anaName + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append("-- " + Messages.getString("DataExplorer.TypeAnalysisLabel") + " " + anaType + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append("-- " + Messages.getString("DataExplorer.PurposeLabel") + " " + anaPurpose + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append("-- " + Messages.getString("DataExplorer.DescriptionLabel") + " " + anaDescription.replace("\n", "\n--     ") + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            sb.append("-- " + Messages.getString("DataExplorer.AnalyzedElementLabel") + " " + aeName + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append("-- " + Messages.getString("DataExplorer.IndicatorLabel") + " " + indName + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.append("-- " + Messages.getString("DataExplorer.ShowingLabel") + " " + showing + " ;\n"); //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        }
        return sb.toString();
    }

    /**
     * Method "getRowsStatement".
     * 
     * @param whereClause the WHERE clause of the statement
     * @return the full SELECT statement with the WHERE clause
     */
    protected String getRowsStatement(String whereClause) {
        String fromClause = getFromClause();
        if (fromClause == null) {
            return null;
        }
        if (whereClause != null) {
            String where = fromClause.contains(dbmsLanguage.where()) ? dbmsLanguage.and() : dbmsLanguage.where();
            return SELECT_ALL + fromClause + where + whereClause;
        } else {
            return SELECT_ALL + fromClause;
        }
    }

    /**
     * Method "getRowsStatement".
     * 
     * @param whereClause the WHERE clause of the statement
     * @param colName the name of select column
     * @return the SELECT statement(only select column) with the WHERE clause
     */
    protected String getValuesStatement(String colName, String whereClause) {
        String fromClause = getFromClause();
        if (fromClause == null) {
            return null;
        }
        if (whereClause != null) {
            String where = fromClause.contains(dbmsLanguage.where()) ? dbmsLanguage.and() : dbmsLanguage.where();
            return SELECT + columnName + fromClause + where + whereClause;
        } else {
            return SELECT + columnName + fromClause;
        }
    }

    protected String getRowsStatement() {
        return getRowsStatement(null);
    }

    protected String getValuesStatement(String columnName) {
        String fromClause = getFromClause();
        if (fromClause == null) {
            return null;
        }
        return SELECT + columnName + fromClause;
    }

    /**
     * DOC zqin Comment method "getFromClause".
     * 
     * @return
     */
    protected String getFromClause() {
        String instantiatedSQL = getIndicatorExpressionSQL();
        if (instantiatedSQL == null) {
            return instantiatedSQL;
        }
        int b = instantiatedSQL.indexOf(this.dbmsLanguage.from());
        String fromClause = instantiatedSQL.substring(b);
        return fromClause;
    }

    /**
     * DOC msjian Comment method "getIndicatorExpressionSQL".
     * 
     * @return
     */
    protected String getIndicatorExpressionSQL() {
        Expression instantiatedExpression = dbmsLanguage.getInstantiatedExpression(indicator);
        return instantiatedExpression == null ? null : instantiatedExpression.getBody();
    }

    public boolean setAnalysis(Analysis analysis) {
        this.analysis = analysis;
        AnalysisContext context = this.analysis.getContext();
        if (context == null) {
            log.error(Messages.getString("DataExplorer.ANALYSISCONTEXTISNULL", analysis.getName()));//$NON-NLS-1$
            return false;
        }
        DataManager dataManager = context.getConnection();
        if (dataManager == null) {
            log.error(Messages.getString("DataExplorer.NOCONNFOUND") + analysis.getName());//$NON-NLS-1$ 
            return false;
        }
        this.dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(dataManager);
        return true;
    }

    public void setEnitty(ChartDataEntity entity) {
        this.entity = entity;
        this.indicator = entity.getIndicator();
        this.indicatorEnum = IndicatorEnum.findIndicatorEnum(indicator.eClass());
        this.columnName = getAnalyzedElementName(indicator);
    }

    /**
     * 
     * Add qiongli handle ColumnSetMultiValueIndicator.
     * 
     * @return
     */
    protected String getAnalyzedElementName(Indicator ind) {
        if (ind.getAnalyzedElement() != null) {
            return dbmsLanguage.quote(ind.getAnalyzedElement().getName());
        }
        StringBuffer name = new StringBuffer(PluginConstant.EMPTY_STRING);
        EObject object = ind.eContainer();
        EList<ModelElement> eLs = null;
        if (object != null && object instanceof ColumnSetMultiValueIndicator) {
            eLs = ((ColumnSetMultiValueIndicator) object).getAnalyzedColumns();

        } else if (ind instanceof AllMatchIndicator) {
            eLs = ((AllMatchIndicator) ind).getAnalyzedColumns();
        }
        if (eLs != null && !eLs.isEmpty()) {
            for (ModelElement mColumn : eLs) {
                name.append(dbmsLanguage.quote(mColumn.getName())).append(",");//$NON-NLS-1$ 
            }
            if (eLs.size() > 0) {
                return org.apache.commons.lang.StringUtils.removeEnd(name.toString(), ",");//$NON-NLS-1$ 
            }
        }

        return name.toString();
    }

    /**
     * DOC bZhou Comment method "getFullyQualifiedTableName".
     * 
     * @param column
     * @return
     */
    protected String getFullyQualifiedTableName(ModelElement column) {
        final ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(column);
        return getFullyQualifiedTableName(columnSetOwner);
    }

    /**
     * DOC bZhou Comment method "getFullyQualifiedTableName".
     * 
     * @param set
     * @return
     */
    protected String getFullyQualifiedTableName(ColumnSet set) {
        Schema parentSchema = SchemaHelper.getParentSchema(set);
        Catalog parentCatalog = CatalogHelper.getParentCatalog(set);
        if (parentSchema != null) {
            parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
        }

        String schemaName = parentSchema == null ? null : parentSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();
        return dbmsLanguage.toQualifiedName(catalogName, schemaName, set.getName());
    }

    /**
     * DOC scorreia Comment method "getRowsStatementWithSubQuery".
     * 
     * @return
     */
    protected String getRowsStatementWithSubQuery() {
        String fromClause = getFromClause();
        if (fromClause == null) {
            return null;
        }
        TdColumn column = (TdColumn) indicator.getAnalyzedElement();
        String table = getFullyQualifiedTableName(column);
        // MOD qiongli 2010-10-28.bug 16658 ,add it if has data filter.
        String queryStr = " SELECT * FROM " + table + dbmsLanguage.where() + columnName + dbmsLanguage.in() //$NON-NLS-1$
                + inBrackets("SELECT " + columnName + fromClause); //$NON-NLS-1$
        // MOD yyi 2011-11-15 3952:avoid adding AND when the where clause is empty.
        return fromClause.contains(dbmsLanguage.where()) && !PluginConstant.EMPTY_STRING.equals(getDataFilterClause()) ? queryStr
                + dbmsLanguage.and() + getDataFilterClause() : queryStr;
    }

    protected String getDistinctValuesStatement(String columnName) {
        String fromClause = getFromClause();
        if (fromClause == null) {
            return null;
        }
        return SELECT_DISTINCT + columnName + fromClause;
    }

    /**
     * Method "getDataFilterClause".
     * 
     * @return the data filter string which represents a where clause (without the where keyword)
     */
    protected String getDataFilterClause() {
        String where = ContextHelper.getDataFilterWithoutContext(analysis);
        return where != null ? where : PluginConstant.EMPTY_STRING;
    }

    /**
     * Method "andDataFilterClause".
     * 
     * @return the data filter string to add to a where clause. The returned string starts with AND if a clause exists.
     * Otherwise it is empty.
     */
    protected String andDataFilterClause() {
        String dataFilter = getDataFilterClause();
        if (dataFilter.length() == 0) {
            return PluginConstant.EMPTY_STRING;
        }
        return dbmsLanguage.and() + inBrackets(dataFilter);
    }

    /**
     * Method "inBrackets".
     * 
     * @param clause
     * @return the given clause surrounded by parenthesis.
     */
    public String inBrackets(String clause) {
        return " (" + clause + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.IDataExplorer#getQueryMap()
     */
    public Map<String, String> getQueryMap() {
        if (NotShowMenu()) {
            return new HashMap<String, String>();
        }

        return getSubClassQueryMap();
    }

    /**
     * when return ture, will not show the drill down menus.
     * 
     * @return
     */
    protected boolean NotShowMenu() {
        return false;
    }

    /**
     * DOC msjian Comment method "getSubClassQueryMap".
     * 
     * @return
     */
    public abstract Map<String, String> getSubClassQueryMap();

}
