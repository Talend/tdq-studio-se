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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * created by talend on Feb 9, 2015
 * Detailled comment
 *
 */
public class TalendGrid extends Grid {

    /**
     * DOC talend TalendGrid constructor comment.
     * @param parent
     * @param style
     */
    public TalendGrid(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * Returns the zero-relative index of the item which is currently at the top
     * of the receiver. This index can change when items are scrolled or new
     * items are added or removed.
     * Override reason: When top item will be invisibled then igron it
     *
     * @return the index of the top item
     * @throws org.eclipse.swt.SWTException
     * <ul>
     * <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that
     * created the receiver</li>
     * </ul>
     */
    @Override
    public int getTopIndex() {
        int superTopIndex=   super.getTopIndex();
         if (superTopIndex ==0){
             return superTopIndex;
         }else if(checkTopIndexIsVisible(superTopIndex)){
             return superTopIndex;
         }else{
             return nextVisibleItemIndex(superTopIndex);
         }
    }

    /**
     * DOC talend Comment method "nextVisibleItemIndex".
     * @param superTopIndex
     * @return
     */
    private int nextVisibleItemIndex(int superTopIndex) {
        boolean visible=false;
        int currentIndex=superTopIndex;
        while(!visible){
            //when current index is more than of the count of items it is a exception case
            if(++currentIndex >= this.getItemCount())
            {
                //restore the old state
                for(GridItem item:this.getItems()){
                    item.setExpanded(false);
                }
//                SWT.error(SWT.ERROR_INVALID_RANGE);
            }
            if(checkTopIndexIsVisible(currentIndex)){
                return currentIndex;
            }
            
        }
        return 0;
    }

    /**
     * DOC talend Comment method "checkTopIndexIsInVisible".
     * @param superTopIndex
     * @return
     */
    private boolean checkTopIndexIsVisible(int superTopIndex) {
        return this.getItem(superTopIndex).isVisible();
    }
    
    

}
