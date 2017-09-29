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
package org.talend.dataprofiler.core.ui.dialog.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;

public class BlockingKeysTableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String BLOCKING_KEY_NAME = "BLOCKING_KEY_NAME"; //$NON-NLS-1$

    public static final String PRECOLUMN = "PRECOLUMN"; //$NON-NLS-1$

    public static final String PRE_ALGO = "PRE_ALGO"; //$NON-NLS-1$

    public static final String PRE_VALUE = "PRE_VALUE"; //$NON-NLS-1$

    public static final String KEY_ALGO = "KEY_ALGO"; //$NON-NLS-1$

    public static final String KEY_VALUE = "KEY_VALUE"; //$NON-NLS-1$

    public static final String POST_ALGO = "POST_ALGO"; //$NON-NLS-1$

    public static final String POST_VALUE = "POST_VALUE"; //$NON-NLS-1$

    List<String> inputColumnNames;

    public BlockingKeysTableLabelProvider(List<String> inputColumnNames) {
        this.inputColumnNames = inputColumnNames;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        Image warn = null;
        Map<String, String> rule = (HashMap<String, String>) element;
        if (0 == columnIndex && !hasColumnMatch(rule)) {
            warn = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
        }
        return warn;
    }

    private boolean hasColumnMatch(Map<String, String> rule) {
        if (inputColumnNames != null) {
            for (String str : inputColumnNames) {
                if (str.equalsIgnoreCase(rule.get(BLOCKING_KEY_NAME)) || str.equalsIgnoreCase(rule.get(PRECOLUMN))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getColumnText(Object element, int columnIndex) {
        Map<String, String> rule = (HashMap<String, String>) element;
        String result = null;
        switch (columnIndex) {
        case 0:
            result = rule.get(BLOCKING_KEY_NAME);
            break;
        case 1:
            result = rule.get(PRECOLUMN);
            break;
        case 2:
            result = BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(rule.get(PRE_ALGO)).getValue();
            break;
        case 3:
            result = rule.get(PRE_VALUE);
            break;
        case 4:
            result = BlockingKeyAlgorithmEnum.getTypeBySavedValue(rule.get(KEY_ALGO)).getValue();
            break;
        case 5:
            result = rule.get(KEY_VALUE);
            break;
        case 6:
            result = BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(rule.get(POST_ALGO)).getValue();
            break;
        case 7:
            result = rule.get(POST_VALUE);
            break;
        }
        return result;
    }
}
