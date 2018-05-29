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
package org.talend.dataprofiler.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.view.AbstractContextView;
import org.talend.core.ui.context.view.Contexts;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.context.TdqContextView;
import org.talend.dataquality.reports.TdReport;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;

/**
 * created by msjian on 2014-6-19 Detailled comment
 * 
 */
public final class ContextViewHelper {

    private ContextViewHelper() {
    }

    public static void updateContextView(IWorkbenchPart part) {
        IWorkbenchPart testedPart = part;
        if (!(part instanceof SupportContextEditor)) {
            testedPart = part.getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
        }
        // only for ReportEditror and AnalysisEditor
        if (testedPart instanceof SupportContextEditor) {
            SupportContextEditor currentEditor = (SupportContextEditor) testedPart;
            Contexts.setTitle(currentEditor.getTitle());
            currentEditor.updateContextView();
        }
    }

    public static void resetContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IViewPart view = page.findView(AbstractContextView.CTX_ID_TDQ);
            if (view != null && view instanceof TdqContextView) {
                ((TdqContextView) view).reset();
            }
        }
    }

    public static void hideContextView(IWorkbenchPart part) {
        boolean currentEditorOpened = false;
        IWorkbenchPage page = part.getSite().getWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editorReferences = page.getEditorReferences();
        for (IEditorReference editorRef : editorReferences) {
            if (editorRef != null && editorRef.getEditor(false) != null) {
                if (editorRef.getEditor(false) instanceof SupportContextEditor) {
                    currentEditorOpened = true;
                    break;
                }
            }
        }
        if (!currentEditorOpened) {
            IViewPart ctxViewer = page.findView(AbstractContextView.CTX_ID_TDQ);
            if (ctxViewer != null) {
                page.hideView(ctxViewer);
            }
        }
    }

    /**
     * Go through every analysis and reports, to find if it imported the current context, and update the context value
     * in it.TDQ-14492,yyin 20180518
     * 
     * @param contextManager
     */
    public static void updateAllContextInAnalysisAndReport(ContextItem contextItem, JobContextManager contextManager) {
        List<AnalysisRepNode> anaList = RepositoryNodeHelper.getAnalysisRepNodes(
                RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.ANALYSIS), true, false);
        if (CollectionUtils.isNotEmpty(anaList)) {
            for (AnalysisRepNode anaNode : anaList) {
                EList<ContextType> contextList = anaNode.getAnalysis().getContextType();
                if (findAndUpdateContext(contextList, contextItem, contextManager)) {
                    ElementWriterFactory.getInstance().createAnalysisWrite().save(anaNode.getAnalysis());
                    // refresh the analysis
                    WorkbenchUtils.refreshCurrentAnalysisEditor(anaNode.getAnalysis().getName());
                }
            }
        }
        List<ReportRepNode> repList = RepositoryNodeHelper.getReportRepNodes(
                RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.REPORTS), true, false);
        if (CollectionUtils.isNotEmpty(repList)) {
            for (ReportRepNode repNode : repList) {
                EList<ContextType> contextList = ((TdReport) repNode.getReport()).getContext();
                if (findAndUpdateContext(contextList, contextItem, contextManager)) {
                    ElementWriterFactory.getInstance().createReportWriter().save(repNode.getReport());
                    // refresh the report
                    WorkbenchUtils.refreshCurrentReportEditor(repNode.getReport().getName());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean findAndUpdateContext(EList<ContextType> contextList, ContextItem contextItem,
            JobContextManager contextManager) {
        EList<ContextType> changedList = contextItem.getContext();
        String contextId = contextItem.getProperty().getId();
        boolean isModified = false;

        // delete context group
        List<IContext> removeGroupContext = contextManager.getRemoveGroupContext();
        if (!removeGroupContext.isEmpty()) {
            List<ContextType> removedGrps = new ArrayList<ContextType>();
            for (IContext removedGroup : removeGroupContext) {
                for (ContextType context : contextList) {
                    if (StringUtils.equals(removedGroup.getName(), context.getName())) {
                        removedGrps.add(context);
                        continue;
                    }
                }
            }
            contextList.removeAll(removedGrps);
        }

        if (CollectionUtils.isNotEmpty(contextList)) {
            for (ContextType modifiedContext : changedList) {
                String modifiedContextName = modifiedContext.getName();
                for (ContextType context : contextList) {
                    // rename context group name
                    Map<IContext, String> renameGroupContext = contextManager.getRenameGroupContext();
                    if (!renameGroupContext.isEmpty() && renameGroupContext.containsValue(context.getName())) {
                        for (IContext renamedContext : renameGroupContext.keySet()) {
                            if (StringUtils.equals(context.getName(), renameGroupContext.get(renamedContext))) {
                                context.setName(renamedContext.getName());
                                break;
                            }
                        }
                    }

                    if (StringUtils.equals(context.getName(), modifiedContextName)) {
                        // update context group
                        updateContextParameters(context, modifiedContext, contextId, contextManager.getNameMap());

                        isModified = true;
                        continue;
                    }
                }
            }
        }
        return isModified;
    }

    /**
     * check each parameter's repositoryContextId, if parameter's repositoryContextId == contextId,
     * 
     * @param context
     * @param modifiedContext
     * @param contextId
     */
    private static void updateContextParameters(ContextType context, ContextType modifiedContext, String contextId,
            Map<String, String> renameMap) {
        @SuppressWarnings("unchecked")
        EList<ContextParameterType> contextParameters = context.getContextParameter();

        for (ContextParameterType contextParam : contextParameters) {
            if (contextParam.getRepositoryContextId() == null) {
                continue;
            } else if (StringUtils.equals(contextId, contextParam.getRepositoryContextId())) {
                // rename context parameter in some group, <newName, oldName>
                if (!renameMap.isEmpty()) {
                    if (renameMap.containsValue(contextParam.getName())) {
                        for (String newName : renameMap.keySet()) {
                            if (StringUtils.equals(contextParam.getName(), renameMap.get(newName))) {
                                contextParam.setName(newName);
                                break;
                            }
                        }
                    }
                }

                // update parameter's value
                ContextParameterType modifiedContextParam = ContextUtils.getContextParameterTypeByName(modifiedContext,
                        contextParam.getName());
                if (modifiedContextParam != null) {
                    contextParam.setComment(modifiedContextParam.getComment());
                    contextParam.setPrompt(modifiedContextParam.getPrompt());
                    contextParam.setRawValue(modifiedContextParam.getRawValue());
                    contextParam.setType(modifiedContextParam.getType());
                    contextParam.setValue(modifiedContextParam.getValue());
                } else {// can not find the current param in the modified context, means that the current param is
                        // deleted
                        // if any parameters are deleted from the repository context, change it to build-in in
                        // analysis/report
                    contextParam.setRepositoryContextId(null);
                }
            }
        }
    }
    
    public static List<String> getImportedListContextNames(List<ContextType> importContextList){
        List<String> importNames = new ArrayList<String>();
        List<String> importIds = new ArrayList<String>();
        for(ContextType importContext : importContextList){
            EList<ContextParameterType> contextParameters = importContext.getContextParameter();

            for (ContextParameterType contextParam : contextParameters) {
                if (contextParam.getRepositoryContextId() == null) {
                    continue;
                } else if(!importIds.contains(contextParam.getRepositoryContextId())){
                    importIds.add(contextParam.getRepositoryContextId());
                    ContextItem contextItem = ContextUtils.getContextItemById2(contextParam.getRepositoryContextId());
                    if(contextItem!=null){
                        importNames.add(contextItem.getProperty().getLabel());
                    }
                }
            }
        }
        return importNames;
    }
}
