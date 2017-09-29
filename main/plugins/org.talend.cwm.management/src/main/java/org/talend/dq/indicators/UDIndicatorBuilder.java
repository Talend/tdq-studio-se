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
package org.talend.dq.indicators;

import org.apache.log4j.Logger;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.impl.UserdefineFactoryImpl;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UDIndicatorBuilder {

    static Logger log = Logger.getLogger(UDIndicatorBuilder.class);

    private boolean initialized = false;

    private UDIndicatorDefinition indicatorDefinition;

    public boolean initializeUDIndicatorBuilder(String udiName) {

        if (initialized) {
            log.warn("User Defined Indicator already initialized. "); //$NON-NLS-1$
            return false;
        }

        this.indicatorDefinition = UserdefineFactoryImpl.eINSTANCE.createUDIndicatorDefinition();
        indicatorDefinition.setName(udiName);
        indicatorDefinition.setLabel(udiName);
        IndicatorCategory udiCategory = DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory();
        if (udiCategory != null && !indicatorDefinition.getCategories().contains(udiCategory)) {
            indicatorDefinition.getCategories().add(udiCategory);
        }
        return true;
    }

    public IndicatorDefinition getUDIndicator() {
        return indicatorDefinition;
    }

}
