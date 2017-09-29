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

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * DOC yyi MatchRuleCTabFolderRenderer class global comment. Detailled comment
 */
public class MatchRuleCTabFolderRenderer extends CTabFolderRenderer {

    public MatchRuleCTabFolderRenderer(CTabFolder parent) {
        super(parent);
    }

    @Override
    protected Point computeSize(int part, int state, GC gc, int wHint, int hHint) {
        // resize width
        if (part == CTabFolderRenderer.PART_MAX_BUTTON) {
            int width = 0, height = 0;
            height = 18;
            width = 20;
            Rectangle trim = computeTrim(part, state, 0, 0, width, height);
            width = trim.width;
            height = trim.height;
            return new Point(width, height);
        }
        return super.computeSize(part, state, gc, wHint, hHint);
    }
}
