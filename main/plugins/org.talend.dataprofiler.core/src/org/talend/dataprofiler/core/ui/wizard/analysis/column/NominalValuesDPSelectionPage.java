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
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;

/**
 * this select data page only used for Nominal Values analysis
 * 
 */
public class NominalValuesDPSelectionPage extends ColumnAnalysisDOSelectionPage {

    protected static Logger log = Logger.getLogger(NominalValuesDPSelectionPage.class);

    private boolean addTextIndicator = true;

    /**
     * Getter for addTextIndicator.
     * 
     * @return the addTextIndicator
     */
    public boolean isAddTextIndicator() {
        return this.addTextIndicator;
    }

    /**
     * Sets the addTextIndicator.
     * 
     * @param addTextIndicator the addTextIndicator to set
     */
    public void setAddTextIndicator(boolean addTextIndicator) {
        this.addTextIndicator = addTextIndicator;
    }

    public NominalValuesDPSelectionPage() {
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
                if (object instanceof ColumnRepNode) {
                    RepositoryNode repositoryNode = (ColumnRepNode) object;
                    DFColumnRepNode columnNode = new DFColumnRepNode(repositoryNode.getObject(), repositoryNode.getParent(),
                            ENodeType.TDQ_REPOSITORY_ELEMENT, ((DQRepositoryNode) repositoryNode).getProject());
                    int javaSQLType = TalendTypeConvert.convertToJDBCType(columnNode.getMetadataColumn().getTalendType());

                    // MetadataColumn column = ((MetadataColumnRepositoryObject)
                    // repositoryNode.getObject()).getTdColumn();
                    // int javaSQLType = TalendTypeConvert.convertToJDBCType(column.getTalendType());
                    if (Java2SqlType.isTextInSQL(javaSQLType)) {
                        advanceToNextPageOrFinish();
                    } else {
                        List<DFColumnRepNode> tempList = new ArrayList<DFColumnRepNode>();
                        tempList.add(columnNode);
                        setAddTextIndicator(false);
                        if (!tempList.isEmpty()) {
                            ElementListSelectionDialog dialog = new ElementListSelectionDialog(null,
                                    new DQRepositoryViewLabelProvider());
                            dialog.setElements(tempList.toArray());
                            dialog.setTitle(DefaultMessagesImpl.getString("CreateNominalAnalysisAction.dataTypeWarning")); //$NON-NLS-1$
                            dialog.setMessage(DefaultMessagesImpl.getString("CreateNominalAnalysisAction.string")); //$NON-NLS-1$
                            dialog.setSize(80, 20);
                            dialog.create();

                            if (Window.OK == dialog.open()) {
                                // zqin get the column and change their datamining type to "Nominal"
                                // use MetadataHelper
                                for (DFColumnRepNode col : tempList) {
                                    MetadataHelper.setDataminingType(DataminingType.NOMINAL, col.getMetadataColumn());
                                }
                                advanceToNextPageOrFinish();
                            }
                        }
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

                            List<DFColumnRepNode> tempList = new ArrayList<DFColumnRepNode>();
                            for (IRepositoryNode obj : nodes) {
                                if (object instanceof ColumnRepNode) {
                                    RepositoryNode repositoryNode = (ColumnRepNode) obj;
                                    DFColumnRepNode columnNode = new DFColumnRepNode(repositoryNode.getObject(),
                                            repositoryNode.getParent(), ENodeType.TDQ_REPOSITORY_ELEMENT,
                                            ((DQRepositoryNode) repositoryNode).getProject());
                                    int javaSQLType = TalendTypeConvert.convertToJDBCType(columnNode.getMetadataColumn()
                                            .getTalendType());
                                    if (!Java2SqlType.isTextInSQL(javaSQLType)) {
                                        tempList.add(columnNode);
                                        setAddTextIndicator(false);
                                    }
                                }
                            }

                            if (!tempList.isEmpty()) {
                                ElementListSelectionDialog dialog = new ElementListSelectionDialog(null,
                                        new DQRepositoryViewLabelProvider());
                                dialog.setElements(tempList.toArray());
                                dialog.setTitle(DefaultMessagesImpl.getString("CreateNominalAnalysisAction.dataTypeWarning")); //$NON-NLS-1$
                                dialog.setMessage(DefaultMessagesImpl.getString("CreateNominalAnalysisAction.string")); //$NON-NLS-1$
                                dialog.setSize(80, 20);
                                dialog.create();

                                if (Window.OK == dialog.open()) {
                                    // zqin get the column and change their datamining type to "Nominal"
                                    for (DFColumnRepNode column : tempList) {
                                        MetadataHelper.setDataminingType(DataminingType.NOMINAL, column.getMetadataColumn());
                                    }
                                    setPageComplete(true);
                                    setMessage(chooseConnStr);
                                }
                            } else {
                                setPageComplete(true);
                                setMessage(chooseConnStr);
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
