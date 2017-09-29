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
package org.talend.dq.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class PatternRepNode extends DQRepositoryNode {

    /**
     * PatternRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public PatternRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public Pattern getPattern() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return ((TDQPatternItem) property.getItem()).getPattern();
        }
        return null;
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        List<IRepositoryNode> languageElement = new ArrayList<IRepositoryNode>();
        IRepositoryViewObject object = this.getObject();
        TDQPatternItem patternItem = (TDQPatternItem) object.getProperty().getItem();
        Pattern pattern = patternItem.getPattern();
        EList<PatternComponent> components = pattern.getComponents();
        for (PatternComponent component : components) {
            RegularExpression re = (RegularExpression) component;
            Expression expression = re.getExpression();
            String language = expression.getLanguage();
            PatternLanguageRepNode plrn = new PatternLanguageRepNode(this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
            plrn.setRegularExpression(re);
            plrn.setId(language);
            plrn.setLabel(language);
            languageElement.add(plrn);
        }
        // MOD gdbu 2011-7-11 bug : 22204
        return filterResultsIfAny(languageElement);
        // ~22204
    }

    @Override
    public String getLabel() {
        if (this.getPattern() != null && this.getPattern().getName() != null) {
            return this.getPattern().getName();
        }
        return super.getLabel();
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel() + " " + getObject().getVersion(); //$NON-NLS-1$
    }
}
