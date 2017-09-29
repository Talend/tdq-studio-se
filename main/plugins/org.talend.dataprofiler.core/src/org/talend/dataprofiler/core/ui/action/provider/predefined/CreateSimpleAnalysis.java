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
package org.talend.dataprofiler.core.ui.action.provider.predefined;

import org.talend.dataprofiler.core.ui.action.AbstractPredefinedActionProvider;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.CreateSimpleAnalysisAction;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class CreateSimpleAnalysis extends AbstractPredefinedActionProvider {

    @Override
    protected AbstractPredefinedAnalysisAction getAction() {
        return new CreateSimpleAnalysisAction();
    }
}
