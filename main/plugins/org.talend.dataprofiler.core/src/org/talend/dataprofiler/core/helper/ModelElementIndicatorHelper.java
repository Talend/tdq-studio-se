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
package org.talend.dataprofiler.core.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.dataprofiler.core.model.DelimitedFileIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.impl.ColumnIndicatorImpl;
import org.talend.dataprofiler.core.model.impl.DelimitedFileIndicatorImpl;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sql.TalendTypeConvert;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class ModelElementIndicatorHelper {

    public final static int COLUMN_NAME_MAX_LENGTH = 30;

    private static Logger log = Logger.getLogger(ModelElementIndicatorHelper.class);

    private ModelElementIndicatorHelper() {
    }

    public static final ModelElementIndicator createModelElementIndicator(IRepositoryNode node) {
        if (node != null) {
            if (node instanceof DBColumnRepNode) {
                return createColumnIndicator(node);
            } else if (node instanceof DFColumnRepNode) {
                return createDFColumnIndicator(node);
            }
        }
        return null;
    }

    public static final ColumnIndicator createColumnIndicator(IRepositoryNode repositoryNode) {
        return new ColumnIndicatorImpl(repositoryNode);
    }

    public static final DelimitedFileIndicator createDFColumnIndicator(IRepositoryNode reposObj) {
        return new DelimitedFileIndicatorImpl(reposObj);
    }

    /**
     *
     * Convert from ModelElementIndicators to ModelElement
     *
     * @param ModelElementIndicators
     * @return
     */
    public static ModelElement[] getModelElementFromMEIndicator(ModelElementIndicator[] ModelElementIndicators) {
        if (ModelElementIndicators == null) {
            return new ModelElement[0];
        }
        ModelElement[] selectedColumns = new ModelElement[ModelElementIndicators.length];
        int index = 0;
        for (ModelElementIndicator modelElemIndi : ModelElementIndicators) {
            IRepositoryViewObject currentObject = modelElemIndi.getModelElementRepositoryNode().getObject();
            if (ISubRepositoryObject.class.isInstance(currentObject)) {
                selectedColumns[index++] = ((ISubRepositoryObject) currentObject).getModelElement();
            }
        }
        return selectedColumns;
    }

    /**
     *
     * Check whether parameters are come from same table in the database
     *
     * @param ModelElementIndicators
     * @return true it is come from same table else it is not
     */
    public static boolean checkSameTable(ModelElementIndicator[] ModelElementIndicators) {
        ModelElement[] modelElements = getModelElementFromMEIndicator(ModelElementIndicators);
        return ColumnHelper.checkSameTable(modelElements);
    }

    /**
     *
     *
     * @deprecated
     *
     * use {@link #switchColumnIndicator(ColumnIndicatorUnit)} instead of it
     * @param indicatorUnit
     * @return
     */
    @Deprecated
    public static final ColumnIndicator switchColumnIndicator(IndicatorUnit indicatorUnit) {
        if (indicatorUnit instanceof ColumnIndicatorUnit) {
            return switchColumnIndicator((ColumnIndicatorUnit) indicatorUnit);
        }
        return null;
    }

    /**
     *
     * Get columns from modelElementIndicators
     *
     * @param modelElementIndicators
     * @return
     */
    public static final List<MetadataColumn> getColumns(ModelElementIndicator[] modelElementIndicators) {
        List<MetadataColumn> columns = new ArrayList<MetadataColumn>();
        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            ColumnIndicator switchColumnIndicator = switchColumnIndicator(modelElementIndicator);
            if (switchColumnIndicator == null) {
                continue;
            }
            columns.add(switchColumnIndicator.getTdColumn());
        }
        return columns;
    }

    /**
     *
     * Get column from modelElementIndicator
     *
     * @param modelElementIndicator
     * @return MetadataColumn if convert is normal else return null
     */
    public static final MetadataColumn getColumn(ModelElementIndicator modelElementIndicator) {
        if (modelElementIndicator == null) {
            return null;
        }
        ColumnIndicator switchColumnIndicator = switchColumnIndicator(modelElementIndicator);
        if (switchColumnIndicator != null) {
            return switchColumnIndicator.getTdColumn();
        }

        // TDQ-10198: support Delimited File connection column
        DelimitedFileIndicator switchDelimitedFileIndicator = switchDelimitedFileIndicator(modelElementIndicator);
        if (switchDelimitedFileIndicator != null) {
            return switchDelimitedFileIndicator.getMetadataColumn();
        }

        return null;
    }

    /**
     *
     * get ColumnIndicator from columnIndicatorUnit
     *
     * @param indicatorUnit
     * @return
     */
    public static final ColumnIndicator switchColumnIndicator(ColumnIndicatorUnit indicatorUnit) {
        if (indicatorUnit.isColumn()) {
            return (ColumnIndicator) indicatorUnit.getModelElementIndicator();
        }
        return null;
    }

    /**
     *
     * get ColumnIndicator from ModelElementIndicator
     *
     * @param indicatorUnit
     * @return
     */
    public static final ColumnIndicator switchColumnIndicator(ModelElementIndicator indicator) {
        if (indicator instanceof ColumnIndicator) {
            return (ColumnIndicator) indicator;
        }
        return null;
    }

    /**
     *
     * get DelimitedFileIndicator from ModelElementIndicator
     *
     * @param indicatorUnit
     * @return
     */
    public static final DelimitedFileIndicator switchDelimitedFileIndicator(ModelElementIndicator indicator) {
        if (indicator instanceof DelimitedFileIndicator) {
            return (DelimitedFileIndicator) indicator;
        }
        return null;
    }

    /**
     *
     * get Connection from ModelElementIndicator
     *
     * @param indicator
     * @return
     */
    public static final Connection getTdDataProvider(ModelElementIndicator indicator) {
        Property property = indicator.getModelElementRepositoryNode().getObject().getProperty();
        if (property != null && property.getItem() instanceof ConnectionItem) {
            return ((ConnectionItem) property.getItem()).getConnection();
        }
        return null;
    }

    /**
     *
     * Get column names from DelimitedFileIndicator
     *
     * @param indicator
     * @return
     */
    public static final List<String> getColumnNameList(DelimitedFileIndicator indicator) {
        try {
            EList<MetadataColumn> columns = indicator.getMetadataColumn().getTable().getColumns();
            List<String> columnNames = new ArrayList<String>();
            for (MetadataColumn columnsElement : columns) {
                columnNames.add(columnsElement.getName());
            }
            return columnNames;
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * DOC xqliu Comment method "getModelElementDisplayName".
     *
     * @param meIndicator
     * @return
     */
    public static final String getModelElementDisplayName(ModelElementIndicator meIndicator) {
        return getModelElementDisplayName(meIndicator, true);
    }

    /**
     * Get the display name of model element
     *
     * @param meIndicator
     * @param isFullName true return complete name else will return apart of name if length more than 30 like aaa...bbb
     * @return EMPTY when any NullPointerException generated
     */
    public static final String getModelElementDisplayName(ModelElementIndicator meIndicator, boolean isFullName) {
        try {
        String meName = meIndicator.getElementName();
        String typeName = StringUtils.EMPTY;// $NON-NLS-1$
        if (meIndicator instanceof ColumnIndicator) {
            TdColumn tdColumn = ((ColumnIndicator) meIndicator).getTdColumn();
            TdSqlDataType sqlDataType = tdColumn.getSqlDataType();
            typeName = sqlDataType != null ? sqlDataType.getName() : "unknown";//$NON-NLS-1$
        } else if (meIndicator instanceof DelimitedFileIndicatorImpl) {
            MetadataColumn mColumn = ((DelimitedFileIndicatorImpl) meIndicator).getMetadataColumn();
            typeName = TalendTypeConvert.convertToJavaType(mColumn.getTalendType());
        }
        String fullNameResult = meName != null
                ? meName + PluginConstant.SPACE_STRING + PluginConstant.PARENTHESIS_LEFT + typeName
                        + PluginConstant.PARENTHESIS_RIGHT
                : "null";//$NON-NLS-1$
        if (isFullName) {
            return fullNameResult;
        } else {
            return getCutOutData(meName, typeName, fullNameResult);
        }
        } catch (NullPointerException e) {
            log.warn(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * Cut out the String as "aaa...bbb (string)" style if length more than 30
     * 
     * @param meName Original column name
     * @param typeName The data type of column
     * @param fullNameResult Original name before cut out
     * @return StringUtils.EMPTY if meName is null or fullNameResult is null or the length of fullNameResult less than
     * meName. Else return a length less than or equal 30 string
     */
    public static String getCutOutData(String meName, String typeName, String fullNameResult) {
        if (fullNameResult == null) {
            return StringUtils.EMPTY;
        }
        int fullLength = fullNameResult.length();
        if (fullLength <= ModelElementIndicatorHelper.COLUMN_NAME_MAX_LENGTH) {
            return fullNameResult;
        }
        if (meName == null || fullNameResult.length() < meName.length()) {
            return StringUtils.EMPTY;
        }
        String realName = meName == null ? StringUtils.EMPTY : meName;
        int realLength = meName.length();
        int parameterLength = fullLength - realLength;
        int displayRealNameLength = ModelElementIndicatorHelper.COLUMN_NAME_MAX_LENGTH - parameterLength;
        String preRealName = realName.substring(0, displayRealNameLength / 2);
        String backRealName = realName
                .substring(realName.length() - (displayRealNameLength - displayRealNameLength / 2) + 3,
                        realName.length());
        return preRealName + "..." + backRealName + PluginConstant.SPACE_STRING + PluginConstant.PARENTHESIS_LEFT
                + (typeName == null ? "unknown" : typeName) + PluginConstant.PARENTHESIS_RIGHT;
    }

    /**
     * Check whether repViewObj and modelElementIndicator is come from same table
     *
     * @param repViewObj
     * @param modelElementIndicators
     */
    public static boolean checkSameTable(MetadataColumnRepositoryObject repViewObj, ModelElementIndicator modelElementIndicator) {
        if (modelElementIndicator == null || repViewObj == null) {
            return false;
        }

        MetadataColumn newColumn = repViewObj.getTdColumn();
        MetadataColumn Oldcolumn = getColumn(modelElementIndicator);

        if (newColumn == null || Oldcolumn == null) {
            return false;
        }
        MetadataTable newMetadataTable = ColumnHelper.getColumnOwnerAsMetadataTable(newColumn);
        MetadataTable oldMetadataTable = ColumnHelper.getColumnOwnerAsMetadataTable(Oldcolumn);

        if (newMetadataTable == null || oldMetadataTable == null) {
            return false;
        }
        if (newMetadataTable.equals(oldMetadataTable)) {
            return true;
        }
        return false;
    }
}
