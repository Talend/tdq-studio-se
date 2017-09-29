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
package org.talend.dataprofiler.core.ui.wizard.database;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizard;
import org.talend.dq.analysis.parameters.ConnectionParameter;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class MetaDataFilterWizard extends AbstractWizard {

    private Logger log = Logger.getLogger(MetaDataFilterWizard.class);

    private MetaDataFilterWizardPage metaDataFilterWizardPage;

    private DatabaseConnectionItem databaseConnectionItem;

    public MetaDataFilterWizard(DatabaseConnectionItem dbConnItem) {
        super();
        this.databaseConnectionItem = dbConnItem;
    }

    @Override
    protected String getEditorName() {
        return DefaultMessagesImpl.getString("MetaDataFilterWizard.ColumnFilter"); //$NON-NLS-1$
    }

    @Override
    public ConnectionParameter getParameter() {
        return null;
    }

    @Override
    protected ResourceFileMap getResourceFileMap() {
        return null;
    }

    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement cwmElement) {
        return null;
    }

    public ModelElement initCWMResourceBuilder() {
        return this.databaseConnectionItem.getConnection();
    }

    @Override
    public boolean performFinish() {
        String metaDataFilter = this.metaDataFilterWizardPage.getMetadataFilterText().getText();
        return isSavePackageFilter(metaDataFilter);

    }

    private boolean isSavePackageFilter(String metaDataFilter) {
        if (!this.getOldPackageFilter().equals(metaDataFilter)) {
            ConnectionHelper.setPackageFilter(this.databaseConnectionItem.getConnection(), metaDataFilter);
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            try {
                ProxyRepositoryFactory.getInstance().save(currentProject, databaseConnectionItem);
            } catch (PersistenceException e) {
                log.error(e);
                return false;
            }
        }
        return true;

    }

    /**
     * This method is used for getting the filter of package.
     * 
     * @return
     */
    public String getOldPackageFilter() {

        return ConnectionHelper.getPackageFilter(this.getDatabaseConnectionItem().getConnection());
    }

    @Override
    public void addPages() {
        initPackageFilterDescription();
        addPage(metaDataFilterWizardPage);
    }

    private void initPackageFilterDescription() {
        setWindowTitle(DefaultMessagesImpl.getString("MetaDataFilterWizard.PackageFilter")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageLib.getImageDescriptor(ImageLib.RUN_IMAGE));
        this.metaDataFilterWizardPage = new MetaDataFilterWizardPage(this);
        this.metaDataFilterWizardPage.setTitle(DefaultMessagesImpl.getString("MetaDataFilterWizard.PackageFilter")); //$NON-NLS-1$
        this.metaDataFilterWizardPage.setDescription(DefaultMessagesImpl.getString("MetaDataFilterWizard.PackageFilterDesc")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#openEditor(org.talend.repository.model.IRepositoryNode
     * )
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        // do nothing here until now
    }

    public DatabaseConnectionItem getDatabaseConnectionItem() {
        return this.databaseConnectionItem;
    }
}
