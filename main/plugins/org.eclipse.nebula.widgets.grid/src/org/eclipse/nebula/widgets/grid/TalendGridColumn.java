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
package org.eclipse.nebula.widgets.grid;


/**
 * DOC talend  class global comment. Detailled comment
 */
public class TalendGridColumn extends GridColumn{

    /**
     * DOC talend TalendGridColumn constructor comment.
     * @param parent
     * @param style
     */
    public TalendGridColumn(Grid parent, int style) {
        super(parent, style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
        GridHeaderRenderer currentHeaderRenderer = getHeaderRenderer();
        if(currentHeaderRenderer!=null&&ITalendGridHeaderRendererHandler.class.isInstance(currentHeaderRenderer)){
            ((ITalendGridHeaderRendererHandler)currentHeaderRenderer).dispose();
        }
    }
    
    

}
