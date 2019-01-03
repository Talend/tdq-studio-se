// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.section;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataquality.record.linkage.ui.composite.DefaultSurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.section.definition.DefaultSurvivorshipDefinitionSection;

public class DefaultSurvivorshipSection extends DefaultSurvivorshipDefinitionSection {

    /**
     * @param form
     * @param parent
     * @param toolkit
     */
    public DefaultSurvivorshipSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, toolkit);
    }

    @Override
    protected DefaultSurvivorshipTableComposite createTableComposite(Composite ruleComp) {
        return new DefaultSurvivorshipTableComposite(ruleComp, SWT.NO_FOCUS);
    }

}
