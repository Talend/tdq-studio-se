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
package org.talend.dataquality.record.linkage.ui.section.definition;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataquality.record.linkage.ui.composite.ParticularDefaultSurvivorshipTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.definition.ParticularDefaultSurvivorshipDefinitionTableComposite;

/**
 * The section used by match rule editor side
 */
public class ParticularDefSurshipMatchRuleSection extends ParticularDefSurshipDefinitionSection {

    /**
     * The constructor of ParticularDefSurshipMatchRuleSection.
     * 
     * @param form
     * @param parent
     * @param toolkit
     */
    public ParticularDefSurshipMatchRuleSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, toolkit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.definition.ParticularDefSurshipDefinitionSection#getTableComposite(org
     * .eclipse.swt.widgets.Composite)
     */
    @Override
    protected ParticularDefaultSurvivorshipTableComposite getTableComposite(Composite ruleComp) {
        return new ParticularDefaultSurvivorshipDefinitionTableComposite(ruleComp, SWT.NO_FOCUS);
    }

}
