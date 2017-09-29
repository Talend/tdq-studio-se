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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.talend.commons.exception.BusinessException;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class ActionHandleFactory {

    private static ActionHandleFactory handleFactory;

    public static ActionHandleFactory getInstance() {
        if (handleFactory == null) {
            handleFactory = new ActionHandleFactory();
        }
        return handleFactory;
    }

    public IDuplicateHandle createDuplicateHandle(IRepositoryNode node) throws BusinessException {
        IDuplicateHandle handle = null;

        EResourceConstant typedConstant = EResourceConstant.getTypedConstant(node.getObject().getProperty().getItem());

        if (typedConstant == null) {
            BusinessException createBusinessException = ExceptionFactory.getInstance().createBusinessException(
                    DefaultMessagesImpl.getString("ActionHandleFactory.duplicateFail", node.getLabel()));//$NON-NLS-1$
            throw createBusinessException;
        } else {
            switch (typedConstant) {
            case DB_CONNECTIONS:
                handle = new DBConnectionDuplicateHandle();
                break;
            case JRXML_TEMPLATE:
                handle = new JrxmlFileDuplicateHandle(node);
                break;
            case ANALYSIS:
                handle = new AnalysisDuplicateHandle();
                break;
            case REPORTS:
                handle = new ReportDuplicateHandle();
                break;
            case PATTERNS:
            case RULES_PARSER:
            case RULES_SQL:
            case RULES_MATCHER:
                handle = new ModelElementDuplicateHandle();
                break;
            case INDICATORS:
                handle = new IndicatorDuplicateHandle();
                break;
            case SOURCE_FILES:
                handle = new SourceFileDuplicateHandle(node);
                break;

            default:
                break;
            }
        }

        return handle;
    }

}
