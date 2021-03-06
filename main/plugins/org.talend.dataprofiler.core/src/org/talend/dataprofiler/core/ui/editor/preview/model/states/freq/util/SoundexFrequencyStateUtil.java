// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util;

import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.SoundexFrequencyExplorer;

/**
 * created by yyin on 2014-12-3 Detailled comment
 *
 */
public class SoundexFrequencyStateUtil {

    public static DataExplorer getDataExplorer() {
        return new SoundexFrequencyExplorer();
    }

}
