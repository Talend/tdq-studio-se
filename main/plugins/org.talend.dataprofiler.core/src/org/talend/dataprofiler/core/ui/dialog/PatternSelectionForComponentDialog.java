package org.talend.dataprofiler.core.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.nodes.PatternRegexFolderRepNode;
import org.talend.dq.nodes.PatternRegexSubFolderRepNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;


public class PatternSelectionForComponentDialog extends ElementTreeSelectionDialog {

    public PatternSelectionForComponentDialog(Shell parent, IBaseLabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
    }

    public PatternSelectionForComponentDialog(Shell parent) {
        super(parent, new DQRepositoryViewLabelProvider(), new PatternViewContentProvider());
        setInput(AnalysisUtils.getSelectDialogInputData(EResourceConstant.PATTERN_REGEX));

        setTitle(DefaultMessagesImpl.getString("PatternSelectionForComponentDialog.title")); //$NON-NLS-1$
        setMessage(DefaultMessagesImpl.getString("PatternSelectionForComponentDialog.rule")); //$NON-NLS-1$

    }


    private static class PatternViewContentProvider extends ResourceViewContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            List<RepositoryNode> analyzeNode = new ArrayList<RepositoryNode>();
            if (!(parentElement instanceof PatternRegexFolderRepNode)) {
                return super.getChildren(parentElement);
            }
            PatternRegexFolderRepNode node = (PatternRegexFolderRepNode) parentElement;
            Object[] children = super.getChildren(node);
            // Only display pattern which has Java or default expression.
            for (Object object : children) {
                if (object instanceof PatternRegexSubFolderRepNode) {
                    analyzeNode.add((RepositoryNode) object);
                } else if (object instanceof PatternRepNode) {
                    PatternRepNode patternNode = (PatternRepNode) object;
                    if (includeJavaOrDefaultExpression(patternNode)) {
                        analyzeNode.add(patternNode);
                    }
                }
            }
            return analyzeNode.toArray();
        }


        /**
         * 
         * Judge if the patternNode include java or default expression.
         * 
         * @param patternNode
         * @return
         */
        private boolean includeJavaOrDefaultExpression(PatternRepNode patternNode) {
            boolean result = false;
            if (patternNode == null) {
                return result;
            }
            EList<PatternComponent> sqlGenericExpression = patternNode.getPattern().getComponents();
            for (PatternComponent sqlExp : sqlGenericExpression) {
                if (sqlExp instanceof RegularExpression) {
                    String language = ((RegularExpression) sqlExp).getExpression().getLanguage();
                    if (DbmsLanguage.SQL.equalsIgnoreCase(language)
                            || PatternLanguageType.JAVA.getLiteral().equals(language)) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }


        @Override
        public Object[] getElements(Object object) {
            return this.getChildren(object);
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof PatternRepNode) {
                return false;
            }
            return true;
        }
    }
}
