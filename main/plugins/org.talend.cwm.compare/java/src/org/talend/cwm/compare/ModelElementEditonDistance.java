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
package org.talend.cwm.compare;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.eobject.EditionDistance;
import org.eclipse.emf.compare.match.eobject.WeightProvider.Descriptor.Registry;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by qiongli on 2015年2月4日 Detailled comment
 *
 */
public class ModelElementEditonDistance extends EditionDistance {

    /**
     * DOC qiongli ModelElementEditonDistance constructor comment.
     * 
     * @param registry
     */
    public ModelElementEditonDistance(Registry registry) {
        super(registry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.compare.match.eobject.EditionDistance#distance(org.eclipse.emf.compare.Comparison,
     * org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    @Override
    public double distance(Comparison inProgress, EObject a, EObject b) {

        boolean isSame = false;
        if (a instanceof DatabaseConnection) {
            return 0;
        }
        if (a instanceof Catalog && b instanceof Catalog) {
            Catalog catalog1 = (Catalog) a;
            Catalog catalog2 = (Catalog) b;
            isSame = StringUtils.equalsIgnoreCase(catalog1.getName(), catalog2.getName());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof Schema && b instanceof Schema) {
            Schema schema1 = (Schema) a;
            Schema schema2 = (Schema) b;
            isSame = StringUtils.equalsIgnoreCase(schema1.getName(), schema2.getName());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TdTable && b instanceof TdTable) {
            TdTable tdTable1 = (TdTable) a;
            TdTable tdTable2 = (TdTable) b;
            isSame = StringUtils.equalsIgnoreCase(tdTable1.getName(), tdTable2.getName());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TdView && b instanceof TdView) {
            TdView tdView1 = (TdView) a;
            TdView tdView2 = (TdView) b;
            isSame = StringUtils.equalsIgnoreCase(tdView1.getName(), tdView2.getName());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TdColumn && b instanceof TdColumn) {
            TdColumn tdColumn1 = (TdColumn) a;
            TdColumn tdColumn2 = (TdColumn) b;
            isSame = StringUtils.equalsIgnoreCase(tdColumn1.getName(), tdColumn2.getName())
                    && distance(inProgress, tdColumn1.getSqlDataType(), tdColumn2.getSqlDataType()) == 0;
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TaggedValue && b instanceof TaggedValue) {
            TaggedValue taggedValue1 = (TaggedValue) a;
            TaggedValue taggedValue2 = (TaggedValue) b;
            isSame = StringUtils.equalsIgnoreCase(taggedValue1.getTag(), taggedValue2.getTag())
                    && StringUtils.equalsIgnoreCase(taggedValue1.getValue(), taggedValue2.getValue());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TdExpression && b instanceof TdExpression) {
            TdExpression tdExpression1 = (TdExpression) a;
            TdExpression tdExpression2 = (TdExpression) b;
            isSame = StringUtils.equalsIgnoreCase(tdExpression1.getLanguage(), tdExpression2.getLanguage())
                    && StringUtils.equalsIgnoreCase(tdExpression1.getBody(), tdExpression2.getBody());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof TdSqlDataType && b instanceof TdSqlDataType) {
            TdSqlDataType tdExpression1 = (TdSqlDataType) a;
            TdSqlDataType tdExpression2 = (TdSqlDataType) b;
            isSame = StringUtils.equalsIgnoreCase(tdExpression1.getName(), tdExpression2.getName());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof MetadataColumn && b instanceof MetadataColumn) {// Added yyin TDQ-8360
            MetadataColumn metaColumn1 = (MetadataColumn) a;
            MetadataColumn metaColumn2 = (MetadataColumn) b;
            isSame = StringUtils.equalsIgnoreCase(metaColumn1.getLabel(), metaColumn2.getLabel());
            return isSame ? 0 : Double.MAX_VALUE;
        } else if (a instanceof MetadataTable && b instanceof MetadataTable) {
            MetadataTable mTable1 = (MetadataTable) a;
            MetadataTable mTable2 = (MetadataTable) b;
            isSame = StringUtils.equalsIgnoreCase(mTable1.getLabel(), mTable2.getLabel());
            return isSame ? 0 : Double.MAX_VALUE;

        }

        return super.distance(inProgress, a, b);

    }

}
