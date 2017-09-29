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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.compare.i18n.DefaultMessagesImpl;
import orgomg.cwm.foundation.keysindexes.KeyRelationship;
import orgomg.cwm.foundation.keysindexes.UniqueKey;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.util.RelationalSwitch;

/**
 * DOC scorreia class global comment. Detailled comment
 */
class UpdateRelationalSwitch extends RelationalSwitch<Boolean> {

    private static Logger log = Logger.getLogger(UpdateRelationalSwitch.class);

    private UpdateCoreSwitch updateCoreSwitch = new UpdateCoreSwitch();

    private EObject recentElement;

    private EObject leftElement;

    private EObject rightElement;

    public void setRightElement(EObject rightElement) {
        // MOD klliu add recentElement getter/setter
        this.setRecentElement(rightElement);
        this.updateCoreSwitch.setRightElement(rightElement);
        this.rightElement = rightElement;
    }

    public void setLeftElement(EObject leftElement) {
        this.leftElement = leftElement;
    }

    @Override
    public Boolean caseForeignKey(ForeignKey object) {
        ColumnSet columnSet = (ColumnSet) object.eContainer();
        if (columnSet == null) {
            return Boolean.FALSE;
        }
        ForeignKey rightColumn = null;
        if (rightElement instanceof ForeignKey) {
            rightColumn = (ForeignKey) rightElement;
        } else {
            return Boolean.FALSE;
        }
        object.setName(rightColumn.getName());
        object.getFeature().clear();
        List<StructuralFeature> refColumns = new ArrayList<StructuralFeature>();
        for (StructuralFeature newColumn : rightColumn.getFeature()) {
            for (Feature column : columnSet.getFeature()) {
                if (column.getName().equalsIgnoreCase(newColumn.getName())) {
                    refColumns.add((StructuralFeature) column);
                    break;
                }
            }
        }

        object.getFeature().addAll(refColumns);
        return Boolean.TRUE;
    }

    @Override
    public Boolean caseKeyRelationship(KeyRelationship object) {
        log.error(DefaultMessagesImpl.getString("UpdateRelationalSwitch.errorUpdateOfObj", object));//$NON-NLS-1$
        return super.caseKeyRelationship(object);
    }

    @Override
    public Boolean casePrimaryKey(PrimaryKey object) {
        log.error(DefaultMessagesImpl.getString("UpdateRelationalSwitch.errorUpdateOfObj", object));//$NON-NLS-1$
        return super.casePrimaryKey(object);
    }

    @Override
    public Boolean caseUniqueKey(UniqueKey object) {
        log.error(DefaultMessagesImpl.getString("UpdateRelationalSwitch.errorUpdateOfObj", object));//$NON-NLS-1$
        return super.caseUniqueKey(object);
    }

    @Override
    public Boolean defaultCase(EObject object) {
        return this.updateCoreSwitch.doSwitch(object);
    }

    public void setRecentElement(EObject recentElement) {
        this.recentElement = recentElement;
    }

    public EObject getRecentElement() {
        return recentElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.resource.relational.util.RelationalSwitch#casePackage(orgomg.cwm.objectmodel.core.Package)
     */
    @Override
    public Boolean casePackage(Package object) {
        if (recentElement instanceof Package) {
            Package pkg = (Package) recentElement;
            object.setName(pkg.getName());
            return true;
        }
        return false;
    }

}
