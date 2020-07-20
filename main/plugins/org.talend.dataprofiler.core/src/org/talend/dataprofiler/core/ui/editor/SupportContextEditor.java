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
package org.talend.dataprofiler.core.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.ContextUtils.ContextItemParamMap;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.link.ContextLinkService;
import org.talend.core.model.context.link.ContextParamLink;
import org.talend.core.model.context.link.ItemContextLink;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.helper.ContextViewHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * the editors support the context view
 * created by msjian on 2014-6-25 Detailled comment
 *
 */
public abstract class SupportContextEditor extends CommonFormEditor {

    public SupportContextEditor() {
        super();
    }

    /**
     * TDQ-18173 msjian: use context link file to check whether the value is the same with repository context, if not,
     * ask you whether to do context propagation.
     * (almost the same logic with ProcessUpdateManager.checkContext(boolean onlySimpleShow))
     * 
     */
    public void checkAndUpdateContext(ModelElement element, String defaultContext) {
        Item currentItem = PropertyHelper.getProperty(element).getItem();
        EList<ContextType> currentContextTypeList = ContextHelper.getAllContextType(currentItem);

        boolean onlySimpleShow = false;

        contextManager = new JobContextManager(currentContextTypeList, defaultContext);

        // TDQ-18631: if current item is not editable, do nothing
        if (!ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(currentItem)) {
            return;
        }

        final String defaultContextName = contextManager.getDefaultContext().getName();
        // record the unsame
        ContextItemParamMap unsameMap = new ContextItemParamMap();
        // built in
        ContextItemParamMap builtInMap = new ContextItemParamMap();
        Set<String> builtInSet = new HashSet<String>();

        Map<Item, Map<String, String>> repositoryRenamedMap = new HashMap<Item, Map<String, String>>();

        ContextItemParamMap deleteParams = new ContextItemParamMap();

        final List<ContextItem> allContextItem = ContextUtils.getAllContextItem();

        Set<String> refContextIds = new HashSet<String>();

        Map<Item, Set<String>> existedParams = new HashMap<Item, Set<String>>();

        // ContextType, List(0) is source, List(1) is context name, List(2) is context group name.
        Map<ContextType, List<String>> needAddedContextMap = new HashMap<ContextType, List<String>>();

        Map<String, Item> tempItemMap = new HashMap<String, Item>();// current real rep context

        ItemContextLink itemContextLink = null;
        try {
            itemContextLink = ContextLinkService.getInstance().loadContextLinkFromJson(currentItem);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        for (IContext context : contextManager.getListContext()) {
            for (IContextParameter param : context.getContextParameterList()) {
                if (!param.isBuiltIn()) {
                    String source = param.getSource();
                    String paramName = param.getName();
                    refContextIds.add(source);
                    ContextParamLink paramLink = null;
                    if (itemContextLink != null) {
                        paramLink = itemContextLink
                                .findContextParamLinkByName(param.getSource(), context.getName(), param.getName());
                    }

                    Item item = tempItemMap.get(source);
                    if (item == null) {
                        item = ContextUtils.findContextItem(allContextItem, source);
                        tempItemMap.put(source, item);
                    }
                    if (item != null) {
                        boolean builtin = true;
                        final ContextType contextType = ContextUtils.getContextTypeByName(item, context.getName());
                        builtin = ContextUtils
                                .compareContextParameter(item, contextType, param, paramLink, repositoryRenamedMap,
                                        existedParams, unsameMap, deleteParams, onlySimpleShow,
                                        StringUtils.equals(context.getName(), defaultContextName));
                        if (!builtin && StringUtils.equals(source, currentItem.getProperty().getId())) {
                            builtin = true;
                        }
                        if (builtin) {
                            // built in
                            if (item != null) {
                                builtInMap.add(item, paramName);
                            } else {
                                builtInSet.add(paramName);
                            }
                        }

                        // for new added context and TDQ-18580 add renamed context
                        List<ContextType> contextTypeList = ((ContextItem) item).getContext();
                        for (ContextType type : contextTypeList) {
                            if (needAddedContextMap.containsKey(type)) {
                                continue;
                            }
                            IContext contextByName =
                                    ContextUtils.getContextByName(contextManager, type.getName(), false);
                            if (contextByName == null) {
                                List list = new ArrayList<>();
                                list.add(source);
                                list.add(type.getName());
                                list.add(getContextItemDisplayName(item));
                                needAddedContextMap.put(type, list);
                            }
                        }
                    }
                }
            }
        }

        // built-in
        if (contextManager instanceof JobContextManager) { // add the lost source context parameters
            Set<String> lostParameters = ((JobContextManager) contextManager).getLostParameters();
            if (lostParameters != null && !lostParameters.isEmpty()) {
                builtInSet.addAll(lostParameters);
                lostParameters.clear();
            }
        }

        // if have context changes
        if (!repositoryRenamedMap.isEmpty() || !unsameMap.isEmpty() || !deleteParams.isEmpty() || !builtInMap.isEmpty()
                || !builtInSet.isEmpty() || !needAddedContextMap.isEmpty()) {

            String updateContextDetailMessage = getContextUpdateDetailListMessage(unsameMap, builtInMap, builtInSet,
                    repositoryRenamedMap, deleteParams, needAddedContextMap);
            // popup to ask user whether update or not.
            if (popupUpdateContextConfirmDialog(updateContextDetailMessage) == Window.OK) {
                // change current context

                // https://jira.talendforge.org/browse/TDQ-18173
                // context group name A B, context variable: filter is "1=1"

                // case3: change context variable name: filter -->filter1 (still repository)
                for (Item item : repositoryRenamedMap.keySet()) {
                    Map<String, String> nameMap = repositoryRenamedMap.get(item);
                    if (nameMap != null && !nameMap.isEmpty()) {
                        for (String newName : nameMap.keySet()) {
                            String oldName = nameMap.get(newName);
                            if (newName.equals(oldName)) {
                                continue;
                            }

                            for (IContext context : contextManager.getListContext()) {
                                for (IContextParameter param : context.getContextParameterList()) {
                                    if (param.isBuiltIn()) { // for buildin, no need to update
                                        continue;
                                    }
                                    if (oldName.equals(param.getName())) {
                                        ContextUtils
                                                .updateParameterFromRepository(item, param, context.getName(), nameMap);
                                    }
                                }
                            }
                        }
                    }
                }
                for (Map<String, String> renamedMap : repositoryRenamedMap.values()) {
                    ContextViewHelper.findAndUpdateFieldUseContext(element, renamedMap);
                }

                // case3: change context variable value: "1=1"-->"2=2" (still repository)
                for (Item item : unsameMap.getContexts()) {
                    Set<String> names = unsameMap.get(item);
                    // set unsameMap key context which name is in names to contextManager.getListContext();
                    if (names != null && !names.isEmpty()) {
                        for (IContext context : contextManager.getListContext()) {
                            for (IContextParameter param : context.getContextParameterList()) {
                                if (param.isBuiltIn()) { // for buildin, no need to update
                                    continue;
                                }
                                for (String contextVariName : names) {
                                    if (contextVariName.equals(param.getName())) {
                                        ContextUtils.updateParameterFromRepository(item, param, context.getName());
                                    }
                                }
                            }
                        }
                    }
                }

                // case7: delete context variable:
                // TDQ-18644: if preference page checked-->delete context variable
                for (Item item : deleteParams.getContexts()) {
                    Set<String> deleteContextNames = deleteParams.get(item);
                    if (deleteContextNames != null && !deleteContextNames.isEmpty()) {
                        for (IContext context : contextManager.getListContext()) {
                            for (String deleteContextName : deleteContextNames) {
                                Iterator<IContextParameter> iterator = context.getContextParameterList().iterator();
                                while (iterator.hasNext()) {
                                    IContextParameter param = iterator.next();
                                    if (deleteContextName.equals(param.getName())) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                    }
                }

                // case11: delete context group--->change to buildIn mode
                if (!builtInSet.isEmpty()) {
                    // do nothing here, just at last need save.
                }

                if (!builtInMap.isEmpty()) {
                    // do nothing here, just at last need save.

                    // case7: delete context variable:
                    // TDQ-18644: if preference page not checked-->change to buildIn mode
                    for (Item item : builtInMap.getContexts()) {
                        Set<String> deleteContextNames = builtInMap.get(item);
                        if (deleteContextNames != null && !deleteContextNames.isEmpty()) {
                            for (IContext context : contextManager.getListContext()) {
                                for (String deleteContextName : deleteContextNames) {
                                    for (IContextParameter param : context.getContextParameterList()) {
                                        if (param.isBuiltIn()) { // for buildin, no need to update
                                            continue;
                                        }
                                        if (deleteContextName.equals(param.getName())) {
                                            param.setSource(IContextParameter.BUILT_IN);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // case 12: add new context in current used context group
                for (ContextType newAddedContext : needAddedContextMap.keySet()) {
                    String newAddedContextSource = needAddedContextMap.get(newAddedContext).get(0);
                    IContext jobContext = ContextViewHelper.convert2IContext(newAddedContext, newAddedContextSource);
                    contextManager.getListContext().add(jobContext);
                }

                // save analysis
                contextManager.saveToEmf(currentContextTypeList);
                ElementWriterFactory.getInstance().create(currentItem).save(currentItem, true);

                // reload current page's model
                getMasterPage().initialize(this);
            }
        }
    }

    /**
     * DOC msjian Comment method "getContextUpdateDetailListMessage".
     * 
     * @param unsameMap
     * @param builtInMap
     * @param builtInSet
     * @param repositoryRenamedMap
     * @param deleteParams
     * @param needAddedContextMap
     */
    private String getContextUpdateDetailListMessage(ContextItemParamMap unsameMap, ContextItemParamMap builtInMap,
            Set<String> builtInSet, Map<Item, Map<String, String>> repositoryRenamedMap,
            ContextItemParamMap deleteParams, Map<ContextType, List<String>> needAddedContextMap) {

        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$

        for (Item item : repositoryRenamedMap.keySet()) {
            Map<String, String> nameMap = repositoryRenamedMap.get(item);
            if (nameMap != null && !nameMap.isEmpty()) {
                for (String newName : nameMap.keySet()) {
                    String oldName = nameMap.get(newName);
                    if (newName.equals(oldName)) {
                        continue;
                    }
                    sb
                            .append("Rename " + oldName + " => " + newName //$NON-NLS-1$ //$NON-NLS-2$
                                    + " detect from repository Context:" //$NON-NLS-1$
                                    + getContextItemDisplayName(item));
                    sb.append(lineSeparator);
                }
            }
        }

        for (Item item : unsameMap.getContexts()) {
            Set<String> names = unsameMap.get(item);
            if (names != null && !names.isEmpty()) {
                for (String contextVariName : names) {
                    sb
                            .append("Update " + contextVariName + " detect from repository Context:" //$NON-NLS-1$ //$NON-NLS-2$
                                    + getContextItemDisplayName(item));
                    sb.append(lineSeparator);
                }
            }
        }

        // case7: delete context variable: --->change to buildIn mode
        for (Item item : deleteParams.getContexts()) {
            Set<String> deleteContextParamNames = deleteParams.get(item);
            if (deleteContextParamNames != null && !deleteContextParamNames.isEmpty()) {
                for (String deleteContextParamName : deleteContextParamNames) {
                    sb
                            .append("Delete " + deleteContextParamName + " detect from repository Context:" //$NON-NLS-1$ //$NON-NLS-2$
                                    + getContextItemDisplayName(item)
                                    + ", will delete it."); //$NON-NLS-1$
                    sb.append(lineSeparator);
                }
            }
        }

        // case11: delete context group--->change to buildIn mode
        // do nothing here, just at last need save.
        for (String paramName : builtInSet) {
            sb.append(paramName + " will change to BuildIn mode."); //$NON-NLS-1$
            sb.append(lineSeparator);
        }

        for (Item item : builtInMap.getContexts()) {
            Set<String> names = builtInMap.get(item);
            if (names != null) {
                for (String paramName : names) {
                    sb
                            .append(paramName + " detect from repository Context:" + getContextItemDisplayName(item) //$NON-NLS-1$
                                    + ", will change to BuildIn mode."); //$NON-NLS-1$
                    sb.append(lineSeparator);
                }
            }
        }

        // case 12: add new context in current used context group
        for (List newAddedContextList : needAddedContextMap.values()) {
            sb
                    .append("Add context " + newAddedContextList.get(1) + " detect from repository Context:" //$NON-NLS-1$ //$NON-NLS-2$
                            + newAddedContextList.get(2));
            sb.append(lineSeparator);
        }

        return sb.toString();
    }

    /**
     * DOC msjian Comment method "getContextItemDisplayName".
     * @param item
     * @return
     */
    private String getContextItemDisplayName(Item item) {
        return ((ContextItem) item).getProperty().getDisplayName();
    }

    /**
     * popup UpdateContextConfirmDialog for user.
     * 
     * @return
     */
    public int popupUpdateContextConfirmDialog(String updateContextDetailMessage) {
        MessageDialog confirmDialog = new MessageDialog(null, "Update Detection", null, //$NON-NLS-1$
                "Follow context changes detected for " + getPartName() + ", do you want to update now?" //$NON-NLS-1$ //$NON-NLS-2$
                        + System.getProperty("line.separator") + System.getProperty("line.separator") //$NON-NLS-1$ //$NON-NLS-2$
                        + updateContextDetailMessage,
                MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
        return confirmDialog.open();
    }
}
