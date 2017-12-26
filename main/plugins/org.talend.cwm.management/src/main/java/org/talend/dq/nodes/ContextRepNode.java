/**
 * 
 */
package org.talend.dq.nodes;

import org.talend.core.model.general.Project;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;


/**
 * @author qiongli
 *
 */
public class ContextRepNode extends DQRepositoryNode {

    /**
     * @param object
     * @param parent
     * @param type
     * @param project
     */
    public ContextRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type, Project project) {
        super(object, parent, type, project);
        // TODO Auto-generated constructor stub
    }

}
