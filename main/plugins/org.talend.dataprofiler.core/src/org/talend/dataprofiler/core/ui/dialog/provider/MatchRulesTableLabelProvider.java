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
import org.talend.dataquality.record.linkage.utils.CustomAttributeMatcherClassNameConvert;

public class MatchRulesTableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public static final String MATCH_KEY_NAME = "MATCH_KEY_NAME"; //$NON-NLS-1$

    public static final String INPUT_COLUMN = "INPUT_COLUMN"; //$NON-NLS-1$

    public static final String MATCHING_TYPE = "MATCHING_TYPE"; //$NON-NLS-1$

    public static final String CUSTOM_MATCHER = "CUSTOM_MATCHER"; //$NON-NLS-1$

    public static final String CONFIDENCE_WEIGHT = "CONFIDENCE_WEIGHT"; //$NON-NLS-1$

    public static final String HANDLE_NULL = "HANDLE_NULL"; //$NON-NLS-1$

    public static final String THRESHOLD = "THRESHOLD"; //$NON-NLS-1$

    public static final String ATTRIBUTE_THRESHOLD = "ATTRIBUTE_THRESHOLD"; //$NON-NLS-1$

    public static final String SURVIVORSHIP_FUNCTION = "SURVIVORSHIP_FUNCTION"; //$NON-NLS-1$

    public static final String PARAMETER = "PARAMETER"; //$NON-NLS-1$

    public static final String TOKENIZATION_TYPE = "TOKENIZATION_TYPE"; //$NON-NLS-1$ The Value should keep same with MatchAnalysisConstant.TOKENIZATION_TYPE

    List<String> inputColumnNames;

    public MatchRulesTableLabelProvider(List<String> inputColumnNames) {
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
                if (str.equalsIgnoreCase(rule.get(MATCH_KEY_NAME)) || str.equalsIgnoreCase(rule.get(INPUT_COLUMN))) {
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
        case 0: // match key name
            result = rule.get(MATCH_KEY_NAME);
            break;
        case 1: // column
            result = rule.get(INPUT_COLUMN);
            break;
        case 2: // matching type
            result = rule.get(MATCHING_TYPE);
            break;
        case 3: // custom matcher class
            result = CustomAttributeMatcherClassNameConvert.getClassName(rule.get(CUSTOM_MATCHER));
            break;
        case 4: // TOKENIZATION_TYPE
            result = rule.get(TOKENIZATION_TYPE);
            break;
        case 5: // confidence weight
            result = rule.get(CONFIDENCE_WEIGHT);
            break;
        case 6: // handle null
            result = rule.get(HANDLE_NULL);
            break;
        case 7: // threshold
            result = rule.get(THRESHOLD);
            break;
        case 8: // survivorship function
            result = rule.get(SURVIVORSHIP_FUNCTION);
            break;
        case 9: // parameter
            result = rule.get(PARAMETER);
            break;
        }
        return result;
    }
}
