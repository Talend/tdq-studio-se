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
import java.util.HashMap;
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
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.context.TdqContextView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.reports.TdReport;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by msjian on 2014-6-19 Detailled comment
 * 
 */
public final class ContextViewHelper {

    private ContextViewHelper() {
    }

    public static void updateContextView(IWorkbenchPart part) {
        if (!(part instanceof SupportContextEditor)) {
            return;
        }
        // only for ReportEditror and AnalysisEditor
        if (part instanceof SupportContextEditor) {
            SupportContextEditor currentEditor = (SupportContextEditor) part;
            Contexts.setTitle(currentEditor.getTitle());
            currentEditor.updateContextView();
        }
    }

    public static void resetContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IViewPart view = page.findView(AbstractContextView.CTX_ID_TDQ);
            if (view != null && view instanceof TdqContextView) {
                refreshView((TdqContextView) view);
                ((TdqContextView) view).reset();
            }
        }
    }

    private static void refreshView(AbstractContextView view) {
        if (view != null) {
            view.setPartName("");
            view.refresh();
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
                    findAndUpdateFieldUseContext(anaNode.getAnalysis(), contextManager);
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
                    findAndUpdateFieldUseContext((TdReport) repNode.getReport(), contextManager);
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

    private static void findAndUpdateFieldUseContext(Analysis analysis, JobContextManager contextManager) {
        findAndUpdateTaggedValue(analysis.getTaggedValue(), TdqAnalysisConnectionPool.NUMBER_OF_CONNECTIONS_PER_ANALYSIS,
                contextManager);
        // check "data filter" in analysis
        String dataFilter = AnalysisHelper.getStringDataFilter(analysis);
        if (ContextHelper.isContextVar(dataFilter)) {
            String changedValue = ContextHelper.checkRenamedContextParameter(contextManager, dataFilter);
            if (StringUtils.isNotBlank(changedValue)) {
                AnalysisHelper.setStringDataFilter(analysis, changedValue);
            }
        }
    }

    private static void findAndUpdateTaggedValue(List<TaggedValue> values, String tagName, JobContextManager contextManager) {
        TaggedValue tagValue = TaggedValueHelper.getTaggedValue(tagName, values);
        if (ContextHelper.isContextVar(tagValue.getValue())) {
            String changedName = ContextHelper.checkRenamedContextParameter(contextManager, tagValue.getValue());
            if (StringUtils.isNotBlank(changedName)) {
                tagValue.setValue(changedName);
            }
        }
    }

    private static Map<String, String> findAndUpdateTaggedValueWithNewName(List<TaggedValue> values, String tagName,
            JobContextManager contextManager) {
        TaggedValue tagValue = TaggedValueHelper.getTaggedValue(tagName, values);
        if (ContextHelper.isContextVar(tagValue.getValue())) {
            String changedName = ContextHelper.checkRenamedContextParameter(contextManager, tagValue.getValue());
            if (StringUtils.isNotBlank(changedName)) {
                Map<String, String> nameMap = new HashMap<String, String>();
                nameMap.put(tagValue.getValue(), changedName);
                tagValue.setValue(changedName);
                return nameMap;
            }
        }
        return null;
    }

    private static String[] reportContextTagValues = { TaggedValueHelper.OUTPUT_FOLDER_TAG, TaggedValueHelper.REP_DBINFO_USER,
            TaggedValueHelper.REP_DBINFO_PASSWORD };

    private static String[] reportContextDBTagValues = { TaggedValueHelper.REP_DBINFO_DBNAME, TaggedValueHelper.REP_DBINFO_HOST,
            TaggedValueHelper.REP_DBINFO_PORT };

    private static void findAndUpdateFieldUseContext(TdReport report, JobContextManager contextManager) {
        for (String tagName : reportContextTagValues) {
            findAndUpdateTaggedValue(report.getTaggedValue(), tagName, contextManager);
        }

        // url = server:port/database, so any one changed will need to update url.
        TaggedValue urlTagValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_URL, report.getTaggedValue());
        for (String tagName : reportContextDBTagValues) {
            Map<String, String> nameMap = findAndUpdateTaggedValueWithNewName(report.getTaggedValue(), tagName, contextManager);
            if (nameMap != null) {
                String oldUrl = urlTagValue.getValue();
                String oldName = nameMap.keySet().iterator().next();
                urlTagValue.setValue(oldUrl.replace(oldName, nameMap.get(oldName)));
            }
        }

        // password
        if (ReportHelper.getPasswordContext(report)) {
            String changedName = ContextHelper.checkRenamedContextParameter(contextManager, ReportHelper.getPassword(report));
            if (StringUtils.isNotBlank(changedName)) {
                ReportHelper.setPassword(changedName, report, true);
            }
        }
        // logoFile
        if (ContextHelper.isContextVar(report.getLogo())) {
            String changedName = ContextHelper.checkRenamedContextParameter(contextManager, report.getLogo());
            if (StringUtils.isNotBlank(changedName)) {
                report.setLogo(changedName);
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
