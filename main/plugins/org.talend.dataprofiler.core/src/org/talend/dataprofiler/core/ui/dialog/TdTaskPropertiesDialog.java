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
package org.talend.dataprofiler.core.ui.dialog;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.markers.internal.MarkerMessages;

/**
 * DOC mzhao class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class TdTaskPropertiesDialog extends TdDialogMarkerProperties {

    private static final String PRIORITY_HIGH = MarkerMessages.propertiesDialog_priorityHigh;

    private static final String PRIORITY_NORMAL = MarkerMessages.propertiesDialog_priorityNormal;

    private static final String PRIORITY_LOW = MarkerMessages.propertiesDialog_priorityLow;

    protected Combo priorityCombo;

    protected Button completedCheckbox;

    /**
     * @param parentShell
     */
    public TdTaskPropertiesDialog(Shell parentShell) {
        super(parentShell);
        setType(IMarker.TASK);
    }

    /**
     * @param parentShell
     */
    public TdTaskPropertiesDialog(Shell parentShell, String title) {
        super(parentShell, title);
        setType(IMarker.TASK);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.markers.internal.DialogMarkerProperties#createAttributesArea(org.eclipse.swt.widgets.Composite
     * )
     */
    protected void createAttributesArea(Composite parent) {
        createSeperator(parent);
        super.createAttributesArea(parent);

        Label label = new Label(parent, SWT.NONE);
        label.setText(MarkerMessages.propertiesDialog_priority);

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);

        priorityCombo = new Combo(composite, SWT.READ_ONLY);
        priorityCombo.setItems(new String[] { PRIORITY_HIGH, PRIORITY_NORMAL, PRIORITY_LOW });
        // Prevent Esc and Return from closing the dialog when the combo is active.
        priorityCombo.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
        priorityCombo.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                if (getMarker() == null) {
                    Map initialAttributes = getInitialAttributes();
                    initialAttributes.put(IMarker.PRIORITY, new Integer(getPriorityFromDialog()));
                }
                markDirty();
            }
        });

        completedCheckbox = new Button(composite, SWT.CHECK);
        completedCheckbox.setText(MarkerMessages.propertiesDialog_completed);
        GridData gridData = new GridData();
        gridData.horizontalIndent = convertHorizontalDLUsToPixels(20);
        completedCheckbox.setLayoutData(gridData);
        completedCheckbox.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                if (getMarker() == null) {
                    Map initialAttributes = getInitialAttributes();
                    initialAttributes.put(IMarker.DONE, completedCheckbox.getSelection() ? Boolean.TRUE : Boolean.FALSE);
                }
                markDirty();
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected boolean getCompleted() {
        IMarker marker = getMarker();
        if (marker == null) {
            Map attributes = getInitialAttributes();
            Object done = attributes.get(IMarker.DONE);
            return done != null && done instanceof Boolean && ((Boolean) done).booleanValue();
        }
        return marker.getAttribute(IMarker.DONE, false);
    }

    @SuppressWarnings("unchecked")
    protected int getPriority() {
        IMarker marker = getMarker();
        int priority = IMarker.PRIORITY_NORMAL;
        if (marker == null) {
            Map attributes = getInitialAttributes();
            Object priorityObj = attributes.get(IMarker.PRIORITY);
            if (priorityObj != null && priorityObj instanceof Integer) {
                priority = ((Integer) priorityObj).intValue();
            }
        } else {
            priority = marker.getAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
        }
        return priority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties#updateEnablement()
     */
    protected void updateEnablement() {
        super.updateEnablement();
        priorityCombo.setEnabled(isEditable());
        completedCheckbox.setEnabled(isEditable());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties#updateDialogForNewMarker()
     */
    @SuppressWarnings("unchecked")
    protected void updateDialogForNewMarker() {
        Map initialAttributes = getInitialAttributes();
        int priority = getPriority();
        initialAttributes.put(IMarker.PRIORITY, new Integer(priority));
        if (priority == IMarker.PRIORITY_HIGH) {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_HIGH));
        } else if (priority == IMarker.PRIORITY_LOW) {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_LOW));
        } else {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_NORMAL));
        }
        boolean completed = getCompleted();
        initialAttributes.put(IMarker.DONE, completed ? Boolean.TRUE : Boolean.FALSE);
        completedCheckbox.setSelection(completed);
        super.updateDialogForNewMarker();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties#updateDialogFromMarker()
     */
    @SuppressWarnings("unchecked")
    protected void updateDialogFromMarker() {
        Map initialAttributes = getInitialAttributes();
        int priority = getPriority();
        initialAttributes.put(IMarker.PRIORITY, new Integer(priority));
        if (priority == IMarker.PRIORITY_HIGH) {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_HIGH));
        } else if (priority == IMarker.PRIORITY_LOW) {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_LOW));
        } else {
            priorityCombo.select(priorityCombo.indexOf(PRIORITY_NORMAL));
        }
        boolean completed = getCompleted();
        initialAttributes.put(IMarker.DONE, completed ? Boolean.TRUE : Boolean.FALSE);
        completedCheckbox.setSelection(completed);
        super.updateDialogFromMarker();
    }

    private int getPriorityFromDialog() {
        int priority = IMarker.PRIORITY_NORMAL;
        if (priorityCombo.getSelectionIndex() == priorityCombo.indexOf(PRIORITY_HIGH)) {
            priority = IMarker.PRIORITY_HIGH;
        } else if (priorityCombo.getSelectionIndex() == priorityCombo.indexOf(PRIORITY_LOW)) {
            priority = IMarker.PRIORITY_LOW;
        }
        return priority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties#getMarkerAttributes()
     */
    @SuppressWarnings("unchecked")
    protected Map getMarkerAttributes() {
        Map attrs = super.getMarkerAttributes();
        attrs.put(IMarker.PRIORITY, new Integer(getPriorityFromDialog()));
        attrs.put(IMarker.DONE, completedCheckbox.getSelection() ? Boolean.TRUE : Boolean.FALSE);
        Object userEditable = attrs.get(IMarker.USER_EDITABLE);
        if (userEditable == null || !(userEditable instanceof Boolean)) {
            attrs.put(IMarker.USER_EDITABLE, Boolean.TRUE);
        }
        return attrs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties.getModifyOperationTitle()
     * 
     * @since 3.3
     */
    protected String getModifyOperationTitle() {
        return MarkerMessages.modifyTask_title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.markers.internal.DialogMarkerProperties.getCreateOperationTitle()
     * 
     * @since 3.3
     */
    protected String getCreateOperationTitle() {
        return MarkerMessages.DialogTaskProperties_CreateTask;

    }

}
