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
package org.talend.dataquality.record.linkage.ui.composite.table;

import org.eclipse.swt.graphics.Color;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class MatchRuleColorRegistry {

    public static final int[][] COLORS = { { 35, 97, 146 }, { 196, 214, 0 }, { 219, 102, 42 }, { 247, 168, 0 }, { 120, 113, 33 },
            { 0, 169, 206 }, { 167, 168, 170 }, { 236, 171, 124 }, { 184, 179, 112 }, { 212, 211, 211 }, { 131, 211, 230 },
            { 255, 211, 139 } };

    private static Color[] swtColors;

    public static Color[] getColorsForSwt() {
        if (null == swtColors) {
            swtColors = new Color[COLORS.length];
            for (int i = 0; i < COLORS.length; i++) {
                swtColors[i] = new Color(null, COLORS[i][0], COLORS[i][1], COLORS[i][2]);
            }
        }
        return swtColors;

    }
}
