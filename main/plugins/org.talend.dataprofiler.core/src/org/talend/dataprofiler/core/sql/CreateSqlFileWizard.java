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
package org.talend.dataprofiler.core.sql;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dq.analysis.parameters.SqlFileParameter;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class CreateSqlFileWizard extends Wizard {

    protected static Logger log = Logger.getLogger(CreateSqlFileWizard.class);

    private CreateSqlFileWizardPage mPage;

    private File sqlFile;

    private SqlFileParameter parameter;

    private TDQSourceFileItem sourceFileItem;


    /**
     * DOC qzhang CreateSqlFileWizard constructor comment.
     * 
     * @param folder
     */
    public CreateSqlFileWizard(SqlFileParameter parameter) {
        this.parameter = parameter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mPage = new CreateSqlFileWizardPage(parameter);
        mPage.setTitle(DefaultMessagesImpl.getString("CreateSqlFileWizard.newSQLFile")); //$NON-NLS-1$
        mPage.setDescription(DefaultMessagesImpl.getString("CreateSqlFileWizard.define")); //$NON-NLS-1$
        mPage.setPageComplete(false);
        addPage(mPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        IPath path = WorkspaceUtils.fileToIFile(new File(parameter.getFolderProvider().getFolder().getPath())).getFullPath()
                .makeRelativeTo(ResourceManager.getSourceFileFolder().getFullPath());

        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(PluginConstant.EMPTY_STRING);
        property.setLabel(WorkspaceUtils.normalize(parameter.getFileName()));
        property.setDisplayName(parameter.getFileName());

        sourceFileItem = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE
                .createTDQSourceFileItem();
        sourceFileItem.setProperty(property);
        sourceFileItem.setName(parameter.getFileName());
        sourceFileItem.setExtension(PluginConstant.SQL_STRING);
        sourceFileItem.setFilename(property.getLabel() + "_" + property.getVersion() + PluginConstant.DOT_STRING
                + PluginConstant.SQL_STRING);
        // set empty content
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContent(PluginConstant.EMPTY_STRING.getBytes());
        sourceFileItem.setContent(byteArray);

        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            property.setId(repositoryFactory.getNextId());
            repositoryFactory.create(sourceFileItem, path);
            this.sqlFile = getCreatedSqlFile(sourceFileItem, path);
            return true;
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "getCreatedSqlFile".
     * 
     * @param sourceFileItem
     * @param path
     * @return
     */
    private File getCreatedSqlFile(TDQSourceFileItem sourceFileItem, IPath path) {
        return ResourceManager.getSourceFileFolder().getRawLocation().append(path)
                .append(WorkspaceUtils.normalize(sourceFileItem.getName()) + "_0.1." + sourceFileItem.getExtension()).toFile(); //$NON-NLS-1$
    }

    /**
     * Getter for sqlFile.
     * 
     * @return the sqlFile
     */
    public File getSqlFile() {
        return this.sqlFile;
    }

    // Added 20130603 yyin TDQ-7143
    public TDQSourceFileItem getSourceFileItem() {
        return this.sourceFileItem;
    }

}
