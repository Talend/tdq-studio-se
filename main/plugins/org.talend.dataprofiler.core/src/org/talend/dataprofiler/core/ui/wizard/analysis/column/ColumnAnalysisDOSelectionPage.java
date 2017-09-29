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
package org.talend.dataprofiler.core.ui.wizard.analysis.column;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.grid.utils.Observerable;
import org.talend.dataprofiler.core.ui.grid.utils.TDQObserver;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisDPSelectionPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.ColumnContentProvider;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment,2011-02-16 feature 15387
 */
public class ColumnAnalysisDOSelectionPage extends AnalysisDPSelectionPage implements Observerable {

    protected static Logger log = Logger.getLogger(ColumnAnalysisDOSelectionPage.class);

    protected static String chooseConnStr = DefaultMessagesImpl.getString("ColumnAnalysisPageStep0.chooseColumn"); //$NON-NLS-1$

    public List<IRepositoryNode> nodes;

    public List<TDQObserver> observers = new ArrayList();

    /**
     * @param pageName
     */
    public ColumnAnalysisDOSelectionPage() {
        this(new ColumnContentProvider());
    }

    /**
     * @param contentProvider, use diffirent ContentProvider for Match analysis and column analysis.
     */
    public ColumnAnalysisDOSelectionPage(ResourceViewContentProvider contentProvider) {
        super(DefaultMessagesImpl.getString("ColumnAnalysisPageStep0.newAnalysis"), chooseConnStr, DefaultMessagesImpl //$NON-NLS-1$
                .getString("ColumnAnalysisPageStep0.Columns"), contentProvider, true); //$NON-NLS-1$
        setPageComplete(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisDPSelectionPage#addListeners()
     */
    @Override
    protected void addListeners() {

        addListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (object instanceof DBCatalogRepNode) {
                    advanceToNextPageOrFinish();
                }
            }

        });
        addListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                try {
                    Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                    nodes = new ArrayList<IRepositoryNode>();
                    if (object instanceof IRepositoryNode) {
                        List<IRepositoryNode> list = ((IStructuredSelection) event.getSelection()).toList();
                        nodes.addAll(list);
                        updateCompleteState();
                        notifyObservers();
                    } else {
                        setPageComplete(false);
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }
            }

        });
    }

    /**
     * 
     * DOC talend Comment method "updateCompleteState".
     */
    void updateCompleteState() {
        // Nodes come from same table or empty both are valid
        if (nodes.size() == 0) {
            setPageComplete(true);
            this.setMessage(chooseConnStr);
        } else {
            if (!RepNodeUtils.isValidSelectionFromSameTable(nodes)) {
                setPageComplete(false);
                this.setMessage(DefaultMessagesImpl.getString("ColumnAnalysisDOSelectionPage.selectColumnError1"), ERROR); //$NON-NLS-1$
            } else {
                if (this.getPreviousPage().getWizard() instanceof PatternFrequencyWizard
                        && !RepNodeUtils.isSupportPatternFrequency(nodes)) {
                    setPageComplete(true);
                    this.setMessage(DefaultMessagesImpl.getString("ColumnAnalysisDOSelectionPage.selectColumnError3"), WARNING); //$NON-NLS-1$
                } else {
                    setPageComplete(true);
                    this.setMessage(""); //$NON-NLS-1$
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#addObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TDQObserver)
     */
    public boolean addObserver(TDQObserver observer) {
        return observers.add(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.grid.utils.Observerable#removeObserver(org.talend.dataprofiler.core.ui.grid.utils
     * .TDQObserver)
     */
    public boolean removeObserver(TDQObserver observer) {
        return observers.remove(observer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#clearObserver()
     */
    public void clearObserver() {
        observers.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.Observerable#notifyObservers()
     */
    public void notifyObservers() {
        for (TDQObserver observer : observers) {
            observer.update(this);
        }

    }
}
