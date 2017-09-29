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
package org.talend.dataprofiler.core.helper;

import java.util.ArrayList;
import java.util.List;

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
        String meName = meIndicator.getElementName();
        String typeName = "";//$NON-NLS-1$
        if (meIndicator instanceof ColumnIndicator) {
            // MOD scorreia 2010-10-20 bug 16403 avoid NPE here
            TdSqlDataType sqlDataType = ((ColumnIndicator) meIndicator).getTdColumn().getSqlDataType();
            typeName = sqlDataType != null ? sqlDataType.getName() : "unknown";//$NON-NLS-1$
        } else if (meIndicator instanceof DelimitedFileIndicatorImpl) {
            MetadataColumn mColumn = ((DelimitedFileIndicatorImpl) meIndicator).getMetadataColumn();
            typeName = TalendTypeConvert.convertToJavaType(mColumn.getTalendType());
        }
        return meName != null ? meName + PluginConstant.SPACE_STRING + PluginConstant.PARENTHESIS_LEFT + typeName
                + PluginConstant.PARENTHESIS_RIGHT : "null";//$NON-NLS-1$
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
