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

import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class PatternLanguageRepNode extends DQRepositoryNode {

    private String label;

    private RegularExpression regularExpression = null;

    /**
     * PatternLanguageRepNode constructor.
     * 
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public PatternLanguageRepNode(RepositoryNode parent, ENodeType type, org.talend.core.model.general.Project inWhichProject) {
        super(null, parent, type, inWhichProject);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label == null ? PluginConstant.EMPTY_STRING : this.label;
    }

    public RegularExpression getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(RegularExpression regularExpression) {
        this.regularExpression = regularExpression;
    }
}
