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
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.objectmodel.core.util.CoreSwitch;


/**
 * DOC scorreia  class global comment. Detailled comment
 */
class UpdateCoreSwitch extends CoreSwitch<Boolean> {

    private EObject recentElement;

    public void setRightElement(EObject rightElement) {
        this.recentElement = rightElement;
    }

    @Override
    public Boolean caseTaggedValue(TaggedValue object) {
        if (recentElement instanceof TaggedValue) {
            final TaggedValue tagged = (TaggedValue) recentElement;
            object.setTag(tagged.getTag());
            object.setValue(tagged.getValue());
            return true;
        }
        return false;
    }

    @Override
    public Boolean caseExpression(Expression object) {
        if (recentElement instanceof Expression) {
            Expression expr = (Expression) recentElement;
            object.setBody(expr.getBody());
            object.setLanguage(expr.getLanguage());
            return true;
        }
        return false;
    }

    
}
