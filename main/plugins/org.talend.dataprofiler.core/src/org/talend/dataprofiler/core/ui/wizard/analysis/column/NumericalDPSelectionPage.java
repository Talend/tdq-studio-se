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
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * this select data page only can select numerical data for analysis: Discrete Data Analysis Summary Statistics Analysis
 * 
 */
public class NumericalDPSelectionPage extends ColumnAnalysisDOSelectionPage {

    protected static Logger log = Logger.getLogger(NumericalDPSelectionPage.class);

    public NumericalDPSelectionPage() {
        super();
        setPageComplete(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnAnalysisDOSelectionPage#addListeners()
     */
    @Override
    protected void addListeners() {

        addListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {

                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                nodes = new ArrayList<IRepositoryNode>();
                if (object instanceof ColumnRepNode) {
                    // only support interger type
                    RepositoryNode repositoryNode = (ColumnRepNode) object;
                    nodes.add(repositoryNode);
                    if (RepNodeUtils.isAllNumberalColumns(nodes)) {
                        advanceToNextPageOrFinish();
                    }
                }
            }

        });
        addListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                try {

                    Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                    nodes = new ArrayList<IRepositoryNode>();
                    if (object instanceof ColumnRepNode) {
                        List<IRepositoryNode> list = ((IStructuredSelection) event.getSelection()).toList();
                        nodes.addAll(list);
                        if (nodes.size() == 0 || RepNodeUtils.isValidSelectionFromSameTable(nodes)) {
                            if (RepNodeUtils.isAllNumberalColumns(nodes)) {
                                setPageComplete(true);
                                setMessage(chooseConnStr);
                            } else {
                                setPageComplete(false);
                                setMessage(
                                        DefaultMessagesImpl.getString("ColumnAnalysisDOSelectionPage.selectColumnError2"), ERROR); //$NON-NLS-1$
                            }
                        } else {
                            setPageComplete(false);
                            setMessage(DefaultMessagesImpl.getString("ColumnAnalysisDOSelectionPage.selectColumnError1"), ERROR); //$NON-NLS-1$
                        }
                    } else {
                        setPageComplete(false);
                        setMessage(chooseConnStr);
                    }

                } catch (Exception e) {
                    log.error(e, e);
                }
            }

        });
    }

}
