package org.eclipse.nebula.widgets.grid;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

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

/**
 * created by talend on Feb 5, 2015 Detailled comment
 *
 */
public class TalendGridItem extends GridItem {
    
    boolean lastHideState=false;

    /**
     * DOC talend TableGridItem constructor comment.
     * 
     * @param parent
     * @param style
     */
    public TalendGridItem(Grid parent, int style) {
        super(parent, style);
    }
    
    
    
    /**
     * DOC talend TalendGridItem constructor comment.
     * @param parent
     * @param style
     * @param index
     */
    public TalendGridItem(Grid parent, int style, int index) {
        super(parent, style, index);
    }



    /**
     * DOC talend TalendGridItem constructor comment.
     * @param parent
     * @param style
     * @param index
     */
    public TalendGridItem(GridItem parent, int style, int index) {
        super(parent, style, index);
    }



    /**
     * DOC talend TalendGridItem constructor comment.
     * @param parent
     * @param style
     */
    public TalendGridItem(GridItem parent, int style) {
        super(parent, style);
    }



    /**
     * 
     * Hide or show the item
     * @param hide reqired hide this item or not
     */
    public void hideItem(boolean hide){
        lastHideState=hide;
       if(getParentItem()==null){
            this.setVisible(!hide);
       }else if(allParentItemIsExpanded()){
           this.setVisible(!hide);
       }
    }



    /**
     * DOC talend Comment method "allParentItemIsExpanded".
     * @return
     */
    private boolean allParentItemIsExpanded() {
        if(getParentItem()==null){
            return this.isExpanded();
        }
        return (!this.hasChildren()||this.isExpanded())&&getTalendParentItem().allParentItemIsExpanded();
    }
    /**
     * DOC talend Comment method "allParentItemIsExpanded".
     * @return
     */
    private TalendGridItem getTalendParentItem() {
        if(getParentItem()==null){
            return null;
        }
        return ((TalendGridItem)getParentItem());
    }
    /**
     * 
     * Judge current item Whether is hided 
     * @return true if the item has been hided else false
     */
    public boolean isHide(){
        return !this.isVisible();
    }



    /**
     *Check whether all of cells are checkable
     * @return true if all of cells are checkable else return false
     */
    public boolean getCheckable() {
        if(this.getParent().getColumnCount()>2){
           return this.getCheckable(1);
        }
//        for(int index=0;index<this.getParent().getColumnCount();index++){
//            if(this.getCheckable(index)){
//                return true;
//            }
//        }
        return true;
    }



    /* (non-Javadoc)
     * @see org.eclipse.nebula.widgets.grid.GridItem#setVisible(boolean)
     */
    @Override
    void setVisible(boolean visible) {
        super.setVisible(visible&&!lastHideState);
    }



    

}
