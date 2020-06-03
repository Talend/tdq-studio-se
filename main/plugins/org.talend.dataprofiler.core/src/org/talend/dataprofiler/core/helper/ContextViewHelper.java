// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.update.RepositoryUpdateManager;
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
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by msjian on 2014-6-19 Detailled comment
 *
 */
public final class ContextViewHelper {

    private ContextItem contextItem;

    private JobContextManager contextManager;

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
    public static void updateAllContextInAnalysisAndReport(RepositoryUpdateManager ruManager, ContextItem contextItem,
            boolean isUpdated) {
        List<AnalysisRepNode> anaList = RepositoryNodeHelper.getAnalysisRepNodes(
                RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.ANALYSIS), true, false);

        // if no rename or remove, no need to continue
        Map<ContextItem, Map<String, String>> contextRenamedMap = ruManager.getContextRenamedMap();

        if (CollectionUtils.isNotEmpty(anaList)) {
            for (AnalysisRepNode anaNode : anaList) {
                EList<ContextType> contextList = anaNode.getAnalysis().getContextType();
                if (findAndUpdateContext(contextList, contextItem, ruManager, isUpdated)) {
                    if (!contextRenamedMap.isEmpty()) {
                        findAndUpdateFieldUseContext(anaNode.getAnalysis(), contextRenamedMap.get(contextItem));
                    }
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
                if (findAndUpdateContext(contextList, contextItem, ruManager, isUpdated)) {
                    findAndUpdateFieldUseContext((TdReport) repNode.getReport(),
                            ruManager.getContextRenamedMap().get(contextItem));
                    ElementWriterFactory.getInstance().createReportWriter().save(repNode.getReport());
                    // refresh the report
                    WorkbenchUtils.refreshCurrentReportEditor(repNode.getReport().getName());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean findAndUpdateContext(EList<ContextType> contextList, ContextItem contextItem,
            RepositoryUpdateManager ruManager, boolean isUpdated) {
        // when the ana/rep had no context, or didnot use the current modified context, no need to proceed.
        if (CollectionUtils.isEmpty(contextList) || !isCurrentContextContained(contextList, contextItem)) {
            return false;
        }
        boolean isModified = false;
        if (!ruManager.getRepositoryAddGroupContext().isEmpty()) {
            List<IContext> addGroupContext = ruManager.getRepositoryAddGroupContext().get(contextItem);
            // add new context group
            if (!addGroupContext.isEmpty()) {
                for (IContext addedGroup : addGroupContext) {
                    contextList.add(duplicateContext(addedGroup, contextItem.getProperty().getId()));
                    isModified = true;
                }
            }
        }
        if (isUpdated && !ruManager.getRepositoryRemoveGroupContext().isEmpty()) {
            // delete context group
            List<IContext> removeGroupContext = ruManager.getRepositoryRemoveGroupContext().get(contextItem);
            if (!removeGroupContext.isEmpty()) {
                List<ContextType> removedGrps = new ArrayList<ContextType>();
                for (IContext removedGroup : removeGroupContext) {
                    for (ContextType context : contextList) {
                        if (StringUtils.equals(removedGroup.getName(), context.getName())) {
                            removedGrps.add(context);
                            isModified = true;
                            continue;
                        }
                    }
                }
                contextList.removeAll(removedGrps);
            }
        }

        List<ContextType> renamedGroups = new ArrayList<ContextType>();
        Map<ContextItem, Map<String, String>> contextRenamedMap = ruManager.getContextRenamedMap();
        for (ContextType context : contextList) {
            // rename context groups' name
            // need to check if the same context.
            EList<ContextParameterType> contextParameter = context.getContextParameter();
            if (contextParameter.isEmpty()) {
                continue;// if the context had no parameter, no need to continue
            }
            // only when the current context is the modified one, continue
            boolean hasThisContext = false;
            for (ContextParameterType param : contextParameter) {
                if (StringUtils.equals(param.getRepositoryContextId(), contextItem.getProperty().getId())) {
                    hasThisContext = true;
                    break;
                }
            }
            if (!hasThisContext) {
                continue;
            }

            // rename the context group
            Map<IContext, String> renameGroupContext = ruManager.getRenameContextGroup();
            if (!renameGroupContext.isEmpty() && renameGroupContext.containsValue(context.getName())) {
                for (IContext renamedContext : renameGroupContext.keySet()) {
                    if (StringUtils.equals(context.getName(), renameGroupContext.get(renamedContext))) {
                        if (isUpdated) {
                            context.setName(renamedContext.getName());
                        } else {// select no: add the renamed context group as a new group TDQ-16114
                            renamedGroups.add(duplicateContext(renamedContext, contextItem.getProperty().getId()));
                        }
                        isModified = true;
                        break;
                    }
                }
            }

            // rename context parameters, delete
            // if (contextItem != null && !contextRenamedMap.isEmpty()) {
            EList<ContextType> changedList = contextItem.getContext();
            for (ContextType modifiedContext : changedList) {
                if (StringUtils.equals(context.getName(), modifiedContext.getName())) {
                    updateContextParameters(context, modifiedContext, contextItem.getProperty().getId(),
                            ruManager.getContextRenamedMap().get(contextItem), isUpdated);
                    isModified = true;
                    break;
                }
            }
            // }
        }
        if (!isUpdated) {// add the renamed context group as a new group, when select no TDQ-16114
            contextList.addAll(renamedGroups);
        }

        return isModified;
    }

    private static boolean isCurrentContextContained(EList<ContextType> contextList, ContextItem contextItem) {
        for (ContextType context : contextList) {
            // need to check if the same context.
            EList<ContextParameterType> contextParameter = context.getContextParameter();
            if (contextParameter.isEmpty()) {
                continue;// if the context had no parameter, no need to continue
            }
            for (ContextParameterType param : contextParameter) {
                if (StringUtils.equals(param.getRepositoryContextId(), contextItem.getProperty().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ContextType duplicateContext(IContext renamedContext, String contextId) {
        ContextType newCtx = TalendFileFactory.eINSTANCE.createContextType();
        newCtx.setConfirmationNeeded(renamedContext.isConfirmationNeeded());
        newCtx.setName(renamedContext.getName());

        List<IContextParameter> contextParameters = renamedContext.getContextParameterList();
        EList<ContextParameterType> newContextParameters = newCtx.getContextParameter();
        for (IContextParameter ctxParam : contextParameters) {
            ContextParameterType newCtxParam = TalendFileFactory.eINSTANCE.createContextParameterType();
            newCtxParam.setRepositoryContextId(contextId);
            newCtxParam.setComment(ctxParam.getComment());
            newCtxParam.setName(ctxParam.getName());
            newCtxParam.setPrompt(ctxParam.getPrompt());
            newCtxParam.setType(ctxParam.getType());
            newCtxParam.setValue(ctxParam.getValue());
            // add ContextParameterType into the new ContextType
            newContextParameters.add(newCtxParam);
        }

        return newCtx;
    }

    public static IContext convert2IContext(ContextType contextType, String repositoryContextId) {
        if (contextType == null) {
            return null;
        }
        IContext jobContext = new JobContext(contextType.getName());
        List<ContextParameterType> repoParams = contextType.getContextParameter();
        for (ContextParameterType repoParam : repoParams) {
            IContextParameter jobParam = new JobContextParameter();
            jobParam.setName(repoParam.getName());
            jobParam.setContext(jobContext);
            jobParam.setComment(repoParam.getComment());
            jobParam.setPrompt(repoParam.getPrompt());
            jobParam.setSource(repositoryContextId);
            jobParam.setType(repoParam.getType());
            jobParam.setValue(repoParam.getValue());
            jobParam.setInternalId(repoParam.getInternalId());
            jobContext.getContextParameterList().add(jobParam);
        }
        return jobContext;
    }

    /**
     * check each parameter's repositoryContextId, if parameter's repositoryContextId == contextId,
     *
     * @param context
     * @param modifiedContext
     * @param contextId
     * @param isUpdated
     */
    private static void updateContextParameters(ContextType context, ContextType modifiedContext, String contextId,
            Map<String, String> renameMap, boolean isUpdated) {
        @SuppressWarnings("unchecked")
        EList<ContextParameterType> contextParameters = context.getContextParameter();

        for (ContextParameterType contextParam : contextParameters) {
            if (contextParam.getRepositoryContextId() == null) {
                continue;
            } else if (StringUtils.equals(contextId, contextParam.getRepositoryContextId())) {
                // rename context parameter in some group, <newName, oldName>
                if (renameMap != null && !renameMap.isEmpty()) {
                    if (renameMap.containsValue(contextParam.getName())) {
                        for (String newName : renameMap.keySet()) {
                            if (StringUtils.equals(contextParam.getName(), renameMap.get(newName))) {
                                contextParam.setName(newName);
                                if (!isUpdated) {// TDQ-16114: when user select "no", also change it to built-in
                                    contextParam.setRepositoryContextId(null);
                                }
                                break;
                            }
                        }
                    }
                }

                // update parameter's value, or move the deleted param to built-in
                ContextParameterType modifiedContextParam = ContextUtils.getContextParameterTypeByName(modifiedContext,
                        contextParam.getName());
                if (modifiedContextParam != null) {
                    contextParam.setComment(modifiedContextParam.getComment());
                    contextParam.setPrompt(modifiedContextParam.getPrompt());
                    contextParam.setRawValue(modifiedContextParam.getRawValue());
                    contextParam.setType(modifiedContextParam.getType());
                    contextParam.setValue(modifiedContextParam.getValue());
                } else {// can not find the current param in the modified context, means that the
                        // current param is deleted
                        // if any parameters are deleted from the repository context, change it to
                        // build-in in analysis/report
                    contextParam.setRepositoryContextId(null);
                }
            }
        }
    }

    public static void findAndUpdateFieldUseContext(ModelElement element, Map<String, String> renamedMap) {
        if (element instanceof Analysis) {
            findAndUpdateFieldUseContext((Analysis) element, renamedMap);
        } else if (element instanceof TdReport) {
            findAndUpdateFieldUseContext((TdReport) element, renamedMap);
        }
    }

    public static void findAndUpdateFieldUseContext(Analysis analysis, Map<String, String> renamedMap) {
        findAndUpdateTaggedValue(analysis.getTaggedValue(), TdqAnalysisConnectionPool.NUMBER_OF_CONNECTIONS_PER_ANALYSIS,
                renamedMap);
        // check "data filter" in analysis
        String dataFilter = AnalysisHelper.getStringDataFilter(analysis);
        if (ContextHelper.isContextVar(dataFilter)) {
            String changedContextName = ContextHelper.checkRenamedContextParameter(renamedMap, dataFilter);
            if (StringUtils.isNotBlank(changedContextName)) {
                AnalysisHelper.setStringDataFilter(analysis, changedContextName);
            }
        }
    }

    private static void findAndUpdateTaggedValue(List<TaggedValue> values, String tagName, Map<String, String> renamedMap) {
        TaggedValue tagValue = TaggedValueHelper.getTaggedValue(tagName, values);
        if (ContextHelper.isContextVar(tagValue.getValue())) {
            String changedContextName = ContextHelper.checkRenamedContextParameter(renamedMap, tagValue.getValue());
            if (StringUtils.isNotBlank(changedContextName)) {
                tagValue.setValue(changedContextName);
            }
        }
    }

    private static Map<String, String> findAndUpdateTaggedValueWithNewName(List<TaggedValue> values, String tagName,
            Map<String, String> renamedMap) {
        TaggedValue tagValue = TaggedValueHelper.getTaggedValue(tagName, values);
        if (ContextHelper.isContextVar(tagValue.getValue())) {
            String changedName = ContextHelper.checkRenamedContextParameter(renamedMap, tagValue.getValue());
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

    public static void findAndUpdateFieldUseContext(TdReport report, Map<String, String> renamedMap) {
        for (String tagName : reportContextTagValues) {
            findAndUpdateTaggedValue(report.getTaggedValue(), tagName, renamedMap);
        }

        // url = server:port/database, so any one changed will need to update url.
        TaggedValue urlTagValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.REP_DBINFO_URL, report.getTaggedValue());
        for (String tagName : reportContextDBTagValues) {
            Map<String, String> nameMap = findAndUpdateTaggedValueWithNewName(report.getTaggedValue(), tagName, renamedMap);
            if (nameMap != null) {
                String oldUrl = urlTagValue.getValue();
                String oldName = nameMap.keySet().iterator().next();
                urlTagValue.setValue(oldUrl.replace(oldName, nameMap.get(oldName)));
            }
        }

        // password
        if (ReportHelper.getPasswordContext(report)) {
            String changedName = ContextHelper.checkRenamedContextParameter(renamedMap, ReportHelper.getPassword(report));
            if (StringUtils.isNotBlank(changedName)) {
                ReportHelper.setPassword(changedName, report, true);
            }
        }
        // logoFile
        if (ContextHelper.isContextVar(report.getLogo())) {
            String changedName = ContextHelper.checkRenamedContextParameter(renamedMap, report.getLogo());
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
