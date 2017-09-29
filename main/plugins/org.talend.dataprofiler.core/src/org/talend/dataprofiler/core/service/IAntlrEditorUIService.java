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
package org.talend.dataprofiler.core.service;

import org.eclipse.ui.part.EditorPart;
import org.talend.dataquality.rules.ParserRule;

/**
 * DOC zshen  class global comment. Detailled comment
 */
public interface IAntlrEditorUIService extends IService {

    public void runTestRuleAction(ParserRule parserRule, EditorPart parserRuleEditor);
}
