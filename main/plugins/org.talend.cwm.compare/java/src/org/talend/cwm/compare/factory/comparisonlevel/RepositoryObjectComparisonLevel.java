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
package org.talend.cwm.compare.factory.comparisonlevel;

import org.eclipse.core.runtime.Assert;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.helper.PropertyHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class RepositoryObjectComparisonLevel extends DataProviderComparisonLevel {

    /**
     * DOC zshen RepositoryObjectComparisonLevel constructor comment.
     * 
     * @param selObj
     */
    public RepositoryObjectComparisonLevel(Object selObj) {
        super(selObj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.compare.factory.comparisonlevel.AbstractComparisonLevel#findDataProvider()
     */
    @Override
    protected Connection findDataProvider() {
        Assert.isTrue(selectedObj instanceof IRepositoryViewObject, this.selectedObj
                + " is not an instance of IRepositoryViewObject");//$NON-NLS-1$
        Connection returnValue = null;
        ModelElement modelElement = PropertyHelper.getModelElement(((IRepositoryViewObject) selectedObj).getProperty());
        if (modelElement instanceof Connection) {
            returnValue = (Connection) modelElement;
        }
        return returnValue;
    }

    @Override
    protected boolean isValid() {
        ModelElement modelElement = PropertyHelper.getModelElement(((IRepositoryViewObject) selectedObj).getProperty());
        return modelElement instanceof Connection;
    }

}
