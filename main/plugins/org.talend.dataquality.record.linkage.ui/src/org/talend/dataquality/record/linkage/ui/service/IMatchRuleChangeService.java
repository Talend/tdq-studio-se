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
package org.talend.dataquality.record.linkage.ui.service;

import org.talend.core.IService;
import org.talend.dataquality.rules.MatchRuleDefinition;

/**
 * created by HHB on 2013-11-1 Detailled comment
 * 
 */
public interface IMatchRuleChangeService extends IService {

    enum ChangeEvent {
        BEFORE_DELETE,
        BEFORE_RENAME
    }

    boolean objectChange(MatchRuleDefinition source, Object oldValue, Object newValue, ChangeEvent event);
}
