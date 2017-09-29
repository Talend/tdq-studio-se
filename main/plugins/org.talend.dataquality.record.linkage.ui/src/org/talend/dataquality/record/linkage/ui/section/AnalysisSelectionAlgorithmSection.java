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
package org.talend.dataquality.record.linkage.ui.section;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * created by zhao on Jul 18, 2014 Detailled comment
 * 
 */
public class AnalysisSelectionAlgorithmSection extends SelectAlgorithmSection {

    AnaMatchSurvivorSection anaMatchSurvSection = null;

    /**
     * DOC zhao AnalysisSelectionAlgorithmSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param toolkit
     */
    public AnalysisSelectionAlgorithmSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, toolkit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.SelectAlgorithmSection#isShowBlockingKeySection(boolean)
     */
    @Override
    protected boolean isShowBlockingKeySection(boolean isVSRAlgo) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.SelectAlgorithmSection#updateMatchAndSurvivorSection()
     */
    @Override
    protected void updateMatchAndSurvivorSection() {
        this.anaMatchSurvSection.redrawnContent();
    }

    public void setAnaMatchSurvivorSection(AnaMatchSurvivorSection matchAndSurvivorKeySection) {
        this.anaMatchSurvSection = matchAndSurvivorKeySection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.SelectAlgorithmSection#notifyMatchSurvSection()
     */
    @Override
    protected void changeDisplayStatus() {
        anaMatchSurvSection.changeSectionDisStatus(!isVSRMode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.SelectAlgorithmSection#removeAllSurvivorship()
     */
    @Override
    protected void removeAllSurvivorship() {
        anaMatchSurvSection.removeAllSurvivorship();
    }

}
