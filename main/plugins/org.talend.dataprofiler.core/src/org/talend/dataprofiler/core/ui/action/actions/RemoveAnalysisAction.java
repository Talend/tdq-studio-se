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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;

/**
 * DOC rli RemoveAnalysisActionProvider class global comment. Detailled comment This action only be used when we had
 * opened DqRepository view case
 */
public class RemoveAnalysisAction extends Action {

    protected static Logger log = Logger.getLogger(RemoveAnalysisAction.class);

    public RemoveAnalysisAction() {
        super(DefaultMessagesImpl.getString("RemoveAnalysisAction.removeAnalysis")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.DELETE_ACTION));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        DQRespositoryView findView = CorePlugin.getDefault().getRepositoryView();
        if (findView == null) {
            return;
        }
        TreeSelection treeSelection = (TreeSelection) findView.getCommonViewer().getSelection();
        TreePath[] paths = treeSelection.getPaths();
        List<Analysis> analysisList;
        Analysis analysisObj = null;
        Map<TDQReportItem, List<Analysis>> removeMap = new HashMap<TDQReportItem, List<Analysis>>();
        for (TreePath path : paths) {
            Object lastSegment = path.getLastSegment();
            if (!(lastSegment instanceof ReportAnalysisRepNode)) {
                return;
            }
            // MOD sizhaoliu 2012-06-12 TDQ-5051 "Remove Anaysis" menu item is not in the right place
            ReportAnalysisRepNode repNode = (ReportAnalysisRepNode) lastSegment;
            analysisObj = repNode.getAnalysis();
            TDQReportItem reportItem = repNode.getReportItem();
            analysisList = removeMap.get(reportItem);
            if (analysisList == null) {
                analysisList = new ArrayList<Analysis>();
                analysisList.add(analysisObj);
                removeMap.put(reportItem, analysisList);
            } else {
                analysisList.add(analysisObj);
            }
        }
        if (analysisObj == null) {
            return;
        }
        String message = paths.length > 1 ? DefaultMessagesImpl.getString(
                "RemoveAnalysisAction.areYouDeleteElement0", paths.length) //$NON-NLS-1$ 
                : DefaultMessagesImpl.getString("RemoveAnalysisAction.areYouDeleteElement2", analysisObj.getName()); //$NON-NLS-1$ 
        boolean openConfirm = MessageDialog.openConfirm(null,
                DefaultMessagesImpl.getString("RemoveAnalysisAction.confirmResourceDelete"), message); //$NON-NLS-1$

        if (openConfirm) {
            Iterator<TDQReportItem> iterator = removeMap.keySet().iterator();
            while (iterator.hasNext()) {
                TDQReportItem reportItem = iterator.next();
                ReportHelper.removeAnalyses((TdReport) reportItem.getReport(), removeMap.get(reportItem));

                // save now modified resources (that contain the Dependency
                // objects)

                ElementWriterFactory.getInstance().createReportWriter().save(reportItem, true);
            }

            IFolder reportsFolder = ResourceManager.getReportsFolder();
            try {
                reportsFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
                log.error(e, e);
            }
            findView.getCommonViewer().refresh();
        }
    }
}
