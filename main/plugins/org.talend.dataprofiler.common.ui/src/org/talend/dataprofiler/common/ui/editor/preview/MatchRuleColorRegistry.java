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
package org.talend.dataprofiler.common.ui.editor.preview;

import org.eclipse.swt.graphics.Color;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class MatchRuleColorRegistry {

    public static final int[][] COLORS = { { 244, 147, 32 }, { 128, 119, 178 }, { 190, 213, 48 }, { 35, 157, 190 },
            { 250, 212, 16 }, { 234, 28, 36 }, { 192, 131, 91 }, { 236, 23, 133 }, { 164, 155, 100 }, { 244, 120, 30 },
            { 128, 168, 201 }, { 190, 169, 42 }, { 37, 126, 175 }, { 250, 174, 15 }, { 250, 212, 16 }, { 235, 104, 47 },
            { 234, 28, 36 }, { 58, 53, 79 } };

    private static java.awt.Color[] awtColors;

    private static Color[] swtColors;

    public static java.awt.Color[] getColorsForAwt() {
        if (null == awtColors) {
            awtColors = new java.awt.Color[COLORS.length];
            for (int i = 0; i < COLORS.length; i++) {
                awtColors[i] = new java.awt.Color(COLORS[i][0], COLORS[i][1], COLORS[i][2]);
            }
        }
        return awtColors;

    }

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
