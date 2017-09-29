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
package org.talend.cwm.compare.factory.update;

import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.relational.TdColumn;
import orgomg.cwm.foundation.keysindexes.KeyRelationship;
import orgomg.cwm.foundation.keysindexes.UniqueKey;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class RemoveTdRelationalSwitch extends UpdateRelationalSwitch {

    private EObject leftElement;

    public void setLeftElement(EObject leftElem) {
        leftElement = leftElem;
    }

    @Override
    public Boolean caseForeignKey(ForeignKey object) {
        ColumnSet columnSet = (ColumnSet) object.eContainer();
        if (columnSet == null) {
            return Boolean.FALSE;
        }
        // MOD zshen 2010.06.10 for feature 12842
        TdColumn column = null;
        if (leftElement instanceof TdColumn) {
            column = (TdColumn) leftElement;
        } else {
            return Boolean.FALSE;
        }
        if (object.getFeature().size() <= 1) {
            columnSet.getOwnedElement().remove(object);
        } else {
            object.getFeature().remove(column);
        }
        column.getKeyRelationship().remove(object);
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseKeyRelationship(KeyRelationship object) {
        return super.caseKeyRelationship(object);
    }

    @Override
    public Boolean casePrimaryKey(PrimaryKey object) {
        ColumnSet columnSet = (ColumnSet) object.eContainer();
        if (columnSet == null) {
            return Boolean.FALSE;
        }
        // MOD zshen 2010.06.10 for feature 12842
        TdColumn column = null;
        if (leftElement instanceof TdColumn) {
            column = (TdColumn) leftElement;
        } else {
            return Boolean.FALSE;
        }
        if (object.getFeature().size() <= 1) {
            columnSet.getOwnedElement().remove(object);
        } else {
            object.getFeature().remove(column);
        }
        column.getUniqueKey().remove(object);

        return Boolean.TRUE;
    }

    @Override
    public Boolean caseUniqueKey(UniqueKey object) {
        return super.caseUniqueKey(object);
    }

}
