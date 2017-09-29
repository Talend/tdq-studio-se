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
package org.talend.dq.writer.impl;

import org.apache.log4j.Logger;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.writer.AElementPersistance;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class PatternWriter extends AElementPersistance {

    static Logger log = Logger.getLogger(PatternWriter.class);

    /**
     * DOC bZhou PatternWriter constructor comment.
     */
    PatternWriter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#addDependencies(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected void addDependencies(ModelElement element) {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#getFileExtension()
     */
    @Override
    protected String getFileExtension() {
        return FactoriesUtil.PATTERN;
    }

    @Override
    public ReturnCode save(final Item item, final boolean careDependency) {
        ReturnCode rc = new ReturnCode();
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("save Analysis item") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                TDQPatternItem patternItem = (TDQPatternItem) item;
                Pattern pattern = patternItem.getPattern();
                // MOD yyi 2012-02-07 TDQ-4621:Update dependencies(connection) when careDependency is true.
                if (careDependency) {
                    saveWithDependencies(patternItem, pattern);
                } else {
                    saveWithoutDependencies(patternItem, pattern);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        try {
            repositoryWorkUnit.throwPersistenceExceptionIfAny();
        } catch (PersistenceException e) {
            log.error(e, e);
            rc.setOk(Boolean.FALSE);
            rc.setMessage(e.getMessage());
        }
        return rc;
    }

    @Override
    protected void notifyResourceChanges() {
        ProxyRepositoryManager.getInstance().save();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.writer.AElementPersistance#removeDependencies(org.talend.core.model.properties.Item)
     */
    @Override
    protected ReturnCode removeDependencies(Item item) {
        return new ReturnCode(true);
    }

}
