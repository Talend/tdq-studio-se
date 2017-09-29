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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;

/**
 * created by zshen on Aug 20, 2013 Detailled comment
 * 
 */
public abstract class AbstractSectionComposite implements PropertyChangeListener {

    protected FormToolkit toolkit;

    protected Section section = null;

    private ScrolledForm form = null;

    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public AbstractSectionComposite(final ScrolledForm form, Composite parent, int style, FormToolkit toolkit) {
        this.toolkit = toolkit;
        this.section = this.toolkit.createSection(parent, style);
        this.form = form;
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));

        section.addExpansionListener(new ExpansionAdapter() {

            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }

        });
        section.setExpanded(true);
    }

    /**
     * Getter for section.
     * 
     * @return the section
     */
    public Section getSection() {
        return this.section;
    }

    public void setClient(Control client) {
        this.section.setClient(client);
    }

    /**
     * Getter for form.
     * 
     * @return the form
     */
    public ScrolledForm getForm() {
        return this.form;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (MatchAnalysisConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
        } else if (MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH.equals(evt.getPropertyName())) {
            listeners.firePropertyChange(MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH, true, false);
        }
    }

    /**
     * change Section Display status when we switch different algorithm
     * 
     */
    public void changeSectionDisStatus(boolean isVisible) {
        GridData gd = (GridData) getSection().getLayoutData();
        gd.exclude = !isVisible;
        getSection().setExpanded(true);
        getSection().setVisible(isVisible);
        getSection().getParent().layout();
    }
}
