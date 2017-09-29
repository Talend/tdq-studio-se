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
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.util.RelationalSwitch;
import orgomg.cwm.objectmodel.core.DataType;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class UpdateTdRelationalSwitch extends RelationalSwitch<Boolean> {

    private UpdateRelationalSwitch updateRelationalSwitch = new UpdateRelationalSwitch();

    private EObject recentElement;

    public void setRightElement(EObject rightElement) {
        this.recentElement = rightElement;
        this.updateRelationalSwitch.setRightElement(rightElement);
    }

    /**
     * DOC xqliu Comment method "setLeftElement".
     */
    public void setLeftElement(EObject rightElement) {
        this.updateRelationalSwitch.setLeftElement(rightElement);

    }

    @Override
    public Boolean caseDataType(DataType object) {
        return super.caseDataType(object);
    }

    /**
     * @deprecated
     * @param object
     * @return
     */
    @Deprecated
    public Boolean casePackage(Package object) {
        if (recentElement instanceof Package) {
            Package pkg = (Package) recentElement;
            object.setName(pkg.getName());
            return true;
        }
        return false;
    }

    @Override
    public Boolean caseNamedColumnSet(NamedColumnSet object) {
        Boolean updated = false;
        if (this.recentElement instanceof NamedColumnSet) {
            // update name
            object.setName(((NamedColumnSet) recentElement).getName());
            updated = true;
        }
        return updated;
    }

    @Override
    public Boolean caseTdSqlDataType(TdSqlDataType object) {
        if (recentElement instanceof TdSqlDataType) {
            TdSqlDataType datatype = (TdSqlDataType) recentElement;
            object.setName(datatype.getName());
            object.setNumericPrecision(datatype.getNumericPrecision());
            object.setJavaDataType(datatype.getJavaDataType());
            return true;
        }
        return false;
    }

    @Override
    public Boolean defaultCase(EObject object) {
        return this.updateRelationalSwitch.doSwitch(object);
    }

    @Override
    public Boolean caseTdColumn(TdColumn object) {
        if (recentElement instanceof TdColumn) {
            TdColumn c = (TdColumn) recentElement;
            object.setName(c.getName());
            object.setLength(c.getLength());
            // MOD klliu update the c.getJavaType()
            object.getSqlDataType().setJavaDataType(c.getSqlDataType().getJavaDataType());
            // ADD msjian TDQ-8546: fix the oracle nullable wrong after reload column list
            object.setNullable(c.isNullable());
            return true;
        }
        return false;
    }

}
