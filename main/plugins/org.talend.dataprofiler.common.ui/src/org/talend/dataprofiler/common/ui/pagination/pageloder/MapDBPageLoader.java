// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.common.ui.pagination.pageloder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.nebula.widgets.pagination.IPageLoader;
import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.nebula.widgets.pagination.collections.PageResult;
import org.talend.cwm.indicator.ColumnFilter;
import org.talend.cwm.indicator.DataValidation;
import org.talend.dataprofiler.common.ui.i18n.Messages;
import org.talend.dataprofiler.common.ui.pagination.controller.PageableWithIndexController;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.MapDBUtils;
import org.talend.dataquality.indicators.validation.DataValidationImpl;

/**
 * created by talend on Aug 14, 2014 Detailled comment
 * 
 */
public class MapDBPageLoader<T> implements IPageLoader<PageResult<Object[]>> {

    private Logger log = Logger.getLogger(MapDBPageLoader.class);
    private AbstractDB<T> db = null;

    private Map<Long, T> indexMap = new HashMap<Long, T>();

    private long itemsSize = 0l;

    private DataValidation dataValidator;

    private ColumnFilter columnFilter;

    private String mapDBKey;

    private Indicator indicator;

    public MapDBPageLoader(AbstractDB<T> db, DataValidation validator, long size) {
        this(db, validator, size, null);

    }

    public MapDBPageLoader(AbstractDB<T> db, DataValidation validator, long size, ColumnFilter filter) {
        this.db = db;
        itemsSize = size;
        dataValidator = validator;
        columnFilter = filter;
    }

    public MapDBPageLoader(AbstractDB<T> db) {
        this(db, null, Long.valueOf(db.size()));
    }

    public MapDBPageLoader(AbstractDB<T> db, DataValidation validator, long size, String mapDBKey, Indicator indicator) {
        this(db, validator, size, null);
        this.mapDBKey = mapDBKey;
        this.indicator = indicator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.nebula.widgets.pagination.IPageLoader#loadPage(org.eclipse.nebula.widgets.pagination.PageableController
     * )
     */
    @Override
    public PageResult<Object[]> loadPage(PageableController controller) {
        long totalSize = itemsSize;
        long pageSize = controller.getPageSize();
        long pageIndex = controller.getPageOffset();

        long fromIndex = pageIndex;
        long toIndex = pageIndex + pageSize;
        if (toIndex > totalSize) {
            toIndex = totalSize;
        }
        // if the mapDB is closed, reconnect it by key/indicator
        if (db.isClosed()) {
            if (StringUtils.isEmpty(mapDBKey) || indicator == null) {
                log.error(Messages.getString("MapDBPageLoader.MapDBIsClosed"));
                return new PageResult<Object[]>(new ArrayList<Object[]>(), 0);
            }
            db = (AbstractDB<T>) MapDBUtils.getMapDB(mapDBKey, indicator);
        }
        if (dataValidator == null) {
            return MapDBPageListHelper.createPage(db, indexMap, columnFilter, fromIndex, toIndex, totalSize);
        } else {
            // synchronized sort information
            if (controller instanceof PageableWithIndexController) {
                ((DataValidationImpl) dataValidator).setProperties(((PageableWithIndexController) controller)
                        .getPropertiesStr());
                ((DataValidationImpl) dataValidator).synSortState(controller.getSortPropertyName(),
                        controller.getSortDirection());
            }
            return MapDBPageListHelper.createPage(db, indexMap, dataValidator, fromIndex, toIndex, totalSize);
        }
    }
}
