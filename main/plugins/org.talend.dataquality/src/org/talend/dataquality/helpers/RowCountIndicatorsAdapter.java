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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.impl.RowCountIndicatorImpl;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class RowCountIndicatorsAdapter extends RowCountIndicatorImpl {

    private Set<RowCountIndicator> rowCountIndiSet = new HashSet<>();

    private static RowCountIndicatorsAdapter instance = null;

    private RowCountIndicatorsAdapter() {
        clear();
    }

    /**
     * Getter for instance.
     * 
     * @return the instance
     */
    public static RowCountIndicatorsAdapter getInstance() {
        if (instance == null) {
            instance = new RowCountIndicatorsAdapter();
        }
        return instance;
    }

    /**
     * Getter for rowCountIndiList.
     * 
     * @return the rowCountIndiList
     */
    public Set<RowCountIndicator> getRowCountIndiSet() {
        return this.rowCountIndiSet;
    }

    public void clear() {
        this.rowCountIndiSet.clear();
    }

    public void addRowCountIndicator(RowCountIndicator rci) {
        if (rci instanceof RowCountIndicatorsAdapter) {
            return;
        }
        this.rowCountIndiSet.add(rci);
    }

    /**
     * Sets the rowCountIndiList.
     * 
     * @param rowCountIndiSet the rowCountIndiList to set
     */
    public void setRowCountIndiSet(Set<RowCountIndicator> rowCountIndiSet) {
        this.rowCountIndiSet = rowCountIndiSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getAnalyzedElement()
     */
    @Override
    public ModelElement getAnalyzedElement() {
        if (this.rowCountIndiSet.isEmpty()) {
            return super.getAnalyzedElement();// current there should return null
        } else {
            return getFirstOne().getAnalyzedElement();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getIndicatorDefinition()
     */
    @Override
    public IndicatorDefinition getIndicatorDefinition() {
        if (this.rowCountIndiSet.isEmpty()) {
            return super.getIndicatorDefinition();// current there should return null
        } else {
            return getFirstOne().getIndicatorDefinition();
        }

    }

    private RowCountIndicator getFirstOne() {
        if (this.rowCountIndiSet.isEmpty()) {
            return null;
        } else {
            return this.rowCountIndiSet.toArray(new RowCountIndicator[rowCountIndiSet.size()])[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getParameters()
     */
    @Override
    public IndicatorParameters getParameters() {
        if (this.rowCountIndiSet.isEmpty()) {
            return null;
        } else {
            return getFirstOne().getParameters();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#setInstantiatedExpression(orgomg.cwm.objectmodel.core.Expression)
     */
    @Override
    public boolean setInstantiatedExpression(Expression expression) {
        int index = 0;
        for (RowCountIndicator rci : this.rowCountIndiSet) {
            TdExpression copySqlExpression = BooleanExpressionHelper.createTdExpression(expression.getLanguage(),
                    expression.getBody());
            rci.setInstantiatedExpression(copySqlExpression);
            index++;
        }
        if (index == 0 || index != rowCountIndiSet.size()) {
            return false;
        }
        return super.setInstantiatedExpression(expression);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.util.List)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        int index = 0;
        for (RowCountIndicator rci : this.rowCountIndiSet) {
            rci.storeSqlResults(objects);
            index++;
        }
        if (index == 0 || index != rowCountIndiSet.size()) {
            return false;
        }
        return super.storeSqlResults(objects);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#setComputed(boolean)
     */
    @Override
    public void setComputed(boolean newComputed) {
        for (RowCountIndicator rci : this.rowCountIndiSet) {
            rci.setComputed(newComputed);
        }
        super.setComputed(newComputed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        int index = 0;
        for (RowCountIndicator rci : this.rowCountIndiSet) {
            rci.handle(data);
            index++;
        }
        if (index == 0 || index != rowCountIndiSet.size()) {
            return false;
        }
        return super.handle(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        int index = 0;
        for (RowCountIndicator rci : this.rowCountIndiSet) {
            rci.reset();
            index++;
        }
        if (index == 0 || index != rowCountIndiSet.size()) {
            return false;
        }
        return super.reset();
    }

}
