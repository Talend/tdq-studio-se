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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.impl.RegularExpressionImpl;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class UpdateStartWithBlankPatternTask extends AbstractWorksapceUpdateTask {

    private static final String PATTERN_NAME = "Starts with blank"; //$NON-NLS-1$

    private static final String JAVA_REGULAR_EXPRESSION = "'^\\s+.*$'";//$NON-NLS-1$

    private static final String JAVA_LANGUAGE = "Java";//$NON-NLS-1$

    public Date getOrder() {
        return createDate(2011, 7, 28);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Jsdoc) update the regular express for java
     */
    @Override
    protected boolean doExecute() throws Exception {
        IPath realtivePath = ResourceManager.getPatternRegexFolder().getFullPath().append("/text")//$NON-NLS-1$
                .makeRelativeTo(ResourceManager.getRootProject().getFullPath());
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = ProxyRepositoryFactory.getInstance()
                .getTdqRepositoryViewObjects(ERepositoryObjectType.TDQ_PATTERN_REGEX, realtivePath.toString());
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            Item item = viewObject.getProperty().getItem();
            Pattern pattern = null;
            if (item != null && item instanceof TDQPatternItem) {
                TDQPatternItem patternItem = (TDQPatternItem) item;
                pattern = patternItem.getPattern();
                boolean isFound = false;
                if (PATTERN_NAME.equals(pattern.getName())) {
                    EList<PatternComponent> components = pattern.getComponents();
                    for (PatternComponent pComponet : components) {
                        RegularExpressionImpl regularExpress = (RegularExpressionImpl) pComponet;
                        if (JAVA_LANGUAGE.equals(regularExpress.getExpression().getLanguage())) {
                            regularExpress.getExpression().setBody(JAVA_REGULAR_EXPRESSION);
                            patternItem.setPattern(pattern);
                            // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
                            ElementWriterFactory.getInstance().createPatternWriter().save(patternItem, true);
                            isFound = true;
                            break;
                        }
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }
        return true;
    }
}
