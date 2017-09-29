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
package org.talend.dataprofiler.core.ui.editor;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC mzhao DQ editor link helper.
 */
public class DQEditorLinkHelper implements ILinkHelper {

    public IStructuredSelection findSelection(IEditorInput editorInput) {
        // if RepositoryView is not opened we will return null
        // MOD msjian TDQ-8416: we don't need to bring the DQ view to top here
        if (CorePlugin.getDefault().getRepositoryViewWithoutBringToTop() == null) {
            return null;
        }
        IRepositoryNode node = null;
        if (editorInput instanceof AbstractItemEditorInput) {
            Item item = ((AbstractItemEditorInput) editorInput).getItem();
            if (item instanceof TDQAnalysisItem) {
                node = RepositoryNodeHelper.recursiveFind(((TDQAnalysisItem) item).getAnalysis());
            } else if (item instanceof TDQReportItem) {
                node = RepositoryNodeHelper.recursiveFind(((TDQReportItem) item).getReport());
            } else if (item instanceof ConnectionItem) {
                node = RepositoryNodeHelper.recursiveFind(((ConnectionItem) item).getConnection());
            } else if (item instanceof TDQPatternItem) {
                node = RepositoryNodeHelper.recursiveFind(((TDQPatternItem) item).getPattern());
            } else if (item instanceof TDQIndicatorDefinitionItem) {
                node = RepositoryNodeHelper.recursiveFind(((TDQIndicatorDefinitionItem) item).getIndicatorDefinition());
            } else if (item instanceof TDQBusinessRuleItem) {
                // MOD klliu bug TDQ-4517 2012-01-16
                node = RepositoryNodeHelper.recursiveFind(((TDQBusinessRuleItem) item).getDqrule());
            } else if (item instanceof TDQMatchRuleItem) {
                node = RepositoryNodeHelper.recursiveFind(((TDQMatchRuleItem) item).getMatchRule());
            }
            if (node != null) {
                return new StructuredSelection(node);
            }
        } else if (editorInput instanceof FileEditorInput) {
            // ADD msjian TDQ-4209 2012-02-03: make the JRXML editor synchronized with the DQ repository view
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = ResourceUtil.getFile(fileEditorInput);
            if (file != null) {
                node = RepositoryNodeHelper.recursiveFindFile(file);
                if (node != null) {
                    return new StructuredSelection(node);
                }
            }
            // TDQ-4209~
        }
        return StructuredSelection.EMPTY;
    }

    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        IRepositoryNode repNode = (IRepositoryNode) aSelection.getFirstElement();
        OpenItemEditorAction openEditorAction = new OpenItemEditorAction(new IRepositoryNode[] { repNode });
        // MOD msjian TDQ-4209 2012-2-7 : modify to IEditorInput type
        try {
            IEditorInput absEditorInput = openEditorAction.computeEditorInput(repNode, false);
            if (absEditorInput != null) {
                aPage.bringToTop(aPage.findEditor(absEditorInput));
            }
        } catch (BusinessException e) {
            ExceptionHandler.process(e, Level.FATAL);
        }
        // TDQ-4209~
    }

}
