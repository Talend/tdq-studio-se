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
package org.talend.dataquality.record.linkage.ui.section;


/**
 * created by zshen on Aug 21, 2013
 * Detailled comment
 *
 */
public interface ITableEditOperation {

    /**
     * DOC zshen Comment method "pasteTableItem".
     */
    abstract public void pasteTableItem();

    /**
     * DOC zshen Comment method "copyTableItem".
     */
    abstract public void copyTableItem();

    /**
     * DOC zshen Comment method "moveDownTableItem".
     */
    abstract public void moveDownTableItem();

    /**
     * DOC zshen Comment method "moveUpTableItem".
     */
    abstract public void moveUpTableItem();

    /**
     * DOC zshen Comment method "removeTableItem".
     */
    abstract public void removeTableItem();

    /**
     * DOC zshen Comment method "addTableItem".
     */
    abstract public void addTableItem();
}
